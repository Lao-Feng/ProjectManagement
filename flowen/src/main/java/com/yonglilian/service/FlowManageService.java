package com.yonglilian.service;

public interface FlowManageService {
	/**
	 * 功能：根据流程分支编号字符串生成流程分支选择列表
	 * @param strNode_No_S 当前活动编号
	 * @param strNode_No 流程分支编号字符串
	 * @param strName 名称
	 * @param strIco 图标
	 * @return returnValue 返回分支选择列表
	 */
	public String getNextActivityList(String strNode_No_S, String strNode_No, 
			String strName, String strIco) throws Exception;

	/**
	 * 功能：根据用户编号字符串生成用户列表
	 * @param strUserLst 用户列表字符串
	 * @param strListName 列表名称
	 * @param ico 图标
	 * @param strtype 类型
	 * @param strAid 选择的活动ID
	 * @return returnValue 返回生成的用户列表
	 */
	public String getShowUserList(String strUserLst, String strListName, String ico, 
			String strtype, String strAid, String SELECTDEPTID) throws Exception;

	/**
	 * 生成用户列表，重新指定角色下的人员,按角色分类。
	 * @param strListName
	 * @param ico
	 * @param strtype
	 * @param strAid
	 * @param roles
	 * @return
	 * @throws Exception
	 */
	public String getRoleUserList(String strListName, String ico, String strtype, 
			String strAid, String roles) throws Exception;

}
