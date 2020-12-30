package zr.zrpower.flowengine.mode.monitor;

import zr.zrpower.common.db.DBEntity;
import zr.zrpower.common.db.DBType;

import java.util.Date;

/**
 * <p>Title: ZRPower</p>
 * <p>
 * Description: 数据表实体映射包
 * </p>
 * <p>
 * 单元名称：FLOW_RUNTIME_ACTIVITY.java
 * </p>
 * <p>
 * 中文解释：流程流转过程表数据实体类
 * </p>
 * <p>
 * 作用：将数据库表流程流转过程表映射为Java类，作为数据传输的载体。
 * </p>
 * 
 * @author Java实体生成器 By NFZR
 * 
 */
public class FLOW_RUNTIME_ACTIVITY extends DBEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected String ID;
	protected String FID;
	protected String SACTIVITY;
	protected String EACTIVITY;
	protected String NAME;
	protected String DOPSN;
	protected Date DODATE;
	protected String DOIDEA;
	protected String DOFLAG;
	protected String SENDPSN;
	protected Date SENDDATE;

	/**
	 * 构造函数
	 */
	public FLOW_RUNTIME_ACTIVITY() {
		super();
		initialize();
	}

	/**
	 * 初始化函数
	 */
	protected void initialize() {
		this.dbrow.setTableName("FLOW_RUNTIME_ACTIVITY");
		this.dbrow.addColumn("ID", null, DBType.STRING); // ID
		this.dbrow.addColumn("FID", null, DBType.STRING); // 流程流转表ID
		this.dbrow.addColumn("SACTIVITY", null, DBType.STRING); // 开始活动
		this.dbrow.addColumn("EACTIVITY", null, DBType.STRING); // 结束活动
		this.dbrow.addColumn("NAME", null, DBType.STRING); // 流转名称
		this.dbrow.addColumn("DOPSN", null, DBType.STRING); // 处理人
		this.dbrow.addColumn("DODATE", null, DBType.DATE); // 处理时间
		this.dbrow.addColumn("DOIDEA", null, DBType.STRING); // 处理意见
		this.dbrow.addColumn("DOFLAG", null, DBType.STRING); // 处理状态
		this.dbrow.addColumn("SENDPSN", null, DBType.STRING); // 发送人
		this.dbrow.addColumn("SENDDATE", null, DBType.DATE); // 发送时间
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
	 * 获取[流程流转表ID]
	 * 
	 * @return String
	 */
	public String getFID() {
		return this.getString(this.dbrow.Column("FID").getString());
	}

	/**
	 * 设置[流程流转表ID]
	 * 
	 * @param FID
	 *            String
	 */
	public void setFID(String FID) {
		this.dbrow.Column("FID").setValue(FID);
	}

	/**
	 * 获取[开始活动]
	 * 
	 * @return String
	 */
	public String getSACTIVITY() {
		return this.getString(this.dbrow.Column("SACTIVITY").getString());
	}

	/**
	 * 设置[开始活动]
	 * 
	 * @param SACTIVITY
	 *            String
	 */
	public void setSACTIVITY(String SACTIVITY) {
		this.dbrow.Column("SACTIVITY").setValue(SACTIVITY);
	}

	/**
	 * 获取[结束活动]
	 * 
	 * @return String
	 */
	public String getEACTIVITY() {
		return this.getString(this.dbrow.Column("EACTIVITY").getString());
	}

	/**
	 * 设置[结束活动]
	 * 
	 * @param EACTIVITY
	 *            String
	 */
	public void setEACTIVITY(String EACTIVITY) {
		this.dbrow.Column("EACTIVITY").setValue(EACTIVITY);
	}

	/**
	 * 获取[流转名称]
	 * 
	 * @return String
	 */
	public String getNAME() {
		return this.getString(this.dbrow.Column("NAME").getString());
	}

	/**
	 * 设置[流转名称]
	 * 
	 * @param NAME
	 *            String
	 */
	public void setNAME(String NAME) {
		this.dbrow.Column("NAME").setValue(NAME);
	}

	/**
	 * 获取[处理人]
	 * 
	 * @return String
	 */
	public String getDOPSN() {
		return this.getString(this.dbrow.Column("DOPSN").getString());
	}

	/**
	 * 设置[处理人]
	 * 
	 * @param DOPSN
	 *            String
	 */
	public void setDOPSN(String DOPSN) {
		this.dbrow.Column("DOPSN").setValue(DOPSN);
	}

	/**
	 * 获取[处理时间]
	 * 
	 * @return java.util.Date
	 */
	public Date getDODATE() {
		return this.dbrow.Column("DODATE").getDate();
	}

	/**
	 * 设置[处理时间]
	 * 
	 * @param DODATE
	 *            java.util.Date
	 */
	public void setDODATE(Date DODATE) {
		this.dbrow.Column("DODATE").setValue(DODATE);
	}

	/**
	 * 获取[处理意见]
	 * 
	 * @return String
	 */
	public String getDOIDEA() {
		return this.getString(this.dbrow.Column("DOIDEA").getString());
	}

	/**
	 * 设置[处理意见]
	 * 
	 * @param DOIDEA
	 *            String
	 */
	public void setDOIDEA(String DOIDEA) {
		this.dbrow.Column("DOIDEA").setValue(DOIDEA);
	}

	/**
	 * 获取[处理状态]
	 * 
	 * @return String
	 */
	public String getDOFLAG() {
		return this.getString(this.dbrow.Column("DOFLAG").getString());
	}

	/**
	 * 设置[处理状态]
	 * 
	 * @param DOFLAG
	 *            String
	 */
	public void setDOFLAG(String DOFLAG) {
		this.dbrow.Column("DOFLAG").setValue(DOFLAG);
	}

	/**
	 * 获取[发送人]
	 * 
	 * @return String
	 */
	public String getSENDPSN() {
		return this.getString(this.dbrow.Column("SENDPSN").getString());
	}

	/**
	 * 设置[发送人]
	 * 
	 * @param SENDPSN
	 *            String
	 */
	public void setSENDPSN(String SENDPSN) {
		this.dbrow.Column("SENDPSN").setValue(SENDPSN);
	}

	/**
	 * 获取[发送时间]
	 * 
	 * @return java.util.Date
	 */
	public Date getSENDDATE() {
		return this.dbrow.Column("SENDDATE").getDate();
	}

	/**
	 * 设置[发送时间]
	 * 
	 * @param SENDDATE
	 *            java.util.Date
	 */
	public void setSENDDATE(Date SENDDATE) {
		this.dbrow.Column("SENDDATE").setValue(SENDDATE);
	}

}