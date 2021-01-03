package com.yonglilian.service;

import com.yonglilian.flowengine.mode.monitor.FLOW_CONFIG_ENTRUST;
import com.yonglilian.flowengine.mode.monitor.FLOW_RUNTIME_ACTIVITY;
import com.yonglilian.flowengine.mode.monitor.FLOW_RUNTIME_ENTRUSTLOG;
import com.yonglilian.flowengine.mode.monitor.FLOW_RUNTIME_PROCESS;
import zr.zrpower.common.util.FunctionMessage;
import zr.zrpower.model.BPIP_USER;

import java.util.Map;

/**
 * 流程监控函数库服务层
 * @author lwk
 *
 */
public interface FlowMonitorService {
	/**
	 * 功能:获取流程流转列表
	 * @param strWhere 查询条件
	 * @return returnValue
	 */
	public FLOW_RUNTIME_PROCESS[] getProList(int currentPage, int PageSize, String strWhere) throws Exception;

	/**
	 * 功能:获取流程流转列表(处理中的流程)
	 * @param strWhere 查询条件
	 * @return returnValue
	 */
	public FLOW_RUNTIME_PROCESS[] getProList2(String strUserID, int currentPage, 
			int PageSize, String strwhere) throws Exception;

	/**
	 * 功能:获取完成的流程流转列表
	 * @param UserID 用户ID
	 * @return returnValue
	 */
	public FLOW_RUNTIME_PROCESS[] getProFinshList(String UserID, int currentPage, 
			int PageSize, String strwhere) throws Exception;

	//------------------------日志------------------------//
	/**
	 * 功能：删除选中日志
	 * @param strID 选中日志ID
	 * @return FunctionMessage
	 */
	public FunctionMessage delDaily(String strID) throws Exception;

	/**
	 * 功能:删除所有日志
	 * @param UnitId 单位ID
	 * @return boolean - true成功，false失败
	 */
	public boolean delDailyList() throws Exception;

	/**
	 * 功能:获取流程委托日志列表
	 * @param UnitId 单位ID
	 * @return returnValue
	 */
	public FLOW_RUNTIME_ENTRUSTLOG[] dailyList(int currentPage, int PageSize) throws Exception;

	/**
	 * 功能:获取流程流转列表(流程监控和流程任务重分配)
	 * @param strUserID 用户ID
	 * @param strUnit 行政区划
	 * @param strAllUnit 单位编号
	 * @return returnValue
	 */
	public FLOW_RUNTIME_PROCESS[] getProList1(String strUserID, String strAllUnit, 
			int currentPage, int PageSize, String type, String strwhere) throws Exception;

	/**
	 * 功能:查询流程流转
	 * @param ID 要查询流程移交的ID
	 * @return FLOW_RUNTIME_PROCESS
	 */
	public FLOW_RUNTIME_PROCESS getPro(String ID) throws Exception;

	/**
	 * 功能:流程移交管理
	 * @param strID 流程流转ID
	 * @param strDOPSN 被移交人ID
	 * @param strUserID 当前用户ID
	 * @return returnValue 返回执行情况
	 */
	public boolean flowDevolveManage(String strID, String strDOPSN, String strUserID) throws Exception;

	/**
	 * 功能: 获取人员列表
	 * @param TableName
	 * @return HashMap
	 */
	public Map<String, Object> getUserList() throws Exception;

	/**
	 * 功能:获取活动列表
	 * @param TableName
	 * @return HashMap
	 */
	public Map<String, Object> getActivityList() throws Exception;

	/**
	 * 功能：根据活动编号得到用户的活动名称
	 * @param strUser_No 用户编号
	 * @return returnValue 返回活动名称
	 */
	public String getActivityName1(String strID) throws Exception;

	/**
	 * 流程权限委托管理EJB开始 功能：获取委托人员列表
	 * @param strWhere查询条件
	 * @return returnValue 返回记录集
	 */
	public FLOW_CONFIG_ENTRUST[] getMonitorList(String userid) throws Exception;

	/**
	 * 功能:委托人增加
	 * @param Entrust
	 * @return
	 * @throws Exception
	 */
	public FunctionMessage addEntrust(FLOW_CONFIG_ENTRUST Entrust) throws Exception;

	/**
	 * 功能：根据用户组ID得到所拥有用户ID名称
	 * @param strID 用户ID
	 * @return returnValue 返回
	 */
	public BPIP_USER[] getentrustName() throws Exception;

	/**
	 * 功能:获取所选委托人编号
	 * @param FLOW_CONFIG_PROCESS
	 * @return String[]
	 */
	public FLOW_CONFIG_ENTRUST getEntrustID(String ID) throws Exception;

	/**
	 * 功能:删除委托人员基本属性
	 * @return FunctionMessage
	 */
	public FunctionMessage deleteEntrust(String ID) throws Exception;

	/**
	 * 功能:编辑委托人属性
	 * @param FlowPk 编辑委托的实体
	 * @return FunctionMessage
	 */
	public FunctionMessage editEntrust(FLOW_CONFIG_ENTRUST Entrust) throws Exception;

	/**
	 * 功能或作用：根据浏览对象编号获取用户真实姓名
	 * @数组确定浏览对象
	 * @param strIUSERNO 受委托人对象编号
	 * @return 返回用户的真实姓名
	 */
	public String getTrueName(String strIUSERNO) throws Exception;

	/**
	 * 功能或作用：获取当前服务器日期（字符型）
	 * @return 当前日期（字符型）
	 */
	public String getDateAsStr() throws Exception;

	/**
	 * 获取完成流程的列表
	 * @param strWhere 查询条件
	 * @return returnValue
	 */
	public FLOW_RUNTIME_ACTIVITY[] getOkList(String strWhere) throws Exception;

	/**
	 * 流程委托管理EJB开始 功能:获取流程流转过程列表
	 * @param strWhere 查询条件
	 * @return returnValue
	 */
	public FLOW_RUNTIME_PROCESS[] getProActList(String strWhere) throws Exception;

	/**
	 * 功能：流程委托管理
	 * @param strID 流程流转ID
	 * @param strDOPSN 被委托人ID
	 * @param strUserID 当前用户ID
	 * @param strUNIT 单位编号
	 * @return returnValue 返回执行情况
	 */
	public boolean flowEntrustManage(String strID, String strDOPSN, String strUserID, String strUNIT) throws Exception;

	/**
	 * 功能：流程任务重分配
	 * @param strID 流程流转ID
	 * @param strDOPSN 待分配人员的ID
	 * @param strUserID 当前用户的ID
	 * @param strUNIT 单位编号
	 * @return returnValue 返回执行情况
	 */
	public boolean flowAnewManage(String strID, String strDOPSN, String strUserID, String strUNIT) throws Exception;

	/**
	 * 功能:获取人员列表
	 * @param TableName
	 * @return HashMap
	 */
	public Map<String, Object> getUserList(String field) throws Exception;

	/**
	 * 功能:得到待办流程列表
	 * @param strUserNo 当前用户编号
	 * @return returnValue 返回待办流程列表
	 */
	public FLOW_RUNTIME_PROCESS[] getFlowTransactList(String strUserNo, String type) throws Exception;

	/**
	 * 功能:得到某一父类下的待办流程列表 *@param strFID 父类编号
	 * @param strUserNo 当前用户编号
	 * @return returnValue 返回待办流程列表
	 */
	public FLOW_RUNTIME_PROCESS[] getFlowTransactList1(String strFID, String strUserNo) throws Exception;

	/**
	 * 功能:得到有个人待办流程的父类列表
	 * @param strUserNo 当前用户编号
	 * @return returnValue 返回父类列表
	 */
	public FLOW_RUNTIME_PROCESS[] getFlowTransactParentList(String strUserNo) throws Exception;

	/**
	 * 功能:得到当前步骤的发送人姓名
	 * @param strAID 活动ID
	 * @param FID 流转ID
	 * @return 返回人员姓名
	 */
	public String getCSendUser(String strAID, String FID) throws Exception;

	/**
	 * 功能:得到更多的待办流程中[流转id+当前步骤/发送人]对照表1
	 * @param strUserID 用户ID
	 * @return 返回对照表
	 */
	public Map<String, Object> getSendUserHashtable1(String strUserID) throws Exception;

	/**
	 * 功能:得到更多的待办流程中[流转id+当前步骤/发送人]对照表2
	 * @param strUserID 用户ID
	 * @return 返回对照表
	 */
	public Map<String, Object> getSendUserHashtable2(String strUserID) throws Exception;

	/**
	 * 功能：得到当前用户对当前流转的流程拥有的任务重分配的类别
	 * @param strUserID 用户编号
	 * @param strID 流程流转编号
	 * @return returnValue 类别 (1 本部门 2 本单位)
	 */
	public String getUserAllotType(String strUserID, String strID) throws Exception;

	/**
	 * 功能:得到待办流程列表
	 * @param strUserNo 当前用户编号
	 * @param currentPage 当前页
	 * @param PageSize 每页显示记录数
	 * @return returnValue 返回待办流程列表
	 */
	public FLOW_RUNTIME_PROCESS[] getFlowTransactList2(String strUserNo, int currentPage, 
			int PageSize, String strwhere) throws Exception;

	public String sumFlowcontent(String strUserNo) throws Exception;

	/**
	 * 创建页面导航链接（用与流程监控和任务重分配）
	 * @param sqlStr
	 * @param currentPage
	 * @param pageSize
	 * @return String
	 */
	public String createPageNavigateBy(String strUserID, int currentPage, int pageSize, 
			String type, String strwhere) throws Exception;

	/**
	 * 创建页面导航链接
	 * @param sqlStr
	 * @param currentPage
	 * @param pageSize
	 * @return String
	 */
	public String createPageNavigateBySql(String sqlStr, int currentPage, int pageSize) throws Exception;
}