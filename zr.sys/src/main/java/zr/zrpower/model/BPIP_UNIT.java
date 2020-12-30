package zr.zrpower.model;

import zr.zrpower.common.db.DBEntity;
import zr.zrpower.common.db.DBType;

/**
 * <p>Title: ZRPower</p>
 * <p>
 * Description: 数据表实体映射包
 * </p>
 * <p>
 * 单元名称：BPIP_UNIT.java
 * </p>
 * <p>
 * 中文解释：单位表数据实体类
 * </p>
 * <p>
 * 作用：将数据库表单位表映射为Java类，作为数据传输的载体。
 * </p>
 * 
 * @author Java实体生成器
 * 
 */
public class BPIP_UNIT extends DBEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected String UNITID;
	protected String UNITNAME;
	protected String STATE;
	protected String ORDERCODE;
	
	//以下为业务操作，不含数据库
	protected String parentId;//父亲单位id
	protected int type;//0:同级，1:下一级

	/**
	 * 构造函数
	 */
	public BPIP_UNIT() {
		super();
		initialize();
	}

	/**
	 * 初始化函数
	 */
	protected void initialize() {
		this.dbrow.setTableName("BPIP_UNIT");
		this.dbrow.addColumn("UNITID", null, DBType.STRING); // 单位编号
		this.dbrow.addColumn("UNITNAME", null, DBType.STRING); // 单位名称
		this.dbrow.addColumn("STATE", null, DBType.STRING); // 单位状态
		this.dbrow.addColumn("ORDERCODE", null, DBType.STRING); // 排序号

		this.dbrow.setPrimaryKey("UNITID");
	}

	/**
	 * 获取[单位编号]
	 * 
	 * @return String
	 */
	public String getUNITID() {
		return this.getString(this.dbrow.Column("UNITID").getString());
	}

	/**
	 * 设置[单位编号]
	 * 
	 * @param UNITID
	 *            String
	 */
	public void setUNITID(String UNITID) {
		this.dbrow.Column("UNITID").setValue(UNITID);
		this.UNITID = UNITID;
	}

	/**
	 * 获取[单位名称]
	 * 
	 * @return String
	 */
	public String getUNITNAME() {
		return this.getString(this.dbrow.Column("UNITNAME").getString());
	}

	/**
	 * 设置[单位名称]
	 * 
	 * @param UNITNAME
	 *            String
	 */
	public void setUNITNAME(String UNITNAME) {
		this.dbrow.Column("UNITNAME").setValue(UNITNAME);
		this.UNITNAME = UNITNAME;
	}

	/**
	 * 获取[单位状态]
	 * 
	 * @return String
	 */
	public String getSTATE() {
		return this.getString(this.dbrow.Column("STATE").getString());
	}

	/**
	 * 设置[单位状态]
	 * 
	 * @param STATE
	 *            String
	 */
	public void setSTATE(String STATE) {
		this.dbrow.Column("STATE").setValue(STATE);
		this.STATE = STATE;
	}

	/**
	 * 获取[排序号]
	 * 
	 * @return String
	 */
	public String getORDERCODE() {
		return this.getString(this.dbrow.Column("ORDERCODE").getString());
	}

	/**
	 * 设置[排序号]
	 * 
	 * @param ORDERCODE
	 *            String
	 */
	public void setORDERCODE(String ORDERCODE) {
		this.dbrow.Column("ORDERCODE").setValue(ORDERCODE);
		this.ORDERCODE = ORDERCODE;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
	
	
	
	

}