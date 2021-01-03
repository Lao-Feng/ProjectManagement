package com.yonglilian.flowengine.mode.config;

import zr.zrpower.common.db.DBEntity;
import zr.zrpower.common.db.DBType;

/**
 * <p>Title: ZRPOWER平台</p>
 * <p>
 * Description: 数据表实体映射包
 * </p>
 * <p>
 * 单元名称：FLOW_CONFIG_CONN_AUTHOR.java
 * </p>
 * <p>
 * 中文解释：关系处理人表数据实体类
 * </p>
 * <p>
 * 作用：将数据库表关系处理人表映射为Java类，作为数据传输的载体。
 * </p>
 * 
 * @author Java实体生成器 By NFZR
 * 
 */
public class FLOW_CONFIG_CONN_AUTHOR extends DBEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 构造函数
	 */
	public FLOW_CONFIG_CONN_AUTHOR() {
		super();
		initialize();
	}

	/**
	 * 初始化函数
	 */
	protected void initialize() {
		this.dbrow.setTableName("FLOW_CONFIG_CONN_AUTHOR");
		this.dbrow.addColumn("ID", null, DBType.STRING); // ID
		this.dbrow.addColumn("CONNTYPE", null, DBType.STRING); // 关系类型
		this.dbrow.addColumn("CONNID", null, DBType.STRING); // 关系ID
		this.dbrow.addColumn("AAID", null, DBType.STRING); // 处理人的活动ID
		this.dbrow.setPrimaryKey("ID");
	}

	/**
	 * 获取[ID]
	 * 
	 * @return String
	 */
	public String getID() {
		return this.getString(this.dbrow.Column("ID").getString());
	}

	/**
	 * 设置[ID]
	 * 
	 * @param ID
	 *            String
	 */
	public void setID(String ID) {
		this.dbrow.Column("ID").setValue(ID);
	}

	/**
	 * 获取[关系类型]
	 * 
	 * @return String
	 */
	public String getCONNTYPE() {
		return this.getString(this.dbrow.Column("CONNTYPE").getString());
	}

	/**
	 * 设置[关系类型]
	 * 
	 * @param CONNTYPE
	 *            String
	 */
	public void setCONNTYPE(String CONNTYPE) {
		this.dbrow.Column("CONNTYPE").setValue(CONNTYPE);
	}

	/**
	 * 获取[关系ID]
	 * 
	 * @return String
	 */
	public String getCONNID() {
		return this.getString(this.dbrow.Column("CONNID").getString());
	}

	/**
	 * 设置[关系ID]
	 * 
	 * @param CONNID
	 *            String
	 */
	public void setCONNID(String CONNID) {
		this.dbrow.Column("CONNID").setValue(CONNID);
	}

	/**
	 * 获取[处理人的活动ID]
	 * 
	 * @return String
	 */
	public String getAAID() {
		return this.getString(this.dbrow.Column("AAID").getString());
	}

	/**
	 * 设置[处理人的活动ID]
	 * 
	 * @param AAID
	 *            String
	 */
	public void setAAID(String AAID) {
		this.dbrow.Column("AAID").setValue(AAID);
	}

}
