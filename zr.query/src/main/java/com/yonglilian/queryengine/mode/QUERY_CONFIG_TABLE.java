package com.yonglilian.queryengine.mode;

import zr.zrpower.common.db.DBEntity;
import zr.zrpower.common.db.DBType;

/**
 * <p>Title: ZRPower</p>
 * <p>
 * Description: 数据表实体映射包
 * </p>
 * <p>
 * 单元名称：QUERY_CONFIG_TABLE.java
 * </p>
 * <p>
 * 中文解释：查询配置表数据实体类
 * </p>
 * <p>
 * 作用：将数据库表查询配置表映射为Java类，作为数据传输的载体。
 * </p>
 * 
 * @author Java实体生成器 By NFZR
 */
public class QUERY_CONFIG_TABLE extends DBEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected String ID;
	protected String NAME;
	protected String MAINTABLE;
	protected String TYPE;
	protected String BID;
	protected String CID;
	protected String IFFIRSTDATA;
	protected String IFMULSEL;
	protected String IFCOMBTN;
	protected String DCODE;
	protected String QITEMTYPE;
	protected String QTABLETYPE;

	/**
	 * 构造函数
	 */
	public QUERY_CONFIG_TABLE() {
		super();
		initialize();
	}

	/**
	 * 初始化函数
	 */
	protected void initialize() {
		this.dbrow.setTableName("QUERY_CONFIG_TABLE");
		this.dbrow.addColumn("ID", null, DBType.STRING); // 编号
		this.dbrow.addColumn("NAME", null, DBType.STRING); // 配置名称
		this.dbrow.addColumn("MAINTABLE", null, DBType.STRING); // 主表名称
		this.dbrow.addColumn("TYPE", null, DBType.STRING); // 默认查询方式
		this.dbrow.addColumn("BID", null, DBType.STRING); // 双击执行的按钮
		this.dbrow.addColumn("CID", null, DBType.STRING); // 单击执行的按钮
		this.dbrow.addColumn("IFFIRSTDATA", null, DBType.STRING); // 首次是否显示第一页的数据
		this.dbrow.addColumn("IFMULSEL", null, DBType.STRING); // 是否可多选
		this.dbrow.addColumn("IFCOMBTN", null, DBType.STRING); // 是否显示公共按钮
		this.dbrow.addColumn("DCODE", null, DBType.STRING); // 加密
		this.dbrow.addColumn("QITEMTYPE", null, DBType.STRING); // 查询项目类型
		this.dbrow.addColumn("QTABLETYPE", null, DBType.STRING); // 查询结果显示表格类型

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
		this.ID = ID;
	}

	/**
	 * 获取[配置名称]
	 * 
	 * @return String
	 */
	public String getNAME() {
		return this.getString(this.dbrow.Column("NAME").getString());
	}

	/**
	 * 设置[配置名称]
	 * 
	 * @param NAME
	 *            String
	 */
	public void setNAME(String NAME) {
		this.dbrow.Column("NAME").setValue(NAME);
		this.NAME = NAME;
	}

	/**
	 * 获取[主表名称]
	 * 
	 * @return String
	 */
	public String getMAINTABLE() {
		return this.getString(this.dbrow.Column("MAINTABLE").getString());
	}

	/**
	 * 设置[主表名称]
	 * 
	 * @param MAINTABLE
	 *            String
	 */
	public void setMAINTABLE(String MAINTABLE) {
		this.dbrow.Column("MAINTABLE").setValue(MAINTABLE);
		this.MAINTABLE = MAINTABLE;
	}

	/**
	 * 获取[默认查询方式]
	 * 
	 * @return String
	 */
	public String getTYPE() {
		return this.getString(this.dbrow.Column("TYPE").getString());
	}

	/**
	 * 设置[默认查询方式]
	 * 
	 * @param TYPE
	 *            String
	 */
	public void setTYPE(String TYPE) {
		this.dbrow.Column("TYPE").setValue(TYPE);
		this.TYPE = TYPE;
	}

	/**
	 * 获取[双击执行的按钮]
	 * 
	 * @return String
	 */
	public String getBID() {
		return this.getString(this.dbrow.Column("BID").getString());
	}

	/**
	 * 设置[双击执行的按钮]
	 * 
	 * @param TYPE
	 *            String
	 */
	public void setBID(String BID) {
		this.dbrow.Column("BID").setValue(BID);
		this.BID = BID;
	}

	/**
	 * 获取[单击执行的按钮]
	 * 
	 * @return String
	 */
	public String getCID() {
		return this.getString(this.dbrow.Column("CID").getString());
	}

	/**
	 * 设置[单击执行的按钮]
	 * 
	 * @param TYPE
	 *            String
	 */
	public void setCID(String CID) {
		this.dbrow.Column("CID").setValue(CID);
		this.CID = CID;
	}

	/**
	 * 获取[首次是否显示第一页的数据]
	 * 
	 * @return String
	 */
	public String getIFFIRSTDATA() {
		return this.getString(this.dbrow.Column("IFFIRSTDATA").getString());
	}

	/**
	 * 设置[首次是否显示第一页的数据]
	 * 
	 * @param TYPE
	 *            String
	 */
	public void setIFFIRSTDATA(String IFFIRSTDATA) {
		this.dbrow.Column("IFFIRSTDATA").setValue(IFFIRSTDATA);
		this.IFFIRSTDATA = IFFIRSTDATA;
	}

	/**
	 * 获取[是否可多选]
	 * 
	 * @return String
	 */
	public String getIFMULSEL() {
		return this.getString(this.dbrow.Column("IFMULSEL").getString());
	}

	/**
	 * 设置[是否可多选]
	 * 
	 * @param TYPE
	 *            String
	 */
	public void setIFMULSEL(String IFMULSEL) {
		this.dbrow.Column("IFMULSEL").setValue(IFMULSEL);
		this.IFMULSEL = IFMULSEL;
	}

	/**
	 * 获取[是否显示公共按钮]
	 * 
	 * @return String
	 */
	public String getIFCOMBTN() {
		return this.getString(this.dbrow.Column("IFCOMBTN").getString());
	}

	/**
	 * 设置[是否显示公共按钮]
	 * 
	 * @param TYPE
	 *            String
	 */
	public void setIFCOMBTN(String IFCOMBTN) {
		this.dbrow.Column("IFCOMBTN").setValue(IFCOMBTN);
		this.IFCOMBTN = IFCOMBTN;
	}

	public String getDCODE() {
		return this.getString(this.dbrow.Column("DCODE").getString());
	}

	public void setDCODE(String DCODE) {
		this.dbrow.Column("DCODE").setValue(DCODE);
		this.DCODE = DCODE;
	}

	public String getQITEMTYPE() {
		return this.getString(this.dbrow.Column("QITEMTYPE").getString());
	}

	public void setQITEMTYPE(String QITEMTYPE) {
		this.dbrow.Column("QITEMTYPE").setValue(QITEMTYPE);
		this.QITEMTYPE = QITEMTYPE;
	}

	public String getQTABLETYPE() {
		return this.getString(this.dbrow.Column("QTABLETYPE").getString());
	}

	public void setQTABLETYPE(String QTABLETYPE) {
		this.dbrow.Column("QTABLETYPE").setValue(QTABLETYPE);
		this.QTABLETYPE = QTABLETYPE;
	}
}