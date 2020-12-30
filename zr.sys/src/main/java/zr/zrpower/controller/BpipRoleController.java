package zr.zrpower.controller;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import zr.zrpower.common.util.R;
import zr.zrpower.intercept.annotation.ZrSafety;
import zr.zrpower.model.BPIP_ROLE;
import zr.zrpower.model.BPIP_ROLE_RERMISSISSON;
import zr.zrpower.model.BPIP_USER_ROLE;
import zr.zrpower.service.BpipRoleService;
import zr.zrpower.service.BpipUserRoleService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 角色操作
 * 
 * @author nfzr
 *
 */
@ZrSafety
@RestController
@RequestMapping("/bpiprole")
public class BpipRoleController extends BaseController {

	private static final long serialVersionUID = 1L;

	@Autowired
	private BpipRoleService bpipRoleService;
	@Autowired
	private BpipUserRoleService bpipUserRoleService;

	/**
	 * 角色对应的权限
	 * 
	 * @param map
	 * @return
	 */
	@RequestMapping("/menulist")
	@RequiresPermissions("bpiprole:menulist")
	public R tables(@RequestParam Map<String, Object> map)  throws Exception{
		List<BPIP_ROLE_RERMISSISSON> list = bpipRoleService.getRoleMenusList(map);
		return R.ok().put("list", list);
	}

	/**
	 * 列表
	 * 
	 * @param map
	 * @return
	 */
	@RequestMapping("/list")
	@RequiresPermissions("bpiprole:list")
	public R list(@RequestParam Map<String, Object> map)  throws Exception{
		List<BPIP_ROLE> list = bpipRoleService.queryList(map);
		return R.ok().put("list", list);
	}

	/**
	 * 详情
	 * 
	 * @param map
	 * @return
	 */
	@RequestMapping("/info/{roleid}")
	@RequiresPermissions("bpiprole:info")
	public R info(@PathVariable("roleid") int roleid)  throws Exception{
		BPIP_ROLE model = bpipRoleService.queryObject(roleid);
		Map<String,Object> map = new HashMap<>();
		map.put("roleid", roleid);
		List<BPIP_ROLE_RERMISSISSON> roleMenusList =bpipRoleService.getRoleMenusList(map);
		if(roleMenusList!=null&&roleMenusList.size()>0) {
			int len=roleMenusList.size();
			List<String> menuList=new ArrayList<>();
			for(int i = 0;i<len;i++) {
				menuList.add(roleMenusList.get(i).getMENUNO());
			}
			model.setRoleMenuList(menuList);
		}
		return R.ok().put("form", model);
	}

	/**
	 * 保存
	 * 
	 * @param BPIP_ROLE
	 * @return
	 */
	@RequestMapping("/save")
	@RequiresPermissions("bpiprole:save")
	public R save(@RequestBody BPIP_ROLE model)  throws Exception{
		if(bpipRoleService.save(model)) {
			return R.ok();
		}
		return R.error(500, "新增角色失败，请联系管理员");			
	}

	/**
	 * 修改
	 * 
	 * @param BPIP_ROLE
	 * @return
	 */
	@RequestMapping("/update")
	@RequiresPermissions("bpiprole:update")
	public R update(@RequestBody BPIP_ROLE model)  throws Exception{
		if(bpipRoleService.update(model)) {
			return R.ok();
		}
		return R.error(500, "修改角色失败，请联系管理员");	
	}

	/**
	 * 删除
	 * 
	 * @param roleIds
	 * @return
	 */
	@RequestMapping("/delete")
	@RequiresPermissions("bpiprole:delete")
	public R delete(@RequestBody int[] roleIds)  throws Exception{
		if(bpipRoleService.delete(roleIds[0])) {
			return R.ok();
		}
		return R.error(500, "删除角色失败，请联系管理员");	
	}
	
	
	
	/************************************/
    /**********以下为角色用户业务操作*******/
	/***********************************/
	/**
	 * 角色用户管理：返回角色用户数据
	 * 
	 * @param map
	 * @return
	 */
	@RequestMapping("/roleUserList")
	public R roleUserList(@RequestParam Map<String, Object> map)  throws Exception{
		List<BPIP_USER_ROLE> list = bpipUserRoleService.queryList(map);
		return R.ok().put("list", list);
	}
	/**
	 * 删除角色id绑定的用户数据
	 * 
	 * @param roleId
	 * @return
	 */
	@RequestMapping("/roleUserDelete/{roleId}")
	public R roleUserDelete(@PathVariable("roleId") int roleId)  throws Exception{
		bpipUserRoleService.delete(roleId);
		return R.ok();
	}
	/**
	 * 保存角色id选中的用户数据
	 * 
	 * @param BPIP_ROLE
	 * @return
	 */
	@RequestMapping("/roleUserSave")
	public R roleUserSave(@RequestBody BPIP_ROLE model)  throws Exception{
		if(model.getRoleUserList()!=null&&model.getRoleUserList().size()>0) {
			System.out.println(model.getRoleUserList().get(0).getUSERID());
			bpipRoleService.saveBatch(model);
			return R.ok();
		}
		return R.error(500, "保存角色用户异常，请联系管理员");
	}
	
	/**
	 * 保存用户添加的角色
	 * 
	 * @param BPIP_USER_ROLE
	 * @return
	 */
	@RequestMapping("/saveUserRole")
	public R saveUserRole(@RequestBody BPIP_USER_ROLE model)  throws Exception{
		if(model.getRoleIdList()!=null&&model.getRoleIdList().size()>0) {
			//先删除历史数据
			bpipUserRoleService.delUserRole(model.getUSERID());
			for(int roleId:model.getRoleIdList()) {
				model.setROLEID(roleId);
				bpipUserRoleService.save(model);
			}
			return R.ok();
		}
		return R.error(500, "新增角色失败，请联系管理员");			
	}

}