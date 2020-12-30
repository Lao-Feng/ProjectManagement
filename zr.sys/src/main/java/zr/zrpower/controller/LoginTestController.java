package zr.zrpower.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import zr.zrpower.common.util.FunctionMessage;
import zr.zrpower.model.SessionUser;
import zr.zrpower.service.LoginService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Enumeration;

/**
 * 网站演示功能控制器
 * @author lwk
 *
 */
@Controller
@RequestMapping("/test")
public class LoginTestController {
	/** The LoginTestController Logger */
	@SuppressWarnings("unused")
	private static final Logger LOGGER = LoggerFactory.getLogger(LoginTestController.class);
	@Autowired
	private LoginService loginService;

	/**
	 * iframe框架页面跳转到处理
	 * @param page 跳转的页面路径
	 * @param type 用户类型，1：设计员，2：管理员，3：李雪勇
	 * @param modelMap
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = { "/url" }) 
	public ModelAndView iframe(@RequestParam(value="page") String page, @RequestParam(name="type", defaultValue="1") Integer type, ModelMap modelMap, 
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		ModelAndView view = new ModelAndView();
		HttpSession session = request.getSession(true);
		
		SessionUser userInfo = (SessionUser) session.getAttribute("userinfo");
		if (userInfo == null) {// 登录超时或者已退出登录
			loginCk(type, request, response);//模拟用户自动登录
			userInfo = (SessionUser) session.getAttribute("userinfo");
		}
		if (page.indexOf(".html") != -1) {// 请求html页面
			Enumeration<String> enums = request.getParameterNames();
			while (enums.hasMoreElements()) {
				String paraName = (String) enums.nextElement();
				String paraVal = request.getParameter(paraName);
				request.setAttribute(paraName, paraVal);
			}
			
			int index = page.indexOf(".");// .html页面路径
			page = page.substring(0, index);
			String prefix = page.substring(0, 1);
			if (prefix.equals("/")) {
				page = page.substring(1);
			} else {// 不做任何处理
			}
			view.setViewName(page);
		} else {// 请求路径url，重定向
			page = page.replace(",", "&");//特殊字符处理
			view.setViewName("redirect:"+page);
		}
		return view;
	}

	/**
	 * 用户登录接口
	 * @param type 用户类型，1：设计员，2：管理员，3：李雪勇
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception 
	 */
	public void loginCk(Integer type, HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession(true);
		String loginID = "";//登录账户
        String password = "";//登录密码
		FunctionMessage fmLoginInfo = null;
		if (type == 1) {
    		loginID = "designer";
    		password = "designer";
    	} else if (type == 2) {
    		loginID = "admin";
    		password = "admin";
    	} else if (type == 3) {
    		loginID = "00001";
    		password = "00001";
    	} else {
    	}
		fmLoginInfo = loginService.login(loginID,password);
		if (fmLoginInfo != null) {
			if (fmLoginInfo.getResult()) {// 登录成功
				SessionUser user = (SessionUser) fmLoginInfo.getValue();
				session.setAttribute("userinfo", user);
			} else {
				request.setAttribute("message", "用户名或密码错误");
			}
		} else {
			session.setAttribute("errMsg", "调用中间件发生错误,未取得验证信息.");
			session.setAttribute("errPath", "RELOGIN");
			request.setAttribute("message", "调用中间件发生错误,未取得验证信息.");
		}
	}
}