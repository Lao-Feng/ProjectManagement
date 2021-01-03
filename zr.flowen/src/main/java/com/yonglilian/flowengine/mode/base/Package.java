package com.yonglilian.flowengine.mode.base;

import zr.zrpower.common.db.DBEntity;
import zr.zrpower.common.db.DBType;

import java.util.Date;

/**
 * <p>Title: 流程分类实体 </p>
 * <p>
 * Description: 数据表实体映射包
 * </p>
 * <p>
 * 单元名称：FLOW_CONFIG_PACKAGE.java
 * </p>
 * <p>
 * 中文解释：数据实体类
 * </p>
 * <p>
 * 作用：将数据库表映射为Java类，作为数据传输的载体。
 * </p>
 */
public class Package extends DBEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected String ID;
	protected String IDENTIFICATION;
	protected String NAME;
	protected String DESC1;
	protected String STATUS;
	protected String ICO;
	protected String CREATEPSN;
	protected Date CREATEDATE;
	protected String FID;

	/**
	 * 构造函数
	 */
	public Package() {
		super();
		initialize();
	}

	/**
	 * 初始化函数
	 */
	protected void initialize() {
		this.dbrow.setTableName("FLOW_CONFIG_PACKAGE");
		this.dbrow.addColumn("ID", null, DBType.STRING);
		this.dbrow.addColumn("IDENTIFICATION", null, DBType.STRING);
		this.dbrow.addColumn("NAME", null, DBType.STRING);
		this.dbrow.addColumn("DESC1", null, DBType.STRING);
		this.dbrow.addColumn("STATUS", null, DBType.STRING);
		this.dbrow.addColumn("ICO", null, DBType.STRING);
		this.dbrow.addColumn("CREATEPSN", null, DBType.STRING);
		this.dbrow.addColumn("CREATEDATE", null, DBType.DATE);
		this.dbrow.addColumn("FID", null, DBType.STRING);
		this.dbrow.setPrimaryKey("ID");
	}

	/**
	 * 获取[]
	 * 
	 * @return String
	 */
	public String getID() {
		return this.getString(this.dbrow.Column("ID").getString());
	}

	/**
	 * 设置[]
	 * 
	 * @param ID
	 *            String
	 */
	public void setID(String ID) {
		this.dbrow.Column("ID").setValue(ID);
		this.ID = ID;
	}

	/**
	 * 获取[]
	 * 
	 * @return String
	 */
	public String getIDENTIFICATION() {
		return this.getString(this.dbrow.Column("IDENTIFICATION").getString());
	}

	/**
	 * 设置[]
	 * 
	 * @param IDENTIFICATION
	 *            String
	 */
	public void setIDENTIFICATION(String IDENTIFICATION) {
		this.dbrow.Column("IDENTIFICATION").setValue(IDENTIFICATION);
		this.IDENTIFICATION = IDENTIFICATION;
	}

	/**
	 * 获取[]
	 * 
	 * @return String
	 */
	public String getNAME() {
		return this.getString(this.dbrow.Column("NAME").getString());
	}

	/**
	 * 设置[]
	 * 
	 * @param NAME
	 *            String
	 */
	public void setNAME(String NAME) {
		this.dbrow.Column("NAME").setValue(NAME);
		this.NAME = NAME;
	}

	/**
	 * 获取[]
	 * 
	 * @return String
	 */
	public String getDESC1() {
		return this.getString(this.dbrow.Column("DESC1").getString());
	}

	/**
	 * 设置[]
	 * 
	 * @param DESC
	 *            String
	 */
	public void setDESC1(String DESC1) {
		this.dbrow.Column("DESC1").setValue(DESC1);
		this.DESC1 = DESC1;
	}

	/**
	 * 获取[]
	 * 
	 * @return String
	 */
	public String getSTATUS() {
		return this.getString(this.dbrow.Column("STATUS").getString());
	}

	/**
	 * 设置[]
	 * 
	 * @param STATUS
	 *            String
	 */
	public void setSTATUS(String STATUS) {
		this.dbrow.Column("STATUS").setValue(STATUS);
		this.STATUS = STATUS;
	}

	/**
	 * 获取[]
	 * 
	 * @return String
	 */
	public String getICO() {
		return this.getString(this.dbrow.Column("ICO").getString());
	}

	/**
	 * 设置[]
	 * 
	 * @param ICO
	 *            String
	 */
	public void setICO(String ICO) {
		this.dbrow.Column("ICO").setValue(ICO);
		this.ICO = ICO;
	}

	/**
	 * 获取[]
	 * 
	 * @return String
	 */
	public String getCREATEPSN() {
		return this.getString(this.dbrow.Column("CREATEPSN").getString());
	}

	/**
	 * 设置[]
	 * 
	 * @param CREATEPSN
	 *            String
	 */
	public void setCREATEPSN(String CREATEPSN) {
		this.dbrow.Column("CREATEPSN").setValue(CREATEPSN);
		this.CREATEPSN = CREATEPSN;
	}

	/**
	 * 获取[]
	 * 
	 * @return java.util.Date
	 */
	public java.util.Date getCREATEDATE() {
		return this.dbrow.Column("CREATEDATE").getDate();
	}

	/**
	 * 设置[]
	 * 
	 * @param CREATEDATE
	 *            java.util.Date
	 */
	public void setCREATEDATE(java.util.Date CREATEDATE) {
		this.dbrow.Column("CREATEDATE").setValue(CREATEDATE);
		this.CREATEDATE = CREATEDATE;
	}

	/**
	 * 获取[]
	 * 
	 * @return String
	 */
	public String getFID() {
		return this.getString(this.dbrow.Column("FID").getString());
	}

	/**
	 * 设置[]
	 * 
	 * @param FID
	 *            String
	 */
	public void setFID(String FID) {
		this.dbrow.Column("FID").setValue(FID);
		this.FID = FID;
	}

}