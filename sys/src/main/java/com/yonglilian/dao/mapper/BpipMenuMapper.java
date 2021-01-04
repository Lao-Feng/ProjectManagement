package com.yonglilian.dao.mapper;


import com.yonglilian.dao.BaseDao;
import com.yonglilian.model.BPIP_MENU;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * 系统菜单dao
 * @author nfzr
 *
 */
@MapperScan({"com.yonglilian.dao.mapper"})
public interface BpipMenuMapper extends BaseDao<BPIP_MENU>{
	
	/**
	 * 判断是否有下一级菜单
	 * @param menuno
	 * @return
	 */
	int nextCount(String menuno);
	
	/**
	 * 删除菜单对应的角色
	 * @param menuno
	 */
	void deleteRoleMenu(String menuno);
	
	/**
	 * 查询最大编号
	 * @param menuno
	 * @return
	 */
	String getMaxNo(String menuno);  
	
}