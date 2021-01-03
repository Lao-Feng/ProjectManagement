package com.yonglilian.dao.mapper;

import com.yonglilian.model.BPIP_MENU;
import tk.mybatis.spring.annotation.MapperScan;

import java.util.List;
import java.util.Map;

/**
 * 用户操作数据层
 * @author lwk
 *
 */
@MapperScan({"com.yonglilian.dao.mapper"})
public interface UntilMapper{
	
	/**
	 * 查询用户所有全查菜单
	 * @param userId,flag,menuNo,len
	 * @return
	 */
	public List<BPIP_MENU> userMenuList(Map<String,Object> map);
	
}