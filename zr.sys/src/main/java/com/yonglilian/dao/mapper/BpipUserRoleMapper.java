package com.yonglilian.dao.mapper;

import com.yonglilian.model.BPIP_USER_ROLE;
import tk.mybatis.spring.annotation.MapperScan;
import zr.zrpower.dao.BaseDao;

/**
 * 数据操作dao
 * @author nfzr
 *
 */
@MapperScan({"com.yonglilian.dao.mapper"})
public interface BpipUserRoleMapper extends BaseDao<BPIP_USER_ROLE>{
	
	/**
	 * 通过用户id删除角色绑定
	 * @param userId
	 */
	void delUserRole(String userId);
}