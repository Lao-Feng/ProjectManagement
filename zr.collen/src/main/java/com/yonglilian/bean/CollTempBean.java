package com.yonglilian.bean;

import java.io.Serializable;
import java.util.List;



/**
 * 生成表单模板
 * 用户业务请求/返回类
 * @author nfzr
 * @email ftl@qq.com
 * @date 2019-08-04 15:20:55
 */
public class CollTempBean implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//表单id
	private String id;
	//生成的表单类型
	private int doctype;
	//生成的字段list
	private List<String> fields;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public int getDoctype() {
		return doctype;
	}
	public void setDoctype(int doctype) {
		this.doctype = doctype;
	}
	public List<String> getFields() {
		return fields;
	}
	public void setFields(List<String> fields) {
		this.fields = fields;
	}
	
}
