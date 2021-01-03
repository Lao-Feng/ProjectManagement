package com.yonglilian.flowengine.mode.config;

import zr.zrpower.common.db.DBEntity;
import zr.zrpower.common.db.DBType;

/**
 * <p>
 * Title: ZRPOWER平台
 * </p>
 * <p>
 * Description: 数据表实体映射包
 * </p>
 * <p>
 * 单元名称：FLOW_CONFIG_ACTIVITY_BUTTON.java
 * </p>
 * <p>
 * 中文解释：活动按钮关系表数据实体类
 * </p>
 * <p>
 * 作用：将数据库表活动按钮关系表映射为Java类，作为数据传输的载体。
 * </p>
 * 
 * @author Java实体生成器 By NFZR
 * 
 */
public class FLOW_CONFIG_ACTIVITY_BUTTON extends DBEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected String ID;
	protected String FID;
	protected String BUTTONID;
	protected String BWHERE;

	/**
	 * 构造函数
	 */
	public FLOW_CONFIG_ACTIVITY_BUTTON() {
		super();
		initialize();
	}

	/**
	 * 初始化函数
	 */
	protected void initialize() {
		this.dbrow.setTableName("FLOW_CONFIG_ACTIVITY_BUTTON");
		this.dbrow.addColumn("ID", null, DBType.STRING); // ID
		this.dbrow.addColumn("FID", null, DBType.STRING); // 活动ID
		this.dbrow.addColumn("BUTTONID", null, DBType.STRING); // 按钮ID
		this.dbrow.addColumn("BWHERE", null, DBType.STRING); // 按钮条件
		this.dbrow.setPrimaryKey("ID");
	}

	/**
	 * 获取[ID]
	 * 
	 * @return String
	 */
	public String getID() {
		return this.getString(this.dbrow.Column("ID").getString());
	}

	/**
	 * 设置[ID]
	 * 
	 * @param ID
	 *            String
	 */
	public void setID(String ID) {
		this.dbrow.Column("ID").setValue(ID);
	}

	/**
	 * 获取[活动ID]
	 * 
	 * @return String
	 */
	public String getFID() {
		return this.getString(this.dbrow.Column("FID").getString());
	}

	/**
	 * 设置[活动ID]
	 * 
	 * @param FID
	 *            String
	 */
	public void setFID(String FID) {
		this.dbrow.Column("FID").setValue(FID);
	}

	/**
	 * 获取[按钮ID]
	 * 
	 * @return String
	 */
	public String getBUTTONID() {
		return this.getString(this.dbrow.Column("BUTTONID").getString());
	}

	/**
	 * 设置[按钮ID]
	 * 
	 * @param BUTTONID
	 *            String
	 */
	public void setBUTTONID(String BUTTONID) {
		this.dbrow.Column("BUTTONID").setValue(BUTTONID);
	}

	/**
	 * 获取[按钮条件]
	 * 
	 * @return String
	 */
	public String getBWHERE() {
		return this.getString(this.dbrow.Column("BWHERE").getString());
	}

	/**
	 * 设置[按钮条件]
	 * 
	 * @param BWHERE
	 *            String
	 */
	public void setBWHERE(String BWHERE) {
		this.dbrow.Column("BWHERE").setValue(BWHERE);
	}

}