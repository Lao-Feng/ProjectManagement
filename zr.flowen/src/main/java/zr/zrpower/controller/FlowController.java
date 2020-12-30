package zr.zrpower.controller;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import zr.zrpower.common.util.DateWork;
import zr.zrpower.common.util.FunctionMessage;
import zr.zrpower.common.util.SysPreperty;
import zr.zrpower.flowengine.mode.base.Button;
import zr.zrpower.flowengine.mode.base.Group;
import zr.zrpower.flowengine.mode.base.Package;
import zr.zrpower.flowengine.mode.base.Variable;
import zr.zrpower.flowengine.mode.config.FLOW_CONFIG_ACTIVITY;
import zr.zrpower.flowengine.mode.config.FLOW_CONFIG_ACTIVITY_CONNE;
import zr.zrpower.flowengine.mode.config.FLOW_CONFIG_PROCESS;
import zr.zrpower.flowengine.mode.config.FLOW_CONFIG_TIME;
import zr.zrpower.flowengine.mode.monitor.FLOW_CONFIG_ENTRUST;
import zr.zrpower.flowengine.mode.monitor.FLOW_RUNTIME_PROCESS;
import zr.zrpower.flowengine.runtime.ActFlowRun;
import zr.zrpower.intercept.annotation.ZrSafety;
import zr.zrpower.model.BPIP_UNIT;
import zr.zrpower.model.SessionUser;
import zr.zrpower.service.*;
import zr.zrpower.service.impl.FlowAutoServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 流程信息控制器
 * @author lwk
 *
 */
@ZrSafety
@Controller
@RequestMapping("/flow")
public class FlowController extends BaseController {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private FLOW_CONFIG_ACTIVITY entityActivity;
	private HttpServletRequest _request;
	static boolean isRun = false;
	/** 流程监控函数库. */
	@Autowired
	private FlowMonitorService flowMonitorService;
	/** 程基本属性管理服务 . */
	@Autowired
	private FlowSetupService flowSetupService;
	/** 流程配置服务. */
	@Autowired
	private FlowConfigService flowConfigService;
	/** 流程管理活动服务. */
	@Autowired
	private FlowManageActService flowManageAct;
	/** 流程控制函数库. */
	@Autowired
	private FlowControlService flowControl;
	/** 流程引擎管理服务. */
	@Autowired
	private FlowManageService flowManageService;
	/** 界面公共函数库服务层. */
	@Autowired
	private UIService uiService;
	/** 单位信息服务层. */
	@Autowired
	private UnitService unitService;
	/** 用户操作服务层. */
	@Autowired
	private UserService userService;

	static {// 静态代码块，先于构造方法执行
		FlowAutoServiceImpl flowAutoServer = null;
	    int intnum = 0;
	    boolean isRun1 = true;
	    try {
	    	flowAutoServer = new FlowAutoServiceImpl();
	    } catch (Exception ex) {
	    	ex.printStackTrace();
	    }
	    while(!isRun){
	      try {
	    	  isRun = true;
	    	  isRun1 = flowAutoServer.startScan();
	    	  if (!isRun1) {isRun=false;}
	      } catch (Exception ex) {
	    	  ex.printStackTrace();
	      }
	      intnum ++;
	      if (intnum > 30) { break; }
	    }
	    flowAutoServer = null;
	}

	/**
	 * 流程信任接口
	 * @param request
	 * @param response
	 */
	@RequestMapping("/flowentrust")
	public void flowEnTrust(HttpServletRequest request, HttpServletResponse response) {
	    try {
	    	request.setCharacterEncoding("UTF-8");
		    response.setContentType("text/html; charset=UTF-8");
		    String strAction = request.getParameter("Act");
		    if (strAction != null) {
		    	if (strAction.equals("add")) {
		    		addFlowEt(request, response);
		    	}
		    	else if (strAction.equals("del")) {
		    		deleteEt(request, response);
		    	}
		    	else if (strAction.equals("edit")) {
		    		editFlowEt(request, response);
		    	}
		    	else if (strAction.equals("getList")) {
		    		getListEt(request, response);
		    	} else {
		    	}
		    }
	    } catch (Exception ex) {
	    	LOGGER.error("FlowController.flowEnTrust Exception:\n", ex);
	    }
	}

	/**
	 * 流程包接口
	 * @param request
	 * @param response
	 */
	@RequestMapping("/flowpackage")
	public String flowpackage(HttpServletRequest request, HttpServletResponse response) {
		String pagepath = "";
		try {
			request.setCharacterEncoding("UTF-8");
		    response.setContentType("text/html; charset=UTF-8");
		    String strAction = request.getParameter("Act");
		    
		    HttpSession session = request.getSession(true);
		    SessionUser user = (SessionUser)session.getAttribute("userinfo");
		    if (strAction != null) {
		      if (strAction.equals("edit")) {
		    	  pagepath = editFlowpk(request, response);
		      }
		      else if (strAction.equals("del")) {
		    	  pagepath = deleteFlowpk(request, response);
		      }
		      else if (strAction.equals("add")) {
		    	  pagepath = addFlowpk(user.getUnitID(), request, response);
		      } else {
		      }
		    }
		} catch (Exception ex) {
			LOGGER.error("FlowController.flowpackage Exception:\n", ex);
		}
		return pagepath;
	}

	/**
	 * 流程按钮接口
	 * @param request
	 * @param response
	 * @throws Exception 
	 */
	@RequestMapping("/flowbutton")
	public void flowButton(HttpServletRequest request, HttpServletResponse response) throws Exception {
	    String strAction = request.getParameter("Act");
	    if (strAction != null) {
	    	if (strAction.equals("edit")) {
	    		editButton(request, response);
	    	}
	    	else if (strAction.equals("del")) {
	    		deleteButton(request, response);
	    	}
	    	else if (strAction.equals("add")) {
	    		addButton(request, response);
	    	}
	    	else if("getList".equals(strAction)){
	    		getListBt(request, response);
	    	} else {
	      	}
	   }
	}

	/**
	 * 流程处理接口
	 * @param request
	 * @param response
	 * @throws Exception 
	 */
	@RequestMapping("/flowprocess")
	public String flowProcess(HttpServletRequest request, HttpServletResponse response) throws Exception {
	    request.setCharacterEncoding("UTF-8");
	    response.setContentType("text/html; charset=UTF-8");
	    String pagepath = "";// 页面跳转路径
	    String strAction = request.getParameter("Act");
	    if (strAction != null) {
	      if (strAction.equals("edit")) {
	    	  pagepath = editProcess(request, response);
	      }
	      else if (strAction.equals("del")) {
	    	  pagepath = deleteProcess(request, response);
	      }
	      else if (strAction.equals("add")) {
	    	  pagepath = addProcess(request, response);
	      }
	      else if (strAction.equals("edit1")) {
	    	  pagepath = edit1Process(request, response);
	      }
	      //----------------得到流程列表----------------//
	      else if (strAction.equals("flowlist")) {
	    	  getflowlist(response);
	      }
	      else if (strAction.equals("newflowlist")) {
	    	  getnewflowlist(request, response);
	      }
	      else if (strAction.equals("cslist")) {
	    	  getFlowCSList(request, response);
	      } else {
	      }
	    }
	    if (pagepath.length() > 0) {
	    	return pagepath;
	    }
//	    response.getWriter().println("{\"data\", \"true\"}");
//	    response.getWriter().flush();
	    return null;
	}

	@RequestMapping("/flowMaActSer")
	public String flowMaActSer(HttpServletRequest request, HttpServletResponse response) throws IOException {
	    _request = request;// HttpServletRequest请求
	    response.setContentType("text/html; charset=UTF-8");
	    String strAction = request.getParameter("Act");
	    String strflowtype = request.getParameter("flowtype");
	    String strformtype = request.getParameter("formtype");
	    String strFID = request.getParameter("FID");
	    String strTYPE = request.getParameter("TYPE");

	    String pagepath = "";
	    FunctionMessage fm = new FunctionMessage(1);
	    fm.setResult(true);
	    entityActivity = new FLOW_CONFIG_ACTIVITY();
	    entityActivity.fullDataFromRequest(request);
	    entityActivity = setReqToModel(entityActivity,request);
	    entityActivity.setID(request.getParameter("ActivityID"));
	    
	    try {
	      if (strAction.equals("Operate")) {
	        if (entityActivity.getID().equals("null")) {
	          //处理增加活动时，活动ID为便于处理设为字符串"null"的情况
	          entityActivity.setID(flowManageAct.getActivityMaxID());
	          entityActivity.setIDENTIFICATION("FA" + entityActivity.getID());
	        }
	        saveBaseProperty(); //基本属性
	        if (strflowtype.equals("1")) {
		        saveStrategyProperty();//策略属性
		        saveOverTimeFieldInfo();//超时属性
		        saveOperateProperty();//操作属性
		        saveAttProperty();//附件属性
		        saveUserGroupProperty();//参与者属性
	        }
	        saveCtlFieldInfo();//可控字段
	      }
	    } catch (Exception ex) {
	    	ex.printStackTrace();
	    	this.Msg = "连接客户端文件中间件服务失败！<br>详细错误信息：<br>   " + ex.toString();
	    	this.returnPath = "javascript:history.back(-1);";
	    }
	    if (fm.getResult()) {
	      String strid = entityActivity.getID();
	      String strISBRANCH = entityActivity.getISBRANCH();
	      pagepath = "redirect:/flow/openflowcfg?Act=ManageActivity&ID="+strid+"&FID="+strFID+"&TYPE="+strTYPE
		  			+"&flowtype="+strflowtype+"&formtype="+strformtype+"&ISBRANCH="+strISBRANCH;
	      return pagepath;
	    }
	    //this.out = response.getWriter();
	    //this.getBox();
	    response.getWriter().println("{\"data\", \"true\"}");
	    response.getWriter().flush();
	    return null;
	}

	/**
	 * 流程流转控制接口
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping("/actionflowturn")
	public void actionFlowTurn(HttpServletRequest request, HttpServletResponse response) throws IOException {
	     response.setContentType("text/html; charset=UTF-8");
	     String strAction = request.getParameter("Act");
	     if (!strAction.equals(null)) {
	      if (strAction.equals("edit")) {
	        EditFlowTurn(request, response);
	      }
	    }
	}

	@RequestMapping("/actionflowentruscarry")
	public void actionFlowEntrusCarry(HttpServletRequest request, HttpServletResponse response) throws IOException {
	    response.setContentType("text/html; charset=UTF-8");
	    String strAction = request.getParameter("Act");
	    if (strAction.equals("edit")){
	        EditFlowEntrus(request, response);
	    }
	    if (strAction.equals("edit1")) {
	        EditFlowEntrus1(request, response);
	    }
	}

	/**
	 * 流程运行接口
	 * @param modelMap
	 * @param request
	 * @param response
	 * @throws IOException 
	 */
	@RequestMapping("/flowrun")
	public String startFlowRun(ModelMap modelMap, 
			HttpServletRequest request, HttpServletResponse response) throws IOException {
	      response.setContentType("text/html; charset=UTF-8");
	      HttpSession session = request.getSession(true);
	      SessionUser user = (SessionUser)session.getAttribute("userinfo");
	      //得到流程的基础属性
	      String strOptCmd = request.getParameter("OptCmd");//记录属性按钮命令值
	      String strIdentification = request.getParameter("Identification");//流程标识
	      String strOptCmd_Name = request.getParameter("OptCmd_Name");//记录属性按钮命令值中文
	      String strWorkflow_No = request.getParameter("Workflow_No");//流程编号
	      String strNode_No_S = request.getParameter("Node_No_S");//当前步骤编号
	      String strM_Node_No_S_E = request.getParameter("M_Node_No_S_E");//下一分支步骤编号
	      String strS_Node_No_S_E = request.getParameter("S_Node_No_S_E");//上一分支步骤编号
	      String strUserNo = request.getParameter("UserNo");//当前用户编号
	      String strExecute_No = request.getParameter("Execute_No");//流程流转编号
	      String strDo_User_Nos = request.getParameter("Do_User_Nos");//选择的处理用户编号
	      String strFormID = request.getParameter("FormID");//业务的ID值
	      String strTitleName = request.getParameter("TitleName");//业务的标题名称
	      String strOtherID = request.getParameter("OtherID");//关联字段的ID值
	      String strPath = request.getParameter("Path");//流程调用文件的路径
	      String strDoIdea = request.getParameter("DoIdea");//处理意见
	      String strUnit = request.getParameter("Unit");//单位编号
	      String strParentID = request.getParameter("ParentID");//父流程ID值或父表单记录值
	      String strParentID1 = request.getParameter("ParentID1");//父流程ID1值或父表单记录值
	      String strRID = request.getParameter("RID");//关联流程的流转ID
	      String strMsgType = request.getParameter("MsgType");//短信提示类别
	      String strRECORDID = request.getParameter("RECORDID");//附件ID
	      String strISBRANCH=request.getParameter("ISBRANCH");//多路分支是否表现在按钮上
	      
	      String pagepath = "";
	      String strIs = "0";
	      this.isOk = true;
	      this.Msg = "非法调用，操作不被接受，你的操作已被记录！";
	      if(strOptCmd!=null){
	      try {
	    	  ActFlowRun actFlowRun = new ActFlowRun();
	    	  //初始化流程运转类-----------------
	    	  actFlowRun.FlowInit(strOptCmd,strIdentification,strOptCmd_Name,strWorkflow_No,strNode_No_S,strM_Node_No_S_E,strS_Node_No_S_E,strUserNo,strExecute_No,strDo_User_Nos,strFormID,strTitleName,strOtherID,strPath,strDoIdea,strUnit,strParentID,strParentID1,strRID,strMsgType,strRECORDID,strISBRANCH);
	    	  //初始化流程运转类结束---------------
	    	  
	    	  //保存-------------------------------------
	    	  if (strOptCmd.equals("SAVE")){
	    		  //执行流程保存服务
	    		  strExecute_No = flowControl.flowSaveServer(actFlowRun);
	    		  //返回
	    		  //---------------
	    		  pagepath = "redirect:"+strPath+"?FlowNo="+strIdentification+"&FormID="+strFormID+"&OtherID="+strOtherID+"&ExecuteNo="+strExecute_No+"&ParentID="+strParentID+"&ParentID1="+strParentID1+"&ISBRANCH="+strISBRANCH+"&RID="+strRID+"&ISSAVE=1";
	    	  }
	    	  //提交-------------------------------------
	    	  else if (strOptCmd.equals("SUBMIT")){
	    		  //执行流程提交服务
	    		  flowControl.flowSubmitServer(actFlowRun,user);
	    		  strIs = "1";
	    	  }
	    	  //处理返回-----------------------------------
	    	  else if (strOptCmd.equals("RETURN")){
	    		  //执行流程处理返回服务
	    		  flowControl.flowReturnServer(actFlowRun,user);
	    		  strIs = "1";
	    	  }
	    	  //退回---------------------------------------
	    	  else if (strOptCmd.equals("UNTREAD")){
	    		  //初始化流程运转类,退回的特殊情况(下面两个参数互换)-----------------
	    		  actFlowRun.FlowInitUntread(strS_Node_No_S_E,strM_Node_No_S_E);
	    		  //初始化流程运转类结束---------------
	    		  //------------
	    		  //执行流程提交服务(第六个参数的值为退回步骤的ID--strS_Node_No_S_E,第六和第七个参数互换)
	    		  flowControl.flowSubmitServer(actFlowRun,user);
	    	  }
	    	  //完成----------------------------------------
	    	  else if (strOptCmd.equals("FINISH")){
	    		  //执行流程提交服务
	    		  flowControl.flowFinishServer(actFlowRun,user);
	    		  strIs = "1";
	    	  }
	    	  //移交----------------------------------------
	    	  else if (strOptCmd.equals("DEVOLVE")){
	    		  //执行流程移交服务
	    		  flowControl.flowDevolveServer(actFlowRun,user);
	    	  }
	    	  //删除----------------------------------------
	    	  else if (strOptCmd.equals("DELETE")){
	    		  //执行流程删除服务
	    		  flowControl.flowDeleteServer(actFlowRun,user);
	    	  }
	    	  //初始化----------------------------------------
	    	  else if (strOptCmd.equals("INIT")){
	    		  //执行流程初始化服务
	    		  flowControl.flowInitServer(actFlowRun,user);
	    	  }
	    	  //收回----------------------------------------
	    	  else if (strOptCmd.equals("TAKEBACK")){
	    		  //执行流程收回服务
	    		  flowControl.flowTakeBackServer(actFlowRun,user);
	    	  }
	    	  //---------------
	    	  //送部门承办----------------------------------------
	    	  else if (strOptCmd.equals("SUBDEPT")){
	    		  //执行送部门承办服务
	    		  flowControl.flowSubDeptServer(actFlowRun,user);
	    	  }
	    	  //---------------
	    	  //分送已阅--------
	    	  else if (strOptCmd.equals("SENDFINISH")){
	    		  //执行分送已阅服务
	    		  flowControl.flowSendFinishServer(actFlowRun);
	    	  }
	    	  //--------------
	    	  if (!strOptCmd.equals("SAVE")){
	    		  FLOW_CONFIG_ACTIVITY activity = null;
	    		  activity = flowControl.getFlowActivity(strNode_No_S);
	    		  //得到当前活动的流程处理提示信息
	    		  String strMessage = activity.getMESSAGE();
	    		  modelMap.put("ISMESSAGE",strIs);
	    		  modelMap.put("Message",strMessage.trim());
	    		  //关闭窗口
	    		  pagepath = "ZrWorkFlow/FlowRun/FlowCloseWindow";
	    	  }
	      } catch (Exception ex){
	    	  ex.printStackTrace();
	    	  this.Msg = "连接中间件服务器失败，请稍后再试！";
	      }  
	      }
	      if (pagepath.length() > 0) {
	    	  return pagepath;
	      }
	      response.getWriter().println("{\"data\", \"true\"}");
		  response.getWriter().flush();
		  return null;
	}

	@RequestMapping("/openflowrun")
	public String openFlowRun(ModelMap modelMap, HttpServletRequest request, 
			HttpServletResponse response) throws ServletException, IOException {
	      response.setContentType("text/html; charset=UTF-8");

	      HttpSession session = request.getSession(true);
	      SessionUser user = (SessionUser)session.getAttribute("userinfo");
	      String strWEBPATH = SysPreperty.getProperty().AppUrl;

	      String ImagePath = "";
	      FLOW_CONFIG_PROCESS Process = null;
	      FLOW_CONFIG_ACTIVITY activity = null;

	      String strExecute_No=""; //流转ID
	      String strWorkflow_No=""; //流程ID
	      String strFlowLink="";    //关联流程
	      String strFlowActivityLink="";//流程步骤示例
	      String strFlowName="";    //流程名称
	      String StrNode_No_S="";   //当前节点编号
	      String strM_Node_No_E=""; //下一节点
	      String strM_Node_No_S_E="";//下一节点选择后
	      String strS_Node_No_E=""; //上一节点
	      String strS_Node_No_S_E=""; //选择后的上一节点
	      String strFormID="";      //表单ID
	      String strTitleName="";   //标题名称
	      String strOtherID="";     //流程的关联值
	      String strParentID="";    //父ID
	      String strParentID1="";    //父ID1
	      String strRID ="";         //关联流程
	      String strActivityName="";//当前步骤的名称
	      String strActivityType="";//当前步骤的类型
	      String strFlowDoLog="";   //流程的处理记录
	      String strIdentification = "";//流程标识

	      String strCollType="";    //采集引擎调用类型,1全部是只读形式显示，否则按流程设置的显示
	      String strisNew="0";      //是否是新建流程,1是，0否
	      boolean isDoFlow = false; //当前用户是否拥有处理权限
	      String strCollActivityID="";//调用采集引擎的活动ID
	      String strFristActivityID="";//第一个的活动ID
	      String strNeedNewFlow="";//必须新建但还没有新建的流程名称
	      String strtype = "";//流程调用特殊类型主要用于刷新父级页面
	      String strISwhere = "0";//与当前步骤相关的线上是否设置有和字段值相关的条件

	      String AttType = "4";     //附件类型
	      String AttMessage = "";   //附件提示
	      int Attnum1 =0;           //需上传的附件数

	      String strFormPath ="";//流程自定义表单页面的路径
	      String strISSAVE ="0"; //是否保存过
	      String strTYPE ="";    //类别 1流程管理
	      String strISBRANCH="";//多路分支是否在操作按钮上实现
	      String ButtonList1="";
	      String ButtonList2="";
	      String ButtonList3="";
	      String ButtonList4="";
	      String ButtonList5="";

	      //得到流程标识
	      strIdentification = request.getParameter("FlowNo");
	      if (strIdentification==null){strIdentification="";}
	      strWorkflow_No = strIdentification;
	      strExecute_No = request.getParameter("ExecuteNo");
	      if (strExecute_No==null){strExecute_No="";}
	      strFormID = request.getParameter("FormID");
	      if (strFormID==null){strFormID="";}
	      strOtherID = request.getParameter("OtherID");
	      if (strOtherID==null){strOtherID="";}
	      strParentID = request.getParameter("ParentID");
	      if (strParentID==null){strParentID="";}
	      strParentID1 = request.getParameter("ParentID1");
	      if (strParentID1==null){strParentID1="";}
	      strRID = request.getParameter("RID");
	      if (strRID==null){strRID="";}
	      strISSAVE = request.getParameter("ISSAVE");
	      if (strISSAVE==null){strISSAVE="";}
	      strTYPE = request.getParameter("TYPE");
	      if (strTYPE==null){strTYPE="";}
	      strtype = request.getParameter("type");
	      if (strtype==null){strtype ="";}
	      strISBRANCH=request.getParameter("ISBRANCH");
	      if (strISBRANCH==null) {
	         strISBRANCH="";
	      }
	      //接收分送阅读时的活动id
	      String strsendid = request.getParameter("sendid");
	      if (strsendid==null){strsendid ="";}
	      //-----------------------------------------------
	      String strRECORDID = "";
	      String strTakeBack = "0";
	      String strFlowFormPath = "";
	      String strWindowTitle = "";
	      String strISSAVE1 = "";
	      //后退保存数据
	      String strISSAVE2 = "";
	      //前进必填留言
	      String strISLEAVE1 = "";
	      //后退必填留言
	      String strISLEAVE2 = "";
	      //活动处理前填写表单地址
	      String strADDFORMPATH = "";
	      String strADDFORMWIDTH = "";
	      String strADDFORMHEIGHT = "";
	      String strADDFORMMESSAGE = "";
	      String showidea = "";
	      ActFlowRun ActFlowRun = null;
	      String showuserid = "";
	      String showuserunit = "";
	      String StrFlowState = "";

	      try {
	        showuserid = user.getUserID();
	        showuserunit = user.getUnitID();
	        ImagePath = SysPreperty.getProperty().AppUrl+"Zrsysmanage/images/blueimg/";//图片路径
	        if (strExecute_No.length()==0) {//生成文件编号
	            strRECORDID = flowControl.globalID(showuserid);
	        }
	        //------------------得到标题------------------
	        if (strExecute_No.length()>0)
	        {strTitleName = flowControl.getTitle(strExecute_No);}
	        //------------------初始化流程默认运转参数------------------
	        ActFlowRun = new ActFlowRun();
	        //-------------------------------------------
	        if (strExecute_No!=null && strExecute_No.length()>0){
	           FLOW_RUNTIME_PROCESS Fprocess = null;
	           Fprocess = flowControl.getFlowRuntimeprocess(strExecute_No);
	           StrFlowState = Fprocess.getSTATE();
	           //根据流程流转表的ID得到流程标识
	           strWorkflow_No = Fprocess.getFLOWID();
	           //根据流程流转表的ID得到流程当前步骤
	           StrNode_No_S = Fprocess.getCURRACTIVITY();
	           //是分送阅读时-------------
	           if (strsendid.length()>0)
	           {StrNode_No_S = strsendid;}
	           //-----------------------------
	           strRECORDID = Fprocess.getRECORDID();//得到附件ID
	           //根据流程标识得到流程ID
	           strWorkflow_No = flowControl.getFlowID(strWorkflow_No);
	        } else {
	            //根据流程标识得到流程ID
	            strWorkflow_No = flowControl.getFlowID(strWorkflow_No);
	            //根据流程ID得到当前流程第一个活动(步骤)的ID
	            StrNode_No_S = flowControl.getFirstActivityID(strWorkflow_No);
	            strisNew="1";//是新建流程
	        }
	        if (StrNode_No_S.length()==0 || StrNode_No_S==null){
	            //第一个步骤编号
	            strFristActivityID = flowControl.getFirstActivityID(strWorkflow_No);
	            StrNode_No_S = strFristActivityID;
	        }
	        //根据流程ID得到流程实体
	        Process = flowControl.getFlowProcess(strWorkflow_No);
	        //根据流程编号得到流程名称
	        strFlowName = Process.getNAME();
	        //根据活动ID得到活动实体
	        activity = flowControl.getFlowActivity(StrNode_No_S);

	        strISBRANCH=activity.getISBRANCH();
	        ActFlowRun.FlowInitDefault(strExecute_No,strIdentification,strOtherID,strParentID,strParentID1,strRID,strRECORDID,strISBRANCH);

	        //得到活动的名称
	        strActivityName =activity.getNAME();
	        //根据当前活动的编号得到下一步的分支活动
	        strM_Node_No_E = flowControl.getNextActivityID(StrNode_No_S,ActFlowRun,user);
	        //根据当前活动及下一步的分支活动串得到包含接收类型的活动串
	        strM_Node_No_S_E = flowControl.getNextActivityTypeList(StrNode_No_S,strM_Node_No_E);
	        //根据当前活动的编号得到上一步的分支活动
	        strS_Node_No_E = flowControl.getUpSelectActivityID(StrNode_No_S,ActFlowRun,user);
	        //根据当前活动及上一步的分支活动串得到包含接收类型的活动串
	        strS_Node_No_S_E = flowControl.getNextActivityTypeList(StrNode_No_S,strS_Node_No_E);

	        strISwhere = flowControl.getISwhere(StrNode_No_S);
	        //得到当前流程相关的可操作流程
	        if (strExecute_No!=null && strExecute_No.length()>0){
	          //得到关联流程的标识串
	          strFlowLink = flowControl.getNextIFlowID(StrNode_No_S,ActFlowRun,user);
	          //得到关联的但还没有新建的关联流程名
	          if (strFlowLink.length()>0){
	          strNeedNewFlow = flowControl.getNeedFlowList(strFlowLink,StrNode_No_S,ActFlowRun);
	          //初始化流程默认运转参数------------------
	          ActFlowRun.FlowInitDefault(strExecute_No,strIdentification,strOtherID,strParentID,strParentID1,strExecute_No,strRECORDID,strISBRANCH);
	          strFlowLink = flowControl.getFlowRunIDHTML(strFlowLink,"/flow/openflowrun","table_title4.gif",ActFlowRun,user);
	          //初始化流程默认运转参数------------------
	          ActFlowRun.FlowInitDefault(strExecute_No,strIdentification,strOtherID,strParentID,strParentID1,strRID,strRECORDID,strISBRANCH);
	          }
	        }
	        //得到流程跟踪记录
	        if (strExecute_No.length()>0)
	        {strFlowDoLog = flowControl.getFlowDoLog(strExecute_No);}

	        //得到当前步骤的类型
	        strActivityType= activity.getTYPE();
	        //得到当前用户是否拥有处理权限
	        if (strExecute_No.length()>0){
	          isDoFlow = flowControl.getIsFlowDoList(strExecute_No,user);
	        }
	        //得到调用采集引擎是否全为只读的类型
	        if ((strActivityType.equals("2") || !isDoFlow) && strisNew.equals("0")) {
	          if (isDoFlow==false && strExecute_No.length()>0)
	          {strCollType="1";}
	          //没有权限或完成的流程显示按最后一步显示
	          strCollActivityID = flowControl.getLastlyActivityID(strWorkflow_No);//将用最后一步的流程属性来显示采集引擎
	        } else {
	           strCollActivityID =StrNode_No_S;
	        }
	        //得到流程处理图示
	        strFlowActivityLink=flowControl.getFlowActivityLink(strWorkflow_No,StrNode_No_S,"/flow/flowmanagecfg?Act=FlowShowMap");
	        //根据活动ID得到活动实体
	        activity = flowControl.getFlowActivity(strCollActivityID);
	        //得到当前活动的附件类型
	        AttType = activity.getATTTYPE();
	        Attnum1 = activity.getATTNUM();
	        //多路分支按钮是否在操作按钮上
	        strISBRANCH=activity.getISBRANCH();
	        //得到当前活动的自定义表单链接路径
	        strFormPath = activity.getFORMPATH();
	        if (strFormPath==null){strFormPath="";}
	        if (strFormPath.length()>0) {//为自定义表单形式
	          if (strFormPath.length()>0){
	             if (strFormPath.lastIndexOf("?") > -1) {//已经带参数
	               strFormPath = strFormPath + "&RUNID="+strExecute_No+"&COLL_AID="+strCollActivityID+"&coll_PKID="+strFormID+"&coll_FKID="+strOtherID+"&PAID1="+strParentID+"&PAID2="+strParentID1;
	             }else{
	               strFormPath = strFormPath + "?RUNID="+strExecute_No+"&COLL_AID="+strCollActivityID+"&coll_PKID="+strFormID+"&coll_FKID="+strOtherID+"&PAID1="+strParentID+"&PAID2="+strParentID1;
	             }
	          }else{
	             strFormPath ="ZrWorkFlow/FlowRun/flowmessage.html";
	          }
	        }
	        //流程数据显示完整路径
	        if (isDoFlow==false && strExecute_No.length()>0){
	          strCollType = "1";
	        }
	        if (strFormPath.length()>0) {//为自定义表单形式
	           strFlowFormPath = strFormPath;
	        }else{
	           strFlowFormPath = "/collect/collengine?COLL_AID="+strCollActivityID+"&coll_PKID="+strFormID+"&coll_FKID="+strOtherID+"&coll_MODE=2&coll_READ="+strCollType+"&PAID1="+strParentID+"&PAID2="+strParentID1+"&RID="+strRID;
	        }
	        strWindowTitle = "["+strFlowName+"]→["+strActivityName+"]";

	        //得到是否显示收回按钮(1是0否)
	        if (isDoFlow==false){
	           strTakeBack = flowControl.getIsTakeBack(strExecute_No,showuserid);
	        }
	       //前进保存数据
	       strISSAVE1 = activity.getISSAVE1();
	       //后退保存数据
	       strISSAVE2 = activity.getISSAVE2();
	       //前进必填留言
	       strISLEAVE1 = activity.getISLEAVE1();
	       //后退必填留言
	       strISLEAVE2 = activity.getISLEAVE2();
	       //活动处理前填写表单地址
	       strADDFORMPATH = activity.getADDFORMPATH();
	       if (strADDFORMPATH==null){strADDFORMPATH="";}
	       //活动处理前填写表单宽
	       strADDFORMWIDTH = activity.getADDFORMWIDTH();
	       if (strADDFORMWIDTH==null){strADDFORMWIDTH="";}
	       //活动处理前填写表单高
	       strADDFORMHEIGHT = activity.getADDFORMHEIGHT();
	       if (strADDFORMHEIGHT==null){strADDFORMHEIGHT="";}
	       //活动处理前非正确填写表单提示
	       strADDFORMMESSAGE = activity.getADDFORMMESSAGE();
	       if (strADDFORMMESSAGE==null){strADDFORMMESSAGE="";}
	       if (strADDFORMMESSAGE.trim().length()==0) {
	          strADDFORMMESSAGE = "请先填写系统设置的表单!";
	       }
	       showidea = "";
	       if (isDoFlow==false && strExecute_No.length()>0) {
	         showidea = "none";
	       }
	       if (strADDFORMPATH.trim().length()>0) {
	         if (strADDFORMPATH.lastIndexOf("?") > -1) {//已经带参数
	            strADDFORMPATH = strADDFORMPATH + "&RUNID="+strExecute_No+"&COLL_AID="+strCollActivityID+"&coll_PKID="+strFormID+"&coll_FKID="+strOtherID+"&PAID1="+strParentID+"&PAID2="+strParentID1;
	         }else{
	            strADDFORMPATH = strADDFORMPATH + "?RUNID="+strExecute_No+"&COLL_AID="+strCollActivityID+"&coll_PKID="+strFormID+"&coll_FKID="+strOtherID+"&PAID1="+strParentID+"&PAID2="+strParentID1;
	         }
	       }
	       ButtonList1 = flowControl.getActivityButton(strExecute_No,StrNode_No_S,"2",ActFlowRun,user,strM_Node_No_E,strS_Node_No_E);//得到当前流程当前步骤的按钮
	       ButtonList2 = flowControl.getActivityButton(strExecute_No,StrNode_No_S,"2","1",user);//得到当前流程当前步骤的按钮
	       ButtonList3 = flowControl.getCreateButton(strExecute_No,showuserid,"2","1");//得到流程创建人的按钮
	       ButtonList4 = flowControl.getDPublicButton(strWorkflow_No,"2","1",ActFlowRun);//得到已经分配过的公共类型的按钮
	       ButtonList5 = flowControl.getPublicButton("2","1");//得到公共类型的按钮
	    } catch (Exception ex) {
	    	ex.printStackTrace();
	    }
	    modelMap.put("Unit",showuserunit);
	    modelMap.put("Identification",strIdentification);
	    modelMap.put("Workflow_No",strWorkflow_No);
	    modelMap.put("RID",strRID);
	    modelMap.put("Node_No_S",StrNode_No_S);
	    modelMap.put("M_Node_No_E",strM_Node_No_E);
	    modelMap.put("M_Node_No_S_E",strM_Node_No_S_E);
	    modelMap.put("S_Node_No_E",strS_Node_No_E);
	    modelMap.put("S_Node_No_S_E",strS_Node_No_S_E);
	    modelMap.put("UserNo",showuserid);
	    modelMap.put("Execute_No",strExecute_No);
	    modelMap.put("ParentID",strParentID);
	    modelMap.put("ParentID1",strParentID1);
	    modelMap.put("FormID",strFormID);
	    modelMap.put("TitleName",strTitleName);
	    modelMap.put("OtherID",strOtherID);
	    modelMap.put("NeedNewFlow",strNeedNewFlow);
	    modelMap.put("AttMessage",AttMessage);
	    modelMap.put("FlowFormPath",strFlowFormPath);
	    modelMap.put("ISSAVE1",strISSAVE1);
	    modelMap.put("ISSAVE2",strISSAVE2);
	    modelMap.put("ISLEAVE1",strISLEAVE1);
	    modelMap.put("ISLEAVE2",strISLEAVE2);
	    modelMap.put("ADDFORMPATH",strADDFORMPATH);
	    modelMap.put("ADDFORMWIDTH",strADDFORMWIDTH);
	    modelMap.put("ADDFORMHEIGHT",strADDFORMHEIGHT);
	    modelMap.put("ADDFORMMESSAGE",strADDFORMMESSAGE);
	    modelMap.put("RECORDID",strRECORDID);
	    modelMap.put("ATTNUM",String.valueOf(Attnum1));
	    modelMap.put("ATTTYPE",AttType);
	    modelMap.put("ISBRANCH",strISBRANCH);
	    modelMap.put("ISWHERE",strISwhere);
	    strWEBPATH = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort();
	    modelMap.put("WEBPATH",strWEBPATH);
	    modelMap.put("FlowDoLog",strFlowDoLog);
	    modelMap.put("FlowActivityLink",strFlowActivityLink);
	    modelMap.put("FlowLink",strFlowLink);
	    modelMap.put("WindowTitle",strWindowTitle);
	    modelMap.put("CollActivityID",strCollActivityID);
	    modelMap.put("showidea",showidea);
	    modelMap.put("ButtonList1",ButtonList1);
	    modelMap.put("ButtonList2",ButtonList2);
	    modelMap.put("ButtonList3",ButtonList3);
	    modelMap.put("ButtonList4",ButtonList4);
	    modelMap.put("ButtonList5",ButtonList5);
	    modelMap.put("TakeBack",strTakeBack);
	    modelMap.put("FlowState",StrFlowState);
	    modelMap.put("ImagePath",ImagePath);

	    return "ZrWorkFlow/FlowRun/FlowRunWindow";
	}

	@RequestMapping("/openflowrun1")
	public String openFlowRun1(ModelMap modelMap, 
			HttpServletRequest request, HttpServletResponse response) throws Exception {
	    response.setContentType("text/html; charset=UTF-8");
		HttpSession session = request.getSession(true);
		SessionUser user = (SessionUser)session.getAttribute("userinfo");
		
		String strWEBPATH = "";
		strWEBPATH = SysPreperty.getProperty().AppUrl;
		String ImagePath = "";
		FLOW_CONFIG_PROCESS Process = null;
		FLOW_CONFIG_ACTIVITY activity = null;
	
		String strExecute_No=""; //流转ID
		String strWorkflow_No=""; //流程ID
		String strFlowLink="";    //关联流程
		String strFlowActivityLink="";//流程步骤示例
		String strFlowName="";    //流程名称
		String StrNode_No_S="";   //当前节点编号
		String strM_Node_No_E=""; //下一节点
		String strM_Node_No_S_E="";//下一节点选择后
		String strS_Node_No_E=""; //上一节点
		String strS_Node_No_S_E=""; //选择后的上一节点
		String strFormID="";      //表单ID
		String strTitleName="";   //标题名称
		String strOtherID="";     //流程的关联值
		String strParentID="";    //父ID
		String strParentID1="";    //父ID1
		String strRID ="";         //关联流程
		String strActivityName="";//当前步骤的名称
		String strFlowDoLog="";   //流程的处理记录
		String strIdentification = "";//流程标识
	
		String strCollType="";    //采集引擎调用类型,1全部是只读形式显示，否则按流程设置的显示
		String strCollActivityID="";//调用采集引擎的活动ID
		String strFristActivityID="";//第一个的活动ID
		String strNeedNewFlow="";//必须新建但还没有新建的流程名称
		String strtype = "";//流程调用特殊类型主要用于刷新父级页面
		String AttType = "4";     //附件类型
		String AttMessage = "";   //附件提示
		int Attnum1 =0;           //需上传的附件数
		String strFormPath ="";//流程自定义表单页面的路径
		String strISSAVE ="0"; //是否保存过
		String strTYPE ="";    //类别 1流程管理
	
		String showuserid = "";
		String showuserunit = "";
		String ButtonList3="";
		String ButtonList5="";
	
		//得到流程标识
		strIdentification = request.getParameter("FlowNo");
		if (strIdentification==null){strIdentification="";}
		strWorkflow_No = strIdentification;
		strExecute_No = request.getParameter("ExecuteNo");
		if (strExecute_No==null){strExecute_No="";}
		strFormID = request.getParameter("FormID");
		if (strFormID==null){strFormID="";}
		strOtherID = request.getParameter("OtherID");
		if (strOtherID==null){strOtherID="";}
		strParentID = request.getParameter("ParentID");
		if (strParentID==null){strParentID="";}
		strParentID1 = request.getParameter("ParentID1");
		if (strParentID1==null){strParentID1="";}
		strRID = request.getParameter("RID");
		if (strRID==null){strRID="";}
		strISSAVE = request.getParameter("ISSAVE");
		if (strISSAVE==null){strISSAVE="";}
		strTYPE = request.getParameter("TYPE");
		if (strTYPE==null){strTYPE="";}
		strtype = request.getParameter("type");
		if (strtype==null){strtype ="";}
		String strRECORDID = "";
		ActFlowRun ActFlowRun = null;
		
		String strISSAVE1 = "";
		//后退保存数据
		String strISSAVE2 = "";
		//前进必填留言
		String strISLEAVE1 = "";
		//后退必填留言
		String strISLEAVE2 = "";
		//活动处理前填写表单地址
		String strADDFORMPATH = "";
		String strADDFORMWIDTH = "";
		String strADDFORMHEIGHT = "";
		String strADDFORMMESSAGE = "";
		String strFlowFormPath = "";
		String strFlowFormPath1 = "";
		String strWindowTitle = "";
		
		try {
		try {
		   showuserid = user.getUserID();
		}
		catch (Exception ex2) {
		   user = (SessionUser)session.getAttribute("userinfo");
		   showuserid = user.getUserID();
		}
		showuserunit = user.getUnitID();
		if (strExecute_No.length()==0) {//生成文件编号
		     strRECORDID = flowControl.globalID(showuserid);
		}
		ImagePath = SysPreperty.getProperty().AppUrl+"Zrsysmanage/images/blueimg/";//图片路径
		
		//初始化流程默认运转参数------------------
		ActFlowRun = new ActFlowRun();
		ActFlowRun.FlowInitDefault(strExecute_No,strIdentification,strOtherID,strParentID,strParentID1,strRID,strRECORDID);
		//初始化默认流程运转参数结束---------------------
		//-------------------------------------------
		if (strExecute_No!=null && strExecute_No.length()>0){
		   FLOW_RUNTIME_PROCESS Fprocess = null;
		   Fprocess = flowControl.getFlowRuntimeprocess(strExecute_No);
		   //根据流程流转表的ID得到流程标识
		   strWorkflow_No = Fprocess.getFLOWID();
		   //根据流程流转表的ID得到流程当前步骤
		   StrNode_No_S = Fprocess.getCURRACTIVITY();
		   strRECORDID = Fprocess.getRECORDID();//得到附件ID
		   //根据流程标识和流程流转的创建日期得到流程ID
		   strWorkflow_No = flowControl.getFlowID(strWorkflow_No);
		}else{
		   //根据流程标识和流程流转的创建日期得到流程ID
		   strWorkflow_No = flowControl.getFlowID(strWorkflow_No);
		   //根据流程ID得到当前流程第一个活动(步骤)的ID
		   StrNode_No_S = flowControl.getFirstActivityID(strWorkflow_No);
		}
		//第一个步骤编号
		strFristActivityID = flowControl.getFirstActivityID(strWorkflow_No);
		if (StrNode_No_S.length()==0 || StrNode_No_S==null){
		  StrNode_No_S = strFristActivityID;
		}
		//根据流程ID得到流程实体
		Process = flowControl.getFlowProcess(strWorkflow_No);
		//根据流程编号得到流程名称
		strFlowName = Process.getNAME();
		//根据活动ID得到活动实体
		activity = flowControl.getFlowActivity(StrNode_No_S);
		//得到活动的名称
		strActivityName =activity.getNAME();
		
		//根据当前活动的编号得到下一步的分支活动
		strM_Node_No_E = flowControl.getNextActivityID(StrNode_No_S,ActFlowRun,user);
		//根据当前活动及下一步的分支活动串得到包含接收类型的活动串
		strM_Node_No_S_E = flowControl.getNextActivityTypeList(StrNode_No_S,strM_Node_No_E);
		//根据当前活动的编号得到上一步的分支活动
		strS_Node_No_E = flowControl.getUpSelectActivityID(StrNode_No_S,ActFlowRun,user);
		//根据当前活动及上一步的分支活动串得到包含接收类型的活动串
		strS_Node_No_S_E = flowControl.getNextActivityTypeList(StrNode_No_S,strS_Node_No_E);
		
		//得到流程跟踪记录
		if (strExecute_No.length()>0) {
			strFlowDoLog = flowControl.getFlowDoLog(strExecute_No);
		}
		//将用最后一步的流程属性来显示采集引擎
		strCollActivityID = flowControl.getLastlyActivityID(strWorkflow_No);
		//得到流程处理图示
		strFlowActivityLink=flowControl.getFlowActivityLink(strWorkflow_No,StrNode_No_S,"/flow/flowmanagecfg?Act=FlowShowMap");
		//根据活动ID得到活动实体
		activity = flowControl.getFlowActivity(strCollActivityID);
		//得到当前活动的附件类型
		AttType = activity.getATTTYPE();
		Attnum1 = activity.getATTNUM();
		
		//得到当前活动的自定义表单链接路径
		strFormPath = activity.getFORMPATH();
		if (strFormPath==null){strFormPath="";}
		if (strFormPath.length()>0) {//为自定义表单形式
		   if (strFormPath.length()>0) {
		     if (strFormPath.lastIndexOf("?") > -1) {//已经带参数
		       strFormPath = strFormPath + "&RUNID="+strExecute_No+"&COLL_AID="+strCollActivityID+"&coll_PKID="+strFormID+"&coll_FKID="+strOtherID+"&PAID1="+strParentID+"&PAID2="+strParentID1;
		     }else{
		       strFormPath = strFormPath + "?RUNID="+strExecute_No+"&COLL_AID="+strCollActivityID+"&coll_PKID="+strFormID+"&coll_FKID="+strOtherID+"&PAID1="+strParentID+"&PAID2="+strParentID1;
		     }
		   }else{
		     strFormPath ="ZrWorkFlow/FlowRun/flowmessage.html";
		   }
		}
		//流程数据显示完整路径
		strFlowFormPath = "";
		strFlowFormPath1 = "";
		if (strFormPath.length()>0) {//为自定义表单形式
		   strFlowFormPath = strFormPath;
		   strFlowFormPath1 = strFlowFormPath;
		   strFlowFormPath1 = strFlowFormPath1+"&coll_READ=2";
		}else{
		   strFlowFormPath = "/collect/collengine?COLL_AID="+strCollActivityID+"&coll_PKID="+strFormID+"&coll_FKID="+strOtherID+"&coll_MODE=2&coll_READ="+strCollType+"&PAID1="+strParentID+"&PAID2="+strParentID1+"&RID="+strRID;
		   strFlowFormPath1 = "/collect/collengine?COLL_AID="+strFristActivityID+"&coll_PKID="+strFormID+"&coll_FKID="+strOtherID+"&coll_MODE=2&coll_READ=2&PAID1="+strParentID+"&PAID2="+strParentID1+"&RID="+strRID;
		}
		strWindowTitle = "["+strFlowName+"]→["+strActivityName+"]";
		//前进保存数据
		strISSAVE1 = activity.getISSAVE1();
		//后退保存数据
		strISSAVE2 = activity.getISSAVE2();
		//前进必填留言
		strISLEAVE1 = activity.getISLEAVE1();
		//后退必填留言
		strISLEAVE2 = activity.getISLEAVE2();
		//活动处理前填写表单地址
		strADDFORMPATH = activity.getADDFORMPATH();
		if (strADDFORMPATH==null){strADDFORMPATH="";}
		//活动处理前填写表单宽
		strADDFORMWIDTH = activity.getADDFORMWIDTH();
		if (strADDFORMWIDTH==null){strADDFORMWIDTH="";}
		//活动处理前填写表单高
		strADDFORMHEIGHT = activity.getADDFORMHEIGHT();
		if (strADDFORMHEIGHT==null){strADDFORMHEIGHT="";}
		//活动处理前非正确填写表单提示
		strADDFORMMESSAGE = activity.getADDFORMMESSAGE();
		if (strADDFORMMESSAGE==null){strADDFORMMESSAGE="";}
		if (strADDFORMMESSAGE.trim().length()==0){
		  strADDFORMMESSAGE = "请先填写系统设置的表单!";
		}
		if (strADDFORMPATH.trim().length()>0){
		     if (strADDFORMPATH.lastIndexOf("?") > -1) {//已经带参数
		       strADDFORMPATH = strADDFORMPATH + "&RUNID="+strExecute_No+"&COLL_AID="+strCollActivityID+"&coll_PKID="+strFormID+"&coll_FKID="+strOtherID+"&PAID1="+strParentID+"&PAID2="+strParentID1;
		     }else{
		       strADDFORMPATH = strADDFORMPATH + "?RUNID="+strExecute_No+"&COLL_AID="+strCollActivityID+"&coll_PKID="+strFormID+"&coll_FKID="+strOtherID+"&PAID1="+strParentID+"&PAID2="+strParentID1;
		     }
		}
		} catch (Exception ex1) {
		}
		 ButtonList3 = flowControl.getCreateButton(strExecute_No,showuserid,"2","1");//得到流程创建人的按钮
		 ButtonList5 = flowControl.getPublicButton("2","1");//得到公共类型的按钮
		 
		 modelMap.put("Unit",showuserunit);
		 modelMap.put("Identification",strIdentification);
		 modelMap.put("Workflow_No",strWorkflow_No);
		 modelMap.put("RID",strRID);
		 modelMap.put("Node_No_S",StrNode_No_S);
		 modelMap.put("M_Node_No_E",strM_Node_No_E);
		 modelMap.put("M_Node_No_S_E",strM_Node_No_S_E);
		 modelMap.put("S_Node_No_E",strS_Node_No_E);
		 modelMap.put("S_Node_No_S_E",strS_Node_No_S_E);
		 modelMap.put("UserNo",showuserid);
		 modelMap.put("Execute_No",strExecute_No);
		 modelMap.put("ParentID",strParentID);
		 modelMap.put("ParentID1",strParentID1);
		 modelMap.put("FormID",strFormID);
		 modelMap.put("TitleName",strTitleName);
		 modelMap.put("OtherID",strOtherID);
		 modelMap.put("NeedNewFlow",strNeedNewFlow);
		 modelMap.put("AttMessage",AttMessage);
		 modelMap.put("FlowFormPath",strFlowFormPath);
		 modelMap.put("FlowFormPath1",strFlowFormPath1);
		 modelMap.put("ISSAVE1",strISSAVE1);
		 modelMap.put("ISSAVE2",strISSAVE2);
		 modelMap.put("ISLEAVE1",strISLEAVE1);
		 modelMap.put("ISLEAVE2",strISLEAVE2);
		 modelMap.put("ADDFORMPATH",strADDFORMPATH);
		 modelMap.put("ADDFORMWIDTH",strADDFORMWIDTH);
		 modelMap.put("ADDFORMHEIGHT",strADDFORMHEIGHT);
		 modelMap.put("ADDFORMMESSAGE",strADDFORMMESSAGE);
		 modelMap.put("RECORDID",strRECORDID);
		 modelMap.put("ATTNUM",String.valueOf(Attnum1));
		 modelMap.put("ATTTYPE",AttType);
		 modelMap.put("WEBPATH",strWEBPATH);
		 modelMap.put("FlowDoLog",strFlowDoLog);
		 modelMap.put("FlowActivityLink",strFlowActivityLink);
		 modelMap.put("FlowLink",strFlowLink);
		 modelMap.put("WindowTitle",strWindowTitle);
		 modelMap.put("CollActivityID",strCollActivityID);
		 modelMap.put("ButtonList3",ButtonList3);
		 modelMap.put("ButtonList5",ButtonList5);
		 modelMap.put("ImagePath",ImagePath);

		 return "ZrWorkFlow/FlowRun/FlowRunWindow1";
	}

	@RequestMapping("/openphoneflow")
	public String openPhoneFlow(ModelMap modelMap, 
			HttpServletRequest request, HttpServletResponse response) throws Exception {
	      response.setContentType("text/html; charset=UTF-8");
	      HttpSession session = request.getSession(true);
	      SessionUser user = (SessionUser)session.getAttribute("userinfo");

	      String strWEBPATH = "";
	      strWEBPATH = SysPreperty.getProperty().AppUrl;
	      String ImagePath = "";
	      FLOW_CONFIG_PROCESS Process = null;
	      FLOW_CONFIG_ACTIVITY activity = null;

	      String strExecute_No=""; //流转ID
	      String strWorkflow_No=""; //流程ID
	      String strFlowLink="";    //关联流程
	      String strFlowActivityLink="";//流程步骤示例
	      String strFlowName="";    //流程名称
	      String StrNode_No_S="";   //当前节点编号
	      String strM_Node_No_E=""; //下一节点
	      String strM_Node_No_S_E="";//下一节点选择后
	      String strS_Node_No_E=""; //上一节点
	      String strS_Node_No_S_E=""; //选择后的上一节点
	      String strFormID="";      //表单ID
	      String strTitleName="";   //标题名称
	      String strOtherID="";     //流程的关联值
	      String strParentID="";    //父ID
	      String strParentID1="";    //父ID1
	      String strRID ="";         //关联流程
	      String strActivityName="";//当前步骤的名称
	      String strActivityType="";//当前步骤的类型
	      String strFlowDoLog="";   //流程的处理记录
	      String strIdentification = "";//流程标识

	      String strCollType="";    //采集引擎调用类型,1全部是只读形式显示，否则按流程设置的显示
	      String strisNew="0";      //是否是新建流程,1是，0否
	      boolean isDoFlow = false; //当前用户是否拥有处理权限
	      String strCollActivityID="";//调用采集引擎的活动ID
	      String strFristActivityID="";//第一个的活动ID
	      String strNeedNewFlow="";//必须新建但还没有新建的流程名称
	      String strtype = "";//流程调用特殊类型主要用于刷新父级页面
	      String strISwhere = "0";//与当前步骤相关的线上是否设置有和字段值相关的条件

	      String AttType = "4";     //附件类型
	      String AttMessage = "";   //附件提示
	      int Attnum1 =0;           //需上传的附件数
	      String strFormPath ="";//流程自定义表单页面的路径
	      String strISSAVE ="0"; //是否保存过
	      String strTYPE ="";    //类别 1流程管理
	      String strISBRANCH="";//多路分支是否在操作按钮上实现

	      //得到流程标识
	      strIdentification = request.getParameter("FlowNo");
	      if (strIdentification==null){strIdentification="";}
	      strWorkflow_No = strIdentification;
	      strExecute_No = request.getParameter("ExecuteNo");
	      if (strExecute_No==null){strExecute_No="";}
	      strFormID = request.getParameter("FormID");
	      if (strFormID==null){strFormID="";}
	      strOtherID = request.getParameter("OtherID");
	      if (strOtherID==null){strOtherID="";}
	      strParentID = request.getParameter("ParentID");
	      if (strParentID==null){strParentID="";}
	      strParentID1 = request.getParameter("ParentID1");
	      if (strParentID1==null){strParentID1="";}
	      strRID = request.getParameter("RID");
	      if (strRID==null){strRID="";}
	      strISSAVE = request.getParameter("ISSAVE");
	      if (strISSAVE==null){strISSAVE="";}
	      strTYPE = request.getParameter("TYPE");
	      if (strTYPE==null){strTYPE="";}
	      strtype = request.getParameter("type");
	      if (strtype==null){strtype ="";}
	      strISBRANCH=request.getParameter("ISBRANCH");
	      if(strISBRANCH==null) {
	        strISBRANCH="";
	      }
	      //接收分送阅读时的活动id
	      String strsendid = request.getParameter("sendid");
	      if (strsendid==null){strsendid ="";}
	      //--------------
	      String strRECORDID = "";
	      String strTakeBack = "0";
	      String strFlowFormPath = "";
	      String strWindowTitle = "";
	      String strISSAVE1 = "";
	      //后退保存数据
	      String strISSAVE2 = "";
	      //前进必填留言
	      String strISLEAVE1 = "";
	      //后退必填留言
	      String strISLEAVE2 = "";
	      //活动处理前填写表单地址
	      String strADDFORMPATH = "";
	      String strADDFORMWIDTH = "";
	      String strADDFORMHEIGHT = "";
	      String strADDFORMMESSAGE = "";
	      String showidea = "";
	      ActFlowRun ActFlowRun = null;
	      String showuserid = "";
	      String showuserunit = "";
	      String StrFlowState = "";

	      try {
	      try {
	         showuserid = user.getUserID();
	      } catch (Exception ex2) {
	         user = (SessionUser)session.getAttribute("userinfo");
	         showuserid = user.getUserID();
	      }
	      showuserunit = user.getUnitID();
	      ImagePath = SysPreperty.getProperty().AppUrl+"Zrsysmanage/images/blueimg/";//图片路径
	      
	      if (strExecute_No.length()==0) {//生成文件编号
	           strRECORDID = flowControl.globalID(showuserid);
	      }
	      //得到标题------------------
	      if (strExecute_No.length()>0)
	      {strTitleName = flowControl.getTitle(strExecute_No);}

	      //初始化流程默认运转参数------------------
	      ActFlowRun = new ActFlowRun();

	      //-------------------------------------------
	      if (strExecute_No!=null && strExecute_No.length()>0){
	         FLOW_RUNTIME_PROCESS Fprocess = null;
	         Fprocess = flowControl.getFlowRuntimeprocess(strExecute_No);
	         StrFlowState = Fprocess.getSTATE();
	         //根据流程流转表的ID得到流程标识
	         strWorkflow_No = Fprocess.getFLOWID();
	         //根据流程流转表的ID得到流程当前步骤
	         StrNode_No_S = Fprocess.getCURRACTIVITY();

	         //是分送阅读时-------------
	        if (strsendid.length()>0)
	        {StrNode_No_S = strsendid;}
	        //-----------------------------
	         strRECORDID = Fprocess.getRECORDID();//得到附件ID
	         //根据流程标识得到流程ID
	         strWorkflow_No = flowControl.getFlowID(strWorkflow_No);
	        }else{
	         //根据流程标识得到流程ID
	         strWorkflow_No = flowControl.getFlowID(strWorkflow_No);
	         //根据流程ID得到当前流程第一个活动(步骤)的ID
	         StrNode_No_S = flowControl.getFirstActivityID(strWorkflow_No);
	         strisNew="1";//是新建流程
	      }
	      if (StrNode_No_S.length()==0 || StrNode_No_S==null){
	        //第一个步骤编号
	        strFristActivityID = flowControl.getFirstActivityID(strWorkflow_No);
	        StrNode_No_S = strFristActivityID;
	      }
	      //根据流程ID得到流程实体
	      Process = flowControl.getFlowProcess(strWorkflow_No);
	      //根据流程编号得到流程名称
	      strFlowName = Process.getNAME();
	      //根据活动ID得到活动实体
	      activity = flowControl.getFlowActivity(StrNode_No_S);
	      strISBRANCH=activity.getISBRANCH();
	      ActFlowRun.FlowInitDefault(strExecute_No,strIdentification,strOtherID,strParentID,strParentID1,strRID,strRECORDID,strISBRANCH);

	      //得到活动的名称
	      strActivityName =activity.getNAME();
	      //根据当前活动的编号得到下一步的分支活动
	      strM_Node_No_E = flowControl.getNextActivityID(StrNode_No_S,ActFlowRun,user);
	      //根据当前活动及下一步的分支活动串得到包含接收类型的活动串
	      strM_Node_No_S_E = flowControl.getNextActivityTypeList(StrNode_No_S,strM_Node_No_E);
	      //根据当前活动的编号得到上一步的分支活动
	      strS_Node_No_E = flowControl.getUpSelectActivityID(StrNode_No_S,ActFlowRun,user);
	      //根据当前活动及上一步的分支活动串得到包含接收类型的活动串
	      strS_Node_No_S_E = flowControl.getNextActivityTypeList(StrNode_No_S,strS_Node_No_E);
	      strISwhere = flowControl.getISwhere(StrNode_No_S);

	      //得到当前流程相关的可操作流程
	      if (strExecute_No!=null && strExecute_No.length()>0){
	         //得到关联流程的标识串
	         strFlowLink = flowControl.getNextIFlowID(StrNode_No_S,ActFlowRun,user);
	         //得到关联的但还没有新建的关联流程名
	         if (strFlowLink.length()>0)
	         {strNeedNewFlow = flowControl.getNeedFlowList(strFlowLink,StrNode_No_S,ActFlowRun);
	         //初始化流程默认运转参数------------------
	         ActFlowRun.FlowInitDefault(strExecute_No,strIdentification,strOtherID,strParentID,strParentID1,strExecute_No,strRECORDID,strISBRANCH);
	         strFlowLink = flowControl.getFlowRunIDHTML(strFlowLink,"FlowRunWindow.html","table_title4.gif",ActFlowRun,user);
	         //初始化流程默认运转参数------------------
	         ActFlowRun.FlowInitDefault(strExecute_No,strIdentification,strOtherID,strParentID,strParentID1,strRID,strRECORDID,strISBRANCH);
	         }
	      }
	      //得到流程跟踪记录
	      if (strExecute_No.length()>0)
	      {strFlowDoLog = flowControl.getFlowDoLog(strExecute_No);}
	      //得到当前步骤的类型
	      strActivityType= activity.getTYPE();
	      //得到当前用户是否拥有处理权限
	      if (strExecute_No.length()>0){
	         isDoFlow = flowControl.getIsFlowDoList(strExecute_No,user);
	      }
	      //得到调用采集引擎是否全为只读的类型
	      if ((strActivityType.equals("2") || !isDoFlow) && strisNew.equals("0")){
	        //strCollType="1";
	        if (isDoFlow==false && strExecute_No.length()>0)
	        {strCollType="1";}
	        //没有权限或完成的流程显示按最后一步显示
	        strCollActivityID = flowControl.getLastlyActivityID(strWorkflow_No);//将用最后一步的流程属性来显示采集引擎
	      }else{
	       strCollActivityID =StrNode_No_S;
	      }
	      //得到流程处理图示
	      strFlowActivityLink=flowControl.getFlowActivityLink(strWorkflow_No,StrNode_No_S,"../FlowShow/FlowShowMap/ShowActivity.html");
	      //根据活动ID得到活动实体
	      activity = flowControl.getFlowActivity(strCollActivityID);
	      //得到当前活动的附件类型
	      AttType = activity.getATTTYPE();
	      Attnum1 = activity.getATTNUM();
	      //多路分支按钮是否在操作按钮上
	      strISBRANCH=activity.getISBRANCH();
	      //得到当前活动的自定义表单链接路径
	      strFormPath = activity.getFORMPATH();
	      if (strFormPath==null){strFormPath="";}
	      if (strFormPath.length()>0) {//为自定义表单形式
	         if (strFormPath.length()>0) {
	           if (strFormPath.lastIndexOf("?") > -1) {//已经带参数
	             strFormPath = strFormPath + "&RUNID="+strExecute_No+"&COLL_AID="+strCollActivityID+"&coll_PKID="+strFormID+"&coll_FKID="+strOtherID+"&PAID1="+strParentID+"&PAID2="+strParentID1;
	           } else {
	             strFormPath = strFormPath + "?RUNID="+strExecute_No+"&COLL_AID="+strCollActivityID+"&coll_PKID="+strFormID+"&coll_FKID="+strOtherID+"&PAID1="+strParentID+"&PAID2="+strParentID1;
	           }
	         } else {
	           strFormPath ="flowmessage.html";
	         }
	      }
	      //流程数据显示完整路径
	      if (isDoFlow==false && strExecute_No.length()>0) {
	         strCollType = "1";
	      }
	      if (strFormPath.length()>0) {//为自定义表单形式
	         strFlowFormPath = strFormPath;
	      } else {
	         strFlowFormPath = "/collect/collenginephone?COLL_AID="+strCollActivityID+"&IfPho=1&coll_PKID="+strFormID+"&coll_FKID="+strOtherID+"&coll_MODE=2&coll_READ="+strCollType+"&PAID1="+strParentID+"&PAID2="+strParentID1+"&RID="+strRID;
	      }
	      strWindowTitle = "["+strFlowName+"]→["+strActivityName+"]";
	      //得到是否显示收回按钮(1是0否)
	      if (isDoFlow==false) {
	        strTakeBack = flowControl.getIsTakeBack(strExecute_No,showuserid);
	      }
	      //前进保存数据
	      strISSAVE1 = activity.getISSAVE1();
	      //后退保存数据
	      strISSAVE2 = activity.getISSAVE2();
	      //前进必填留言
	      strISLEAVE1 = activity.getISLEAVE1();
	      //后退必填留言
	      strISLEAVE2 = activity.getISLEAVE2();
	      //活动处理前填写表单地址
	      strADDFORMPATH = activity.getADDFORMPATH();

	      if (strADDFORMPATH==null){strADDFORMPATH="";}
	      //活动处理前填写表单宽
	      strADDFORMWIDTH = activity.getADDFORMWIDTH();
	      if (strADDFORMWIDTH==null){strADDFORMWIDTH="";}
	      //活动处理前填写表单高
	      strADDFORMHEIGHT = activity.getADDFORMHEIGHT();
	      if (strADDFORMHEIGHT==null){strADDFORMHEIGHT="";}
	      //活动处理前非正确填写表单提示
	      strADDFORMMESSAGE = activity.getADDFORMMESSAGE();
	      if (strADDFORMMESSAGE==null){strADDFORMMESSAGE="";}
	      if (strADDFORMMESSAGE.trim().length()==0){
	        strADDFORMMESSAGE = "请先填写系统设置的表单!";
	      }
	      showidea = "";
	      if (isDoFlow==false && strExecute_No.length()>0) {
	    	  showidea = "none";
	      }
	      if (strADDFORMPATH.trim().length()>0) {
	           if (strADDFORMPATH.lastIndexOf("?") > -1) {//已经带参数
	             strADDFORMPATH = strADDFORMPATH + "&RUNID="+strExecute_No+"&COLL_AID="+strCollActivityID+"&coll_PKID="+strFormID+"&coll_FKID="+strOtherID+"&PAID1="+strParentID+"&PAID2="+strParentID1;
	           } else {
	             strADDFORMPATH = strADDFORMPATH + "?RUNID="+strExecute_No+"&COLL_AID="+strCollActivityID+"&coll_PKID="+strFormID+"&coll_FKID="+strOtherID+"&PAID1="+strParentID+"&PAID2="+strParentID1;
	           }
	      }
	      } catch (Exception ex1) {
	      }
	      String ID = request.getParameter("ID");
	      FLOW_CONFIG_ACTIVITY entityActivity=null;
	      if (ID.trim().length() > 0) {
	         entityActivity = flowManageAct.getActivityById(ID);
	      }
	     String buttonlist="";
	     Button[] buttons2;
	     buttons2 = flowManageAct.getButtonListByFid(entityActivity.getID());
	     if (buttons2 != null){
	         for (int i = 0; i < buttons2.length; i++) {
	            buttonlist= buttonlist + "<div class=\"am-modal-actions-group\">\r\n";
	            buttonlist= buttonlist + "<ul class=\"am-list\">\r\n";
	            buttonlist= buttonlist + "<li class=\"am-modal-actions-header\"><a href=\"javascript:"+buttons2[i].getPROPERTY()+"\" class=\"am-btn am-btn-primary am-radius\">"+buttons2[i].getBNAME()+"</a></li>\r\n";
	            buttonlist= buttonlist + "</ul>\r\n</div>\r\n";
	         }
	      }
	      String strNodeLst ="";
	      strNodeLst = flowManageService.getNextActivityList(StrNode_No_S,strM_Node_No_E,"record","flowselect.gif");
	      strNodeLst=strNodeLst.replace("<table border='0'","<!--");
	      strNodeLst=strNodeLst.replace("<input type='radio'","--><input type='radio'");
	      strNodeLst=strNodeLst.replace("<img","<!--");
	      strNodeLst=strNodeLst.replace("align='absmiddle'>","align='absmiddle'>-->");
	      strNodeLst=strNodeLst.replace("</td></tr></table>","<!--</td></tr></table>-->");

	      modelMap.put("Unit",showuserunit);
	      modelMap.put("Identification",strIdentification);
	      modelMap.put("Workflow_No",strWorkflow_No);
	      modelMap.put("RID",strRID);
	      modelMap.put("Node_No_S",StrNode_No_S);
	      modelMap.put("M_Node_No_E",strM_Node_No_E);
	      modelMap.put("M_Node_No_S_E",strM_Node_No_S_E);
	      modelMap.put("S_Node_No_E",strS_Node_No_E);
	      modelMap.put("S_Node_No_S_E",strS_Node_No_S_E);
	      modelMap.put("UserNo",showuserid);
	      modelMap.put("Execute_No",strExecute_No);
	      modelMap.put("ParentID",strParentID);
	      modelMap.put("ParentID1",strParentID1);
	      modelMap.put("FormID",strFormID);
	      modelMap.put("TitleName",strTitleName);
	      modelMap.put("OtherID",strOtherID);
	      modelMap.put("NeedNewFlow",strNeedNewFlow);
	      modelMap.put("AttMessage",AttMessage);
	      modelMap.put("FlowFormPath",strFlowFormPath);
	      modelMap.put("ISSAVE1",strISSAVE1);
	      modelMap.put("ISSAVE2",strISSAVE2);
	      modelMap.put("ISLEAVE1",strISLEAVE1);
	      modelMap.put("ISLEAVE2",strISLEAVE2);
	      modelMap.put("ADDFORMPATH",strADDFORMPATH);
	      modelMap.put("ADDFORMWIDTH",strADDFORMWIDTH);
	      modelMap.put("ADDFORMHEIGHT",strADDFORMHEIGHT);
	      modelMap.put("ADDFORMMESSAGE",strADDFORMMESSAGE);
	      modelMap.put("RECORDID",strRECORDID);
	      modelMap.put("ATTNUM",String.valueOf(Attnum1));
	      modelMap.put("ATTTYPE",AttType);
	      modelMap.put("ISBRANCH",strISBRANCH);
	      modelMap.put("ISWHERE",strISwhere);
	      modelMap.put("WEBPATH",strWEBPATH);
	      modelMap.put("FlowDoLog",strFlowDoLog);
	      modelMap.put("FlowActivityLink",strFlowActivityLink);
	      modelMap.put("FlowLink",strFlowLink);
	      modelMap.put("WindowTitle",strWindowTitle);
	      modelMap.put("CollActivityID",strCollActivityID);
	      modelMap.put("showidea",showidea);
	      modelMap.put("TakeBack",strTakeBack);
	      modelMap.put("FlowState",StrFlowState);
	      modelMap.put("ImagePath",ImagePath);

	      modelMap.put("buttonlist",buttonlist);
	      modelMap.put("NodeLst",strNodeLst);

	    return "ZrPhoneEngine/ZrWork/FlowRunWindow";
	}

	/**
	 * 打开流程引擎配置
	 * @param request
	 * @param response
	 * @throws Exception 
	 */
	@RequestMapping("/openflowcfg")
	public String openFlowConfig(ModelMap modelMap, 
			HttpServletRequest request, HttpServletResponse response) throws Exception {
	      response.setContentType("text/html; charset=UTF-8");
	      String strAct = request.getParameter("Act");
	      HttpSession session=request.getSession(true);
	      SessionUser user = (SessionUser)session.getAttribute("userinfo");
	      String pagepath = "";
	      //打开处理流程----------------
	      if (strAct.equals("doflow")){
	        String path="";
	        String ID = request.getParameter("ID");
	        FLOW_RUNTIME_PROCESS frp = flowMonitorService.getPro(ID);
	        path=frp.getFLOWPATH();
	        //处理老数据(可以去掉)----------------
	        path=path.replaceAll("/ZrWorkFlow/FlowRun/FlowRunWindow.html","/flow/openflowrun");
	        //----------------------
	        modelMap.put("PATH", path);
	        
	        pagepath = "ZrWorkFlow/FlowMonitor/FlowToTurn/Edit";
	      }
	      //打开委托处理流程----
	      else if (strAct.equals("wtflow")){
	        String ID = request.getParameter("ID");
	        FLOW_RUNTIME_PROCESS frp = flowMonitorService.getPro(ID);

	        //根据活动ID得到活动实体
	        FLOW_CONFIG_ACTIVITY activity = null;
	        activity = flowControl.getFlowActivity(frp.getCURRACTIVITY());
	        modelMap.put("ID",ID);
	        modelMap.put("USERID",user.getUserID());
	        modelMap.put("UNITID",user.getUnitID());
	        modelMap.put("FLOWNAME",frp.getNAME());
	        modelMap.put("CNAME",activity.getNAME());
	        
	        pagepath = "ZrWorkFlow/FlowMonitor/FlowToTurn/WTEdit";
	      }
	      //打开委托选择人员----
	      else if (strAct.equals("wtsuser")){
	          String id=request.getParameter("id");//获取字段ID
	          String name=request.getParameter("name");//获取字段name
	          modelMap.put("UNITID",user.getUnitID());
	          modelMap.put("ID",id);
	          modelMap.put("NAME",name);
	          
	          pagepath = "ZrWorkFlow/FlowMonitor/FlowToTurn/user";
	      }
	      //打开移交处理流程----
	      else if (strAct.equals("yjflow")){
	        String ID = request.getParameter("ID");
	        FLOW_RUNTIME_PROCESS frp = flowMonitorService.getPro(ID);
	        //根据活动ID得到活动实体
	        FLOW_CONFIG_ACTIVITY activity = null;
	        activity = flowControl.getFlowActivity(frp.getCURRACTIVITY());

	        modelMap.put("ID",ID);
	        modelMap.put("USERID",user.getUserID());
	        modelMap.put("UNITID",user.getUnitID());
	        modelMap.put("FLOWNAME",frp.getNAME());
	        modelMap.put("CNAME",activity.getNAME());
	        
	        pagepath = "ZrWorkFlow/FlowMonitor/FlowToTurn/YJEdit";
	      }
	      //打开处理流程(流程管理方式)----
	      else if (strAct.equals("doMflow")){
	        String path="";
	        String ID = request.getParameter("ID");
	        FLOW_RUNTIME_PROCESS frp = flowMonitorService.getPro(ID);
	        path=frp.getFLOWPATH();
	        //处理老数据(可以去掉)-----
	        path=path.replaceAll("/ZrWorkFlow/FlowRun/FlowRunWindow.html","/flow/openflowrun");
	        //----------------------
	        path=path.replaceAll("/flow/openflowrun","/flow/openflowrun1");
	        path=path.replaceAll("FlowNo=","TYPE=1&FlowNo=");
	        modelMap.put("PATH",path);
	        
	        pagepath = "ZrWorkFlow/FlowMonitor/FlowToTurn/Edit";
	      }
	      //打开增加流程委托----
	      else if (strAct.equals("AddEntrust")){
	        String SDATE = flowMonitorService.getDateAsStr();
	        modelMap.put("USERID",user.getUserID());
	        modelMap.put("UNITID",user.getUnitID());
	        modelMap.put("SDATE",SDATE);
	        
	        pagepath = "ZrWorkFlow/FlowMonitor/FlowEntrust/AddFrm";
	      }
	      //打开编辑流程委托----
	      else if (strAct.equals("EditEntrust")){
	        String ID = request.getParameter("ID");
	        FLOW_CONFIG_ENTRUST  Entrust=flowMonitorService.getEntrustID(ID);
	        modelMap.put("ID",ID);
	        modelMap.put("USERID",user.getUserID());
	        modelMap.put("SDATE",DateWork.DateToString(Entrust.getSDATE()));
	        modelMap.put("EDATE",DateWork.DateToString(Entrust.getEDATE()));
	        modelMap.put("IUSER",flowMonitorService.getTrueName(Entrust.getIUSERNO()));
	        
	        pagepath = "ZrWorkFlow/FlowMonitor/FlowEntrust/EditFrm";
	      }
	      //打开待办工作列表----
	      else if (strAct.equals("showlist") || strAct.equals("showmorelist")){
	        String message = "";
	        String flowlist = "";
	        FLOW_RUNTIME_PROCESS[] flow = null;
	        //得到查询的记录集
	        if (strAct.equals("showlist")) {
	          flow = flowMonitorService.getFlowTransactList(user.getUserID(),"1");
	        }
	        else if (strAct.equals("showmorelist")) {
	          flow = flowMonitorService.getFlowTransactList(user.getUserID(),"2");
	        }
	        if (flow!=null && flow.length>0){
	             message = " <bgsound src='"+SysPreperty.getProperty().AppUrl+"Zrsysmanage/media/message.wav' loop='1'>";
	        }
	        String strPath= "";
	        String strName= "";
	        String strTime = "";
	        String strRID = "";
	        int itemNum=8;

	        if (flow!=null && flow.length>0){
	         if(itemNum>flow.length) {
	           itemNum=flow.length;
	         }
	         for(int i=0;i<itemNum;i++){
	          strPath=flow[i].getFLOWPATH().toString();
	          strName=flow[i].getNAME().toString();
	          strRID= flow[i].getRID().toString();
	          //处理老数据(可以去掉)-----
	          strPath=strPath.replaceAll("/ZrWorkFlow/FlowRun/FlowRunWindow.html","/flow/openflowrun");
	          //----------------------
	          if (strRID.equals("send")) {//是分送的情况
	             flowlist = flowlist +"<tr><td><img src='"+SysPreperty.getProperty().AppUrl+"Zrsysmanage/images/blueimg/file_ico.gif' border='0' align='absmiddle'>\r\n";
	             flowlist = flowlist +"<a href='#' class='f_black3 f_blue' onclick=OpenUrl('"+strPath+"','1') title=''>"+strName.replaceAll("→分送","→阅")+"</a></td></tr>\r\n";
	          } else {
	             strTime = String.valueOf(flow[i].getACCEPTDATE());
	             if (strTime.length()>10)
	             {strTime = strTime.substring(0,10);}
	              flowlist = flowlist +"<tr><td><img src='"+SysPreperty.getProperty().AppUrl+"Zrsysmanage/images/blueimg/file_ico.gif' border='0' align='absmiddle'>\r\n";
	              flowlist = flowlist +"<a href='#' class='f_black3 f_blue'  onclick=OpenUrl('"+strPath+"','1') title=''>["+strName+"]["+strTime+"]</a></td></tr>\r\n";
	          }
	        }
	      }else{
	           flowlist = "<tr><td><img src='"+SysPreperty.getProperty().AppUrl+"Zrsysmanage/images/blueimg/file_ico.gif' border='0' align='absmiddle'>提示：无待办流程!</td></tr>\r\n";
	      }
	      modelMap.put("MESSAGE",message);
	      modelMap.put("FLOWLIST",flowlist);
	      
	      pagepath = "ZrWorkFlow/FlowMonitor/FlowToTurn/ShowList";
	      }
	      //删除流程日志-----------------
	      else if (strAct.equals("DelDaily")){
	          String strID = request.getParameter("ID");
	          String isok=request.getParameter("isdel");
	          if(isok.equals("no")) {
	        	  flowMonitorService.delDailyList();
	          } else {
	        	  flowMonitorService.delDaily(strID);
	          }
	          pagepath = "ZrWorkFlow/FlowMonitor/FlowControl/DailyList";
	      }
	      //-----------------------------
	      //任务重分配管理-----------------
	      else if (strAct.equals("Allotment")){
	          String ID = request.getParameter("ID");
	          FLOW_RUNTIME_PROCESS frp = flowMonitorService.getPro(ID);
	          //根据活动ID得到活动实体
	          FLOW_CONFIG_ACTIVITY activity = null;
	          activity = flowControl.getFlowActivity(frp.getCURRACTIVITY());
	          modelMap.put("ID",ID);
	          modelMap.put("USERID",user.getUserID());
	          modelMap.put("UNITID",user.getUnitID());
	          modelMap.put("FLOWNAME",frp.getNAME());
	          modelMap.put("CNAME",activity.getNAME());
	          
	          pagepath = "ZrWorkFlow/FlowMonitor/FlowAllotment/Edit";
	      }
	      //---------------------------
	      //打开增加按钮-----------------
	      else if (strAct.equals("addbutton")){
	    	  modelMap.put("USERID",user.getUserID());
	          pagepath = "ZrWorkFlow/BaseSetup/FlowBaseButton/ButtonAdd";
	      }
	      //--------------------
	      //打开增加按钮-----------------
	      else if (strAct.equals("editbutton")){
	          String ID = request.getParameter("ID");
	          Button Button = flowSetupService.getButtonID(ID);
	          modelMap.put("ID",ID);
	          modelMap.put("UNITID",user.getUnitID());
	          modelMap.put("BUTTON",Button);

	          pagepath = "ZrWorkFlow/BaseSetup/FlowBaseButton/ButtonEdit";
	      }
	      //------------------------------
	      //打开流程意见选择-----------------
	      else if (strAct.equals("openflowidea")){
	    	  modelMap.put("USERID",user.getUserID());
	          pagepath = "ZrWorkFlow/FlowRun/WfIdeaList";
	      }
	      //打开新增意见-----------------
	      else if (strAct.equals("addflowidea")){
	    	  modelMap.put("USERID",user.getUserID());
	          pagepath = "ZrWorkFlow/FlowRun/IdeaAddFrm";
	      }
	      //打开选择流程步骤-----------------
	      else if (strAct.equals("selectflowactivity")){
	           String strNode_No_S ="";//当前活动编号
	           String strNode_No_E ="";//可供选择的活动编号
	           String strNodeLst ="";

	           strNode_No_S = request.getParameter("Node_No_S");
	           strNode_No_E = request.getParameter("Node_No_E");
	           String strtype = request.getParameter("type");
	           if (strtype==null){strtype="";}
	           strNodeLst = flowManageService.getNextActivityList(strNode_No_S,strNode_No_E,"record","flowselect.gif");
	           modelMap.put("type",strtype);
	           modelMap.put("NodeLst",strNodeLst);
	           
	           pagepath = "ZrWorkFlow/FlowRun/WfActivityList";
	      }
	      //打开流程附件检测-----------------
	      else if (strAct.equals("Attcheck")){
	           String strCNodeNo = request.getParameter("CNodeNo");
	           String strNodeNo = request.getParameter("NodeNo");
	           //根据当前活动和下一活动得到是否需要附件提示
	           String IsAttMessage = flowControl.getISAttMessage(strCNodeNo,strNodeNo);
	           modelMap.put("AttMessage",IsAttMessage);
	           
	           pagepath = "ZrWorkFlow/FlowRun/Attcheck";
	      }
	      //流程选择单位时得到单位信息--------
	      else if (strAct.equals("getunitmain")){
	        String strUnitid = request.getParameter("nodeId");
	        BPIP_UNIT uInfo =  unitService.getUnit(strUnitid);
	        String strDeptInside ="";
	        String strDeptInsideName="";
	        //得到选择单位下的部门内勤用户编号串
	        strDeptInside = flowControl.getDeptInside(strUnitid);
	        //得到选择单位下的部门内勤用户名称串
	        strDeptInsideName = flowControl.getDeptInsideName(strUnitid);
	        if (strDeptInsideName.length()==0){
	           strDeptInside = "0";
	           strDeptInsideName = "没有设置内勤,联系管理员设置!";
	        }
	        JSONObject json=new JSONObject();
	        if(uInfo!=null){
	           json.put("DeptInside",strDeptInside);
	           json.put("UNITNAME",uInfo.getUNITNAME());
	           json.put("DeptInsideName",strDeptInsideName);
	        }else{
	           json.put("DeptInside","0");
	           json.put("UNITNAME","请选择单位或部门");
	           json.put("DeptInsideName","0");
	        }
	        response.getWriter().println(json);
	        response.getWriter().flush();
	      }
	      //-----------------------
	      //流程选择单位-------------------
	      else if (strAct.equals("selectunit")){
	          String strtype = request.getParameter("type");
	          if (strtype==null){strtype="1";}
	          modelMap.put("TYPE",strtype);
	          
	          pagepath = "ZrWorkFlow/FlowRun/getunit";
	      }
	      //------------------------------
	      //得到流程选择人员----------------
	      else if (strAct.equals("selectuser")){
	           //得到当前登录用户所在的单位id
	           String strUNITID =user.getUnitID();
	           String strtype=request.getParameter("type");//获取人员选择类别
	           String nodeno=request.getParameter("nodeno");//获取流程步骤id
	           FLOW_CONFIG_ACTIVITY activity = null;
	           activity = flowControl.getFlowActivity(nodeno);
	           //得到活动的分配策略(1分配一人 2分配多人)
	           String strAstrategy = activity.getASTRATEGY();
	           String strAs= "true";
	           if (strAstrategy.equals("1")){
	              strAs= "true";
	           }else{
	              strAs= "false";
	           }
	           Calendar cal = Calendar.getInstance();
	           SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	           String mDateTime = formatter.format(cal.getTime());
	           modelMap.put("TYPE",strtype);
	           modelMap.put("UNITID",strUNITID);
	           modelMap.put("AS",strAs);
	           modelMap.put("mDateTime",mDateTime);
	           
	           pagepath = "ZrWorkFlow/FlowRun/getpsn";
	      }
	      //--------------------------------
	      //得到流程选择人员列表----------------
	      else if (strAct.equals("selectuserlist")){
	           String strCNodeNo;
	           String strExecuteNo;
	           String strNodeNo;
	           String strUserLst="";
	           String strAstrategy="";
	           String strName="";
	           String str_OtherID="";
	           String str_ParentID ="";
	           String str_ParentID1 ="";
	           String strIsNote = "";//短信提醒类别
	           String SELECTDEPTID = "";

	           //多路分支是否表现在操作按钮上
	           String strISBRANCH="";
	           strCNodeNo = request.getParameter("CNodeNo");
	           if (strCNodeNo==null){strCNodeNo="";}
	           strExecuteNo = request.getParameter("ExecuteNo");
	           if (strExecuteNo==null){strExecuteNo="";}
	           strNodeNo = request.getParameter("NodeNo");
	           if (strNodeNo==null){strNodeNo="";}

	           str_OtherID = request.getParameter("OtherID");
	           if (str_OtherID==null){str_OtherID="";}
	           str_ParentID = request.getParameter("ParentID");
	           if (str_ParentID==null){str_ParentID="";}
	           str_ParentID1 = request.getParameter("ParentID1");
	           if (str_ParentID1==null){str_ParentID1="";}
	           SELECTDEPTID = request.getParameter("SELECTDEPTID");
	           if (SELECTDEPTID==null){SELECTDEPTID="";}
	           String strtype = request.getParameter("type");
	           if (strtype==null){strtype="";}

	           FLOW_CONFIG_ACTIVITY activity = null;
	           activity = flowControl.getFlowActivity(strNodeNo);
	           if (activity!=null) {
	              //得到活动的分配策略(1分配一人 2分配多人)
	              strAstrategy = activity.getASTRATEGY();
	              //得到选择步骤的名称
	              strName = activity.getNAME();
	              //得到是否短信提示的状态
	              strIsNote=activity.getISNOTE();
	              strISBRANCH=activity.getISBRANCH();
	           }
	           //初始化流程默认运转参数------------------
	           ActFlowRun ActFlowRun = new ActFlowRun();
	           ActFlowRun.FlowInitDefault(strExecuteNo,"",str_OtherID,str_ParentID,str_ParentID1,"","",strISBRANCH);
	           //初始化默认流程运转参数结束---------------
	           strUserLst = flowControl.getActivityDoUserList(strExecuteNo,strCNodeNo,strNodeNo,ActFlowRun,user);

	           //根据用户编号字符串得到用户列表
	           strUserLst = flowManageService.getShowUserList(strUserLst,"record","flowuser.gif",strAstrategy,strNodeNo,SELECTDEPTID);

	           //根据指定的角色得到用户列表(按角色分组),备用接口，动态指定角色时使用。
	           //strUserLst = flowmanage.GetRoleUserList("record","flowuser.gif",strAstrategy,strNodeNo,"301,302,303,304");

	           modelMap.put("TYPE",strtype);
	           modelMap.put("Astrategy",strAstrategy);
	           modelMap.put("IsNote",strIsNote);
	           modelMap.put("Name",strName);
	           modelMap.put("UserLst",strUserLst);

	           pagepath = "ZrWorkFlow/FlowRun/WfUserList";
	      }
	      //得到送部门承办人人员列表----------------
	      else if (strAct.equals("selectdeptpsn")){
	           String strUnitid = user.getUnitID();
	           String strUSERID = user.getUserID();
	           String strUserList = flowControl.getCUnitUserList(strUnitid,strUSERID,"record","flowuser.gif","2");
	           modelMap.put("UserLst",strUserList);
	           
	           pagepath = "ZrWorkFlow/FlowRun/getDeptPsn";
	      }
	      //得到分送人员列表----------------
	      else if (strAct.equals("dosendpsn")){
	           String roles = request.getParameter("roles");//角色编号串
	           String sunitid = request.getParameter("sunitid");//左包含的单位编码位数
	           if (sunitid==null || sunitid.length()==0)
	           {sunitid = "";}
	           String isdept = request.getParameter("isdept");//是否本部门的人,1是，0否
	           String isSMS = request.getParameter("issms");//是否发送短信,1是，0否
	           if (isSMS==null){isSMS="0";}
	           String showsid = request.getParameter("showsid");
	           if (showsid==null){showsid="0";}
	           Calendar cal = Calendar.getInstance();
	           SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	           String mDateTime = formatter.format(cal.getTime());
	           String TitleName = "";
	           String username = user.getName();
	           TitleName = request.getParameter("TitleName");//标题
	           if (TitleName==null){TitleName="";}
	           TitleName = "["+TitleName+"→分送]["+username+"]["+mDateTime+"]";
	           String strUnitid = user.getUnitID();
	           if (sunitid.length()>0)
	           {sunitid = strUnitid.substring(0,Integer.parseInt(sunitid));}
	           String strUserList = flowControl.getShowRolesUserList(roles,"record","flowuser.gif","2",strUnitid,sunitid,isdept);
	           if (isSMS.equals("1")){
	              strUserList = "短信提示：<input type='text' id='smsvalue' name='smsvalue' size='42' value='"+TitleName+"'><br>"+strUserList;
	           }
	           modelMap.put("showsid",showsid);
	           modelMap.put("isSMS",isSMS);
	           modelMap.put("UserLst",strUserList);
	           
	           pagepath = "ZrWorkFlow/FlowRun/getSendPsn";
	      }
	      //执行流程分送操作----------------
	      else if (strAct.equals("doflowsend")){
	           String users = request.getParameter("users");//用户编号串
	           String cid = request.getParameter("cid");//当前活动id
	           String sid = request.getParameter("sid");//分送显示的活动id

	           String ExecuteNo = request.getParameter("ExecuteNo");//当前流程流转id
	           String smsvalue = request.getParameter("smsvalue");//发送短信内容
	           if (smsvalue==null){smsvalue="";}
	           String username = user.getName();
	           String TitleName = "";//流程标题
	           TitleName= flowControl.getTitle(ExecuteNo);
	           //执行流程发送流程---
	           flowControl.dosend(ExecuteNo,sid,cid,TitleName,users+"/"+smsvalue,user.getUserID(),username);
	           
	           pagepath = "ZrWorkFlow/FlowRun/SendPsn";
	      }
	      //多人提交同时处理----------------
	      else if (strAct.equals("IsShow")){
	           String ExecuteNo = request.getParameter("ExecuteNo");//当前流程流转id
	           String cid = request.getParameter("cid");//当前活动id
	           String type = request.getParameter("type");//调用类别
	           String svalue="0";
	           //执行流程发送流程---
	           boolean rvalue = flowControl.getIsShow(ExecuteNo,cid);
	           if (rvalue){svalue="1";}
	           modelMap.put("type",type);
	           modelMap.put("svalue",svalue);
	           
	           pagepath = "ZrWorkFlow/FlowRun/GetIsShow";
	      }
	      //线上的条件判断------------------
	      else if (strAct.equals("iswhere")){
	          String strCID = request.getParameter("CID");
	          if (strCID==null){strCID="";}
	          String strExecute_No = request.getParameter("Execute_No");
	          if (strExecute_No==null){strExecute_No="";}
	          String strIdentification = request.getParameter("Identification");
	          if (strIdentification==null){strIdentification="";}
	          String strOtherID = request.getParameter("OtherID");
	          if (strOtherID==null){strOtherID="";}
	          String strParentID = request.getParameter("ParentID");
	          if (strParentID==null){strParentID="";}
	          String strParentID1 = request.getParameter("ParentID1");
	          if (strParentID1==null){strParentID1="";}
	          String strRID = request.getParameter("RID");
	          if (strRID==null){strRID="";}
	          String strRECORDID = request.getParameter("RECORDID");
	          if (strRECORDID==null){strRECORDID="";}
	          String strISBRANCH = request.getParameter("ISBRANCH");
	          if (strISBRANCH==null){strISBRANCH="";}
	          ActFlowRun ActFlowRun = null;
	          ActFlowRun = new ActFlowRun();
	          ActFlowRun.FlowInitDefault(strExecute_No,strIdentification,strOtherID,strParentID,strParentID1,strRID,strRECORDID,strISBRANCH);

	         //根据当前活动的编号得到下一步的分支活动
	         String strM_Node_No_E = flowControl.getNextActivityID(strCID,ActFlowRun,user);
	         //根据当前活动及下一步的分支活动串得到包含接收类型的活动串
	         String strM_Node_No_S_E = flowControl.getNextActivityTypeList(strCID,strM_Node_No_E);
	         //根据当前活动的编号得到上一步的分支活动
	         String strS_Node_No_E = flowControl.getUpSelectActivityID(strCID,ActFlowRun,user);
	         //根据当前活动及上一步的分支活动串得到包含接收类型的活动串
	         String strS_Node_No_S_E = flowControl.getNextActivityTypeList(strCID,strS_Node_No_E);

	         modelMap.put("s1",strM_Node_No_E);
	         modelMap.put("s2",strM_Node_No_S_E);
	         modelMap.put("s3",strS_Node_No_E);
	         modelMap.put("s4",strS_Node_No_S_E);

	         pagepath = "ZrWorkFlow/FlowRun/getiswhere";
	      }
	      //------------------------------
	      //打开流程移交-------------  -----
	      else if (strAct.equals("Devolve")){
	         String strUnitid = "_";
	         String strUserList = flowControl.getShowUnitUserList(strUnitid,"record","flowuser.gif","1");
	         modelMap.put("UserList",strUserList);
	         
	         pagepath = "ZrWorkFlow/FlowRun/DevolveMain";
	      }
	      //------------------------------------
	      //得到流程在手机上的列表------------------
	      else if (strAct.equals("phoneflowlist")){
	         FLOW_RUNTIME_PROCESS[] flow = null;
	         String DataBaseType = SysPreperty.getProperty().DataBaseType;//数据库类型
	         String currentPage = request.getParameter("page");
	         if(currentPage == null){currentPage = "1";}
	         String FDATE1=request.getParameter("FDATE1");
	         String FDATE2=request.getParameter("FDATE2");
	         String strw2 = " 1=1 ";
	         if(request.getParameter("FDATE1")!=null && !request.getParameter("FDATE1").equals("")){
                if (request.getParameter("FDATE2")!=null && !request.getParameter("FDATE2").equals("")) {
                     if (DataBaseType.equals("1")){
                         strw2 = strw2 +" and B.CREATEDATE >= to_date('"+FDATE1+" 00:00:00','yyyy-mm-dd hh24:mi:ss') AND B.CREATEDATE <= to_date('"+FDATE2+" 23:59:59','yyyy-mm-dd hh24:mi:ss')";
                     }
                     else if (DataBaseType.equals("2")){
                         strw2 = strw2 +" and B.CREATEDATE >= '"+FDATE1+" 00:00:00' AND B.CREATEDATE <= '"+FDATE2+" 23:59:59'";
                     }
                     else if (DataBaseType.equals("3")) {
                    	 strw2 = strw2 +" and B.CREATEDATE >= '"+FDATE1+" 00:00:00' AND B.CREATEDATE <= '"+FDATE2+" 23:59:59'";
                     }
                  }
	          }
	         String list = "";
	         //得到查询的记录集
	          flow = flowMonitorService.getFlowTransactList2(user.getUserID(),Integer.parseInt(currentPage), 20, strw2);
	          if (flow != null && flow.length > 0) {
	            for (int i = 0; i < flow.length; i++) {
	              try {
	                list = list + "<li><a class='am-text-truncate'  href='/flow/openflowcfg?Act=phoneflow&ID=" + flow[i].getID() + "' data-rel='" + flow[i].getNAME() + "'>\r\n";
	                list = list + "<span class='widget-name am-text-truncate'>" + flow[i].getNAME() + "</span></a></li>\r\n";
	              } catch (Exception ex) {
	              }
	             }
	         }
	         modelMap.put("FDATE1",FDATE1);
	         modelMap.put("FDATE2",FDATE2);
	         modelMap.put("LIST",list);
	         
	         pagepath = "ZrPhoneEngine/ZrWork/List";
	      }
	      //------------------------------
	      //打开手机流程前处理------------------
	      else if (strAct.equals("phoneflow")){
	          String path="";
	          String ID = request.getParameter("ID");
	          FLOW_RUNTIME_PROCESS frp = flowMonitorService.getPro(ID);
	          path=frp.getFLOWPATH();
	          //处理老数据(可以去掉)-----
	          path=path.replaceAll("/ZrWorkFlow/FlowRun/FlowRunWindow.html","/flow/openphoneflow");
	          //----------------------
	          path=path.replaceAll("/flow/openflowrun","/flow/openphoneflow");
	          String STRID=frp.getCURRACTIVITY();
	          path = path + "&ID="+STRID;

	          modelMap.put("PATH",path);
	          
	          pagepath = "ZrPhoneEngine/ZrWork/openflow";
	      }
	      //打开手机选择人员列表------------------
	      else if (strAct.equals("phoneuserlist")){
	        String strCNodeNo;
	        String strExecuteNo;
	        String strNodeNo;
	        String strUserLst="";
	        String strAstrategy="";
	        String strType="";
	        String str_OtherID="";
	        String str_ParentID ="";
	        String str_ParentID1 ="";
	        String AttMessage ="";//附件提示信息
	        String IsAttMessage ="0";//是否需要检测附件提示
	        String strIsNote = "";//短信提醒类别
	        String SELECTDEPTID = "";
	        //多路分支是否表现在操作按钮上
	        String strISBRANCH="";

	        strCNodeNo = request.getParameter("CNodeNo");
	        if (strCNodeNo==null){strCNodeNo="";}
	        strExecuteNo = request.getParameter("ExecuteNo");
	        if (strExecuteNo==null){strExecuteNo="";}
	        strNodeNo = request.getParameter("NodeNo");
	        if (strNodeNo==null){strNodeNo="";}
	        strType = request.getParameter("Type");
	        if (strType==null){strType="";}
	        AttMessage = request.getParameter("AttMessage");
	        if (AttMessage==null){AttMessage="";}
	        str_OtherID = request.getParameter("OtherID");
	        if (str_OtherID==null){str_OtherID="";}
	        str_ParentID = request.getParameter("ParentID");
	        if (str_ParentID==null){str_ParentID="";}
	        str_ParentID1 = request.getParameter("ParentID1");
	        if (str_ParentID1==null){str_ParentID1="";}
	        SELECTDEPTID = request.getParameter("SELECTDEPTID");
	        if (SELECTDEPTID==null){SELECTDEPTID="";}

	        FLOW_CONFIG_ACTIVITY activity = null;
	        activity = flowControl.getFlowActivity(strNodeNo);
	        if (activity!=null){
	          //得到活动的分配策略(1分配一人 2分配多人)
	          strAstrategy = activity.getASTRATEGY();
	          //得到是否短信提示的状态
	          strIsNote=activity.getISNOTE();
	          strISBRANCH=activity.getISBRANCH();
	        }
	        //初始化流程默认运转参数------------------
	        ActFlowRun ActFlowRun = new ActFlowRun();
	        ActFlowRun.FlowInitDefault(strExecuteNo,"",str_OtherID,str_ParentID,str_ParentID1,"","",strISBRANCH);
	        //初始化默认流程运转参数结束---------------
	        if (AttMessage.length()>0){
	            //根据当前活动和下一活动得到是否需要附件提示
	            IsAttMessage = flowControl.getISAttMessage(strCNodeNo,strNodeNo);
	        }
	        strUserLst = flowControl.getActivityDoUserList(strExecuteNo,strCNodeNo,strNodeNo,ActFlowRun,user);
	        //根据用户编号字符串得到用户列表
	        strUserLst = flowManageService.getShowUserList(strUserLst,"record","flowuser.gif",strAstrategy,strNodeNo,SELECTDEPTID);

	        //根据指定的角色得到用户列表(按角色分组),备用接口，动态指定角色时使用。
	        //strUserLst = flowmanage.GetRoleUserList("record","flowuser.gif",strAstrategy,strNodeNo,"301,302,303,304");
	        
	        modelMap.put("IsAttMessage",IsAttMessage);
	        modelMap.put("IsNote",strIsNote);
	        modelMap.put("UserList",strUserLst);
	        
	        pagepath = "ZrPhoneEngine/ZrWork/WfUserList";
	      }
	      //打开手机选择流程步骤列表------------------
	      else if (strAct.equals("phoneActivityList")){
	        String strNode_No_S ="";//当前活动编号
	        String strNode_No_E ="";//可供选择的活动编号
	        String strNodeLst ="";
	        strNode_No_S = request.getParameter("Node_No_S");
	        strNode_No_E = request.getParameter("Node_No_E");
	        strNodeLst = flowManageService.getNextActivityList(strNode_No_S,strNode_No_E,"record","flowselect.gif");
	        modelMap.put("NodeLst",strNodeLst);
	        
	        pagepath = "ZrPhoneEngine/ZrWork/WfActivityList";
	      }
	      //打开手机选择部门承办人列表------------------
	      else if (strAct.equals("phoneDeptPsn")){
	        String strUnitid = user.getUnitID();
	        String strUSERID = user.getUserID();
	        String strUserList = flowControl.getCUnitUserList(strUnitid,strUSERID,"record","flowuser.gif","2");
	        modelMap.put("UserList",strUserList);
	        
	        pagepath = "ZrPhoneEngine/ZrWork/getDeptPsn";
	      }
	      //得到手机端分送人员列表----------------
	      else if (strAct.equals("phonesendpsn")){
	        String roles = request.getParameter("roles");//角色编号串
	        String sunitid = request.getParameter("sunitid");//左包含的单位编码位数
	        if (sunitid==null || sunitid.length()==0)
	        {sunitid = "";}
	        String isdept = request.getParameter("isdept");//是否本部门的人,1是，0否
	        String isSMS = request.getParameter("issms");//是否发送短信,1是，0否
	        if (isSMS==null){isSMS="0";}
	        Calendar cal = Calendar.getInstance();
	        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	        String mDateTime = formatter.format(cal.getTime());

	        String TitleName = "";
	        String username = user.getName();
	        TitleName = request.getParameter("TitleName");//标题
	        if (TitleName==null){TitleName="";}
	        TitleName = "["+TitleName+"→分送]["+username+"]["+mDateTime+"]";
	        String strUnitid = user.getUnitID();
	        if (sunitid.length()>0)
	        {sunitid = strUnitid.substring(0,Integer.parseInt(sunitid));}
	        String strUserList = flowControl.getShowRolesUserList(roles,"record","flowuser.gif","2",strUnitid,sunitid,isdept);
	        if (isSMS.equals("1")) {
	           strUserList = "短信提示：<input type='text' name='smsvalue' size='42' value='"+TitleName+"'><br>"+strUserList;
	        }
	        modelMap.put("isSMS",isSMS);
	        modelMap.put("UserList",strUserList);
	        
	        pagepath = "ZrPhoneEngine/ZrWork/getSendPsn";
	      }
	      //执行手机端流程分送操作----------------
	      else if (strAct.equals("phonedoflowsend")){
	        String users = request.getParameter("users");//用户编号串
	        String cid = request.getParameter("cid");//当前活动id
	        String sid = request.getParameter("sid");//分送显示的活动id
	        String ExecuteNo = request.getParameter("ExecuteNo");//当前流程流转id
	        String smsvalue = request.getParameter("smsvalue");//发送短信内容
	        if (smsvalue==null){smsvalue="";}
	        String TitleName= flowControl.getTitle(ExecuteNo);
	        String username = user.getName();
	        //执行流程发送流程---
	        flowControl.dosend(ExecuteNo,sid,cid,TitleName,users+"/"+smsvalue,user.getUserID(),username);
	        
	        pagepath = "ZrPhoneEngine/ZrWork/SendPsn";
	      }
	      //打开手机端流程移交------------------
	      else if (strAct.equals("PhoneDevolve")){
	         String strUnitid = "_";
	         String strUserList = flowControl.getShowUnitUserList(strUnitid,"record","flowuser.gif","1");
	         modelMap.put("UserList",strUserList);
	         
	         pagepath = "ZrPhoneEngine/ZrWork/DevolveMain";
	      }
	      //------------------------------
	      //打开流程包设计------------------
	      else if (strAct.equals("PackageDesign")){
	         String strFID = request.getParameter("FID");
	         if (strFID ==null)
	         {strFID = "";}
	         modelMap.put("FID",strFID);
	         
	         pagepath = "ZrWorkFlow/flowconfig/FlowProcess/PackageDesign";
	      }
	      //------------------------------
	      //打开流程包列表------------------
	      else if (strAct.equals("PackageList")){
	         Package[] FlowPk = null;
	         String strFID = request.getParameter("FID");
	         if (strFID ==null)
	         {strFID = "";}
	         String PData = "";
	         int row=0;
	         int low=0;
	         FlowPk = flowSetupService.getFlowPackageList1(strFID);
	         if (FlowPk!=null && FlowPk.length>0){
	             PData = PData + "{\"total\":"+String.valueOf(FlowPk.length)+",\"list\":[";
	             for(int i=0;i<FlowPk.length;i++){
	               if (i==0){
	                  PData = PData + "{\"id\":\""+FlowPk[i].getID()+"\",\"flow_id\":\"4\",\"process_name\":\""+FlowPk[i].getNAME()+"\",\"process_to\":\"\",\"icon\":\"icon-book\",\"style\":\"width:172px;height:40px;line-height:40px;color:#0e76a8;left:"+String.valueOf(low*206+10)+"px;top:"+String.valueOf(row*58+10)+"px;\"}";
	               }else{
	                  PData = PData + ",{\"id\":\""+FlowPk[i].getID()+"\",\"flow_id\":\"4\",\"process_name\":\""+FlowPk[i].getNAME()+"\",\"process_to\":\"\",\"icon\":\"icon-book\",\"style\":\"width:172px;height:40px;line-height:40px;color:#0e76a8;left:"+String.valueOf(low*206+10)+"px;top:"+String.valueOf(row*58+10)+"px;\"}";
	               }
	               if(i!=0 && (i+1) % 5==0){
	                  row=row+1;
	                  low = 0;
	               }else{
	                  low = low + 1;
	               }
	             }
	             PData = PData + "]}";
	          }
	         modelMap.put("PData",PData);
	         
	         pagepath = "ZrWorkFlow/flowconfig/Other/PackageList";
	      }
	      //------------------------------
	      //增加流程包(分类)------------------
	      else if (strAct.equals("PackageAdd")){
	         Calendar cal = Calendar.getInstance();
	         SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	         String mDateTime = formatter.format(cal.getTime());
	         String strFID = request.getParameter("FID");
	         if (strFID==null || strFID.length()==0)
	         {strFID = "000";}
	         modelMap.put("FID",strFID);
	         modelMap.put("mDateTime",mDateTime);
	         modelMap.put("UnitID",user.getUnitID());
	         modelMap.put("UserID",user.getUserID());
	         modelMap.put("Name",user.getName());
	         
	         pagepath = "ZrWorkFlow/flowconfig/Other/PackageAdd";
	      }
	      //编辑流程包(分类)------------------
	      else if (strAct.equals("PackageEdit")){
	         String ID = request.getParameter("ID");
	         Package Pack = flowSetupService.getPackageID(ID);
	         String CREATEDATE = Pack.getCREATEDATE().toString().substring(0,10);
	         String TureName = userService.getUserName(Pack.getCREATEPSN());
	         modelMap.put("TureName",TureName);
	         modelMap.put("CREATEDATE",CREATEDATE);
	         modelMap.put("Package",Pack);
	         
	         pagepath = "ZrWorkFlow/flowconfig/Other/PackageEdit";
	      }
	      //复制流程包(分类)------------------
	      else if (strAct.equals("FlowPackageCopy")){
	         String strID = request.getParameter("PackageID");
	         String unitid = user.getUnitID().toString();
	         Vector<Object> vec = new Vector<Object>();
	         vec = flowConfigService.copyFlowPackage(strID,unitid);
	         //------------------
	         int iSySize = vec.size();  //剩余记录
	         int iDqSize = 0;           //当前记录
	         int iCs = 0;               //循环次数
	         while(iSySize>0){
	           if(iSySize-100>0){
	             iDqSize = 100;
	             iSySize = iSySize-100;
	           } else{
	             iDqSize = iSySize;
	             iSySize = 0;
	           }
	           //调用
	           flowConfigService.copyFlowPublic(vec,iCs,iDqSize);
	           iCs++;
	         }
	         //-------------------
	         //返回
	         pagepath = "redirect:/flow/openflowcfg?Act=PackageDesign";
	      }
	      //打开流程设计------------------
	      else if (strAct.equals("FlowShowDesign")){
	          String strPackageID = request.getParameter("PackageID");
	          modelMap.put("PackageID",strPackageID);
	          
	          pagepath = "ZrWorkFlow/flowconfig/FlowProcess/FlowShowDesign";
	      }
	      //打开流程设计列表------------------
	      else if (strAct.equals("FlowDesignList")){
	          FLOW_CONFIG_PROCESS[] PROCESS = null;
	          String strPackageID = request.getParameter("PackageID");
	          String PData = "";
	          int row=0;
	          int low=0;
	          PROCESS = flowConfigService.getFlowList(strPackageID);
	          if (PROCESS!=null && PROCESS.length>0){
	             PData = PData + "{\"total\":"+String.valueOf(PROCESS.length)+",\"list\":[";
	             for(int i=0;i<PROCESS.length;i++){
	               if (i==0){
	                  PData = PData + "{\"id\":\""+PROCESS[i].getID()+"\",\"flow_id\":\"4\",\"process_name\":\""+PROCESS[i].getNAME()+"\",\"process_to\":\"\",\"icon\":\"icon-th-large\",\"style\":\"width:172px;height:40px;line-height:40px;color:#0e76a8;left:"+String.valueOf(low*206+10)+"px;top:"+String.valueOf(row*58+10)+"px;\"}";
	               }else{
	                  PData = PData + ",{\"id\":\""+PROCESS[i].getID()+"\",\"flow_id\":\"4\",\"process_name\":\""+PROCESS[i].getNAME()+"\",\"process_to\":\"\",\"icon\":\"icon-th-large\",\"style\":\"width:172px;height:40px;line-height:40px;color:#0e76a8;left:"+String.valueOf(low*206+10)+"px;top:"+String.valueOf(row*58+10)+"px;\"}";
	               }
	               if (i!=0 && (i+1) % 5==0){
	                  row=row+1;
	                  low = 0;
	               }else{
	                  low = low + 1;
	               }
	            }
	            PData = PData + "]}";
	          }else{
	            PData = PData + "{\"total\":0,\"list\":[";
	            PData = PData + "]}";
	          }
	          modelMap.put("PackageID",strPackageID);
	          modelMap.put("PData",PData);
	          
	          pagepath = "ZrWorkFlow/flowconfig/FlowProcess/List";
	      }
	      //-----------------------------------
	      //打开新增流程-------------------------
	      else if (strAct.equals("AddFlowFrm")){
	          Calendar cal = Calendar.getInstance();
	          SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	          String inputdate = formatter.format(cal.getTime());
	          String strPackageID = request.getParameter("PackageID");

	          String LIST1 = flowSetupService.showDocnameSelect("COLL_DOC_CONFIG","");
	          String LIST2 = flowConfigService.group_AllList("srcList");
	          String LIST3 = flowConfigService.flow_Group_List("","destList","");
	          String LIST4 = flowConfigService.group_AllList("srcList1");
	          String LIST5 = flowConfigService.flow_Group_List1("","destList1","");

	          modelMap.put("PackageID",strPackageID);
	          modelMap.put("inputdate",inputdate);
	          modelMap.put("UserID",user.getUserID());
	          modelMap.put("Name",user.getName());
	          modelMap.put("LIST1",LIST1);
	          modelMap.put("LIST2",LIST2);
	          modelMap.put("LIST3",LIST3);
	          modelMap.put("LIST4",LIST4);
	          modelMap.put("LIST5",LIST5);

	          pagepath = "ZrWorkFlow/flowconfig/FlowProcess/AddFrm";
	      }
	      //-----------------------------------
	      //打开编辑流程-------------------------
	      else if (strAct.equals("EditFlowFrm")){
	          String ID = request.getParameter("ID");
	          FLOW_CONFIG_PROCESS Pr = flowConfigService.getPROCESSID(ID);
	          String truename = userService.getUserName(Pr.getCREATEPSN());
	          String cdate = Pr.getCREATEDATE().toString().substring(0,10);
	          String LIST1 = flowSetupService.showDocnameSelect("COLL_DOC_CONFIG",Pr.getDOCID());
	          String LIST2 = flowSetupService.showDocnameSelect("COLL_DOC_CONFIG",Pr.getDOCIDPHO());
	          String LIST3 = flowConfigService.group_AllList("srcList");
	          String LIST4 = flowConfigService.flow_Group_List(ID,"destList","");
	          String LIST5 = flowConfigService.group_AllList("srcList1");
	          String LIST6 = flowConfigService.flow_Group_List1(ID,"destList1","");
	          
	          modelMap.put("PROCESS",Pr);
	          modelMap.put("cdate",cdate);
	          modelMap.put("truename",truename);
	          modelMap.put("LIST1",LIST1);
	          modelMap.put("LIST2",LIST2);
	          modelMap.put("LIST3",LIST3);
	          modelMap.put("LIST4",LIST4);
	          modelMap.put("LIST5",LIST5);
	          modelMap.put("LIST6",LIST6);
	          
	          pagepath = "ZrWorkFlow/flowconfig/FlowProcess/EditFrm";
	      }
	      //-----------------------------------
	      //打开流程的模板文件--------------------
	      else if (strAct.equals("opendoc")){
	          String collengine = "";
	          String strID = request.getParameter("ID");//配置ID
	          if (strID==null){strID = "";}
	          String strtype = request.getParameter("type");//类别
	          if (strtype==null){strtype = "1";}
	          if (strtype.equals("1")){
	             collengine = "/collect/collengine";
	          }else{
	             collengine = "/collect/collenginephone";
	          }
	          modelMap.put("ID",strID);
	          modelMap.put("collengine",collengine);
	          
	          pagepath = "ZrWorkFlow/flowconfig/FlowProcess/opendoc";
	      }
	      //-----------------------------------
	      //复制流程----------------------------
	      else if (strAct.equals("FlowCopy")){
	          String strPackageID = request.getParameter("PackageID");
	          String strFlowID = request.getParameter("FlowID");
	          if (flowConfigService.copyFlow(strFlowID)){
	        	  pagepath = "redirect:/flow/openflowcfg?Act=FlowShowDesign&PackageID="+strPackageID;
	          }
	      }
	      //-----------------------------------
	      //打开流程设计-------------------------
	      else if (strAct.equals("FlowDesign")){
	           String strWorkflow_No = request.getParameter("ID");
	           FLOW_CONFIG_PROCESS Process = null;
	           Process = flowControl.getFlowProcess(strWorkflow_No);
	           String PID =  Process.getFLOWPACKAGE();
	           String strFlowType = Process.getTYPE();
	           String strFlowFormType = Process.getFORMTYPE();
	           modelMap.put("Workflow_No",strWorkflow_No);
	           modelMap.put("PID",PID);
	           modelMap.put("FlowType",strFlowType);
	           modelMap.put("FlowFormType",strFlowFormType);
	           
	           pagepath = "ZrWorkFlow/flowconfig/FlowProcess/FlowDesign";
	      }
	      //-----------------------------------
	      //新建流程步骤-------------------------
	      else if (strAct.equals("Activitynew")){
	          String strFlowID = request.getParameter("FlowID");
	          if (flowConfigService.activitynew(strFlowID)){
	        	  pagepath = "redirect:/flow/openflowcfg?Act=FlowDesign&ID="+strFlowID;
	          }
	      }
	      //-----------------------------------
	      //新建子流程步骤-------------------------
	      else if (strAct.equals("Activitynew1")){
	          String strflowcid = request.getParameter("flowcid");
	          String strflowsid = request.getParameter("flowsid");
	          if (flowConfigService.activitynew1(strflowcid,strflowsid)){
	        	  pagepath = "redirect:/flow/openflowcfg?Act=FlowDesign&ID="+strflowcid;
	          }
	      }
	      //-----------------------------------
	      //打开流程步骤间的关系设计图---------------
	      else if (strAct.equals("EditActivity")){
	          FLOW_CONFIG_ACTIVITY[] Activity = null;
	          String strPackageID = request.getParameter("PackageID");
	          String strWorkflow_No = request.getParameter("Workflow_No");
	          String strflow_Type = request.getParameter("type");
	          String strflow_formtype = request.getParameter("formtype");
	          String PData = "";
	          int row=0;
	          int low=0;
	          String flow_ico="icon-th";
	          String XYCSS[]=null;
	          String toflows[]=null;
	          String toflow = "";
	          String toflow1 = "";
	          String SID = "";
	          String EID = "";
	          String HA = "";
	          String HB = "";
	          String strWidth="";
	          String SE="";
	          String strSub = "";
	          Activity = flowControl.getActivityList(strWorkflow_No);
	          if (Activity!=null && Activity.length>0){
	             PData = PData + "{\"total\":"+String.valueOf(Activity.length)+",\"list\":[";
	             //特殊处理老数据的关系-----
	             Map<String, Object> gxHashtable = new HashMap<String, Object>();
	             for(int i=0;i<Activity.length;i++) {
	               if (Activity[i].getTYPE().equals("1")) {
	                  SID = Activity[i].getID();
	               }
	               if (Activity[i].getTYPE().equals("2")) {
	                  EID = Activity[i].getID();
	               }
	               if (Activity[i].getTYPE().equals("5"))
	               {strSub = strSub + ","+Activity[i].getID();}
	               XYCSS = Activity[i].getXYCSS().split("/");
	               if (XYCSS[0].equals("1")) {//老格式的数据
	                 if (XYCSS[3].length()>0){
	                   HA = XYCSS[3];
	                   HB = XYCSS[1];
	                   if (HA.equals("0")){HA= "S"+SID;}
	                   if (HA.equals("-1")){HA= "E"+EID;}
	                   if (HB.equals("0")){HB= "S"+SID;}
	                   if (HB.equals("-1")){HB= "E"+EID;}
	                   gxHashtable.put(HA,HB);
	                 }
	               }
	            }
	           //----------------------
	           for(int i=0;i<Activity.length;i++){
	             XYCSS = Activity[i].getXYCSS().split("/");
	             if (XYCSS[0].equals("1")){
	               XYCSS[5]=XYCSS[5].substring(0,XYCSS[5].length()-3)+"px";
	               XYCSS[6]=XYCSS[6].substring(0,XYCSS[6].length()-3)+"px";
	             }
	             if (Activity[i].getTYPE().equals("1")){
	               flow_ico= "icon-play";
	               SE="S";
	             }
	             if (Activity[i].getTYPE().equals("2")){
	               flow_ico= "icon-ok";
	               SE="E";
	             }
	             if (Activity[i].getTYPE().equals("3")){
	               flow_ico= "icon-th";
	               SE="";
	             }
	             if (Activity[i].getTYPE().equals("5")){
	                flow_ico= "icon-random";
	                SE="Z";
	             }
	             if (XYCSS[0].equals("1")){
	                toflow = (String) gxHashtable.get(Activity[i].getID());
	             }else{
	                toflow = XYCSS[3];
	                toflows = toflow.split(",");
	                for (int y=0;y<toflows.length;y++) {
	                  toflow = toflows[y];
	                  if (toflow.equals(SID)) {
	                    toflow = "S"+toflow;
	                  }
	                  if (toflow.equals(EID)) {
	                    toflow = "E"+toflow;
	                  }
	                  if (toflow.length()>0 && strSub.indexOf(toflow) != -1) {
	                    toflow = "Z"+toflow;
	                  }
	                  if (y==0) {
	                     toflow1 = toflow;
	                  }else {
	                     toflow1 = toflow1 +","+toflow;
	                  }
	               }
	             }
	             if (Activity[i].getTYPE().equals("1") || Activity[i].getTYPE().equals("2")) {
	                 strWidth="80px";
	             }else{
	                 strWidth="140px";
	             }
	             if (Activity[i].getTYPE().equals("5")) {
	                 strWidth="210px";
	             }
	             if (i==0) {
	                PData = PData + "{\"id\":\""+SE+Activity[i].getID()+"\",\"flow_id\":\"4\",\"process_name\":\""+Activity[i].getNAME()+"\",\"process_to\":\""+toflow1+"\",\"icon\":\""+flow_ico+"\",\"style\":\"width:"+strWidth+";height:40px;line-height:40px;color:#0e76a8;left:"+XYCSS[5]+";top:"+XYCSS[6]+";\",\"css\":\"color:#0e76a8;left:"+XYCSS[5]+";top:"+XYCSS[6]+";\"}";
	             }else{
	                PData = PData + ",{\"id\":\""+SE+Activity[i].getID()+"\",\"flow_id\":\"4\",\"process_name\":\""+Activity[i].getNAME()+"\",\"process_to\":\""+toflow1+"\",\"icon\":\""+flow_ico+"\",\"style\":\"width:"+strWidth+";height:40px;line-height:40px;color:#0e76a8;left:"+XYCSS[5]+";top:"+XYCSS[6]+";\",\"css\":\"color:#0e76a8;left:"+XYCSS[5]+";top:"+XYCSS[6]+";\"}";
	             }
	             if (i!=0 && (i+1) % 5==0) {
	                row=row+1;
	                low = 0;
	             }else{
	                low = low + 1;
	             }
	           }
	           PData = PData + "]}";
	          }
	          modelMap.put("Workflow_No",strWorkflow_No);
	          modelMap.put("flow_Type",strflow_Type);
	          modelMap.put("flow_formtype",strflow_formtype);
	          modelMap.put("PackageID",strPackageID);
	          modelMap.put("PData",PData);
	          
	          pagepath = "ZrWorkFlow/flowconfig/FlowProcess/EditActivity";
	      }
	      //-------------------------------------
	      //复制流程步骤---------------------------
	      else if (strAct.equals("ActivityCopy")){
	          String strFlowID = request.getParameter("FlowID");
	          String strActivityID = request.getParameter("ActivityID");
	          if (flowConfigService.copyActivity(strActivityID)) {
	        	  pagepath = "redirect:/flow/openflowcfg?Act=FlowDesign&ID="+strFlowID;
	          }
	      }
	      //-------------------------------------
	      //流程步骤属性设置-----------------------
	      else if (strAct.equals("ManageActivity")){
	          String ID = request.getParameter("ID");
	          if (ID==null){ID="";}
	          String FID = request.getParameter("FID");
	          if (FID==null){FID="";}
	          FLOW_CONFIG_ACTIVITY entityActivity=null;
	          String strflowtype = request.getParameter("flowtype");
	          String strformtype = request.getParameter("formtype");
	          String ActivityType = "3";
	          String IDDis="";//供显示使用
	          String DocID ="";//表单引擎的配置文档ID
	          if (ID.trim().length() > 0) {
	             entityActivity = flowManageAct.getActivityById(ID);
	             if(entityActivity!=null){
	                IDDis=entityActivity.getID();
	                ActivityType = entityActivity.getTYPE();
	             }
	             //表单引擎的配置文档ID
	             DocID = flowManageAct.getDocID(entityActivity.getFID());
	           }
	           if (entityActivity == null) {
	              entityActivity = new FLOW_CONFIG_ACTIVITY();
	              entityActivity.setID("null");//首次没有ID，故设置一特殊值，以便后台处理
	              entityActivity.setIDENTIFICATION("");
	              entityActivity.setFID(request.getParameter("FID"));
	              entityActivity.setTYPE(request.getParameter("TYPE"));
	              entityActivity.setISSIGN("0");
	              entityActivity.setOPENHELP("0");
	              entityActivity.setISNOTE("0");
	              entityActivity.setISMESSAGE("0");
	              entityActivity.setISSAVE1("0");
	              entityActivity.setISSAVE2("0");
	              entityActivity.setISLEAVE1("0");
	              entityActivity.setISLEAVE2("0");
	              entityActivity.setASTRATEGY("1");
	              entityActivity.setCSTRATEGY("1");
	              entityActivity.setISBRANCH("0");
	              entityActivity.setATTTYPE("1");
	           }
	           Button[] buttons1, buttons2;
	           buttons1 = flowManageAct.getButtonList();
	           buttons2 = flowManageAct.getButtonListByFid(entityActivity.getID());
	           Group[] groups1, groups2;
	           groups1 = flowManageAct.getUserGroupList();
	           groups2 = flowManageAct.getUserGroupListByFid(entityActivity.getID());
	           String LIST1="";
	           String LIST2="";
	           String LIST3="";
	           String LIST4="";
	           
	           if (buttons1 != null){
	               for (int i = 0; i < buttons1.length; i++){
	                  LIST1= LIST1 + "<option value='" + buttons1[i].getID() + "'>" + buttons1[i].getBNAME() + "</option>";
	               }
	           }
	           if (buttons2 != null){
	               for (int i = 0; i < buttons2.length; i++){
	                   LIST2= LIST2 + "<option value='" + buttons2[i].getID() + "'>" + buttons2[i].getBNAME() + "</option>";
	               }
	           }
	           if (groups1 != null){
	               for (int i = 0; i < groups1.length; i++){
	                   LIST3= LIST3 +"<option value='" + groups1[i].getID() + "'>" + groups1[i].getNAME() + "</option>";
	               }
	           }
	           if (groups2 != null){
	               for (int i = 0; i < groups2.length; i++){
	                   LIST4= LIST4 +"<option value='" + groups2[i].getID() + "'>" + groups2[i].getNAME() + "</option>";
	               }
	           }
	           modelMap.put("ID",ID);
	           modelMap.put("FID",FID);
	           modelMap.put("UserID",user.getUserID());
	           modelMap.put("Activity",entityActivity);
	           modelMap.put("flowtype",strflowtype);
	           modelMap.put("formtype",strformtype);
	           modelMap.put("ActivityType",ActivityType);
	           modelMap.put("DocID",DocID);
	           modelMap.put("IDDis",IDDis);
	           modelMap.put("LIST1",LIST1);
	           modelMap.put("LIST2",LIST2);
	           modelMap.put("LIST3",LIST3);
	           modelMap.put("LIST4",LIST4);

	           pagepath = "ZrWorkFlow/flowconfig/FlowProcess/ManageActivity";
	      }
	      //-------------------------------------
	      //流程步骤间的属性(线上)设置--------------
	      else if (strAct.equals("ManageActivityConne")){
	          String strcid = request.getParameter("cid");
	          if (strcid==null){strcid="";}
	          String streid = request.getParameter("eid");
	          if (streid==null){streid="";}
	          String ID="";
	          ID = flowConfigService.getActivityID(strcid,streid);
	          FLOW_CONFIG_ACTIVITY_CONNE Pr = flowConfigService.getActivityID(ID);
	          Variable[] sessionVariables = null;
	          try{
	            sessionVariables= flowSetupService.getVariableList();
	          } catch(Exception ex) {}
	          String LIST1 = "";
	          if(sessionVariables!=null){
	             for( int i=0;i< sessionVariables.length;i++){
	                 LIST1 = LIST1 + "<option value=\""+sessionVariables[i].getCODE()+"\">"+sessionVariables[i].getNAME()+"</option>";
	             }
	          }
	          String LIST2 = flowSetupService.showPackageSelect1(Pr.getTYPE());
	          String LIST3 = flowConfigService.activity_AllList("hiddList","",Pr.getFID());
	          String LIST4 = flowConfigService.flow_Activity_List1(Pr.getFID(),"srcList","");
	          String LIST5 = flowConfigService.flow_Activity_List(ID,"destList","");

	          modelMap.put("ID",ID);
	          modelMap.put("cid",strcid);
	          modelMap.put("eid",streid);
	          modelMap.put("Pr",Pr);
	          modelMap.put("LIST1",LIST1);
	          modelMap.put("LIST2",LIST2);
	          modelMap.put("LIST3",LIST3);
	          modelMap.put("LIST4",LIST4);
	          modelMap.put("LIST5",LIST5);

	          pagepath = "ZrWorkFlow/flowconfig/FlowProcess/ManageActivityConne";
	      }
	      //-------------------------------------
	      //删除流程步骤间的属性(线上)设置-----------
	      else if (strAct.equals("deleteconnid")){
	          String strcid = request.getParameter("cid");
	          if (strcid==null){strcid="";}
	          String streid = request.getParameter("eid");
	          if (streid==null){streid="";}
	          flowControl.deleteConnID(strcid,streid);
	          
	          pagepath = "ZrWorkFlow/flowconfig/FlowProcess/deleteconnid";
	      }
	      //-------------------------------------
	      //设置条件------------------------------
	      else if (strAct.equals("CtlAbleFieldDValue")){
	          Variable[] Variables = null;
	          Variables = flowSetupService.getVariableList();
	          String strResult=request.getParameter("strResult");
	          String LIST1="";
	          if(Variables!=null){
	            for(int i=0;i< Variables.length;i++){
	               LIST1 = LIST1 + "<option value=\""+Variables[i].getCODE()+"\">"+Variables[i].getNAME()+"</option>";
	            }
	          }
	          LIST1 += "<option value=\"^UserIdea^\">用户意见(流程内置变量)</option>";
	          modelMap.put("LIST1",LIST1);
	          modelMap.put("Result",strResult);

	          pagepath = "ZrWorkFlow/flowconfig/FlowProcess/CtlAbleFieldDValue";
	      } else {
	      }
	      //-------------------------------------
	      if (pagepath.length() > 0) {
	    	  return pagepath;
	      }
	      response.getWriter().println("{\"data\", \"true\"}");
		  response.getWriter().flush();
		  return null;
	}

	@RequestMapping("/flowmanagecfg")
	public String flowManageConfig(ModelMap modelMap, 
			HttpServletRequest request, HttpServletResponse response) throws Exception {
	      response.setContentType("text/html; charset=UTF-8");
	      String strAct = request.getParameter("Act");
	      String pagepath = "";
	      //打开流程图示-----------------
	      if (strAct.equals("FlowShowMap")) {
			FLOW_CONFIG_ACTIVITY[] Activity = null;
			String strWorkflow_No = request.getParameter("Workflow_No");
			String strAID = request.getParameter("AID");
			
			String PData = "";
			int row=0;
			int low=0;
			String flow_ico="icon-th";
			String XYCSS[]=null;
			String toflows[]=null;
			String toflow = "";
			String toflow1 = "";
			String SID = "";
			String EID = "";
			String HA = "";
			String HB = "";
			@SuppressWarnings("unused")
			String strWidth="";
			String SE="";
			String strSub = "";
			String dq="";
			
			Activity = flowControl.getActivityList(strWorkflow_No);
			if (Activity!=null && Activity.length>0){
			      PData = PData + "{\"total\":"+String.valueOf(Activity.length)+",\"list\":[";
			      //特殊处理老数据的关系------------------
			      Hashtable<String, String> gxHashtable = new Hashtable<String, String> ();
			      for(int i=0;i<Activity.length;i++){
			       if (Activity[i].getTYPE().equals("1")) {
			          SID = Activity[i].getID();
			       }
			       if (Activity[i].getTYPE().equals("2")) {
			          EID = Activity[i].getID();
			       }
			       if (Activity[i].getTYPE().equals("5")) {
			    	   strSub = strSub + ","+Activity[i].getID();
			       }
			       XYCSS = Activity[i].getXYCSS().split("/");
			       if (XYCSS[0].equals("1")) {//老格式的数据
			          if (XYCSS[3].length()>0) {
			             HA = XYCSS[3];
			             HB = XYCSS[1];
			             if (HA.equals("0")){HA= "S"+SID;}
			             if (HA.equals("-1")){HA= "E"+EID;}
			             if (HB.equals("0")){HB= "S"+SID;}
			             if (HB.equals("-1")){HB= "E"+EID;}
			             gxHashtable.put(HA,HB);
			          }
			       }
			     }
			     //----------------------
			     for(int i=0;i<Activity.length;i++){
			       XYCSS = Activity[i].getXYCSS().split("/");
			       if (XYCSS[0].equals("1")) {
			          XYCSS[5]=XYCSS[5].substring(0,XYCSS[5].length()-3)+"px";
			          XYCSS[6]=XYCSS[6].substring(0,XYCSS[6].length()-3)+"px";
			       }
			       if (Activity[i].getTYPE().equals("1")) {
			         flow_ico= "icon-play";
			         SE="S";
			       }
			       if (Activity[i].getTYPE().equals("2")) {
			         flow_ico= "icon-ok";
			         SE="E";
			       }
			       if (Activity[i].getTYPE().equals("3")) {
			         flow_ico= "icon-th";
			         SE="";
			       }
			       if (Activity[i].getTYPE().equals("5")) {
			         flow_ico= "icon-random";
			         SE="Z";
			       }
			        if (XYCSS[0].equals("1")) {
			           toflow = (String) gxHashtable.get(Activity[i].getID());
			        }else {
			           toflow = XYCSS[3];
			           toflows = toflow.split(",");
			           for (int y=0;y<toflows.length;y++) {
			           toflow = toflows[y];
			           if (toflow.equals(SID)) {
			              toflow = "S"+toflow;
			           }
			           if (toflow.equals(EID)) {
			              toflow = "E"+toflow;
			           }
			           if (toflow.length()>0 && strSub.indexOf(toflow) != -1) {
			              toflow = "Z"+toflow;
			           }
			           if (y==0) {
			             toflow1 = toflow;
			           }else {
			             toflow1 = toflow1 +","+toflow;
			           }
			           }
			        }
			        
			        if (Activity[i].getTYPE().equals("1") || Activity[i].getTYPE().equals("2")) {
			            strWidth="80px";
			        }else {
			            strWidth="120px";
			        }
			        if (Activity[i].getTYPE().equals("5")) {
			            strWidth="200px";
			        }
			        String color="#0e76a8";
			       if (Activity[i].getID().equals(strAID)) {
			          dq="当前步骤：";
			          color="rgb(254, 153, 0)";
			          if (Activity[i].getTYPE().equals("1") || Activity[i].getTYPE().equals("2")) {
			            strWidth="120px";
			          }else {
			            strWidth="180px";
			          }
			       }else {
			         dq="";
			       }
			       
			       if (i==0) {
			         PData = PData + "{\"id\":\""+SE+Activity[i].getID()+"\",\"flow_id\":\"4\",\"process_name\":\""+dq+Activity[i].getNAME()+"\",\"process_to\":\""+toflow1+"\",\"icon\":\""+flow_ico+"\",\"css\":\"color:"+color+";left:"+XYCSS[5]+";top:"+XYCSS[6]+";\"}";
			       }else {
			         PData = PData + ",{\"id\":\""+SE+Activity[i].getID()+"\",\"flow_id\":\"4\",\"process_name\":\""+dq+Activity[i].getNAME()+"\",\"process_to\":\""+toflow1+"\",\"icon\":\""+flow_ico+"\",\"css\":\"color:"+color+";left:"+XYCSS[5]+";top:"+XYCSS[6]+";\"}";
			       }
			       
			       if (i!=0 && (i+1) % 5==0) {
			          row=row+1;
			          low = 0;
			       }else {
			          low = low + 1;
			       }
			     }
			     PData = PData + "]}";
			  }
			  modelMap.put("PData",PData);
	          pagepath = "ZrWorkFlow/FlowShow/FlowShowMap/ShowActivity";
	      }
	      return pagepath;
	}

	@RequestMapping("/flowdesign")
	public String flowDesign(HttpServletRequest request, HttpServletResponse response) throws IOException {
	      response.setContentType("text/html; charset=UTF-8");
	      //得到流程的基础属性
	      String str_FlowCss = request.getParameter("tmp_save");
	      String ObjectFile = request.getParameter("ObjectFile");
	      String type = request.getParameter("type");
	      String formtype = request.getParameter("formtype");
	      String Workflow_No = request.getParameter("Workflow_No");
	      
	      boolean isOk = false;
	      this.Msg = "非法调用，操作不被接受，你的操作已被记录！";
	      try {
	          //保存流程配置
	    	  isOk = flowControl.saveFlowConfig(Workflow_No,str_FlowCss);
	      } catch (Exception ex){
	    	  ex.printStackTrace();
	    	  isOk = false;
	    	  this.Msg = "连接中间件服务器失败，请稍后再试！";
	      }
	      if (isOk) {
	    	  return "redirect:"+ObjectFile+"&type="+type+"&formtype="+formtype+"&Workflow_No="+Workflow_No;
	      }
	      response.getWriter().println("{\"data\", \"true\"}");
		  response.getWriter().flush();
		  return null;
	}

	@RequestMapping("/startflowautoserver")
	public void startFlowAutoServer(HttpServletRequest request, HttpServletResponse response) throws Exception {
	    response.setContentType("text/html; charset=UTF-8");
	    HttpSession session = request.getSession(true);
	    SessionUser user = (SessionUser) session.getAttribute("userinfo");
	    
	    StringBuffer sb = new StringBuffer();
	    sb.append("<HTML>");
	    sb.append("<HEAD>");
	    sb.append("<TITLE></TITLE>");
	    sb.append("<META HTTP-EQUIV=\"Pragma\" CONTENT=\"no-cache\">");
	    sb.append("<META http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">");
	    sb.append("<LINK type=\"text/css\" rel=\"stylesheet\" href=\"css/"+user.getUserCSS()+"\">");
	    sb.append("<SCRIPT type=\"text/javascript\" src=\"/static/Zrsysmanage/script/Public.js\"></SCRIPT>");
	    sb.append("</HEAD>");
	    sb.append("<BODY class=\"BodyMain\">");
	    sb.append("<FORM name=\"recordfrm\" method=\"post\" Action=\"\">");

	    sb.append("<!--HTML Head表单头显示[显示内容,图标文件,用户肤色的图片文件夹]-->");
	    sb.append(uiService.showHeadHtml("启动流程引擎自动服务","IcoTitle.gif",user.getUserImage()));
	    sb.append("<!--HTML Body Start显示表单开始部分-->");
	    sb.append(uiService.showBodyStartHtml());

	    sb.append("<table border=\"0\" width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" height=\"100%\">");
	    sb.append("<tr><td align=\"center\" valign=\"middle\">");
	    sb.append("<table border=\"0\" width=\"378\" cellspacing=\"0\" cellpadding=\"0\" height=\"296\" background=\""+SysPreperty.getProperty().AppUrl+"images/"+user.getUserImage()+"/menumessageback.jpg\">");
	    sb.append("<tr>");
	    sb.append("<td width=\"24\" height=\"37\"></td>");
	    sb.append("<td height=\"37\"></td>");
	    sb.append("<td width=\"26\" height=\"37\"></td>");
	    sb.append("</tr>");
	    sb.append("<tr>");
	    sb.append("<td width=\"24\"></td>");
	    sb.append("<td align=\"left\" colspan=\"2\">");
	    sb.append("<div id=mainbod style=\"VISIBILITY: visible; POSITION: absolute;\"></div>");
	    sb.append("<div id=blurthis style=\"VISIBILITY: visible; POSITION: absolute\"></div>");
	    sb.append("</td>");
	    sb.append("</tr>");
	    sb.append("<tr>");
	    sb.append("<td width=\"24\" height=\"37\"></td>");
	    sb.append("<td height=\"37\" align=\"center\">");
	    sb.append("</td>");
	    sb.append("<td width=\"26\" height=\"37\"></td>");
	    sb.append("</tr>");
	    sb.append("<tr>");
	    sb.append("<td width=\"24\" height=\"32\"></td>");
	    sb.append("<td height=\"32\"></td>");
	    sb.append("<td width=\"26\" height=\"32\"></td>");
	    sb.append("</tr>");
	    sb.append("</table>");
	    sb.append("</td></tr></table>");

	    sb.append(uiService.showFootHtml("", user.getUserImage()));
	    sb.append("</FORM>");

	    sb.append("<script language=\"javascript\">");
	    sb.append("var thissize=10;");
	    sb.append("var textfont='Verdana';");
	    sb.append("var textcolor= new Array();");
	    sb.append("textcolor[0]='EEEEEE';");
	    sb.append("textcolor[1]='DDDDDD';");
	    sb.append("textcolor[2]='CCCCCC';");
	    sb.append("textcolor[3]='AAAAAA';");
	    sb.append("textcolor[4]='888888';");
	    sb.append("textcolor[5]='666666';");
	    sb.append("textcolor[6]='555555';");
	    sb.append("textcolor[7]='444444';");
	    sb.append("textcolor[8]='333333';");
	    sb.append("textcolor[9]='222222';");
	    sb.append("textcolor[10]='111111';");
	    sb.append("textcolor[11]='000000';");

	    sb.append("var message = new Array();");
	    if(isRun){
	      sb.append("message[0]='已经启动流程引擎自动服务!';");
	      sb.append("message[1]='服务正在运行中...';");
	    }else{
	      sb.append("message[0]='不能启动流程引擎自动服务!';");
	      sb.append("message[1]='启动时出现错误！';");
	    }
	    sb.append("var i_blurstrength=20;");
	    sb.append("var i_message=0;");
	    sb.append("var i_textcolor=0;");

	    sb.append("function blurtext() {");
	    sb.append("if(document.all) {");
	    sb.append("if (i_blurstrength >=-2) {");
	    sb.append("if (i_textcolor >=textcolor.length-1) {i_textcolor=textcolor.length-1}");
	    sb.append("blurthis.innerHTML=\"<span id='blurpit1' style='position:absolute;visibility:visible;width:160px; top:5px;left:5px;filter:blur(add=0,strength=\"+i_blurstrength+\",direction=90);font-family:\"+textfont+\";font-size:\"+thissize+\"pt;color:\"+textcolor[i_textcolor]+\"'>\"+message[i_message]+\"</span>\";");
	    sb.append("document.close();");
	    sb.append("i_blurstrength=i_blurstrength-2;");
	    sb.append("i_textcolor++;");
	    sb.append("var timer=setTimeout(\"blurtext()\",50);");
	    sb.append("}");

	    sb.append("else {");
	    sb.append("if (i_textcolor >=textcolor.length-1) {i_textcolor=textcolor.length-1}");
	    sb.append("blurthis.innerHTML=\"<span id='blurit1' style='position:absolute;visibility:visible;width:160px; top:5px;left:5px;filter:blendTrans(duration=4.2);font-family:\"+textfont+\";font-size:\"+thissize+\"pt;color:FF0000'>\"+message[i_message]+\"</span>\";");
	    sb.append("i_message++;");
	    sb.append("if (i_message>=message.length){i_message=0}");

	    sb.append("i_blurstrength=20;");
	    sb.append("i_textcolor=0;");
	    sb.append("clearTimeout(timer);");
	    sb.append("var timer=setTimeout(\"blurtext()\",2000);");
	    sb.append("}");
	    sb.append("}");
	    sb.append("}");
	    sb.append("window.onload=blurtext;");

	    sb.append("</script>");
	    sb.append("</BODY>");
	    sb.append("</HTML>");

	    response.getWriter().print(sb.toString());
	    response.getWriter().flush();
	}

	@SuppressWarnings("unused")
	@RequestMapping("/flowrunPhone")
	public String flowRunPhone(HttpServletRequest request, HttpServletResponse response) throws IOException {
	      response.setContentType("text/html; charset=UTF-8");
	      HttpSession session = request.getSession(true);
	      SessionUser user = (SessionUser)session.getAttribute("userinfo");
	      String pagepath = "";
	      //得到流程的基础属性
	      String strOptCmd = request.getParameter("OptCmd");//记录属性按钮命令值
	      String strIdentification = request.getParameter("Identification");//流程标识
	      String strOptCmd_Name = request.getParameter("OptCmd_Name");//记录属性按钮命令值中文
	      String strWorkflow_No = request.getParameter("Workflow_No");//流程编号
	      String strNode_No_S = request.getParameter("Node_No_S");//当前步骤编号
	      String strM_Node_No_S_E = request.getParameter("M_Node_No_S_E");//下一分支步骤编号
	      String strS_Node_No_S_E = request.getParameter("S_Node_No_S_E");//上一分支步骤编号
	      String strUserNo = request.getParameter("UserNo");//当前用户编号
	      String strExecute_No = request.getParameter("Execute_No");//流程流转编号
	      String strDo_User_Nos = request.getParameter("Do_User_Nos");//选择的处理用户编号
	      String strFormID = request.getParameter("FormID");//业务的ID值
	      String strTitleName = request.getParameter("TitleName");//业务的标题名称
	      String strOtherID = request.getParameter("OtherID");//关联字段的ID值
	      String strPath = request.getParameter("Path");//流程调用文件的路径
	      String strDoIdea = request.getParameter("DoIdea");//处理意见
	      String strUnit = request.getParameter("Unit");//单位编号
	      String strParentID = request.getParameter("ParentID");//父流程ID值或父表单记录值
	      String strParentID1 = request.getParameter("ParentID1");//父流程ID1值或父表单记录值
	      String strRID = request.getParameter("RID");//关联流程的流转ID
	      String strMsgType = request.getParameter("MsgType");//短信提示类别
	      String strRECORDID = request.getParameter("RECORDID");//附件ID
	      String strISBRANCH=request.getParameter("ISBRANCH");//多路分支是否表现在按钮上
	      
	      String strIs = "0";
	      this.isOk = true;
	      this.Msg = "非法调用，操作不被接受，你的操作已被记录！";
	      if(strOptCmd!=null){
		      try {
			      ActFlowRun actFlowRun = new ActFlowRun();
			      //初始化流程运转类-----------------
			      actFlowRun.FlowInit(strOptCmd,strIdentification,strOptCmd_Name,strWorkflow_No,strNode_No_S,strM_Node_No_S_E,strS_Node_No_S_E,strUserNo,strExecute_No,strDo_User_Nos,strFormID,strTitleName,strOtherID,strPath,strDoIdea,strUnit,strParentID,strParentID1,strRID,strMsgType,strRECORDID,strISBRANCH);
			      //初始化流程运转类结束----------------------
		         //保存-------------------------------------
		         if (strOptCmd.equals("SAVE")){
		           //执行流程保存服务
		           strExecute_No = flowControl.flowSaveServer(actFlowRun);
		           strPath=strPath.substring(1);
		           //返回
		           //---------------
		           //---------------
		           pagepath = "redirect:"+strPath+"?FlowNo="+strIdentification+"&FormID="+strFormID+"&OtherID="+strOtherID+"&ExecuteNo="+strExecute_No+"&ParentID="+strParentID+"&ParentID1="+strParentID1+"&ISBRANCH="+strISBRANCH+"&RID="+strRID+"&ISSAVE=1";
		         }
		         //提交-------------------------------------
		         else if (strOptCmd.equals("SUBMIT")){
		           //执行流程提交服务
		           flowControl.flowSubmitServer(actFlowRun,user);
		           strIs = "1";
		         }
		         //处理返回-----------------------------------
		         else if (strOptCmd.equals("RETURN")){
		           //执行流程处理返回服务
		           flowControl.flowReturnServer(actFlowRun,user);
		           strIs = "1";
		         }
		         //退回---------------------------------------
		         else if (strOptCmd.equals("UNTREAD")){
		           //初始化流程运转类,退回的特殊情况(下面两个参数互换)-----------------
		        	 actFlowRun.FlowInitUntread(strS_Node_No_S_E,strM_Node_No_S_E);
		           //初始化流程运转类结束---------------
		           //------------
		           //执行流程提交服务(第六个参数的值为退回步骤的ID--strS_Node_No_S_E,第六和第七个参数互换)
		           flowControl.flowSubmitServer(actFlowRun,user);
		         }
		         //完成----------------------------------------
		         else if (strOptCmd.equals("FINISH")){
		            //执行流程提交服务
		             flowControl.flowFinishServer(actFlowRun,user);
		             strIs = "1";
		         }
		         //移交----------------------------------------
		         else if (strOptCmd.equals("DEVOLVE")){
		            //执行流程移交服务
		             flowControl.flowDevolveServer(actFlowRun,user);
		         }
		         //删除----------------------------------------
		         else if (strOptCmd.equals("DELETE")){
		            //执行流程删除服务
		             flowControl.flowDeleteServer(actFlowRun,user);
		         }
		         //初始化----------------------------------------
		         else if (strOptCmd.equals("INIT")){
		            //执行流程初始化服务
		             flowControl.flowInitServer(actFlowRun,user);
		         }
		         //收回----------------------------------------
		         else if (strOptCmd.equals("TAKEBACK")){
		            //执行流程收回服务
		             flowControl.flowTakeBackServer(actFlowRun,user);
		         }
		         //---------------
		         //送部门承办----------------------------------------
		         else if (strOptCmd.equals("SUBDEPT")){
		            //执行送部门承办服务
		             flowControl.flowSubDeptServer(actFlowRun,user);
		         }
		         //---------------
		         //分送已阅--------
		         else if (strOptCmd.equals("SENDFINISH")){
		            //执行分送已阅服务
		             flowControl.flowSendFinishServer(actFlowRun);
		         }
		         if (!strOptCmd.equals("SAVE")){// 关闭窗口
		        	 pagepath = "redirect:/flow/openflowcfg?Act=phoneflowlist";
		         }
		      }
		      catch (Exception ex){
		    	  this.Msg = "连接中间件服务器失败，请稍后再试！";
		      }
	      }
	      if (pagepath.length() > 0) {
	    	  return pagepath;
	      }
	      response.getWriter().println("{\"data\", \"true\"}");
		  response.getWriter().flush();
	      return null;
	}

 	///=====================================控制器接口私有方法=====================================///
	private void getListEt(HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		JSONObject json = new JSONObject();
		HttpSession session = request.getSession(true);
		SessionUser user = (SessionUser) session.getAttribute("userinfo");
		List<Object> newList = new ArrayList<Object>();
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		FLOW_CONFIG_ENTRUST[] entrust = null;
		entrust = flowMonitorService.getMonitorList(user.getUserID());
		if (entrust != null && entrust.length > 0) {
			for (int i = 0; i < entrust.length; i++) {
				try {
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("ID", entrust[i].getID());
					map.put("IUSERNO", entrust[i].getIUSERNO());
					map.put("SDATE", formatter.format(entrust[i].getSDATE()));
					map.put("EDATE", formatter.format(entrust[i].getEDATE()));
					newList.add(map);
				} catch (Exception e) {
				}
			}
		}
		json.put("rows", newList);
		json.put("total", "0");
		response.getWriter().println(json);
		response.getWriter().flush();
	}

	/**
	 * 添加委托人员
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	private void addFlowEt(HttpServletRequest request, HttpServletResponse response) throws IOException {
		JSONObject json = new JSONObject();
		String strReturn = "0";
		FunctionMessage fm = new FunctionMessage(1);
		FLOW_CONFIG_ENTRUST Entrust = new FLOW_CONFIG_ENTRUST();
		try {
			Entrust.fullDataFromRequest(request);
			fm = flowMonitorService.addEntrust(Entrust);
			if (fm.getResult()) {
				Entrust = null;
				strReturn = "1";
				// response.sendRedirect("ZrWorkFlow/FlowMonitor/FlowEntrust/EntrustList.html");
			} else {
				this.Msg = fm.getMessage();
				this.isOk = fm.getResult();
				strReturn = "0";
			}
			Entrust = null;
		} catch (Exception ex2) {
			this.Msg = "连接客户端文件中间件服务失败！<br>详细错误信息：<br>   " + ex2.toString();
		}
		// this.out = response.getWriter();
		// this.returnPath = "ZrWorkFlow/FlowMonitor/FlowEntrust/EntrustList.html";
		// this.getBox();
		json.put("strReturn", strReturn);
		response.getWriter().println(json);
		response.getWriter().flush();
	}

	/**
	 * 删除委托人员基本属性
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	private void deleteEt(HttpServletRequest request, HttpServletResponse response) throws IOException {
		JSONObject json = new JSONObject();
		String strReturn = "0";
		try {
			FunctionMessage fm = new FunctionMessage(1);
			fm = flowMonitorService.deleteEntrust(request.getParameter("ID"));
			if (fm.getResult()) {
				strReturn = "1";
				// response.sendRedirect("ZrWorkFlow/FlowMonitor/FlowEntrust/EntrustList.html");
			} else {
				this.Msg = fm.getMessage();
				this.isOk = fm.getResult();
				strReturn = "0";
			}
		} catch (Exception ex) {
			this.Msg = "连接文件管理中间件服务失败！<br>详细错误信息：" + ex.toString();
		}
		// this.out = response.getWriter();
		// this.returnPath = "ZrWorkFlow/FlowMonitor/FlowEntrust/EntrustList.html";
		// this.getBox();
		json.put("strReturn", strReturn);
		response.getWriter().println(json);
		response.getWriter().flush();
	}

	/**
	 * 修改委托人员
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	private void editFlowEt(HttpServletRequest request, HttpServletResponse response) throws IOException {
		JSONObject json = new JSONObject();
		String strReturn = "0";
		FunctionMessage fm = new FunctionMessage(1);
		FLOW_CONFIG_ENTRUST Entrust = new FLOW_CONFIG_ENTRUST();
		Entrust.fullDataFromRequest(request);
		try {
			fm = flowMonitorService.editEntrust(Entrust);
			if (fm.getResult()) {
				strReturn = "1";
				// response.sendRedirect("ZrWorkFlow/FlowMonitor/FlowEntrust/EntrustList.html");
			} else {
				this.Msg = fm.getMessage();
				this.isOk = fm.getResult();
				strReturn = "0";
			}
		} catch (Exception ex) {
			this.Msg = "文件管理中间件服务失败！<br>详细错误信息：" + ex.toString();
		}
		// this.out = response.getWriter();
		// this.returnPath = "ZrWorkFlow/FlowMonitor/FlowEntrust/EntrustList.html";
		// this.getBox();
		json.put("strReturn", strReturn);
		response.getWriter().println(json);
		response.getWriter().flush();
	}

	/**
	 * 修改流程包信息
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	private String editFlowpk(HttpServletRequest request, HttpServletResponse response) throws IOException {
	    FunctionMessage fm = new FunctionMessage(1);
	    Package flowPkID = new Package();
	    flowPkID.fullDataFromRequest(request);
	    try {
	      fm = flowSetupService.editFlowPk(flowPkID);
	      if (fm.getResult()) {
	        return "ZrWorkFlow/flowconfig/Other/CloseWindow";
	      } else {
	        this.Msg = fm.getMessage();
	        this.isOk = fm.getResult();
	      }
	    }
	    catch (Exception ex) {
	      this.Msg = "文件管理中间件服务失败！<br>详细错误信息：" + ex.toString();
	    }
	    this.out = response.getWriter();
	    this.returnPath = "ZrWorkFlow/flowconfig/Other/CloseWindow.html";
	    //this.getBox();
	    return "ZrWorkFlow/flowconfig/Other/CloseWindow";
	}

	/**
	 * 删除流程包
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	private String deleteFlowpk(HttpServletRequest request, HttpServletResponse response) throws IOException {
	    try {
	      String strID = request.getParameter("ID");
	      if (flowSetupService.deleteFlowPk(strID)){
	      }
	    } catch (Exception ex) {
	    	ex.printStackTrace();
	    }
	    return "redirect:/flow/openflowcfg?Act=PackageDesign";
	}

	/**
	 * 添加流程包
	 * @param UNITID
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	private String addFlowpk(String UNITID, HttpServletRequest request, HttpServletResponse response) throws IOException {
	    FunctionMessage fm = new FunctionMessage(1);
	    Package FlowPkID = new Package();
	    try {
	      FlowPkID.fullDataFromRequest(request);
	      fm = flowSetupService.addFlowPk(FlowPkID,UNITID);
	      if (fm.getResult()) {
	         FlowPkID = null;
	         return "ZrWorkFlow/flowconfig/Other/CloseWindow";
	      } else {
	         this.Msg = fm.getMessage();
	         this.isOk = fm.getResult();
	         FlowPkID = null;
	         this.returnPath = "ZrWorkFlow/flowconfig/Other/CloseWindow.html";
	         return "ZrWorkFlow/flowconfig/Other/CloseWindow";
	      }
	    } catch (Exception ex2) {
	      this.Msg = "连接客户端文件中间件服务失败！<br>详细错误信息：<br>   " + ex2.toString();
	    }
	    this.out = response.getWriter();
	    this.returnPath = "ZrWorkFlow/flowconfig/Other/CloseWindow.html";
	    //this.getBox();
	    return "ZrWorkFlow/flowconfig/Other/CloseWindow";
	}

	private void getListBt(HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		JSONObject json=new JSONObject();
	    //枚举值
		Map<String, Object> Hashtable = new HashMap<String, Object>();
	    Hashtable.put("1","功能按钮");
	    Hashtable.put("2","文档按钮");
	    Hashtable.put("3","控制按钮");
	    Hashtable.put("4","公共功能按钮");
	    Hashtable.put("5","公共已分配按钮");
	    Hashtable.put("6","流程创建人按钮");
	    
	    Map<String, Object> Hashtable1 = new HashMap<String, Object>();
	    Hashtable1.put("1","页面上");
	    Hashtable1.put("2","页面下");
	    Hashtable1.put("3","页面上和下");
	    
	    List<Object> newList = new ArrayList<Object>();
		Button[] Bt = flowSetupService.getButtonList();
		try {
			if (Bt!=null && Bt.length>0) {
			  for(int i=0;i<Bt.length;i++) {
				  Map<String, Object> map = new HashMap<String, Object>();
				  map.put("ID", Bt[i].getID());
				  map.put("BNAME", Bt[i].getBNAME());
				  map.put("NAME", Bt[i].getNAME());
				  map.put("TYPE", Hashtable.get(Bt[i].getTYPE()));
				  map.put("POSITION", Hashtable1.get(Bt[i].getPOSITION()));
				  map.put("ICO", Bt[i].getICO());
				  map.put("PROPERTY", Bt[i].getPROPERTY());
				  if (i < Bt.length-1 ){
		            map.put("CODE", Bt[i].getCODE());
				  }else{
					map.put("CODE", Bt[i].getCODE());
				  }
				  newList.add(map);
				  map = null;
			  }
			}
		}catch (Exception ex) {
			ex.printStackTrace();
		}
		json.put("rows", newList);
		json.put("total", String.valueOf(Bt.length));
		response.getWriter().println(json);
		response.getWriter().flush();
	}

	/**
	 * 修改流程包信息
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	private void editButton(HttpServletRequest request, HttpServletResponse response) throws IOException {
		JSONObject json = new JSONObject();
		String strReturn = "0";
	    FunctionMessage fm = new FunctionMessage(1);
	    Button ButtonID = new Button();
	    ButtonID.fullDataFromRequest(request);
	    try {
	      fm = flowSetupService.editButton(ButtonID);
	      if (fm.getResult()) {
	        //response.sendRedirect("ZrWorkFlow/BaseSetup/FlowBaseButton/ButtonList.html");
	        strReturn="1";
	      } else {
	    	strReturn="0";
	        this.Msg = fm.getMessage();
	        this.isOk = fm.getResult();
	      }
	    } catch (Exception ex) {
	      this.Msg = "文件管理中间件服务失败！<br>详细错误信息：" + ex.toString();
	    }
	    //this.out = response.getWriter();
	    //this.returnPath = "ZrWorkFlow/BaseSetup/FlowBaseButton/ButtonList.html";
	    //this.getBox();
	    json.put("strReturn", strReturn);
		response.getWriter().println(json);
		response.getWriter().flush();
	}

	/**
	 * 删除流程包
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	private void deleteButton(HttpServletRequest request, HttpServletResponse response) throws IOException {
		JSONObject json = new JSONObject();
		String strReturn = "0";
	    try {
	      FunctionMessage fm = new FunctionMessage(1);
	      fm = flowSetupService.deleteButton(request.getParameter("ID"));
	      if (fm.getResult()) {
	        this.Msg = fm.getMessage();
	        this.isOk = fm.getResult();
	        strReturn="1";
	      } else {
	        this.Msg = fm.getMessage();
	        this.isOk = fm.getResult();
	        strReturn="0";
	      }
	      //response.sendRedirect("ZrWorkFlow/BaseSetup/FlowBaseButton/ButtonList.html");
	    } catch (Exception ex) {
	      this.Msg = "连接文件管理中间件服务失败！<br>详细错误信息：" + ex.toString();
	    }
	    //this.out = response.getWriter();
	    //this.returnPath = "ZrWorkFlow/BaseSetup/FlowBaseButton/ButtonList.html";
	    //this.getBox();
	    json.put("strReturn", strReturn);
		response.getWriter().println(json);
		response.getWriter().flush();
	}

	/**
	 * 添加流程包
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	private void addButton(HttpServletRequest request, HttpServletResponse response) throws IOException {
		JSONObject json = new JSONObject();
	    FunctionMessage fm = new FunctionMessage(1);
	    Button ButtonID = new Button();
	    String strReturn = "0";
	    try {
	      ButtonID.fullDataFromRequest(request);
	      fm = flowSetupService.addButton(ButtonID);
	      if (fm.getResult()) {
	        ButtonID = null;
	        strReturn="1";
	        //response.sendRedirect("ZrWorkFlow/BaseSetup/FlowBaseButton/ButtonList.html");
	      } else {
	    	strReturn="0";
	        this.Msg = fm.getMessage();
	        this.isOk = fm.getResult();
	      }
	      ButtonID = null;
	    } catch (Exception ex2) {
	      strReturn="0";
	      this.Msg = "连接客户端文件中间件服务失败！<br>详细错误信息：<br>   " + ex2.toString();
	    }
	    //this.out = response.getWriter();
	    //this.returnPath = "ZrWorkFlow/BaseSetup/FlowBaseButton/ButtonList.html";
	    //this.getBox();
	    json.put("strReturn", strReturn);
		response.getWriter().println(json);
		response.getWriter().flush();
	}

	/**
	 * 修改流程管理基本属性
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	private String editProcess(HttpServletRequest request, HttpServletResponse response) throws IOException {
	    FLOW_CONFIG_PROCESS ProcessId = new FLOW_CONFIG_PROCESS();
	    ProcessId.fullDataFromRequest(request);
	    try {
	      String strFormfields = request.getParameter("formfields");
	      String strFormfields1 = request.getParameter("formfields1");
	      flowConfigService.editPr(ProcessId,strFormfields,strFormfields1);
	      
	      return "redirect:/iframe?page=ZrWorkFlow/flowconfig/Other/CloseWindow.html";
	    } catch (Exception ex) {
	      this.Msg = "文件管理中间件服务失败！<br>详细错误信息：" + ex.toString();
	    }
	    ProcessId = null;
	    this.returnPath = "ZrWorkFlow/flowconfig/Other/CloseWindow.html";
	    //this.getBox();
	    return "redirect:/iframe?page=ZrWorkFlow/flowconfig/Other/CloseWindow.html";
	}

	/**
	 * 删除流程管理基本属性
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	private String deleteProcess(HttpServletRequest request, HttpServletResponse response) throws IOException {
	    String pagepath = "";
		try {
	      String ID = request.getParameter("ID");
	      FLOW_CONFIG_PROCESS Pr = flowConfigService.getPROCESSID(ID);
	      String FLOWPACKAGE = Pr.getFLOWPACKAGE();
	      flowConfigService.deletePr(ID);
	      pagepath = "redirect:/flow/openflowcfg?Act=FlowShowDesign&PackageID="+FLOWPACKAGE;
	    } catch (Exception ex) {
	    	ex.printStackTrace();
	    	pagepath = "redirect:/flow/openflowcfg?Act=FlowShowDesign&PackageID=";
	    }
	    return pagepath;
	}

	/**
	 * 添加流程管理基本属性
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	private String addProcess(HttpServletRequest request, HttpServletResponse response) throws IOException {
	    FLOW_CONFIG_PROCESS ProcessId = new FLOW_CONFIG_PROCESS();
	    try {
	      ProcessId.fullDataFromRequest(request);
	      String strFormfields = request.getParameter("formfields");
	      String strFormfields1 = request.getParameter("formfields1");
	      flowConfigService.addPr(ProcessId,strFormfields,strFormfields1);
	      
	      ProcessId = null;
	    } catch (Exception ex) {
	    	ex.printStackTrace();
	    }
	    return "redirect:/iframe?page=ZrWorkFlow/flowconfig/Other/CloseWindow.html";
	}

	/**
	 * 修改流程管理基本属性
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	private String edit1Process(HttpServletRequest request, HttpServletResponse response) throws IOException {
	     String strconneid = request.getParameter("ID");
	     String cid = request.getParameter("CID");
	     String eid = request.getParameter("EID");
	     try {
	       String strName = request.getParameter("NAME");
	       String strdesc1 = request.getParameter("DESC1");
	       String strwhere1 = request.getParameter("WHERE1");
	       String type = request.getParameter("CONNTYPE");
	       String ISNEED = request.getParameter("ISNEED");
	       String ISATT = request.getParameter("ISATT");
	       String strFormfields = request.getParameter("formfields");
	       
	       flowConfigService.updataActivity(strconneid,strName,strdesc1,strwhere1,type,ISNEED,ISATT,strFormfields);
	     } catch (Exception ex) {
	    	 ex.printStackTrace();
	     }
	     return "redirect:/flow/openflowcfg?Act=ManageActivityConne&cid="+cid+"&eid="+eid;
	}

	/**
	 * 得到流程列表
	 * @param response
	 * @throws Exception  
	 */
	public String getflowlist(HttpServletResponse response) throws Exception {
          FLOW_CONFIG_PROCESS[] PROCESS = null;
          //得到查询的记录集
          PROCESS = flowConfigService.getFlowList1();
          String strshow_data = "";
           if(PROCESS.length>0&&PROCESS!=null){
               strshow_data = "{\"total\":" + PROCESS.length + ",\"rows\":[";
               for(int i=0;i<PROCESS.length;i++){
                      strshow_data = strshow_data + "{";
                      strshow_data = strshow_data + "\"ID\":\"" + PROCESS[i].getID()+ "\",";
                      if (i < PROCESS.length - 1) {
                          strshow_data = strshow_data + "\"NAME\":\""+ PROCESS[i].getNAME()+ "\"},\r\n";
                      } else {
                          strshow_data = strshow_data + "\"NAME\":\""+ PROCESS[i].getNAME()+ "\"}";
                      }
                }
                strshow_data = strshow_data + "]}";
              } else {
                   strshow_data = "{\"total\":0,\"rows\":[]}";
              }
            try {
            	response.getOutputStream().write(strshow_data.getBytes("UTF-8"));
                response.getOutputStream().flush();
            } catch (IOException e) {
                  e.printStackTrace();
            }
            PROCESS = null;
            return "";
	}

	public String getnewflowlist(HttpServletRequest request,HttpServletResponse response) throws Exception{
       String FID=request.getParameter("FID");
       String ID=request.getParameter("ID");
       String strResult = flowManageAct.getCtrlAbleFieldByFID(FID,ID);;
       // ctrlAbleFieldInfo="英文表名#中文表名#英文字段名#中文字段名#是#否#否#否#默认值&英文表名1#中文表名1#英文字段名#中文字段名#是#否#否#否#默认值1"
       String[] tempArray = strResult.split("&");
       String[] tempArraySub;
       // HashMap
       Map<String, String> msp = new HashMap<String, String>();
       msp.put("是", "checked='true'");
       msp.put("否", "");
       msp.put("", "");
       String strshow_data = "";
       if (tempArray != null && tempArray.length > 0) {
           strshow_data = "{\"total\":" + tempArray.length + ",\"rows\":[";
           for (int i = 0; i < tempArray.length; i++) {
                   if(tempArray[i].length()<1){
                            continue;
                   }
                   tempArraySub=tempArray[i].split("#");
                   strshow_data = strshow_data + "{";
                   strshow_data = strshow_data + "\"NAME7\":\""+tempArraySub[0]+"."+tempArraySub[2]+"\",";
                   strshow_data = strshow_data + "\"NAME1\":\"["+tempArraySub[1]+"]"+tempArraySub[3]+"\",";
                   strshow_data = strshow_data + "\"NAME2\":\""+tempArraySub[4]+"\",";
                   strshow_data = strshow_data + "\"NAME3\":\""+tempArraySub[5]+"\",";
                   strshow_data = strshow_data + "\"NAME4\":\""+tempArraySub[6]+"\",";
                   strshow_data = strshow_data + "\"NAME5\":\""+tempArraySub[7]+"\",";
                   try {
                           if (i < tempArray.length - 1) {
                                   strshow_data = strshow_data + "\"NAME6\":\""+tempArraySub[8]+"\"},\r\n";
                           } else {
                                   strshow_data = strshow_data + "\"NAME6\":\""+tempArraySub[8]+"\"}";
                           }
                   } catch (Exception exdata) {
                	   if (i < tempArray.length - 1) {
                                   strshow_data = strshow_data + "\"NAME6\":\"\"},\r\n";
                           } else {
                                   strshow_data = strshow_data + "\"NAME6\":\"\"}";
                           }
                   }
           }
           strshow_data = strshow_data + "]}";
       } else {
           strshow_data = "{\"total\":0,\"rows\":[]}";
       }
       try {
           response.getOutputStream().write(strshow_data.getBytes("UTF-8"));
           response.getOutputStream().flush();
       } catch (IOException e) {
           e.printStackTrace();
       }
       return "";
	}

	public String getFlowCSList(HttpServletRequest request, HttpServletResponse response) throws Exception{
       FLOW_CONFIG_TIME[] overTimeEntitys=null; //超时属性
       String FID=request.getParameter("FID");
       String strshow_data = "";
       String strname = "";
       overTimeEntitys = flowManageAct.getOverTimeList("  FID='"+FID+"'");
       if (overTimeEntitys!=null && overTimeEntitys.length>0){
         strshow_data = "{\"total\":" + overTimeEntitys.length + ",\"rows\":[";
         for(int i=0;i<overTimeEntitys.length;i++){
             //overTimeEntitys[i]
             if (overTimeEntitys[i].getABNORMITYID().equals("01")) {
               strname ="消息提示";
             } else {
               strname ="短信提示";
             }
             strshow_data = strshow_data + "{";
             strshow_data = strshow_data + "\"NAME5\":\""+overTimeEntitys[i].getID()+"\",";
             strshow_data = strshow_data + "\"NAME1\":\""+overTimeEntitys[i].getDAY()+"\",";
             strshow_data = strshow_data + "\"NAME2\":\""+overTimeEntitys[i].getNAME()+"\",";
             strshow_data = strshow_data + "\"NAME3\":\""+strname+"\",";
             if (i < overTimeEntitys.length - 1) {
                strshow_data = strshow_data + "\"NAME4\":\""+overTimeEntitys[i].getFREQUENCY()+"\"},\r\n";
             } else {
                strshow_data = strshow_data + "\"NAME4\":\""+overTimeEntitys[i].getFREQUENCY()+"\"}";
             }
         }
         strshow_data = strshow_data + "]}";
       } else {
         strshow_data = "{\"total\":0,\"rows\":[]}";
       }
       try {
           response.getOutputStream().write(strshow_data.getBytes("UTF-8"));
           response.getOutputStream().flush();
       } catch (IOException e) {
           e.printStackTrace();
       }
       overTimeEntitys=null;
       overTimeEntitys=null;
       return "";
	}

	/**
	 * 处理基本属性
	 * @return FunctionMessage
	 */
	private FunctionMessage saveBaseProperty() {
	    FunctionMessage fmTemp = new FunctionMessage();
	    try {
	      fmTemp = flowManageAct.saveBaseProperty(entityActivity);
	    } catch (Exception ex) {
	      fmTemp = new FunctionMessage(false, ex.getMessage());
	    }
	    return fmTemp;
	}

	/**
	 * 处理策略属性
	 * @return FunctionMessage
	 */
	private FunctionMessage saveStrategyProperty() {
	    FunctionMessage fmTemp = new FunctionMessage();
	    try {
	      fmTemp = flowManageAct.saveStrategyProperty(entityActivity);
	    } catch (Exception ex) {
	      fmTemp = new FunctionMessage(false, ex.getMessage());
	    }
	    return fmTemp;
	}

	private void saveOperateProperty() {
	    String[] BUTTONIDs = _request.getParameter("OperateProterty").split("#");
	    try {
	      flowManageAct.deleteButtonFromActivity(entityActivity.getID());
	      for (int i = 0; i < BUTTONIDs.length; i++) {
	        flowManageAct.addButtonToActivity(entityActivity.getID(), BUTTONIDs[i]);
	      }
	    } catch (Exception ex) {
	    	ex.printStackTrace();
	    }
	}

	private void saveOverTimeFieldInfo() {
	    try {
	    	flowManageAct.saveOverTimeFieldInfo(
	    		  entityActivity.getID(), _request.getParameter("OverTimeFieldInfo"));
	    } catch (Exception ex) {
	    	ex.printStackTrace();
	    }
	}

	/**
	 * 处理附件属性
	 * @return FunctionMessage
	 */
	private FunctionMessage saveAttProperty() {
	    FunctionMessage fmTemp = new FunctionMessage();
	    try {
	      fmTemp = flowManageAct.saveAttProperty(entityActivity);
	    } catch (Exception ex) {
	      fmTemp = new FunctionMessage(false, ex.getMessage());
	    }
	    return fmTemp;
	}

	private void saveCtlFieldInfo() {
	    try {
	    	flowManageAct.saveCtlFieldInfo(
	    		  entityActivity.getID(), _request.getParameter("CtlFieldInfoStr"));
	    } catch (Exception ex) {
	    	ex.printStackTrace();
	    }
	}

	private void saveUserGroupProperty() {
	    String[] UserGroupIDs = _request.getParameter("UserGroupProterty").split("#");
	    try {
	      flowManageAct.deleteUserGroupFromActivity(entityActivity.getID());
	      for (int i = 0; i < UserGroupIDs.length; i++) {
	    	  flowManageAct.addUserGroupToActivity(entityActivity.getID(), UserGroupIDs[i]);
	      }
	    } catch (Exception ex) {
	    	ex.printStackTrace();
	    }
	}
	
	public FLOW_CONFIG_ACTIVITY setReqToModel(FLOW_CONFIG_ACTIVITY entityActivity,HttpServletRequest request) {
		entityActivity.setFID(request.getParameter("FID")); // 过程ID
		entityActivity.setIDENTIFICATION(request.getParameter("IDENTIFICATION")); // 活动标识
		entityActivity.setNAME(request.getParameter("NAME")); // 活动名称
		entityActivity.setDESC1(request.getParameter("DESC1")); // 活动描述
		entityActivity.setTYPE(request.getParameter("TYPE")); // 活动类型
		entityActivity.setORDER1(request.getParameter("ORDER1") !=null ? Integer.valueOf(request.getParameter("ORDER1")) : 1);//LONG); // 活动序号
		entityActivity.setISSIGN(request.getParameter("ISSIGN")); // 会签是否顺序执行
		entityActivity.setASTRATEGY(request.getParameter("ASTRATEGY")); // 分配策略
		entityActivity.setCSTRATEGY(request.getParameter("CSTRATEGY")); // 完成策略
		entityActivity.setCNUM(request.getParameter("CNUM") !=null ? Integer.valueOf(request.getParameter("CNUM")) : 0);//LONG); // 完成数量或百分比
		entityActivity.setOPENHELP(request.getParameter("OPENHELP")); // 是否自动打开帮助
		entityActivity.setXYCSS(request.getParameter("XYCSS")); // 活动坐标值
		entityActivity.setATTTYPE(request.getParameter("ATTTYPE")); // 附件类型
		entityActivity.setATTNUM(request.getParameter("ATTNUM") !=null ? Integer.valueOf(request.getParameter("ATTNUM")) : 0);//LONG); // 需上传附件数
		entityActivity.setISNOTE(request.getParameter("ISNOTE")); // 是否短信提示
		entityActivity.setISMESSAGE(request.getParameter("ISMESSAGE")); // 是否消息提示
		entityActivity.setFORMPATH(request.getParameter("FORMPATH")); // 自定义表单页面地址
		entityActivity.setMESSAGE(request.getParameter("MESSAGE")); // 活动处理后提示消息
		entityActivity.setISSAVE1(request.getParameter("ISSAVE1")); // 前进保存数据
		entityActivity.setISSAVE2(request.getParameter("ISSAVE2")); // 后退保存数据
		entityActivity.setISLEAVE1(request.getParameter("ISLEAVE1")); // 前进必填留言
		entityActivity.setISLEAVE2(request.getParameter("ISLEAVE2")); // 后退必填留言
		entityActivity.setADDFORMPATH(request.getParameter("ADDFORMPATH")); // 活动处理前填写表单地址
		entityActivity.setADDFORMWIDTH(request.getParameter("ADDFORMWIDTH")); // 活动处理前填写表单宽度
		entityActivity.setADDFORMHEIGHT(request.getParameter("ADDFORMHEIGHT")); // 活动处理前填写表单高度
		entityActivity.setADDFORMMESSAGE(request.getParameter("ADDFORMMESSAGE")); // 活动处理前非正确填写表单提示
		entityActivity.setISBRANCH(request.getParameter("ISBRANCH")); // 多路分支是否表现在按钮上
		return entityActivity;
	}

	/**
	 * 流程移交管理
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	private void EditFlowTurn(HttpServletRequest request, HttpServletResponse response) throws IOException {
	    String strDOPSN=request.getParameter("DOPSN");
	    String strUserID=request.getParameter("UserID");
	    String strID=request.getParameter("ID");
	    JSONObject json=new JSONObject();

		String strReturn = "0";
	    try{
	      if (flowMonitorService.flowDevolveManage(strID,strDOPSN,strUserID)){
	          strReturn="1";
	          //response.sendRedirect("ZrWorkFlow/FlowMonitor/FlowToTurn/List.html");
	      }
	    } catch (Exception ex){
	      this.Msg = "流程移交管理中间件服务失败！<br>详细错误信息：" + ex.toString();
	      strReturn="0";
	    }
	    //this.out = response.getWriter();
	    //this.getBox();
	    json.put("strReturn", strReturn);
		response.getWriter().println(json);
		response.getWriter().flush();
	}

	/**
	 * 流程委托处理
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	private void EditFlowEntrus(HttpServletRequest request,HttpServletResponse response) throws IOException{
	    String strDOPSN=request.getParameter("DOPSN");
	    String strUserID=request.getParameter("UserID");
	    String strUNIT=request.getParameter("UNIT");
	    String strID=request.getParameter("ID");

	    JSONObject json=new JSONObject();
		String strReturn = "0";
	    try {
	      if (flowMonitorService.flowEntrustManage(strID,strDOPSN,strUserID,strUNIT)){
	         strReturn="1";
	      }
	    } catch (Exception ex) {
	      this.Msg = "连接中间件服务器失败，请稍后再试！";
	      strReturn="0";
	    }
	    json.put("strReturn", strReturn);
		response.getWriter().println(json);
		response.getWriter().flush();
	}

	/**
	 * 流程任务重分配处理
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	private void EditFlowEntrus1(HttpServletRequest request,HttpServletResponse response) throws IOException{
		  String strDOPSN = request.getParameter("DOPSN");
		  String strUserID = request.getParameter("UserID");
		  String strUNIT = request.getParameter("UNIT");
		  String strID = request.getParameter("ID");
		  JSONObject json = new JSONObject();

		  String strReturn = "0";
		  try {
		    if (flowMonitorService.flowAnewManage(strID,strDOPSN,strUserID,strUNIT)){
		       strReturn="1";
		    }
		  } catch (Exception ex){
		      this.Msg = "连接中间件服务器失败，请稍后再试！";
		  }
		  json.put("strReturn", strReturn);
		  response.getWriter().println(json);
		  response.getWriter().flush();
	}

}