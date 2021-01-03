package com.yonglilian.model;

import zr.zrpower.common.db.DBEntity;
import zr.zrpower.common.db.DBType;

/**
 * <p>Description: 数据表实体映射包</p>
 * <p>
 * 单元名称：BPIP_HANDSET.java
 * </p>
 * <p>
 * 中文解释：手机短信管理表数据实体类
 * </p>
 * <p>
 * 作用：将数据库表手机短信管理表映射为Java类，作为数据传输的载体。
 * </p>
 * 
 * @author Java实体生成器
 * 
 */
public class BPIP_HANDSET extends DBEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 构造函数
	 */
	public BPIP_HANDSET() {
		super();
		initialize();
	}

	/**
	 * 初始化函数
	 */
	protected void initialize() {
		this.dbrow.setTableName("BPIP_HANDSET");
		this.dbrow.addColumn("ID", null, DBType.STRING); // 编号
		this.dbrow.addColumn("USERNO", null, DBType.STRING); // 发送人人员编号
		this.dbrow.addColumn("MOBILE", null, DBType.STRING); // 手机号
		this.dbrow.addColumn("CONTENT", null, DBType.STRING); // 发送内容
		this.dbrow.addColumn("SENDDATE", null, DBType.DATE); // 发送日期
		this.dbrow.addColumn("FINISHDATE", null, DBType.DATE); // 完成日期
		this.dbrow.addColumn("TYPEBOX", null, DBType.LONG); // 短信箱
		this.dbrow.addColumn("SENDTYPE", null, DBType.STRING); // 发送类型
		this.dbrow.addColumn("MONTH", null, DBType.LONG); // 月
		this.dbrow.addColumn("WEEK", null, DBType.LONG); // 星期
		this.dbrow.addColumn("DAY", null, DBType.LONG); // 天
		this.dbrow.addColumn("HOUR", null, DBType.LONG); // 时
		this.dbrow.addColumn("MINUTE", null, DBType.LONG); // 分
		this.dbrow.addColumn("ISSEND", null, DBType.STRING); // 是否已经发送
		this.dbrow.addColumn("TTABLEID", null, DBType.STRING); // 对应表ID值
		this.dbrow.addColumn("SUSERNO", null, DBType.STRING); // 收信息人的人员编号
		this.dbrow.setPrimaryKey("ID");
	}

	/**
	 * 获取[编号]
	 * 
	 * @return String
	 */
	public String getID() {
		return this.getString(this.dbrow.Column("ID").getString());
	}

	/**
	 * 设置[编号]
	 * 
	 * @param ID
	 *            String
	 */
	public void setID(String ID) {
		this.dbrow.Column("ID").setValue(ID);
	}

	/**
	 * 获取[发送人人员编号]
	 * 
	 * @return String
	 */
	public String getUSERNO() {
		return this.getString(this.dbrow.Column("USERNO").getString());
	}

	/**
	 * 设置[发送人人员编号]
	 * 
	 * @param USERNO
	 *            String
	 */
	public void setUSERNO(String USERNO) {
		this.dbrow.Column("USERNO").setValue(USERNO);
	}

	/**
	 * 获取[手机号]
	 * 
	 * @return String
	 */
	public String getMOBILE() {
		return this.getString(this.dbrow.Column("MOBILE").getString());
	}

	/**
	 * 设置[手机号]
	 * 
	 * @param MOBILE
	 *            String
	 */
	public void setMOBILE(String MOBILE) {
		this.dbrow.Column("MOBILE").setValue(MOBILE);
	}

	/**
	 * 获取[发送内容]
	 * 
	 * @return String
	 */
	public String getCONTENT() {
		return this.getString(this.dbrow.Column("CONTENT").getString());
	}

	/**
	 * 设置[发送内容]
	 * 
	 * @param CONTENT
	 *            String
	 */
	public void setCONTENT(String CONTENT) {
		this.dbrow.Column("CONTENT").setValue(CONTENT);
	}

	/**
	 * 获取[发送日期]
	 * 
	 * @return java.util.Date
	 */
	public java.util.Date getSENDDATE() {
		return this.dbrow.Column("SENDDATE").getDate();
	}

	/**
	 * 设置[发送日期]
	 * 
	 * @param SENDDATE
	 *            java.util.Date
	 */
	public void setSENDDATE(java.util.Date SENDDATE) {
		this.dbrow.Column("SENDDATE").setValue(SENDDATE);
	}

	/**
	 * 获取[完成日期]
	 * 
	 * @return java.util.Date
	 */
	public java.util.Date getFINISHDATE() {
		return this.dbrow.Column("FINISHDATE").getDate();
	}

	/**
	 * 设置[完成日期]
	 * 
	 * @param FINISHDATE
	 *            java.util.Date
	 */
	public void setFINISHDATE(java.util.Date FINISHDATE) {
		this.dbrow.Column("FINISHDATE").setValue(FINISHDATE);
	}

	/**
	 * 获取[短信箱]
	 * 
	 * @return int
	 */
	public int getTYPEBOX() {
		return this.dbrow.Column("TYPEBOX").getInteger();
	}

	/**
	 * 设置[短信箱]
	 * 
	 * @param TYPEBOX
	 *            int
	 */
	public void setTYPEBOX(int TYPEBOX) {
		this.dbrow.Column("TYPEBOX").setValue(Integer.toString(TYPEBOX));
	}

	/**
	 * 获取[发送类型]
	 * 
	 * @return String
	 */
	public String getSENDTYPE() {
		return this.getString(this.dbrow.Column("SENDTYPE").getString());
	}

	/**
	 * 设置[发送类型]
	 * 
	 * @param SENDTYPE
	 *            String
	 */
	public void setSENDTYPE(String SENDTYPE) {
		this.dbrow.Column("SENDTYPE").setValue(SENDTYPE);
	}

	/**
	 * 获取[月]
	 * 
	 * @return int
	 */
	public int getMONTH() {
		return this.dbrow.Column("MONTH").getInteger();
	}

	/**
	 * 设置[月]
	 * 
	 * @param MONTH
	 *            int
	 */
	public void setMONTH(int MONTH) {
		this.dbrow.Column("MONTH").setValue(Integer.toString(MONTH));
	}

	/**
	 * 获取[星期]
	 * 
	 * @return int
	 */
	public int getWEEK() {
		return this.dbrow.Column("WEEK").getInteger();
	}

	/**
	 * 设置[星期]
	 * 
	 * @param WEEK
	 *            int
	 */
	public void setWEEK(int WEEK) {
		this.dbrow.Column("WEEK").setValue(Integer.toString(WEEK));
	}

	/**
	 * 获取[天]
	 * 
	 * @return int
	 */
	public int getDAY() {
		return this.dbrow.Column("DAY").getInteger();
	}

	/**
	 * 设置[天]
	 * 
	 * @param DAY
	 *            int
	 */
	public void setDAY(int DAY) {
		this.dbrow.Column("DAY").setValue(Integer.toString(DAY));
	}

	/**
	 * 获取[时]
	 * 
	 * @return int
	 */
	public int getHOUR() {
		return this.dbrow.Column("HOUR").getInteger();
	}

	/**
	 * 设置[时]
	 * 
	 * @param HOUR
	 *            int
	 */
	public void setHOUR(int HOUR) {
		this.dbrow.Column("HOUR").setValue(Integer.toString(HOUR));
	}

	/**
	 * 获取[分]
	 * 
	 * @return int
	 */
	public int getMINUTE() {
		return this.dbrow.Column("MINUTE").getInteger();
	}

	/**
	 * 设置[分]
	 * 
	 * @param MINUTE
	 *            int
	 */
	public void setMINUTE(int MINUTE) {
		this.dbrow.Column("MINUTE").setValue(Integer.toString(MINUTE));
	}

	/**
	 * 获取[是否已经发送]
	 * 
	 * @return String
	 */
	public String getISSEND() {
		return this.getString(this.dbrow.Column("ISSEND").getString());
	}

	/**
	 * 设置[是否已经发送]
	 * 
	 * @param ISSEND
	 *            String
	 */
	public void setISSEND(String ISSEND) {
		this.dbrow.Column("ISSEND").setValue(ISSEND);
	}

	/**
	 * 获取[对应表ID值]
	 * 
	 * @return String
	 */
	public String getTTABLEID() {
		return this.getString(this.dbrow.Column("TTABLEID").getString());
	}

	/**
	 * 设置[对应表ID值]
	 * 
	 * @param TTABLEID
	 *            String
	 */
	public void setTTABLEID(String TTABLEID) {
		this.dbrow.Column("TTABLEID").setValue(TTABLEID);
	}

	/**
	 * 获取[收信息人的人员编号]
	 * 
	 * @return String
	 */
	public String getSUSERNO() {
		return this.getString(this.dbrow.Column("SUSERNO").getString());
	}

	/**
	 * 设置[收信息人的人员编号]
	 * 
	 * @param SUSERNO
	 *            String
	 */
	public void setSUSERNO(String SUSERNO) {
		this.dbrow.Column("SUSERNO").setValue(SUSERNO);
	}

}
