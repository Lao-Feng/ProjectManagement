package com.yonglilian.flowengine.mode.config;

import zr.zrpower.common.db.DBEntity;
import zr.zrpower.common.db.DBType;

/**
 * <p>
 * Title: ZRPOWER平台
 * </p>
 * <p>
 * Description: 数据表实体映射包
 * </p>
 * <p>
 * 单元名称：FLOW_CONFIG_PROSESS_GROUP.java
 * </p>
 * <p>
 * 中文解释：过程流程组表数据实体类
 * </p>
 * <p>
 * 作用：将数据库表过程流程组表映射为Java类，作为数据传输的载体。
 * </p>
 * 
 * @author Java实体生成器 By NFZR
 * 
 */
public class FLOW_CONFIG_PROSESS_GROUP extends DBEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected String ID;
	protected String PROSESSID;
	protected String GROUPID;
	protected String TYPE;

	/**
	 * 构造函数
	 */
	public FLOW_CONFIG_PROSESS_GROUP() {
		super();
		initialize();
	}

	/**
	 * 初始化函数
	 */
	protected void initialize() {
		this.dbrow.setTableName("FLOW_CONFIG_PROSESS_GROUP");
		this.dbrow.addColumn("ID", null, DBType.STRING); // ID
		this.dbrow.addColumn("PROSESSID", null, DBType.STRING); // 过程ID
		this.dbrow.addColumn("GROUPID", null, DBType.STRING); // 流程组ID
		this.dbrow.addColumn("TYPE", null, DBType.STRING); // 类别
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
	 * 获取[过程ID]
	 * 
	 * @return String
	 */
	public String getPROSESSID() {
		return this.getString(this.dbrow.Column("PROSESSID").getString());
	}

	/**
	 * 设置[过程ID]
	 * 
	 * @param PROSESSID
	 *            String
	 */
	public void setPROSESSID(String PROSESSID) {
		this.dbrow.Column("PROSESSID").setValue(PROSESSID);
	}

	/**
	 * 获取[流程组ID]
	 * 
	 * @return String
	 */
	public String getGROUPID() {
		return this.getString(this.dbrow.Column("GROUPID").getString());
	}

	/**
	 * 设置[流程组ID]
	 * 
	 * @param GROUPID
	 *            String
	 */
	public void setGROUPID(String GROUPID) {
		this.dbrow.Column("GROUPID").setValue(GROUPID);
	}

	/**
	 * 获取[类别]
	 * 
	 * @return String
	 */
	public String getTYPE() {
		return this.getString(this.dbrow.Column("TYPE").getString());
	}

	/**
	 * 设置[类别]
	 * 
	 * @param TYPE
	 *            String
	 */
	public void setTYPE(String TYPE) {
		this.dbrow.Column("TYPE").setValue(TYPE);
	}

}