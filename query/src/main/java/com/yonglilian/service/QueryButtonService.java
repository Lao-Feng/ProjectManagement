package com.yonglilian.service;

import com.yonglilian.common.util.FunctionMessage;
import com.yonglilian.queryengine.mode.QUERY_CONFIG_BUTTON;

public interface QueryButtonService {
	/**
	 * 获取查询配置按钮列表
	 * @param strwhere 查询条件
	 * @return QUERY_CONFIG_BUTTON[]
	 */
	public QUERY_CONFIG_BUTTON[] getButtonList(String FID) throws Exception;

	public QUERY_CONFIG_BUTTON[] getShowButton(String FID) throws Exception;

	/**
	 * 获取查询配置按钮列表
	 * @param FID 查询配置ID
	 * @return QUERY_CONFIG_BUTTON[]
	 */
	public QUERY_CONFIG_BUTTON[] getButtonByFID(String FID) throws Exception;

	/**
	 * 增加查询按钮信息
	 * @param QUERY_CONFIG_BUTTON 新查询配置按钮实体
	 * @return FunctionMessage 保存是否成功,成功或是失败消息
	 */
	public FunctionMessage addButton(QUERY_CONFIG_BUTTON Bt) throws Exception;

	/**
	 * 功能：编辑查询配置按钮
	 * @param QUERY_CONFIG_BUTTON 编辑查询配置按钮的实体
	 * @return FunctionMessage
	 */
	public FunctionMessage editButton(QUERY_CONFIG_BUTTON Bt) throws Exception;

	/**
	 * 功能：删除查询配置按钮
	 * @param ID 要删除的查询配置按钮ID
	 * @return FunctionMessage
	 */
	public FunctionMessage deleteButton(String ID) throws Exception;

	/**
	 * 功能：获取查询配置按钮编号
	 * @param id 查询配置按钮ID
	 * @return String[]
	 */
	public QUERY_CONFIG_BUTTON getButtonByID(String id) throws Exception;

}