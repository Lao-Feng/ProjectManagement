package zr.zrpower.service;

import zr.zrpower.collectionengine.CollectionInfo;
import zr.zrpower.collectionengine.Request;
import zr.zrpower.model.SessionUser;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * ZRPower自定义表单引擎
 * </p>
 * <p>
 * 自定义表单引擎的核心服务
 * </p>
 * 
 * @author NFZR
 */
public interface CollectionService {
	/**
	 * ftl  调整 （保存提交数据）
	 * @param request 客户端提交数据
	 * @return String 返回主键id
	 */
	public String saveOrUpdate(Request request, SessionUser userInfo) throws Exception;
	/**
	 * ftl  调整 （调用采集）
	 * @param request 客户端采集请求
	 * @return String 自定义表单引擎加工后输出的HTML
	 */
	public String collectionData(Request request, SessionUser userInfo) throws Exception;

	/**
	 * ftl  调整 （采集预览）
	 * @param request 请求信息
	 * @param userInfo 用户会话
	 * @return String 输出的HTML页面字符串
	 */
	public String collectionPriview(Request request, SessionUser userInfo) throws Exception;

	public String printPriview(Request request, SessionUser userInfo) throws Exception;

	/**
	 * ftl  调整(打印界面 MODE=3,CMODE=0 带章打印 MODE=3,CMODE=1 无章打印 MODE=3,CMODE=2 公章位置)
	 * MODE=3,CMODE=33
	 * @param request
	 * @param userInfo
	 * @return String
	 */
	public String printDocument(Request request) throws Exception;

	public String getDateChangeJs(Hashtable<String, Object> htDate) throws Exception;

	public int getPrintTempletCount(String AID) throws Exception;

	public byte[] getCollImage(String id, String pkid, String strImg) throws Exception;

	/**
	 * 取出表中的图片字段数组
	 * @param TableName 表名
	 * @param PrimaryFiled 主键
	 * @param PKID 主键值
	 * @param ImgField 图片字段
	 * @return byte[]
	 */
	public byte[] getImage(String TableName, String PrimaryKey, String PKID, String ImgField) throws Exception;

	/**
	 * 获取数据表的字段列表
	 * @param TableID 数据表编号
	 * @return List 包含(字段编号、字段名称)的List
	 */
	public List<Map<String, Object>> getUserList(String UnitID) throws Exception;

	public String[][] getUserArrary(String UnitID) throws Exception;

	/**
	 * 更新数据
	 * @param data
	 *            Hashtable Colums 数据 其中：Colums.get("TableName") 为要操作的数据表
	 *            Colums.get("UpdateType") 操作方式:"add"添加，"edit"修改
	 *            Colums.get("UserID") 执行操作的用户名 Colums.get("UnitID") 执行操作的用户单位
	 *            其它项为数据项
	 *
	 * @return String 操作成功，返回主键ID，失败返回-1
	 */
	public String updateTable(String updateType, CollectionInfo cInfo, Request request, SessionUser userinfo) throws Exception;

	/**
	 * 功能:生成字典列表
	 * @param tableName 表名
	 * @return 返回字典列表
	 */
	public String doGetDictList(String tableName) throws Exception;

	/**
	 * 根据关联流程的流转ID得到COLLFKID
	 * @param Rid
	 * @return String
	 */
	public String getCollFkidByRid(String Rid) throws Exception;

	/**
	 * 字体信息保存
	 * @param AID 活动ID
	 * @param PKID 主键ID
	 * @param CssFont 字体信息
	 * @return boolean
	 */
	public boolean saveCssFont(String FlowRunId, String CssFont) throws Exception;

	/**
	 * 根据流程运转ID得到当前打印表单的字体设置情况
	 * @param FlowRunId 流程运转ID
	 * @return
	 * @throws Exception
	 */
	public String getFontCss1(String FlowRunId) throws Exception;

	/**
	 * 得到表单配置的名称------------------
	 * @param ID
	 * @return
	 * @throws Exception
	 */
	public String getCollName(String ID) throws Exception;

	/**
	 * ftl 调整（删除表单引擎对应的数据）
	 * @param docid
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public boolean datadelete(String docid, String id) throws Exception;

	/**
	 * 得到照片字段的长度
	 * @param tableName
	 * @param fieldName
	 * @return
	 * @throws Exception
	 */
	public int getBlobSize(String tableName, String fieldName) throws Exception;

}