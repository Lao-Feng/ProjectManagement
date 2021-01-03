package com.yonglilian.service;


import com.yonglilian.queryengine.mode.*;

import java.util.List;

/**
 * 查询配置管理EJB服务层
 * @author lwk
 *
 */
public interface QueryConfigService {
	/**
	 * 根据条件获取关系配置信息列表
	 * @param strWhere 条件字符串
	 * @return QUERY_CONFIG_CONNECTION[]
	 */
	public QUERY_CONFIG_CONNECTION[] getConfigConnectionByWhere(String strWhere) throws Exception;

	/**
	 * 根据ID获取查询配置实体信息
	 * @param ID
	 * @return QUERY_CONFIG_TABLE
	 */
	public QUERY_CONFIG_TABLE getConfigByID(String ID) throws Exception;

	/**
	 * 处理查询配置基本属性信息
	 * @param entityObj 信息实体
	 * @return boolean
	 */
	public boolean saveBaseProperty(QUERY_CONFIG_TABLE entityObj) throws Exception;

	/**
	 * 处理查询配置关联表属性信息
	 * @param FID
	 * @param FieldInfoStr
	 * @return boolean
	 */
	public boolean saveRelationProperty(String FID, String FieldInfoStr) throws Exception;

	/**
	 * 处理初始条件属性信息
	 * @param FID
	 * @param FieldInfoStr
	 * @return boolean
	 */
	public boolean saveInitWhereProperty(String FID, String FieldInfoStr) throws Exception;

	/**
	 * 根据条件获取关系配置初始条件属性信息列表
	 * @param strWhere 条件字符串
	 * @return QUERY_CONFIG_INIT[]
	 */
	public QUERY_CONFIG_INIT[] getConfigInitByWhere(String strWhere) throws Exception;

	/**
	 * 处理查询列属性信息
	 * @param FID
	 * @param FieldInfoStr
	 * @return boolean
	 */
	public boolean saveQueryFieldProperty(String FID, String FieldInfoStr) throws Exception;

	/**
	 * 根据条件获取查询列属性信息列表
	 * @param strWhere 条件字符串
	 * @return QUERY_CONFIG_QUERYFIELD[]
	 */
	public QUERY_CONFIG_QUERYFIELD[] getQueryFieldByWhere(String strWhere) throws Exception;

	/**
	 * 处理结果列属性信息
	 * @param FID 配置表ID
	 * @param disFieldInfoStr 显示列字串
	 * @param hidFieldInfoStr 隐藏列字串
	 * @return boolean
	 */
	public boolean saveResultFieldProperty(String FID, String disFieldInfoStr, 
			String hidFieldInfoStr) throws Exception;

	/**
	 * 根据条件获取结果列属性信息列表
	 * @param strWhere 条件字符串
	 * @return QUERY_CONFIG_SHOWFIELD []
	 */
	public QUERY_CONFIG_SHOWFIELD[] getResultFieldByWhere(String strWhere) throws Exception;

	/**
	 * 处理明细参数属性信息
	 * @param FID
	 * @param FieldInfoStr
	 * @return boolean
	 */
	public boolean saveDetailParameterProperty(String FID, String FieldInfoStr, 
			String BRelationInfoStr) throws Exception;

	/**
	 * 根据条件获取明细参数列属性信息列表
	 * @param strWhere 条件字符串
	 * @return QUERY_CONFIG_PARAMETER []
	 */
	public QUERY_CONFIG_PARAMETER[] getDetailParameterFieldByWhere(String strWhere) throws Exception;

	/**
	 * 保存排序属性信息
	 * @param FID 查询配置ID
	 * @param FieldInfoStr
	 * @return boolean
	 */
	public boolean saveSortProperty(String FID, String FieldInfoStr) throws Exception;

	/**
	 * 根据条件获取明细参数列属性信息列表
	 * @param strWhere 条件字符串
	 * @return QUERY_CONFIG_PARAMETER []
	 */
	public QUERY_CONFIG_ORDER[] getSortByWhere(String strWhere) throws Exception;

	/**
	 * 删除与配置项相关的所有信息
	 * @param FID
	 * @return boolean
	 */
	public boolean deleteConfigByID(String FID) throws Exception;

	/**
	 * 功能或作用：取出最大的记录流水号
	 * @param TableName 数据库表名
	 * @param FieldName 数据库字段名称
	 * @param FieldLen 数据库字段长度
	 * @Return MaxNo 执行后返回一个MaxNo字符串
	 */
	public String getMaxFieldNo(String TableName, String FieldName, int FieldLen) throws Exception;

	/**
	 * 查询符合条件的查询配置
	 * @param where 查询条件
	 * @return QUERY_CONFIG_TABLE[]
	 */
	public QUERY_CONFIG_TABLE[] all_Query_List(String where) throws Exception;

	/**
	 * 获取不包含指定表名称的数据表列表
	 * @param tableName 表名
	 * @return List 包含(表名称、表中文名称)的List
	 */
	public List<Object> getTableList(String tableName) throws Exception;

	/**
	 * 获取数据表的字段列表
	 * @param TableName 数据表名称
	 * @return List 包含(字段名称、字段中文名称)的List
	 */
	public List<Object> getFieldList(String TableName) throws Exception;

	/**
	 * 获取数据表的字段列表 表名.表名.表名......
	 * @param TableName 数据表名称
	 * @param flag 是否包含字段类型
	 * @return List 包含(字段名称、字段中文名称)的List
	 */
	public List<Object> getTableFieldList(String TableName, boolean flag) throws Exception;

	/**
	 * 获取表名.字段名中文
	 * @param tableFieldName 表名.字段名(英文)
	 * @return resultStr 表名.字段名中文
	 */
	public String getTableFieldCnNameByEn(String tableFieldName) throws Exception;

	/**
	 * 查询所有此状态的文件
	 * @param prentNo 父结点ID
	 * @return String
	 */
	public String max_OrderNo(String prentNo) throws Exception;

	/**
	 * 复制查询配置-----
	 * @param strID
	 * @return
	 * @throws Exception
	 */
	public boolean querycopy(String strID) throws Exception;

	/**
	 * 得到查询字段是否为精确查询
	 * @param strID
	 * @param strFiled
	 * @return
	 * @throws Exception
	 */
	public String getPrecision(String strID, String strFiled) throws Exception;

	/**
	 * 设置查询字段是否为精确查询
	 * @param strID
	 * @param strFiled
	 * @param svalue
	 * @return
	 */
	public boolean setPrecision(String strID, String strFiled, String svalue) throws Exception;

	/**
	 * 新建查询配置
	 * @return
	 * @throws Exception
	 */
	public String newQuery() throws Exception;

	/**
	 * 功能：获取流程变量配置列表
	 * @return String[]
	 */
	public Variable[] getVariableList() throws Exception;

	/**
	 * 功能:生成字典列表
	 * @param tableName 表名
	 * @return resultStr 返回字典列表
	 */
	public String getDictList(String tableName) throws Exception;
}