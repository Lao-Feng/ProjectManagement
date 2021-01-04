package com.yonglilian.dao.mapper;


import com.yonglilian.dao.BaseDao;
import com.yonglilian.model.BPIP_TABLE;
import com.yonglilian.model.CODE_YWXT;
import tk.mybatis.spring.annotation.MapperScan;

import java.util.List;
import java.util.Map;

/**
 * 字典数据操作dao
 * @author nfzr
 *
 */
@MapperScan({"com.yonglilian.dao.mapper"})
public interface BpipCodeMapper extends BaseDao<CODE_YWXT>{
	
	/**
	 * 查询数据库的表
	 * @param map
	 * @return
	 */
	List<BPIP_TABLE> getTables(Map<String,Object> map);
	
	/**
	 * 查询详情
	 * @param map {table,code}
	 * @return
	 */
	CODE_YWXT queryObject(Map<String,Object> map);
	
	
}