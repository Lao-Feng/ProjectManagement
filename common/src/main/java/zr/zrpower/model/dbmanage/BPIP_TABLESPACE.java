package zr.zrpower.model.dbmanage;

import zr.zrpower.common.db.DBEntity;
import zr.zrpower.common.db.DBType;

/**
 * <p>Title: ZRPower</p>
 * <p>
 * Description: 数据表实体映射包
 * </p>
 * <p>
 * 单元名称：BPIP_TABLESPACE.java
 * </p>
 * <p>
 * 中文解释：表分类表数据实体类
 * </p>
 * <p>
 * 作用：将数据库表分类表映射为Java类，作为数据传输的载体。
 * </p>
 * @author Java实体生成器 By NFZR
 * 
 */
public class BPIP_TABLESPACE extends DBEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected String ID;
	protected String CHINESENAME;
	protected String DESCRIPTION;

	/**
	 * 构造函数
	 */
	public BPIP_TABLESPACE() {
		super();
		initialize();
	}

	/**
	 * 初始化函数
	 */
	protected void initialize() {
		this.dbrow.setTableName("BPIP_TABLESPACE");
		this.dbrow.addColumn("ID", null, DBType.STRING); // 表空间编号
		this.dbrow.addColumn("CHINESENAME", null, DBType.STRING); // 中文名称
		this.dbrow.addColumn("DESCRIPTION", null, DBType.STRING); // 描述
		this.dbrow.setPrimaryKey("ID");
	}

	/**
	 * 获取[表空间编号]
	 * 
	 * @return String
	 */
	public String getID() {
		return this.getString(this.dbrow.Column("ID").getString());
	}

	/**
	 * 设置[表空间编号]
	 * 
	 * @param ID
	 *            String
	 */
	public void setID(String ID) {
		this.dbrow.Column("ID").setValue(ID);
		this.ID = ID;
	}

	/**
	 * 获取[中文名称]
	 * 
	 * @return String
	 */
	public String getCHINESENAME() {
		return this.getString(this.dbrow.Column("CHINESENAME").getString());
	}

	/**
	 * 设置[中文名称]
	 * 
	 * @param CHINESENAME
	 *            String
	 */
	public void setCHINESENAME(String CHINESENAME) {
		this.dbrow.Column("CHINESENAME").setValue(CHINESENAME);
		this.CHINESENAME = CHINESENAME;
	}

	/**
	 * 获取[描述]
	 * 
	 * @return String
	 */
	public String getDESCRIPTION() {
		return this.getString(this.dbrow.Column("DESCRIPTION").getString());
	}

	/**
	 * 设置[描述]
	 * 
	 * @param DESCRIPTION
	 *            String
	 */
	public void setDESCRIPTION(String DESCRIPTION) {
		this.dbrow.Column("DESCRIPTION").setValue(DESCRIPTION);
		this.DESCRIPTION = DESCRIPTION;
	}

}
