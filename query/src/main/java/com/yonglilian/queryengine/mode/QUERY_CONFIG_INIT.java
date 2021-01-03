package com.yonglilian.queryengine.mode;

import zr.zrpower.common.db.DBEntity;
import zr.zrpower.common.db.DBType;

/**
 * <p>Title: ZRPower</p>
 * <p>
 * Description: 数据表实体映射包
 * </p>
 * <p>
 * 单元名称：QUERY_CONFIG_INIT.java
 * </p>
 * <p>
 * 中文解释：查询配置初始条件表数据实体类
 * </p>
 * <p>
 * 作用：将数据库表查询配置初始条件表映射为Java类，作为数据传输的载体。
 * </p>
 * 
 * @author Java实体生成器 By NFZR
 */
public class QUERY_CONFIG_INIT extends DBEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected String ID;
	protected String FID;
	protected String QLEFT;
	protected String FIELD;
	protected String SYMBOL;
	protected String WHEREVALUE;
	protected String QRIGHT;
	protected String LOGIC;

	/**
	 * 构造函数
	 */
	public QUERY_CONFIG_INIT() {
		super();
		initialize();
	}

	/**
	 * 初始化函数
	 */
	protected void initialize() {
		this.dbrow.setTableName("QUERY_CONFIG_INIT");
		this.dbrow.addColumn("ID", null, DBType.STRING); // 人员编号
		this.dbrow.addColumn("FID", null, DBType.STRING); // 配置表ID
		this.dbrow.addColumn("QLEFT", null, DBType.STRING); // 左括号
		this.dbrow.addColumn("FIELD", null, DBType.STRING); // 条件字段
		this.dbrow.addColumn("SYMBOL", null, DBType.STRING); // 条件符
		this.dbrow.addColumn("WHEREVALUE", null, DBType.STRING); // 条件值
		this.dbrow.addColumn("QRIGHT", null, DBType.STRING); // 右括号
		this.dbrow.addColumn("LOGIC", null, DBType.STRING); // 逻辑符
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
	 * 获取[左括号]
	 * 
	 * @return String
	 */
	public String getQLEFT() {
		return this.getString(this.dbrow.Column("QLEFT").getString());
	}

	/**
	 * 设置[左括号]
	 * 
	 * @param QLEFT
	 *            String
	 */
	public void setQLEFT(String QLEFT) {
		this.dbrow.Column("QLEFT").setValue(QLEFT);
	}

	/**
	 * 获取[条件字段]
	 * 
	 * @return String
	 */
	public String getFIELD() {
		return this.getString(this.dbrow.Column("FIELD").getString());
	}

	/**
	 * 设置[条件字段]
	 * 
	 * @param FIELD
	 *            String
	 */
	public void setFIELD(String FIELD) {
		this.dbrow.Column("FIELD").setValue(FIELD);
	}

	/**
	 * 获取[条件符]
	 * 
	 * @return String
	 */
	public String getSYMBOL() {
		return this.getString(this.dbrow.Column("SYMBOL").getString());
	}

	/**
	 * 设置[条件符]
	 * 
	 * @param SYMBOL
	 *            String
	 */
	public void setSYMBOL(String SYMBOL) {
		this.dbrow.Column("SYMBOL").setValue(SYMBOL);
	}

	/**
	 * 获取[条件值]
	 * 
	 * @return String
	 */
	public String getWHEREVALUE() {
		return this.getString(this.dbrow.Column("WHEREVALUE").getString());
	}

	/**
	 * 设置[条件值]
	 * 
	 * @param WHEREVALUE
	 *            String
	 */
	public void setWHEREVALUE(String WHEREVALUE) {
		this.dbrow.Column("WHEREVALUE").setValue(WHEREVALUE);
	}

	/**
	 * 获取[右括号]
	 * 
	 * @return String
	 */
	public String getQRIGHT() {
		return this.getString(this.dbrow.Column("QRIGHT").getString());
	}

	/**
	 * 设置[右括号]
	 * 
	 * @param QRIGHT
	 *            String
	 */
	public void setQRIGHT(String QRIGHT) {
		this.dbrow.Column("QRIGHT").setValue(QRIGHT);
	}

	/**
	 * 获取[逻辑符]
	 * 
	 * @return String
	 */
	public String getLOGIC() {
		return this.getString(this.dbrow.Column("LOGIC").getString());
	}

	/**
	 * 设置[逻辑符]
	 * 
	 * @param LOGIC
	 *            String
	 */
	public void setLOGIC(String LOGIC) {
		this.dbrow.Column("LOGIC").setValue(LOGIC);
	}
}