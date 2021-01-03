package com.yonglilian.flowengine.mode.base;

import zr.zrpower.common.db.DBEntity;
import zr.zrpower.common.db.DBType;

/**
 *
 * <p>
 * Title: ZRPower
 * </p>
 *
 * <p>
 * Description: 数据表实体映射包
 * </p>
 *
 * <p>
 * 单元名称：FLOW_BASE_GROUP.java
 * </p>
 *
 * <p>
 * 中文解释：流程组表数据实体类
 * </p>
 *
 * <p>
 * 作用：将数据库表流程组表映射为Java类，作为数据传输的载体。
 * </p>
 *
 *
 * @author Java实体生成器 By NFZR
 *
 *
 */
public class Group extends DBEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 构造函数
	 */
	public Group() {
		super();
		initialize();
	}

	/**
	 * 初始化函数
	 */
	protected void initialize() {
		this.dbrow.setTableName("FLOWGROUP");
		this.dbrow.addColumn("ID", null, DBType.STRING); // 流程组ID
		this.dbrow.addColumn("NAME", null, DBType.STRING); // 流程组名称
		this.dbrow.addColumn("DESC1", null, DBType.STRING); // 流程组描述
		this.dbrow.setPrimaryKey("ID");
	}

	/**
	 * 获取[流程组ID]
	 * 
	 * @return String
	 */
	public String getID() {
		return this.getString(this.dbrow.Column("ID").getString());
	}

	/**
	 * 设置[流程组ID]
	 * 
	 * @param ID
	 *            String
	 */
	public void setID(String ID) {
		this.dbrow.Column("ID").setValue(ID);
	}

	/**
	 * 获取[流程组名称]
	 * 
	 * @return String
	 */
	public String getNAME() {
		return this.getString(this.dbrow.Column("NAME").getString());
	}

	/**
	 * 设置[流程组名称]
	 * 
	 * @param NAME
	 *            String
	 */
	public void setNAME(String NAME) {
		this.dbrow.Column("NAME").setValue(NAME);
	}

	/**
	 * 获取[流程组描述]
	 * 
	 * @return String
	 */
	public String getDESC1() {
		return this.getString(this.dbrow.Column("DESC1").getString());
	}

	/**
	 * 设置[流程组描述]
	 * 
	 * @param DESC1
	 *            String
	 */
	public void setDESC1(String DESC1) {
		this.dbrow.Column("DESC1").setValue(DESC1);
	}

}
