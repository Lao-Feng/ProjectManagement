package com.yonglilian.model;

import zr.zrpower.common.db.DBEntity;
import zr.zrpower.common.db.DBType;

/**
 * <p>Title: ZRPower</p>
 * <p>
 * Description: 数据表实体映射包
 * </p>
 * <p>
 * 单元名称：BPIP_USER.java
 * </p>
 * <p>
 * 中文解释：用户表数据实体类
 * </p>
 * <p>
 * 作用：将数据库表用户表映射为Java类，作为数据传输的载体。
 * </p>
 * 
 * @author Java实体生成器
 */
public class SessionUser extends DBEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected String LoginID;
	protected String Name;
	protected String UserID;
	protected String LCODE;
	protected String UnitName;
	protected String UnitID;
	protected String Face;
	protected String UserCSS;
	protected String UserImage;
	protected String UserIndexColor;
	protected int UserPageSize;
	protected String WINDOWTYPE;
	protected String UserFileID;
	protected String WebEditCss;
	protected String Custom1;
	protected String Custom2;
	protected String Custom3;
	protected String Custom4;
	protected String Custom5;

	/**
	 * 构造函数
	 */
	public SessionUser() {
		super();
		initialize();
	}

	/**
	 * 初始化函数
	 */
	protected void initialize() {
		this.dbrow.setTableName("PIP_USER_SESSION");
		this.dbrow.addColumn("LoginID", null, DBType.STRING); // 登录名
		this.dbrow.addColumn("Name", null, DBType.STRING); // 姓名
		this.dbrow.addColumn("UserID", null, DBType.STRING); // 用户编号
		this.dbrow.addColumn("LCODE", null, DBType.STRING); // 用户内部编号

		this.dbrow.addColumn("UnitName", null, DBType.STRING); // 所在单位名称
		this.dbrow.addColumn("UnitID", null, DBType.STRING); // 所在单位编号
		this.dbrow.addColumn("Face", null, DBType.STRING); // 头像

		this.dbrow.addColumn("UserCSS", null, DBType.STRING); // 用户的css样式
		this.dbrow.addColumn("UserImage", null, DBType.STRING); // 用户的图片(肤色)文件夹
		this.dbrow.addColumn("UserIndexColor", null, DBType.STRING); // 用户的框架边框颜色
		this.dbrow.addColumn("UserPageSize", null, DBType.LONG); // 用户每页显示的记录数
		this.dbrow.addColumn("WINDOWTYPE", null, DBType.STRING); // 用户的界面样式
		this.dbrow.addColumn("UserFileID", null, DBType.STRING); // 用户附件ID
		this.dbrow.addColumn("WebEditCss", null, DBType.STRING); // 用户在线编辑器的样式

		this.dbrow.addColumn("Custom1", null, DBType.STRING); // 自定义1
		this.dbrow.addColumn("Custom2", null, DBType.STRING); // 自定义2
		this.dbrow.addColumn("Custom3", null, DBType.STRING); // 自定义3
		this.dbrow.addColumn("Custom4", null, DBType.STRING); // 自定义4
		this.dbrow.addColumn("Custom5", null, DBType.STRING); // 自定义5
	}

	// ======================================================
	// [用户编号]属性
	// ======================================================
	public String getUserID() {
		return this.dbrow.Column("UserID").getString();
	}

	public void setUserID(String UserID) {
		this.dbrow.Column("UserID").setValue(UserID);
		this.UserID = UserID;
	}

	// ======================================================
	// [登录名]属性
	// ======================================================
	public String getLoginID() {
		return this.dbrow.Column("LoginID").getString();
	}

	public void setLoginID(String LoginID) {
		this.dbrow.Column("LoginID").setValue(LoginID);
		this.LoginID = LoginID;
	}

	// ======================================================
	// [姓名]属性
	// ======================================================
	public String getName() {
		return this.dbrow.Column("Name").getString();
	}

	public void setName(String Name) {
		this.dbrow.Column("Name").setValue(Name);
		this.Name = Name;
	}

	// ======================================================
	// [所在单位名称]属性
	// ======================================================
	public String getUnitName() {
		return this.dbrow.Column("UnitName").getString();
	}

	public void setUnitName(String UnitName) {
		this.dbrow.Column("UnitName").setValue(UnitName);
		this.UnitName = UnitName;
	}

	// ======================================================
	// [所在单位编号]属性
	// ======================================================
	public String getUnitID() {
		return this.dbrow.Column("UnitID").getString();
	}

	public void setUnitID(String UnitID) {
		this.dbrow.Column("UnitID").setValue(UnitID);
		this.UnitID = UnitID;
	}

	// ======================================================
	// [用户内部编号]属性
	// ======================================================
	public String getLCODE() {
		return this.dbrow.Column("LCODE").getString();
	}

	public void setLCODE(String LCODE) {
		this.dbrow.Column("LCODE").setValue(LCODE);
		this.LCODE = LCODE;
	}

	// ======================================================
	// [用户头像]属性
	// ======================================================
	public String getFace() {
		return this.dbrow.Column("Face").getString();
	}

	public void setFace(String Face) {
		this.dbrow.Column("Face").setValue(Face);
		this.Face = Face;
	}

	// ======================================================
	// [用户的css样式]属性
	// ======================================================
	public String getUserCSS() {
		return this.dbrow.Column("UserCSS").getString();
	}

	public void setUserCSS(String UserCSS) {
		this.dbrow.Column("UserCSS").setValue(UserCSS);
		this.UserCSS = UserCSS;
	}

	// ======================================================
	// [用户的图片(肤色)文件夹]属性
	// ======================================================
	public String getUserImage() {
		return this.dbrow.Column("UserImage").getString();
	}

	public void setUserImage(String UserImage) {
		this.dbrow.Column("UserImage").setValue(UserImage);
		this.UserImage = UserImage;
	}

	// ======================================================
	// [用户的框架边框颜色]属性
	// ======================================================
	public String getUserIndexColor() {
		return this.dbrow.Column("UserIndexColor").getString();
	}

	public void setUserIndexColor(String UserIndexColor) {
		this.dbrow.Column("UserIndexColor").setValue(UserIndexColor);
		this.UserIndexColor = UserIndexColor;
	}

	// ======================================================
	// [用户每页显示的记录数]属性
	// ======================================================
	public int getUserPageSize() {
		return this.dbrow.Column("UserPageSize").getInteger();
	}

	public void setUserPageSize(int UserPageSize) {
		this.dbrow.Column("UserPageSize").setValue(Integer.toString(UserPageSize));
		this.UserPageSize = UserPageSize;
	}

	// ======================================================
	// [用户界面样式]属性
	// ======================================================
	public String getUserWINDOWTYPE() {
		return this.dbrow.Column("WINDOWTYPE").getString();

	}

	public void setUserWINDOWTYPE(String UserWINDOWTYPE) {
		this.dbrow.Column("WINDOWTYPE").setValue(UserWINDOWTYPE);
		this.WINDOWTYPE = UserWINDOWTYPE;
	}

	// ======================================================
	// [用户的文件ID]属性
	// ======================================================
	public String getUserFileID() {
		return this.dbrow.Column("UserFileID").getString();
	}

	public void setUserFileID(String UserFileID) {
		this.dbrow.Column("UserFileID").setValue(UserFileID);
		this.UserFileID = UserFileID;
	}

	// ======================================================
	// [用户的文件ID]属性
	// ======================================================
	public String getWebEditCss() {
		return this.dbrow.Column("WebEditCss").getString();
	}

	public void setWebEditCss(String WebEditCss) {
		this.dbrow.Column("WebEditCss").setValue(WebEditCss);
		this.WebEditCss = WebEditCss;
	}

	// ======================================================
	// [自定义参数1]属性
	// ======================================================
	public String getCustom1() {
		return this.dbrow.Column("Custom1").getString();
	}

	public void setCustom1(String Custom1) {
		this.dbrow.Column("Custom1").setValue(Custom1);
		this.Custom1 = Custom1;
	}

	// ======================================================
	// [自定义参数2]属性
	// ======================================================
	public String getCustom2() {
		return this.dbrow.Column("Custom2").getString();
	}

	public void setCustom2(String Custom2) {
		this.dbrow.Column("Custom2").setValue(Custom2);
		this.Custom2 = Custom2;
	}

	// ======================================================
	// [自定义参数3]属性
	// ======================================================
	public String getCustom3() {
		return this.dbrow.Column("Custom3").getString();
	}

	public void setCustom3(String Custom3) {
		this.dbrow.Column("Custom3").setValue(Custom3);
		this.Custom3 = Custom3;
	}

	// ======================================================
	// [自定义参数4]属性
	// ======================================================
	public String getCustom4() {
		return this.dbrow.Column("Custom4").getString();
	}

	public void setCustom4(String Custom4) {
		this.dbrow.Column("Custom4").setValue(Custom4);
		this.Custom4 = Custom4;
	}

	// ======================================================
	// [自定义参数5]属性
	// ======================================================
	public String getCustom5() {
		return this.dbrow.Column("Custom5").getString();
	}

	public void setCustom5(String Custom5) {
		this.dbrow.Column("Custom5").setValue(Custom5);
		this.Custom5 = Custom5;
	}

}
