package zr.zrpower.bean;

import java.io.Serializable;



/**
 * 
 * 用户业务请求/返回类
 * @author nfzr
 * @email ftl@qq.com
 * @date 2019-08-04 15:20:55
 */
public class CollConfigOperateFieldBean implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
	private String id;
	//
	private String fid;
	//
	private String field;
	//
	private String isdisplay;
	//
	private String isedit;
	//
	private String ismustfill;
	//
	private String default1;
	//
	private String isforce;

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
	public void setIsdisplay(String isdisplay) {
		this.isdisplay = isdisplay;
	}
	/**
	 * 获取：
	 */
	public String getIsdisplay() {
		return isdisplay;
	}
	/**
	 * 设置：
	 */
	public void setIsedit(String isedit) {
		this.isedit = isedit;
	}
	/**
	 * 获取：
	 */
	public String getIsedit() {
		return isedit;
	}
	/**
	 * 设置：
	 */
	public void setIsmustfill(String ismustfill) {
		this.ismustfill = ismustfill;
	}
	/**
	 * 获取：
	 */
	public String getIsmustfill() {
		return ismustfill;
	}
	/**
	 * 设置：
	 */
	public void setDefault1(String default1) {
		this.default1 = default1;
	}
	/**
	 * 获取：
	 */
	public String getDefault1() {
		return default1;
	}
	/**
	 * 设置：
	 */
	public void setIsforce(String isforce) {
		this.isforce = isforce;
	}
	/**
	 * 获取：
	 */
	public String getIsforce() {
		return isforce;
	}
}
