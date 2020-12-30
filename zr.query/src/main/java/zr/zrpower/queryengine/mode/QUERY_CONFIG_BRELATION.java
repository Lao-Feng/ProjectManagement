package zr.zrpower.queryengine.mode;

import zr.zrpower.common.db.DBEntity;
import zr.zrpower.common.db.DBType;

/**
 * <p>Title: ZRPower</p>
 * <p>
 * Description: 数据表实体映射包
 * </p>
 * <p>
 * 单元名称：QUERY_CONFIG_BRELATION.java
 * </p>
 * <p>
 * 中文解释：按钮配置关系表数据实体类
 * </p>
 * <p>
 * 作用：将数据库表按钮配置关系表映射为Java类，作为数据传输的载体。
 * </p>
 * 
 * @author Java实体生成器 By NFZR
 */
public class QUERY_CONFIG_BRELATION extends DBEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 构造函数
	 */
	public QUERY_CONFIG_BRELATION() {
		super();
		initialize();
	}

	/**
	 * 初始化函数
	 */
	protected void initialize() {
		this.dbrow.setTableName("QUERY_CONFIG_BRELATION");
		this.dbrow.addColumn("ID", null, DBType.STRING); // 人员编号
		this.dbrow.addColumn("FID", null, DBType.STRING); // 配置表ID
		this.dbrow.addColumn("BID", null, DBType.STRING); // 按钮ID
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
	}

	/**
	 * 获取[配置表ID]
	 * 
	 * @return String
	 */
	public String getFID() {
		return this.getString(this.dbrow.Column("FID").getString());
	}

	/**
	 * 设置[配置表ID]
	 * 
	 * @param FID
	 *            String
	 */
	public void setFID(String FID) {
		this.dbrow.Column("FID").setValue(FID);
	}

	/**
	 * 获取[按钮ID]
	 * 
	 * @return String
	 */
	public String getBID() {
		return this.getString(this.dbrow.Column("BID").getString());
	}

	/**
	 * 设置[按钮ID]
	 * 
	 * @param BID
	 *            String
	 */
	public void setBID(String BID) {
		this.dbrow.Column("BID").setValue(BID);
	}

}