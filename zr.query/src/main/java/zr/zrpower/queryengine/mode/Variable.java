package zr.zrpower.queryengine.mode;

import zr.zrpower.common.db.DBEntity;
import zr.zrpower.common.db.DBType;

/**
 * <p>Title: ZRPOWER平台</p>
 * <p>
 * Description: 数据表实体映射包
 * </p>
 * <p>
 * 单元名称：VARIABLE.java
 * </p>
 * <p>
 * 中文解释：流程变量配置表数据实体类
 * </p>
 * <p>
 * 作用：将数据库表流程变量配置表映射为Java类，作为数据传输的载体。
 * </p>
 * 
 * @author Java实体生成器 By NFZR
 */
public class Variable extends DBEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 构造函数
	 */
	public Variable() {
		super();
		initialize();
	}

	/**
	 * 初始化函数
	 */
	protected void initialize() {
		this.dbrow.setTableName("VARIABLE");
		this.dbrow.addColumn("CODE", null, DBType.STRING); // 变量执行代码
		this.dbrow.addColumn("NAME", null, DBType.STRING); // 变量名称
	}

	/**
	 * 获取[变量名称]
	 * 
	 * @return String
	 */
	public String getNAME() {
		return this.getString(this.dbrow.Column("NAME").getString());
	}

	/**
	 * 设置[变量名称]
	 * 
	 * @param NAME
	 *            String
	 */
	public void setNAME(String NAME) {
		this.dbrow.Column("NAME").setValue(NAME);
	}

	/**
	 * 获取[变量执行代码]
	 * 
	 * @return String
	 */
	public String getCODE() {
		return this.getString(this.dbrow.Column("CODE").getString());
	}

	/**
	 * 设置[变量执行代码]
	 * 
	 * @param CODE
	 *            String
	 */
	public void setCODE(String CODE) {
		this.dbrow.Column("CODE").setValue(CODE);
	}

}