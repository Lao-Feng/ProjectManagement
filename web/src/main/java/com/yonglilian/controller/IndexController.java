package com.yonglilian.controller;

import com.yonglilian.common.util.*;
import com.yonglilian.model.SessionUser;
import com.yonglilian.service.CollectionService;
import com.yonglilian.service.LoginService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.Enumeration;
import java.util.Map;

/**
 * 网站首页控制器
 * 
 * @author nfzr
 *
 */
@RestController
@RequestMapping("/")
public class IndexController {
	/** The IndexController Logger */
	private static final Logger LOGGER = LoggerFactory.getLogger(IndexController.class);

	@Autowired
	private LoginService loginService;
	@Autowired
	private CollectionService collectionService;

	/**
	 * "/index"或者"/"跳转到登录页面[主题一]
	 * 
	 * @param modelMap
	 * @return
	 */
	@RequestMapping(value = { "/index", "/" }, method = { RequestMethod.GET })
	public ModelAndView index(ModelMap modelMap) {
		modelMap.put("message", "");
		ModelAndView view = new ModelAndView();
		view.setViewName("index");
		return view;
	}

	/**
	 * "/index1"跳转到登录页面[主题二]
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = { "/index1" }, method = { RequestMethod.GET })
	public ModelAndView index1(ModelMap modelMap) {
		modelMap.put("message", "");
		ModelAndView view = new ModelAndView();
		view.setViewName("index1");
		return view;
	}

	/**
	 * iframe框架页面跳转到处理
	 * 
	 * @param page
	 *            跳转的页面路径
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = { "/iframe" })
	public ModelAndView iframe(@RequestParam(value = "page") String page, ModelMap modelMap, HttpServletRequest request,
			HttpServletResponse response) {

		ModelAndView view = new ModelAndView();
		if (page.indexOf(".html") != -1) {// 请求html页面
			Enumeration<String> enums = request.getParameterNames();
			while (enums.hasMoreElements()) {
				String paraName = (String) enums.nextElement();
				String paraVal = request.getParameter(paraName);
				modelMap.put(paraName, paraVal);
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
			view.setViewName("redirect:" + page);
		}
		return view;
	}

	/**
	 * 登录退出等相关接口
	 * 
	 * @param request
	 * @param response
	 * @return String
	 */
	@RequestMapping("/checklogin")
	public R checkLogin(@RequestParam Map<String, Object> params, HttpServletRequest request,
						HttpServletResponse response) {
		try {
			response.setContentType("text/html; charset=UTF-8");
			if (params.containsKey("Act")) {
				if (String.valueOf(params.get("Act")).equals("loginCk")) {
					// 登录
					return loginCk(params, request);
				} else {
					// 登出
					Subject currentUser = SecurityUtils.getSubject();
					currentUser.logout();
					request.getSession().invalidate();
					return R.ok();
				}
			} else {
				return R.error("请求参数有误！");
			}
		} catch (Exception ex) {
			LOGGER.error("IndexController.checkLogin Exception:\n", ex);
		}
		return R.ok();
	}

	/**
	 * 获取图形验证码
	 * 
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	@RequestMapping(value = { "/getImage" }, method = { RequestMethod.GET })
	public void getImage(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("image/jpeg");// 设置相应类型,告诉浏览器输出的内容为图片
		response.setHeader("Pragma", "No-cache");// 设置响应头信息，告诉浏览器不要缓存此内容
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expire", 0);
		RandomValidateCode randomValidateCode = new RandomValidateCode();
		try {
			randomValidateCode.getRandcode(request, response);// 输出图片方法
		} catch (Exception e) {
			LOGGER.error("IndexController.getImage() Exception:\n", e);
		}
	}

	/**
	 * 文件下载方法
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping("/serverfile")
	public void getServerfile(HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.setCharacterEncoding("UTF-8");
		int type = 0;// 下载文件类型
		if (request.getParameter("type") != null) {
			type = Integer.parseInt(request.getParameter("type"));
		}
		String nameval = request.getParameter("name");
		String fileName = request.getParameter("file");
		if (StringUtils.isBlank(fileName)) {
			return;
		}
		String name = fileName.substring(fileName.indexOf("/") + 1);
		String ext = fileName.substring(fileName.indexOf("."));
		String path = SysPreperty.getProperty().UploadFilePath + "/";
		switch (type) {
		case 0:
			path = SysPreperty.getProperty().UploadFilePath + "/";
			break;
		case 1:
			path = SysPreperty.getProperty().UploadImagePath + "/";
			break;
		case 2:
			path = SysPreperty.getProperty().IcoPath + "/";
			break;
		case 4:
			path = SysPreperty.getProperty().ShowTempletPath + "/";
			break;
		case 5:
			path = SysPreperty.getProperty().PrintTempletPath + "/";
			break;
		case 6:
			path = SysPreperty.getProperty().UploadFilePath + "/AttManage/";
			name = nameval;
			break;
		case 7:
			path = SysPreperty.getProperty().LogFilePath;
			path = path.substring(0, path.length() - 3) + "StatExcel/";
			break;
		}
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		try {
			// 设置文件输出类型，根据文件扩展名
			if (ext.equals(".gif")) {
				response.setContentType("image/gif");
			} else if (ext.equals(".jpeg")) {
				response.setContentType("image/jpeg");
			} else if (ext.equals(".jpg")) {
				response.setContentType("image/jpeg");
			} else if (ext.equals(".png")) {
				response.setContentType("image/png");
			} else {// 其他文件类型
				// 设置文件输出类型，文件扩展名为.*[二进制流，不知道下载文件类型]
				response.setContentType("application/octet-stream");
			}
			// 有的web服务器可能用下面的代码才正常
			response.addHeader("Content-Disposition",
					"attachment; filename=\"" + new String(name.getBytes("UTF-8"), "ISO8859-1"));
			// 获取文件的长度
			long fileLength = new File(path + fileName).length();
			// 设置输出长度
			response.setHeader("Content-Length", String.valueOf(fileLength));
			// 获取输入流
			bis = new BufferedInputStream(new FileInputStream(path + fileName));
			// 获取输出流
			bos = new BufferedOutputStream(response.getOutputStream());
			byte[] buff = new byte[1024];
			int bytesRead;
			while ((bytesRead = bis.read(buff, 0, buff.length)) != -1) {
				bos.write(buff, 0, bytesRead);
			}
		} catch (Exception e) {
			LOGGER.error("IndexController.getServerfile() Exception:\n", e);
		} finally {// 关闭流
			if (bis != null) {
				bis.close();
			}
			if (bos != null) {
				bos.close();
			}
		}
	}

	/**
	 * 用户登录接口
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public R loginCk(Map<String, Object> params, HttpServletRequest request) throws Exception {
		HttpSession session = request.getSession(true);
		FunctionMessage fmLoginInfo = null;
		Object yzmObj = session.getAttribute("RANDOMVALIDATECODEKEY");
		if (String.valueOf(params.get("captcha")).equals(yzmObj.toString())) {
			if (params.containsKey("LOGINID") && params.containsKey("LOGINPW")) {
				// 获得当前Subject
				Subject currentUser = SecurityUtils.getSubject();
				// 验证用户是否验证，即是否登录
				if (!currentUser.isAuthenticated()) {
					// 把用户名和密码封装为 UsernamePasswordToken 对象
					String username = (String) params.get("LOGINID");
					String userpawword = (String) params.get("LOGINPW");
					UsernamePasswordToken token = new UsernamePasswordToken(username, userpawword);
					// remembermMe记住密码
					token.setRememberMe(true);
					try {
						// 执行登录.
						currentUser.login(token);
						fmLoginInfo = loginService.login(String.valueOf(params.get("LOGINID")),String.valueOf(params.get("LOGINPW")));
						if (fmLoginInfo != null) {
							if (fmLoginInfo.getResult()) {// 登录成功
								SessionUser user = (SessionUser) fmLoginInfo.getValue();
								session.setAttribute("userinfo", user);
								//调用角色授权
								currentUser.hasRole(user.getUserID());
								return R.ok().put("userInfo", fmLoginInfo);
							} else {
								return R.error(500, "用户名或密码错误");
							}
						} else {
							return R.error(500, "调用中间件发生错误,未取得验证信息.");
						}
					} catch (IncorrectCredentialsException e) {
						System.out.println("shiro验收错误："+e);
						return R.error("账户验证失败");
					} catch (ExcessiveAttemptsException e) {
						return R.error("登录失败次数过多!");
					} catch (LockedAccountException e) {
						return R.error("帐号已被锁定!");
					} catch (DisabledAccountException e) {
						return R.error("帐号已被禁用!");
					} catch (ExpiredCredentialsException e) {
						return R.error("账户已过期!");
					} catch (UnknownAccountException e) {
						return R.error("账户验证失败");
					} catch (UnauthorizedException e) {
						return R.error("您没有得到相应的授权！");
					} catch (Exception e) {
						return R.error("账户验证出错");
					}
				}else {
					fmLoginInfo = loginService.login(String.valueOf(params.get("LOGINID")),String.valueOf(params.get("LOGINPW")));
					if (fmLoginInfo != null) {
						if (fmLoginInfo.getResult()) {// 登录成功
							SessionUser user = (SessionUser) fmLoginInfo.getValue();
							session.setAttribute("userinfo", user);
							//调用角色授权
							currentUser.hasRole(user.getUserID());
							return R.ok().put("userInfo", fmLoginInfo);
						} else {
							return R.error(500, "用户名或密码错误");
						}
					} else {
						return R.error(500, "调用中间件发生错误,未取得验证信息.");
					}
				}
			}else {
				return R.error(500, "用户名和密码必须输入");
			}
		} else {
			return R.error(500, "验证码输入错误");
		}
	}
	


	/**
	 * 文件上传接口，可以上传一个或多个文件
	 * 
	 * @param request
	 * @throws IOException
	 */
	@RequestMapping(value = { "/uploadFile" }, method = { RequestMethod.POST })
	@ResponseBody
	public String uploadFile(HttpServletRequest request) throws IOException {
		String result = "false";// true：上传文件成功，false：上传文件失败
		// 上传文件的保存路径，可以根据业务需求自行更改
		String rootPath = "D:/JavaPlatform/workspace/ZRSpringBoot/Files";
		File fileDir = new File(rootPath);
		if (!fileDir.exists()) {// 如果文件夹不存在，则创建
			fileDir.mkdirs();
		}
		FileOutputStream fileOutput = null;
		FileInputStream fileInput = null;
		try {
			// 转换request，解析出request中的文件
			MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
			MultipartFile multipartFile = multipartRequest.getFile("file");
			// 获取文件名全称，包含文件后缀名
			String fileName = multipartFile.getOriginalFilename();
			fileOutput = new FileOutputStream(new File(rootPath + "/" + fileName));
			// multiReq.getFile("file")中"file"为上传文件的参数名称，切记！！！
			fileInput = (FileInputStream) multipartFile.getInputStream();

			byte[] buffer = new byte[1024];
			int length = 0;
			while ((length = fileInput.read(buffer)) != -1) {
				fileOutput.write(buffer, 0, length);
			}
			result = "true";
		} catch (Exception ex) {
			ex.printStackTrace();
			result = "false";
		} finally {
			if (fileInput != null) {
				fileInput.close();
			}
			if (fileOutput != null) {
				fileOutput.close();
			}
		}
		return result;
	}
	/**
	 * 图片引擎接口
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping("/photoengine")
	public String dophotoEngine(HttpServletRequest request, HttpServletResponse response) throws IOException {
	    String retpage = "";
		try {
	      String mId = request.getParameter("coll_id");
	      String mPkid = request.getParameter("coll_pkid");
	      String mImg = request.getParameter("coll_img");

	      byte[] mImage = collectionService.getCollImage(mId,mPkid,mImg);
	      if (mImage != null) {
	        response.reset();
	        response.setContentType("image/jpeg");
	        ServletOutputStream output = response.getOutputStream();
	        output.write(mImage);
	        output.close();
	      } else {
	    	  retpage = "redirect:/static/Zrsysmanage/images/NoImage.gif";
	      }
	    } catch (Exception ex) {
	    	retpage = "redirect:/static/Zrsysmanage/images/NoImage.gif";
	    }
		return retpage;
	}
}