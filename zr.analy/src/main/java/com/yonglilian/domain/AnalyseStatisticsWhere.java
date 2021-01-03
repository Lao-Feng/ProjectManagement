package com.yonglilian.domain;

import java.io.Serializable;



/**
 * 
 * 数据库表实体
 * @author nfzr
 * @email ftl@qq.com
 * @date 2019-08-01 01:49:08
 */
public class AnalyseStatisticsWhere implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
	private String id;
	//
	private String fid;
	//
	private String sleft;
	//
	private String field;
	//
	private String symbol;
	//
	private String wherevalue;
	//
	private String sright;
	//
	private String logic;

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
	public void setSleft(String sleft) {
		this.sleft = sleft;
	}
	/**
	 * 获取：
	 */
	public String getSleft() {
		return sleft;
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
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
	/**
	 * 获取：
	 */
	public String getSymbol() {
		return symbol;
	}
	/**
	 * 设置：
	 */
	public void setWherevalue(String wherevalue) {
		this.wherevalue = wherevalue;
	}
	/**
	 * 获取：
	 */
	public String getWherevalue() {
		return wherevalue;
	}
	/**
	 * 设置：
	 */
	public void setSright(String sright) {
		this.sright = sright;
	}
	/**
	 * 获取：
	 */
	public String getSright() {
		return sright;
	}
	/**
	 * 设置：
	 */
	public void setLogic(String logic) {
		this.logic = logic;
	}
	/**
	 * 获取：
	 */
	public String getLogic() {
		return logic;
	}
}
