package zr.zrpower.flowengine.mode.base;

import zr.zrpower.common.db.DBEntity;
import zr.zrpower.common.db.DBType;

/**
 * <p>
 * Title: ZRPOWER平台
 * </p>
 * <p>
 * Description: 数据表实体映射包
 * </p>
 * <p>
 * 单元名称：Button.java
 * </p>
 * <p>
 * 中文解释：流程按钮表数据实体类
 * </p>
 * <p>
 * 作用：将数据库表流程按钮表映射为Java类，作为数据传输的载体。
 * </p>
 * 
 * @author Java实体生成器 By NFZR
 * 
 */
public class Button extends DBEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected String ID;
	protected String BNAME;
	protected String NAME;
	protected String TYPE;
	protected String ICO;
	protected String PROPERTY;
	protected String POSITION;
	protected String CODE;
	protected String ROLEIDS;

	/**
	 * 构造函数
	 */
	public Button() {
		super();
		initialize();
	}

	/**
	 * 初始化函数
	 */
	protected void initialize() {
		this.dbrow.setTableName("FLOW_BASE_BUTTON");
		this.dbrow.addColumn("ID", null, DBType.STRING); // 按钮ID
		this.dbrow.addColumn("BNAME", null, DBType.STRING); // 按钮名称
		this.dbrow.addColumn("NAME", null, DBType.STRING); // 按钮运转名称
		this.dbrow.addColumn("TYPE", null, DBType.STRING); // 按钮类别
		this.dbrow.addColumn("ICO", null, DBType.STRING); // 按钮图标
		this.dbrow.addColumn("PROPERTY", null, DBType.STRING); // HTML/TXT值
		this.dbrow.addColumn("POSITION", null, DBType.STRING); // 按钮布局分类
		this.dbrow.addColumn("CODE", null, DBType.STRING); // 排序号
		this.dbrow.addColumn("ROLEIDS", null, DBType.STRING); // 所属用户组或角色编号
		this.dbrow.setPrimaryKey("ID");
	}

	/**
	 * 获取[按钮ID]
	 * 
	 * @return String
	 */
	public String getID() {
		return this.getString(this.dbrow.Column("ID").getString());
	}

	/**
	 * 设置[按钮ID]
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
	public String getBNAME() {
		return this.getString(this.dbrow.Column("BNAME").getString());
	}

	/**
	 * 设置[按钮名称]
	 * 
	 * @param BNAME
	 *            String
	 */
	public void setBNAME(String BNAME) {
		this.dbrow.Column("BNAME").setValue(BNAME);
		this.BNAME = BNAME;
	}

	/**
	 * 获取[按钮运转名称]
	 * 
	 * @return String
	 */
	public String getNAME() {
		return this.getString(this.dbrow.Column("NAME").getString());
	}

	/**
	 * 设置[按钮运转名称]
	 * 
	 * @param NAME
	 *            String
	 */
	public void setNAME(String NAME) {
		this.dbrow.Column("NAME").setValue(NAME);
		this.NAME = NAME;
	}

	/**
	 * 获取[按钮类别]
	 * 
	 * @return String
	 */
	public String getTYPE() {
		return this.getString(this.dbrow.Column("TYPE").getString());
	}

	/**
	 * 设置[按钮类别]
	 * 
	 * @param TYPE
	 *            String
	 */
	public void setTYPE(String TYPE) {
		this.dbrow.Column("TYPE").setValue(TYPE);
		this.TYPE = TYPE;
	}

	/**
	 * 获取按钮图标
	 * 
	 * @return String
	 */
	public String getICO() {
		return this.getString(this.dbrow.Column("ICO").getString());
	}

	/**
	 * 设置按钮图标
	 * 
	 * @param ISO
	 *            String
	 */
	public void setICO(String ICO) {
		this.dbrow.Column("ICO").setValue(ICO);
		this.ICO = ICO;
	}

	/**
	 * 获取[HTML/TXT值]
	 * 
	 * @return String
	 */
	public String getPROPERTY() {
		return this.getString(this.dbrow.Column("PROPERTY").getString());
	}

	/**
	 * 设置[HTML/TXT值]
	 * 
	 * @param PROPERTY
	 *            String
	 */
	public void setPROPERTY(String PROPERTY) {
		this.dbrow.Column("PROPERTY").setValue(PROPERTY);
		this.PROPERTY = PROPERTY;
	}

	/**
	 * 获取[按钮布局分类]
	 * 
	 * @return String
	 */
	public String getPOSITION() {
		return this.getString(this.dbrow.Column("POSITION").getString());
	}

	/**
	 * 设置[按钮布局分类]
	 * 
	 * @param POSITION
	 *            String
	 */
	public void setPOSITION(String POSITION) {
		this.dbrow.Column("POSITION").setValue(POSITION);
		this.POSITION = POSITION;
	}

	/**
	 * 获取[排序号]
	 * 
	 * @return String
	 */
	public String getCODE() {
		return this.getString(this.dbrow.Column("CODE").getString());
	}

	/**
	 * 设置[排序号]
	 * 
	 * @param CODE
	 *            String
	 */
	public void setCODE(String CODE) {
		this.dbrow.Column("CODE").setValue(CODE);
		this.CODE = CODE;
	}

	/**
	 * 获取[所属用户组或角色编号]
	 * 
	 * @return String
	 */
	public String getROLEIDS() {
		return this.getString(this.dbrow.Column("ROLEIDS").getString());
	}

	/**
	 * 设置[所属用户组或角色编号]
	 * 
	 * @param ROLEIDS
	 *            String
	 */
	public void setROLEIDS(String ROLEIDS) {
		this.dbrow.Column("ROLEIDS").setValue(ROLEIDS);
		this.ROLEIDS = ROLEIDS;
	}

}