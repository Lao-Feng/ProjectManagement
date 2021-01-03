package com.yonglilian.domain;

import java.io.Serializable;



/**
 * 
 * 数据库表实体
 * @author nfzr
 * @email ftl@qq.com
 * @date 2019-07-30 22:31:25
 */
public class QueryConfigQueryfield implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
	private String id;
	//
	private String fid;
	//
	private String field;
	//
	private String isprecision;
	//
	private String ismust;
	//
	private String isday;
	//
	private String dvalue;

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
	public void setField(String field) {
		this.field = field;
	}
	/**
	 * 获取：
	 */
	public String getField() {
		return field;
	}
	/**
	 * 设置：
	 */
	public void setIsprecision(String isprecision) {
		this.isprecision = isprecision;
	}
	/**
	 * 获取：
	 */
	public String getIsprecision() {
		return isprecision;
	}
	/**
	 * 设置：
	 */
	public void setIsmust(String ismust) {
		this.ismust = ismust;
	}
	/**
	 * 获取：
	 */
	public String getIsmust() {
		return ismust;
	}
	/**
	 * 设置：
	 */
	public void setIsday(String isday) {
		this.isday = isday;
	}
	/**
	 * 获取：
	 */
	public String getIsday() {
		return isday;
	}
	/**
	 * 设置：
	 */
	public void setDvalue(String dvalue) {
		this.dvalue = dvalue;
	}
	/**
	 * 获取：
	 */
	public String getDvalue() {
		return dvalue;
	}
}
