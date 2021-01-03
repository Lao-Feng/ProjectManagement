package com.yonglilian.service;

import com.yonglilian.collectionengine.COLL_DOC_PRINT;
import com.yonglilian.collectionengine.CollectionInfo;
import zr.zrpower.common.util.FunctionMessage;

import java.util.List;
import java.util.Map;

/**
 * 自定义表单引擎配置管理服务层
 * @author lwk
 *
 */
public interface ConfigService {
	/**
	 * 查询文档配置表数据实体
	 * @param condition 条件
	 * @return
	 * @throws Exception
	 */
	public CollectionInfo[] allListConfig(String condition) throws Exception;

	/**
	 * 查询文档配置表数据实体(分页形式)
	 * @return
	 * @throws Exception
	 */
	public CollectionInfo[] getListConfig() throws Exception;

	/**
	 * 文档配置表数据编号
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public CollectionInfo getInfoID(String id) throws Exception;

	/**
	 * 添加配置文件
	 * @param bg 配置文件
	 * @return
	 * @throws Exception
	 */
	public FunctionMessage addConfig(CollectionInfo bg) throws Exception;

	/**
	 * 编辑配置文件
	 * @param bg 编辑的配置文件
	 * @return
	 * @throws Exception
	 */
	public FunctionMessage editConfig(CollectionInfo bg) throws Exception;

	/**
	 * 删除配置文件
	 * @param ID 配置文件的ID
	 * @return
	 * @throws Exception
	 */
	public boolean delConfig(String ID) throws Exception;

	/**
	 * 获取表列表
	 * @return
	 * @throws Exception
	 */
	public Map<String, String> doGetTableList() throws Exception;

	/**
	 * 获取字段列表
	 * @return
	 * @throws Exception
	 */
	public Map<String, String> GetFieldList() throws Exception;

	/**
	 * 功能或作用：取出最大ID
	 * @param table
	 * @return
	 * @throws Exception
	 */
	public String getMaxFieldNo(String table) throws Exception;

	/**
	 * 显示表单列表
	 * @param table 查寻的表
	 * @param where 查寻的表的条件
	 * @param value 获取的值
	 * @param name 显示的名字
	 * @param listName 该控件名字
	 * @param def 默认选项
	 * @return
	 * @throws Exception
	 */
	public String showList(String table, String where, String value, 
			String name, String listName, String def) throws Exception;

	/**
	 * 获取数据表列表
	 * @return
	 * @throws Exception
	 */
	public String[][] getTableList() throws Exception;

	/**
	 * 获取数据表的字段列表
	 * @param tableID
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> getFieldList(String tableID) throws Exception;
  
	/**
     * 获取数据表的字段列表
     * @param tableID String   数据表编号
     * @return List            包含(字段编号、字段名称)的List
     */
    public String[][] getFieldList1(String tableID) throws Exception;

    /**
     * 增加打印模版配置
     * @param print COLL_DOC_PRINT 新打印模版配置实体
     * @return FunctionMessage 保存是否成功,成功或是失败消息
     */ 
    public FunctionMessage addPrint(COLL_DOC_PRINT print) throws Exception;

    /**
     * 编辑打印模版配置
     * @param print COLL_DOC_PRINT 配置的实体
     * @return FunctionMessage
     */
    public FunctionMessage editPrint(COLL_DOC_PRINT print) throws Exception;

    /**
     * 功能：删除打印模版配置
     * @return FunctionMessage
     */
    public FunctionMessage deletePrint(String ID) throws Exception;

    /**
     * 功能：获取打印模版配置列表
     * @param type
     * @return String[]
     */
    public COLL_DOC_PRINT[] getPrintList(String type) throws Exception;

    /**
     * 功能：获取打印模版编号
     * @param COLL_DOC_PRINT String
     * @return String[]
     */
    public COLL_DOC_PRINT getPrintID(String id) throws Exception;

    /**
     * 查询最小ID
     * @param TableName
     * @param sID
     * @param ID
     * @return
     * @throws Exception
     */
    public String getMinID(String tableName, String sID,String ID) throws Exception;

    /**
     * 更新配置文件模板
     * @param tempaetID
     * @param tempaet
     * @return
     * @throws Exception
     */
    public boolean updateTempaet(String tempaetID, CollectionInfo tempaet) throws Exception;

    /**
     * 更新配置文件打印模板
     * @param PrintTempaetID
     * @param PrintTempaet
     * @return
     * @throws Exception
     */
    public boolean updatePrintTempaet(String printTempaetID, COLL_DOC_PRINT printTempaet) throws Exception;

    /**
     * 根据过程获取其可控字段的表名.字段名的信息字串列表
     * @param ID 文档配置ID
     * @return String //
     */
    public String getCtrlAbleFieldByFID(String ID) throws Exception;

    /**
     * 可控字段属性处理
     * @param FID String 过程ID
     * @param FieldInfoStr String 可控字段信息字符串
     * @return FunctionMessage
     */
    public FunctionMessage saveCtlFieldInfo(String FID, String fieldInfoStr) throws Exception;

    /**
     * 功能或作用：指定表及ID字段的长度生成最大ID
     * @Return String 执行后返回一个最大ID
     */
    public String getMaxID(String tableName, int idLength) throws Exception;

    /**
     * 生成待选字段列表
     * @param listName
     * @param ID
     * @return
     * @throws Exception
     */
    public String getSFieldList(String listName, String ID) throws Exception;

    /**
     * 生成默认模板
     * @param ID
     * @param type
     * @param fields
     * @return
     * @throws Exception
     */
    public boolean saveTemplaet(String ID, String type, String fields) throws Exception;

    /**
     * 获取数据表的列表
     * @return List 包含(表名称、表中文名称)的List
     */
    public List<Map<String, Object>> getTableList1() throws Exception;

    /**
     * 获取数据表的字段列表
     * @param TableName String   数据表名称
     * @return List            包含(字段名称、字段中文名称)的List
     */
    public List<Object> doGetFieldList(String tableName) throws Exception;

    /**
     * 根据TableName表名获取TableID表ID
     * @param tableName
     * @return
     * @throws Exception
     */
    public String getTableIDByTableName(String tableName) throws Exception;
}
