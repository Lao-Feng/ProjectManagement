package zr.zrpower.analyseengine.mode;

import zr.zrpower.common.db.DBEntity;
import zr.zrpower.common.db.DBType;

/**
 * <p>Title: ZRPower</p>
 * <p>
 * Description: 数据表实体映射包
 * </p>
 * <p>
 * 单元名称：ANALYSE_STATISTICS_RESULT.java
 * </p>
 * <p>
 * 中文解释：临时统计结果表数据实体类
 * </p>
 * <p>
 * 作用：将数据库表临时统计结果表映射为Java类，作为数据传输的载体。
 * </p>
 * 
 * @author Java实体生成器 By NFZR
 *
 *
 */
public class ANALYSE_STATISTICS_RESULT extends DBEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected String ID;
	protected String FID;
	protected String FIELD1;
	protected String FIELD2;
	protected String FIELD3;
	protected String FIELD4;
	protected String FIELD5;
	protected String FIELD6;
	protected String FIELD7;
	protected String FIELD8;
	protected String FIELD9;
	protected String FIELD10;
	protected String FIELD11;
	protected String FIELD12;
	protected String FIELD13;
	protected String FIELD14;
	protected String FIELD15;
	protected String FIELD16;
	protected String FIELD17;
	protected String FIELD18;
	protected String FIELD19;
	protected String FIELD20;
	protected String FIELD21;
	protected String FIELD22;
	protected String FIELD23;
	protected String FIELD24;
	protected String FIELD25;
	protected String FIELD26;
	protected String FIELD27;
	protected String FIELD28;
	protected String FIELD29;
	protected String FIELD30;
	protected String FIELD31;
	protected String FIELD32;
	protected String FIELD33;
	protected String FIELD34;
	protected String FIELD35;
	protected String FIELD36;
	protected String FIELD37;
	protected String FIELD38;
	protected String FIELD39;
	protected String FIELD40;
	protected String FIELD41;
	protected String FIELD42;
	protected String FIELD43;
	protected String FIELD44;
	protected String FIELD45;
	protected String FIELD46;
	protected String FIELD47;
	protected String FIELD48;
	protected String FIELD49;
	protected String FIELD50;
	protected String SUSERNO;

	/**
	 * 构造函数
	 */
	public ANALYSE_STATISTICS_RESULT() {
		super();
		initialize();
	}

	/**
	 * 初始化函数
	 */
	protected void initialize() {
		this.dbrow.setTableName("ANALYSE_STATISTICS_RESULT");
		this.dbrow.addColumn("ID", null, DBType.STRING); // 编号
		this.dbrow.addColumn("FID", null, DBType.STRING); // 统计配置表ID
		this.dbrow.addColumn("FIELD1", null, DBType.STRING); // 临时统计字段1
		this.dbrow.addColumn("FIELD2", null, DBType.STRING); // 临时统计字段2
		this.dbrow.addColumn("FIELD3", null, DBType.STRING); // 临时统计字段3
		this.dbrow.addColumn("FIELD4", null, DBType.STRING); // 临时统计字段4
		this.dbrow.addColumn("FIELD5", null, DBType.STRING); // 临时统计字段5
		this.dbrow.addColumn("FIELD6", null, DBType.STRING); // 临时统计字段6
		this.dbrow.addColumn("FIELD7", null, DBType.STRING); // 临时统计字段7
		this.dbrow.addColumn("FIELD8", null, DBType.STRING); // 临时统计字段8
		this.dbrow.addColumn("FIELD9", null, DBType.STRING); // 临时统计字段9
		this.dbrow.addColumn("FIELD10", null, DBType.STRING); // 临时统计字段10
		this.dbrow.addColumn("FIELD11", null, DBType.STRING); // 临时统计字段11
		this.dbrow.addColumn("FIELD12", null, DBType.STRING); // 临时统计字段12
		this.dbrow.addColumn("FIELD13", null, DBType.STRING); // 临时统计字段13
		this.dbrow.addColumn("FIELD14", null, DBType.STRING); // 临时统计字段14
		this.dbrow.addColumn("FIELD15", null, DBType.STRING); // 临时统计字段15
		this.dbrow.addColumn("FIELD16", null, DBType.STRING); // 临时统计字段16
		this.dbrow.addColumn("FIELD17", null, DBType.STRING); // 临时统计字段17
		this.dbrow.addColumn("FIELD18", null, DBType.STRING); // 临时统计字段18
		this.dbrow.addColumn("FIELD19", null, DBType.STRING); // 临时统计字段19
		this.dbrow.addColumn("FIELD20", null, DBType.STRING); // 临时统计字段20
		this.dbrow.addColumn("FIELD21", null, DBType.STRING); // 临时统计字段21
		this.dbrow.addColumn("FIELD22", null, DBType.STRING); // 临时统计字段22
		this.dbrow.addColumn("FIELD23", null, DBType.STRING); // 临时统计字段23
		this.dbrow.addColumn("FIELD24", null, DBType.STRING); // 临时统计字段24
		this.dbrow.addColumn("FIELD25", null, DBType.STRING); // 临时统计字段25
		this.dbrow.addColumn("FIELD26", null, DBType.STRING); // 临时统计字段26
		this.dbrow.addColumn("FIELD27", null, DBType.STRING); // 临时统计字段27
		this.dbrow.addColumn("FIELD28", null, DBType.STRING); // 临时统计字段28
		this.dbrow.addColumn("FIELD29", null, DBType.STRING); // 临时统计字段29
		this.dbrow.addColumn("FIELD30", null, DBType.STRING); // 临时统计字段30
		this.dbrow.addColumn("FIELD31", null, DBType.STRING); // 临时统计字段31
		this.dbrow.addColumn("FIELD32", null, DBType.STRING); // 临时统计字段32
		this.dbrow.addColumn("FIELD33", null, DBType.STRING); // 临时统计字段33
		this.dbrow.addColumn("FIELD34", null, DBType.STRING); // 临时统计字段34
		this.dbrow.addColumn("FIELD35", null, DBType.STRING); // 临时统计字段35
		this.dbrow.addColumn("FIELD36", null, DBType.STRING); // 临时统计字段36
		this.dbrow.addColumn("FIELD37", null, DBType.STRING); // 临时统计字段37
		this.dbrow.addColumn("FIELD38", null, DBType.STRING); // 临时统计字段38
		this.dbrow.addColumn("FIELD39", null, DBType.STRING); // 临时统计字段39
		this.dbrow.addColumn("FIELD40", null, DBType.STRING); // 临时统计字段40
		this.dbrow.addColumn("FIELD41", null, DBType.STRING); // 临时统计字段41
		this.dbrow.addColumn("FIELD42", null, DBType.STRING); // 临时统计字段42
		this.dbrow.addColumn("FIELD43", null, DBType.STRING); // 临时统计字段43
		this.dbrow.addColumn("FIELD44", null, DBType.STRING); // 临时统计字段44
		this.dbrow.addColumn("FIELD45", null, DBType.STRING); // 临时统计字段45
		this.dbrow.addColumn("FIELD46", null, DBType.STRING); // 临时统计字段46
		this.dbrow.addColumn("FIELD47", null, DBType.STRING); // 临时统计字段47
		this.dbrow.addColumn("FIELD48", null, DBType.STRING); // 临时统计字段48
		this.dbrow.addColumn("FIELD49", null, DBType.STRING); // 临时统计字段49
		this.dbrow.addColumn("FIELD50", null, DBType.STRING); // 临时统计字段50
		this.dbrow.addColumn("SUSERNO", null, DBType.STRING); // 统计人编号
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
	 * 获取[临时统计字段1]
	 * 
	 * @return String
	 */
	public String getFIELD1() {
		return this.getString(this.dbrow.Column("FIELD1").getString());
	}

	/**
	 * 设置[临时统计字段1]
	 * 
	 * @param FIELD1
	 *            String
	 */
	public void setFIELD1(String FIELD1) {
		this.dbrow.Column("FIELD1").setValue(FIELD1);
	}

	/**
	 * 获取[临时统计字段2]
	 * 
	 * @return String
	 */
	public String getFIELD2() {
		return this.getString(this.dbrow.Column("FIELD2").getString());
	}

	/**
	 * 设置[临时统计字段2]
	 * 
	 * @param FIELD2
	 *            String
	 */
	public void setFIELD2(String FIELD2) {
		this.dbrow.Column("FIELD2").setValue(FIELD2);
	}

	/**
	 * 获取[临时统计字段3]
	 * 
	 * @return String
	 */
	public String getFIELD3() {
		return this.getString(this.dbrow.Column("FIELD3").getString());
	}

	/**
	 * 设置[临时统计字段3]
	 * 
	 * @param FIELD3
	 *            String
	 */
	public void setFIELD3(String FIELD3) {
		this.dbrow.Column("FIELD3").setValue(FIELD3);
	}

	/**
	 * 获取[临时统计字段4]
	 * 
	 * @return String
	 */
	public String getFIELD4() {
		return this.getString(this.dbrow.Column("FIELD4").getString());
	}

	/**
	 * 设置[临时统计字段4]
	 * 
	 * @param FIELD4
	 *            String
	 */
	public void setFIELD4(String FIELD4) {
		this.dbrow.Column("FIELD4").setValue(FIELD4);
	}

	/**
	 * 获取[临时统计字段5]
	 * 
	 * @return String
	 */
	public String getFIELD5() {
		return this.getString(this.dbrow.Column("FIELD5").getString());
	}

	/**
	 * 设置[临时统计字段5]
	 * 
	 * @param FIELD5
	 *            String
	 */
	public void setFIELD5(String FIELD5) {
		this.dbrow.Column("FIELD5").setValue(FIELD5);
	}

	/**
	 * 获取[临时统计字段6]
	 * 
	 * @return String
	 */
	public String getFIELD6() {
		return this.getString(this.dbrow.Column("FIELD6").getString());
	}

	/**
	 * 设置[临时统计字段6]
	 * 
	 * @param FIELD6
	 *            String
	 */
	public void setFIELD6(String FIELD6) {
		this.dbrow.Column("FIELD6").setValue(FIELD6);
	}

	/**
	 * 获取[临时统计字段7]
	 * 
	 * @return String
	 */
	public String getFIELD7() {
		return this.getString(this.dbrow.Column("FIELD7").getString());
	}

	/**
	 * 设置[临时统计字段7]
	 * 
	 * @param FIELD7
	 *            String
	 */
	public void setFIELD7(String FIELD7) {
		this.dbrow.Column("FIELD7").setValue(FIELD7);
	}

	/**
	 * 获取[临时统计字段8]
	 * 
	 * @return String
	 */
	public String getFIELD8() {
		return this.getString(this.dbrow.Column("FIELD8").getString());
	}

	/**
	 * 设置[临时统计字段8]
	 * 
	 * @param FIELD8
	 *            String
	 */
	public void setFIELD8(String FIELD8) {
		this.dbrow.Column("FIELD8").setValue(FIELD8);
	}

	/**
	 * 获取[临时统计字段9]
	 * 
	 * @return String
	 */
	public String getFIELD9() {
		return this.getString(this.dbrow.Column("FIELD9").getString());
	}

	/**
	 * 设置[临时统计字段9]
	 * 
	 * @param FIELD9
	 *            String
	 */
	public void setFIELD9(String FIELD9) {
		this.dbrow.Column("FIELD9").setValue(FIELD9);
	}

	/**
	 * 获取[临时统计字段10]
	 * 
	 * @return String
	 */
	public String getFIELD10() {
		return this.getString(this.dbrow.Column("FIELD10").getString());
	}

	/**
	 * 设置[临时统计字段10]
	 * 
	 * @param FIELD10
	 *            String
	 */
	public void setFIELD10(String FIELD10) {
		this.dbrow.Column("FIELD10").setValue(FIELD10);
	}

	/**
	 * 获取[临时统计字段11]
	 * 
	 * @return String
	 */
	public String getFIELD11() {
		return this.getString(this.dbrow.Column("FIELD11").getString());
	}

	/**
	 * 设置[临时统计字段11]
	 * 
	 * @param FIELD11
	 *            String
	 */
	public void setFIELD11(String FIELD11) {
		this.dbrow.Column("FIELD11").setValue(FIELD11);
	}

	/**
	 * 获取[临时统计字段12]
	 * 
	 * @return String
	 */
	public String getFIELD12() {
		return this.getString(this.dbrow.Column("FIELD12").getString());
	}

	/**
	 * 设置[临时统计字段12]
	 * 
	 * @param FIELD12
	 *            String
	 */
	public void setFIELD12(String FIELD12) {
		this.dbrow.Column("FIELD12").setValue(FIELD12);
	}

	/**
	 * 获取[临时统计字段13]
	 * 
	 * @return String
	 */
	public String getFIELD13() {
		return this.getString(this.dbrow.Column("FIELD13").getString());
	}

	/**
	 * 设置[临时统计字段13]
	 * 
	 * @param FIELD13
	 *            String
	 */
	public void setFIELD13(String FIELD13) {
		this.dbrow.Column("FIELD13").setValue(FIELD13);
	}

	/**
	 * 获取[临时统计字段14]
	 * 
	 * @return String
	 */
	public String getFIELD14() {
		return this.getString(this.dbrow.Column("FIELD14").getString());
	}

	/**
	 * 设置[临时统计字段14]
	 * 
	 * @param FIELD14
	 *            String
	 */
	public void setFIELD14(String FIELD14) {
		this.dbrow.Column("FIELD14").setValue(FIELD14);
	}

	/**
	 * 获取[临时统计字段15]
	 * 
	 * @return String
	 */
	public String getFIELD15() {
		return this.getString(this.dbrow.Column("FIELD15").getString());
	}

	/**
	 * 设置[临时统计字段15]
	 * 
	 * @param FIELD15
	 *            String
	 */
	public void setFIELD15(String FIELD15) {
		this.dbrow.Column("FIELD15").setValue(FIELD15);
	}

	/**
	 * 获取[临时统计字段16]
	 * 
	 * @return String
	 */
	public String getFIELD16() {
		return this.getString(this.dbrow.Column("FIELD16").getString());
	}

	/**
	 * 设置[临时统计字段16]
	 * 
	 * @param FIELD16
	 *            String
	 */
	public void setFIELD16(String FIELD16) {
		this.dbrow.Column("FIELD16").setValue(FIELD16);
	}

	/**
	 * 获取[临时统计字段17]
	 * 
	 * @return String
	 */
	public String getFIELD17() {
		return this.getString(this.dbrow.Column("FIELD17").getString());
	}

	/**
	 * 设置[临时统计字段17]
	 * 
	 * @param FIELD17
	 *            String
	 */
	public void setFIELD17(String FIELD17) {
		this.dbrow.Column("FIELD17").setValue(FIELD17);
	}

	/**
	 * 获取[临时统计字段18]
	 * 
	 * @return String
	 */
	public String getFIELD18() {
		return this.getString(this.dbrow.Column("FIELD18").getString());
	}

	/**
	 * 设置[临时统计字段18]
	 * 
	 * @param FIELD18
	 *            String
	 */
	public void setFIELD18(String FIELD18) {
		this.dbrow.Column("FIELD18").setValue(FIELD18);
	}

	/**
	 * 获取[临时统计字段19]
	 * 
	 * @return String
	 */
	public String getFIELD19() {
		return this.getString(this.dbrow.Column("FIELD19").getString());
	}

	/**
	 * 设置[临时统计字段19]
	 * 
	 * @param FIELD19
	 *            String
	 */
	public void setFIELD19(String FIELD19) {
		this.dbrow.Column("FIELD19").setValue(FIELD19);
	}

	/**
	 * 获取[临时统计字段20]
	 * 
	 * @return String
	 */
	public String getFIELD20() {
		return this.getString(this.dbrow.Column("FIELD20").getString());
	}

	/**
	 * 设置[临时统计字段20]
	 * 
	 * @param FIELD20
	 *            String
	 */
	public void setFIELD20(String FIELD20) {
		this.dbrow.Column("FIELD20").setValue(FIELD20);
	}

	/**
	 * 获取[临时统计字段21]
	 * 
	 * @return String
	 */
	public String getFIELD21() {
		return this.getString(this.dbrow.Column("FIELD21").getString());
	}

	/**
	 * 设置[临时统计字段21]
	 * 
	 * @param FIELD21
	 *            String
	 */
	public void setFIELD21(String FIELD21) {
		this.dbrow.Column("FIELD21").setValue(FIELD21);
	}

	/**
	 * 获取[临时统计字段22]
	 * 
	 * @return String
	 */
	public String getFIELD22() {
		return this.getString(this.dbrow.Column("FIELD22").getString());
	}

	/**
	 * 设置[临时统计字段22]
	 * 
	 * @param FIELD22
	 *            String
	 */
	public void setFIELD22(String FIELD22) {
		this.dbrow.Column("FIELD22").setValue(FIELD22);
	}

	/**
	 * 获取[临时统计字段23]
	 * 
	 * @return String
	 */
	public String getFIELD23() {
		return this.getString(this.dbrow.Column("FIELD23").getString());
	}

	/**
	 * 设置[临时统计字段23]
	 * 
	 * @param FIELD23
	 *            String
	 */
	public void setFIELD23(String FIELD23) {
		this.dbrow.Column("FIELD23").setValue(FIELD23);
	}

	/**
	 * 获取[临时统计字段24]
	 * 
	 * @return String
	 */
	public String getFIELD24() {
		return this.getString(this.dbrow.Column("FIELD24").getString());
	}

	/**
	 * 设置[临时统计字段24]
	 * 
	 * @param FIELD24
	 *            String
	 */
	public void setFIELD24(String FIELD24) {
		this.dbrow.Column("FIELD24").setValue(FIELD24);
	}

	/**
	 * 获取[临时统计字段25]
	 * 
	 * @return String
	 */
	public String getFIELD25() {
		return this.getString(this.dbrow.Column("FIELD25").getString());
	}

	/**
	 * 设置[临时统计字段25]
	 * 
	 * @param FIELD25
	 *            String
	 */
	public void setFIELD25(String FIELD25) {
		this.dbrow.Column("FIELD25").setValue(FIELD25);
	}

	/**
	 * 获取[临时统计字段26]
	 * 
	 * @return String
	 */
	public String getFIELD26() {
		return this.getString(this.dbrow.Column("FIELD26").getString());
	}

	/**
	 * 设置[临时统计字段26]
	 * 
	 * @param FIELD26
	 *            String
	 */
	public void setFIELD26(String FIELD26) {
		this.dbrow.Column("FIELD26").setValue(FIELD26);
	}

	/**
	 * 获取[临时统计字段27]
	 * 
	 * @return String
	 */
	public String getFIELD27() {
		return this.getString(this.dbrow.Column("FIELD27").getString());
	}

	/**
	 * 设置[临时统计字段27]
	 * 
	 * @param FIELD27
	 *            String
	 */
	public void setFIELD27(String FIELD27) {
		this.dbrow.Column("FIELD27").setValue(FIELD27);
	}

	/**
	 * 获取[临时统计字段28]
	 * 
	 * @return String
	 */
	public String getFIELD28() {
		return this.getString(this.dbrow.Column("FIELD28").getString());
	}

	/**
	 * 设置[临时统计字段28]
	 * 
	 * @param FIELD28
	 *            String
	 */
	public void setFIELD28(String FIELD28) {
		this.dbrow.Column("FIELD28").setValue(FIELD28);
	}

	/**
	 * 获取[临时统计字段29]
	 * 
	 * @return String
	 */
	public String getFIELD29() {
		return this.getString(this.dbrow.Column("FIELD29").getString());
	}

	/**
	 * 设置[临时统计字段29]
	 * 
	 * @param FIELD29
	 *            String
	 */
	public void setFIELD29(String FIELD29) {
		this.dbrow.Column("FIELD29").setValue(FIELD29);
	}

	/**
	 * 获取[临时统计字段30]
	 * 
	 * @return String
	 */
	public String getFIELD30() {
		return this.getString(this.dbrow.Column("FIELD30").getString());
	}

	/**
	 * 设置[临时统计字段30]
	 * 
	 * @param FIELD30
	 *            String
	 */
	public void setFIELD30(String FIELD30) {
		this.dbrow.Column("FIELD30").setValue(FIELD30);
	}

	/**
	 * 获取[临时统计字段31]
	 * 
	 * @return String
	 */
	public String getFIELD31() {
		return this.getString(this.dbrow.Column("FIELD31").getString());
	}

	/**
	 * 设置[临时统计字段31]
	 * 
	 * @param FIELD31
	 *            String
	 */
	public void setFIELD31(String FIELD31) {
		this.dbrow.Column("FIELD31").setValue(FIELD31);
	}

	/**
	 * 获取[临时统计字段32]
	 * 
	 * @return String
	 */
	public String getFIELD32() {
		return this.getString(this.dbrow.Column("FIELD32").getString());
	}

	/**
	 * 设置[临时统计字段32]
	 * 
	 * @param FIELD32
	 *            String
	 */
	public void setFIELD32(String FIELD32) {
		this.dbrow.Column("FIELD32").setValue(FIELD32);
	}

	/**
	 * 获取[临时统计字段33]
	 * 
	 * @return String
	 */
	public String getFIELD33() {
		return this.getString(this.dbrow.Column("FIELD33").getString());
	}

	/**
	 * 设置[临时统计字段33]
	 * 
	 * @param FIELD33
	 *            String
	 */
	public void setFIELD33(String FIELD33) {
		this.dbrow.Column("FIELD33").setValue(FIELD33);
	}

	/**
	 * 获取[临时统计字段34]
	 * 
	 * @return String
	 */
	public String getFIELD34() {
		return this.getString(this.dbrow.Column("FIELD34").getString());
	}

	/**
	 * 设置[临时统计字段34]
	 * 
	 * @param FIELD34
	 *            String
	 */
	public void setFIELD34(String FIELD34) {
		this.dbrow.Column("FIELD34").setValue(FIELD34);
	}

	/**
	 * 获取[临时统计字段35]
	 * 
	 * @return String
	 */
	public String getFIELD35() {
		return this.getString(this.dbrow.Column("FIELD35").getString());
	}

	/**
	 * 设置[临时统计字段35]
	 * 
	 * @param FIELD35
	 *            String
	 */
	public void setFIELD35(String FIELD35) {
		this.dbrow.Column("FIELD35").setValue(FIELD35);
	}

	/**
	 * 获取[临时统计字段36]
	 * 
	 * @return String
	 */
	public String getFIELD36() {
		return this.getString(this.dbrow.Column("FIELD36").getString());
	}

	/**
	 * 设置[临时统计字段36]
	 * 
	 * @param FIELD36
	 *            String
	 */
	public void setFIELD36(String FIELD36) {
		this.dbrow.Column("FIELD36").setValue(FIELD36);
	}

	/**
	 * 获取[临时统计字段37]
	 * 
	 * @return String
	 */
	public String getFIELD37() {
		return this.getString(this.dbrow.Column("FIELD37").getString());
	}

	/**
	 * 设置[临时统计字段37]
	 * 
	 * @param FIELD37
	 *            String
	 */
	public void setFIELD37(String FIELD37) {
		this.dbrow.Column("FIELD37").setValue(FIELD37);
	}

	/**
	 * 获取[临时统计字段38]
	 * 
	 * @return String
	 */
	public String getFIELD38() {
		return this.getString(this.dbrow.Column("FIELD38").getString());
	}

	/**
	 * 设置[临时统计字段38]
	 * 
	 * @param FIELD38
	 *            String
	 */
	public void setFIELD38(String FIELD38) {
		this.dbrow.Column("FIELD38").setValue(FIELD38);
	}

	/**
	 * 获取[临时统计字段39]
	 * 
	 * @return String
	 */
	public String getFIELD39() {
		return this.getString(this.dbrow.Column("FIELD39").getString());
	}

	/**
	 * 设置[临时统计字段39]
	 * 
	 * @param FIELD39
	 *            String
	 */
	public void setFIELD39(String FIELD39) {
		this.dbrow.Column("FIELD39").setValue(FIELD39);
	}

	/**
	 * 获取[临时统计字段40]
	 * 
	 * @return String
	 */
	public String getFIELD40() {
		return this.getString(this.dbrow.Column("FIELD40").getString());
	}

	/**
	 * 设置[临时统计字段40]
	 * 
	 * @param FIELD40
	 *            String
	 */
	public void setFIELD40(String FIELD40) {
		this.dbrow.Column("FIELD40").setValue(FIELD40);
	}

	/**
	 * 获取[临时统计字段41]
	 * 
	 * @return String
	 */
	public String getFIELD41() {
		return this.getString(this.dbrow.Column("FIELD41").getString());
	}

	/**
	 * 设置[临时统计字段41]
	 * 
	 * @param FIELD41
	 *            String
	 */
	public void setFIELD41(String FIELD41) {
		this.dbrow.Column("FIELD41").setValue(FIELD41);
	}

	/**
	 * 获取[临时统计字段42]
	 * 
	 * @return String
	 */
	public String getFIELD42() {
		return this.getString(this.dbrow.Column("FIELD42").getString());
	}

	/**
	 * 设置[临时统计字段42]
	 * 
	 * @param FIELD42
	 *            String
	 */
	public void setFIELD42(String FIELD42) {
		this.dbrow.Column("FIELD42").setValue(FIELD42);
	}

	/**
	 * 获取[临时统计字段43]
	 * 
	 * @return String
	 */
	public String getFIELD43() {
		return this.getString(this.dbrow.Column("FIELD43").getString());
	}

	/**
	 * 设置[临时统计字段43]
	 * 
	 * @param FIELD43
	 *            String
	 */
	public void setFIELD43(String FIELD43) {
		this.dbrow.Column("FIELD43").setValue(FIELD43);
	}

	/**
	 * 获取[临时统计字段44]
	 * 
	 * @return String
	 */
	public String getFIELD44() {
		return this.getString(this.dbrow.Column("FIELD44").getString());
	}

	/**
	 * 设置[临时统计字段44]
	 * 
	 * @param FIELD44
	 *            String
	 */
	public void setFIELD44(String FIELD44) {
		this.dbrow.Column("FIELD44").setValue(FIELD44);
	}

	/**
	 * 获取[临时统计字段45]
	 * 
	 * @return String
	 */
	public String getFIELD45() {
		return this.getString(this.dbrow.Column("FIELD45").getString());
	}

	/**
	 * 设置[临时统计字段45]
	 * 
	 * @param FIELD45
	 *            String
	 */
	public void setFIELD45(String FIELD45) {
		this.dbrow.Column("FIELD45").setValue(FIELD45);
	}

	/**
	 * 获取[临时统计字段46]
	 * 
	 * @return String
	 */
	public String getFIELD46() {
		return this.getString(this.dbrow.Column("FIELD46").getString());
	}

	/**
	 * 设置[临时统计字段46]
	 * 
	 * @param FIELD46
	 *            String
	 */
	public void setFIELD46(String FIELD46) {
		this.dbrow.Column("FIELD46").setValue(FIELD46);
	}

	/**
	 * 获取[临时统计字段47]
	 * 
	 * @return String
	 */
	public String getFIELD47() {
		return this.getString(this.dbrow.Column("FIELD47").getString());
	}

	/**
	 * 设置[临时统计字段47]
	 * 
	 * @param FIELD47
	 *            String
	 */
	public void setFIELD47(String FIELD47) {
		this.dbrow.Column("FIELD47").setValue(FIELD47);
	}

	/**
	 * 获取[临时统计字段48]
	 * 
	 * @return String
	 */
	public String getFIELD48() {
		return this.getString(this.dbrow.Column("FIELD48").getString());
	}

	/**
	 * 设置[临时统计字段48]
	 * 
	 * @param FIELD48
	 *            String
	 */
	public void setFIELD48(String FIELD48) {
		this.dbrow.Column("FIELD48").setValue(FIELD48);
	}

	/**
	 * 获取[临时统计字段49]
	 * 
	 * @return String
	 */
	public String getFIELD49() {
		return this.getString(this.dbrow.Column("FIELD49").getString());
	}

	/**
	 * 设置[临时统计字段49]
	 * 
	 * @param FIELD49
	 *            String
	 */
	public void setFIELD49(String FIELD49) {
		this.dbrow.Column("FIELD49").setValue(FIELD49);
	}

	/**
	 * 获取[临时统计字段50]
	 * 
	 * @return String
	 */
	public String getFIELD50() {
		return this.getString(this.dbrow.Column("FIELD50").getString());
	}

	/**
	 * 设置[临时统计字段50]
	 * 
	 * @param FIELD50
	 *            String
	 */
	public void setFIELD50(String FIELD50) {
		this.dbrow.Column("FIELD50").setValue(FIELD50);
	}

	/**
	 * 获取[统计人编号]
	 * 
	 * @return String
	 */
	public String getSUSERNO() {
		return this.getString(this.dbrow.Column("SUSERNO").getString());
	}

	/**
	 * 设置[统计人编号]
	 * 
	 * @param SUSERNO
	 *            String
	 */
	public void setSUSERNO(String SUSERNO) {
		this.dbrow.Column("SUSERNO").setValue(SUSERNO);
	}
}