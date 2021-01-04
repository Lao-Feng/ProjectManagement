package com.yonglilian.service;

import com.yonglilian.model.dbmanage.BPIP_FIELD;
import com.yonglilian.model.dbmanage.BPIP_TABLE;

/**
 * 数据库操作管理
 * @author nfzr
 *
 */
public interface BaseDbaDaoService {
	
	/**
	 * 创建表
	 * @param model
	 * @return
	 */
	boolean CreatTable(BPIP_TABLE model);
	/**
	 * 删除表
	 * @param tableName
	 * @return
	 */
	boolean DropTable(String tableName);
	/**
	 * 修改表key
	 * @param model
	 * @return
	 */
	boolean AlterTableKey(BPIP_TABLE model);
	/**
	 * 创建表字段
	 * @param model
	 * @return
	 */
    boolean CreatField(BPIP_FIELD model);
	/**
	 * 删除表字段
	 * @param model
	 * @return
	 */
	boolean DropField(BPIP_FIELD model);
	/**
	 * 修改表字段
	 * @param model
	 * @return
	 */
	boolean AlterField(BPIP_FIELD model);
	
	/**
	 * 判断表字段是否存在
	 * @param model
	 * @return
	 */
	boolean booleanField(BPIP_FIELD model);
	/**
	 * 执行sql语句操作表
	 * @param sql
	 * @return
	 */
	boolean dbaSql(String sql);
}
