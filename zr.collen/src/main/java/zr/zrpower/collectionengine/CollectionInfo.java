package zr.zrpower.collectionengine;

import zr.zrpower.common.db.DBEntity;
import zr.zrpower.common.db.DBType;

import java.util.Hashtable;

/**
 * <p>Title: ZRPower</p>
 * <p>
 * Description: 数据表实体映射包
 * </p>
 * <p>
 * 单元名称：CollectionInfo.java
 * </p>
 * <p>
 * 中文解释：文档配置表数据实体类
 * </p>
 * <p>
 * 作用：将数据库表文档配置表映射为Java类，作为数据传输的载体。
 * </p>
 * 
 * @author Java实体生成器 By NFZR
 */
public class CollectionInfo extends DBEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Hashtable<String, Object> hPrintTemplet = new Hashtable<String, Object>();// 打印模版
	protected String ID;
	protected String DOCNAME;
	protected String TEMPLAET;
	protected String DOCTYPE;
	protected String MAINTABLE;
	protected String OTHERFIELD;

	/**
	 * 构造函数
	 */
	public CollectionInfo() {
		super();
		initialize();
	}

	/**
	 * 初始化函数
	 */
	protected void initialize() {
		this.dbrow.setTableName("COLL_DOC_CONFIG");
		this.dbrow.addColumn("ID", null, DBType.STRING); // ID
		this.dbrow.addColumn("DOCNAME", null, DBType.STRING); // 文档名称
		this.dbrow.addColumn("TEMPLAET", null, DBType.STRING); // 模板
		this.dbrow.addColumn("DOCTYPE", null, DBType.STRING); // 文档类型
		this.dbrow.addColumn("MAINTABLE", null, DBType.STRING); // 绑定数据表
		this.dbrow.addColumn("OTHERFIELD", null, DBType.STRING); // 关联主键
		this.dbrow.setPrimaryKey("ID");
	}

	public void addPrintTemplet(int Page, String Templet) {
		String page = Integer.toString(Page);
		if (this.hPrintTemplet.get(page) != null) {
			this.hPrintTemplet.remove(page);
		}
		this.hPrintTemplet.put(page, Templet);
	}

	public String getPrintTemplet(int Page) {
		String result = null;
		String page = Integer.toString(Page);
		result = (String) hPrintTemplet.get(page);
		return result;
	}

	public int getPrintTempletCount() {
		return hPrintTemplet.size();
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
		this.ID = ID;
	}

	/**
	 * 获取[文档名称]
	 * 
	 * @return String
	 */
	public String getDOCNAME() {
		return this.getString(this.dbrow.Column("DOCNAME").getString());
	}

	/**
	 * 设置[文档名称]
	 * 
	 * @param DOCNAME
	 *            String
	 */
	public void setDOCNAME(String DOCNAME) {
		this.dbrow.Column("DOCNAME").setValue(DOCNAME);
		this.DOCNAME = DOCNAME;
	}

	/**
	 * 获取[模板]
	 * 
	 * @return String
	 */
	public String getTEMPLAET() {
		return this.getString(this.dbrow.Column("TEMPLAET").getString());
	}

	/**
	 * 设置[模板]
	 * 
	 * @param TEMPLAET
	 *            String
	 */
	public void setTEMPLAET(String TEMPLAET) {
		this.dbrow.Column("TEMPLAET").setValue(TEMPLAET);
		this.TEMPLAET = TEMPLAET;
	}

	/**
	 * 获取[打印模板]
	 * 
	 * @return String
	 */
	public String getDOCTYPE() {
		return this.getString(this.dbrow.Column("DOCTYPE").getString());
	}

	/**
	 * 设置[打印模板]
	 * 
	 * @param DOCTYPE
	 *            String
	 */
	public void setDOCTYPE(String DOCTYPE) {
		this.dbrow.Column("DOCTYPE").setValue(DOCTYPE);
		this.DOCTYPE = DOCTYPE;
	}

	/**
	 * 获取[主表名]
	 * 
	 * @return String
	 */
	public String getMAINTABLE() {
		return this.getString(this.dbrow.Column("MAINTABLE").getString());
	}

	/**
	 * 设置[主表名]
	 * 
	 * @param MAINTABLE
	 *            String
	 */
	public void setMAINTABLE(String MAINTABLE) {
		this.dbrow.Column("MAINTABLE").setValue(MAINTABLE);
	}

	/**
	 * 获取[关联主键]
	 * 
	 * @return String
	 */
	public String getOTHERFIELD() {
		return this.getString(this.dbrow.Column("OTHERFIELD").getString());
	}

	/**
	 * 设置[关联主键]
	 * 
	 * @param OTHERFIELD
	 *            String
	 */
	public void setOTHERFIELD(String OTHERFIELD) {
		this.dbrow.Column("OTHERFIELD").setValue(OTHERFIELD);
		this.OTHERFIELD = OTHERFIELD;
	}
}