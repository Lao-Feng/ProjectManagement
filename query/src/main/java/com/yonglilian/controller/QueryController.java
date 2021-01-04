package com.yonglilian.controller;

import com.yonglilian.common.util.FunctionMessage;
import com.yonglilian.intercept.annotation.ZrSafety;
import com.yonglilian.model.SessionUser;
import com.yonglilian.queryengine.JxExcel;
import com.yonglilian.queryengine.Request;
import com.yonglilian.queryengine.mode.*;
import com.yonglilian.service.QueryButtonService;
import com.yonglilian.service.QueryControlService;
import com.yonglilian.service.QueryEngineConfigService;
import com.yonglilian.service.QueryService;
import com.yonglilian.service.impl.QueryControlImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 查询相关控制器
 * @author lwk
 *
 */
@ZrSafety
@Controller
@RequestMapping("/query")
public class QueryController extends BaseController {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private FunctionMessage fm;
	private QUERY_CONFIG_TABLE objConfigTable;
	private HttpServletRequest _request;
	/** 查询服务层. */
	@Autowired
	private QueryService queryService;
	/** 查询控制管理服务层. */
	@Autowired
	private QueryControlService queryControlService;
	/** 查询配置管理服务层. */
	@Autowired
	private QueryEngineConfigService queryConfig;
	/** 按钮查询服务层. */
	@Autowired
	private QueryButtonService queryButtonService;

	/**
	 * 查询配置按钮接口
	 * @param request
	 * @param response
	 * @throws IOException 
	 */
	@RequestMapping("/queryconfigbutton")
	public void queryConfigButton(HttpServletRequest request, HttpServletResponse response) throws IOException {
	    response.setContentType("text/html; charset=UTF-8");
	    String strAction = request.getParameter("Act");
	    try {
	    	if (strAction != null) {
	  	      if (strAction.equals("edit")) {
	  	        EditButton(request, response);
	  	      }
	  	      else if (strAction.equals("del")) {
	  	        DeleteButton(request, response);
	  	      }
	  	      else if (strAction.equals("add")) {
	  	        AddButton(request, response);
	  	      } else {
	  	      }
	  	    }
	    } catch (Exception ex) {
	    	LOGGER.error("QueryController.queryConfigButton Exception:\n", ex);
	    }
	}

	/**
	 * 查询配置接口
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping("/queryconfig")
	public String queryconfig(HttpServletRequest request, HttpServletResponse response) throws IOException {
	    String rtnPath = "";
	    response.setContentType("text/html; charset=UTF-8");
	    
	    _request = request;
	    fm = new FunctionMessage(1);
	    fm.setResult(true);
	    String strAction = request.getParameter("Act1");
	    objConfigTable = new QUERY_CONFIG_TABLE();
	    objConfigTable.fullDataFromRequest(_request);
	    if (objConfigTable.getID() == null) {
	        objConfigTable.setID("");
	    }
	    try {
	        if (objConfigTable.getID().trim().equals("")) {
	        	objConfigTable.setID(queryConfig.getMaxFieldNo("QUERY_CONFIG_TABLE", "ID", 8));
	        }
	        if (strAction.equals("Save")) {
	        	saveBaseProperty(); //基本属性
	        	saveRelationProperty(); //关联表属性
	        	saveInitWhereProperty(); //初始条件属性
	        	saveQueryFieldProperty(); //查询列属性
	        	saveResultFieldProperty();//结果列属性
	        	saveDetailParameterProperty();//按钮参数属性
	        	saveSortProperty();           //排序属性
	          	//rtnPath = "ZrQueryEngine/Config/ManageConfig.jsp?SelectedItems=" + objConfigTable.getID();
	        }
	        else if (strAction.equals("Delete")) {
	          queryConfig.deleteConfigByID(objConfigTable.getID());
//	          rtnPath = "ZrQueryEngine/Config/List.jsp";
	        }
	    } catch (Exception ex) {
	        fm.setResult(false);
	        this.Msg = "连接客户端文件中间件服务失败！<br>详细错误信息：<br>   " + ex.toString();
//	        this.returnPath = "javascript:history.back(-1);";
	      }
	      if (fm.getResult()) {
	        return rtnPath;
	      }
	      this.out = response.getWriter();
	      //this.getBox();
	      return rtnPath;
	}

	/**
	 * 查询PC页面
	 * @param request
	 * @param response
	 * @throws IOException 
	 */
	@RequestMapping("/QueryPage")
	public void queryPage(HttpServletRequest request, HttpServletResponse response) throws IOException {
	    response.setContentType("text/html; charset=UTF-8");
	    
	    String TITLENAME = request.getParameter("TITLENAME");
	    if (TITLENAME == null) {TITLENAME = "";}
	    String STB = request.getParameter("STB");
	    if (STB == null) {STB = "0";}

	    String ISCODE = request.getParameter("ISCODE");
	    if (ISCODE == null) {ISCODE = "1";}
	    if (ISCODE.length()==0) {ISCODE = "1";}

	    if (TITLENAME.length()>0 && ISCODE.equals("1")) {
	          TITLENAME = new String(TITLENAME.getBytes("iso-8859-1"),"UTF-8");
	    }
	    try {
	      HttpSession session = request.getSession(true);
	      SessionUser user = (SessionUser) session.getAttribute("userinfo");
	      Request rq=null;
	     try {
	       rq = new Request();
	       rq.fullItem(request);
	     }
	     catch (Exception ex) {
	     }
	     String ID = request.getParameter("ID");
	     if (ID == null) {ID = "";}

	     String strResult = "";
	     strResult = getTemplateStr(user, request, rq, TITLENAME);
	     response.getWriter().print(strResult);
	    }
	    catch (Exception ex) {
	      response.getWriter().print("<p>发生异常！</p><p>详细错误信息:</p><p>" + ex.toString() + "</p>");
	    }
	}

	/**
	 * 查询手机页面
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping("/QueryPageSerPhone")
	public void queryPageSerPhone(HttpServletRequest request, HttpServletResponse response) throws IOException {
	    response.setContentType("text/html; charset=utf-8");
	    String TITLENAME = request.getParameter("TITLENAME");
	    if (TITLENAME == null) {TITLENAME = "";}

	    String ISCODE = request.getParameter("ISCODE");
	    if (ISCODE == null) {ISCODE = "1";}
	    if (ISCODE.length()==0) {ISCODE = "1";}

	    if (TITLENAME.length()>0 && ISCODE.equals("1")) {
	          TITLENAME = new String(TITLENAME.getBytes("iso-8859-1"),"utf-8");
	    }
	    try {
	      HttpSession session = request.getSession(true);
	      SessionUser user = (SessionUser) session.getAttribute("userinfo");
	      Request req=null;
	      try {
	    	 req = new Request();
	    	 req.fullItem(request);
	      }
	      catch (Exception ex) {
	      }
	      String strResult = "";
	      strResult = getPhoneTemplateStr(user, request, req, TITLENAME);
	      response.getWriter().print(strResult);
	    }
	    catch (Exception ex) {
	      response.getWriter().print("<p>发生异常！</p><p>详细错误信息:</p><p>" + ex.toString() + "</p>");
	    }
	}

	@RequestMapping("/openquerycfg")
	public String openquerycfg(HttpServletRequest request, HttpServletResponse response) throws Exception {
	      response.setContentType("text/html; charset=UTF-8");
	      
	      String pagepath = "";
	      String strAct = request.getParameter("Act");
	      HttpSession session=request.getSession(true);
	      SessionUser user = (SessionUser)session.getAttribute("userinfo");
	      //新增查询按钮---------------------------
	      if (strAct.equals("AddButton")) {
	          String FID=request.getParameter("FID");
	          if (FID==null){FID="0";}
	          request.setAttribute("FID",FID);
	          pagepath = "ZrQueryEngine/ButtonConfig/Add";
	          return pagepath;
	      }
	      //-------------------------------------
	      //编辑查询按钮---------------------------
	      else if (strAct.equals("EditButton")) {
	          String ID = request.getParameter("ID");
	          QUERY_CONFIG_BUTTON Bt = queryButtonService.getButtonByID(ID);
	          String FID=request.getParameter("FID");
	          if (FID==null){FID="0";}
	          request.setAttribute("ID",ID);
	          request.setAttribute("FID",FID);
	          request.setAttribute("BUTTON",Bt);
	          pagepath = "ZrQueryEngine/ButtonConfig/Edit";
	          return pagepath;
	      }
	      //-------------------------------------
	      //复制查询------------------------------
	      else if (strAct.equals("querycopy")) {
	          String ID = request.getParameter("ID");//配置ID
	          //执行复制配置---
	          queryConfig.querycopy(ID);
	          pagepath = "ZrQueryEngine/Config/copy";
	          return pagepath;
	      }
	      //-------------------------------------
	      //新建查询管理配置-----------------------
	      else if (strAct.equals("newmanageconfig")) {
	          String ID = queryConfig.newQuery();
	          return "redirect:/query/openquerycfg?Act=manageconfig&ID="+ID;
	      }
	      //-------------------------------------
	      //编辑查询管理配置-----------------------
	      else if (strAct.equals("manageconfig")) {
	          int i=0;
	          String ID =request.getParameter("ID");
	          if (ID==null){ID="";}
	          String QITEMTYPE = "1";
	          String QTABLETYPE = "1";
	          QUERY_CONFIG_TABLE objConfigTable=null;
	          if (ID.length()>0) {
	             objConfigTable = queryConfig.getConfigByID(ID);
	             QITEMTYPE = objConfigTable.getQITEMTYPE();
	             QTABLETYPE = objConfigTable.getQTABLETYPE();
	             if (QITEMTYPE==null){QITEMTYPE="";}
	             if (QITEMTYPE.length()==0){QITEMTYPE="1";}
	             if (QTABLETYPE==null){QTABLETYPE="";}
	             if (QTABLETYPE.length()==0){QTABLETYPE="1";}
	          }
	          //----------------------------关联表属性-----------NAME1_1--NAME1_10-----------------------------------
	          QUERY_CONFIG_CONNECTION[] connectionList;
	          connectionList=queryConfig.getConfigConnectionByWhere(" FID='"+objConfigTable.getID()+"'");

	          //----------------------------------初始条件属性----------------------------------------
	          QUERY_CONFIG_INIT[]   whereList;
	          whereList=queryConfig.getConfigInitByWhere(" FID='"+objConfigTable.getID()+"'");

	          //----------------------------------查询列属性----------------------------------------
	          QUERY_CONFIG_QUERYFIELD[] queryFieldList;
	          queryFieldList=queryConfig.getQueryFieldByWhere(" FID='"+objConfigTable.getID()+"'");
	          //----------------------------------结果列属性----------------------------------------
	          QUERY_CONFIG_SHOWFIELD[] resultFieldList;
	          resultFieldList=queryConfig.getResultFieldByWhere(" FID='"+objConfigTable.getID()+"'");

	          //----------------------------------明细参数属性--------------------------------------//
	          QUERY_CONFIG_PARAMETER[] detailParameterList;
	          detailParameterList=queryConfig.getDetailParameterFieldByWhere(" FID='"+objConfigTable.getID()+"'");

	          QUERY_CONFIG_BUTTON[] buttonAll = null;
	          QUERY_CONFIG_BUTTON[] buttonConfig = null;
	          buttonAll=queryButtonService.getShowButton(objConfigTable.getID());
	          buttonConfig=queryButtonService.getButtonList(" ID In(Select BID from QUERY_CONFIG_BRELATION Where FID='"+objConfigTable.getID()+"')");
	          buttonConfig=queryButtonService.getButtonByFID(objConfigTable.getID());
	          //----------------------------------排序属性-----------------------------------//
	          QUERY_CONFIG_ORDER[] orderList=null;
	          orderList=queryConfig.getSortByWhere(" FID='"+objConfigTable.getID()+"'");

	          String LIST1 = "";
	          if(buttonAll!=null)
	          {
	            for(i=0;i<buttonAll.length;i++)
	            {
	               if (objConfigTable.getCID().equals(buttonAll[i].getID()))
	               {
	                 LIST1 = LIST1 +  "<option value="+buttonAll[i].getID()+" selected>"+buttonAll[i].getNAME()+"</option>\r\n";
	               }else
	               {
	                 LIST1 = LIST1 +  "<option value="+buttonAll[i].getID()+">"+buttonAll[i].getNAME()+"</option>\r\n";
	               }
	            }
	          }
	          String LIST2 = "";
	          if(buttonAll!=null)
	          {
	            for(i=0;i<buttonAll.length;i++)
	            {
	               if (objConfigTable.getBID().equals(buttonAll[i].getID()))
	               {
	                 LIST2 = LIST2 +  "<option value="+buttonAll[i].getID()+" selected>"+buttonAll[i].getNAME()+"</option>\r\n";
	               }else
	               {
	                 LIST2 = LIST2 +  "<option value="+buttonAll[i].getID()+">"+buttonAll[i].getNAME()+"</option>\r\n";
	               }
	            }
	          }
	          String LIST3 = "";
	          if(buttonAll!=null&&buttonAll.length>0){
	            for(i=0;i<buttonAll.length;i++){
	                LIST3 =  LIST3  + "obj.options.add(new Option(\""+buttonAll[i].getNAME()+"\",\""+buttonAll[i].getID()+"\"));\r\n";
	            }
	          }
	          String LIST4 = "";
	          if(buttonConfig!=null){
	            for(i=0;i<buttonConfig.length;i++){
	               LIST4 =  LIST4  + "obj2.options.add(new Option(\""+buttonConfig[i].getNAME()+"\",\""+buttonConfig[i].getID()+"\"));\r\n";
	            }
	          }
	          //onInitRelationTableBind-----
	          String onInitRelationTableBind="";
	          onInitRelationTableBind = "function onInitRelationTableBind(){\r\n";
	          onInitRelationTableBind = onInitRelationTableBind  + "var tableFieldName;\r\n";
	          onInitRelationTableBind = onInitRelationTableBind  + "var tableFieldCnName;\r\n";
	          onInitRelationTableBind = onInitRelationTableBind  + "var joinTypeCnName;\r\n";
	          String joinTypeCnName="";
	          if(connectionList!=null&&connectionList.length>0)
	          {
	             for(i=0;i<connectionList.length;i++){
	                      onInitRelationTableBind = onInitRelationTableBind  + "var row={};\r\n";
	                      onInitRelationTableBind = onInitRelationTableBind  + "tableFieldName=\""+connectionList[i].getCFIELD()+"\";//处理左关联表\r\n";
	                      onInitRelationTableBind = onInitRelationTableBind  + "getTableFieldCnNameByEn = new  GetTableFieldCnNameByEn(tableFieldName);\r\n";
	                      onInitRelationTableBind = onInitRelationTableBind  + "tableFieldCnName=getTableFieldCnNameByEn.getString();\r\n";
	                      onInitRelationTableBind = onInitRelationTableBind  + "row.NAME1_1=tableFieldCnName.split(\".\")[0];\r\n";
	                      onInitRelationTableBind = onInitRelationTableBind  + "row.NAME1_2=tableFieldCnName.split(\".\")[1];\r\n";
	                      onInitRelationTableBind = onInitRelationTableBind  + "row.NAME1_6=tableFieldName.split(\".\")[0];\r\n";
	                      onInitRelationTableBind = onInitRelationTableBind  + "row.NAME1_7=tableFieldName.split(\".\")[1];\r\n";
	                      onInitRelationTableBind = onInitRelationTableBind  + "tableFieldName=\""+connectionList[i].getMFIELD()+"\";//处理右关联表\r\n";
	                      onInitRelationTableBind = onInitRelationTableBind  + "getTableFieldCnNameByEn = new  GetTableFieldCnNameByEn(tableFieldName);\r\n";
	                      onInitRelationTableBind = onInitRelationTableBind  + "tableFieldCnName=getTableFieldCnNameByEn.getString();\r\n";
	                      onInitRelationTableBind = onInitRelationTableBind  + "row.NAME1_3=tableFieldCnName.split(\".\")[0];\r\n";
	                      onInitRelationTableBind = onInitRelationTableBind  + "row.NAME1_4=tableFieldCnName.split(\".\")[1];\r\n";
	                      onInitRelationTableBind = onInitRelationTableBind  + "row.NAME1_8=tableFieldName.split(\".\")[0];\r\n";
	                      onInitRelationTableBind = onInitRelationTableBind  + "row.NAME1_9=tableFieldName.split(\".\")[1];\r\n";

	                        if(connectionList[i].getJOINTYPE().equals("1")){
	                          joinTypeCnName="一般相等关联";
	                        }else if(connectionList[i].getJOINTYPE().equals("2")){
	                           joinTypeCnName="左外关联";
	                        }else if(connectionList[i].getJOINTYPE().equals("3")){
	                            joinTypeCnName="右外关联";
	                        }else{
	                           joinTypeCnName="一般相等关联";
	                        }
	                        onInitRelationTableBind = onInitRelationTableBind  + "row.NAME1_5=\""+joinTypeCnName+"\"\r\n";
	                        onInitRelationTableBind = onInitRelationTableBind  + "row.NAME1_10=\""+connectionList[i].getJOINTYPE()+"\"\r\n";
	                        onInitRelationTableBind = onInitRelationTableBind  + "//继续填充 主表英文名称\r\n";
	                        onInitRelationTableBind = onInitRelationTableBind  + "if(allRelTableStr.indexOf(row.NAME1_6)==-1){\r\n";
	                        onInitRelationTableBind = onInitRelationTableBind  + "allRelTableStr=allRelTableStr+row.NAME1_6+\".\";\r\n";
	                        onInitRelationTableBind = onInitRelationTableBind  + "}\r\n";
	                        onInitRelationTableBind = onInitRelationTableBind  + "if(allRelTableStr.indexOf(row.NAME1_8)==-1){\r\n";
	                        onInitRelationTableBind = onInitRelationTableBind  + "allRelTableStr=allRelTableStr+row.NAME1_8+\".\";\r\n";
	                        onInitRelationTableBind = onInitRelationTableBind  + "}\r\n";
	                        onInitRelationTableBind = onInitRelationTableBind  + "$(\"#list1\").datagrid(\"appendRow\", row);\r\n";
	                        onInitRelationTableBind = onInitRelationTableBind  + "// 更新初始条件==条件列=select选择\r\n";
	                        onInitRelationTableBind = onInitRelationTableBind  + "loadinitWhereTableFieldInfo();\r\n";
	                        onInitRelationTableBind = onInitRelationTableBind  + "//填充其它大文本左右选择的select字段\r\n";
	                        onInitRelationTableBind = onInitRelationTableBind  + "loadAllRelTableFieldInfo();\r\n";
	              }
	          }
	          onInitRelationTableBind = onInitRelationTableBind  + "}\r\n";

	          //onInitWhereFieldBind
	          String onInitWhereFieldBind = "";
	          onInitWhereFieldBind = "function onInitWhereFieldBind(){\r\n";
	          onInitWhereFieldBind = onInitWhereFieldBind +"var strTempEn,strTempCn;\r\n";
	          if(whereList!=null){
	            for(i=0;i<whereList.length;i++){
	              onInitWhereFieldBind = onInitWhereFieldBind +"var row={};\r\n";
	              onInitWhereFieldBind = onInitWhereFieldBind +"//左括号\r\n";
	              onInitWhereFieldBind = onInitWhereFieldBind +"strTempEn=\""+whereList[i].getQLEFT()+"\";\r\n";
	              onInitWhereFieldBind = onInitWhereFieldBind +"if(strTempEn==\"0\"){\r\n";
	              onInitWhereFieldBind = onInitWhereFieldBind +"strTempCn=\"无\";\r\n";
	              onInitWhereFieldBind = onInitWhereFieldBind +"}else{\r\n";
	              onInitWhereFieldBind = onInitWhereFieldBind +"strTempCn=strTempEn;\r\n";
	              onInitWhereFieldBind = onInitWhereFieldBind +"}\r\n";
	              onInitWhereFieldBind = onInitWhereFieldBind +"row.NAME2_1=strTempCn;\r\n";
	              onInitWhereFieldBind = onInitWhereFieldBind +"row.NAME2_7=strTempEn;\r\n";
	              onInitWhereFieldBind = onInitWhereFieldBind +"strTempEn=\""+whereList[i].getFIELD()+"\";\r\n";
	              onInitWhereFieldBind = onInitWhereFieldBind +"strTempCn=\""+queryConfig.getTableFieldCnNameByEn(whereList[i].getFIELD())+"\";\r\n";
	              onInitWhereFieldBind = onInitWhereFieldBind +"row.NAME2_2=strTempCn;\r\n";
	              onInitWhereFieldBind = onInitWhereFieldBind +"row.NAME2_8=strTempEn;\r\n";
	              onInitWhereFieldBind = onInitWhereFieldBind +"row.NAME2_3=\""+whereList[i].getSYMBOL()+"\";\r\n";
	              onInitWhereFieldBind = onInitWhereFieldBind +"row.NAME2_4=\""+whereList[i].getWHEREVALUE()+"\";\r\n";
	              onInitWhereFieldBind = onInitWhereFieldBind +"strTempEn=\""+whereList[i].getQRIGHT()+"\";\r\n";
	              onInitWhereFieldBind = onInitWhereFieldBind +"if(strTempEn==\"0\"){\r\n";
	              onInitWhereFieldBind = onInitWhereFieldBind +"strTempCn=\"无\";\r\n";
	              onInitWhereFieldBind = onInitWhereFieldBind +"}else{\r\n";
	              onInitWhereFieldBind = onInitWhereFieldBind +"strTempCn=strTempEn;\r\n";
	              onInitWhereFieldBind = onInitWhereFieldBind +"}\r\n";
	              onInitWhereFieldBind = onInitWhereFieldBind +"row.NAME2_5=strTempCn;\r\n";
	              onInitWhereFieldBind = onInitWhereFieldBind +"row.NAME2_9=strTempEn;\r\n";
	              onInitWhereFieldBind = onInitWhereFieldBind +"strTempEn=\""+whereList[i].getLOGIC()+"\";\r\n";
	              onInitWhereFieldBind = onInitWhereFieldBind +"if(strTempEn==\"1\"){\r\n";
	              onInitWhereFieldBind = onInitWhereFieldBind +"   strTempCn=\"与\";\r\n";
	              onInitWhereFieldBind = onInitWhereFieldBind +"}if(strTempEn==\"2\"){\r\n";
	              onInitWhereFieldBind = onInitWhereFieldBind +"   strTempCn=\"或\";\r\n";
	              onInitWhereFieldBind = onInitWhereFieldBind +"}if(strTempEn==\"0\"){\r\n";
	              onInitWhereFieldBind = onInitWhereFieldBind +"   strTempCn=\"无\";\r\n";
	              onInitWhereFieldBind = onInitWhereFieldBind +"}\r\n";
	              onInitWhereFieldBind = onInitWhereFieldBind +"row.NAME2_6=strTempCn;\r\n";
	              onInitWhereFieldBind = onInitWhereFieldBind +"row.NAME2_10=strTempEn;\r\n";
	              onInitWhereFieldBind = onInitWhereFieldBind +"$(\"#list2\").datagrid(\"appendRow\", row);\r\n";
	            }
	          }
	          onInitWhereFieldBind = onInitWhereFieldBind +"}\r\n";

	          //function onInitQueryFieldDestList()
	          String onInitQueryFieldDestList = "";
	          onInitQueryFieldDestList = "function onInitQueryFieldDestList(){\r\n";
	          onInitQueryFieldDestList = onInitQueryFieldDestList + "var rows = $(\"#list7\").datagrid(\"getRows\");//得到rows对象\r\n";
	          onInitQueryFieldDestList = onInitQueryFieldDestList + "var columns = $(\"#list7\").datagrid(\"options\").columns;//得到columns对象\r\n";
	          onInitQueryFieldDestList = onInitQueryFieldDestList + "var trnum=0;//用于交换\r\n";

	          if(queryFieldList!=null&&queryFieldList.length>0){
	            for(i=0;i<queryFieldList.length;i++){
	               onInitQueryFieldDestList = onInitQueryFieldDestList + "//开始对比，查看表格是否已经存在相同字段，存在替换更新\r\n";
	               onInitQueryFieldDestList = onInitQueryFieldDestList + "for(var i=0;i<rows.length;i++){\r\n";
	               onInitQueryFieldDestList = onInitQueryFieldDestList + "if(rows[i].NAME7_3==\""+queryFieldList[i].getFIELD()+"\"){\r\n";
	               onInitQueryFieldDestList = onInitQueryFieldDestList + "       rows[i][columns[0][1].field]=\"是\";\r\n";
	               onInitQueryFieldDestList = onInitQueryFieldDestList + "       $('#list7').datagrid('refreshRow', i);\r\n";
	               onInitQueryFieldDestList = onInitQueryFieldDestList + "       mysortTr(i,'list7',trnum);\r\n";
	               onInitQueryFieldDestList = onInitQueryFieldDestList + "       trnum++;\r\n";
	               onInitQueryFieldDestList = onInitQueryFieldDestList + "    }\r\n";
	               onInitQueryFieldDestList = onInitQueryFieldDestList + " }\r\n";
	           }
	          }
	          onInitQueryFieldDestList = onInitQueryFieldDestList + "}\r\n";
	          //function onInitResultFieldDestList()
	          String onInitResultFieldDestList = "";

	          onInitResultFieldDestList ="function onInitResultFieldDestList(){\r\n";
	          onInitResultFieldDestList = onInitResultFieldDestList + "var rows = $(\"#list3\").datagrid(\"getRows\");//得到rows对象\r\n";
	          onInitResultFieldDestList = onInitResultFieldDestList + "var columns = $(\"#list3\").datagrid(\"options\").columns;//得到columns对象\r\n";
	          onInitResultFieldDestList = onInitResultFieldDestList + "var tableFieldName;\r\n";
	          onInitResultFieldDestList = onInitResultFieldDestList + "var trnum=0;\r\n";

	          String ISNUMBER="否";
	          String QALIGN ="居左";
	          if(resultFieldList!=null&&resultFieldList.length>0){
	            for(i=0;i<resultFieldList.length;i++){
	               if(resultFieldList[i].getISSHOW().equals("1")){//显示
	                   if (resultFieldList[i].getISNUMBER()==null){
	                 ISNUMBER="否";
	               }else{
	                 if (resultFieldList[i].getISNUMBER().equals("1")){
	                    ISNUMBER="是";
	                 }else{
	                    ISNUMBER="否";
	                 }
	               }
	               if (resultFieldList[i].getQALIGN()==null){
	                   QALIGN="居左";
	               }else{
	                 if (resultFieldList[i].getQALIGN().equals("0")){
	                    QALIGN="居左";
	                 }else if (resultFieldList[i].getQALIGN().equals("1")){
	                    QALIGN="居中";
	                 }else if (resultFieldList[i].getQALIGN().equals("2")){
	                    QALIGN="居右";
	                 }else{
	                        QALIGN="居左";
	                 }
	              }
	              onInitResultFieldDestList = onInitResultFieldDestList + "//开始对比，查看表格是否已经存在相同字段，存在替换更新===显示的\r\n";
	              onInitResultFieldDestList = onInitResultFieldDestList + "for(var i=0;i<rows.length;i++){\r\n";
	              onInitResultFieldDestList = onInitResultFieldDestList + "  if(rows[i].NAME3_3==\""+resultFieldList[i].getFIELD()+"\"){\r\n";
	              onInitResultFieldDestList = onInitResultFieldDestList + "          rows[i][columns[0][1].field]=\"是\";\r\n";
	              onInitResultFieldDestList = onInitResultFieldDestList + "          rows[i][columns[0][2].field]=\"显示\";\r\n";
	              onInitResultFieldDestList = onInitResultFieldDestList + "          rows[i][columns[0][3].field]=\""+resultFieldList[i].getCOLWIDTH()+"\";\r\n";
	              onInitResultFieldDestList = onInitResultFieldDestList + "          rows[i][columns[0][5].field]=\""+QALIGN+"\";\r\n";
	              onInitResultFieldDestList = onInitResultFieldDestList + "          rows[i][columns[0][6].field]=\""+ISNUMBER+"\";\r\n";
	              onInitResultFieldDestList = onInitResultFieldDestList + "          $('#list3').datagrid('refreshRow', i);\r\n";
	              onInitResultFieldDestList = onInitResultFieldDestList + "          mysortTr(i,'list3',trnum);\r\n";
	              onInitResultFieldDestList = onInitResultFieldDestList + "          trnum++;\r\n";
	              onInitResultFieldDestList = onInitResultFieldDestList + "  }}\r\n";
	            }else{//隐藏
	               onInitResultFieldDestList = onInitResultFieldDestList + "//开始对比，查看表格是否已经存在相同字段，存在替换更新==隐藏的\r\n";
	               onInitResultFieldDestList = onInitResultFieldDestList + "for(var i=0;i<rows.length;i++){\r\n";
	               onInitResultFieldDestList = onInitResultFieldDestList + "if(rows[i].NAME3_3==\""+resultFieldList[i].getFIELD()+"\"){\r\n";
	               onInitResultFieldDestList = onInitResultFieldDestList + " rows[i][columns[0][1].field]=\"是\";\r\n";
	               onInitResultFieldDestList = onInitResultFieldDestList + " rows[i][columns[0][2].field]=\"隐藏\";\r\n";
	               onInitResultFieldDestList = onInitResultFieldDestList + "         $('#list3').datagrid('refreshRow', i);\r\n";
	               onInitResultFieldDestList = onInitResultFieldDestList + "         mysortTr(i,'list3',trnum);\r\n";
	               onInitResultFieldDestList = onInitResultFieldDestList + "         trnum++;\r\n";
	               onInitResultFieldDestList = onInitResultFieldDestList + "}}\r\n";
	            }
	           }
	          }
	          onInitResultFieldDestList = onInitResultFieldDestList + "}\r\n";
	          //function onInitDetailParameterBind()
	          String onInitDetailParameterBind = "";
	          onInitDetailParameterBind = "function onInitDetailParameterBind(){\r\n";
	          onInitDetailParameterBind = onInitDetailParameterBind + "var tableFieldName;\r\n";
	          if(detailParameterList!=null&&detailParameterList.length>0){
	            for(i=0;i<detailParameterList.length;i++){
	              onInitDetailParameterBind = onInitDetailParameterBind + "var row={};\r\n";
	              onInitDetailParameterBind = onInitDetailParameterBind + "tableFieldName=\""+detailParameterList[i].getFIELD()+"\";\r\n";
	              onInitDetailParameterBind = onInitDetailParameterBind + "row.NAME4_1=\""+detailParameterList[i].getID()+"\";\r\n";
	              onInitDetailParameterBind = onInitDetailParameterBind + "row.NAME4_2=\""+detailParameterList[i].getNAME()+"\";\r\n";
	              onInitDetailParameterBind = onInitDetailParameterBind + "row.NAME4_3=\""+queryConfig.getTableFieldCnNameByEn(detailParameterList[i].getFIELD())+"\";\r\n";
	              onInitDetailParameterBind = onInitDetailParameterBind + "row.NAME4_4=tableFieldName;\r\n";
	              onInitDetailParameterBind = onInitDetailParameterBind + "row.NAME4_5=\""+detailParameterList[i].getBID()+"\";\r\n";
	              onInitDetailParameterBind = onInitDetailParameterBind + "$(\"#list4\").datagrid(\"appendRow\", row);\r\n";
	           }
	         }
	         onInitDetailParameterBind = onInitDetailParameterBind + "}\r\n";
	         //function onInitgridSortBind()
	         String onInitgridSortBind = "";
	         onInitgridSortBind = "function onInitgridSortBind(){\r\n";
	         onInitgridSortBind = onInitgridSortBind + "var sortTypeEn,tableFieldName;\r\n";
	         if(orderList!=null&&orderList.length>0){
	           for(i=0;i<orderList.length;i++){
	             onInitgridSortBind = onInitgridSortBind + "var row={};\r\n";
	             onInitgridSortBind = onInitgridSortBind + "tableFieldName=\""+orderList[i].getFIELD()+"\";\r\n";
	             onInitgridSortBind = onInitgridSortBind + "sortTypeEn=\""+orderList[i].getTYPE()+"\";\r\n";
	             onInitgridSortBind = onInitgridSortBind + "row.NAME6_1=\""+orderList[i].getID()+"\";\r\n";
	             onInitgridSortBind = onInitgridSortBind + "row.NAME6_2=\""+queryConfig.getTableFieldCnNameByEn(orderList[i].getFIELD())+"\";\r\n";
	             onInitgridSortBind = onInitgridSortBind + "if(sortTypeEn==\"1\"){\r\n";
	             onInitgridSortBind = onInitgridSortBind + "    row.NAME6_3=\"升序\";\r\n";
	             onInitgridSortBind = onInitgridSortBind + "}else{\r\n";
	             onInitgridSortBind = onInitgridSortBind + "    row.NAME6_3=\"降序\";\r\n";
	             onInitgridSortBind = onInitgridSortBind + "}\r\n";
	             onInitgridSortBind = onInitgridSortBind + "row.NAME6_4=tableFieldName;\r\n";
	             onInitgridSortBind = onInitgridSortBind + "row.NAME6_5=sortTypeEn;\r\n";
	             onInitgridSortBind = onInitgridSortBind + "$(\"#list6\").datagrid(\"appendRow\", row);\r\n";
	          }
	         }
	         onInitgridSortBind = onInitgridSortBind + "}\r\n";

	          request.setAttribute("LIST1",LIST1);
	          request.setAttribute("LIST2",LIST2);
	          request.setAttribute("LIST3",LIST3);
	          request.setAttribute("LIST4",LIST4);
	          request.setAttribute("onInitRelationTableBind",onInitRelationTableBind);
	          request.setAttribute("onInitWhereFieldBind",onInitWhereFieldBind);
	          request.setAttribute("onInitQueryFieldDestList",onInitQueryFieldDestList);
	          request.setAttribute("onInitResultFieldDestList",onInitResultFieldDestList);
	          request.setAttribute("onInitDetailParameterBind",onInitDetailParameterBind);
	          request.setAttribute("onInitgridSortBind",onInitgridSortBind);

	          request.setAttribute("objConfigTable",objConfigTable);
	          pagepath = "ZrQueryEngine/Config/ManageConfig_edit";
	          return pagepath;
	      }
	      //-------------------------------------
	      //设置条件------------------------------
	      else if (strAct.equals("ConValueSetup")) {
	           Variable[] Variables = null;
	           Variables = queryConfig.getVariableList();
	           String strResult=request.getParameter("strResult");
	           String name=request.getParameter("name");
	           String win=request.getParameter("win");
	           String LIST = "";
	           if(Variables!=null)
	           {
	             for(int i=0;i< Variables.length;i++)
	             {
	                 LIST = LIST + "<option value=\""+Variables[i].getCODE()+"\">"+Variables[i].getNAME()+"</option>\r\n";
	             }
	           }
	           request.setAttribute("LIST",LIST);
	           request.setAttribute("Result",strResult);
	           request.setAttribute("name",name);
	           request.setAttribute("win",win);
	           pagepath = "ZrQueryEngine/Config/ConValueSetup";
	           return pagepath;
	       }
	       //-----------------------------------
	       //得到字拼音快速录入选项---
	       else if (strAct.equals("selectdict")) {
	           String strDictTable = request.getParameter("DictTable");
	           String strSelectId = request.getParameter("SelectId");
	           String strList = queryConfig.getDictList(strDictTable);
	           request.setAttribute("SID",strSelectId);
	           request.setAttribute("SLIST",strList);
	           pagepath = "ZrQueryEngine/selectDictPage";
	           return pagepath;
	        }
	        //导出excel---
	       else if (strAct.equals("excel")) {
	           Date date = new Date();
	           DateFormat fmt = new SimpleDateFormat("yyyyMMdd");
	           String StrID = request.getParameter("ID");
	           String Strpage = request.getParameter("page");
	           if (Strpage==null){Strpage="1";}
	           String Strcrow = request.getParameter("crow");
	           if (Strcrow==null){Strcrow="0";}
	           String Strexp= request.getParameter("exp");
	           if (Strexp==null){Strexp="0";}
	           
	           String P1 = request.getParameter("P1");
	           String P2 = request.getParameter("P2");
	           String P3 = request.getParameter("P3");
	           String P4 = request.getParameter("P4");
	           String P5 = request.getParameter("P5");
	           int k=0;
	           @SuppressWarnings("unused")
			String snum = "01";

	           QueryControlImpl queryControlImpl = (QueryControlImpl) (session.getAttribute("queryControl"));
	           String fileName="",fileName1="",fileurl="";
	           fileName=queryControlService.getQueryTitle(StrID) + fmt.format(date);
	           
	           if (Integer.parseInt(Strcrow)>5000 && Strexp.equals("0"))//分多文件导出
	           {
	        	   fileurl = "<br>&nbsp;提示：导出的记录数大于5000条时将分成多个文件导出。<br><br>";
	        	   for (int i=0;i<=Integer.parseInt(Strcrow);i=i+5000)
	        	   {
	        	         k++;
	        	         if (k<10){snum="0"+String.valueOf(k);}
	        	         else
	        	         {snum=String.valueOf(k);}
	        	         fileName1 = fileName +"("+String.valueOf(k)+")"+ ".XLS";
	        	         
	        	         fileurl = fileurl + "&nbsp;<a href=\"/query/openquerycfg?Act=excel&exp=1&crow="+Strcrow+"&page="+String.valueOf(k)+"&ID="+StrID+"&P1="+P1+"&P2="+P2+"&P3="+P3+"&P4="+P4+"&P5="+P5+"  target=\"_blank\">"+fileName1+"</a><br>\r\n";
	        	   }
	        	   response.getWriter().print(fileurl);
		           return "";
	           }
	           if (Integer.parseInt(Strcrow)<5000 && Strexp.equals("0"))
	           {
	        	   fileurl = "<br>&nbsp;提示：导出的记录数大于5000条时将分成多个文件导出。<br><br>";
	        	   fileName1 = fileName + ".XLS";
	        	   fileurl = fileurl + "&nbsp;<a href=\"/query/openquerycfg?Act=excel&exp=1&crow="+Strcrow+"&page=1&ID="+StrID+"&P1="+P1+"&P2="+P2+"&P3="+P3+"&P4="+P4+"&P5="+P5+"  target=\"_blank\">"+fileName1+"</a><br>\r\n";
	        	   response.getWriter().print(fileurl);
	        	   return "";
	           }
	           
	           try {
	        	   if (Integer.parseInt(Strcrow)>5000)
	        	   {
	                 fileName = queryControlService.getQueryTitle(StrID) + fmt.format(date) +"("+Strpage+")"+ ".XLS";
	        	   }else
	        	   {
	        		 fileName = queryControlService.getQueryTitle(StrID) + fmt.format(date) + ".XLS";  
	        	   }
	        	   JxExcel jxExcel = new JxExcel();
	               response.reset();
	               response.setContentType("application/vnd.ms-excel;charset=UTF-8");
	               response.addHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes("GBK"), "iso8859-1"));
	               ServletOutputStream os = response.getOutputStream();
	               jxExcel.WriteExcel(queryControlImpl, os, StrID, user, P1, P2, P3, P4, P5, Strpage, Strcrow);
	               os.flush();
	               os.close();
	               os=null;
	               response.flushBuffer();
	            }catch (Exception ex) {
	                out.print("导出Excel文件出现异常" + ex.getMessage());
	            }
	      }
	      return "";
	}

	///=====================================控制器接口私有方法=====================================///
	/**
	 * 修改配置按扭信息
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	private void EditButton(HttpServletRequest request, HttpServletResponse response) throws IOException {
		QUERY_CONFIG_BUTTON Button = new QUERY_CONFIG_BUTTON();
		Button.fullDataFromRequest(request);
		try {
			queryButtonService.editButton(Button);
		} catch (Exception ex) {
			this.Msg = "配置按扭管理中间件服务失败！<br>详细错误信息：" + ex.toString();
		}
		this.out = response.getWriter();
	}

	/**
	 * 删除配置按扭
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	private void DeleteButton(HttpServletRequest request, HttpServletResponse response) throws IOException {
		try {
			FunctionMessage fm = new FunctionMessage(1);
			fm = queryButtonService.deleteButton(request.getParameter("ID"));
			if (fm.getResult()) {
				this.Msg = fm.getMessage();
				this.isOk = fm.getResult();
			} else {
				this.Msg = fm.getMessage();
				this.isOk = fm.getResult();
			}
		} catch (Exception ex) {
			this.Msg = "配置按扭管理中间件服务失败！<br>详细错误信息：" + ex.toString();
		}
		this.out = response.getWriter();
	}

	/**
	 * 添加配置按扭
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	private void AddButton(HttpServletRequest request, HttpServletResponse response) throws IOException {
		QUERY_CONFIG_BUTTON ButtonID = new QUERY_CONFIG_BUTTON();
		try {
			ButtonID.fullDataFromRequest(request);
			queryButtonService.addButton(ButtonID);
		} catch (Exception ex2) {
			this.Msg = "配置按扭管理中间件服务失败！<br>详细错误信息：<br>   " + ex2.toString();
		}
		this.out = response.getWriter();
	}

	/**
	 * 处理基本属性
	 * @return boolean
	 */
	private boolean saveBaseProperty() {
	    try {
	    	return queryConfig.saveBaseProperty(objConfigTable);
	    } catch (Exception ex) {
	    	ex.printStackTrace();
	    	return false;
	    }
	}

	/**
	 * 关联表属性
	 * @return
	 */
	private boolean saveRelationProperty() {
	    try {
	    	return queryConfig.saveRelationProperty(
	    		  objConfigTable.getID(), _request.getParameter("RelationInfoStr"));
	    } catch (Exception ex) {
	    	ex.printStackTrace();
	    	return false;
	    }
	}

	/**
	 * 初始条件属性
	 * @return
	 */
	private boolean saveInitWhereProperty() {
	    try {
	    	return queryConfig.saveInitWhereProperty(
	    			objConfigTable.getID(),  _request.getParameter("WhereInfoStr"));
	    } catch (Exception ex) {
	    	ex.printStackTrace();
	    	return false;
	    }
	}

	/**
	 * 查询列属性
	 */
	private boolean saveQueryFieldProperty() {
	    try {
	    	return queryConfig.saveQueryFieldProperty(
	    			objConfigTable.getID(), _request.getParameter("QueryFieldInfoStr"));
	    } catch (Exception ex) {
	    	ex.printStackTrace();
	    	return false;
	    }
	}

	/**
	 * 结果列属性
	 * @return
	 */
	private boolean saveResultFieldProperty() {
	    try {
	    	return queryConfig.saveResultFieldProperty(
	    			objConfigTable.getID(), 
	    			_request.getParameter("ResultDisFieldInfoStr"), 
	    			_request.getParameter("ResultHidFieldInfoStr"));
	    } catch (Exception ex) {
	    	ex.printStackTrace();
	    	return false;
	    }
	}

	/**
	 * 按钮参数属性
	 * @return
	 */
	private boolean saveDetailParameterProperty() {
	    try {
	    	return queryConfig.saveDetailParameterProperty(
	    			objConfigTable.getID(),
	    			_request.getParameter("DetailParameterInfoStr"),
	    			_request.getParameter("BRelationInfoStr"));
	    } catch (Exception ex) {
	    	ex.printStackTrace();
	    	return false;
	    }
	}

	/**
	 * 排序参数属性
	 * @return
	 */
	private boolean saveSortProperty() {
	    try {
	    	return queryConfig.saveSortProperty(
	    			objConfigTable.getID(),  _request.getParameter("SortInfoStr"));
	    } catch (Exception ex) {
		   ex.printStackTrace();
		   return false;
	    }  
	}

	private String getTemplateStr(SessionUser user, HttpServletRequest _request, Request rq, String TITLENAME) {
	    @SuppressWarnings("unused")
		StringBuffer sb = new StringBuffer();
//	    String strRe ="";
//	    String sumROW = "0";
//	    String QTABLETYPE = "1";
//
//	    String ID = _request.getParameter("ID");
//	    if (ID == null) {ID = "";}
//	    String SHOWTITLE = _request.getParameter("SHOWTITLE");
//	    if (SHOWTITLE == null) {SHOWTITLE = "";}
//	    String STB = _request.getParameter("STB");
//	    if (STB == null) {STB = "0";}
//	    String ALIGN = _request.getParameter("ALIGN");
//	    if (ALIGN == null) {ALIGN = "center";}
//	    String XH = _request.getParameter("XH");
//	    if (XH == null) {XH = "1";}
//
//	    String ISCODE = _request.getParameter("ISCODE");
//	    if (ISCODE == null) {ISCODE = "1";}
//	    if (ISCODE.length()==0) {ISCODE = "1";}
//
//	    String ISNULL = _request.getParameter("ISNULL");
//	    if (ISNULL == null) {ISNULL = "1";}
//
//	    String BUTTONIDS = _request.getParameter("BUTTONIDS");
//	    if (BUTTONIDS == null) {BUTTONIDS = "";}
//
//	    String ifFirstData = _request.getParameter("ifFirstData");
//	    String strWhere = _request.getParameter("strWhere");
//	    if(strWhere == null){strWhere = "";}
//
//	    String queryFlag =_request.getParameter("queryFlag");
//	    if(queryFlag == null){queryFlag = "0";}
//
//	    String swidth =_request.getParameter("SWIDTH");
//	    if(swidth == null){swidth = "0";}
//
//	    String sheight =_request.getParameter("SHEIGHT");
//	    if(sheight == null){sheight = "0";}
//
//	    int currentPage=1;
//	    try {
//	      currentPage = Integer.parseInt(_request.getParameter("page"));
//	    }
//	    catch (Exception ex) {
//	      currentPage = 1;
//	    }
//	    try {
//	      QUERY_CONFIG_TABLE queryConfigEntity = queryControlService.getConfigTable(ID);
//	      String queryHidDis = _request.getParameter("queryDisHid");
//
//	      if (queryHidDis != null) {
//	        if (queryHidDis.trim().length() > 0) {
//	          queryHidDis = "none";
//	        }
//	        else {
//	          queryHidDis = "";
//	        }
//	      }
//	      else {
//	        if (queryConfigEntity.getTYPE().equals("2")) {
//	          queryHidDis = "none";
//	        }
//	        else {
//	          queryHidDis = "";
//	        }
//	      }
//	      //1 一般查询     2 操作查询     3 显示列表    4 嵌入Iframe  5 查询录入项和结果分离
//	      String queryType = queryConfigEntity.getTYPE();
//
//	      sb.append("<HTML>\r\n");
//	      sb.append("<HEAD>\r\n");
//	      sb.append("<TITLE></TITLE>\r\n");
//	      sb.append("<META HTTP-EQUIV=\"Pragma\" CONTENT=\"no-cache\">\r\n");
//	      sb.append("<META http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\r\n");
//
//	      //新框架
//	      sb.append("<link rel=\"stylesheet\" type=\"text/css\" href=\"/static/easyui/themes/default/easyui.css\">\r\n");
//	      sb.append("<link rel=\"stylesheet\" type=\"text/css\" href=\"/static/easyui/themes/icon.css\">\r\n");
//	      sb.append("<script type=\"text/javascript\" src=\"/static/easyui/js/jquery.min.js\"></script>\r\n");
//	      sb.append("<script type=\"text/javascript\" src=\"/static/easyui/js/jquery.easyui.min.js\"></script>\r\n");
//	      sb.append("<script type=\"text/javascript\" src=\"/static/easyui/js/easyui.common.js\"></script>\r\n");
//	      sb.append("<script type=\"text/javascript\" src=\"/static/easyui/js/easyui-lang-zh_CN.js\"></script>\r\n");
//	      sb.append("<script type=\"text/javascript\" src=\"/static/scripts/QueryEngine.js\" type=\"text/jscript\" ></script>\r\n");
//	      sb.append("<script type=\"text/javascript\" src=\"/static/scripts/QueryPublic.js\"></script>\r\n");
//
//	      sb.append("<style>\r\n");
//	      sb.append("td{font-size: 12px;padding-left:10px;}\r\n");
//	      sb.append("</style>\r\n");
//
//	      sb.append("</HEAD>\r\n");
//	      sb.append("<BODY class=\"BodyMain\" style=\"margin: 0px;margin-right: 1px;width:100%;height:100%;;overflow:hidden;\">\r\n");
//	      sb.append("<FORM name=\"recordfrm\" id=\"recordfrm\" method=\"post\" Action=\"/query/QueryPage?ID=")
//	      	.append(ID).append("&SHOWTITLE="+SHOWTITLE+"&TITLENAME="+TITLENAME+"&STB="+STB+"&ALIGN="+ALIGN+"&XH="+XH+"&ISCODE="+ISCODE+"&BUTTONIDS="+BUTTONIDS+"&ISNULL="+ISNULL+"\">\r\n");
//	      //页面传递的参数
//	      String P1, P2, P3, P4, P5;
//	      P1 = _request.getParameter("P1");
//	      if (P1 != null) {
//	        sb.append("<input type=\"hidden\" name=\"P1\" value=\"" ).append(P1).append("\">\r\n");
//	      }
//	      P2 = _request.getParameter("P2");
//	      if (P2 != null) {
//	        sb.append("<input type=\"hidden\" name=\"P2\" value=\"").append(P2).append("\">\r\n");
//	      }
//	      P3 = _request.getParameter("P3");
//	      if (P3 != null) {
//	        sb.append("<input type=\"hidden\" name=\"P3\" value=\"").append(P3).append("\">\r\n");
//	      }
//	      P4 = _request.getParameter("P4");
//	      if (P4 != null) {
//	        sb.append("<input type=\"hidden\" name=\"P4\" value=\"").append(P4).append("\">\r\n");
//	      }
//	      P5 = _request.getParameter("P5");
//	      if (P5 != null) {
//	        sb.append("<input type=\"hidden\" name=\"P5\" value=\"").append(P5).append("\">\r\n");
//	      }
//	      sb.append("<input type=\"hidden\" name=\"page\" value=\"\">\r\n");
//	      sb.append("<input type=\"hidden\" name=\"setall\" value=\"0\">\r\n");
//	      sb.append("<input type=\"hidden\" name=\"queryDisHid\" value=\"")
//	      	.append(queryHidDis).append("\">\r\n");
//	      //嵌入式查询时候
//	      if(queryType.equals("5")&&ifFirstData != null){
//	          //queryType="2";//转换为操作查询
//	          sb.append("<input type=hidden name=ifFirstData id=ifFirstData value=1>\r\n");
//	      }else{
//	          if(ifFirstData == null){ifFirstData = queryConfigEntity.getIFFIRSTDATA();}
//	          sb.append("<input type=hidden name=ifFirstData id=ifFirstData value="+ifFirstData+">\r\n");
//	      }
//	      sb.append("<input type=hidden name=strWhere value="+strWhere+">\r\n");
//	      sb.append("<input type=hidden name=queryFlag id=queryFlag value="+queryFlag+">\r\n");
//	      sb.append("<input type=hidden name=SWIDTH value="+swidth+">\r\n");
//	      sb.append("<input type=hidden name=SHEIGHT value="+sheight+">\r\n");
//
//	      String getQTABLETYPE = "";
//	      if (queryConfigEntity.getQTABLETYPE()==null){
//	        getQTABLETYPE = "1";
//	      }else{
//	        getQTABLETYPE = queryConfigEntity.getQTABLETYPE();
//	      }
//	      if (getQTABLETYPE.equals("2")){
//	        sb.append("<input type=hidden id=selectrow name=selectrow value=0>\r\n");
//	      }else{
//	        sb.append("<input type=hidden id=selectrow  name=selectrow value=x>\r\n");
//	      }
//	      if(!queryConfigEntity.getTYPE().equals("4") && !SHOWTITLE.equals("0")){
//	        if (TITLENAME.length()==0){
//	           TITLENAME = queryConfigEntity.getNAME();
//	        }
//	      }
//	      sb.append("<!--HTML Body Start显示表单开始部分-->");
//	      sb.append("<div id=\"bodydiv\" class=\"easyui-layout\" style=\"width:100%;height:100%;;overflow:hidden;\"> "
//	               +"<TABLE height=\"99.9%\" width=\"100%\" cellSpacing='0' >\r\n");
//
//	      String queryItemDis = "";
//	      //按钮js
//	      StringBuffer button=new StringBuffer();
//	      button.append("var toolbarTable = ['-',");//userOruint
//	      if (!queryType.equals("3") && !queryType.equals("4")){
//	        if(queryType.equals("5") && queryFlag.equals("1")){
//	          queryItemDis = "none";
//	        }
//	        sb.append("<TR height=2><TD></TD></TR>");
//	        String queryItemStr = queryControlService.createQueryItem(ID,queryConfigEntity.getQITEMTYPE(),user,rq);
//	        int ItemRowNum = queryControlService.getQueryItemRowNum(ID,queryConfigEntity.getQITEMTYPE());
//	        if (ItemRowNum > 0) {
//	          sb.append("<TR style=\"display:'"+queryItemDis+"'\"><TD  valign=TOP>\r\n");
//	            if(queryType.equals("5") && queryFlag.equals("0")){
//	              sb.append(" <DIV id=\"queryItem\" class=\"datagrid-querybar\" style='padding:5px 0;overflow:auto;'>\r\n");
//	            }else{
//	              sb.append(" <DIV id=\"queryItem\" class=\"datagrid-querybar\"  style='width:99.5%;overflow:auto;'>\r\n");
//	            }
//	          sb.append("<TABLE border='0' width='1155px' cellspacing=\"0\" cellpadding=\"1\" valign=center>\r\n");
//	          sb.append("<TR><TD width=135px></TD><TD width=250px></TD><TD width=135px></TD><TD width=250px></TD><TD width=135px></TD><TD width=250px></TD></TR>\r\n");
//	          sb.append(queryItemStr);
//	          sb.append("</TABLE>\r\n");
//	          sb.append("</DIV>\r\n");
//
//	        if (ItemRowNum > 0) {
//	          if(!(queryType.equals("5") && queryFlag.equals("1"))){
//	            //查询按钮
//	                  button.append("{text:'查 询',iconCls:'userOruint',handler:function(){javascript:recordfrm.page.value=\"1\";recordfrm.ifFirstData.value=\"1\";recordfrm.queryFlag.value=\"1\";submitquery();}},'-',\r\n");
//	          }
//	        }
//	        if((queryType.equals("5") && queryFlag.equals("1"))){
//	                //返 回按钮
//	            button.append("{text:'返 回',iconCls:'icon-undo',handler:function(){javascript:recordfrm.page.value=\"1\";recordfrm.ifFirstData.value=\"0\";recordfrm.queryFlag.value=\"0\";document.recordfrm.submit();}},'-',\r\n");
//	         }
//	        //	        自定义的按钮
//	        button.append(queryControlService.createButtonNavigate(ID,"1",user,BUTTONIDS));
//	        if(queryConfigEntity.getIFCOMBTN().equals("1")){
//	                 button.append("{text:'导出Excel',iconCls:'icon-ftl-info',handler:function(){javascript:expexcel('/query/openquerycfg?Act=excel&ID="+ID+"&P1="+P1+"&P2="+P2+"&P3="+P3+"&P4="+P4+"&P5="+P5+"');}},'-',\r\n");
//	         }
//	        if(ItemRowNum > 0 && !queryType.equals("5")) {
//	                 button.append("{text:'显示/隐藏查询',iconCls:'icon-filter',handler:function(){javascript:showquery();}},'-',\r\n");
//	        }
//	        //封装按钮
//	        button.deleteCharAt(button.length()-1);
//	        }
//	      }
//	      if(button.toString().length()>28){
//	          button.append(" ];");
//	      }else{
//	          button.delete(0, button.length());
//	          button.append("var toolbarTable =null;");
//	      }
//	      sb.append("<TR><TD height=\"100%\" valign=\"top\">\r\n");
//
//	      //===============显示表格数据
//	      if(!(queryType.equals("5") && queryFlag.equals("0")))  //若是5，则在没有查询前是不需要显示的
//	      {
//	        QTABLETYPE = queryConfigEntity.getQTABLETYPE();
//	        if (QTABLETYPE==null){QTABLETYPE="";}
//	        if (QTABLETYPE.length()==0){QTABLETYPE="1";}
//	        if (QTABLETYPE.equals("1")){
//	            sb.append("<div data-options=\"region:'center',title:''\" class=\"panel-body\" style=\"width:99.7%;height:100%;\">");
//	            sb.append(queryControlService.getQueryResultTable(ID,queryConfigEntity.getQTABLETYPE(),queryType,user,rq,P1,P2,P3,P4,P5,ifFirstData,currentPage,swidth,sheight,XH));
//	            sb.append("</div>");
//	        }else{
//	            sb.append("<div data-options=\"region:'center',title:''\" class=\"panel-body\" style=\"width:99.7%;height:100%;\">");
//	            sb.append(queryControlService.getQueryResultTable(ID,queryConfigEntity.getQTABLETYPE(),queryType,user,rq,P1,P2,P3,P4,P5,ifFirstData,currentPage,swidth,sheight,XH));
//	            sb.append("</div>");
//	        }
//	      }else{
//	          sb.append("<div style=\"padding:5px 0;\" class='datagrid-querybar' align=\"center\">");
//	          sb.append("<a href=\"javascript:recordfrm.page.value=1;recordfrm.ifFirstData.value=1;recordfrm.queryFlag.value=1;submitquery();\" class=\"easyui-linkbutton\" data-options=\"iconCls:'userOruint'\" style=\"width:80px\">查询信息</a>");
//	          sb.append("</div>");
//	      }
//	      sb.append("</TD></TR>\r\n");
//	      sb.append("</TABLE>\r\n</div>\r\n");
//	      sb.append("</FORM>\r\n");
//
//	      //输出必填项js=========================================
//	      sb.append("<script type=\"text/javascript\">\r\n");
//	      sb.append("//设置表格监听方法\r\n");
//	      sb.append("var oldHeit=0;//默认高度\r\n");
//	      sb.append("$(window).resize(function(){\r\n");
//	      sb.append("  $('#data').datagrid('resize', {\r\n");
//	      sb.append("    width:function(){return document.body.clientWidth;},\r\n");
//	      sb.append("    height:function(){return document.body.clientHeight;},\r\n");
//	      sb.append("  });\r\n");
//	      sb.append("  \r\n");
//	      sb.append("}); \r\n");
//	      //操作查询，
//	      if(queryType.equals("2")){
//	          sb.append("document.all.queryItem.style.display='none';\r\n");
//	      }
//	      //分离查询
//	      if(queryType.equals("5") && queryFlag.equals("1")){
//	          sb.append("document.all.queryItem.style.display='none';\r\n");
//	      }
//	      //设置表格按钮
//	      sb.append(""+button.toString()+"\r\n\r\n\r\n");
//	      
//	      //输出必填项js
//	      //String outJS = queryControl.GetIsmustJs(ID,ISNULL);
//	      sb.append("//查询方法\r\n");
//	      sb.append("function submitquery(){\r\n");
//	      sb.append("  var isnull='0';\r\n");
//	      sb.append("  document.recordfrm.submit();\r\n");
//	      sb.append("}\r\n");
//	      sb.append("\r\n");
//	      sb.append("//显示隐藏查询项目\r\n");
//	      sb.append("function showquery(){\r\n");
//	      sb.append(" if($('#queryItem').css('display')=='none'){\r\n");
//	      sb.append("     $('#queryItem').show();\r\n");
//	      sb.append("     $('.datagrid-view').height($(\"#bodydiv\").height()-$('#queryItem').height()-70);\r\n");//
//	      sb.append("     $('.datagrid-body').height($(\"#bodydiv\").height()-$('#queryItem').height()-95);\r\n");
//	      sb.append(" }else{\r\n");
//	      sb.append("     $('#queryItem').hide();\r\n");
//	      sb.append("     $('#data').datagrid('resize', {\r\n");
//	      sb.append("          height:function(){return document.body.clientHeight;}\r\n");
//	      sb.append("     });\r\n");
//	      sb.append(" }\r\n");
//
//	      sb.append("\r\n");
//	      sb.append("}\r\n");
//	      //获取分页显示
//          String Navigate1[] = queryControlService.createPageNavigate(ID,user,P1,P2,P3,P4,P5,currentPage);
//          sumROW = "0";//总行数
//          if(Navigate1[1]==null||Navigate1[1]==""){
//                        sumROW = "0";//总行数
//          }else{
//                        sumROW = Navigate1[1];//总行数
//          }
//	      sb.append("//导出EXCL\r\n");
//	      sb.append("function expexcel(url){\r\n");
//	      sb.append("url=url+'&crow="+sumROW+"';\r\n");      
//	      sb.append("onexcel(url);\r\n");
//	      sb.append("}\r\n\r\n");
//	      sb.append("//设置页面的初始高度\r\n");
//	      sb.append("function getOutHeight(){\r\n");
//	      sb.append("    oldHeit=$('.datagrid-view').height();\r\n");
//	      sb.append("}\r\n\r\n");
//	      sb.append("setTimeout(\"getOutHeight();\",1000);\r\n\r\n");
//
//	      sb.append("//表格单击双击事件\r\n");
//	      sb.append("$(function(){ \r\n");
//	      sb.append("  $('#data').datagrid({\r\n");
//	      sb.append("  //单击事件   \r\n");
//	      sb.append("  onClickRow:function(rowIndex,rowData){\r\n");
//
//	      sb.append("    //alert(\"单击\");\r\n");
//	      sb.append("  },\r\n");
//	      sb.append("  //双击事件\r\n");
//	      sb.append("  onDblClickRow:function(rowIndex,rowData){\r\n");
//	      String scriptStr = "";
//	      try {
//	    	  scriptStr = queryControlService.createButtonScript(
//	    			  queryControlService.getButtonByID(queryConfigEntity.getBID()), ID);
//	      }
//	      catch (Exception ex) {}
//	      if (scriptStr.trim().length() > 0) {
//	        sb.append("parseButtonJs(\"").append(scriptStr).append("\");\r\n");
//	      }
//	      sb.append("  //alert(\"双击\");\r\n");
//	      sb.append("  }\r\n");
//	      sb.append("  });\r\n");
//	      sb.append("}); \r\n");
//
//	      //初始化页面高度
//	      sb.append("//初始化页面高度\r\n");
//	      sb.append("$(\"#bodydiv\").height(document.body.clientHeight); \r\n");
//	      sb.append("$(\"#bodydiv\").height(document.body.clientHeight); \r\n");
//	      sb.append("</script>\r\n");
//
//	      //弹出导出EXCL窗口
//	      sb.append("<!-- 弹出窗口 -->\r\n");
//	      sb.append("<div id=\"win\" ></div>\r\n");
//	      sb.append("</BODY>\r\n");
//	      sb.append("</HTML>\r\n");
//
//	        strRe = sb.toString();
//	        //将按钮带有参数的替换成实际的参数值
//	        if (P1 == null) {P1="";}
//	        if (P2 == null) {P2="";}
//	        if (P3 == null) {P3="";}
//	        if (P4 == null) {P4="";}
//	        if (P5 == null) {P5="";}
//	        strRe=strRe.replaceAll("\\{P1\\}",P1);
//	        strRe=strRe.replaceAll("\\{P2\\}",P2);
//	        strRe=strRe.replaceAll("\\{P3\\}",P3);
//	        strRe=strRe.replaceAll("\\{P4\\}",P4);
//	        strRe=strRe.replaceAll("\\{P5\\}",P5);
//	        //--------------
//
//	      HttpSession session = _request.getSession(true);
//	      session.setAttribute("queryControl", queryControlService);
//	    } catch (Exception ex) {
//	    	ex.printStackTrace();
//	    	return "";
//	    }
	    return "";//strRe
	}

	private String getPhoneTemplateStr(SessionUser user, HttpServletRequest _request, 
			Request req, String TITLENAME) {
	    StringBuffer sb = new StringBuffer();
	    String strRe ="";
	    String ID = _request.getParameter("ID");
	    if (ID == null) {ID = "";}
	    String SHOWTITLE = _request.getParameter("SHOWTITLE");
	    if (SHOWTITLE == null) {SHOWTITLE = "";}
	    String ISCODE = _request.getParameter("ISCODE");
	    if (ISCODE == null) {ISCODE = "1";}
	    if (ISCODE.length()==0) {ISCODE = "1";}

	    String ISNULL = _request.getParameter("ISNULL");
	    if (ISNULL == null) {ISNULL = "1";}

	    String BUTTONIDS = _request.getParameter("BUTTONIDS");
	    if (BUTTONIDS == null) {BUTTONIDS = "";}

	    String ifFirstData = _request.getParameter("ifFirstData");
	    String strWhere = _request.getParameter("strWhere");
	    if(strWhere == null){strWhere = "";}

	    String queryFlag =_request.getParameter("queryFlag");
	    if(queryFlag == null){queryFlag = "0";}

	    String swidth =_request.getParameter("SWIDTH");
	    if(swidth == null){swidth = "0";}

	    String sheight =_request.getParameter("SHEIGHT");
	    if(sheight == null){sheight = "0";}

	    int currentPage=1;
	    try {
	      currentPage = Integer.parseInt(_request.getParameter("page"));
	    }
	    catch (Exception ex) {
	      currentPage = 1;
	    }
	    try {
	      QUERY_CONFIG_TABLE queryConfigEntity = queryService.getConfigTable(ID);
	      if(ifFirstData == null)
	      {ifFirstData = queryConfigEntity.getIFFIRSTDATA();}
	      String queryHidDis = _request.getParameter("queryDisHid");
	      if (queryHidDis != null) {
	        if (queryHidDis.trim().length() > 0) {
	          queryHidDis = "none";
	        }
	        else {
	          queryHidDis = "";
	        }
	      }
	      else {
	        if (queryConfigEntity.getTYPE().equals("2")) {
	          queryHidDis = "none";
	        }
	        else {
	          queryHidDis = "";
	        }
	      }
	      //获取查询标题
	      String title=queryConfigEntity.getNAME();
	      sb.append("<!doctype html>\r\n");
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
	      sb.append("<meta name=\"msapplication-TileImage\" content=\"assets/i/app-icon72x72@2x.png\">\r\n");
	      sb.append("<meta name=\"msapplication-TileColor\" content=\"#0e90d2\">\r\n");
	      sb.append("<link rel=\"icon\" type=\"image/png\" href=\"/static/assets/i/favicon.png\">\r\n");
	      sb.append("<link rel=\"icon\" sizes=\"192x192\" href=\"/static/assets/i/app-icon72x72@2x.png\">\r\n");
	      sb.append("<link rel=\"apple-touch-icon-precomposed\" href=\"/static/assets/i/app-icon72x72@2x.png\">\r\n");
	      sb.append("<link rel=\"stylesheet\" href=\"/static/assets/font_icon/css/font-awesome.css\">\r\n");
	      sb.append("<link rel=\"stylesheet\" href=\"/static/assets/css/amazeui.min.css\">\r\n");
	      sb.append("<link rel=\"stylesheet\" href=\"/static/assets/css/app.css\">\r\n");
	      sb.append("<link rel=\"stylesheet\" href=\"/static/assets/css/amazeui.datetimepicker.css\"/>\r\n");
	      sb.append("<script src=\"/static/assets/js/jquery.min.js\"></script>\r\n");
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

	      //查询条件及分页
	      sb.append("<!-- main  start -->\r\n");
	      sb.append("<div class=\"main-contor\">\r\n");
	      sb.append("    <!-- 侧边栏内容   开始 -->\r\n");
	      sb.append("<form name=\"recordfrm\" method=\"post\" Action=\"/query/QueryPageSerPhone?ID=").append(
	      ID).append("&SHOWTITLE="+SHOWTITLE+"&TITLENAME="+TITLENAME+"&ISCODE="+ISCODE+"&BUTTONIDS="+BUTTONIDS+"&ISNULL="+ISNULL+"\">\r\n");

	      //页面传递的参数
	      String P1, P2, P3, P4, P5;
	      P1 = _request.getParameter("P1");
	      if (P1 != null) {
	        sb.append("<input type=\"hidden\" name=\"P1\" value=\"" ).append(P1).append("\">\r\n");
	      }
	      P2 = _request.getParameter("P2");
	      if (P2 != null) {
	        sb.append("<input type=\"hidden\" name=\"P2\" value=\"").append(P2).append("\">\r\n");
	      }
	      P3 = _request.getParameter("P3");
	      if (P3 != null) {
	        sb.append("<input type=\"hidden\" name=\"P3\" value=\"").append(P3).append("\">\r\n");
	      }
	      P4 = _request.getParameter("P4");
	      if (P4 != null) {
	        sb.append("<input type=\"hidden\" name=\"P4\" value=\"").append(P4).append("\">\r\n");
	      }
	      P5 = _request.getParameter("P5");
	      if (P5 != null) {
	        sb.append("<input type=\"hidden\" name=\"P5\" value=\"").append(P5).append("\">\r\n");
	      }
	      sb.append("<input type=\"hidden\" name=\"page\" value=\"\">\r\n");
	      sb.append("<input type=\"hidden\" name=\"setall\" value=\"0\">\r\n");
	      sb.append("<input type=\"hidden\" name=\"queryDisHid\" value=\"").append(queryHidDis).append("\">");
	      sb.append("<input type=hidden name=ifFirstData value="+ifFirstData+">\r\n");
	      sb.append("<input type=hidden name=strWhere value="+strWhere+">\r\n");
	      sb.append("<input type=hidden name=queryFlag value="+queryFlag+">\r\n");
	      sb.append("<input type=hidden name=SWIDTH value="+swidth+">\r\n");
	      sb.append("<input type=hidden name=SHEIGHT value="+sheight+">\r\n");
	      String getQTABLETYPE = "";
	      if (queryConfigEntity.getQTABLETYPE()==null){
	        getQTABLETYPE = "1";
	      }else{
	        getQTABLETYPE = queryConfigEntity.getQTABLETYPE();
	      }if (getQTABLETYPE.equals("2")){
	        sb.append("<input type=hidden id=selectrow name=selectrow value=0>\r\n");
	      }else{
	        sb.append("<input type=hidden id=selectrow  name=selectrow value=x>\r\n");
	      }
	      //1、查询条件侧边栏目
	      String queryItemStr = queryService.createQueryItem(ID,queryConfigEntity.getQITEMTYPE(),user,req);
	      //2 一般查询     2 操作查询     3 显示列表    4 嵌入Iframe  5 查询录入项和结果分离
	      String queryType = queryConfigEntity.getTYPE();
	      //3查询结果
	      String conter=queryService.getQueryResultTable(ID,queryConfigEntity.getQTABLETYPE(),queryType,user,req,P1,P2,P3,P4,P5,ifFirstData,currentPage,swidth,sheight);
	      //4分页组件
	      String Navigate1[] = queryService.createPageNavigate(ID,user,P1,P2,P3,P4,P5,currentPage);
	      sb.append("<nav data-am-widget=\"menu\" class=\"am-menu  am-menu-offcanvas1\"  data-am-menu-offcanvas> \r\n");
	      sb.append("<div class=\"am-offcanvas\" >\r\n");
	      sb.append("  <div class=\"am-offcanvas-bar\">\r\n");
	      sb.append("  <ul class=\"am-menu-nav am-avg-sm-1\">\r\n");
	      sb.append("     "+queryItemStr+"\r\n");
	      //查询刷新按钮
	      sb.append("     <li class=\"am-parent\">\r\n");
	      sb.append("       <ul class=\"am-pagination\">\r\n");
	      sb.append("         <li style=\"display: inherit;width: 92%;text-align: center;\"><a href=\"javascript:recordfrm.page.value='1';recordfrm.submit();\" class=\"am-active\" >检索查询</a></li>\r\n");
	      sb.append("       </ul>\r\n");
	      sb.append("     </li>\r\n");

	      sb.append("     "+Navigate1[0]+"\r\n");
	      sb.append("  </ul>\r\n");
	      sb.append("  </div>\r\n");
	      sb.append("</div>\r\n");
	      sb.append("</nav>\r\n");
	      sb.append("</form></div>");
	      sb.append("<!-- 左边栏查询   结束 -->\r\n");

	      sb.append("<!-- 内容栏目  开始 -->\r\n");
	      sb.append("<div data-am-widget=\"slider\" class=\"am-slider am-slider-b1\" data-am-slider='{&quot;controlNav&quot;:false}' >\r\n");
	      sb.append("<ul class=\"am-slides\" id='tab_list'>\r\n");
	      sb.append("<li></li></ul>\r\n");
	      //查询结果显示
	      sb.append(conter);
	      sb.append("</ul>\r\n");
	      sb.append("</div>\r\n");
	      sb.append("<!-- 内容栏目  结束 -->\r\n");

	      sb.append("<!-- 按钮栏  开始 -->\r\n");
	      //查询是否有配置按钮
	      String buttow=queryService.createButtonNavigate(ID,queryConfigEntity.getQTABLETYPE(),user,BUTTONIDS);
	      sb.append("<div class=\"am-modal-actions\" id=\"my-actions\">\r\n");
	      sb.append("<div class=\"am-modal-actions-group\">\r\n");
	      sb.append("  <ul class=\"am-list\">\r\n");
	      sb.append("  "+buttow+"\r\n");
	      sb.append("  </ul>\r\n");
	      sb.append("</div>\r\n");
	      sb.append("<div class=\"am-modal-actions-group\">\r\n");
	      sb.append("  <button class=\"am-btn am-btn-secondary am-btn-block\" data-am-modal-close>取消</button>\r\n");
	      sb.append(" </div>\r\n");
	      sb.append("</div>\r\n");
	      sb.append("<!-- 按钮栏  结束 -->\r\n");
	      sb.append("</div>\r\n");
	      sb.append("<!-- main  end -->\r\n");
	      sb.append("<!-- model  start -->\r\n");
	      sb.append("<div class=\"am-modal am-modal-alert\" tabindex=\"-1\" id=\"my-erro\">\r\n");
	      sb.append("<div class=\"am-modal-dialog\">\r\n");
	      sb.append("  <div class=\"am-modal-hd\">请选择数据行操作!</div>\r\n");
	      sb.append("  </div>\r\n");
	      sb.append("  <div class=\"am-modal-footer\">\r\n");
	      sb.append("    <span class=\"am-modal-btn\">确定</span>\r\n");
	      sb.append("  </div>\r\n");
	      sb.append("</div>\r\n");
	      sb.append("</div>\r\n");
	      sb.append("<div class=\"am-modal am-modal-alert\" tabindex=\"-1\" id=\"my-tile\">\r\n");
	      sb.append("<div class=\"am-modal-dialog\">\r\n");
	      sb.append("  <div class=\"am-modal-hd\">不存在指定字段的值，按钮参数设置有误，请检查！!</div>\r\n");
	      sb.append("  </div>\r\n");
	      sb.append("  <div class=\"am-modal-footer\">\r\n");
	      sb.append("    <span class=\"am-modal-btn\">确定</span>\r\n");
	      sb.append("  </div>\r\n");
	      sb.append("</div>\r\n");
	      sb.append("</div>\r\n");
	      sb.append("<!-- model  end -->\r\n");

	      sb.append("<script type=\"text/javascript\" src=\"/static/ZrPhoneEngine/js/input_table.js\"></script>\r\n");
	      sb.append("<script src=\"/static/assets/js/amazeui.min.js\"></script>\r\n");
	      sb.append("<script src=\"/static/assets/js/amazeui.datetimepicker.min.js\"></script>\r\n");
	      sb.append("<script src=\"/static/assets/js/locales/amazeui.datetimepicker.zh-CN.js\"></script>\r\n");
	      sb.append("<script type=\"text/javascript\">\r\n");
	      sb.append("_$set_htmlTab();//加载数据\r\n");
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
	      sb.append("$('.tr_row').on('click', function() {\r\n");
	      sb.append("	$('#my-actions').modal('open');\r\n");
	      sb.append("});\r\n");

	      sb.append("function TableOndblclick()");
	      sb.append("{");
	      String scriptStr = "";
	      try {
	    	  scriptStr = queryService.createButtonScript(queryControlService.getButtonByID(
	            queryConfigEntity.getBID()),ID);
	      }
	      catch (Exception ex) {
	      }
	      if (scriptStr.trim().length() > 0) {
	        sb.append("parseButtonJs_PHONE(\"").append(scriptStr).append("\");");
	      }
	      sb.append("}\r\n");

	      sb.append("function TableOnclick()");
	      sb.append("{");
	      scriptStr = "";
	      try {
	    	  scriptStr = queryService.createButtonScript(queryControlService.getButtonByID(
	            queryConfigEntity.getCID()),ID);
	      }
	      catch (Exception ex) {
	      }
	      if (scriptStr.trim().length() > 0) {
	        sb.append("parseButtonJs_PHONE(\"").append(scriptStr).append("\");");
	      }
	      sb.append("}\r\n");

	      sb.append("function ExeOndblclick(id)");
	      sb.append("{");
	      sb.append("}\r\n");

	      sb.append("function ExeOnclick(id)");
	      sb.append("{");
	      sb.append("}\r\n");
	      sb.append(" </script>\r\n");

	      sb.append("</body>\r\n");
	      sb.append("</html>\r\n");

	        strRe = sb.toString();
	        //将按钮带有参数的替换成实际的参数值
	        if (P1 == null) {P1="";}
	        if (P2 == null) {P2="";}
	        if (P3 == null) {P3="";}
	        if (P4 == null) {P4="";}
	        if (P5 == null) {P5="";}
	        strRe=strRe.replaceAll("\\{P1\\}",P1);
	        strRe=strRe.replaceAll("\\{P2\\}",P2);
	        strRe=strRe.replaceAll("\\{P3\\}",P3);
	        strRe=strRe.replaceAll("\\{P4\\}",P4);
	        strRe=strRe.replaceAll("\\{P5\\}",P5);
	        //--------------

	      HttpSession session = _request.getSession(true);
	      session.setAttribute("queryControl", queryControlService);
	    }
	    catch (Exception ex) {
	    	ex.printStackTrace();
	    	return "";
	    }
	    return strRe;  
	}
}