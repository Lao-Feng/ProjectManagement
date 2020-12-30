package zr.zrpower.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import zr.zrpower.common.util.R;
import zr.zrpower.model.SessionUser;
import zr.zrpower.queryengine.JxExcel;
import zr.zrpower.queryengine.Request;
import zr.zrpower.queryengine.mode.QUERY_CONFIG_TABLE;
import zr.zrpower.service.QueryControlService;
import zr.zrpower.service.ZRQueryHtmlUtils;
import zr.zrpower.service.impl.QueryControlImpl;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 查询引擎view控制
 * @author nfzr
 *
 */
@RestController
@RequestMapping("/zrquery")
public class ZRQueryController extends BaseController {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** 查询控制管理服务层. */
	@Autowired
	private QueryControlService queryControlService;

	/**
	 * ftl=调整（PC页面，获取查询数据）
	 * 
	 * @param params
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("/rows")
	public R rows(@RequestParam Map<String, Object> params, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		
		List<Map<String, Object>> strResult = new ArrayList<>();
		String TITLENAME = request.getParameter("TITLENAME");
		if (TITLENAME == null) {
			TITLENAME = "";
		}
		String STB = request.getParameter("STB");
		if (STB == null) {
			STB = "0";
		}

		String ISCODE = request.getParameter("ISCODE");
		if (ISCODE == null) {
			ISCODE = "1";
		}
		if (ISCODE.length() == 0) {
			ISCODE = "1";
		}

		if (TITLENAME.length() > 0 && ISCODE.equals("1")) {
			TITLENAME = new String(TITLENAME.getBytes("iso-8859-1"), "UTF-8");
		}
		try {
			HttpSession session = request.getSession(true);
			SessionUser user = (SessionUser) session.getAttribute("userinfo");
			Request rq = null;
			try {
				rq = new Request();
				rq.fullItem(request);
			} catch (Exception ex) {
			}

			strResult = getQueryRows(user, request, rq, TITLENAME);
		} catch (Exception ex) {
			response.getWriter().print("<p>发生异常！</p><p>详细错误信息:</p><p>" + ex.toString() + "</p>");
		}
		return R.ok().put("rows", strResult);
	}

	/**
	 * ftl 调整（导出表格数据excl）
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("/openquerycfg")
	public void openquerycfg(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession(true);
		SessionUser user = (SessionUser) session.getAttribute("userinfo");
		Date date = new Date();
		DateFormat fmt = new SimpleDateFormat("yyyyMMdd");
		String StrID = request.getParameter("ID");
		String Strpage = request.getParameter("page");
		if (Strpage == null) {
			Strpage = "1";
		}
		String Strcrow = request.getParameter("crow");
		if (Strcrow == null) {
			Strcrow = "0";
		}
		String Strexp = request.getParameter("exp");
		if (Strexp == null) {
			Strexp = "0";
		}

		String P1 = request.getParameter("P1");
		String P2 = request.getParameter("P2");
		String P3 = request.getParameter("P3");
		String P4 = request.getParameter("P4");
		String P5 = request.getParameter("P5");
		int k = 0;

		QueryControlImpl queryControlImpl = (QueryControlImpl) (session.getAttribute("queryControl"));
		String fileName = "", fileName1 = "", fileurl = "";
		fileName = queryControlService.getQueryTitle(StrID) + fmt.format(date);
		if (Integer.parseInt(Strcrow) > 5000 && Strexp.equals("0")) {// 分多文件导出
			fileurl = "<br>&nbsp;提示：导出的记录数大于5000条时将分成多个文件导出。<br><br>";
			for (int i = 0; i <= Integer.parseInt(Strcrow); i = i + 5000) {
				k++;
				fileName1 = fileName + "(" + String.valueOf(k) + ")" + ".XLS";

				fileurl = fileurl + "&nbsp;<a href=\"/zrquery/openquerycfg?Act=excel&exp=1&crow=" + Strcrow + "&page="
						+ String.valueOf(k) + "&ID=" + StrID + "&P1=" + P1 + "&P2=" + P2 + "&P3=" + P3 + "&P4=" + P4
						+ "&P5=" + P5 + "  target=\"_blank\">" + fileName1 + "</a><br>\r\n";
			}
			response.getWriter().print(fileurl);
		}
		if (Integer.parseInt(Strcrow) < 5000 && Strexp.equals("0")) {
			fileurl = "<br>&nbsp;提示：导出的记录数大于5000条时将分成多个文件导出。<br><br>";
			fileName1 = fileName + ".XLS";
			fileurl = fileurl + "&nbsp;<a href=\"/zrquery/openquerycfg?Act=excel&exp=1&crow=" + Strcrow + "&page=1&ID="
					+ StrID + "&P1=" + P1 + "&P2=" + P2 + "&P3=" + P3 + "&P4=" + P4 + "&P5=" + P5
					+ "  target=\"_blank\">" + fileName1 + "</a><br>\r\n";
			response.getWriter().print(fileurl);
		}

		try {
			if (Integer.parseInt(Strcrow) > 5000) {
				fileName = queryControlService.getQueryTitle(StrID) + fmt.format(date) + "(" + Strpage + ")" + ".XLS";
			} else {
				fileName = queryControlService.getQueryTitle(StrID) + fmt.format(date) + ".XLS";
			}
			JxExcel jxExcel = new JxExcel();
			response.reset();
			response.setContentType("application/vnd.ms-excel;charset=UTF-8");
			response.addHeader("Content-Disposition",
					"attachment;filename=" + new String(fileName.getBytes("GBK"), "iso8859-1"));
			ServletOutputStream os = response.getOutputStream();
			jxExcel.WriteExcel(queryControlImpl, os, StrID, user, P1, P2, P3, P4, P5, Strpage, Strcrow);
			os.flush();
			os.close();
			os = null;
			response.flushBuffer();
		} catch (Exception ex) {
			out.print("导出Excel文件出现异常" + ex.getMessage());
		}
	}

	/**
	 * ftl 调整（查询PC页面)
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping("/page")
	public void queryPage(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.setContentType("text/html; charset=UTF-8");
		String TITLENAME = request.getParameter("TITLENAME");
		if (TITLENAME == null) {
			TITLENAME = "";
		}
		String STB = request.getParameter("STB");
		if (STB == null) {
			STB = "0";
		}
		String ISCODE = request.getParameter("ISCODE");
		if (ISCODE == null) {
			ISCODE = "1";
		}
		if (ISCODE.length() == 0) {
			ISCODE = "1";
		}
		if (TITLENAME.length() > 0 && ISCODE.equals("1")) {
			TITLENAME = new String(TITLENAME.getBytes("iso-8859-1"), "UTF-8");
		}
		try {
			HttpSession session = request.getSession(true);
			SessionUser user = (SessionUser) session.getAttribute("userinfo");
			Request rq = null;
			try {
				rq = new Request();
				rq.fullItem(request);
			} catch (Exception ex) {
			}
			String ID = request.getParameter("ID");
			if (ID == null) {
				ID = "";
			}

			String strResult = "";
			strResult = getTemplateStr(user, request, rq, TITLENAME);
			response.getWriter().print(strResult);
		} catch (Exception ex) {
			response.getWriter().print("<p>发生异常！</p><p>详细错误信息:</p><p>" + ex.toString() + "</p>");
		}
	}

	
	/***
	 * ftl 调整，生成PC端查询调用html
	 * 
	 * @param user
	 * @param _request
	 * @param rq
	 * @param TITLENAME
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private String getTemplateStr(SessionUser user, HttpServletRequest _request, Request rq, String TITLENAME) {
		Map<String, Object> temps = new HashMap<>();
		String queryHtml = "";

		String ID = _request.getParameter("ID");
		if (ID == null) {
			ID = "";
		}
		String SHOWTITLE = _request.getParameter("SHOWTITLE");
		if (SHOWTITLE == null) {
			SHOWTITLE = "";
		}
		String STB = _request.getParameter("STB");
		if (STB == null) {
			STB = "0";
		}
		String ALIGN = _request.getParameter("ALIGN");
		if (ALIGN == null) {
			ALIGN = "center";
		}
		String XH = _request.getParameter("XH");
		if (XH == null) {
			XH = "1";
		}

		String ISCODE = _request.getParameter("ISCODE");
		if (ISCODE == null) {
			ISCODE = "1";
		}
		if (ISCODE.length() == 0) {
			ISCODE = "1";
		}

		String ISNULL = _request.getParameter("ISNULL");
		if (ISNULL == null) {
			ISNULL = "1";
		}

		String BUTTONIDS = _request.getParameter("BUTTONIDS");
		if (BUTTONIDS == null) {
			BUTTONIDS = "";
		}

		String ifFirstData = _request.getParameter("ifFirstData");
		String strWhere = _request.getParameter("strWhere");
		if (strWhere == null) {
			strWhere = "";
		}

		String queryFlag = _request.getParameter("queryFlag");
		if (queryFlag == null) {
			queryFlag = "0";
		}

		String swidth = _request.getParameter("SWIDTH");
		if (swidth == null) {
			swidth = "0";
		}

		String sheight = _request.getParameter("SHEIGHT");
		if (sheight == null) {
			sheight = "0";
		}

		int currentPage = 1;
		try {
			currentPage = Integer.parseInt(_request.getParameter("page"));
		} catch (Exception ex) {
			currentPage = 1;
		}
		try {
			QUERY_CONFIG_TABLE queryConfigEntity = queryControlService.getConfigTable(ID);
			String queryHidDis = _request.getParameter("queryDisHid");

			if (queryHidDis != null) {
				if (queryHidDis.trim().length() > 0) {
					queryHidDis = "none";
				} else {
					queryHidDis = "";
				}
			} else {
				if (queryConfigEntity.getTYPE().equals("2")) {
					queryHidDis = "none";
				} else {
					queryHidDis = "";
				}
			}
			// 1 一般查询 2 操作查询=3 可嵌入式列表  4 查询录入项和结果分离
			String queryType = queryConfigEntity.getTYPE();
			// 1首页加载数据  0首页不加载数据
			ifFirstData = queryConfigEntity.getIFFIRSTDATA();
			Map<String, Object> hidden = new HashMap<>();
			hidden.put("ID", ID);
			hidden.put("SHOWTITLE", SHOWTITLE);
			hidden.put("TITLENAME", TITLENAME);
			hidden.put("STB", STB);
			hidden.put("ALIGN", ALIGN);
			hidden.put("XH", XH);
			hidden.put("ISCODE", ISCODE);
			hidden.put("BUTTONIDS", BUTTONIDS);
			hidden.put("ISNULL", ISNULL);
			hidden.put("queryType", Integer.valueOf(queryType));//查询类型
			// 页面传递的参数
			String P1, P2, P3, P4, P5;
			P1 = _request.getParameter("P1") != null ? _request.getParameter("P1") : "";
			P2 = _request.getParameter("P2") != null ? _request.getParameter("P2") : "";
			P3 = _request.getParameter("P3") != null ? _request.getParameter("P3") : "";
			P4 = _request.getParameter("P4") != null ? _request.getParameter("P4") : "";
			P5 = _request.getParameter("P5") != null ? _request.getParameter("P5") : "";
			hidden.put("P1", P1);
			hidden.put("P2", P2);
			hidden.put("P3", P3);
			hidden.put("P4", P4);
			hidden.put("P5", P5);

			String getQTABLETYPE = "";
			if (queryConfigEntity.getQTABLETYPE() == null) {
				getQTABLETYPE = "1";
			} else {
				getQTABLETYPE = queryConfigEntity.getQTABLETYPE();
			}
			if (getQTABLETYPE.equals("2")) {
				hidden.put("selectrow", "0");
			} else {
				hidden.put("selectrow", "x");
			}
			if (!queryConfigEntity.getTYPE().equals("4") && !SHOWTITLE.equals("0")) {
				if (TITLENAME.length() == 0) {
					TITLENAME = queryConfigEntity.getNAME();
					hidden.put("TITLENAME", TITLENAME);
				}
			}
			//1、一般查询 2、操作查询 4、分离结果页，显示条件
			List<Map<String, Object>> queryItemList = new ArrayList<>();
			List<String> tablelist = new ArrayList<>();
			if(queryType.equals("1")||queryType.equals("2")||queryType.equals("4")) {
				if(queryType.equals("2")) {
					hidden.put("show_inpt", false);
				}else {
					hidden.put("show_inpt", true);
				}
				queryItemList = queryControlService.createQueryItem(ID,queryConfigEntity.getQITEMTYPE(), user, rq);
				if (queryItemList != null && queryItemList.size() > 0) {
					tablelist = (List<String>) queryItemList.get(queryItemList.size() - 1).get("table");
					temps.put("tables", tablelist);
					queryItemList.remove(queryItemList.size() - 1);
				}
				temps.put("items", queryItemList);
			}else {
				hidden.put("show_inpt", false);
				temps.put("tables", tablelist);
				temps.put("items", queryItemList);
			}
			//1、一般查询 2、操作查询  4、分离结果页面 显示按钮组
			List<Map<String, Object>> queryButtonList = new ArrayList<Map<String, Object>>();
			List<String> queryClikeList = new ArrayList<String>();
			Map<String, Object> butt = null;
			if(queryType.equals("1")||queryType.equals("2")||queryType.equals("4")) {
				// 查询按钮
				if(!queryType.equals("4")) {
					butt = null;
					butt = new HashMap<String, Object>();
					butt.put("name", "查 询");
					butt.put("icon", "el-icon-search");
					butt.put("click", "ZRqueryData");
					queryButtonList.add(butt);
				}
				// 自定义的按钮
				List<Map<String, Object>> zdybuttlist = queryControlService.createButtonNavigate(ID, "1", user,BUTTONIDS);
				if (zdybuttlist != null && zdybuttlist.size() > 0) {
					int len = zdybuttlist.size();
					List<String> zdylist = (List<String>) zdybuttlist.get(len - 1).get("buttlist");
					// 组装按钮js
					queryClikeList.addAll(zdylist);
					// 删除最后定义的按钮方法
					zdybuttlist.remove(len - 1);
					// 在合并
					queryButtonList.addAll(zdybuttlist);
				}
				// 公共按钮组
				if (queryConfigEntity.getIFCOMBTN().equals("1")) {
					butt = null;
					butt = new HashMap<String, Object>();
					butt.put("name", "导出Excel");
					butt.put("icon", "el-icon-document-delete");
					butt.put("click", "ZRqueryExcl");
					queryButtonList.add(butt);
					String excl = "ZRexpexcel('zrquery/openquerycfg?Act=excel&ID=" + ID + "&P1=" + P1 + "&P2=" + P2
							+ "&P3=" + P3 + "&P4=" + P4 + "&P5=" + P5 + "');";
					queryClikeList.add("ZRqueryExcl(){ " + excl + "},");
					
					if(!queryType.equals("4")) {
						butt = null;
						butt = new HashMap<String, Object>();
						butt.put("name", "显示/隐藏");
						butt.put("icon", "el-icon-s-operation");
						butt.put("click", "ZRqtopIshsow");
						queryButtonList.add(butt);
					}
				}
				// 加载按钮及按钮js
				hidden.put("show_butt", true);
				temps.put("butts", queryButtonList);
				temps.put("clicks", queryClikeList);
			}else {
				hidden.put("show_butt", false);
				temps.put("butts", queryButtonList);
				temps.put("clicks", queryClikeList);
			}
			//1、一般  2、操作  3、嵌入式  4、分离结果页面 显示表格
			List<Map<String, Object>> resultList = new ArrayList<>();
			Map<String, Object> map = new HashMap<>();
			if (queryType.equals("1")||queryType.equals("2")||queryType.equals("3")||queryType.equals("4")) {
				hidden.put("show_rows", true);
				resultList = queryControlService.getQueryResultTable(ID, queryConfigEntity.getQTABLETYPE(), queryType,
						user, rq, P1, P2, P3, P4, P5, ifFirstData, currentPage, swidth, sheight, XH);
				if (resultList != null && resultList.size() > 0) {
					// 0、装载数据表格单双选属性
					map = resultList.get(0);
					temps.put("rtable", map);
					// 1、装载数据表格显示属性
					resultList.remove(0);
					temps.put("rfields", resultList);
				}
			}else {
				hidden.put("show_rows", false);
				temps.put("rtable", map);
				temps.put("rfields", resultList);
			}
			//1、一般  2、操作  3、嵌入式  是否首页加载数据
			if (!queryType.equals("4")&&ifFirstData.equals("1")) {
				hidden.put("load_rows", true);
			}else {
				hidden.put("load_rows", false);
			}
	
			// ===============显示表格数据=======================//
			
			if (!(queryType.equals("5") && queryFlag.equals("0"))) { // 若是5，则在没有查询前是不需要显示的
				hidden.put("how_rows", 1);
				
			} else {
				hidden.put("how_rows", 0);
			}
			
			// 设置表格单击按钮
			String butstr = queryControlService.createButtonScript(queryControlService.getButtonByID(queryConfigEntity.getCID()), ID);
        	if(butstr.trim().length()>0) {
        		temps.put("onclick", "this.ZRbutts_"+Integer.valueOf(queryConfigEntity.getCID())+"()");
        	}else {
        		temps.put("onclick", "");
        	}

			// 设置表格双击按钮
			butstr = queryControlService.createButtonScript(queryControlService.getButtonByID(queryConfigEntity.getBID()), ID);
        	if(butstr.trim().length()>0) {
        		temps.put("ondblclick", "this.ZRbutts_"+Integer.valueOf(queryConfigEntity.getBID())+"()");
        	}else {
        		temps.put("ondblclick", "");
        	}

			// 装载初始化参数
			temps.put("hidden", hidden);
			// 调用模板生成html
			queryHtml = ZRQueryHtmlUtils.generatorCode(temps);
			HttpSession session = _request.getSession(true);
			session.setAttribute("queryControl", queryControlService);
		} catch (Exception ex) {
			ex.printStackTrace();
			return "";
		}
		return queryHtml;
	}

	/***
	 * ftl 调整，生成PC端查询返回rows data
	 * 
	 * @param user
	 * @param _request
	 * @param rq
	 * @param TITLENAME
	 * @return
	 */
	@SuppressWarnings("unused")
	private List<Map<String, Object>> getQueryRows(SessionUser user, HttpServletRequest _request, Request rq,
			String TITLENAME) {
		List<Map<String, Object>> resultList = new ArrayList<>();

		String ID = _request.getParameter("ID");
		if (ID == null) {
			ID = "";
		}
		String SHOWTITLE = _request.getParameter("SHOWTITLE");
		if (SHOWTITLE == null) {
			SHOWTITLE = "";
		}
		String STB = _request.getParameter("STB");
		if (STB == null) {
			STB = "0";
		}
		String ALIGN = _request.getParameter("ALIGN");
		if (ALIGN == null) {
			ALIGN = "center";
		}
		String XH = _request.getParameter("XH");
		if (XH == null) {
			XH = "1";
		}

		String ISCODE = _request.getParameter("ISCODE");
		if (ISCODE == null) {
			ISCODE = "1";
		}
		if (ISCODE.length() == 0) {
			ISCODE = "1";
		}

		String ISNULL = _request.getParameter("ISNULL");
		if (ISNULL == null) {
			ISNULL = "1";
		}

		String BUTTONIDS = _request.getParameter("BUTTONIDS");
		if (BUTTONIDS == null) {
			BUTTONIDS = "";
		}

		String ifFirstData = _request.getParameter("ifFirstData");
		String strWhere = _request.getParameter("strWhere");
		if (strWhere == null) {
			strWhere = "";
		}

		String queryFlag = _request.getParameter("queryFlag");
		if (queryFlag == null) {
			queryFlag = "0";
		}

		String swidth = _request.getParameter("SWIDTH");
		if (swidth == null) {
			swidth = "0";
		}

		String sheight = _request.getParameter("SHEIGHT");
		if (sheight == null) {
			sheight = "0";
		}

		int currentPage = 1;
		try {
			currentPage = Integer.parseInt(_request.getParameter("page"));
		} catch (Exception ex) {
			currentPage = 1;
		}
		try {
			QUERY_CONFIG_TABLE queryConfigEntity = queryControlService.getConfigTable(ID);
			// 1 一般查询 2 操作查询 3 显示列表 4 嵌入Iframe 5 查询录入项和结果分离
			String queryType = queryConfigEntity.getTYPE();
			// 页面传递的参数
			String P1, P2, P3, P4, P5;
			P1 = _request.getParameter("P1");
			P2 = _request.getParameter("P2");
			P3 = _request.getParameter("P3");
			P4 = _request.getParameter("P4");
			P5 = _request.getParameter("P5");
			resultList = queryControlService.getQueryResultRows(ID, queryConfigEntity.getQTABLETYPE(), queryType, user,
					rq, P1, P2, P3, P4, P5, ifFirstData, currentPage, swidth, sheight, XH);
			HttpSession session = _request.getSession(true);
			session.setAttribute("queryControl", queryControlService);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return resultList;
	}

	
}