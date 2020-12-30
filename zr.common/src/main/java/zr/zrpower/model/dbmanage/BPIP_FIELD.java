package zr.zrpower.model.dbmanage;

import zr.zrpower.common.db.DBEntity;
import zr.zrpower.common.db.DBType;

/**
 * <p>Title: ZRPower</p>
 * <p>
 * Description: 数据表实体映射包
 * </p>
 * <p>
 * 单元名称：BPIP_FIELD.java
 * </p>
 * <p>
 * 中文解释：字段名表数据实体类
 * </p>
 * <p>
 * 作用：将数据库表字段名表映射为Java类，作为数据传输的载体。
 * </p>
 * @author Java实体生成器 By NFZR
 * 
 */
public class BPIP_FIELD extends DBEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected String FIELDID;// 字段编号
	protected String TABLEID;// 表编号
	protected String FIELDNAME;// 字段名
	protected String FIELDTAG;// 特殊属性
	protected String CHINESENAME;// 中文名称
	protected String FIELDTYPE;
	protected int FIELDLENGTH;
	protected String ISNULL;
	protected String DICTTABLE;
	protected String DESCRIPTION;
	protected int TAGEXT;
	protected String AUTO1;
	protected String AUTO2;
	protected String AUTO3;
	protected int BLOBSIZE;
	protected String QFIELD;
	protected String ISKEY;
	
	//新增字段
	protected String INITVALUE;//默认值
	
	//业务要求，数据库无字段
	protected String TABLENAME;//数据库表英文名称
	protected String FIELDTYPENAME;//字段数据库类型名称
	protected String FIELDNAMEOLD;//字段修改前的字段名称

	/**
	 * 构造函数
	 */
	public BPIP_FIELD() {
		super();
		initialize();
	}

	/**
	 * 初始化函数
	 */
	protected void initialize() {
		this.dbrow.setTableName("BPIP_FIELD");
		this.dbrow.addColumn("FIELDID", null, DBType.STRING); // 字段编号
		this.dbrow.addColumn("TABLEID", null, DBType.STRING); // 表编号
		this.dbrow.addColumn("FIELDNAME", null, DBType.STRING); // 字段名
		this.dbrow.addColumn("FIELDTAG", null, DBType.STRING); // 特殊属性
		this.dbrow.addColumn("CHINESENAME", null, DBType.STRING); // 中文名称
		this.dbrow.addColumn("FIELDTYPE", null, DBType.STRING); // 字段类型
		this.dbrow.addColumn("FIELDLENGTH", null, DBType.LONG); // 字段长度
		this.dbrow.addColumn("ISNULL", null, DBType.STRING); // 是否可空
		this.dbrow.addColumn("DICTTABLE", null, DBType.STRING); // 对应字典表
		this.dbrow.addColumn("DESCRIPTION", null, DBType.STRING); // 关联
		this.dbrow.addColumn("TAGEXT", null, DBType.LONG); // 扩展属性
		this.dbrow.addColumn("AUTO1", null, DBType.STRING); // 自动编号路径1
		this.dbrow.addColumn("AUTO2", null, DBType.STRING); // 自动编号路径2
		this.dbrow.addColumn("AUTO3", null, DBType.STRING); // 自动编号路径3
		this.dbrow.addColumn("BLOBSIZE", null, DBType.LONG); // 照片大小控制
		this.dbrow.addColumn("QFIELD", null, DBType.STRING); // 查询关联字段
		this.dbrow.addColumn("ISKEY", null, DBType.STRING); // 是否主键
		this.dbrow.addColumn("INITVALUE", null, DBType.STRING); // 默认值
		
		this.dbrow.setPrimaryKey("FIELDID");
	}

	/**
	 * 获取[字段编号]
	 * 
	 * @return String
	 */
	public String getFIELDID() {
		return this.getString(this.dbrow.Column("FIELDID").getString());
	}

	/**
	 * 设置[字段编号]
	 * 
	 * @param FIELDID
	 *            String
	 */
	public void setFIELDID(String FIELDID) {
		this.dbrow.Column("FIELDID").setValue(FIELDID);
		this.FIELDID = FIELDID;
	}

	/**
	 * 获取[表编号]
	 * 
	 * @return String
	 */
	public String getTABLEID() {
		return this.getString(this.dbrow.Column("TABLEID").getString());
	}

	/**
	 * 设置[表编号]
	 * 
	 * @param TABLEID
	 *            String
	 */
	public void setTABLEID(String TABLEID) {
		this.dbrow.Column("TABLEID").setValue(TABLEID);
		this.TABLEID = TABLEID;
	}

	/**
	 * 获取[字段名]
	 * 
	 * @return String
	 */
	public String getFIELDNAME() {
		return this.getString(this.dbrow.Column("FIELDNAME").getString());
	}

	/**
	 * 设置[字段名]
	 * 
	 * @param FIELDNAME
	 *            String
	 */
	public void setFIELDNAME(String FIELDNAME) {
		this.dbrow.Column("FIELDNAME").setValue(FIELDNAME);
		this.FIELDNAME = FIELDNAME;
	}

	/**
	 * 获取[特殊属性]
	 * 
	 * @return String
	 */
	public String getFIELDTAG() {
		return this.getString(this.dbrow.Column("FIELDTAG").getString());
	}

	/**
	 * 设置[特殊属性]
	 * 
	 * @param FIELDTAG
	 *            String
	 */
	public void setFIELDTAG(String FIELDTAG) {
		this.dbrow.Column("FIELDTAG").setValue(FIELDTAG);
		this.FIELDTAG = FIELDTAG;
	}

	/**
	 * 获取[中文名称]
	 * 
	 * @return String
	 */
	public String getCHINESENAME() {
		return this.getString(this.dbrow.Column("CHINESENAME").getString());
	}

	/**
	 * 设置[中文名称]
	 * 
	 * @param CHINESENAME
	 *            String
	 */
	public void setCHINESENAME(String CHINESENAME) {
		this.dbrow.Column("CHINESENAME").setValue(CHINESENAME);
		this.CHINESENAME = CHINESENAME;
	}

	/**
	 * 获取[字段类型]
	 * 
	 * @return String
	 */
	public String getFIELDTYPE() {
		return this.getString(this.dbrow.Column("FIELDTYPE").getString());
	}

	/**
	 * 设置[字段类型]
	 * 
	 * @param FIELDTYPE
	 *            String
	 */
	public void setFIELDTYPE(String FIELDTYPE) {
		this.dbrow.Column("FIELDTYPE").setValue(FIELDTYPE);
		this.FIELDTYPE = FIELDTYPE;
	}

	/**
	 * 获取[字段长度]
	 * 
	 * @return int
	 */
	public int getFIELDLENGTH() {
		return this.dbrow.Column("FIELDLENGTH").getInteger();
	}

	/**
	 * 设置[字段长度]
	 * 
	 * @param FIELDLENGTH
	 *            int
	 */
	public void setFIELDLENGTH(int FIELDLENGTH) {
		this.dbrow.Column("FIELDLENGTH").setValue(Integer.toString(FIELDLENGTH));
		this.FIELDLENGTH = FIELDLENGTH;
	}

	/**
	 * 获取[是否可空]
	 * 
	 * @return String
	 */
	public String getISNULL() {
		return this.getString(this.dbrow.Column("ISNULL").getString());
	}

	/**
	 * 设置[是否可空]
	 * 
	 * @param ISNULL
	 *            String
	 */
	public void setISNULL(String ISNULL) {
		this.dbrow.Column("ISNULL").setValue(ISNULL);
		this.ISNULL = ISNULL;
	}

	/**
	 * 获取[对应字典表]
	 * 
	 * @return String
	 */
	public String getDICTTABLE() {
		return this.getString(this.dbrow.Column("DICTTABLE").getString());
	}

	/**
	 * 设置[对应字典表]
	 * 
	 * @param DICTTABLE
	 *            String
	 */
	public void setDICTTABLE(String DICTTABLE) {
		this.dbrow.Column("DICTTABLE").setValue(DICTTABLE);
		this.DICTTABLE = DICTTABLE;
	}

	/**
	 * 获取[描述]
	 * 
	 * @return String
	 */
	public String getDESCRIPTION() {
		return this.getString(this.dbrow.Column("DESCRIPTION").getString());
	}

	/**
	 * 设置[描述]
	 * 
	 * @param DESCRIPTION
	 *            String
	 */
	public void setDESCRIPTION(String DESCRIPTION) {
		this.dbrow.Column("DESCRIPTION").setValue(DESCRIPTION);
		this.DESCRIPTION = DESCRIPTION;
	}

	/**
	 * 获取[扩展属性]
	 * 
	 * @return int
	 */
	public int getTAGEXT() {
		return this.dbrow.Column("TAGEXT").getInteger();
	}

	/**
	 * 设置[扩展属性]
	 * 
	 * @param TAGEXT
	 *            int
	 */
	public void setTAGEXT(int TAGEXT) {
		this.dbrow.Column("TAGEXT").setValue(Integer.toString(TAGEXT));
		this.TAGEXT = TAGEXT;
	}

	/**
	 * 获取[自动编号路径1]
	 * 
	 * @return String
	 */
	public String getAUTO1() {
		return this.getString(this.dbrow.Column("AUTO1").getString());
	}

	/**
	 * 设置[自动编号路径1]
	 * 
	 * @param DESCRIPTION
	 *            String
	 */
	public void setAUTO1(String AUTO1) {
		this.dbrow.Column("AUTO1").setValue(AUTO1);
		this.AUTO1 = AUTO1;
	}

	/**
	 * 获取[自动编号路径2]
	 * 
	 * @return String
	 */
	public String getAUTO2() {
		return this.getString(this.dbrow.Column("AUTO2").getString());
	}

	/**
	 * 设置[自动编号路径2]
	 * 
	 * @param DESCRIPTION
	 *            String
	 */
	public void setAUTO2(String AUTO2) {
		this.dbrow.Column("AUTO2").setValue(AUTO2);
		this.AUTO2 = AUTO2;
	}

	/**
	 * 获取[自动编号路径3]
	 * 
	 * @return String
	 */
	public String getAUTO3() {
		return this.getString(this.dbrow.Column("AUTO3").getString());
	}

	/**
	 * 设置[自动编号路径3]
	 * 
	 * @param DESCRIPTION
	 *            String
	 */
	public void setAUTO3(String AUTO3) {
		this.dbrow.Column("AUTO3").setValue(AUTO3);
		this.AUTO3 = AUTO3;
	}

	/**
	 * 获取[照片大小控制]
	 * 
	 * @return int
	 */
	public int getBLOBSIZE() {
		return this.dbrow.Column("BLOBSIZE").getInteger();
	}

	/**
	 * 设置[照片大小控制]
	 * 
	 * @param BLOBSIZE
	 *            int
	 */
	public void setBLOBSIZE(int BLOBSIZE) {
		this.dbrow.Column("BLOBSIZE").setValue(Integer.toString(BLOBSIZE));
		this.BLOBSIZE = BLOBSIZE;
	}

	/**
	 * 获取[查询关联字段]
	 * 
	 * @return String
	 */
	public String getQFIELD() {
		return this.getString(this.dbrow.Column("QFIELD").getString());
	}

	/**
	 * 设置[查询关联字段]
	 * 
	 * @param QFIELD
	 *            String
	 */
	public void setQFIELD(String QFIELD) {
		this.dbrow.Column("QFIELD").setValue(QFIELD);
		this.QFIELD = QFIELD;
	}

	/**
	 * 获取[是否主键字段]
	 * 
	 * @return String
	 */
	public String getISKEY() {
		return this.getString(this.dbrow.Column("ISKEY").getString());
	}

	/**
	 * 设置[是否主键字段]
	 * 
	 * @param ISKEY
	 *            String
	 */
	public void setISKEY(String ISKEY) {
		this.dbrow.Column("ISKEY").setValue(ISKEY);
		this.ISKEY = ISKEY;
	}
	/**
	 * 获取[默认值]
	 * 
	 * @return String
	 */
	public String getINITVALUE() {
		return this.getString(this.dbrow.Column("INITVALUE").getString());
	}

	/**
	 * 设置[默认值]
	 * 
	 * @param ISKEY
	 *            String
	 */
	public void setINITVALUE(String INITVALUE) {
		this.dbrow.Column("INITVALUE").setValue(INITVALUE);
		this.INITVALUE = INITVALUE;
	}
	
	

	public String getTABLENAME() {
		return TABLENAME;
	}

	public void setTABLENAME(String tABLENAME) {
		TABLENAME = tABLENAME;
	}

	public String getFIELDTYPENAME() {
		return FIELDTYPENAME;
	}

	public void setFIELDTYPENAME(String fIELDTYPENAME) {
		FIELDTYPENAME = fIELDTYPENAME;
	}

	public String getFIELDNAMEOLD() {
		return FIELDNAMEOLD;
	}

	public void setFIELDNAMEOLD(String fIELDNAMEOLD) {
		FIELDNAMEOLD = fIELDNAMEOLD;
	}
	
	

}