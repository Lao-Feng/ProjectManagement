package zr.zrpower.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import zr.zrpower.collectionengine.Request;
import zr.zrpower.intercept.annotation.ZrSafety;
import zr.zrpower.model.SessionUser;
import zr.zrpower.service.CollectionPhoneService;
import zr.zrpower.service.CollectionService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 表单引擎控制，返回html数据
 * @author nfzr
 *
 */
@ZrSafety
@Controller
@RequestMapping("/collect")
public class ZRCollectionController {
	/** 自定义表单引擎的核心服务. */
	@Autowired
	private CollectionService collectService;
	/** 自定义表单引擎的核心服务. */
	@Autowired
	private CollectionPhoneService collectPhoneService;

	/**
	 * ftl  调整（生成表单）
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("/collengine")
	public void collectEngine(HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.setContentType("text/html; charset=utf-8");
		PrintWriter output = null;
		try {
			output = response.getWriter();
			String html = "<p align=\"center\">非法调用，程序将自动返回上一界面...</p>";
		    int iMode = 0;    //采集模式：0采集模版预览，1打印模版预览，2数据采集，3打印
		    Request req = new Request(request);
		    if(req!=null && req.getCount()>0) {
		      String sMode = req.getStringItem("coll_mode");
		      if(sMode!=null&&!sMode.equals("")){
		         iMode = Integer.parseInt(sMode);
		      }else{
		    	  req.appendItem("coll_mode","0");
		      }
		      try {
		         HttpSession session = request.getSession(true);
		         SessionUser userInfo = (SessionUser) session.getAttribute("userinfo");
		         switch(iMode){
		            case 0 : html = collectService.collectionPriview(req,userInfo);
		                     break;
		            case 1 : html = collectService.printPriview(req,userInfo);
		                     break;
		            case 2 :html = collectService.collectionData(req,userInfo);
		                     break;
		            case 3 : html = collectService.printDocument(req);
		                     break;
		         }
		       }
		       catch (Exception ex) {
		    	   ex.printStackTrace();
		    	   output.print("<p>采集引擎发生异常，请稍后再试！</p><p>详细错误信息:</p><p>" + ex.toString() + "</p>"); ;
		       }
		    }
		    //临时加,采集引擎没有调用到配置信息时刷新父窗口-------------
		    if (html.equals("采集引擎发生异常：<br>流程配置与采集配置错误，未找到相关配置信息。") 
		    		|| html.equals("采集引擎发生错误，采集配置信息未找到！")) {
                StringBuffer sb = new StringBuffer();
                sb.append("<HTML>");
                sb.append("<script language=\"javascript\">");
                sb.append("setTimeout('show();',800);");
                sb.append("function show(){\r\n");
                sb.append("parent.window.location.reload();\r\n");
                sb.append("}\r\n");
                sb.append(" </script>");
                sb.append("</HTML>");
                output.print(sb.toString());
		    } else {
		    	output.print(html);
		    }
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (output != null) {
				output.close();
			}
		}
	}

	/**
	 * ftl  调整（ 表单打印）
	 * @param request
	 * @param modelMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/opencollengine")
	public String openCollectEngine(HttpServletRequest request, ModelMap modelMap) throws Exception {
		String pagepath = "";
		try {
		    String strAct = request.getParameter("Act");//类别
		    if (strAct.equals("openprint")) {//调用表单打印-----------------------
		       String AID  = request.getParameter("AID");//流程步骤id或表单配置ID
		       String DOCID = request.getParameter("DOCID");
		       String TYPE = request.getParameter("TYPE");//1 纵向打印 2 横向打印
		       if (TYPE==null){TYPE="1";}
		       String frameUrl = "/collect/opencollengine?Act=openprintbar&AID="+AID+"&DOCID="+DOCID;
		       modelMap.put("AID",AID);
		       modelMap.put("DOCID",DOCID);
		       modelMap.put("TYPE",TYPE);
		       modelMap.put("frameUrl",frameUrl);
		       
		       pagepath = "zrcollen/DoPrint";
		       return pagepath;
		    }
		    //----------------------------------------------------------------
		    else if (strAct.equals("openprintbar")) {//调用表单打印头部-----------------------
		      String AID = request.getParameter("AID");
		      String DOCID = request.getParameter("DOCID");
		      int CPAGE = 1;
		      CPAGE= collectService.getPrintTempletCount(AID);
		      String SLIST = "";
		      for (int i=1; i<CPAGE+1; i++) {
		          SLIST =  SLIST + "<option value="+String.valueOf(i)+">第"+String.valueOf(i)+"页</option>";
		      }
		      modelMap.put("AID",AID);
		      modelMap.put("DOCID",DOCID);
		      modelMap.put("CPAGE",String.valueOf(CPAGE));
		      modelMap.put("SLIST",SLIST);
		      
		      pagepath = "zrcollen/PrintBar";
		      return pagepath;
		    }
		    //-----------------------调用表单打印内容-----------------------//
		    else if (strAct.equals("openprintmain")) {
		      String TYPE = request.getParameter("TYPE");
		      if(TYPE==null||TYPE.equals("")){
		         TYPE = "1";
		      }
		      modelMap.put("TYPE",TYPE);
		      
		      pagepath = "zrcollen/PrintMain";
		      return pagepath;
		    } else {
		    }
		    //----------------------------------------------------------------
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return pagepath;
	}

	/**
	 * ftl  调整（手机调用）
	 * @param request
	 * @param modelMap
	 * @return
	 */
	@RequestMapping("/opencollphone")
	public String openCollectPhone(HttpServletRequest request, ModelMap modelMap) {
		String pagepath = "";
		try {
	       String strID = request.getParameter("ID");//配置ID
	       if (strID==null){strID = "";}
	       String strDOCID = request.getParameter("DOCID");//文档ID
	       if (strDOCID==null){strDOCID = "";}
	       String strREADONLY = request.getParameter("READONLY");//只读 1 是
	       if (strREADONLY==null){strREADONLY = "0";}
	       String getREURL = "";
	       String strREURL = request.getParameter("REURL");//保存后返回路径
	       if (strREURL==null){strREURL = "";}
	       getREURL = strREURL.replaceAll(",","&");

	       String strDOCNAME = collectService.getCollName(strID);
	       String strISDELETE = request.getParameter("ISDELETE");//是否删除
	       if (strISDELETE==null){strISDELETE = "";}
	       if (strISDELETE.equals("1")) {//执行删除操作
	    	   collectService.datadelete(strDOCID,strID);
	       }
	       modelMap.put("ID",strID);
	       modelMap.put("DOCID",strDOCID);
	       modelMap.put("READONLY",strREADONLY);
	       modelMap.put("DOCNAME",strDOCNAME);
	       modelMap.put("REURL",getREURL);
	       modelMap.put("ISDELETE",strISDELETE);
	       String frameUrl = "/collect/collenginephone?COLL_AID="+strID+"&coll_PKID="+strDOCID+"&coll_FKID="+strDOCID+"&coll_MODE=2&coll_READ="+strREADONLY;
	       modelMap.put("frameUrl",frameUrl);

	       pagepath = "ZrPhoneEngine/collenginephone";
	       return pagepath;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return pagepath;
	}

	@RequestMapping("/collenginephone")
	public void collenginephone(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.setContentType("text/html; charset=utf-8");
	    String html = "<p align=\"center\">非法调用，程序将自动返回上一界面...</p>";
	    Request req = new Request(request);
	    if (req!=null && req.getCount()>0) {
	      try {
	         HttpSession session = request.getSession(true);
	         SessionUser userInfo = (SessionUser)session.getAttribute("userinfo");
	         
	         html = collectPhoneService.collectionData(req, userInfo);
	       }
	       catch (Exception ex) {
	         response.getWriter().print("<p>表单引擎发生异常，请稍后再试！</p><p>详细错误信息:</p><p>" + ex.toString() + "</p>"); ;
	       }
	    }
	    //临时加,表单引擎没有调用到配置信息时刷新父窗口-------------
	    if (html.equals("表单引擎发生异常：<br>流程配置与表单配置错误，未找到相关配置信息。") || html.equals("表单引擎发生错误，表单配置信息未找到！")) {
            StringBuffer sb = new StringBuffer();
            sb.append("<HTML>");
            sb.append("<script language=\"javascript\">");
            sb.append("setTimeout('show();',800);");
            sb.append("function show(){\r\n");
            sb.append("parent.window.location.reload();\r\n");
            sb.append("}\r\n");
            sb.append(" </script>");
            sb.append("</HTML>");
            response.getWriter().print(sb.toString());
	    } else {
	        response.getWriter().print(html);
	    }
	}
}