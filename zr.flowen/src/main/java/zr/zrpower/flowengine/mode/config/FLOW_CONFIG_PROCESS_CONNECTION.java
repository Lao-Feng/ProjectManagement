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
 * 单元名称：FLOW_CONFIG_PROCESS_CONNECTION.java
 * </p>
 * <p>
 * 中文解释：活动流程依赖转发(关系)表数据实体类
 * </p>
 * <p>
 * 作用：将数据库表活动流程依赖转发(关系)表映射为Java类，作为数据传输的载体。
 * </p>
 * 
 * @author Java实体生成器 By NFZR
 * 
 */
public class FLOW_CONFIG_PROCESS_CONNECTION extends DBEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected String ID;
	protected String FID;
	protected String CID;
	protected String EID;
	protected String NAME;
	protected String FDESC;
	protected String FWHERE;
	protected String TYPE;

	/**
	 * 构造函数
	 */
	public FLOW_CONFIG_PROCESS_CONNECTION() {
		super();
		initialize();
	}

	/**
	 * 初始化函数
	 */
	protected void initialize() {
		this.dbrow.setTableName("FLOW_CONFIG_PROCESS_CONNECTION");
		this.dbrow.addColumn("ID", null, DBType.STRING); // ID
		this.dbrow.addColumn("FID", null, DBType.STRING); // 过程ID
		this.dbrow.addColumn("CID", null, DBType.STRING); // 活动ID
		this.dbrow.addColumn("EID", null, DBType.STRING); // 后续过程(流程)ID
		this.dbrow.addColumn("NAME", null, DBType.STRING); // 依赖名称
		this.dbrow.addColumn("FDESC", null, DBType.STRING); // 依赖描述
		this.dbrow.addColumn("FWHERE", null, DBType.STRING); // 依赖条件
		this.dbrow.addColumn("TYPE", null, DBType.STRING); // 接收人类型
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
	 * 获取[过程ID]
	 * 
	 * @return String
	 */
	public String getFID() {
		return this.getString(this.dbrow.Column("FID").getString());
	}

	/**
	 * 设置[过程ID]
	 * 
	 * @param FID
	 *            String
	 */
	public void setFID(String FID) {
		this.dbrow.Column("FID").setValue(FID);
	}

	/**
	 * 获取[活动ID]
	 * 
	 * @return String
	 */
	public String getCID() {
		return this.getString(this.dbrow.Column("CID").getString());
	}

	/**
	 * 设置[活动ID]
	 * 
	 * @param CID
	 *            String
	 */
	public void setCID(String CID) {
		this.dbrow.Column("CID").setValue(CID);
	}

	/**
	 * 获取[后续过程(流程)ID]
	 * 
	 * @return String
	 */
	public String getEID() {
		return this.getString(this.dbrow.Column("EID").getString());
	}

	/**
	 * 设置[后续过程(流程)ID]
	 * 
	 * @param EID
	 *            String
	 */
	public void setEID(String EID) {
		this.dbrow.Column("EID").setValue(EID);
	}

	/**
	 * 获取[依赖名称]
	 * 
	 * @return String
	 */
	public String getNAME() {
		return this.getString(this.dbrow.Column("NAME").getString());
	}

	/**
	 * 设置[依赖名称]
	 * 
	 * @param NAME
	 *            String
	 */
	public void setNAME(String NAME) {
		this.dbrow.Column("NAME").setValue(NAME);
	}

	/**
	 * 获取[依赖描述]
	 * 
	 * @return String
	 */
	public String getFDESC() {
		return this.getString(this.dbrow.Column("FDESC").getString());
	}

	/**
	 * 设置[依赖描述]
	 * 
	 * @param FDESC
	 *            String
	 */
	public void setFDESC(String FDESC) {
		this.dbrow.Column("FDESC").setValue(FDESC);
	}

	/**
	 * 获取[依赖条件]
	 * 
	 * @return String
	 */
	public String getFWHERE() {
		return this.getString(this.dbrow.Column("FWHERE").getString());
	}

	/**
	 * 设置[依赖条件]
	 * 
	 * @param FWHERE
	 *            String
	 */
	public void setFWHERE(String FWHERE) {
		this.dbrow.Column("FWHERE").setValue(FWHERE);
	}

	/**
	 * 获取[接收人类型]
	 * 
	 * @return String
	 */
	public String getTYPE() {
		return this.getString(this.dbrow.Column("TYPE").getString());
	}

	/**
	 * 设置[接收人类型]
	 * 
	 * @param TYPE
	 *            String
	 */
	public void setTYPE(String TYPE) {
		this.dbrow.Column("TYPE").setValue(TYPE);
	}

}