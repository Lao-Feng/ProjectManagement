package com.yonglilian.dao.mapper;

import com.yonglilian.dao.BaseDao;
import com.yonglilian.model.dbmanage.BPIP_TABLESPACE;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * 表分类管理数据层
 * @author nfzr
 *
 */
@MapperScan({"com.yonglilian.dao.mapper"})
public interface BpipTablespaceMapper extends BaseDao<BPIP_TABLESPACE> {
	
	
	/**
	 * 生成最大的id
	 * @return
	 */
	String getMaxNo();
	
	/**
	 * 查询表空间下是否存在表
	 * @param id
	 * @return
	 */
	int queryTables(String id);
}
