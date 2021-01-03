package com.yonglilian.service;

import com.yonglilian.queryengine.Request;
import com.yonglilian.queryengine.mode.QUERY_CONFIG_BUTTON;
import com.yonglilian.queryengine.mode.QUERY_CONFIG_TABLE;
import zr.zrpower.model.SessionUser;

/**
 * 查询控制管理EJB
 * @author nfzr
 * 
 */
public interface QueryService {
	/**
	 * 获取导出表格的行标题
	 * @return String[]
	 */
	public String[] getQueryTHTitle(String ID) throws Exception;

	/**
	 * 获取查询标题
	 * @return String
	 */
	public String getQueryTitle(String ID) throws Exception;

	/**
	 *根据查询配置建立查询输入项
	 * @param ID 配置ID
	 * @return String
	 */
	public String createQueryItem(String ID, String QITEMTYPE, SessionUser User, Request Rq) throws Exception;

	public int getQueryItemRowNum(String ID, String QITEMTYPE) throws Exception;

	/**
	 *生成查询的结果表
	 * @param request
	 * @return String
	 */
	public String getQueryResultTable(String ID, String QTABLETYPE, String queryType, 
			SessionUser User, Request Rq, String P1, String P2, String P3, String P4, 
			String P5, String ifFirstData, int CurrentPage, String swidth, String sheight) throws Exception;

	/**
	 *生成查询的结果打印表
	 * @param request
	 * @return String
	 */
	public String getQueryResultPrintTable(String strID, SessionUser User,
			String P1, String P2, String P3, String P4, String P5) throws Exception;

	public String getQueryResultTableBySql(String sqlStr, int currentPage, int pageSize, 
			String[][] columnsName, 
			String chkcolumnName, String chkType, boolean eDblClick) throws Exception;

	/**
	 * 根据SQL语句生成结果页面
	 * @param sqlStr
	 * @param currentPage
	 * @param pageSize
	 * @param chkcolumnName 选择列名称
	 * @param chkType 选择类型 0 没有选择 1 单选 2 多选
	 * @param eDblClick 表格行是否支持双击操作
	 * @param ifFirtSnCol 是否有第一列序号列
	 * @return String
	 */
	public String getQueryResultTableBySql(String sqlStr, int currentPage, int pageSize, 
			String[][] columnsName, String chkcolumnName, 
			String chkType, boolean eDblClick, boolean ifFirtSnCol) throws Exception;

	/**
	 * 创建页面导航链接
	 * @param sqlStr
	 * @param currentPage
	 * @param pageSize
	 * @return String
	 */
	public String createPageNavigateBySql(String sqlStr, int currentPage, int pageSize) throws Exception;

	/**
	 * 创建页面导航链接
	 * @param currentPage
	 * @return String
	 */
	public String[] createPageNavigate(String ID, SessionUser User, String P1,
			String P2, String P3, String P4, String P5, int currentPage) throws Exception;

	/**
	 * 创建查询配置按钮
	 *
	 * @return String
	 */
	public String createButtonNavigate(String ID, String QTABLETYPE,
			SessionUser User, String BUTTONIDS) throws Exception;

	public String[][] getExcelData(String ID, SessionUser User, String P1, String P2, 
			String P3, String P4, String P5, String strPage, String crow) throws Exception;

	/**
	 * 页面参数类型
	 * @param buttonObj QUERY_CONFIG_BUTTON
	 * @return String
	 */
	public String createButtonScript(QUERY_CONFIG_BUTTON buttonObj, String ID) throws Exception;

	/**
	 * 得到文书列表
	 * @return
	 */
	public String[][] getDocNameArray() throws Exception;

	/**
	 *根据生成是否必填的js
	 * @param ID 配置ID
	 * @return String
	 */
	public String getIsmustJs(String ID, String ISNULL) throws Exception;

	/**
	 * 得到查询结果字段是否转换为数字数组
	 * @param ID
	 * @return
	 * @throws Exception
	 */
	public String[] getIsNumber(String ID) throws Exception;

	/**
	 * 得到字典属性
	 * @param NAME
	 * @return
	 * @throws Exception
	 */
	public String[][] getCodeTable(String NAME) throws Exception;

	public QUERY_CONFIG_TABLE getConfigTable(String ID) throws Exception;

	public QUERY_CONFIG_BUTTON getButtonByID(String id) throws Exception;
}