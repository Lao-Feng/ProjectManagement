package com.yonglilian.model;

import zr.zrpower.common.db.DBEntity;
import zr.zrpower.common.db.DBType;

/**
 * <p>Title: ZRPower</p>
 * <p>
 * Description: 数据表实体映射包
 * </p>
 * <p>
 * 单元名称：CODE_YWXT.java
 * </p>
 * <p>
 * 中文解释：单位类型代码表数据实体类
 * </p>
 * <p>
 * 作用：将数据库表单位类型代码表映射为Java类，作为数据传输的载体。
 * </p>
 * 
 * @author Java实体生成器
 */
public class CODE_YWXT extends DBEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected String CODE;
	protected String NAME;
	protected String SPELL;
	protected String ISSHOW;
	
	//业务操作
	protected String table;//数据库字典表名称

	/**
	 * 构造函数
	 */
	public CODE_YWXT() {
		super();
		initialize();
	}

	/**
	 * 初始化函数
	 */
	protected void initialize() {
		this.dbrow.setTableName("CODE_YWXT");
		this.dbrow.addColumn("CODE", null, DBType.STRING); // 单位类型代码
		this.dbrow.addColumn("NAME", null, DBType.STRING); // 单位类型名称
		this.dbrow.addColumn("SPELL", null, DBType.STRING); // 拼音字头
		this.dbrow.addColumn("ISSHOW", null, DBType.STRING); // 是否禁用
		this.dbrow.setPrimaryKey("CODE");
	}

	/**
	 * 获取[单位类型代码]
	 * 
	 * @return String
	 */
	public String getCODE() {
		return this.getString(this.dbrow.Column("CODE").getString());
	}

	/**
	 * 设置[单位类型代码]
	 * 
	 * @param CODE
	 *            String
	 */
	public void setCODE(String CODE) {
		this.dbrow.Column("CODE").setValue(CODE);
	}

	/**
	 * 获取[单位类型名称]
	 * 
	 * @return String
	 */
	public String getNAME() {
		return this.getString(this.dbrow.Column("NAME").getString());
	}

	/**
	 * 设置[单位类型名称]
	 * 
	 * @param NAME
	 *            String
	 */
	public void setNAME(String NAME) {
		this.dbrow.Column("NAME").setValue(NAME);
	}

	/**
	 * 获取[拼音字头]
	 * 
	 * @return String
	 */
	public String getSPELL() {
		return this.getString(this.dbrow.Column("SPELL").getString());
	}

	/**
	 * 设置[拼音字头]
	 * 
	 * @param SPELL
	 *            String
	 */
	public void setSPELL(String SPELL) {
		this.dbrow.Column("SPELL").setValue(SPELL);
	}

	/**
	 * 获取[是否禁用]
	 * 
	 * @return String
	 */
	public String getISSHOW() {
		return this.getString(this.dbrow.Column("ISSHOW").getString());
	}

	/**
	 * 设置[是否禁用]
	 * 
	 * @param ISSHOW
	 *            String
	 */
	public void setISSHOW(String ISSHOW) {
		this.dbrow.Column("ISSHOW").setValue(ISSHOW);
	}

	public String getTable() {
		return table;
	}

	public void setTable(String table) {
		this.table = table;
	}
	
	
}