package zr.zrpower.service;

import com.alibaba.fastjson.JSONArray;
import zr.zrpower.common.util.FunctionMessage;
import zr.zrpower.model.dbmanage.BPIP_FIELD;

/**
 * 字段名表管理服务层
 * @author lwk
 *
 */
public interface FieldService {
	/**
	 * 加字段名信息
	 * @param TC 新字段名表实体
	 * @return 保存是否成功,成功或是失败消息
	 * @throws Exception
	 */
	public FunctionMessage addTC(BPIP_FIELD TC) throws Exception;

	/**
     * 编辑字段名表
     * @param TC BPIP_FIELD 编辑的实体
     * @return FunctionMessage
     *
     */
	public FunctionMessage editTC(BPIP_FIELD TC) throws Exception;

	/**
     * 删除字段名
     * @return FunctionMessage
     *
     */
	public FunctionMessage deleteTC(String ID) throws Exception;

	/**
	 * 获取字段名列表
	 * @param BPIP_FIELD String
	 * @return String[][]
	 */
	public BPIP_FIELD[] getBpipFieldList(String condition) throws Exception;

	/**
	 * 获取字段名编号
	 * @param BPIP_FIELD String
	 * @return String[][]
	 */
	public BPIP_FIELD getFieldId(String fieldID) throws Exception;

	/**
	 * 查询最小TABLEID
	 * @param strok
	 * @return
	 */
	public String getMinTABLEID(String strok) throws Exception;

	/**
	 * 根据表编号获取字段中文名称
	 * @param where
	 * @return
	 * @throws Exception
	 */
	public BPIP_FIELD[] getCHINESENAME(String where) throws Exception;

	/**
	 * 根据表编号获取字段中文名称1
	 * @param tableID
	 * @return
	 * @throws Exception
	 */
	public BPIP_FIELD[] getCHINESENAME1(String tableID) throws Exception;

	/**
	 * 根据表编号获取字段名称
	 * @param where
	 * @return
	 * @throws Exception
	 */
	public BPIP_FIELD[] getFIELDNAME(String where) throws Exception;

	/**
	 * 根据表编号获取字段名称1
	 * @param tableID
	 * @return
	 * @throws Exception
	 */
	public BPIP_FIELD[] getFIELDNAME1(String tableID) throws Exception;

	/**
	 * 根据表编号获取表名称
	 * @param tableID
	 * @return String
	 */
	public String getTableName(String tableID) throws Exception;

	/**
	 * 根据表编号获取表中文名称
	 * @param tableID
	 * @return String
	 */
	public String getTableCHINESENAME(String tableID) throws Exception;

	/**
	 * 根据字段编号获取字段中文名称
	 * @param fieldId   编号
	 * @return String
	 */
	public String getFieldCHINESENAME(String fieldId) throws Exception;

	/**
     * 功能或作用：根据表编号获取字段记录计算字段记录
     * @Return String 执行后返回一个最大字符
     */
	public String sumTable(String ID) throws Exception;

	/**
	 * 根据表编号和字段编号获取表中文名称和字段中文名称
	 * @param tableID String   表编号和字段编号
	 * @return 格式：表名称.字段名称
	 */
	public String getTaNAME(String tableID) throws Exception;

	/**
     * 获取表列表
     * @param tableID String
     * @return String[][]
     */
    public String[][] getFieldList(String tableID) throws Exception;

    /**
     * 生成带表的下拉列表
     * @param tableID String  选中表ID
     * @param strID String    选中的表名称
     * @return String         下拉列表的HTML代码
     */
    public String showFieldSelect(String tableID, String strID) throws Exception;

    /**
     * 生成用二维数组填充的下拉列表
     * @param tableList String[][]  二维字符串
     * @param strID String       选中的代码值（可以为空）
     * @return String              下拉列表的HTML代码
     */
    public String showFieldSelect(String[][] tableList, String strID) throws Exception;

    /**
     * 功能或作用：根据编号查询表,字段中文名称（通用）
     * String tableOrFieldChinesename  表和字段名
     * String tableOrFieldTablename    表或字段的中文名
     * String tableOrFieldId           表或字段所在的表的条件字段
     * String tableOrFieldIdCondition  表或字段所在的表的条件字段的查询值
     * @Return String 执行后返回所要的名称
	 */
	public String getAllTableFieldName(String tableOrFieldChinesename,
                                       String tableOrFieldTablename,
                                       String tableOrFieldId,
                                       String tableOrFieldIdCondition) throws Exception;

	/**
	 * 得到关联下拉选择项
	 * @param tableName
	 * @param fieldName
	 * @param sID
	 * @return
	 * @throws Exception
	 */
	public JSONArray dtselect(String tableName, String fieldName, String sID) throws Exception;
}