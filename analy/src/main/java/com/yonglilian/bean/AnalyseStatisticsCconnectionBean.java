package com.yonglilian.bean;

import java.io.Serializable;



/**
 * 
 * 用户业务请求/返回类
 * @author nfzr
 * @email ftl@qq.com
 * @date 2019-08-01 01:49:08
 */
public class AnalyseStatisticsCconnectionBean implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
	private String id;
	//
	private String fid;
	//
	private String mfield;
	//
	private String cfield;
	//
	private String jointype;
	
	//业务关联
	private String mfieldtable;
	private String cfieldtable;

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
	public void setMfield(String mfield) {
		this.mfield = mfield;
	}
	/**
	 * 获取：
	 */
	public String getMfield() {
		return mfield;
	}
	/**
	 * 设置：
	 */
	public void setCfield(String cfield) {
		this.cfield = cfield;
	}
	/**
	 * 获取：
	 */
	public String getCfield() {
		return cfield;
	}
	/**
	 * 设置：
	 */
	public void setJointype(String jointype) {
		this.jointype = jointype;
	}
	/**
	 * 获取：
	 */
	public String getJointype() {
		return jointype;
	}
	public String getMfieldtable() {
		return mfieldtable;
	}
	public void setMfieldtable(String mfieldtable) {
		this.mfieldtable = mfieldtable;
	}
	public String getCfieldtable() {
		return cfieldtable;
	}
	public void setCfieldtable(String cfieldtable) {
		this.cfieldtable = cfieldtable;
	}
	
	
}
