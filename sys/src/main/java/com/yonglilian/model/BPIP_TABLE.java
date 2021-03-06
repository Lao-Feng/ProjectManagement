package com.yonglilian.model;

import zr.zrpower.common.db.DBEntity;
import zr.zrpower.common.db.DBType;

/**
 * <p>Title: ZRPower</p>
 * <p>
 * Description: 数据表实体映射包
 * </p>
 * <p>
 * 单元名称：BPIP_TABLE.java
 * </p>
 * <p>
 * 中文解释：表名表数据实体类
 * </p>
 * <p>
 * 作用：将数据库表表名表映射为Java类，作为数据传输的载体。
 * </p>
 * 
 * @author Java实体生成器
 * 
 */
public class BPIP_TABLE extends DBEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected String TABLEID;
	protected String TABLENAME;
	protected String CHINESENAME;
	protected String TABLESPACE;
	protected String DESCRIPTION;
	protected String TABLETYPE;
	protected String PRIMARYKEY;
	protected String ISFLOW;
	protected String OTHERFIELD;

	/**
	 * 构造函数
	 */
	public BPIP_TABLE() {
		super();
		initialize();
	}

	/**
	 * 初始化函数
	 */
	protected void initialize() {
		this.dbrow.setTableName("BPIP_TABLE");
		this.dbrow.addColumn("TABLEID", null, DBType.STRING); // 表编号
		this.dbrow.addColumn("TABLENAME", null, DBType.STRING); // 表名
		this.dbrow.addColumn("CHINESENAME", null, DBType.STRING); // 中文名称
		this.dbrow.addColumn("TABLESPACE", null, DBType.STRING); // 存放表空间
		this.dbrow.addColumn("DESCRIPTION", null, DBType.STRING); // 描述
		this.dbrow.addColumn("TABLETYPE", null, DBType.STRING); // 表类型
		this.dbrow.addColumn("PRIMARYKEY", null, DBType.STRING); // 主键1
		this.dbrow.addColumn("ISFLOW", null, DBType.STRING); //
		this.dbrow.addColumn("OTHERFIELD", null, DBType.STRING); //
		this.dbrow.setPrimaryKey("TABLEID");
	}

	/**
	 * 获取[表编号]
	 * 
	 * @return String
	 */
	public String getTABLEID() {
		return this.getString(this.dbrow.Column("TABLEID").getString());
	}

	/**
	 * 设置[表编号]
	 * 
	 * @param TABLEID
	 *            String
	 */
	public void setTABLEID(String TABLEID) {
		this.dbrow.Column("TABLEID").setValue(TABLEID);
	}

	/**
	 * 获取[表名]
	 * 
	 * @return String
	 */
	public String getTABLENAME() {
		return this.getString(this.dbrow.Column("TABLENAME").getString());
	}

	/**
	 * 设置[表名]
	 * 
	 * @param TABLENAME
	 *            String
	 */
	public void setTABLENAME(String TABLENAME) {
		this.dbrow.Column("TABLENAME").setValue(TABLENAME);
	}

	/**
	 * 获取[中文名称]
	 * 
	 * @return String
	 */
	public String getCHINESENAME() {
		return this.getString(this.dbrow.Column("CHINESENAME").getString());
	}

	/**
	 * 设置[中文名称]
	 * 
	 * @param CHINESENAME
	 *            String
	 */
	public void setCHINESENAME(String CHINESENAME) {
		this.dbrow.Column("CHINESENAME").setValue(CHINESENAME);
	}

	/**
	 * 获取[存放表空间]
	 * 
	 * @return String
	 */
	public String getTABLESPACE() {
		return this.getString(this.dbrow.Column("TABLESPACE").getString());
	}

	/**
	 * 设置[存放表空间]
	 * 
	 * @param TABLESPACE
	 *            String
	 */
	public void setTABLESPACE(String TABLESPACE) {
		this.dbrow.Column("TABLESPACE").setValue(TABLESPACE);
	}

	/**
	 * 获取[描述]
	 * 
	 * @return String
	 */
	public String getDESCRIPTION() {
		return this.getString(this.dbrow.Column("DESCRIPTION").getString());
	}

	/**
	 * 设置[描述]
	 * 
	 * @param DESCRIPTION
	 *            String
	 */
	public void setDESCRIPTION(String DESCRIPTION) {
		this.dbrow.Column("DESCRIPTION").setValue(DESCRIPTION);
	}

	/**
	 * 获取[表类型]
	 * 
	 * @return String
	 */
	public String getTABLETYPE() {
		return this.getString(this.dbrow.Column("TABLETYPE").getString());
	}

	/**
	 * 设置[表类型]
	 * 
	 * @param TABLETYPE
	 *            String
	 */
	public void setTABLETYPE(String TABLETYPE) {
		this.dbrow.Column("TABLETYPE").setValue(TABLETYPE);
	}

	/**
	 * 获取[主键1]
	 * 
	 * @return String
	 */
	public String getPRIMARYKEY() {
		return this.getString(this.dbrow.Column("PRIMARYKEY").getString());
	}

	/**
	 * 设置[主键1]
	 * 
	 * @param PRIMARYKEY
	 *            String
	 */
	public void setPRIMARYKEY(String PRIMARYKEY) {
		this.dbrow.Column("PRIMARYKEY").setValue(PRIMARYKEY);
	}

	/**
	 * 获取[]
	 * 
	 * @return String
	 */
	public String getISFLOW() {
		return this.getString(this.dbrow.Column("ISFLOW").getString());
	}

	/**
	 * 设置[]
	 * 
	 * @param ISFLOW
	 *            String
	 */
	public void setISFLOW(String ISFLOW) {
		this.dbrow.Column("ISFLOW").setValue(ISFLOW);
	}

	/**
	 * 获取[]
	 * 
	 * @return String
	 */
	public String getOTHERFIELD() {
		return this.getString(this.dbrow.Column("OTHERFIELD").getString());
	}

	/**
	 * 设置[]
	 * 
	 * @param OTHERFIELD
	 *            String
	 */
	public void setOTHERFIELD(String OTHERFIELD) {
		this.dbrow.Column("OTHERFIELD").setValue(OTHERFIELD);
	}

}