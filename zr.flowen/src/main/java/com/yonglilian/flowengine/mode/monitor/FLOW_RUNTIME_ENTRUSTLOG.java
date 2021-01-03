package com.yonglilian.flowengine.mode.monitor;

import zr.zrpower.common.db.DBEntity;
import zr.zrpower.common.db.DBType;

import java.util.Date;

/**
 * <p>Title: ZRPower</p>
 * <p>
 * Description: 数据表实体映射包
 * </p>
 * <p>
 * 单元名称：FLOW_RUNTIME_ENTRUSTLOG.java
 * </p>
 * <p>
 * 中文解释：流程权限委托日志管理表数据实体类
 * </p>
 * <p>
 * 作用：将数据库表流程权限委托日志管理表映射为Java类，作为数据传输的载体。
 * </p>
 * 
 * @author Java实体生成器 By NFZR
 * 
 */
public class FLOW_RUNTIME_ENTRUSTLOG extends DBEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected String ID;
	protected String SUSERNO;
	protected String IUSERNO;
	protected Date SDATE;
	protected Date EDATE;
	protected String TYPE;
	protected String FLOWNAME;
	protected String FLOWID;
	protected String FLOWNODE;
	protected Date LOGDATE;

	/**
	 * 构造函数
	 */
	public FLOW_RUNTIME_ENTRUSTLOG() {
		super();
		initialize();
	}

	/**
	 * 初始化函数
	 */
	protected void initialize() {
		this.dbrow.setTableName("FLOW_RUNTIME_ENTRUSTLOG");
		this.dbrow.addColumn("ID", null, DBType.STRING); // ID
		this.dbrow.addColumn("SUSERNO", null, DBType.STRING); // 委托人编号
		this.dbrow.addColumn("IUSERNO", null, DBType.STRING); // 受委托人编号
		this.dbrow.addColumn("SDATE", null, DBType.DATE); // 委托开始时间
		this.dbrow.addColumn("EDATE", null, DBType.DATE); // 委托结束时间
		this.dbrow.addColumn("TYPE", null, DBType.STRING); // 委托类型
		this.dbrow.addColumn("FLOWNAME", null, DBType.STRING); // 委托流程名称
		this.dbrow.addColumn("FLOWID", null, DBType.STRING); // 委托流程编号
		this.dbrow.addColumn("FLOWNODE", null, DBType.STRING); // 委托流程步骤
		this.dbrow.addColumn("LOGDATE", null, DBType.DATE); // 日志日期
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
	 * 获取[委托人编号]
	 * 
	 * @return String
	 */
	public String getSUSERNO() {
		return this.getString(this.dbrow.Column("SUSERNO").getString());
	}

	/**
	 * 设置[委托人编号]
	 * 
	 * @param SUSERNO
	 *            String
	 */
	public void setSUSERNO(String SUSERNO) {
		this.dbrow.Column("SUSERNO").setValue(SUSERNO);
	}

	/**
	 * 获取[受委托人编号]
	 * 
	 * @return String
	 */
	public String getIUSERNO() {
		return this.getString(this.dbrow.Column("IUSERNO").getString());
	}

	/**
	 * 设置[受委托人编号]
	 * 
	 * @param IUSERNO
	 *            String
	 */
	public void setIUSERNO(String IUSERNO) {
		this.dbrow.Column("IUSERNO").setValue(IUSERNO);
	}

	/**
	 * 获取[委托开始时间]
	 * 
	 * @return java.util.Date
	 */
	public Date getSDATE() {
		return this.dbrow.Column("SDATE").getDate();
	}

	/**
	 * 设置[委托开始时间]
	 * 
	 * @param SDATE
	 *            java.util.Date
	 */
	public void setSDATE(Date SDATE) {
		this.dbrow.Column("SDATE").setValue(SDATE);
	}

	/**
	 * 获取[委托结束时间]
	 * 
	 * @return java.util.Date
	 */
	public Date getEDATE() {
		return this.dbrow.Column("EDATE").getDate();
	}

	/**
	 * 设置[委托结束时间]
	 * 
	 * @param EDATE
	 *            java.util.Date
	 */
	public void setEDATE(Date EDATE) {
		this.dbrow.Column("EDATE").setValue(EDATE);
	}

	/**
	 * 获取[委托类型]
	 * 
	 * @return String
	 */
	public String getTYPE() {
		return this.getString(this.dbrow.Column("TYPE").getString());
	}

	/**
	 * 设置[委托类型]
	 * 
	 * @param TYPE
	 *            String
	 */
	public void setTYPE(String TYPE) {
		this.dbrow.Column("TYPE").setValue(TYPE);
	}

	/**
	 * 获取[委托流程名称]
	 * 
	 * @return String
	 */
	public String getFLOWNAME() {
		return this.getString(this.dbrow.Column("FLOWNAME").getString());
	}

	/**
	 * 设置[委托流程名称]
	 * 
	 * @param FLOWNAME
	 *            String
	 */
	public void setFLOWNAME(String FLOWNAME) {
		this.dbrow.Column("FLOWNAME").setValue(FLOWNAME);
	}

	/**
	 * 获取[委托流程编号]
	 * 
	 * @return String
	 */
	public String getFLOWID() {
		return this.getString(this.dbrow.Column("FLOWID").getString());
	}

	/**
	 * 设置[委托流程编号]
	 * 
	 * @param FLOWID
	 *            String
	 */
	public void setFLOWID(String FLOWID) {
		this.dbrow.Column("FLOWID").setValue(FLOWID);
	}

	/**
	 * 获取[委托流程步骤]
	 * 
	 * @return String
	 */
	public String getFLOWNODE() {
		return this.getString(this.dbrow.Column("FLOWNODE").getString());
	}

	/**
	 * 设置[委托流程步骤]
	 * 
	 * @param FLOWNODE
	 *            String
	 */
	public void setFLOWNODE(String FLOWNODE) {
		this.dbrow.Column("FLOWNODE").setValue(FLOWNODE);
	}

	/**
	 * 获取[日志日期]
	 * 
	 * @return java.util.Date
	 */
	public Date getLOGDATE() {
		return this.dbrow.Column("LOGDATE").getDate();
	}

	/**
	 * 设置[日志日期]
	 * 
	 * @param LOGDATE
	 *            java.util.Date
	 */
	public void setLOGDATE(Date LOGDATE) {
		this.dbrow.Column("LOGDATE").setValue(LOGDATE);
	}

}