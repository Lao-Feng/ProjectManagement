package com.yonglilian.model;

import zr.zrpower.common.db.DBEntity;
import zr.zrpower.common.db.DBType;

import java.util.List;

/**
 * <p>Title: ZRPower</p>
 * <p>
 * Description: 数据表实体映射包
 * </p>
 * <p>
 * 单元名称：BPIP_USER_ROLE.java
 * </p>
 * <p>
 * 中文解释：平台用户角色表数据实体类
 * </p>
 * <p>
 * 作用：将数据库表平台用户角色表映射为Java类，作为数据传输的载体。
 * </p>
 * 
 * @author Java实体生成器
 */
public class BPIP_USER_ROLE extends DBEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected String USERID;
	protected int ROLEID;
	protected String UNITID;
	
	//以下为业务操作
	protected List<Integer> roleIdList; 

	/**
	 * 构造函数
	 */
	public BPIP_USER_ROLE() {
		super();
		initialize();
	}

	/**
	 * 初始化函数
	 */
	protected void initialize() {
		this.dbrow.setTableName("BPIP_USER_ROLE");
		this.dbrow.addColumn("USERID", null, DBType.STRING); // 用户编号
		this.dbrow.addColumn("ROLEID", null, DBType.LONG); // 角色编号
		this.dbrow.addColumn("UNITID", null, DBType.STRING); // 单位编号
	}

	/**
	 * 获取[用户编号]
	 * 
	 * @return String
	 */
	public String getUSERID() {
		return this.getString(this.dbrow.Column("USERID").getString());
	}

	/**
	 * 设置[用户编号]
	 * 
	 * @param USERID
	 *            String
	 */
	public void setUSERID(String USERID) {
		this.dbrow.Column("USERID").setValue(USERID);
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
	}

	public List<Integer> getRoleIdList() {
		return roleIdList;
	}

	public void setRoleIdList(List<Integer> roleIdList) {
		this.roleIdList = roleIdList;
	}
	
	
	

}