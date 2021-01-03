package com.yonglilian.queryengine.mode;

import zr.zrpower.common.db.DBEntity;
import zr.zrpower.common.db.DBType;

/**
 * <p>Title: ZRPower</p>
 * <p>
 * Description: 数据表实体映射包
 * </p>
 * <p>
 * 单元名称：QUERY_CONFIG_SHOWFIELD.java
 * </p>
 * <p>
 * 中文解释：查询显示结果配置表数据实体类
 * </p>
 * <p>
 * 作用：将数据库表查询显示结果配置表映射为Java类，作为数据传输的载体。
 * </p>
 * 
 * @author Java实体生成器 By NFZR
 */
public class QUERY_CONFIG_SHOWFIELD extends DBEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected String ID;
	protected String FID;
	protected String FIELD;
	protected String ISSHOW;
	protected String COLWIDTH;
	protected String ISNUMBER;
	protected String QALIGN;

	/**
	 * 构造函数
	 */
	public QUERY_CONFIG_SHOWFIELD() {
		super();
		initialize();
	}

	/**
	 * 初始化函数
	 */
	protected void initialize() {
		this.dbrow.setTableName("QUERY_CONFIG_SHOWFIELD");
		this.dbrow.addColumn("ID", null, DBType.STRING); // 人员编号
		this.dbrow.addColumn("FID", null, DBType.STRING); // 配置表ID
		this.dbrow.addColumn("FIELD", null, DBType.STRING); // 字段
		this.dbrow.addColumn("ISSHOW", null, DBType.STRING); // 是否显示
		this.dbrow.addColumn("COLWIDTH", null, DBType.STRING); // 显示列宽
		this.dbrow.addColumn("ISNUMBER", null, DBType.STRING); // 导出excel时是否转化为数字(1是0否)
		this.dbrow.addColumn("QALIGN", null, DBType.STRING); // 对齐方式 0 左对齐 1 居中 2 右对齐
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
	 * 获取[字段]
	 * 
	 * @return String
	 */
	public String getFIELD() {
		return this.getString(this.dbrow.Column("FIELD").getString());
	}

	/**
	 * 设置[字段]
	 * 
	 * @param FIELD
	 *            String
	 */
	public void setFIELD(String FIELD) {
		this.dbrow.Column("FIELD").setValue(FIELD);
	}

	/**
	 * 获取[是否显示]
	 * 
	 * @return String
	 */
	public String getISSHOW() {
		return this.getString(this.dbrow.Column("ISSHOW").getString());
	}

	/**
	 * 设置[是否显示]
	 * 
	 * @param ISSHOW
	 *            String
	 */
	public void setISSHOW(String ISSHOW) {
		this.dbrow.Column("ISSHOW").setValue(ISSHOW);
	}

	/**
	 * 获取[显示列宽]
	 * 
	 * @return String
	 */
	public String getCOLWIDTH() {
		return this.getString(this.dbrow.Column("COLWIDTH").getString());
	}

	/**
	 * 设置[显示列宽]
	 * 
	 * @param COLWIDTH
	 *            String
	 */
	public void setCOLWIDTH(String COLWIDTH) {
		this.dbrow.Column("COLWIDTH").setValue(COLWIDTH);
	}

	/**
	 * 获取[导出excel时是否转化为数字]
	 * 
	 * @return String
	 */
	public String getISNUMBER() {
		return this.getString(this.dbrow.Column("ISNUMBER").getString());
	}

	/**
	 * 设置[导出excel时是否转化为数字]
	 * 
	 * @param ISNUMBER
	 *            String
	 */
	public void setISNUMBER(String ISNUMBER) {
		this.dbrow.Column("ISNUMBER").setValue(ISNUMBER);
	}

	public String getQALIGN() {
		return this.getString(this.dbrow.Column("QALIGN").getString());
	}

	public void setQALIGN(String QALIGN) {
		this.dbrow.Column("QALIGN").setValue(QALIGN);
	}

}