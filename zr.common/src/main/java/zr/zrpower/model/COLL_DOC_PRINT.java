package zr.zrpower.model;

import zr.zrpower.common.db.DBEntity;
import zr.zrpower.common.db.DBType;

/**
 * <p>Title: ZRPOWER平台</p>
 * <p>
 * Description: 数据表实体映射包
 * </p>
 * <p>
 * 单元名称：COLL_DOC_PRINT.java
 * </p>
 * <p>
 * 中文解释：打印模版配置表数据实体类
 * </p>
 * <p>
 * 作用：将数据库表打印模版配置表映射为Java类，作为数据传输的载体。
 * </p>
 * 
 * @author Java实体生成器 By NFZR
 */
public class COLL_DOC_PRINT extends DBEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 构造函数
	 */
	public COLL_DOC_PRINT() {
		super();
		initialize();
	}

	/**
	 * 初始化函数
	 */
	protected void initialize() {
		this.dbrow.setTableName("COLL_DOC_PRINT");
		this.dbrow.addColumn("ID", null, DBType.STRING); // 编号
		this.dbrow.addColumn("DOCID", null, DBType.STRING); // 文档编号
		this.dbrow.addColumn("TEMPLAET", null, DBType.STRING); // 打印模版
		this.dbrow.addColumn("PAGE", null, DBType.LONG); // 页码
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
	 * 获取[文档编号]
	 * 
	 * @return String
	 */
	public String getDOCID() {
		return this.getString(this.dbrow.Column("DOCID").getString());
	}

	/**
	 * 设置[文档编号]
	 * 
	 * @param DOCID
	 *            String
	 */
	public void setDOCID(String DOCID) {
		this.dbrow.Column("DOCID").setValue(DOCID);
	}

	/**
	 * 获取[打印模版]
	 * 
	 * @return String
	 */
	public String getTEMPLAET() {
		return this.getString(this.dbrow.Column("TEMPLAET").getString());
	}

	/**
	 * 设置[打印模版]
	 * 
	 * @param TEMPLAET
	 *            String
	 */
	public void setTEMPLAET(String TEMPLAET) {
		this.dbrow.Column("TEMPLAET").setValue(TEMPLAET);
	}

	/**
	 * 获取[页码]
	 * 
	 * @return int
	 */
	public int getPAGE() {
		return this.dbrow.Column("PAGE").getInteger();
	}

	/**
	 * 设置[页码]
	 * 
	 * @param PAGE
	 *            int
	 */
	public void setPAGE(int PAGE) {
		this.dbrow.Column("PAGE").setValue(Integer.toString(PAGE));
	}

}
