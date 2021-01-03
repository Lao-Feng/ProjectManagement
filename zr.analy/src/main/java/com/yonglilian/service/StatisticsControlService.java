package com.yonglilian.service;

import com.yonglilian.analyseengine.mode.ANALYSE_STATISTICS_MAIN;
import zr.zrpower.model.SessionUser;

import java.util.List;
import java.util.Map;

/**
 * Title:数据统计引擎服务
 * @author lwk
 *
 */
public interface StatisticsControlService {
	/**
	 * ftl 调整（功能:计算报表）
	 * @param strID 统计配置ID
	 * @param strP1   参数1
	 * @param strP2   参数2
	 * @param strP3	参数3
	 * @param strP4	参数4
	 * @param strP5	参数5
	 * @param UNITID	默认单位id
	 * @param strConn	自定义条件
	 * @return
	 */
	public String getCompute(String strID, String strP1, String strP2, String strP3, String strP4, String strP5,
			SessionUser user, String strConn, String UNITID) throws Exception;

	/**
	 * 功能:得到显示单位格详细信息的组成条件
	 * @param strID	统计配置ID
	 * @param UNITID	点击行的单们位编号
	 * @param TIER	点击列
	 * @param P1	参数1
	 * @param P2	参数2
	 * @param P3	参数3
	 * @param P4	参数4
	 * @param P5	参数5
	 * @param user	用户SESSION
	 * @return 组成的条件
	 */
	public String getLinkWhere(String strID, String UNITID, String TIER, String P1, String P2, String P3, String P4,
			String P5, SessionUser user) throws Exception;

	/**
	 * 根据配置ID得到统计配置实体
	 * @param strID	配置ID
	 * @return String
	 */
	public ANALYSE_STATISTICS_MAIN getSmain(String strID) throws Exception;

	/**
	 * 功能：根据配置ID得到统计结果列表
	 * @param strID	配置ID
	 * @param strUserNo	用户编号
	 * @return 统计结果列表
	 */
	public List<Object> getEndTableList(String strID, String strUserNo) throws Exception;

	/**
	 * 功能：得到报表表格显示的相关属性
	 * @param strID	配置ID
	 * @return 统计结果列表
	 */
	public String[][] getTableShowList(String strID) throws Exception;

	/**
	 * 功能：得到报表表格可显示详细列表的列号串
	 * @param strID	配置ID
	 * @return 列号串
	 */
	public String getTiers(String strID) throws Exception;

	/**
	 * ftl  调整（功能：得到年、季度、月、周、日、自定义日期格式HTML）
	 * @return retValue
	 */
	public String getDateHtml() throws Exception;

	/**
	 * 功能：得到月报日期格式HTML
	 * @return retValue
	 */
	public String getMonthHtml() throws Exception;

	/**
	 * 功能：得到年报日期格式HTML
	 * @return retValue
	 */
	public String getYearHtml() throws Exception;

	/**
	 * 功能：得到日报日期格式HTML
	 * @return retValue
	 */
	public String getDateDayHtml() throws Exception;

	/**
	 * ftl   调整（功能：生成下拉字典HTML）
	 * @return retValue
	 */
	public String getCodeHtml(String tablename) throws Exception;

	/**
     * 功能：得到年份比较条件格式HTML
     * @return retValue
     */
     public String getYearComHtml() throws Exception;

	/**
     **功能：得到年份月份比较条件格式HTML
     * @return retValue
     */
     public String getYearMonthComHtml() throws Exception;

	/**
     **功能：得到年份季度比较条件格式HTML
     * @return retValue
     */
     public String getYearQuarterComHtml() throws Exception;
     /**
 	 * ftl   调整（功能：生成下拉字典HTML）
 	 * @return retValue
 	 */
 	public List<Map<String, Object>> getVueCodeHtml(String tablename) throws Exception;

     /**
      * ftl  调整（得到网页模板形式的显示HTML(套用模板表头形式)）
      * @param strID 配置ID
      */
     public String getShowHtml(String strID, String userID)throws Exception;
     
     /**
      * ftl  调整（得到网页PC端模板形式的显示HTML(套用模板表头形式)）
      * @param strID 配置ID
      */
     public Map<String,Object> getShowPcHtml(String strID, String userID)throws Exception;
     /**
      * ftl  调整（获取统计的临时表数据)）
      * @param strID 配置ID
      */
     public String getShowPcDatas(String strID, String userID)throws Exception;

    /**
	 * ftl  调整（生成默认模板）
	 * @param ID
	 * @return
	 * @throws Exception
	 */
    public boolean savetemplaet(String ID) throws Exception;

	/**
	 * 获取导出表格的行标题
	 * @return String[]
	 */
    public String[] getTHTitle(String ID) throws Exception;

	/**
	 * 是否转成数字
	 * @param ID
	 * @return
	 */
    public String[] getIsNumber(String ID) throws Exception;

	/**
	 * 得导出excel的数据
	 * @param strID
	 * @param userID
	 * @return
	 * @throws Exception
	 */
    public String[][] getExcelData(String strID, String userID) throws Exception;

}