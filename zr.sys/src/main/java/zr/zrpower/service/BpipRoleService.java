package zr.zrpower.service;

import zr.zrpower.model.BPIP_ROLE;
import zr.zrpower.model.BPIP_ROLE_RERMISSISSON;

import java.util.List;
import java.util.Map;

/**
 * 
 * 角色管理
 * @author nfzr
 *
 */
public interface BpipRoleService {
	
	/**
	 * 获取数据库表
	 * @param map
	 * @return
	 */
	List<BPIP_ROLE_RERMISSISSON> getRoleMenusList(Map<String,Object> map) throws Exception;
	
	/**
	 * 获得详情
	 * @param table
	 * @param code
	 * @return
	 */
	BPIP_ROLE queryObject(int roleid) throws Exception;

	/**
	 * 获取条件下的list
	 * @param map
	 * @return
	 */
	List<BPIP_ROLE> queryList(Map<String, Object> map) throws Exception;

	/**
	 * 保存数据
	 * @param model
	 */
	boolean save(BPIP_ROLE model) throws Exception;

	/**
	 * 修改数据
	 * @param model
	 */
	boolean update(BPIP_ROLE model) throws Exception;

	/**
	 * 删除数据
	 * @param map 
	 * @return
	 */
	boolean delete(int roleId) throws Exception;
	
	/**
	 * 删除多条数据
	 * @param codes
	 * @return
	 */
	boolean deleteBatch(String[] codes) throws Exception;
	
	/**
	 * 批量插入角色=用户绑定数据
	 * @param model
	 */
	void saveBatch(BPIP_ROLE model) throws Exception;

}
