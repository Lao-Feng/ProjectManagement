package com.yonglilian.queryengine.mode;

import zr.zrpower.common.db.DBEntity;
import zr.zrpower.common.db.DBType;

/**
 * <p>Title: ZRPower</p>
 * <p>
 * Description: 数据表实体映射包
 * </p>
 * <p>
 * 单元名称：QUERY_CONFIG_PARAMETER.java
 * </p>
 * <p>
 * 中文解释：按钮参数配置表数据实体类
 * </p>
 * <p>
 * 作用：将数据库表按钮参数配置表映射为Java类，作为数据传输的载体。
 * </p>
 * 
 * @author Java实体生成器 By NFZR
 */
public class QUERY_CONFIG_PARAMETER extends DBEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected String ID;
	protected String FID;
	protected String BID;
	protected String NAME;
	protected String FIELD;

	/**
	 * 构造函数
	 */
	public QUERY_CONFIG_PARAMETER() {
		super();
		initialize();
	}

	/**
	 * 初始化函数
	 */
	protected void initialize() {
		this.dbrow.setTableName("QUERY_CONFIG_PARAMETER");
		this.dbrow.addColumn("ID", null, DBType.STRING); // 人员编号
		this.dbrow.addColumn("FID", null, DBType.STRING); // 配置表ID
		this.dbrow.addColumn("BID", null, DBType.STRING); // 按钮ID
		this.dbrow.addColumn("NAME", null, DBType.STRING); // 参数名
		this.dbrow.addColumn("FIELD", null, DBType.STRING); // 参数字段
		this.dbrow.setPrimaryKey("ID");
	}

	/**
	 * 获取[人员编号]
	 * 
	 * @return String
	 */
	public String getID() {
		return this.getString(this.dbrow.Column("ID").getString());
	}

	/**
	 * 设置[人员编号]
	 * 
	 * @param ID
	 *            String
	 */
	public void setID(String ID) {
		this.dbrow.Column("ID").setValue(ID);
	}

	/**
	 * 获取[配置表ID]
	 * 
	 * @return String
	 */
	public String getFID() {
		return this.getString(this.dbrow.Column("FID").getString());
	}

	/**
	 * 设置[配置表ID]
	 * 
	 * @param FID
	 *            String
	 */
	public void setFID(String FID) {
		this.dbrow.Column("FID").setValue(FID);
	}

	/**
	 * 获取[按钮ID]
	 * 
	 * @return String
	 */
	public String getBID() {
		return this.getString(this.dbrow.Column("BID").getString());
	}

	/**
	 * 设置[按钮ID]
	 * 
	 * @param BID
	 *            String
	 */
	public void setBID(String BID) {
		this.dbrow.Column("BID").setValue(BID);
	}

	/**
	 * 获取[参数名]
	 * 
	 * @return String
	 */
	public String getNAME() {
		return this.getString(this.dbrow.Column("NAME").getString());
	}

	/**
	 * 设置[参数名]
	 * 
	 * @param NAME
	 *            String
	 */
	public void setNAME(String NAME) {
		this.dbrow.Column("NAME").setValue(NAME);
	}

	/**
	 * 获取[参数字段]
	 * 
	 * @return String
	 */
	public String getFIELD() {
		return this.getString(this.dbrow.Column("FIELD").getString());
	}

	/**
	 * 设置[参数字段]
	 * 
	 * @param FIELD
	 *            String
	 */
	public void setFIELD(String FIELD) {
		this.dbrow.Column("FIELD").setValue(FIELD);
	}
}