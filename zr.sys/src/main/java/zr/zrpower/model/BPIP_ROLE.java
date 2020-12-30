package zr.zrpower.model;

import zr.zrpower.common.db.DBEntity;
import zr.zrpower.common.db.DBType;

import java.util.List;

/**
 * <p>Title: ZRPower</p>
 * <p>
 * Description: 数据表实体映射包
 * </p>
 * <p>
 * 单元名称：BPIP_ROLE.java
 * </p>
 * <p>
 * 中文解释：平台角色表数据实体类
 * </p>
 * <p>
 * 作用：将数据库表平台角色表映射为Java类，作为数据传输的载体。
 * </p>
 * @author Java实体生成器
 * 
 */
public class BPIP_ROLE extends DBEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected int ROLEID;
	protected String ROLENAME;
	protected String DESCRIPTION;
	
	//以下为业务操作
	protected List<String> roleMenuList;//角色对应的菜单id数据权限
	protected List<BPIP_USER_ROLE> roleUserList;//对应的角色用户数据

	/**
	 * 构造函数
	 */
	public BPIP_ROLE() {
		super();
		initialize();
	}

	/**
	 * 初始化函数
	 */
	protected void initialize() {
		this.dbrow.setTableName("BPIP_ROLE");
		this.dbrow.addColumn("ROLEID", null, DBType.LONG); // 角色编号
		this.dbrow.addColumn("ROLENAME", null, DBType.STRING); // 角色名称
		this.dbrow.addColumn("DESCRIPTION", null, DBType.STRING); // 角色描述
		this.dbrow.setPrimaryKey("ROLEID");
	}

	/**
	 * 获取[角色编号]
	 * 
	 * @return int
	 */
	public int getROLEID() {
		return this.dbrow.Column("ROLEID").getInteger();
	}

	/**
	 * 设置[角色编号]
	 * 
	 * @param ROLEID
	 *            int
	 */
	public void setROLEID(int ROLEID) {
		this.dbrow.Column("ROLEID").setValue(Integer.toString(ROLEID));
		this.ROLEID = ROLEID;
	}

	/**
	 * 获取[角色名称]
	 * 
	 * @return String
	 */
	public String getROLENAME() {
		return this.getString(this.dbrow.Column("ROLENAME").getString());
	}

	/**
	 * 设置[角色名称]
	 * 
	 * @param ROLENAME
	 *            String
	 */
	public void setROLENAME(String ROLENAME) {
		this.dbrow.Column("ROLENAME").setValue(ROLENAME);
		this.ROLENAME = ROLENAME;
	}

	/**
	 * 获取[角色描述]
	 * 
	 * @return String
	 */
	public String getDESCRIPTION() {
		return this.getString(this.dbrow.Column("DESCRIPTION").getString());
	}

	/**
	 * 设置[角色描述]
	 * 
	 * @param DESCRIPTION
	 *            String
	 */
	public void setDESCRIPTION(String DESCRIPTION) {
		this.dbrow.Column("DESCRIPTION").setValue(DESCRIPTION);
		this.DESCRIPTION = DESCRIPTION;
	}

	public List<String> getRoleMenuList() {
		return roleMenuList;
	}

	public void setRoleMenuList(List<String> roleMenuList) {
		this.roleMenuList = roleMenuList;
	}

	public List<BPIP_USER_ROLE> getRoleUserList() {
		return roleUserList;
	}

	public void setRoleUserList(List<BPIP_USER_ROLE> roleUserList) {
		this.roleUserList = roleUserList;
	}
	
	

	
	

}
