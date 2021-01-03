package com.yonglilian.flowengine.mode.base;

import zr.zrpower.common.db.DBEntity;
import zr.zrpower.common.db.DBType;

/**
 * <p>Title: ZRPower</p>
 * <p>
 * Description: 数据表实体映射包
 * </p>
 * <p>
 * 单元名称：BPIP_UNIT.java
 * </p>
 * <p>
 * 中文解释：单位表数据实体类
 * </p>
 * <p>
 * 作用：将数据库表单位表映射为Java类，作为数据传输的载体。
 * </p>
 * 
 * @author Java实体生成器
 * 
 */
public class BPIP_UNIT extends DBEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 构造函数
	 */
	public BPIP_UNIT() {
		super();
		initialize();
	}

	/**
	 * 初始化函数
	 */
	protected void initialize() {
		this.dbrow.setTableName("BPIP_UNIT");
		this.dbrow.addColumn("UNITID", null, DBType.STRING); // 单位编号
		this.dbrow.addColumn("UNITNAME", null, DBType.STRING); // 单位名称
		this.dbrow.addColumn("UNITTYPE", null, DBType.STRING); // 单位类型
		this.dbrow.addColumn("STATE", null, DBType.STRING); // 单位状态
		this.dbrow.setPrimaryKey("UNITID");
	}

	/**
	 * 获取[单位编号]
	 * 
	 * @return String
	 */
	public String getUNITID() {
		return this.getString(this.dbrow.Column("UNITID").getString());
	}

	/**
	 * 设置[单位编号]
	 * 
	 * @param UNITID
	 *            String
	 */
	public void setUNITID(String UNITID) {
		this.dbrow.Column("UNITID").setValue(UNITID);
	}

	/**
	 * 获取[单位名称]
	 * 
	 * @return String
	 */
	public String getUNITNAME() {
		return this.getString(this.dbrow.Column("UNITNAME").getString());
	}

	/**
	 * 设置[单位名称]
	 * 
	 * @param UNITNAME
	 *            String
	 */
	public void setUNITNAME(String UNITNAME) {
		this.dbrow.Column("UNITNAME").setValue(UNITNAME);
	}

	/**
	 * 获取[单位类型]
	 * 
	 * @return String
	 */
	public String getUNITTYPE() {
		return this.getString(this.dbrow.Column("UNITTYPE").getString());
	}

	/**
	 * 设置[单位类型]
	 * 
	 * @param UNITTYPE
	 *            String
	 */
	public void setUNITTYPE(String UNITTYPE) {
		this.dbrow.Column("UNITTYPE").setValue(UNITTYPE);
	}

	/**
	 * 获取[单位状态]
	 * 
	 * @return String
	 */
	public String getSTATE() {
		return this.getString(this.dbrow.Column("STATE").getString());
	}

	/**
	 * 设置[单位状态]
	 * 
	 * @param STATE
	 *            String
	 */
	public void setSTATE(String STATE) {
		this.dbrow.Column("STATE").setValue(STATE);
	}

}