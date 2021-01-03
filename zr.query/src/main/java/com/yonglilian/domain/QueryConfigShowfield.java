package com.yonglilian.domain;

import java.io.Serializable;



/**
 * 
 * 数据库表实体
 * @author nfzr
 * @email ftl@qq.com
 * @date 2019-07-30 22:31:25
 */
public class QueryConfigShowfield implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
	private String id;
	//
	private String fid;
	//
	private String field;
	//
	private String isshow;
	//
	private String colwidth;
	//
	private String isnumber;
	//
	private String qalign;

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
	public void setIsshow(String isshow) {
		this.isshow = isshow;
	}
	/**
	 * 获取：
	 */
	public String getIsshow() {
		return isshow;
	}
	/**
	 * 设置：
	 */
	public void setColwidth(String colwidth) {
		this.colwidth = colwidth;
	}
	/**
	 * 获取：
	 */
	public String getColwidth() {
		return colwidth;
	}
	/**
	 * 设置：
	 */
	public void setIsnumber(String isnumber) {
		this.isnumber = isnumber;
	}
	/**
	 * 获取：
	 */
	public String getIsnumber() {
		return isnumber;
	}
	/**
	 * 设置：
	 */
	public void setQalign(String qalign) {
		this.qalign = qalign;
	}
	/**
	 * 获取：
	 */
	public String getQalign() {
		return qalign;
	}
}
