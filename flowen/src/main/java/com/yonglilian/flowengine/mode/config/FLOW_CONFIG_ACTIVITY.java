package com.yonglilian.flowengine.mode.config;

import zr.zrpower.common.db.DBEntity;
import zr.zrpower.common.db.DBType;

/**
 * <p>
 * 单元名称：FLOW_CONFIG_ACTIVITY.java
 * </p>
 * <p>
 * 中文解释：活动表数据实体类
 * </p>
 * <p>
 * 作用：将数据库表活动表映射为Java类，作为数据传输的载体。
 * </p>
 */
public class FLOW_CONFIG_ACTIVITY extends DBEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected String ID;
	protected String FID;
	protected String IDENTIFICATION;
	protected String NAME;
	protected String DESC1;
	protected String TYPE;
	protected Integer ORDER1;
	protected String ISSIGN;
	protected String ASTRATEGY;
	protected String CSTRATEGY;
	protected Integer CNUM;
	protected String OPENHELP;
	protected String XYCSS;
	protected String ATTTYPE;
	protected Integer ATTNUM;
	protected String ISNOTE;
	protected String ISMESSAGE;
	protected String FORMPATH;
	protected String MESSAGE;
	protected String ISSAVE1;
	protected String ISSAVE2;
	protected String ISLEAVE1;
	protected String ISLEAVE2;
	protected String ADDFORMPATH;
	protected String ADDFORMWIDTH;
	protected String ADDFORMHEIGHT;
	protected String ADDFORMMESSAGE;
	protected String ISBRANCH;

	/**
	 * 构造函数
	 */
	public FLOW_CONFIG_ACTIVITY() {
		super();
		initialize();
	}

	/**
	 * 初始化函数
	 */
	protected void initialize() {
		this.dbrow.setTableName("FLOW_CONFIG_ACTIVITY");
		this.dbrow.addColumn("ID", null, DBType.STRING); // 活动ID
		this.dbrow.addColumn("FID", null, DBType.STRING); // 过程ID
		this.dbrow.addColumn("IDENTIFICATION", null, DBType.STRING); // 活动标识
		this.dbrow.addColumn("NAME", null, DBType.STRING); // 活动名称
		this.dbrow.addColumn("DESC1", null, DBType.STRING); // 活动描述
		this.dbrow.addColumn("TYPE", null, DBType.STRING); // 活动类型
		this.dbrow.addColumn("ORDER1", null, DBType.LONG); // 活动序号
		this.dbrow.addColumn("ISSIGN", null, DBType.STRING); // 会签是否顺序执行
		this.dbrow.addColumn("ASTRATEGY", null, DBType.STRING); // 分配策略
		this.dbrow.addColumn("CSTRATEGY", null, DBType.STRING); // 完成策略
		this.dbrow.addColumn("CNUM", null, DBType.LONG); // 完成数量或百分比
		this.dbrow.addColumn("OPENHELP", null, DBType.STRING); // 是否自动打开帮助
		this.dbrow.addColumn("XYCSS", null, DBType.STRING); // 活动坐标值
		this.dbrow.addColumn("ATTTYPE", null, DBType.STRING); // 附件类型
		this.dbrow.addColumn("ATTNUM", null, DBType.LONG); // 需上传附件数
		this.dbrow.addColumn("ISNOTE", null, DBType.STRING); // 是否短信提示
		this.dbrow.addColumn("ISMESSAGE", null, DBType.STRING); // 是否消息提示
		this.dbrow.addColumn("FORMPATH", null, DBType.STRING); // 自定义表单页面地址
		this.dbrow.addColumn("MESSAGE", null, DBType.STRING); // 活动处理后提示消息
		this.dbrow.addColumn("ISSAVE1", null, DBType.STRING); // 前进保存数据
		this.dbrow.addColumn("ISSAVE2", null, DBType.STRING); // 后退保存数据
		this.dbrow.addColumn("ISLEAVE1", null, DBType.STRING); // 前进必填留言
		this.dbrow.addColumn("ISLEAVE2", null, DBType.STRING); // 后退必填留言
		this.dbrow.addColumn("ADDFORMPATH", null, DBType.STRING); // 活动处理前填写表单地址
		this.dbrow.addColumn("ADDFORMWIDTH", null, DBType.STRING); // 活动处理前填写表单宽度
		this.dbrow.addColumn("ADDFORMHEIGHT", null, DBType.STRING); // 活动处理前填写表单高度
		this.dbrow.addColumn("ADDFORMMESSAGE", null, DBType.STRING); // 活动处理前非正确填写表单提示
		this.dbrow.addColumn("ISBRANCH", null, DBType.STRING); // 多路分支是否表现在按钮上

		this.dbrow.setPrimaryKey("ID");
	}

	/**
	 * 获取[活动ID]
	 * 
	 * @return String
	 */
	public String getID() {
		return this.getString(this.dbrow.Column("ID").getString());
	}

	/**
	 * 设置[活动ID]
	 * 
	 * @param ID
	 *            String
	 */
	public void setID(String ID) {
		this.dbrow.Column("ID").setValue(ID);
		this.ID = ID;
	}

	/**
	 * 获取[过程ID]
	 * 
	 * @return String
	 */
	public String getFID() {
		return this.getString(this.dbrow.Column("FID").getString());
	}

	/**
	 * 设置[过程ID]
	 * 
	 * @param FID
	 *            String
	 */
	public void setFID(String FID) {
		this.dbrow.Column("FID").setValue(FID);
		this.FID = FID;
	}

	/**
	 * 获取[活动标识]
	 * 
	 * @return String
	 */
	public String getIDENTIFICATION() {
		return this.getString(this.dbrow.Column("IDENTIFICATION").getString());
	}

	/**
	 * 设置[活动标识]
	 * 
	 * @param IDENTIFICATION
	 *            String
	 */
	public void setIDENTIFICATION(String IDENTIFICATION) {
		this.dbrow.Column("IDENTIFICATION").setValue(IDENTIFICATION);
		this.IDENTIFICATION = IDENTIFICATION;
	}

	/**
	 * 获取[活动名称]
	 * 
	 * @return String
	 */
	public String getNAME() {
		return this.getString(this.dbrow.Column("NAME").getString());
	}

	/**
	 * 设置[活动名称]
	 * 
	 * @param NAME
	 *            String
	 */
	public void setNAME(String NAME) {
		this.dbrow.Column("NAME").setValue(NAME);
		this.NAME = NAME;
	}

	/**
	 * 获取[活动描述]
	 * 
	 * @return String
	 */
	public String getDESC1() {
		return this.getString(this.dbrow.Column("DESC1").getString());
	}

	/**
	 * 设置[活动描述]
	 * 
	 * @param DESC
	 *            String
	 */
	public void setDESC1(String DESC1) {
		this.dbrow.Column("DESC1").setValue(DESC1);
		this.DESC1 = DESC1;
	}

	/**
	 * 获取[活动类型]
	 * 
	 * @return String
	 */
	public String getTYPE() {
		return this.getString(this.dbrow.Column("TYPE").getString());
	}

	/**
	 * 设置[活动类型]
	 * 
	 * @param TYPE
	 *            String
	 */
	public void setTYPE(String TYPE) {
		this.dbrow.Column("TYPE").setValue(TYPE);
		this.TYPE = TYPE;
	}

	/**
	 * 获取[活动序号]
	 * 
	 * @return int
	 */
	public Integer getORDER1() {
		return this.dbrow.Column("ORDER1").getInteger();
	}

	/**
	 * 设置[活动序号]
	 * 
	 * @param ORDER
	 *            int
	 */
	public void setORDER1(Integer ORDER1) {
		this.dbrow.Column("ORDER1").setValue(ORDER1);
		this.ORDER1 = ORDER1;
	}

	/**
	 * 获取[会签是否顺序执行]
	 * 
	 * @return String
	 */
	public String getISSIGN() {
		return this.getString(this.dbrow.Column("ISSIGN").getString());
	}

	/**
	 * 设置[会签是否顺序执行]
	 * 
	 * @param ISSIGN
	 *            String
	 */
	public void setISSIGN(String ISSIGN) {
		this.dbrow.Column("ISSIGN").setValue(ISSIGN);
		this.ISSIGN = ISSIGN;
	}

	/**
	 * 获取[分配策略]
	 * 
	 * @return String
	 */
	public String getASTRATEGY() {
		return this.getString(this.dbrow.Column("ASTRATEGY").getString());
	}

	/**
	 * 设置[分配策略]
	 * 
	 * @param ASTRATEGY
	 *            String
	 */
	public void setASTRATEGY(String ASTRATEGY) {
		this.dbrow.Column("ASTRATEGY").setValue(ASTRATEGY);
		this.ASTRATEGY = ASTRATEGY;
	}

	/**
	 * 获取[完成策略]
	 * 
	 * @return String
	 */
	public String getCSTRATEGY() {
		return this.getString(this.dbrow.Column("CSTRATEGY").getString());
	}

	/**
	 * 设置[完成策略]
	 * 
	 * @param CSTRATEGY
	 *            String
	 */
	public void setCSTRATEGY(String CSTRATEGY) {
		this.dbrow.Column("CSTRATEGY").setValue(CSTRATEGY);
		this.CSTRATEGY = CSTRATEGY;
	}

	/**
	 * 获取[完成数量或百分比]
	 * 
	 * @return int
	 */
	public Integer getCNUM() {
		return this.dbrow.Column("CNUM").getInteger();
	}

	/**
	 * 设置[完成数量或百分比]
	 * 
	 * @param CNUM
	 *            int
	 */
	public void setCNUM(Integer CNUM) {
		this.dbrow.Column("CNUM").setValue(CNUM);
		this.CNUM = CNUM;
	}

	/**
	 * 获取[是否自动打开帮助]
	 * 
	 * @return String
	 */
	public String getOPENHELP() {
		return this.getString(this.dbrow.Column("OPENHELP").getString());
	}

	/**
	 * 设置[是否自动打开帮助]
	 * 
	 * @param OPENHELP
	 *            String
	 */
	public void setOPENHELP(String OPENHELP) {
		this.dbrow.Column("OPENHELP").setValue(OPENHELP);
		this.OPENHELP = OPENHELP;
	}

	/**
	 * 获取[活动坐标值]
	 * 
	 * @return String
	 */
	public String getXYCSS() {
		return this.getString(this.dbrow.Column("XYCSS").getString());
	}

	/**
	 * 设置[活动坐标值]
	 * 
	 * @param XYCSS
	 *            String
	 */
	public void setXYCSS(String XYCSS) {
		this.dbrow.Column("XYCSS").setValue(XYCSS);
		this.XYCSS = XYCSS;
	}

	/**
	 * 获取[附件类型]
	 * 
	 * @return String
	 */
	public String getATTTYPE() {
		return this.getString(this.dbrow.Column("ATTTYPE").getString());
	}

	/**
	 * 设置[附件类型]
	 * 
	 * @param ATTTYPE
	 *            String
	 */
	public void setATTTYPE(String ATTTYPE) {
		this.dbrow.Column("ATTTYPE").setValue(ATTTYPE);
		this.ATTTYPE = ATTTYPE;
	}

	/**
	 * 获取[需上传附件数]
	 * 
	 * @return int
	 */
	public Integer getATTNUM() {
		return this.dbrow.Column("ATTNUM").getInteger();
	}

	/**
	 * 设置[需上传附件数]
	 * 
	 * @param ATTNUM
	 *            int
	 */
	public void setATTNUM(Integer ATTNUM) {
		this.dbrow.Column("ATTNUM").setValue(ATTNUM);
		this.ATTNUM = ATTNUM;
	}

	/**
	 * 获取[短信提示]
	 * 
	 * @return String
	 */
	public String getISNOTE() {
		return this.getString(this.dbrow.Column("ISNOTE").getString());
	}

	/**
	 * 设置[短信提示]
	 * 
	 * @param ISNOTE
	 *            String
	 */
	public void setISNOTE(String ISNOTE) {
		this.dbrow.Column("ISNOTE").setValue(ISNOTE);
		this.ISNOTE = ISNOTE;
	}

	/**
	 * 获取[消息提示]
	 * 
	 * @return String
	 */
	public String getISMESSAGE() {
		return this.getString(this.dbrow.Column("ISMESSAGE").getString());
	}

	/**
	 * 设置[消息提示]
	 * 
	 * @param ISMESSAGE
	 *            String
	 */
	public void setISMESSAGE(String ISMESSAGE) {
		this.dbrow.Column("ISMESSAGE").setValue(ISMESSAGE);
		this.ISMESSAGE = ISMESSAGE;
	}

	/**
	 * 获取[自定义表单页面地址]
	 * 
	 * @return String
	 */
	public String getFORMPATH() {
		return this.getString(this.dbrow.Column("FORMPATH").getString());
	}

	/**
	 * 设置[自定义表单页面地址]
	 * 
	 * @param FORMPATH
	 *            String
	 */
	public void setFORMPATH(String FORMPATH) {
		this.dbrow.Column("FORMPATH").setValue(FORMPATH);
		this.FORMPATH = FORMPATH;
	}

	/**
	 * 获取[活动处理后的提示消息]
	 * 
	 * @return String
	 */
	public String getMESSAGE() {
		return this.getString(this.dbrow.Column("MESSAGE").getString());
	}

	/**
	 * 设置[活动处理后的提示消息]
	 * 
	 * @param MESSAGE
	 *            String
	 */
	public void setMESSAGE(String MESSAGE) {
		this.dbrow.Column("MESSAGE").setValue(MESSAGE);
		this.MESSAGE = MESSAGE;
	}

	/**
	 * 获取[前进保存数据]
	 * 
	 * @return String
	 */
	public String getISSAVE1() {
		return this.getString(this.dbrow.Column("ISSAVE1").getString());
	}

	/**
	 * 设置[前进保存数据]
	 * 
	 * @param ISSAVE1
	 *            String
	 */
	public void setISSAVE1(String ISSAVE1) {
		this.dbrow.Column("ISSAVE1").setValue(ISSAVE1);
		this.ISSAVE1 = ISSAVE1;
	}

	/**
	 * 获取[后退保存数据]
	 * 
	 * @return String
	 */
	public String getISSAVE2() {
		return this.getString(this.dbrow.Column("ISSAVE2").getString());
	}

	/**
	 * 设置[后退保存数据]
	 * 
	 * @param ISSAVE2
	 *            String
	 */
	public void setISSAVE2(String ISSAVE2) {
		this.dbrow.Column("ISSAVE2").setValue(ISSAVE2);
		this.ISSAVE2 = ISSAVE2;
	}

	/**
	 * 获取[前进必填留言]
	 * 
	 * @return String
	 */
	public String getISLEAVE1() {
		return this.getString(this.dbrow.Column("ISLEAVE1").getString());
	}

	/**
	 * 设置[前进必填留言]
	 * 
	 * @param ISLEAVE1
	 *            String
	 */
	public void setISLEAVE1(String ISLEAVE1) {
		this.dbrow.Column("ISLEAVE1").setValue(ISLEAVE1);
		this.ISLEAVE1 = ISLEAVE1;
	}

	/**
	 * 获取[后退必填留言]
	 * 
	 * @return String
	 */
	public String getISLEAVE2() {
		return this.getString(this.dbrow.Column("ISLEAVE2").getString());
	}

	/**
	 * 设置[后退必填留言]
	 * 
	 * @param ISLEAVE2
	 *            String
	 */
	public void setISLEAVE2(String ISLEAVE2) {
		this.dbrow.Column("ISLEAVE2").setValue(ISLEAVE2);
		this.ISLEAVE2 = ISLEAVE2;
	}

	/**
	 * 获取[活动处理前填写表单地址]
	 * 
	 * @return String
	 */
	public String getADDFORMPATH() {
		return this.getString(this.dbrow.Column("ADDFORMPATH").getString());
	}

	/**
	 * 设置[活动处理前填写表单地址]
	 * 
	 * @param ADDFORMPATH
	 *            String
	 */
	public void setADDFORMPATH(String ADDFORMPATH) {
		this.dbrow.Column("ADDFORMPATH").setValue(ADDFORMPATH);
		this.ADDFORMPATH = ADDFORMPATH;
	}

	/**
	 * 获取[活动处理前填写表单宽度]
	 * 
	 * @return String
	 */
	public String getADDFORMWIDTH() {
		return this.getString(this.dbrow.Column("ADDFORMWIDTH").getString());
	}

	/**
	 * 设置[活动处理前填写表单宽度]
	 * 
	 * @param ADDFORMWIDTH
	 *            String
	 */
	public void setADDFORMWIDTH(String ADDFORMWIDTH) {
		this.dbrow.Column("ADDFORMWIDTH").setValue(ADDFORMWIDTH);
		this.ADDFORMWIDTH = ADDFORMWIDTH;
	}

	/**
	 * 获取[活动处理前填写表单高度]
	 * 
	 * @return String
	 */
	public String getADDFORMHEIGHT() {
		return this.getString(this.dbrow.Column("ADDFORMHEIGHT").getString());
	}

	/**
	 * 设置[活动处理前填写表单高度]
	 * 
	 * @param ADDFORMHEIGHT
	 *            String
	 */
	public void setADDFORMHEIGHT(String ADDFORMHEIGHT) {
		this.dbrow.Column("ADDFORMHEIGHT").setValue(ADDFORMHEIGHT);
		this.ADDFORMHEIGHT = ADDFORMHEIGHT;
	}

	/**
	 * 获取[活动处理前非正确填写表单提示]
	 * 
	 * @return String
	 */
	public String getADDFORMMESSAGE() {
		return this.getString(this.dbrow.Column("ADDFORMMESSAGE").getString());
	}

	/**
	 * 设置[活动处理前非正确填写表单提示]
	 * 
	 * @param ADDFORMMESSAGE
	 *            String
	 */
	public void setADDFORMMESSAGE(String ADDFORMMESSAGE) {
		this.dbrow.Column("ADDFORMMESSAGE").setValue(ADDFORMMESSAGE);
		this.ADDFORMMESSAGE = ADDFORMMESSAGE;
	}

	/**
	 * 获取[多路分支是否表现在按钮上]
	 * 
	 * @return String
	 */
	public String getISBRANCH() {
		return this.getString(this.dbrow.Column("ISBRANCH").getString());
	}

	/**
	 * 设置[多路分支是否表现在按钮上]
	 * 
	 * @param ISBRANCH
	 *            String
	 */
	public void setISBRANCH(String ISBRANCH) {
		this.dbrow.Column("ISBRANCH").setValue(ISBRANCH);
		this.ISBRANCH = ISBRANCH;
	}

}