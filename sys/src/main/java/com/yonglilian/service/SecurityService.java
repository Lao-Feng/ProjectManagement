package com.yonglilian.service;

import com.yonglilian.model.BPIP_MENU;
import com.yonglilian.model.BPIP_ROLE;
import com.yonglilian.model.BPIP_USER_ROLE;
import zr.zrpower.model.BPIP_USER;

import java.util.List;

/**
 * 系统安全管理服务层
 * 
 * @author lwk
 *
 */
public interface SecurityService {
	/**
	 * 功能或作用：获取菜单列表
	 * @param strWhere 查询条件
	 * @return
	 * @throws Exception
	 */
	public BPIP_MENU[] getMenuList(String strWhere) throws Exception;

	/**
	 * 功能或作用：获取菜单列表
	 * @param strPARENTNO 查询条件
	 * @return
	 * @throws Exception
	 */
	public BPIP_MENU[] getMenuList1(String strPARENTNO) throws Exception;

	/**
	 * 功能或作用：获取菜单列表
	 * @return
	 * @throws Exception
	 */
	public BPIP_MENU[] getMenuAllList() throws Exception;

	/**
	 * 功能或作用：添加新菜单
	 * @param menu 菜单实体
	 * @param strPARENTNO 父结点
	 * @return
	 * @throws Exception
	 */
	public boolean addMenu(BPIP_MENU menu, String strPARENTNO) throws Exception;

	/**
	 * 删除菜单
	 * @param MenuID
	 * @return
	 * @throws Exception
	 */
	public boolean deleteMenu(String menuID) throws Exception;

	/**
	 * 编辑菜单
	 * @param menu 菜单信息实体
	 * @return
	 * @throws Exception
	 */
	public boolean updateMenu(BPIP_MENU menu) throws Exception;

	/**
	 * 功能或作用：获取角色列表
	 * @param strWhere 查询条件
	 * @return
	 * @throws Exception
	 */
	public BPIP_ROLE[] getRoleList(String strWhere) throws Exception;

	/**
	 * 功能或作用：获取角色列表
	 * @param roleID
	 * @return
	 * @throws Exception
	 */
	public BPIP_ROLE[] getRoleList2(String roleID) throws Exception;

	/**
	 * 功能或作用：获取角色列表
	 * @return
	 * @throws Exception
	 */
	public BPIP_ROLE[] getRoleList1() throws Exception;

	/**
	 * 功能或作用：生成角色用户列表
	 * @param roleNo 角色编号
	 * @param listName 列表名称
	 * @param Is_Enabled 是否只读
	 * @return returnValue 返回记录集
	 */
	public String getRoleUserList(String roleNo, String listName, String Is_Enabled) throws Exception;

	/**
	 * 功能或作用：根据用户编号得到用户的单位名称
	 * @param userNo 用户编号
	 * @return 返回单位名称
	 * @throws Exception
	 */
	public String getUserUnitName(String userNo) throws Exception;

	/**
	 * 功能或作用：根据用户编号得到用户的真实名称
	 * @param userNo 用户编号
	 * @return 返回用户名称
	 * @throws Exception
	 */
	public String getUserTrueName(String userNo) throws Exception;

	/**
	 * 功能或作用：得到所有用户的选择列表
	 * @param listName 列表名称
	 * @param Is_Enabled 是否只读
	 * @return 返回用户名称
	 * @throws Exception
	 */
	public String getUserAllList(String listName, String Is_Enabled) throws Exception;

	/**
	 * 功能或作用：添加角色
	 * @param role 角色实体
	 * @param menuNo 选择的菜单编号
	 * @return
	 * @throws Exception
	 */
	public boolean addRole(BPIP_ROLE role, String menuNo) throws Exception;

	/**
	 * 功能或作用：分析规则字符串，生成数组
	 * @param strItems 字符串
	 * @param strItemMark 标识符
	 * @return
	 * @throws Exception
	 */
	public List<String> getArrayList(String strItems, String strItemMark) throws Exception;

	/**
	 * 功能或作用：根据角色名称得到角色编号
	 * @param roleName 角色名称
	 * @return 返回角色编号
	 * @throws Exception
	 */
	public Integer getRoleID(String roleName) throws Exception;

	/**
	 * 功能或作用：根据角色ID得到拥有该角色的用户列表
	 * @param roleID 角色ID
	 * @return
	 * @throws Exception
	 */
	public BPIP_USER[] getRoleUserList(String roleID) throws Exception;

	/**
	 * 功能或作用：根据角色ID得到属于该角色的菜单列表
	 * @param roleID 角色ID
	 * @return
	 * @throws Exception
	 */
	public BPIP_MENU[] getRoleMenuList(String roleID) throws Exception;

	/**
	 * 功能或作用：得到当前编号的父菜单名称
	 * @param menuNo 角色ID
	 * @param intFlag 菜单层数
	 * @return 返回菜单名称
	 * @throws Exception
	 */
	public String getMenuParentName(String menuNo, int intFlag) throws Exception;

	/**
	 * 功能或作用：根据菜单编号和角色编号删除角色权限表的记录
	 * @param roleNo 角色ID
	 * @param menuNo 菜单编号
	 * @return
	 * @throws Exception
	 */
	public boolean doRoleMenuDel(String roleNo, String menuNo) throws Exception;

	/**
	 * 功能或作用：根据用户编号和角色编号删除用户角色表的记录
	 * @param roleNo 角色ID
	 * @param userNo 用户编号
	 * @return
	 * @throws Exception
	 */
	public boolean doRoleUserDel(String roleNo, String userNo) throws Exception;

	/**
	 * 功能或作用：编辑角色
	 * @param role 角色实体
	 * @param menuNo 选择的菜单编号
	 * @return
	 * @throws Exception
	 */
	public boolean editRole(BPIP_ROLE role, String menuNo) throws Exception;

	/**
     * 功能或作用：编辑角色
     * @param strRole 角色实体
     * @param unitId 单位ID
     * @param formFields 选择的列表(用户)
     * @return
     */
    public boolean editRoleUser(String strRole, String unitId, String formFields) throws Exception;

    /**
     * 功能或作用：删除角色及相关的信息
     * @param roleID 角色ID
     * @return 返回是否删除成功
     */
    public boolean doRoleDel(String roleID) throws Exception;

    /**
     * 功能或作用：删除指定表下指定ID的记录
     * @param strTable 表名
     * @param strID ID值
     * @param strType 类型(1字符 2数字)
     * @return 返回是否删除成功
     */
    public boolean delTableData(String strTable, String strID, String strType) throws Exception;

    /**
     * 功能或作用：保存用户角色管理
     * @param roelListStr 角色ID字符串
     * @param userListStr 用户ID字符串
     * @return returnValue 返回值
     */
    public boolean doUserRoleManage(String roelListStr, String userListStr) throws Exception;

    /**
     * 功能或作用：根据用户ID得到属于该用户的角色列表
     * @param userID 用户ID
     * @return returnValue 返回记录集
     */
    public BPIP_USER_ROLE[] getUserRoleList(String userID) throws Exception;

    /**
     * 功能或作用：根据角色编号得到角色名称
     * @param roleNo
     * @return 返回用户名称
     * @throws Exception
     */
    public String getRoleTrueName(String roleNo) throws Exception;

    /**
     * 功能或作用：获取最大的角色编号加1
     * @Return int 执行后返回一个最大的角色编号加1
     */
    public Integer getRoleId() throws Exception;

    /**
     * 功能或作用：根据角色区域获取角色信息
     * @param unitId查询条件
     * @return BPIP_ROLE 返回记录集
     * String unitId 通过单位前缀编码模糊查询
     */
    public BPIP_ROLE[] getXzRoleList(String unitId) throws Exception;

    /**
     * 功能或作用：根据登录角色 获取所属菜单权限
     * @param roleId
     * @return
     * @throws Exception
     */
    public BPIP_MENU[] getMenuAllListRole(String roleId) throws Exception;

}
