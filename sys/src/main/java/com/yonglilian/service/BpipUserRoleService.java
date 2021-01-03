package com.yonglilian.service;

import com.yonglilian.model.BPIP_USER_ROLE;

import java.util.List;
import java.util.Map;

/**
 * 
 * 角色用户管理
 * @author nfzr
 *
 */
public interface BpipUserRoleService {

	/**
	 * 获取条件下的list
	 * @param map
	 * @return
	 */
	List<BPIP_USER_ROLE> queryList(Map<String, Object> map) throws Exception;

	/**
	 * 保存数据
	 * @param model
	 */
	void save(BPIP_USER_ROLE model) throws Exception;

	/**
	 * 删除数据
	 * @param roleId 
	 * @return
	 */
	void delete(int roleId) throws Exception;
	
	/**
	 * 删除数据
	 * @param userId 
	 * @return
	 */
	void delUserRole(String userId) throws Exception;
	
}
