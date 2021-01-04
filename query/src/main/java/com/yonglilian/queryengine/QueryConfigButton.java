package com.yonglilian.queryengine;

import com.yonglilian.common.util.FunctionMessage;
import com.yonglilian.queryengine.mode.QUERY_CONFIG_BUTTON;
import com.yonglilian.service.impl.QueryButtonServiceImpl;
import com.yonglilian.web.bean.MessageBox;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class QueryConfigButton extends MessageBox {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	QueryButtonServiceImpl queryButton = new QueryButtonServiceImpl();

	// Initialize global variables
	public void init() throws ServletException {
	}

	// Process the HTTP Get request
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html; charset=UTF-8");
		String strAction = request.getParameter("Act");
		if (strAction != null) {
			if (strAction.equals("edit")) {
				EditButton(request, response);
			}
			if (strAction.equals("del")) {
				DeleteButton(request, response);
			}
			if (strAction.equals("add")) {
				AddButton(request, response);
			}
		}
	}

	// Process the HTTP Post request
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

	// Clean up resources
	public void destroy() {
	}

	// 修改配置按扭信息
	private void EditButton(HttpServletRequest request, HttpServletResponse response) throws IOException {
		QUERY_CONFIG_BUTTON Button = new QUERY_CONFIG_BUTTON();
		Button.fullDataFromRequest(request);
		try {
			queryButton.editButton(Button);
		} catch (Exception ex) {
			this.Msg = "配置按扭管理中间件服务失败！<br>详细错误信息：" + ex.toString();
		}
		this.out = response.getWriter();
	}

	// 删除配置按扭
	private void DeleteButton(HttpServletRequest request, HttpServletResponse response) throws IOException {
		try {
			FunctionMessage fm = new FunctionMessage(1);
			fm = queryButton.deleteButton(request.getParameter("ID"));
			if (fm.getResult()) {
				this.Msg = fm.getMessage();
				this.isOk = fm.getResult();
			} else {
				this.Msg = fm.getMessage();
				this.isOk = fm.getResult();
			}
		} catch (Exception ex) {
			this.Msg = "配置按扭管理中间件服务失败！<br>详细错误信息：" + ex.toString();
		}
		this.out = response.getWriter();
	}

	/**
	 * 添加配置按扭
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	private void AddButton(HttpServletRequest request, HttpServletResponse response) throws IOException {
		QUERY_CONFIG_BUTTON ButtonID = new QUERY_CONFIG_BUTTON();
		try {
			ButtonID.fullDataFromRequest(request);
			queryButton.addButton(ButtonID);
		} catch (Exception ex) {
			this.Msg = "配置按扭管理中间件服务失败！<br>详细错误信息：<br>" + ex.toString();
		}
		this.out = response.getWriter();
	}
}