package zr.zrpower.dao.mapper;


import tk.mybatis.spring.annotation.MapperScan;
import zr.zrpower.dao.BaseDao;
import zr.zrpower.model.BPIP_ROLE;
import zr.zrpower.model.BPIP_ROLE_RERMISSISSON;

import java.util.List;
import java.util.Map;

/**
 * 数据操作dao
 * @author nfzr
 *
 */
@MapperScan({"zr.zrpower.dao.mapper"})
public interface BpipRoleMapper extends BaseDao<BPIP_ROLE>{
	/**
	 * 保存角色
	 * @param model
	 * @return
	 */
	int saveBpipRole(BPIP_ROLE model);
	
	/**
	 * 查询数据库的表
	 * @param map
	 * @return
	 */
	List<BPIP_ROLE_RERMISSISSON> getRoleMenusList(Map<String,Object> map);
	
	/**
	 * 查询详情
	 * @param map 
	 * @return
	 */
	BPIP_ROLE queryObject(Map<String,Object> map);
	
	/**
	 * 删除菜单对应的角色
	 * @param roleid
	 */
	void deleteRoleMenu(int roleid);
	
	/**
	 * 删除角色用户授权
	 * @param roleid
	 */
	void deleteRoleUser(int roleid);
	
	/**
	 * 插入角色对应的菜单
	 * @param model
	 */
	void saveRoleMenu(BPIP_ROLE_RERMISSISSON model);
	
	/**
	 * 批量插入角色=用户绑定数据
	 * @param model
	 */
	void saveBatch(BPIP_ROLE model);
}