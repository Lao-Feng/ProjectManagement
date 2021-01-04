package com.yonglilian.model;



/**
 * <p>Project:com.luffy	<p>
 * <p>Module:common	<p>
 * <p>Description: 封装分页参数<p>
 *
 * @author WANGZY25
 * @date 2017年10月23日 下午9:10:37
 */

public class PageInfoBean {

	private Integer limit;
    
	private Integer offset;
	    
	private String order ="asc" ;
	  
	private String orderBy;
	
	public PageInfoBean(Integer limit , Integer offset , String orderBy , String order){
		this.limit = limit;
		this.offset = offset;
		this.orderBy = orderBy;
		this.order = order;
	}
	
	public PageInfoBean(){
		
	}

	public Integer getLimit() {
		return limit;
	}

	public void setLimit(Integer limit) {
		this.limit = limit;
	}

	public Integer getOffset() {
		return offset;
	}

	public void setOffset(Integer offset) {
		this.offset = offset;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public String getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}
	
	
}
