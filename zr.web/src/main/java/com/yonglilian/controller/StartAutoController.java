package com.yonglilian.controller;

import com.yonglilian.ejb.sys.AutoServer;
import com.yonglilian.service.UIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import zr.zrpower.common.util.SysPreperty;
import zr.zrpower.controller.BaseController;
import zr.zrpower.intercept.annotation.ZrSafety;
import zr.zrpower.model.SessionUser;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 自动开启服务控制器
 * @author lwk
 *
 */
@ZrSafety
@Controller
@RequestMapping("/")
public class StartAutoController extends BaseController {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static boolean isRun = false;
	/** 界面公共函数库服务层. */
	@Autowired
	private UIService uiService;
	
	static {// 静态代码块，先于构造方法执行
	    int intnum=0;
	    boolean isRun1 = true;
	    while(!isRun){
	      try {
	        isRun = true;
	        AutoServer server =  new AutoServer();
	        isRun1 = server.StartScan();
	        if (!isRun1){isRun=false;}
	      }
	      catch (Exception ex) {
	        ex.printStackTrace();
	      }
	      intnum ++;
	      if (intnum>30) {
	    	  break;
	      }
	    }
	}

	/**
	 * 自动开启服务接口
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("/startautoserver")
	public void startAutoServer(HttpServletRequest request, HttpServletResponse response) throws Exception {
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
	    sb.append(uiService.showHeadHtml("启动系统自动服务","IcoTitle.gif",user.getUserImage()));
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
	      sb.append("message[0]='已经启动系统自动服务!';");
	      sb.append("message[1]='服务正在运行中...';");
	    }else{
	      sb.append("message[0]='不能启动系统自动服务!';");
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
}