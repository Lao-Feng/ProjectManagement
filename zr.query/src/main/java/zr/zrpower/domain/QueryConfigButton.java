package zr.zrpower.domain;

import java.io.Serializable;



/**
 * 数据表实体
 * 
 * @author nfzr
 * @email ftl@qq.com
 * @date 2019-07-26 11:09:04
 */
public class QueryConfigButton implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
	private String id;
	//
	private String name;
	//
	private String ico;
	//
	private String pathorjs;
	//
	private String runname;
	//
	private String roleids;
	//
	private String fid;
	//
	private String bcode;

	/**
	 * 设置：
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * 获取：
	 */
	public String getId() {
		return id;
	}
	/**
	 * 设置：
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * 获取：
	 */
	public String getName() {
		return name;
	}
	/**
	 * 设置：
	 */
	public void setIco(String ico) {
		this.ico = ico;
	}
	/**
	 * 获取：
	 */
	public String getIco() {
		return ico;
	}
	/**
	 * 设置：
	 */
	public void setPathorjs(String pathorjs) {
		this.pathorjs = pathorjs;
	}
	/**
	 * 获取：
	 */
	public String getPathorjs() {
		return pathorjs;
	}
	/**
	 * 设置：
	 */
	public void setRunname(String runname) {
		this.runname = runname;
	}
	/**
	 * 获取：
	 */
	public String getRunname() {
		return runname;
	}
	/**
	 * 设置：
	 */
	public void setRoleids(String roleids) {
		this.roleids = roleids;
	}
	/**
	 * 获取：
	 */
	public String getRoleids() {
		return roleids;
	}
	/**
	 * 设置：
	 */
	public void setFid(String fid) {
		this.fid = fid;
	}
	/**
	 * 获取：
	 */
	public String getFid() {
		return fid;
	}
	/**
	 * 设置：
	 */
	public void setBcode(String bcode) {
		this.bcode = bcode;
	}
	/**
	 * 获取：
	 */
	public String getBcode() {
		return bcode;
	}
}
