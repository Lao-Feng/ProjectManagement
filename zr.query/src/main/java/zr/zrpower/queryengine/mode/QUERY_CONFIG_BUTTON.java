package zr.zrpower.queryengine.mode;

import zr.zrpower.common.db.DBEntity;
import zr.zrpower.common.db.DBType;

/**
 * <p>Title: ZRPower</p>
 * <p>
 * Description: 数据表实体映射包
 * </p>
 * <p>
 * 单元名称：QUERY_CONFIG_BUTTON.java
 * </p>
 * <p>
 * 中文解释：查询按钮表数据实体类
 * </p>
 * <p>
 * 作用：将数据库表查询按钮表映射为Java类，作为数据传输的载体。
 * </p>
 * 
 * @author Java实体生成器 By NFZR
 */
public class QUERY_CONFIG_BUTTON extends DBEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected String ID;
	protected String NAME;
	protected String RUNNAME;
	protected String ICO;
	protected String PATHORJS;
	protected String ROLEIDS;
	protected String FID;
	protected String BCODE;

	/**
	 * 构造函数
	 */
	public QUERY_CONFIG_BUTTON() {
		super();
		initialize();
	}

	/**
	 * 初始化函数
	 */
	protected void initialize() {
		this.dbrow.setTableName("QUERY_CONFIG_BUTTON");
		this.dbrow.addColumn("ID", null, DBType.STRING); // 人员编号
		this.dbrow.addColumn("NAME", null, DBType.STRING); // 按钮名称
		this.dbrow.addColumn("RUNNAME", null, DBType.STRING); // 按钮运转名称
		this.dbrow.addColumn("ICO", null, DBType.STRING); // 图标
		this.dbrow.addColumn("PATHORJS", null, DBType.STRING); // 链接地址或JS函数
		this.dbrow.addColumn("ROLEIDS", null, DBType.STRING); // 所属角色编号
		this.dbrow.addColumn("FID", null, DBType.STRING); // 父ID
		this.dbrow.addColumn("BCODE", null, DBType.STRING); // 排序号
		this.dbrow.setPrimaryKey("ID");
	}

	/**
	 * 获取[人员编号]
	 * 
	 * @return String
	 */
	public String getID() {
		return this.getString(this.dbrow.Column("ID").getString());
	}

	/**
	 * 设置[人员编号]
	 * 
	 * @param ID
	 *            String
	 */
	public void setID(String ID) {
		this.dbrow.Column("ID").setValue(ID);
		this.ID = ID;
	}

	/**
	 * 获取[按钮名称]
	 * 
	 * @return String
	 */
	public String getNAME() {
		return this.getString(this.dbrow.Column("NAME").getString());
	}

	/**
	 * 设置[按钮名称]
	 * 
	 * @param NAME
	 *            String
	 */
	public void setNAME(String NAME) {
		this.dbrow.Column("NAME").setValue(NAME);
		this.NAME = NAME;
	}

	/**
	 * 获取[按钮运转名称]
	 * 
	 * @return String
	 */
	public String getRUNNAME() {
		return this.getString(this.dbrow.Column("RUNNAME").getString());
	}

	/**
	 * 设置[按钮运转名称]
	 * 
	 * @param NAME
	 *            String
	 */
	public void setRUNNAME(String RUNNAME) {
		this.dbrow.Column("RUNNAME").setValue(RUNNAME);
		this.RUNNAME = RUNNAME;
	}

	/**
	 * 获取[图标]
	 * 
	 * @return String
	 */
	public String getICO() {
		return this.getString(this.dbrow.Column("ICO").getString());
	}

	/**
	 * 设置[图标]
	 * 
	 * @param ICO
	 *            String
	 */
	public void setICO(String ICO) {
		this.dbrow.Column("ICO").setValue(ICO);
		this.ICO = ICO;
	}

	/**
	 * 获取[链接地址或JS函数]
	 * 
	 * @return String
	 */
	public String getPATHORJS() {
		return this.getString(this.dbrow.Column("PATHORJS").getString());
	}

	/**
	 * 设置[链接地址或JS函数]
	 * 
	 * @param PATHORJS
	 *            String
	 */
	public void setPATHORJS(String PATHORJS) {
		this.dbrow.Column("PATHORJS").setValue(PATHORJS);
		this.PATHORJS = PATHORJS;
	}

	// 所属角色编号
	public String getROLEIDS() {
		return this.getString(this.dbrow.Column("ROLEIDS").getString());
	}

	public void setROLEIDS(String ROLEIDS) {
		this.dbrow.Column("ROLEIDS").setValue(ROLEIDS);
		this.ROLEIDS = ROLEIDS;
	}

	public String getFID() {
		return this.getString(this.dbrow.Column("FID").getString());
	}

	public void setFID(String FID) {
		this.dbrow.Column("FID").setValue(FID);
		this.FID = FID;
	}

	public String getBCODE() {
		return this.getString(this.dbrow.Column("BCODE").getString());
	}

	public void setBCODE(String BCODE) {
		this.dbrow.Column("BCODE").setValue(BCODE);
		this.BCODE = BCODE;
	}

}