package zr.zrpower.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zr.zrpower.common.db.DBEngine;
import zr.zrpower.common.db.DBSet;
import zr.zrpower.common.util.*;
import zr.zrpower.dao.UserMapper;
import zr.zrpower.flowengine.mode.monitor.FLOW_CONFIG_ENTRUST;
import zr.zrpower.flowengine.mode.monitor.FLOW_RUNTIME_ACTIVITY;
import zr.zrpower.flowengine.mode.monitor.FLOW_RUNTIME_ENTRUSTLOG;
import zr.zrpower.flowengine.mode.monitor.FLOW_RUNTIME_PROCESS;
import zr.zrpower.model.BPIP_USER;
import zr.zrpower.service.FlowMonitorService;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 流程监控函数库服务层实现
 * @author lwk
 *
 */
@Service
public class FlowMonitorServiceImpl implements FlowMonitorService {
	/** The FlowMonitorServiceImpl Logger. */
	private static final Logger LOGGER = LoggerFactory.getLogger(FlowMonitorServiceImpl.class);
	private DBEngine dbEngine;
	private static int clients = 0;
	private Random RANDOM = new Random();
	/** 数据库类型，1：Oracle，2：MSsql，3：MySQL */
	static String DataBaseType = SysPreperty.getProperty().DataBaseType;
	static String strFlowRoleType = "2";// 流程调用的角色类型
	
	public static String GetProList_pageCount  = "0";
	public static String GetProList2_pageCount  = "0";
	public static String GetProFinshList_pageCount  = "0";
	public static String DailyList_pageCount  = "0";
	public static String GetProList1_pageCount  = "0";
	public static String GetFlowTransactList2_pageCount  = "0";

	/** 用户操作数据层. */
	@Autowired
	private UserMapper userMapper;
//	/** 流程引擎相关接口的DAO层. */
//	@Autowired
//	private FlowDAOMapper flowDAOMapper;

	/**
	 * 构造方法
	 */
	public FlowMonitorServiceImpl() {
		dbEngine = new DBEngine(SysPreperty.getProperty().MainDataSource, 
				SysPreperty.getProperty().IsConvert);
		dbEngine.initialize();
	    if (clients<1) {
	    }
	    clients=1;
	}

	@Override
	public FLOW_RUNTIME_PROCESS[] getProList(int currentPage, int PageSize, String strWhere) throws Exception {
	    FLOW_RUNTIME_PROCESS[] returnValue = null;
	    String strSQL = "";
	    StringBuffer addBuf = new StringBuffer();
	    addBuf.append("Select B.ID,B.NAME,B.CREATEDATE,C.NAME AS CREATEPSN "
	    			+ "From FLOW_RUNTIME_PROCESS B Left Join BPIP_USER C on B.CREATEPSN=C.USERID "
	    			+ "where "+strWhere+" order by B.CREATEDATE desc");
	    strSQL = addBuf.toString();
	    addBuf.delete(0, addBuf.length());//清空 
	    if (currentPage <= 0) {
	    	currentPage = 1;// 初始化页码
	    }
	    if (DataBaseType.equals("1")) {// Oracle数据库
	    	addBuf.append("Select T.* From (SELECT A.*, ROWNUM RN FROM ("+strSQL+") AS A WHERE ROWNUM <= ")
		     	  .append(currentPage*PageSize+") AS T WHERE RN > "+(currentPage-1)*PageSize);
	    }
	    else if (DataBaseType.equals("2")) {// MSSQL数据库 
	    	addBuf.append("Select Top "+PageSize+" T.* From (SELECT ROW_NUMBER() OVER(ORDER BY ID) AS MSROWNUM, A.* FROM ("+strSQL+") AS A) AS T WHERE MSROWNUM > ")
	     	   	  .append((currentPage-1)*PageSize);
	    }
	    else if (DataBaseType.equals("3")) {// MySQL数据库
	    	int offset = (currentPage-1)*PageSize;
	    	addBuf.append("SELECT T.* FROM ("+strSQL+") AS T LIMIT "+offset+","+PageSize);
	    }
	    List<Map<String, Object>> dbsetCount = userMapper.selectListMapExecSQL(strSQL);
	    List<Map<String, Object>> retlist = userMapper.selectListMapExecSQL(addBuf.toString());
	    int length = retlist != null ? retlist.size() : 0;
	    if (length > 0) {
	        returnValue = new FLOW_RUNTIME_PROCESS[length];
	        for (int i = 0; i < length; i++) {
	          Map<String, Object> retmap = retlist.get(i);
	          returnValue[i] = (FLOW_RUNTIME_PROCESS) ReflectionUtil.convertMapToBean(retmap, FLOW_RUNTIME_PROCESS.class);
	        }
	        //记录总条数
	        if (returnValue != null) {
	        	GetProList_pageCount = dbsetCount.size()+"";
	        }
	    }
	    return returnValue;  
	}

	@Override
	public FLOW_RUNTIME_PROCESS[] getProList2(String strUserID, int currentPage, int PageSize, String strwhere)
			throws Exception {
	     FLOW_RUNTIME_PROCESS[] returnValue = null;
	     String strSQL = "";
	     StringBuffer addBuf = new StringBuffer();
	     addBuf.append("Select B.ID,B.NAME,B.CREATEDATE,C.NAME AS FACCEPTPSN,D.NAME AS CURRACTIVITY,E.NAME AS ACCEPTPSN "
	     			 + "From FLOW_RUNTIME_PROCESS B Left Join BPIP_USER C on B.FACCEPTPSN=C.USERID Left Join FLOW_CONFIG_ACTIVITY D on B.CURRACTIVITY=D.ID Left Join BPIP_USER E on B.ACCEPTPSN=E.USERID "
	     			 + "Where ((B.CREATEPSN ='");
	     addBuf.append(strUserID+"' and B.ACCEPTPSN not like '"+strUserID+"' and B.STATE<>'4') or (B.ACCEPTPSN not like '");
	     addBuf.append(strUserID+"' and B.STATE<>'4')) and B.ID in (select FID from FLOW_RUNTIME_ACTIVITY where DOPSN='"+strUserID);
	     addBuf.append("' and DOFLAG='1') and "+strwhere+"order by B.CREATEDATE desc");
	     strSQL = addBuf.toString();
	     addBuf.delete(0, addBuf.length());//清空
	     if (currentPage <= 0) {
	    	 currentPage = 1;// 初始化页码
	     }
	     if (DataBaseType.equals("1")) {// Oracle数据库
	    	 addBuf.append("Select T.* From (SELECT A.*, ROWNUM RN FROM ("+strSQL+") AS A WHERE ROWNUM <= ")
		     	   .append(currentPage*PageSize+") AS T WHERE RN > "+(currentPage-1)*PageSize);
	     }
	     else if (DataBaseType.equals("2")) {// MSSQL数据库 
	    	 addBuf.append("Select Top "+PageSize+" T.* From (SELECT ROW_NUMBER() OVER(ORDER BY ID) AS MSROWNUM, A.* FROM ("+strSQL+") AS A) AS T WHERE MSROWNUM > ")
	     	   	   .append((currentPage-1)*PageSize);
	     }
	     else if (DataBaseType.equals("3")) {// MySQL数据库
	    	 int offset = (currentPage-1)*PageSize;
	    	 addBuf.append("SELECT T.* FROM ("+strSQL+") AS T LIMIT "+offset+","+PageSize);
	     }
	     List<Map<String, Object>> dbsetPageCount = userMapper.selectListMapExecSQL(strSQL);
		 List<Map<String, Object>> retlist = userMapper.selectListMapExecSQL(addBuf.toString());
		 int length = retlist != null ? retlist.size() : 0;
	     if (length > 0) {
	         returnValue = new FLOW_RUNTIME_PROCESS[length];
	         for (int i = 0; i < length; i++) {
	        	 Map<String, Object> retmap = retlist.get(i);
	        	 returnValue[i] = (FLOW_RUNTIME_PROCESS) ReflectionUtil.convertMapToBean(retmap, FLOW_RUNTIME_PROCESS.class);
	         }
	         if(returnValue != null){
	        	 GetProList2_pageCount=dbsetPageCount.size()+"";
	         }
	     }
	     return returnValue;  
	}

	@Override
	public FLOW_RUNTIME_PROCESS[] getProFinshList(String UserID, int currentPage, int PageSize, String strwhere)
			throws Exception {
		FLOW_RUNTIME_PROCESS[] returnValue = null;
	    String strSQL = "";
	    StringBuffer addBuf = new StringBuffer();
	    addBuf.append("Select B.ID,B.NAME,B.CREATEDATE,B.FDATE,C.NAME AS FACCEPTPSN,D.NAME AS CURRACTIVITY,E.NAME AS ACCEPTPSN "
	    			+ "From FLOW_RUNTIME_PROCESS B Left Join BPIP_USER C on B.FACCEPTPSN=C.USERID Left Join FLOW_CONFIG_ACTIVITY D on B.CURRACTIVITY=D.ID Left Join BPIP_USER E on B.ACCEPTPSN=E.USERID "
	    			+ "where B.STATE='4' and B.ID in (select FID from FLOW_RUNTIME_ACTIVITY where DOPSN='"+UserID);
	    addBuf.append("' and DOFLAG='1') and "+strwhere+" order by B.CREATEDATE desc");
	    strSQL = addBuf.toString();
	    addBuf.delete(0, addBuf.length());//清空
	    if (currentPage <= 0) {
	    	currentPage = 1;// 初始化页码
	    }
	    if (DataBaseType.equals("1")) {// Oracle数据库
	 	    addBuf.append("Select T.* From (SELECT A.*, ROWNUM RN FROM ("+strSQL+") AS A WHERE ROWNUM <= ")
	 	    	  .append((currentPage*PageSize)+") AS T WHERE RN > "+(currentPage-1)*PageSize);
	    }
	    else if (DataBaseType.equals("2")) {// MSSQL数据库 
	    	 addBuf.append("Select Top "+PageSize+" T.* From (SELECT ROW_NUMBER() OVER(ORDER BY ID) AS MSROWNUM, A.* FROM ("+strSQL+") AS A) AS T WHERE MSROWNUM > ")
	     	   	   .append((currentPage-1)*PageSize);
	    }
	    else if (DataBaseType.equals("3")) {// MySQL数据库
	    	 int offset = (currentPage-1)*PageSize;
	    	 addBuf.append("SELECT T.* FROM ("+strSQL+") AS T LIMIT "+offset+","+PageSize);
	    }
	    List<Map<String, Object>> dbsetPageCount = userMapper.selectListMapExecSQL(strSQL);
		List<Map<String, Object>> retlist = userMapper.selectListMapExecSQL(addBuf.toString());
		int length = retlist != null ? retlist.size() : 0;
		if (length > 0) {
		    returnValue = new FLOW_RUNTIME_PROCESS[length];
		    for (int i = 0; i < length; i++) {
		    	Map<String, Object> retmap = retlist.get(i);
		    	returnValue[i] = (FLOW_RUNTIME_PROCESS) ReflectionUtil.convertMapToBean(retmap, FLOW_RUNTIME_PROCESS.class);
		    }
		    if(returnValue != null){
		    	GetProFinshList_pageCount=dbsetPageCount.size()+"";
		    }
		}
		return returnValue;
	}

	@Override
	public FunctionMessage delDaily(String strID) throws Exception {
	    FunctionMessage funMsg = new FunctionMessage(1);
	    try {
	      String execSQL = "Delete From FLOW_RUNTIME_ENTRUSTLOG Where ID='"+strID+"'";
	      Integer retInt = userMapper.deleteExecSQL(execSQL);
	      if (retInt != null && retInt > 0) {
	    	  funMsg.setResult(true);
	    	  funMsg.setMessage("日志【" + strID + "】已经删除");
	      } else {// retInt == 0
	    	  funMsg.setMessage("删除不成功");
	    	  funMsg.setResult(false);
	      }
	    } catch (Exception e) {
	    	LOGGER.error("FlowMonitorServiceImpl.delDaily Exception:\n", e);
	    	funMsg.setResult(false);
	    	funMsg.setMessage("调用方法delDaily出现异常" + e.toString());
	    }
	    return funMsg;
	}

	@Override
	public boolean delDailyList() throws Exception {
		String execSQL = "Delete From FLOW_RUNTIME_ENTRUSTLOG";
	    Integer retInt = userMapper.deleteExecSQL(execSQL);
	    if (retInt != null && retInt > 0) {
	    	return true;
	    }
	    return false;
	}

	@Override
	public FLOW_RUNTIME_ENTRUSTLOG[] dailyList(int currentPage, int PageSize) throws Exception {
	    FLOW_RUNTIME_ENTRUSTLOG[] returnValue = null;
	    String strSQL = "";
	    StringBuffer addBuf = new StringBuffer();
	    addBuf.append("Select B.ID,B.SDATE,B.EDATE,B.FLOWNAME,B.FLOWID,B.FLOWNODE,B.LOGDATE,B.TYPE,C.NAME AS SUSERNO,D.NAME AS IUSERNO "
	    			+ "From FLOW_RUNTIME_ENTRUSTLOG B Left Join BPIP_USER C on B.SUSERNO=C.USERID Left Join BPIP_USER D on B.IUSERNO=D.USERID "
	    			+ "order by B.LOGDATE desc");
	    strSQL = addBuf.toString();
	    addBuf.delete(0, addBuf.length());//清空
	    if (currentPage <= 0) {
	    	currentPage = 1;// 初始化页码
	    }
	    if (DataBaseType.equals("1")) {// Oracle数据库
	 	    addBuf.append("Select T.* From (SELECT A.*, ROWNUM RN FROM ("+strSQL+") AS A WHERE ROWNUM <= ")
	 	    	  .append((currentPage*PageSize)+") AS T WHERE RN > "+(currentPage-1)*PageSize);
	    }
	    else if (DataBaseType.equals("2")) {// MSSQL数据库 
	    	 addBuf.append("Select Top "+PageSize+" T.* From (SELECT ROW_NUMBER() OVER(ORDER BY ID) AS MSROWNUM, A.* FROM ("+strSQL+") AS A) AS T WHERE MSROWNUM > ")
	     	   	   .append((currentPage-1)*PageSize);
	    }
	    else if (DataBaseType.equals("3")) {// MySQL数据库
	    	 int offset = (currentPage-1)*PageSize;
	    	 addBuf.append("SELECT T.* FROM ("+strSQL+") AS T LIMIT "+offset+","+PageSize);
	    }
	    List<Map<String, Object>> dbsetPageCount = userMapper.selectListMapExecSQL(strSQL);
		List<Map<String, Object>> retlist = userMapper.selectListMapExecSQL(addBuf.toString());
		int length = retlist != null ? retlist.size() : 0;
	    if (length > 0) {
	        returnValue = new FLOW_RUNTIME_ENTRUSTLOG[length];
	        for (int i = 0; i < length; i++) {
	        	Map<String, Object> retmap = retlist.get(i);
	        	returnValue[i] = (FLOW_RUNTIME_ENTRUSTLOG) ReflectionUtil.convertMapToBean(retmap, FLOW_RUNTIME_ENTRUSTLOG.class);
	        }
	        if(returnValue != null){
	        	DailyList_pageCount=dbsetPageCount.size()+"";
	        }
	    }
	    return returnValue;  
	}

	@Override
	public FLOW_RUNTIME_PROCESS[] getProList1(String strUserID, String strAllUnit, 
			int currentPage, int PageSize, String type, String strwhere) throws Exception {
	    FLOW_RUNTIME_PROCESS[] returnValue = null;
	    String strTMP = "";
	    String strTMP1 = "";
	    String strATMP = "";
	    String strUsers = "";
	    String strFLOW1 = "";//可分配的流程串
	    String strFLOW2 = "";//可分配的本部门流程串
	    String strFLOW3 = "";//可分配的本单位流程串
	    String strROLEID = "";
	    //得到本部门下的人员串
	    String strSQL = "select USERID from BPIP_USER where UNITID = '"+strAllUnit+"'";
	    List<Map<String, Object>> retlist = userMapper.selectListMapExecSQL(strSQL);
	    int length = retlist != null ? retlist.size() : 0;
	    if (length > 0){
	         for (int i=0; i<length; i++) {
	        	Map<String, Object> retmap = retlist.get(i);
	            if (i==0) {
	               strATMP = retmap.get("USERID").toString();
	            } else {
	              strATMP = strATMP +","+retmap.get("USERID").toString();
	            }
	         }
	    }
	    strUsers=strATMP;
	    //流程ID与流程标识对照表
	    Map<String, Object> FlowHashtable = new HashMap<String, Object>();
	    //本单位正在运转的流程
	    strSQL = "select ID,IDENTIFICATION from FLOW_CONFIG_PROCESS";
	    List<Map<String, Object>> retlist1 = userMapper.selectListMapExecSQL(strSQL);
	    int length1 = retlist1 != null ? retlist1.size() : 0;
	    if (length1 > 0){
	         for (int i=0; i<length1; i++) {
	        	 Map<String, Object> retmap1 = retlist1.get(i);
	             FlowHashtable.put(retmap1.get("ID").toString(), retmap1.get("IDENTIFICATION").toString());
	         }
	    }
        strSQL = "select ROLEID from BPIP_USER_ROLE where USERID='"+strUserID+"'";
        List<Map<String, Object>> retlist2 = userMapper.selectListMapExecSQL(strSQL);
        int length2 = retlist2 != null ? retlist2.size() : 0;
        if (length2 > 0){
           for (int i=0; i<length2; i++) {
        	   Map<String, Object> retmap2 = retlist2.get(i);
               if (i==0) {
                 strATMP = "'"+retmap2.get("ROLEID").toString()+"'";
               } else {
                 strATMP = strATMP + ",'"+retmap2.get("ROLEID").toString()+"'";
               }
           }
        }
        strROLEID = strATMP;
	    strSQL = "select PROSESSID from FLOW_CONFIG_PROSESS_GROUP where ISDEPT='1' and TYPE='"+type+"' and GROUPID in ("+strROLEID+")";
	    List<Map<String, Object>> retlist3 = userMapper.selectListMapExecSQL(strSQL);
        int length3 = retlist3 != null ? retlist3.size() : 0;
	    if (length3 > 0) {
	         for (int i=0; i<length3; i++) {
	        	 Map<String, Object> retmap3 = retlist3.get(i);
	             strTMP = retmap3.get("PROSESSID").toString();
	             strTMP = (String) FlowHashtable.get(strTMP);
	             strFLOW1 = strFLOW1+"'"+strTMP+"',";
	             strFLOW2 = strFLOW2+"'"+strTMP+"',";
	         }
	    }
	    //查找当前用户是监控所有的流程
	    strSQL = "select PROSESSID from FLOW_CONFIG_PROSESS_GROUP where ISDEPT='0' and TYPE='"+type+"' and GROUPID in ("+strROLEID+")";
	    List<Map<String, Object>> retlist4 = userMapper.selectListMapExecSQL(strSQL);
        int length4 = retlist4 != null ? retlist4.size() : 0;
	    if (length4 > 0) {
	         for (int i=0; i<length4; i++) {
	        	 Map<String, Object> retmap4 = retlist4.get(i);
	             strTMP = retmap4.get("PROSESSID").toString();
	             strTMP = (String) FlowHashtable.get(strTMP);
	             strFLOW1 = strFLOW1+"'"+strTMP+"',";
	             strFLOW3 = strFLOW3+"'"+strTMP+"',";
	         }
	    }
	    if (strFLOW1.length()>0) {
	      strFLOW1 = strFLOW1.substring(0,strFLOW1.length()-1);
	    }
	    if (strFLOW1.length()>0) {
	       strSQL = "Select B.ID,B.NAME,B.ACCEPTDATE,B.CREATEDATE,B.STATE,B.FLOWID,B.CREATEPSN AS CREATEPSN1,C.NAME AS FACCEPTPSN,D.NAME AS CURRACTIVITY,E.NAME AS ACCEPTPSN "
	       		  + "From FLOW_RUNTIME_PROCESS B Left Join BPIP_USER C on B.FACCEPTPSN=C.USERID Left Join FLOW_CONFIG_ACTIVITY D on B.CURRACTIVITY=D.ID Left Join BPIP_USER E on B.ACCEPTPSN=E.USERID "
	       		  + "where B.STATE<>'2' and B.STATE<>'3' and B.STATE<>'4' and B.FLOWID in ("+strFLOW1+") and "+strwhere+" order by B.CREATEDATE desc";
	       LOGGER.info("getAllotmentList sql:" + strSQL);
	       List<Map<String, Object>> dbsetPageCount = userMapper.selectListMapExecSQL(strSQL);
	       GetProList1_pageCount = dbsetPageCount.size()+"";
	       if (currentPage <= 0) {
	    	   currentPage = 1;// 初始化页码
		   }
	       if (DataBaseType.equals("1")) {// Oracle数据库
	    	   strSQL = "Select T.* From (SELECT A.*, ROWNUM RN FROM ("+strSQL+") AS A WHERE ROWNUM <= "+
		 	    	  	(currentPage*PageSize)+") AS T WHERE RN > "+(currentPage-1)*PageSize;
		   }
		   else if (DataBaseType.equals("2")) {// MSSQL数据库 
			   strSQL = "Select Top "+PageSize+" T.* From (SELECT ROW_NUMBER() OVER(ORDER BY ID) AS MSROWNUM, A.* FROM ("+strSQL+") AS A) AS T WHERE MSROWNUM > "+
		     	  	  	(currentPage-1)*PageSize;
		   }
		   else if (DataBaseType.equals("3")) {// MySQL数据库
		    	int offset = (currentPage-1)*PageSize;
		    	strSQL = "SELECT T.* FROM ("+strSQL+") AS T LIMIT "+offset+","+PageSize;
		   }
	       List<Map<String, Object>> retlist5 = userMapper.selectListMapExecSQL(strSQL);
	       int length5 = retlist5 != null ? retlist5.size() : 0;
	       if (length5 > 0) {
	           //先计算记录数
	           int rownum=0;
	           for (int i = 0; i < length5; i++) {
	        	   Map<String, Object> retmap5 = retlist5.get(i);
	               strTMP = retmap5.get("FLOWID").toString();
	               strTMP1 = retmap5.get("CREATEPSN1").toString();
	               if (strFLOW2.lastIndexOf(strTMP) > -1) {//是本部门的流程
	                  if (IsMatchDept(strTMP1,strUsers)) {//当前处理人是本部门的人员
	                	  rownum=rownum+1;
	                	  //翻页时加
	                	  continue;
	                  }
	               }
	               if (strFLOW3.lastIndexOf(strTMP) > -1) {//是本部门的流程
	            	   rownum=rownum+1;
	               }
	           }
	           //-----------------------
	           returnValue = new FLOW_RUNTIME_PROCESS[rownum];
	           rownum=0;
	           for (int i = 0; i < length5; i++) {
	        	   Map<String, Object> retmap5 = retlist5.get(i);
	               strTMP = retmap5.get("FLOWID").toString();
	               strTMP1 = retmap5.get("CREATEPSN1").toString();
	               if (strFLOW2.lastIndexOf(strTMP) > -1) {//是本部门的流程
	                  if (IsMatchDept(strTMP1,strUsers)) {//创建人是本部门的人员
	                	  returnValue[rownum] = (FLOW_RUNTIME_PROCESS) ReflectionUtil.convertMapToBean(retmap5, FLOW_RUNTIME_PROCESS.class);
	                	  rownum=rownum+1;
	                	  //翻页时加
	                	  continue;
	                  }
	               }
	               if (strFLOW3.lastIndexOf(strTMP) > -1) {//是本部门的流程
	                    returnValue[rownum] = (FLOW_RUNTIME_PROCESS) ReflectionUtil.convertMapToBean(retmap5, FLOW_RUNTIME_PROCESS.class);
	                    rownum=rownum+1;
	               }
	           }
	           //------------------------------------------
	       }
	    }
	    return returnValue;  
	}

	@Override
	public FLOW_RUNTIME_PROCESS getPro(String ID) throws Exception {
	    FLOW_RUNTIME_PROCESS bp = null;
	    try {
	      StringBuffer addBuf = new StringBuffer();
	      addBuf.append("Select * From FLOW_RUNTIME_PROCESS Where ID='"+ID.trim()+"'");
	      Map<String, Object> retmap = userMapper.selectMapExecSQL(addBuf.toString());
	      if (retmap == null || retmap.size() < 1) {
	    	  return null;
	      } else {// retmap != null
	          bp = (FLOW_RUNTIME_PROCESS) ReflectionUtil.convertMapToBean(retmap, FLOW_RUNTIME_PROCESS.class);
	      }
	    } catch (Exception e) {
	      return null;
	    }
	    return bp;    
	}

	@Override
	public boolean flowDevolveManage(String strID, String strDOPSN, String strUserID) throws Exception {
	    boolean returnValue = true;
	    String strACCEPTPSN = ""; //当前处理人
	    //得到当前接收人
	    StringBuffer addBuf = new StringBuffer();
	    addBuf.append("Select ACCEPTPSN From FLOW_RUNTIME_PROCESS where ID='"+strID+"'");
	    Map<String, Object> retmap = userMapper.selectMapExecSQL(addBuf.toString());
	    if (retmap != null && retmap.size() > 0) {
	        strACCEPTPSN = retmap.get("ACCEPTPSN").toString();
	    }
	    //将处理人中的本人替换成被移交人
	    strACCEPTPSN = strACCEPTPSN.replaceAll(strUserID, strDOPSN);
	    //重设流程接收人
	    String[] strSQLs = new String[2];
	    addBuf.delete(0,addBuf.length());//清空
	    addBuf.append("update FLOW_RUNTIME_PROCESS set ACCEPTPSN='"+strACCEPTPSN+"' where ID='"+strID+"'");
	    strSQLs[0] = addBuf.toString();
	    //更新所有的本人接收到的流转过程记录为被移交人
	    addBuf.delete(0,addBuf.length());//清空
	    addBuf.append("update FLOW_RUNTIME_ACTIVITY set DOPSN='"+strDOPSN+"' where FID='")
	          .append(strID+"' and DOPSN='"+strUserID+"'");
	    strSQLs[1] = addBuf.toString();
	    for (String strSQL : strSQLs) {
	    	userMapper.updateExecSQL(strSQL);
	    }
	    //增加流程移交日志
	    String strName = getFlowRunName(strID); //得到流转流程的名称
	    String strCurrAID = getCurrActivityID(strID); //得到当前步骤的ID
	    String strCurrAName = getActivityName(strCurrAID); //得到当前步骤的名称
	    InsertFlowLog(strUserID, strDOPSN, "", "", "3", strName, strID,strCurrAName);
	    return returnValue;
	}

	@Override
	public Map<String, Object> getUserList() throws Exception {
		Map<String, Object> resultValue = new HashMap<String, Object>();
	    String strSQL = "Select USERID,NAME From BPIP_USER order by ORBERCODE,USERID";
	    List<Map<String, Object>> retlist = userMapper.selectListMapExecSQL(strSQL);
	    int length = retlist != null ? retlist.size() : 0;
	    if (length > 0) {
	        for (int i = 0; i < length; i++) {
	        	Map<String, Object> retmap = retlist.get(i);
	        	resultValue.put(retmap.get("USERID").toString(), retmap.get("NAME").toString());
	        }
	    }
	    return resultValue;
	}

	@Override
	public Map<String, Object> getActivityList() throws Exception {
		Map<String, Object> resultValue = new HashMap<String, Object>();
	    String strSQL = "Select ID,NAME From FLOW_CONFIG_ACTIVITY";
	    List<Map<String, Object>> retlist = userMapper.selectListMapExecSQL(strSQL);
	    int length = retlist != null ? retlist.size() : 0;
	    if (length > 0) {
	        for (int i = 0; i < length; i++) {
	        	Map<String, Object> retmap = retlist.get(i);
	        	resultValue.put(retmap.get("ID").toString(), retmap.get("NAME").toString());
	        }
	    }
	    return resultValue;
	}

	@Override
	public String getActivityName1(String strID) throws Exception {
		StringBuffer addBuf = new StringBuffer();
	    addBuf.append("Select NAME from FLOW_CONFIG_ACTIVITY where ID = '"+strID+"'");
	    Map<String, Object> retmap = userMapper.selectMapExecSQL(addBuf.toString());
	    addBuf.delete(0,addBuf.length());//清空
	    if (retmap != null && retmap.size() > 0) {
	    	addBuf.append(retmap.get("NAME").toString());
	    }
	    return addBuf.toString();
	}

	@Override
	public FLOW_CONFIG_ENTRUST[] getMonitorList(String userid) throws Exception {
	    FLOW_CONFIG_ENTRUST[] returnValue = null;
	    StringBuffer addBuf = new StringBuffer();
	    addBuf.append("Select a.ID,a.SDATE,a.EDATE,b.NAME "
	    			+ "From FLOW_CONFIG_ENTRUST a Left Join BPIP_USER b on a.IUSERNO=b.USERID "
	    			+ "Where a.SUSERNO ='").append(userid+"'");
	    List<Map<String, Object>> retlist = userMapper.selectListMapExecSQL(addBuf.toString());
	    int length = retlist != null ? retlist.size() : 0;
	    String strtmp = "";
	    if (length > 0) {
	        returnValue = new FLOW_CONFIG_ENTRUST[length];
	        for (int i = 0; i < length; i++) {
	          Map<String, Object> retmap = retlist.get(i);
	          returnValue[i] = (FLOW_CONFIG_ENTRUST) ReflectionUtil.convertMapToBean(retmap, FLOW_CONFIG_ENTRUST.class);
	          strtmp = retmap.get("NAME").toString();
	          returnValue[i].setIUSERNO(strtmp);
	        }
	    }
	    return returnValue;  
	}

	@Override
	public FunctionMessage addEntrust(FLOW_CONFIG_ENTRUST Entrust) throws Exception {
	    List<Object> arrList_User_No = getArrayList(Entrust.getIUSERNO(), ",");
	    FunctionMessage fm = new FunctionMessage(1);
	    String strid = "";
	    strid = getMaxFieldNo("FLOW_CONFIG_ENTRUST", "ID", 8);
	    String MaxNo = strid;
	    try {
	      for (int i = 0; i < arrList_User_No.size(); i++) {
	        if (i > 0) {
	          MaxNo = String.valueOf(Integer.parseInt(MaxNo) + 1);
	          int LenMaxNo = MaxNo.length();
	          MaxNo = "0000000000000000000000000" + MaxNo;
	          MaxNo = MaxNo.substring(17 + LenMaxNo);
	        }
	        Entrust.setID(MaxNo);
	        Entrust.setIUSERNO(arrList_User_No.get(i).toString());
	        boolean isOK = dbEngine.ExecuteInsert(Entrust.getData());
	        if (isOK) {
	          fm.setMessage("增加人员保存成功");
	          fm.setResult(true);
	          //增加流程权限委托日志
	          String strSdate = DateWork.DateTimeToString(Entrust.getSDATE());
	          String strEdate = DateWork.DateTimeToString(Entrust.getEDATE());
	          if (strSdate.length() > 0) {
	            strSdate = strSdate.substring(0, 10);
	          }
	          if (strEdate.length() > 0) {
	            strEdate = strEdate.substring(0, 10);
	          }
	          InsertFlowLog(Entrust.getSUSERNO(), arrList_User_No.get(i).toString(), strSdate, strEdate, "4", "", "", "");
	        }
	      }
	    } catch (Exception e) {
	      e.printStackTrace();
	      fm.setResult(false);
	      fm.setMessage("调用方法addEntrust出现异常" + e.toString());
	    }
	    return fm;
	}

	@Override
	public BPIP_USER[] getentrustName() throws Exception {
		BPIP_USER entrust[] = null;
	    try {
	      String strSQL = "Select * From BPIP_USER";
	      List<Map<String, Object>> retlist = userMapper.selectListMapExecSQL(strSQL);
	      int length = retlist != null ? retlist.size() : 0;
	      if (length == 0) {
	        return null;
	      } else {// length > 0
	    	entrust = new BPIP_USER[length];
	        for (int i = 0; i < length; i++) {
	          Map<String, Object> retmap = retlist.get(i);
	          entrust[i] = (BPIP_USER) ReflectionUtil.convertMapToBean(retmap, BPIP_USER.class);
	        }
	      }
	    } catch (Exception e) {
	      e.printStackTrace();
	      return null;
	    }
	    return entrust;
	}

	@Override
	public FLOW_CONFIG_ENTRUST getEntrustID(String ID) throws Exception {
	    FLOW_CONFIG_ENTRUST bp = null;
	    FunctionMessage fm = new FunctionMessage(1);
	    try {
	      StringBuffer addBuf = new StringBuffer();
	      addBuf.append("Select * From FLOW_CONFIG_ENTRUST Where ID='"+ID+"'");
	      Map<String, Object> retmap = userMapper.selectMapExecSQL(addBuf.toString());
	      if (retmap != null && retmap.size() > 0) {
	        bp = (FLOW_CONFIG_ENTRUST) ReflectionUtil.convertMapToBean(retmap, FLOW_CONFIG_ENTRUST.class);
	      } else {
	        fm.setResult(false);
	        fm.setMessage("你没有编辑的权限");
	      }
	    } catch (Exception e) {
	      e.printStackTrace();
	      return null;
	    }
	    return bp;  
	}

	@Override
	public FunctionMessage deleteEntrust(String ID) throws Exception {
	    FunctionMessage fm = new FunctionMessage(1);
	    try {
	      StringBuffer addBuf = new StringBuffer();
	      addBuf.append("Delete From FLOW_CONFIG_ENTRUST where ID='"+ID+"'");
	      userMapper.deleteExecSQL(addBuf.toString());
	      fm.setResult(true);
	      fm.setMessage("已经成功删除");
	    } catch (Exception e) {
	      e.printStackTrace();
	      fm.setResult(false);
	      fm.setMessage("调用方法DeleteEntrust出现异常" + e.toString());
	    }
	    return fm;  
	}

	@Override
	public FunctionMessage editEntrust(FLOW_CONFIG_ENTRUST Entrust) throws Exception {
	    FunctionMessage fm = new FunctionMessage(1);
	    try {
	      boolean isOk = dbEngine.ExecuteEdit(Entrust.getData(), "ID=" + Entrust.getID());
	      if (isOk) {
	        fm.setResult(true);
	        fm.setMessage("修改成功");
	      } else {
	        fm.setResult(false);
	        fm.setMessage("【" + Entrust.getID() + "】不存在");
	      }
	    } catch (Exception e) {
	      e.printStackTrace();
	      fm.setResult(false);
	      fm.setMessage("调用方法EditEntrust出现异常" + e.toString());
	    }
	    return fm;  
	}

	@Override
	public String getTrueName(String strIUSERNO) throws Exception {
	    StringBuffer addBuf = new StringBuffer();
	    if (strIUSERNO.indexOf(",") > -1) {
	        if (strIUSERNO.indexOf("'")==-1) {
	            strIUSERNO = strIUSERNO.replaceAll(",", "','");
	            addBuf.append("'"+strIUSERNO+"'");
	            strIUSERNO = addBuf.toString();
	        }
	    } else {
	        if (strIUSERNO.indexOf("'")==-1) {
	            addBuf.append("'"+strIUSERNO+"'");
	            strIUSERNO = addBuf.toString();
	        }
	    }
	    addBuf.delete(0,addBuf.length());//清空
	    addBuf.append("select NAME from BPIP_USER where USERID in ("+strIUSERNO+")");
	    List<Map<String, Object>> retlist = userMapper.selectListMapExecSQL(addBuf.toString());
	    int length = retlist != null ? retlist.size() : 0;
	    addBuf.delete(0,addBuf.length());//清空
	    if (length > 0) {
	        for (int i=0; i<length; i++) {
	          Map<String, Object> retmap = retlist.get(i);
	          if (addBuf.toString().length() > 0) {
	            addBuf.append(","+retmap.get("NAME").toString());
	          } else {
	            addBuf.append(retmap.get("NAME").toString());
	          }
	        }
	    }
	    return addBuf.toString();  
	}

	@Override
	public String getDateAsStr() throws Exception {
	    Calendar cal = Calendar.getInstance();
	    DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	    String mDateTime = formatter.format(cal.getTime());
	    return (mDateTime);
	}

	@Override
	public FLOW_RUNTIME_ACTIVITY[] getOkList(String strWhere) throws Exception {
	    FLOW_RUNTIME_ACTIVITY[] returnValue = null;
	    StringBuffer addBuf = new StringBuffer();
	    addBuf.append("Select * From FLOW_RUNTIME_ACTIVITY "+strWhere);
	    List<Map<String, Object>> retlist = userMapper.selectListMapExecSQL(addBuf.toString());
	    int length = retlist != null ? retlist.size() : 0;
	    if (length > 0) {
	        returnValue = new FLOW_RUNTIME_ACTIVITY[length];
	        for (int i = 0; i < length; i++) {
	          Map<String, Object> retmap = retlist.get(i);
	          returnValue[i] = (FLOW_RUNTIME_ACTIVITY) ReflectionUtil.convertMapToBean(retmap, FLOW_RUNTIME_ACTIVITY.class);
	        }
	    }
	    return returnValue;  
	}

	@Override
	public FLOW_RUNTIME_PROCESS[] getProActList(String strWhere) throws Exception {
	    FLOW_RUNTIME_PROCESS[] returnValue = null;
	    List<Map<String, Object>> retlist = null;
	    StringBuffer addBuf = new StringBuffer();
	    addBuf.append("Select ID From FLOW_RUNTIME_ACTIVITY "+strWhere);
	    List<Map<String, Object>> retlist1 = userMapper.selectListMapExecSQL(addBuf.toString());
	    if (retlist1 != null && retlist1.size() > 0) {
	      addBuf.delete(0,addBuf.length());//清空
	      addBuf.append("select * from FLOW_RUNTIME_PROCESS where ID='"
	    		  	   + retlist1.get(0).get("FID").toString()+"'");
	      retlist = userMapper.selectListMapExecSQL(addBuf.toString());
	      retlist1 = null;//赋空值
	    }
	    int length = retlist != null ? retlist.size() : 0;
	    if (length > 0) {
	        returnValue = new FLOW_RUNTIME_PROCESS[length];
	        for (int i = 0; i < length; i++) {
	          Map<String, Object> retmap = retlist.get(i);
	          returnValue[i] = (FLOW_RUNTIME_PROCESS) ReflectionUtil.convertMapToBean(retmap, FLOW_RUNTIME_PROCESS.class);
	        }
	    }
	    return returnValue;  
	}

	@Override
	public boolean flowEntrustManage(String strID, String strDOPSN, String strUserID, String strUNIT) throws Exception {
		boolean returnValue = true;
	    String strACCEPTPSN = ""; //当前处理人
	    //得到当前接收人
	    StringBuffer addBuf = new StringBuffer();
	    addBuf.append("Select ACCEPTPSN From FLOW_RUNTIME_PROCESS where ID='"+strID+"'");
	    Map<String, Object> retmap = userMapper.selectMapExecSQL(addBuf.toString());
	    if (retmap != null && retmap.size() > 0) {
	        strACCEPTPSN = retmap.get("ACCEPTPSN").toString();
	    }
	    //将处理人中的本人替换成受委托人
	    strACCEPTPSN = strACCEPTPSN.replaceAll(strUserID, strDOPSN);
	    //重设流程接收人
	    String[] strSQLs = new String[2];
	    addBuf.delete(0,addBuf.length());//清空
	    addBuf.append("update FLOW_RUNTIME_PROCESS set ACCEPTPSN='"+strACCEPTPSN+"' where ID='"+strID)
	          .append("'");
	    strSQLs[0] =addBuf.toString();
	    //更新本人接收到的流转过程记录为受委托人
	    addBuf.delete(0,addBuf.length());//清空
	    addBuf.append("update FLOW_RUNTIME_ACTIVITY set DOPSN='"+strDOPSN+"' where FID='"+strID)
	        .append("' and DOPSN='"+strUserID+"' and DOFLAG='0'");
	    strSQLs[1] = addBuf.toString();
	    for (String strSQL : strSQLs) {
	    	userMapper.updateExecSQL(strSQL);
	    }
	    //增加流程委托日志
	    String strName = getFlowRunName(strID); //得到流转流程的名称
	    String strCurrAID = getCurrActivityID(strID); //得到当前步骤的ID
	    String strCurrAName = getActivityName(strCurrAID); //得到当前步骤的名称
	    InsertFlowLog(strUserID, strDOPSN, "", "", "2", strName, strID, strCurrAName);
	    return returnValue;
	}

	@Override
	public boolean flowAnewManage(String strID, String strDOPSN, String strUserID, String strUNIT) throws Exception {
	    boolean returnValue = true;
	    //String strACCEPTPSN = ""; //当前处理人
	    String strSENDPSN = ""; //发送人
	    String strSENDDATE = ""; //发送时间
	    String strSACTIVITY = ""; //开始步骤
	    String strEACTIVITY = ""; //开始步骤
	    String strNAME = ""; //流转名称
	    String strDoUser = ""; //拆分后的单一处理人变量
	    String strRunID = ""; //流转过程的ID
	    int UserNum = 0; //人员数量
	    StringBuffer addBuf = new StringBuffer();
	    
	    //得到当前步骤的发送人ID、发送时间、开始活动ID、结束活动ID、流转名称
	    addBuf.append("Select SENDPSN,SENDDATE,SACTIVITY,EACTIVITY,NAME From FLOW_RUNTIME_ACTIVITY "
	    			+ "where DOFLAG='0' and FID='"+strID+"'");
	    DBSet dbset1 = dbEngine.QuerySQL(addBuf.toString());
	    if (dbset1 != null && dbset1.RowCount() > 0) {
	        strSENDPSN = dbset1.Row(0).Column("SENDPSN").getString();
	        strSENDDATE = DateWork.DateTimeToString(dbset1.Row(0).Column("SENDDATE").getDate());
	        if (strSENDDATE.length() > 10) {
	        	strSENDDATE = strSENDDATE.substring(0, 10);
	        }
	        strSACTIVITY = dbset1.Row(0).Column("SACTIVITY").getString();
	        strEACTIVITY = dbset1.Row(0).Column("EACTIVITY").getString();
	        strNAME = dbset1.Row(0).Column("NAME").getString();
	        dbset1=null;//赋空值
	    }
	    //删除当前流程待处理人的相关流程记录
	    addBuf.delete(0,addBuf.length());//清空
	    addBuf.append("delete from FLOW_RUNTIME_ACTIVITY where DOFLAG='0' and FID='"+strID+"'");
	    userMapper.deleteExecSQL(addBuf.toString());
	    //重设流程接收人
	    List<Object> UserList = getArrayList(strDOPSN, ",");
	    UserNum = UserList.size(); //处理人数量
	    addBuf.delete(0,addBuf.length());//清空
	    addBuf.append("update FLOW_RUNTIME_PROCESS set ACCEPTPSN='"+strDOPSN+"',ACCEPTPSNNUM=")
	          .append(UserNum+" where ID='"+strID+"'");
	    userMapper.updateExecSQL(addBuf.toString());
	    //插入新分配的流转过程记录
	    for (int i = 0; i < UserList.size(); i++) {
	      strDoUser = UserList.get(i).toString();
	      //计算流程流转过程表的ID
	      strRunID = getFLOW_RUNTIME_ACTIVITY_ID();
	      addBuf.delete(0, addBuf.length());//清空
	      if (DataBaseType.equals("1")) {// Oracle数据库
	    	  addBuf.append("Insert into FLOW_RUNTIME_ACTIVITY(ID,FID,SACTIVITY,EACTIVITY,NAME,DOPSN,DOFLAG,DOIP,SENDPSN,SENDDATE) values ('")
          			.append(strRunID+"','"+strID+"','"+strSACTIVITY+"','"+strEACTIVITY)
          			.append("','"+strNAME+"','"+strDoUser+"','0','','"+strSENDPSN+"',to_date('")
          			.append(strSENDDATE+"','yyyy-mm-dd'))");
	      }
	      else if (DataBaseType.equals("2")) {// MSSQL数据库
	    	  addBuf.append("Insert into FLOW_RUNTIME_ACTIVITY(ID,FID,SACTIVITY,EACTIVITY,NAME,DOPSN,DOFLAG,DOIP,SENDPSN,SENDDATE) values ('")
	            	.append(strRunID+"','"+strID+"','"+strSACTIVITY+"','"+strEACTIVITY)
	            	.append("','"+strNAME+"','"+strDoUser+"','0','','"+strSENDPSN+"','")
	            	.append(strSENDDATE+"')");
	      }
	      else if (DataBaseType.equals("3")) {// MySQL数据库
	    	  addBuf.append("Insert into FLOW_RUNTIME_ACTIVITY(ID,FID,SACTIVITY,EACTIVITY,NAME,DOPSN,DOFLAG,DOIP,SENDPSN,SENDDATE) values ('")
          			.append(strRunID+"','"+strID+"','"+strSACTIVITY+"','"+strEACTIVITY)
          			.append("','"+strNAME+"','"+strDoUser+"','0','','"+strSENDPSN+"','")
          			.append(strSENDDATE+"')");
	      }
	      userMapper.insertExecSQL(addBuf.toString());
	    }
	    //增加流程任务重分配日志
	    String strName = getFlowRunName(strID); //得到流转流程的名称
	    String strCurrAID = getCurrActivityID(strID); //得到当前步骤的ID
	    String strCurrAName = getActivityName(strCurrAID); //得到当前步骤的名称
	    InsertFlowLog(strUserID, strDOPSN, "", "", "1", strName, strID, strCurrAName);
	    
	    return returnValue;  
	}

	@Override
	public Map<String, Object> getUserList(String field) throws Exception {
		Map<String, Object> resultValue = new HashMap<String, Object>();
	    String strSQL = "Select USERID,NAME From BPIP_USER order by ORBERCODE,USERID";
	    List<Map<String, Object>> retlist = userMapper.selectListMapExecSQL(strSQL);
	    int length = retlist != null ? retlist.size() : 0;
	    if (length > 0) {
	        for (int i = 0; i < length; i++) {
	        	Map<String, Object> retmap = retlist.get(i);
	        	resultValue.put(retmap.get("USERID").toString(), retmap.get("NAME").toString());
	        }
	    }
	    return resultValue;
	}

	@Override
	public FLOW_RUNTIME_PROCESS[] getFlowTransactList(String strUserNo, String type) throws Exception {
	    FLOW_RUNTIME_PROCESS[] returnValue = null;
	    String strSQL = "";
	    String strCid = "";
	    String strFLOWPATH = "";
	    int ListNum = 0; //列表的行数
	    int cNum = 0;    //创建的行数
	    int gnum = 0;
	    String row="8";
	    if (type.equals("1")) {
	       row="8";
	    } else {
	       row="500";
	    }
	    strSQL ="Select count(ID) as num From FLOW_RUNTIME_ACTIVITY where DOFLAG='0' and DOIDEA='send' and DOPSN='"+strUserNo+"'";
	    List<Map<String, Object>> retlist = userMapper.selectListMapExecSQL(strSQL);
	    if (retlist != null && retlist.size() > 0) {
	        gnum = Integer.parseInt(retlist.get(0).get("num").toString());
	    }
	    retlist = null;
	    if (gnum>0) {
	       if (DataBaseType.equals("1")) {//oracle数据库
	          strSQL = "Select a.ID,a.FLOWID,a.FLOWPATH,a.RID,a.CURRACTIVITY,a.ACCEPTDATE,a.FORMID,b.DOIP,b.NAME "
	          		 + "From FLOW_RUNTIME_PROCESS a left join FLOW_RUNTIME_ACTIVITY b on a.ID=b.FID "
	          		 + "where b.DOFLAG='0' and b.DOIDEA='send' and DOPSN='" + strUserNo + "' and rownum<" + row + " order by b.SENDDATE desc";
	       }
	       else if (DataBaseType.equals("2")) {//mssql数据库
	    	   strSQL = "Select top " + row + " a.ID,a.FLOWID,a.FLOWPATH,a.RID,a.CURRACTIVITY,a.ACCEPTDATE,a.FORMID,b.DOIP,b.NAME "
	    	   		  + "From FLOW_RUNTIME_PROCESS a left join FLOW_RUNTIME_ACTIVITY b on a.ID=b.FID "
	    	   		  + "where b.DOFLAG='0' and b.DOIDEA='send' and DOPSN='" + strUserNo + "'  order by b.SENDDATE desc";
	       }
	       else if (DataBaseType.equals("3")) {//MySQL数据库
	    	   strSQL = "Select a.ID,a.FLOWID,a.FLOWPATH,a.RID,a.CURRACTIVITY,a.ACCEPTDATE,a.FORMID,b.DOIP,b.NAME "
	    	   		  + "From FLOW_RUNTIME_PROCESS a left join FLOW_RUNTIME_ACTIVITY b on a.ID=b.FID "
	    	   		  + "where b.DOFLAG='0' and b.DOIDEA='send' and DOPSN='" + strUserNo + "' order by b.SENDDATE desc LIMIT 0,"+row;
	       } else {
	       }
	       retlist = userMapper.selectListMapExecSQL(strSQL);
	    }
	    if (DataBaseType.equals("1")) {//oracle数据库
	        strSQL ="Select ID,FLOWID,FLOWPATH,RID,CURRACTIVITY,ACCEPTDATE,NAME,FORMID "
	        	  + "From FLOW_RUNTIME_PROCESS where STATE<>'2' and STATE<>'3' and STATE<>'4' and ACCEPTPSN like '%"+strUserNo+"%' and rownum<"+row+" order by ID desc";
	    }
	    else if (DataBaseType.equals("2")) {//mssql数据库
	        strSQL ="Select top "+row+" ID,FLOWID,FLOWPATH,RID,CURRACTIVITY,ACCEPTDATE,NAME,FORMID "
	        	  + "From FLOW_RUNTIME_PROCESS where STATE<>'2' and STATE<>'3' and STATE<>'4' and ACCEPTPSN like '%"+strUserNo+"%' order by ID desc";
	    }
	    else if (DataBaseType.equals("3")) {//MySQL数据库
	        strSQL ="Select ID,FLOWID,FLOWPATH,RID,CURRACTIVITY,ACCEPTDATE,NAME,FORMID "
	        	  + "From FLOW_RUNTIME_PROCESS where STATE<>'2' and STATE<>'3' and STATE<>'4' and ACCEPTPSN like '%"+strUserNo+"%' order by ID desc LIMIT 0,"+row;
	    } else {
	    }
	    List<Map<String, Object>> retlist1 = userMapper.selectListMapExecSQL(strSQL);
	    if (retlist1 != null && retlist1.size()>0) {
	        cNum = retlist1.size();
	    }
	    if (retlist != null && retlist.size()>0) {
	        cNum = cNum + retlist.size();
	    }
	    if (cNum > 0) {
	    	returnValue = new FLOW_RUNTIME_PROCESS[cNum];	
	    }
	    try {
	      //正常收到流程的情况----------
	      int length1 = retlist1 != null ? retlist1.size() : 0;
	      if (length1 > 0) {
	          for (int i = 0; i < length1; i++) {
	        	  Map<String, Object> retmap1 = retlist1.get(i);
	              returnValue[ListNum] = (FLOW_RUNTIME_PROCESS) ReflectionUtil.convertMapToBean(retmap1, FLOW_RUNTIME_PROCESS.class);
	              ListNum = ListNum + 1;
	          }
	      }
	      //分送收到流程的情况----------
	      if (retlist != null) {
	    	  int length = retlist.size();
	    	  if (length > 0) {
	             for (int i = 0; i < length; i++) {
	            	 Map<String, Object> retmap = retlist.get(i);
	                 strCid = retmap.get("DOIP").toString();
	                 strFLOWPATH = retmap.get("FLOWPATH").toString();
	                 
	                 strFLOWPATH =strFLOWPATH + "&sendid="+strCid;
	                 retmap.put("FLOWPATH", strFLOWPATH);//在原地址IP后加分送查看的活动ID
	                 retmap.put("RID", "send");//区分是否是分送的记录
	                 
	                 returnValue[ListNum] = (FLOW_RUNTIME_PROCESS) ReflectionUtil.convertMapToBean(retmap, FLOW_RUNTIME_PROCESS.class);
	                 ListNum = ListNum + 1;
	             }
	    	  }
	      }
	      //--------------------------
	    } catch (Exception ex1) {
	    	LOGGER.error("出错getFlowTransactList:\n", ex1);
	    }
	    return returnValue;  
	}

	@Override
	public FLOW_RUNTIME_PROCESS[] getFlowTransactList1(String strFID, String strUserNo) throws Exception {
		FLOW_RUNTIME_PROCESS[] returnValue = null;
	    String strCURRACTIVITY = ""; //当前活动的ID
	    String strSignType = ""; //当前活动的会签类型
	    String strACCEPTPSN = ""; //当前接收人列表
	    String tmpUserNo = ""; //临时人员编号
	    int ListNum = 0; //列表的行数
	    StringBuffer addBuf = new StringBuffer();
	    addBuf.append("Select * From FLOW_RUNTIME_PROCESS where PARENTID='"+strFID)
	          .append("' and STATE<>'2' and STATE<>'3' and STATE<>'4' and ACCEPTPSN like '%")
	          .append(strUserNo+"%' order by ACCEPTDATE desc");
	    List<Map<String, Object>> retlist = userMapper.selectListMapExecSQL(addBuf.toString());
	    addBuf.delete(0, addBuf.length());//清空
	    addBuf.append("Select CURRACTIVITY From FLOW_RUNTIME_PROCESS where PARENTID='"+strFID)
	          .append("' and STATE<>'2' and STATE<>'3' and STATE<>'4' and ACCEPTPSN like '%"+strUserNo)
	          .append("%'");
	    String strWhere =addBuf.toString();
	    Map<String, Object> SignType = new HashMap<String, Object>();//活动/会签对照Hashtable
	    SignType = getSignTypeHashtable(strWhere);
	    int length = retlist != null ? retlist.size() : 0;
	    if (length > 0) {
	        returnValue = new FLOW_RUNTIME_PROCESS[getFlowTransactNum(strFID, strUserNo)];
	        for (int i = 0; i < length; i++) {
	          Map<String, Object> retmap = retlist.get(i);
	          //得到当前活动的ID
	          strCURRACTIVITY = retmap.get("CURRACTIVITY").toString();
	          //得到当前接收人列表
	          strACCEPTPSN = retmap.get("ACCEPTPSN").toString();
	          //得到当前活动的会签类型
	          try {
	            strSignType = (String) SignType.get(strCURRACTIVITY);
	            if (strSignType==null) {strSignType="0";}
	          } catch (Exception ex) {
	            LOGGER.error("错误信息:", ex.toString());
	          }
	          if (strSignType.equals("1")) { //需顺序会签
	            //判断接收人列表中的第一个人是否是本人
	            List<Object> UserList = getArrayList(strACCEPTPSN, ",");
	            tmpUserNo = UserList.get(0).toString();
	            if (tmpUserNo.equals(strUserNo)) { //是本人时显示待办流程，否则待前面的人处理完后才收到。
	              returnValue[ListNum] = (FLOW_RUNTIME_PROCESS) ReflectionUtil.convertMapToBean(retmap, FLOW_RUNTIME_PROCESS.class);
	              ListNum = ListNum + 1;
	            }
	            //-------------------------------------
	          } else { //不需要顺序会签
	            returnValue[ListNum] = (FLOW_RUNTIME_PROCESS) ReflectionUtil.convertMapToBean(retmap, FLOW_RUNTIME_PROCESS.class);
	            ListNum = ListNum + 1;
	          }
	        }
	    }
	    return returnValue;
	}

	@Override
	public FLOW_RUNTIME_PROCESS[] getFlowTransactParentList(String strUserNo) throws Exception {
	    FLOW_RUNTIME_PROCESS[] returnValue = null;
	    StringBuffer  addBuf = new   StringBuffer();
	    addBuf.append("Select distinct PARENTID From FLOW_RUNTIME_PROCESS where STATE<>'2' and STATE<>'3' "
	    			+ "and STATE<>'4' and ACCEPTPSN like '%").append(strUserNo+"%' order by PARENTID");
	    List<Map<String, Object>> retlist = userMapper.selectListMapExecSQL(addBuf.toString());
	    int length = retlist != null ? retlist.size() : 0;
	    if (length > 0) {
	          returnValue = new FLOW_RUNTIME_PROCESS[length];
	          for (int i = 0; i < length; i++) {
	        	  Map<String, Object> retmap = retlist.get(i);
	              returnValue[i] = (FLOW_RUNTIME_PROCESS) ReflectionUtil.convertMapToBean(retmap, FLOW_RUNTIME_PROCESS.class);
	          }
	    }
	    return returnValue;  
	}

	@Override
	public String getCSendUser(String strAID, String FID) throws Exception {
	    String resultValue = "";
	    String isSend = "0";
	    StringBuffer addBuf = new StringBuffer();
	    addBuf.append("Select b.NAME From FLOW_RUNTIME_ACTIVITY a left join BPIP_USER b on a.SENDPSN=b.USERID "
	    			+ "where a.FID='"+FID+"' And a.EACTIVITY='"+strAID+"' and a.DOFLAG='0'");
	    List<Map<String, Object>> retlist = userMapper.selectListMapExecSQL(addBuf.toString());
	    int length = retlist != null ? retlist.size() : 0;
	    if (length > 0) {
	    	if (retlist.get(0) != null && retlist.get(0).get("NAME") != null) {
	    		resultValue = retlist.get(0).get("NAME").toString();
	    	}
	        isSend = "1";
	    }
	    if (isSend.equals("0"))//没有找到（一般为退回时的情况）
	    {
	      addBuf.delete(0,addBuf.length());//清空
	      addBuf.append("Select b.NAME From FLOW_RUNTIME_ACTIVITY  a left join BPIP_USER b on a.DOPSN=b.USERID where a.FID='")
	            .append(FID+"' And a.SACTIVITY='"+strAID+"' order by ID desc");
	      List<Map<String, Object>> retlist1 = userMapper.selectListMapExecSQL(addBuf.toString());
		  int length1 = retlist1 != null ? retlist1.size() : 0;
	      if (length1 > 0) {
	    	  if (retlist1.get(0) != null && retlist1.get(0).get("NAME") != null) {
	    		  resultValue = retlist1.get(0).get("NAME").toString();
	    	  }
	      }
	    }
	    return resultValue;
	}

	@Override
	public Map<String, Object> getSendUserHashtable1(String strUserID) throws Exception {
	      String strFID="";
	      String strEACTIVITY="";
	      String strNAME="";
	      Map<String, Object> NameHashtable = new HashMap<String, Object>();
	      StringBuffer addBuf = new StringBuffer();
	      if (DataBaseType.equals("1")) {// Oracle数据库
	    	  addBuf.append("Select a.FID,a.EACTIVITY,b.NAME From FLOW_RUNTIME_ACTIVITY a left join BPIP_USER b on a.SENDPSN=b.USERID "
		  			  + "where rownum<500 and a.DOPSN='"+strUserID+"' and a.DOFLAG='0'");
	      }
	      else if (DataBaseType.equals("2")) {//MSSQL数据库
			  addBuf.append("Select top 500 a.FID,a.EACTIVITY,b.NAME From FLOW_RUNTIME_ACTIVITY a left join BPIP_USER b on a.SENDPSN=b.USERID "
			  			  + "where a.DOPSN='"+strUserID+"' and a.DOFLAG='0'");
		  }
	      else if (DataBaseType.equals("3")) {
			  addBuf.append("Select a.FID,a.EACTIVITY,b.NAME From FLOW_RUNTIME_ACTIVITY a left join BPIP_USER b on a.SENDPSN=b.USERID "
			  			  + "where a.DOPSN='"+strUserID+"' and a.DOFLAG='0' limit 500");
		  }
		  List<Map<String, Object>> retlist = userMapper.selectListMapExecSQL(addBuf.toString());
		  int length = retlist != null ? retlist.size() : 0;
	      if (length > 0) {
	          for (int i=0;i<length;i++) {
	        	  Map<String, Object> retmap = retlist.get(i);
	        	  CollectionUtil.convertMapNullToStr(retmap);
	              strFID = retmap.get("FID").toString();
	              strEACTIVITY = retmap.get("EACTIVITY").toString();
	              strNAME = retmap.get("NAME").toString();
	              addBuf.delete(0,addBuf.length());//清空
	              addBuf.append(strFID+strEACTIVITY);
	              NameHashtable.put(addBuf.toString(),strNAME);
	          }
	      }
	      return NameHashtable;
	}

	@Override
	public Map<String, Object> getSendUserHashtable2(String strUserID) throws Exception {
		String strFID = "";
        String strFIDS = "";
        String strCS = "";
        String strSACTIVITY = "";
        String strNAME = "";
        Map<String, Object> NameHashtable = new HashMap<String, Object>();
        StringBuffer addBuf = new StringBuffer();
        if (DataBaseType.equals("1")) {
        	addBuf.append("Select a.FID,a.SACTIVITY,b.NAME From FLOW_RUNTIME_ACTIVITY a left join BPIP_USER b on a.DOPSN=b.USERID "
					+ "where rownum<500 and a.DOPSN='"+strUserID+"' order by a.ID desc");
        }
        else if (DataBaseType.equals("2")) {//MSSQL数据库
			addBuf.append("Select top 500 a.FID,a.SACTIVITY,b.NAME From FLOW_RUNTIME_ACTIVITY a left join BPIP_USER b on a.DOPSN=b.USERID "
						+ "where a.DOPSN='"+strUserID+"' order by a.ID desc");
		}
        else if (DataBaseType.equals("3")) {
			addBuf.append("Select a.FID,a.SACTIVITY,b.NAME From FLOW_RUNTIME_ACTIVITY a left join BPIP_USER b on a.DOPSN=b.USERID "
						+ "where a.DOPSN='"+strUserID+"' order by a.ID desc limit 500");
		}
		List<Map<String, Object>> retlist = userMapper.selectListMapExecSQL(addBuf.toString());
		int length = retlist != null ? retlist.size() : 0;
        if (length > 0) {
            for (int i=0; i<length; i++) {
            	Map<String, Object> retmap = retlist.get(i);
            	CollectionUtil.convertMapNullToStr(retmap);
                strFID = retmap.get("FID").toString();
                strSACTIVITY = retmap.get("SACTIVITY").toString();
                strNAME = retmap.get("NAME").toString();
                addBuf.delete(0,addBuf.length());//清空
                addBuf.append(strFID+strSACTIVITY);
                strCS=addBuf.toString();
                if (strFIDS.indexOf(strCS)==-1) {
                  NameHashtable.put(strCS,strNAME);
                  addBuf.delete(0,addBuf.length());//清空
                  addBuf.append(strFIDS+strCS+",");
                  strFIDS = addBuf.toString();
                }
            }
        }
        return NameHashtable;
	}

	@Override
	public String getUserAllotType(String strUserID, String strID) throws Exception {
	      String returnValue = "1";
	      String strFLOWID = "";
	      //得到当前流转流程的流程标识
	      StringBuffer  addBuf = new   StringBuffer();
	      addBuf.append("select FLOWID from FLOW_RUNTIME_PROCESS where ID='"+strID+"'");
	      Map<String, Object> retmap = userMapper.selectMapExecSQL(addBuf.toString());
	      if (retmap != null && retmap.size() > 0) {
	    	  strFLOWID = retmap.get("FLOWID").toString();
	      }
	      //-----------
	      //查找当前用户是否有对本流程有全单位的任务重分配权限
	      addBuf.delete(0,addBuf.length());//清空
          addBuf.append("select PROSESSID from FLOW_CONFIG_PROSESS_GROUP where ISDEPT='0' and TYPE='2' and PROSESSID in (select ID from FLOW_CONFIG_PROCESS where IDENTIFICATION='")
                .append(strFLOWID+"') and GROUPID in (select ROLEID from BPIP_USER_ROLE where USERID='"+strUserID)
                .append("')");
          List<Map<String, Object>> retlist2 = userMapper.selectListMapExecSQL(addBuf.toString());
	      if (retlist2 != null) {
	         if (retlist2.size() > 0){
	             returnValue = "2";
	         }
	      }
	      return returnValue;
	}

	@Override
	public FLOW_RUNTIME_PROCESS[] getFlowTransactList2(String strUserNo, 
			int currentPage, int PageSize, String strwhere) throws Exception {
	    FLOW_RUNTIME_PROCESS[] returnValue = null;
	    String strSQL = "";
	    String strCURRACTIVITY = ""; //当前活动的ID
	    String strSignType = ""; //当前活动的会签类型
	    String strACCEPTPSN = ""; //当前接收人列表
	    String tmpUserNo = ""; //临时人员编号
	    int ListNum = 0; //列表的行数
	    StringBuffer addBuf = new StringBuffer();
//	    String pageCount = "0";
	    if (DataBaseType.equals("3")) {//MySQL数据库
	      addBuf.append("Select B.ID,B.NAME,B.CREATEDATE,B.CURRACTIVITY,B.ACCEPTPSN,B.ACCEPTDATE "
	      			  + "From FLOW_RUNTIME_PROCESS B Left Join BPIP_USER C on B.CREATEPSN=C.USERID "
	      			  + "where B.STATE<>'2' and B.STATE<>'3' and B.STATE<>'4' and B.ACCEPTPSN like '%");
	      addBuf.append(strUserNo+"%' and "+strwhere+" order by B.ACCEPTDATE desc");
	      strSQL = addBuf.toString();
	      addBuf.delete(0,addBuf.length());//清空
	      int offset = (currentPage - 1) * PageSize;
	      if (offset < 0) { offset = 0; }
	      addBuf.append(strSQL+" limit "+offset+","+PageSize);
	    } else {
	      addBuf.append("Select B.ID,B.NAME,B.CREATEDATE,B.CURRACTIVITY,B.ACCEPTPSN,B.ACCEPTDATE,C.NAME AS CREATEPSN "
	      			  + "From FLOW_RUNTIME_PROCESS B Left Join BPIP_USER C on B.CREATEPSN=C.USERID "
	      			  + "where B.STATE<>'2' and B.STATE<>'3' and B.STATE<>'4' and B.ACCEPTPSN like '%");
	      addBuf.append(strUserNo+"%' and "+strwhere+" order by B.ACCEPTDATE desc");
	      strSQL = addBuf.toString();
	      addBuf.delete(0, addBuf.length());//清空 
	      if (DataBaseType.equals("1")) {// Oracle数据库
	    	 addBuf.append("Select T.* From (SELECT A.*, ROWNUM RN FROM ("+strSQL+") AS A WHERE ROWNUM <= ")
		     	   .append(currentPage*PageSize+") AS T WHERE RN > "+(currentPage-1)*PageSize);
	      }
	      else if (DataBaseType.equals("2")) {// MSSQL数据库 
	    	 addBuf.append("Select Top "+PageSize+" T.* From (SELECT ROW_NUMBER() OVER(ORDER BY ID) AS MSROWNUM, A.* FROM ("+strSQL+") AS A) AS T WHERE MSROWNUM > ")
	     	   	   .append((currentPage-1)*PageSize);
	      }
	    }
	    List<Map<String, Object>> dbsetCount = userMapper.selectListMapExecSQL(strSQL.toString());
	    List<Map<String, Object>> dbset = userMapper.selectListMapExecSQL(addBuf.toString());
	    List<Map<String, Object>> dbset1 = null;
	    if (DataBaseType.equals("3")) {//MySQL
	       addBuf.delete(0,addBuf.length());//清空
	       addBuf.append("Select C.NAME From FLOW_RUNTIME_PROCESS B Left Join BPIP_USER C on B.CREATEPSN=C.USERID where B.STATE<>'2' and B.STATE<>'3' and B.STATE<>'4' and B.ACCEPTPSN like '%");
	       addBuf.append(strUserNo+"%' and "+strwhere+" order by B.ACCEPTDATE desc");
	       strSQL = addBuf.toString();
	       addBuf.delete(0,addBuf.length());//清空
	       int offset = (currentPage - 1) * PageSize;
	       if (offset < 0) { offset = 0; }
	       addBuf.append(strSQL+" limit "+String.valueOf(offset)+","+PageSize);
	       dbset1 = userMapper.selectListMapExecSQL(addBuf.toString());
	    }
	    addBuf.delete(0,addBuf.length());//清空
	    addBuf.append("Select CURRACTIVITY From FLOW_RUNTIME_PROCESS  where STATE<>'2' and STATE<>'3' and STATE<>'4' and ACCEPTPSN like '%");
	    addBuf.append(strUserNo+"%'");
	    String strWhere = addBuf.toString();
	    
	    try {
	      Map<String, Object> SignType = getSignTypeHashtable(strWhere);//活动/会签对照Hashtable
	      if (dbset != null && dbset.size() > 0) {
	          returnValue = new FLOW_RUNTIME_PROCESS[getFlowTransactNum2(strUserNo,currentPage,PageSize)];
	          for (int i = 0; i < dbset.size(); i++) {
	            //得到当前活动的ID
	            strCURRACTIVITY = dbset.get(i).get("CURRACTIVITY").toString();
	            //得到当前接收人列表
	            strACCEPTPSN = dbset.get(i).get("ACCEPTPSN").toString();
	            //得到当前活动的会签类型
	            try {
	              strSignType = (String) SignType.get(strCURRACTIVITY);
	              if (strSignType == null) {
	                strSignType = "0";
	              }
	            }
	            catch (Exception ex) {
	              LOGGER.error("getFlowTransactList2错误信息:", ex);
	            }
	            if (strSignType.equals("1")) { //需顺序会签
	              //判断接收人列表中的第一个人是否是本人
	              List<Object> UserList = getArrayList(strACCEPTPSN, ",");
	              tmpUserNo = UserList.get(0).toString();
	              if (tmpUserNo.equals(strUserNo)) { //是本人时显示待办流程，否则待前面的人处理完后才收到。
	                returnValue[ListNum] = (FLOW_RUNTIME_PROCESS) ReflectionUtil.convertMapToBean(dbset.get(i), FLOW_RUNTIME_PROCESS.class);
	                if (DataBaseType.equals("3")) {//MySQL
	                    returnValue[ListNum].setCREATEPSN(dbset1.get(i).get("NAME").toString());
	                }
	                ListNum = ListNum + 1;
	              }
	              //-------------------------------------
	            } else { //不需要顺序会签
	              returnValue[ListNum] = (FLOW_RUNTIME_PROCESS) ReflectionUtil.convertMapToBean(dbset.get(i), FLOW_RUNTIME_PROCESS.class);
	              if (DataBaseType.equals("3")) {//MySQL
	                    returnValue[ListNum].setCREATEPSN(dbset1.get(i).get("NAME").toString());
	              }
	              ListNum = ListNum + 1;
	            }
	          }
	      }
	    } catch (Exception ex1) {
	    	ex1.printStackTrace();
	    }
	    if (returnValue != null) {
	    	GetFlowTransactList2_pageCount = dbsetCount.size()+"";
	    }
	    return returnValue;
	}

	@Override
	public String sumFlowcontent(String strUserNo) throws Exception {
	    String sum = "0";
	    StringBuffer addBuf = new StringBuffer();
	    addBuf.append("Select count(ID) as count From FLOW_RUNTIME_PROCESS where STATE not in('2','3','4') "
	    			+ "and ACCEPTPSN like '%").append(strUserNo+"%'");
	    List<Map<String, Object>> retlist = userMapper.selectListMapExecSQL(addBuf.toString());
	    if (retlist != null && retlist.size() > 0) {
	    	if (retlist.get(0) != null && retlist.get(0).get("count") != null) {
	    		sum = retlist.get(0).get("count").toString();
	    	}
	    }
	    return sum;
	}

	@Override
	public String createPageNavigateBy(String strUserID, int currentPage, int pageSize, String type, String strwhere)
			throws Exception {
	    LOGGER.info("createPageNavigateBy开始调用...");
	    long startTime = System.currentTimeMillis();
	    String resultStr = "";
	    int intRowCount;
	    int _CurrentPage; //当前页号,查询分页时使用
	    String strTMP = "";
	    String strFLOW1 = "";//可分配的流程串
	    String strROLEID="";
	    String strSql = "";
	    //得到本部门下的人员串
	    //流程ID与流程标识对照表
	    Map<String, Object> FlowHashtable = new HashMap<String, Object>();
	    //本单位正在运转的流程
	    strSql = "select ID,IDENTIFICATION from FLOW_CONFIG_PROCESS";
	    List<Map<String, Object>> retlist1 = userMapper.selectListMapExecSQL(strSql);
	    int length1 = retlist1 != null ? retlist1.size() : 0;
	    if (length1 > 0) {
	         for (int i=0; i<length1; i++) {
	        	 Map<String, Object> retmap1 = retlist1.get(i);
	             FlowHashtable.put(retmap1.get("ID").toString(), retmap1.get("IDENTIFICATION").toString());
	         }
	    }
        //查找当前用户是只能监控本部门的流程
        strSql = "select ROLEID from BPIP_USER_ROLE where USERID='"+strUserID+"'";
        List<Map<String, Object>> db = userMapper.selectListMapExecSQL(strSql);
        int dblen = db != null ? db.size() : 0;
        if (dblen > 0) {
           for (int i=0; i<dblen; i++) {
        	   Map<String, Object> dbmap = db.get(i);
                if (i==0) {
                   strROLEID = "'"+dbmap.get("ROLEID").toString()+"'";
                } else {
                   strROLEID = strROLEID + ",'"+dbmap.get("ROLEID").toString()+"'";
                }
             }
        }
        strSql = "select PROSESSID from FLOW_CONFIG_PROSESS_GROUP where ISDEPT='1' and TYPE='"+type+"' and GROUPID in ("+strROLEID+")";
        //String strATMP = "";
        List<Map<String, Object>> dbset2 = userMapper.selectListMapExecSQL(strSql);
        int dbset2len = dbset2 != null ? dbset2.size() : 0;
	    if (dbset2len > 0) {
	         for (int i=0; i<dbset2len; i++) {
	        	 Map<String, Object> dbset2map = dbset2.get(i);
	             strTMP = dbset2map.get("PROSESSID").toString();
	             strTMP = (String) FlowHashtable.get(strTMP);
	             strFLOW1 = strFLOW1 + "'"+strTMP+"',";
	         }
	    }
	    //查找当前用户是监控所有的流程
	    strSql = "select PROSESSID from FLOW_CONFIG_PROSESS_GROUP where ISDEPT='0' and TYPE='"+type+"' and GROUPID in ("+strROLEID+")";
	    List<Map<String, Object>> dbset3 = userMapper.selectListMapExecSQL(strSql);
	    int dbset3len = dbset3 != null ? dbset3.size() : 0;
	    if (dbset3len > 0) {
	         for (int i=0; i<dbset3len; i++) {
	        	 Map<String, Object> dbset3map = dbset3.get(i);
	        	 strTMP = dbset3map.get("PROSESSID").toString();
	             strTMP = (String) FlowHashtable.get(strTMP);
	             strFLOW1 = strFLOW1 +"'"+strTMP+"',";
	         }
	    }
	    if (strFLOW1.length()>0) {
	      strFLOW1 = strFLOW1.substring(0,strFLOW1.length()-1);
	    }
	    if (strFLOW1.length()>0) {
	      strSql ="Select count(ID) as RCount From FLOW_RUNTIME_PROCESS where STATE<>'2' "
	      		+ "and STATE<>'3' and STATE<>'4' and FLOWID in ("+strFLOW1+") and "+strwhere;
	      _CurrentPage = currentPage;
	      List<Map<String, Object>> ds = userMapper.selectListMapExecSQL(strSql);
	      int dslen = ds != null ? ds.size() : 0;
	      if (dslen == 0) {
	        intRowCount = 0;
	      } else {// dslen > 0
	        intRowCount = Integer.parseInt(ds.get(0).get("RCount").toString());
	      }
	      resultStr = createPageMenu(pageSize, intRowCount, _CurrentPage);
	      
	      long endTime = System.currentTimeMillis();
	      LOGGER.info("createPageNavigateBySql执行完成，耗时：" + (endTime - startTime) + " ms.");
	    }
	    return resultStr;  
	}

	@Override
	public String createPageNavigateBySql(String sqlStr, int currentPage, int pageSize) throws Exception {
	      LOGGER.info("createPageNavigateBySql开始调用...");
	      long startTime = System.currentTimeMillis();
	      String resultStr = "";
	      int intRowCount;
	      int _CurrentPage; //当前页号,查询分页时使用
	      // sqlStr 格式为 SELECT  count(*) as RowCount Where ...
	      _CurrentPage = currentPage;
	      DBSet dbset = dbEngine.QuerySQL(sqlStr);
	      if (dbset == null) {
	          intRowCount = 0;
	      } else {// dbset != null
	          intRowCount = dbset.Row(0).Column("RCount").getInteger();
	          dbset=null;//赋空值
	      }
	      resultStr = createPageMenu(pageSize, intRowCount, _CurrentPage);
	      long endTime = System.currentTimeMillis();
	      LOGGER.info("createPageNavigateBySql执行完成，耗时：" + (endTime - startTime) + " ms.");
	      return resultStr;  
	}

	/**
	 * 功能或作用：分析规则字符串，生成数组(不带后分隔符)
	 * @param strItems 字符串
	 * @param strItemMark 标识符
	 * @return 返回数组
	 */
	private List<Object> getArrayList(String strItems, String strItemMark) {
		int intItemLen, i = 0, n = 0;
		strItems = strItems + strItemMark;
		String strItem;
		List<Object> strList = new ArrayList<Object>();
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
	 * 功能：判断流程的处理人是否包括于本部门的人员中
	 * @param strDo 流程处理人
	 * @param strUsers 本部门的人员串
	 * @return
	 */
	private boolean IsMatchDept(String strDo,String strUsers) {
		boolean returnValue = false;
		String strTmp ="";
		List<Object> arrList1 = new ArrayList<Object>();
		arrList1 = getArrayList(strDo,",");
		for (int i = 0; i < arrList1.size(); i++) {
			strTmp = arrList1.get(i).toString();
			if (strUsers.lastIndexOf(strTmp) > -1) {//是本部门的人员
				returnValue = true;
                break;
			}
		}
		return returnValue;  
	}

	/**
	 * 得到流程运转步骤表的ID
	 * @return
	 */
	private String getFLOW_RUNTIME_ACTIVITY_ID() {
       String strValue ="";
       String strNum ="";
       try {
           strNum = myGetRandom(4);
       } catch (Exception ex) {
           strNum = "0";
       }
       strValue = "S"+globalID1()+strNum;
       int znum = 20-strValue.length();
       for (int k=0; k<znum; k++) {
           strValue = strValue + "0";
       }
       if (strValue.length()>20) {
          strValue = strValue.substring(0,20);
       }
       return strValue; 
	}

	private String myGetRandom(int i) {
		if (i == 0) { return ""; }
		String revalue = "";
		for (int k = 0; k < i; k++) {
			revalue = revalue + RANDOM.nextInt(9);
		}
		return revalue;
	}

	/**
	 * 加入同步锁，多用户使用时保证FLOW_RUNTIME_ACTIVITY_ID唯一性
	 * @return
	 */
	private synchronized String globalID1() {
		String strResult = "";
		try {
			Date dt = new Date();
			long lg = dt.getTime();
			Long ld = new Long(lg);
			strResult = ld.toString();
		} catch (Exception ex) {
			strResult ="";
		}
		return strResult;
	}

	/**
	 * 功能或作用：取出最大的记录流水号
	 * @param TableName 数据库表名
	 * @param FieldName 数据库字段名称
	 * @param FieldLen 数据库字段长度
	 * @Return MaxNo 执行后返回一个MaxNo字符串
	 */
	private String getMaxFieldNo(String TableName, String FieldName, int FieldLen) {
	    String MaxNo = "";
	    int LenMaxNo = 0;
	    StringBuffer addBuf = new StringBuffer();
	    addBuf.append("SELECT MAX("+FieldName+") AS MaxNo FROM "+TableName);
	    try {
	    	DBSet dbset = dbEngine.QuerySQL(addBuf.toString());
	    	if (dbset != null && dbset.RowCount() > 0) {
    			MaxNo = dbset.Row(0).Column("MaxNo").getString();
    			if (MaxNo != null && MaxNo.length() > 0) {
		            MaxNo = String.valueOf(Integer.parseInt(MaxNo) + 1);
		            LenMaxNo = MaxNo.length();
		            addBuf.delete(0,addBuf.length());//清空
		            addBuf.append("0000000000000000000000000"+MaxNo);
		            MaxNo = addBuf.toString();
    			} else {
    				MaxNo = "00000000000000000000000001";
    				LenMaxNo = 1;
    			}
	    		dbset=null;//赋空值
	    	}
	    	MaxNo = MaxNo.substring(25 - FieldLen + LenMaxNo);
	    } catch (Exception ex) {
	    	ex.printStackTrace();
	    }
	    return MaxNo;
	}

	/**
	 * 功能：根据用户编号字符串生成名称字符串
	 * @param strString 字符串
	 * @return returnValue 返回分支选择列表
	 * @throws Exception 
	 */ 
	@SuppressWarnings("unused")
	private String getArrList(String strString) throws Exception {
	    String strNode_S_No = "", strNode_S_Name = "";
	    String returnValue = "";
	    if (strString.trim().length() == 0) {
	    	return "";
	    }
	    if (strString.trim().length() == 16) {
	    	strNode_S_Name = getUserName(strString);
	    	return strNode_S_Name;
	    }
	    int i = 0;
	    List<Object> arrList_strNode_No = getArrayList(strString, ",");
	    for (i = 0; i < arrList_strNode_No.size(); i++) {
	    	strNode_S_No = arrList_strNode_No.get(i).toString();
	    	strNode_S_Name = getUserName(strNode_S_No);
	    	returnValue = returnValue + strNode_S_Name + ",";
	    }
	    if (returnValue.length() > 0) {
	    	returnValue = returnValue.substring(0, returnValue.length() - 1);
	    }
	    return returnValue;
	}

	/**
	 * 功能:获取人员列表
	 * @param userId String
	 * @return String
	 * @throws Exception 
	 */  
	private String getUserName(String userId) throws Exception {
	    String resultValue = "";
	    StringBuffer addBuf = new StringBuffer();
	    addBuf.append("Select NAME From BPIP_USER where USERID='"+userId.trim()+"'");
	    Map<String, Object> retmap = userMapper.selectMapExecSQL(addBuf.toString());
	    addBuf.delete(0,addBuf.length());//清空
	    if (retmap != null && retmap.size() > 0) {
	        addBuf.append(retmap.get("NAME").toString());
	    }
	    return resultValue;  
	}

	/**
	 * 功能:得到某父类的待办流程的数量
	 * @param strFID父类编号
	 * @param strUserNo 当前用户编号
	 * @return returnValue 返回待办流程的数量
	 * @throws Exception 
	 */
	private int getFlowTransactNum(String strFID, String strUserNo) throws Exception {
	    int returnValue = 0;
	    String strCURRACTIVITY = ""; //当前活动的ID
	    String strSignType = ""; //当前活动的会签类型
	    String strACCEPTPSN = ""; //当前接收人列表
	    String tmpUserNo = ""; //临时人员编号
	    StringBuffer addBuf = new StringBuffer();
	    addBuf.append("Select CURRACTIVITY,ACCEPTPSN From FLOW_RUNTIME_PROCESS where PARENTID='"+strFID)
	          .append("' and STATE<>'2' and STATE<>'3' and STATE<>'4' and ACCEPTPSN like '%"+strUserNo+"%'");
	    DBSet dbset = dbEngine.QuerySQL(addBuf.toString());
	    
	    addBuf.delete(0,addBuf.length());//清空
	    addBuf.append("Select CURRACTIVITY From FLOW_RUNTIME_PROCESS where PARENTID='"+strFID)
	          .append("' and STATE<>'2' and STATE<>'3' and STATE<>'4' and ACCEPTPSN like '%"+strUserNo)
	          .append("%'");
	    String strWhere = addBuf.toString();
	    Map<String, Object> SignType = new HashMap<String, Object>();//活动/会签对照HashMap
	    SignType = getSignTypeHashtable(strWhere);
	    if (dbset != null) {
	      if (dbset.RowCount() > 0) {
	        for (int i = 0; i < dbset.RowCount(); i++) {
	          //得到当前活动的ID
	          strCURRACTIVITY = dbset.Row(i).Column("CURRACTIVITY").getString();
	          //得到当前接收人列表
	          strACCEPTPSN = dbset.Row(i).Column("ACCEPTPSN").getString();
	          //得到当前活动的会签类型
	          try {
	        	  strSignType = (String) SignType.get(strCURRACTIVITY);
	        	  if (strSignType==null) {strSignType="0";}
	          } catch (Exception ex) {
	        	  LOGGER.error("错误信息:", ex.toString());
	          }
	          if (strSignType.equals("1")) {//需顺序会签
	            //判断接收人列表中的第一个人是否是本人
	            List<Object> UserList = getArrayList(strACCEPTPSN, ",");
	            tmpUserNo = UserList.get(0).toString();
	            if (tmpUserNo.equals(strUserNo)) {//是本人时显示待办流程，否则待前面的人处理完后才收到。
	            	returnValue = returnValue + 1;
	            }
	            //-------------------------------------
	          } else { //不需要顺序会签
	            returnValue = returnValue + 1;
	          }
	        }
	      }
	      dbset=null;//赋空值
	    }
	    return returnValue;  
	}

	/**
	 * 功能：根据指定条件得到活动/是否会签对照表
	 * @param strWhere 条件
	 * @return 活动/是否会签对照表
	 * @throws Exception 
	 */
	private Map<String, Object> getSignTypeHashtable(String strWhere) throws Exception {
	    Map<String, Object> SignType = new HashMap<String, Object>();
	    String strID = "";
	    String strISSIGN = "";
	    StringBuffer addBuf = new StringBuffer();
	    addBuf.append("select ID,ISSIGN from FLOW_CONFIG_ACTIVITY where ID in ("+strWhere+")");
	    List<Map<String, Object>> retlist = userMapper.selectListMapExecSQL(addBuf.toString());
	    int length = retlist != null ? retlist.size() : 0;
	    if (length > 0) {
	        for(int i = 0; i < length; i++) {
	        	Map<String, Object> retmap = retlist.get(i);
	        	strID = retmap.get("ID").toString();
	        	strISSIGN = retmap.get("ISSIGN").toString();
	        	if (strISSIGN==null) {strISSIGN="0";}
	        	SignType.put(strID,strISSIGN);
	        }
	    }
	    return SignType;	  
	}

	/**
	 * 功能：增加流程操作日志
	 * @param strSUSERNO 委托人编号
	 * @param strIUSERNO 受委托人编号
	 * @param strSDATE 委托开始时间
	 * @param strEDATE 委托结束时间
	 * @param strTYPE 委托类型
	 * @param strFLOWNAME 委托流程名称
	 * @param strFLOWID 委托流程编号
	 * @param strFLOWNODE 委托流程步骤
	 * @param strUNIT 单位编号
	 * @return returnValue 返回执行情况
	 * @throws Exception 
	 */
	private boolean InsertFlowLog(String strSUSERNO, String strIUSERNO, String strSDATE, String strEDATE, 
			String strTYPE, String strFLOWNAME, String strFLOWID, String strFLOWNODE) throws Exception {
	    String tmpUserNo = "";
	    String strMaxID = "";
	    StringBuffer addBuf = new StringBuffer();
	    List<Object> UserList = getArrayList(strIUSERNO, ",");
	    for (int i = 0; i < UserList.size(); i++) {
	      tmpUserNo = UserList.get(i).toString();
	      strMaxID = getMaxFieldNo("FLOW_RUNTIME_ENTRUSTLOG", "ID", 8);
	      addBuf.delete(0,addBuf.length());//清空
	      addBuf.append("Insert into FLOW_RUNTIME_ENTRUSTLOG(ID,SUSERNO,IUSERNO,SDATE,EDATE,TYPE,FLOWNAME,FLOWID,FLOWNODE,LOGDATE) values ('")
	            .append(strMaxID+"','"+strSUSERNO+"','"+tmpUserNo+"'");
	      
	      if (strSDATE.length() > 0) {
	    	  //数据库类型，1：Oracle，2：MSSQL，3：MySQL
	    	  if (DataBaseType.equals("1")) {//Oracle数据库
	    		  addBuf.append(",to_date('"+strSDATE+"','yyyy-mm-dd')");
	    	  } else if (DataBaseType.equals("2")) {//MSSQL数据库
	    		  addBuf.append(",'"+strSDATE+"'");
	    	  } else {// DataBaseType=3，MySQL数据库
	    		  addBuf.append(",'"+strSDATE+"'");
	    	  }
	      } else {
	    	addBuf.append(",sysdate()");
	      }
	      if (strEDATE.length() > 0) {
	          //数据库类型，1：Oracle，2：MSSQL，3：MySQL
	    	  if (DataBaseType.equals("1")) {//Oracle数据库
	    		  addBuf.append(",to_date('"+strEDATE+"','yyyy-mm-dd')");
	    	  } else if (DataBaseType.equals("2")) {//MSSQL数据库
	    		  addBuf.append(",'"+strEDATE+"'");
	    	  } else {// DataBaseType=3，MySQL数据库
	    		  addBuf.append(",'"+strEDATE+"'");
	    	  }
	      } else {
	        addBuf.append(",sysdate()");
	      }
	      addBuf.append(",'"+strTYPE+"','"+strFLOWNAME+"','"+strFLOWID)
	            .append("','"+strFLOWNODE+"',sysdate())");
	      
	      userMapper.insertExecSQL(addBuf.toString());
	    }
	    return true;
	}

	/**
	 * 功能：根据流程流转表的ID得到当前活动(当前步骤)的ID
	 * @param strID流程流转表的ID
	 * @return returnValue 返回当前活动(当前步骤)的ID
	 * @throws Exception 
	 */
	private String getCurrActivityID(String strID) throws Exception {
	    StringBuffer addBuf = new StringBuffer();
	    addBuf.append("Select CURRACTIVITY From FLOW_RUNTIME_PROCESS where ID='"+strID+"'");
	    Map<String, Object> retmap = userMapper.selectMapExecSQL(addBuf.toString());
	    addBuf.delete(0,addBuf.length());//清空
	    if (retmap != null && retmap.size() > 0) {
	        addBuf.append(retmap.get("CURRACTIVITY").toString());
	    }
	    return addBuf.toString();  
	}

	/**
	 * 功能：根据流程流转表的ID得到流转的名称
	 * @param strID流程流转表的ID
	 * @return returnValue 返回流转的名称
	 * @throws Exception 
	 */
	private String getFlowRunName(String strID) throws Exception {
	    StringBuffer addBuf = new StringBuffer();
	    addBuf.append("Select NAME From FLOW_RUNTIME_PROCESS where ID='"+strID+"'");
	    Map<String, Object> retmap = userMapper.selectMapExecSQL(addBuf.toString());
	    addBuf.delete(0,addBuf.length());//清空
	    if (retmap != null && retmap.size() > 0) {
	        addBuf.append(retmap.get("NAME").toString());
	    }
	    return addBuf.toString();
	}

	/**
	 * 功能：根据活动ID得到活动名称
	 * @param strID活动ID
	 * @return returnValue 返回活动名称
	 * @throws Exception 
	 */
	private String getActivityName(String strID) throws Exception {
	    StringBuffer addBuf = new StringBuffer();
	    addBuf.append("Select NAME From FLOW_CONFIG_ACTIVITY where ID='"+strID+"'");
	    Map<String, Object> retmap = userMapper.selectMapExecSQL(addBuf.toString());
	    addBuf.delete(0,addBuf.length());//清空
	    if (retmap != null && retmap.size() > 0) {
	        addBuf.append(retmap.get("NAME").toString());
	    }
	    return addBuf.toString();
	}

	/**
	 * 功能:得到待办流程的数量
	 * @param strUserNo 当前用户编号
	 * @return returnValue 返回待办流程的数量
	 * @throws Exception 
	 */
	private int getFlowTransactNum2(String strUserNo, int currentPage, int PageSize) throws Exception {
	     int returnValue = 0;
	     String strSQL = new String();
	     String strCURRACTIVITY = ""; //当前活动的ID
	     String strSignType = ""; //当前活动的会签类型
	     String strACCEPTPSN = ""; //当前接收人列表
	     String tmpUserNo = ""; //临时人员编号
	     StringBuffer addBuf = new StringBuffer();
	     
	     addBuf.append("Select * From FLOW_RUNTIME_PROCESS where STATE<>'2' and STATE<>'3' and STATE<>'4' and ACCEPTPSN like '%"+strUserNo+"%'");
	     strSQL = addBuf.toString();
	     addBuf.delete(0, addBuf.length());//清空 
	     if (currentPage <= 0) {
	    	 currentPage = 1;// 初始化页码
	     }
	     if (DataBaseType.equals("1")) {// Oracle数据库
	    	 addBuf.append("Select T.* From (SELECT A.*, ROWNUM RN FROM ("+strSQL+") AS A WHERE ROWNUM <= ")
		     	   .append(currentPage*PageSize+") AS T WHERE RN > "+(currentPage-1)*PageSize);
	     }
	     else if (DataBaseType.equals("2")) {// MSSQL数据库 
	    	 addBuf.append("Select Top "+PageSize+" T.* From (SELECT ROW_NUMBER() OVER(ORDER BY ID) AS MSROWNUM, A.* FROM ("+strSQL+") AS A) AS T WHERE MSROWNUM > ")
	     	   	   .append((currentPage-1)*PageSize);
	     }
	     else if (DataBaseType.equals("3")) {// MySQL数据库
	    	 int offset = (currentPage-1)*PageSize;
	    	 addBuf.append("SELECT T.* FROM ("+strSQL+") AS T LIMIT "+offset+","+PageSize);
	     }
	     List<Map<String, Object>> retlist = userMapper.selectListMapExecSQL(addBuf.toString());
	     int length = retlist != null ? retlist.size() : 0;
	     
	     addBuf.delete(0,addBuf.length());//清空
	     addBuf.append("Select CURRACTIVITY From FLOW_RUNTIME_PROCESS where STATE<>'2' and STATE<>'3' and STATE<>'4' and ACCEPTPSN like '%"+strUserNo+"%'");
	     //活动/会签对照HashMap
	     Map<String, Object> SignType = getSignTypeHashtable(addBuf.toString());
	     if (length > 0) {
	         for (int i = 0; i < length; i++) {
	           Map<String, Object> retmap = retlist.get(i);
	           //得到当前活动的ID
	           strCURRACTIVITY = retmap.get("CURRACTIVITY").toString();
	           //得到当前接收人列表
	           strACCEPTPSN = retmap.get("ACCEPTPSN").toString();
	           //得到当前活动的会签类型
	           try {
	             strSignType = (String) SignType.get(strCURRACTIVITY);
	             if (strSignType==null)
	             {strSignType="0";}
	           }  catch (Exception ex) {
	             LOGGER.error("错误信息:", ex.toString());
	           }
	           if (strSignType.equals("1")) { //需顺序会签
	             //判断接收人列表中的第一个人是否是本人
	             List<Object> UserList = getArrayList(strACCEPTPSN, ",");
	             tmpUserNo = UserList.get(0).toString();
	             if (tmpUserNo.equals(strUserNo)) { //是本人时显示待办流程，否则待前面的人处理完后才收到。
	               returnValue = returnValue + 1;
	             }
	             //-------------------------------------
	           } else { //不需要顺序会签
	             returnValue = returnValue + 1;
	           }
	         }
	     }
	     return returnValue; 
	}

	/**
	 * 功能或作用：实现翻页
	 * @param PageSize 一页的记录数
	 * @param FileUrl 文件名
	 * @param intRowCount 总的记录数
	 * @param intPage 当前页
	 * @Return PageMenu 执行后返回一个PageMenu字符串
	 */
	private String createPageMenu(int intPageSize, int intRowCount, int intPage) {
        String PageMenu = "";
        StringBuffer addBuf = new StringBuffer();
        addBuf.append("&nbsp;");
        int intPageCount = 0, i, sum1 = 0, sum2 = 0, sum3 = intRowCount;
        while (((intRowCount - intPageSize) > 0) || (intRowCount > 0)) {
            intPageCount++;
            intRowCount = intRowCount - intPageSize;
        }
        if (intPage > intPageCount) {
            intPage = intPageCount;
        }
        if (intPageCount > 1) {
            sum1 = intPage - 1;
            if (sum1 < 1) {
                sum1 = 1;
            }
            sum2 = intPage + 1;
            if (sum2 > intPageCount) {
                sum2 = intPageCount;
            }
            addBuf.append("<a href='javascript:recordfrm.page.value=\"1\";recordfrm.submit();'>首页</a>")
                  .append("&nbsp;&nbsp;<a href='javascript:recordfrm.page.value=\""+String.valueOf(sum1))
                  .append("\";recordfrm.submit();'>上一页</a>"+"&nbsp;&nbsp;<a href='javascript:recordfrm.page.value=\"")
                  .append(String.valueOf(sum2)+"\";recordfrm.submit();'>下一页</a>"+"&nbsp;&nbsp;<a href='javascript:recordfrm.page.value=\"")
                  .append(String.valueOf(intPageCount)+"\";recordfrm.submit();'>末页</a>&nbsp;&nbsp;")
                  .append("<select size='1' class='PageStyle' Onchange='javascript:recordfrm.page.value=this.value;recordfrm.submit();'>");
            
            for (i = 1; i <= intPageCount; i++) {
                if (intPage == i) {
                    addBuf.append("<option value='"+String.valueOf(i)+"' selected >第"+String.valueOf(i)+"页</option>");
                } else {
                    addBuf.append("<option value='"+String.valueOf(i)+"' >第"+String.valueOf(i)+"页</option>");
                }
            }
            addBuf.append("</select>"+"&nbsp;&nbsp;页次："+String.valueOf(intPage)+"/")
                  .append(String.valueOf(intPageCount)+"&nbsp;&nbsp;共"+String.valueOf(sum3))
                  .append("条");
        } else {
            addBuf.append("&nbsp;&nbsp;共"+String.valueOf(sum3)+"条");
        }
        //String checkboxStr = ""; //不需要checkbox
        PageMenu = addBuf.toString();
        addBuf.delete(0,addBuf.length());//清空
        addBuf.append("</TD><TD align='right'>"+PageMenu);
        PageMenu = addBuf.toString();
        return PageMenu;
	}

	public void initHashtable() {
		dbEngine.inithashtable();
	}

}