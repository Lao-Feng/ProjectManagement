package com.yonglilian.bean;

import com.yonglilian.domain.QueryConfigTable;

import java.io.Serializable;
import java.util.List;



/**
 * 用户业务请求/返回类
 * 
 * @author nfzr
 * @email ftl@qq.com
 * @date 2019-07-26 01:10:50
 */
public class QueryConfigTableBean implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
	private String id;
	//
	private String name;
	//
	private String maintable;
	//
	private String type;
	//
	private String bid;
	//
	private String iffirstdata;
	//
	private String ifmulsel;
	//
	private String ifcombtn;
	//
	private String cid;
	//
	private String dcode;
	//
	private String qitemtype;
	//
	private String qtabletype;
	
	//业务操作
	private QueryConfigTable table;//查询配置主表
	private List<QueryConfigConnectionBean> connectionList;//关联表
	private List<QueryConfigInitBean> queryInitList; //初始条件
	private List<String> queryFieldList; //查询条件列表
	private List<QueryConfigShowfieldBean> queryShowList; //查询结果列表
	private List<QueryConfigParameterBean> queryParameterList;//按钮参数列表
	private List<QueryConfigOrderBean> queryOrderList;//排序列表       

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
	public void setType(String type) {
		this.type = type;
	}
	/**
	 * 获取：
	 */
	public String getType() {
		return type;
	}
	/**
	 * 设置：
	 */
	public void setBid(String bid) {
		this.bid = bid;
	}
	/**
	 * 获取：
	 */
	public String getBid() {
		return bid;
	}
	/**
	 * 设置：
	 */
	public void setIffirstdata(String iffirstdata) {
		this.iffirstdata = iffirstdata;
	}
	/**
	 * 获取：
	 */
	public String getIffirstdata() {
		return iffirstdata;
	}
	/**
	 * 设置：
	 */
	public void setIfmulsel(String ifmulsel) {
		this.ifmulsel = ifmulsel;
	}
	/**
	 * 获取：
	 */
	public String getIfmulsel() {
		return ifmulsel;
	}
	/**
	 * 设置：
	 */
	public void setIfcombtn(String ifcombtn) {
		this.ifcombtn = ifcombtn;
	}
	/**
	 * 获取：
	 */
	public String getIfcombtn() {
		return ifcombtn;
	}
	/**
	 * 设置：
	 */
	public void setCid(String cid) {
		this.cid = cid;
	}
	/**
	 * 获取：
	 */
	public String getCid() {
		return cid;
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
	/**
	 * 设置：
	 */
	public void setQitemtype(String qitemtype) {
		this.qitemtype = qitemtype;
	}
	/**
	 * 获取：
	 */
	public String getQitemtype() {
		return qitemtype;
	}
	/**
	 * 设置：
	 */
	public void setQtabletype(String qtabletype) {
		this.qtabletype = qtabletype;
	}
	/**
	 * 获取：
	 */
	public String getQtabletype() {
		return qtabletype;
	}
	
	
	
	public QueryConfigTable getTable() {
		return table;
	}
	public void setTable(QueryConfigTable table) {
		this.table = table;
	}
	public List<QueryConfigConnectionBean> getConnectionList() {
		return connectionList;
	}
	public void setConnectionList(List<QueryConfigConnectionBean> connectionList) {
		this.connectionList = connectionList;
	}
	public List<QueryConfigInitBean> getQueryInitList() {
		return queryInitList;
	}
	public void setQueryInitList(List<QueryConfigInitBean> queryInitList) {
		this.queryInitList = queryInitList;
	}
	public List<String> getQueryFieldList() {
		return queryFieldList;
	}
	public void setQueryFieldList(List<String> queryFieldList) {
		this.queryFieldList = queryFieldList;
	}
	public List<QueryConfigShowfieldBean> getQueryShowList() {
		return queryShowList;
	}
	public void setQueryShowList(List<QueryConfigShowfieldBean> queryShowList) {
		this.queryShowList = queryShowList;
	}
	public List<QueryConfigParameterBean> getQueryParameterList() {
		return queryParameterList;
	}
	public void setQueryParameterList(List<QueryConfigParameterBean> queryParameterList) {
		this.queryParameterList = queryParameterList;
	}
	public List<QueryConfigOrderBean> getQueryOrderList() {
		return queryOrderList;
	}
	public void setQueryOrderList(List<QueryConfigOrderBean> queryOrderList) {
		this.queryOrderList = queryOrderList;
	}
	
	
}
