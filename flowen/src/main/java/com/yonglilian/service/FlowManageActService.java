package com.yonglilian.service;

import com.yonglilian.common.util.FunctionMessage;
import com.yonglilian.flowengine.mode.base.Button;
import com.yonglilian.flowengine.mode.base.Group;
import com.yonglilian.flowengine.mode.config.FLOW_CONFIG_ACTIVITY;
import com.yonglilian.flowengine.mode.config.FLOW_CONFIG_TIME;

import java.util.List;
import java.util.Map;

/**
 * 管理活动的各种信息，包括以下各种活动属性: 
 * 基本属性
 * 策略属性
 * 超时属性
 * 操作属性
 * 可控字段属性
 * 签章属性
 * 参与者属性
 * 特殊属性
 * @author NFZR
 *
 */
public interface FlowManageActService {
	/**
	 * 处理流程活动基本属性信息
	 * @param entityObj
	 *            活动信息的实体
	 * @return FunctionMessage
	 */

	public FunctionMessage saveBaseProperty(FLOW_CONFIG_ACTIVITY entityObj) throws Exception;

	/**
	 * 处理流程活动策略属性信息
	 * @param entityObj
	 *            活动信息的实体
	 * @return FunctionMessage
	 */
	public FunctionMessage saveStrategyProperty(FLOW_CONFIG_ACTIVITY entityObj) throws Exception;

	/**
	 * 处理流程的附件属性信息
	 * @param entityObj
	 *            活动信息的实体
	 * @return FunctionMessage
	 */
	public FunctionMessage saveAttProperty(FLOW_CONFIG_ACTIVITY entityObj) throws Exception;

	public FLOW_CONFIG_ACTIVITY getActivityById(String ID) throws Exception;

	/**
	 * 功能或作用：取出最大ID（活动信息）
	 * @Return String 执行后返回一个最大ID（活动信息）
	 */
	public String getActivityMaxID() throws Exception;

	// ***************************************开始超时属性***************************//
	/**
	 * 根据活动获取所属的超时属性
	 * @param strWhere
	 * @return FLOW_CONFIG_TIME[]
	 */
	public FLOW_CONFIG_TIME[] getOverTimeList(String strWhere) throws Exception;

	/**
	 * 超时属性字段处理
	 * @param FID 过程ID
	 * @param FieldInfoStr
	 *            超时字段信息字符串
	 * @return FunctionMessage
	 */
	public FunctionMessage saveOverTimeFieldInfo(String FID, String FieldInfoStr) throws Exception;

	// ***************************************结束超时属性***************************//

	// ***************************************开始操作属性***************************//
	/**
	 * 获取根据流程活动按钮获取不在活动内的功能按钮列表
	 * @param FLOW_BASE_BUTTON
	 * @return Button[]
	 */
	public Button[] getButtonList() throws Exception;

	/**
	 * 获取根据流程活动按钮获取在活动内的功能按钮列表
	 * @param FLOW_BASE_BUTTON
	 * @return Button[]
	 */
	public Button[] getButtonListByFid(String FID) throws Exception;

	/**
	 * 功能或作用:将指定的功能按钮加入到指定的活动中
	 * @param FID 活动ID
	 * @param BUTTONID
	 *            功能按钮ID
	 */
	public FunctionMessage addButtonToActivity(String FID, String BUTTONID) throws Exception;

	/**
	 * 功能或作用:从指定的活动中删除功能
	 * @param FID 活动ID
	 * 
	 */
	public FunctionMessage deleteButtonFromActivity(String FID) throws Exception;

	// ***************************************结束操作属性***************************//

	// ***************************************开始可控字段属性***************************//
	/**
	 * 根据过程获取其可控字段的表名.字段名的信息字串列表
	 * @param FID 过程ID
	 * @param AID 活动ID
	 * @return String
	 */
	public String getCtrlAbleFieldByFID(String FID, String AID) throws Exception;

	/**
	 * 可控字段属性处理
	 * 
	 * @param FID 过程ID
	 * @param FieldInfoStr
	 *            可控字段信息字符串
	 * @return FunctionMessage
	 */
	public FunctionMessage saveCtlFieldInfo(String FID, String FieldInfoStr) throws Exception;
	// ***************************************结束可控字段属性***************************//

	// ***************************************开始参与者属性***************************//
	/**
	 * 获取根据流程活动按钮获取不在活动内的功能按钮列表
	 * @return Group[]
	 */
	public Group[] getUserGroupList() throws Exception;

	/**
	 * 获取根据流程活动获取相应的用户组
	 * @param FID
	 * @return Group[]
	 */
	public Group[] getUserGroupListByFid(String FID) throws Exception;

	/**
	 * 功能或作用:将指定的用户组加入到指定的活动中
	 * @param FID 活动ID
	 * @param GROUPID 用户组ID
	 */
	public FunctionMessage addUserGroupToActivity(String FID, String GROUPID) throws Exception;

	/**
	 * 功能或作用:从指定的活动中删除用户组
	 * @param FID 活动ID
	 * 
	 */
	public FunctionMessage deleteUserGroupFromActivity(String FID) throws Exception;

	/**
	 * 获取数据表的列表
	 * @return List 包含(表名称、表中文名称)的List
	 */
	public List<Map<String, Object>> getTableList() throws Exception;

	/**
	 * 根据表名称获取数据表的列表
	 * 
	 * @return List 包含(表名称、表中文名称)的List
	 */
	public List<Map<String, Object>> getTableList1(String StrWhere) throws Exception;

	/**
	 * 获取数据表的字段列表
	 * 
	 * @param TableName
	 *            String 数据表名称
	 * @return List 包含(字段名称、字段中文名称)的List
	 */
	public List<Map<String, Object>> getFieldList(String TableName) throws Exception;

	////////////////////////////////////////////////////////////////////////////////
	/**
	 * 功能或作用：指定表及ID字段的长度生成最大ID
	 * 
	 * @Return String 执行后返回一个最大ID
	 */

	public String getMaxID(String tableName, int idLength) throws Exception;

	/**
	 * 功能：根据流程编号得到对就应的自定义表单引擎的文档配置ID
	 * 
	 * @param strID流程编号
	 * @return returnValue 返回文档配置ID
	 */
	public String getDocID(String strID) throws Exception;
}