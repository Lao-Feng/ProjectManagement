package com.yonglilian.service;

import com.yonglilian.model.BPIP_OPERATE_TABLE;

/**
 * 表结构维护管理服务层
 * 
 * @author lwk
 *
 */
public interface OperateTableService {
	/**
	 * 功能：增加字段
	 * @param field 字段信息
	 * @return
	 * @throws Exception
	 */
	public boolean addField(BPIP_OPERATE_TABLE field) throws Exception;

	/**
	 * 功能：删除字段
	 * @param field 字段信息
	 * @return
	 * @throws Exception
	 */
	public boolean deleteField(BPIP_OPERATE_TABLE field) throws Exception;

	/**
	 * 功能：删除指定的表
	 * @param TableName 表名
	 * @return
	 * @throws Exception
	 */
	public boolean deleteTable(String tableName) throws Exception;

	/**
	 * 功能：修改字段
	 * @param field 字段信息
	 * @return
	 * @throws Exception
	 */
	public boolean modifyField(BPIP_OPERATE_TABLE field) throws Exception;

	/**
	 * 功能：更改表名
	 * @param oldName 旧表名
	 * @param newName 新表名
	 * @return
	 * @throws Exception
	 */
	public boolean changeTableName(String oldName, String newName) throws Exception;

}
