package zr.zrpower.flowengine.mode.monitor;

import zr.zrpower.common.db.DBEntity;
import zr.zrpower.common.db.DBType;

import java.util.Date;

/**
 * <p>
 * Title: ZRPOWER平台
 * </p>
 * <p>
 * Description: 数据表实体映射包
 * </p>
 * <p>
 * 单元名称：FLOW_RUNTIME_PROCESS.java
 * </p>
 * <p>
 * 中文解释：流程流转表数据实体类
 * </p>
 * <p>
 * 作用：将数据库表流程流转表映射为Java类，作为数据传输的载体。
 * </p>
 * 
 * @author Java实体生成器 By NFZR
 * 
 */
public class FLOW_RUNTIME_PROCESS extends DBEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected String ID;
	protected String FLOWID;
	protected String FLOWPATH;
	protected String NAME;
	protected String CREATEPSN;
	protected Date CREATEDATE;
	protected String ACCEPTPSN;
	protected Date ACCEPTDATE;
	protected String CURRACTIVITY;
	protected int READCOUNT;
	protected String STATE;
	protected String FORMTABLE;
	protected String FORMID;
	protected String OTHERID;
	protected int ACCEPTPSNNUM;
	protected String FACCEPTPSN;
	protected Date FDATE;
	protected String ISABNORMITY;
	protected Date ABNORMITYDATE;
	protected String PARENTID;
	protected String PARENTID1;
	protected String RID;
	protected String RECORDID;
	protected String FUID;

	/**
	 * 构造函数
	 */
	public FLOW_RUNTIME_PROCESS() {
		super();
		initialize();
	}

	/**
	 * 初始化函数
	 */
	protected void initialize() {
		this.dbrow.setTableName("FLOW_RUNTIME_PROCESS");
		this.dbrow.addColumn("ID", null, DBType.STRING); // ID
		this.dbrow.addColumn("FLOWID", null, DBType.STRING); // 流程标识
		this.dbrow.addColumn("FLOWPATH", null, DBType.STRING); // 业务流程的地址（相对地址）
		this.dbrow.addColumn("NAME", null, DBType.STRING); // 流程名称
		this.dbrow.addColumn("CREATEPSN", null, DBType.STRING); // 创建人
		this.dbrow.addColumn("CREATEDATE", null, DBType.DATE); // 创建日期
		this.dbrow.addColumn("ACCEPTPSN", null, DBType.STRING); // 接收人
		this.dbrow.addColumn("ACCEPTDATE", null, DBType.DATE); // 接收日期
		this.dbrow.addColumn("CURRACTIVITY", null, DBType.STRING); // 当前活动
		this.dbrow.addColumn("READCOUNT", null, DBType.LONG); // 阅读次数
		this.dbrow.addColumn("STATE", null, DBType.STRING); // 是否完成
		this.dbrow.addColumn("FORMTABLE", null, DBType.STRING); // 主表名
		this.dbrow.addColumn("FORMID", null, DBType.STRING); // 业务表id
		this.dbrow.addColumn("OTHERID", null, DBType.STRING); // 关联id
		this.dbrow.addColumn("ACCEPTPSNNUM", null, DBType.LONG); // 当前接收人数量
		this.dbrow.addColumn("FACCEPTPSN", null, DBType.STRING); // 当前已处理人列表
		this.dbrow.addColumn("FDATE", null, DBType.DATE); // 完成时间
		this.dbrow.addColumn("ISABNORMITY", null, DBType.STRING); // 是否已异常处理
		this.dbrow.addColumn("ABNORMITYDATE", null, DBType.DATE); // 异常处理日期
		this.dbrow.addColumn("PARENTID", null, DBType.STRING); // 父级流程或父级表单的关联值
		this.dbrow.addColumn("PARENTID1", null, DBType.STRING); // 父级流程或父级表单的关联值1
		this.dbrow.addColumn("RID", null, DBType.STRING); // 关联流程的流转ID
		this.dbrow.addColumn("RECORDID", null, DBType.STRING); // 附件ID
		this.dbrow.addColumn("FUID", null, DBType.STRING); // 已经处理过的人员

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
	 * 获取[流程标识]
	 * 
	 * @return String
	 */
	public String getFLOWID() {
		return this.getString(this.dbrow.Column("FLOWID").getString());
	}

	/**
	 * 设置[流程标识]
	 * 
	 * @param FLOWID
	 *            String
	 */
	public void setFLOWID(String FLOWID) {
		this.dbrow.Column("FLOWID").setValue(FLOWID);
	}

	/**
	 * 获取[业务流程的地址（相对地址）]
	 * 
	 * @return String
	 */
	public String getFLOWPATH() {
		return this.getString(this.dbrow.Column("FLOWPATH").getString());
	}

	/**
	 * 设置[业务流程的地址（相对地址）]
	 * 
	 * @param FLOWPATH
	 *            String
	 */
	public void setFLOWPATH(String FLOWPATH) {
		this.dbrow.Column("FLOWPATH").setValue(FLOWPATH);
	}

	/**
	 * 获取[流程名称]
	 * 
	 * @return String
	 */
	public String getNAME() {
		return this.getString(this.dbrow.Column("NAME").getString());
	}

	/**
	 * 设置[流程名称]
	 * 
	 * @param NAME
	 *            String
	 */
	public void setNAME(String NAME) {
		this.dbrow.Column("NAME").setValue(NAME);
	}

	/**
	 * 获取[创建人]
	 * 
	 * @return String
	 */
	public String getCREATEPSN() {
		return this.getString(this.dbrow.Column("CREATEPSN").getString());
	}

	/**
	 * 设置[创建人]
	 * 
	 * @param CREATEPSN
	 *            String
	 */
	public void setCREATEPSN(String CREATEPSN) {
		this.dbrow.Column("CREATEPSN").setValue(CREATEPSN);
	}

	/**
	 * 获取[创建日期]
	 * 
	 * @return java.util.Date
	 */
	public Date getCREATEDATE() {
		return this.dbrow.Column("CREATEDATE").getDate();
	}

	/**
	 * 设置[创建日期]
	 * 
	 * @param CREATEDATE
	 *            java.util.Date
	 */
	public void setCREATEDATE(Date CREATEDATE) {
		this.dbrow.Column("CREATEDATE").setValue(CREATEDATE);
	}

	/**
	 * 获取[接收人]
	 * 
	 * @return String
	 */
	public String getACCEPTPSN() {
		return this.getString(this.dbrow.Column("ACCEPTPSN").getString());
	}

	/**
	 * 设置[接收人]
	 * 
	 * @param ACCEPTPSN
	 *            String
	 */
	public void setACCEPTPSN(String ACCEPTPSN) {
		this.dbrow.Column("ACCEPTPSN").setValue(ACCEPTPSN);
	}

	/**
	 * 获取[接收日期]
	 * 
	 * @return java.util.Date
	 */
	public Date getACCEPTDATE() {
		return this.dbrow.Column("ACCEPTDATE").getDate();
	}

	/**
	 * 设置[接收日期]
	 * 
	 * @param ACCEPTDATE
	 *            java.util.Date
	 */
	public void setACCEPTDATE(Date ACCEPTDATE) {
		this.dbrow.Column("ACCEPTDATE").setValue(ACCEPTDATE);
	}

	/**
	 * 获取[当前活动]
	 * 
	 * @return String
	 */
	public String getCURRACTIVITY() {
		return this.getString(this.dbrow.Column("CURRACTIVITY").getString());
	}

	/**
	 * 设置[当前活动]
	 * 
	 * @param CURRACTIVITY
	 *            String
	 */
	public void setCURRACTIVITY(String CURRACTIVITY) {
		this.dbrow.Column("CURRACTIVITY").setValue(CURRACTIVITY);
	}

	/**
	 * 获取[阅读次数]
	 * 
	 * @return int
	 */
	public int getREADCOUNT() {
		return this.dbrow.Column("READCOUNT").getInteger();
	}

	/**
	 * 设置[阅读次数]
	 * 
	 * @param READCOUNT
	 *            int
	 */
	public void setREADCOUNT(int READCOUNT) {
		this.dbrow.Column("READCOUNT").setValue(Integer.toString(READCOUNT));
	}

	/**
	 * 获取[是否完成]
	 * 
	 * @return String
	 */
	public String getSTATE() {
		return this.getString(this.dbrow.Column("STATE").getString());
	}

	/**
	 * 设置[是否完成]
	 * 
	 * @param STATE
	 *            String
	 */
	public void setSTATE(String STATE) {
		this.dbrow.Column("STATE").setValue(STATE);
	}

	/**
	 * 获取[]
	 * 
	 * @return String
	 */
	public String getFORMTABLE() {
		return this.getString(this.dbrow.Column("FORMTABLE").getString());
	}

	/**
	 * 设置[]
	 * 
	 * @param FORMTABLE
	 *            String
	 */
	public void setFORMTABLE(String FORMTABLE) {
		this.dbrow.Column("FORMTABLE").setValue(FORMTABLE);
	}

	/**
	 * 获取[]
	 * 
	 * @return String
	 */
	public String getFORMID() {
		return this.getString(this.dbrow.Column("FORMID").getString());
	}

	/**
	 * 设置[]
	 * 
	 * @param FORMID
	 *            String
	 */
	public void setFORMID(String FORMID) {
		this.dbrow.Column("FORMID").setValue(FORMID);
	}

	/**
	 * 获取[]
	 * 
	 * @return String
	 */
	public String getOTHERID() {
		return this.getString(this.dbrow.Column("OTHERID").getString());
	}

	/**
	 * 设置[]
	 * 
	 * @param OTHERID
	 *            String
	 */
	public void setOTHERID(String OTHERID) {
		this.dbrow.Column("OTHERID").setValue(OTHERID);
	}

	/**
	 * 获取[]
	 * 
	 * @return int
	 */
	public int getACCEPTPSNNUM() {
		return this.dbrow.Column("ACCEPTPSNNUM").getInteger();
	}

	/**
	 * 设置[]
	 * 
	 * @param ACCEPTPSNNUM
	 *            int
	 */
	public void setACCEPTPSNNUM(int ACCEPTPSNNUM) {
		this.dbrow.Column("ACCEPTPSNNUM").setValue(Integer.toString(ACCEPTPSNNUM));
	}

	/**
	 * 获取[]
	 * 
	 * @return String
	 */
	public String getFACCEPTPSN() {
		return this.getString(this.dbrow.Column("FACCEPTPSN").getString());
	}

	/**
	 * 设置[]
	 * 
	 * @param FACCEPTPSN
	 *            String
	 */
	public void setFACCEPTPSN(String FACCEPTPSN) {
		this.dbrow.Column("FACCEPTPSN").setValue(FACCEPTPSN);
	}

	/**
	 * 获取[]
	 * 
	 * @return java.util.Date
	 */
	public Date getFDATE() {
		return this.dbrow.Column("FDATE").getDate();
	}

	/**
	 * 设置[]
	 * 
	 * @param FDATE
	 *            java.util.Date
	 */
	public void setFDATE(Date FDATE) {
		this.dbrow.Column("FDATE").setValue(FDATE);
	}

	/**
	 * 获取[]
	 * 
	 * @return String
	 */
	public String getISABNORMITY() {
		return this.getString(this.dbrow.Column("ISABNORMITY").getString());
	}

	/**
	 * 设置[]
	 * 
	 * @param ISABNORMITY
	 *            String
	 */
	public void setISABNORMITY(String ISABNORMITY) {
		this.dbrow.Column("ISABNORMITY").setValue(ISABNORMITY);
	}

	/**
	 * 获取[]
	 * 
	 * @return java.util.Date
	 */
	public Date getABNORMITYDATE() {
		return this.dbrow.Column("ABNORMITYDATE").getDate();
	}

	/**
	 * 设置[]
	 * 
	 * @param ABNORMITYDATE
	 *            java.util.Date
	 */
	public void setABNORMITYDATE(Date ABNORMITYDATE) {
		this.dbrow.Column("ABNORMITYDATE").setValue(ABNORMITYDATE);
	}

	/**
	 * 获取[父级流程或父级表单的关联值]
	 * 
	 * @return String
	 */
	public String getPARENTID() {
		return this.getString(this.dbrow.Column("PARENTID").getString());
	}

	/**
	 * 设置[父级流程或父级表单的关联值]
	 * 
	 * @param PARENTID
	 *            String
	 */
	public void setPARENTID(String PARENTID) {
		this.dbrow.Column("PARENTID").setValue(PARENTID);
	}

	/**
	 * 获取[父级流程或父级表单的关联值1]
	 * 
	 * @return String
	 */
	public String getPARENTID1() {
		return this.getString(this.dbrow.Column("PARENTID1").getString());
	}

	/**
	 * 设置[父级流程或父级表单的关联值]
	 * 
	 * @param PARENTID1
	 *            String
	 */
	public void setPARENTID1(String PARENTID1) {
		this.dbrow.Column("PARENTID1").setValue(PARENTID1);
	}

	/**
	 * 获取[关联流程的流转ID]
	 * 
	 * @return String
	 */
	public String getRID() {
		return this.getString(this.dbrow.Column("RID").getString());
	}

	/**
	 * 设置[关联流程的流转ID]
	 * 
	 * @param RID
	 *            String
	 */
	public void setRID(String RID) {
		this.dbrow.Column("RID").setValue(RID);
	}

	/**
	 * 获取[附件ID]
	 * 
	 * @return String
	 */
	public String getRECORDID() {
		return this.getString(this.dbrow.Column("RECORDID").getString());
	}

	/**
	 * 设置[附件ID]
	 * 
	 * @param RECORDID
	 *            String
	 */
	public void setRECORDID(String RECORDID) {
		this.dbrow.Column("RECORDID").setValue(RECORDID);
	}

	public String getFUID() {
		return this.getString(this.dbrow.Column("FUID").getString());
	}

	public void setFUID(String FUID) {
		this.dbrow.Column("FUID").setValue(FUID);
	}

}
