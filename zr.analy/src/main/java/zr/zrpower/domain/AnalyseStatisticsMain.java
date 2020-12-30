package zr.zrpower.domain;

import java.io.Serializable;



/**
 * 
 * 数据库表实体
 * @author nfzr
 * @email ftl@qq.com
 * @date 2019-08-01 01:49:08
 */
public class AnalyseStatisticsMain implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
	private String id;
	//
	private String statisticsname;
	//
	private String sdesc;
	//
	private String tableid;
	//
	private String timestype;
	//
	private String planartable;
	//
	private String planarfield;
	//
	private String cplanarfield;
	//
	private String wherevalue;
	//
	private String exceltemplate;
	//
	private String planarfieldname;
	//
	private String wismatch;
	//
	private String cjoin;
	//
	private String isunit;
	//
	private String sinputtype;
	//
	private String sinputpage;
	//
	private String isagv;
	//
	private String issum;
	//
	private String isshowtype;
	//
	private String showlink;
	//
	private String timetemplate;
	//
	private String codetable;
	//
	private String tbutton;
	//
	private String isnumber;
	//
	private String addfield;
	//
	private String dcode;
	//
	private String iszero;

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
	public void setStatisticsname(String statisticsname) {
		this.statisticsname = statisticsname;
	}
	/**
	 * 获取：
	 */
	public String getStatisticsname() {
		return statisticsname;
	}
	/**
	 * 设置：
	 */
	public void setSdesc(String sdesc) {
		this.sdesc = sdesc;
	}
	/**
	 * 获取：
	 */
	public String getSdesc() {
		return sdesc;
	}
	/**
	 * 设置：
	 */
	public void setTableid(String tableid) {
		this.tableid = tableid;
	}
	/**
	 * 获取：
	 */
	public String getTableid() {
		return tableid;
	}
	/**
	 * 设置：
	 */
	public void setTimestype(String timestype) {
		this.timestype = timestype;
	}
	/**
	 * 获取：
	 */
	public String getTimestype() {
		return timestype;
	}
	/**
	 * 设置：
	 */
	public void setPlanartable(String planartable) {
		this.planartable = planartable;
	}
	/**
	 * 获取：
	 */
	public String getPlanartable() {
		return planartable;
	}
	/**
	 * 设置：
	 */
	public void setPlanarfield(String planarfield) {
		this.planarfield = planarfield;
	}
	/**
	 * 获取：
	 */
	public String getPlanarfield() {
		return planarfield;
	}
	/**
	 * 设置：
	 */
	public void setCplanarfield(String cplanarfield) {
		this.cplanarfield = cplanarfield;
	}
	/**
	 * 获取：
	 */
	public String getCplanarfield() {
		return cplanarfield;
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
	public void setExceltemplate(String exceltemplate) {
		this.exceltemplate = exceltemplate;
	}
	/**
	 * 获取：
	 */
	public String getExceltemplate() {
		return exceltemplate;
	}
	/**
	 * 设置：
	 */
	public void setPlanarfieldname(String planarfieldname) {
		this.planarfieldname = planarfieldname;
	}
	/**
	 * 获取：
	 */
	public String getPlanarfieldname() {
		return planarfieldname;
	}
	/**
	 * 设置：
	 */
	public void setWismatch(String wismatch) {
		this.wismatch = wismatch;
	}
	/**
	 * 获取：
	 */
	public String getWismatch() {
		return wismatch;
	}
	/**
	 * 设置：
	 */
	public void setCjoin(String cjoin) {
		this.cjoin = cjoin;
	}
	/**
	 * 获取：
	 */
	public String getCjoin() {
		return cjoin;
	}
	/**
	 * 设置：
	 */
	public void setIsunit(String isunit) {
		this.isunit = isunit;
	}
	/**
	 * 获取：
	 */
	public String getIsunit() {
		return isunit;
	}
	/**
	 * 设置：
	 */
	public void setSinputtype(String sinputtype) {
		this.sinputtype = sinputtype;
	}
	/**
	 * 获取：
	 */
	public String getSinputtype() {
		return sinputtype;
	}
	/**
	 * 设置：
	 */
	public void setSinputpage(String sinputpage) {
		this.sinputpage = sinputpage;
	}
	/**
	 * 获取：
	 */
	public String getSinputpage() {
		return sinputpage;
	}
	/**
	 * 设置：
	 */
	public void setIsagv(String isagv) {
		this.isagv = isagv;
	}
	/**
	 * 获取：
	 */
	public String getIsagv() {
		return isagv;
	}
	/**
	 * 设置：
	 */
	public void setIssum(String issum) {
		this.issum = issum;
	}
	/**
	 * 获取：
	 */
	public String getIssum() {
		return issum;
	}
	/**
	 * 设置：
	 */
	public void setIsshowtype(String isshowtype) {
		this.isshowtype = isshowtype;
	}
	/**
	 * 获取：
	 */
	public String getIsshowtype() {
		return isshowtype;
	}
	/**
	 * 设置：
	 */
	public void setShowlink(String showlink) {
		this.showlink = showlink;
	}
	/**
	 * 获取：
	 */
	public String getShowlink() {
		return showlink;
	}
	/**
	 * 设置：
	 */
	public void setTimetemplate(String timetemplate) {
		this.timetemplate = timetemplate;
	}
	/**
	 * 获取：
	 */
	public String getTimetemplate() {
		return timetemplate;
	}
	/**
	 * 设置：
	 */
	public void setCodetable(String codetable) {
		this.codetable = codetable;
	}
	/**
	 * 获取：
	 */
	public String getCodetable() {
		return codetable;
	}
	/**
	 * 设置：
	 */
	public void setTbutton(String tbutton) {
		this.tbutton = tbutton;
	}
	/**
	 * 获取：
	 */
	public String getTbutton() {
		return tbutton;
	}
	/**
	 * 设置：
	 */
	public void setIsnumber(String isnumber) {
		this.isnumber = isnumber;
	}
	/**
	 * 获取：
	 */
	public String getIsnumber() {
		return isnumber;
	}
	/**
	 * 设置：
	 */
	public void setAddfield(String addfield) {
		this.addfield = addfield;
	}
	/**
	 * 获取：
	 */
	public String getAddfield() {
		return addfield;
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
	public void setIszero(String iszero) {
		this.iszero = iszero;
	}
	/**
	 * 获取：
	 */
	public String getIszero() {
		return iszero;
	}
}
