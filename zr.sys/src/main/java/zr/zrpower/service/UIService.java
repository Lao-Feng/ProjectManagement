package zr.zrpower.service;

import zr.zrpower.model.BPIP_MENU;

import java.util.List;

/**
 * 界面公共函数库服务层
 * 
 * @author lwk
 *
 */
public interface UIService {
	/**
	 * 新框架菜单 查询第一级菜单
	 * @param userID 登陆用户ID
	 * @return
	 * @throws Exception
	 */
	public String[] loadMenuJqueryOne(String userID) throws Exception;

	/**
	 * 新框架菜单，左边菜单为二级、三级菜单体
	 * @param userID 登陆用户ID
	 * @param ymenId
	 * @return
	 * @throws Exception
	 */
	public String loadMenuJquery(String userID, String ymenId) throws Exception;

	/**
	 * 返回菜单
	 * @param userID 登陆用户ID
	 * @return
	 * @throws Exception
	 */
	public List<BPIP_MENU> loadUserMenu(String userID) throws Exception;

	/**
	 * 作用或功能：显示（表单脚/表格/窗体）头部HTML
	 * @param strTitle 标题名称
	 * @param userImagePath 用户图片路径
	 * @return HTML
	 * @throws Exception
	 */
	public String showHeadHtml(String strTitle, String userImagePath) throws Exception;

	/**
	 * 作用或功能：显示（表单脚/表格/窗体）头部HTML
	 * @param strTitle 标题名称
	 * @param titleImage 标题图标
	 * @param userImagePath 用户图片路径
	 * @return HTML
	 * @throws Exception
	 */
	public String showHeadHtml(String strTitle, String titleImage, String userImagePath) throws Exception;

	/**
     * 作用或功能：点击或链接按钮
     * @param strName 按钮名称 如：增加
     * @param strScript 点击时执行的javascript
     * @param strWidth 功能按钮长度
     * @param strImage 图标文件
     * @param userImagePath 用户图片路径
     * @return 返回HTML
     */
    public String clickButton(String strName, String strScript, String strImage, String userImagePath) throws Exception;

    /**
     * 作用或功能：点击按钮
     * @param strName 按钮名称 如：增加
     * @param strScript 点击时执行的javascript
     * @param strWidth 功能按钮长度
     * @param strImage 图标文件
     * @param userImagePath 用户图片路径
     * @return 返回HTML
     */
    public String clickButton1(String strName, String strScript, String strImage, String userImagePath) throws Exception;

    /**
     * 作用或功能：显示（表单脚/表格/窗体）中间开始部分HTML
     * @return
     * @throws Exception
     */
    public String showBodyStartHtml() throws Exception;

    /**
     * 功能或作用：显示（表单脚/表格/窗体）页脚
     * @param strTitle
     * @param userImagePath
     * @return
     * @throws Exception
     */
    public String showFootHtml(String strTitle, String userImagePath) throws Exception;

    /**
     * 功能或作用：显示表单脚结束部分
     * @return Foot HTML
     */
    public String showFootHtml() throws Exception;

    /**
     * 功能或作用：实现翻页
     * @param pageSize 一页的记录数
     * @param fileUrl 文件名
     * @param rowCount 总的记录数
     * @param page 当前页
     * @param strAll 是否分页显示
     * @Return PageMenu 执行后返回一个PageMenu字符串
     */
    public String createPageMenu(int pageSize, String fileUrl, int rowCount, int page, String strAll) throws Exception;
}