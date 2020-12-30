package zr.zrpower.flowengine.mode.config;

import zr.zrpower.common.db.DBEntity;
import zr.zrpower.common.db.DBType;

/**
 * <p>Title: ZRPower</p>
 * <p>
 * Description: 数据表实体映射包
 * </p>
 * <p>
 * 单元名称：FLOW_CONFIG_ACTIVITY_CONNE.java
 * </p>
 * <p>
 * 中文解释：活动依赖转发(关系)表数据实体类
 * </p>
 * <p>
 * 作用：将数据库表活动依赖转发(关系)表映射为Java类，作为数据传输的载体。
 * </p>
 */
public class FLOW_CONFIG_ACTIVITY_CONNE extends DBEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected String ID;
	protected String FID;
	protected String CID;
	protected String SID;
	protected String EID;
	protected String NAME;
	protected String DESC1;
	protected String WHERE1;
	protected String TYPE;
	protected String ISNEED;
	protected String ISATT;

	/**
	 * 构造函数
	 */
	public FLOW_CONFIG_ACTIVITY_CONNE() {
		super();
		initialize();
	}

	/**
	 * 初始化函数
	 */
	protected void initialize() {
		this.dbrow.setTableName("FLOW_CONFIG_ACTIVITY_CONNE");
		this.dbrow.addColumn("ID", null, DBType.STRING); // ID
		this.dbrow.addColumn("FID", null, DBType.STRING); // 过程ID
		this.dbrow.addColumn("CID", null, DBType.STRING); // 当前活动ID
		this.dbrow.addColumn("SID", null, DBType.STRING); // 前趋活动ID
		this.dbrow.addColumn("EID", null, DBType.STRING); // 后续活动ID
		this.dbrow.addColumn("NAME", null, DBType.STRING); // 依赖名称
		this.dbrow.addColumn("DESC1", null, DBType.STRING); // 依赖描述
		this.dbrow.addColumn("WHERE1", null, DBType.STRING); // 依赖条件
		this.dbrow.addColumn("TYPE", null, DBType.STRING); // 接收人类型
		this.dbrow.addColumn("ISNEED", null, DBType.STRING); // 是否必建流程
		this.dbrow.addColumn("ISATT", null, DBType.STRING); // 附件检测
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
	 * 获取[当前活动ID]
	 * 
	 * @return String
	 */
	public String getCID() {
		return this.getString(this.dbrow.Column("CID").getString());
	}

	/**
	 * 设置[当前活动ID]
	 * 
	 * @param CID
	 *            String
	 */
	public void setCID(String CID) {
		this.dbrow.Column("CID").setValue(CID);
		this.CID = CID;
	}

	/**
	 * 获取[前趋活动ID]
	 * 
	 * @return String
	 */
	public String getSID() {
		return this.getString(this.dbrow.Column("SID").getString());
	}

	/**
	 * 设置[前趋活动ID]
	 * 
	 * @param SID
	 *            String
	 */
	public void setSID(String SID) {
		this.dbrow.Column("SID").setValue(SID);
		this.SID = SID;
	}

	/**
	 * 获取[后续活动ID]
	 * 
	 * @return String
	 */
	public String getEID() {
		return this.getString(this.dbrow.Column("EID").getString());
	}

	/**
	 * 设置[后续活动ID]
	 * 
	 * @param EID
	 *            String
	 */
	public void setEID(String EID) {
		this.dbrow.Column("EID").setValue(EID);
		this.EID = EID;
	}

	/**
	 * 获取[依赖名称]
	 * 
	 * @return String
	 */
	public String getNAME() {
		return this.getString(this.dbrow.Column("NAME").getString());
	}

	/**
	 * 设置[依赖名称]
	 * 
	 * @param NAME
	 *            String
	 */
	public void setNAME(String NAME) {
		this.dbrow.Column("NAME").setValue(NAME);
		this.NAME = NAME;
	}

	/**
	 * 获取[依赖描述]
	 * 
	 * @return String
	 */
	public String getDESC1() {
		return this.getString(this.dbrow.Column("DESC1").getString());
	}

	/**
	 * 设置[依赖描述]
	 * 
	 * @param DESC
	 *            String
	 */
	public void setDESC1(String DESC1) {
		this.dbrow.Column("DESC1").setValue(DESC1);
		this.DESC1 = DESC1;
	}

	/**
	 * 获取[依赖条件]
	 * 
	 * @return String
	 */
	public String getWHERE1() {
		return this.getString(this.dbrow.Column("WHERE1").getString());
	}

	/**
	 * 设置[依赖条件]
	 * 
	 * @param WHERE
	 *            String
	 */
	public void setWHERE1(String WHERE1) {
		this.dbrow.Column("WHERE1").setValue(WHERE1);
		this.WHERE1 = WHERE1;
	}

	/**
	 * 获取[接收人类型]
	 * 
	 * @return String
	 */
	public String getTYPE() {
		return this.getString(this.dbrow.Column("TYPE").getString());
	}

	/**
	 * 设置[接收人类型]
	 * 
	 * @param TYPE
	 *            String
	 */
	public void setTYPE(String TYPE) {
		this.dbrow.Column("TYPE").setValue(TYPE);
		this.TYPE = TYPE;
	}

	/**
	 * 获取[是否必建流程]
	 * 
	 * @return String
	 */
	public String getISNEED() {
		return this.getString(this.dbrow.Column("ISNEED").getString());
	}

	/**
	 * 设置[是否必建流程]
	 * 
	 * @param ISNEED
	 *            String
	 */
	public void setISNEED(String ISNEED) {
		this.dbrow.Column("ISNEED").setValue(ISNEED);
		this.ISNEED = ISNEED;
	}

	/**
	 * 获取[附件检测]
	 * 
	 * @return String
	 */
	public String getISATT() {
		return this.getString(this.dbrow.Column("ISATT").getString());
	}

	/**
	 * 设置[附件检测]
	 * 
	 * @param ISATT
	 *            String
	 */
	public void setISATT(String ISATT) {
		this.dbrow.Column("ISATT").setValue(ISATT);
		this.ISATT = ISATT;
	}

}
