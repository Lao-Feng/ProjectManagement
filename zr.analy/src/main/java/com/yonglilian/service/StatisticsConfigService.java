package com.yonglilian.service;

import com.yonglilian.analyseengine.mode.*;
import zr.zrpower.common.util.FunctionMessage;

import java.util.List;
import java.util.Map;

/**
 * Title: 数据统计EJB服务
 * </p>Description:数据统计函数库</p>
 * @author lwk
 * 
 */
public interface StatisticsConfigService {
	/**
	 ** 功能：得到统计配置列表
	 * @return ANALYSE_STATISTICS_MAIN[]
	 */
	public ANALYSE_STATISTICS_MAIN[] getStatisticsMainList() throws Exception;

	/**
	 * 功能:根据数据统计ID得到配置的实体
	 * @param ID 数据统计ID
	 * @return ANALYSE_STATISTICS_MAIN[]
	 */
	public ANALYSE_STATISTICS_MAIN[] getANALYSE_STATISTICS_MAIN(String ID) throws Exception;

	/**
	 * 功能: 得到统计表的枚举列表
	 * @return HashMap
	 */
	public Map<String, Object> getTableEnumList() throws Exception;

	/**
	 * 功能:获取表列表(下拉选择用)
	 * @return String[][] 包含(数据表名称、数据表中文名称)的字符串数组
	 */
	public String[][] GetTableList() throws Exception;

	/**
	 * 根据表名称获取数据表的列表
	 * @return list 包含(表名称、表中文名称)的list
	 */
	public List<Map<String, Object>> getTableList1(String StrWhere) throws Exception;

	/**
	 * 功能:获取某表下的字段列表(下拉选择用)
	 * @param strID 配置ID
	 * @return String[][] 包含(数据表编号、数据表中文名称)的字符串数组
	 */
	public String[][] getTableFieldList(String strID) throws Exception;

	/**
	 * 功能:获取某表下的字段列表,包含字段类型(下拉选择用)
	 * @param strID 配置ID
	 * @return String[][] 包含(数据表编号、数据表中文名称)的字符串数组
	 */
	public String[][] getTableFieldList1(String strID) throws Exception;

	/**
	 * 功能:获取某表下的日期型字段列表(下拉选择用)
	 * @param strID 配置ID
	 * @return String[][] 包含(数据表编号、数据表中文名称)的字符串数组
	 */
	public String[][] getTableFieldList2(String strID) throws Exception;

	/**
	 * 功能:根据表编号获取某表下的字段列表
	 * @param tableName 表名
	 * @return String[][] 包含(字段名称、字段中文名称)的字符串数组
	 */
	public String[][] getTableFieldList3(String tableName) throws Exception;

	/**
	 * 功能:保存统计配置信息
	 * @param ANALYSE_STATISTICS_MAIN 数据统计实体
	 */
	public FunctionMessage saveAnalyseMain(ANALYSE_STATISTICS_MAIN entityObj) throws Exception;

	/**
	 * 功能:删除数据统计信息
	 * @param strID 配置ID
	 * @return retvalue 是否成功
	 */
	public boolean statisticsDelete(String strID) throws Exception;

	// ************************************开始统计计算字段配置************************************//
	/**
	 * 根据统计配置ID获取所属统计计算字段配置
	 * @param strFID
	 * @return ANALYSE_STATISTICS_CFIELD[]
	 */
	public ANALYSE_STATISTICS_CFIELD[] getAnalyseCFieldList(String strFID) throws Exception;

	/**
	 * 统计计算字段配置处理
	 * @param strFID String 统计案配置ID
	 * @param analyseCField
	 *            String 统计计算字段配置信息字符串
	 * @return FunctionMessage
	 */
	public FunctionMessage saveAnalyseCFieldInfo(String strFID, String analyseCField) throws Exception;

	// ************************************开始统计条件配置************************************//
	/**
	 *根据统计配置ID获取所属统计条件配置
	 * @param strFID String
	 * @return ANALYSE_STATISTICS_WHERE[]
	 */
	public ANALYSE_STATISTICS_WHERE[] getAnalyseWhereList(String strFID) throws Exception;

	/**
     * 统计条件配置处理
     * @param strFID String 统计案配置ID
     * @param analyseWhere String 统计条件配置信息字符串
     * @return FunctionMessage
     */
    public FunctionMessage saveAnalyseWhereInfo(String strFID, String analyseWhere) throws Exception;

	// ***************************************结束统计条件配置***************************
	// ************************************统计计算字段关联配置表***4--1************************
	/**
     *根据统计配置ID获取所属统计关联配置
     * @param strFID String
     * @return ANALYSE_STATISTICS_CCONNECTION[]
     */
    public ANALYSE_STATISTICS_CCONNECTION[] getAnalyseCConnectionList(String strFID) throws Exception;

	/**
     * 统计计算字段关联配置表处理
     * @param FID String 统计案配置ID
     * @param analyseConnection String 统计计算字段关联配置信息字符串
     * @return FunctionMessage
     */
    public FunctionMessage saveAnalyseCConnectionInfo(String FID, String analyseConnection) throws Exception;

	/**
     *根据统计配置ID获取所属自定义统计字段配置
     * @param strFID String
     * @return ANALYSE_STATISTICS_CUSTOM[]
     */
    public ANALYSE_STATISTICS_CUSTOM[] getAnalyseCustomList(String strFID) throws Exception;

	// ************************************开始统计计算字段初始条件配置*****7**********************
	/**
     *根据统计配置ID获取所属统计计算字段初始条件
     * @param strFID String
     * @return ANALYSE_STATISTICS_CWHERE[]
     */
    public ANALYSE_STATISTICS_CWHERE[] getAnalyseCWhereList(String strFID) throws Exception;

	/**
     * 统计计算字段初始条件处理
     * @param FID String 统计案配置ID
     * @param analyseCWhere String 统计计算字段初始条件信息字符串
     * @return FunctionMessage
     */
    public FunctionMessage saveAnalyseCWhereInfo(String FID, String analyseCWhere) throws Exception;

	// ************************************结束统计计算字段初始条件配置***************************//
	// ************************************开始自定义二维统计配置********************************//
	/**
      * 功能或作用：根据编号查询表,字段中文名称（通用）
      * String tableOrFieldchinesename  表和字段名
      * String tableOrFieldTablename    表或字段的中文名
      * String tableOrFieldId           表或字段所在的表的条件字段
      * String tableOrFieldIdCondition  表或字段所在的表的条件字段的查询值
      * @Return String 执行后返回所要的名称
      */
     public String getAllTableFieldName(String tableOrFieldchinesename,
                                        String tableOrFieldTablename,
                                        String tableOrFieldId,
                                        String tableOrFieldIdCondition) throws Exception;

	/**
     * 功能或作用：根据表编号和字段英文名称查询字段中文名称
     * String tableId           表编号
     * String fieldName         字段英文名称
     * @Return String 执行后返回所要的字段中文名称
     */
    public String getAllFieldName(String tableId, String fieldName) throws Exception;

	// =根据表名称得到表表名.字段名======================================================
	/**
     * 获取表名.字段名列表
     * @return String[][]
     */
    public String[][] getTableFieldListList(String tableName) throws Exception;

	/**
     * 功能：生成带表名.字段名的下拉列表
     * @param tableID String    表ID
     * @return String         下拉列表的HTML代码
     */
    public String showTableFieldSelect(String tableID) throws Exception;

	/**
     * 功能：生成用二维数组填充的下拉列表
     * @param tableFieldList String[][]  二维字符串
     * @return String              下拉列表的HTML代码
     */
    public String showTableFieldSelect(String[][] tableFieldList) throws Exception;

	// =============================================================================
	// 根据表名称得到表字段名==========================================================
	/**
     * 获取字段名列表
     * @param tableName
     * @return String[][]
     */
    public String[][] getFieldListList(String tableName) throws Exception;

	/**
     * 功能：生成带字段名的下拉列表
     * @param tableName 表名
     * @return String 下拉列表的HTML代码
     */
    public String showFieldSelect(String tableName) throws Exception;

	/**
     * 功能：生成用二维数组填充的下拉列表
     * @param fieldList String[][]  二维字符串
     * @return String              下拉列表的HTML代码
     */
    public String showFieldSelect(String[][] fieldList) throws Exception;

	// =============================================================================
	/**
     * 获取数据表列表
     * @return String[][] 包含(数据表编号、数据表中文名称)的字符串数组
     */
    public String[][] getTableList() throws Exception;

	/**
     * 获取数据表的字段列表
     * @param tableName
     * @return list 包含(字段编号、字段名称)的list
     */
    public List<Map<String, Object>> getFieldList(String tableName) throws Exception;

	/**
     * 获取数据表的字段列表
     * @param tableName 数据表编号
     * @return List 包含(字段编号、字段名称)的List
     */
    public String[][] getFieldList1(String tableName) throws Exception;

	public void setReTable(String ID,String FILENAME) throws Exception;

	/**
	 * 功能：获取流程变量配置列表
	 * @return String[]
	 */
	public Variable[] getVariableList() throws Exception;
	//---------------------------------自定义函数结束---------------------------------//
}
