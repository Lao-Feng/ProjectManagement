package com.yonglilian.bean;

import java.io.Serializable;



/**
 * 
 * 用户业务请求/返回类
 * @author nfzr
 * @email ftl@qq.com
 * @date 2019-07-30 22:31:25
 */
public class QueryTmpBean implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
	private String userid;
	//
	private String tmp;

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
	public void setTmp(String tmp) {
		this.tmp = tmp;
	}
	/**
	 * 获取：
	 */
	public String getTmp() {
		return tmp;
	}
}
