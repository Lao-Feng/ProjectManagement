package zr.zrpower.model.dbmanage;

import zr.zrpower.common.db.DBEntity;
import zr.zrpower.common.db.DBType;

/**
 * <p>Title: ZRPower</p>
 * <p>
 * Description: 数据表实体映射包
 * </p>
 * <p>
 * 单元名称：BPIP_TABLE.java
 * </p>
 * <p>
 * 中文解释：表名表数据实体类
 * </p>
 * <p>
 * 作用：将数据库表表名表映射为Java类，作为数据传输的载体。
 * </p>
 * @author Java实体生成器 By NFZR
 * 
 */
public class BPIP_TABLE extends DBEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected String TABLEID;
	protected String TABLENAME;
	protected String CHINESENAME;
	protected String TABLESPACE;
	protected String DESCRIPTION;
	protected String TABLETYPE;
	protected String PRIMARYKEY;
	protected String TITLE;
	
	//新增数据库表参数
	protected String KEYTYPE;//主键数据库类型
	protected Integer KEYLEN;//主键长度
	
	//业务操作
	protected String TABLENAMEOLD;//表重命名前的名称
	protected String PRIMARYKEYOLD;//表重命名前的主键
	protected String CHINESENAMEOLD;//表汉字名称被重命名
	protected String KEYTYPEID;//主键数据库类型ID

	/**
	 * 构造函数
	 */
	public BPIP_TABLE() {
		super();
		initialize();
	}

	/**
	 * 初始化函数
	 */
	protected void initialize() {
		this.dbrow.setTableName("BPIP_TABLE");
		this.dbrow.addColumn("TABLEID", null, DBType.STRING); // 表编号
		this.dbrow.addColumn("TABLENAME", null, DBType.STRING); // 表名
		this.dbrow.addColumn("CHINESENAME", null, DBType.STRING); // 中文名称
		this.dbrow.addColumn("TABLESPACE", null, DBType.STRING); // 存放表空间
		this.dbrow.addColumn("DESCRIPTION", null, DBType.STRING); // 描述
		this.dbrow.addColumn("TABLETYPE", null, DBType.STRING); // 表类型
		this.dbrow.addColumn("PRIMARYKEY", null, DBType.STRING); // 主键
		this.dbrow.addColumn("TITLE", null, DBType.STRING); // 标题
		
		this.dbrow.addColumn("KEYTYPE", null, DBType.STRING); // 主键数据库类型
		this.dbrow.addColumn("KEYLEN", null, DBType.LONG); // 主键长度
		this.dbrow.setPrimaryKey("TABLEID");
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
	 * 获取[表名]
	 * 
	 * @return String
	 */
	public String getTABLENAME() {
		return this.getString(this.dbrow.Column("TABLENAME").getString());
	}

	/**
	 * 设置[表名]
	 * 
	 * @param TABLENAME
	 *            String
	 */
	public void setTABLENAME(String TABLENAME) {
		this.dbrow.Column("TABLENAME").setValue(TABLENAME);
		this.TABLENAME = TABLENAME;
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
	 * 获取[存放表空间]
	 * 
	 * @return String
	 */
	public String getTABLESPACE() {
		return this.getString(this.dbrow.Column("TABLESPACE").getString());
	}

	/**
	 * 设置[存放表空间]
	 * 
	 * @param TABLESPACE
	 *            String
	 */
	public void setTABLESPACE(String TABLESPACE) {
		this.dbrow.Column("TABLESPACE").setValue(TABLESPACE);
		this.TABLESPACE = TABLESPACE;
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
	 * 获取[表类型]
	 * 
	 * @return String
	 */
	public String getTABLETYPE() {
		return this.getString(this.dbrow.Column("TABLETYPE").getString());
	}

	/**
	 * 设置[表类型]
	 * 
	 * @param TABLETYPE
	 *            String
	 */
	public void setTABLETYPE(String TABLETYPE) {
		this.dbrow.Column("TABLETYPE").setValue(TABLETYPE);
		this.TABLETYPE = TABLETYPE;
	}

	/**
	 * 获取[主键]
	 * 
	 * @return String
	 */
	public String getPRIMARYKEY() {
		return this.getString(this.dbrow.Column("PRIMARYKEY").getString());
	}

	/**
	 * 设置[主键]
	 * 
	 * @param PRIMARYKEY
	 *            String
	 */
	public void setPRIMARYKEY(String PRIMARYKEY) {
		this.dbrow.Column("PRIMARYKEY").setValue(PRIMARYKEY);
		this.PRIMARYKEY = PRIMARYKEY;
	}

	/**
	 * 获取[标题]
	 * 
	 * @return String
	 */
	public String getTITLE() {
		return this.getString(this.dbrow.Column("TITLE").getString());
	}

	/**
	 * 设置[标题]
	 * 
	 * @param TITLE
	 *            String
	 */
	public void setTITLE(String TITLE) {
		this.dbrow.Column("TITLE").setValue(TITLE);
		this.TITLE = TITLE;
	}


	public String getKEYTYPE() {
		return this.getString(this.dbrow.Column("KEYTYPE").getString());
	}

	public void setKEYTYPE(String KEYTYPE) {
		this.dbrow.Column("KEYTYPE").setValue(KEYTYPE);
		this.KEYTYPE = KEYTYPE;
	}

	public Integer getKEYLEN() {
		return this.dbrow.Column("KEYLEN").getInteger();
	}

	public void setKEYLEN(Integer KEYLEN) {
		this.dbrow.Column("KEYLEN").setValue(KEYLEN);
		this.KEYLEN = KEYLEN;
	}

	public String getTABLENAMEOLD() {
		return TABLENAMEOLD;
	}

	public void setTABLENAMEOLD(String tABLENAMEOLD) {
		TABLENAMEOLD = tABLENAMEOLD;
	}

	public String getPRIMARYKEYOLD() {
		return PRIMARYKEYOLD;
	}

	public void setPRIMARYKEYOLD(String pRIMARYKEYOLD) {
		PRIMARYKEYOLD = pRIMARYKEYOLD;
	}

	public String getCHINESENAMEOLD() {
		return CHINESENAMEOLD;
	}

	public void setCHINESENAMEOLD(String cHINESENAMEOLD) {
		CHINESENAMEOLD = cHINESENAMEOLD;
	}

	public String getKEYTYPEID() {
		return KEYTYPEID;
	}

	public void setKEYTYPEID(String kEYTYPEID) {
		KEYTYPEID = kEYTYPEID;
	}

	

}
