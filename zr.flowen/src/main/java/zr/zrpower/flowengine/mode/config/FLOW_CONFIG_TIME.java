package zr.zrpower.flowengine.mode.config;

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
 * 单元名称：FLOW_CONFIG_TIME.java
 * </p>
 * <p>
 * 中文解释：时间限制表数据实体类
 * </p>
 * <p>
 * 作用：将数据库表时间限制表映射为Java类，作为数据传输的载体。
 * </p>
 * 
 * @author Java实体生成器 By NFZR
 * 
 */
public class FLOW_CONFIG_TIME extends DBEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected String ID;
	protected String FID;
	protected int DAY;
	protected String NAME;
	protected String ABNORMITYID;
	protected int FREQUENCY;

	/**
	 * 构造函数
	 */
	public FLOW_CONFIG_TIME() {
		super();
		initialize();
	}

	/**
	 * 初始化函数
	 */
	protected void initialize() {
		this.dbrow.setTableName("FLOW_CONFIG_TIME");
		this.dbrow.addColumn("ID", null, DBType.STRING); // ID
		this.dbrow.addColumn("FID", null, DBType.STRING); // 活动ID
		this.dbrow.addColumn("DAY", null, DBType.LONG); // 持续时间(天)
		this.dbrow.addColumn("NAME", null, DBType.STRING); // 限制名称
		this.dbrow.addColumn("ABNORMITYID", null, DBType.STRING); // 异常处理ID
		this.dbrow.addColumn("FREQUENCY", null, DBType.LONG); // 执行频率(天)
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
	 * 获取[持续时间(天)]
	 * 
	 * @return int
	 */
	public int getDAY() {
		return this.dbrow.Column("DAY").getInteger();
	}

	/**
	 * 设置[持续时间(天)]
	 * 
	 * @param DAY
	 *            int
	 */
	public void setDAY(int DAY) {
		this.dbrow.Column("DAY").setValue(Integer.toString(DAY));
	}

	/**
	 * 获取[限制名称]
	 * 
	 * @return String
	 */
	public String getNAME() {
		return this.getString(this.dbrow.Column("NAME").getString());
	}

	/**
	 * 设置[限制名称]
	 * 
	 * @param NAME
	 *            String
	 */
	public void setNAME(String NAME) {
		this.dbrow.Column("NAME").setValue(NAME);
	}

	/**
	 * 获取[异常处理ID]
	 * 
	 * @return String
	 */
	public String getABNORMITYID() {
		return this.getString(this.dbrow.Column("ABNORMITYID").getString());
	}

	/**
	 * 设置[异常处理ID]
	 * 
	 * @param ABNORMITYID
	 *            String
	 */
	public void setABNORMITYID(String ABNORMITYID) {
		this.dbrow.Column("ABNORMITYID").setValue(ABNORMITYID);
	}

	/**
	 * 获取[执行频率(天)]
	 * 
	 * @return int
	 */
	public int getFREQUENCY() {
		return this.dbrow.Column("FREQUENCY").getInteger();
	}

	/**
	 * 设置[执行频率(天)]
	 * 
	 * @param FREQUENCY
	 *            int
	 */
	public void setFREQUENCY(int FREQUENCY) {
		this.dbrow.Column("FREQUENCY").setValue(Integer.toString(FREQUENCY));
	}

}
