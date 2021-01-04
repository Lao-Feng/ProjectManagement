package com.yonglilian.service.impl;

import com.yonglilian.common.util.ReflectionUtil;
import com.yonglilian.common.util.SysPreperty;
import com.yonglilian.dao.UserMapper;
import com.yonglilian.flowengine.mode.config.FLOW_CONFIG_ACTIVITY;
import com.yonglilian.service.FlowManageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zr.zrpower.common.db.DBEngine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FlowManageServiceImpl implements FlowManageService {
	/** The FlowManageServiceImpl Logger. */
	private static final Logger LOGGER = LoggerFactory.getLogger(FlowManageServiceImpl.class);
	private DBEngine dbEngine;
	static private int clients = 0;
	/** 用户操作数据层. */
	@Autowired
	private UserMapper userMapper;

	/**
	 * 构造方法
	 */
	public FlowManageServiceImpl() {
		dbEngine = new DBEngine(SysPreperty.getProperty().MainDataSource, 
				SysPreperty.getProperty().IsConvert);
		dbEngine.initialize();
		if (clients < 1) {
		}
		clients = 1;
	}

	/**
	 * 功能：根据活动ID得到活动实体
	 * @param strID活动ID
	 * @return
	 * @throws Exception 
	 */
	private FLOW_CONFIG_ACTIVITY getFlowActivity(String strID) throws Exception {
		FLOW_CONFIG_ACTIVITY activity = null;
		StringBuffer addBuf = new StringBuffer();
		addBuf.append("Select * From FLOW_CONFIG_ACTIVITY where ID='" + strID + "'");
		Map<String, Object> retmap = userMapper.selectMapExecSQL(addBuf.toString());
		if (retmap != null && retmap.size() > 0) {
			activity = (FLOW_CONFIG_ACTIVITY) ReflectionUtil.convertMapToBean(retmap, FLOW_CONFIG_ACTIVITY.class);
		}
		return activity;
	}

	@SuppressWarnings("unused")
	@Override
	public String getNextActivityList(String strNode_No_S, String strNode_No, String strName, 
			String strIco) throws Exception {
		try {
			String strchecked = "";
			String strInType = "";
			String strNode_S_No = "", strNode_S_Name = "";
			int i = 0;
			List<String> arrList_strNode_No = new ArrayList<String>();
			FLOW_CONFIG_ACTIVITY activity = null;
			StringBuffer addBuf = new StringBuffer();
			StringBuffer ReBuf = new StringBuffer();
			
			Map<String, Object> flowActivityMap = getFlowActivityHashtable(strNode_No);
			Map<String, Object> activityTypeMap = new HashMap<String, Object>();
			
			addBuf.append("Select CID,TYPE From FLOW_CONFIG_ACTIVITY_CONNE where (SID='" + strNode_No_S)
				  .append("' or EID='" + strNode_No_S + "') and CID in (" + strNode_No).append(")");
			List<Map<String, Object>> retlist = userMapper.selectListMapExecSQL(addBuf.toString());
			int length = retlist != null ? retlist.size() : 0;
			if (length > 0) {
				for (i = 0; i < length; i++) {
					Map<String, Object> retmap = retlist.get(i);
					activityTypeMap.put(retmap.get("CID").toString(), retmap.get("TYPE").toString());
				}
			}
			arrList_strNode_No = getArrayList(strNode_No, ",");
			for (i = 0; i < arrList_strNode_No.size(); i++) {
				if (i == 0) {
					strchecked = "checked";
				} else {
					strchecked = "";
				}
				strNode_S_No = arrList_strNode_No.get(i).toString();
				activity = (FLOW_CONFIG_ACTIVITY) flowActivityMap.get(strNode_S_No);
				if (activity != null) {
					strNode_S_Name = activity.getNAME();
				}
				// 得到接收类型
				strInType = (String) activityTypeMap.get(strNode_S_No);
				if (strInType == null) {
					strInType = "0";
				}
				// ----------//
				ReBuf.append("<el-radio v-model=\"radio1\" label=\""+strNode_S_No + "/" + strInType+"\" border @change=\"selectOnclick\">"+strNode_S_Name+"</el-radio>\r\n");
//				ReBuf.append("<table border='0' cellspacing='0' cellpadding='0'><tr><td width='5'></td><td style='cursor:pointer' onclick=\"selectOnclick('")
//					 .append( + "');\"><input type='radio' " + strchecked
//						  + " onclick=\"selectOnclick('")
//					 .append(strNode_S_No + "/" + strInType + "');\" name='" + strName + "' value='")
//					 .append(strNode_S_No + "/" + strInType + "'>" + "<img src='ZrWorkFlow/images/" + strIco
//						  + "' border='0' align='absmiddle'>" + strNode_S_Name)
//					 .append("</td></tr></table>");
			}
			return ReBuf.toString();
		} catch (Exception ex) {
			LOGGER.info("流程引擎---GetNextActivityList函数出错：" + ex.toString());
			System.out.print("流程引擎---GetNextActivityList函数出错：" + ex.toString());
			return "";
		}
	}

	@Override
	public String getShowUserList(String strUserLst, String strListName, String ico, 
			String strtype, String strAid, String SELECTDEPTID) throws Exception {
		try {
			String strNo = "";
			String strName = "";
			int k = 0;
			int intWorkNum = 0;
			String strWorkNum = "";
			String strMsg = "";
			String strTmp = "";
			String strcheck = "";
			String strchecked = "";
			StringBuffer addBuf = new StringBuffer();
			StringBuffer tmpBuf = new StringBuffer();
			String strListName1 = "";
			
			if (strtype.equals("1")) {
				strcheck = "radio";
			} else {
				strcheck = "checkbox";
			}
			// 得到是否显示待处理人的工作量
			String isShow = "0";
			FLOW_CONFIG_ACTIVITY activity = null;
			activity = getFlowActivity(strAid);
			String strMsgType = "";
			if (activity != null) {
				strMsgType = activity.getISNOTE();
			}
			// 短信提示为:自定义(默认是)
			if (strMsgType.equals("2")) {
				strMsg = "<input type='radio' name='msgtype1' id='2' value='2' checked>是<input type='radio' name='msgtype1' id='3' value='3'>否";
			}
			// 短信提示为:自定义(默认否)
			else if (strMsgType.equals("3")) {
				strMsg = "<input type='radio' name='msgtype1' id='2' value='2'>是<input type='radio' name='msgtype1' id='3' value='3' checked>否";
			}
			// 短信提示为:自定义短信提示内容
			else if (strMsgType.equals("4")) {
				strMsg = "<input type='text' id='msgtype1' name='msgtype1' size='42' value=''>";
			} else {
			}
			addBuf.delete(0, addBuf.length());// 清空
			addBuf.append("<table cellSpacing='0' cellPadding='0' border='0' width='100%'>\r\n");
			if (isShow.equals("1")) {
				addBuf.append("<tr><td colspan='3'><font class='WriteSign'>提示：为加快审批，请选择工作量少的人员[状态条表示待批工作量]。</font></td></tr>");
			}
			if (strMsg.length() > 0) {
				addBuf.append("<tr><td colspan='3'>短信提示：" + strMsg + "</td></tr>");
			}
			List<String> arrList_strNode_No1 = new ArrayList<String>();
			List<String> arrList_Work = new ArrayList<String>();
			
			List<String> arrList_strNode_No = getArrayList(strUserLst, ",");
			int[] worknum = null;
			worknum = new int[arrList_strNode_No.size()];
			// 按待办工作量重新排序人员顺序
			if (isShow.equals("1")) {
				// 得到相关人员的已有待办工作量
				Map<String, Object> workNumMap = getUserWorkNumMap(strUserLst);
				for (int y = 0; y < arrList_strNode_No.size(); y++) {
					strNo = arrList_strNode_No.get(y).toString();
					strTmp = (String) workNumMap.get(strNo);
					if (strTmp != null) {
						intWorkNum = Integer.parseInt(strTmp);
					} else {
						intWorkNum = 0;
					}
					worknum[y] = intWorkNum;
				}
				// 处理排序---------
				for (int x = 0; x < 30; x++) {
					for (int y = 0; y < arrList_strNode_No.size(); y++) {
						strNo = arrList_strNode_No.get(y).toString();
						intWorkNum = worknum[y];
						if (x == intWorkNum) {
							arrList_strNode_No1.add(strNo);
							arrList_Work.add(String.valueOf(intWorkNum));
						}
					}
				}
				for (int y = 0; y < arrList_strNode_No.size(); y++) {
					strNo = arrList_strNode_No.get(y).toString();
					intWorkNum = worknum[y];
					if (intWorkNum >= 30) {
						arrList_strNode_No1.add(strNo);
						arrList_Work.add(String.valueOf(intWorkNum));
					}
				}
				// ---------------------
			} else {
				arrList_strNode_No1 = arrList_strNode_No;
			}
			// -------------------------
			StringBuffer sqlBuf = new StringBuffer();
			String strYdeptName = "";
			String strDeptName = "";
			String strDeptID = "";
			if (SELECTDEPTID.length() > 0) {
				if (SELECTDEPTID.length() == 12 || SELECTDEPTID.split(",")[0].length() == 12) {
					sqlBuf.append("select a.USERID,a.NAME,a.UNITID,b.UNITNAME "
							+ "FROM BPIP_USER a left join BPIP_UNIT b on a.UNITID=b.UNITID "
							+ "where a.USERSTATE='0' and a.USERID in (" 
							+ strUserLst + ") and a.UNITID in (" + SELECTDEPTID
							+ ") order by b.ORDERCODE,a.UNITID,a.ORBERCODE");
				} else {
					sqlBuf.append("select a.USERID,a.NAME,a.UNITID,b.UNITNAME "
							+ "FROM BPIP_USER a left join BPIP_UNIT b on a.UNITID=b.UNITID "
							+ "where a.USERSTATE='0' and a.USERID in ("
							+ SELECTDEPTID + ") order by b.ORDERCODE,a.UNITID,a.ORBERCODE");
				}
			} else {
				sqlBuf.append("select a.USERID,a.NAME,a.UNITID,b.UNITNAME "
						+ "FROM BPIP_USER a left join BPIP_UNIT b on a.UNITID=b.UNITID "
						+ "where a.USERSTATE='0' and a.USERID in ("
						+ strUserLst + ")  order by b.ORDERCODE,a.UNITID,a.ORBERCODE");
			}
			List<Map<String, Object>> retlist = userMapper.selectListMapExecSQL(sqlBuf.toString());
			int length = retlist != null ? retlist.size() : 0;
			if (length > 0) {
				for (int i = 0; i < length; i++) {
					Map<String, Object> retmap = retlist.get(i);
					strNo = retmap.get("USERID").toString();
					strName = retmap.get("NAME").toString();
					strDeptName = retmap.get("UNITNAME").toString();
					strDeptID = retmap.get("UNITID").toString();
					if (length == 1) {
						strchecked = "checked";
					} else {
						strchecked = "";
					}
					if (isShow.equals("1")) {
						intWorkNum = Integer.parseInt(arrList_Work.get(i).toString());
						if (intWorkNum < 20) {
							strWorkNum = String.valueOf(intWorkNum);
						} else {
							strWorkNum = "20";
						}
						if (intWorkNum > 0) {
							tmpBuf.delete(0, tmpBuf.length());// 清空
							tmpBuf.append(strName + "<img src='" + SysPreperty.getProperty().AppUrl
										+ "Zrsysmanage/images/blueimg/")
								  .append("icoBar.gif' border='0' width='" + strWorkNum
										+ "' height='12' align='absmiddle' alt='")
								  .append(arrList_Work.get(i).toString() + "'>");
							strName = tmpBuf.toString();
						}
					}
					if (!strYdeptName.equals(strDeptName)) {
						if (k == 1) {
							addBuf.append("<td></td><td></td></tr>\r\n");
						}
						if (k == 2) {
							addBuf.append("<td></td></tr>\r\n");
						}
						if (strcheck.equals("checkbox")) {
							addBuf.append("<tr><td colspan='3'><input type='checkbox' onclick=DeptOnclick('" + strDeptID
										+ "'); name='" + strDeptID + "' id='" + strDeptID + "' value='" + strDeptID + "'>")
								  .append(strDeptName).append("</td></tr>\r\n");
						} else {
							addBuf.append("<tr><td colspan='3'>" + strDeptName + "</td></tr>\r\n");
						}
						k = 0;
					}
					if (strNo.length() > 0) {
						k = k + 1;
						if (!strtype.equals("1")) {
							strListName1 = "";
							tmpBuf.delete(0, tmpBuf.length());// 清空
							tmpBuf.append(strListName + strNo);
							strListName1 = strDeptID + tmpBuf.toString();
						} else {
							// strListName1 = strListName+strNo;
							strListName1 = strListName;
						}
						if (k == 4) {
							k = 1;
						}
						if (k == 1) {
							addBuf.append("<tr>\r\n");
						}
						addBuf.append("<td align='left' height='25' style='cursor:pointer ' onclick=\"selectOnclick('" + strNo)
							  .append("');\"><input type='" + strcheck + "' " + strchecked + " onclick=\"selectOnclick('")
							  .append(strNo + "');\" name='" + strListName1 + "' id='" + strNo + "' value='" + strNo + "'>");
						
						addBuf.append("<img src='" + SysPreperty.getProperty().AppUrl + "Zrsysmanage/images/blueimg/")
							  .append(ico + "' border='0' align='absmiddle'>");
						addBuf.append(strName + "</td>");
						if (k == 3) {
							addBuf.append("</tr>\r\n");
						}
					}
					strYdeptName = strDeptName;
				}
			}
			if (k == 1) {
				addBuf.append("<td></td><td></td></tr></table>\r\n");
			}
			if (k == 2) {
				addBuf.append("<td></td></tr></table>\r\n");
			}
			if (k == 3) {
				addBuf.append("</table>\r\n");
			}
			return addBuf.toString();
		} catch (Exception ex) {
			LOGGER.error("流程引擎---GetShowUserList函数出错：" + ex.toString());
			System.out.print("流程引擎---GetShowUserList函数出错：" + ex.toString());
			return "";
		}
	}

	@Override
	public String getRoleUserList(String strListName, String ico, String strtype, 
			String strAid, String roles) throws Exception {
		try {
			String strNo = "";
			String strName = "";
			int k = 0;
			int intWorkNum = 0;
			String strWorkNum = "";
			String strMsg = "";
			String strcheck = "";
			String strchecked = "";
			StringBuffer addBuf = new StringBuffer();
			StringBuffer tmpBuf = new StringBuffer();
			String strListName1 = "";
			
			if (strtype.equals("1")) {
				strcheck = "radio";
			} else {
				strcheck = "checkbox";
			}
			// 得到是否显示待处理人的工作量
			String isShow = "0";
			FLOW_CONFIG_ACTIVITY activity = null;
			activity = getFlowActivity(strAid);
			String strMsgType = "";
			if (activity != null) {
				strMsgType = activity.getISNOTE();
			}
			// 短信提示为:自定义(默认是)
			if (strMsgType.equals("2")) {
				strMsg = "<input type='radio' name='msgtype1' id='2' value='2' checked>是<input type='radio' name='msgtype1' id='3' value='3'>否";
			}
			// 短信提示为:自定义(默认否)
			else if (strMsgType.equals("3")) {
				strMsg = "<input type='radio' name='msgtype1' id='2' value='2'>是<input type='radio' name='msgtype1' id='3' value='3' checked>否";
			}
			// 短信提示为:自定义短信提示内容
			else if (strMsgType.equals("4")) {
				strMsg = "<input type='text' id='msgtype1' name='msgtype1' size='42' value=''>";
			}
			addBuf.delete(0, addBuf.length());// 清空
			addBuf.append("<table cellSpacing='0' cellPadding='0' border='0' width='100%'>\r\n");

			if (strMsg.length() > 0) {
				addBuf.append("<tr><td colspan='3'>短信提示：").append(strMsg).append("</td></tr>");
			}
			List<Object> arrList_Work = new ArrayList<Object>();
			
			StringBuffer sqlBuf = new StringBuffer();
			String strYdeptName = "";
			String strDeptName = "";
			String strShowDeptName = "";
			String strDeptID = "";
			if (roles.length() > 0) {
				sqlBuf.append("select a.USERID,a.NAME,a.UNITID,b.LONGNAME,c.ROLEID,d.ROLENAME "
							+ "FROM BPIP_USER a left join BPIP_UNIT b on a.UNITID=b.UNITID left join BPIP_USER_ROLE c on a.userid=c.userid left join bpip_role d on c.roleid=d.roleid "
							+ "where c.roleid in (" + roles + ") order by c.roleid,a.ORBERCODE");
			} else {
				sqlBuf.append("select a.USERID,a.NAME,a.UNITID,b.LONGNAME,c.ROLEID,d.ROLENAME "
							+ "FROM BPIP_USER a left join BPIP_UNIT b on a.UNITID=b.UNITID left join BPIP_USER_ROLE c on a.userid=c.userid left join bpip_role d on c.roleid=d.roleid left join flow_config_activity_group e on c.roleid=e.GROUPID "
							+ "where e.activityid='" + strAid + "'  order by c.roleid,a.ORBERCODE");
			}
			List<Map<String, Object>> retlist = userMapper.selectListMapExecSQL(sqlBuf.toString());
			int length = retlist != null ? retlist.size() : 0;
			if (length > 0) {
				for (int i = 0; i < length; i++) {
					Map<String, Object> retmap = retlist.get(i);
					strNo = retmap.get("USERID").toString();
					strName = retmap.get("NAME").toString();
					strDeptName = retmap.get("ROLENAME").toString();
					strDeptID = retmap.get("ROLEID").toString();
					strShowDeptName = retmap.get("UNITNAME").toString();
					
					if (length == 1) {
						strchecked = "checked";
					} else {
						strchecked = "";
					}
					if (isShow.equals("1")) {
						intWorkNum = Integer.parseInt(arrList_Work.get(i).toString());
						if (intWorkNum < 20) {
							strWorkNum = String.valueOf(intWorkNum);
						} else {
							strWorkNum = "20";
						}
						if (intWorkNum > 0) {
							tmpBuf.delete(0, tmpBuf.length());// 清空
							tmpBuf.append(strName).append("<img src='").append(SysPreperty.getProperty().AppUrl)
								  .append("Zrsysmanage/images/blueimg/").append("icoBar.gif' border='0' width='")
								  .append(strWorkNum).append("' height='12' align='absmiddle' alt='")
								  .append(arrList_Work.get(i).toString()).append("'>");
							strName = tmpBuf.toString();
						}
					}
					if (!strYdeptName.equals(strDeptName)) {
						if (k == 1) {
							addBuf.append("<td></td><td></td></tr>\r\n");
						}
						if (k == 2) {
							addBuf.append("<td></td></tr>\r\n");
						}
						if (strcheck.equals("checkbox")) {
							addBuf.append("<tr><td colspan='3'><input type='checkbox' onclick=DeptOnclick('" + strDeptID
										+ "'); name='" + strDeptID + "' id='" + strDeptID + "' value='" + strDeptID + "'>")
								  .append(strDeptName).append("</td></tr>\r\n");
						} else {
							addBuf.append("<tr><td colspan='3'>").append(strDeptName).append("</td></tr>\r\n");
						}
						k = 0;
					}
					if (strNo.length() > 0) {
						k = k + 1;
						if (!strtype.equals("1")) {
							strListName1 = "";
							tmpBuf.delete(0, tmpBuf.length());// 清空
							tmpBuf.append(strListName).append(strNo);
							strListName1 = strDeptID + tmpBuf.toString();
						} else {
							strListName1 = strListName + strNo;
						}
						if (k == 4) {
							k = 1;
						}
						if (k == 1) {
							addBuf.append("<tr>\r\n");
						}
						addBuf.append("<td align='left' height='25' style='cursor:pointer ' onclick=\"selectOnclick('")
							  .append(strNo).append("');\"><input type='").append(strcheck).append("' ")
							  .append(strchecked).append(" onclick=\"selectOnclick('").append(strNo)
							  .append("');\" name='").append(strListName1).append("' id='").append(strNo)
							  .append("' value='").append(strNo).append("'>");
						
						addBuf.append("<img src='").append(SysPreperty.getProperty().AppUrl)
							  .append("Zrsysmanage/images/blueimg/").append(ico)
							  .append("' border='0' align='absmiddle'>");
						
						addBuf.append(strName + "/" + strShowDeptName).append("</td>");
						if (k == 3) {
							addBuf.append("</tr>\r\n");
						}
					}
					strYdeptName = strDeptName;
				}
			}
			if (k == 1) {
				addBuf.append("<td></td><td></td></tr></table>\r\n");
			}
			if (k == 2) {
				addBuf.append("<td></td></tr></table>\r\n");
			}
			if (k == 3) {
				addBuf.append("</table>\r\n");
			}
			LOGGER.info("html:" + addBuf.toString());
			return addBuf.toString();
		} catch (Exception ex) {
			LOGGER.error("流程引擎---GetRoleUserList函数出错：" + ex.toString());
			System.out.print("流程引擎---GetRoleUserList函数出错：" + ex.toString());
			return "";
		}
	}

	/**
	 * 功能：得到活动ID/活动实体对照表
	 * @param strIDs活动ID串或条件
	 * @return 对照表
	 * @throws Exception 
	 */
	private Map<String, Object> getFlowActivityHashtable(String strIDs) throws Exception {
		FLOW_CONFIG_ACTIVITY activity = null;
		StringBuffer addBuf = new StringBuffer();
		Map<String, Object> flowActivityMap = new HashMap<String, Object>();
		addBuf.append("Select * From FLOW_CONFIG_ACTIVITY where ID in(" + strIDs + ")");
		List<Map<String, Object>> retlist = userMapper.selectListMapExecSQL(addBuf.toString());
		int length = retlist != null ? retlist.size() : 0;
		if (length > 0) {
			for (int i = 0; i < length; i++) {
				Map<String, Object> retmap = retlist.get(i);
				activity = (FLOW_CONFIG_ACTIVITY) ReflectionUtil.convertMapToBean(retmap, FLOW_CONFIG_ACTIVITY.class);
				flowActivityMap.put(retmap.get("ID").toString(), activity);
			}
		}
		return flowActivityMap;
	}

	/**
	 * 功能或作用：分析规则字符串，生成数组
	 * @param strItems 字符串
	 * @param strItemMark 标识符
	 * @return 返回数组
	 */
	private List<String> getArrayList(String strItems, String strItemMark) {
		int intItemLen, i = 0, n = 0;
		strItems = strItems + strItemMark;
		String strItem;
		List<String> strList = new ArrayList<String>();
		intItemLen = strItems.length();
		while (i < intItemLen) {
			n = strItems.indexOf(strItemMark, i);
			strItem = strItems.substring(i, n);
			strList.add(strItem);
			i = n + 1;
		}
		return strList;
	}

	/**
	 * 根据用户编号得到用户的待办工作量对照表
	 * @param strUserID 用户编号
	 * @return 对照表
	 * @throws Exception 
	 */
	private Map<String, Object> getUserWorkNumMap(String strUserIDs) throws Exception {
		int intReturn = 0;
		String strCode = "";
		StringBuffer addBuf = new StringBuffer();
		Map<String, Object> workNumMap = new HashMap<String, Object>();
		if (strUserIDs.indexOf(",") > -1) {
			if (strUserIDs.indexOf("'") == -1) {
				strUserIDs = strUserIDs.replaceAll(",", "','");
				addBuf.append("'" + strUserIDs + "'");
				strUserIDs = addBuf.toString();
			}
		} else {
			if (strUserIDs.indexOf("'") == -1) {
				addBuf.append("'" + strUserIDs + "'");
				strUserIDs = addBuf.toString();
			}
		}
		addBuf.delete(0, addBuf.length());// 清空
		addBuf.append("SELECT substr(ACCEPTPSN,1,16) as code,count(ID) as num FROM FLOW_RUNTIME_PROCESS "
					+ "where (STATE='0' or STATE='1') and ACCEPTPSN in (" + strUserIDs + ") group by substr(ACCEPTPSN,1,16)");
		List<Map<String, Object>> retlist = userMapper.selectListMapExecSQL(addBuf.toString());
		int length = retlist != null ? retlist.size() : 0;
		if (length > 0) {
			for (int i = 0; i < length; i++) {
				Map<String, Object> retmap = retlist.get(i);
				strCode = retmap.get("code").toString();
				intReturn = Integer.parseInt(retmap.get("num").toString());
				workNumMap.put(strCode, String.valueOf(intReturn));
			}
		}
		return workNumMap;
	}

}