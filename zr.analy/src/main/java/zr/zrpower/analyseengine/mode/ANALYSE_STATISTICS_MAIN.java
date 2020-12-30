package zr.zrpower.analyseengine.mode;

import zr.zrpower.common.db.DBEntity;
import zr.zrpower.common.db.DBType;

/**
 * <p>Title: ZRPower</p>
 * <p>
 * Description: 数据表实体映射包
 * </p>
 * <p>
 * 单元名称：ANALYSE_STATISTICS_MAIN.java
 * </p>
 * <p>
 * 中文解释：统计配置表数据实体类
 * </p>
 * <p>
 * 作用：将数据库表统计配置表映射为Java类，作为数据传输的载体。
 * </p>
 * 
 * @author Java实体生成器 By NFZR
 */
public class ANALYSE_STATISTICS_MAIN extends DBEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected String ID;
	protected String STATISTICSNAME;
	protected String SDESC;
	protected String TABLEID;
	protected String TIMESTYPE;
	protected String PLANARTABLE;
	protected String PLANARFIELD;
	protected String CPLANARFIELD;
	protected String WHEREVALUE;
	protected String EXCELTEMPLATE;
	protected String PLANARFIELDNAME;
	protected String WISMATCH;
	protected String CJOIN;
	protected String ISUNIT;
	protected String ISNUMBER;
	protected String SINPUTTYPE;
	protected String SINPUTPAGE;
	protected String ISAGV;
	protected String ISSUM;
	protected String ISSHOWTYPE;
	protected String SHOWLINK;
	protected String TIMETEMPLATE;
	protected String CODETABLE;
	protected String TBUTTON;
	protected String ADDFIELD;
	protected String ISZERO;

	/**
	 * 构造函数
	 */
	public ANALYSE_STATISTICS_MAIN() {
		super();
		initialize();
	}

	/**
	 * 初始化函数
	 */
	protected void initialize() {
		this.dbrow.setTableName("ANALYSE_STATISTICS_MAIN");
		this.dbrow.addColumn("ID", null, DBType.STRING); // 编号
		this.dbrow.addColumn("STATISTICSNAME", null, DBType.STRING); // 统计配置名称
		this.dbrow.addColumn("SDESC", null, DBType.STRING); // 统计配置描述
		this.dbrow.addColumn("TABLEID", null, DBType.STRING); // 统计主表ID
		this.dbrow.addColumn("TIMESTYPE", null, DBType.STRING); // 定时类型
		this.dbrow.addColumn("PLANARTABLE", null, DBType.STRING); // 维数据对应的表
		this.dbrow.addColumn("PLANARFIELD", null, DBType.STRING); // 维数据对应的关联字段
		this.dbrow.addColumn("CPLANARFIELD", null, DBType.STRING); // 当前表与维数据对应的关联字段
		this.dbrow.addColumn("WHEREVALUE", null, DBType.STRING); // 维数据的条件
		this.dbrow.addColumn("EXCELTEMPLATE", null, DBType.STRING); // EXCEL显示模板
		this.dbrow.addColumn("PLANARFIELDNAME", null, DBType.STRING); // 维数据对应的关联中文字段
		this.dbrow.addColumn("WISMATCH", null, DBType.STRING); // 维数据项是否包含统计单位
		this.dbrow.addColumn("CJOIN", null, DBType.STRING); // 对应关系
		this.dbrow.addColumn("ISUNIT", null, DBType.STRING); // 是否按单位方式统计

		this.dbrow.addColumn("SINPUTTYPE", null, DBType.STRING); // 报表统计的自定义条件类型
		this.dbrow.addColumn("SINPUTPAGE", null, DBType.STRING); // 条件录入页面
		this.dbrow.addColumn("ISAGV", null, DBType.STRING); // 是否显示平均值统计行
		this.dbrow.addColumn("ISSUM", null, DBType.STRING); // 是否显示合计统计行
		this.dbrow.addColumn("ISSHOWTYPE", null, DBType.STRING); // 报表显示类型
		this.dbrow.addColumn("SHOWLINK", null, DBType.STRING); // 单元格的详细信息链接地址

		this.dbrow.addColumn("TIMETEMPLATE", null, DBType.STRING); // 描述性报表模板
		this.dbrow.addColumn("CODETABLE", null, DBType.STRING); // 统计字典表
		this.dbrow.addColumn("TBUTTON", null, DBType.STRING); // 统计按钮描述
		this.dbrow.addColumn("ISNUMBER", null, DBType.STRING); // 显示序号
		this.dbrow.addColumn("ADDFIELD", null, DBType.STRING); // 附加显示字段
		this.dbrow.addColumn("ISZERO", null, DBType.STRING); // 是否显示空行
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
		this.ID = ID;
	}

	/**
	 * 获取[统计配置名称]
	 * 
	 * @return String
	 */
	public String getSTATISTICSNAME() {
		return this.getString(this.dbrow.Column("STATISTICSNAME").getString());
	}

	/**
	 * 设置[统计配置名称]
	 * 
	 * @param STATISTICSNAME
	 *            String
	 */
	public void setSTATISTICSNAME(String STATISTICSNAME) {
		this.dbrow.Column("STATISTICSNAME").setValue(STATISTICSNAME);
		this.STATISTICSNAME = STATISTICSNAME;
	}

	/**
	 * 获取[统计配置描述]
	 * 
	 * @return String
	 */
	public String getSDESC() {
		return this.getString(this.dbrow.Column("SDESC").getString());
	}

	/**
	 * 设置[统计配置描述]
	 * 
	 * @param SDESC
	 *            String
	 */
	public void setSDESC(String SDESC) {
		this.dbrow.Column("SDESC").setValue(SDESC);
		this.SDESC = SDESC;
	}

	/**
	 * 获取[统计主表ID]
	 * 
	 * @return String
	 */
	public String getTABLEID() {
		return this.getString(this.dbrow.Column("TABLEID").getString());
	}

	/**
	 * 设置[统计主表ID]
	 * 
	 * @param TABLEID
	 *            String
	 */
	public void setTABLEID(String TABLEID) {
		this.dbrow.Column("TABLEID").setValue(TABLEID);
		this.TABLEID = TABLEID;
	}

	/**
	 * 获取[定时类型]
	 * 
	 * @return String
	 */
	public String getTIMESTYPE() {
		return this.getString(this.dbrow.Column("TIMESTYPE").getString());
	}

	/**
	 * 设置[定时类型]
	 * 
	 * @param TIMESTYPE
	 *            String
	 */
	public void setTIMESTYPE(String TIMESTYPE) {
		this.dbrow.Column("TIMESTYPE").setValue(TIMESTYPE);
		this.TIMESTYPE = TIMESTYPE;
	}

	/**
	 * 获取[维数据对应的表]
	 * 
	 * @return String
	 */
	public String getPLANARTABLE() {
		return this.getString(this.dbrow.Column("PLANARTABLE").getString());
	}

	/**
	 * 设置[维数据对应的表]
	 * 
	 * @param PLANARTABLE
	 *            String
	 */
	public void setPLANARTABLE(String PLANARTABLE) {
		this.dbrow.Column("PLANARTABLE").setValue(PLANARTABLE);
		this.PLANARTABLE = PLANARTABLE;
	}

	/**
	 * 获取[维数据对应的关联字段]
	 * 
	 * @return String
	 */
	public String getPLANARFIELD() {
		return this.getString(this.dbrow.Column("PLANARFIELD").getString());
	}

	/**
	 * 设置[维数据对应的关联字段]
	 * 
	 * @param PLANARFIELD
	 *            String
	 */
	public void setPLANARFIELD(String PLANARFIELD) {
		this.dbrow.Column("PLANARFIELD").setValue(PLANARFIELD);
		this.PLANARFIELD = PLANARFIELD;
	}

	/**
	 * 获取[当前表与维数据对应的关联字段]
	 * 
	 * @return String
	 */
	public String getCPLANARFIELD() {
		return this.getString(this.dbrow.Column("CPLANARFIELD").getString());
	}

	/**
	 * 设置[当前表与维数据对应的关联字段]
	 * 
	 * @param CPLANARFIELD
	 *            String
	 */
	public void setCPLANARFIELD(String CPLANARFIELD) {
		this.dbrow.Column("CPLANARFIELD").setValue(CPLANARFIELD);
		this.CPLANARFIELD = CPLANARFIELD;
	}

	/**
	 * 获取[维数据的条件]
	 * 
	 * @return String
	 */
	public String getWHEREVALUE() {
		return this.getString(this.dbrow.Column("WHEREVALUE").getString());
	}

	/**
	 * 设置[维数据的条件]
	 * 
	 * @param WHEREVALUE
	 *            String
	 */
	public void setWHEREVALUE(String WHEREVALUE) {
		this.dbrow.Column("WHEREVALUE").setValue(WHEREVALUE);
		this.WHEREVALUE = WHEREVALUE;
	}

	/**
	 * 获取[EXCEL显示模板]
	 * 
	 * @return String
	 */
	public String getEXCELTEMPLATE() {
		return this.getString(this.dbrow.Column("EXCELTEMPLATE").getString());
	}

	/**
	 * 设置[EXCEL显示模板]
	 * 
	 * @param EXCELTEMPLATE
	 *            String
	 */
	public void setEXCELTEMPLATE(String EXCELTEMPLATE) {
		this.dbrow.Column("EXCELTEMPLATE").setValue(EXCELTEMPLATE);
		this.EXCELTEMPLATE = EXCELTEMPLATE;
	}

	/**
	 * 获取[维数据对应的关联中文字段]
	 * 
	 * @return String
	 */
	public String getPLANARFIELDNAME() {
		return this.getString(this.dbrow.Column("PLANARFIELDNAME").getString());
	}

	/**
	 * 设置[维数据对应的关联中文字段]
	 * 
	 * @param PLANARFIELDNAME
	 *            String
	 */
	public void setPLANARFIELDNAME(String PLANARFIELDNAME) {
		this.dbrow.Column("PLANARFIELDNAME").setValue(PLANARFIELDNAME);
		this.PLANARFIELDNAME = PLANARFIELDNAME;
	}

	/**
	 * 获取[维数据项是否包含统计单位]
	 * 
	 * @return String
	 */
	public String getWISMATCH() {
		return this.getString(this.dbrow.Column("WISMATCH").getString());
	}

	/**
	 * 设置[维数据项是否包含统计单位]
	 * 
	 * @param WISMATCH
	 *            String
	 */
	public void setWISMATCH(String WISMATCH) {
		this.dbrow.Column("WISMATCH").setValue(WISMATCH);
		this.WISMATCH = WISMATCH;
	}

	/**
	 * 获取[对应关系]
	 * 
	 * @return String
	 */
	public String getCJOIN() {
		return this.getString(this.dbrow.Column("CJOIN").getString());
	}

	/**
	 * 设置[对应关系]
	 * 
	 * @param CJOIN
	 *            String
	 */
	public void setCJOIN(String CJOIN) {
		this.dbrow.Column("CJOIN").setValue(CJOIN);
		this.CJOIN = CJOIN;
	}

	/**
	 * 获取[报表统计的自定义条件类型]
	 * 
	 * @return String
	 */
	public String getSINPUTTYPE() {
		return this.getString(this.dbrow.Column("SINPUTTYPE").getString());
	}

	/**
	 * 设置[报表统计的自定义条件类型]
	 * 
	 * @param SINPUTTYPE
	 *            String
	 */
	public void setSINPUTTYPE(String SINPUTTYPE) {
		this.dbrow.Column("SINPUTTYPE").setValue(SINPUTTYPE);
		this.SINPUTTYPE = SINPUTTYPE;
	}

	/**
	 * 获取[条件录入页面]
	 * 
	 * @return String
	 */
	public String getSINPUTPAGE() {
		return this.getString(this.dbrow.Column("SINPUTPAGE").getString());
	}

	/**
	 * 设置[条件录入页面]
	 * 
	 * @param SINPUTPAGE
	 *            String
	 */
	public void setSINPUTPAGE(String SINPUTPAGE) {
		this.dbrow.Column("SINPUTPAGE").setValue(SINPUTPAGE);
		this.SINPUTPAGE = SINPUTPAGE;
	}

	/**
	 * 获取[是否显示平均值统计行]
	 * 
	 * @return String
	 */
	public String getISAGV() {
		return this.getString(this.dbrow.Column("ISAGV").getString());
	}

	/**
	 * 设置[是否显示平均值统计行]
	 * 
	 * @param ISAGV
	 *            String
	 */
	public void setISAGV(String ISAGV) {
		this.dbrow.Column("ISAGV").setValue(ISAGV);
		this.ISAGV = ISAGV;
	}

	/**
	 * 获取[是否显示合计统计行]
	 * 
	 * @return String
	 */
	public String getISSUM() {
		return this.getString(this.dbrow.Column("ISSUM").getString());
	}

	/**
	 * 设置[是否显示合计统计行]
	 * 
	 * @param ISSUM
	 *            String
	 */
	public void setISSUM(String ISSUM) {
		this.dbrow.Column("ISSUM").setValue(ISSUM);
		this.ISSUM = ISSUM;
	}

	/**
	 * 获取[报表显示类型]
	 * 
	 * @return String
	 */
	public String getISSHOWTYPE() {
		return this.getString(this.dbrow.Column("ISSHOWTYPE").getString());
	}

	/**
	 * 设置[报表显示类型]
	 * 
	 * @param ISSHOWTYPE
	 *            String
	 */
	public void setISSHOWTYPE(String ISSHOWTYPE) {
		this.dbrow.Column("ISSHOWTYPE").setValue(ISSHOWTYPE);
		this.ISSHOWTYPE = ISSHOWTYPE;
	}

	/**
	 * 获取[单元格的详细信息链接地址]
	 * 
	 * @return String
	 */
	public String getSHOWLINK() {
		return this.getString(this.dbrow.Column("SHOWLINK").getString());
	}

	/**
	 * 设置[单元格的详细信息链接地址]
	 * 
	 * @param SHOWLINK
	 *            String
	 */
	public void setSHOWLINK(String SHOWLINK) {
		this.dbrow.Column("SHOWLINK").setValue(SHOWLINK);
		this.SHOWLINK = SHOWLINK;
	}

	/**
	 * 获取[描述性报表模板]
	 * 
	 * @return String
	 */
	public String getTIMETEMPLATE() {
		return this.getString(this.dbrow.Column("TIMETEMPLATE").getString());
	}

	/**
	 * 设置[描述性报表模板]
	 * 
	 * @param TIMETEMPLATE
	 *            String
	 */
	public void setTIMETEMPLATE(String TIMETEMPLATE) {
		this.dbrow.Column("TIMETEMPLATE").setValue(TIMETEMPLATE);
		this.TIMETEMPLATE = TIMETEMPLATE;
	}

	/**
	 * 获取[统计字典表]
	 * 
	 * @return String
	 */
	public String getCODETABLE() {
		return this.getString(this.dbrow.Column("CODETABLE").getString());
	}

	/**
	 * 设置[统计字典表]
	 * 
	 * @param CODETABLE
	 *            String
	 */
	public void setCODETABLE(String CODETABLE) {
		this.dbrow.Column("CODETABLE").setValue(CODETABLE);
		this.CODETABLE = CODETABLE;
	}

	/**
	 * 获取[统计按钮描述]
	 * 
	 * @return String
	 */
	public String getTBUTTON() {
		return this.getString(this.dbrow.Column("TBUTTON").getString());
	}

	/**
	 * 设置[统计按钮描述]
	 * 
	 * @param TBUTTON
	 *            String
	 */
	public void setTBUTTON(String TBUTTON) {
		this.dbrow.Column("TBUTTON").setValue(TBUTTON);
		this.TBUTTON = TBUTTON;
	}

	public String getISUNIT() {
		return this.getString(this.dbrow.Column("ISUNIT").getString());
	}

	public void setISUNIT(String ISUNIT) {
		this.dbrow.Column("ISUNIT").setValue(ISUNIT);
		this.ISUNIT = ISUNIT;
	}

	public String getISNUMBER() {
		return this.getString(this.dbrow.Column("ISNUMBER").getString());
	}

	public void setISNUMBER(String ISNUMBER) {
		this.dbrow.Column("ISNUMBER").setValue(ISNUMBER);
		this.ISNUMBER = ISNUMBER;
	}

	public String getADDFIELD() {
		return this.getString(this.dbrow.Column("ADDFIELD").getString());
	}

	public void setADDFIELD(String ADDFIELD) {
		this.dbrow.Column("ADDFIELD").setValue(ADDFIELD);
		this.ADDFIELD = ADDFIELD;
	}

	public String getISZERO() {
		return this.getString(this.dbrow.Column("ISZERO").getString());
	}

	public void setISZERO(String ISZERO) {
		this.dbrow.Column("ISZERO").setValue(ISZERO);
		this.ISZERO = ISZERO;
	}

}
