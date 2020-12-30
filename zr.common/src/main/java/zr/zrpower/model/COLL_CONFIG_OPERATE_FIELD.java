package zr.zrpower.model;

import zr.zrpower.common.db.DBEntity;
import zr.zrpower.common.db.DBType;

/**
 * <p>Title: ZRPower</p>
 * <p>
 * Description: 数据表实体映射包
 * </p>
 * <p>
 * 单元名称：COLL_CONFIG_OPERATE_FIELD.java
 * </p>
 * <p>
 * 中文解释：活动可操作字段表数据实体类
 * </p>
 * <p>
 * 作用：将数据库表活动可操作字段表映射为Java类，作为数据传输的载体。
 * </p>
 * 
 * @author Java实体生成器 By NFZR
 */
public class COLL_CONFIG_OPERATE_FIELD extends DBEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected String ID;
	protected String FID;
	protected String FIELD;
	protected String ISDISPLAY;
	protected String ISEDIT;
	protected String ISMUSTFILL;
	protected String DEFAULT1;
	protected String ISFORCE;

	/**
	 * 构造函数
	 */
	public COLL_CONFIG_OPERATE_FIELD() {
		super();
		initialize();
	}

	/**
	 * 初始化函数
	 */
	protected void initialize() {
		this.dbrow.setTableName("COLL_CONFIG_OPERATE_FIELD");
		this.dbrow.addColumn("ID", null, DBType.STRING); // ID
		this.dbrow.addColumn("FID", null, DBType.STRING); // 活动ID
		this.dbrow.addColumn("FIELD", null, DBType.STRING); // 操作字段
		this.dbrow.addColumn("ISDISPLAY", null, DBType.STRING); // 是否显示
		this.dbrow.addColumn("ISEDIT", null, DBType.STRING); // 是否编辑
		this.dbrow.addColumn("ISMUSTFILL", null, DBType.STRING); // 是否可为空
		this.dbrow.addColumn("DEFAULT1", null, DBType.STRING); // 默认值
		this.dbrow.addColumn("ISFORCE", null, DBType.STRING); // 是否强制设置默认值
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
	 * 获取[活动ID]
	 * 
	 * @return String
	 */
	public String getFID() {
		return this.getString(this.dbrow.Column("FID").getString());
	}

	/**
	 * 设置[活动ID]
	 * 
	 * @param FID
	 *            String
	 */
	public void setFID(String FID) {
		this.dbrow.Column("FID").setValue(FID);
	}

	/**
	 * 获取[操作字段]
	 * 
	 * @return String
	 */
	public String getFIELD() {
		return this.getString(this.dbrow.Column("FIELD").getString());
	}

	/**
	 * 设置[操作字段]
	 * 
	 * @param FIELD
	 *            String
	 */
	public void setFIELD(String FIELD) {
		this.dbrow.Column("FIELD").setValue(FIELD);
	}

	/**
	 * 获取[]
	 * 
	 * @return String
	 */
	public String getISDISPLAY() {
		return this.getString(this.dbrow.Column("ISDISPLAY").getString());
	}

	/**
	 * 设置[]
	 * 
	 * @param ISDISPLAY
	 *            String
	 */
	public void setISDISPLAY(String ISDISPLAY) {
		this.dbrow.Column("ISDISPLAY").setValue(ISDISPLAY);
	}

	/**
	 * 获取[]
	 * 
	 * @return String
	 */
	public String getISEDIT() {
		return this.getString(this.dbrow.Column("ISEDIT").getString());
	}

	/**
	 * 设置[]
	 * 
	 * @param ISEDIT
	 *            String
	 */
	public void setISEDIT(String ISEDIT) {
		this.dbrow.Column("ISEDIT").setValue(ISEDIT);
	}

	/**
	 * 获取[]
	 * 
	 * @return String
	 */
	public String getISMUSTFILL() {
		return this.getString(this.dbrow.Column("ISMUSTFILL").getString());
	}

	/**
	 * 设置[]
	 * 
	 * @param ISMUSTFILL
	 *            String
	 */
	public void setISMUSTFILL(String ISMUSTFILL) {
		this.dbrow.Column("ISMUSTFILL").setValue(ISMUSTFILL);
	}

	/**
	 * 获取[]
	 * 
	 * @return String
	 */
	public String getDEFAULT1() {
		return this.getString(this.dbrow.Column("DEFAULT1").getString());
	}

	/**
	 * 设置[]
	 * 
	 * @param DEFAULT1
	 *            String
	 */
	public void setDEFAULT1(String DEFAULT1) {
		this.dbrow.Column("DEFAULT1").setValue(DEFAULT1);
	}

	/**
	 * 获取[是否强制设置默认值]
	 * 
	 * @return String
	 */
	public String getISFORCE() {
		return this.getString(this.dbrow.Column("ISFORCE").getString());
	}

	/**
	 * 设置[是否强制设置默认值]
	 * 
	 * @param ISFORCE
	 *            String
	 */
	public void setISFORCE(String ISFORCE) {
		this.dbrow.Column("ISFORCE").setValue(ISFORCE);
	}
}