package zr.zrpower.analyseengine;

import zr.zrpower.common.db.DBEntity;
import zr.zrpower.common.db.DBType;

/**
 * <p>
 * 数据分析运行类
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright (c)NFZR
 * </p>
 */

public class AnalyseRun extends DBEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 构造函数
	 */
	public AnalyseRun() {
		super();
		initialize();
	}

	/**
	 * 初始化函数
	 */
	protected void initialize() {
		this.dbrow.setTableName("AnalyseRun");
		this.dbrow.addColumn("P1", null, DBType.STRING); // 参数1
		this.dbrow.addColumn("P2", null, DBType.STRING); // 参数2
		this.dbrow.addColumn("P3", null, DBType.STRING); // 参数3
		this.dbrow.addColumn("P4", null, DBType.STRING); // 参数4
		this.dbrow.addColumn("P5", null, DBType.STRING); // 参数5
		this.dbrow.addColumn("ID", null, DBType.STRING); // 配置ID
		this.dbrow.addColumn("InitObject", null, DBType.STRING); // 分析对象
		this.dbrow.addColumn("Inittype", null, DBType.STRING); // 分析类型
		this.dbrow.addColumn("InitYear1", null, DBType.STRING); // 分析开始年
		this.dbrow.addColumn("InitYear2", null, DBType.STRING); // 分析结束年
		this.dbrow.addColumn("InitMonth", null, DBType.STRING); // 分析月
		this.dbrow.addColumn("InitDate1", null, DBType.STRING); // 分析自定义开始日期
		this.dbrow.addColumn("InitDate2", null, DBType.STRING); // 分析自定义结束日期
		this.dbrow.addColumn("InitValue", null, DBType.STRING); // 分析值
		this.dbrow.addColumn("InitAnalyseType", null, DBType.STRING);// 分析类别
	}

	public void setP1(String P1) {
		this.dbrow.Column("P1").setValue(P1);
	}

	public String getP1() {
		return this.dbrow.Column("P1").getString();
	}

	public void setP2(String P2) {
		this.dbrow.Column("P2").setValue(P2);
	}

	public String getP2() {
		return this.dbrow.Column("P2").getString();
	}

	public void setP3(String P3) {
		this.dbrow.Column("P3").setValue(P3);
	}

	public String getP3() {
		return this.dbrow.Column("P3").getString();
	}

	public void setP4(String P4) {
		this.dbrow.Column("P4").setValue(P4);
	}

	public String getP4() {
		return this.dbrow.Column("P4").getString();
	}

	public void setP5(String P5) {
		this.dbrow.Column("P5").setValue(P5);
	}

	public String getP5() {
		return this.dbrow.Column("P5").getString();
	}

	public void setID(String ID) {
		this.dbrow.Column("ID").setValue(ID);
	}

	public String getID() {
		return this.dbrow.Column("ID").getString();
	}

	public void setInitObject(String InitObject) {
		this.dbrow.Column("InitObject").setValue(InitObject);
	}

	public String getInitObject() {
		return this.dbrow.Column("InitObject").getString();
	}

	public void setInittype(String Inittype) {
		this.dbrow.Column("Inittype").setValue(Inittype);
	}

	public String getInittype() {
		return this.dbrow.Column("Inittype").getString();
	}

	public void setInitYear1(String InitYear1) {
		this.dbrow.Column("InitYear1").setValue(InitYear1);
	}

	public String getInitYear1() {
		return this.dbrow.Column("InitYear1").getString();
	}

	public void setInitYear2(String InitYear2) {
		this.dbrow.Column("InitYear2").setValue(InitYear2);
	}

	public String getInitYear2() {
		return this.dbrow.Column("InitYear2").getString();
	}

	public void setInitMonth(String InitMonth) {
		this.dbrow.Column("InitMonth").setValue(InitMonth);
	}

	public String getInitMonth() {
		return this.dbrow.Column("InitMonth").getString();
	}

	public void setInitDate1(String InitDate1) {
		this.dbrow.Column("InitDate1").setValue(InitDate1);
	}

	public String getInitDate1() {
		return this.dbrow.Column("InitDate1").getString();
	}

	public void setInitDate2(String InitDate2) {
		this.dbrow.Column("InitDate2").setValue(InitDate2);
	}

	public String getInitDate2() {
		return this.dbrow.Column("InitDate2").getString();
	}

	public void setInitValue(String InitValue) {
		this.dbrow.Column("InitValue").setValue(InitValue);
	}

	public String getInitValue() {
		return this.dbrow.Column("InitValue").getString();
	}

	public void setInitAnalyseType(String InitAnalyseType) {
		this.dbrow.Column("InitAnalyseType").setValue(InitAnalyseType);
	}

	public String getInitAnalyseType() {
		return this.dbrow.Column("InitAnalyseType").getString();
	}

	// ----------------------------------
	// 初始化流程运转参数
	public void initParam(String P1, String P2, String P3, String P4, String P5, String strID, String strobject,
			String strtype, String stryear1, String stryear2, String strmonth, String strdate1, String strdate2,
			String strsvalue, String AnalyseType) {
		setP1(P1);
		setP2(P2);
		setP3(P3);
		setP4(P4);
		setP5(P5);
		setID(strID);
		setInitObject(strobject);
		setInittype(strtype);
		setInitYear1(stryear1);
		setInitYear2(stryear2);
		setInitMonth(strmonth);
		setInitDate1(strdate1);
		setInitDate2(strdate2);
		setInitValue(strsvalue);
		setInitAnalyseType(AnalyseType);
	}
	// ----------------------------------
}