package com.yonglilian.service;

import com.yonglilian.flowengine.mode.base.Package;
import com.yonglilian.flowengine.mode.config.FLOW_CONFIG_ACTIVITY_CONNE;
import com.yonglilian.flowengine.mode.config.FLOW_CONFIG_PROCESS;
import zr.zrpower.common.util.FunctionMessage;

import java.util.List;
import java.util.Vector;

/**
 * 流程引擎-流程配置管理
 * @author lwk
 *
 */
public interface FlowConfigService {
	/**
	 * 功能：增加流程基本属性信息
	 * @param Pr 新流程基本属性实体
	 * @param strFormfields
	 * @param strFormfields1
	 * @return
	 * @throws Exception
	 */
	public FunctionMessage addPr(FLOW_CONFIG_PROCESS Pr, String strFormfields, String strFormfields1) throws Exception;

	/**
	 * 功能：编辑流程基本属性
	 * @param Pr 编辑流程基本属性的实体
	 * @return FunctionMessage
	 */
	public FunctionMessage editPr(FLOW_CONFIG_PROCESS Pr, String strFormfields, String strFormfields1) throws Exception;

	/**
	 * 功能：删除流程流程及流程的相关配置
	 * @param ID 流程ID
	 * @return FunctionMessage 返回执行情况
	 */
	public FunctionMessage deletePr(String ID) throws Exception;

	/**
	 * 功能：获取流程基本属性列表
	 * @param FLOW_CONFIG_PROCESS
	 * @return String[]
	 */
	public FLOW_CONFIG_PROCESS[] getFlowPROCESSList(String strwhere) throws Exception;

	/**
	 * 功能：获取流程基本属性编号
	 * @param FLOW_CONFIG_PROCESS
	 * @return String[]
	 */
	public FLOW_CONFIG_PROCESS getPROCESSID(String id) throws Exception;

	/**
	 * 功能：得到所有流程用户的选择列表
	 * @param Lst_Name列表名称
	 * @param UnitID 用户单位
	 * @return returnValue 返回流程用户名称
	 */
	public String group_AllList(String Lst_Name) throws Exception;

	/**
	 * 功能：生成流程用户列表（监控属性）
	 * @param strGroupId 流程ID
	 * @param Lst_Name列表名称
	 * @param Is_Enabled是否只读
	 * @return returnValue 返回记录集
	 */
	public String flow_Group_List(String strGroupId, String Lst_Name, String Is_Enabled) throws Exception;

	/**
	 * 功能：生成流程用户列表(重分配属性)
	 * @param strGroupId 流程用户编号
	 * @param Lst_Name列表名称
	 * @param Is_Enabled是否只读
	 * @return returnValue 返回记录集
	 */
	public String flow_Group_List1(String strGroupId, String Lst_Name, String Is_Enabled) throws Exception;

	/**
	 * 功能：分析规则字符串，生成数组
	 * @param strItems 字符串
	 * @param strItemMark 标识符
	 * @return 返回数组
	 */
	public List<Object> getArrayList(String strItems, String strItemMark) throws Exception;

	/**
	 * 功能：获取流程列表
	 * @return returnValue 返回记录集
	 */
	public FLOW_CONFIG_PROCESS[] getFlowList() throws Exception;

	/**
	 * 功能：根据流程编号查询出对应的流程包ID
	 * @param ID 流程对应的ID
	 * @return String
	 * @throws Exception
	 */
	public String getFlowPackage(String ID) throws Exception;

	/**
	 * 功能：获取流程列表
	 * @param strPackageID包ID
	 * @return returnValue 返回记录集
	 */
	public FLOW_CONFIG_PROCESS[] getFlowList(String strPackageID) throws Exception;

	/**
	 * 功能：获取流程列表(与当流程流程属于同一个包的流程)
	 * @param unitid 单位编号
	 * @return returnValue 返回记录集
	 */
	public FLOW_CONFIG_PROCESS[] getFlowList1() throws Exception;

	/**
	 * 功能：获取流程列表
	 * @param strPackageID包ID
	 * @return returnValue 返回记录集
	 */
	public FLOW_CONFIG_PROCESS[] getFlowList2(String strPackageID) throws Exception;

	/**
	 * 功能：执行批量sql
	 * @param vec 实体集
	 * @param cnum1 当前次数
	 * @param cnum2 当前记录数
	 * @return returnValue 返回结果
	 */
	public boolean copyFlowPublic(Vector<Object> vec, int cnum1, int cnum2) throws Exception;

	/**
	 * 功能：拷贝某流程包
	 * @param strID 待拷贝的包ID
	 * @param strUnitID 当前单位编号
	 * @return returnValue 返回结果
	 */
	public Vector<Object> copyFlowPackage(String strID, String strUnitID) throws Exception;

	/**
	 * 功能：拷贝某流程
	 * @param strID 待拷贝的流程ID
	 * @return returnValue 返回结果
	 */
	public boolean copyFlow(String strID) throws Exception;

	/**
	 * 功能：拷贝某活动
	 * @param strID 待拷贝的活动ID
	 * @return returnValue 返回结果
	 */
	public boolean copyActivity(String strID) throws Exception;

	public boolean activitynew(String strID) throws Exception;

	public boolean activitynew1(String strflowcno, String strflowsno) throws Exception;

	/**
	 * 功能：得到流程包的记录集(自定义条件)
	 * @param strWhere 自定义条件
	 * @return returnValue 返回记录集
	 */
	public Package[] getPackageCustomList(String strWhere) throws Exception;

	/**
	 * 获取流程基本活动属性编号
	 * @param id
	 * @return String[]
	 */
	public FLOW_CONFIG_ACTIVITY_CONNE getActivityID(String id) throws Exception;

	public String getActivityID(String cid, String eid) throws Exception;

	/**
	 * 功能:更新活动关系的属性
	 * @param strID 	关系ID
	 * @param strName 	关系名称
	 * @param strName 	关系描述
	 * @param strwhere1	关系条件
	 * @param type 		接收类型
	 * @param ISNEED 	是否必建流程
	 * @param ISATT 	附件检测
	 * @param strFormfields 接收类型为某一步的处理人时的步骤列表
	 * @return FunctionMessage
	 */
	public FunctionMessage updataActivity(String strID, String strName, String StrDesc1, 
			String strwhere1, String type, String ISNEED, String ISATT, String strFormfields) throws Exception;

	/**
	 * 功能：得到所有流程活动的选择列表
	 * @param Lst_Name列表名称
	 * @param Is_Enabled是否只读
	 * @param strFlowID 当前流程编号
	 * @return returnValue 返回流程用户名称
	 */
	public String activity_AllList(String Lst_Name, String Is_Enabled, String strFlowID) throws Exception;

	/**
	 * 功能：生成流程活动列表
	 * @param strGroupId 流程活编号
	 * @param Lst_Name列表名称
	 * @param Is_Enabled是否只读
	 * @return returnValue 返回记录集
	 */
	public String flow_Activity_List(String strId, String Lst_Name, String Is_Enabled) throws Exception;

	/**
	 * 功能：生成流程活动列表1
	 * @param strId 流程ID
	 * @param Lst_Name列表名称
	 * @param Is_Enabled是否只读
	 * @return returnValue 返回记录集
	 */
	public String flow_Activity_List1(String strId, String Lst_Name, String Is_Enabled) throws Exception;

	/**
	 * 功能:根据活动编号获取活动名称
	 * @param strID
	 * @return
	 * @throws Exception
	 */
	public String getGroupTrueName1(String strID) throws Exception;

	/**
	 * 功能：根据流程编号得到流程的名称
	 * @param strGroupID编号
	 * @return returnValue 返回流程名称
	 */
	public String getProcessTrueName(String strGroupID) throws Exception;

	/**
	 * 功能：根据包编号得到包的组合名称
	 * @param strID编号
	 * @return returnValue 返回包的组合名称
	 */
	public String getPackageName(String strID) throws Exception;

	/**
	 * 功能：根据流程编号得到包的组合名称
	 * @param strID 流程编号
	 * @return returnValue 返回包的组合名称
	 */
	public String getFPackageName(String strID) throws Exception;
}