package zr.zrpower.web.bean;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

//import zr.zrpower.service.UserService;

@Controller
@RequestMapping("/manageBean")
public class ManageBean {
	/** 用户操作服务层. */
//	@Autowired
//	private UserService userService;
//
//	@RequestMapping("/getCheckPw")
//	@ResponseBody
//	public String getCheckPw(String strUserID, String strPw) {
//		String retvaule = "0";
//		boolean result = false;
//		try {
//			result = userService.checkPw(strUserID, strPw);
//			if (result) {
//				retvaule = "1";
//			} else {
//				retvaule = "0";
//			}
//		} catch (Exception ex) {
//			ex.printStackTrace();
//		}
//		return retvaule;
//	}

}