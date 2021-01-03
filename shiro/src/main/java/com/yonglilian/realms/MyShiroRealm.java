package com.yonglilian.realms;


import com.yonglilian.dao.mapper.LoginMapper;
import com.yonglilian.model.BPIP_MENU;
import com.yonglilian.service.BpipMenuService;
import com.yonglilian.service.UIService;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import zr.zrpower.common.util.MD5;
import zr.zrpower.model.BPIP_USER;

import java.util.*;
import java.util.stream.Collectors;
/**
 * realm实现类,用于实现具体的验证和授权方法
 * @author Bean
 *
 */
public class MyShiroRealm extends AuthorizingRealm {
	
	@Autowired
	private LoginMapper loginMapper;
    @Autowired
	private BpipMenuService sysMenuService;
	
	@Autowired
	private UIService uiService;

	/**
	 * 方面用于加密 参数：AuthenticationToken是从表单穿过来封装好的对象
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token ) throws AuthenticationException {
		System.out.println("doGetAuthenticationInfo:" + token);

		String username = ( String )token.getPrincipal();
		String password = new String( ( char[] )token.getCredentials() );

		// 查询用户信息
		Map<String,Object> map= new HashMap<>();
		map.put("LOGINID", username);
		BPIP_USER user = loginMapper.login(map);

		// 账号不存在
		if( user == null ) {
			throw new UnknownAccountException( "账号或密码不正确" );
		}

		// 密码错误
		if (!user.getLOGINPW().toString().equals(MD5.toMD5(password))&& !password.equals("autologinpassword")) {
			throw new IncorrectCredentialsException( "账号或密码不正确" );
		}

		// 账号锁定
		if( user.getUSERSTATE().equals("1")) {
			throw new LockedAccountException( "账号已被锁定,请联系管理员" );
		}
		
		//加密盐值
		ByteSource salt = ByteSource.Util.bytes(username);
		String md5Pwd = new Md5Hash(password, username).toHex();
//		System.out.println("designer   "+new Md5Hash("designer", "designer").toHex());
		
		List<String> permsList = null;

		// 系统管理员，拥有最高权限
		if( user.getLOGINID().equals("designer") ) {
			List<BPIP_MENU> menuList;
			try {
				menuList = sysMenuService.queryList( new HashMap<>() );
				permsList = menuList.stream().parallel().map( BPIP_MENU::getPERMS ).collect( Collectors.toList() );
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			try {
				List<BPIP_MENU> menuList = uiService.loadUserMenu(user.getUSERID());
				permsList = menuList.stream().parallel().map( BPIP_MENU::getPERMS ).collect( Collectors.toList() );
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		// 用户权限列表
		Set<String> permsSet = permsList.stream().parallel().filter( StringUtils::isNotBlank ).map( String::trim ).map( s -> s.split( "," ) ).map( Arrays::asList ).flatMap( Collection::stream ).collect( Collectors.toSet() );
		user.setPermsSet(permsSet);

		return new SimpleAuthenticationInfo( user, md5Pwd,salt,getName() );
	}

	/**
	 * 授权(验证权限时调用)
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {

		System.out.println("MyShiroRealm的doGetAuthorizationInfo授权方法执行");
		BPIP_USER user = ( BPIP_USER )principals.getPrimaryPrincipal();
		Set<String> permsSet =user.getPermsSet();
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
		info.setStringPermissions( permsSet );
		return info;
	}

}