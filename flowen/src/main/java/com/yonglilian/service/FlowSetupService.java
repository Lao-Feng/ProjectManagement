package com.yonglilian.service;

import com.yonglilian.common.util.FunctionMessage;
import com.yonglilian.flowengine.mode.base.Button;
import com.yonglilian.flowengine.mode.base.Package;
import com.yonglilian.flowengine.mode.base.Variable;

/**
 * 流程引擎-流程基本属性管理服务层
 * @author lwk
 *
 */
public interface FlowSetupService {
	/**
	 * 增加流程包信息
	 * @param FlowPk 新流程包实体
	 * @return FunctionMessage 保存是否成功,成功或是失败消息
	 */
	public FunctionMessage addFlowPk(Package FlowPk, String UNITID) throws Exception;

	/**
	 * 编辑流程包
	 * @param FlowPk 编辑流程包的实体
	 * @return FunctionMessage
	 */
	public FunctionMessage editFlowPk(Package FlowPk) throws Exception;

	/**
	 * 功能：删除流程包
	 * @return FunctionMessage
	 */
	public boolean deleteFlowPk(String strID) throws Exception;

	/**
	 * 功能：获取流程包列表
	 * @param FLOW_CONFIG_PACKAGE
	 * @return String[]
	 */
	public Package[] getFlowPackageList(String strwhere) throws Exception;

	/**
	 * 功能：获取流程包列表
	 * @param FLOW_CONFIG_PACKAGE
	 * @return String[]
	 */
	public Package[] getFlowPackageList1(String strFID) throws Exception;

	/**
	 * 功能：获取流程包列表
	 * @param FLOW_CONFIG_PACKAGE
	 * @return String[]
	 */
	public Package[] getFlowPackageList2(String strFID) throws Exception;

	/**
	 * 功能：获取流程包编号
	 * @param FLOW_CONFIG_PACKAGE
	 * @return String[]
	 */
	public Package getPackageID(String id) throws Exception;

	/**
	 * 功能：获取流程列表
	 * @param TableName
	 * @param strID 流程ID
	 * @return String[][]
	 */
	public String[][] getPackageList(String Table, String strID) throws Exception;

	public String showPackageSelect1(String strID) throws Exception;

	/**
	 * 功能：生成带流程的下拉列表
	 * @param Table 表名称
	 * @param strID 流程ID
	 * @return String 下拉列表的HTML代码
	 */
	public String showPackageSelect(String Table, String strID) throws Exception;

	/**
	 * 功能：生成用二维数组填充的下拉列表
	 * @param Dictlist 二维字符串
	 * @param strCode 选中的代码值（可以为空）
	 * @return String 下拉列表的HTML代码
	 */
	public String showPackageSelect1(String[][] Processlist, String strID) throws Exception;

	//
	public String showPackageSelect(String[][] Packagelist, String strID) throws Exception;

	/**
	 * 增加流程组信息
	 * @param FlowPk 新流程按钮实体
	 * @return FunctionMessage 保存是否成功,成功或是失败消息
	 */
	public FunctionMessage addButton(Button Bt) throws Exception;

	/**
	 * 功能：编辑流程按钮
	 * @param FlowPk 编辑流程按钮的实体
	 * @return FunctionMessage
	 */
	public FunctionMessage editButton(Button Bt) throws Exception;

	/**
	 * 功能：删除流程按钮
	 * @return FunctionMessage
	 */
	public FunctionMessage deleteButton(String ID) throws Exception;

	/**
	 * 获取流程按钮列表
	 * @param FLOW_BASE_BUTTON
	 * @return String[]
	 */
	public Button[] getButtonList() throws Exception;

	/**
	 * 功能：获取流程按钮编号
	 * @param FLOW_BASE_BUTTON
	 * @return String[]
	 */
	public Button getButtonID(String id) throws Exception;

	/**
	 * 功能：获取流程变量配置列表
	 * @return String[]
	 */
	public Variable[] getVariableList() throws Exception;

	/**
	 * 获取文档配置列表
	 * @param DOCNAME
	 * @return String[][]
	 */
	public String[][] getDocnameList(String Table) throws Exception;

	/**
	 * 功能：生成带文档配置的下拉列表
	 * @param Table 表名称
	 * @param strID 选中的文档配置代码（可以为空）
	 * @return String 下拉列表的HTML代码
	 */
	public String showDocnameSelect(String Table, String strID) throws Exception;

	/**
	 * 功能：生成用二维数组填充的下拉列表
	 * @param Dictlist 二维字符串
	 * @param strCode 选中的代码值（可以为空）
	 * @return String 下拉列表的HTML代码
	 */
	public String showDocnameSelect(String[][] DocnameList, String strID) throws Exception;

	/**
	 * 功能：判断某个包下是否有子包
	 * @param strID 包ID
	 * @return 返回结果(1是0否);
	 */
	public String IsSubPackage(String strID) throws Exception;

}