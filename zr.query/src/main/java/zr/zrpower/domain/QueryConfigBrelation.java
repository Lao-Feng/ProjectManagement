package zr.zrpower.domain;

import java.io.Serializable;



/**
 * 
 * 数据库表实体
 * @author nfzr
 * @email ftl@qq.com
 * @date 2019-07-30 22:31:25
 */
public class QueryConfigBrelation implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
	private String id;
	//
	private String fid;
	//
	private String bid;

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
	public void setBid(String bid) {
		this.bid = bid;
	}
	/**
	 * 获取：
	 */
	public String getBid() {
		return bid;
	}
}
