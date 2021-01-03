package com.yonglilian.model;

import zr.zrpower.common.db.DBEntity;
import zr.zrpower.common.db.DBType;

/**
 * <p>Title: ZRPower</p>
 * <p>
 * Description: 数据表实体映射包
 * </p>
 * <p>
 * 单元名称：BPIP_USER_REMOVE.java
 * </p>
 * <p>
 * 中文解释：用户调动情况表数据实体类
 * </p>
 * <p>
 * 作用：将数据库表用户调动情况表映射为Java类，作为数据传输的载体。
 * </p>
 * 
 * @author Java实体生成器
 */
public class BPIP_USER_REMOVE extends DBEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected int ID;
	protected java.util.Date MOVETIME;
	protected String USERID;
	protected String BEFOREUNIT;
	protected String AFTERUNIT;
	protected String MOVEREASON;

	/**
	 * 构造函数
	 */
	public BPIP_USER_REMOVE() {
		super();
		initialize();
	}

	/**
	 * 初始化函数
	 */
	protected void initialize() {
		this.dbrow.setTableName("BPIP_USER_REMOVE");
		this.dbrow.addColumn("ID", null, DBType.LONG); // 编号
		this.dbrow.addColumn("MOVETIME", null, DBType.DATE); // 调动时间
		this.dbrow.addColumn("USERID", null, DBType.STRING); // 用户编号
		this.dbrow.addColumn("BEFOREUNIT", null, DBType.STRING); // 调动前单位
		this.dbrow.addColumn("AFTERUNIT", null, DBType.STRING); // 调动后单位
		this.dbrow.addColumn("MOVEREASON", null, DBType.STRING); // 调动原因

		this.dbrow.setPrimaryKey("ID");
	}

	/**
	 * 获取[编号]
	 * 
	 * @return int
	 */
	public int getID() {
		return this.dbrow.Column("ID").getInteger();
	}

	/**
	 * 设置[编号]
	 * 
	 * @param ID
	 *            int
	 */
	public void setID(int ID) {
		this.dbrow.Column("ID").setValue(Integer.toString(ID));
	}

	/**
	 * 获取[调动时间]
	 * 
	 * @return java.util.Date
	 */
	public java.util.Date getMOVETIME() {
		return this.dbrow.Column("MOVETIME").getDate();
	}

	/**
	 * 设置[调动时间]
	 * 
	 * @param MOVETIME
	 *            java.util.Date
	 */
	public void setMOVETIME(java.util.Date MOVETIME) {
		this.dbrow.Column("MOVETIME").setValue(MOVETIME);
	}

	/**
	 * 获取[用户编号]
	 * 
	 * @return String
	 */
	public String getUSERID() {
		return this.getString(this.dbrow.Column("USERID").getString());
	}

	/**
	 * 设置[用户编号]
	 * 
	 * @param USERID
	 *            String
	 */
	public void setUSERID(String USERID) {
		this.dbrow.Column("USERID").setValue(USERID);
	}

	/**
	 * 获取[调动前单位]
	 * 
	 * @return String
	 */
	public String getBEFOREUNIT() {
		return this.getString(this.dbrow.Column("BEFOREUNIT").getString());
	}

	/**
	 * 设置[调动前单位]
	 * 
	 * @param BEFOREUNIT
	 *            String
	 */
	public void setBEFOREUNIT(String BEFOREUNIT) {
		this.dbrow.Column("BEFOREUNIT").setValue(BEFOREUNIT);
	}

	/**
	 * 获取[调动后单位]
	 * 
	 * @return String
	 */
	public String getAFTERUNIT() {
		return this.getString(this.dbrow.Column("AFTERUNIT").getString());
	}

	/**
	 * 设置[调动后单位]
	 * 
	 * @param AFTERUNIT
	 *            String
	 */
	public void setAFTERUNIT(String AFTERUNIT) {
		this.dbrow.Column("AFTERUNIT").setValue(AFTERUNIT);
	}

	/**
	 * 获取[调动原因]
	 * 
	 * @return String
	 */
	public String getMOVEREASON() {
		return this.getString(this.dbrow.Column("MOVEREASON").getString());
	}

	/**
	 * 设置[调动原因]
	 * 
	 * @param MOVEREASON
	 *            String
	 */
	public void setMOVEREASON(String MOVEREASON) {
		this.dbrow.Column("MOVEREASON").setValue(MOVEREASON);
	}

}