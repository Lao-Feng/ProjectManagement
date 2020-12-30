package zr.zrpower.analyseengine.mode;

import zr.zrpower.common.db.DBEntity;
import zr.zrpower.common.db.DBType;

/**
 * <p>Title: ZRPower</p>
 * <p>
 * Description: 数据表实体映射包
 * </p>
 * <p>
 * 单元名称：ANALYSE_STATISTICS_CUSTOM.java
 * </p>
 * <p>
 * 中文解释：统计分组配置表数据实体类
 * </p>
 * <p>
 * 作用：将数据库表统计分组配置表映射为Java类，作为数据传输的载体。
 * </p>
 * 
 * @author Java实体生成器 By NFZR
 */
public class ANALYSE_STATISTICS_CUSTOM extends DBEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 构造函数
	 */
	public ANALYSE_STATISTICS_CUSTOM() {
		super();
		initialize();
	}

	/**
	 * 初始化函数
	 */
	protected void initialize() {
		this.dbrow.setTableName("ANALYSE_STATISTICS_CUSTOM");
		this.dbrow.addColumn("ID", null, DBType.STRING); // 编号
		this.dbrow.addColumn("FID", null, DBType.STRING); // 统计配置表ID
		this.dbrow.addColumn("GFIELD", null, DBType.STRING); // 自定义统计字段
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
	 * 获取[自定义统计字段]
	 * 
	 * @return String
	 */
	public String getGFIELD() {
		return this.getString(this.dbrow.Column("GFIELD").getString());
	}

	/**
	 * 设置[自定义统计字段]
	 * 
	 * @param GFIELD
	 *            String
	 */
	public void setGFIELD(String GFIELD) {
		this.dbrow.Column("GFIELD").setValue(GFIELD);
	}

}
