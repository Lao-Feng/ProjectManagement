package com.yonglilian.dao.mapper;


import com.yonglilian.dao.BaseDao;
import com.yonglilian.model.BPIP_USER;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * 用户dao
 * @author nfzr
 *
 */
@MapperScan({"com.yonglilian.dao.mapper"})
public interface BpipUserMapper extends BaseDao<BPIP_USER>{
	
	/**
	 * 通用户loginId判断用户是否存在
	 * @param loginId
	 * @return
	 */
	int queryUserId(String loginId);
	
	/**
	 * 保存成功，返回userId
	 * @param model
	 * @return
	 */
	void saveBack(BPIP_USER model);
	
	
	/**
	 * 查询最大编号
	 * @param unitId
	 * @return
	 */
	String getMaxNo(String unitId);  
	
	/**
	 * 删除用户关联系统数据
	 * @param userId
	 */
	void delUserPhoto(String userId);
	void delUserRemove(String userId);
	void delUserIdea(String userId);
	void delUserRole(String userId);
	
	
}