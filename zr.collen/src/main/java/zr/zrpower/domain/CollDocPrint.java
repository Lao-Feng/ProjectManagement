package zr.zrpower.domain;

import java.io.Serializable;

/**
 * 
 * 数据库表实体
 * @author nfzr
 * @email ftl@qq.com
 * @date 2019-08-04 15:20:55
 */
public class CollDocPrint implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
	private String id;
	//
	private String docid;
	//
	private String templaet;
	//
	private Integer page;

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
	public void setDocid(String docid) {
		this.docid = docid;
	}
	/**
	 * 获取：
	 */
	public String getDocid() {
		return docid;
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
	public void setPage(Integer page) {
		this.page = page;
	}
	/**
	 * 获取：
	 */
	public Integer getPage() {
		return page;
	}
}
