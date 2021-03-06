package com.yonglilian.analyseengine.mode;

import zr.zrpower.common.db.DBEntity;
import zr.zrpower.common.db.DBType;

/**
 * <p>Title: ZRPower</p>
 * <p>
 * Description: 数据表实体映射包
 * </p>
 * <p>
 * 单元名称：ANALYSE_STATISTICS_CCONNECTION.java
 * </p>
 * <p>
 * 中文解释：统计计算字段关联配置表数据实体类
 * </p>
 * <p>
 * 作用：将数据库表统计计算字段关联配置表映射为Java类，作为数据传输的载体。
 * </p>
 * 
 * @author Java实体生成器 By NFZR
 */
public class ANALYSE_STATISTICS_CCONNECTION extends DBEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected String ID;
	protected String FID;
	protected String MFIELD;
	protected String CFIELD;
	protected String JOINTYPE;

	/**
	 * 构造函数
	 */
	public ANALYSE_STATISTICS_CCONNECTION() {
		super();
		initialize();
	}

	/**
	 * 初始化函数
	 */
	protected void initialize() {
		this.dbrow.setTableName("ANALYSE_STATISTICS_CCONNECTION");
		this.dbrow.addColumn("ID", null, DBType.STRING); // 字段编号
		this.dbrow.addColumn("FID", null, DBType.STRING); // 统计计算字段表ID
		this.dbrow.addColumn("MFIELD", null, DBType.STRING); // 右关联表字段
		this.dbrow.addColumn("CFIELD", null, DBType.STRING); // 左关联表字段
		this.dbrow.addColumn("JOINTYPE", null, DBType.STRING); // 关联类型
		this.dbrow.setPrimaryKey("ID");
	}

	/**
	 * 获取[字段编号]
	 * 
	 * @return String
	 */
	public String getID() {
		return this.getString(this.dbrow.Column("ID").getString());
	}

	/**
	 * 设置[字段编号]
	 * 
	 * @param ID
	 *            String
	 */
	public void setID(String ID) {
		this.dbrow.Column("ID").setValue(ID);
	}

	/**
	 * 获取[统计计算字段表ID]
	 * 
	 * @return String
	 */
	public String getFID() {
		return this.getString(this.dbrow.Column("FID").getString());
	}

	/**
	 * 设置[统计计算字段表ID]
	 * 
	 * @param FID
	 *            String
	 */
	public void setFID(String FID) {
		this.dbrow.Column("FID").setValue(FID);
	}

	/**
	 * 获取[右关联表字段]
	 * 
	 * @return String
	 */
	public String getMFIELD() {
		return this.getString(this.dbrow.Column("MFIELD").getString());
	}

	/**
	 * 设置[右关联表字段]
	 * 
	 * @param MFIELD
	 *            String
	 */
	public void setMFIELD(String MFIELD) {
		this.dbrow.Column("MFIELD").setValue(MFIELD);
	}

	/**
	 * 获取[左关联表字段]
	 * 
	 * @return String
	 */
	public String getCFIELD() {
		return this.getString(this.dbrow.Column("CFIELD").getString());
	}

	/**
	 * 设置[左关联表字段]
	 * 
	 * @param CFIELD
	 *            String
	 */
	public void setCFIELD(String CFIELD) {
		this.dbrow.Column("CFIELD").setValue(CFIELD);
	}

	/**
	 * 获取[关联类型]
	 * 
	 * @return String
	 */
	public String getJOINTYPE() {
		return this.getString(this.dbrow.Column("JOINTYPE").getString());
	}

	/**
	 * 设置[关联类型]
	 * 
	 * @param JOINTYPE
	 *            String
	 */
	public void setJOINTYPE(String JOINTYPE) {
		this.dbrow.Column("JOINTYPE").setValue(JOINTYPE);
	}
}