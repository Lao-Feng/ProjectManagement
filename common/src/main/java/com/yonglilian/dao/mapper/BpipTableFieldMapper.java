package com.yonglilian.dao.mapper;

import com.yonglilian.dao.BaseDao;
import com.yonglilian.model.dbmanage.BPIP_FIELD;
import tk.mybatis.spring.annotation.MapperScan;

import java.util.List;
import java.util.Map;

/**
 * 表字段数据层
 * @author nfzr
 *
 */
@MapperScan({"com.yonglilian.dao.mapper"})
public interface BpipTableFieldMapper extends BaseDao<BPIP_FIELD> {
	

	/**
	 * 查询数据库表最大的fieldId
	 * @return
	 */
	String getMaxNo();
	
	/**
	 * 通过表英文名称查询数据库是否已经创建字段
	 * @param map {fieldName,tableId}
	 * @return
	 */
	int isField(Map<String,Object> map);
	
	List<BPIP_FIELD> selectList(Map<String,Object> map);
	
}
