package zr.zrpower.flowengine.mode.config;

import zr.zrpower.common.db.DBEntity;
import zr.zrpower.common.db.DBType;

import java.util.Date;

/**
 * <p>
 * Description: 流程配置表实体
 * </p>
 * <p>
 * 单元名称：FLOW_CONFIG_PROCESS.java
 * </p>
 * <p>
 * 中文解释：过程表数据实体类
 * </p>
 * <p>
 * 作用：将数据库表过程表映射为Java类，作为数据传输的载体。
 * </p>
 */
public class FLOW_CONFIG_PROCESS extends DBEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected String ID;
	protected String IDENTIFICATION;
	protected String NAME;
	protected String DESC1;
	protected String FLOWPACKAGE;
	protected String DOCID;
	protected String DOCIDPHO;
	protected String STATUS;
	protected String CREATEPSN;
	protected Date CREATEDATE;
	protected String CODE;
	protected String ICO;
	protected String TYPE;
	protected int CNUM;
	protected String FORMTYPE;
	protected String ISDELETEFORM;
	protected String DCODE;

	/**
	 * 构造函数
	 */
	public FLOW_CONFIG_PROCESS() {
		super();
		initialize();
	}

	/**
	 * 初始化函数
	 */
	protected void initialize() {
		this.dbrow.setTableName("FLOW_CONFIG_PROCESS");
		this.dbrow.addColumn("ID", null, DBType.STRING); // 过程ID
		this.dbrow.addColumn("IDENTIFICATION", null, DBType.STRING); // 过程标识
		this.dbrow.addColumn("NAME", null, DBType.STRING); // 过程名称
		this.dbrow.addColumn("DESC1", null, DBType.STRING); // 过程描述
		this.dbrow.addColumn("FLOWPACKAGE", null, DBType.STRING); // 所属流程包
		this.dbrow.addColumn("DOCID", null, DBType.STRING); // 文档配置ID
		this.dbrow.addColumn("DOCIDPHO", null, DBType.STRING); // 文档配置DOCIDPHO
		this.dbrow.addColumn("STATUS", null, DBType.STRING); // 发布状态
		this.dbrow.addColumn("CREATEPSN", null, DBType.STRING); // 创建者
		this.dbrow.addColumn("CREATEDATE", null, DBType.DATE); // 创建日期
		this.dbrow.addColumn("CODE", null, DBType.STRING); // 序号
		this.dbrow.addColumn("ICO", null, DBType.STRING); // 流程图标
		this.dbrow.addColumn("TYPE", null, DBType.STRING); // 流程类别
		this.dbrow.addColumn("CNUM", null, DBType.LONG); // 能新建的流程数
		this.dbrow.addColumn("FORMTYPE", null, DBType.STRING); // 表单类别
		this.dbrow.addColumn("ISDELETEFORM", null, DBType.STRING); // 删除流程时是否删除表单数据

		this.dbrow.setPrimaryKey("ID");
	}

	/**
	 * 获取[过程ID]
	 * 
	 * @return String
	 */
	public String getID() {
		return this.getString(this.dbrow.Column("ID").getString());
	}

	/**
	 * 设置[过程ID]
	 * 
	 * @param ID
	 *            String
	 */
	public void setID(String ID) {
		this.dbrow.Column("ID").setValue(ID);
		this.ID = ID;
	}

	/**
	 * 获取[过程标识]
	 * 
	 * @return String
	 */
	public String getIDENTIFICATION() {
		return this.getString(this.dbrow.Column("IDENTIFICATION").getString());
	}

	/**
	 * 设置[过程标识]
	 * 
	 * @param IDENTIFICATION
	 *            String
	 */
	public void setIDENTIFICATION(String IDENTIFICATION) {
		this.dbrow.Column("IDENTIFICATION").setValue(IDENTIFICATION);
		this.IDENTIFICATION = IDENTIFICATION;
	}

	/**
	 * 获取[过程名称]
	 * 
	 * @return String
	 */
	public String getNAME() {
		return this.getString(this.dbrow.Column("NAME").getString());
	}

	/**
	 * 设置[过程名称]
	 * 
	 * @param NAME
	 *            String
	 */
	public void setNAME(String NAME) {
		this.dbrow.Column("NAME").setValue(NAME);
		this.NAME = NAME;
	}

	/**
	 * 获取[过程描述]
	 * 
	 * @return String
	 */
	public String getDESC1() {
		return this.getString(this.dbrow.Column("DESC1").getString());
	}

	/**
	 * 设置[过程描述]
	 * 
	 * @param DESC
	 *            String
	 */
	public void setDESC1(String DESC1) {
		this.dbrow.Column("DESC1").setValue(DESC1);
		this.DESC1 = DESC1;
	}

	/**
	 * 获取[所属流程包]
	 * 
	 * @return String
	 */
	public String getFLOWPACKAGE() {
		return this.getString(this.dbrow.Column("FLOWPACKAGE").getString());
	}

	/**
	 * 设置[所属流程包]
	 * 
	 * @param FLOWPACKAGE
	 *            String
	 */
	public void setFLOWPACKAGE(String FLOWPACKAGE) {
		this.dbrow.Column("FLOWPACKAGE").setValue(FLOWPACKAGE);
		this.FLOWPACKAGE = FLOWPACKAGE;
	}

	/**
	 * 获取[文档配置ID]
	 * 
	 * @return String
	 */
	public String getDOCID() {
		return this.getString(this.dbrow.Column("DOCID").getString());
	}

	/**
	 * 设置[文档配置ID]
	 * 
	 * @param DOCID
	 *            String
	 */
	public void setDOCID(String DOCID) {
		this.dbrow.Column("DOCID").setValue(DOCID);
		this.DOCID = DOCID;
	}

	/**
	 * 获取[文档配置DOCIDPHO]
	 * 
	 * @return String
	 */
	public String getDOCIDPHO() {
		return this.getString(this.dbrow.Column("DOCIDPHO").getString());
	}

	/**
	 * 设置[文档配置DOCIDPHO]
	 * 
	 * @param DOCID
	 *            String
	 */
	public void setDOCIDPHO(String DOCIDPHO) {
		this.dbrow.Column("DOCIDPHO").setValue(DOCIDPHO);
		this.DOCIDPHO = DOCIDPHO;
	}

	/**
	 * 获取[发布状态]
	 * 
	 * @return String
	 */
	public String getSTATUS() {
		return this.getString(this.dbrow.Column("STATUS").getString());
	}

	/**
	 * 设置[发布状态]
	 * 
	 * @param STATUS
	 *            String
	 */
	public void setSTATUS(String STATUS) {
		this.dbrow.Column("STATUS").setValue(STATUS);
		this.STATUS = STATUS;
	}

	/**
	 * 获取[创建者]
	 * 
	 * @return String
	 */
	public String getCREATEPSN() {
		return this.getString(this.dbrow.Column("CREATEPSN").getString());
	}

	/**
	 * 设置[创建者]
	 * 
	 * @param CREATEPSN
	 *            String
	 */
	public void setCREATEPSN(String CREATEPSN) {
		this.dbrow.Column("CREATEPSN").setValue(CREATEPSN);
		this.CREATEPSN = CREATEPSN;
	}

	/**
	 * 获取[创建日期]
	 * 
	 * @return java.util.Date
	 */
	public java.util.Date getCREATEDATE() {
		return this.dbrow.Column("CREATEDATE").getDate();
	}

	/**
	 * 设置[创建日期]
	 * 
	 * @param CREATEDATE
	 *            java.util.Date
	 */
	public void setCREATEDATE(java.util.Date CREATEDATE) {
		this.dbrow.Column("CREATEDATE").setValue(CREATEDATE);
		this.CREATEDATE = CREATEDATE;
	}

	/**
	 * 获取[序号]
	 * 
	 * @param CODE
	 *            java.util.Date
	 */

	public String getCODE() {
		return this.getString(this.dbrow.Column("CODE").getString());
	}

	/**
	 * 设置[序号]
	 * 
	 * @param STATUS
	 *            String
	 */
	public void setCODE(String CODE) {
		this.dbrow.Column("CODE").setValue(CODE);
		this.CODE = CODE;
	}

	/**
	 * 获取[流程图标]
	 * 
	 * @param STATUS
	 *            String
	 */
	public String getICO() {
		return this.getString(this.dbrow.Column("ICO").getString());
	}

	/**
	 * 设置[流程图标]
	 * 
	 * @param STATUS
	 *            String
	 */
	public void setICO(String ICO) {
		this.dbrow.Column("ICO").setValue(ICO);
		this.ICO = ICO;
	}

	/**
	 * 获取[流程类别]
	 * 
	 * @return String
	 */
	public String getTYPE() {
		return this.getString(this.dbrow.Column("TYPE").getString());
	}

	/**
	 * 设置[流程类别]
	 * 
	 * @param TYPE
	 *            String
	 */
	public void setTYPE(String TYPE) {
		this.dbrow.Column("TYPE").setValue(TYPE);
		this.TYPE = TYPE;
	}

	/**
	 * 获取[能新建的流程数]
	 * 
	 * @return int
	 */
	public int getCNUM() {
		return this.dbrow.Column("CNUM").getInteger();
	}

	/**
	 * 设置[能新建的流程数]
	 * 
	 * @param CNUM
	 *            int
	 */
	public void setCNUM(int CNUM) {
		this.dbrow.Column("CNUM").setValue(Integer.toString(CNUM));
		this.CNUM = CNUM;
	}

	/**
	 * 获取[表单类别]
	 * 
	 * @return String
	 */
	public String getFORMTYPE() {
		return this.getString(this.dbrow.Column("FORMTYPE").getString());
	}

	/**
	 * 设置[表单类别]
	 * 
	 * @param FORMTYPE
	 *            String
	 */
	public void setFORMTYPE(String FORMTYPE) {
		this.dbrow.Column("FORMTYPE").setValue(FORMTYPE);
		this.FORMTYPE = FORMTYPE;
	}

	/**
	 * 获取[删除流程时是否删除表单数据]
	 * 
	 * @return String
	 */
	public String getISDELETEFORM() {
		return this.getString(this.dbrow.Column("ISDELETEFORM").getString());
	}

	/**
	 * 设置[删除流程时是否删除表单数据]
	 * 
	 * @param ISDELETEFORM
	 *            String
	 */
	public void setISDELETEFORM(String ISDELETEFORM) {
		this.dbrow.Column("ISDELETEFORM").setValue(ISDELETEFORM);
		this.ISDELETEFORM = ISDELETEFORM;
	}

	public String getDCODE() {
		return DCODE;
	}

	public void setDCODE(String dCODE) {
		DCODE = dCODE;
	}
}