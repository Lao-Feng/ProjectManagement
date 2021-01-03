package com.yonglilian.bean;

import java.io.Serializable;
import java.util.List;



/**
 * 
 * 用户业务请求/返回类
 * @author nfzr
 * @email ftl@qq.com
 * @date 2019-08-04 15:20:55
 */
public class CollDocConfigBean implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
	private String id;
	//
	private String docname;
	//
	private String templaet;
	//
	private String otherfield;
	//
	private String maintable;
	//
	private String doctype;
	//
	private String dcode;
	
	//业务操作
	private List<CollConfigOperateFieldBean> fieldlist;

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
	public void setDocname(String docname) {
		this.docname = docname;
	}
	/**
	 * 获取：
	 */
	public String getDocname() {
		return docname;
	}
	/**
	 * 设置：
	 */
	public void setTemplaet(String templaet) {
		this.templaet = templaet;
	}
	/**
	 * 获取：
	 */
	public String getTemplaet() {
		return templaet;
	}
	/**
	 * 设置：
	 */
	public void setOtherfield(String otherfield) {
		this.otherfield = otherfield;
	}
	/**
	 * 获取：
	 */
	public String getOtherfield() {
		return otherfield;
	}
	/**
	 * 设置：
	 */
	public void setMaintable(String maintable) {
		this.maintable = maintable;
	}
	/**
	 * 获取：
	 */
	public String getMaintable() {
		return maintable;
	}
	/**
	 * 设置：
	 */
	public void setDoctype(String doctype) {
		this.doctype = doctype;
	}
	/**
	 * 获取：
	 */
	public String getDoctype() {
		return doctype;
	}
	/**
	 * 设置：
	 */
	public void setDcode(String dcode) {
		this.dcode = dcode;
	}
	/**
	 * 获取：
	 */
	public String getDcode() {
		return dcode;
	}
	public List<CollConfigOperateFieldBean> getFieldlist() {
		return fieldlist;
	}
	public void setFieldlist(List<CollConfigOperateFieldBean> fieldlist) {
		this.fieldlist = fieldlist;
	}
	
	
}
