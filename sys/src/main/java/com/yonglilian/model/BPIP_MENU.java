package com.yonglilian.model;

import zr.zrpower.common.db.DBEntity;
import zr.zrpower.common.db.DBType;

/**
 * <p>
 * Title: ZRPower
 * </p>
 * <p>
 * Description: 数据表实体映射包
 * </p>
 * <p>
 * 单元名称：BPIP_MENU.java
 * </p>
 * <p>
 * 中文解释：系统菜单表数据实体类
 * </p>
 * <p>
 * 作用：将数据库表系统菜单表映射为Java类，作为数据传输的载体。
 * </p>
 * 
 * @author Java实体生成器
 * 
 */
public class BPIP_MENU extends DBEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected String MENUNO;
	protected String MENUNAME;
	protected String ISPOWER;
	protected String URL;
	protected String ISOPEN;
	protected String FLAG;
	protected String MNCODE;
	protected String PERMS;//授权标识

	protected String parentId;// 上一级菜单Id
	protected String ICON;// 图标

	/**
	 * 构造函数
	 */
	public BPIP_MENU() {
		super();
		initialize();
	}

	/**
	 * 初始化函数
	 */
	protected void initialize() {
		this.dbrow.setTableName("BPIP_MENU");
		this.dbrow.addColumn("MENUNO", null, DBType.STRING); // 菜单编号
		this.dbrow.addColumn("MENUNAME", null, DBType.STRING); // 菜单名称
		this.dbrow.addColumn("ISPOWER", null, DBType.STRING); // 任务类型
		this.dbrow.addColumn("URL", null, DBType.STRING); // 链接地址
		this.dbrow.addColumn("ISOPEN", null, DBType.STRING); // 是否新打开窗口
		this.dbrow.addColumn("FLAG", null, DBType.STRING); // 状态
		this.dbrow.addColumn("MNCODE", null, DBType.STRING); // 排序号
		this.dbrow.addColumn("ICON", null, DBType.STRING); // ICON
		this.dbrow.addColumn("PERMS", null, DBType.STRING); // 授权标识
		this.dbrow.setPrimaryKey("MENUNO");
	}

	/**
	 * 获取[菜单编号]
	 * 
	 * @return String
	 */
	public String getMENUNO() {
		return this.getString(this.dbrow.Column("MENUNO").getString());
	}

	/**
	 * 设置[菜单编号]
	 * 
	 * @param MENUNO
	 *            String
	 */
	public void setMENUNO(String MENUNO) {
		this.dbrow.Column("MENUNO").setValue(MENUNO);
		this.MENUNO = MENUNO;
	}

	/**
	 * 获取[菜单名称]
	 * 
	 * @return String
	 */
	public String getMENUNAME() {
		return this.getString(this.dbrow.Column("MENUNAME").getString());
	}

	/**
	 * 设置[菜单名称]
	 * 
	 * @param MENUNAME
	 *            String
	 */
	public void setMENUNAME(String MENUNAME) {
		this.dbrow.Column("MENUNAME").setValue(MENUNAME);
		this.MENUNAME = MENUNAME;
	}

	/**
	 * 获取[任务类型]
	 * 
	 * @return String
	 */
	public String getISPOWER() {
		return this.getString(this.dbrow.Column("ISPOWER").getString());
	}

	/**
	 * 设置[任务类型]
	 * 
	 * @param ISPOWER
	 *            String
	 */
	public void setISPOWER(String ISPOWER) {
		this.dbrow.Column("ISPOWER").setValue(ISPOWER);
		this.ISPOWER = ISPOWER;
	}

	/**
	 * 获取[链接地址]
	 * 
	 * @return String
	 */
	public String getURL() {
		return this.getString(this.dbrow.Column("URL").getString());
	}

	/**
	 * 设置[链接地址]
	 * 
	 * @param URL
	 *            String
	 */
	public void setURL(String URL) {
		this.dbrow.Column("URL").setValue(URL);
		this.URL = URL;
	}

	/**
	 * 获取[是否新打开窗口]
	 * 
	 * @return String
	 */
	public String getISOPEN() {
		return this.getString(this.dbrow.Column("ISOPEN").getString());
	}

	/**
	 * 设置[是否新打开窗口]
	 * 
	 * @param ISOPEN
	 *            String
	 */
	public void setISOPEN(String ISOPEN) {
		this.dbrow.Column("ISOPEN").setValue(ISOPEN);
		this.ISOPEN = ISOPEN;
	}

	/**
	 * 获取[状态]
	 * 
	 * @return String
	 */
	public String getFLAG() {
		return this.getString(this.dbrow.Column("FLAG").getString());
	}

	/**
	 * 设置[状态]
	 * 
	 * @param FLAG
	 *            String
	 */
	public void setFLAG(String FLAG) {
		this.dbrow.Column("FLAG").setValue(FLAG);
		this.FLAG = FLAG;
	}

	// 排序号
	public String getMNCODE() {
		return this.getString(this.dbrow.Column("MNCODE").getString());
	}

	public void setMNCODE(String MNCODE) {
		this.dbrow.Column("MNCODE").setValue(MNCODE);
		this.MNCODE = MNCODE;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getICON() {
		return ICON;
	}

	public void setICON(String ICON) {
		this.ICON = ICON;
	}

	public String getPERMS() {
		return PERMS;
	}

	public void setPERMS(String pERMS) {
		PERMS = pERMS;
	}
	
	

}