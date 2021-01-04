package com.yonglilian.service;

/**
 * 系统管理服务层
 * @author lwk
 *
 */
public interface SeManageService {
	/**
	 * 功能：得到系统附件列表
	 * @param strFLOWID 流程标识
	 * @param strFORMID 表单ID值
	 * @return 返回附件列表
	 * @throws Exception
	 */
	public String getSysFileNameList(String strFLOWID, String strFORMID) throws Exception;

	/**
	 * 功能：新建文件夹
	 * @param strPath 文件夹的路径
	 * @return
	 * @throws Exception
	 */
	public boolean createFileFoler(String strPath) throws Exception;

	/**
	 * 功能：增加系统附件记录
	 * @param strFLOWID 流程标识
	 * @param strFORMID 表单ID
	 * @param strAID 活动ID
	 * @param strFileName 文件名
	 * @param strUserID 用户ID
	 * @param strUserID 用户附件ID
	 * @param strpTitle 原文件标题
	 * @return return 返回是否成功
	 */
	public boolean sysAttFileAdd(String strFLOWID, String strFORMID, String strAID, 
			  String strFileName, String strUserID, String strUserFileID,String strpTitle) throws Exception;

	/**
	 * 功能：显示系统附件列表
	 * @param strFLOWID 流程标识
	 * @param strFORMID 表单ID
	 * @param strUserID 用户ID
	 * @return 返回附件列表
	 * @throws Exception
	 */
	public String showSysAttFile(String strFLOWID, String strFORMID, String strUserID) throws Exception;

	/**
	 * 功能：删除系统附件记录
	 * @param strFileID 附件ID
	 * @return
	 * @throws Exception
	 */
	public boolean sysAttFileDel(String strFileID) throws Exception;

	/**
	 * 功能：根据文件ID得到系统附件服务器上的完整路径
	 * @param strFileID 附件ID
	 * @return
	 * @throws Exception
	 */
	public String sysAttFileAllPath(String strFileID) throws Exception;

	/**
	 * 功能：根据文件ID得到系统附件的文件名称
	 * @param strFileID 附件ID
	 * @return return 返回完整路径
	 */
	public String getSysAttFileName(String strFileID) throws Exception;

	/**
	 * 功能或作用：得到待选择用户的列表
	 * @param strType 1本单位人员 2所有人员
	 * @param strUnitID 单位ID
	 * @return 返回用户选择列表
	 */
	public String getAllTrueNameLst(String strType, String strUnitID) throws Exception;

	/**
	 * 功能或作用：得到待选择用户的列表
	 * @param strUnitID 单位ID
	 * @param strImage
	 * @param strIco
	 * @return
	 * @throws Exception
	 */
	public String getUserNameLst(String strUnitID, String strImage, String strIco) throws Exception;

	/**
	 * 功能：得到已经上传的附件数
	 * @param strFLOWID 流程标识
	 * @param strFORMID 表单ID
	 * @return 返回附件数
	 * @throws Exception
	 */
	public int getAttNum(String strFLOWID, String strFORMID) throws Exception;

}
