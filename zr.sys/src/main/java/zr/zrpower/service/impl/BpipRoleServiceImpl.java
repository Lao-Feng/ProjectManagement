package zr.zrpower.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zr.zrpower.common.db.DBEngine;
import zr.zrpower.common.util.SysPreperty;
import zr.zrpower.dao.mapper.BpipRoleMapper;
import zr.zrpower.model.BPIP_ROLE;
import zr.zrpower.model.BPIP_ROLE_RERMISSISSON;
import zr.zrpower.service.BpipRoleService;

import java.util.List;
import java.util.Map;

/**
 * 接口实现
 * @author nfzr
 *
 */
@Service
public class BpipRoleServiceImpl implements BpipRoleService {
	/** The UserServiceImpl Logger. */
	@SuppressWarnings("unused")
	private static final Logger LOGGER = LoggerFactory.getLogger(BpipRoleServiceImpl.class);
	String dataBaseType = SysPreperty.getProperty().DataBaseType;
	/** 数据库引擎. */
	private DBEngine dbEngine;
	/** 用户操作数据层. */
	@Autowired
	private BpipRoleMapper bpipRoleMapper;

	/**
	 * 构造方法
	 */
	public BpipRoleServiceImpl()  throws Exception{
		dbEngine = new DBEngine(SysPreperty.getProperty().MainDataSource, SysPreperty.getProperty().IsConvert);
		dbEngine.initialize();
	}

	@Override
	public List<BPIP_ROLE_RERMISSISSON> getRoleMenusList(Map<String, Object> map)  throws Exception{
		// TODO Auto-generated method stub
		return bpipRoleMapper.getRoleMenusList(map);
	}

	@Override
	public BPIP_ROLE queryObject(int roleid)  throws Exception{
		// TODO Auto-generated method stub
		return bpipRoleMapper.queryObject(roleid);
	}

	@Override
	public List<BPIP_ROLE> queryList(Map<String, Object> map)  throws Exception{
		// TODO Auto-generated method stub
		return bpipRoleMapper.queryList(map);
	}

	@Override
	public boolean save(BPIP_ROLE model)  throws Exception{
		// TODO Auto-generated method stub
		boolean reback= false;
		if(bpipRoleMapper.saveBpipRole(model)>=1) {
			//开始保存角色对应的权限菜单
			if(model.getRoleMenuList()!=null&&model.getRoleMenuList().size()>0) {
				BPIP_ROLE_RERMISSISSON roleRer = null;
				for(String menuNo : model.getRoleMenuList()) {
					roleRer = new BPIP_ROLE_RERMISSISSON();
					roleRer.setROLEID(model.getROLEID());
					roleRer.setMENUNO(menuNo);
					bpipRoleMapper.saveRoleMenu(roleRer);
					roleRer = null;
				}
			}
			reback= true;
		}
		return reback;
	}

	@Override
	public boolean update(BPIP_ROLE model)  throws Exception{
		// TODO Auto-generated method stub
		boolean reback= false;
		if(bpipRoleMapper.update(model)==1) {
			//首先删除以前的数据
			bpipRoleMapper.deleteRoleMenu(model.getROLEID());
			//再次保存角色对应的权限菜单
			if(model.getRoleMenuList()!=null&&model.getRoleMenuList().size()>0) {
				BPIP_ROLE_RERMISSISSON roleRer = null;
				for(String menuNo : model.getRoleMenuList()) {
					roleRer = new BPIP_ROLE_RERMISSISSON();
					roleRer.setROLEID(model.getROLEID());
					roleRer.setMENUNO(menuNo);
					bpipRoleMapper.saveRoleMenu(roleRer);
					roleRer = null;
				}
			}
			reback= true;
			reback= true;
		}
		return reback;
	}

	@Override
	public boolean delete(int roleId)  throws Exception{
		// TODO Auto-generated method stub
		bpipRoleMapper.deleteRoleUser(roleId);//删除角色权限对应的用户授权数据
		bpipRoleMapper.deleteRoleMenu(roleId);//删除角色对应的菜单权限数据
		bpipRoleMapper.delete(roleId);//删除角色
		return true;
	}

	@Override
	public boolean deleteBatch(String[] codes)  throws Exception{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void saveBatch(BPIP_ROLE model) throws Exception {
		// TODO Auto-generated method stub
		bpipRoleMapper.saveBatch(model);
	}

	
}