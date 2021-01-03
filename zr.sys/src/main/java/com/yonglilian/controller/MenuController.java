package com.yonglilian.controller;

import com.yonglilian.model.BPIP_MENU;
import com.yonglilian.service.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import zr.zrpower.controller.BaseController;
import zr.zrpower.intercept.annotation.ZrSafety;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 菜单操作
 * @author nfzr
 *
 */
@ZrSafety
@Controller
@RequestMapping("/menu")
public class MenuController extends BaseController {
	private static final long serialVersionUID = 1L;
	/** 系统管理服务层. */
	@Autowired
	private SecurityService menuService;

	@RequestMapping("/actionmenu")
	public String actionMenu(ModelMap modelMap, 
			HttpServletRequest request, HttpServletResponse response) {
		String pagepath = "";
		try {
		    String strAct = request.getParameter("Act");
		    this.isOk = true;
		    this.Msg = "非法调用，你的操作已被记录！";
		    if(strAct!=null){
		        try {
		          if (strAct.equals("edit")) {
		            BPIP_MENU menu = new BPIP_MENU();
		            menu.fullDataFromRequest(request);
		            if (menuService.updateMenu(menu)){
		                menu = null;
		            } else {
		              this.isOk = false;
		              this.Msg = "修改失败，请与系统管理员联系！";
		            }
		            response.getWriter().write("{\"data\":\"success\"}");
					response.getWriter().flush();
					return null;
		          }
		          else if (strAct.equals("delete")) {
		            String strMENUNO = request.getParameter("MENUNO");
		            if (strMENUNO != null) {
		                if (menuService.deleteMenu(strMENUNO)) {
		                } else {
		                  this.isOk = false;
		                  this.Msg = "删除菜单出错或有关联数据,不能删除!";
		                }
		            }
		            else {
		              this.isOk = false;
		              this.Msg = "删除菜单出错!";
		            }
		            response.getWriter().write("{\"data\":\"success\"}");
					response.getWriter().flush();
					return null;
		          }
		          else if (strAct.equals("add")) {
		            String strPARENTNO = request.getParameter("PARENTNO");
		            BPIP_MENU Menu = new BPIP_MENU();
		            Menu.fullDataFromRequest(request);

		            if (menuService.addMenu(Menu, strPARENTNO)) {
		            } else {
		            	this.Msg = "增加菜单失败！";
		            }
		            response.getWriter().write("{\"data\":\"success\"}");
					response.getWriter().flush();
					return null;
		          }
		          //打开增加菜单----
		          else if (strAct.equals("openadd")){
		             String strPARENTNO = request.getParameter("Menu_No");
		             if (strPARENTNO==null)
		             {strPARENTNO = "";}
		             modelMap.put("PARENTNO",strPARENTNO);
		             
		             pagepath = "Zrsysmanage/menu/AddFrm";
		             return pagepath;
		          }
		          //打开编辑菜单----
		          else if (strAct.equals("openedit")){
		            BPIP_MENU[] menu = null;
		            BPIP_MENU menu1 = null;
		            String ls = "";
		            String strPARENTNO = request.getParameter("Menu_No");
		            String strMENUNO = request.getParameter("ID");
		            String strWhere = " where MENUNO = '"+strMENUNO+"'";
		            menu = menuService.getMenuList(strWhere);
		            menu1 = menu[0];
		            if (menu1.getISPOWER().equals("0")) {
		               ls = "<option value='0' selected >类别</option>\r\n";
		               ls = ls + "<option value='1'>任务</option>\r\n";
		            } else {
		               ls = "<option value='0'>类别</option>\r\n";
		               ls = ls + "<option value='1' selected >任务</option>\r\n";
		            }
		            menu1.setISPOWER(ls);
		            if (menu1.getISOPEN().equals("0")) {
		               ls = "<option value='0' selected >否</option>\r\n";
		               ls = ls + "<option value='1'>是</option>\r\n";
		            } else {
		               ls = "<option value='0'>否</option>\r\n";
		               ls = ls + "<option value='1' selected >是</option>\r\n";
		            }
		            menu1.setISOPEN(ls);
		            if (menu1.getFLAG().equals("1")) {
		               ls = "<option value='1' selected >启用</option>\r\n";
		               ls = ls + "<option value='0'>禁止</option>\r\n";
		            } else {
		              ls = "<option value='1'  >启用</option>\r\n";
		               ls = ls + "<option value='0' selected>禁止</option>\r\n";
		            }
		            menu1.setFLAG(ls);
		            modelMap.put("PARENTNO",strPARENTNO);
		            modelMap.put("MENU",menu1);
		            
		            pagepath = "Zrsysmanage/menu/EditFrm";
		            return pagepath;
		          }
		        } catch (Exception ex){
		           this.Msg = "连接中间件服务器失败，请稍后再试！";
		        }
		    }
		    this.out = response.getWriter();
		    //this.getBox();
		} catch (Exception ex) {
			LOGGER.error("MenuController.actionMenu Exception:\n", ex);
		}
		return pagepath;
	}
}