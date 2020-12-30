package zr.zrpower.service;

import zr.zrpower.flowengine.mode.base.Package;
import zr.zrpower.flowengine.mode.config.FLOW_CONFIG_ACTIVITY;
import zr.zrpower.flowengine.mode.config.FLOW_CONFIG_ACTIVITY_CONNE;
import zr.zrpower.flowengine.mode.config.FLOW_CONFIG_PROCESS;
import zr.zrpower.flowengine.mode.config.FLOW_CONFIG_PROCESS_CONNECTION;
import zr.zrpower.flowengine.mode.monitor.FLOW_RUNTIME_PROCESS;
import zr.zrpower.flowengine.runtime.ActFlowRun;
import zr.zrpower.model.SessionUser;

/**
 * 流程控制函数库服务层
 * @author lwk
 *
 */
public interface FlowControlService {
	/**
	 * 功能：生成流程图开始部分标签
	 * @return returnValue 返回标签串
	 */
	public String getFlowStartLabel() throws Exception;

	/**
	 * 功能：生成流程图结束部分标签
	 * @return returnValue 返回标签串
	 */
	public String getFlowEndLabel() throws Exception;

	/**
	 * 功能：生成流程图开始活动标签
	 * @param x x坐标
	 * @param x y坐标
	 * @param ico 图标
	 * @return returnValue 返回标签串
	 */
	public String getFlowStartActivityLabel(String x, String y, String ico) throws Exception;

	/**
	 * 功能：生成流程图结束活动标签
	 * @param x x坐标
	 * @param x y坐标
	 * @param ico 图标
	 * @return returnValue 返回标签串
	 */
	public String getFlowEndActivityLabel(String x, String y, String ico) throws Exception;

	/**
	 * 功能：生成流程图人工活动标签
	 * @param x x坐标
	 * @param x y坐标
	 * @param id 活动id
	 * @param name 活动名称
	 * @param desc 活动关联提示
	 * @param ico 图标
	 * @return returnValue 返回标签串
	 */
	public String getFlowHumanActivityLabel(String x, String y, String id, String name, String desc, String ico) throws Exception;

	/**
	 * 功能：生成流程图自动活动标签
	 * @param x x坐标
	 * @param x y坐标
	 * @param id 活动id
	 * @param name 活动名称
	 * @param desc 活动关联提示
	 * @param ico 图标
	 * @return returnValue 返回标签串
	 */
	public String getFlowTimerActivityLabel(String x, String y, String id, String name, String desc, String ico) throws Exception;

	/**
	 * 功能：生成流程图子流程标签
	 * @param x x坐标
	 * @param x y坐标
	 * @param id 子流程id
	 * @param name 子流程名称
	 * @param desc 子流程提示
	 * @param ico 图标
	 * @return returnValue 返回标签串
	 */
	public String getFlowSubFlowLabel(String x, String y, String id, String name, String desc, String ico) throws Exception;

	/**
	 * 功能：生成活动间的连接线标签
	 * @param startID 开始结点的ID
	 * @param endID 结束结点的ID
	 * @param id 连接线id
	 * @return returnValue 返回标签串
	 */
	public String getFlowLineLabel(String startID, String endID, String id) throws Exception;

	/**
	 * 功能：生成通用包标签
	 * @param x x坐标
	 * @param x y坐标
	 * @param id 通用包id
	 * @param name 名称
	 * @param desc 关联提示
	 * @param ico 图标
	 * @return returnValue 返回标签串
	 */
	public String getFlowTPackageLabel(String x, String y, String id, String name, String desc, String ico) throws Exception;

	/**
	 * 功能：得到某个流程下的所有活动列表
	 * @param flowID 流程ID
	 * @return returnValue 返回活动列表
	 */
	public FLOW_CONFIG_ACTIVITY[] getActivityList(String flowID) throws Exception;

	/**
	 * 功能：得到某个活动的关联活动列表
	 * @param flowID 流程ID
	 * @param ActivityID 活动ID
	 * @return returnValue 返回关系列表
	 */
	public FLOW_CONFIG_ACTIVITY_CONNE[] getActivityConneList(String flowID, String ActivityID) throws Exception;

	/**
	 * 功能：得到某个活动的关联流程列表
	 * @param flowID 流程ID
	 * @param ActivityID 活动ID
	 * @return returnValue 返回关系列表
	 */
	public FLOW_CONFIG_PROCESS_CONNECTION[] getProcessConneList(String flowID, String ActivityID) throws Exception;

	/**
	 * 功能：保存流程的配置
	 * @param FlowID 流程ID
	 * @param FlowConfigCss 流程配置的字符串
	 * @return 是否保存成功
	 */
	public boolean saveFlowConfig(String FlowID, String FlowConfigCss) throws Exception;

	/**
	 * 功能：删除某个活动的关联活动
	 * @param FlowID 流程ID
	 * @param ActivityID 活动ID
	 * @return 是否删除成功
	 */
	public boolean deleteActivityConne(String FlowID, String ActivityID) throws Exception;

	/**
	 * 功能：删除某个关联线
	 */
	public boolean deleteConnID(String cid, String eid) throws Exception;

	/**
	 * 功能：删除某个活动的关联流程
	 * @param FlowID 流程ID
	 * @param ActivityID 活动ID
	 * @return 是否删除成功
	 */
	public boolean deleteProcessConne(String FlowID, String ActivityID) throws Exception;

	/**
	 * 功能：得到某个流程的开始活动的ID
	 * @param FlowID 流程ID
	 * @return 返回开始活动ID
	 */
	public String getFlowStartActivityID(String FlowID) throws Exception;

	/**
	 * 功能：得到某个流程的结束活动的ID
	 * @param FlowID 流程ID
	 * @return 返回结束活动ID
	 */
	public String getFlowEndActivityID(String FlowID) throws Exception;

	/**
	 * 功能：更新某个活动的关系及坐标等
	 * @param FlowID 流程ID
	 * @param ActivityID 活动ID
	 * @param strCss 关系及坐标等
	 * @return 是否更新成功
	 */
	public boolean updateActivityXycss(String FlowID, String ActivityID, String strCss) throws Exception;

	/**
	 * 功能或作用：取出最大的记录流水号
	 * @param TableName 数据库表名
	 * @param FieldName 数据库字段名称
	 * @param FieldLen 数据库字段长度
	 * @Return MaxNo 执行后返回一个MaxNo字符串
	 */
	public String getMaxFieldNo(String TableName, String FieldName, int FieldLen) throws Exception;

	/**
	 * 功能：检测某流程是否有指定的活动,如果没有新增活动(用于开始活动、结束活动、子流程活动)
	 * @param FlowID 流程ID
	 * @param type 活动类型
	 * @param SubFlowNo 子流程编号(子流程活动时有值)
	 * @param SubFlowName 子流程名称(子流程活动时有值)
	 * @Return 返回插入的活动ID
	 */
	public String selectFlowActivity(String FlowID, String type, String SubFlowNo, String SubFlowName) throws Exception;

	/**
	 * 功能：根据流程编号和子流程编号得到某流程的某个活动(子流程类)的ID
	 * @param FlowID 流程ID
	 * @param SubFlowID 子流程ID
	 * @return 返回活动的排序ID
	 */
	public String getSubFlowActivityID(String FlowID, String SubFlowID) throws Exception;

	//----------------------------------流程运行控制部分-----------------------------//
	/**
	 * 功能：根据流程流转表的ID得到创建日期
	 * @param strID流程流转表的ID
	 * @return returnValue 返回创建日期
	 */
	public String getFlowRunCDate(String strID) throws Exception;

	/**
	 * 功能或作用：获取当前服务器日期（字符型）
	 * @return 当前日期（字符型）
	 */
	public String getDateAsStr() throws Exception;

	/**
	 * 功能：根据流程标识和流程流转的创建日期得到流程ID
	 * @param strNO流程标识
	 * @param strDate流程流转的创建日期
	 * @param unitid 单位编号
	 * @return returnValue 返回流程ID
	 */
	public String getFlowID(String strNO) throws Exception;

	/**
	 * 功能：根据流程ID得到当前流程的第一个活动的ID
	 * @param strID流程ID
	 * @return returnValue 返回第一个活动的ID
	 */
	public String getFirstActivityID(String strID) throws Exception;

	/**
	 * 功能：根据流程ID得到当前流程的最后一个活动的ID
	 * @param strID流程ID
	 * @return returnValue 返回最后一个活动的ID
	 */
	public String getLastlyActivityID(String strID) throws Exception;

	/**
	 * 功能：根据活动ID得到当前活动的拥有的按钮
	 * @param strExecute_No 流程流转编号
	 * @param strID 活动ID
	 * @param strtype 按钮布局 1上 2下
	 * @param strbuttontype 按钮类型 1 按钮形式 2图标形式
	 * @return returnValue 返回拥有的按钮HTML
	 */
	public String getActivityButton(String strExecute_No, String strID, String strtype, 
			String strbuttontype, SessionUser user) throws Exception;

	/**
	 * 功能:判断当前用户是否是当前流转流程中的待处理人
	 * @param strExecute_No 当前流程流转编号
	 * @return returnValue 返回真假
	 */
	public boolean getIsFlowDoList(String strExecute_No, SessionUser user) throws Exception;

	/**
	 * 功能：得到公共按钮
	 * @param strtype 按钮布局 1上 2下
	 * @param strbuttontype 按钮类型 1 按钮形式 2图标形式
	 * @return returnValue 返回拥有的按钮HTML
	 */
	public String getPublicButton(String strtype, String strbuttontype) throws Exception;

	/**
	 * 功能：得到流程创建人按钮
	 * @param strExecute_No 流程流转ID
	 * @param unitid 单位ID
	 * @param strUserID 用户ID
	 * @param strtype 按钮布局 1上 2下
	 * @param strbuttontype 按钮类型 1 按钮形式 2图标形式
	 * @return returnValue 返回按钮HTML
	 */
	public String getCreateButton(String strExecute_No, String strUserID, 
			String strtype, String strbuttontype) throws Exception;

	/**
	 * 功能：得到指定流程的已经分配过的公共类型按钮
	 * @param str_FlowID 流程ID
	 * @param unitid 单位ID
	 * @param strtype 按钮布局 1上 2下
	 * @param strbuttontype 按钮类型 1 按钮形式 2图标形式
	 * @return returnValue 返回拥有的按钮HTML
	 */
	public String getDPublicButton(String str_FlowID, String strtype, String strbuttontype, 
			ActFlowRun ActFlowRun) throws Exception;

	/**
	 * 功能：根据活动ID得到下一分支活动的ID
	 * @param str_ActivityID 当前活动的ID
	 * @return returnValue 返回分支活动的ID字符串
	 */
	public String getNextActivityID(String str_ActivityID, ActFlowRun ActFlowRun, SessionUser user) throws Exception;

	/**
	 * 功能：根据活动ID得到上一分支活动的ID
	 * @param str_ActivityID 当前活动的ID
	 * @return returnValue 返回分支活动的ID字符串
	 */
	public String getUpSelectActivityID(String str_ActivityID, ActFlowRun ActFlowRun, 
			SessionUser user) throws Exception;

	/**
	 * 功能：根据活动ID得到下一分支流程的流程标识字符串
	 * @param str_ActivityID 当前活动的ID[多个活动ID时,号分隔]
	 * @return returnValue 返回分支流程的流程标识字符串
	 */
	public String getNextIFlowID(String str_ActivityID, ActFlowRun ActFlowRun, SessionUser user) throws Exception;

	/**
	 * 功能：根据流程分支编号字符串生成编号加接收类型串
	 * @param strNode_No_S 当前活动编号
	 * @param strNode_No 流程分支编号字符串
	 * @return returnValue 返回加接收类型串
	 */
	public String getNextActivityTypeList(String strNode_No_S, String strNode_No) throws Exception;

	/**
	 * 功能：根据条件生成用户的意见选择列表
	 * @param strCurrUserNo 当前用户编号
	 * @return returnValue 返回意见选择列表
	 */
	public String getUserIdeaList(String strCurrUserNo) throws Exception;

	/**
	 * 功能：得到活动已经上传的附件数
	 * @param strFLOWID流程标识
	 * @param strFORMID表单ID值
	 * @param strID活动ID
	 * @return returnValue 返回附件数
	 */
	public int getActivityFAttNum(String strFLOWID, String strFORMID, String strID) throws Exception;

	/**
	 * 功能：得到某一活动的办理人字符串
	 * 
	 * @param strExecuteNo流程流转表ID
	 * @param strCNodeNo当前活动的ID
	 * @param strNode_No选择活动的ID
	 * @return strUserlist 返回可处理的人员编号字符串
	 */
	public String getActivityDoUserList(String strExecuteNo, String strCNodeNo, String strNodeNo, 
			ActFlowRun ActFlowRun, SessionUser user) throws Exception;

	/**
	 * 功能：根据单位编号生成单位下的用户列表
	 * @param strUnitID 单位编号
	 * @param strListName 列表名称
	 * @param ico 图标
	 * @param strtype 类型
	 * @return returnValue 返回生成的用户列表
	 */
	public String getShowUnitUserList(String strUnitID, String strListName, 
			String ico, String strtype) throws Exception;

	/**
	 * 功能：根据单位编号生成单位下的用户列表(不包括本人)
	 * @param strUnitID 单位编号
	 * @param strUSERID 用户ID
	 * @param strListName 列表名称
	 * @param strUserimage 用户图片文件夹
	 * @param ico 图标
	 * @param strtype 类型
	 * @return returnValue 返回生成的用户列表
	 */
	public String getCUnitUserList(String strUnitID, String strUSERID, 
			String strListName, String ico, String strtype) throws Exception;

	/**
	 * 功能：根据角色编号串生成用户列表
	 * @param strRoles 单位编号
	 * @param strListName 列表名称
	 * @param strUserimage 用户图片文件夹
	 * @param ico 图标
	 * @param strtype 类型
	 * @return returnValue 返回生成的用户列表
	 */
	public String getShowRolesUserList(String strRoles, String strListName, String ico, String strtype,
			String strUnitid, String sunitid, String isdept) throws Exception;

	/**
	 * 功能：根据流程标识得到当前单位基础数据类型流程的活动ID
	 * @param strID 流程标识
	 * @param strUnit 单位编号
	 * @return returnValue 返回活动ID
	 */
	public String getDataAid(String strID) throws Exception;

	/**
	 * 功能：根据流程标识生成流程链接的HTML
	 * @param strValue 流程标识字符串
	 * @param strPath 链接页面路径
	 * @param strIco 图标
	 * @param strUserImage 用户图片路径
	 * @return returnValue 返回流程链接的HTML
	 */
	public String getFlowRunIDHTML(String strValue, String strPath, String strIco, 
			ActFlowRun ActFlowRun, SessionUser user) throws Exception;

	/**
	 * 功能：根据关联的流程标识得到必须新建和还未新建的流程名称串
	 * @param strValue 流程标识字符串
	 * @param AID 当前步骤ID
	 * @return returnValue 返回名称串
	 */
	public String getNeedFlowList(String strValue, String AID, ActFlowRun ActFlowRun) throws Exception;

	/**
	 * 功能：流程提交处理服务
	 * @return returnValue 返回是否执行成功
	 */
	public boolean flowSubmitServer(ActFlowRun ActFlowRun, SessionUser user) throws Exception;

	/**
	 * 功能：流程保存处理服务
	 * @return returnValue 返回是否执行成功
	 */
	public String flowSaveServer(ActFlowRun ActFlowRun) throws Exception;

	/**
	 * 功能：执行送部门承办人服务
	 * @return returnValue 返回是否执行成功
	 */
	public boolean flowSubDeptServer(ActFlowRun ActFlowRun, SessionUser user) throws Exception;

	/**
	 * 功能：执行分送已阅服务
	 * @return returnValue 返回是否执行成功
	 */
	public boolean flowSendFinishServer(ActFlowRun ActFlowRun) throws Exception;

	/**
	 * 功能：流程分送服务（不影响当前流程的执行，用于查看）
	 * @param ExecuteNo 流程流转id
	 * @param sid 收到分送的文件后用什么活动来显示
	 * @param cid 当前活动id
	 * @param TitleName 流程标题
	 * @param users 分送人员的编号串
	 * @param userid 当前用户id
	 * @param username 当前用户名称
	 * @return returnValue 返回是否执行成功
	 */
	public boolean dosend(String ExecuteNo, String sid, String cid, String TitleName, 
			String users, String userid, String username) throws Exception;

	/**
	 * 功能：流程完成处理服务
	 * @return returnValue 返回是否执行成功
	 */
	public boolean flowFinishServer(ActFlowRun ActFlowRun, SessionUser user) throws Exception;

	/**
	 * 功能：流程移交处理服务
	 * @return returnValue 返回是否执行成功
	 */
	public boolean flowDevolveServer(ActFlowRun ActFlowRun, SessionUser user) throws Exception;

	/**
	 * 功能：流程初始化处理服务
	 * @return returnValue 返回是否执行成功
	 */
	public boolean flowInitServer(ActFlowRun ActFlowRun, SessionUser user) throws Exception;

	/**
	 * 功能：流程收回服务
	 * @return returnValue 返回是否执行成功
	 */
	public boolean flowTakeBackServer(ActFlowRun ActFlowRun, SessionUser user) throws Exception;

	/**
	 * 功能：流程删除处理服务
	 * @return returnValue 返回是否执行成功
	 */
	public boolean flowDeleteServer(ActFlowRun ActFlowRun, SessionUser user) throws Exception;

	/**
	 * 功能：流程处理返回服务
	 * @return returnValue 返回是否执行成功
	 */
	public boolean flowReturnServer(ActFlowRun ActFlowRun, SessionUser user) throws Exception;

	/**
	 * 功能：发送提示消息
	 * @param strUserNos 待处理人编号字符串
	 * @param strMessage 提示的消息内容
	 * @return returnValue 返回处理后的结果
	 */
	public String sendFlowMessage(String strUserNos, String strMessage) throws Exception;

	/**
	 * 功能：根据包ID得到包下的第一个包,用于很多关联流程形成流程总台时。
	 * @param strPackageID 包ID
	 * @return returnValue 返回结果
	 */
	public String getPackageFirst(String strPackageID) throws Exception;

	/**
	 * 功能：根据包ID得到包名。
	 * @param strPackageID 包ID
	 * @return returnValue 返回结果
	 */
	public String getPackageName(String strPackageID) throws Exception;

	/**
	 * 功能：根据当前步骤和选择步骤得到附件提示的情况。
	 * @param CAID 当前活动
	 * @param SAID 选择活动
	 * @return returnValue 返回结果
	 */
	public String getISAttMessage(String CAID, String SAID) throws Exception;

	/**
	 * 功能:得到指定单位下的部门内勤用户编号串
	 * @param strUnitID 单位编号
	 * @return 部门内勤用户编号串
	 */
	public String getDeptInside(String strUnitID) throws Exception;

	/**
	 * 功能:得到指定单位下的用户编号串
	 * @param strUnitID 单位编号
	 * @return 用户编号串
	 */
	public String getDeptUsers(String strUnitID) throws Exception;

	/**
	 * 功能:得到指定单位下的部门内勤用户名称串
	 * @param strUnitID 单位编号
	 * @return 部门内勤用户名称串
	 */
	public String getDeptInsideName(String strUnitID) throws Exception;

	/**
	 * 功能:得到指定单位下的用户名称串
	 * @param strUnitID 单位编号
	 * @return 用户名称串
	 */
	public String getDeptUserNames(String strUnitID) throws Exception;

	/**
	 * 得到是否显示收回按钮(1是0否)
	 * @param strExecuteNo 流程运转ID
	 * @param strUserID 流程运转ID
	 * @return
	 */
	public String getIsTakeBack(String strExecuteNo, String strUserID) throws Exception;

	/**
	 * 功能：根据流程流转表的ID得到流程运转表的实体
	 * @param strID流程流转表的ID
	 * @return
	 */
	public FLOW_RUNTIME_PROCESS getFlowRuntimeprocess(String strID) throws Exception;

	/**
	 * 功能：根据活动ID得到活动实体
	 * @param strID活动ID
	 * @return
	 */
	public FLOW_CONFIG_ACTIVITY getFlowActivity(String strID) throws Exception;

	/**
	 * 功能：根据流程ID得到流程实体
	 * @param strID流程ID
	 * @return
	 */
	public FLOW_CONFIG_PROCESS getFlowProcess(String strID) throws Exception;

	/**
	 * 功能：根据流程包ID得到流程包实体
	 * @param strID 流程包ID
	 * @return
	 */
	public Package getFlowPackage(String strID) throws Exception;

	/**
	 * 功能：根据流程包ID得到下级流程包实体列表
	 * @param strID 流程包ID
	 * @return
	 */
	public Package[] getFlowPackageList(String strID) throws Exception;

	/**
	 * 功能：判断某个包下是否有子包
	 * @param strID 流程包ID
	 * @return
	 */
	public boolean getIsSubPackage(String strID) throws Exception;

	/**
	 * 功能：增加流程管理日志 strExecuteNo 流程运转ID user 用户session
	 */
	public void addFlowManageNote(String strExecuteNo, SessionUser user) throws Exception;

	/**
	 * 功能：得到流程的标题
	 * @param 流程运转ID
	 * @return 返回标题
	 */
	public String getTitle(String ID) throws Exception;

	/**
	 * 功能：获取文档唯一ID编号
	 * @param userID 用户编号
	 * @return 返回字符串
	 */
	public String globalID(String userID) throws Exception;

	/**
	 * 功能：根据活动ID得到当前活动的拥有的按钮
	 * @param strExecute_No 流程流转编号
	 * @param strID 活动ID
	 * @param strtype 按钮布局 1上 2下
	 * @param strM_Node_No_E 下一步的分支活动
	 * @param strS_Node_No_E 上一步的分支活动
	 * @return returnValue 返回拥有的按钮HTML
	 */
	public String getActivityButton(String strExecute_No, String strID, String strtype, ActFlowRun ActFlowRun,
			SessionUser user, String strM_Node_No_E, String strS_Node_No_E) throws Exception;

	/**
	 * 根据流程编号得到流程的名称
	 * @param strID
	 * @return String 返回对应的流程名称
	 */
	public String getActButton(String strID) throws Exception;

	/**
	 * 功能：得到是否执行确认处理(多人同时处理提交或退回时用到)
	 * @Execute_No 流转ID,CID步骤ID
	 * @return returnValue 返回是否
	 */
	public boolean getIsShow(String Execute_No, String CID) throws Exception;

	/**
	 * 根据采集配置ID得到相关的表名串
	 * @param strID 采集配置ID
	 * @return String 返回表名串
	 */
	public String getDocTables(String strID) throws Exception;

	/**
	 * 功能：根据流程ID得到当前流程的所有活动图示
	 * @param strID 流程ID
	 * @param strAID 当前活动ID
	 * @param strPath 文件路径
	 * @return returnValue 返回活动图示HTML
	 */
	public String getFlowActivityLink(String strID, String strAID, String strPath) throws Exception;

	/**
	 * 功能：根据流转编号生成流程跟踪记录
	 * @param Execute_No 流程流转编号
	 * @return returnValue 返回跟踪记录
	 */
	public String getFlowDoLog(String Execute_No) throws Exception;

	/**
	 * 判断与当前步骤相关联的线上是否有表字段值作为条件的情况
	 * @param CID
	 * @return
	 * @throws Exception
	 */
	public String getISwhere(String CID) throws Exception;
}
