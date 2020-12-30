package zr.zrpower.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import zr.zrpower.intercept.annotation.ZrSafety;
import zr.zrpower.model.SessionUser;
import zr.zrpower.service.SeManageService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 用户调动控制器
 * @author nfzr
 *
 */
@ZrSafety
@Controller
@RequestMapping("/zruser")
public class ZRunitController extends BaseController {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** 系统管理服务层. */
	@Autowired
	private SeManageService seManageService;

	/**
	 * 用户行为接口
	 * @param modelMap
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/actionuser")
	public String actionUser(ModelMap modelMap, HttpServletRequest request, HttpServletResponse response) {
		String pagepath = "";// 页面路径
		try {
		    response.setContentType("text/html; charset=UTF-8");
		    String strAct = request.getParameter("Act");
		    HttpSession session = request.getSession(true);
		    SessionUser user = (SessionUser) session.getAttribute("userinfo");
		    try {
		    	if (strAct != null) {
			        //加载用户的附件---
		    		if (strAct.equals("loadatt")) {
			            String strFLOWID = request.getParameter("FLOWID");  //得到流程标识ID或表名
			            String strDOCID  = request.getParameter("DOCID");   //得到文档ID
			            String strAID  = request.getParameter("AID");       //当前活动ID(不是流程类型时为空)
			            String strTYPE = request.getParameter("TYPE");      //得到显示类型(1 管理所有附件 2 管理个人上传的附件 3 浏览附件)
			            String strList ="";
			            if (strTYPE.equals("1")){
			               strList = seManageService.showSysAttFile(strFLOWID,strDOCID,"");//得到附件显示列表
			            }else {
			               strList = seManageService.showSysAttFile(strFLOWID,strDOCID,user.getUserID());//得到附件显示列表
			            }
			            String strShowList = seManageService.getSysFileNameList(strFLOWID,strDOCID);
			            int Attnum = seManageService.getAttNum(strFLOWID,strDOCID);
			            
			            modelMap.put("FLOWID",strFLOWID);
			            modelMap.put("DOCID",strDOCID);
			            modelMap.put("AID",strAID);
			            modelMap.put("TYPE",strTYPE);
			            modelMap.put("ATTNUM",String.valueOf(Attnum));
			            modelMap.put("LIST1",strList);
			            modelMap.put("LIST2",strShowList);
			            user.setUserFileID("");//清空用户的文件ID
			            
			            pagepath = "ZrWorkFlow/file";
			            return pagepath;
			        }
			        //删除用户的附件---
		    		else if (strAct.equals("deleteatt")) {
			          String strID = request.getParameter("ID");//得到附件ID
			          String strFLOWID  = request.getParameter("FLOWID"); //得到流程标识ID
			          String strDOCID  = request.getParameter("DOCID");   //得到文档ID
			          String strAID  = request.getParameter("AID");       //得到活动ID
			          String strTYPE  = request.getParameter("TYPE");     //类型
			          seManageService.sysAttFileDel(strID);
			          
			          return "redirect:/zruser/actionuser?Act=loadatt&FLOWID="+strFLOWID+"&DOCID="+strDOCID+"&AID="+strAID+"&TYPE="+strTYPE;
			        }
			        
		    	}
		    } catch (Exception ex) {
		    	ex.printStackTrace();
		    }
		} catch (Exception ex) {
			LOGGER.error("UserController.actionUser Exception:\n", ex);
		}
		return pagepath;
	}

	
//
//	/**
//	 * 用户意见操作接口
//	 * @param request
//	 * @param response
//	 * @throws IOException
//	 */
//	@RequestMapping("/actionuseridea")
//	public void actionUserIdea(HttpServletRequest request, HttpServletResponse response) {
//	   response.setContentType("text/html; charset=UTF-8");
//	   String strAction = request.getParameter("Act");
//	   try {
//		   if(strAction != null){
//		     if(strAction.equals("delete")){
//		       deleteUserIdea(request, response);
//		     }
//		     if(strAction.equals("add")){
//		       addUserIdea(request, response);
//		     }
//		   }
//	   } catch (Exception e) {
//			LOGGER.error("UserController.actionUserIdea Exception:\n", e);
//	   }
//	}
//
//	/**
//	 * 删除意见
//	 * @param request
//	 * @param response
//	 * @throws IOException
//	 */
//	private void deleteUserIdea(HttpServletRequest request, HttpServletResponse response) throws IOException {
//		  try {
//			  userIdeaService.delUserIdea(request.getParameter("ID"));
//		      this.Msg = "意见信息删除成功！";
//		      this.isOk = true;
//		  } catch (Exception ex) {
//			  ex.printStackTrace();
//		  }
//	}
//

}