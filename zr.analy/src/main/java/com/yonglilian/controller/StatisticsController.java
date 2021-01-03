package com.yonglilian.controller;

import com.yonglilian.analyseengine.Request;
import com.yonglilian.analyseengine.bean.JxExcel;
import com.yonglilian.analyseengine.mode.ANALYSE_STATISTICS_CCONNECTION;
import com.yonglilian.analyseengine.mode.ANALYSE_STATISTICS_CWHERE;
import com.yonglilian.analyseengine.mode.ANALYSE_STATISTICS_MAIN;
import com.yonglilian.analyseengine.mode.Variable;
import com.yonglilian.service.StatisticsConfigService;
import com.yonglilian.service.StatisticsControlPhoneService;
import com.yonglilian.service.StatisticsControlService;
import com.yonglilian.service.impl.StatisticsControlServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import zr.zrpower.common.util.FunctionMessage;
import zr.zrpower.common.util.StringUtils;
import zr.zrpower.common.util.SysPreperty;
import zr.zrpower.common.web.FileUpload;
import zr.zrpower.controller.BaseController;
import zr.zrpower.intercept.annotation.ZrSafety;
import zr.zrpower.model.SessionUser;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * 菜单信息控制器
 * @author lwk
 *
 */
@ZrSafety
@Controller
@RequestMapping("/statistics")
public class StatisticsController extends BaseController {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** 数据统计EJB服务. */
	@Autowired
	private StatisticsConfigService statisticsCfgService;
	/** 数据统计引擎服务. */
	@Autowired
	private StatisticsControlService statisticsControlService;
	/** 数据统计引擎手机服务. */
	@Autowired
	private StatisticsControlPhoneService statisticsControlPhoneService;
	/**
	 * 数据统计引擎服务
	 */
	@Autowired
    private StatisticsControlServiceImpl SControlService;
	private ANALYSE_STATISTICS_MAIN analysemianentity;

	/**
	 * 统计学行为
	 * @param request
	 * @param response
	 */
	@RequestMapping("/actionstatistics")
	public void actionAtatistics(HttpServletRequest request, HttpServletResponse response) {
	      response.setContentType("text/html;charset=UTF-8");
	      String strAct = request.getParameter("Act");
	      //--统计配置----------------------------------------------------------------
	      try {
	          analysemianentity = new ANALYSE_STATISTICS_MAIN();
	          analysemianentity.fullDataFromRequest(request);
	      } catch (NumberFormatException ex) {
	    	  ex.printStackTrace();
	      }
	      try {
	          if (strAct.equals("savemain")) {
	               saveConfigMain(); // 统计配置
	               analysemianentity = null;
	          }
	          if (strAct.equals("delmain")) {
	               delMain();        // 删除统计配置
	               analysemianentity = null;
	         }
	      } catch (Exception ex) {
	    	  ex.printStackTrace();
	      }
	}

	/**
	 * 统计学引擎
	 * @param request
	 * @param response
	 * @throws IOException 
	 */
	@RequestMapping("/statisticsengine")
	public String statisticsEngine(HttpServletRequest request, HttpServletResponse response) throws IOException {
	      response.setContentType("text/html; charset=UTF-8");
	      Request req = null;
	      try {
	    	  req = new Request();
	    	  req.fullItem(request);
	      } catch (Exception ex) {
	         response.getWriter().print("<p>req=new Request()发生异常！</p><p>详细错误信息:</p><p>"+ex.toString()+"</p>");
	      }
	      String strResult = "";
	      String ID = request.getParameter("ID");
	      if (ID == null) {
	          ID = "";
	      }
	      String SHOWTITLE = request.getParameter("SHOWTITLE");
	      if (SHOWTITLE == null) {SHOWTITLE = "";}

	      String P1 = request.getParameter("P1");
	      if (P1 == null) {
	          P1 ="";
	      }
	      String P2 = request.getParameter("P2");
	      if (P2 == null) {
	          P1 ="";
	      }
	      String P3 = request.getParameter("P3");
	      if (P3 == null) {
	          P3 ="";
	      }
	      String P4 = request.getParameter("P4");
	      if (P4 == null) {
	          P4 ="";
	      }
	      String P5 = request.getParameter("P5");
	      if (P5 == null) {
	          P5 ="";
	      }
	      String TYPE = request.getParameter("TYPE");
	      if (TYPE == null) {
	          TYPE ="";
	      }
	      //调用报表的用户类别 0 按用户的行政区划权限 1 部门领导 2 普通员工
	      String USERTYPE = request.getParameter("USERTYPE");
	      if (USERTYPE == null) {
	          USERTYPE ="2";
	      }
	      String UNITID = request.getParameter("UNITID");
	      if(UNITID==null){UNITID="";}

	      try {
	           HttpSession session = request.getSession(true);
	           SessionUser user = (SessionUser) session.getAttribute("userinfo");

	           ANALYSE_STATISTICS_MAIN Smain = statisticsControlService.getSmain(ID);

	           //------------------生成页面------------------
	           strResult = getTemplateStr(ID,P1,P2,P3,P4,P5,user,Smain,req,TYPE,USERTYPE,SHOWTITLE,UNITID);
	           if (strResult.equals("SINPUTPAGE")) {
	               //------------------转到自定义页面------------------
	               return "redirect:"+Smain.getSINPUTPAGE()+"?ID="+ID+"&P1="+P1+"&P2="+P2+"&P3="+P3+"&P4="+P4+"&P5="+P5+"&USERTYPE="+USERTYPE;
	           } else {//显示页面
	               response.getWriter().print(strResult);
	           }
	           user = null;
	           Smain = null;
	      } catch (Exception ex) {
	          response.getWriter().print("<p>发生异常！</p><p>详细错误信息:</p><p>" + ex.toString() + "</p>");
	      }
	      return null;
	}

	/**
	 * 报表计算引擎配置
	 * @param request
	 * @param response
	 */
	@RequestMapping("/getActionStatistics")
	public String getActionStatistics(HttpServletRequest request, HttpServletResponse response) {
	    response.setContentType("text/html; charset=UTF-8");
        String pagepath = "ZrAnalyseEngine/StatisticsConfig/StatisticsIndex";
	    return pagepath;
	}

	/**
	 * 统计学行为1
	 * @param request
	 * @param response
	 */
	@RequestMapping("/actionstatistics1")
	public String actionStatistics1(HttpServletRequest request, HttpServletResponse response) {
	    response.setContentType("text/html; charset=UTF-8");
	    String strTemplat = SysPreperty.getProperty().LogFilePath;
	    strTemplat = strTemplat.substring(0,strTemplat.length()-3)+"StatExcel";
	    
	    String pagepath = "";
	    //分析上传数据
	    FileUpload fileUpload = new FileUpload();
	    fileUpload.setSize(1024*1024*20);  //上传限制为20M
	    fileUpload.setObjectPath(strTemplat);

	    boolean bUploadOK = false;  //上传是否成功
	    try {
	    	fileUpload.uploadFile(request);
	      	bUploadOK = true;
	    } catch (IOException ex) {
	    	ex.printStackTrace();
	    }
	    if(bUploadOK){
	      Map<String, Object> htFields = fileUpload.getRtFields();
	      String saFile = fileUpload.getTEMPLAET();
	      String strAct = (String) htFields.get("Act");
          if (strAct.equals("reload")) {
        	  try {
                String strID = (String) htFields.get("ID");
                statisticsCfgService.setReTable(strID, saFile);
                pagepath = "ZrAnalyseEngine/StatisticsConfig/StatisticsIndex";
        	  } catch (Exception ex) {
        		  ex.printStackTrace();
        	  }
          }
	    }
	    return pagepath;
	}

	@RequestMapping("/actionstatisticsother")
	public void actionStatisticsOther(HttpServletRequest request, HttpServletResponse response) throws IOException {
	     response.setContentType("text/html; charset=UTF-8");
	     String strAct = request.getParameter("Act");
	     if (strAct != null) {
	         if (strAct.equals("savecfield")) {      //统计计算字段配置
	             saveCField(request, response);
	         }
	         else if (strAct.equals("savewhere")) {
	        	 saveWhere(request, response);        //统计条件配置
	         }
	         else if (strAct.equals("savecconnection")) {
	             saveCConnection(request, response);  //统计计算字段关联配置表
	         }
	         else if (strAct.equals("savecwhere")) {
	        	 saveCWhere(request, response);        //计算字段初始条件
	         } else {
	         }
	     }
	}

	@RequestMapping("/statisticscompute")
	public void statisticsCompute(HttpServletRequest request, HttpServletResponse response) throws IOException {
	    response.setContentType("text/html; charset=UTF-8");
	    String ID = request.getParameter("ID");
	    if (ID == null) {
	      ID = "";
	    }
	    String P1 = request.getParameter("P1");
	    if (P1 == null) {
	       P1 ="";
	    }
	    String P2 = request.getParameter("P2");
	    if (P2 == null) {
	       P1 ="";
	    }
	    String P3 = request.getParameter("P3");
	    if (P3 == null) {
	       P3 ="";
	    }
	    String P4 = request.getParameter("P4");
	    if (P4 == null) {
	       P4 ="";
	    }
	    String P5 = request.getParameter("P5");
	    if (P5 == null) {
	       P5 ="";
	    }
	    String CONN = request.getParameter("CONN");
	    if (CONN == null) {
	       CONN ="";
	    }
	    //默认单位ID，为空时统计当前单位，有值时统计指定单位
	    String UNITID = request.getParameter("UNITID");
	    if (UNITID == null){
	        UNITID ="";
	    }
	    try {
	      HttpSession session = request.getSession(true);
	      SessionUser user = (SessionUser) session.getAttribute("userinfo");
	      StringBuffer sb = new StringBuffer();
	      
	      ANALYSE_STATISTICS_MAIN Smain = null;
	      Smain = statisticsControlService.getSmain(ID);
	      //在网页上显示的表格模板
	      String strTIMETEMPLATE = Smain.getTIMETEMPLATE();
	      if (strTIMETEMPLATE==null){strTIMETEMPLATE="";}
	      
	      //计算报表
	      statisticsControlService.getCompute(ID,P1,P2,P3,P4,P5,user,CONN,UNITID);
	        sb.append("<HTML>");
	        sb.append("<HEAD>");
	        sb.append("<TITLE></TITLE>");
	        sb.append("<META HTTP-EQUIV=\"Pragma\" CONTENT=\"no-cache\">");
	        sb.append("<META http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">");
	        sb.append("</HEAD>");
	        sb.append("<BODY class=\"BodyMain\">");
	        sb.append("<FORM name=\"recordfrm\" method=\"post\" Action=\"\">");
	        sb.append("</FORM>");
	        sb.append("<script language=\"javascript\">");
	        sb.append("setTimeout('show();',100);");
	        sb.append("function show(){\r\n");
	        
            if (strTIMETEMPLATE.trim().length() > 0) {//有网页显示模板
                   sb.append("parent.document.data.location.href='/statistics/openstatistics?Act=datashowtable&ID="+ID+"&P1="+P1+"&P2="+P2+"&P3="+P3+"&P4="+P4+"&P5="+P5+"';\r\n");
            } else {
                   sb.append("parent.document.data.location.href='/iframe?page=ZrAnalyseEngine/StatisticsShow/message_ts.html';\r\n");
            }
            sb.append("parent.document.all('isexp').value='1';\r\n");
            sb.append("}\r\n");
            sb.append(" </script>");
            sb.append("</BODY>");
            sb.append("</HTML>");
            
            response.getWriter().print(sb.toString());
            user = null;
            Smain = null;
	    } catch (Exception ex) {
	    	response.getWriter().print("<p>发生异常！</p><p>详细错误信息:</p><p>" + ex.toString() + "</p>");
	    }
	}

	@RequestMapping("/statisticssubmitcompute")
	public void statisticsSubmitCompute(HttpServletRequest request, HttpServletResponse response) throws IOException {
	    response.setContentType("text/html; charset=UTF-8");
	    try {
            StringBuffer sb = new StringBuffer();
            sb.append("<HTML>");
            sb.append("<HEAD>");
            sb.append("<TITLE></TITLE>");
            sb.append("<META HTTP-EQUIV=\"Pragma\" CONTENT=\"no-cache\">");
            sb.append("<META http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">");
            sb.append("</HEAD>");
            sb.append("<BODY class=\"BodyMain\">");
            sb.append("<FORM name=\"recordfrm\" method=\"post\" Action=\"/statistics/statisticscompute\">");
            sb.append("<input type=\"hidden\" name=\"ID\" value=\"\">");
            sb.append("<input type=\"hidden\" name=\"P1\" value=\"\">");
            sb.append("<input type=\"hidden\" name=\"P2\" value=\"\">");
            sb.append("<input type=\"hidden\" name=\"P3\" value=\"\">");
            sb.append("<input type=\"hidden\" name=\"P4\" value=\"\">");
            sb.append("<input type=\"hidden\" name=\"P5\" value=\"\">");
            sb.append("<input type=\"hidden\" name=\"USERTYPE\" value=\"\">");
            sb.append("<input type=\"hidden\" name=\"CONN\" value=\"\">");
            
            sb.append("</FORM>");
            sb.append("</BODY>");
            sb.append("</HTML>");
            response.getWriter().print(sb.toString());
	    } catch (Exception ex) {
	    	response.getWriter().print("<p>发生异常！</p><p>详细错误信息:</p><p>" + ex.toString() + "</p>");
	    }
	}

	@RequestMapping("/statisticsenginephone")
	public String statisticsEnginePhone(HttpServletRequest request, HttpServletResponse response) throws IOException {
	      response.setContentType("text/html; charset=UTF-8");
	      Request req = null;
	      try {
	    	  req = new Request();
	    	  req.fullItem(request);
	      } catch (Exception ex) {
	    	  response.getWriter().print("<p>rq = new Request()发生异常！</p><p>详细错误信息:</p><p>" + ex.toString() + "</p>");
	      }
	      String strResult = "";
	      String ID = request.getParameter("ID");
	      if (ID == null) {
	          ID = "";
	      }
	      String SHOWTITLE = request.getParameter("SHOWTITLE");
	      if (SHOWTITLE == null) {SHOWTITLE = "";}

	      String P1 = request.getParameter("P1");
	      if (P1 == null) {
	          P1 ="";
	      }
	      String P2 = request.getParameter("P2");
	      if (P2 == null) {
	          P1 ="";
	      }
	      String P3 = request.getParameter("P3");
	      if (P3 == null) {
	          P3 ="";
	      }
	      String P4 = request.getParameter("P4");
	      if (P4 == null) {
	          P4 ="";
	      }
	      String P5 = request.getParameter("P5");
	      if (P5 == null) {
	          P5 ="";
	      }
	      String TYPE = request.getParameter("TYPE");
	      if (TYPE == null) {
	          TYPE ="";
	      }
	      //调用报表的用户类别 0 按用户的行政区划权限 1 部门领导 2 普通员工
	      String USERTYPE = request.getParameter("USERTYPE");
	      if (USERTYPE == null) {
	          USERTYPE ="2";
	      }
	      try {
	           HttpSession session = request.getSession(true);
	           SessionUser user = (SessionUser) session.getAttribute("userinfo");

	           ANALYSE_STATISTICS_MAIN smain = null;
	           smain = statisticsControlPhoneService.getSmain(ID);

	           //生成页面------------------HttpServletRequest request
	           strResult = getTemplateStr(ID, P1, P2, P3, P4, P5, user, smain, req, TYPE, USERTYPE, SHOWTITLE, request);
	           if (strResult.equals("SINPUTPAGE")) {
	               //转到自定义页面-------------
	               return "redirect:"+smain.getSINPUTPAGE()+"?ID="+ID+"&P1="+P1+"&P2="+P2+"&P3="+P3+"&P4="+P4+"&P5="+P5+"&USERTYPE="+USERTYPE;
	           } else {//显示页面
	               response.getWriter().print(strResult);
	           }
	           user = null;
	      } catch (Exception ex) {
	          response.getWriter().print("<p>发生异常！</p><p>详细错误信息:</p><p>" + ex.toString() + "</p>");
	      }
	      return null;
	}

	@RequestMapping("/openstatistics")
	public String openStatistics(HttpServletRequest request, HttpServletResponse response) {
		response.setContentType("text/html; charset=UTF-8");
		String pagepath = "";
		try {
		      String strAct = request.getParameter("Act");
		      HttpSession session=request.getSession(true);
		      SessionUser user = (SessionUser)session.getAttribute("userinfo");
		      //编辑统计配置---------------------------
		      if (strAct.equals("StatisticsEdit")) {
		         String ID=request.getParameter("ID");
		         ANALYSE_STATISTICS_MAIN[] STATISTICS = statisticsCfgService.getANALYSE_STATISTICS_MAIN(ID);
		         String strTemplat = SysPreperty.getProperty().LogFilePath;
		         strTemplat = strTemplat.substring(0,strTemplat.length()-3)+"StatExcel";
		         String showcode = STATISTICS[0].getSINPUTTYPE();
		         if (showcode.equals("3") || showcode.equals("B") || showcode.equals("9") || showcode.equals("A") || showcode.equals("C")){
		            showcode = "";
		         } else {
		            showcode = "none";
		         }
		         String strISZERO = STATISTICS[0].getISZERO();
		         if (strISZERO==null){strISZERO="0";}
		         STATISTICS[0].setISZERO(strISZERO);

		         ANALYSE_STATISTICS_MAIN STATISTICS1 = new ANALYSE_STATISTICS_MAIN();
		         STATISTICS1 = STATISTICS[0];

		         request.setAttribute("STATISTICS",STATISTICS1);
		         pagepath = "ZrAnalyseEngine/StatisticsConfig/StatisticsEdit";
		      }
		      //-------------------------------------
		      //打开上传统计模板-----------------------
		      else if (strAct.equals("Reload")) {
		          String ID=request.getParameter("ID");
		          request.setAttribute("ID",ID);
		          pagepath = "ZrAnalyseEngine/StatisticsConfig/Reload";
		      }
		      //生成统计默认模板-----------------------
		      else if (strAct.equals("statisticssc")) {
		          String ID=request.getParameter("ID");
		          statisticsControlService.savetemplaet(ID);
		          response.getWriter().write("{\"data\":\"success\"}");
					response.getWriter().flush();
					return null;
		      }
		      //------------------------------------
		      //删除统计-----------------------------
		      else if (strAct.equals("sdelete")) {
		          String ID=request.getParameter("ID");
		          statisticsCfgService.statisticsDelete(ID);
		          response.getWriter().write("{\"data\":\"success\"}");
					response.getWriter().flush();
					return null;
		      }
		      //------------------------------------
		      //选择表-------------------------------
		      else if (strAct.equals("TableTree")) {
		          String name=request.getParameter("name");
		          String sdate="";
		          java.util.Date dt=new java.util.Date();
		          long lg=dt.getTime();
		          Long ld=new Long(lg);
		          sdate = ld.toString();
		          request.setAttribute("name",name);
		          request.setAttribute("sdate",sdate);
		          
		          pagepath = "ZrAnalyseEngine/StatisticsConfig/TableTree";
		      }
		      //------------------------------------
		      //选择表1-------------------------------
		      else if (strAct.equals("TableTree1")) {
		          String name=request.getParameter("name");
		          String id=request.getParameter("id");
		          String win=request.getParameter("win");//关闭标记
		          String sdate="";
		          java.util.Date dt=new java.util.Date();
		          long lg=dt.getTime();
		          Long ld=new Long(lg);
		          sdate = ld.toString();
		          request.setAttribute("name",name);
		          request.setAttribute("id",id);
		          request.setAttribute("win",win);
		          request.setAttribute("sdate",sdate);
		          
		          pagepath = "ZrAnalyseEngine/StatisticsConfig/TableTree1";
		      }
		      //--------------------------------------
		      //选择字段-------------------------------
		      else if (strAct.equals("SelectField")) {
		           String LIST = "";
		           String TableName=request.getParameter("TableName");//获取的表名称
		           String name=request.getParameter("name");//对应的字段

		           String[][] FieldList = statisticsCfgService.getTableFieldList3(TableName);
		           if (FieldList != null) {
		              if (FieldList.length > 0) {
		                for (int i = 0; i < FieldList.length; i++) {
		                   if(i==0){
		                	   LIST = "<option value=''>--选择--</option>\r\n";
		                   }
		                   LIST = LIST + "<option value='"+FieldList[i][0]+"'>"+FieldList[i][1]+"</option>\r\n";
		               }
		            }
		          }
		          request.setAttribute("name",name);
		          request.setAttribute("LIST",LIST);
		          
		          pagepath = "ZrAnalyseEngine/StatisticsConfig/SelectField";
		      }
		      //---------------------------------------
		      //选择字段1-------------------------------
		      else if (strAct.equals("SelectField1")) {
		           String LIST = "";
		           String TableName=request.getParameter("TableName");//获取的表名称
		           String name=request.getParameter("name");//对应的字段
		           String id=request.getParameter("id");//对应的字段==英文
		           String win=request.getParameter("win");//关闭标记
		           
		           String[][] FieldList = statisticsCfgService.getTableFieldList3(TableName);
		           if (FieldList != null) {
		              if (FieldList.length > 0) {
		                for (int i = 0; i < FieldList.length; i++) {
		                   if(i==0){
		                         LIST = "<option value=''>--选择--</option>\r\n";
		                   }
		                   LIST = LIST + "<option value='"+FieldList[i][0]+"'>"+FieldList[i][1]+"</option>\r\n";
		               }
		            }
		          }
		          request.setAttribute("name",name);
		          request.setAttribute("LIST",LIST);
		          request.setAttribute("id",id);
		          request.setAttribute("win",win);
		          
		          pagepath = "ZrAnalyseEngine/StatisticsConfig/SelectField1";
		      }
		      //------------------------------------
		      //设置条件------------------------------
		      else if (strAct.equals("ConValueSetup")) {
		           Variable[] Variables = null;
		           Variables = statisticsCfgService.getVariableList();
		           String strResult=request.getParameter("strResult");
		           String name=request.getParameter("name");
		           String win=request.getParameter("win");
		           String LIST = "";
		           if (Variables != null) {
		             for (int i=0; i< Variables.length; i++) {
		                 LIST = LIST + "<option value=\""+Variables[i].getCODE()+"\">"+Variables[i].getNAME()+"</option>\r\n";
		             }
		           }
		           request.setAttribute("LIST",LIST);
		           request.setAttribute("Result",strResult);
		           request.setAttribute("name",name);
		           request.setAttribute("win",win);
		           
		           pagepath = "ZrAnalyseEngine/StatisticsConfig/ConValueSetup";
		       }
		       //-----------------------------------
		       //设置复杂条件-------------------------
		       else if (strAct.equals("CtlAbleFieldDValue")) {
		           String strID=request.getParameter("ID");
		           ANALYSE_STATISTICS_MAIN[] STATISTICS = statisticsCfgService.getANALYSE_STATISTICS_MAIN(strID);
		           String StrWhere = " TableName ='"+STATISTICS[0].getTABLEID()+"'";
		           String LIST = "";
		           Variable[] Variables = null;
		           Variables = statisticsCfgService.getVariableList();
		           String strResult=request.getParameter("strResult");
		           String name=request.getParameter("name");
		           if(Variables!=null) {
		             for(int i=0;i< Variables.length;i++) {
		                 LIST = LIST + "<option value=\""+Variables[i].getCODE()+"\">"+Variables[i].getNAME()+"</option>\r\n";
		             }
		           }
		           request.setAttribute("LIST",LIST);
		           request.setAttribute("Result",strResult);
		           request.setAttribute("name",name);
		           request.setAttribute("StrWhere",StrWhere);
		           
		           pagepath = "ZrAnalyseEngine/StatisticsConfig/CtlAbleFieldDValue";
		       }
		       //-----------------------------------
		       //设置关联配置-------------------------
		       else if (strAct.equals("ListSun")) {
		           String strID=request.getParameter("FID");
		           ANALYSE_STATISTICS_CCONNECTION[] cconnection = statisticsCfgService.getAnalyseCConnectionList(strID);
		           String  tablename = "",fieldname = "",tablechinese="",tableid="",fieldchinese="";
		           String  tablename1 = "",fieldname1 = "",tablechinese1="",tableid1="",fieldchinese1="";
		           String strshow_data = "";
		           String gl = "";
		           strshow_data = "";

		           if (cconnection!=null && cconnection.length>0){
		               String tmpStr = "";
		               String tmpStr1 ="";
		               strshow_data = "{\"total\":" + cconnection.length + ",\"rows\":[";
		               for(int i=0;i<cconnection.length;i++){
		                      strshow_data = strshow_data + "{";
		                      tmpStr = cconnection[i].getCFIELD();
		                      try {
		                        tablename  = tmpStr.split("\\.")[0];
		                        fieldname  = tmpStr.split("\\.")[1];
		                        tablechinese = statisticsCfgService.getAllTableFieldName("CHINESENAME","BPIP_TABLE","TABLENAME",tablename);
		                        tableid = statisticsCfgService.getAllTableFieldName("TABLEID","BPIP_TABLE","TABLENAME",tablename);
		                        fieldchinese = statisticsCfgService.getAllFieldName(tableid, fieldname);
		                      } catch (Exception ex) {
		                    	  ex.printStackTrace();
		                      }
		                      tmpStr1 = cconnection[i].getMFIELD();
		                      try {
		                        tablename1  = tmpStr1.split("\\.")[0];
		                        fieldname1  = tmpStr1.split("\\.")[1];
		                        tablechinese1 = statisticsCfgService.getAllTableFieldName("CHINESENAME","BPIP_TABLE","TABLENAME",tablename1);
		                        tableid1 = statisticsCfgService.getAllTableFieldName("TABLEID","BPIP_TABLE","TABLENAME",tablename1);
		                        fieldchinese1 = statisticsCfgService.getAllFieldName(tableid1,fieldname1);
		                      } catch (Exception ex) {
		                    	  ex.printStackTrace();
		                      }
		                      strshow_data = strshow_data + "\"f1\":\""+cconnection[i].getID()+"\",";
		                      strshow_data = strshow_data + "\"f2\":\""+tablechinese+"."+fieldchinese+"\",";
		                      strshow_data = strshow_data + "\"f3\":\""+tablechinese1+"."+fieldchinese1+"\",";
		                      gl = cconnection[i].getJOINTYPE();
		                      if (gl.equals("1")){gl="一般";}
		                      if (gl.equals("2")){gl="左外关联";}
		                      if (gl.equals("3")){gl="右外关联";}
		                      strshow_data = strshow_data + "\"f4\":\""+gl+"\",";
		                      strshow_data = strshow_data + "\"f5\":\""+cconnection[i].getCFIELD()+"\",";
		                      strshow_data = strshow_data + "\"f6\":\""+cconnection[i].getMFIELD()+"\",";
		                      if (i<cconnection.length-1){
		                         strshow_data = strshow_data + "\"f7\":\""+cconnection[i].getJOINTYPE()+"\"},\r\n";
		                      }else{
		                         strshow_data = strshow_data + "\"f7\":\""+cconnection[i].getJOINTYPE()+"\"}";
		                      }
		                  }
		                  strshow_data = strshow_data + "]}";
		               } else {
	                      strshow_data = "{\"total\":0,\"rows\":[]}";
		               }
		               request.setAttribute("ID",strID);
		               request.setAttribute("strshow_data",strshow_data);
		               
		               pagepath = "ZrAnalyseEngine/StatisticsConfig/ListSun";
		       }
		       //设置初始条件配置----------------------
		       else if (strAct.equals("ListSunWhere")) {
		               String strID=request.getParameter("ID");
		               String strTableName=request.getParameter("TableName");
		               ANALYSE_STATISTICS_CWHERE[] where = statisticsCfgService.getAnalyseCWhereList(strID);
		               ANALYSE_STATISTICS_CCONNECTION[] conn = statisticsCfgService.getAnalyseCConnectionList(strID);
		               String StrWhere = " TableName ='"+strTableName+"'";
		               String strshow_data = "";
		               String gl = "",g2 = "",g3 = "";
		               strshow_data = "";
		               if (conn!=null && conn.length>0){
		                  String tmpStr ="";
		                  String tmpStr1 ="";
		                  String tablename = "";
		                  String tablename1 = "";
		                  for(int i=0;i<conn.length;i++){
		                    tmpStr =conn[i].getCFIELD();
		                    tmpStr1 =conn[i].getMFIELD();
		                    tablename = tmpStr.split("\\.")[0];
		                    tablename1 = tmpStr1.split("\\.")[0];
		                    StrWhere += " OR TableName ='"+tablename+"'";
		                    StrWhere += " OR TableName ='"+tablename1+"'";
		                   }
		                 }
		                 if (where!=null && where.length>0){
		                    String  tablename = "",fieldname = "",tablechinese="",tableid="",fieldchinese="";
		                    String tmpStr = "";
		                    strshow_data = "{\"total\":" + where.length + ",\"rows\":[";
		                    for(int i=0;i<where.length;i++){
		                      strshow_data = strshow_data + "{";
		                      strshow_data = strshow_data + "\"f1\":\""+where[i].getID()+"\",";
		                      gl = where[i].getSLEFT();
		                      if (gl.equals("0")){gl="无";}
		                      strshow_data = strshow_data + "\"f2\":\""+gl+"\",";
		                      tmpStr = where[i].getFIELD();
		                      try {
		                        tablename  = tmpStr.split("\\.")[0];
		                        fieldname  = tmpStr.split("\\.")[1];
		                        tablechinese = statisticsCfgService.getAllTableFieldName("CHINESENAME","BPIP_TABLE","TABLENAME",tablename);
		                        tableid = statisticsCfgService.getAllTableFieldName("TABLEID","BPIP_TABLE","TABLENAME",tablename);
		                        fieldchinese = statisticsCfgService.getAllFieldName(tableid,fieldname);
		                      } catch (Exception ex) {
		                    	  ex.printStackTrace();
		                      }
		                      strshow_data = strshow_data + "\"f3\":\""+tablechinese+"."+fieldchinese+"\",";
		                      strshow_data = strshow_data + "\"f4\":\""+where[i].getSYMBOL()+"\",";
		                      strshow_data = strshow_data + "\"f5\":\""+where[i].getWHEREVALUE()+"\",";

		                      g2 = where[i].getSRIGHT();
		                      if (g2.equals("0")){g2="无";}
		                      strshow_data = strshow_data + "\"f6\":\""+g2+"\",";

		                      g3 = where[i].getLOGIC();
		                      if (g3.equals("0")){g3="无";}
		                      if (g3.equals("1")){g3="与";}
		                      if (g3.equals("2")){g3="或";}
		                      strshow_data = strshow_data + "\"f7\":\""+g3+"\",";
		                      strshow_data = strshow_data + "\"f8\":\""+where[i].getSLEFT()+"\",";
		                      strshow_data = strshow_data + "\"f9\":\""+where[i].getFIELD()+"\",";
		                      strshow_data = strshow_data + "\"f10\":\""+where[i].getSYMBOL()+"\",";
		                      strshow_data = strshow_data + "\"f11\":\""+where[i].getWHEREVALUE()+"\",";
		                      strshow_data = strshow_data + "\"f12\":\""+where[i].getSRIGHT()+"\",";
		                      if (i<where.length-1) {
		                         strshow_data = strshow_data + "\"f13\":\""+where[i].getLOGIC()+"\"},\r\n";
		                      } else {
		                         strshow_data = strshow_data + "\"f13\":\""+where[i].getLOGIC()+"\"}";
		                      }
		                    }
		                    strshow_data = strshow_data + "]}";
		                   }else{
		                    strshow_data = "{\"total\":0,\"rows\":[]}";
		               }
		               String LIST = statisticsCfgService.showTableFieldSelect(StrWhere);

		               request.setAttribute("ID",strID);
		               request.setAttribute("strshow_data",strshow_data);
		               request.setAttribute("LIST",LIST);
		               
		               pagepath = "ZrAnalyseEngine/StatisticsConfig/ListSunWhere";
		       }
		       //------------------------------------
		       //展示报表计算后的数据----------------------
		       else if (strAct.equals("datashowtable")) {
		            //统计方案配置ID
		            String strID = request.getParameter("ID");
		            if (strID == null) {
		               strID ="";
		            }
		            String P1 = request.getParameter("P1");
		            if (P1 == null) {
		               P1 ="";
		            }
		            String P2 = request.getParameter("P2");
		            if (P2 == null) {
		               P1 ="";
		            }
		            String P3 = request.getParameter("P3");
		            if (P3 == null) {
		               P3 ="";
		            }
		            String P4 = request.getParameter("P4");
		            if (P4 == null) {
		               P4 ="";
		            }
		            String P5 = request.getParameter("P5");
		            if (P5 == null) {
		               P5 ="";
		            }
		            String strHtml = statisticsControlService.getShowHtml(strID,user.getUserID());
		            ANALYSE_STATISTICS_MAIN Smain = null;
		            Smain = statisticsControlService.getSmain(strID);
		            //得到统计配置方案的名称
		            String strNAME = Smain.getSTATISTICSNAME();
		            if (strNAME==null){strNAME="";}
		            //得到单元格的详细信息链接地址
		            String strSHOWLINK = Smain.getSHOWLINK();
		            if (strSHOWLINK==null){strSHOWLINK="";}
		            //得到报表表格可显示详细列表的列号串
		            String strTiers = statisticsControlService.getTiers(strID);
		            if (strTiers==null){strTiers="";}
		            
		            request.setAttribute("ID",strID);
		            request.setAttribute("P1",P1);
		            request.setAttribute("P2",P2);
		            request.setAttribute("P3",P3);
		            request.setAttribute("P4",P4);
		            request.setAttribute("P5",P5);
		            request.setAttribute("strHtml",strHtml+"<!-- 弹出窗口 -->\r\n<div id=\"win\" ></div>");
		            request.setAttribute("strSHOWLINK",strSHOWLINK);
		            request.setAttribute("strTiers",strTiers);

		            pagepath = "ZrAnalyseEngine/StatisticsShow/datashowtable";
		       }
		       //----------------------------------------
		       //导出报表数据为excel-----------------------
		       else if (strAct.equals("TableExcel")) {
		            Date date = new Date();
		            SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");
		            String StrID = request.getParameter("ID");
		            String fileName="";
		            try {
		               ANALYSE_STATISTICS_MAIN STATISTICS = SControlService.getSmain(StrID);
		               fileName = STATISTICS.getSTATISTICSNAME() + fmt.format(date) + ".XLS";
		               JxExcel jxExcel = new JxExcel();
		               response.reset();
		               response.setContentType("application/vnd.ms-excel;charset=UTF-8");
		               response.addHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes("GBK"), "iso8859-1"));
		               ServletOutputStream os = response.getOutputStream();
		               jxExcel.WriteExcel(SControlService, os, StrID, user);
		            } catch (Exception ex) {
		               out.print("导出Excel文件出现异常" + ex.getMessage());
		            }
		       }
		       //打开报表明细-----------------------
		       else if (strAct.equals("openlink")) {
		             //统计方案配置ID
		             String strID = request.getParameter("ID");
		             String UNITID = request.getParameter("UNITID");
		             if (UNITID == null){
		                UNITID ="";
		             }
		             String TIER = request.getParameter("TIER");
		             if (TIER == null){
		                TIER ="";
		             }
		             String LINK = request.getParameter("LINK");
		             if (LINK == null){
		                LINK ="";
		             }
		             String P1 = request.getParameter("P1");
		             if (P1 == null) {
		                 P1 ="";
		             }
		             String P2 = request.getParameter("P2");
		             if (P2 == null) {
		                P1 ="";
		             }
		             String P3 = request.getParameter("P3");
		             if (P3 == null) {
		                P3 ="";
		             }
		             String P4 = request.getParameter("P4");
		             if (P4 == null) {
		                P4 ="";
		             }
		             String P5 = request.getParameter("P5");
		             if (P5 == null) {
		                P5 ="";
		             }
		             //得到要查询的详细记录的组成条件(sql)
		             String strWHRRE = statisticsControlService.getLinkWhere(strID, UNITID, TIER, P1, P2, P3, P4, P5, user);
		             request.setAttribute("LINK",LINK);
		             request.setAttribute("strWHRRE",strWHRRE);
		             
		             pagepath = "ZrAnalyseEngine/StatisticsShow/openlink";
		       }
		       //---------------------------------
		} catch (Exception ex) {
			LOGGER.error("StatisticsController.openStatistics Exception:\n", ex);
		} finally {
			if (out != null) {
				out.close();
			}
		}
		if (StringUtils.isNotBlank(pagepath)) {
			return pagepath;
		}
		return null;
	}

	///=====================================控制器接口私有方法=====================================///
	/**
	 * 保存统计配置
	 * @return FunctionMessage
	 */
	private FunctionMessage saveConfigMain() {
		FunctionMessage fmTemp = new FunctionMessage();
		try {
			fmTemp = statisticsCfgService.saveAnalyseMain(analysemianentity);
		} catch (Exception ex) {
			fmTemp = new FunctionMessage(false, ex.getMessage());
		}
		return fmTemp;
	}

	private boolean delMain() {
		boolean isOk = false;
		try {
			isOk = statisticsCfgService.statisticsDelete(analysemianentity.getID());
		} catch (Exception ex) {
			return false;
		}
		return isOk;
	}

	private String getTemplateStr(String ID, String P1, String P2, String P3, String P4, 
			String P5, SessionUser user, ANALYSE_STATISTICS_MAIN Smain, Request req, 
			String TYPE,String USERTYPE,String SHOWTITLE,String UNITID) {
	  StringBuffer sb = new StringBuffer();
	  try {
	       String sTMP = "";
	       sb.append("<HTML>");
	       sb.append("<HEAD>");
	       sb.append("<TITLE></TITLE>");
	       sb.append("<META HTTP-EQUIV=\"Pragma\" CONTENT=\"no-cache\">");
	       sb.append("<META http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">");

	       sb.append("<SCRIPT type=\"text/javascript\" src=\"/static/ZrAnalyseEngine/js/QueryEngine.js\"></SCRIPT>");
	       sb.append("<SCRIPT type=\"text/javascript\" src=\"/static/ZrAnalyseEngine/js/PrintFuncs.js\"></SCRIPT>");

	       sb.append("<link rel=\"stylesheet\" type=\"text/css\" href=\"/static/easyui/themes/default/easyui.css\">\r\n");
	       sb.append("<link rel=\"stylesheet\" type=\"text/css\" href=\"/static/easyui/themes/icon.css\">\r\n");
	       sb.append("<script type=\"text/javascript\" src=\"/static/easyui/js/jquery.min.js\"></script>\r\n");
	       sb.append("<script type=\"text/javascript\" src=\"/static/easyui/js/jquery.easyui.min.js\"></script>\r\n");
	       sb.append("<script type=\"text/javascript\" src=\"/static/easyui/js/easyui.common.js\"></script>\r\n");
	       sb.append("<script type=\"text/javascript\" src=\"/static/easyui/js/easyui-lang-zh_CN.js\"></script>\r\n");

	       sb.append("<style>\r\n");
	       sb.append("td{font-size: 12px;padding-left:10px;}\r\n");
	       sb.append("/*--------能填写项加亮边框-------*/\r\n");
	       sb.append(".input{/**编辑**/\r\n");
	       sb.append(" position: relative;\r\n");
	       sb.append(" border: 1px solid rgb(149, 184, 231);\r\n");
	       sb.append(" background-color: #fff;\r\n");
	       sb.append(" vertical-align: middle;\r\n");
	       sb.append(" display: inline-block;\r\n");
	       sb.append(" overflow: hidden;\r\n");
	       sb.append(" white-space: nowrap;\r\n");
	       sb.append(" margin: 0;\r\n");
	       sb.append(" padding: 0;\r\n");
	       sb.append(" -moz-border-radius: 5px 5px 5px 5px;\r\n");
	       sb.append(" -webkit-border-radius: 5px 5px 5px 5px;\r\n");
	       sb.append(" border-radius: 5px 5px 5px 5px;\r\n");
	       sb.append(" height: 21px;\r\n");
	       sb.append("}\r\n");
	       sb.append("</style>\r\n");
	       sb.append("</HEAD>");
	       sb.append("<BODY class=\"BodyMain\">");
	       sb.append("<FORM name=\"recordfrm\" method=\"post\" Action=\"/statistics/statisticsengine?ID="+ID+"&SHOWTITLE="+SHOWTITLE+"&UNITID="+UNITID+"&TYPE=1\">");
	       sb.append("<input type='hidden'  id='isexp' name='isexp' value='0'>");

	       //页面传递的参数
	       if (ID != null) {
	           sb.append("<input type=\"hidden\"  id=\"ID\" name=\"ID\" value=\"" + ID + "\">");
	       }
	       if (P1 == null) {
	           P1="";
	       }
	       if (P2 == null) {
	           P2="";
	       }
	       if (P3 == null) {
	           P3="";
	       }
	       if (P4 == null) {
	           P4="";
	       }
	       if (P5 == null) {
	           P5="";
	       }
	       if (USERTYPE == null) {
	           USERTYPE="";
	       }
	       sb.append("<input type=\"hidden\" id=\"P1\" name=\"P1\" value=\"" + P1 + "\">");
	       sb.append("<input type=\"hidden\" id=\"P2\" name=\"P2\" value=\"" + P2 + "\">");
	       sb.append("<input type=\"hidden\" id=\"P3\" name=\"P3\" value=\"" + P3 + "\">");
	       sb.append("<input type=\"hidden\" id=\"P4\"  name=\"P4\" value=\"" + P4 + "\">");
	       sb.append("<input type=\"hidden\" id=\"P5\" name=\"P5\" value=\"" + P5 + "\">");
	       sb.append("<input type=\"hidden\"  id=\"USERTYPE\"  name=\"USERTYPE\" value=\"" + USERTYPE + "\">");
	       sb.append("<input type=\"hidden\"  id=\"UNITID\"  name=\"UNITID\" value=\"" + UNITID + "\">");

	       //得到报表统计的自定义条件类型
	       String strSINPUTTYPE = Smain.getSINPUTTYPE();
	       //得到报表的显示类型(1一般报表 2 嵌入式报表)
	       String strISSHOWTYPE = Smain.getISSHOWTYPE();
	       //在网页上显示的表格模板
	       String strTIMETEMPLATE = Smain.getTIMETEMPLATE();
	       if (strTIMETEMPLATE==null){strTIMETEMPLATE="";}

	       String strTbutton = "";
	       strTbutton = Smain.getTBUTTON();
	       if (strTbutton==null)
	       {strTbutton="";}
	       if (strTbutton.length()==0){strTbutton="统计";}

	       //字典设置
	       String CodeHtml = "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\r\n";
	       String CODETABLE = Smain.getCODETABLE();
	       if (CODETABLE==null){CODETABLE="";}
	       //生成下拉字典
	       if (CODETABLE.length()>2)
	       {CodeHtml = statisticsControlService.getCodeHtml(CODETABLE);}

	       sb.append("<input type=\"hidden\"  id=\"SINPUTTYPE\" name=\"SINPUTTYPE\" value=\"" + strSINPUTTYPE + "\">");
	       sb.append(" <TABLE height=\"96%\" width=\"100%\" cellSpacing='0' >");

	       //非嵌入式报表时加载按钮
		   if (!strISSHOWTYPE.equals("2")){
		       //报表统计的自定义条件类型
		       if (!strSINPUTTYPE.equals("2") && !strSINPUTTYPE.equals("3")  && !strSINPUTTYPE.equals("6") && !strSINPUTTYPE.equals("7") && !strSINPUTTYPE.equals("8") && !strSINPUTTYPE.equals("9") && !strSINPUTTYPE.equals("A")  && !strSINPUTTYPE.equals("B") && !strSINPUTTYPE.equals("C"))
		       {
		             sb.append("<TR Height=30><TD align='left'>");
		       }
		       if (strSINPUTTYPE.equals("2"))//年、月、周、日、固定格式条件
		       {
		             sb.append("<TR><TD  valign=TOP>");
		             sb.append(" <DIV   style='width:100%;height:40px;overflow:auto;'>");
		             sb.append("<TABLE border='0' width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" valign=center>");
		             sb.append("<TR><TD width=20%></TD><TD width=30%></TD><TD width=20%></TD><TD width=30%></TD></TR>");
		             //加固定日期统计条件
		             sb.append(statisticsControlService.getDateHtml());
		             sb.append("</TABLE>");
		             sb.append("</DIV>");
		             sb.append("</TD></TR>");
	
		             sb.append("<TR Height=30><TD align='center'>");
		             sb.append("<a class=\"easyui-linkbutton\" data-options=\"iconCls:'icon-reload'\" href=\"javascript:void(0)\" onclick=\"javascript:Statistics();\" style=\"width:80px\">"+strTbutton+"</a>");
		       }
		       if (strSINPUTTYPE.equals("3") || strSINPUTTYPE.equals("B"))//日报固定格式条件
		       {
		             sb.append("<TR><TD  valign=TOP>");
		             sb.append(" <DIV   style='width:100%;height:40px;overflow:auto;'>");
		             //加固定日期统计条件
		             sTMP = statisticsControlService.getDateDayHtml();
		             if (CODETABLE.length()>2)
		             {
		                 sTMP = sTMP.replaceAll("width=\"750\"","width=\"850\"");
		             }
		             sb.append(sTMP);
		             sb.append("<td>"+CodeHtml+"</td><td>");
	
		             sb.append("<a class=\"easyui-linkbutton\" data-options=\"iconCls:'icon-reload'\" href=\"javascript:void(0)\" onclick=\"javascript:Statistics1();\" style=\"width:80px\">"+strTbutton+"</a>");
	
		             sb.append("&nbsp;<a class=\"easyui-linkbutton\" data-options=\"iconCls:'icon-ftl-info'\" href=\"javascript:void(0)\" onclick=\"javascript:expexcel();\" style=\"width:110px\">导出Excel</a>");
	
		             sb.append("</td><td width='80'></td>\r\n");
		             sb.append("</tr></table></DIV>");
		       }
		       if (strSINPUTTYPE.equals("9"))//年报固定格式条件
		       {
		             sb.append("<TR><TD  valign=TOP>");
		             sb.append(" <DIV   style='width:100%;height:40px;overflow:auto;'>");
		             //加固定日期统计条件
		             sTMP = statisticsControlService.getYearHtml();
		             if (CODETABLE.length()>2)
		             {
		                 sTMP = sTMP.replaceAll("width=\"700\"","width=\"800\"");
		             }
		             sb.append(sTMP);
		             sb.append("<td>"+CodeHtml+"</td><td>");
	
		             sb.append("<a class=\"easyui-linkbutton\" data-options=\"iconCls:'icon-reload'\" href=\"javascript:void(0)\" onclick=\"javascript:Statistics5();\" style=\"width:80px\">"+strTbutton+"</a>");
		             sb.append("&nbsp;<a class=\"easyui-linkbutton\" data-options=\"iconCls:'icon-ftl-info'\" href=\"javascript:void(0)\" onclick=\"javascript:expexcel();\" style=\"width:110px\">导出Excel</a>");
	
		             sb.append("</td><td width='80'></td>\r\n");
		             sb.append("</tr></table></DIV>");
		       }
		       if (strSINPUTTYPE.equals("A") || strSINPUTTYPE.equals("C"))//月报固定格式条件
		       {
		             sb.append("<TR><TD  valign=TOP>");
		             sb.append(" <DIV   style='width:100%;height:40px;overflow:auto;'>");
		             //加固定日期统计条件
		             sTMP = statisticsControlService.getMonthHtml();
		             if (CODETABLE.length()>2)
		             {
		                 sTMP = sTMP.replaceAll("width=\"700\"","width=\"800\"");
		             }
		             sb.append(sTMP);
		             sb.append("<td>"+CodeHtml+"</td><td>");
	
		             sb.append("<a class=\"easyui-linkbutton\" data-options=\"iconCls:'icon-reload'\" href=\"javascript:void(0)\" onclick=\"javascript:Statistics6();\" style=\"width:80px\">"+strTbutton+"</a>");
		             sb.append("&nbsp;<a class=\"easyui-linkbutton\" data-options=\"iconCls:'icon-ftl-info'\" href=\"javascript:void(0)\" onclick=\"javascript:expexcel();\" style=\"width:110px\">导出Excel</a>");
	
		             sb.append("</td><td width='50'></td>\r\n");
		             sb.append("</tr></table></DIV>");
		       }
		       if (strSINPUTTYPE.equals("6"))//年份比较条件(统计年份比较时使用)
		       {
		           sb.append("<TR><TD  valign=TOP>");
		           sb.append(" <DIV   style='width:100%;height:40px;overflow:auto;'>");
		           //加年份比较条件
		           sb.append(statisticsControlService.getYearComHtml());
		           sb.append("</DIV>");
		           sb.append("</TD></TR>");
	
		           sb.append("<TR Height=30><TD align='center'>");
	
		           sb.append("<a class=\"easyui-linkbutton\" data-options=\"iconCls:'icon-reload'\" href=\"javascript:void(0)\" onclick=\"javascript:Statistics2();\" style=\"width:80px\">"+strTbutton+"</a>");
		       }
		       if (strSINPUTTYPE.equals("7"))//年份月份比较条件(统计年份月份比较时使用)
		       {
		           sb.append("<TR><TD valign=TOP>");
		           sb.append(" <DIV style='width:100%;height:40px;overflow:auto;'>");
		           //加年份月份比较条件
		           sb.append(statisticsControlService.getYearMonthComHtml());
		           sb.append("</DIV>");
		           sb.append("</TD></TR>");
	
		           sb.append("<TR Height=30><TD align='center'>");
		           sb.append("<a class=\"easyui-linkbutton\" data-options=\"iconCls:'icon-reload'\" href=\"javascript:void(0)\" onclick=\"javascript:Statistics3();\" style=\"width:80px\">"+strTbutton+"</a>");
		       }
		       if (strSINPUTTYPE.equals("8"))//年份季度比较条件(统计年份季度比较时使用)
		       {
		           sb.append("<TR><TD  valign=TOP>");
		           sb.append(" <DIV   style='width:100%;height:40px;overflow:auto;'>");
		           //加年份季度比较条件
		           sb.append(statisticsControlService.getYearQuarterComHtml());
		           sb.append("</DIV>");
		           sb.append("</TD></TR>");
	
		           sb.append("<TR Height=30><TD align='center'>");
		           sb.append("<a class=\"easyui-linkbutton\" data-options=\"iconCls:'icon-reload'\" href=\"javascript:void(0)\" onclick=\"javascript:Statistics4();\" style=\"width:80px\">"+strTbutton+"</a>");
		       }
		       else if (!strSINPUTTYPE.equals("3") && !strSINPUTTYPE.equals("9") && !strSINPUTTYPE.equals("A")  && !strSINPUTTYPE.equals("B") && !strSINPUTTYPE.equals("C")){
		    	   
		          sb.append("&nbsp;<a class=\"easyui-linkbutton\" data-options=\"iconCls:'icon-ftl-info'\" href=\"javascript:void(0)\" onclick=\"javascript:expexcel();\" style=\"width:110px\">导出Excel</a>");
		          sb.append("</TD></TR>");
		       } else {
		         sb.append("</TD></TR>");
		       }
		   }
	       sb.append("<TR><TD height=\"100%\" valign=\"top\">");
	       sb.append("<DIV style='width:0px;height:0px;display:none;'>");
	       sb.append("<iframe id=computer name=computer src=\"/statistics/statisticssubmitcompute\" border=0 frameborder=0 width=1 height=1></iframe>");
	       sb.append("</DIV>");

	       if (!strSINPUTTYPE.equals("2") && !strSINPUTTYPE.equals("3")  && !strSINPUTTYPE.equals("6") && !strSINPUTTYPE.equals("7") && !strSINPUTTYPE.equals("8") && !strSINPUTTYPE.equals("9") && !strSINPUTTYPE.equals("A") && !strSINPUTTYPE.equals("B") && !strSINPUTTYPE.equals("C"))
	       {
	           if (strISSHOWTYPE.equals("2"))
	           {
	              sb.append("<iframe id=data name=data src=\"/iframe?page=ZrAnalyseEngine/StatisticsShow/message2.html\" border=0 frameborder=0 width=100% height=100%></iframe>");
	           }else{
	              sb.append("<iframe id=data name=data src=\"/iframe?page=ZrAnalyseEngine/StatisticsShow/message1.html\" border=0 frameborder=0 width=100% height=100%></iframe>");
	           }
	       } else {
	          sb.append("<iframe id=data name=data src=\"/iframe?page=ZrAnalyseEngine/StatisticsShow/message.html\" border=0 frameborder=0 width=100% height=100%></iframe>");
	       }
	       sb.append("</TD></TR>");
	       sb.append("</TABLE>");

	       sb.append("</FORM>");

	       //JS函数
	       sb.append("<script language=\"javascript\">");
	       if (!strSINPUTTYPE.equals("2") && !strSINPUTTYPE.equals("3")  && !strSINPUTTYPE.equals("6") && !strSINPUTTYPE.equals("7") && !strSINPUTTYPE.equals("8") && !strSINPUTTYPE.equals("9") && !strSINPUTTYPE.equals("A") && !strSINPUTTYPE.equals("B") && !strSINPUTTYPE.equals("C"))
	       {
	           sb.append("setTimeout('computer();',300);");
	       }
	       else if (strSINPUTTYPE.equals("B"))
	       {
	           sb.append("setTimeout('timesub();',300);");
	       }
	       else if (strSINPUTTYPE.equals("C"))
	       {
	           sb.append("setTimeout('timesub1();',300);");
	       }

	       else if (strSINPUTTYPE.equals("2")){
	         sb.append("setTimeout('Statistics();',600);");
	       }
	       else if (strSINPUTTYPE.equals("3") || strSINPUTTYPE.equals("B")){
	         sb.append("setTimeout('Statistics1();',600);");
	       }
	       else if (strSINPUTTYPE.equals("9")){
	         sb.append("setTimeout('Statistics5();',600);");
	       }
	       else if (strSINPUTTYPE.equals("A")  || strSINPUTTYPE.equals("C")){
	         sb.append("setTimeout('Statistics6();',600);");
	       }

	       else if (strSINPUTTYPE.equals("6")){
	         sb.append("setTimeout('Statistics2();',600);");
	       }
	       else if (strSINPUTTYPE.equals("7")){
	         sb.append("setTimeout('Statistics3();',600);");
	       }
	       else if (strSINPUTTYPE.equals("8")){
	         sb.append("setTimeout('Statistics4();',600);");
	       }

	       sb.append("function SetSelectValue(SelectId, DictTable){\r\n");
	       //网页形式字典选择----------------
           sb.append("var mFileUrl =\"/iframe?page=ZrCollEngine/selectDictPage.html&DictTable=\"+DictTable+\"&SelectId=\"+SelectId;\r\n");
           sb.append("var strValue=showModalDialog(mFileUrl,window,'status:no;help:no;dialogHeight:312px;dialogWidth:382px');\r\n");
           sb.append("if (strValue!=null){var arrTmp = strValue.split('/');\r\n");
           sb.append("document.getElementById(SelectId).options.length = 0;\r\n");
           sb.append("document.getElementById(SelectId).options[0] = new Option(arrTmp[1],arrTmp[0]);\r\n");
           sb.append("document.getElementById(SelectId).options[0].selected = true;\r\n");
           sb.append("document.getElementById(SelectId).options[0].className = 'selectlist';}\r\n");
	       //网页形式-----------------

	       sb.append("}\r\n");
	       sb.append("function computer(){\r\n");
	       sb.append("document.computer.location.href='/statistics/statisticscompute?ID="+ID+"&P1="+P1+"&P2="+P2+"&P3="+P3+"&P4="+P4+"&P5="+P5+"&USERTYPE="+USERTYPE+"&UNITID="+UNITID+"';\r\n");
	       sb.append("}\r\n");

	       sb.append("function timesub(){\r\n");
	       sb.append("var date1 = $(\"input[name='date1']\").val();\r\n");
	       sb.append("date1 = date1.replace('-','/');\r\n");
	       sb.append("var  a  =  new  Date(date1);\r\n");
	       sb.append("a = a.valueOf();\r\n");
	       sb.append("a = a - 1*24*60*60*1000;\r\n");
	       sb.append("a = new Date(a);\r\n");
	       sb.append("sm=a.getMonth()+1;\r\n");
	       sb.append("sd=a.getDate();\r\n");

	       sb.append("if ((a.getMonth()+1)<10){\r\n");
	       sb.append("sm='0'+sm;\r\n");
	       sb.append("}\r\n");

	       sb.append("if (a.getDate()<10){\r\n");
	       sb.append("sd='0'+sd;\r\n");
	       sb.append("}\r\n");

	       sb.append("var v1 =a.getFullYear()+'-'+sm+'-'+sd;\r\n");
	       sb.append("$(\"#date1\").textbox('setValue',v1);;\r\n");
	       sb.append("}\r\n");

	       sb.append("function timesub1(){\r\n");

	       sb.append("var year = $(\"#year1\").combobox('getValue');\r\n");
	       sb.append("var month  = $(\"#Month\").combobox('getValue');\r\n");

	       sb.append("var tdate = year+'/'+month+'/01';\r\n");
	       sb.append("var  d  =  new  Date(tdate);\r\n");
	       sb.append("var y = d.getYear();\r\n");
	       sb.append("var m = d.getMonth()+1;\r\n");

	       sb.append("if (m==1){\r\n");
	       sb.append("y = y -1;\r\n");
	       sb.append("m = 12;\r\n");
	       sb.append("}else{\r\n");
	       sb.append("m = m-1;\r\n");
	       sb.append("}\r\n");

	       sb.append("if (m<10){\r\n");
	       sb.append("m = '0'+m;\r\n");
	       sb.append("}\r\n");

	       sb.append("var zy = String(y);\r\n");
	       sb.append("var zy1 = year.substr(0,2)+zy.substr(zy.length - 2,zy.length-1);\r\n");

	       sb.append("$('#year1').combobox('setValue',zy1);\r\n");
	       sb.append("$('#Month').combobox('setValue',m);\r\n");

	       sb.append("}\r\n");

	       //导出excel
	       sb.append("function expexcel(){\r\n");
	       sb.append("var isexp = $('#isexp').val();");
	       sb.append("if (isexp=='1'){");
	       sb.append("var url='/statistics/openstatistics?Act=TableExcel&ID="+ID+"';\r\n");

	       sb.append("var name='PopWindow';\r\n");
	       sb.append("var ShowPopWindow = null;");
	       sb.append("var status='toolbar=no,location=no,directories=no,menubar=no,resizable=yes,scrollbars=yes,width=200,height=100';");
	       sb.append("ShowPopWindow = window.open(url,name,status);");
	       sb.append("ShowPopWindow.moveTo(280,200);");
	       sb.append("ShowPopWindow.focus();");
	       sb.append("}else{");
	       sb.append("alert('请先生成报表！');}");
	       sb.append("}\r\n");

	       //生成统计报表
	       sb.append("function Statistics(){\r\n");
	       //得到报表类别
	       sb.append("var type = '';\r\n");
	       sb.append("type = $(\"input[name='type']:checked\").val();\r\n");

	       sb.append("var sdate = '';\r\n");
	       sb.append("var edate = '';\r\n");
	       sb.append("var year1 =  $(\"#year1\").combobox('getValue');\r\n");
	       sb.append("var Quarter = $(\"#Quarter\").combobox('getValue');\r\n");
	       sb.append("var Month = $(\"#Month\").combobox('getValue');\r\n");
	       sb.append("var Week = $(\"#Week\").combobox('getValue');\r\n");

	       sb.append("var date1 = $(\"input[name='date1']\").val();\r\n");
	       sb.append("var date2 = $(\"input[name='date2']\").val();\r\n");
	       sb.append("var date3 = $(\"input[name='date3']\").val();\r\n");

	       //年报
	       sb.append("if (type=='1'){\r\n");
	       sb.append("sdate = year1+'-01-01';\r\n");
	       sb.append("edate = year1+'-12-31';\r\n");
	       sb.append("}\r\n");
	       //季度报
	       sb.append("if (type=='2'){\r\n");
	       sb.append("if (Quarter=='1'){\r\n");
	       sb.append("sdate = year1+'-01-01';\r\n");
	       sb.append("edate = year1+'-03-31';\r\n");
	       sb.append("}\r\n");
	       sb.append("if (Quarter=='2'){\r\n");
	       sb.append("sdate = year1+'-04-01';\r\n");
	       sb.append("edate = year1+'-06-30';\r\n");
	       sb.append("}\r\n");
	       sb.append("if (Quarter=='3'){\r\n");
	       sb.append("sdate = year1+'-07-01';\r\n");
	       sb.append("edate = year1+'-09-30';\r\n");
	       sb.append("}\r\n");
	       sb.append("if (Quarter=='4'){\r\n");
	       sb.append("sdate = year1+'-10-01';\r\n");
	       sb.append("edate = year1+'-12-31';\r\n");
	       sb.append("}\r\n");
	       sb.append("}\r\n");
	       //月报
	       sb.append("if (type=='3'){\r\n");
	       sb.append("if(Month=='01' || Month=='03' || Month=='05' || Month=='07' || Month=='08' || Month=='10' || Month=='12'){\r\n");
	       sb.append("sdate = year1+'-'+Month+'-01';\r\n");
	       sb.append("edate = year1+'-'+Month+'-31';\r\n");
	       sb.append("}\r\n");
	       sb.append("if(Month=='04' || Month=='06' || Month=='09' || Month=='11'){\r\n");
	       sb.append("sdate = year1+'-'+Month+'-01';\r\n");
	       sb.append("edate = year1+'-'+Month+'-30';\r\n");
	       sb.append("}\r\n");
	       sb.append("if(Month=='02' && RunNian(year1)==true){\r\n");
	       sb.append("sdate = year1+'-'+Month+'-02';\r\n");
	       sb.append("edate = year1+'-'+Month+'-29';\r\n");
	       sb.append("}\r\n");
	       sb.append("if(Month=='02' && RunNian(year1)==false){\r\n");
	       sb.append("sdate = year1+'-'+Month+'-02';\r\n");
	       sb.append("edate = year1+'-'+Month+'-28';\r\n");
	       sb.append("}\r\n");

	       sb.append("}\r\n");
	       //周报
	       sb.append("if (type=='4'){\r\n");
	       sb.append("var d = new   Date(year1,0,1);\r\n");
	       sb.append("d.setDate(parseInt('1065432'.charAt(d.getDay())) + Week * 7);\r\n");
	       sb.append("var fe = getFirstAndEnd(d);\r\n");
	       sb.append("sdate = fe.first.format('yyyy-MM-dd');\r\n");
	       sb.append("edate = fe.end.format('yyyy-MM-dd');\r\n");
	       sb.append("}\r\n");
	       //日报
	       sb.append("if (type=='5'){\r\n");
	       sb.append("sdate = date1;\r\n");
	       sb.append("edate = date1;\r\n");
	       sb.append("}\r\n");
	       //自定义日期
	       sb.append("if (type=='6'){\r\n");
	       sb.append("sdate = date2;\r\n");
	       sb.append("edate = date3;\r\n");
	       sb.append("}\r\n");

	       //计算报表
	       sb.append("document.data.location.href='/iframe?page=ZrAnalyseEngine/StatisticsShow/message1.html';\r\n");
	       sb.append("document.computer.location.href='/statistics/statisticscompute?ID="+ID+"&P1='+sdate+'&P2='+edate+'&P3="+P3+"&P4="+P4+"&P5="+P5+"&USERTYPE="+USERTYPE+"&UNITID="+UNITID+"';\r\n");

	       sb.append("$('#P1').val(sdate);\r\n");
	       sb.append("$('#P2').val(edate);\r\n");

	       sb.append("}\r\n");

	       //生成统计报表(日报)
	       sb.append("function Statistics1(){\r\n");

	       sb.append("var date1=$(\"input[name='date1']\").val();\r\n");

	       if (CODETABLE.length()>0)
	       {
	          sb.append("var code = $('#code').combobox('getValue');\r\n");
	          sb.append("var name = \"\";\r\n");
	          sb.append("var strs= new Array();\r\n");
	          sb.append("var strs= code.split(\"/\");\r\n");
	          sb.append("if (strs.length==2){");
	          sb.append("code = strs[0];\r\n");
	          sb.append("name = strs[1];\r\n");
	          sb.append(";\r\n");
	          sb.append("}");
	       }
	       //计算报表
	       sb.append("document.data.location.href='/iframe?page=ZrAnalyseEngine/StatisticsShow/message1.html';\r\n");
	       if (CODETABLE.length()>0)
	       {
	          sb.append("document.computer.location.href='/statistics/statisticscompute?ID="+ID+"&P1='+date1+'&P2='+code+'&P3='+name+'&P4="+P4+"&P5="+P5+"&USERTYPE="+USERTYPE+"&UNITID="+UNITID+"';\r\n");
	       }else{
	         sb.append("document.computer.location.href='/statistics/statisticscompute?ID="+ID+"&P1='+date1+'&P2="+P2+"&P3="+P3+"&P4="+P4+"&P5="+P5+"&USERTYPE="+USERTYPE+"&UNITID="+UNITID+"';\r\n");
	       }
	       sb.append("$('#P1').val(date1);\r\n");
	       if (CODETABLE.length()>0)
	       {
	           sb.append("$('#P2').val(code);\r\n");
	           sb.append("$('#P3').val(name);\r\n");
	       }
	       sb.append("}\r\n");

	       //生成统计报表(年报)
	       sb.append("function Statistics5(){\r\n");
	       sb.append("var date1 = $(\"#year1\").combobox('getValue');\r\n");

	       if (CODETABLE.length()>0)
	       {
	          sb.append("var code = $('#code').combobox('getValue');\r\n");
	          sb.append("var name = \"\";\r\n");
	          sb.append("var strs= new Array();\r\n");
	          sb.append("var strs= code.split(\"/\");\r\n");
	          sb.append("if (strs.length==2){");
	          sb.append("code = strs[0];\r\n");
	          sb.append("name = strs[1];\r\n");
	          sb.append(";\r\n");
	          sb.append("}");
	       }
	       //计算报表
	       sb.append("document.data.location.href='/iframe?page=ZrAnalyseEngine/StatisticsShow/message1.html';\r\n");

	       if (CODETABLE.length()>0) {
	         sb.append("document.computer.location.href='/statistics/statisticscompute?ID="+ID+"&P1='+date1+'&P2='+code+'&P3='+name+'&P4="+P4+"&P5="+P5+"&USERTYPE="+USERTYPE+"&UNITID="+UNITID+"';\r\n");

	       } else {
	         sb.append("document.computer.location.href='/statistics/statisticscompute?ID="+ID+"&P1='+date1+'&P2="+P2+"&P3="+P3+"&P4="+P4+"&P5="+P5+"&USERTYPE="+USERTYPE+"&UNITID="+UNITID+"';\r\n");

	       }
	       sb.append("$('#P1').val(date1);\r\n");

	       if (CODETABLE.length()>0) {
	           sb.append("$('#P2').val(code);\r\n");
	           sb.append("$('#P3').val(name);\r\n");
	       }
	       sb.append("}\r\n");

	       //生成统计报表(月报)
	       sb.append("function Statistics6(){\r\n");
	       sb.append("var date1 = $(\"#year1\").combobox('getValue');\r\n");
	       sb.append("var Month = $(\"#Month\").combobox('getValue');\r\n");

	       if (CODETABLE.length()>0) {
	          sb.append("var code = $('#code').combobox('getValue');\r\n");
	          sb.append("var name = \"\";\r\n");
	          sb.append("var strs= new Array();\r\n");
	          sb.append("var strs= code.split(\"/\");\r\n");
	          sb.append("if (strs.length==2){");
	          sb.append("code = strs[0];\r\n");
	          sb.append("name = strs[1];\r\n");
	          sb.append(";\r\n");
	          sb.append("}");
	       }

	       //计算报表
	       sb.append("document.data.location.href='/iframe?page=ZrAnalyseEngine/StatisticsShow/message1.html';\r\n");
	       if (CODETABLE.length()>0) {
	          sb.append("document.computer.location.href='/statistics/statisticscompute?ID="+ID+"&P1='+date1+'&P2='+Month+'&P3='+code+'&P4='+name+'&P5="+P5+"&USERTYPE="+USERTYPE+"&UNITID="+UNITID+"';\r\n");
	       } else {
	          sb.append("document.computer.location.href='/statistics/statisticscompute?ID="+ID+"&P1='+date1+'&P2='+Month+'&P3="+P3+"&P4="+P4+"&P5="+P5+"&USERTYPE="+USERTYPE+"&UNITID="+UNITID+"';\r\n");
	       }
	       sb.append("$('#P1').val(date1);\r\n");
	       sb.append("$('#P2').val(Month);\r\n");

	       if (CODETABLE.length()>0) {
	           sb.append("$('#P3').val(code);\r\n");
	           sb.append("$('#P4').val(name);\r\n");
	       }
	       sb.append("}\r\n");

	       //生成统计报表(年份比较)
	       sb.append("function Statistics2(){\r\n");

	       sb.append("var date1 = $(\"#date1\").combobox('getValue');\r\n");
	       sb.append("var date2 = $(\"#date2\").combobox('getValue');\r\n");

	       //计算报表
	       sb.append("document.data.location.href='/iframe?page=ZrAnalyseEngine/StatisticsShow/message1.html';\r\n");
	       sb.append("document.computer.location.href='/statistics/statisticscompute?ID="+ID+"&P1='+date1+'&P2='+date2+'&P3="+P3+"&P4="+P4+"&P5="+P5+"&USERTYPE="+USERTYPE+"&UNITID="+UNITID+"';\r\n");

	       sb.append("$('#P1').val(date1);\r\n");
	       sb.append("$('#P2').val(date2);\r\n");

	       sb.append("}\r\n");

	       //生成统计报表(年月比较)
	       sb.append("function Statistics3(){\r\n");
	       sb.append("var year1 = $(\"#date1\").combobox('getValue');\r\n");
	       sb.append("var year2 = $(\"#date2\").combobox('getValue');\r\n");

	       sb.append("var month1 = $(\"#Month1\").combobox('getValue');\r\n");
	       sb.append("var month2 = $(\"#Month2\").combobox('getValue');\r\n");

	       sb.append("var dateS1 = '';\r\n");
	       sb.append("var dateE1 = '';\r\n");
	       sb.append("var dateS2 = '';\r\n");
	       sb.append("var dateE2 = '';\r\n");
	       sb.append("if(month1=='01' || month1=='03' || month1=='05' || month1=='07' || month1=='08' || month1=='10' || month1=='12'){\r\n");
	       sb.append("dateS1 = year1+'-'+month1+'-01';\r\n");
	       sb.append("dateE1 = year1+'-'+month1+'-31';\r\n");
	       sb.append("}\r\n");
	       sb.append("if(month1=='04' || month1=='06' || month1=='09' || month1=='11'){\r\n");
	       sb.append("dateS1 = year1+'-'+month1+'-01';\r\n");
	       sb.append("dateE1 = year1+'-'+month1+'-30';\r\n");
	       sb.append("}\r\n");
	       sb.append("if(month1=='02' && RunNian(year1)==true){\r\n");
	       sb.append("dateS1 = year1+'-'+month1+'-02';\r\n");
	       sb.append("dateE1 = year1+'-'+month1+'-29';\r\n");
	       sb.append("}\r\n");
	       sb.append("if(month1=='02' && RunNian(year1)==false){\r\n");
	       sb.append("dateS1 = year1+'-'+month1+'-02';\r\n");
	       sb.append("dateE1 = year1+'-'+month1+'-28';\r\n");
	       sb.append("}\r\n");
	       sb.append("if(month2=='01' || month2=='03' || month2=='05' || month2=='07' || month2=='08' || month2=='10' || month2=='12'){\r\n");
	       sb.append("dateS2 = year2+'-'+month2+'-01';\r\n");
	       sb.append("dateE2 = year2+'-'+month2+'-31';\r\n");
	       sb.append("}\r\n");
	       sb.append("if(month2=='04' || month2=='06' || month2=='09' || month2=='11'){\r\n");
	       sb.append("dateS2 = year2+'-'+month2+'-01';\r\n");
	       sb.append("dateE2 = year2+'-'+month2+'-30';\r\n");
	       sb.append("}\r\n");
	       sb.append("if(month2=='02' && RunNian(year2)==true){\r\n");
	       sb.append("dateS2 = year2+'-'+month2+'-02';\r\n");
	       sb.append("dateE2 = year2+'-'+month2+'-29';\r\n");
	       sb.append("}\r\n");
	       sb.append("if(month2=='02' && RunNian(year2)==false){\r\n");
	       sb.append("dateS2 = year2+'-'+month2+'-02';\r\n");
	       sb.append("dateE2 = year2+'-'+month2+'-28';\r\n");
	       sb.append("}\r\n");

	       //计算报表
	       sb.append("document.data.location.href='/iframe?page=ZrAnalyseEngine/StatisticsShow/message1.html';\r\n");
	       sb.append("document.computer.location.href='/statistics/statisticscompute?ID="+ID+"&P1='+dateS1+'&P2='+dateE1+'&P3='+dateS2+'&P4='+dateE2+'&P5="+P5+"&USERTYPE="+USERTYPE+"&UNITID="+UNITID+"';\r\n");

	       sb.append("$('#P1').val(dateS1);\r\n");
	       sb.append("$('#P2').val(dateE1);\r\n");
	       sb.append("$('#P3').val(dateS2);\r\n");
	       sb.append("$('#P4').val(dateE2);\r\n");

	       sb.append("}\r\n");

	       //生成统计报表(年季度比较)
	       sb.append("function Statistics4(){\r\n");
	       sb.append("var year1 = $(\"#date1\").combobox('getValue');\r\n");
	       sb.append("var year2 = $(\"#date2\").combobox('getValue');\r\n");
	       sb.append("var Quarter1 = $(\"#Quarter1\").combobox('getValue');\r\n");
	       sb.append("var Quarter2 = $(\"#Quarter2\").combobox('getValue');\r\n");

	       sb.append("var dateS1 = '';\r\n");
	       sb.append("var dateE1 = '';\r\n");
	       sb.append("var dateS2 = '';\r\n");
	       sb.append("var dateE2 = '';\r\n");
	       sb.append("if (Quarter1=='1'){\r\n");
	       sb.append("dateS1 = year1+'-01-01';\r\n");
	       sb.append("dateE1 = year1+'-03-31';\r\n");
	       sb.append("}\r\n");
	       sb.append("if (Quarter1=='2'){\r\n");
	       sb.append("dateS1 = year1+'-04-01';\r\n");
	       sb.append("dateE1 = year1+'-06-30';\r\n");
	       sb.append("}\r\n");
	       sb.append("if (Quarter1=='3'){\r\n");
	       sb.append("dateS1 = year1+'-07-01';\r\n");
	       sb.append("dateE1 = year1+'-09-30';\r\n");
	       sb.append("}\r\n");
	       sb.append("if (Quarter1=='4'){\r\n");
	       sb.append("dateS1 = year1+'-10-01';\r\n");
	       sb.append("dateE1 = year1+'-12-31';\r\n");
	       sb.append("}\r\n");
	       sb.append("if (Quarter2=='1'){\r\n");
	       sb.append("dateS2 = year2+'-01-01';\r\n");
	       sb.append("dateE2 = year2+'-03-31';\r\n");
	       sb.append("}\r\n");
	       sb.append("if (Quarter2=='2'){\r\n");
	       sb.append("dateS2 = year2+'-04-01';\r\n");
	       sb.append("dateE2 = year2+'-06-30';\r\n");
	       sb.append("}\r\n");
	       sb.append("if (Quarter2=='3'){\r\n");
	       sb.append("dateS2 = year2+'-07-01';\r\n");
	       sb.append("dateE2 = year2+'-09-30';\r\n");
	       sb.append("}\r\n");
	       sb.append("if (Quarter2=='4'){\r\n");
	       sb.append("dateS2 = year2+'-10-01';\r\n");
	       sb.append("dateE2 = year2+'-12-31';\r\n");
	       sb.append("}\r\n");

	       //计算报表
	       sb.append("document.data.location.href='/iframe?page=ZrAnalyseEngine/StatisticsShow/message1.html';\r\n");
	       sb.append("document.computer.location.href='/statistics/statisticscompute?ID="+ID+"&P1='+dateS1+'&P2='+dateE1+'&P3='+dateS2+'&P4='+dateE2+'&P5="+P5+"&USERTYPE="+USERTYPE+"&UNITID="+UNITID+"';\r\n");

	       sb.append("$('#P1').val(dateS1);\r\n");
	       sb.append("$('#P2').val(dateE1);\r\n");
	       sb.append("$('#P3').val(dateS2);\r\n");
	       sb.append("$('#P4').val(dateE2);\r\n");

	       sb.append("}\r\n");

	       //检查输入年份的有效性JS
	       sb.append("function CheckYear(){\r\n");
	       sb.append("var objSrc = event.srcElement;\r\n");
	       sb.append("if (objSrc.tagName == 'INPUT'){\r\n");
	       sb.append("if(event.keyCode >= 48 && event.keyCode <= 57){return true;}else{return false;}}\r\n");
	       sb.append("return false;}\r\n");
	       //取得是否为润年
	       sb.append("function RunNian(The_Year){\r\n");
	       sb.append("if ((The_Year%400==0) || ((The_Year%4==0) && (The_Year%100!=0)))\r\n");
	       sb.append("return true;\r\n");
	       sb.append("else\r\n");
	       sb.append("return false;\r\n");
	       sb.append("}\r\n");
	       //根据年和周得到开始时间和结束时间相关-----------------
	       sb.append("Date.prototype.getWeek = function(flag)\r\n");
	       sb.append("{\r\n");
	       sb.append("var first=new Date(this.getFullYear(),0,1);\r\n");
	       sb.append("var n=parseInt('1065432'.charAt(first.getDay()));\r\n");
	       sb.append("n=this.getTime()-first.getTime()-n*24*60*60*1000;\r\n");
	       sb.append("n=Math.ceil(n/(7*24*60*60*1000));\r\n");
	       sb.append("return (flag==true&&first.getDay()!=1)?(n+1):n;\r\n");
	       sb.append("}\r\n");
	       sb.append("Date.prototype.format = function(format)\r\n");
	       sb.append("{\r\n");
	       sb.append("var o = {");
	       sb.append("'M+':this.getMonth()+1,");
	       sb.append("'d+':this.getDate(),");
	       sb.append("'h+':this.getHours(),");
	       sb.append("'m+':this.getMinutes(),");
	       sb.append("'s+':this.getSeconds(),");
	       sb.append("'q+':Math.floor((this.getMonth()+3)/3),");
	       sb.append("'S':this.getMilliseconds()");
	       sb.append("}\r\n");
	       sb.append("if(/(y+)/.test(format))  format=format.replace(RegExp.$1,");
	       sb.append("(this.getFullYear()+'').substr(4  - RegExp.$1.length));");
	       sb.append("for(var k in o)if(new  RegExp('('+ k +')').test(format))");
	       sb.append("format = format.replace(RegExp.$1,");
	       sb.append("RegExp.$1.length==1 ? o[k]:");
	       sb.append("('00'+ o[k]).substr((''+ o[k]).length));\r\n");
	       sb.append("return format;\r\n");
	       sb.append("}\r\n");
	       sb.append("function getFirstAndEnd(d)\r\n");
	       sb.append("{\r\n");
	       sb.append("var w=d.getDay(),n = 24*60*60*1000;\r\n");
	       sb.append("var first = new Date(d.getTime() - parseInt('6012345'.charAt(w))*n);\r\n");
	       sb.append("var end = new Date(d.getTime() + parseInt('0654321'.charAt(w))*n);\r\n");
	       sb.append("return {first:first,end:end};\r\n");
	       sb.append("}\r\n");
	       //根据年和周得到开始时间和结束时间相关结束-----------
	       sb.append(" </script>");
	       sb.append("</BODY>");
	       sb.append("</HTML>");
	  }
	  catch (Exception ex) {
	      return "";
	  }
	  return sb.toString();
	}

	/**
	 * 保存计算字段配置
	 * @return FunctionMessage
	 */
	private void saveCField(HttpServletRequest request, HttpServletResponse response) throws IOException {
		try {
			String strFID = request.getParameter("FID");
			String SaveAnalyseCFieldStr  = request.getParameter("AnalyseCFieldStr");
			statisticsCfgService.saveAnalyseCFieldInfo(strFID,SaveAnalyseCFieldStr);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * 保存统计条件配置
	 * @return FunctionMessage
	 */
	private void saveWhere(HttpServletRequest request, HttpServletResponse response) throws IOException {
		try {
			String strFID = request.getParameter("FID");
			String saveAnalyseWhereStr  = request.getParameter("AnalyseWhereStr");
			statisticsCfgService.saveAnalyseWhereInfo(strFID, saveAnalyseWhereStr);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * 统计计算字段关联配置表
	 * @return FunctionMessage
	 */
	private void saveCConnection(HttpServletRequest request, HttpServletResponse response) throws IOException {
         try {
        	 String strFID = request.getParameter("FID");
        	 String saveAnalyseCConnectionStr  = request.getParameter("AnalyseCConnectionStr");
        	 statisticsCfgService.saveAnalyseCConnectionInfo(strFID, saveAnalyseCConnectionStr);
         } catch (Exception ex) {
        	 ex.printStackTrace();
         }
	}

	/**
	 * 保存计算字段初始条件
	 * @return FunctionMessage
	 */
	private void saveCWhere(HttpServletRequest request, HttpServletResponse response) throws IOException {
		try {
			String strFID = request.getParameter("FID");
			String SaveAnalyseCWhereStr  = request.getParameter("AnalyseCWhereStr");
			statisticsCfgService.saveAnalyseCWhereInfo(strFID,SaveAnalyseCWhereStr);
          } catch (Exception ex) {
        	  ex.printStackTrace();
          }
	}

	private String getTemplateStr(String ID, String P1, String P2, String P3, String P4, 
			String P5, SessionUser user, ANALYSE_STATISTICS_MAIN Smain, Request req, 
			String TYPE, String USERTYPE, String SHOWTITLE, HttpServletRequest request) {
	  StringBuffer sb = new StringBuffer();
	  String title=Smain.getSTATISTICSNAME();//报表标题
	  try {
	       sb.append("<!doctype html>\r\n");
	       sb.append("<html class=\"no-js\">\r\n");
	       sb.append("<head>\r\n");
	       sb.append("<meta charset=\"utf-8\">\r\n");
	       sb.append("<title>"+title+"</title>\r\n");
	       sb.append("<meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\r\n");
	       sb.append("<meta name=\"description\" content=\"\">\r\n");
	       sb.append("<meta name=\"keywords\" content=\"\">\r\n");
	       sb.append("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0\">\r\n");
	       sb.append("<meta name=\"apple-touch-fullscreen\" content=\"yes\">\r\n");
	       sb.append("<meta name=\"x5-fullscreen\" content=\"true\">\r\n");
	       sb.append("<!-- Set render engine for 360 browser -->\r\n");
	       sb.append("<meta name=\"renderer\" content=\"webkit\">\r\n");
	       sb.append("<!-- No Baidu Siteapp-->\r\n");
	       sb.append("<meta http-equiv=\"Cache-Control\" content=\"no-siteapp\"/>\r\n");
	       sb.append("<!-- Add to homescreen for Chrome on Android -->\r\n");
	       sb.append("<meta name=\"mobile-web-app-capable\" content=\"yes\">\r\n");
	       sb.append("<!-- Add to homescreen for Safari on iOS -->\r\n");
	       sb.append("<meta name=\"apple-mobile-web-app-capable\" content=\"yes\">\r\n");
	       sb.append("<meta name=\"apple-mobile-web-app-status-bar-style\" content=\"black\">\r\n");
	       sb.append("<meta name=\"apple-mobile-web-app-title\" content=\"Amaze UI\"/>\r\n");
	       sb.append("<!-- Tile icon for Win8 (144x144 + tile color) -->\r\n");
	       sb.append("<meta name=\"msapplication-TileImage\" content=\"/static/assets/i/app-icon72x72@2x.png\">\r\n");
	       sb.append("<meta name=\"msapplication-TileColor\" content=\"#0e90d2\">\r\n");
	       sb.append("<link rel=\"icon\" type=\"image/png\" href=\"/static/assets/i/favicon.png\">\r\n");
	       sb.append("<link rel=\"icon\" sizes=\"192x192\" href=\"/static/assets/i/app-icon72x72@2x.png\">\r\n");
	       sb.append("<link rel=\"apple-touch-icon-precomposed\" href=\"/static/assets/i/app-icon72x72@2x.png\">\r\n");
	       sb.append("<link rel=\"stylesheet\" href=\"/static/assets/font_icon/css/font-awesome.css\">\r\n");
	       sb.append("<link rel=\"stylesheet\" href=\"/static/assets/css/amazeui.min.css\">\r\n");
	       sb.append("<link rel=\"stylesheet\" href=\"/static/assets/css/app.css\">\r\n");
	       sb.append("<link rel=\"stylesheet\" href=\"/static/assets/css/amazeui.datetimepicker.css\"/>\r\n");
	       sb.append("<script src=\"/static/assets/js/jquery.min.js\"></script>\r\n");
	       sb.append("<script src='/static/assets/js/amazeui.min.js'></script>\r\n");
	       sb.append("<script type=\"text/javascript\" src=\"/static/ZrPhoneEngine/js/QueryEngine.js\"></script>\r\n");
	       sb.append("<script type=\"text/javascript\" src=\"/static/Zrsysmanage/script/Public.js\" type=\"text/jscript\"></script>\r\n");
	       sb.append("<style type='text/css'>\r\n");
	       sb.append(".NoNewline\r\n");
	       sb.append(" {\r\n");
	       sb.append(" word-break: keep-all;/*必须*/\r\n");
	       sb.append(" white-space: nowrap; //不换行  \r\n");
	       sb.append(" text-overflow: ellipsis; //超出部分用....代替  \r\n");
	       sb.append(" overflow: hidden; width:80px;//超出隐藏  \r\n");
	       sb.append(" }\r\n");
	       sb.append("</style> \r\n");

	       sb.append("<script type=\"text/javascript\">\r\n");
	       sb.append("</script>\r\n");

	       sb.append("</head>\r\n");
	       sb.append("<body>\r\n");
	       ////是头部
	       sb.append("<!-- header  start -->\r\n");
	       sb.append("<header data-am-widget=\"header\" class=\"am-header am-header-default am-header-fixed\">\r\n");
	       sb.append("    <div class=\"am-header-left am-header-nav\">\r\n");
	       sb.append("        <a href=\"javascript:history.go(-1);\" class=\"\">\r\n");
	       sb.append("              <i class=\"am-header-icon am-icon-chevron-left\"></i>\r\n");
	       sb.append("        </a>\r\n");
	       sb.append("    </div>\r\n");
	       sb.append("    <h1 class=\"am-header-title\" id=\"m_t\">\r\n");
	       sb.append("        <a href=\"javascript:void(0)\" class=\"\">"+title+"</a>\r\n");
	       sb.append("    </h1>\r\n");
	       sb.append("    <div class=\"am-header-right am-header-nav\">\r\n");
	       sb.append("        <a href=\"javascript:void(0)\" class=\"am-menu-toggle\">\r\n");
	       sb.append("              <i class=\"am-menu-toggle-icon am-icon-search\"></i>\r\n");
	       sb.append("        </a>\r\n");
	       sb.append("    </div>\r\n");
	       sb.append(" </header>\r\n");
	       sb.append("<!-- header  end -->\r\n");

	       sb.append("<!-- main  start -->\r\n");
	       sb.append("<div class=\"main-contor\">\r\n");
	       sb.append("    <!-- 侧边栏内容   开始 -->\r\n");
	       sb.append("<form name=\"recordfrm\" method=\"post\" Action=\"/statistics/statisticsenginephone?ID="+ID+"&SHOWTITLE="+SHOWTITLE+"&TYPE=1\">\r\n");

	       //页面传递的参数
	       sb.append("<input type='hidden'  id='isexp' name='isexp' value='0'>\r\n");
	       if (ID != null) {sb.append("<input type=\"hidden\"  id=\"ID\" name=\"ID\" value=\"" + ID + "\">\r\n");}
	       if (P1 == null) {P1="";}if (P2 == null) {P2="";}
	       if (P3 == null) {P3="";}if (P4 == null) {P4="";}
	       if (P5 == null) {P5="";}if (USERTYPE == null) {USERTYPE="";}
	       sb.append("<input type=\"hidden\" id=\"P1\" name=\"P1\" value=\"" + P1 + "\">\r\n");
	       sb.append("<input type=\"hidden\" id=\"P2\" name=\"P2\" value=\"" + P2 + "\">\r\n");
	       sb.append("<input type=\"hidden\" id=\"P3\" name=\"P3\" value=\"" + P3 + "\">\r\n");
	       sb.append("<input type=\"hidden\" id=\"P4\"  name=\"P4\" value=\"" + P4 + "\">\r\n");
	       sb.append("<input type=\"hidden\" id=\"P5\" name=\"P5\" value=\"" + P5 + "\">\r\n");
	       sb.append("<input type=\"hidden\"  id=\"USERTYPE\"  name=\"USERTYPE\" value=\"" + USERTYPE + "\">\r\n");

	       //得到报表统计的自定义条件类型
	       String strSINPUTTYPE = Smain.getSINPUTTYPE();
	       //在网页上显示的表格模板
	       String strTIMETEMPLATE = Smain.getTIMETEMPLATE();
	       if (strTIMETEMPLATE==null){strTIMETEMPLATE="";}
	       String strTbutton = "";
	       strTbutton = Smain.getTBUTTON();
	       if (strTbutton==null){strTbutton="";}
	       if (strTbutton.length()==0){strTbutton="计算统计";}

	       //字典设置
	       String CodeHtml = "";
	       String CODETABLE = Smain.getCODETABLE();
	       if (CODETABLE==null){CODETABLE="";}
	       //生成下拉字典
	       if (CODETABLE.length()>2){CodeHtml = statisticsControlPhoneService.getCodeHtml(CODETABLE);}
	       sb.append("<input type=\"hidden\"  id=\"SINPUTTYPE\" name=\"SINPUTTYPE\" value=\"" + strSINPUTTYPE + "\">\r\n");

	       sb.append("<nav data-am-widget=\"menu\" class=\"am-menu  am-menu-offcanvas1\"  data-am-menu-offcanvas> \r\n");
	       sb.append("<div class=\"am-offcanvas\" >\r\n");
	       sb.append("  <div class=\"am-offcanvas-bar\">\r\n");
	       sb.append("  <ul class=\"am-menu-nav am-avg-sm-1\">\r\n");

	       if (!strSINPUTTYPE.equals("2") && !strSINPUTTYPE.equals("3") && !strSINPUTTYPE.equals("6") && !strSINPUTTYPE.equals("7") && !strSINPUTTYPE.equals("8") && !strSINPUTTYPE.equals("9") && !strSINPUTTYPE.equals("A")  && !strSINPUTTYPE.equals("B") && !strSINPUTTYPE.equals("C"))
	       {
	             sb.append("<TR Height=30><TD align='left'>");
	       }
	       else if (strSINPUTTYPE.equals("2"))//年、月、周、日、固定格式条件
	       {
	    	 //加固定日期统计条件
	    	   sb.append(statisticsControlPhoneService.getDateHtml());
	           sb.append("     <li class=\"am-parent\">\r\n");
	           sb.append("       <ul class=\"am-pagination\">\r\n");
	           sb.append("         <li style=\"display: inherit;width: 99%;text-align: center;\"><a href=\"javascript:Statistics();\" class=\"am-active\" >"+strTbutton+"</a></li>\r\n");
	           sb.append("       </ul>\r\n");
	           sb.append("     </li>\r\n");
	       }
	       //日报固定格式条件
	       else if (strSINPUTTYPE.equals("3") || strSINPUTTYPE.equals("B")){
	             sb.append(statisticsControlPhoneService.getDateDayHtml());
	             //下拉
	             if(CodeHtml.length()>10){
	            	 sb.append("<li class='am-parent'>\r\n");
	            	 sb.append("<a href='##' class='' >其它条件</a>\r\n");
	            	 sb.append("  <ul class='am-menu-sub am-collapse  am-avg-sm-1 '>\r\n");
	            	 sb.append("     <li class=''>\r\n");
	            	 sb.append("        "+CodeHtml+"'/>\r\n");
	            	 sb.append("      </li>\r\n");
	            	 sb.append("  </ul>\r\n");
	            	 sb.append(" </li>\r\n");
	             }
	             sb.append("     <li class=\"am-parent\">\r\n");
	             sb.append("       <ul class=\"am-pagination\">\r\n");
	             sb.append("         <li style=\"display: inherit;width: 99%;text-align: center;\"><a href=\"javascript:Statistics1();\" class=\"am-active\" >"+strTbutton+"</a></li>\r\n");
	             sb.append("       </ul>\r\n");
	             sb.append("     </li>\r\n");
	       }
	       //年报固定格式条件
	       else if (strSINPUTTYPE.equals("9")){
	    	     sb.append("<li class='am-parent'>\r\n");
	      	     sb.append("<a href='##' class='' >年份</a>\r\n");
	      	     sb.append("  <ul class='am-menu-sub am-collapse  am-avg-sm-1 '>\r\n");
	      	     sb.append("     <li class=''>\r\n");
	      	     sb.append(statisticsControlPhoneService.getYearHtml());
	      	     sb.append("      </li>\r\n");
	      	     sb.append("  </ul>\r\n");
	      	     sb.append(" </li>\r\n");

	             //下拉
	             if(CodeHtml.length()>10){
	            	 sb.append("<li class='am-parent'>\r\n");
	            	 sb.append("<a href='##' class='' >其它条件</a>\r\n");
	            	 sb.append("  <ul class='am-menu-sub am-collapse  am-avg-sm-1 '>\r\n");
	            	 sb.append("     <li class=''>\r\n");
	            	 sb.append("        "+CodeHtml+"'/>\r\n");
	            	 sb.append("      </li>\r\n");
	            	 sb.append("  </ul>\r\n");
	            	 sb.append(" </li>\r\n");
	             }
	             sb.append("     <li class=\"am-parent\">\r\n");
	             sb.append("       <ul class=\"am-pagination\">\r\n");
	             sb.append("         <li style=\"display: inherit;width: 99%;text-align: center;\"><a href=\"javascript:Statistics5();\" class=\"am-active\" >"+strTbutton+"</a></li>\r\n");
	             sb.append("       </ul>\r\n");
	             sb.append("     </li>\r\n");
	       }
	       //月报固定格式条件
	       else if (strSINPUTTYPE.equals("A")  || strSINPUTTYPE.equals("C")){
	             sb.append("<li class='am-parent'>\r\n");
	      	     sb.append("<a href='##' class='' >年月份</a>\r\n");
	      	     sb.append("  <ul class='am-menu-sub am-collapse  am-avg-sm-1 '>\r\n");
	      	     sb.append("     <li class=''>\r\n");
	      	     sb.append(statisticsControlPhoneService.getMonthHtml());
	      	     sb.append("      </li>\r\n");
	      	     sb.append("  </ul>\r\n");
	      	     sb.append(" </li>\r\n");

	             //下拉
	             if(CodeHtml.length()>10){
	            	 sb.append("<li class='am-parent'>\r\n");
	            	 sb.append("<a href='##' class='' >其它条件</a>\r\n");
	            	 sb.append("  <ul class='am-menu-sub am-collapse  am-avg-sm-1 '>\r\n");
	            	 sb.append("     <li class=''>\r\n");
	            	 sb.append("        "+CodeHtml+"'/>\r\n");
	            	 sb.append("      </li>\r\n");
	            	 sb.append("  </ul>\r\n");
	            	 sb.append(" </li>\r\n");
	             }
	             sb.append("     <li class=\"am-parent\">\r\n");
	             sb.append("       <ul class=\"am-pagination\">\r\n");
	             sb.append("         <li style=\"display: inherit;width: 99%;text-align: center;\"><a href=\"javascript:Statistics6();\" class=\"am-active\" >"+strTbutton+"</a></li>\r\n");
	             sb.append("       </ul>\r\n");
	             sb.append("     </li>\r\n");
	       }
	       //年份比较条件(统计年份比较时使用)
	       else if (strSINPUTTYPE.equals("6")){
	           sb.append("<li class='am-parent'>\r\n");
	    	     sb.append("<a href='##' class='' >年份比较</a>\r\n");
	    	     sb.append("  <ul class='am-menu-sub am-collapse  am-avg-sm-1 '>\r\n");
	    	     sb.append("     <li class=''>\r\n");
	    	     sb.append(statisticsControlPhoneService.getYearComHtml());
	    	     sb.append("      </li>\r\n");
	    	     sb.append("  </ul>\r\n");
	    	     sb.append(" </li>\r\n");

	           sb.append("     <li class=\"am-parent\">\r\n");
	           sb.append("       <ul class=\"am-pagination\">\r\n");
	           sb.append("         <li style=\"display: inherit;width: 99%;text-align: center;\"><a href=\"javascript:Statistics2();\" class=\"am-active\" >"+strTbutton+"</a></li>\r\n");
	           sb.append("       </ul>\r\n");
	           sb.append("     </li>\r\n");
	       }
	       //年份月份比较条件(统计年份月份比较时使用)
	       else if (strSINPUTTYPE.equals("7")){
	           sb.append("<li class='am-parent'>\r\n");
	  	       sb.append("<a href='##' class='' >年月比较</a>\r\n");
	  	       sb.append("  <ul class='am-menu-sub am-collapse  am-avg-sm-1 '>\r\n");
	  	       sb.append("     <li class=''>\r\n");
	  	       sb.append(statisticsControlPhoneService.getYearMonthComHtml());
	  	       sb.append("      </li>\r\n");
	  	       sb.append("  </ul>\r\n");
	  	       sb.append(" </li>\r\n");

	           sb.append("     <li class=\"am-parent\">\r\n");
	           sb.append("       <ul class=\"am-pagination\">\r\n");
	           sb.append("         <li style=\"display: inherit;width: 99%;text-align: center;\"><a href=\"javascript:Statistics3();\" class=\"am-active\" >"+strTbutton+"</a></li>\r\n");
	           sb.append("       </ul>\r\n");
	           sb.append("     </li>\r\n");
	       }
	       //年份季度比较条件(统计年份季度比较时使用)
	       else if (strSINPUTTYPE.equals("8")){
	    	   sb.append("<li class='am-parent'>\r\n");
	  	       sb.append("<a href='##' class='' >年季比较</a>\r\n");
	  	       sb.append("  <ul class='am-menu-sub am-collapse  am-avg-sm-1 '>\r\n");
	  	       sb.append("     <li class=''>\r\n");
	  	       sb.append(statisticsControlPhoneService.getYearQuarterComHtml());
	  	       sb.append("      </li>\r\n");
	  	       sb.append("  </ul>\r\n");
	  	       sb.append(" </li>\r\n");

	           sb.append("     <li class=\"am-parent\">\r\n");
	           sb.append("       <ul class=\"am-pagination\">\r\n");
	           sb.append("         <li style=\"display: inherit;width: 99%;text-align: center;\"><a href=\"javascript:Statistics4();\" class=\"am-active\" >"+strTbutton+"</a></li>\r\n");
	           sb.append("       </ul>\r\n");
	           sb.append("     </li>\r\n");
	       }
	       sb.append("  </ul>\r\n");
	       sb.append("  </div>\r\n");
	       sb.append("</div>\r\n");
	       sb.append("</nav>\r\n");
	       sb.append("</form></div>");
	       sb.append("<!-- 左边栏查询   结束 -->\r\n");

	       //得到结果
	       String UNITID=request.getParameter("UNITID");
	       if(UNITID==null){UNITID="";}
	       statisticsControlPhoneService.getCompute(ID,P1,P2,P3,P4,P5,user,"",UNITID);
	       //计算报表
	       String strHtml = statisticsControlPhoneService.getShowHtml(ID,user.getUserID());
	       sb.append("<!-- 内容栏目  开始 -->\r\n");
	       sb.append("<div data-am-widget=\"slider\" class=\"am-slider am-slider-b1\" data-am-slider='{&quot;controlNav&quot;:false}' >\r\n");
	       sb.append("<ul class=\"am-slides\" id='tab_list'>\r\n");
	       sb.append("<li></li></ul>\r\n");
	       //查询结果显示
	       sb.append(strHtml);
	       sb.append("</ul>\r\n");
	       sb.append("</div>\r\n");
	       sb.append("<!-- 内容栏目  结束 -->\r\n");
	       sb.append("</div>\r\n");
	       sb.append("<!-- main  end -->\r\n");

	       sb.append("<script type=\"text/javascript\" src=\"/static/ZrPhoneEngine/js/input_table.js\"></script>\r\n");
	       sb.append("<script src=\"/static/assets/js/amazeui.min.js\"></script>\r\n");
	       sb.append("<script src=\"/static/assets/js/amazeui.datetimepicker.min.js\"></script>\r\n");
	       sb.append("<script src=\"/static/assets/js/locales/amazeui.datetimepicker.zh-CN.js\"></script>\r\n");
	       sb.append("<script type=\"text/javascript\">\r\n");
	       sb.append("_$set_htmlTab_statis();//加载数据\r\n");
	       sb.append("//停止列表自动播放\r\n");
	       sb.append("$(function() {\r\n");
	       sb.append("  $('.am-slider').flexslider('stop');\r\n");
	       sb.append("});\r\n");
	       sb.append(" //去除侧边框闪退\r\n");
	       sb.append("$('.am-menu-toggle').on('click', function() {\r\n");
	       sb.append("	$(\".am-offcanvas\").on(\"open.offcanvas.amui\",\r\n");
	       sb.append("		function(e) {\r\n");
	       sb.append("		    $(window).off('resize.offcanvas.amui');\r\n");
	       sb.append("			$(window).unbind(\"resize.offcanvas.amui orientationchange.offcanvas.amui\");\r\n");
	       sb.append("   });\r\n");
	       sb.append("	$(\".am-offcanvas\").offCanvas('open');\r\n");
	       sb.append("	});\r\n");
	       sb.append("//表格单击事件\r\n");
	       sb.append("$('.tr_row').on('click', function(n,value) {\r\n");
	       sb.append("	//$('#my-actions').modal('open');\r\n");
	       sb.append("  //首先查看是否有双击事件\r\n");
	       sb.append("  if(arrayJs.length>0){\r\n");
	       sb.append("     eval(arrayJs[$(this).index()]);\r\n");
	       sb.append("  }\r\n");
	       sb.append("});\r\n");
	       sb.append("function ExeOndblclick(id)");
	       sb.append("{");
	       sb.append("}\r\n");

	       sb.append("function ExeOnclick(id)");
	       sb.append("{");
	       sb.append("}\r\n");
	       sb.append("</script>\r\n");

	       //JS函数
	       sb.append("<script language=\"javascript\">");

	       if (strSINPUTTYPE.equals("B")) {
	           sb.append("setTimeout('timesub();',300);");
	       }
	       if (strSINPUTTYPE.equals("C")) {
	           sb.append("setTimeout('timesub1();',300);");
	       }
	       sb.append("function SetSelectValue(SelectId, DictTable){\r\n");
	       //网页形式字典选择----------------
           sb.append("var mFileUrl =\"/iframe?page=ZrCollEngine/selectDictPage.html&DictTable=\"+DictTable+\"&SelectId=\"+SelectId;\r\n");
           sb.append("var strValue=showModalDialog(mFileUrl,window,'status:no;help:no;dialogHeight:312px;dialogWidth:382px');\r\n");
           sb.append("if (strValue!=null){var arrTmp = strValue.split('/');\r\n");
           sb.append("document.getElementById(SelectId).options.length = 0;\r\n");
           sb.append("document.getElementById(SelectId).options[0] = new Option(arrTmp[1],arrTmp[0]);\r\n");
           sb.append("document.getElementById(SelectId).options[0].selected = true;\r\n");
           sb.append("document.getElementById(SelectId).options[0].className = 'selectlist';}\r\n");
	       //网页形式-----------------

	       sb.append("}\r\n");
	       sb.append("function computer(){\r\n");
	       sb.append("document.computer.location.href='/statistics/statisticscompute?ID="+ID+"&P1="+P1+"&P2="+P2+"&P3="+P3+"&P4="+P4+"&P5="+P5+"&USERTYPE="+USERTYPE+"';\r\n");
	       sb.append("}\r\n");

	       sb.append("function timesub(){\r\n");
	       sb.append("var date1 = $(\"input[name='date1']\").val();\r\n");
	       sb.append("date1 = date1.replace('-','/');\r\n");
	       sb.append("var  a  =  new  Date(date1);\r\n");
	       sb.append("a = a.valueOf();\r\n");
	       sb.append("a = a - 1*24*60*60*1000;\r\n");
	       sb.append("a = new Date(a);\r\n");
	       sb.append("sm=a.getMonth()+1;\r\n");
	       sb.append("sd=a.getDate();\r\n");

	       sb.append("if ((a.getMonth()+1)<10){\r\n");
	       sb.append("sm='0'+sm;\r\n");
	       sb.append("}\r\n");

	       sb.append("if (a.getDate()<10){\r\n");
	       sb.append("sd='0'+sd;\r\n");
	       sb.append("}\r\n");

	       sb.append("var v1 =a.getFullYear()+'-'+sm+'-'+sd;\r\n");
	       sb.append("$(\"#date1\").val(v1);;\r\n");
	       sb.append("}\r\n");

	       sb.append("function timesub1(){\r\n");
	       sb.append("var year = $(\"#year1\").val();\r\n");
	       sb.append("var month  = $(\"#Month\").val();\r\n");
	       sb.append("var tdate = year+'/'+month+'/01';\r\n");
	       sb.append("var  d  =  new  Date(tdate);\r\n");
	       sb.append("var y = d.getYear();\r\n");
	       sb.append("var m = d.getMonth()+1;\r\n");
	       sb.append("if (m==1){\r\n");
	       sb.append("y = y -1;\r\n");
	       sb.append("m = 12;\r\n");
	       sb.append("}else{\r\n");
	       sb.append("m = m-1;\r\n");
	       sb.append("}\r\n");
	       sb.append("if (m<10){\r\n");
	       sb.append("m = '0'+m;\r\n");
	       sb.append("}\r\n");
	       sb.append("var zy = String(y);\r\n");
	       sb.append("var zy1 = year.substr(0,2)+zy.substr(zy.length - 2,zy.length-1);\r\n");
	       sb.append("$select_in('year1',zy1);\r\n");
	       sb.append("$select_in('Month',m);\r\n");
	       sb.append("}\r\n");

	       //给下拉框填充默认值
	       sb.append("//填充下拉，字段ID，对比值\r\n");
	       sb.append("function $select_in(idname,vale){\r\n");
	       sb.append("$.each($(\"li\"),function(n,value) { ; \r\n");
	       sb.append("   if ($(this).attr(\"data-value\")!=undefined&&$(this).attr(\"data-value\")== vale) { \r\n");
	       sb.append("       var idnum=$(this).attr(\"data-index\"); \r\n");
	       sb.append("       idnum=parseInt(idnum); \r\n");
	       sb.append("       $(\"#\"+idname).eq(0).selected('select', idnum); \r\n");
	       sb.append("   }  \r\n");
	       sb.append("});\r\n");
	       sb.append("}\r\n");

	       //生成统计报表
	       sb.append("function Statistics(){\r\n");
	       //得到报表类别
	       sb.append("var type = '';\r\n");
	       sb.append("type = $(\"input[name='type']:checked\").val();\r\n");

	       sb.append("var sdate = '';\r\n");
	       sb.append("var edate = '';\r\n");
	       sb.append("var year1 =  $(\"#year1\").val();\r\n");
	       sb.append("var Quarter = $(\"#Quarter\").val();\r\n");
	       sb.append("var Month = $(\"#Month\").val();\r\n");
	       sb.append("var Week = $(\"#Week\").val();\r\n");
	       sb.append("var date1 = $(\"input[name='date1']\").val();\r\n");
	       sb.append("var date2 = $(\"input[name='date2']\").val();\r\n");
	       sb.append("var date3 = $(\"input[name='date3']\").val();\r\n");

	       //年报
	       sb.append("if (type=='1'){\r\n");
	       sb.append("sdate = year1+'-01-01';\r\n");
	       sb.append("edate = year1+'-12-31';\r\n");
	       sb.append("}\r\n");
	       //季度报
	       sb.append("if (type=='2'){\r\n");
	       sb.append("if (Quarter=='1'){\r\n");
	       sb.append("sdate = year1+'-01-01';\r\n");
	       sb.append("edate = year1+'-03-31';\r\n");
	       sb.append("}\r\n");
	       sb.append("if (Quarter=='2'){\r\n");
	       sb.append("sdate = year1+'-04-01';\r\n");
	       sb.append("edate = year1+'-06-30';\r\n");
	       sb.append("}\r\n");
	       sb.append("if (Quarter=='3'){\r\n");
	       sb.append("sdate = year1+'-07-01';\r\n");
	       sb.append("edate = year1+'-09-30';\r\n");
	       sb.append("}\r\n");
	       sb.append("if (Quarter=='4'){\r\n");
	       sb.append("sdate = year1+'-10-01';\r\n");
	       sb.append("edate = year1+'-12-31';\r\n");
	       sb.append("}\r\n");
	       sb.append("}\r\n");
	       //月报
	       sb.append("if (type=='3'){\r\n");
	       sb.append("if(Month=='01' || Month=='03' || Month=='05' || Month=='07' || Month=='08' || Month=='10' || Month=='12'){\r\n");
	       sb.append("sdate = year1+'-'+Month+'-01';\r\n");
	       sb.append("edate = year1+'-'+Month+'-31';\r\n");
	       sb.append("}\r\n");
	       sb.append("if(Month=='04' || Month=='06' || Month=='09' || Month=='11'){\r\n");
	       sb.append("sdate = year1+'-'+Month+'-01';\r\n");
	       sb.append("edate = year1+'-'+Month+'-30';\r\n");
	       sb.append("}\r\n");
	       sb.append("if(Month=='02' && RunNian(year1)==true){\r\n");
	       sb.append("sdate = year1+'-'+Month+'-02';\r\n");
	       sb.append("edate = year1+'-'+Month+'-29';\r\n");
	       sb.append("}\r\n");
	       sb.append("if(Month=='02' && RunNian(year1)==false){\r\n");
	       sb.append("sdate = year1+'-'+Month+'-02';\r\n");
	       sb.append("edate = year1+'-'+Month+'-28';\r\n");
	       sb.append("}\r\n");

	       sb.append("}\r\n");
	       //周报
	       sb.append("if (type=='4'){\r\n");
	       sb.append("var d = new   Date(year1,0,1);\r\n");
	       sb.append("d.setDate(parseInt('1065432'.charAt(d.getDay())) + Week * 7);\r\n");
	       sb.append("var fe = getFirstAndEnd(d);\r\n");
	       sb.append("sdate = fe.first.format('yyyy-MM-dd');\r\n");
	       sb.append("edate = fe.end.format('yyyy-MM-dd');\r\n");
	       sb.append("}\r\n");
	       //日报
	       sb.append("if (type=='5'){\r\n");
	       sb.append("sdate = date1;\r\n");
	       sb.append("edate = date1;\r\n");
	       sb.append("}\r\n");
	       //自定义日期
	       sb.append("if (type=='6'){\r\n");
	       sb.append("sdate = date2;\r\n");
	       sb.append("edate = date3;\r\n");
	       sb.append("}\r\n");
	       sb.append("$('#P1').val(sdate);\r\n");
	       sb.append("$('#P2').val(edate);\r\n");
	       //计算报表
	       sb.append("document.recordfrm.submit();\r\n");
	       sb.append("}\r\n");
	       //生成统计报表(日报)
	       sb.append("function Statistics1(){\r\n");
	       sb.append("var date1=$(\"input[name='date1']\").val();\r\n");
	       if (CODETABLE.length()>0) {
	          sb.append("var code = $('#code').val();\r\n");
	          sb.append("var name = \"\";\r\n");
	          sb.append("var strs= new Array();\r\n");
	          sb.append("var strs= code.split(\"/\");\r\n");
	          sb.append("if (strs.length==2){");
	          sb.append("code = strs[0];\r\n");
	          sb.append("name = strs[1];\r\n");
	          sb.append(";\r\n");
	          sb.append("}");
	       }
	       sb.append("$('#P1').val(date1);\r\n");
	       if (CODETABLE.length()>0){
	           sb.append("$('#P2').val(code);\r\n");
	           sb.append("$('#P3').val(name);\r\n");
	       }
	       sb.append("document.recordfrm.submit();\r\n");
	       sb.append("}\r\n");

	       //生成统计报表(年报)
	       sb.append("function Statistics5(){\r\n");
	       sb.append("var date1 = $(\"#year1\").val();\r\n");

	       if (CODETABLE.length()>0) {
	          sb.append("var code = $('#code').val();\r\n");
	          sb.append("var name = \"\";\r\n");
	          sb.append("var strs= new Array();\r\n");
	          sb.append("var strs= code.split(\"/\");\r\n");
	          sb.append("if (strs.length==2){");
	          sb.append("code = strs[0];\r\n");
	          sb.append("name = strs[1];\r\n");
	          sb.append(";\r\n");
	          sb.append("}");
	       }
	       sb.append("$('#P1').val(date1);\r\n");
	       if (CODETABLE.length()>0){
	           sb.append("$('#P2').val(code);\r\n");
	           sb.append("$('#P3').val(name);\r\n");
	       }
	       sb.append("document.recordfrm.submit();\r\n");
	       sb.append("}\r\n");

	       //生成统计报表(月报)
	       sb.append("function Statistics6(){\r\n");
	       sb.append("var date1 = $(\"#year1\").val();\r\n");
	       sb.append("var Month = $(\"#Month\").val();\r\n");
	       if (CODETABLE.length()>0){
	          sb.append("var code = $('#code').val();\r\n");
	          sb.append("var name = \"\";\r\n");
	          sb.append("var strs= new Array();\r\n");
	          sb.append("var strs= code.split(\"/\");\r\n");
	          sb.append("if (strs.length==2){");
	          sb.append("code = strs[0];\r\n");
	          sb.append("name = strs[1];\r\n");
	          sb.append(";\r\n");
	          sb.append("}");
	       }
	       //计算报表
	       sb.append("$('#P1').val(date1);\r\n");
	       sb.append("$('#P2').val(Month);\r\n");
	       if (CODETABLE.length()>0){
	           sb.append("$('#P3').val(code);\r\n");
	           sb.append("$('#P4').val(name);\r\n");
	       }
	       sb.append("document.recordfrm.submit();\r\n");
	       sb.append("}\r\n");

	       //生成统计报表(年份比较)
	       sb.append("function Statistics2(){\r\n");
	       sb.append("var date1 = $(\"#date1\").val();\r\n");
	       sb.append("var date2 = $(\"#date2\").val();\r\n");
	       sb.append("$('#P1').val(date1);\r\n");
	       sb.append("$('#P2').val(date2);\r\n");
	       sb.append("document.recordfrm.submit();\r\n");
	       sb.append("}\r\n");

	       //生成统计报表(年月比较)
	       sb.append("function Statistics3(){\r\n");
	       sb.append("var year1 = $(\"#date1\").val();\r\n");
	       sb.append("var year2 = $(\"#date2\").val();\r\n");

	       sb.append("var month1 = $(\"#Month1\").val();\r\n");
	       sb.append("var month2 = $(\"#Month2\").val();\r\n");

	       sb.append("var dateS1 = '';\r\n");
	       sb.append("var dateE1 = '';\r\n");
	       sb.append("var dateS2 = '';\r\n");
	       sb.append("var dateE2 = '';\r\n");
	       sb.append("if(month1=='01' || month1=='03' || month1=='05' || month1=='07' || month1=='08' || month1=='10' || month1=='12'){\r\n");
	       sb.append("dateS1 = year1+'-'+month1+'-01';\r\n");
	       sb.append("dateE1 = year1+'-'+month1+'-31';\r\n");
	       sb.append("}\r\n");
	       sb.append("if(month1=='04' || month1=='06' || month1=='09' || month1=='11'){\r\n");
	       sb.append("dateS1 = year1+'-'+month1+'-01';\r\n");
	       sb.append("dateE1 = year1+'-'+month1+'-30';\r\n");
	       sb.append("}\r\n");
	       sb.append("if(month1=='02' && RunNian(year1)==true){\r\n");
	       sb.append("dateS1 = year1+'-'+month1+'-02';\r\n");
	       sb.append("dateE1 = year1+'-'+month1+'-29';\r\n");
	       sb.append("}\r\n");
	       sb.append("if(month1=='02' && RunNian(year1)==false){\r\n");
	       sb.append("dateS1 = year1+'-'+month1+'-02';\r\n");
	       sb.append("dateE1 = year1+'-'+month1+'-28';\r\n");
	       sb.append("}\r\n");
	       sb.append("if(month2=='01' || month2=='03' || month2=='05' || month2=='07' || month2=='08' || month2=='10' || month2=='12'){\r\n");
	       sb.append("dateS2 = year2+'-'+month2+'-01';\r\n");
	       sb.append("dateE2 = year2+'-'+month2+'-31';\r\n");
	       sb.append("}\r\n");
	       sb.append("if(month2=='04' || month2=='06' || month2=='09' || month2=='11'){\r\n");
	       sb.append("dateS2 = year2+'-'+month2+'-01';\r\n");
	       sb.append("dateE2 = year2+'-'+month2+'-30';\r\n");
	       sb.append("}\r\n");
	       sb.append("if(month2=='02' && RunNian(year2)==true){\r\n");
	       sb.append("dateS2 = year2+'-'+month2+'-02';\r\n");
	       sb.append("dateE2 = year2+'-'+month2+'-29';\r\n");
	       sb.append("}\r\n");
	       sb.append("if(month2=='02' && RunNian(year2)==false){\r\n");
	       sb.append("dateS2 = year2+'-'+month2+'-02';\r\n");
	       sb.append("dateE2 = year2+'-'+month2+'-28';\r\n");
	       sb.append("}\r\n");
	       sb.append("$('#P1').val(dateS1);\r\n");
	       sb.append("$('#P2').val(dateE1);\r\n");
	       sb.append("$('#P3').val(dateS2);\r\n");
	       sb.append("$('#P4').val(dateE2);\r\n");
	       sb.append("document.recordfrm.submit();\r\n");
	       sb.append("}\r\n");

	       //生成统计报表(年季度比较)
	       sb.append("function Statistics4(){\r\n");
	       sb.append("var year1 = $(\"#date1\").val();\r\n");
	       sb.append("var year2 = $(\"#date2\").val();\r\n");
	       sb.append("var Quarter1 = $(\"#Quarter1\").val();\r\n");
	       sb.append("var Quarter2 = $(\"#Quarter2\").val();\r\n");

	       sb.append("var dateS1 = '';\r\n");
	       sb.append("var dateE1 = '';\r\n");
	       sb.append("var dateS2 = '';\r\n");
	       sb.append("var dateE2 = '';\r\n");
	       sb.append("if (Quarter1=='1'){\r\n");
	       sb.append("dateS1 = year1+'-01-01';\r\n");
	       sb.append("dateE1 = year1+'-03-31';\r\n");
	       sb.append("}\r\n");
	       sb.append("if (Quarter1=='2'){\r\n");
	       sb.append("dateS1 = year1+'-04-01';\r\n");
	       sb.append("dateE1 = year1+'-06-30';\r\n");
	       sb.append("}\r\n");
	       sb.append("if (Quarter1=='3'){\r\n");
	       sb.append("dateS1 = year1+'-07-01';\r\n");
	       sb.append("dateE1 = year1+'-09-30';\r\n");
	       sb.append("}\r\n");
	       sb.append("if (Quarter1=='4'){\r\n");
	       sb.append("dateS1 = year1+'-10-01';\r\n");
	       sb.append("dateE1 = year1+'-12-31';\r\n");
	       sb.append("}\r\n");
	       sb.append("if (Quarter2=='1'){\r\n");
	       sb.append("dateS2 = year2+'-01-01';\r\n");
	       sb.append("dateE2 = year2+'-03-31';\r\n");
	       sb.append("}\r\n");
	       sb.append("if (Quarter2=='2'){\r\n");
	       sb.append("dateS2 = year2+'-04-01';\r\n");
	       sb.append("dateE2 = year2+'-06-30';\r\n");
	       sb.append("}\r\n");
	       sb.append("if (Quarter2=='3'){\r\n");
	       sb.append("dateS2 = year2+'-07-01';\r\n");
	       sb.append("dateE2 = year2+'-09-30';\r\n");
	       sb.append("}\r\n");
	       sb.append("if (Quarter2=='4'){\r\n");
	       sb.append("dateS2 = year2+'-10-01';\r\n");
	       sb.append("dateE2 = year2+'-12-31';\r\n");
	       sb.append("}\r\n");
	       sb.append("$('#P1').val(dateS1);\r\n");
	       sb.append("$('#P2').val(dateE1);\r\n");
	       sb.append("$('#P3').val(dateS2);\r\n");
	       sb.append("$('#P4').val(dateE2);\r\n");
	       sb.append("document.recordfrm.submit();\r\n");
	       sb.append("}\r\n");

	       //检查输入年份的有效性JS
	       sb.append("function CheckYear(){\r\n");
	       sb.append("var objSrc = event.srcElement;\r\n");
	       sb.append("if (objSrc.tagName == 'INPUT'){\r\n");
	       sb.append("if(event.keyCode >= 48 && event.keyCode <= 57){return true;}else{return false;}}\r\n");
	       sb.append("return false;}\r\n");
	       //取得是否为润年
	       sb.append("function RunNian(The_Year){\r\n");
	       sb.append("if ((The_Year%400==0) || ((The_Year%4==0) && (The_Year%100!=0)))\r\n");
	       sb.append("return true;\r\n");
	       sb.append("else\r\n");
	       sb.append("return false;\r\n");
	       sb.append("}\r\n");
	       //根据年和周得到开始时间和结束时间相关-----------------
	       sb.append("Date.prototype.getWeek = function(flag)\r\n");
	       sb.append("{\r\n");
	       sb.append("var first=new Date(this.getFullYear(),0,1);\r\n");
	       sb.append("var n=parseInt('1065432'.charAt(first.getDay()));\r\n");
	       sb.append("n=this.getTime()-first.getTime()-n*24*60*60*1000;\r\n");
	       sb.append("n=Math.ceil(n/(7*24*60*60*1000));\r\n");
	       sb.append("return (flag==true&&first.getDay()!=1)?(n+1):n;\r\n");
	       sb.append("}\r\n");
	       sb.append("Date.prototype.format = function(format)\r\n");
	       sb.append("{\r\n");
	       sb.append("var o = {");
	       sb.append("'M+':this.getMonth()+1,");
	       sb.append("'d+':this.getDate(),");
	       sb.append("'h+':this.getHours(),");
	       sb.append("'m+':this.getMinutes(),");
	       sb.append("'s+':this.getSeconds(),");
	       sb.append("'q+':Math.floor((this.getMonth()+3)/3),");
	       sb.append("'S':this.getMilliseconds()");
	       sb.append("}\r\n");
	       sb.append("if(/(y+)/.test(format))  format=format.replace(RegExp.$1,");
	       sb.append("(this.getFullYear()+'').substr(4  - RegExp.$1.length));");
	       sb.append("for(var k in o)if(new  RegExp('('+ k +')').test(format))");
	       sb.append("format = format.replace(RegExp.$1,");
	       sb.append("RegExp.$1.length==1 ? o[k]:");
	       sb.append("('00'+ o[k]).substr((''+ o[k]).length));\r\n");
	       sb.append("return format;\r\n");
	       sb.append("}\r\n");
	       sb.append("function getFirstAndEnd(d)\r\n");
	       sb.append("{\r\n");
	       sb.append("var w=d.getDay(),n = 24*60*60*1000;\r\n");
	       sb.append("var first = new Date(d.getTime() - parseInt('6012345'.charAt(w))*n);\r\n");
	       sb.append("var end = new Date(d.getTime() + parseInt('0654321'.charAt(w))*n);\r\n");
	       sb.append("return {first:first,end:end};\r\n");
	       sb.append("}\r\n");
	       sb.append("//多级展示\r\n");
	       sb.append("function opencompute(unitid,showname,tier,type){\r\n");
	       sb.append("document.recordfrm.action='/statistics/statisticsenginephone?ID="+ID+"&SHOWTITLE="+SHOWTITLE+"&TYPE='+type+'&UNITID='+unitid;\r\n");
	       sb.append("document.recordfrm.submit();\r\n");
	       sb.append("}\r\n");
	       //根据年和周得到开始时间和结束时间相关结束-----------
	       sb.append(" </script>");
	       sb.append("</BODY>");
	       sb.append("</HTML>");
	  } catch (Exception ex) {
		  ex.printStackTrace();
	      return "";
	  }
	  return sb.toString();
	}
}