package com.yonglilian.service.impl;

import com.yonglilian.common.util.DateWork;
import com.yonglilian.common.util.ReflectionUtil;
import com.yonglilian.common.util.StringWork;
import com.yonglilian.common.util.SysPreperty;
import com.yonglilian.dao.UserMapper;
import com.yonglilian.flowengine.mode.base.Package;
import com.yonglilian.flowengine.mode.config.FLOW_CONFIG_ACTIVITY;
import com.yonglilian.flowengine.mode.config.FLOW_CONFIG_ACTIVITY_CONNE;
import com.yonglilian.flowengine.mode.config.FLOW_CONFIG_PROCESS;
import com.yonglilian.flowengine.mode.config.FLOW_CONFIG_PROCESS_CONNECTION;
import com.yonglilian.flowengine.mode.monitor.FLOW_RUNTIME_PROCESS;
import com.yonglilian.flowengine.runtime.ActFlowRun;
import com.yonglilian.model.SessionUser;
import com.yonglilian.service.FlowControlService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zr.zrpower.common.db.DBEngine;
import zr.zrpower.common.db.DBServer;
import zr.zrpower.common.db.DBSet;
import zr.zrpower.common.db.DBType;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 流程控制函数库服务层实现
 * @author lwk
 *
 */
@Service
public class FlowControlServiceImpl implements FlowControlService {
	/** The FlowControlServiceImpl Logger. */
	private static final Logger LOGGER = LoggerFactory.getLogger(FlowControlServiceImpl.class);
	private DBEngine dbEngine;
	private static int clients = 0;
	static String DataBaseType = SysPreperty.getProperty().DataBaseType;// 数据库类型
	private Timer timerListener; // 队列侦听器
	
	static Map<String, Object> GetTitleHashtable = new HashMap<String, Object>();
	static Map<String, Object> FLOW_RUNTIME_PROCESSHashtable = new HashMap<String, Object>();
	static Map<String, Object> GetFlowActivityLinkHashtable = new HashMap<String, Object>();
	
	static Map<String, Object> GetUserNameHashtable = new HashMap<String, Object>();
	static Map<String, Object> GetFlowRunIDHTMLHashtable = new HashMap<String, Object>();
	static Map<String, Object> ADDHashtable = new HashMap<String, Object>();

	/** 用户操作数据层. */
	@Autowired
	private UserMapper userMapper;

	/**
	 * 构造方法
	 */
	public FlowControlServiceImpl() {
		dbEngine = new DBEngine(SysPreperty.getProperty().MainDataSource, 
				SysPreperty.getProperty().IsConvert);
		dbEngine.initialize();
	    if (clients < 1) {
	    	int intnum = 3600;//60分钟扫描一次
	    	timerListener = new Timer();
	    	timerListener.schedule(new ScanTask(), 0, intnum * 1000);	     
	    }
	    clients = 1;
	}

	class ScanTask extends TimerTask {
		public void run() {
			LOGGER.info("流程引擎扫描任务开始！");
			doMemory_Manage();
		}
	}

	@Override
	public String getFlowStartLabel() throws Exception {
		StringBuffer returnValue = new StringBuffer();
	    returnValue.append("<DIV class=WorkFlowMap id=Chart style='BORDER-RIGHT: silver 0px solid; BORDER-TOP: silver 1px solid; Z-INDEX: 101; LEFT: 0px; BORDER-LEFT: silver 0px solid; WIDTH:300%; BORDER-BOTTOM: silver 0px solid; POSITION: absolute; TOP: 0px; HEIGHT:250%'>\r\n");
	    returnValue.append("   <v:group id=WorkFlowGroup style='WIDTH: 200px; POSITION: absolute; HEIGHT: 200px; v-text-anchor: middle-center' coordsize = '2000,2000'>\r\n");
	    returnValue.append("      <v:polyline id=line1 style='Z-INDEX: 2000; POSITION: absolute' points = '0,0,5,5'>\r\n");
	    returnValue.append("         <!--钢笔可视化--><v:stroke dashstyle = 'shortDash'></v:stroke>\r\n");
	    returnValue.append("         </v:polyline>\r\n");
	    
	    return returnValue.toString();
	}

	@Override
	public String getFlowEndLabel() throws Exception {
		StringBuffer returnValue = new StringBuffer();
	    returnValue.append("</v:group></DIV>\r\n");
	    
	    return returnValue.toString();
	}

	@Override
	public String getFlowStartActivityLabel(String x, String y, String ico) throws Exception {
		StringBuffer returnValue = new StringBuffer();
	    returnValue.append("<v:Group id=WorkFlowItemGroup style='Z-INDEX: 9000; LEFT:" + x 
	    				+ "; WIDTH: 400px; POSITION: absolute; TOP: " + y + "; HEIGHT: 400px' xmlns:v='urn:schemas-microsoft-com:vml' coordsize = '400,400'>\r\n");
	    returnValue.append("   <v:oval class=WorkFlowItem id=0 title=开始 LimiteDate='1' style='WIDTH: 400px; POSITION: absolute; HEIGHT: 400px' DepName='开始活动' coordsize='21600,21600' strokecolor='#ffffff'>\r\n");
	    returnValue.append("      <v:fill src='../../images/" + ico + "' type = 'frame' angle = '0' method ='sigma'></v:fill>\r\n");
	    returnValue.append("      <v:stroke></v:stroke></v:oval>\r\n");
	    returnValue.append("        <v:TextBox style='POSITION:absolute;MARGIN-TOP: 18pt;TOP:18pt;LEFT:6pt;'>开始</v:TextBox>\r\n");
	    returnValue.append("        <v:line class=EndLine style='CURSOR: hand' Refid='0' from = '400,200' to = '550,200' strokecolor = 'black'><v:stroke endarrow = 'classic'></v:stroke></v:line></v:Group>\r\n");
	    
	    return returnValue.toString();
	}

	@Override
	public String getFlowEndActivityLabel(String x, String y, String ico) throws Exception {
	    StringBuffer returnValue = new StringBuffer();
	    returnValue.append("<v:Group id=WorkFlowItemGroup style='Z-INDEX: 9000; LEFT:" + x 
	    				 + "; WIDTH: 400px; POSITION: absolute; TOP:" + y + "; HEIGHT: 400px' xmlns:v='urn:schemas-microsoft-com:vml' coordsize = '400,400'>\r\n");
	    returnValue.append("   <v:oval class=WorkFlowItem id=-1 title=结束活动 LimiteDate='2' style='WIDTH: 400px; POSITION: absolute; HEIGHT: 400px' DepName='结束活动' coordsize='21600,21600' fillcolor='blue' strokecolor='#ffffff'>\r\n");
	    returnValue.append("      <v:fill src='../../images/" + ico + "' type = 'frame' angle = '0' method = 'sigma'></v:fill><v:stroke></v:stroke></v:oval>\r\n");
	    returnValue.append("      <v:TextBox style='POSITION:absolute;MARGIN-TOP: 18pt;TOP:18pt;LEFT:16pt;'>结束</v:TextBox>\r\n");
	    returnValue.append("      <v:line class=StartLine Refid='-1' from ='-100,200' to = '0,200' strokecolor = 'black'><v:stroke startarrow ='oval'></v:stroke></v:line></v:Group>\r\n");
	    
	    return returnValue.toString();  
	}

	@Override
	public String getFlowHumanActivityLabel(String x, String y, String id, String name, String desc, String ico) throws Exception {
	    try {
	        StringBuffer returnValue = new StringBuffer();
	        returnValue.append("<v:Group id=WorkFlowItemGroup style='Z-INDEX: 9000; LEFT:" + x 
	        				 + "; WIDTH: 500px; POSITION: absolute; TOP:" + y + "; HEIGHT: 400px' xmlns:v='urn:schemas-microsoft-com:vml' coordsize = '500,400'>\r\n");
	        returnValue.append("   <v:rect class=WorkFlowItem id=" + id + " title=" 
	        				   + desc + " LimiteDate='3' style='WIDTH: 500px; POSITION: absolute; HEIGHT: 400px' DepName='" 
	                           + name + "' coordsize = '21600,21600' fillcolor = 'blue' strokecolor ='#ffffff'>\r\n");
	        returnValue.append("      <v:fill src='../../images/" + ico + "' type = 'frame' angle = '0' method ='sigma'></v:fill><v:stroke></v:stroke></v:rect>\r\n");
	        returnValue.append("      <v:TextBox style='POSITION:absolute;MARGIN-TOP: 18pt;TOP:18pt;LEFT:10pt;'>" + name + "</v:TextBox>\r\n");
	        returnValue.append("      <v:line class=EndLine style='CURSOR: hand' Refid='" + id + "' from = '500,200' to ='650,200' strokecolor = 'black'><v:stroke endarrow ='classic'></v:stroke></v:line>\r\n");
	        returnValue.append("      <v:line class=StartLine Refid='" + id + "' from ='-100,200' to = '0,200' strokecolor = 'black'><v:stroke startarrow ='oval'></v:stroke></v:line></v:Group>\r\n");
	        
	        return returnValue.toString();
        } catch (Exception ex) {
        	ex.printStackTrace();
        	return "";
        }
	}

	@Override
	public String getFlowTimerActivityLabel(String x, String y, String id, String name, String desc, String ico) throws Exception {
	    StringBuffer returnValue = new StringBuffer();
	    returnValue.append("<v:Group id=WorkFlowItemGroup style='Z-INDEX: 9000; LEFT:" + x 
	    				 + "; WIDTH: 400px; POSITION: absolute; TOP:" + y + "; HEIGHT: 400px' xmlns:v='urn:schemas-microsoft-com:vml' coordsize = '400,400'>\r\n");
	    returnValue.append("   <v:oval class=WorkFlowItem id=" + id + " title=" 
	    				 + desc + " LimiteDate='4' style='WIDTH: 400px; POSITION: absolute; HEIGHT: 400px' DepName='" 
	    				 + name + "' coordsize = '21600,21600' fillcolor = 'blue' strokecolor ='#ffffff'>\r\n");
	    returnValue.append("      <v:fill src='../../images/" + ico + "' type = 'frame' angle = '0' method ='sigma'></v:fill><v:stroke></v:stroke></v:oval>\r\n");
	    returnValue.append("      <v:TextBox style='POSITION:absolute;MARGIN-TOP: 18pt;TOP:18pt;LEFT:10pt;'>" + name + "</v:TextBox>\r\n");
	    returnValue.append("      <v:line class=EndLine style='CURSOR: hand' Refid='" + id + "' from='400,200' to='550,200' strokecolor='black'><v:stroke endarrow ='classic'></v:stroke></v:line>\r\n");
	    returnValue.append("      <v:line class=StartLine Refid='" + id + "' from ='-100,200' to = '0,200' strokecolor = 'black'><v:stroke startarrow ='oval'></v:stroke></v:line></v:Group>\r\n");
	    
	    return returnValue.toString();
	}

	@Override
	public String getFlowSubFlowLabel(String x, String y, String id, String name, String desc, String ico) throws Exception {
	    StringBuffer returnValue = new StringBuffer();
	    FLOW_CONFIG_ACTIVITY activity = null;
	    activity = getFlowActivity(id);
	    String strFlowID = activity.getDESC1();
	    returnValue.append("<v:Group id=WorkFlowItemGroup style='Z-INDEX: 9000; LEFT:" + x 
	    				 + "; WIDTH: 500px; POSITION: absolute; TOP:" + y + "; HEIGHT: 500px' xmlns:v='urn:schemas-microsoft-com:vml' coordsize = '500,500'>\r\n");
	    returnValue.append("   <v:rect class=WorkFlowItem id=" + id + " title=" + desc 
	    				 + " LimiteDate='flow" + strFlowID + "' style='WIDTH: 500px; POSITION: absolute; HEIGHT: 500px' DepName='" 
	    				 + name + "' coordsize = '21600,21600' fillcolor = 'blue' strokecolor ='#ffffff'>\r\n");
	    returnValue.append("      <v:fill src='../../images/" + ico + "' type = 'frame' angle = '0' method ='sigma'></v:fill><v:stroke></v:stroke></v:rect>\r\n");
	    returnValue.append("      <v:TextBox style='POSITION:absolute;MARGIN-TOP: 20pt;TOP:20pt;LEFT:10pt;'>" + name + "</v:TextBox>\r\n");
	    returnValue.append("      <v:line class=StartLine Refid='" + id + "' from ='-100,250' to = '0,250' strokecolor = 'black'><v:stroke startarrow ='oval'></v:stroke></v:line></v:Group>\r\n");
	    
	    return returnValue.toString();
	}

	@Override
	public String getFlowLineLabel(String startID, String endID, String id) throws Exception {
	    try {
	        StringBuffer returnValue = new StringBuffer();
	        FLOW_CONFIG_ACTIVITY activity = null;
	        activity = getFlowActivity(startID);
	        if (activity != null) {
	          if (activity.getTYPE().equals("1")) {
	            startID = "0";
	          }
	          if (activity.getTYPE().equals("2")) {
	            startID = "-1";
	          }
	          activity = getFlowActivity(endID);
	          if (activity.getTYPE().equals("1")) {
	            endID = "0";
	          }
	          if (activity.getTYPE().equals("2")) {
	            endID = "-1";
	          }
	          activity = null;//赋空值
	        }
	        returnValue.append("<v:polyline class=NormalLine id=ConnectLine  LimiteDate='" + id 
	        				 + "' style='Z-INDEX: -1' xmlns:v='urn:schemas-microsoft-com:vml' EndShape='" 
	        				 + startID + "' BeginShape='" + endID 
	        				 + "' points = '0,0,10,10' strokecolor ='blue'>\r\n");
	        returnValue.append("   <v:stroke endarrow = 'classic'></v:stroke></v:polyline>\r\n");
	        
	        return returnValue.toString();
	      } catch (Exception ex) {
	        ex.printStackTrace();
	        LOGGER.error("提示：getFlowLineLabel出错：", ex);
	        return "";
	      }    
	}

	@Override
	public String getFlowTPackageLabel(String x, String y, String id, String name, String desc, String ico)
			throws Exception {
	    StringBuffer returnValue = new StringBuffer();
	    returnValue.append("<v:Group id=WorkFlowItemGroup style='Z-INDEX: 9000; LEFT:" + x 
	    				 + "; WIDTH: 500px; POSITION: absolute; TOP:" + y + "; HEIGHT: 400px' xmlns:v='urn:schemas-microsoft-com:vml' coordsize = '500,400'>\r\n");
	    returnValue.append("   <v:rect class=WorkFlowItem id=" + id + " title=" + desc 
	    				 + " LimiteDate='3' style='WIDTH: 500px; POSITION: absolute; HEIGHT: 400px' DepName='" 
	    				 + name + "' coordsize = '21600,21600' fillcolor = 'blue' strokecolor ='#ffffff'>\r\n");
	    returnValue.append("      <v:fill src='" + ico + "' type = 'frame' angle = '0' method ='sigma'></v:fill><v:stroke></v:stroke></v:rect>\r\n");
	    returnValue.append("      <v:TextBox style='POSITION:absolute;MARGIN-TOP: 18pt;TOP:18pt;LEFT:2pt;'>" + name + "</v:TextBox>\r\n");
	    returnValue.append("      </v:Group>\r\n");
	    
	    return returnValue.toString();  
	}

	@Override
	public FLOW_CONFIG_ACTIVITY[] getActivityList(String flowID) throws Exception {
		FLOW_CONFIG_ACTIVITY[] returnValue = null;
	    StringBuffer addBuf = new StringBuffer();
	    addBuf.append("Select * From FLOW_CONFIG_ACTIVITY where FID='"+flowID+"' order by ORDER1");
	    List<Map<String, Object>> retlist = userMapper.selectListMapExecSQL(addBuf.toString());
	    int length = retlist != null ? retlist.size() : 0;
	    if (length > 0) {
	        returnValue = new FLOW_CONFIG_ACTIVITY[length];
	        for (int i = 0; i < length; i++) {
	          Map<String, Object> retmap = retlist.get(i);
	          returnValue[i] = (FLOW_CONFIG_ACTIVITY) ReflectionUtil.convertMapToBean(retmap, FLOW_CONFIG_ACTIVITY.class);
	        }
	    }
	    return returnValue;
	}

	@Override
	public FLOW_CONFIG_ACTIVITY_CONNE[] getActivityConneList(String flowID, String ActivityID) throws Exception {
		FLOW_CONFIG_ACTIVITY_CONNE[] returnValue = null;
	    StringBuffer addBuf = new StringBuffer();
	    addBuf.append("Select * From FLOW_CONFIG_ACTIVITY_CONNE where FID='"+flowID+"' and CID='"+ActivityID+"'");
	    List<Map<String, Object>> retlist = userMapper.selectListMapExecSQL(addBuf.toString());
	    int length = retlist !=  null ? retlist.size() : 0;
	    if (length > 0) {
	        returnValue = new FLOW_CONFIG_ACTIVITY_CONNE[length];
	        for (int i = 0; i < length; i++) {
	          Map<String, Object> retmap = retlist.get(i);
	          returnValue[i] = (FLOW_CONFIG_ACTIVITY_CONNE) ReflectionUtil.convertMapToBean(retmap, FLOW_CONFIG_ACTIVITY_CONNE.class);
	        }
	    }
	    return returnValue;
	}

	@Override
	public FLOW_CONFIG_PROCESS_CONNECTION[] getProcessConneList(String flowID, String ActivityID) throws Exception {
	    FLOW_CONFIG_PROCESS_CONNECTION[] returnValue = null;
	    StringBuffer strSQL = new StringBuffer();
	    strSQL.append("Select * From FLOW_CONFIG_PROCESS_CONNECTION where FID='"+flowID)
	          .append("' and CID='"+ActivityID+"' order by CODE,ID");
	    List<Map<String, Object>> retlist = userMapper.selectListMapExecSQL(strSQL.toString());
	    int length = retlist !=  null ? retlist.size() : 0;
	    if (length > 0) {
	        returnValue = new FLOW_CONFIG_PROCESS_CONNECTION[length];
	        for (int i = 0; i < length; i++) {
	          Map<String, Object> retmap = retlist.get(i);
	          returnValue[i] = (FLOW_CONFIG_PROCESS_CONNECTION) ReflectionUtil.convertMapToBean(
	        		  retmap, FLOW_CONFIG_PROCESS_CONNECTION.class);
	        }
	    }
	    return returnValue;  
	}

	@Override
	public boolean saveFlowConfig(String FlowID, String FlowConfigCss) throws Exception {
	    boolean saveStat = false;// 流程是否保存成功
	    DBServer dbServer = new DBServer();
	    saveStat = dbServer.saveFlowConfig(FlowID, FlowConfigCss);
	    initHashtable();
	    return saveStat;
	}

	@Override
	public boolean deleteActivityConne(String FlowID, String ActivityID) throws Exception {
		StringBuffer addBuf = new StringBuffer();
	    addBuf.append("Delete From FLOW_CONFIG_ACTIVITY_CONNE Where FID='"+FlowID)
	          .append("' and CID='"+ActivityID+"'");
	    Integer retInt = userMapper.deleteExecSQL(addBuf.toString());
	    if (retInt != null && retInt > 0) {
	    	return true;
	    }
	    return false;
	}

	@Override
	public boolean deleteConnID(String cid, String eid) throws Exception {
	    String strsql ="", xycss="";
	    strsql ="select XYCSS from FLOW_CONFIG_ACTIVITY where ID='"+cid+"'";
	    Map<String, Object> retmap = userMapper.selectMapExecSQL(strsql);
	    if (retmap != null && retmap.size() > 0) {
	        xycss = retmap.get("XYCSS").toString();
	        
	        xycss = xycss.replaceAll(","+eid,"");
	        xycss = xycss.replaceAll(eid+",","");
	        xycss = xycss.replaceAll(eid,"");
	        strsql ="update FLOW_CONFIG_ACTIVITY set XYCSS ='"+xycss+"' where ID='"+cid+"'";
	        
	        userMapper.updateExecSQL(strsql);
	    }
	    strsql = "Delete From FLOW_CONFIG_ACTIVITY_CONNE "
	    	   + "Where (SID='"+cid+"' and CID='"+eid+"') or (SID='"+cid+"' and EID='"+eid+"')";
	    Integer retInt = userMapper.deleteExecSQL(strsql);
	    if (retInt != null && retInt > 0) {
	    	return true;
	    }
	    return false;
	}

	@Override
	public boolean deleteProcessConne(String FlowID, String ActivityID) throws Exception {
		StringBuffer addBuf = new StringBuffer();
	    addBuf.append("Delete From FLOW_CONFIG_PROCESS_CONNECTION Where FID='"+FlowID+"' and CID='"+ActivityID+"'");
	    Integer retInt = userMapper.deleteExecSQL(addBuf.toString());
	    if (retInt != null && retInt > 0) {
	    	return true;
	    }
	    return false;
	}

	@Override
	public String getFlowStartActivityID(String FlowID) throws Exception {
		StringBuffer addBuf = new StringBuffer();
	    addBuf.append("Select ID From FLOW_CONFIG_ACTIVITY where FID='"+FlowID+"' and TYPE='1'");
	    DBSet dbset = dbEngine.QuerySQL(addBuf.toString());
	    if (dbset != null && dbset.RowCount() > 0) {
	        addBuf.delete(0,addBuf.length());//清空
	        addBuf.append(dbset.Row(0).Column("ID").getString());
	        dbset = null;//赋空值
	    }
	    return addBuf.toString();
	}

	@Override
	public String getFlowEndActivityID(String FlowID) throws Exception {
		StringBuffer addBuf = new StringBuffer();
	    addBuf.append("Select ID From FLOW_CONFIG_ACTIVITY where FID='"+FlowID+"' and TYPE='2'");
	    DBSet dbset = dbEngine.QuerySQL(addBuf.toString());
	    if (dbset != null && dbset.RowCount() > 0) {
	        addBuf.delete(0,addBuf.length());//清空
	        addBuf.append(dbset.Row(0).Column("ID").getString());
	        dbset = null;//赋空值
	    }
	    return addBuf.toString();
	}

	@Override
	public boolean updateActivityXycss(String FlowID, String ActivityID, String strCss) throws Exception {
	    try {
		    String strID = "";
		    String[] FlowCssList1 = strCss.split("/");
		    //检查活动ID是否是刚插入的子流程
		    String strActivityID = FlowCssList1[1];
		    if (strActivityID.indexOf("flow") != -1) {
		      strActivityID = strActivityID.replaceAll("flow", "");
		      strID = getSubFlowActivityID(FlowID, strActivityID);
		    }
		    strCss = strCss.replaceAll("flow" + strActivityID, strID);
		    //-------------------------
		    //检查关系ID是否有刚插入的子流程
		    strActivityID = FlowCssList1[3];
		    if (strActivityID.indexOf("flow") != -1) {
		      String[] FlowCssList2 = strActivityID.split(",");
		      for (int i = 0; i < FlowCssList2.length; i++) {
		        if (FlowCssList2[i].indexOf("flow") != -1) {
		          strActivityID = FlowCssList2[i].replaceAll("flow", "");
		          strID = getSubFlowActivityID(FlowID, strActivityID);
		        }
		        strCss = strCss.replaceAll("flow" + strActivityID, strID);
		      }
		    }
		    //-------------------------
		    StringBuffer addBuf = new StringBuffer();
		    addBuf.append("update FLOW_CONFIG_ACTIVITY set XYCSS='"+strCss+"' "
		    			+ "where FID='"+FlowID+"' and ID='"+ActivityID+"'");
		    boolean isOK = dbEngine.ExecuteSQL(addBuf.toString());
		    if (isOK) {
		    	return true;
		    }
		    return false;
	    } catch (Exception ex) {
	         LOGGER.error("流程引擎---updateActivityXycss函数出错：", ex);
	         return false;
	    }
	}

	@Override
	public String getMaxFieldNo(String TableName, String FieldName, int FieldLen) throws Exception {
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
	      }
	      MaxNo = MaxNo.substring(25 - FieldLen + LenMaxNo);
	    } catch (Exception ex) {
	      	ex.printStackTrace();
	    }
	    return MaxNo;  
	}

	@Override
	public String selectFlowActivity(String FlowID, String type, String SubFlowNo, String SubFlowName) throws Exception {
	    String isActivity = "0";
	    String strMaxNo = "";
	    StringBuffer addBuf = new StringBuffer();
	    if (SubFlowNo.length() > 0) {
	      addBuf.append("Select ID From FLOW_CONFIG_ACTIVITY where FID='"+FlowID+"' and TYPE='"+type+"' and DESC1='"+"'");
	    } else {
	      addBuf.append("Select ID From FLOW_CONFIG_ACTIVITY where FID='"+FlowID+"' and TYPE='"+type+"'");
	    }
	    DBSet dbset = dbEngine.QuerySQL(addBuf.toString());
	    if (dbset != null && dbset.RowCount() > 0) {
	       isActivity = "1";
	       dbset=null;//赋空值
	    }
	    if (isActivity.equals("0")) {
	      strMaxNo = getMaxFieldNo("FLOW_CONFIG_ACTIVITY", "ID", 10);
	      addBuf.delete(0,addBuf.length());//清空
	      if (type.equals("1")) {
	        addBuf.append("Insert into FLOW_CONFIG_ACTIVITY(ID,FID,IDENTIFICATION,NAME,DESC1,TYPE,ORDER1) values ('")
	            .append(strMaxNo+"','"+FlowID+"','FA"+strMaxNo+"','开始','','1','0')");
	      }
	      else if (type.equals("2")) {
	        addBuf.append("Insert into FLOW_CONFIG_ACTIVITY(ID,FID,IDENTIFICATION,NAME,DESC1,TYPE,ORDER1) values ('")
	            .append(strMaxNo+"','"+FlowID+"','FA"+strMaxNo+"','结束','','2','10000')");
	      }
	      else if (type.equals("5")) {
	        addBuf.append("Insert into FLOW_CONFIG_ACTIVITY(ID,FID,IDENTIFICATION,NAME,DESC1,TYPE,ORDER1) values ('")
	            .append(strMaxNo+"','"+FlowID+"','FA"+strMaxNo+"','")
	            .append(SubFlowName+"','"+SubFlowNo+"','5','9999')");
	      } else {
	      }
	      dbEngine.QuerySQL(addBuf.toString());
	    }
	    return strMaxNo;  
	}

	@Override
	public String getSubFlowActivityID(String FlowID, String SubFlowID) throws Exception {
		StringBuffer addBuf = new StringBuffer();
	    addBuf.append("Select ID From FLOW_CONFIG_ACTIVITY where FID='"+FlowID+"' and DESC1='"+SubFlowID+"'");
	    DBSet dbset = dbEngine.QuerySQL(addBuf.toString());
	    if (dbset != null && dbset.RowCount() > 0) {
	        addBuf.delete(0,addBuf.length());//清空
	        addBuf.append(dbset.Row(0).Column("ID").getString());
	        dbset = null;//赋空值
	    }
	    return addBuf.toString();
	}

	@Override
	public String getFlowRunCDate(String strID) throws Exception {
		String returnValue = "";
	    StringBuffer addBuf = new StringBuffer();
	    addBuf.append("Select CREATEDATE From FLOW_RUNTIME_PROCESS where ID='"+strID+"'");
	    DBSet dbset = dbEngine.QuerySQL(addBuf.toString());
	    if (dbset != null && dbset.RowCount() > 0) {
	        returnValue = DateWork.DateTimeToString(dbset.Row(0).Column("CREATEDATE").getDate());
	        if (returnValue.length() > 10) {
	        	returnValue = returnValue.substring(0, 10);
	        }
	        dbset=null;//赋空值
	    }
	    return returnValue;
	}

	@Override
	public String getDateAsStr() throws Exception { 
		Calendar cal = Calendar.getInstance();
	    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	    String mDateTime = dateFormat.format(cal.getTime());
	    
	    return mDateTime;
	}

	@Override
	public String getFlowID(String strNO) throws Exception {
        String returnValue = "";
        returnValue = "";
        StringBuffer addBuf = new StringBuffer();
        addBuf.append("Select ID From FLOW_CONFIG_PROCESS where IDENTIFICATION='"+strNO+"'");
        List<Map<String, Object>> retlist = userMapper.selectListMapExecSQL(addBuf.toString());
        if (retlist != null && retlist.size() > 0) {
        	if (retlist.get(0) != null && retlist.get(0).get("ID") != null) {
        		returnValue = retlist.get(0).get("ID").toString();
        	}
        }
        return returnValue;
	}

	@Override
	public String getFirstActivityID(String strID) throws Exception {
		String strResult = "";
        StringBuffer addBuf = new StringBuffer();
        addBuf.append("Select ID From FLOW_CONFIG_ACTIVITY "
        			+ "where FID='"+strID+"' and TYPE='3' and ORDER1<>0 order by ORDER1");
        List<Map<String, Object>> retlist = userMapper.selectListMapExecSQL(addBuf.toString());
        if (retlist != null && retlist.size() > 0) {
        	if (retlist.get(0) != null && retlist.get(0).get("ID") != null) {
        		strResult = retlist.get(0).get("ID").toString();
        	}
            retlist = null;//赋空值
        }
        return strResult;
	}

	@Override
	public String getLastlyActivityID(String strID) throws Exception {
        StringBuffer addBuf = new StringBuffer();
        String strRevalue = "";
        addBuf.append("Select ID From FLOW_CONFIG_ACTIVITY "
        			+ "where FID='"+strID+"' and TYPE='3' and ORDER1<>9998 order by ORDER1 desc");
        List<Map<String, Object>> retlist = userMapper.selectListMapExecSQL(addBuf.toString());
        if (retlist != null && retlist.size() > 0) {
        	if (retlist.get(0) != null && retlist.get(0).get("ID") != null) {
        		strRevalue = retlist.get(0).get("ID").toString();
        	}
        	retlist = null;//赋空值
        }
        return strRevalue;
	}

	@Override
	public String getActivityButton(String strExecute_No, String strID, String strtype, 
			String strbuttontype, SessionUser user) throws Exception {
	    try {
	        String strButtonName = "", strButtonIco = "", strButtonProperty = "",strROLEIDS="";
	        String strsql = "";
	        boolean isTrue = true;
	        boolean isTrue1 = true;
	        StringBuffer addBuf = new StringBuffer();
	        if (strExecute_No!=null && strExecute_No.length()>0) {
	          isTrue = getIsFlowDoList(strExecute_No, user); //判断当前用户是否是当前流转流程中的待处理人
	        }
	        if (isTrue){
	              //---得到是否执行确认处理(多人同时处理提交时用到)---------
	              isTrue1 = getIsShow(strExecute_No,strID);
	              if (strtype.equals("1")) {
	                addBuf.append("Select a.NAME,a.ICO,a.PROPERTY,a.ROLEIDS From FLOW_BASE_BUTTON a left join FLOW_CONFIG_ACTIVITY_BUTTON b on a.id=b.BUTTONID "
	                			+ "where (a.POSITION='1' or a.POSITION='3') and a.TYPE<>'2' AND a.TYPE<>'3' and b.FID='")
	                      .append(strID).append("' order by b.ID");
	              } else {
	                addBuf.append("Select a.NAME,a.ICO,a.PROPERTY,a.ROLEIDS From FLOW_BASE_BUTTON a left join FLOW_CONFIG_ACTIVITY_BUTTON b on a.id=b.BUTTONID "
	                			+ "where (a.POSITION='2' or a.POSITION='3') and a.TYPE<>'2' AND a.TYPE<>'3' and b.FID='")
	                      .append(strID).append("' order by b.ID");
	              }
	              List<Map<String, Object>> retlist1 = userMapper.selectListMapExecSQL(addBuf.toString());
	              int length1 = retlist1 != null ? retlist1.size() : 0;
	              addBuf.delete(0,addBuf.length());//清空
	              if (length1 > 0) {
	                  for (int i=0; i<length1; i++) {
	                	  Map<String, Object> retmap1 = retlist1.get(i);
		                  strButtonName = retmap1.get("NAME") != null ? retmap1.get("NAME").toString() : "";
		                  strButtonIco = retmap1.get("ICO") != null ? retmap1.get("ICO").toString() : "";
		                  strButtonProperty = retmap1.get("PROPERTY") != null ? retmap1.get("PROPERTY").toString() : "";
		                  strROLEIDS = retmap1.get("ROLEIDS") != null ? retmap1.get("ROLEIDS").toString() : "";
		                  if (strButtonProperty.indexOf("saveColl('2')") !=-1 && isTrue1) {
		                     strButtonName = "确认处理";
		                     strButtonProperty = "submitFinish()";
		                  }
		                  if (strButtonProperty.indexOf("doUntread()") !=-1 && isTrue1) {
		                     strButtonName = "确认处理[退回]";
		                     strButtonProperty = "UntreadFinish()";
		                  }
		                  if (strROLEIDS.length()>0) {
		                        String isshow ="0";
		                        strsql ="select ROLEID from bpip_user_role where USERID='"+user.getUserID()+"' and ROLEID in ("+strROLEIDS+")";
		                        List<Map<String, Object>> retlist2 = userMapper.selectListMapExecSQL(strsql);
		                        if (retlist2 != null && retlist2.size()>0) {//有显示权限
		                           isshow ="1";
		                        } else {
		                           isshow ="0";
		                        }
			                    if (isshow.equals("1")) {//有显示权限
			                         if (strbuttontype.equals("1")) {
			                               addBuf.append(clickButton(strButtonName, strButtonProperty, strButtonIco));
			                         } else {
			                               addBuf.append(clickButton1(strButtonName, strButtonProperty, strButtonIco));
			                         }
			                    }
		                  } else {
		                    if (strbuttontype.equals("1")) {
		                        addBuf.append(clickButton(strButtonName, strButtonProperty, strButtonIco));
		                    } else {
		                        addBuf.append(clickButton1(strButtonName, strButtonProperty, strButtonIco));
		                    }
		                  }
	                  }
	              }
	        }
	        return addBuf.toString();
	    } catch (Exception ex) {
	    	LOGGER.error("流程引擎---GetActivityButton函数出错：", ex);
	    	return "";
	    }
	}

	@Override
	public boolean getIsFlowDoList(String strExecute_No, SessionUser user) throws Exception {
	    boolean returnValue = false;
	    String tmpUserNo = ""; //临时人员编号
	    String strSQL = "";
	    strSQL = "Select ACCEPTPSN From FLOW_RUNTIME_PROCESS where ID ='"+strExecute_No+"' and STATE<>'2' and STATE<>'3' and STATE<>'4' and ACCEPTPSN like '%"+user.getUserID()+"%'";
	    //dbSet dbset = dbengine.QuerySQL(strSQL,"flow24",strExecute_No+user.getUserID());
	    List<Map<String, Object>> dbset = userMapper.selectListMapExecSQL(strSQL);
	    if (dbset != null && dbset.size() > 0) {
	    	if (dbset.get(0) != null && dbset.get(0).get("ACCEPTPSN") != null) {
	    		tmpUserNo = dbset.get(0).get("ACCEPTPSN").toString();
	    	}
	        dbset = null;//赋空值
	    }
	    if (tmpUserNo.length() > 0) {
	      returnValue = true;
	    }
	    //查找是否是传阅中的人员
	    tmpUserNo = "";
	    strSQL ="Select DOPSN From FLOW_RUNTIME_ACTIVITY where DOPSN='"+user.getUserID()+"' and DOFLAG='0' and DOIDEA='send' and FID='"+strExecute_No+"'";
	    //dbSet dbsend = dbengine.QuerySQL(strSQL,"flow25",strExecute_No+user.getUserID());
	    List<Map<String, Object>> dbsend = userMapper.selectListMapExecSQL(strSQL);
	    if (dbsend != null && dbsend.size() > 0) {
	    	if (dbsend.get(0) != null && dbsend.get(0).get("DOPSN") != null) {
	    		tmpUserNo = dbsend.get(0).get("DOPSN").toString();
	    	}
	        dbsend = null;//赋空值
	    }
	    if (tmpUserNo.length() > 0) {
	    	returnValue = true;
	    }
	    return returnValue;  
	}

	@Override
	public String getPublicButton(String strtype, String strbuttontype) throws Exception {
	    String strButtonName = "", strButtonIco = "", strButtonProperty = "";
	    StringBuffer addBuf = new StringBuffer();
	    String strRevalue = "";
	    if (strtype.equals("1")) {
	      addBuf.append("Select NAME,ICO,PROPERTY From FLOW_BASE_BUTTON where TYPE='4' and (POSITION='1' or POSITION='3')  order by CODE,ID");
	    } else {
	      addBuf.append("Select NAME,ICO,PROPERTY From FLOW_BASE_BUTTON where TYPE='4' and (POSITION='2' or POSITION='3')  order by CODE,ID");
	    }
	    List<Map<String, Object>> dbset = userMapper.selectListMapExecSQL(addBuf.toString());
	    addBuf.delete(0,addBuf.length());//清空
	    if (dbset != null && dbset.size() > 0) {
	      for (int i = 0; i < dbset.size(); i++) {
	    	Map<String, Object> retmap = dbset.get(i);
	        strButtonName = retmap.get("NAME").toString();
	        strButtonIco = retmap.get("ICO").toString();
	        strButtonProperty = retmap.get("PROPERTY") != null ? retmap.get("PROPERTY").toString() : "";
	        if (strbuttontype.equals("1")) {
	             addBuf.append(clickButton(strButtonName, strButtonProperty, strButtonIco));
	        } else {
	             addBuf.append(clickButton1(strButtonName, strButtonProperty, strButtonIco));
	        }
	      }
	      dbset = null;//赋空值
	    }
	    strRevalue = addBuf.toString();
	    
	    return strRevalue;  
	}

	@Override
	public String getCreateButton(String strExecute_No, String strUserID, String strtype, String strbuttontype) throws Exception {
	    String strButtonName = "", strButtonIco = "", strButtonProperty = "";
	    StringBuffer addBuf = new StringBuffer();
	    if (strExecute_No.length()==0) {
	       return "";
	    }
	    FLOW_RUNTIME_PROCESS Fprocess = null;
	    Fprocess = getFlowRuntimeprocess(strExecute_No);
	    if (!Fprocess.getCREATEPSN().equals(strUserID)) {
	       return "";
	    }
	    addBuf.delete(0,addBuf.length());//清空
	    if (strtype.equals("1")) {
	        addBuf.append("Select NAME,ICO,PROPERTY From FLOW_BASE_BUTTON where TYPE='6' and (POSITION='1' or POSITION='3') order by CODE,ID");
	    } else {
	        addBuf.append("Select NAME,ICO,PROPERTY From FLOW_BASE_BUTTON where TYPE='6' and (POSITION='2' or POSITION='3') order by CODE,ID");
	    }
	    List<Map<String, Object>> dbset = userMapper.selectListMapExecSQL(addBuf.toString());
	    addBuf.delete(0,addBuf.length());//清空
	    if (dbset != null && dbset.size()>0) {
	      for (int i = 0; i < dbset.size(); i++) {
	    	Map<String, Object> retmap = dbset.get(i);
	        strButtonName = retmap.get("NAME").toString();
	        strButtonIco = retmap.get("ICO").toString();
	        strButtonProperty = retmap.get("PROPERTY") != null ? retmap.get("PROPERTY").toString() : "";
	        if (strbuttontype.equals("1")) {
	             addBuf.append(clickButton(strButtonName, strButtonProperty, strButtonIco));
	        } else {
	        	addBuf.append(clickButton1(strButtonName, strButtonProperty, strButtonIco));
	        }
	      }
	      dbset = null;//赋空值
	    }
	    return addBuf.toString();  
	}

	@Override
	public String getDPublicButton(String str_FlowID, String strtype, String strbuttontype, ActFlowRun ActFlowRun) throws Exception {
	    String strButtonName = "", strButtonIco = "", strButtonProperty = "";
	    StringBuffer addBuf = new StringBuffer();
	    String strRunID = ActFlowRun.getExecute_No();//流程运转ID
	    addBuf.delete(0,addBuf.length());//清空
	    if (strtype.equals("1")) {
	        addBuf.append("Select distinct NAME,ICO,PROPERTY,ROLEIDS From FLOW_BASE_BUTTON where TYPE='5' and (POSITION='1' or POSITION='3') and ID in (select BUTTONID from FLOW_CONFIG_ACTIVITY_BUTTON where FID in (select ID from FLOW_CONFIG_ACTIVITY where FID='")
	          	  .append(str_FlowID+"' and ID in (select EACTIVITY from FLOW_RUNTIME_ACTIVITY where FID='"+strRunID+"')))");
	    } else {
	        addBuf.append("Select distinct NAME,ICO,PROPERTY,ROLEIDS From FLOW_BASE_BUTTON where TYPE='5' and (POSITION='2' or POSITION='3') and ID in (select BUTTONID from FLOW_CONFIG_ACTIVITY_BUTTON where FID in (select ID from FLOW_CONFIG_ACTIVITY where FID='")
	          	  .append(str_FlowID+"' and ID in (select EACTIVITY from FLOW_RUNTIME_ACTIVITY where FID='"+strRunID+"')))");
	    }
	    List<Map<String, Object>> dbset = userMapper.selectListMapExecSQL(addBuf.toString());
	    int length = dbset != null ? dbset.size() : 0;
	    addBuf.delete(0, addBuf.length());//清空
	    if (length > 0) {
	    	for (int i = 0; i < length; i++) {
	    		Map<String, Object> retmap = dbset.get(i);
	    		strButtonName = retmap.get("NAME").toString();
	    		strButtonIco = retmap.get("ICO").toString();
	    		strButtonProperty = retmap.get("PROPERTY") != null ? retmap.get("PROPERTY").toString() : "";
	    		if (strbuttontype.equals("1")) {
	    			addBuf.append(clickButton(strButtonName, strButtonProperty, strButtonIco));
	    		} else {
	    			addBuf.append(clickButton1(strButtonName, strButtonProperty, strButtonIco));
	    		}
	      }
	      dbset = null;//赋空值
	    }
	    return addBuf.toString();  
	}

	@Override
	public String getNextActivityID(String str_ActivityID, ActFlowRun ActFlowRun, SessionUser user) throws Exception {
	    try {
	        String strCID = "", strWhere = "";
	        boolean isWhere = true;
	        StringBuffer addBuf = new StringBuffer();
	        addBuf.append("Select a.CID,a.WHERE1 From FLOW_CONFIG_ACTIVITY_CONNE a,FLOW_CONFIG_ACTIVITY b "
	        			+ "where a.CID=b.ID and (a.SID='"+str_ActivityID+"' or a.EID='"+str_ActivityID
	        			+ "') and  b.ORDER1 <>'9999' order by b.ORDER1");
	        
	        List<Map<String, Object>> dbset = userMapper.selectListMapExecSQL(addBuf.toString());
	        int length = dbset != null ? dbset.size() : 0;
	        addBuf.delete(0,addBuf.length());//清空
	        if (length > 0) {
	          for (int i = 0; i < length; i++) {
	        	Map<String, Object> retmap = dbset.get(i);
	            strCID = retmap.get("CID").toString();
	            if (retmap.get("WHERE1") != null) {
	            	strWhere = retmap.get("WHERE1").toString();
	            }
	            if (strCID.length() > 0) {
	              if (strWhere.length() > 0) {
	                LOGGER.info("解释语句开始...");
	                isWhere = getConditionValueByExpression(strWhere,ActFlowRun,user);
	                if (isWhere) {
	                   if (getFlowActivityOrder(str_ActivityID)<=getFlowActivityOrder(strCID)) {
	                       addBuf.append(strCID).append(",");
	                   }
	                   LOGGER.info("解释语句结束:", "true");
	                } else {
	                   LOGGER.info("解释语句结束:", "flase");
	                }
	              } else {
	                if (getFlowActivityOrder(str_ActivityID)<=getFlowActivityOrder(strCID)) {
	                     addBuf.append(strCID).append(",");
	                }
	              }
	            }
	          }
	          if (addBuf.toString().length() > 0) {
	            return addBuf.toString().substring(0, addBuf.toString().length() - 1);
	          }
	          dbset=null;//赋空值
	        }
	        return "";
        } catch (Exception ex) {
             LOGGER.error("流程引擎---GetNextActivityID函数出错：", ex);
             return "";
        }
	}

	@Override
	public String getUpSelectActivityID(String str_ActivityID, ActFlowRun ActFlowRun, 
			SessionUser user) throws Exception {
	    try {
	        String strCID = "", strWhere = "";
	        boolean isWhere = true;
	        StringBuffer addBuf = new StringBuffer();
	        addBuf.append("Select a.CID,a.WHERE1 From FLOW_CONFIG_ACTIVITY_CONNE a,FLOW_CONFIG_ACTIVITY b where a.CID=b.ID and (a.EID='")
	              .append(str_ActivityID).append("' or a.SID='").append(str_ActivityID).append("') and b.ORDER1 <>'9999' order by b.ORDER1");
	        
	        List<Map<String, Object>> dbset = userMapper.selectListMapExecSQL(addBuf.toString());
	        int length = dbset != null ? dbset.size() : 0;
	        addBuf.delete(0, addBuf.length());//清空
	        if (length > 0) {
	          for (int i = 0; i < length; i++) {
	        	Map<String, Object> retmap = dbset.get(i);
	            strCID = retmap.get("CID").toString();
	            strWhere = "";
	            if (retmap.get("WHERE1") != null) {
	            	strWhere = retmap.get("WHERE1").toString();
	            }
	            if (strCID.length() > 0) {
	              if (strWhere.length() > 0) {
	                LOGGER.info("解释语句开始:");
	                isWhere = getConditionValueByExpression(strWhere,ActFlowRun,user);
	                if (isWhere) {
	                  if (getFlowActivityOrder(str_ActivityID)>getFlowActivityOrder(strCID)) {
	                       addBuf.append(strCID).append(",");
	                  }
	                  LOGGER.info("解释语句结束:", "true");
	                } else {
	                  LOGGER.info("解释语句结束:", "flase");
	                }
	              } else {
	                if (getFlowActivityOrder(str_ActivityID)>getFlowActivityOrder(strCID))
	                     {addBuf.append(strCID).append(",");}
	              }
	            }
	          }
	          if (addBuf.toString().length() > 0) {
	             return addBuf.toString().substring(0, addBuf.toString().length() - 1);
	          }
	          dbset=null;//赋空值
	        }
	        return "";
        } catch (Exception ex) {
             LOGGER.error("流程引擎---GetUpSelectActivityID函数出错：", ex);
             return "";
        }
	}

	@Override
	public String getNextIFlowID(String str_ActivityID, ActFlowRun ActFlowRun, SessionUser user) throws Exception {
	    try {
	        String strCID = "", strIdentification = "";
	        String strWhere = "";
	        boolean isWhere = true;
	        StringBuffer addBuf = new StringBuffer();
	        addBuf.append("Select a.CID,a.WHERE1,b.DESC1,c.IDENTIFICATION From FLOW_CONFIG_ACTIVITY_CONNE a left join FLOW_CONFIG_ACTIVITY b on a.CID=b.ID left join FLOW_CONFIG_PROCESS c on b.desc1=c.id "
	        			+ "where (a.SID in ("+str_ActivityID+") or a.EID in ("+str_ActivityID+")) and b.ORDER1=9999 order by a.CID");
	        List<Map<String, Object>> dbset = userMapper.selectListMapExecSQL(addBuf.toString());
	        		//dbengine.QuerySQL(addBuf.toString(),"flow26",str_ActivityID);
	        addBuf.delete(0,addBuf.length());//清空
	        if (dbset != null && dbset.size() > 0) {
	          int length = dbset.size();
	          addBuf.delete(0,addBuf.length());//清空
	          for (int i = 0; i < length; i++) {
	        	Map<String, Object> retmap = dbset.get(i);
	            strCID = retmap.get("CID").toString();
	            if (retmap.get("WHERE1") != null) {
	            	strWhere = retmap.get("WHERE1").toString();
	            }
	            if (strCID==null){strCID="";}
	            if (strCID.length() > 0) {
	              strIdentification = retmap.get("IDENTIFICATION").toString();
	              LOGGER.info("Identification:", strIdentification);
	              if (strWhere==null){strWhere="";}
	              if (strWhere.length() > 0) {
	            	LOGGER.info("解释语句开始:", "");
	                isWhere = getConditionValueByExpression(strWhere,ActFlowRun,user);
	                if (isWhere) {
	                    addBuf.append(strIdentification+",");
	                    LOGGER.info("解释语句结束:", "true");
	                } else {
	                	LOGGER.info("解释语句结束:", "flase");
	                }
	              } else {
	            	  addBuf.append(strIdentification+",");
	              }
	            }
	          }
	          if (addBuf.toString().length() > 0) {
	             return addBuf.toString().substring(0, addBuf.length() - 1);
	          }
	          dbset=null;//赋空值
	        }
	        return "";
        } catch (Exception ex) {
             LOGGER.info("流程引擎---getNextIFlowID函数出错：", ex);
             return "";
        }
	}

	@Override
	public String getNextActivityTypeList(String strNode_No_S, String strNode_No) throws Exception {
	    String returnValue = "";
	    String strInType = "0";
	    String strNode_S_No = "";
	    StringBuffer addBuf = new StringBuffer();
	    //得到接收类型
	    if (strNode_No.length()>0) {
	        addBuf.append("Select CID,TYPE From FLOW_CONFIG_ACTIVITY_CONNE where (SID='"+strNode_No_S)
	        	  .append("' or EID='").append(strNode_No_S).append("') and CID in ("+strNode_No+")");
	        List<Map<String, Object>> dbset = userMapper.selectListMapExecSQL(addBuf.toString());
	        int length = dbset != null ? dbset.size() : 0;
		    addBuf.delete(0,addBuf.length());//清空
		    if (length > 0) {
		        for(int i=0; i<length; i++) {
		        	Map<String, Object> retmap = dbset.get(i);
		            strNode_S_No = retmap.get("CID").toString();
		            strInType = retmap.get("TYPE").toString();
		            addBuf.append(strNode_S_No).append("/").append(strInType).append(",");
		        }
		        dbset=null;//赋空值
		    }
		    //----------
		    returnValue = addBuf.toString();
		    if (returnValue.length()>0) {
		    	returnValue=returnValue.substring(0,returnValue.length()-1);
		    }
	    }
	    return returnValue;  
	}

	@Override
	public String getUserIdeaList(String strCurrUserNo) throws Exception {
	    StringBuffer addBuf = new StringBuffer();
	    addBuf.append("Select CONTENT from BPIP_USER_IDEA where USER_NO='"+strCurrUserNo+"'");
	    List<Map<String, Object>> dbset = userMapper.selectListMapExecSQL(addBuf.toString());
	    addBuf.delete(0,addBuf.length());//清空
	    addBuf.append("<select name='selectIdea' class='SelectStyle' size=1 >\r\n");
	    if (dbset != null && dbset.size()>0) {
	      int length = dbset.size();
	      for (int i = 0; i < length; i++) {
	    	  Map<String, Object> retmap = dbset.get(i);
	    	  addBuf.append("<option value='"+retmap.get("CONTENT").toString())
	            	.append("'>"+retmap.get("CONTENT").toString()+"</option>\r\n");
	      }
	      dbset=null;//赋空值
	    }
	    addBuf.append("</select>\r\n");
	    return addBuf.toString();  
	}

	@Override
	public int getActivityFAttNum(String strFLOWID, String strFORMID, String strID) throws Exception {
	    int returnValue = 0;
	    StringBuffer addBuf = new StringBuffer();
	    addBuf.append("Select count(ID) as NUM From BPIP_ATT where FLOWID ='"+strFLOWID)
	          .append("' and FORMID='"+strFORMID+"' and AID='"+strID+"'");
	    List<Map<String, Object>> dbset = userMapper.selectListMapExecSQL(addBuf.toString());
	    if (dbset != null && dbset.size() > 0) {
	        returnValue = Integer.parseInt(dbset.get(0).get("NUM").toString());
	        dbset = null;//赋空值
	    }
	    return returnValue;  
	}

	@Override
	public String getActivityDoUserList(String strExecuteNo, String strCNodeNo, String strNodeNo, 
			ActFlowRun ActFlowRun, SessionUser user) throws Exception {
	    try {
	        String strValue = "", strConnID = "", strUserlist = "";
	        String strUnitID = user.getUnitID();
	        String strUserID = user.getUserID();
	        StringBuffer addBuf = new StringBuffer();
	        //得到当前流程流向的接收人类型
	        addBuf.append("Select ID,TYPE From FLOW_CONFIG_ACTIVITY_CONNE where (SID='"+strCNodeNo)
	            .append("' or EID='"+strCNodeNo+"') and CID='"+strNodeNo+"'");
	        List<Map<String, Object>> dbset = userMapper.selectListMapExecSQL(addBuf.toString());
	        if (dbset != null && dbset.size() > 0) {
	        	if (dbset.get(0) != null) {
	        		strConnID = dbset.get(0).get("ID") != null ? dbset.get(0).get("ID").toString() : "";
	        		strValue = dbset.get(0).get("TYPE") != null ? dbset.get(0).get("TYPE").toString() : "";
	        	}
	            //类型为本人
	            if (strValue.equals("1")) {
	              strUserlist = strUserID;
	              LOGGER.info("接收人类型为本人，接收人是：", strUserlist);
	            }
	            //类型为本部门的人
	            if (strValue.equals("2")) {
	              //得到本部门的所有人员字符串
	              strUserlist = getCDeptUser(strUnitID);
	              LOGGER.info("接收人类型为本部门的所有人员，接收人是：", strUserlist);
	            }
	            //类型为上一处理人
	            if (strValue.equals("3")) {
	              //得到上一步骤的处理人
	              strUserlist = getFlowUpDoUser(strExecuteNo);
	              LOGGER.info("接收人类型为上一处理人，接收人是：", strUserlist);
	            }
	            //类型为本流程的创建人
	            if (strValue.equals("4")) {
	              FLOW_RUNTIME_PROCESS Fprocess = null;
	              Fprocess = getFlowRuntimeprocess(strExecuteNo);
	              //得到本流程的创建人
	              if (Fprocess!=null) {
	            	  strUserlist = Fprocess.getCREATEPSN();
	              }
	              LOGGER.info("接收人类型为本流程的创建人，接收人是：", strUserlist);
	            }
	            //类型为某一活动的处理人
	            if (strValue.equals("5")) {
	              //得到相关活动的处理人
	              strUserlist = getSelectActivityDoUser(strExecuteNo,strConnID,ActFlowRun);
	              LOGGER.info("接收人类型为某一活动的处理人，接收人是：", strUserlist);
	              //------------------
	            }
	            //类型为其它(流程组)(将启用流程活动中的可操作用户组)
	            if (strValue.equals("6") || strValue.length() == 0 ||
	                (!strValue.equals("1") && !strValue.equals("2") &&
	                 !strValue.equals("3") && !strValue.equals("4") &&
	                 !strValue.equals("5") && !strValue.equals("7") &&
	                 !strValue.equals("8") &&
	                 !strValue.equals("H") && !strValue.equals("P") && !strValue.equals("X") && !strValue.equals("M") && !strValue.equals("N") &&
	                 !strValue.equals("I"))) {
	              //得到可操作用户组的人员编号
	              strUserlist = getFlowGroupUser(strNodeNo);
	              LOGGER.info("接收人类型为其它(流程组)，接收人是：", strUserlist);
	              //----------------------
	            }
	            //类型为其它(本部门流程组)(将启用流程活动中的可操作用户组下的本部门人员)
	            if (strValue.equals("7")) {
	              //得到本部门的可操作用户组的人员编号
	              strUserlist = getFlowGroupDeptUser(strNodeNo, strUnitID);
	              LOGGER.info("接收人类型为其它(本部门流程组)，接收人是：", strUserlist);
	              //----------------------
	            }
	            
	            //本部门及上级部门流程组
	            if (strValue.equals("H")) {
	              //本部门及上级部门流程组的人员编号
	              strUserlist = getFlowGroupDeptUpUser(strNodeNo,strUnitID,"1");
	              LOGGER.info("接收人类型为本部门及上级部门流程组，接收人是：", strUserlist);
	              //----------------------
	            }
	            //本部门及下级部门流程组
	            if (strValue.equals("I")) {
	              //本部门及下级部门流程组的人员编号
	              strUserlist = getFlowGroupDeptUpUser(strNodeNo,strUnitID,"2");
	              LOGGER.info("接收人类型为本部门及上级部门流程组，接收人是：", strUserlist);
	              //----------------------
	            }
	            //本部门及同级部门流程组
	            if (strValue.equals("X")) {
	              strUserlist = getFlowGroupDeptUpUser(strNodeNo,strUnitID,"3");
	              LOGGER.info("接收人类型为本部门同级部门流程组，接收人是：", strUserlist);
	              //----------------------
	            }
	            //本部门-同级部门-上级部门流程组
	            if (strValue.equals("M")) {
	              //本部门及下级部门流程组的人员编号
	              strUserlist = getFlowGroupDeptUpUser(strNodeNo,strUnitID,"4");
	              LOGGER.info("接收人类型为本部门同级部门流程组，接收人是：", strUserlist);
	              //----------------------
	            }
	            //本部门-上级同级部门流程组
	            if (strValue.equals("N")) {
	              //本部门及下级部门流程组的人员编号
	              strUserlist = getFlowGroupDeptUpUser(strNodeNo,strUnitID,"5");
	              LOGGER.info("接收人类型为本部门上级同级部门流程组，接收人是：", strUserlist);
	              //----------------------
	            }
	            dbset = null;//赋空值
	        }
	        return strUserlist;
        } catch (Exception ex) {
             LOGGER.error("流程引擎---GetActivityDoUserList函数出错：", ex);
             return "";
        }      
	}

	@Override
	public String getShowUnitUserList(String strUnitID, String strListName, String ico, String strtype) throws Exception {
	    try {
	        String strNo = "";
	        String strName = "";
	        String strLongUnitName = "";
	        String strYLongUnitName = "";
	        String strUNITID = "";
	        
	        int k = 0;
	        int row = 0;
	        StringBuffer addBuf = new StringBuffer();
	        StringBuffer tmpBuf = new StringBuffer();
	        
	        String strcheck = "";
	        String strchecked = "";
	        if (strtype.equals("1")) {
	        	strcheck = "radio";
	        } else {
	        	strcheck = "checkbox";
	        }
	        addBuf.append("<table cellSpacing='0' cellPadding='0' border='0' width='100%'>\r\n");
            StringWork sw = new StringWork();
            //得到去掉0后的单位编码
            String strZunit = sw.CutLastZero(strUnitID,2);
            //检测去0后的单位下人数
            tmpBuf.append("Select count(USERID) as num From BPIP_USER where USERSTATE='0' and UNITID like '"+strZunit+"%'");
            List<Map<String, Object>> dbset0 = userMapper.selectListMapExecSQL(tmpBuf.toString());
            int rownum = 0;
            if (dbset0 != null && dbset0.size()>0) {
                 rownum = Integer.parseInt(dbset0.get(0).get("num").toString());
            }
            tmpBuf.delete(0,tmpBuf.length());//清空
            if (rownum<450) {//450个用户以下可以一次选择
                tmpBuf.append("Select a.USERID,a.NAME,b.UNITNAME,b.UNITID From BPIP_USER a left join BPIP_UNIT b on a.UNITID=b.UNITID where   a.USERSTATE='0' and a.UNITID like '"+strZunit+"%' order by a.UNITID,a.ORBERCODE,a.USERID");

            } else {
                tmpBuf.append("Select a.USERID,a.NAME,b.UNITNAME,b.UNITID From BPIP_USER a left join BPIP_UNIT b on a.UNITID=b.UNITID where   a.USERSTATE='0' and a.UNITID='"+strUnitID+"' order by a.UNITID,a.ORBERCODE,a.USERID");
            }
	        List<Map<String, Object>> dbset1 = userMapper.selectListMapExecSQL(tmpBuf.toString());
	        if (dbset1 != null && dbset1.size() > 0) {
	        	int length1 = dbset1.size();
	            for (int i = 0; i < length1; i++) {
	            	Map<String, Object> retmap1 = dbset1.get(i);
	                //if (i == 0) {
	                //   strchecked = "checked";
	                //}
	                //else {
	                    strchecked = "";
	                //}
	                strNo = retmap1.get("USERID").toString();
	                strName = retmap1.get("NAME").toString();
	                strLongUnitName = retmap1.get("UNITNAME").toString();
	                strUNITID = retmap1.get("UNITID").toString();
	                if (strNo.length() > 0) {
	                    if (!strtype.equals("1")) {
	                       strListName = strListName + strNo;
	                    }
	                    //----
	                    if (!strLongUnitName.equals(strYLongUnitName)) {
	                    	addBuf.append( "<tr><td colspan='3'>"+strLongUnitName+"</td></tr>\r\n");
	                        if (row>0) {
	                          if (k==1) {
	                        	  addBuf.append("<td></td><td></td>\r\n");
	                          }
	                          if (k==2) {
	                        	  addBuf.append("<td></td>\r\n");
	                          }
	                          k=3;
	                        }
	                    }
	                    //----
	                    row = row +1;
	                    k = k + 1;
	                    if (k == 4) {
	                       k = 1;
	                    }
	                    if (k == 1) {
	                       addBuf.append("<tr>\r\n");
	                    }
	                    addBuf.append("<td align='left' height='25' style='cursor:pointer ' onclick=\"selectOnclick('"+strNo)
	                          .append("');\"><input type='"+strcheck+"' "+strchecked+" onclick=\"selectOnclick('")
	                          .append(strNo+"');\" name='"+strListName+"' id='"+strNo+"'  title='"+strUNITID+"'  value='")
	                          .append(strNo+"'>"+"<img src='"+SysPreperty.getProperty().AppUrl+"Zrsysmanage/images/blueimg/")
	                          .append(ico+"' border='0' align='absmiddle'>"+strName+"</td>");
	                    
	                    if (k == 3) {
	                        addBuf.append("</tr>\r\n");
	                    }
	                    strYLongUnitName = strLongUnitName;
	                }
	                //-----------------
	              }
	              dbset1 = null;//赋空值
	        }
	        if (k==1) {
	        	addBuf.append("<td></td><td></td></table>\r\n");
	        }
	        if (k==2) {
	        	addBuf.append("<td></td></table>\r\n");
	        }
	        if (k==3) {
	        	addBuf.append("</table>\r\n");
	        }
	        return addBuf.toString();
         } catch (Exception ex) {
              LOGGER.error("流程引擎---getShowUnitUserList函数出错：", ex);
              return "";
         }      
	}

	@Override
	public String getCUnitUserList(String strUnitID, String strUSERID, String strListName, 
			String ico, String strtype) throws Exception {
      try {
          String strNo = "";
          String strName = "";
          String strLongUnitName = "";
          String strYLongUnitName = "";
          String strUNITID = "";
          
          int k = 0;
          int row = 0;
          StringBuffer addBuf = new StringBuffer();
          StringBuffer tmpBuf = new StringBuffer();
          
          String strcheck = "";
          String strchecked = "";
          if (strtype.equals("1")) {
            strcheck = "radio";
          }
          else {
            strcheck = "checkbox";
          }
          addBuf.append("<table cellSpacing='0' cellPadding='0' border='0' width='100%'>\r\n");
          StringWork sw = new StringWork();
          //得到去掉0后的单位编码
          String strZunit = sw.CutLastZero(strUnitID,2);
          //检测去0后的单位下人数
          tmpBuf.append("Select count(USERID) as num From BPIP_USER where  USERSTATE='0' and UNITID like '"+strZunit+"%'");
          List<Map<String, Object>> dbset0 = userMapper.selectListMapExecSQL(tmpBuf.toString());
          int rownum = 0;
          if (dbset0 != null && dbset0.size()>0) {
               rownum = Integer.parseInt(dbset0.get(0).get("num").toString());
          }
          tmpBuf.delete(0,tmpBuf.length());//清空
          if (rownum < 450) {//450个用户以下可以一次选择
              tmpBuf.append("Select a.USERID,a.NAME,b.UNITNAME,b.UNITID From BPIP_USER a left join BPIP_UNIT b on a.UNITID=b.UNITID where    a.USERSTATE='0' and a.USERID<>'"+strUSERID+"' and a.UNITID like '"+strZunit+"%' order by a.UNITID,a.ORBERCODE,a.USERID");
          } else {
              tmpBuf.append("Select a.USERID,a.NAME,b.UNITNAME,b.UNITID From BPIP_USER a left join BPIP_UNIT b on a.UNITID=b.UNITID where    a.USERSTATE='0' and a.USERID<>'"+strUSERID+"' and a.UNITID='"+strUnitID+"' order by a.UNITID,a.ORBERCODE,a.USERID");
          }
          List<Map<String, Object>> dbset1 = userMapper.selectListMapExecSQL(tmpBuf.toString());
          if (dbset1 != null && dbset1.size() > 0) {
        	  int length1 = dbset1.size();
              for (int i = 0; i < length1; i++) {
            	  Map<String, Object> retmap1 = dbset1.get(i);
                  strchecked = "";
                  strNo = retmap1.get("USERID").toString();
                  strName = retmap1.get("NAME").toString();
                  strLongUnitName = retmap1.get("UNITNAME").toString();
                  strUNITID = retmap1.get("UNITID").toString();
                  if (strNo.length() > 0) {
                      if (!strtype.equals("1")) {
                         strListName = strListName + strNo;
                      }
                      //----
                      if (!strLongUnitName.equals(strYLongUnitName)) {
                          addBuf.append("<tr><td colspan='3'><input type='checkbox' onclick=DeptOnclick('"+strUNITID+"'); name='"+strUNITID+"' id='"+strUNITID+"' value='"+strUNITID+"'>").append(strLongUnitName).append("</td></tr>\r\n");
                          //addBuf.append("<tr><td colspan='3'>"+strLongUnitName+"(<a href='#' onclick='selectallPm("+strUNITID+");'>全选</a>&nbsp;&nbsp;<a href='#' onclick='selectnotBM("+strUNITID+");'>取消全选</a>)</td></tr>\r\n");
                          if (row > 0) {
                            if (k==1) {
                            	addBuf.append("<td></td><td></td>\r\n");
                            }
                            if (k==2) {
                            	addBuf.append("<td></td>\r\n");
                            }
                            k=3;
                          }
                      }
                      //----
                      row = row +1;
                      k = k + 1;
                      if (k == 4) {
                         k = 1;
                      }
                      if (k == 1) {
                         addBuf.append("<tr>\r\n");
                      }
                      addBuf.append("<td align='left' height='25' style='cursor:pointer ' onclick=\"selectOnclick('"+strNo)
                            .append("');\"><input type='"+strcheck+"' "+strchecked+" onclick=\"selectOnclick('")
                            .append(strNo+"');\" name='"+strListName+"' id='"+strNo+"' title='"+strUNITID+"' value='")
                            .append(strNo+"'>"+"<img src='"+SysPreperty.getProperty().AppUrl+"Zrsysmanage/images/blueimg/")
                            .append(ico+"' border='0' align='absmiddle'>"+strName)
                            .append("</td>");
                      if (k == 3) {
                          addBuf.append("</tr>\r\n");
                      }
                      strYLongUnitName = strLongUnitName;
                  }
                //-----------------
               }
          }
          dbset1 = null;//赋空值
          if (k==1) {
            addBuf.append("<td></td><td></td></table>\r\n");
          }
          if (k==2) {
            addBuf.append("<td></td></table>\r\n");
          }
          if (k==3) {
             addBuf.append("</table>\r\n");
          }
          return addBuf.toString();
       } catch (Exception ex) {
            LOGGER.error("流程引擎---getCUnitUserList函数出错：", ex);
            return "";
       }
	}

	@Override
	public String getShowRolesUserList(String strRoles, String strListName, String ico, String strtype,
			String strUnitid, String sunitid, String isdept) throws Exception {
	    try {
	        String strNo = "";
	        String strName = "";
	        String strLongUnitName = "";
	        String strYLongUnitName = "";
	        String strUNITID = "";
	        
	        int k = 0;
	        int row = 0;
	        StringBuffer addBuf = new StringBuffer();
	        StringBuffer tmpBuf = new StringBuffer();
	        
	        String sADD = " 1=1 ";
	        String strcheck = "";
	        String strchecked = "";
	        if (strtype.equals("1")) {
	          strcheck = "radio";
	        } else {
	          strcheck = "checkbox";
	        }
	        if (sunitid.length()>0) {
	           sADD = sADD + " and a.UNITID like '"+sunitid+"%'";
	        }
	        if (isdept.equals("1")) {
	            StringWork sw = new StringWork();
	            //得到去掉0后的本单位编码
	            strUnitid = sw.CutLastZero(strUnitid,2);
	            sADD = sADD + " and a.UNITID like '"+strUnitid+"%'";
	        }
	        addBuf.append("<table cellSpacing='0' cellPadding='0' border='0' width='100%'>\r\n");
	        tmpBuf.delete(0,tmpBuf.length());//清空
            if (strRoles.length()>0) {
                tmpBuf.append("Select a.USERID,a.NAME,b.UNITNAME,b.UNITID From BPIP_USER a left join BPIP_UNIT b on a.UNITID=b.UNITID where "+sADD+"  and   a.USERSTATE='0'   and  a.USERID in (select USERID from  BPIP_USER_ROLE where ROLEID in ("+strRoles+")) order by a.UNITID,a.ORBERCODE,a.USERID");
            } else {
                tmpBuf.append("Select a.USERID,a.NAME,b.UNITNAME,b.UNITID From BPIP_USER a left join BPIP_UNIT b on a.UNITID=b.UNITID where "+sADD+"  and   a.USERSTATE='0'  order by a.UNITID,a.ORBERCODE,a.USERID");
            }
	        List<Map<String, Object>> dbset1 = userMapper.selectListMapExecSQL(tmpBuf.toString());
	        if (dbset1 != null && dbset1.size() > 0) {
	        	int length1 = dbset1.size();
	            for (int i = 0; i < length1; i++) {
	            	Map<String, Object> retmap1 = dbset1.get(i);
	                strchecked = "";
	                strNo = retmap1.get("USERID").toString();
	                strName = retmap1.get("NAME").toString();
	                strLongUnitName = retmap1.get("UNITNAME").toString();
	                strUNITID = retmap1.get("UNITID").toString();
	                if (strNo.length() > 0) {
	                    if (!strtype.equals("1")) {
	                       strListName = strListName + strNo;
	                    }
	                    //----
	                    if (!strLongUnitName.equals(strYLongUnitName)) {
	                        addBuf.append("<tr><td colspan='3'>"+strLongUnitName+"(<a href='#' onclick=\"selectallPm('"+strUNITID+"');\">全选</a>&nbsp;&nbsp;<a href='#' onclick=\"selectnotBM('"+strUNITID+"');\">取消全选</a>)</td></tr>\r\n");
	                        if (row > 0) {
	                          if (k==1) {
	                        	  addBuf.append("<td></td><td></td>\r\n");
	                          }
	                          if (k==2) {
	                        	  addBuf.append("<td></td>\r\n");
	                          }
	                          k=3;
	                        }
	                    }
	                    //----
	                    row = row +1;
	                    k = k + 1;
	                    if (k == 4) {
	                       k = 1;
	                    }
	                    if (k == 1) {
	                       addBuf.append("<tr>\r\n");
	                    }
	                    addBuf.append("<td align='left' height='25' style='cursor:pointer ' onclick=\"selectOnclick('"+strNo)
	                          .append("');\"><input type='"+strcheck+"' "+strchecked+" onclick=\"selectOnclick('")
	                          .append(strNo+"');\" name='"+strListName+"' id='"+strNo+"' title='"+strUNITID+"' value='")
	                          .append(strNo+"'>"+"<img src='"+SysPreperty.getProperty().AppUrl+"Zrsysmanage/images/blueimg/")
	                          .append(ico+"' border='0' align='absmiddle'>"+strName)
	                          .append("</td>");
	                    if (k == 3) {
	                        addBuf.append("</tr>\r\n");
	                    }
	                    strYLongUnitName = strLongUnitName;
	                }
	              //-----------------
	             }
	           dbset1=null;//赋空值
	        }
	        if (k==1) {
	        	addBuf.append("<td></td><td></td></table>\r\n");
	        }
	        if (k==2) {
	        	addBuf.append("<td></td></table>\r\n");
	        }
	        if (k==3) {
	        	addBuf.append("</table>\r\n");
	        }
	        return addBuf.toString();
         } catch (Exception ex) {
              LOGGER.error("流程引擎---getShowRolesUserList函数出错：", ex);
              return "";
         }
	}

	@Override
	public String getDataAid(String strID) throws Exception {
	    String returnValue = "";
	    StringBuffer addBuf = new StringBuffer();
	    addBuf.append("Select ID from FLOW_CONFIG_ACTIVITY where TYPE='3' and FID in (select ID from FLOW_CONFIG_PROCESS "
	    			+ "where IDENTIFICATION='"+strID+"' and FLOWPACKAGE in (select ID from FLOW_CONFIG_PACKAGE))");
	    
	    List<Map<String, Object>> dbset = userMapper.selectListMapExecSQL(addBuf.toString());
	    if (dbset != null && dbset.size() > 0) {
	    	if (dbset.get(0) != null && dbset.get(0).get("ID") != null) {
	    		returnValue = dbset.get(0).get("ID").toString();
	    	}
	        dbset = null;//赋空值
	    }
	    if (returnValue.length()==0) {
	      addBuf.delete(0,addBuf.length());//清空
	      addBuf.append("Select ID from FLOW_CONFIG_ACTIVITY where FID in (select ID from FLOW_CONFIG_PROCESS where IDENTIFICATION='")
	            .append(strID+"' and FLOWPACKAGE in (select ID from FLOW_CONFIG_PACKAGE))");
	      List<Map<String, Object>> dbset1 = userMapper.selectListMapExecSQL(addBuf.toString());
	      if (dbset1 != null && dbset1.size() > 0) {
	    	  if (dbset1.get(0) != null && dbset1.get(0).get("ID") != null) {
	    		  returnValue = dbset1.get(0).get("ID").toString();
	    	  }
	          dbset1 = null;//赋空值
	      }
	    }
	    return returnValue;  
	}

	@Override
	public String getFlowRunIDHTML(String strValue, String strPath, String strIco, 
			ActFlowRun ActFlowRun, SessionUser user) throws Exception {
	    try {
	        String strRValue = "";
	        strRValue = (String) GetFlowRunIDHTMLHashtable.get(strValue+user.getUserID());
	        if (strRValue == null) {
		        String strID = "";
		        String strFLOWRUNID = "";
		        String strFORMID = "";
		        String strFLOWNAME = "";
		        String strRID = ActFlowRun.getRID();
		        String strTmp="";
		        StringBuffer reBuf = new StringBuffer();
		        String strSubHtml="";
		        //流程标识/名称对照HashMap
		        Map<String, Object>flowNameHashtable = getFlowNameHashtable(strValue);
		        //流程标识/可新建流程数对照HashMap
		        Map<String, Object> NewNumHashtable = getFlowNewNumHashtable(strValue);
		        //流程标识/已经建立的流程数对照表
		        Map<String, Object> AlreadyNumHashtable = getFlowAlreadyNumHashtable(strValue,ActFlowRun,"");
		        //流程标识/流程下的已经走过的流程对照表
		        Map<String, Object> FlowHtmlHashtable = getFlowHtmlHashtable3(strValue,ActFlowRun);
		        
		        int FlowNewNum = 1; //流程可以新建的数量
		        int FlowcNum = 0; //当前已有的流程数量
		        List<Object> List = getArrayList(strValue, ",");
		        for (int i = 0; i < List.size(); i++) {
		          strID = List.get(i).toString();
		          //根据流程标识得到流程的名称
		          strFLOWNAME = (String) flowNameHashtable.get(strID);
		          if (strFLOWNAME == null) {strFLOWNAME = "";}
		          //得到流程可以新建的数量
		          strTmp= (String) NewNumHashtable.get(strID);
		          if (strTmp==null){FlowNewNum=0;}else{FlowNewNum=Integer.parseInt(strTmp);}
		          //得到流程已经新建的数量
		          strTmp= (String) AlreadyNumHashtable.get(strID);
		          if (strTmp==null){FlowcNum=0;}else{FlowcNum=Integer.parseInt(strTmp);}
		          
		          //组合HTML
		          if (strID.length() > 0) {
		            if (FlowcNum >= FlowNewNum && FlowNewNum!=0) {
		            	reBuf.append("<img src='").append(SysPreperty.getProperty().AppUrl)
			            	 .append("Zrsysmanage/images/blueimg/").append(strIco)
			            	 .append("' border='0' align='absmiddle'>")
			            	 .append(strFLOWNAME).append("<br>\r\n");
		            } else {
		            	reBuf.append("<img src='").append(SysPreperty.getProperty().AppUrl)
		            		 .append("Zrsysmanage/images/blueimg/").append(strIco)
		            		 .append("' border='0' align='absmiddle'>").append("<a href=javascript:OpenCLinFlowWindow('")
		                  	 .append(strPath).append("?FlowNo=").append(strID).append("&FormID=").append(strFORMID).append("&ExecuteNo=")
		                  	 .append(strFLOWRUNID).append("&RID=").append(strRID).append("') title='")
		                  	 .append(strFLOWNAME).append("'>").append(strFLOWNAME)
		                  	 .append("</a>[<font class='WriteSign'>点击新建</font>]<br>\r\n");
		            }
		            strSubHtml = (String)FlowHtmlHashtable.get(strID);
		            if (strSubHtml==null) { strSubHtml=""; }
		            reBuf.append(strSubHtml);
		          }
		        }
		        strRValue = reBuf.toString()+" ";
		        GetFlowRunIDHTMLHashtable.put(strValue+user.getUserID(),strRValue);
	        }
	        return strRValue;
	    } catch (Exception ex) {
	    	LOGGER.error("流程引擎---getFlowRunIDHTML函数出错：", ex);
	    	return "";
	    }
	}

	@Override
	public String getNeedFlowList(String strValue, String AID, ActFlowRun ActFlowRun) throws Exception {
		try {
			String strID = "";
	          String strISneed = "0";
	          String strISNew = "0";
	          String strFLOWNAME = "";
	          StringBuffer addBuf = new StringBuffer();
	          StringBuffer reBuf = new StringBuffer();
	          
	          //流程标识/名称对照Hashtable
	          Map<String, Object> flowNameHashtable = getFlowNameHashtable(strValue);
	          //流程标识/流程是否为必须建立的流程对照表
	          Map<String, Object> NeedNewFlowHashtable = getNeedNewFlowHashtable(strValue,AID);
	          
	          List<Object> List = getArrayList(strValue, ",");
	          for (int i = 0; i < List.size(); i++) {
	            strISNew = "0";
	            strID = List.get(i).toString();
	            
	            //根据流程标识和当前活动id得到流程是否为必须建立的流程
	            strISneed = (String)NeedNewFlowHashtable.get(strID);
	            if (strISneed==null){strISneed="0";}
	            
	            addBuf.delete(0,addBuf.length());//清空
	            //判断是否已经建立流程
	            addBuf.append("Select ID from FLOW_RUNTIME_PROCESS where FLOWID = '"+strID+"' and (OTHERID ='")
	                  .append(ActFlowRun.getOtherID()+"' or PARENTID ='"+ActFlowRun.getOtherID()+"' or PARENTID1 ='")
	                  .append(ActFlowRun.getOtherID()+"'");
	            
	            if (ActFlowRun.getParentID().length()>0 && !ActFlowRun.getParentID().equals("null")) {
	            	addBuf.append(" or OTHERID ='"+ActFlowRun.getParentID()+"' or PARENTID ='")
	                  	  .append(ActFlowRun.getParentID()+"' or PARENTID1 ='"+ActFlowRun.getParentID())
	                  	  .append("'");
	            }
	            if (ActFlowRun.getParentID1().length()>0 && !ActFlowRun.getParentID1().equals("null")) {
	            	addBuf.append(" or OTHERID ='"+ActFlowRun.getParentID1()+"' or PARENTID ='"+ActFlowRun.getParentID1())
	                  	  .append("' or PARENTID1 ='"+ActFlowRun.getParentID1()+"'");
	            }
	            addBuf.append(")");
	            
	            List<Map<String, Object>> dbset = userMapper.selectListMapExecSQL(addBuf.toString());
	            if (dbset != null && dbset.size() > 0) {
	                strISNew = "1";
	            }
	            //--------------------
	            if (strISNew.equals("0") && strISneed.equals("1")) {//没有新建且为必须要新建的流程时
	              //根据流程标识得到流程的名称
	              strFLOWNAME = (String) flowNameHashtable.get(strID);
	              if (strFLOWNAME == null) {strFLOWNAME = "";}
	              
	              reBuf.append(strFLOWNAME+"\r\n");
	            }
	          }
	          return reBuf.toString();
        } catch (Exception ex) {
             LOGGER.error("流程引擎---getNeedFlowList函数出错：", ex);
             return "";
        }
	}

	@Override
	public boolean flowSubmitServer(ActFlowRun ActFlowRun, SessionUser user) throws Exception {
	    try {
	        boolean returnValue = true;
	        GetFlowActivityLinkHashtable.clear();
	        Vector<Object> mVec = new Vector<Object>();//存放所有SQL语句
	        //检测待处理人中是否有权限委托的，有权限委托的替换成受委托人
	        String Do_User_Nos = flowEntrustJudgement(ActFlowRun);
	        if (Do_User_Nos==null) {
	        	Do_User_Nos="";
	        }
	        if (Do_User_Nos.length()>0) {
	        	ActFlowRun.setDo_User_Nos(Do_User_Nos);
        	}
	        if (ActFlowRun.getExecute_No().length() > 0) { //流程保存过
	          //更新流程流转记录
	          mVec = updateFlowRunData(ActFlowRun,user,mVec);
	        } else { //流程没有保存过
	          //插入流程流转记录
	          String strID = getFLOW_RUNTIME_PROCESS_ID();
	          mVec = InsertFlowRunData(ActFlowRun,mVec,strID);
	        }
	        //将所有SQL装入数组
	        String[] sqls = new String[mVec.size()];
	        for(int i=0;i<mVec.size();i++){
	            sqls[i] = (String)mVec.get(i);
	        }
	        //执行相关sql
	        returnValue = dbEngine.ExecuteSQLs(sqls);
	        return returnValue;
        } catch (Exception ex) {
             LOGGER.error("流程引擎---flowSubmitServer函数出错：", ex);
             return false;
        }
	}

	@Override
	public String flowSaveServer(ActFlowRun ActFlowRun) throws Exception {
	    String strID = "";
	    try {
		    GetFlowActivityLinkHashtable.clear();
		    String returnValue = "";
		    Vector<Object> mVec = new Vector<Object>();//存放所有SQL语句
		    if (ActFlowRun.getExecute_No().length() > 0) { //流程保存过
		          //保存业务数据
		          //----------------------只保存处理意见----------------------
		          StringBuffer addBuf = new StringBuffer();
		          addBuf.append("update FLOW_RUNTIME_ACTIVITY set DOIDEA='"+ActFlowRun.getDoIdea()+"' where FID='")
		                .append(ActFlowRun.getExecute_No()+"' and EACTIVITY='"+ActFlowRun.getNode_No_S())
		                .append("' and DOPSN='"+ActFlowRun.getUserNo()+"' and DOFLAG='0'");
		          userMapper.updateExecSQL(addBuf.toString());
		          returnValue = ActFlowRun.getExecute_No();
		    } else {
		        //流程没有保存过
		        //插入流程流转记录
		        strID = getFLOW_RUNTIME_PROCESS_ID();
		        mVec = InsertFlowRunData(ActFlowRun,mVec,strID);
		        //将所有SQL装入数组
		        String[] sqls = new String[mVec.size()];
		        for(int i=0;i<mVec.size();i++){
		            sqls[i] = (String)mVec.get(i);
		        }
		        //执行相关sql
		        dbEngine.ExecuteSQLs(sqls);
		        
		        returnValue = strID;
		    }
		    return returnValue;
	    } catch (Exception ex) {
	         LOGGER.error("流程引擎---flowSaveServer函数出错：", ex);
	         return "";
	    }
	}

	@Override
	public boolean flowSubDeptServer(ActFlowRun ActFlowRun, SessionUser user) throws Exception {
	    boolean returnValue = true;
	    Vector<Object> mVec = new Vector<Object>();//存放所有SQL语句
		//得到流程运转参数-----------------
		String Workflow_No = ActFlowRun.getWorkflow_No();
		String Node_No_S = ActFlowRun.getNode_No_S();
		String UserNo = ActFlowRun.getUserNo();
		String Execute_No = ActFlowRun.getExecute_No();
		String DoIdea = ActFlowRun.getDoIdea();
		String Do_User_Nos=ActFlowRun.getDo_User_Nos();
		StringBuffer addBuf = new StringBuffer();
		
		//得到流程运转参数-----------------
		FLOW_CONFIG_PROCESS Process = getFlowProcess1(Workflow_No);
		String TitleName1 ="";
		if (Process != null) {
		   TitleName1 = Process.getNAME();
		}
		FLOW_CONFIG_ACTIVITY activity = null;
		activity = getFlowActivity(Node_No_S);
		//----------------在待处理人中去掉当前人员且加个部门传阅的人员----------------
		//----------------得到流程运转的相关参数----------------
		String DoUserList="";
		String TmpUser="";
		String strFUID = "";
		
		FLOW_RUNTIME_PROCESS Fprocess = getFlowRuntimeprocess(Execute_No);
		DoUserList = Fprocess.getACCEPTPSN();
		strFUID = Fprocess.getFUID();
		if (strFUID==null) {
			strFUID="";
		}		
		List<Object> UserList = getArrayList(DoUserList, ",");
		DoUserList = "";
		for (int i = 0; i < UserList.size(); i++) {
			TmpUser = UserList.get(i).toString();
			if (!TmpUser.equals(UserNo)) {
				DoUserList = DoUserList + TmpUser + ",";
			}
		}
		if (DoUserList.length() > 0) {
			DoUserList = DoUserList.substring(0, DoUserList.length() - 1);
		}
		if (DoUserList.length()>0) {
			DoUserList = DoUserList+","+Do_User_Nos;
		} else {
			DoUserList = Do_User_Nos;
		}
		//-------------在待处理人中去掉当前人员且加个部门传阅的人员结束-------------
		//在待处理人中增加选择的承办人----
		
		if (strFUID.indexOf(user.getUserID())==-1) {
		  strFUID = strFUID + ","+user.getUserID();
		}
		if (strFUID.length()>2000) {
		  strFUID = strFUID.substring(0,2000);
		}
		addBuf.delete(0,addBuf.length());//清空
		addBuf.append("update FLOW_RUNTIME_PROCESS set ACCEPTPSN='"+DoUserList+"',FUID='"+strFUID
					+"' where ID='"+Execute_No).append("'");
		mVec.add(addBuf.toString());
		
		Fprocess.setACCEPTPSN(DoUserList);
		Fprocess.setFUID(strFUID);
		FLOW_RUNTIME_PROCESSHashtable.put(Execute_No,Fprocess);
		//-----------------------------
		//-----增加每一个人的处理步骤------
		UserList = getArrayList(Do_User_Nos, ",");
		String strDoUser ="";
		String strRunID ="";
		String strConnName ="";
		
		for (int i = 0; i < UserList.size(); i++) {
		 strDoUser = UserList.get(i).toString();
		 //计算流程流转过程表的ID
		 strRunID = getFLOW_RUNTIME_ACTIVITY_ID();
		 //根据开始活动和结束活动得到关系的名称
		 strConnName = getActivityConnName(Node_No_S, Node_No_S);
		 
		 //由于开始活动和结束活动得到关系的名称未设置，故暂时将开始活动的名称设为流转名称，这样在审批记录中可查看
		 if (Process!=null && activity!=null)
		 {
			  addBuf.delete(0,addBuf.length());//清空
			  addBuf.append(Process.getNAME()).append("→").append(activity.getNAME());
			  strConnName=addBuf.toString();
		 }
		 //---------------------------------------
		 addBuf.delete(0,addBuf.length());//清空
		 addBuf.append("Insert into FLOW_RUNTIME_ACTIVITY(ID,FID,SACTIVITY,EACTIVITY,NAME,DOPSN,DOFLAG,DOIP,SENDPSN,SENDDATE) values ('")
		       .append(strRunID).append("','").append(Execute_No).append("','").append(Node_No_S).append("','").append(Node_No_S)
		       .append("','").append(strConnName).append("','").append(strDoUser).append("','0','','").append(user.getUserID()).append("',sysdate)");
	
		 mVec.add(addBuf.toString());
	   }
	   //更新当条流程流转记录的相关字段(处理时间、处理意见、处理状态)
	   String activityName="";
	   if (activity!=null) {
		   activityName = activity.getNAME();
	   }
       addBuf.delete(0,addBuf.length());//清空
       addBuf.append("update FLOW_RUNTIME_ACTIVITY set DODATE=sysdate,DOIDEA='").append(DoIdea)
       		 .append("',DOFLAG='1',DOIP='',NAME='").append(TitleName1)
       		 .append("→").append(activityName).append("' where FID='").append(Execute_No).append("' and EACTIVITY='")
       		 .append(Node_No_S).append("' and DOPSN='").append(UserNo).append("' and DOFLAG='0'");
       
       mVec.add(addBuf.toString());
       //将所有SQL装入数组
	   String[] sqls = new String[mVec.size()];
	   for(int i=0;i<mVec.size();i++){
	      sqls[i] = (String)mVec.get(i);
	   }
	   //执行相关sql
	   returnValue = dbEngine.ExecuteSQLs(sqls);
	   
	   return returnValue;  
	}

	@Override
	public boolean flowSendFinishServer(ActFlowRun ActFlowRun) throws Exception {
        boolean returnValue = false;
        GetFlowActivityLinkHashtable.clear();
        //得到流程运转参数-----------------
        String Node_No_S = ActFlowRun.getNode_No_S();
        String UserNo = ActFlowRun.getUserNo();
        String Execute_No = ActFlowRun.getExecute_No();
        String DoIdea = ActFlowRun.getDoIdea();
        String strSQL = "";
        
        //更新当条流程流转记录的相关字段(处理时间、处理意见、处理状态)
        strSQL = "update FLOW_RUNTIME_ACTIVITY set DODATE=sysdate, DOIDEA='"+DoIdea+"', DOFLAG='1' "
        	   + "where FID='"+Execute_No+"' and EACTIVITY='"+Node_No_S+"' and DOPSN='"+UserNo+"' and DOFLAG='0'";
        Integer retInt = userMapper.updateExecSQL(strSQL);
        if (retInt != null && retInt > 0) {
        	returnValue = true;
        }
        /*
        //去掉流程流转记录中的当前处理人
        Vector mVec1 = new Vector();//存放所有SQL语句
        mVec1 = UpdateFlowRunTimeDoUser(ActFlowRun,mVec1);

        //将所有SQL装入数组
        String[] sqls1 = new String[mVec1.size()];
        for(int i=0;i<mVec1.size();i++){
          sqls1[i] = (String)mVec1.get(i);
        }
        //执行相关sql
        returnValue = dbengine.ExecuteSQLs(sqls1);
        */
        return returnValue;
	}

	@Override
	public boolean dosend(String ExecuteNo, String sid, String cid, String TitleName, 
			String users, String userid, String username) throws Exception {
        boolean returnValue =true;
        GetFlowActivityLinkHashtable.clear();
        String strDoUser = "";
        String strRunID = "";
        String strSQL = "";
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String mDateTime = formatter.format(cal.getTime());
        
        String strSms = "";
        strSms = users.split("/")[1];
        users = users.split("/")[0];
        
        //检测待处理人中是否有权限委托的，有权限委托的替换成受委托人
        users = flowEntrustJudgement(users);
        if (users==null) { users=""; }
        if (strSms.length()>180) {
        	strSms=strSms.substring(0,180)+"...";
        }
        TitleName = "["+TitleName+"→分送]["+username+"]["+mDateTime+"]";
        String strSMaxN0A = "";
        String strMobile = "";
        
        List<Object> UserList = getArrayList(users, ",");
        for (int i = 0; i < UserList.size(); i++) {
             strDoUser = UserList.get(i).toString();
             strRunID = getFLOW_RUNTIME_ACTIVITY_ID();
             //处理IP字段设置为收到分送的文件后用什么活动来显示的活动ID，用于区分是否是分送出来的文件。
             strSQL ="Insert into FLOW_RUNTIME_ACTIVITY(ID,FID,SACTIVITY,EACTIVITY,NAME,DOPSN,DOFLAG,DOIP,SENDPSN,SENDDATE,DOIDEA) values ('"
            		 +strRunID+"','"+ExecuteNo+"','"+cid+"','"+sid+"','"+TitleName+"','"+strDoUser+"','0','"+sid+"','"+userid+"',sysdate,'send')";
             //mVec.add(strSQL);
             userMapper.insertExecSQL(strSQL);
             
             //--------------------短信--------------------
             if (strSms.length()>0 && !strSms.equals("no")) {
                  strSMaxN0A = getBPIP_HANDSET_ID();
                  strMobile = getUserMobile(strDoUser);//得到处理人的手机号
                  strSQL ="Insert into BPIP_HANDSET(ID,USERNO,MOBILE,CONTENT,SENDDATE,FINISHDATE,TYPEBOX,SENDTYPE,MONTH,WEEK,DAY,HOUR,MINUTE,ISSEND,TTABLEID,SUSERNO) values ('"
                		  +strSMaxN0A+"','"+userid+"','"+strMobile+"','"+strSms+"',sysdate,sysdate,5,0,0,0,0,0,0,'0','"+strRunID+"','"+strDoUser+"')";
                  
                  userMapper.insertExecSQL(strSQL);
             }
             //----------------------
        }
        return returnValue;
	}

	@Override
	public boolean flowFinishServer(ActFlowRun ActFlowRun, SessionUser user) throws Exception {
	    try {
	        boolean returnValue = true;
	        GetFlowActivityLinkHashtable.clear();
	        Vector<Object> mVec = new Vector<Object>();//存放所有SQL语句
	        //-----------------得到流程运转参数-----------------
	        String Workflow_No = ActFlowRun.getWorkflow_No();
	        String Node_No_S = ActFlowRun.getNode_No_S();
	        String UserNo = ActFlowRun.getUserNo();
	        String Execute_No = ActFlowRun.getExecute_No();
	        String TitleName = ActFlowRun.getTitleName();
	        String DoIdea = ActFlowRun.getDoIdea();
	        StringBuffer  addBuf = new   StringBuffer();
	        
	        //-----------------得到流程运转参数-----------------
	        FLOW_CONFIG_PROCESS Process = getFlowProcess1(Workflow_No);
	        String TitleName1 ="";
	        if (Process!=null) {
	            TitleName1 = Process.getNAME();
	        }
	        if (TitleName.length() == 0) {
	          addBuf.append(TitleName1).append("→完成");
	          TitleName = addBuf.toString();
	        } else {
	            if (Process!=null) {
	              addBuf.append(TitleName).append("→").append(Process.getNAME()).append("→完成");
	              TitleName = addBuf.toString();
	            } else {
	              addBuf.append(TitleName).append("→完成");
	              TitleName = addBuf.toString();
	            }
	        }
	        //---------------------------处理多个待处理人时---------------------------
	        //--------得到当前流转的待处理人列表--------
	        String strCUserNo = "";
	        int UserNum = 0;
	        FLOW_RUNTIME_PROCESS Fprocess = null;
	        Fprocess = getFlowRuntimeprocess(Execute_No);
	        
	        String strFUID = Fprocess.getFUID();
	        if (strFUID==null) {
	        	strFUID="";
	        }
	        if (Fprocess!=null) {
	        	strCUserNo = Fprocess.getACCEPTPSN();
	        }
	        LOGGER.info("flowFinishServer:得到当前流转的待处理人列表:"+strCUserNo);
	        List<Object> UserList = getArrayList(strCUserNo, ",");
	        //-------------待处理人数量-------------
	        UserNum = UserList.size();
	        //--***
	        FLOW_CONFIG_ACTIVITY activity = null;
	        activity = getFlowActivity(Node_No_S);
	        //根据活动ID得到活动的完成策略类型
	        String strCstrategyType = "";
	        if (activity!=null) {
	        	strCstrategyType = activity.getCSTRATEGY();
	        }
	        //--***
	        if (strCstrategyType.equals("2")) { //必须完成所有
		        if (UserNum > 1)//有多个待处理人时只需去掉当前处理人
		        {
		            //去掉流程流转记录中的当前处理人
		            Vector<Object> mVec1 = new Vector<Object>();//存放所有SQL语句
		            mVec1 = updateFlowRunTimeDoUser(ActFlowRun,mVec1);
		            //更新当条流程流转记录的相关字段(处理时间、处理意见、处理状态)
		            
		            String activityName="";
		            if (activity!=null) {activityName = activity.getNAME();}
		            
		            addBuf.delete(0,addBuf.length());//清空
		            addBuf.append("update FLOW_RUNTIME_ACTIVITY set DODATE=sysdate,DOIDEA='").append(DoIdea)
		            	  .append("',DOFLAG='1',NAME='").append(TitleName1)
		            	  .append("→").append(activityName).append("' where FID='").append(Execute_No).append("' and EACTIVITY='")
		            	  .append(Node_No_S).append("' and DOPSN='").append(UserNo).append("' and DOFLAG='0'");
		            
		            mVec1.add(addBuf.toString());
		            
		            //将所有SQL装入数组
		            String[] sqls1 = new String[mVec1.size()];
		            for(int i=0;i<mVec1.size();i++){
		               sqls1[i] = (String)mVec1.get(i);
		            }
		            //执行相关sql
		            returnValue = dbEngine.ExecuteSQLs(sqls1);
		            return returnValue;
		        }
		        //---------------------处理多个待处理人结束---------------------
	        }
	        //更新当条流程流转记录的相关字段(处理时间、处理意见、处理状态)
	        String activityName="";
	        if (activity!=null) {activityName = activity.getNAME();}
	        
	        addBuf.delete(0,addBuf.length());//清空
	        addBuf.append("update FLOW_RUNTIME_ACTIVITY set DODATE=sysdate,DOIDEA='").append(DoIdea)
	              .append("',DOFLAG='1',NAME='").append(TitleName1)
	              .append("→").append(activityName).append("' where FID='").append(Execute_No).append("' and EACTIVITY='")
	              .append(Node_No_S).append("' and DOPSN='").append(UserNo).append("' and DOFLAG='0'");
	        
	        mVec.add(addBuf.toString());
	        //得到结束活动的ID
	        String strEndActivityID = getEndActivityID(ActFlowRun);
	        if (strFUID.indexOf(user.getUserID())==-1) {
	           strFUID = strFUID + ","+user.getUserID();
	        }
	        if (strFUID.length()>2000) {
	           strFUID = strFUID.substring(0,2000);
	        }
	        //更新当前流转记录为完成状态
	        addBuf.delete(0,addBuf.length());//清空
	        addBuf.append("update FLOW_RUNTIME_PROCESS set NAME='").append(TitleName).append("',ACCEPTPSN='',ACCEPTPSNNUM=0,STATE='4',CURRACTIVITY='")
	              .append(strEndActivityID).append("',FDATE=sysdate,FUID='"+strFUID+"' where ID='").append(Execute_No).append("'");
	        
	        mVec.add(addBuf.toString());
	        
	        Fprocess.setNAME(TitleName);
	        Fprocess.setACCEPTPSN("");
	        Fprocess.setACCEPTPSNNUM(0);
	        Fprocess.setSTATE("4");
	        Fprocess.setCURRACTIVITY(strEndActivityID);
	        Fprocess.setFUID(strFUID);
	        FLOW_RUNTIME_PROCESSHashtable.put(Execute_No,Fprocess);
	        
	        //将所有SQL装入数组
	        String[] sqls = new String[mVec.size()];
	        for(int i=0; i<mVec.size(); i++) {
	            sqls[i] = (String)mVec.get(i);
	        }
	        //执行相关sql
	        returnValue = dbEngine.ExecuteSQLs(sqls);
	        
	        return returnValue;
        } catch (Exception ex) {
             LOGGER.error("流程引擎---flowFinishServer函数出错：", ex);
             return false;
        }
	}

	@Override
	public boolean flowDevolveServer(ActFlowRun ActFlowRun, SessionUser user) throws Exception {
	     try{
	         boolean returnValue = true;
	         GetFlowActivityLinkHashtable.clear();
	         Vector<Object> mVec = new Vector<Object>();//存放所有SQL语句
	         //检测待处理人中是否有权限委托的，有权限委托的替换成受委托人
	         String Do_User_Nos = flowEntrustJudgement(ActFlowRun);
	         if (Do_User_Nos.length()>0)
	         {ActFlowRun.setDo_User_Nos(Do_User_Nos);}

	         //设置下一步为当前步骤(移交到本步骤)
	         ActFlowRun.setM_Node_No_S_E(ActFlowRun.getNode_No_S());
	         //设置意见为移交处理
	         ActFlowRun.setDoIdea("移交处理。");
	         
	         //更新流程流转记录
	         mVec = updateFlowRunData(ActFlowRun,user,mVec);
	         //将所有SQL装入数组
	         String[] sqls = new String[mVec.size()];
	         for(int i=0;i<mVec.size();i++){
	             sqls[i] = (String)mVec.get(i);
	         }
	         //执行相关sql
	         returnValue = dbEngine.ExecuteSQLs(sqls);
	         
	         return returnValue;
        } catch (Exception ex) {
             LOGGER.error("流程引擎---flowDevolveServer函数出错：", ex);
             return false;
        }
	}

	@Override
	public boolean flowInitServer(ActFlowRun ActFlowRun, SessionUser user) throws Exception {
	     try{
	         boolean returnValue = true;
	         GetFlowActivityLinkHashtable.clear();
	         Vector<Object> mVec = new Vector<Object>();//存放所有SQL语句
	         FLOW_RUNTIME_PROCESS Fprocess = null;
	         Fprocess = getFlowRuntimeprocess(ActFlowRun.getExecute_No());
	         String strUserID ="";
	         String strCONNID = "";
	         String strFLOWNAME = "";
	         
	         //将初始化后的处理人设置成流程创建人
	         if (Fprocess!=null) {
	            strUserID = Fprocess.getCREATEPSN();
	         }
	         ActFlowRun.setDo_User_Nos(strUserID);
	        //设置流程的第一步为当前步骤(初始化为第一步)
	        //根据流程ID得到当前流程第一个活动(步骤)的ID
	        String FirstActivityID = getFirstActivityID(ActFlowRun.getWorkflow_No());
	        ActFlowRun.setM_Node_No_S_E(FirstActivityID);
	        //设置意见为[流程初始化处理]
	        ActFlowRun.setDoIdea("流程初始化处理。");
	        
	        //更新流程流转记录
	        mVec = updateFlowRunData(ActFlowRun,user,mVec);
	        //----------------------更新上一条流程流转过程记录----------------------//
	        StringBuffer  addBuf = new   StringBuffer();
	        addBuf.delete(0,addBuf.length());//清空
	        addBuf.append("select NAME,PARENTID1 from FLOW_RUNTIME_PROCESS where ID='"+ActFlowRun.getExecute_No()+"'");
	        Map<String, Object> dbset = userMapper.selectMapExecSQL(addBuf.toString());
	        if (dbset != null && dbset.size() > 0) {
	              strFLOWNAME = dbset.get("NAME").toString();
	              strCONNID = dbset.get("PARENTID1").toString();
	              dbset = null;//赋空值
	        }
	        addBuf.delete(0,addBuf.length());//清空
	        addBuf.append("update FLOW_RUNTIME_ACTIVITY set DODATE=sysdate, DOIDEA='流程初始化处理。', DOFLAG='1' "
	        			+ "where FID='"+ActFlowRun.getExecute_No()+"' and EACTIVITY='"+ActFlowRun.getNode_No_S()+"' and DOFLAG='0'");
	        
	        mVec.add(addBuf.toString());
	        //将所有SQL装入数组
	        String[] sqls = new String[mVec.size()];
	        for(int i=0;i<mVec.size();i++){
	            sqls[i] = (String)mVec.get(i);
	        }
	        //执行相关sql
	        returnValue = dbEngine.ExecuteSQLs(sqls);
	        
	        //插入初始化日志到流程管理日志表中
	        String strMAXID = getMaxFieldNo("FLOW_MANAGE_NOTE", "ID", 20);
	        addBuf.delete(0,addBuf.length());//清空
	        addBuf.append("Insert into FLOW_MANAGE_NOTE(ID,FLOWRUNID,CONNID,FLOWNAME,USERID,OPERATETIME,OPERATETYPE) values ('")
	              .append(strMAXID+"','"+ActFlowRun.getExecute_No()+"','"+strCONNID+"','"+strFLOWNAME)
	              .append("','"+user.getUserID()+"',sysdate,'初始化')");
	        userMapper.insertExecSQL(addBuf.toString());
	        //---------------------------------------------------------
	        return returnValue;
        } catch (Exception ex) {
             LOGGER.error("流程引擎---flowInitServer函数出错：", ex);
             return false;
        }
	}

	@Override
	public boolean flowTakeBackServer(ActFlowRun ActFlowRun, SessionUser user) throws Exception {
	     try{
	         boolean returnValue = true;
	         Vector<Object> mVec = new Vector<Object>();//存放所有SQL语句
	         //将收回后的处理人设置成本人
	         String strUserID = ActFlowRun.getUserNo();
	         ActFlowRun.setDo_User_Nos(strUserID);
	         //得到退回步骤的活动ID
	         String ActivityID = getFlowTakeBackID(ActFlowRun);
	         ActFlowRun.setM_Node_No_S_E(ActivityID);
	         //设置意见为[收回流程]
	         ActFlowRun.setDoIdea("收回流程。");
	         
	         //更新流程流转记录
	         mVec = updateFlowRunData(ActFlowRun,user,mVec);
	         //---------------更新上一条流程流转过程记录----------------------
	         StringBuffer  addBuf = new   StringBuffer();
	         addBuf.append("update FLOW_RUNTIME_ACTIVITY set DODATE=sysdate, DOIDEA='收回流程。', DOFLAG='1' "
	         			 + "where FID='"+ActFlowRun.getExecute_No()+"' and EACTIVITY='"+ActFlowRun.getNode_No_S()+"' and DOFLAG='0'");
	         
	         mVec.add(addBuf.toString());
	         //将所有SQL装入数组
	         String[] sqls = new String[mVec.size()];
	         for(int i=0;i<mVec.size();i++){
	            sqls[i] = (String)mVec.get(i);
	         }
	         //执行相关sql
	         returnValue = dbEngine.ExecuteSQLs(sqls);
	         
	         return returnValue;
        } catch (Exception ex) {
             LOGGER.error("流程引擎---flowTakeBackServer函数出错：", ex);
             return false;
        }	      
	}

	@Override
	public boolean flowDeleteServer(ActFlowRun ActFlowRun, SessionUser user) throws Exception {
		try {
		   List<Map<String, Object>> dbset = null;
		   boolean returnValue = true;
	       GetFlowActivityLinkHashtable.clear();
	       String strExecute_No = ActFlowRun.getExecute_No();
	       String strFormID = ActFlowRun.getFormID();
           String strIdentification = ActFlowRun.getIdentification();
           String strID="";
           String strFORMTABLE = "";
           String strFORMID = "";
           String strPRIMARYKEY = "";
           String strFLOWNAME = "";
           String strCONNID = "";
           StringBuffer addBuf = new StringBuffer();
           
           String strISDELETEFORM = "1";
           
           FLOW_CONFIG_PROCESS Process = getFlowProcessBS(strIdentification);
           if (Process!=null) {
              strISDELETEFORM = Process.getISDELETEFORM();
           }
           //-----------------------------
           addBuf.append("select FORMTABLE,FORMID,NAME,PARENTID1 from FLOW_RUNTIME_PROCESS where ID='"+strExecute_No+"'");
           Map<String, Object> retmap = userMapper.selectMapExecSQL(addBuf.toString());
           if (retmap != null && retmap.size() > 0) {
              strFORMTABLE = retmap.get("FORMTABLE").toString();
              strFORMID = retmap.get("FORMID").toString();
              strFLOWNAME = retmap.get("NAME").toString();
              strCONNID = retmap.get("PARENTID1").toString();
              retmap = null;//赋空值
           }
           if (strISDELETEFORM.equals("0")) {
                 String[] SQLS = new String[2];
                 //删除当前流程流转过程表的相关数据
                 addBuf.delete(0,addBuf.length());//清空
                 addBuf.append("delete from FLOW_RUNTIME_ACTIVITY where FID='"+strExecute_No+"'");
                 SQLS[0]=addBuf.toString();
                 //删除当前流程
                 addBuf.delete(0, addBuf.length());//清空
                 addBuf.append("delete from FLOW_RUNTIME_PROCESS where ID='"+strExecute_No+"'");
                 SQLS[1]=addBuf.toString();
                 for (String execSQL : SQLS) {
                	 userMapper.deleteExecSQL(execSQL);
                 }
                 returnValue = true;
           } else {
        	  //得到业务表的主键
              addBuf.delete(0,addBuf.length());//清空
              addBuf.append("select PRIMARYKEY from BPIP_TABLE where TABLENAME='"+strFORMTABLE+"'");
              dbset = userMapper.selectListMapExecSQL(addBuf.toString());
              if (dbset != null && dbset.size() > 0) {
            	  if (dbset.get(0) != null && dbset.get(0).get("PRIMARYKEY") != null) {
            		  strPRIMARYKEY = dbset.get(0).get("PRIMARYKEY").toString();
            	  }
                 dbset = null;//赋空值
              }
              String[] SQLS = new String[3];
              //删除业务表的数据
              addBuf.delete(0,addBuf.length());//清空
              addBuf.append("delete from "+strFORMTABLE+" where "+strPRIMARYKEY+"='"+strFORMID+"'");
              SQLS[0]=addBuf.toString();
              //删除当前流程流转过程表的相关数据
              addBuf.delete(0,addBuf.length());//清空
              addBuf.append("delete from FLOW_RUNTIME_ACTIVITY where FID='"+strExecute_No+"'");
              SQLS[1]=addBuf.toString();
              //删除当前流程
              addBuf.delete(0,addBuf.length());//清空
              addBuf.append("delete from FLOW_RUNTIME_PROCESS where ID='"+strExecute_No+"'");
              SQLS[2]=addBuf.toString();
              for (String execSQL : SQLS) {
             	 userMapper.deleteExecSQL(execSQL);
              }
              returnValue = true;
           }
           //删除附件信息(包括存在目录下的文件)
           addBuf.delete(0,addBuf.length());//清空
           addBuf.append("select ID from BPIP_ATT where (FLOWID='"+strIdentification+"' and FORMID='"+strFormID
        		   		+"') or (FLOWID='"+strIdentification+"' and FORMID='"+strExecute_No+"')");
           dbset = userMapper.selectListMapExecSQL(addBuf.toString());
           if (dbset != null && dbset.size() > 0) {
        	   int length = dbset.size();
               for (int i=0; i<length; i++) {
                   strID = dbset.get(i).get("ID").toString();
                   //删除服务器上附件记录及目录下的附件
                   sysAttFileDel(strID);
               }
               dbset = null;//赋空值
           }
           //----------------------------
           //插入删除日志到流程管理日志表中
           addBuf.delete(0,addBuf.length());//清空
           String strMAXID = getMaxFieldNo("FLOW_MANAGE_NOTE", "ID", 20);
           addBuf.append("Insert into FLOW_MANAGE_NOTE(ID,FLOWRUNID,CONNID,FLOWNAME,USERID,OPERATETIME,OPERATETYPE) values ('")
            	 .append(strMAXID+"','"+strExecute_No+"','"+strCONNID+"','"+strFLOWNAME)
            	 .append("','"+user.getUserID()+"',sysdate,'删除')");
           userMapper.insertExecSQL(addBuf.toString());
           
           return returnValue;
         } catch (Exception ex) {
             LOGGER.error("流程引擎---flowDeleteServer函数出错：", ex);
             return false;
         }
	}

	@Override
	public boolean flowReturnServer(ActFlowRun ActFlowRun, SessionUser user) throws Exception {
	    try {
	        GetFlowActivityLinkHashtable.clear();
	        boolean returnValue = true;
	        Vector<Object> mVec = new Vector<Object>();  //存放所有SQL语句
	        String strUpActivityID = "";
	        String strUpActivitySendUserNo = "";
	        String strCUserNo = "";
	        int UserNum = 1;
	        //----------------得到流程运转初始化参数----------------
	        String Node_No_S=ActFlowRun.getNode_No_S();
	        String UserNo=ActFlowRun.getUserNo();
	        String Execute_No=ActFlowRun.getExecute_No();
	        String DoIdea=ActFlowRun.getDoIdea();
	        
	        FLOW_RUNTIME_PROCESS Fprocess = getFlowRuntimeprocess(Execute_No);
	        String strFlowid = ActFlowRun.getWorkflow_No();
	        //----------------初始化----------------
	        FLOW_CONFIG_ACTIVITY activity = getFlowActivity(ActFlowRun.getNode_No_S());
	        //根据活动ID得到活动的完成策略类型
	        String strCstrategyType = "";
	        if (activity!=null) {
	        	strCstrategyType = activity.getCSTRATEGY();
	        }
	        //根据活动ID得到活动的完成策略的数量或百分比
	        int strCstrategyNum = 0;
	        if (activity!=null) {
	        	strCstrategyNum = activity.getCNUM();
        	}
	        LOGGER.info("flowReturnServer:","完成策略类型:"+strCstrategyType);
	        LOGGER.info("flowReturnServer:","根据活动ID得到活动的完成策略的数量或百分比:"+strCstrategyNum);
	        //根据流转编号、当前活动ID、当前用户得到发送步骤的活动ID
	        strUpActivityID = getUpActivityID(ActFlowRun);
	        LOGGER.info("FlowReturnServer:","根据流转编号、当前活动ID、当前用户得到发送步骤的活动ID:"+strUpActivityID);
	        if (strUpActivityID.length()==0)//为空时取第一步
	        {
	           strUpActivityID = getFirstActivityID(strFlowid);
	        }
	        //根据流转编号、当前活动编号、当前用户得到发送步骤的发送人员编号
	        strUpActivitySendUserNo = getUpActivitySendUserNo(ActFlowRun);
	        LOGGER.info("FlowReturnServer:","根据流转编号、当前活动编号、当前用户得到发送步骤的发送人员编号:"+strUpActivitySendUserNo);
	        
	        if (strUpActivitySendUserNo.length()==0) {//为空时取流程创建人
	           strUpActivitySendUserNo = Fprocess.getCREATEPSN();
	        }
	        //-------------得到当前流转的待处理人列表-------------
	        if (Fprocess!=null) { strCUserNo = Fprocess.getACCEPTPSN(); }
	        LOGGER.info("flowReturnServer:","得到当前流转的待处理人列表:"+strCUserNo);
	        List<Object> UserList = getArrayList(strCUserNo, ",");
	        //-------------待处理人数量-------------
	        UserNum = UserList.size();
	        //-------------------------------初始化结束-------------------------------
	        LOGGER.info("FlowReturnServer:","类型:"+strCstrategyType);
	        if (strCstrategyType.equals("1")) { //任意完成一个
	          //--------------直接返回给上一步的处理人-------------------
	          LOGGER.info("FlowReturnServer:",strUpActivityID+"/"+strUpActivitySendUserNo);
	          //根据流程处理返回时的条件更新流程
	          mVec = updateFlowRunTimeData(strUpActivityID,strUpActivitySendUserNo,ActFlowRun,user,mVec);
	          //-----------------------------------------------------
	        }
	        if (strCstrategyType.equals("2")) { //必须完成所有
	          if (UserNum == 1) { //只有最后一个处理人
	            //--------------直接返回给上一步的处理人-------------------
	            //根据流程处理返回时的条件更新流程
	            mVec = updateFlowRunTimeData(strUpActivityID,strUpActivitySendUserNo,ActFlowRun,user,mVec);
	            //-----------------------------------------------------
	          } else {
	            //去掉流程流转记录中的当前处理人
	            mVec = updateFlowRunTimeDoUser(ActFlowRun,mVec);
	          }
	        }
	        if (strCstrategyType.equals("3")) { //按完成数据
	          int DoUserCount =0;
	          if (Fprocess!=null) {
	            DoUserCount = Fprocess.getACCEPTPSNNUM(); //得到当前步骤的总共处理人数
	          }
	          if (DoUserCount <= strCstrategyNum) { //总共的处理人员<=完成策略的数量按[完成所有来处理]
	            //------#############################################
	            if (UserNum == 1) { //只有最后一个处理人
	              //--------------直接返回给上一步的处理人-------------------
	              //根据流程处理返回时的条件更新流程
	              mVec = updateFlowRunTimeData(strUpActivityID,strUpActivitySendUserNo,ActFlowRun,user,mVec);
	              //-----------------------------------------------------
	            } else {
	              //去掉流程流转记录中的当前处理人
	              mVec = updateFlowRunTimeDoUser(ActFlowRun,mVec);
	            }
	            //------#############################################
	          } else {
	            if ((DoUserCount - UserNum + 1) >= strCstrategyNum) {
	              //根据流程处理返回时的条件更新流程
	              mVec = updateFlowRunTimeData(strUpActivityID,strUpActivitySendUserNo,ActFlowRun,user,mVec);
	              //-----------------------------------------------------
	            } else {
	              //去掉流程流转记录中的当前处理人
	              mVec = updateFlowRunTimeDoUser(ActFlowRun,mVec);
	            }
	          }
	        }
	        if (strCstrategyType.equals("4")) { //按完成的百分比
	          int DoUserCount =0;
	          if (Fprocess!=null) {
	             DoUserCount = Fprocess.getACCEPTPSNNUM(); //得到当前步骤的总共处理人数
	          }
	          double finishedNum = (DoUserCount - UserNum + 1) / DoUserCount;
	          double setupNum = strCstrategyNum / 100;
	          if (finishedNum >= setupNum) { //根据流程处理返回时的条件更新流程
	            mVec = updateFlowRunTimeData(strUpActivityID,strUpActivitySendUserNo,ActFlowRun,user,mVec);
	            //-----------------------------------------------------
	          } else { //去掉流程流转记录中的当前处理人
	            mVec = updateFlowRunTimeDoUser(ActFlowRun,mVec);
	          }
	        }
	        //更新当条流程流转记录的相关字段(处理时间、处理意见、处理状态)
	        StringBuffer  addBuf = new   StringBuffer();
	        addBuf.append("update FLOW_RUNTIME_ACTIVITY set DODATE=sysdate,DOIDEA='"+DoIdea+"',DOFLAG='1' where FID='"+Execute_No+"' and EACTIVITY='"+Node_No_S)
	              .append("' and DOPSN='"+UserNo+"' and DOFLAG='0'");
	        
	        mVec.add(addBuf.toString());
	        //将所有SQL装入数组
	        String[] sqls = new String[mVec.size()];
	        for(int i=0;i<mVec.size();i++){
	            sqls[i] = (String)mVec.get(i);
	        }
	        //执行相关sql
	        returnValue = dbEngine.ExecuteSQLs(sqls);
	        
	        LOGGER.info("flowReturnServer:","结束:"+returnValue);
	        return returnValue;
	     } catch (Exception ex) {
	    	 LOGGER.error("流程引擎---flowReturnServer函数出错：", ex);
	         return false;
	     }
	}

	@Override
	public String sendFlowMessage(String strUserNos, String strMessage) throws Exception {
	    String tmpUserID = ""; //临时用户编号
	    String returnValue = ""; //处理结果
	    StringBuffer addBuf = new StringBuffer();
	    List<Object> UserList = getArrayList(strUserNos, ",");
	    for (int i = 0; i < UserList.size(); i++) {
	      tmpUserID = UserList.get(i).toString();
	      //插入消息
	      addBuf.delete(0,addBuf.length());//清空
	      addBuf.append("Insert into BPIP_MSGCONTENT(CUSERNO,CONTENT,SENDDATE,ISCK) values ('")
	      		.append(tmpUserID+"','"+strMessage+"',sysdate,'0')");
	      
	      userMapper.insertExecSQL(addBuf.toString());
	    }
	    return returnValue;  
	}

	@Override
	public String getPackageFirst(String strPackageID) throws Exception {
	    StringBuffer addBuf = new StringBuffer();
	    //得到当前流程包下的第一个流程包
	    addBuf.append("Select ID From FLOW_CONFIG_PACKAGE where FID='"+strPackageID+"' order by ID");
	    List<Map<String, Object>> dbset = userMapper.selectListMapExecSQL(addBuf.toString());
	    addBuf.delete(0,addBuf.length());//清空
	    if (dbset != null && dbset.size() > 0) {
	        addBuf.append(dbset.get(0).get("ID").toString());
	        dbset = null;//赋空值
	    }
	    return addBuf.toString();  
	}

	@Override
	public String getPackageName(String strPackageID) throws Exception {
	    StringBuffer addBuf = new StringBuffer();
	    //得到当前流程包的包名
	    addBuf.append("Select NAME From FLOW_CONFIG_PACKAGE where ID='"+strPackageID+"'");
	    Map<String, Object> dbset = userMapper.selectMapExecSQL(addBuf.toString());
	    addBuf.delete(0,addBuf.length());//清空
	    if (dbset != null && dbset.size() > 0) {
	        addBuf.append(dbset.get("NAME").toString());
	        dbset = null;//赋空值
	    }
	    return addBuf.toString();  
	}

	@Override
	public String getISAttMessage(String CAID, String SAID) throws Exception {
	      StringBuffer addBuf = new StringBuffer();
	      String returnValue = "0"; //返回值
	      addBuf.append("Select ISATT From FLOW_CONFIG_ACTIVITY_CONNE where (SID='"+CAID)
	            .append("' or EID='"+CAID+"') and CID='"+SAID+"'");
	      List<Map<String, Object>> dbset = userMapper.selectListMapExecSQL(addBuf.toString());
	      if (dbset != null && dbset.size() > 0) {
	    	  if (dbset.get(0) != null && dbset.get(0).get("ISATT") != null) {
	    		  returnValue = dbset.get(0).get("ISATT").toString();
	    	  }
	          dbset = null;//赋空值
	      }
	      return returnValue;  
	}

	@Override
	public String getDeptInside(String strUnitID) throws Exception {
		String strReturn="";
        StringBuffer addBuf = new StringBuffer();
        addBuf.append("Select USERID From BPIP_USER where UNITID='"+strUnitID)
              .append("' and DEPTINSIDE='1' order by ORBERCODE,USERID");
        List<Map<String, Object>> dbset1 = userMapper.selectListMapExecSQL(addBuf.toString());
        addBuf.delete(0,addBuf.length());//清空
        if (dbset1 != null && dbset1.size()>0) {
          for (int i = 0; i < dbset1.size(); i++) {
             addBuf.append(dbset1.get(i).get("USERID").toString()+",");
          }
          dbset1 = null;//赋空值
        }
        strReturn = addBuf.toString();
        if (strReturn.length()>0) {
        	strReturn = strReturn.substring(0,strReturn.length()-1);
    	}
        return strReturn;
	}

	@Override
	public String getDeptUsers(String strUnitID) throws Exception {
	      String strReturn="";
	      StringBuffer addBuf = new StringBuffer();
	      addBuf.append("Select USERID From BPIP_USER where USERSTATE='0' and UNITID='"+strUnitID+"' order by ORBERCODE,USERID");
	      List<Map<String, Object>> dbset1 = userMapper.selectListMapExecSQL(addBuf.toString());
	      addBuf.delete(0, addBuf.length());//清空
	      if (dbset1 != null && dbset1.size() > 0) {
	        for (int i = 0; i < dbset1.size(); i++) {
	           addBuf.append(dbset1.get(i).get("USERID").toString()+",");
	        }
	        dbset1 = null;//赋空值
	      }
	      strReturn = addBuf.toString();
	      if (strReturn.length()>0) {
	    	  strReturn = strReturn.substring(0,strReturn.length()-1);
    	  }
	      return strReturn;  
	}

	@Override
	public String getDeptInsideName(String strUnitID) throws Exception {
        String strReturn = "";
        StringBuffer addBuf = new StringBuffer();
        addBuf.append("Select NAME From BPIP_USER where USERSTATE='0' and UNITID='"+strUnitID+"' and DEPTINSIDE='1' order by ORBERCODE,USERID");
        List<Map<String, Object>> dbset1 = userMapper.selectListMapExecSQL(addBuf.toString());
        addBuf.delete(0,addBuf.length());//清空
        if (dbset1 != null && dbset1.size()>0) {
          for (int i = 0; i < dbset1.size(); i++) {
             addBuf.append(dbset1.get(i).get("NAME").toString()+",");
          }
          dbset1 = null;//赋空值
        }
        strReturn = addBuf.toString();
        if (strReturn.length()>0) {
        	strReturn = strReturn.substring(0,strReturn.length()-1);
    	}
        return strReturn;
	}

	@Override
	public String getDeptUserNames(String strUnitID) throws Exception {
        String strReturn="";
        StringBuffer addBuf = new StringBuffer();
        addBuf.append("Select NAME From BPIP_USER where USERSTATE='0' and UNITID='"+strUnitID+"' order by ORBERCODE,USERID");
        List<Map<String, Object>> dbset1 = userMapper.selectListMapExecSQL(addBuf.toString());
        addBuf.delete(0,addBuf.length());//清空
        if (dbset1 != null && dbset1.size()>0) {
          for (int i = 0; i < dbset1.size(); i++) {
             addBuf.append(dbset1.get(i).get("NAME").toString()+",");
          }
          dbset1 = null;//赋空值
        }
        strReturn = addBuf.toString();
        if (strReturn.length()>0) {
        	strReturn = strReturn.substring(0,strReturn.length()-1);
    	}
        return strReturn;
	}

	@Override
	public String getIsTakeBack(String strExecuteNo, String strUserID) throws Exception {
		  String strReturn = "0";
		  String strSACTIVITY="";
		  String strEACTIVITY="";
		  String strIs1="0";
		  String strIs2="0";
		  String strIs3="0";
		  StringBuffer addBuf = new StringBuffer();
		  if (strExecuteNo!=null && strExecuteNo.length()>0) {
		     addBuf.append("SELECT SACTIVITY,EACTIVITY FROM FLOW_RUNTIME_ACTIVITY where DOFLAG='0' and FID='"+strExecuteNo)
		           .append("' and SENDPSN='"+strUserID+"' and DOPSN<>'"+strUserID+"' order by ID desc");
		     
		     List<Map<String, Object>> dbset = userMapper.selectListMapExecSQL(addBuf.toString());
		     if(dbset!=null && dbset.size()>0) {
		    	 if (dbset.get(0) != null) {
		    		 strSACTIVITY = dbset.get(0).get("SACTIVITY") != null ? dbset.get(0).get("SACTIVITY").toString() : "";
		        	strEACTIVITY = dbset.get(0).get("EACTIVITY") != null ? dbset.get(0).get("EACTIVITY").toString() : "";
		    	 }
		    	 if (getFlowActivityOrder(strSACTIVITY)<getFlowActivityOrder(strEACTIVITY)) {
		    		 strIs1="1";
		    	 }
		    	 dbset = null;//赋空值
		     }
		     //流程是否正在运行
		     addBuf.delete(0,addBuf.length());//清空
		     addBuf.append("SELECT ID FROM FLOW_RUNTIME_PROCESS where ID='"+strExecuteNo+"' and STATE='1'");
		     dbset = userMapper.selectListMapExecSQL(addBuf.toString());
		     if(dbset != null && dbset.size()>0) {
		        strIs2="1";
		        dbset = null;//赋空值
		     }
		     //当前处理人不包含本人
		     addBuf.delete(0,addBuf.length());//清空
		     addBuf.append("SELECT ID FROM FLOW_RUNTIME_PROCESS where ID='"+strExecuteNo+"' and ACCEPTPSN like '%")
		           .append(strUserID+"%'");
		     dbset = userMapper.selectListMapExecSQL(addBuf.toString());
		     if(dbset != null && dbset.size() > 0) {
		        strIs3="0";
		        dbset = null;//赋空值
		     } else {
		        strIs3="1";
		     }
		  }
		  if (strIs1.equals("1") && strIs2.equals("1") && strIs3.equals("1")) {
		     strReturn ="1";
		  }
		  return strReturn;
	}

	@Override
	public FLOW_RUNTIME_PROCESS getFlowRuntimeprocess(String strID) throws Exception {
		  if (strID.length() == 0) {
			  return null;
		  }
		  FLOW_RUNTIME_PROCESS Fprocess = null;
		  Fprocess = (FLOW_RUNTIME_PROCESS) FLOW_RUNTIME_PROCESSHashtable.get(strID);
		  if (Fprocess == null) {
			 StringBuffer addBuf = new StringBuffer();
		     addBuf.append("Select * From FLOW_RUNTIME_PROCESS where ID='"+strID+"'");
		     Map<String, Object> retmap = userMapper.selectMapExecSQL(addBuf.toString());
		     if (retmap != null && retmap.size() > 0) {
		        Fprocess = (FLOW_RUNTIME_PROCESS) ReflectionUtil.convertMapToBean(retmap, FLOW_RUNTIME_PROCESS.class);
		        retmap = null;//赋空值
		     }
		     if (Fprocess != null) {
		       FLOW_RUNTIME_PROCESSHashtable.put(strID,Fprocess);
		     }
		  }
		  return Fprocess;
	}

	@Override
	public FLOW_CONFIG_ACTIVITY getFlowActivity(String strID) throws Exception {
	    FLOW_CONFIG_ACTIVITY Activity = null;
	    StringBuffer addBuf = new StringBuffer();
	    addBuf.append("Select * From FLOW_CONFIG_ACTIVITY where ID='"+strID+"'");
	    Map<String, Object> retmap = userMapper.selectMapExecSQL(addBuf.toString());
	    if (retmap != null && retmap.size() > 0) {
	         Activity = (FLOW_CONFIG_ACTIVITY) ReflectionUtil.convertMapToBean(retmap, FLOW_CONFIG_ACTIVITY.class);
	         retmap = null;//赋空值
	    }
	    return Activity;
	}

	@Override
	public FLOW_CONFIG_PROCESS getFlowProcess(String strID) throws Exception {
	    FLOW_CONFIG_PROCESS Process = null;
	    StringBuffer addBuf = new StringBuffer();
	    addBuf.append("Select * From FLOW_CONFIG_PROCESS where ID='"+strID+"'");
	    Map<String, Object> retmap = userMapper.selectMapExecSQL(addBuf.toString());
	    if (retmap != null && retmap.size() > 0) {
	         Process = (FLOW_CONFIG_PROCESS) ReflectionUtil.convertMapToBean(retmap, FLOW_CONFIG_PROCESS.class);
	         retmap = null;//赋空值
	    }
	    return Process;
	}

	@Override
	public Package getFlowPackage(String strID) throws Exception {
		Package package1 = null;
        StringBuffer addBuf = new StringBuffer();
        addBuf.append("SELECT * FROM FLOW_CONFIG_PACKAGE where ID = '"+strID+"'");
        Map<String, Object> retmap = userMapper.selectMapExecSQL(addBuf.toString());
        if (retmap != null && retmap.size() > 0) {
        	package1 = (Package) ReflectionUtil.convertMapToBean(retmap, Package.class);
        	retmap = null;//赋空值
        }
        return package1;
	}

	@Override
	public Package[] getFlowPackageList(String strID) throws Exception {
		Package[] packages = null;
		StringBuffer addBuf = new StringBuffer();
        addBuf.append("SELECT * FROM FLOW_CONFIG_PACKAGE where FID = '"+strID+"' order by ID");
        List<Map<String, Object>> dbset = userMapper.selectListMapExecSQL(addBuf.toString());
        if (dbset != null && dbset.size() > 0) {
        	int length = dbset.size();
        	packages = new Package[length];
            for (int i=0; i<length; i++) {
            	Map<String, Object> retmap = dbset.get(i);
            	packages[i] = (Package) ReflectionUtil.convertMapToBean(retmap, Package.class);
            }
            dbset = null;//赋空值
        }
        return packages;
	}

	@Override
	public boolean getIsSubPackage(String strID) throws Exception {
		boolean result = false;
        StringBuffer addBuf = new StringBuffer();
        addBuf.append("SELECT ID FROM FLOW_CONFIG_PACKAGE where FID = '"+strID+"'");
        List<Map<String, Object>> dbset = userMapper.selectListMapExecSQL(addBuf.toString());
        if (dbset != null && dbset.size() > 0){
           result = true;
           dbset = null;//赋空值
        }
        return result;
	}

	@Override
	public void addFlowManageNote(String strExecuteNo, SessionUser user) throws Exception {
		   //插入修改日志到流程管理日志表中
	       String strMAXID = "";
	       String strFLOWNAME = "";
	       String strCONNID = "";
	       StringBuffer addBuf = new StringBuffer();
	       addBuf.append("select NAME,PARENTID1 from FLOW_RUNTIME_PROCESS where ID='"+strExecuteNo+"'");
	       Map<String, Object> retmap = userMapper.selectMapExecSQL(addBuf.toString());
	       if (retmap != null && retmap.size() > 0) {
	          strFLOWNAME = retmap.get("NAME").toString();
	          strCONNID = retmap.get("PARENTID1").toString();
	          retmap = null;//赋空值
	       }
	       strMAXID = getMaxFieldNo("FLOW_MANAGE_NOTE","ID",20);
	       addBuf.delete(0,addBuf.length());//清空
	       addBuf.append("Insert into FLOW_MANAGE_NOTE(ID,FLOWRUNID,CONNID,FLOWNAME,USERID,OPERATETIME,OPERATETYPE) values ('")
	             .append(strMAXID+"','"+strExecuteNo+"','"+strCONNID+"','"+strFLOWNAME)
	             .append("','"+user.getUserID()+"',sysdate,'修改')");
	       
	       userMapper.insertExecSQL(addBuf.toString());
	       //--------------------------------
	}

	@Override
	public String getTitle(String ID) throws Exception {
	     String strResult = "";
	     if (ID.length()==0) {
	    	 return strResult;
	     }
	     strResult = (String) GetTitleHashtable.get(ID);
	     if (strResult==null) {
		     String strSQL="";
		     String formtable="";
		     String formid="";
		     String PRIMARYKEY="";
		     String TITLE="";
		     strResult = "";
		     strSQL = "select FORMTABLE,FORMID from flow_runtime_process where ID='"+ID+"'";
		     Map<String, Object> retmap = userMapper.selectMapExecSQL(strSQL);
		     if (retmap != null && retmap.size() > 0) {
		          formtable = retmap.get("FORMTABLE").toString();
		          formid = retmap.get("FORMID").toString();
		          
		          strSQL = "select PRIMARYKEY,TITLE from BPIP_TABLE where TABLENAME='"+formtable+"'";
		          List<Map<String, Object>> retlist = userMapper.selectListMapExecSQL(strSQL);
		          if (retlist != null && retlist.size() > 0) {
		        	 if (retlist.get(0) != null) {
		        		 PRIMARYKEY = retlist.get(0).get("PRIMARYKEY") != null ? retlist.get(0).get("PRIMARYKEY").toString() : "";
		        		 TITLE = retlist.get(0).get("TITLE") != null ? retlist.get(0).get("TITLE").toString() : "";
		        	 }
		             strSQL = "select "+TITLE+" from "+formtable+" where "+PRIMARYKEY+"='"+formid+"'";
		             DBSet dbset = dbEngine.QuerySQL(strSQL);
		             if (dbset != null && dbset.RowCount() > 0) {
		                strResult = dbset.Row(0).Column(TITLE).getString();
		             }
		             dbset = null;//赋空值
		          }
		          if (strResult ==null) {
		        	  strResult = "";
	        	  }
		          if (strResult.length()==0) {
		        	  strResult = "no";
		          }
		          GetTitleHashtable.put(ID,strResult);
		     }
	     }
	     if (strResult.equals("no")) {
	    	 strResult = "";
	     }
	     return strResult; 
	}

	@Override
	public String globalID(String userID) throws Exception {
		 String strResult = "";
	     Date dt = new Date();
	     long lg = dt.getTime();
	     Long ld = new Long(lg);
	     strResult = ld.toString();
	     strResult = userID+strResult;
	     if (strResult.length()>30) {
	    	 strResult = strResult.substring(0,30);
    	 }
	     return strResult;
	}

	@Override
	public String getActivityButton(String strExecute_No, String strID, String strtype, ActFlowRun ActFlowRun,
			SessionUser user, String strM_Node_No_E, String strS_Node_No_E) throws Exception {
		   try {
			    String strButtonName = "", strButtonIco = "", strButtonProperty = "",strROLEIDS="";
			    String strsql="";
			    boolean isTrue = true;
			    boolean isQX = true;
			    StringBuffer addBuf = new StringBuffer();
			    //获得多路分支是否在按钮上操作 1是 0否
			    String strISBRANCH=ActFlowRun.getISBRANCH();
			    //获得下一分支步骤编号 提交按钮
			    String M_Node_No_S_E=strM_Node_No_E;//ActFlowRun.getM_Node_No_S_E();
			    String str[]=null;
			    
			    //获得上一分支步骤编号 退回 按钮
			    String S_Node_No_S_E=strS_Node_No_E;
			    String str1[]=null;
			    
			    if (strExecute_No!=null && strExecute_No.length()>0) {
			      isTrue = getIsFlowDoList(strExecute_No,user); //判断当前用户是否是当前流转流程中的待处理人
			    }
			    if (isTrue){
			          //----------------
			          if (strtype.equals("1")) {
			        	  addBuf.append("Select a.NAME,a.ICO,a.PROPERTY,a.ROLEIDS From FLOW_BASE_BUTTON a left join FLOW_CONFIG_ACTIVITY_BUTTON b on a.ID=b.BUTTONID "
			        	  			  + "where (a.POSITION='1' or a.POSITION='3') and a.TYPE<>'2' AND a.TYPE<>'3' and b.FID='")
			                	.append(strID).append("' order by b.ID");
			          } else {
			        	  addBuf.append("Select a.NAME,a.ICO,a.PROPERTY,a.ROLEIDS From FLOW_BASE_BUTTON a left join FLOW_CONFIG_ACTIVITY_BUTTON b on a.ID=b.BUTTONID "
			        	  			  + "where (a.POSITION='2' or a.POSITION='3') and a.TYPE<>'2' AND a.TYPE<>'3' and b.FID='")
			                	.append(strID).append("' order by b.ID");
			          }
			          List<Map<String, Object>> dbset1 = userMapper.selectListMapExecSQL(addBuf.toString());
			          addBuf.delete(0, addBuf.length());//清空
			          if (dbset1 != null && dbset1.size() > 0) {
			        	  int length1 = dbset1.size();
			              for (int i=0; i<length1; i++) {
			            	  Map<String, Object> retmap1 = dbset1.get(i);
			                  strButtonName = retmap1.get("NAME").toString();
			                  strButtonIco = retmap1.get("ICO").toString();
			                  strButtonProperty = retmap1.get("PROPERTY").toString();
			                  if (retmap1.get("ROLEIDS") != null) {
			                	  strROLEIDS = retmap1.get("ROLEIDS").toString();
			                  }
			                  isQX = false;
			                  if (strROLEIDS.length() > 0) {
			                       strsql = "select ROLEID from bpip_user_role "
			                       		  + "where USERID='" + user.getUserID() + "' and ROLEID in (" + strROLEIDS + ")";
			                       List<Map<String, Object>> dbset2 = userMapper.selectListMapExecSQL(strsql);
			                       if (dbset2 != null && dbset2.size() > 0) { //有显示权限
			                           isQX = true;
			                       }
			                  } else {
			                	   isQX = true;
		                	  }
			                  /*******************多路分支是否表现在操作按钮上实现 2011-04-20修改****************************/
			                  if (strISBRANCH.equals("1") && isQX) {
			                      /**
			                       * 下一分支步骤
			                       */
			                      str = M_Node_No_S_E.split(",");
			                      /**
			                      * 上一分支步骤
			                      */
			                      str1 = S_Node_No_S_E.split(",");
			                      if(str.length>=1){
			                      for (int kk = 0; kk < str.length; kk++) {
			                          if (strButtonProperty.indexOf("saveColl('2')") !=-1) {
			                              addBuf.append(clickButton(getActButton(str[kk]),"saveColl1('2','" + str[kk] + "')",strButtonIco));
			                          }
			                        }
			                      }
			                     if(str1.length>=1){
			                         for (int yy = 0; yy < str1.length; yy++) {
			                             if (strButtonProperty.indexOf("doUntread()") !=-1) {
			                                 addBuf.append(clickButton(getActButton(str1[yy]),"doUntread1('" + str1[yy] + "')",strButtonIco));
			                             }
			                         }
			                     }
			                     if(!(strButtonProperty.indexOf("saveColl('2')") !=-1) && !(strButtonProperty.indexOf("doUntread()") !=-1)) {
			                          addBuf.append(clickButton(strButtonName, strButtonProperty, strButtonIco));
			                     }
			                  }
			              }
			           }
			           dbset1 = null;//赋空值
			    }
			    //----------------
			    return addBuf.toString();
		   } catch (Exception ex) {
		         LOGGER.error("流程引擎---getActivityButton函数出错：", ex);
		         return "";
		    }
	}

	@Override
	public String getActButton(String strID) throws Exception {
	     String valueName="";
	     StringBuffer addBuf = new StringBuffer();
	     addBuf.append("select NAME from flow_config_activity where id='"+strID+"'");
	     Map<String, Object> dbset= userMapper.selectMapExecSQL(addBuf.toString());
	     if (dbset != null && dbset.size() > 0) {
	    	   if (dbset.get("NAME") != null) {
	    		   valueName= dbset.get("NAME").toString();
	    	   }
               LOGGER.info("获得对应流程配置的名称："+valueName);
	     }
	     return valueName;  
	}

	@Override
	public boolean getIsShow(String Execute_No, String CID) throws Exception {
	    try {
	        boolean returnValue = false;
	        String strCUserNo = "";
	        String strASTRATEGY = "";
	        int UserNum = 1;
	        FLOW_RUNTIME_PROCESS Fprocess = getFlowRuntimeprocess(Execute_No);
	        //-------------------------初始化-------------------------//
	        FLOW_CONFIG_ACTIVITY activity = getFlowActivity(CID);
	        //根据活动ID得到活动的完成策略类型
	        String strCstrategyType = "";
	        if (activity != null) {
	        	strCstrategyType = activity.getCSTRATEGY();
	        	strASTRATEGY = activity.getASTRATEGY();
	        }
	        if (strASTRATEGY.equals("1")) {//分配1人
	           return false;
	        }
	        //根据活动ID得到活动的完成策略的数量或百分比
	        int strCstrategyNum = 0;
	        if (activity!=null) {
	        	strCstrategyNum = activity.getCNUM();
	        }
	        LOGGER.info("getIsShow:完成策略类型:"+strCstrategyType);
	        //得到当前流转的待处理人列表
	        if (Fprocess!=null) {
	        	strCUserNo = Fprocess.getACCEPTPSN();
	        }
	        LOGGER.info("getIsShow:得到当前流转的待处理人列表:"+strCUserNo);
	        List<Object> UserList = getArrayList(strCUserNo, ",");
	        //待处理人数量-------------
	        UserNum = UserList.size();
	        //--------------初始化结束------------------------------------------------
	        LOGGER.info("getIsShow:类型:"+strCstrategyType);
	        if (strCstrategyType.equals("1")) { //任意完成一个
	           return false;
	        }
	        if (strCstrategyType.equals("2")) { //必须完成所有
	          if (UserNum == 1) { //只有最后一个处理人
	             return false;
	          } else {
	             return true;
	          }
	        }
	        if (strCstrategyType.equals("3")) { //按完成数据
	          int DoUserCount =0;
	          if (Fprocess!=null) {
	            DoUserCount = Fprocess.getACCEPTPSNNUM(); //得到当前步骤的总共处理人数
	          }
	          if (DoUserCount <= strCstrategyNum) { //总共的处理人员<=完成策略的数量按[完成所有来处理]
	            if (UserNum == 1) { //只有最后一个处理人
	              return false;
	            } else {
	                return true;
	            }
	          } else {
	            if ( (DoUserCount - UserNum + 1) >= strCstrategyNum) {
	               return false;
	            }
	            else {
	               return true;
	            }
	          }
	        }
	        if (strCstrategyType.equals("4")) { //按完成的百分比
	          int DoUserCount =0;
	          if (Fprocess!=null) {
	             DoUserCount = Fprocess.getACCEPTPSNNUM(); //得到当前步骤的总共处理人数
	          }
	          double finishedNum = (DoUserCount - UserNum + 1) / DoUserCount;
	          double setupNum = strCstrategyNum / 100;
	          if (finishedNum >= setupNum) { //可以执行提交处理
	            return false;
	          } else { //去掉流程流转记录中的当前处理人
	            return true;
	          }
	        }
	        return returnValue;
         } catch (Exception ex) {
             LOGGER.error("流程引擎---getIsShow函数出错：", ex);
             return false;
         }
	}

	@Override
	public String getDocTables(String strID) throws Exception {
	      String reValue = "";
	      String strSQL="select MAINTABLE from COLL_DOC_CONFIG where ID='"+strID+"'";
	      Map<String, Object> retmap1 = userMapper.selectMapExecSQL(strSQL);
	      if (retmap1 != null && retmap1.size() > 0) {
	         reValue = retmap1.get("MAINTABLE").toString();
	      }
	      strSQL="select TABLENAME from BPIP_TABLE where TABLEID in ("+reValue+")";
	      List<Map<String, Object>> dbset= userMapper.selectListMapExecSQL(strSQL);
	      reValue = "";
	      if (dbset != null && dbset.size() > 0) {
	    	  int length = dbset.size();
	    	  for (int i = 0; i < length; i++) {
	    		  Map<String, Object> retmap = dbset.get(i);
	              if (reValue.length()==0) {
	                  reValue = retmap.get("TABLENAME").toString();
	              } else {
	                  reValue=reValue+","+retmap.get("TABLENAME").toString();
	              }
	         }
	      }
	      return reValue;
	}

	@SuppressWarnings("unchecked")
	@Override
	public String getFlowActivityLink(String strID, String strAID, String strPath) throws Exception {
        try {
	        String strTmpID = "";
	        String strTmpNAME = "";
	        StringBuffer addBuf = new StringBuffer();
	        List<Map<String, Object>> dbset = (List<Map<String, Object>>) GetFlowActivityLinkHashtable.get(strID);
	        if (dbset == null || dbset.size() < 1) {
	           addBuf.append("Select ID,NAME From FLOW_CONFIG_ACTIVITY where FID='").append(strID)
	            	 .append("' and (TYPE='2' or TYPE='3' or TYPE='4') and ORDER1<>9998 and ORDER1<>0 order by ORDER1");
	           dbset = userMapper.selectListMapExecSQL(addBuf.toString());
	           if (dbset != null && dbset.size() > 0) {
	               GetFlowActivityLinkHashtable.put(strID, dbset);
	           }
	        }
	        if (dbset != null && dbset.size() > 0) {
	            addBuf.delete(0, addBuf.length());//清空
	            addBuf.append("\r\n<center><table cellspacing='0' cellpadding='0' border='0'>\r\n<tr><td align='center' height='2'></td></tr><tr><td align='center'>开始</td></tr>\r\n");
	            int length = dbset.size();
	            for (int i = 0; i < length; i++) {
	              Map<String, Object> retmap = dbset.get(i);
	              strTmpID = retmap.get("ID").toString();
	              strTmpNAME = retmap.get("NAME").toString();
	              if (strAID.length() > 0) {
	                if (strTmpID.equals(strAID)) {//是当前活动
	                	addBuf.append("<tr><td align='center'>↓</td></tr>\r\n<tr><td align='center'><a href=javascript:OpenFlowMap('")
	                		  .append(strPath).append("&Workflow_No=").append(strID).append("&AID=").append(strAID)
	                		  .append("')><font class='WriteSign'>").append(strTmpNAME).append("</font></a></td></tr>\r\n");
	                } else {
	                	addBuf.append("<tr><td align='center'>↓</td></tr>\r\n<tr><td align='center'><a href=javascript:OpenFlowMap('")
	                		  .append(strPath).append("&Workflow_No=").append(strID).append("&AID=").append(strAID)
	                		  .append("')>").append(strTmpNAME).append("</a></td></tr>\r\n");
	                }
	              }
	          }
	          addBuf.append("</table></center>\r\n");
	          dbset = null;//赋空值
	        }
	        return addBuf.toString();
        } catch (Exception ex) {
             LOGGER.error("流程引擎---getFlowActivityLink函数出错：", ex);
             return "";
        }
	}

	@Override
	public String getFlowDoLog(String Execute_No) throws Exception {
		   String strSql = "";
		   String strEACTIVITY = "";
		   String strEACTIVITY1 = "";
		   String strDOPSN = "";
		   String strDOPSNNAME = "";
		   String strDODATE = "";
		   String strDOIDEA = "";
		   String strDOIDEA1 = "";
		   String strDOFLAG = "";//处理状态
		   String strNODOPSNS = "";//正在处理的人员
		   FLOW_CONFIG_ACTIVITY activity = null;
		   int k = 0;
		   StringBuffer addBuf = new StringBuffer();
		   StringBuffer reBuf = new StringBuffer();
		   
		   reBuf.append("<table border='0' cellspacing='0' cellpadding='0' align='center'>\r\n");
		   
		   strSql = "select SACTIVITY,EACTIVITY,DOPSN,DODATE,DOIDEA,DOFLAG from FLOW_RUNTIME_ACTIVITY where FID='"+Execute_No+"' and  (DOIDEA is null or DOIDEA != '收回流程。') order by DODATE";//DOFLAG='1' and 
		   DBSet dbset = dbEngine.QuerySQL(strSql);
		   if (dbset != null && dbset.RowCount()>0) {
		     for (int i = 0; i < dbset.RowCount(); i++) {
		       strEACTIVITY = "";
		       strEACTIVITY1 = "";
		       strDOPSN = "";
		       strDODATE = "";
		       strDOIDEA = "";
		       strDOIDEA1 = "";
		       strDOFLAG = "";
		       
		       strDOFLAG = dbset.Row(i).Column("DOFLAG").getString();
		       strEACTIVITY = dbset.Row(i).Column("EACTIVITY").getString();
		       activity = getFlowActivity(strEACTIVITY);
		       
		       if (activity!=null) {
		    	   strEACTIVITY = activity.getNAME();
		       }
		       if (strEACTIVITY.length() > 9) {
		         addBuf.delete(0,addBuf.length());//清空
		         addBuf.append(strEACTIVITY.substring(0, 9)).append("...");
		         strEACTIVITY1 = addBuf.toString();
		       } else {
		         strEACTIVITY1 = strEACTIVITY;
		       }
		       strDOPSN = dbset.Row(i).Column("DOPSN").getString();
		       //根据流程创建人的编号得到创建人的姓名
		       strDOPSNNAME = (String) GetUserNameHashtable.get(strDOPSN);
		       if (strDOPSNNAME == null) {
		          strDOPSNNAME = getUserNameHashtable(strDOPSN);
		       }
		       strDODATE = DateWork.DateTimeToString(dbset.Row(i).Column("DODATE").getDate());
		       
		       strDOIDEA = dbset.Row(i).Column("DOIDEA").getString();
		       strDOIDEA1 = strDOIDEA;
		       if (strDOFLAG.equals("1")) {
		           //如果有签名图片显示图片
		           String isIDIOGRAPH = "";
		           strSql ="select IDIOGRAPH from BPIP_USER_PHOTO where USERID='"+strDOPSN+"' and  IDIOGRAPH is not null";
		           DBSet dbset2 = dbEngine.QuerySQL(strSql,"flow11",strDOPSN);
		           if (dbset2 != null && dbset2.RowCount()>0) {
		              isIDIOGRAPH = "1";
		           } else {
		              isIDIOGRAPH = "0";
		           }
			       if(isIDIOGRAPH.equals("1")) {
			    	   strDOPSNNAME = "<img src='/user/showuserphoto?Act=idiograph&userid=" + strDOPSN+"' width='100' height='30' border='0'>";
			       }
			       //
			       
			       //if (strDOIDEA.length() > 0) {//为空时不显示控制
			       k = k + 1;
			       reBuf.append("<tr><td height='3'></td></tr><tr><td>\r\n").append("<table border='0' width='163'  style='border-style: solid; '  cellspacing='0' cellpadding='0' align='center'>")
		             	.append("<tr><td height='30' width='163' colspan='2' align='center'  bgcolor='#B2E2F0'><a href=# title='").append(strEACTIVITY).append("'><font class='titlefont'>")
			            .append(strEACTIVITY1).append("</font></a></td></tr><tr><td width='163' colspan='2' height='55'  bgcolor='#F7F7F7'>").append("<table border='0' width='160'  align='center'><tr><td><a href=# title='")
			            .append(strDOIDEA).append("'>").append(strDOIDEA1).append("</a></td></tr></table></td></tr><tr><td height='36' width='28' rowspan='2' bgcolor='#F7F7F7'>&nbsp;<img align='absmiddle' src='")
			            .append("/static/ZrWorkFlow/images/").append("flowideaico2.gif'></td>").append("<td height='28' width='135' bgcolor='#F7F7F7'>").append(strDOPSNNAME).append("</td></tr><tr><td height='18' width='135' bgcolor='#F7F7F7'>")
			            .append(strDODATE).append("</td></tr></table></td><tr>");
		       //}
		       } else {
		          if (strNODOPSNS.length()==0) {
		             strNODOPSNS = strDOPSNNAME;
		          } else {
		             strNODOPSNS = strNODOPSNS + "、"+strDOPSNNAME;
		          }
		       }
		     }
		     dbset = null;//赋空值
		   }
		   //显示正在处理中的人员-----------------
		   strEACTIVITY = "";
		   strEACTIVITY1 = "";
		   strNODOPSNS = "";
		   String STATE = "";
		   
		     FLOW_RUNTIME_PROCESS Fprocess = getFlowRuntimeprocess(Execute_No);
		     STATE = Fprocess.getSTATE();
		     strEACTIVITY = Fprocess.getCURRACTIVITY();
		     strNODOPSNS = Fprocess.getACCEPTPSN();
		     
		     if (STATE.equals("1")) {
		        activity = getFlowActivity(strEACTIVITY);
		        if (activity!=null) {
		        	strEACTIVITY = activity.getNAME();
		        }
		        if (strEACTIVITY.length() > 9) {
		            addBuf.delete(0,addBuf.length());//清空
		            addBuf.append(strEACTIVITY.substring(0, 9)).append("...");
		            strEACTIVITY1 = addBuf.toString();
		        } else {
		            strEACTIVITY1 = strEACTIVITY;
		        }
		        String tmpUSerName = "";
		        if (strNODOPSNS.length()>0) {
		             String [] userlist = strNODOPSNS.split(",");
		             for (int i=0;i<userlist.length;i++) {
		                tmpUSerName = (String) GetUserNameHashtable.get(userlist[i]);
		                if (tmpUSerName == null) {
		                   tmpUSerName = getUserNameHashtable(userlist[i]);
		                }
		                if (i==0) {
		                   strNODOPSNS = tmpUSerName;
		                } else {
		                   strNODOPSNS = strNODOPSNS + "," +tmpUSerName;
		                }
		             }
		             reBuf.append("<tr><td height='3'></td><tr><tr><td>\r\n").append("<table border='0'  style='border-style: solid; ' width='163'  height='121' cellspacing='0' cellpadding='0' align='center'>")
		             	  .append("<tr><td height='30' width='163' colspan='2' align='center' bgcolor='#B2E2F0'><a href=# title='").append(strEACTIVITY).append("'><font class='titlefont'>")
		             	  .append(strEACTIVITY1).append("</font></a></td></tr><tr><td width='163' colspan='2' height='55' bgcolor='#F7F7F7'>").append("<table border='0' width='160' align='center'><tr><td><a href=# style='color:red' title='")
		             	  .append(strNODOPSNS).append("'>办理中：").append(strNODOPSNS).append("</a></td></tr></table></td></tr><tr><td height='36' width='28' rowspan='2' bgcolor='#F7F7F7'>&nbsp;")
		             	  .append("</td>").append("<td height='18' width='135' bgcolor='#F7F7F7'>").append("</td></tr><tr><td height='18' width='135' bgcolor='#F7F7F7'>")
		             	  .append("</td></tr></table></td><tr>");
		        }
		   }
		   //显示正在处理中的人员结束--------------
		   reBuf.append("</table>");
		   
		   return reBuf.toString();	  
	}

	@Override
	public String getISwhere(String CID) throws Exception {
        String revalue = "0";
        String strSQL = "";
        String strWHERE1 = "";
        strSQL = "select WHERE1 from FLOW_CONFIG_ACTIVITY_CONNE where CID='"+CID+"' or SID='"+CID+"'";
        
        List<Map<String, Object>> dbset = userMapper.selectListMapExecSQL(strSQL);
        if (dbset != null && dbset.size() > 0) {
        	int length = dbset.size();
        	for (int i=0; i<length; i++) {
        		Map<String, Object> retmap = dbset.get(i);
        		if (retmap != null && retmap.get("WHERE1") != null) {
        			strWHERE1 = retmap.get("WHERE1").toString();
        		}
	            if (strWHERE1.indexOf("[") > -1) {
	                revalue = "1";
	                break;
	            }
        	}
        }
        return revalue;
	}

    public void initHashtable() {
    	GetTitleHashtable.clear();
    	FLOW_RUNTIME_PROCESSHashtable.clear();
    	GetFlowActivityLinkHashtable.clear();
    	GetUserNameHashtable.clear();
    	GetFlowRunIDHTMLHashtable.clear();
    	ADDHashtable.clear();
    	FlowMonitorServiceImpl flowMonitor = new FlowMonitorServiceImpl();
    	flowMonitor.initHashtable();
    	dbEngine.inithashtable();
   }

    private void doMemory_Manage() {
    	LOGGER.info("内存清理开始。");
    	initHashtable();
    	LOGGER.info("内存清理清结束。");
    }

	/**
	 * 功能：根据流程ID得到当前流程的结束活动的ID
	 * @return returnValue 返回结束活动的ID
	 * @throws Exception 
	 */
	private String getEndActivityID(ActFlowRun ActFlowRun) throws Exception {
	    StringBuffer addBuf = new StringBuffer();
	    //得到流程ID
	    String strID = ActFlowRun.getWorkflow_No();
	    addBuf.append("Select ID From FLOW_CONFIG_ACTIVITY where FID='"+strID+"' and TYPE='2' order by ORDER1");
	    List<Map<String, Object>> retlist = userMapper.selectListMapExecSQL(addBuf.toString());
	    if (retlist != null && retlist.size() > 0) {
	        addBuf.delete(0,addBuf.length());//清空
	        addBuf.append(retlist.get(0).get("ID").toString());
	        retlist = null;//赋空值
	    }
	    return addBuf.toString();  
	}

	/**
	 * 作用或功能：流程按钮
	 * @param strName 按钮名称 如：提交
	 * @param strScript 点击时执行的javascript
	 * @param str_Image 图标文件
	 * @return 返回HTML
	 */
	private String clickButton(String strName, String strScript, String str_Image) {
	     StringBuffer strResult = new StringBuffer();
	     strResult.append("&nbsp;<el-button type=\"primary\" size=\"medium\" icon=\""+str_Image+"\" href=\"javascript:void(0)\" onclick=\"javascript:"+strScript+"\">"+strName+"</el-button>");
	     
	     return strResult.toString(); 
	}

	/**
	 * 作用或功能：流程按钮1(图标形式)
	 * @param strName 按钮名称 如：提交
	 * @param strScript 点击时执行的javascript
	 * @param str_Image 图标文件
	 * @return 返回HTML
	 */
	private String clickButton1(String strName, String strScript, String str_Image) {
	      StringBuffer strResult = new StringBuffer();
	      //--------
	      if (str_Image.length() > 3) {
	        strResult.append("<td><table height='22' border='0' cellspacing='0' cellpadding='0'><tr><td style='cursor:pointer' onclick=\"" 
	        				+ strScript+"\" height='22'><img border='0' align='absmiddle' src='"
	        				+ SysPreperty.getProperty().AppUrl+"Zrsysmanage/images/blueimg/"
	        				+ "'>&nbsp;<font class='titleFont'>"
	        				+ strName+"</font>&nbsp;</td></tr></table></td>\r\n");
	      } else {
	        strResult.append("<td><table height='22' border='0' cellspacing='0' cellpadding='0'><tr><td style='cursor:pointer' onclick=\""
	        				+ strScript+"\" height='22' >&nbsp;<font class='titleFont'>"
	        				+ strName+"</font>&nbsp;</td></tr></table></td>\r\n");
	      }
	      //----------
	      return strResult.toString();  
	}

	/**
	 * 功能:处理条件表达式，使其能直接参与数据库的Where条件运算
	 * @param expression String            待计算的函数表达式
	 * @return boolean                     函数返回的结果真或假
	 * 表达式中的[表名.字段名]的值在表达式计算前须取出来后将表达式的对应部公置换掉
	 * @throws Exception 
	 */
	private boolean getConditionValueByExpression(String expression, ActFlowRun ActFlowRun, SessionUser user) throws Exception {
	    String sqlStr;
	    boolean returnValue = false;
	    LOGGER.info("getConditionValueByExpression开始调用...");
	    expression = prepareExpression(expression,ActFlowRun,user);
	    StringBuffer addBuf = new StringBuffer();
	    //通过执行SQL语句来计算逻辑表达式的值
	    addBuf.append("Select count(*) as amount from FLOW_CONFIG_PROCESS Where ("+expression+")");
	    sqlStr = addBuf.toString();
	    LOGGER.info(sqlStr);
	    List<Map<String, Object>> retlist = userMapper.selectListMapExecSQL(sqlStr);
	    if (retlist != null && retlist.size() > 0) {
	    	returnValue = Integer.parseInt(retlist.get(0).get("amount").toString()) > 0 ? true : false;
	    	retlist = null;//赋空值
	    } else {
	      returnValue = true; //当retlist为null时，则表达式出现错误
	    }
	    LOGGER.info("getConditionValueByExpression结束调用.");
	    return returnValue;
	}

	/**
	 * 功能:处理表达式，使其能计算出来
	 * @param expression 待计算的函数表达式
	* @return String     处理完后的表达式
	 * @throws Exception 
	*/
	private String prepareExpression(String expression,ActFlowRun ActFlowRun,SessionUser user) throws Exception {
	    String sqlStr, relationField, tempValue,sqlStrTemp,strPrimary;
	    //初始化参数---------
	    String RelationFieldValue = ActFlowRun.getOtherID();//关联字段的ID值
	    String strParentID = ActFlowRun.getParentID();//父流程ID值或父表单记录值
	    String strParentID1 = ActFlowRun.getParentID1();//父流程ID1值或父表单记录值1
	    String strExecute_No = ActFlowRun.getExecute_No();//流程流转编号
	    //初始化参数---------
	    DBSet mdbset = null;
	    int beginIndex, endIndex;
	    String[] TableField;
	    SimpleDateFormat fmt;
	    StringBuffer  addBuf = new   StringBuffer();
	    fmt = new SimpleDateFormat("yyyy-MM-dd");
	    LOGGER.info("prepareExpression", "开始调用");
	    LOGGER.info("expression="+expression);
	    while (expression.indexOf("[") > -1) { //处理表达式中的[表名.字段名]
	      beginIndex = expression.indexOf("[");
	      endIndex = expression.indexOf("]");
	      TableField = expression.substring(beginIndex + 1, endIndex).split("\\.");
	      
	      addBuf.delete(0,addBuf.length());//清空
	      addBuf.append("Select "+TableField[1]+" from "+TableField[0]);
	      sqlStr = addBuf.toString();
	      // relationField = GetRelationField(FORMTABLE);
	      relationField = getRelationField(TableField[0]);
	      mdbset = null;
	      //根据流转ID
	      if(strExecute_No!= null && strExecute_No.trim().length()>0) {
	        strPrimary=getPrimaryFieldName(TableField[0]);
	        if(strPrimary != null) {
	          addBuf.delete(0,addBuf.length());//清空
	          addBuf.append("Select "+TableField[1]+" from "+TableField[0])
	                .append(" WHERE "+strPrimary+"=(SELECT FORMID FROM FLOW_RUNTIME_PROCESS WHERE ID='")
	                .append(strExecute_No+"')");
	          sqlStrTemp = addBuf.toString();
	          LOGGER.info(sqlStrTemp);
	          mdbset = dbEngine.QuerySQL(sqlStrTemp);
	        }
	      }
	      if (mdbset == null) {
	        addBuf.delete(0,addBuf.length());//清空
	        addBuf.append(sqlStr+" Where "+relationField+"='"+RelationFieldValue+"'");
	        mdbset = dbEngine.QuerySQL(addBuf.toString());
	        LOGGER.info("sqla:"+addBuf.toString());
	      }
	      if (mdbset == null) {
	        addBuf.delete(0,addBuf.length());//清空
	        addBuf.append(sqlStr+" Where "+relationField+"='"+strParentID+"'");
	        mdbset = dbEngine.QuerySQL(addBuf.toString());
	        LOGGER.info("sqlb:"+addBuf.toString());
	      }
	      if (mdbset == null) {
	        addBuf.delete(0,addBuf.length());//清空
	        addBuf.append(sqlStr+" Where "+relationField+"='"+strParentID1+"'");
	        mdbset = dbEngine.QuerySQL(addBuf.toString());
	        LOGGER.info("sqlc:"+addBuf.toString());
	      }
	      if (mdbset != null) {
	        try {
	          if (mdbset.Row(0).Column(TableField[1]).getType() == DBType.DATE || 
	        		  mdbset.Row(0).Column(TableField[1]).getType() == DBType.DATETIME) {
	             fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	             tempValue = fmt.format(mdbset.Row(0).Column(TableField[1]).getDate());
	          } else {
	            tempValue = mdbset.Row(0).Column(TableField[1]).getValue().toString();
	          }
	        } catch (Exception ex) {
	          LOGGER.error("出现异常", ex.getMessage());
	          tempValue = "";
	        }
	        expression = expression.replaceAll("\\[" + TableField[0] + "\\." + TableField[1] + "\\]", tempValue);
	      } else {
	        //为了算法的稳定，若没有只有将相应的[表名.字段名]置为了
	        expression = expression.replaceAll("\\[" + TableField[0] + "\\." + TableField[1] + "\\]", "");
	      }
	    }
	    //处理当前日期
	    if (DataBaseType.equals("1")) {// Oracle数据库
			expression = expression.replaceAll("sysdate", "to_char(sysdate,'yyyy-mm-dd')");
		}
		else if (DataBaseType.equals("2")) {// MSSQL数据库
			expression = expression.replaceAll("sysdate", "getdate()");
		}
		else if (DataBaseType.equals("3")){// MySQL数据库
			expression = expression.replaceAll("sysdate", "str_to_date(sysdate,'%Y-%m-%d')");
		}
	    //处理自定义的变量
	    if (expression.lastIndexOf("{LoginID}") > -1) { //登录名
	      expression = expression.replaceAll("\\{LoginID\\}", user.getLoginID());
	    }
	    if (expression.lastIndexOf("{Name}") > -1) { //姓名
	      expression = expression.replaceAll("\\{Name\\}", user.getName());
	    }
	    if (expression.lastIndexOf("{UserID}") > -1) { //用户编号
	      expression = expression.replaceAll("\\{UserID\\}", user.getUserID());
	    }
	    if (expression.lastIndexOf("{LCODE}") > -1) { //用户内部编号
	      expression = expression.replaceAll("\\{LCODE\\}", user.getLCODE());
	    }
	    if (expression.lastIndexOf("{UnitName}") > -1) { //所在单位名称
	      expression = expression.replaceAll("\\{UnitName\\}", user.getUnitName());
	    }
	    if (expression.lastIndexOf("{UnitID}") > -1) { //所在单位编号
	      expression = expression.replaceAll("\\{UnitID\\}", user.getUnitID());
	    }
	    if (expression.lastIndexOf("{Custom1}") > -1) { //自定义参数1
	           expression = expression.replaceAll("\\{Custom1\\}", user.getCustom1());
	    }
	    if (expression.lastIndexOf("{Custom2}") > -1) { //自定义参数2
	           expression = expression.replaceAll("\\{Custom2\\}", user.getCustom2());
	    }
	    if (expression.lastIndexOf("{Custom3}") > -1) { //自定义参数3
	           expression = expression.replaceAll("\\{Custom3\\}", user.getCustom3());
	    }
	    if (expression.lastIndexOf("{Custom4}") > -1) { //自定义参数4
	           expression = expression.replaceAll("\\{Custom4\\}", user.getCustom4());
	    }
	    if (expression.lastIndexOf("{Custom5}") > -1) { //自定义参数5
	           expression = expression.replaceAll("\\{Custom5\\}", user.getCustom5());
	    }
	    //-------------------------
	    /*******************************时间的处理************************************/
	    Date date = new Date();
	    if (expression.lastIndexOf("{YYYY年}") > -1) { //当前年(中文式)
	      fmt = new SimpleDateFormat("yyyy年");
	      expression = expression.replaceAll("\\{YYYY年\\}", fmt.format(date));
	    }
	    if (expression.lastIndexOf("{YYYY年MM月}") > -1) { //当前年月(中文式)
	      fmt = new SimpleDateFormat("yyyy年MM月");
	      expression = expression.replaceAll("\\{YYYY年\\}", fmt.format(date));
	    }
	    if (expression.lastIndexOf("{YYYY年MM月DD日}") > -1) { //当前日期(中文式)
	      fmt = new SimpleDateFormat("yyyy年MM月dd日");
	      expression = expression.replaceAll("\\{YYYY年MM月DD日\\}", fmt.format(date));
	    }
	    if (expression.lastIndexOf("{YYYY年MM月DD日 HH:MM:SS}") > -1) { //当前时间(中文式)
	      fmt = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
	      expression = expression.replaceAll("\\{YYYY年MM月DD日 HH:MM:SS\\}", fmt.format(date));
	    }
	    if (expression.lastIndexOf("{YYYY}") > -1) { //当前年
	      fmt = new SimpleDateFormat("yyyy");
	      expression = expression.replaceAll("\\{YYYY\\}", fmt.format(date));
	    }
	    if (expression.lastIndexOf("{YYYY-MM}") > -1) { //当前年月
	      fmt = new SimpleDateFormat("yyyy-MM");
	      expression = expression.replaceAll("\\{YYYY-MM\\}", fmt.format(date));
	    }
	    if (expression.lastIndexOf("{YYYY-MM-DD}") > -1) { //当前日期
	      fmt = new SimpleDateFormat("yyyy-MM-dd");
	      expression = expression.replaceAll("\\{YYYY-MM-DD\\}", fmt.format(date));
	    }
	    if (expression.lastIndexOf("{YYYY-MM-DD HH:MM:SS}") > -1) { //当前时间
	      fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	      expression = expression.replaceAll("\\{YYYY-MM-DD HH:MM:SS\\}", fmt.format(date));
	    }
	    LOGGER.info("prepareExpression结束调用");
	    mdbset = null;//赋空值
	    return expression;
	}

	/**
	 * 根据表名称获取关联字段的名称
	 * @param FORMTABLE String  表名称
	 * @return String           关联字段名称
	 * @throws Exception 
	 */
	private String getRelationField(String FORMTABLE) throws Exception {
	    StringBuffer addBuf = new StringBuffer();
	    addBuf.append("SELECT b.FIELDNAME FROM BPIP_TABLE a,BPIP_FIELD b,COLL_DOC_CONFIG c Where a.tablename='")
	          .append(FORMTABLE+"' and b.TABLEID=a.TABLEID and c.MAINTABLE=a.Tableid and b.FIELDID=c.OTHERFIELD");
	    List<Map<String, Object>> retlist = userMapper.selectListMapExecSQL(addBuf.toString());
	    addBuf.delete(0,addBuf.length());//清空
	    if (retlist != null && retlist.size() > 0) {
	      addBuf.append(retlist.get(0).get("FIELDNAME").toString());
	      retlist = null;//赋空值
	    }
	    return addBuf.toString();
	}

	/**
	 * 根据表名获取其主键字段名
	 * @param tableName String
	 * @return String
	 * @throws Exception 
	 */
	private String getPrimaryFieldName(String tableName) throws Exception {
	    String resultStr = null;
	    StringBuffer addBuf = new StringBuffer();
	    addBuf.append("SELECT PRIMARYKEY FROM BPIP_TABLE WHERE TABLENAME='"+tableName+"'");
	    List<Map<String, Object>> retlist = userMapper.selectListMapExecSQL(addBuf.toString());
	    if(retlist != null && retlist.size() > 0) {
	      resultStr = retlist.get(0).get("PRIMARYKEY").toString();
	      retlist = null;//赋空值
	    }
	    return resultStr;  
	}

	/**
	 * 功能：得到某个活动的排序号
	 * @param ActivityID 活动ID
	 * @return 返回活动的排序号
	 * @throws Exception
	 */
	private int getFlowActivityOrder(String ActivityID) throws Exception {
	    int returnValue = 0;
	    StringBuffer addBuf = new StringBuffer();
	    addBuf.append("Select ORDER1 From FLOW_CONFIG_ACTIVITY where ID='"+ActivityID+"'");
	    List<Map<String, Object>> dbset = userMapper.selectListMapExecSQL(addBuf.toString());
	    if (dbset != null && dbset.size() > 0) {
	          returnValue = Integer.parseInt(dbset.get(0).get("ORDER1").toString());
	          dbset = null;//赋空值
	    }
	    return returnValue;  
	}

	/**
	 * 功能：得到当前部门的所有人员字符串
	 * @param strUnitID单位或部门ID
	 * @return returnValue 返回本部门的人员字符串
	 * @throws Exception 
	 */
	private String getCDeptUser(String strUnitID) throws Exception {
	    StringBuffer addBuf = new StringBuffer();
	    String strRevalue = "";
	    addBuf.append("Select USERID From BPIP_USER where USERSTATE='0' and UNITID='"+strUnitID)
	          .append("' order by ORBERCODE,USERID");
	    List<Map<String, Object>> dbset = userMapper.selectListMapExecSQL(addBuf.toString());
	    addBuf.delete(0,addBuf.length());//清空
	    int length = dbset != null ? dbset.size() : 0;
	    if (length > 0) {
	      for (int i = 0; i < length; i++) {
	    	Map<String, Object> retmap = dbset.get(i);
	        if (addBuf.toString().length()==0) {
	            addBuf.append(retmap.get("USERID").toString());
	        } else {
	           addBuf.append(","+retmap.get("USERID").toString());
	        }
	      }
	      dbset = null;//赋空值
	    }
	    strRevalue = addBuf.toString();
	    return strRevalue;  
	}

	/**
	 * 功能：得到当前部门及上级部门的所有人员字符串
	 * @param strUnitID单位或部门ID
	 * @return returnValue 返回人员字符串
	 */
	private String getCUpDeptUser(String strUnitID) {
	    StringBuffer addBuf = new StringBuffer();
	    addBuf.append("Select USERID From BPIP_USER where USERSTATE='0' and UNITID='"+strUnitID+"'");
	    StringWork sw = new StringWork();
	    //得到去掉0后的本单位编码
	    String strZunit = sw.CutLastZero(strUnitID,2);
	    String strZunit1="";
	    String strZero="";
	    if (strZunit.length()>2) {
	      for (int i=strZunit.length()-2;i>0;i=i-2) {
	         strZero="";
	         for (int j=0;j<12-i;j++) {
	           strZero = strZero + "0";
	         }
	         strZunit1 = strZunit.substring(0,i)+strZero;
	         addBuf.append(" or UNITID='"+strZunit1+"'");
	      }
	    }
	    return addBuf.toString(); 
	}

	/**
	 * 功能：得到当前流程上一步的处理人
	 * @param strExecuteNo流程流转表ID
	 * @return returnValue 返回上一步的处理人编号
	 * @throws Exception 
	 */
	private String getFlowUpDoUser(String strExecuteNo) throws Exception {
	    StringBuffer addBuf = new StringBuffer();
	    //得到处理时间最晚的一条记录（即上一步的处理记录）
	    addBuf.append("Select DOPSN From FLOW_RUNTIME_ACTIVITY where FID='"+strExecuteNo+"' order DODATE desc");
	    List<Map<String, Object>> dbset = userMapper.selectListMapExecSQL(addBuf.toString());
	    addBuf.delete(0,addBuf.length());//清空
	    if (dbset != null && dbset.size() > 0) {
	       addBuf.append(dbset.get(0).get("DOPSN").toString());
	       dbset = null;//赋空值
	    }
	    return addBuf.toString();  
	}

	/**
	 * 功能：得到某一流向的相关活动的处理人(接收人属性的接收人类型为:某一活动的处理人时使用)
	 * @param strExecuteNo 流程流转表ID
	 * @param strConnID 流向关系ID
	 * @param ActFlowRun 流程流转类
	 * @return returnValue 返回流程处理人编号
	 */
	private String getSelectActivityDoUser(String strExecuteNo, String strConnID, ActFlowRun ActFlowRun) {
	    try {
		    String returnValue = "";
		    String OtherValue = "";
		    String strFlowRunPARENTID1="";
		    String strTmp="";
		    StringBuffer addBuf = new StringBuffer();
		    StringBuffer ReBuf = new StringBuffer();
		    FLOW_RUNTIME_PROCESS Fprocess = null;
		    Fprocess = getFlowRuntimeprocess(strExecuteNo);
		    if (Fprocess!=null) {
		        OtherValue = Fprocess.getOTHERID();
		        strFlowRunPARENTID1 = Fprocess.getPARENTID1();
		    } else {
		        OtherValue =ActFlowRun.getOtherID();
		        strFlowRunPARENTID1 = ActFlowRun.getParentID1();
		    }
		    //---------------------------------
		      addBuf.append("Select DOPSN From FLOW_RUNTIME_ACTIVITY where DOFLAG='1' and EACTIVITY  in (Select AAID From FLOW_CONFIG_CONN_AUTHOR "
		      			  + "where CONNID='"+strConnID+"') and FID in (Select ID From FLOW_RUNTIME_PROCESS where (OTHERID='"+OtherValue)
		            .append("' or PARENTID='"+OtherValue+"') and PARENTID1='"+strFlowRunPARENTID1+"')");
		      
		      List<Map<String, Object>> dbset1 = userMapper.selectListMapExecSQL(addBuf.toString());
		      if (dbset1 != null && dbset1.size() > 0) {
		    	  int length1 = dbset1.size();
		          for (int i=0; i<length1; i++) {
		        	 Map<String, Object> retmap1 = dbset1.get(i);
		             strTmp = retmap1.get("DOPSN").toString();
		             if (ReBuf.toString().indexOf(strTmp)==-1) {
		            	 ReBuf.append(strTmp+",");
		             }
		          }
		          dbset1 = null;//赋空值
		        }
		        //--------------------------------
		        if (ReBuf.toString().length()==0) {//没有找到
		          if (Fprocess!=null) {
		        	  OtherValue = Fprocess.getPARENTID();
		          } else {
		        	  OtherValue = ActFlowRun.getParentID();
		          }
		          addBuf.delete(0,addBuf.length());//清空
		          addBuf.append("Select DOPSN From FLOW_RUNTIME_ACTIVITY where DOFLAG='1' and  EACTIVITY in (Select AAID From FLOW_CONFIG_CONN_AUTHOR "
		          			  + "where CONNID='"+strConnID+"') and FID in (Select ID From FLOW_RUNTIME_PROCESS where OTHERID='"+OtherValue)
		                .append("' or PARENTID='"+OtherValue+"' or PARENTID1='"+OtherValue+"')");
		          
		          List<Map<String, Object>> dbset2 = userMapper.selectListMapExecSQL(addBuf.toString());
		          if (dbset2 != null && dbset2.size() > 0) {
		        	  int length2 = dbset2.size();
		              for (int i=0;i<length2;i++) {
		            	  Map<String, Object> remtap2 = dbset2.get(i);
		                  strTmp = remtap2.get("DOPSN").toString();
		                  if (ReBuf.toString().indexOf(strTmp)==-1) {
		                    ReBuf.append(strTmp+",");
		                  }
		              }
		              dbset2=null;//赋空值
		          }
		        }
		        if (ReBuf.toString().length()==0) {//没有找到
		          OtherValue = strFlowRunPARENTID1;
		          addBuf.delete(0,addBuf.length());//清空
		          addBuf.append("Select DOPSN From FLOW_RUNTIME_ACTIVITY where  DOFLAG='1' and EACTIVITY in (Select AAID From FLOW_CONFIG_CONN_AUTHOR where CONNID='")
		                .append(strConnID+"') and FID in (Select ID From FLOW_RUNTIME_PROCESS where OTHERID='")
		                .append(OtherValue+"' or PARENTID='"+OtherValue+"' or PARENTID1='")
		                .append(OtherValue+"')");
		          
		          List<Map<String, Object>> dbset3 = userMapper.selectListMapExecSQL(addBuf.toString());
		          int length3 = dbset3 != null ? dbset3.size() : 0;
		          if (length3 > 0) {
		            for (int i=0; i<length3; i++) {
		               Map<String, Object> retmap3 = dbset3.get(i);
		               strTmp = retmap3.get("DOPSN").toString();
		               if (ReBuf.toString().indexOf(strTmp)==-1) {
		                 ReBuf.append(strTmp+",");
		               }
		            }
		            dbset3 = null;//赋空值
		          }
		        }
		    //-----------------------------------------------
		    returnValue = ReBuf.toString();
		    if (returnValue.length() > 0) {
		      returnValue = returnValue.substring(0, returnValue.length() - 1);
		    }
		    return returnValue;
	    } catch (Exception ex) {
	         LOGGER.error("流程引擎---GetSelectActivityDoUser函数出错：", ex);
	         return "";
	    }  
	}

	/**
	 * 功能：得到可操作用户组的人员编号
	 * @param strID 活动ID
	 * @return returnValue 返回人员编号字符串
	 * @throws Exception 
	 */
	private String getFlowGroupUser(String strID) throws Exception {
        String tmpUserNo = "";
        StringBuffer addBuf = new StringBuffer();
        //根据组ID得到用户编号-------  
        addBuf.append("Select DISTINCT a.USERID From BPIP_USER_ROLE a left join FLOW_CONFIG_ACTIVITY_GROUP b on a.ROLEID=b.GROUPID where b.ACTIVITYID='")
              .append(strID + "'");
        List<Map<String, Object>> dbset1 = userMapper.selectListMapExecSQL(addBuf.toString());
        addBuf.delete(0, addBuf.length());//清空
        if (dbset1 != null && dbset1.size()>0) {
          int length1 = dbset1.size();
          for (int j = 0; j < length1; j++) {
        	  Map<String, Object> retmap1 = dbset1.get(j);
              tmpUserNo = retmap1.get("USERID").toString();
              if (addBuf.toString().length()==0) {
                    addBuf.append(tmpUserNo);
              } else {
                    addBuf.append(","+tmpUserNo);
              }
          }
          dbset1 = null;//赋空值
        }
        //-------------------------
        return addBuf.toString();  
	}

	/**
	 * 功能：得到本部门的可操作用户组的人员编号
	 * @param strID 活动ID
	 * @param UnitID 部门ID
	 * @return returnValue 返回人员编号字符串
	 * @throws Exception 
	 */
	private String getFlowGroupDeptUser(String strID, String strUnitID) throws Exception {
	    String strRvalue = "";
	    String tmpUserNo = "";
	    StringBuffer addBuf = new StringBuffer();
	    //根据组ID得到用户编号-------
	    addBuf.append("Select DISTINCT USERID From BPIP_USER_ROLE where ROLEID in (Select GROUPID From FLOW_CONFIG_ACTIVITY_GROUP where ACTIVITYID='")
	          .append(strID+"') and USERID in(Select USERID From BPIP_USER where  USERSTATE='0' and UNITID='"+strUnitID+"')");
	    
	    List<Map<String, Object>> dbset1 = userMapper.selectListMapExecSQL(addBuf.toString());
	    addBuf.delete(0, addBuf.length());//清空
	    if (dbset1 != null && dbset1.size()>0) {
	    	int length1 = dbset1.size();
	        for (int j = 0; j < length1; j++) {
	        	Map<String, Object> retmap1 = dbset1.get(j);
                tmpUserNo = retmap1.get("USERID").toString();
                if (addBuf.toString().length()==0) {
                   addBuf.append(tmpUserNo);
                } else {
                   addBuf.append(","+tmpUserNo);
                }
	        }
	        dbset1 = null;//赋空值
	    }
	    strRvalue = addBuf.toString();
	    
	    return strRvalue;  
	}

	/**
	 * 功能：得到本部门及上级或下级部门可操作用户组的人员编号
	 * @param strID 活动ID
	 * @param UnitID 部门ID
	 * @param type 1 上级部门 2 下级部门
	 * @return returnValue 返回人员编号字符串
	 */
	private String getFlowGroupDeptUpUser(String strID, String strUnitID, String type) {
	    try {
		    String strRevalue = "";
		    strRevalue = (String) ADDHashtable.get(strID+strUnitID+type);
		    if (strRevalue == null) {
			    String tmpUserNo = "";
			    //本单位及上级单位
			    String strUpSql="";
			    String strDownSql="";
			    StringBuffer addBuf = new StringBuffer();
			    if (type.equals("1")) {
			         //本单位及上级单位的所有人员sql
			         strUpSql = getCUpDeptUser(strUnitID);
			    }
			    if (type.equals("2")){
			        //本单位及下级单位的所有人员字符串
			        StringWork sw = new StringWork();
			        String strZunit = sw.CutLastZero(strUnitID,2);
			        addBuf.append("Select USERID From BPIP_USER where  USERSTATE='0' and UNITID like '"+strZunit+"%'");
			        strDownSql = addBuf.toString();
			    }
			    if (type.equals("3")){
			        //本单位及同级单位的所有人员字符串
			        StringWork sw = new StringWork();
			        String strZunit = sw.CutLastZero(strUnitID,2);
			        strZunit = strZunit.substring(0,strZunit.length()-2);
			        addBuf.append("Select USERID From BPIP_USER where  USERSTATE='0' and UNITID like '"+strZunit+"%'");
			        strDownSql = addBuf.toString();
			    }
			    if (type.equals("4")){
			         //本部门、同级部门、上级部门的所有人员字符串
			         StringWork sw = new StringWork();
			         strDownSql = getCUpDeptUser(strUnitID);
			         
			         String strZunit = sw.CutLastZero(strUnitID,2);
			         strZunit = strZunit.substring(0,strZunit.length()-2);
			         strDownSql = strDownSql + " or UNITID like '"+strZunit+"%'";
			    }
			    if (type.equals("5")){
			       String strccv = "";
			       String strZE = "";
			       
			       StringWork sw = new StringWork();
			       String strZunit = sw.CutLastZero(strUnitID,2);
			       strZunit = strZunit.substring(0,strZunit.length()-2);
			       for (int en=0;en<(12-strZunit.length());en++) {
			          strZE = strZE + "0";
			       }
			       strccv = strZunit.substring(0,strZunit.length()-4);
			       addBuf.append("Select USERID From BPIP_USER where  USERSTATE='0' and (UNITID like '"+strZunit+"%' or (UNITID like '"+strccv+"%' and UNITID like '%"+strZE+"' ))");
			       strDownSql = addBuf.toString();
			   }
			   addBuf.delete(0,addBuf.length());//清空
			   //根据组ID得到用户编号-------
		       if (type.equals("1")) {
	                addBuf.append("Select DISTINCT USERID From BPIP_USER_ROLE where ROLEID in (Select GROUPID From FLOW_CONFIG_ACTIVITY_GROUP where ACTIVITYID='")
	                      .append(strID+"') and USERID in ("+strUpSql+")");
	           } else {
	                addBuf.append("Select DISTINCT USERID From BPIP_USER_ROLE where ROLEID in (Select GROUPID From FLOW_CONFIG_ACTIVITY_GROUP where ACTIVITYID='")
	                      .append(strID+"')  and USERID in ("+strDownSql+")");
	           }
			   List<Map<String, Object>> dbset1 = userMapper.selectListMapExecSQL(addBuf.toString());
			   addBuf.delete(0,addBuf.length());//清空
			   if (dbset1 != null && dbset1.size()>0) {
				   int length1 = dbset1.size();
			       for (int j = 0; j < length1; j++) {
			    	   Map<String, Object> retmap1 = dbset1.get(j);
			           tmpUserNo = retmap1.get("USERID").toString();
			           if (addBuf.toString().indexOf(tmpUserNo) == -1) { //没有增加
			               if (addBuf.toString().length()==0) {
			                   addBuf.append(tmpUserNo);
			               } else {
			                   addBuf.append(","+tmpUserNo);
			               }
			           }
			       }
			       dbset1=null;//赋空值
			   }
			   //-------------------------
			   strRevalue = addBuf.toString();
			   ADDHashtable.put(strID+strUnitID+type,strRevalue);
		    }
		    return strRevalue;
	    } catch (Exception ex) {
	         LOGGER.error("流程引擎---GetFlowGroupDeptUpUser函数出错：", ex);
	         return "";
	    }
	}

	/**
	 * 功能：流程标识/流程名称对照表
	 * @param strWhere 条件
	 * @return 对照表
	 * @throws Exception 
	 */
	private Map<String, Object> getFlowNameHashtable(String strWhere) throws Exception {
	    String strIDENTIFICATION = "";
	    String strIDENTIFICATIONS = "";
	    String strNAME = "";
	    StringBuffer addBuf = new StringBuffer();
	    Map<String, Object> FlowName = new HashMap<String, Object>();
	    if (strWhere.indexOf(",")>-1) {
	        if (strWhere.indexOf("'")==-1) {
	            strWhere = strWhere.replaceAll(",", "','");
	            addBuf.append("'"+strWhere+"'");
	            strWhere = addBuf.toString();
	        }
	    } else {
	        if (strWhere.indexOf("'")==-1) {
	            addBuf.append("'"+strWhere+"'");
	            strWhere = addBuf.toString();
	        }
	    }
	    addBuf.delete(0,addBuf.length());//清空
	    addBuf.append("Select IDENTIFICATION,NAME from FLOW_CONFIG_PROCESS "
	    			+ "where IDENTIFICATION in ("+strWhere+")");
	    List<Map<String, Object>> dbset = userMapper.selectListMapExecSQL(addBuf.toString());
	    if (dbset != null && dbset.size() > 0) {
	       int length = dbset.size();
	       for(int i=0; i<length; i++) {
	    	  Map<String, Object> retmap = dbset.get(i);
	          strIDENTIFICATION = retmap.get("IDENTIFICATION").toString();
	          strNAME = retmap.get("NAME").toString();
	          if (strIDENTIFICATIONS.indexOf(strIDENTIFICATION)==-1) {
	            FlowName.put(strIDENTIFICATION,strNAME);
	            addBuf.delete(0,addBuf.length());//清空
	            addBuf.append(strIDENTIFICATIONS+strIDENTIFICATION+",");
	            strIDENTIFICATIONS=addBuf.toString();
	          }
	       }
	       dbset = null;//赋空值
	    }
	    return FlowName;  
	}

	/**
	 * 功能：根据流程标识得到流程标识/可以新建的流程数对照表
	 * @param strID 流程标识串
	 * @param strUnit 流程单位
	 * @return 对照表
	 * @throws Exception 
	 */
	private Map<String, Object> getFlowNewNumHashtable(String strID) throws Exception {
	     int intNum = 0;
	     String strIDENTIFICATION="";
	     String strIDENTIFICATIONS="";
	     StringBuffer addBuf = new StringBuffer();
	     Map<String, Object> NewNumHashtable = new HashMap<String, Object>();
	     if (strID.indexOf(",")>-1) {
	         if (strID.indexOf("'")==-1) {
	             strID = strID.replaceAll(",", "','");
	             addBuf.append("'").append(strID).append("'");
	             strID = addBuf.toString();
	         }
	     } else {
	         if (strID.indexOf("'")==-1) {
	             addBuf.append("'").append(strID).append("'");
	             strID = addBuf.toString();
	         }
	     }
	     addBuf.delete(0,addBuf.length());//清空
	     addBuf.append("Select IDENTIFICATION,CNUM from FLOW_CONFIG_PROCESS "
	     			 + "where IDENTIFICATION in ("+strID).append(")");
	     List<Map<String, Object>> dbset = userMapper.selectListMapExecSQL(addBuf.toString());
	     if (dbset != null && dbset.size() > 0) {
	    	 int length = dbset.size();
	         for(int i=0; i<length; i++) {
	        	Map<String, Object> retmap = dbset.get(i);
	            intNum = Integer.parseInt(retmap.get("CNUM").toString());
	            strIDENTIFICATION = retmap.get("IDENTIFICATION").toString();
	            if (strIDENTIFICATIONS.indexOf(strIDENTIFICATION)==-1) {
	               NewNumHashtable.put(strIDENTIFICATION,String.valueOf(intNum));
	               addBuf.delete(0,addBuf.length());//清空
	               addBuf.append(strIDENTIFICATIONS).append(strIDENTIFICATION).append(",");
	               strIDENTIFICATIONS=addBuf.toString();
	            }
	         }
	         dbset = null;//赋空值
	     }
	     return NewNumHashtable;
	}

	/**
	 * 功能：根据流程标识串和关联ID得到流程标识/已经建立的流程数对照表
	 * @param strID 流程标识中
     * @param ActFlowRun 流程运转参数
     * @param strWhere 条件
     * @return 对照表
	 * @throws Exception 
     */
    private Map<String, Object> getFlowAlreadyNumHashtable(String strID, ActFlowRun ActFlowRun, String strWhere) throws Exception {
	      String strRID = ActFlowRun.getRID();
	      String strOtherID = ActFlowRun.getOtherID();
	      String strPARENTID = ActFlowRun.getParentID();
	      String strPARENTID1 = ActFlowRun.getParentID1();
	      Map<String, Object> AlreadyNumHashtable1 = new HashMap<String, Object>();
	      Map<String, Object> AlreadyNumHashtable2 = new HashMap<String, Object>();
	      String strIDENTIFICATIONS="";
	      String strIDENTIFICATION="";
	      String strTmp="";
	      int intNum=0;
	      StringBuffer addBuf1 = new StringBuffer();
	      StringBuffer addBuf2 = new StringBuffer();
	      StringBuffer tmpBuf = new StringBuffer();
	      
	      if (strWhere.length()>0) {
	         strID = strWhere;
	      } else {
		      if (strID.indexOf(",")>-1) {
		        if (strID.indexOf("'")==-1) {
		            strID = strID.replaceAll(",", "','");
		            addBuf1.append("'"+strID+"'");
		            strID = addBuf1.toString();
		        }
		      } else {
		        if (strID.indexOf("'")==-1) {
		            addBuf1.append("'"+strID+"'");
		            strID = addBuf1.toString();
		        }
		      }
	      }
          addBuf1.delete(0, addBuf1.length());//清空
          addBuf1.append("Select FLOWID,count(ID) as num from FLOW_RUNTIME_PROCESS where FLOWID in ("+strID)
                 .append(") and RID='"+strRID+"' group by FLOWID");
          addBuf2.append("Select FLOWID,count(ID) as num from FLOW_RUNTIME_PROCESS where FLOWID in ("+strID)
              	 .append(") and (OTHERID ='"+strOtherID+"' or PARENTID ='"+strOtherID+"' or PARENTID1 ='")
              	 .append(strOtherID+"'");
          
          if (strPARENTID.length()>0 && !strPARENTID.equals("null")) {
        	  addBuf2.append("or OTHERID ='"+strPARENTID+"' or PARENTID ='"+strPARENTID)
                	 .append("' or PARENTID1 ='"+strPARENTID+"'");
          }
          if (strPARENTID1.length()>0 && !strPARENTID1.equals("null")) {
        	  addBuf2.append("or OTHERID ='"+strPARENTID1+"' or PARENTID ='"+strPARENTID1)
                	 .append("' or PARENTID1 ='"+strPARENTID1+"'");
          }
          addBuf2.append(")  group by FLOWID");
          //无关联ID的情况
          List<Map<String, Object>> dbset1 = userMapper.selectListMapExecSQL(addBuf2.toString());
          if (dbset1 != null && dbset1.size() > 0) {
        	  int length1 = dbset1.size();
	          for(int i=0; i<length1; i++) {
	        	  Map<String, Object> retmap1 = dbset1.get(i);
	              strIDENTIFICATION = retmap1.get("FLOWID").toString();
	              intNum = Integer.parseInt(retmap1.get("num").toString());
	              if (strIDENTIFICATIONS.indexOf(strIDENTIFICATION)==-1) {
	                  AlreadyNumHashtable1.put(strIDENTIFICATION,String.valueOf(intNum));
	                  tmpBuf.delete(0,tmpBuf.length());//清空
	                  tmpBuf.append(strIDENTIFICATIONS+strIDENTIFICATION+",");
	                  strIDENTIFICATIONS=tmpBuf.toString();
	              }
	          }
	          dbset1 = null;//赋空值
          }
	      if (strRID.length()>0) {
	        strIDENTIFICATIONS = "";
	        List<Map<String, Object>> dbset2 = userMapper.selectListMapExecSQL(addBuf1.toString());
	        if (dbset2 != null && dbset2.size() > 0) {
	        	int length2 = dbset2.size();
	            for (int i=0; i<length2; i++) {
	               Map<String, Object> retmap2 = dbset2.get(i);
	               strIDENTIFICATION = retmap2.get("FLOWID").toString();
	               intNum = Integer.parseInt(retmap2.get("num").toString());
	               if (strIDENTIFICATIONS.indexOf(strIDENTIFICATION)==-1) {
	                 if (intNum==0) {
	                     strTmp =(String)AlreadyNumHashtable1.get(strIDENTIFICATION);
	                     if (strTmp==null) {
	                    	 intNum =0;
	                     } else {
	                    	 intNum = Integer.parseInt(strTmp);
	                     }
	                     AlreadyNumHashtable2.put(strIDENTIFICATION,String.valueOf(intNum));
	                 } else {
	                     AlreadyNumHashtable2.put(strIDENTIFICATION,String.valueOf(intNum));
	                 }
	                 tmpBuf.delete(0,tmpBuf.length());//清空
	                 tmpBuf.append(strIDENTIFICATIONS+strIDENTIFICATION+",");
	                 strIDENTIFICATIONS=tmpBuf.toString();
	               }
	            }
	            dbset2 = null;//赋空值
	        }
	        //-----------
	      } else {
	            return AlreadyNumHashtable1;
	      }
	      return AlreadyNumHashtable2;
    }

    /**
     * 功能：流程标识/流程下已经走过的流程Html对照表3。
     * @param strWhere 流程标识条件或流程标识串
     * @param  ActFlowRun 流程流转相关参数
     * @return 对照表
     * @throws Exception 
     */
    private Map<String, Object> getFlowHtmlHashtable3(String strWhere, ActFlowRun ActFlowRun) throws Exception {
    	Map<String, Object> FlowHtmlHashtable = new HashMap<String, Object>();
    	String strFlowName = ""; //流转名称
    	String strFlowPATH = ""; //流转地址
    	String strSTATE="";
    	String strRunIco="";
    	String strFLOWID="";
    	String strUPFLOWID="";
    	StringBuffer reBuf = new StringBuffer();
    	String strRID = ActFlowRun.getRID();
    	String strOtherID = ActFlowRun.getOtherID();
    	String strPARENTID1 = ActFlowRun.getParentID1();
    	
    	if (strWhere.indexOf(",")>-1) {
	        if (strWhere.indexOf("'")==-1) {
	            strWhere = strWhere.replaceAll(",", "','");
	            reBuf.append("'"+strWhere+"'");
	            strWhere = reBuf.toString();
	        }
    	} else {
	        if (strWhere.indexOf("'")==-1) {
	            reBuf.append("'"+strWhere+"'");
	            strWhere = reBuf.toString();
	        }
    	}
    	reBuf.delete(0,reBuf.length());//清空
    	reBuf.append("Select NAME,STATE,FLOWPATH,FLOWID from FLOW_RUNTIME_PROCESS where (FLOWID in("+strWhere)
             .append(") and (OTHERID ='"+strOtherID+"' or PARENTID ='"+strOtherID)
             .append( "' or PARENTID1 ='"+strOtherID+"') and PARENTID1 ='"+strPARENTID1)
             .append("') or RID='"+strRID+"' order by FLOWID,CREATEDATE");
    	
    	List<Map<String, Object>> dbset = userMapper.selectListMapExecSQL(reBuf.toString());
    	reBuf.delete(0,reBuf.length());//清空
    	if (dbset != null && dbset.size() > 0) {
    	  int length = dbset.size();
          for (int k = 0; k < length; k++) {
        	  Map<String, Object> retmap = dbset.get(k);
              strFlowName = retmap.get("NAME").toString();
              strSTATE = retmap.get("STATE").toString();
              strFlowPATH = retmap.get("FLOWPATH").toString();
              strFLOWID = retmap.get("FLOWID").toString();
              
              if (strSTATE.equals("4")) {
                  strRunIco="flowrun5.gif";
              } else {
            	  strRunIco="flowrun4.gif";
              }
              if (strFlowName.indexOf("→")>-1) {
            	  strFlowName = strFlowName.substring(strFlowName.indexOf("→"));
              }
              if (!strUPFLOWID.equals(strFLOWID)) {//新的一个流程
                  if (strUPFLOWID.length()>0) {
                      FlowHtmlHashtable.put(strUPFLOWID,reBuf.toString());
                      reBuf.delete(0,reBuf.length());//清空
                  }
              }
              //得到当前审批表下已经建立或运转的流程
              reBuf.append("<table border='0' width='100%' height='20' cellspacing='0' cellpadding='0'><tr><td height=20>&nbsp;&nbsp;<img border='0' align='absmiddle' src='")
                   .append(SysPreperty.getProperty().AppUrl+"Zrsysmanage/images/blueimg/"+strRunIco)
                   .append("'>"+"<a href=\"javascript:OpenLinFlowWindow('"+strFlowPATH+"');\" title='")
                   .append(strFlowName+"'>"+strFlowName+"</a></td></tr></table>");
              strUPFLOWID = strFLOWID;
              //-------------------------------
          }
          FlowHtmlHashtable.put(strFLOWID,reBuf.toString());
          dbset = null;//赋空值
    	}
    	return FlowHtmlHashtable;
    }

    /**
     * 功能或作用：分析规则字符串，生成数组
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
     * 功能：根据流程标识得到当前流程是否是必须新建的流程对照表
     * @param strIDs 流程标识串
     * @param AID 活动ID
     * @return 对照表
     * @throws Exception 
     */
    private Map<String, Object> getNeedNewFlowHashtable(String strIDs,String AID) throws Exception {
    	Map<String, Object> NeedNewFlowHashtable = new HashMap<String, Object>();
       String strIDENTIFICATION="";
       String strISNEED="";
       StringBuffer addBuf = new StringBuffer();
       if (strIDs.indexOf(",")>-1) {
          if (strIDs.indexOf("'")==-1) {
              strIDs = strIDs.replaceAll(",", "','");
              addBuf.append("'"+strIDs+"'");
              strIDs = addBuf.toString();
          }
       } else {
          if (strIDs.indexOf("'")==-1) {
              addBuf.append("'"+strIDs+"'");
              strIDs = addBuf.toString();
          }
       }
       addBuf.delete(0,addBuf.length());//清空
       addBuf.append("Select a.ISNEED,b.DESC1,c.IDENTIFICATION from FLOW_CONFIG_ACTIVITY_CONNE a left join FLOW_CONFIG_ACTIVITY b on a.CID=b.ID left join FLOW_CONFIG_PROCESS c on b.DESC1=c.ID "
       			   + "where (a.SID='"+AID+"' or a.EID='"+AID+"') and a.CID in (select ID from FLOW_CONFIG_ACTIVITY where DESC1 in (select ID from FLOW_CONFIG_PROCESS "
       			   + "where IDENTIFICATION in ("+strIDs+")))");
       List<Map<String, Object>> dbset = userMapper.selectListMapExecSQL(addBuf.toString());
       if (dbset != null && dbset.size() > 0) {
    	   int length = dbset.size();
           for (int i=0; i<length; i++) {
        	   Map<String, Object> retmap = dbset.get(i);
               strIDENTIFICATION = retmap.get("IDENTIFICATION").toString();
               strISNEED = retmap.get("ISNEED").toString();
               NeedNewFlowHashtable.put(strIDENTIFICATION, strISNEED);
           }
           dbset = null;//赋空值
       }
       return NeedNewFlowHashtable; 
    }

    /**
     * 功能：检测待处理人中是否有权限委托的，有权限委托的替换成受委托人
     * @return returnValue 返回处理后的待处理人列表
     * @throws Exception 
     */
    private String flowEntrustJudgement(ActFlowRun ActFlowRun) throws Exception {
      String returnValue = ""; //返回人员字符串
      String tmpUserID = ""; //临时用户ID
      String strSUSERNO=""; //接收人用户ID
      String strSUSERNOs=""; //接收人用户ID串
      String strIUSERNO = ""; //受委托用户ID
      
      Calendar cal = Calendar.getInstance();
      SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
      String mDateTime = formatter.format(cal.getTime());
      
      String Do_User_Nos = ActFlowRun.getDo_User_Nos();
      List<Object> UserList = getArrayList(Do_User_Nos, ",");
      //接收人编号/受委托人编号对照表
      Map<String, Object> JudgementHashtable = new HashMap<String, Object>();
      StringBuffer  addBuf = new   StringBuffer();
      if (DataBaseType.equals("1")) {// Oracle数据库
    	  addBuf.append("select SUSERNO,IUSERNO from FLOW_CONFIG_ENTRUST where SUSERNO in ("+Do_User_Nos)
    			.append(") and SDATE<=to_date('"+mDateTime+"','yyyy-mm-dd') and EDATE>=to_date('"+mDateTime+"','yyyy-mm-dd')");
      }
      else if (DataBaseType.equals("2")) {//MSSQL数据库
    	  addBuf.append("select SUSERNO,IUSERNO from FLOW_CONFIG_ENTRUST where SUSERNO in ("+Do_User_Nos)
          		.append(") and SDATE<='"+mDateTime+"' and EDATE>='"+mDateTime+"'");
      }
      else if (DataBaseType.equals("3")) {// MySQL数据库
    	  addBuf.append("select SUSERNO,IUSERNO from FLOW_CONFIG_ENTRUST where SUSERNO in ("+Do_User_Nos)
    			.append(") and SDATE<='"+mDateTime+"' and EDATE>='"+mDateTime+"'");
      }
      List<Map<String, Object>> dbset = userMapper.selectListMapExecSQL(addBuf.toString());
      addBuf.delete(0, addBuf.length());//清空
      if (dbset != null && dbset.size() > 0) {
    	  int length = dbset.size();
          for (int i=0; i<length; i++) {
        	  Map<String, Object> retmap = dbset.get(i);
              strSUSERNO = retmap.get("SUSERNO").toString();
              strIUSERNO = retmap.get("IUSERNO").toString();
              if (strSUSERNOs.indexOf(strSUSERNO)==-1) {
                  JudgementHashtable.put(strSUSERNO,strIUSERNO);
                  addBuf.append(strIUSERNO+",");
              }
          }
          dbset = null;//赋空值
      }
      strSUSERNOs = addBuf.toString();
      //---------------------------------------------
      addBuf.delete(0,addBuf.length());//清空
      for (int i = 0; i < UserList.size(); i++) {
          tmpUserID = UserList.get(i).toString();
          //查询当前用户是否有复合条件的权限委托信息
          strIUSERNO = (String)JudgementHashtable.get(tmpUserID);
          if (strIUSERNO==null) {
        	  strIUSERNO="";
    	  }
          if (strIUSERNO.length() > 0) { //找到受委托人
            addBuf.append(strIUSERNO+",");
          } else {
            addBuf.append(tmpUserID+",");
          }
      }
      returnValue = addBuf.toString();
      if (returnValue.length() > 0) {
          returnValue = returnValue.substring(0, returnValue.length() - 1);
      }
      return returnValue;
    }

    private String flowEntrustJudgement(String users) throws Exception {
        String returnValue = ""; //返回人员字符串
        String tmpUserID = ""; //临时用户ID
        String strSUSERNO=""; //接收人用户ID
        String strSUSERNOs=""; //接收人用户ID串
        String strIUSERNO = ""; //受委托用户ID
        
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String mDateTime = formatter.format(cal.getTime());
        
        String Do_User_Nos = users;
        List<Object> UserList = getArrayList(Do_User_Nos, ",");
        // 接收人编号/受委托人编号对照表
        Map<String, Object> JudgementHashtable = new HashMap<String, Object>();
        StringBuffer addBuf = new StringBuffer();
        if (DataBaseType.equals("2"))//mssql数据库
        {
            addBuf.append("select SUSERNO,IUSERNO from FLOW_CONFIG_ENTRUST where SUSERNO in ("+Do_User_Nos)
            	  .append(") and SDATE<='"+mDateTime+"' and EDATE>='"+mDateTime+"'");
        } else {
            addBuf.append("select SUSERNO,IUSERNO from FLOW_CONFIG_ENTRUST where SUSERNO in ("+Do_User_Nos)
            	  .append(") and SDATE<=to_date('"+mDateTime+"','yyyy-mm-dd') and EDATE>=to_date('"+mDateTime+"','yyyy-mm-dd')");
        }
        List<Map<String, Object>> dbset = userMapper.selectListMapExecSQL(addBuf.toString());
        addBuf.delete(0,addBuf.length());//清空
        if (dbset != null && dbset.size() > 0) {
        	int length = dbset.size();
            for (int i=0; i<length; i++) {
            	Map<String, Object> retmap = dbset.get(i);
                strSUSERNO = retmap.get("SUSERNO").toString();
                strIUSERNO = retmap.get("IUSERNO").toString();
                if (strSUSERNOs.indexOf(strSUSERNO)==-1) {
                    JudgementHashtable.put(strSUSERNO,strIUSERNO);
                    addBuf.append(strIUSERNO+",");
                }
             }
             dbset = null;//赋空值
        }
        strSUSERNOs = addBuf.toString();
        //---------------------------------------------
        addBuf.delete(0,addBuf.length());//清空
        for (int i = 0; i < UserList.size(); i++) {
            tmpUserID = UserList.get(i).toString();
            //查询当前用户是否有复合条件的权限委托信息
            strIUSERNO = (String)JudgementHashtable.get(tmpUserID);
            if (strIUSERNO==null){strIUSERNO="";}

            if (strIUSERNO.length() > 0) { //找到受委托人
              addBuf.append(strIUSERNO+",");
            } else {
              addBuf.append(tmpUserID+",");
            }
        }
        returnValue = addBuf.toString();
        if (returnValue.length() > 0) {
            returnValue = returnValue.substring(0, returnValue.length() - 1);
        }
        return returnValue;
    }

    /**
     * 功能：更新流程流转记录
     * @param OptCmd_Name 记录属性按钮命令值中文
     * @param Workflow_No 流程编号
     * @param Node_No_S 当前步骤编号
     * @param M_Node_No_S_E 下一分支步骤编号
     * @param UserNo 当前用户编号
     * @param Execute_No 流程流转编号
     * @param Do_User_Nos 选择的处理用户编号
     * @param FormID 业务表的ID值
     * @param TitleName 标题名称
     * @param OtherID 关联字段的ID值
     * @param DoIdea 处理意见
     * @return returnValue 返回是否执行成功
     */
    @SuppressWarnings("unused")
	private Vector<Object> updateFlowRunData(ActFlowRun ActFlowRun, SessionUser user, Vector<Object> mVec) {
	      try{
		      String strRunID = "";
		      String strConnName = "";
		      int UserNum = 1;
		      int MobileAdd=0;
		      String strIsSendMsg = "";
		      String strMaxID = "";
		      String strMobile = "";
		      String strMsgTitle = "";
		      String strMsgTitle1 = "";
		      //String strCONTENT = "";
		      String strCONTENTs = "";
		      
		      //得到流程运转参数-------
		      String Workflow_No=ActFlowRun.getWorkflow_No();
		      String Node_No_S=ActFlowRun.getNode_No_S();
		      String M_Node_No_S_E=ActFlowRun.getM_Node_No_S_E();
		      
		      String Execute_No=ActFlowRun.getExecute_No();
		      String Do_User_Nos=ActFlowRun.getDo_User_Nos();
		      String TitleName=ActFlowRun.getTitleName();
		      String DoIdea=ActFlowRun.getDoIdea();
		      String strFLOWID=ActFlowRun.getIdentification();
		      String FormID=ActFlowRun.getFormID();
		      String OtherID=ActFlowRun.getOtherID();
		      String strParentID=ActFlowRun.getParentID();
		      String strParentID1=ActFlowRun.getParentID1();
		      String strRID=ActFlowRun.getRID();
		      String MsgType=ActFlowRun.getMsgType();
		      //得到流程运转参数---------
		      StringBuffer addBuf = new StringBuffer();
		      
		      String strDoUser = "";
		      List<Object> UserList = getArrayList(Do_User_Nos, ",");
		      //接收人数量-------------
		      UserNum = UserList.size();
		      FLOW_CONFIG_ACTIVITY activity = null;
		      activity = getFlowActivity(M_Node_No_S_E);
		      FLOW_CONFIG_PROCESS Process = null;
		      Process = getFlowProcess1(Workflow_No);
		      
		      FLOW_RUNTIME_PROCESS Fprocess = null;
		      Fprocess = getFlowRuntimeprocess(Execute_No);
		      String strFUID = Fprocess.getFUID();
		      if (strFUID==null) {
		    	  strFUID="";
		      }
		      if (strFUID.indexOf(user.getUserID())==-1) {
		         strFUID = strFUID + ","+user.getUserID();
		      }
		      if (strFUID.length()>2000) {
		         strFUID = strFUID.substring(0,2000);
		      }
		      if (Process!=null && activity!=null) {
		        if (TitleName.length() == 0) {
		          addBuf.append(Process.getNAME()).append("→").append(activity.getNAME());
		          TitleName = addBuf.toString();
		          strMsgTitle = Process.getNAME();
		        } else {
		          addBuf.append(TitleName).append("→").append(Process.getNAME()).append("→").append(activity.getNAME());
		          TitleName = addBuf.toString();
		          addBuf.delete(0,addBuf.length());//清空
		          addBuf.append(TitleName).append(":").append(Process.getNAME());
		          strMsgTitle = addBuf.toString();
		        }
		      }
		      //--------------------更新流转记录-----------------------------
		      addBuf.delete(0,addBuf.length());//清空
		      addBuf.append("update FLOW_RUNTIME_PROCESS set NAME='").append(TitleName).append("',ACCEPTPSN='")
		            .append(Do_User_Nos).append("',ACCEPTDATE=sysdate,CURRACTIVITY='").append(M_Node_No_S_E)
		            .append("',STATE='1',ACCEPTPSNNUM=").append(UserNum).append(",ISABNORMITY='0',FUID='"+strFUID+"' where ID='")
		            .append(Execute_No).append("'");
		      
		      mVec.add(addBuf.toString());
		      
		      Fprocess.setNAME(TitleName);
		      Fprocess.setACCEPTPSN(Do_User_Nos);
		      Fprocess.setSTATE("1");
		      Fprocess.setCURRACTIVITY(M_Node_No_S_E);
		      Fprocess.setACCEPTPSNNUM(UserNum);
		      Fprocess.setISABNORMITY("0");
		      Fprocess.setFUID(strFUID);
		      FLOW_RUNTIME_PROCESSHashtable.put(Execute_No,Fprocess);
		      //---------------更新上一条流程流转过程记录----------------
		      addBuf.delete(0,addBuf.length());//清空
		      if (DoIdea.trim().length()>0) {
		          addBuf.append("update FLOW_RUNTIME_ACTIVITY set DODATE=sysdate,DOIDEA='").append(DoIdea)
		                .append("',DOFLAG='1' where FID='")
		                .append(Execute_No).append("' and EACTIVITY='").append(Node_No_S).append("' and DOPSN='")
		                .append(user.getUserID()).append("' and DOFLAG='0'");
		
		      } else {
		          addBuf.append("update FLOW_RUNTIME_ACTIVITY set DODATE=sysdate,DOFLAG='1' where FID='")
		          		.append(Execute_No).append("' and EACTIVITY='").append(Node_No_S)
		                .append("' and DOPSN='").append(user.getUserID()).append("' and DOFLAG='0'");
		      }
		      mVec.add(addBuf.toString());
		      
		      //------------------插入流转过程记录[每个处理人一条记录]------------------//
		      if (MsgType.equals("2")){strIsSendMsg ="1";}
		      if(MsgType.equals("1")){
		          activity = getFlowActivity(M_Node_No_S_E);
		           if (activity!=null) {
		             strIsSendMsg = activity.getISNOTE();//为1时发送短信提示
		           }
		      }
		      if(activity.getISNOTE().equals("4") && MsgType.length()>0) {
		         strIsSendMsg ="1";
		      }
		      LOGGER.info("发送短信步骤:",M_Node_No_S_E);
		      LOGGER.info("是否发送短信:",strIsSendMsg);
		      LOGGER.info("MsgType1:",MsgType);
		      
		      for (int i = 0; i < UserList.size(); i++) {
		    	  strDoUser = UserList.get(i).toString();
		    	  //计算流程流转过程表的ID
		    	  strRunID = getFLOW_RUNTIME_ACTIVITY_ID();
		    	  //根据开始活动和结束活动得到关系的名称
		    	  strConnName = getActivityConnName(Node_No_S, M_Node_No_S_E);
		    	  
		    	  //由于开始活动和结束活动得到关系的名称未设置，故暂时将开始活动的名称设为流转名称，这样在审批记录中可查看
		    	  if (Process!=null && activity!=null) {
			         addBuf.delete(0,addBuf.length());//清空
			         addBuf.append(Process.getNAME()).append("→").append(activity.getNAME());
			         strConnName=addBuf.toString();
		    	  }
		    	  //---------------------------------2007-8-11---------------------------------
		    	  addBuf.delete(0,addBuf.length());//清空
		    	  addBuf.append("Insert into FLOW_RUNTIME_ACTIVITY(ID,FID,SACTIVITY,EACTIVITY,NAME,DOPSN,DOFLAG,DOIP,SENDPSN,SENDDATE) values ('")
		           		.append(strRunID).append("','").append(Execute_No).append("','").append(Node_No_S).append("','").append(M_Node_No_S_E)
		           		.append("','").append(strConnName).append("','").append(strDoUser).append("','0','','").append(user.getUserID()).append("',sysdate)");
		    	  
		    	  mVec.add(addBuf.toString());
		    	  //处理短信提示-----------------------------
		    	  if (strIsSendMsg==null){strIsSendMsg="";}
		    	  if (strIsSendMsg.equals("1")) {//需发送短信
			          addBuf.delete(0,addBuf.length());//清空
			          //addBuf.append("已收到").append(GetUserTrueName(user.getUserID())).append("传来的[").append(strMsgTitle).append("]");
			          strMsgTitle1 ="已收到"+getUserTrueName(user.getUserID())+"传来的["+strMsgTitle+"]";
			          strMobile = getUserMobile(strDoUser);//得到处理人的手机号
			          
			          List<Object> MobileList = getArrayList(strMobile,",");
			          for(int j=0;j<MobileList.size();j++) {
				          strMobile = MobileList.get(j).toString();
				          if (strMobile.length()>0) {
				                 if(MsgType.length()>1) {
				                   strCONTENTs=MsgType;
				                 } else {
				                     //处理短信长度大于60个字的情况
				                     strCONTENTs = strMsgTitle1;
				                 }
			                     if (strCONTENTs.length()>180) {
			                       strCONTENTs = strCONTENTs.substring(0,180)+"...";
			                     }
				                 strMaxID = getBPIP_HANDSET_ID();
				                 addBuf.delete(0,addBuf.length());//清空
				                 addBuf.append("Insert into BPIP_HANDSET(ID,USERNO,MOBILE,CONTENT,SENDDATE,FINISHDATE,TYPEBOX,SENDTYPE,MONTH,WEEK,DAY,HOUR,MINUTE,ISSEND,TTABLEID,SUSERNO) values ('")
				                       .append(strMaxID).append("','").append(user.getUserID()).append("','").append(strMobile).append("','").append(strCONTENTs)
				                       .append("',sysdate,sysdate,5,0,0,0,0,0,0,'0','").append(strRunID).append("','").append(strDoUser).append("')");
				                 mVec.add(addBuf.toString());
				                 MobileAdd++;
				            }
			          }
		    	  }
		    	  //--------------------------处理短信提示结束--------------------------
		      }
		      //----------------------处理消息提示----------------------
		      String strIsSendMessage = "";//为1时发送消息提示
		      if (activity!=null) {
		        strIsSendMessage = activity.getISMESSAGE();
		      }
		      String strSendMessage = "";
		      if (strIsSendMessage==null){strIsSendMessage="";}
		      if (strIsSendMessage.equals("1")) {//需发送消息
		         addBuf.delete(0,addBuf.length());//清空
		         addBuf.append("<a href=# onclick=OpenUrl(''/flow/openflowrun?FlowNo=").append(strFLOWID).append("&ExecuteNo=")
		               .append(Execute_No).append("&FormID=").append(FormID).append("&OtherID=").append(OtherID).append("&ParentID=").append(strParentID)
		               .append("&ParentID1=").append(strParentID1).append("&RID=").append(strRID).append("'',''1'') title=''>")
		               .append(TitleName).append("</a>");
		         strSendMessage = addBuf.toString();
		         mVec = getSendFlowMessage(Do_User_Nos,strSendMessage,mVec);
		      }
		      //-------------------处理消息提示结束-------------------
		      //---------------------------------------------------
		      return mVec;
	     } catch (Exception ex) {
	         LOGGER.error("流程引擎---updateFlowRunData函数出错：", ex);
	         return null;
	     }
    }

    /**
     * 功能：根据人员编号得到手机号。
     * @param strUserNo 人员编号
     * @return returnValue 返回手机号
     * @throws Exception  
     */
    private String getUserMobile(String strUserNo) throws Exception {
        StringBuffer addBuf = new StringBuffer();
        addBuf.append("Select MOBILE From BPIP_USER where USERID='"+strUserNo+"'");
        List<Map<String, Object>> dbset0 = userMapper.selectListMapExecSQL(addBuf.toString());
        addBuf.delete(0,addBuf.length());//清空
        if (dbset0 != null && dbset0.size() > 0){
        	if (dbset0.get(0) != null && dbset0.get(0).get("MOBILE") != null) {
        		addBuf.append(dbset0.get(0).get("MOBILE").toString());
        	}
            dbset0 = null;//赋空值
        }
        return addBuf.toString();
    }

    /**
     * 功能：根据用户编号得到用户的真实名称
     * @param strUser_No用户编号
     * @return returnValue 返回用户名称
     * @throws Exception 
     */
    private String getUserTrueName(String strUser_No) throws Exception {
    	StringBuffer addBuf = new StringBuffer();
    	addBuf.append("Select NAME from BPIP_USER where USERID = '"+strUser_No+"'");
    	List<Map<String, Object>> dbset = userMapper.selectListMapExecSQL(addBuf.toString());
    	addBuf.delete(0,addBuf.length());//清空
    	if (dbset != null && dbset.size() > 0) {
    		if (dbset.get(0) != null && dbset.get(0).get("NAME") != null) {
    			addBuf.append(dbset.get(0).get("NAME").toString());
    		}else
    		{
    			addBuf.append("");
    		}
    		dbset = null;//赋空值
    	}
    	return addBuf.toString();
    }

    /**
     * 功能：根据开始活动和结束活动得到关系的名称
     * @param strSID 开始活动ID
     * @param strEID 结束活动ID
     * @return returnValue 返回关系的名称
     * @throws Exception 
     */
    private String getActivityConnName(String strSID, String strEID) throws Exception {
    	StringBuffer addBuf = new StringBuffer();
    	addBuf.append("Select NAME from FLOW_CONFIG_ACTIVITY_CONNE where (SID='"+strSID+"' or EID='")
          	  .append(strSID+"') and CID='"+strEID+"'");
    	List<Map<String, Object>> dbset = userMapper.selectListMapExecSQL(addBuf.toString());
    	addBuf.delete(0, addBuf.length());//清空
    	if (dbset != null && dbset.size() > 0) {
    		if (dbset.get(0) != null && dbset.get(0).get("NAME") != null) {
    			addBuf.append(dbset.get(0).get("NAME").toString());
    		}else
    		{
    			addBuf.append("");
    		}
    		dbset = null;//赋空值
    	}
    	return addBuf.toString();
    }

    /**
     * 功能：增加发送提示消息的SQL
     * @param strUserNos 待处理人编号字符串
     * @param strMessage 提示的消息内容
     * @return returnValue 返回处理后的结果
     */
    private Vector<Object> getSendFlowMessage(String strUserNos, String strMessage, Vector<Object> mVec) {
    	String tmpUserID = ""; //临时用户编号
    	StringBuffer addBuf = new StringBuffer();
    	List<Object> UserList = getArrayList(strUserNos, ",");
    	for (int i = 0; i < UserList.size(); i++) {
    		tmpUserID = UserList.get(i).toString();
    		addBuf.delete(0,addBuf.length());//清空
    		//插入消息
    		addBuf.append("Insert into BPIP_MSGCONTENT(CUSERNO,CONTENT,SENDDATE,ISCK) values ('")
              	  .append(tmpUserID+"','"+strMessage+"',sysdate,'0')");
        
    		mVec.add(addBuf.toString());
    	}
    	return mVec;
    }

    private FLOW_CONFIG_PROCESS getFlowProcess1(String strID) throws Exception {
	    FLOW_CONFIG_PROCESS Process = null;
	    StringBuffer addBuf = new StringBuffer();
	    addBuf.append("Select * From FLOW_CONFIG_PROCESS where ID='"+strID+"'");
	    List<Map<String, Object>> dbset = userMapper.selectListMapExecSQL(addBuf.toString());
	    if (dbset != null && dbset.size() > 0) {
	         Process = (FLOW_CONFIG_PROCESS) ReflectionUtil.convertMapToBean(dbset.get(0), FLOW_CONFIG_PROCESS.class);
	         dbset = null;//赋空值
	    }
	    return Process;
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
         for (int k=0;k<znum;k++) {
             strValue = strValue + "0";
         }
         if (strValue.length()>20) {
            strValue = strValue.substring(0,20);
         }
         return strValue;
    }

    /**
     * 得到流程运转步骤表的ID
     * @return
     */
    private String getBPIP_HANDSET_ID() {
        String strValue ="";
        String strNum ="";
        try {
              strNum = myGetRandom(4);
        } catch (Exception ex) {
              strNum = "0";
        }
        strValue = "H"+globalID1()+strNum;
        int znum = 20-strValue.length();
        for (int k=0;k<znum;k++) {
        	strValue = strValue + "0";
        }
        if (strValue.length()>20) {
        	strValue = strValue.substring(0,20);
        }
        return strValue;
    }

    private String myGetRandom(int i) {
        Random s = new Random();
        if (i == 0) { return ""; }
        String revalue = "";
        for (int k = 0; k < i; k++) {
             revalue = revalue + s.nextInt(9);
        }
        return revalue;
    }

    /**
	 * 加入同步锁，多用户使用时保证FLOW_RUNTIME_PROCESS_ID唯一性
	 * @return
	 */
    private synchronized String globalID1() {
    	String strResult="";
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
     * 得到流程运转表的ID
     * @return
     */
    private String getFLOW_RUNTIME_PROCESS_ID() {
          String strValue ="";
          String strNum ="";
          try {
              strNum = myGetRandom(4);
          } catch (Exception ex) {
              strNum = "0";
          }
          strValue = "Z"+globalID1()+strNum;
          int znum = 20-strValue.length();
          for (int k=0;k<znum;k++) {
              strValue = strValue + "0";
          }
          if (strValue.length()>20) {
             strValue = strValue.substring(0,20);
          }
          return strValue;
    }

    /**
     * 功能：新增流程流转记录
     * @return returnValue 返回是否执行成功
     */
    @SuppressWarnings("unused")
	private Vector<Object> InsertFlowRunData(ActFlowRun ActFlowRun, Vector<Object> mVec, String strID) {
	     try {
		     String strRunID = "";
		     String strFLOWID = "";
		     String strFORMTABLE = "";
		     String strConnName = "";
		     String strIsSendMsg = "";
		     String strMaxID = "";
		     String strMobile = "";
		     String strMsgTitle = "";
		     String strMsgTitle1 = "";
		     //String strCONTENT = "";
		     String strCONTENTs = "";
		     //----------------得到流程运转参数----------------
		     String Workflow_No=ActFlowRun.getWorkflow_No();
		     String Node_No_S=ActFlowRun.getNode_No_S();
		     String M_Node_No_S_E=ActFlowRun.getM_Node_No_S_E();
		     String UserNo=ActFlowRun.getUserNo();
		     String Do_User_Nos=ActFlowRun.getDo_User_Nos();
		     String FilePath=ActFlowRun.getPath();
		     String TitleName=ActFlowRun.getTitleName();
		     String FormID=ActFlowRun.getFormID();
		     String OtherID=ActFlowRun.getOtherID();
		     String strParentID=ActFlowRun.getParentID();
		     String strParentID1=ActFlowRun.getParentID1();
		     String strRID=ActFlowRun.getRID();
		     String DoIdea=ActFlowRun.getDoIdea();
		     String MsgType=ActFlowRun.getMsgType();
		     String RECORDID=ActFlowRun.getRECORDID();
		     
		     //得到流程运转参数---------------
		     StringBuffer addBuf = new StringBuffer();
		     int UserNum = 1;
		     int MobileAdd=0;
		     //已增加的开始和结束字符串
		     String strSES="";
		     FLOW_CONFIG_PROCESS Process = getFlowProcess1(Workflow_No);
		     
		     //根据流程编号得到流程标识
		     if (Process!=null) {
		    	 strFLOWID = Process.getIDENTIFICATION();
		     }
		     //页面路径
		     addBuf.append(FilePath).append("?FlowNo=").append(strFLOWID).append("&ExecuteNo=").append(strID).append("&FormID=")
		           .append(FormID).append("&OtherID=").append(OtherID).append("&ParentID=").append(strParentID)
		           .append("&ParentID1=").append(strParentID1).append("&RID=").append(strRID);
		     
		     FilePath = addBuf.toString();
		     //根据流程编号得到流程的主表名称
		     strFORMTABLE = getFlowFormTable(Workflow_No);
		     //处理人为空，保存流程时
		     if (Do_User_Nos.length() == 0) {
		       M_Node_No_S_E = Node_No_S; //下一步活动设置成当前活动
		       Do_User_Nos = UserNo; //接收人设置成本人
		       //-------------接收人数量-------------
		       List<Object> List = getArrayList(Do_User_Nos, ",");
		       UserNum = List.size();
		       //----------------------
		     }
		     //--------------------插入流转记录-----------------------------
		     FLOW_CONFIG_ACTIVITY activity = getFlowActivity(M_Node_No_S_E);
		     addBuf.delete(0,addBuf.length());//清空
		     if (Process!=null && activity!=null) {
		       if (TitleName.length() == 0) {
		         addBuf.append(Process.getNAME()).append("→").append(activity.getNAME());
		         TitleName = addBuf.toString();
		         strMsgTitle = Process.getNAME();
		       } else {
		         addBuf.append(TitleName).append("→").append(Process.getNAME()).append("→").append(activity.getNAME());
		         TitleName = addBuf.toString();
		         addBuf.delete(0,addBuf.length());//清空
		         addBuf.append(TitleName).append(":").append(Process.getNAME());
		         strMsgTitle = addBuf.toString();
		       }
		     }
		     addBuf.delete(0, addBuf.length());//清空
		     addBuf.append("Insert into FLOW_RUNTIME_PROCESS(ID,FLOWID,FLOWPATH,FORMTABLE,FORMID,OTHERID,NAME,CREATEPSN,CREATEDATE,ACCEPTPSN,ACCEPTDATE,CURRACTIVITY,READCOUNT,STATE,ACCEPTPSNNUM,ISABNORMITY,ABNORMITYDATE,PARENTID,PARENTID1,RID,RECORDID) values ('")
		           .append(strID).append("','").append(strFLOWID).append("','").append(FilePath).append("','").append(strFORMTABLE)
		           .append("','").append(FormID).append("','").append(OtherID).append("','").append(TitleName).append("','")
		           .append(UserNo).append("',sysdate,'").append(Do_User_Nos).append("',sysdate,'").append(M_Node_No_S_E)
		           .append("',0,'1',").append(UserNum).append(",'0',sysdate,'").append(strParentID)
		           .append("','").append(strParentID1).append("','").append(strRID).append("','").append(RECORDID).append("')");
		     
		     mVec.add(addBuf.toString());
		     if (FLOW_RUNTIME_PROCESSHashtable.containsKey(strID)) {
		        FLOW_RUNTIME_PROCESSHashtable.remove(strID);
		     }
		     //------------------插入流转过程记录(每个处理人一条记录)------------------
		     String strDoUser = "";
		     List<Object> UserList = getArrayList(Do_User_Nos, ",");
		     if (MsgType.equals("2")) {
		    	 strIsSendMsg ="1";
		     }
		     if(MsgType.equals("1")) {
		         activity = getFlowActivity(M_Node_No_S_E);
		         if (activity!=null) {
		            strIsSendMsg = activity.getISNOTE();//为1时发送短信提示
		         }
		     }
		     if(activity.getISNOTE().equals("4") && MsgType.length()>0) {
		        strIsSendMsg ="1";
		     }
		     LOGGER.info("发送短信步骤:",M_Node_No_S_E);
		     LOGGER.info("是否发送短信:",strIsSendMsg);
		     LOGGER.info("MsgType:",MsgType);
		     
		     for (int i = 0; i < UserList.size(); i++) {
		       strDoUser = UserList.get(i).toString();
		       //计算流程流转过程表的ID
		       strRunID = getFLOW_RUNTIME_ACTIVITY_ID();
		       //根据开始活动和结束活动得到关系的名称
		       strConnName = getActivityConnName(Node_No_S, M_Node_No_S_E);
		       
		       //由于开始活动和结束活动得到关系的名称未设置，故暂时将开始活动的名称设为流转名称，这样在审批记录中可查看
		       activity = getFlowActivity(M_Node_No_S_E);
		       if (Process!=null && activity!=null) {
		         addBuf.delete(0,addBuf.length());//清空
		         addBuf.append(Process.getNAME()).append("→").append(activity.getNAME());
		         strConnName= addBuf.toString();
		       }
		       addBuf.delete(0,addBuf.length());//清空
		       addBuf.append(strSES).append("/").append(Node_No_S).append(",").append(M_Node_No_S_E);
		       strSES = addBuf.toString();
		       
		       addBuf.delete(0,addBuf.length());//清空
		       if (Node_No_S.equals(M_Node_No_S_E)) {
		           addBuf.append("Insert into FLOW_RUNTIME_ACTIVITY(ID,FID,SACTIVITY,EACTIVITY,NAME,DOPSN,DOIDEA,DOFLAG,DOIP,SENDPSN,SENDDATE) values ('")
		                 .append(strRunID).append("','").append(strID).append("','").append(Node_No_S).append("','").append(M_Node_No_S_E)
		                 .append("','").append(strConnName).append("','").append(strDoUser).append("','").append(DoIdea).append("','0','','")
		                 .append(UserNo).append("',sysdate)");
		       } else {
		           addBuf.append("Insert into FLOW_RUNTIME_ACTIVITY(ID,FID,SACTIVITY,EACTIVITY,NAME,DOPSN,DOFLAG,DOIP,SENDPSN,SENDDATE) values ('")
		                 .append(strRunID).append("','").append(strID).append("','").append(Node_No_S).append("','").append(M_Node_No_S_E)
		                 .append("','").append(strConnName).append("','").append(strDoUser).append("','0','','").append(UserNo).append("',sysdate)");
		       }
		       mVec.add(addBuf.toString());
		       //-----------------------------处理短信提示-----------------------------
		       if (strIsSendMsg.equals("1"))//需发送短信
		       {
		         addBuf.delete(0,addBuf.length());//清空
		         //addBuf.append("已收到").append(GetUserTrueName(UserNo)).append("传来的[").append(strMsgTitle).append("]");
		         strMsgTitle1 = "已收到"+getUserTrueName(UserNo)+"传来的["+strMsgTitle+"]";
		         
		         strMobile = getUserMobile(strDoUser);//得到处理人的手机号
		         List<Object> MobileList = getArrayList(strMobile,",");
		         for(int j=0;j<MobileList.size();j++) {
		           strMobile = MobileList.get(j).toString();
		           if (strMobile.length()>0) {
		               if(MsgType.length()>1) {
		                    strCONTENTs=MsgType;
		               } else {
		                    //处理短信长度大于60个字的情况
		                    strCONTENTs = strMsgTitle1;
		               }
		               LOGGER.info("发送短信内容:",strCONTENTs);
		               if (strCONTENTs.length()>180) {
		            	   strCONTENTs = strCONTENTs.substring(0,180)+"...";
		               }
		               strMaxID = getBPIP_HANDSET_ID();
		               addBuf.delete(0,addBuf.length());//清空
		               addBuf.append("Insert into BPIP_HANDSET(ID,USERNO,MOBILE,CONTENT,SENDDATE,FINISHDATE,TYPEBOX,SENDTYPE,MONTH,WEEK,DAY,HOUR,MINUTE,ISSEND,TTABLEID,SUSERNO) values ('")
		                  	 .append(strMaxID).append("','").append(UserNo).append("','").append(strMobile).append("','").append(strCONTENTs)
		                  	 .append("',sysdate,sysdate,5,0,0,0,0,0,0,'0','").append(strRunID).append("','").append(strDoUser).append("')");
		               
		               mVec.add(addBuf.toString());
		               MobileAdd++;
		           }
		         }
		       }
		       //--------------------------处理短信提示结束--------------------------
		     }
		     //------------------插入开始步骤的流程流转过程记录(先判断,没有则加入)------------------
		     addBuf.delete(0,addBuf.length());//清空
		     addBuf.append(Node_No_S).append(",").append(Node_No_S);
		     
		     if (strSES.lastIndexOf(addBuf.toString())<=-1) {
		         //计算流程流转过程表的ID
		         strRunID = getFLOW_RUNTIME_ACTIVITY_ID();
		         addBuf.delete(0,addBuf.length());//清空
		         addBuf.append("Insert into FLOW_RUNTIME_ACTIVITY(ID,FID,SACTIVITY,EACTIVITY,NAME,DOPSN,DODATE,DOIDEA,DOFLAG,DOIP,SENDPSN,SENDDATE) values ('")
		               .append(strRunID).append("','").append(strID).append("','").append(Node_No_S).append("','").append(Node_No_S).append("','")
		               .append(strConnName).append("','").append(UserNo).append("',sysdate,'").append(DoIdea).append("','1','','")
		               .append(UserNo).append("',sysdate)");
		         mVec.add(addBuf.toString());
		     }
		     //---------------------------------------------------
		     //---------------------处理消息提示--------------------
		     String strIsSendMessage = "";//为1时发送消息提示
		     if (activity!=null) {
		    	 strIsSendMessage = activity.getISMESSAGE();
	    	 }
		     String strSendMessage = "";
		     if (strIsSendMessage.equals("1"))//需发送消息
		     {
		        addBuf.delete(0,addBuf.length());//清空
		        addBuf.append("<a href=# onclick=OpenUrl(''/flow/openflowrun?FlowNo=")
		              .append(strFLOWID).append("&ExecuteNo=").append(strID).append("&FormID=").append(FormID)
		              .append("&OtherID=").append(OtherID).append("&ParentID=").append(strParentID).append("&ParentID1=")
		              .append(strParentID1).append("&RID=").append(strRID).append("'',''1'') title=''>").append(TitleName).append("</a>");
		        
		        strSendMessage = addBuf.toString();
		        mVec = getSendFlowMessage(Do_User_Nos,strSendMessage,mVec);
		     }
		     //处理消息提示结束-------------------
		     return mVec;
	     }  catch (Exception ex) {
	    	 LOGGER.error("流程引擎---InsertFlowRunData函数出错：", ex);
	    	 return null;
	     }
    }

    /**
     * 功能：根据流程ID得到流程的主表名称
     * @param strID 流程ID
     * @return returnValue 返回主表名称
     * @throws Exception 
     */
    private String getFlowFormTable(String strID) throws Exception {
      StringBuffer addBuf = new StringBuffer();
      addBuf.append("Select TABLENAME from BPIP_TABLE where TABLEID in (Select MAINTABLE from COLL_DOC_CONFIG "
      			  + "where ID in(Select DOCID from FLOW_CONFIG_PROCESS where ID = '"+strID+"'))");
      List<Map<String, Object>> dbset = userMapper.selectListMapExecSQL(addBuf.toString());
      
      addBuf.delete(0,addBuf.length());//清空
      if (dbset != null && dbset.size() > 0) {
    	  if (dbset.get(0) != null && dbset.get(0).get("TABLENAME") != null) {
    		  addBuf.append(dbset.get(0).get("TABLENAME").toString());
    	  }else
    	  {
    		  addBuf.append("");
    	  }
          dbset = null;//赋空值
      }
      return addBuf.toString();
    }

    /**
     * 功能：去掉流程流转记录中的当前处理人
     * @return returnValue 返回结果
     * @throws Exception 
     */
    private Vector<Object> updateFlowRunTimeDoUser(ActFlowRun ActFlowRun, Vector<Object> mVec) throws Exception {
	      String DoUserList = "";
	      String TmpUser = "";
	      //得到流程运转的相关参数-------------
	      String Execute_No = ActFlowRun.getExecute_No();
	      String UserID = ActFlowRun.getUserNo();
	      //得到流程运转的相关参数-------------
	      FLOW_RUNTIME_PROCESS Fprocess = null;
	      Fprocess = getFlowRuntimeprocess(Execute_No);
	      DoUserList = Fprocess.getACCEPTPSN();
	      String strFUID = Fprocess.getFUID();
	      if (strFUID==null) { strFUID=""; }
	      if (!strFUID.equals(UserID)) {
	         strFUID = strFUID + ","+UserID;
	      }
	      List<Object> UserList = getArrayList(DoUserList, ",");
	      DoUserList = "";
	      for (int i = 0; i < UserList.size(); i++) {
	        TmpUser = UserList.get(i).toString();
	        if (!TmpUser.equals(UserID)) {
	          DoUserList = DoUserList + TmpUser + ",";
	        }
	      }
	      if (DoUserList.length() > 0) {
	        DoUserList = DoUserList.substring(0, DoUserList.length() - 1);
	      }
	      StringBuffer addBuf = new StringBuffer();
	      addBuf.append("update FLOW_RUNTIME_PROCESS set ACCEPTPSN='"+DoUserList+"',FUID='"+strFUID+"' "
	      			  + "where ID='"+Execute_No).append("'");
	      mVec.add(addBuf.toString());
	      
	      Fprocess.setACCEPTPSN(DoUserList);
	      Fprocess.setFUID(strFUID);
	      FLOW_RUNTIME_PROCESSHashtable.put(Execute_No,Fprocess);
	      
	      return mVec;
    }

    /**
     * 得到流程收回后的步骤ID
     * @return
     * @throws Exception 
     */
    private String getFlowTakeBackID(ActFlowRun ActFlowRun) throws Exception {
		  String strReturn = "0";
		  String strUserID = ActFlowRun.getUserNo();
		  String Execute_No = ActFlowRun.getExecute_No();
		  StringBuffer addBuf = new StringBuffer();
		  addBuf.append("SELECT SACTIVITY FROM FLOW_RUNTIME_ACTIVITY where DOFLAG='0' "
		  			  + "and FID='"+Execute_No+"' and SENDPSN='"+strUserID+"'");
		  List<Map<String, Object>> dbset = userMapper.selectListMapExecSQL(addBuf.toString());
		  if(dbset != null && dbset.size() > 0) {
			 if (dbset.get(0) != null && dbset.get(0).get("SACTIVITY") != null) {
				 strReturn = dbset.get(0).get("SACTIVITY").toString();
			 }
		     dbset = null;//赋空值
		  }
		  return strReturn;
    }

    /**
    * 功能：根据流程标识和流程独立运转的单位编号得到流程实体
    * @param strFlowID 流程标识串
    * @param strUnit 流程独立运转的单位编号
    * @return
     * @throws Exception 
    */
    private FLOW_CONFIG_PROCESS getFlowProcessBS(String strFlowID) throws Exception {
           FLOW_CONFIG_PROCESS Process = null;
           StringBuffer addBuf = new  StringBuffer();
           addBuf.append("SELECT * FROM FLOW_CONFIG_PROCESS where IDENTIFICATION='"+strFlowID)
                 .append("' and FLOWPACKAGE in (select ID from FLOW_CONFIG_PACKAGE)");
           List<Map<String, Object>> dbset = userMapper.selectListMapExecSQL(addBuf.toString());
           if (dbset != null && dbset.size() > 0) {
                Process = (FLOW_CONFIG_PROCESS) ReflectionUtil.convertMapToBean(dbset.get(0), FLOW_CONFIG_PROCESS.class);
                dbset = null;//赋空值
           }
           return Process;
    }

	/**
	 * 功能：删除系统附件记录
	 * @param strFileID 附件ID
	 * @return return 返回是否成功
	 * @throws Exception 
	 */
	private boolean sysAttFileDel(String strFileID) throws Exception {
       StringBuffer addBuf = new StringBuffer();
       addBuf.append(SysPreperty.getProperty().UploadFilePath+"/AttManage");
       String strALLPath = addBuf.toString();
       addBuf.delete(0,addBuf.length());//清空
       addBuf.append("select FILEPATH,FILENAME from BPIP_ATT where ID='"+strFileID+"'");
       
       Map<String, Object> retmap = userMapper.selectMapExecSQL(addBuf.toString());
       addBuf.delete(0,addBuf.length());//清空
       if (retmap != null && retmap.size() > 0) {
           addBuf.append(strALLPath+"/"+retmap.get("FILEPATH").toString())
                 .append("/"+retmap.get("FILENAME").toString());
           retmap = null;//赋空值
       }
       //删除服务器上的文件
       File file = new File(addBuf.toString());
       file.delete();
       //删除数据库里的记录
       addBuf.delete(0,addBuf.length());//清空
       addBuf.append("delete From BPIP_ATT where ID='"+strFileID+"'");
       Integer retInt = userMapper.deleteExecSQL(addBuf.toString());
       if (retInt != null && retInt > 0) {
    	   return true;
       }
       return false;
	}

	/**
	 * 功能：根据流转编号、当前活动ID、当前用户得到发送步骤的活动ID
	 * @return returnValue 返回上一步的处理活动ID
	 * @throws Exception 
	 */
	private String getUpActivityID(ActFlowRun ActFlowRun) throws Exception {
	    String returnValue = "";
	    String strSACTIVITY = "";
	    String strEACTIVITY = "";
	    //得到流程运转的相关参数---------------
	    String Execute_No = ActFlowRun.getExecute_No();
	    String ActivityID = ActFlowRun.getNode_No_S();
//	    String UserID = ActFlowRun.getUserNo();
	    StringBuffer  addBuf = new   StringBuffer();
	    //得到流程运转的相关参数---------------
	    //addBuf.append("select SACTIVITY,EACTIVITY from FLOW_RUNTIME_ACTIVITY where FID='"+Execute_No)
	    //      .append("' and EACTIVITY='"+ActivityID+"' and DOPSN='"+UserID+"' order by ID desc");
	    addBuf.append("select SACTIVITY,EACTIVITY from FLOW_RUNTIME_ACTIVITY where FID='"+Execute_No)
	          .append("' and EACTIVITY='"+ActivityID+"' and SACTIVITY<>EACTIVITY order by ID desc");
	    
	    List<Map<String, Object>> dbset = userMapper.selectListMapExecSQL(addBuf.toString());
	    if (dbset != null && dbset.size()>0) {
	      int length = dbset.size();
	      for (int i=0; i<length; i++) {
	    	Map<String, Object> retmap = dbset.get(i);
	        strSACTIVITY = retmap.get("SACTIVITY").toString();
	        strEACTIVITY = retmap.get("EACTIVITY").toString();
	        if (getFlowActivityOrder(strSACTIVITY)<getFlowActivityOrder(strEACTIVITY)) {
	          returnValue = strSACTIVITY;
	          break;
	        }
	      }
	      dbset=null;//赋空值
	    }
	    return returnValue;
	}

	/**
	 * 功能：根据流转编号、当前活动ID、当前用户得到发送步骤的发送人员编号
	 * @return returnValue 返回上一步的处理活动ID
	 * @throws Exception 
	 */
	private String getUpActivitySendUserNo(ActFlowRun ActFlowRun) throws Exception {
	    String returnValue = "";
	    String strSACTIVITY = "";
	    String strEACTIVITY = "";
	    String strSENDPSN = "";
	    //---------------得到流程运转的相关参数---------------
	    String Execute_No = ActFlowRun.getExecute_No();
	    String ActivityID = ActFlowRun.getNode_No_S();
	    //String UserID = ActFlowRun.getUserNo();
	    //---------------得到流程运转的相关参数---------------
	    StringBuffer  addBuf = new   StringBuffer();
	    //addBuf.append("select SENDPSN,SACTIVITY,EACTIVITY from FLOW_RUNTIME_ACTIVITY where FID='"+Execute_No)
	    //      .append("' and EACTIVITY='"+ActivityID+"' and DOPSN='"+UserID+"' order by ID desc");
	    addBuf.append("select SENDPSN,SACTIVITY,EACTIVITY from FLOW_RUNTIME_ACTIVITY where FID='"+Execute_No)
	          .append("' and EACTIVITY='"+ActivityID+"' and SACTIVITY<>EACTIVITY order by ID desc");
	    
	    List<Map<String, Object>> dbset = userMapper.selectListMapExecSQL(addBuf.toString());
	    if (dbset != null && dbset.size()>0) {
	      int length = dbset.size();
	      for (int i=0; i<length; i++) {
	    	Map<String, Object> retmap = dbset.get(i);
	        strSACTIVITY = retmap.get("SACTIVITY").toString();
	        strEACTIVITY = retmap.get("EACTIVITY").toString();
	        strSENDPSN = retmap.get("SENDPSN").toString();
	        if (getFlowActivityOrder(strSACTIVITY)<getFlowActivityOrder(strEACTIVITY)) {
	          returnValue = strSENDPSN;
	          break;
	        }
	      }
	      dbset = null;//赋空值
	    }
	    return returnValue;
	}

	/**
	 * 功能：根据条件更新流程的流转记录
	 * @param ActivityID 当前活动ID
	 * @param DoUserID 处理人员用户编号串
	 * @return returnValue 返回结果
	 */
	private Vector<Object> updateFlowRunTimeData(String ActivityID, String DoUserID, ActFlowRun ActFlowRun, SessionUser user, Vector<Object> mVec) {
	    try {
		    //得到流程运转参数---------
		    String Execute_No = ActFlowRun.getExecute_No();
		    String TitleName = ActFlowRun.getTitleName();
		    String Workflow_No=ActFlowRun.getWorkflow_No();
		    String Node_No_S=ActFlowRun.getNode_No_S();
		    String M_Node_No_S_E=ActFlowRun.getM_Node_No_S_E();
		    //得到流程运转参数---------
		    String strConnName = "";
		    int UserNum = 1;
		    String strName ="";
		    StringBuffer addBuf = new StringBuffer();
		    
		    FLOW_RUNTIME_PROCESS Fprocess = getFlowRuntimeprocess(Execute_No);
		    String strFUID = Fprocess.getFUID();
		    if (strFUID==null) { strFUID=""; }
		    if (strFUID.indexOf(user.getUserID())==-1) {
		       strFUID = strFUID + ","+user.getUserID();
		    }
		    if (strFUID.length()>2000) {
		       strFUID = strFUID.substring(0,2000);
		    }
		    String strFlowFS = "";
		    if (Fprocess!=null) {
		      strFlowFS = Fprocess.getFLOWID();//得到流程标识
		    }
		    String strFLOWNAME = getFlowIName(strFlowFS);//根据流程标识得到流程的名称
		    FLOW_CONFIG_ACTIVITY activity = getFlowActivity(ActivityID);
		    String strActivityName = "";
		    if (activity!=null) {
		       strActivityName = activity.getNAME();//得到活动名称
		    }
		    if (TitleName.length() == 0) {
		      addBuf.append(strFLOWNAME).append("→").append(strActivityName);
		      strName = addBuf.toString();
		    } else {
		      addBuf.append(TitleName).append("→").append(strFLOWNAME).append("→").append(strActivityName);
		      strName = addBuf.toString();
		    }
		    List<Object> UserList = getArrayList(DoUserID, ",");
		    //-------------接收人数量-------------
		    UserNum = UserList.size();
		    addBuf.delete(0,addBuf.length());//清空
		    addBuf.append("update FLOW_RUNTIME_PROCESS set ACCEPTPSN='").append(DoUserID).append("',NAME='").append(strName)
		          .append("',ACCEPTDATE=sysdate,CURRACTIVITY='").append(ActivityID).append("',STATE='1',ACCEPTPSNNUM=").append(UserNum)
		          .append(",FUID='"+strFUID+"' where ID='").append(Execute_No).append("'");
		    
		    mVec.add(addBuf.toString());
		    
		    Fprocess.setNAME(TitleName);
		    Fprocess.setACCEPTPSN(DoUserID);
		    Fprocess.setSTATE("1");
		    Fprocess.setCURRACTIVITY(ActivityID);
		    Fprocess.setACCEPTPSNNUM(UserNum);
		    Fprocess.setFUID(strFUID);
		    FLOW_RUNTIME_PROCESSHashtable.put(Execute_No,Fprocess);
		    
		    //处理返回时新增加一条记录,以便记录同一步骤多次操作的情况---2009-04-27-----
		    String strMaxN0 = getFLOW_RUNTIME_ACTIVITY_ID();
		    //根据开始活动和结束活动得到关系的名称
		    strConnName = getActivityConnName(Node_No_S, M_Node_No_S_E);
		    
		    FLOW_CONFIG_PROCESS Process = getFlowProcess1(Workflow_No);
		    if (Process!=null && activity!=null) {
			    addBuf.delete(0,addBuf.length());//清空
			    addBuf.append(Process.getNAME()).append("→").append(activity.getNAME());
			    strConnName=addBuf.toString();
		    }
		    addBuf.delete(0,addBuf.length());//清空
		    addBuf.append("Insert into FLOW_RUNTIME_ACTIVITY(ID,FID,SACTIVITY,EACTIVITY,NAME,DOPSN,DOFLAG,DOIP,SENDPSN,SENDDATE) values ('")
		          .append(strMaxN0).append("','").append(Execute_No).append("','").append(Node_No_S).append("','").append(ActivityID)
		          .append("','").append(strConnName).append("','").append(DoUserID).append("','0','','").append(user.getUserID()).append("',sysdate)");
		    mVec.add(addBuf.toString());
		    //处理返回时新增加一条记录,以便记录同一步骤多次操作的情况结束--------------
		    return mVec;
	    } catch (Exception ex) {
	    	LOGGER.error("流程引擎---UpdateFlowRunTimeData函数出错：", ex);
	    	return null;
	    }
	}

	/**
	 * 功能：根据流程标识得到流程的名称
	 * @param strFlowID 流程标识
	 * @return returnValue 返回流程的名称
	 * @throws Exception 
	 */
	private String getFlowIName(String strFlowID) throws Exception {
	    StringBuffer addBuf = new StringBuffer();
	    addBuf.append("Select NAME from FLOW_CONFIG_PROCESS where IDENTIFICATION = '"+strFlowID+"'");
	    List<Map<String, Object>> dbset = userMapper.selectListMapExecSQL(addBuf.toString());
	    addBuf.delete(0,addBuf.length());//清空
	    if (dbset != null && dbset.size() > 0) {
	    	if (dbset.get(0) != null && dbset.get(0).get("NAME") != null) {
	    		addBuf.append(dbset.get(0).get("NAME").toString());
	    	} else {
	    		addBuf.append("");
	    	}
	        dbset = null;//赋空值
	    }
	    return addBuf.toString();
	}

	private String getUserNameHashtable(String strPSN) throws Exception {
	    StringBuffer addBuf = new StringBuffer();
	    String strUSERID="";
	    String strNAME="";
	    String strRevalue = "";
	    addBuf.append("Select USERID,NAME from BPIP_USER where USERID='"+strPSN+"'");
	    Map<String, Object> retmap = userMapper.selectMapExecSQL(addBuf.toString());
	    if (retmap != null && retmap.size() > 0) {   
	           strUSERID = retmap.get("USERID").toString();
	           strNAME = retmap.get("NAME").toString();           
	           strRevalue = strNAME;
	           GetUserNameHashtable.put(strUSERID,strNAME);          
	    } else {
	    	   GetUserNameHashtable.put(strPSN,"");
	    }
	    retmap = null;//赋空值
	    return strRevalue;  
	}
}