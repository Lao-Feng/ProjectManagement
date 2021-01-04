package com.yonglilian.service;

import com.yonglilian.common.util.FunctionMessage;
import com.yonglilian.model.CODE_YWXT;

/**
 * 字典管理服务层
 * 
 * @author lwk
 *
 */
public interface UdictService {
	/**
	 * 获取字典列表
	 * @param strWhere
	 * @return
	 * @throws Exception
	 */
    public CODE_YWXT[] getCodeList(String strWhere) throws Exception;

    /**
     * 获取字段名编号
     * @param strCode
     * @param strTableID
     * @return
     * @throws Exception
     */
    public CODE_YWXT getDcitId(String strCode,String strTableID) throws Exception;

    /**
     * 增加字典信息
     * @param DC 新增的实体
     * @param strTableID
     * @return
     * @throws Exception
     */
    public FunctionMessage addDC(CODE_YWXT DC, String strTableID) throws Exception;

    /**
     * 编辑字段名表
     * @param DC 编辑的实体
     * @param strTableID
     * @param id
     * @return
     * @throws Exception
     */
    public FunctionMessage editDC(CODE_YWXT DC, String strTableID, String id) throws Exception;

    /**
     * 删除字段名
     * @param ID
     * @param strTableID
     * @return
     * @throws Exception
     */
    public FunctionMessage deleteDC(String ID, String strTableID) throws Exception;

    /**
     * getCodeList1
     * @param strWhere
     * @return
     * @throws Exception
     */
    public CODE_YWXT[] getCodeList1(String strWhere) throws Exception;

    /**
     * 生成带字典的下拉列表
     * @param DictTable	字典表名称
     * @param strCode	选中的字典代码（可以为空）
     * @return 下拉列表的HTML代码
     * @throws Exception
     */
    public String showDictSelect(String dictTable, String strCode) throws Exception;

    /**
     * 获取字典列表
     * @param tableName String
     * @return String[][]
     */
    public String[][] getDictList(String tableName) throws Exception;

    /**
     * 生成用二维数组填充的下拉列表
     * @param dictList String[][]  二维字符串
     * @param strCode  String      选中的代码值（可以为空）
     * @return String              下拉列表的HTML代码
     */
    public String showDictSelect(String[][] dictList, String strCode) throws Exception;

    /**
     * 获取字典中的中文名称
     * @param tableName	字典表名
     * @param dictCode	字典代码
     * @return 中文名称
     * @throws Exception
     */
    public String getDictName(String tableName, String dictCode) throws Exception;
}