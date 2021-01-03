package zr.zrpower.intercept;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;
import zr.zrpower.common.util.StringUtils;
import zr.zrpower.intercept.annotation.ZrSafety;
import zr.zrpower.model.SessionUser;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 页面拦截用户是否登录，如果已经登录则跳转到首页
 * @author lwk
 *
 */
@Aspect
@Component
public class ZrWebCheckAspect {
	private final String RootPath = "/";
	private final String LoginPath = "/index";
	private final String LoginPath1 = "/index1";
	private final String OPT_STATUS = "on";

	public String Err_Access = "{\"code\":\"-1\", \"msg\":\"您的请求被系统拦截，请登陆后重试.\", \"data\": null}";

	/**
	 * ZRSpringBoot的Controller类的切面->切入点
	 */
	@Pointcut("execution(public * zr.zrpower.controller..*..*(..))")
	public  void execMethodAspect() {
	}

	/**
	 * 在HTTP请求Controller类之前执行拦截
	 * @param joinPoint
	 * @throws Throwable
	 */
	@Around("execMethodAspect()")
	public Object callMethod(ProceedingJoinPoint joinPoint) throws Throwable {
		/**
		 * 获取request和response对象
		 */
		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        HttpServletResponse response = attributes.getResponse();
        
		request.setCharacterEncoding("UTF-8");// 设置HTTP请求编码为UTF-8
        response.setCharacterEncoding("UTF-8");// 设置HTTP响应编码为UTF-8
		HttpSession session = request.getSession(true);
		
		String strAutologin = request.getParameter("autologin");
		if (StringUtils.isBlank(strAutologin)) {
			strAutologin = "";
		}
		String strAction = request.getParameter("Act");
		if (StringUtils.isBlank(strAction)) {
			strAction = "1";
		}
		// 进入ZrpowerWeb系统接口层时验证用户请求
        Class<?> clazz = joinPoint.getTarget().getClass();
		if (null == clazz) {
			throw (new RuntimeException("System Error: not found Class."));
		}
		// 拿到类级别的注解
		ZrSafety zrSafety = clazz.getAnnotation(ZrSafety.class);
        if (zrSafety != null && OPT_STATUS.equals(zrSafety.value())) {
        	SessionUser userInfo = (SessionUser) session.getAttribute("userinfo");
        	if (userInfo == null) {// 登录超时或者已退出登录，则跳转到登录页面
        		String redirect = "";
    			if (strAction.equals("2")) {// 第二种主界面
    				redirect = "redirect:/index1";
    			} else {// 第一种主界面
    				redirect = "redirect:/index";
    			}
    			return redirect;
        	} else {// 用户已登录
        		return joinPoint.proceed();// 方法继续执行
        	}
        }
		String reqUrl = request.getServletPath();// 获取request请求路径
		if (RootPath.equals(reqUrl) || LoginPath.equals(reqUrl) || LoginPath1.equals(reqUrl)) {
	        ModelAndView view = new ModelAndView();
        	request.setAttribute("message", "");
			if (strAction.equals("2") || LoginPath1.equals(reqUrl)) {// 第二种主界面
				view.setViewName("index1");
			}
			else if (strAction.equals("1") || LoginPath.equals(reqUrl)) {// 第一种主界面
				view.setViewName("index");
			} else {// 其他情况
			}
			if (!strAutologin.equals("1")) {
				String txtlzm = request.getParameter("txtlzm");
				Object yzmObj = session.getAttribute("RANDOMVALIDATECODEKEY");
				String seslzm = "";
				if (yzmObj != null) {
					seslzm = yzmObj.toString();
				}
				if (StringUtils.isNotBlank(txtlzm) && StringUtils.isNotBlank(seslzm) && !seslzm.equals(txtlzm)) {
					request.setAttribute("message", "2");
				}
			}
			return view;
        }
		return joinPoint.proceed();// 方法继续执行
	}
}