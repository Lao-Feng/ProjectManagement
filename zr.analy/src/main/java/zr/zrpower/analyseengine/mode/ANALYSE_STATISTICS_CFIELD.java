package zr.zrpower.analyseengine.mode;

import zr.zrpower.common.db.DBEntity;
import zr.zrpower.common.db.DBType;

/**
 * <p>Title: ZRPOWER平台</p>
 * <p>
 * Description: 数据表实体映射包
 * </p>
 * <p>
 * 单元名称：ANALYSE_STATISTICS_CFIELD.java
 * </p>
 * <p>
 * 中文解释：统计计算字段配置表数据实体类
 * </p>
 * <p>
 * 作用：将数据库表统计计算字段配置表映射为Java类，作为数据传输的载体。
 * </p>
 * 
 * @author Java实体生成器 By NFZR
 */
public class ANALYSE_STATISTICS_CFIELD extends DBEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected String ID;
	protected String FID;
	protected String SFIELDNAME;
	protected String SFIELDSHOWNAME;
	protected String EXPRESSIONS;
	protected int DISTINCTION;
	protected String ISSHOW;
	protected String SAVEFIELD;
	protected String EXPRESSIONSWHERE;
	protected String ADDSIGN;
	protected int RADIXPOINT;
	protected String SHOWCODE;
	protected String TABLEID;
	protected String CPLANARFIELD;
	protected String CJOIN;
	protected String CPLANARESPECIAL;

	/**
	 * 构造函数
	 */
	public ANALYSE_STATISTICS_CFIELD() {
		super();
		initialize();
	}

	/**
	 * 初始化函数
	 */
	protected void initialize() {
		this.dbrow.setTableName("ANALYSE_STATISTICS_CFIELD");
		this.dbrow.addColumn("ID", null, DBType.STRING); // 编号
		this.dbrow.addColumn("FID", null, DBType.STRING); // 统计配置表ID
		this.dbrow.addColumn("SFIELDNAME", null, DBType.STRING); // 计算字段名称
		this.dbrow.addColumn("SFIELDSHOWNAME", null, DBType.STRING); // 计算字段显示名称
		this.dbrow.addColumn("EXPRESSIONS", null, DBType.STRING); // 计算表达式
		this.dbrow.addColumn("DISTINCTION", null, DBType.LONG); // 计算级别
		this.dbrow.addColumn("ISSHOW", null, DBType.STRING); // 是否隐藏列
		this.dbrow.addColumn("SAVEFIELD", null, DBType.STRING); // 数据存放字段
		this.dbrow.addColumn("EXPRESSIONSWHERE", null, DBType.STRING); // 表达式运算条件
		this.dbrow.addColumn("ADDSIGN", null, DBType.STRING); // 附加符号
		this.dbrow.addColumn("RADIXPOINT", null, DBType.LONG); // 小数点位数
		this.dbrow.addColumn("SHOWCODE", null, DBType.STRING); // 显示序号
		this.dbrow.addColumn("TABLEID", null, DBType.STRING); // 统计主表
		this.dbrow.addColumn("CPLANARFIELD", null, DBType.STRING); // 当前表与维数据对应的关联字段
		this.dbrow.addColumn("CJOIN", null, DBType.STRING); // 对应关系
		this.dbrow.addColumn("CPLANARESPECIAL", null, DBType.STRING); // 当前表与维数据对应的特殊关联设置
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
	 * 获取[统计配置表ID]
	 * 
	 * @return String
	 */
	public String getFID() {
		return this.getString(this.dbrow.Column("FID").getString());
	}

	/**
	 * 设置[统计配置表ID]
	 * 
	 * @param FID
	 *            String
	 */
	public void setFID(String FID) {
		this.dbrow.Column("FID").setValue(FID);
	}

	/**
	 * 获取[计算字段名称]
	 * 
	 * @return String
	 */
	public String getSFIELDNAME() {
		return this.getString(this.dbrow.Column("SFIELDNAME").getString());
	}

	/**
	 * 设置[计算字段名称]
	 * 
	 * @param SFIELDNAME
	 *            String
	 */
	public void setSFIELDNAME(String SFIELDNAME) {
		this.dbrow.Column("SFIELDNAME").setValue(SFIELDNAME);
	}

	/**
	 * 获取[计算字段显示名称]
	 * 
	 * @return String
	 */
	public String getSFIELDSHOWNAME() {
		return this.getString(this.dbrow.Column("SFIELDSHOWNAME").getString());
	}

	/**
	 * 设置[计算字段显示名称]
	 * 
	 * @param SFIELDSHOWNAME
	 *            String
	 */
	public void setSFIELDSHOWNAME(String SFIELDSHOWNAME) {
		this.dbrow.Column("SFIELDSHOWNAME").setValue(SFIELDSHOWNAME);
	}

	/**
	 * 获取[计算表达式]
	 * 
	 * @return String
	 */
	public String getEXPRESSIONS() {
		return this.getString(this.dbrow.Column("EXPRESSIONS").getString());
	}

	/**
	 * 设置[计算表达式]
	 * 
	 * @param EXPRESSIONS
	 *            String
	 */
	public void setEXPRESSIONS(String EXPRESSIONS) {
		this.dbrow.Column("EXPRESSIONS").setValue(EXPRESSIONS);
	}

	/**
	 * 获取[计算级别]
	 * 
	 * @return int
	 */
	public int getDISTINCTION() {
		return this.dbrow.Column("DISTINCTION").getInteger();
	}

	/**
	 * 设置[计算级别]
	 * 
	 * @param DISTINCTION
	 *            int
	 */
	public void setDISTINCTION(int DISTINCTION) {
		this.dbrow.Column("DISTINCTION").setValue(Integer.toString(DISTINCTION));
	}

	/**
	 * 获取[是否隐藏列]
	 * 
	 * @return String
	 */
	public String getISSHOW() {
		return this.getString(this.dbrow.Column("ISSHOW").getString());
	}

	/**
	 * 设置[是否隐藏列]
	 * 
	 * @param ISSHOW
	 *            String
	 */
	public void setISSHOW(String ISSHOW) {
		this.dbrow.Column("ISSHOW").setValue(ISSHOW);
	}

	/**
	 * 获取[表达式运算条件]
	 * 
	 * @return String
	 */
	public String getEXPRESSIONSWHERE() {
		return this.getString(this.dbrow.Column("EXPRESSIONSWHERE").getString());
	}

	/**
	 * 设置[表达式运算条件]
	 * 
	 * @param EXPRESSIONSWHERE
	 *            String
	 */
	public void setEXPRESSIONSWHERE(String EXPRESSIONSWHERE) {
		this.dbrow.Column("EXPRESSIONSWHERE").setValue(EXPRESSIONSWHERE);
	}

	/**
	 * 获取[附加符号]
	 * 
	 * @return String
	 */
	public String getADDSIGN() {
		return this.getString(this.dbrow.Column("ADDSIGN").getString());
	}

	/**
	 * 设置[附加符号]
	 * 
	 * @param ADDSIGN
	 *            String
	 */
	public void setADDSIGN(String ADDSIGN) {
		this.dbrow.Column("ADDSIGN").setValue(ADDSIGN);
	}

	/**
	 * 获取[小数点位数]
	 * 
	 * @return int
	 */
	public int getRADIXPOINT() {
		return this.dbrow.Column("RADIXPOINT").getInteger();
	}

	/**
	 * 设置[小数点位数]
	 * 
	 * @param RADIXPOINT
	 *            int
	 */
	public void setRADIXPOINT(int RADIXPOINT) {
		this.dbrow.Column("RADIXPOINT").setValue(Integer.toString(RADIXPOINT));
	}

	/**
	 * 获取[显示序号]
	 * 
	 * @return String
	 */
	public String getSHOWCODE() {
		return this.getString(this.dbrow.Column("SHOWCODE").getString());
	}

	/**
	 * 设置[显示序号]
	 * 
	 * @param SHOWCODE
	 *            String
	 */
	public void setSHOWCODE(String SHOWCODE) {
		this.dbrow.Column("SHOWCODE").setValue(SHOWCODE);
	}

	/**
	 * 获取[统计主表]
	 * 
	 * @return String
	 */
	public String getTABLEID() {
		return this.getString(this.dbrow.Column("TABLEID").getString());
	}

	/**
	 * 设置[统计主表]
	 * 
	 * @param TABLEID
	 *            String
	 */
	public void setTABLEID(String TABLEID) {
		this.dbrow.Column("TABLEID").setValue(TABLEID);
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
	}

	/**
	 * 获取[当前表与维数据对应的特殊关联设置]
	 * 
	 * @return String
	 */
	public String getCPLANARESPECIAL() {
		return this.getString(this.dbrow.Column("CPLANARESPECIAL").getString());
	}

	/**
	 * 设置[当前表与维数据对应的特殊关联设置]
	 * 
	 * @param CPLANARESPECIAL
	 *            String
	 */
	public void setCPLANARESPECIAL(String CPLANARESPECIAL) {
		this.dbrow.Column("CPLANARESPECIAL").setValue(CPLANARESPECIAL);
	}

	/**
	 * 获取[数据存放字段]
	 * 
	 * @return String
	 */
	public String getSAVEFIELD() {
		return this.getString(this.dbrow.Column("SAVEFIELD").getString());
	}

	/**
	 * 设置[数据存放字段]
	 * 
	 * @param SAVEFIELD
	 *            String
	 */
	public void setSAVEFIELD(String SAVEFIELD) {
		this.dbrow.Column("SAVEFIELD").setValue(SAVEFIELD);
	}

}
