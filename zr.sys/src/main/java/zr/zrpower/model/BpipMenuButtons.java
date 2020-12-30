package zr.zrpower.model;

import java.io.Serializable;



/**
 * 系统菜单按钮表
 * 用户业务请求/返回类
 * @author nfzr
 * @email ftl@qq.com
 * @date 2019-10-14 12:18:01
 */
public class BpipMenuButtons implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//id
	private Long id;
	//菜单/id
	private String menuId;
	//按钮/权限名称
	private String name;
	//授权标识
	private String perms;
	//图标
	private String icon;

	/**
	 * 设置：id
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * 获取：id
	 */
	public Long getId() {
		return id;
	}
	/**
	 * 设置：菜单/id
	 */
	public void setMenuId(String menuId) {
		this.menuId = menuId;
	}
	/**
	 * 获取：菜单/id
	 */
	public String getMenuId() {
		return menuId;
	}
	/**
	 * 设置：按钮/权限名称
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * 获取：按钮/权限名称
	 */
	public String getName() {
		return name;
	}
	/**
	 * 设置：授权标识
	 */
	public void setPerms(String perms) {
		this.perms = perms;
	}
	/**
	 * 获取：授权标识
	 */
	public String getPerms() {
		return perms;
	}
	/**
	 * 设置：图标
	 */
	public void setIcon(String icon) {
		this.icon = icon;
	}
	/**
	 * 获取：图标
	 */
	public String getIcon() {
		return icon;
	}
}
