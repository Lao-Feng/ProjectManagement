package com.yonglilian.bean;

import java.io.Serializable;



/**
 * 
 * 用户业务请求/返回类
 * @author nfzr
 * @email ftl@qq.com
 * @date 2019-07-30 22:31:25
 */
public class QueryParameterBean implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
	private String userid;
	//
	private String queryid;
	//
	private String upHeight;
	//
	private String downHeight;

	/**
	 * 设置：
	 */
	public void setUserid(String userid) {
		this.userid = userid;
	}
	/**
	 * 获取：
	 */
	public String getUserid() {
		return userid;
	}
	/**
	 * 设置：
	 */
	public void setQueryid(String queryid) {
		this.queryid = queryid;
	}
	/**
	 * 获取：
	 */
	public String getQueryid() {
		return queryid;
	}
	/**
	 * 设置：
	 */
	public void setUpHeight(String upHeight) {
		this.upHeight = upHeight;
	}
	/**
	 * 获取：
	 */
	public String getUpHeight() {
		return upHeight;
	}
	/**
	 * 设置：
	 */
	public void setDownHeight(String downHeight) {
		this.downHeight = downHeight;
	}
	/**
	 * 获取：
	 */
	public String getDownHeight() {
		return downHeight;
	}
}
