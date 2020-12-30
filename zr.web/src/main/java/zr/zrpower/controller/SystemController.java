package zr.zrpower.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import zr.zrpower.common.util.StringUtils;
import zr.zrpower.common.util.SysPreperty;
import zr.zrpower.common.web.FileUpload;
import zr.zrpower.intercept.annotation.ZrSafety;
import zr.zrpower.model.SessionUser;
import zr.zrpower.service.SeManageService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Map;

/**
 * 系统服务控制器
 * @author lwk
 *
 */
@ZrSafety
@Controller
@RequestMapping("/system")
public class SystemController extends BaseController {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** 系统管理服务层. */
	@Autowired
	private SeManageService semanageService;

	@RequestMapping("/actionsysattmanage")
	public String actionSysAttManage(HttpServletRequest request, HttpServletResponse response) throws IOException {
	    HttpSession session = request.getSession(true);
	    SessionUser user = (SessionUser) session.getAttribute("userinfo");
	    try {
		    java.util.Date dt = new java.util.Date();
		    long lg = dt.getTime();
		    Long ld = new Long(lg);
		    String mRecordID = ld.toString();
		    
		    String strPath = SysPreperty.getProperty().UploadFilePath+"/AttManage";
		    String strFile_ID = user.getUserFileID();
		    String strUser_ID = user.getUserID();
		    
		    if (StringUtils.isBlank(strFile_ID)) {
		       strFile_ID = mRecordID;
		       //创建目录
		       semanageService.createFileFoler(strPath);
		       semanageService.createFileFoler(strPath+"/"+strUser_ID);
		       semanageService.createFileFoler(strPath+"/"+strUser_ID+"/"+strFile_ID);
		       //重设用户的文件ID
		       user.setUserFileID(strFile_ID);
		    }
		    strPath = strPath+"/"+strUser_ID+"/"+strFile_ID;
		    //上传附件
		    FileUpload fileUpload = new FileUpload();
		    fileUpload.setSize(15 * 1024 * 1024); //上传的大小限制为15MB
		    fileUpload.setObjectPath(strPath);    //上传路径
		    fileUpload.uploadFile(request);//上传
		    
		    Map<String, Object> htFields = fileUpload.getRtFields();
		    String fileName = fileUpload.getTEMPLAET();
		    System.out.println("上传文件名称："+fileName);
		    String strFLOWID = (String) htFields.get("FLOWID");
		    String strDOCID = (String) htFields.get("DOCID");
		    String strAID = (String) htFields.get("AID");
		    String strTYPE = (String) htFields.get("TYPE");
		    String strpTitle = (String) htFields.get("pTitle");
		    
		    int t1 = strpTitle.lastIndexOf("\\");
		    if(t1 >= 0 &&  t1 < strpTitle.length()){
		    	strpTitle = strpTitle.substring(t1+1,strpTitle.length());
		    }
		    //插入附件记录
		    semanageService.sysAttFileAdd(strFLOWID, strDOCID, strAID, fileName, strUser_ID, strFile_ID, strpTitle);
		    
		    return "redirect:/zruser/actionuser?Act=loadatt&FLOWID="+strFLOWID+"&DOCID="+strDOCID+"&AID="+strAID+"&TYPE="+strTYPE+"&parentload=1";
	    } catch (Exception ex){
	        ex.printStackTrace();
	        //this.Msg = "连接中间件服务器失败，请稍后再试！";
	    }
	    response.getWriter().write("{\"data\":\"success\"}");
		response.getWriter().flush();
		return null;
	}

}