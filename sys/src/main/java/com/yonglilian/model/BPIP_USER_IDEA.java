package com.yonglilian.model;

import zr.zrpower.common.db.DBEntity;
import zr.zrpower.common.db.DBType;

/**
 * <p>Title: ZRPower</p>
 * <p>
 * Description: 数据表实体映射包
 * </p>
 * <p>
 * 单元名称：BPIP_USER_IDEA.java
 * </p>
 * <p>
 * 中文解释：意见设置表数据实体类
 * </p>
 * <p>
 * 作用：将数据库表意见设置表映射为Java类，作为数据传输的载体。
 * </p>
 * 
 * @author Java实体生成器
 */
public class BPIP_USER_IDEA extends DBEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected String ID;
	protected String USER_NO;
	protected String CONTENT;

	/**
	 * 构造函数
	 */
	public BPIP_USER_IDEA() {
		super();
		initialize();
	}

	/**
	 * 初始化函数
	 */
	protected void initialize() {
		this.dbrow.setTableName("BPIP_USER_IDEA");
		this.dbrow.addColumn("ID", null, DBType.STRING); // 编号
		this.dbrow.addColumn("USER_NO", null, DBType.STRING); // 用户编号
		this.dbrow.addColumn("CONTENT", null, DBType.STRING); // 意见内容
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
	 * 获取[用户编号]
	 * 
	 * @return String
	 */
	public String getUSER_NO() {
		return this.getString(this.dbrow.Column("USER_NO").getString());
	}

	/**
	 * 设置[用户编号]
	 * 
	 * @param USER_NO
	 *            String
	 */
	public void setUSER_NO(String USER_NO) {
		this.dbrow.Column("USER_NO").setValue(USER_NO);
	}

	/**
	 * 获取[意见内容]
	 * 
	 * @return String
	 */
	public String getCONTENT() {
		return this.getString(this.dbrow.Column("CONTENT").getString());
	}

	/**
	 * 设置[意见内容]
	 * 
	 * @param CONTENT
	 *            String
	 */
	public void setCONTENT(String CONTENT) {
		this.dbrow.Column("CONTENT").setValue(CONTENT);
	}

}