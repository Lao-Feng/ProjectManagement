package zr.zrpower.bean;

import java.io.Serializable;



/**
 * 
 * 用户业务请求/返回类
 * @author nfzr
 * @email ftl@qq.com
 * @date 2019-07-30 22:31:25
 */
public class QueryConfigInitBean implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
	private String id;
	//
	private String fid;
	//
	private String qleft;
	//
	private String field;
	//
	private String symbol;
	//
	private String wherevalue;
	//
	private String qright;
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
	public void setQleft(String qleft) {
		this.qleft = qleft;
	}
	/**
	 * 获取：
	 */
	public String getQleft() {
		return qleft;
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
	public void setQright(String qright) {
		this.qright = qright;
	}
	/**
	 * 获取：
	 */
	public String getQright() {
		return qright;
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
