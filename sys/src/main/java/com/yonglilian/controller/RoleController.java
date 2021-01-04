package com.yonglilian.controller;

import com.yonglilian.common.util.StringWork;
import com.yonglilian.intercept.annotation.ZrSafety;
import com.yonglilian.model.BPIP_MENU;
import com.yonglilian.model.BPIP_ROLE;
import com.yonglilian.model.SessionUser;
import com.yonglilian.service.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 角色信息控制器
 * @author lwk
 *
 */
@ZrSafety
@Controller
@RequestMapping("/role")
public class RoleController extends BaseController {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** 系统安全管理服务层. */
	@Autowired
	private SecurityService roleService;

	@RequestMapping("/actionrole")
	public String actionRole(ModelMap modelMap, 
			HttpServletRequest request, HttpServletResponse response) {
		String pagepath = "";
		try {
			String strAct = request.getParameter("Act");
			this.isOk = true;
			this.Msg = "非法调用，你的操作已被记录！";
			if (strAct != null) {
				try {
					if (strAct.equals("edit")) {
						String strMenu_No = request.getParameter("Menu_No");
						BPIP_ROLE Role = new BPIP_ROLE();
						Role.fullDataFromRequest(request);
						if (roleService.editRole(Role, strMenu_No)) {
						} else {
							this.Msg = "编辑角色信息失败或角色重复或本角色已经分配用户！";
						}
						response.getWriter().write("{\"data\":\"success\"}");
						response.getWriter().flush();
						return null;
					} else if (strAct.equals("edituser")) {
						String strFormfields = request.getParameter("formfields");
						String strRole = request.getParameter("ROLEID");
						String strUnitId = request.getParameter("UNITID");
						if (roleService.editRoleUser(strRole, strUnitId, strFormfields)) {
						} else {
							this.Msg = "编辑角色信息失败或角色重复！";
						}
						response.getWriter().write("{\"data\":\"success\"}");
						response.getWriter().flush();
						return null;
					} else if (strAct.equals("delete")) {
						String strROLEID = request.getParameter("ROLEID");
						if (strROLEID != null) {
							if (roleService.doRoleDel(strROLEID)) {
							} else {
								this.isOk = false;
								this.Msg = "删除角色出错!";
							}
						} else {
							this.isOk = false;
							this.Msg = "删除角色出错!";
						}
						response.getWriter().write("{\"data\":\"success\"}");
						response.getWriter().flush();
						return null;
					} else if (strAct.equals("userrolemanage")) {
						String strRoelListStr = request.getParameter("RoelListStr");
						String strUserListStr = request.getParameter("UserListStr");
						if (roleService.doUserRoleManage(strRoelListStr, strUserListStr)) {
						} else {
							this.Msg = "保存角色出错!";
						}
						response.getWriter().write("{\"data\":\"success\"}");
						response.getWriter().flush();
						return null;
					} else if (strAct.equals("add")) {
						String strMenu_No = request.getParameter("Menu_No");

						BPIP_ROLE Role = new BPIP_ROLE();
						Role.fullDataFromRequest(request);
						if (roleService.addRole(Role, strMenu_No)) {
						} else {
							this.Msg = "增加角色失败或角色重复！";
						}
						response.getWriter().write("{\"data\":\"success\"}");
						response.getWriter().flush();
						return null;
					}
					// 打开新增角色
					else if (strAct.equals("openaddrole")) {
						int intRoleId = 0;
						intRoleId = roleService.getRoleId();
						modelMap.put("ROLEID", String.valueOf(intRoleId));

						pagepath = "Zrsysmanage/role/AddFrm";
						return pagepath;
					}
					// 打开编辑角色
					else if (strAct.equals("openeditrole")) {
						BPIP_ROLE[] role = null;
						BPIP_ROLE role1 = null;
						BPIP_MENU[] rolemenu = null;
						String strRole_ID = request.getParameter("ID");
						String roleMenuNoStr = "";

						rolemenu = roleService.getRoleMenuList(strRole_ID);
						if (rolemenu != null && rolemenu.length > 0) {
							for (int i = 0; i < rolemenu.length; i++) {
								roleMenuNoStr += "," + rolemenu[i].getMENUNO();
							}
						}
						// 得到角色的记录集
						String strWhere = " where ROLEID=" + strRole_ID;
						role = roleService.getRoleList(strWhere);
						role1 = role[0];
						modelMap.put("ROLEMENUS", roleMenuNoStr);
						modelMap.put("ROLE", role1);

						pagepath = "Zrsysmanage/role/EditFrm";
						return pagepath;
					}
					// 打开编辑角色用户
					else if (strAct.equals("openroleuser")) {
						BPIP_ROLE[] role = null;
						BPIP_ROLE role1 = null;
						HttpSession session = request.getSession(true);
						SessionUser user = (SessionUser) session.getAttribute("userinfo");
						String strRole_ID = request.getParameter("ID");
						String strunit = "";
						strunit = user.getUnitID();
						StringWork sw = new StringWork();
						strunit = sw.CutLastZero(strunit, 2);
						role = roleService.getRoleList2(strRole_ID);
						role1 = role[0];
						String userlist1 = roleService.getUserAllList("srcList", "");
						String userlist2 = roleService.getRoleUserList(strRole_ID, "destList", "");

						modelMap.put("UNITID", user.getUnitID());
						modelMap.put("ROLE", role1);
						modelMap.put("USERLIST1", userlist1);
						modelMap.put("USERLIST2", userlist2);

						pagepath = "Zrsysmanage/role/EditFrmUser";
						return pagepath;
					} else {
					}
				} catch (Exception ex) {
					ex.printStackTrace();
					this.Msg = "连接中间件服务器失败，请稍后再试！";
				}
			}
			this.out = response.getWriter();
			// this.getBox();
		} catch (Exception ex) {
			LOGGER.error("RoleController.actionRole Exception:\n", ex);
		}
		return pagepath;
	}
}