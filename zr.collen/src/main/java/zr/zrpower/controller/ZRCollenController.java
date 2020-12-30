package zr.zrpower.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import zr.zrpower.collectionengine.Request;
import zr.zrpower.common.util.R;
import zr.zrpower.intercept.annotation.ZrSafety;
import zr.zrpower.model.SessionUser;
import zr.zrpower.service.CollectionService;
import zr.zrpower.service.ZRCollenHtmlUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * 表单引擎view控制
 * 
 * @author nfzr
 * 
 */
@ZrSafety
@RestController
@RequestMapping("/zrcollen")
public class ZRCollenController extends BaseController {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** 自定义表单引擎的核心服务. */
	@Autowired
	private CollectionService collectService;

	/**
	 * ftl  调整（表单生成html接口）
	 * 
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
			int iMode = 0; // 采集模式：0采集模版预览，1打印模版预览，2数据采集，3打印
			Request req = new Request(request);
			if (req != null && req.getCount() > 0) {
				String sMode = req.getStringItem("coll_mode");
				if (sMode != null && !sMode.equals("")) {
					iMode = Integer.parseInt(sMode);
				} else {
					req.appendItem("coll_mode", "0");
				}
				try {
					HttpSession session = request.getSession(true);
					SessionUser userInfo = (SessionUser) session.getAttribute("userinfo");
					switch (iMode) {
					case 0:
						html = collectService.collectionData(req, userInfo);//collectionPriview(req, userInfo);//表单预览
						break;
					case 1:
						html = collectService.printPriview(req, userInfo);//打印
						break;
					case 2:
						html = collectService.collectionData(req, userInfo);//读取数据
						break;
					case 3:
						html = collectService.printDocument(req);//打印界面
						break;
					}
				} catch (Exception ex) {
					ex.printStackTrace();
					output.print("<p>采集引擎发生异常，请稍后再试！</p><p>详细错误信息:</p><p>" + ex.toString() + "</p>");
				}
			}
			// 临时加,采集引擎没有调用到配置信息时刷新父窗口-------------
			if (html.equals("采集引擎发生异常：<br>流程配置与采集配置错误，未找到相关配置信息。") || html.equals("采集引擎发生错误，采集配置信息未找到！")) {
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
	 * ftl  调整（表单数据提交接口）
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("/saveorupdate")
	public R saveorupdate(@RequestParam Map<String, Object> params, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String id ="";
		try {
			HttpSession session = request.getSession(true);
			SessionUser userInfo = (SessionUser) session.getAttribute("userinfo");
			Request req = new Request(request);
			id =collectService.saveOrUpdate(req, userInfo);
			if("-1".equals(id)) {
				return R.error();
			}
		} catch (Exception ex) {
			return R.error();
		}
		return R.ok().put("id", id);
	}

	/**
	 * ftl 调整（显示表单view 视图接口）
	 * 
	 * @param request
	 * @param modelMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/form")
	public void form(HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.setContentType("text/html; charset=UTF-8");
		Map<String, Object> maps = null;
		String ID = request.getParameter("ID") != null ? request.getParameter("ID") : "";// 配置ID ==  COLL_AID
		String DOCID = request.getParameter("DOCID") != null ? request.getParameter("DOCID") : "";// 文档ID  == coll_PKID、coll_FKID
		String READONLY = request.getParameter("READONLY") != null ? request.getParameter("READONLY") : "0";// 只读 1 是  coll_READ=1
		String ATT = request.getParameter("ATT") != null ? request.getParameter("ATT") : "0";// 是否有附件 1 是
		String BUTT = request.getParameter("BUTT") != null ? request.getParameter("BUTT") : "0";// 是否显示按钮 1 是
		String TYPE = request.getParameter("TYPE") != null ? request.getParameter("TYPE") : "add";// add.edit,print,printbar,view  == coll_MODE
		if (ID.length() > 0) {
			maps = null;
			maps = new HashMap<String, Object>();
			//转换成表单调用模式
			if(TYPE.equals("add")) {
				request.setAttribute("coll_mode", "2");
				maps.put("coll_mode", 2);
			}else if(TYPE.equals("edit")) {
				request.setAttribute("coll_mode", "2");
				maps.put("coll_mode", 2);
			}else if(TYPE.equals("print")) {
				request.setAttribute("coll_mode", "1");
				maps.put("coll_mode", 1);
			}else if(TYPE.equals("view")) {
				request.setAttribute("coll_mode", "0");
				maps.put("coll_mode", 0);
			}else {
				request.setAttribute("coll_mode", "3");
				maps.put("coll_mode", 3);
			}
			//只读
			if (READONLY.equals("1")) {
				request.setAttribute("coll_READ", "1");
				maps.put("coll_READ", 1);
			}else {
				maps.put("coll_READ", "0");
			}
			maps.put("COLL_AID", ID);
			maps.put("coll_PKID", DOCID);
			maps.put("coll_FKID", DOCID);
			maps.put("BUTT", BUTT);
			maps.put("ATT", ATT);
			// 调用模板生成html
			String collenHtml = "";
			collenHtml = ZRCollenHtmlUtils.generatorCode(maps);
			response.getWriter().print(collenHtml);
		}
	}

	/**
	 * ftl  调整（删除表单数据）
	 * @param params
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("/delform")
	public R deleteForm(@RequestParam Map<String, Object> params) throws Exception {
		boolean fa = false;
		if(params.containsKey("docid")&&params.containsKey("formid")) {
			fa = collectService.datadelete(String.valueOf(params.get("docid")), String.valueOf(params.get("formid")));
		}
		return fa == true ? R.ok() : R.error("删除异常，提交空表单");
	}
}
