package com.yonglilian.controller;

import com.alibaba.fastjson.JSON;
import com.yonglilian.analyseengine.bean.JxExcel;
import com.yonglilian.analyseengine.mode.ANALYSE_STATISTICS_MAIN;
import com.yonglilian.service.StatisticsControlService;
import com.yonglilian.service.ZRAnalyHtmlUtils;
import com.yonglilian.service.impl.StatisticsControlServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import zr.zrpower.common.util.R;
import zr.zrpower.controller.BaseController;
import zr.zrpower.intercept.annotation.ZrSafety;
import zr.zrpower.model.SessionUser;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 菜单信息控制器
 * 
 * @author lwk
 *
 */
@ZrSafety
@Controller
@RestController
@RequestMapping("/zranaly")
public class ZRStatisticsController extends BaseController {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** 数据统计引擎服务. */
	@Autowired
	private StatisticsControlService statisticsControlService;
	/**
	 * 数据统计引擎服务
	 */
	@Autowired
	private StatisticsControlServiceImpl SControlService;

	/**
	 * ftl 调整（统计引擎视图view展示）
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("/statisview")
	public void statisview(HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.setContentType("text/html; charset=UTF-8");
		Map<String, Object> maps = null,analyQuery=null;
		String ID = request.getParameter("ID") != null ? request.getParameter("ID") : "";// 配置ID == COLL_AID
		HttpSession session = request.getSession(true);
		SessionUser user = (SessionUser) session.getAttribute("userinfo");
		if (ID.length() > 0) {
			maps = null;
			maps = new HashMap<String, Object>();
			maps = statisticsControlService.getShowPcHtml(ID, user.getUserID());
			analyQuery = getTemplateStr(request, response);
			//隐藏提交
			if(analyQuery.containsKey("hidden")) {
				maps.put("hidden", analyQuery.get("hidden"));
			}else {
				maps.put("hidden", null);
			}
			//字典选择 
			if(analyQuery.containsKey("codelist")) {
				maps.put("codelist", analyQuery.get("codelist"));
			}else {
				maps.put("codelist", 0);
			}
			
			// 调用模板生成html
			String collenHtml = "";
			collenHtml = ZRAnalyHtmlUtils.htmlCode(maps);
			response.getWriter().print(collenHtml);
		}
	}


	/***
	 * ftl 调整（调用引擎返回统计数据）
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping("/statiscompute")
	public R statisticsCompute(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String json = "";
		String ID = request.getParameter("ID");
		if (ID == null) {
			ID = "";
		}
		String P1 = request.getParameter("P1");
		if (P1 == null) {
			P1 = "";
		}
		String P2 = request.getParameter("P2");
		if (P2 == null) {
			P1 = "";
		}
		String P3 = request.getParameter("P3");
		if (P3 == null) {
			P3 = "";
		}
		String P4 = request.getParameter("P4");
		if (P4 == null) {
			P4 = "";
		}
		String P5 = request.getParameter("P5");
		if (P5 == null) {
			P5 = "";
		}
		String CONN = request.getParameter("CONN");
		if (CONN == null) {
			CONN = "";
		}
		// 默认单位ID，为空时统计当前单位，有值时统计指定单位
		String UNITID = request.getParameter("UNITID");
		if (UNITID == null) {
			UNITID = "";
		}
		try {
			HttpSession session = request.getSession(true);
			SessionUser user = (SessionUser) session.getAttribute("userinfo");
			// 计算报表
			statisticsControlService.getCompute(ID, P1, P2, P3, P4, P5, user, CONN, UNITID);
			json = statisticsControlService.getShowPcDatas(ID, user.getUserID());
			user = null;
		} catch (Exception ex) {
			return R.error("报表统计发送异常!");
		}
		return R.ok().put("json", json);
	}
	
	
	/**
	 * ftl  调整（调用生成统计默认模板）
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("/templaet")
	public R statistemplaet(HttpServletRequest request) throws Exception {
		String ID=request.getParameter("ID");
        statisticsControlService.savetemplaet(ID);
		return R.ok();
	}
	
	/***
	 * ftl   调整（导出到excl）
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/statisexcl")
	public void statisexcl(HttpServletRequest request, HttpServletResponse response) {
		response.setContentType("text/html; charset=UTF-8");
		try {
			HttpSession session = request.getSession(true);
			SessionUser user = (SessionUser) session.getAttribute("userinfo");
			Date date = new Date();
			SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");
			String StrID = request.getParameter("ID");
			String fileName = "";
			try {
				ANALYSE_STATISTICS_MAIN STATISTICS = SControlService.getSmain(StrID);
				fileName = STATISTICS.getSTATISTICSNAME() + fmt.format(date) + ".XLS";
				JxExcel jxExcel = new JxExcel();
				response.reset();
				response.setContentType("application/vnd.ms-excel;charset=UTF-8");
				response.addHeader("Content-Disposition",
						"attachment;filename=" + new String(fileName.getBytes("GBK"), "iso8859-1"));
				ServletOutputStream os = response.getOutputStream();
				jxExcel.WriteExcel(SControlService, os, StrID, user);
			} catch (Exception ex) {
				out.print("导出Excel文件出现异常" + ex.getMessage());
			}
		} catch (Exception ex) {
			LOGGER.error("StatisticsController.openStatistics Exception:\n", ex);
		} finally {
			if (out != null) {
				out.close();
			}
		}
	}

	
	/***
	 * ftl 调整（生成统计条件页面map）
	 * 
	 * @param ID
	 * @param P1
	 * @param P2
	 * @param P3
	 * @param P4
	 * @param P5
	 * @param user
	 * @param Smain
	 * @param req
	 * @param TYPE
	 * @param USERTYPE
	 * @param SHOWTITLE
	 * @param UNITID
	 * @return
	 */
	private Map<String,Object> getTemplateStr(HttpServletRequest request, HttpServletResponse response) {
		Map<String,Object> result = new HashMap<>();
		//数据参数请求
		List<String> formMap = new ArrayList<String>();
		String ID = request.getParameter("ID");
		String SHOWTITLE = request.getParameter("SHOWTITLE");
		String P1 = request.getParameter("P1");
		String P2 = request.getParameter("P2");
		String P3 = request.getParameter("P3");
		String P4 = request.getParameter("P4");
		String P5 = request.getParameter("P5");
		String TYPE = request.getParameter("TYPE");
		// 调用报表的用户类别 0 按用户的行政区划权限 1 部门领导 2 普通员工
		String USERTYPE = request.getParameter("USERTYPE");
		String UNITID = request.getParameter("UNITID");
		try {
			ANALYSE_STATISTICS_MAIN Smain = statisticsControlService.getSmain(ID);
			ID = ID!=null&&ID.trim().length()>0 ? ID.trim() : "";
			P1 = P1!=null&&P1.trim().length()>0 ? P1.trim() : "";
			P2 = P2!=null&&P2.trim().length()>0 ? P2.trim() : "";
			P3 = P3!=null&&P3.trim().length()>0 ? P3.trim() : "";
			P4 = P4!=null&&P4.trim().length()>0 ? P4.trim() : "";
			P5 = P5!=null&&P5.trim().length()>0 ? P5.trim() : "";
			TYPE = TYPE!=null&&TYPE.trim().length()>0 ? TYPE.trim() : "1";
			SHOWTITLE = SHOWTITLE!=null&&SHOWTITLE.trim().length()>0 ? SHOWTITLE.trim() : "";
			UNITID = UNITID!=null&&UNITID.trim().length()>0 ? UNITID.trim() : "";
			USERTYPE = USERTYPE!=null&&USERTYPE.trim().length()>0 ? USERTYPE.trim() : "2";
			formMap.add("ID:'"+ID+"',");
			formMap.add("P1:'"+P1+"',");
			formMap.add("P2:'"+P2+"',");
			formMap.add("P3:'"+P3+"',");
			formMap.add("P4:'"+P4+"',");
			formMap.add("P5:'"+P5+"',");
			formMap.add("SHOWTITLE:'"+SHOWTITLE+"',");
			formMap.add("UNITID:'"+UNITID+"',");
			formMap.add("TYPE:'"+TYPE+"',");
			formMap.add("USERTYPE:'"+USERTYPE+"',");
			// 得到报表统计的自定义条件类型
			String tdLink = Smain.getSHOWLINK();
			tdLink = tdLink != null && tdLink.length()>0 ? tdLink : "";
			tdLink = "\""+tdLink+"\"";
			formMap.add("tdLink:"+tdLink+",");
			// 得到报表统计的自定义条件类型
			String strSINPUTTYPE = Smain.getSINPUTTYPE();
			// 得到报表的显示类型(1一般报表 2 嵌入式报表)
			String strISSHOWTYPE = Smain.getISSHOWTYPE();
			// 在网页上显示的表格模板
			String strTIMETEMPLATE = Smain.getTIMETEMPLATE();
			if (strTIMETEMPLATE == null) {
				strTIMETEMPLATE = "";
			}
			// 统计按钮名称
			String strTbutton = Smain.getTBUTTON();
			strTbutton = strTbutton != null && strTbutton.length()>0 ? strTbutton : "统计";
			String CODETABLE = Smain.getCODETABLE();
			// 生成下拉字典
			if (CODETABLE != null && CODETABLE.length() > 2) {
				List<Map<String,Object>> codeList = statisticsControlService.getVueCodeHtml(CODETABLE);
				result.put("codelist", JSON.toJSON(codeList).toString());
			}else {
				result.put("codelist", 0);
			}
			// 报表类型
			formMap.add("SINPUTTYPE:'"+strSINPUTTYPE+"',");
			// 非嵌入式报表时加载按钮
			if (!strISSHOWTYPE.equals("2")) {
				formMap.add("excl:true,");
				formMap.add("exclbut:'"+strTbutton+"',");
			}
			
			result.put("hidden", formMap);
		} catch (Exception ex) {
			return null;
		}
		return result;
	}

}