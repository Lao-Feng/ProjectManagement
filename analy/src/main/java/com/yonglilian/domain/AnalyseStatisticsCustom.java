package com.yonglilian.domain;

import java.io.Serializable;



/**
 * 
 * 数据库表实体
 * @author nfzr
 * @email ftl@qq.com
 * @date 2019-08-01 01:49:08
 */
public class AnalyseStatisticsCustom implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
	private String id;
	//
	private String fid;
	//
	private String gfield;

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
	public void setGfield(String gfield) {
		this.gfield = gfield;
	}
	/**
	 * 获取：
	 */
	public String getGfield() {
		return gfield;
	}
}
