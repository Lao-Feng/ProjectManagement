package zr.zrpower.model;

import zr.zrpower.common.db.DBEntity;
import zr.zrpower.common.db.DBType;

import java.util.Set;

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
public class BPIP_USER extends DBEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected String USERID;
	protected String LOGINID;
	protected String LOGINPW;
	protected String USERSTATE;
	protected String NAME;
	protected String LCODE;
	protected String UNITID;
	protected String SEX;
	protected java.util.Date BIRTHDAY;
	protected String PHONE;
	protected String MOBILE;
	protected String EMAIL;
	protected String USERIMAGE;
	protected int LOGINTIME;
	protected int PAGESIZE;
	protected String WINDOWTYPE;
	protected String DEPTINSIDE;
	protected String ORBERCODE;
	protected java.util.Date ONLINEDATE;
	
	protected Set<String> permsSet;//用户权限标识

	/**
	 * 构造函数
	 */
	public BPIP_USER() {
		super();
		initialize();
	}

	/**
	 * 初始化函数
	 */
	protected void initialize() {
		this.dbrow.setTableName("BPIP_USER");
		this.dbrow.addColumn("USERID", null, DBType.STRING); // 用户编号
		this.dbrow.addColumn("LOGINID", null, DBType.STRING); // 登录名称
		this.dbrow.addColumn("LOGINPW", null, DBType.STRING); // 登录口令
		this.dbrow.addColumn("USERSTATE", null, DBType.STRING); // 用户状态
		this.dbrow.addColumn("NAME", null, DBType.STRING); // 真实姓名
		this.dbrow.addColumn("LCODE", null, DBType.STRING); // 内部编号
		this.dbrow.addColumn("UNITID", null, DBType.STRING); // 所在单位
		this.dbrow.addColumn("SEX", null, DBType.STRING); // 性别
		this.dbrow.addColumn("BIRTHDAY", null, DBType.DATE); // 出生日期
		this.dbrow.addColumn("PHONE", null, DBType.STRING); // 办公电话
		this.dbrow.addColumn("MOBILE", null, DBType.STRING); // 手机
		this.dbrow.addColumn("EMAIL", null, DBType.STRING); // 电子邮件
		this.dbrow.addColumn("USERIMAGE", null, DBType.STRING); // 头像
		this.dbrow.addColumn("LOGINTIME", null, DBType.LONG); // 登录次数

		this.dbrow.addColumn("PAGESIZE", null, DBType.LONG); // 表单每页显示的记录数
		this.dbrow.addColumn("WINDOWTYPE", null, DBType.STRING); // 界面样式

		this.dbrow.addColumn("DEPTINSIDE", null, DBType.STRING); // 部门内勤
		this.dbrow.addColumn("ORBERCODE", null, DBType.STRING); // 排序号
		this.dbrow.addColumn("ONLINEDATE", null, DBType.DATE); // 在线更新时间

		this.dbrow.setPrimaryKey("USERID");
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
		this.USERID = USERID;
	}

	/**
	 * 获取[登录名称]
	 * 
	 * @return String
	 */
	public String getLOGINID() {
		return this.getString(this.dbrow.Column("LOGINID").getString());
	}

	/**
	 * 设置[登录名称]
	 * 
	 * @param LOGINID
	 *            String
	 */
	public void setLOGINID(String LOGINID) {
		this.dbrow.Column("LOGINID").setValue(LOGINID);
		this.LOGINID = LOGINID;
	}

	/**
	 * 获取[登录口令]
	 * 
	 * @return String
	 */
	public String getLOGINPW() {
		return this.getString(this.dbrow.Column("LOGINPW").getString());
	}

	/**
	 * 设置[登录口令]
	 * 
	 * @param LOGINPW
	 *            String
	 */
	public void setLOGINPW(String LOGINPW) {
		this.dbrow.Column("LOGINPW").setValue(LOGINPW);
		this.LOGINPW = LOGINPW;
	}

	/**
	 * 获取[用户状态]
	 * 
	 * @return String
	 */
	public String getUSERSTATE() {
		return this.getString(this.dbrow.Column("USERSTATE").getString());
	}

	/**
	 * 设置[用户状态]
	 * 
	 * @param USERSTATE
	 *            String
	 */
	public void setUSERSTATE(String USERSTATE) {
		this.dbrow.Column("USERSTATE").setValue(USERSTATE);
		this.USERSTATE = USERSTATE;
	}

	/**
	 * 获取[真实姓名]
	 * 
	 * @return String
	 */
	public String getNAME() {
		return this.getString(this.dbrow.Column("NAME").getString());
	}

	/**
	 * 设置[真实姓名]
	 * 
	 * @param NAME
	 *            String
	 */
	public void setNAME(String NAME) {
		this.dbrow.Column("NAME").setValue(NAME);
		this.NAME = NAME;
	}

	/**
	 * 获取[内部编号]
	 * 
	 * @return String
	 */
	public String getLCODE() {
		return this.getString(this.dbrow.Column("LCODE").getString());
	}

	/**
	 * 设置[内部编号]
	 * 
	 * @param LCODE
	 *            String
	 */
	public void setLCODE(String LCODE) {
		this.dbrow.Column("LCODE").setValue(LCODE);
		this.LCODE = LCODE;
	}

	/**
	 * 获取[所在单位]
	 * 
	 * @return String
	 */
	public String getUNITID() {
		return this.getString(this.dbrow.Column("UNITID").getString());
	}

	/**
	 * 设置[所在单位]
	 * 
	 * @param UNITID
	 *            String
	 */
	public void setUNITID(String UNITID) {
		this.dbrow.Column("UNITID").setValue(UNITID);
		this.UNITID = UNITID;
	}

	/**
	 * 获取[性别]
	 * 
	 * @return String
	 */
	public String getSEX() {
		return this.getString(this.dbrow.Column("SEX").getString());
	}

	/**
	 * 设置[性别]
	 * 
	 * @param SEX
	 *            String
	 */
	public void setSEX(String SEX) {
		this.dbrow.Column("SEX").setValue(SEX);
		this.SEX = SEX;
	}

	/**
	 * 获取[出生日期]
	 * 
	 * @return java.util.Date
	 */
	public java.util.Date getBIRTHDAY() {
		return this.dbrow.Column("BIRTHDAY").getDate();
	}

	/**
	 * 设置[出生日期]
	 * 
	 * @param BIRTHDAY
	 *            java.util.Date
	 */
	public void setBIRTHDAY(java.util.Date BIRTHDAY) {
		this.dbrow.Column("BIRTHDAY").setValue(BIRTHDAY);
		this.BIRTHDAY = BIRTHDAY;
	}

	/**
	 * 获取[办公电话]
	 * 
	 * @return String
	 */
	public String getPHONE() {
		return this.getString(this.dbrow.Column("PHONE").getString());
	}

	/**
	 * 设置[办公电话]
	 * 
	 * @param PHONE
	 *            String
	 */
	public void setPHONE(String PHONE) {
		this.dbrow.Column("PHONE").setValue(PHONE);
		this.PHONE = PHONE;
	}

	/**
	 * 获取[手机]
	 * 
	 * @return String
	 */
	public String getMOBILE() {
		return this.getString(this.dbrow.Column("MOBILE").getString());
	}

	/**
	 * 设置[手机]
	 * 
	 * @param MOBILE
	 *            String
	 */
	public void setMOBILE(String MOBILE) {
		this.dbrow.Column("MOBILE").setValue(MOBILE);
		this.MOBILE = MOBILE;
	}

	/**
	 * 获取[电子邮件]
	 * 
	 * @return String
	 */
	public String getEMAIL() {
		return this.getString(this.dbrow.Column("EMAIL").getString());
	}

	/**
	 * 设置[电子邮件]
	 * 
	 * @param EMAIL
	 *            String
	 */
	public void setEMAIL(String EMAIL) {
		this.dbrow.Column("EMAIL").setValue(EMAIL);
		this.EMAIL = EMAIL;
	}

	/**
	 * 获取[头像]
	 * 
	 * @return String
	 */
	public String getUSERIMAGE() {
		return this.getString(this.dbrow.Column("USERIMAGE").getString());
	}

	/**
	 * 设置[头像]
	 * 
	 * @param USERIMAGE
	 *            String
	 */
	public void setUSERIMAGE(String USERIMAGE) {
		this.dbrow.Column("USERIMAGE").setValue(USERIMAGE);
		this.USERIMAGE = USERIMAGE;
	}

	/**
	 * 获取[登录次数]
	 * 
	 * @return int
	 */
	public int getLOGINTIME() {
		return this.dbrow.Column("LOGINTIME").getInteger();
	}

	/**
	 * 设置[登录次数]
	 * 
	 * @param LOGINTIME
	 *            int
	 */
	public void setLOGINTIME(int LOGINTIME) {
		this.dbrow.Column("LOGINTIME").setValue(Integer.toString(LOGINTIME));
		this.LOGINTIME = LOGINTIME;
	}

	/**
	 * 获取[表单每页显示的记录数]
	 * 
	 * @return int
	 */
	public int getPAGESIZE() {
		return this.dbrow.Column("PAGESIZE").getInteger();
	}

	/**
	 * 设置[表单每页显示的记录数]
	 * 
	 * @param PAGESIZE
	 *            int
	 */
	public void setPAGESIZE(int PAGESIZE) {
		this.dbrow.Column("PAGESIZE").setValue(Integer.toString(PAGESIZE));
		this.PAGESIZE = PAGESIZE;
	}

	/**
	 * 获取[界面样式]
	 * 
	 * @return String
	 */
	public String getWINDOWTYPE() {
		return this.getString(this.dbrow.Column("WINDOWTYPE").getString());
	}

	/**
	 * 设置[界面样式]
	 * 
	 * @param WINDOWTYPE
	 *            String
	 */
	public void setWINDOWTYPE(String WINDOWTYPE) {
		this.dbrow.Column("WINDOWTYPE").setValue(WINDOWTYPE);
		this.WINDOWTYPE = WINDOWTYPE;
	}

	/**
	 * 获取[部门内勤]
	 * 
	 * @return String
	 */
	public String getDEPTINSIDE() {
		return this.getString(this.dbrow.Column("DEPTINSIDE").getString());
	}

	/**
	 * 设置[部门内勤]
	 * 
	 * @param DEPTINSIDE
	 *            String
	 */
	public void setDEPTINSIDE(String DEPTINSIDE) {
		this.dbrow.Column("DEPTINSIDE").setValue(DEPTINSIDE);
		this.DEPTINSIDE = DEPTINSIDE;
	}

	/**
	 * 获取[排序号]
	 * 
	 * @return String
	 */
	public String getORBERCODE() {
		return this.getString(this.dbrow.Column("ORBERCODE").getString());
	}

	/**
	 * 设置[排序号]
	 * 
	 * @param ORBERCODE
	 *            String
	 */
	public void setORBERCODE(String ORBERCODE) {
		this.dbrow.Column("ORBERCODE").setValue(ORBERCODE);
		this.ORBERCODE = ORBERCODE;
	}

	/**
	 * 用户图像数据
	 */
	private BPIP_USER_PHOTO UserPhoto = null;

	public BPIP_USER_PHOTO getUserPhoto() {
		return UserPhoto;
	}

	public void setUserPhoto(BPIP_USER_PHOTO userphoto) {
		UserPhoto = userphoto;
	}

	/**
	 * 获取[在线更新时间]
	 * 
	 * @return java.util.Date
	 */
	public java.util.Date getONLINEDATE() {
		return this.dbrow.Column("ONLINEDATE").getDate();
	}

	/**
	 * 设置[在线更新时间]
	 * 
	 * @param ONLINEDATE
	 *            java.util.Date
	 */
	public void setONLINEDATE(java.util.Date ONLINEDATE) {
		this.dbrow.Column("ONLINEDATE").setValue(ONLINEDATE);
		this.ONLINEDATE = ONLINEDATE;
	}

	public Set<String> getPermsSet() {
		return permsSet;
	}

	public void setPermsSet(Set<String> permsSet) {
		this.permsSet = permsSet;
	}
	
	

}