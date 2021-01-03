package com.yonglilian.analyseengine.mode;

import zr.zrpower.common.db.DBEntity;
import zr.zrpower.common.db.DBType;

/**
 * <p>Title: ZRPower</p>
 * <p>
 * Description: 数据表实体映射包
 * </p>
 * <p>
 * 单元名称：ANALYSE_STATISTICS_CWHERE.java
 * </p>
 * <p>
 * 中文解释：统计条件表数据实体类
 * </p>
 * <p>
 * 作用：将数据库表统计条件表映射为Java类，作为数据传输的载体。
 * </p>
 * 
 * @author Java实体生成器 By NFZR
 */
public class ANALYSE_STATISTICS_CWHERE extends DBEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected String ID;
	protected String FID;
	protected String SLEFT;
	protected String FIELD;
	protected String SYMBOL;
	protected String WHEREVALUE;
	protected String SRIGHT;
	protected String LOGIC;

	/**
	 * 构造函数
	 */
	public ANALYSE_STATISTICS_CWHERE() {
		super();
		initialize();
	}

	/**
	 * 初始化函数
	 */
	protected void initialize() {
		this.dbrow.setTableName("ANALYSE_STATISTICS_CWHERE");
		this.dbrow.addColumn("ID", null, DBType.STRING); // 编号
		this.dbrow.addColumn("FID", null, DBType.STRING); // 统计项目编号
		this.dbrow.addColumn("SLEFT", null, DBType.STRING); // 左括号
		this.dbrow.addColumn("FIELD", null, DBType.STRING); // 条件字段
		this.dbrow.addColumn("SYMBOL", null, DBType.STRING); // 条件符
		this.dbrow.addColumn("WHEREVALUE", null, DBType.STRING); // 条件值
		this.dbrow.addColumn("SRIGHT", null, DBType.STRING); // 右括号
		this.dbrow.addColumn("LOGIC", null, DBType.STRING); // 逻辑符
		this.dbrow.setPrimaryKey("ID");
	}

	/**
	 * 获取[编号]
	 * 
	 * @return String
	 */
	public String getID() {
		return this.getString(this.dbrow.Column("ID").getString());
	}

	/**
	 * 设置[编号]
	 * 
	 * @param ID
	 *            String
	 */
	public void setID(String ID) {
		this.dbrow.Column("ID").setValue(ID);
	}

	/**
	 * 获取[统计项目编号]
	 * 
	 * @return String
	 */
	public String getFID() {
		return this.getString(this.dbrow.Column("FID").getString());
	}

	/**
	 * 设置[统计项目编号]
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
	public String getSLEFT() {
		return this.getString(this.dbrow.Column("SLEFT").getString());
	}

	/**
	 * 设置[左括号]
	 * 
	 * @param SLEFT
	 *            String
	 */
	public void setSLEFT(String SLEFT) {
		this.dbrow.Column("SLEFT").setValue(SLEFT);
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
	public String getSRIGHT() {
		return this.getString(this.dbrow.Column("SRIGHT").getString());
	}

	/**
	 * 设置[右括号]
	 * 
	 * @param SRIGHT
	 *            String
	 */
	public void setSRIGHT(String SRIGHT) {
		this.dbrow.Column("SRIGHT").setValue(SRIGHT);
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