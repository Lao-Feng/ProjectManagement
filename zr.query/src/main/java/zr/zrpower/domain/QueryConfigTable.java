package zr.zrpower.domain;

import java.io.Serializable;



/**
 * 数据库表实体
 * 
 * @author nfzr
 * @email ftl@qq.com
 * @date 2019-07-26 01:10:50
 */
public class QueryConfigTable implements Serializable {
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
}
