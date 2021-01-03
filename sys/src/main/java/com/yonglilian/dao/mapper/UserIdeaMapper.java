package com.yonglilian.dao.mapper;

import tk.mybatis.spring.annotation.MapperScan;

import java.util.Map;

/**
 * 意见设置数据层
 * @author lwk
 *
 */
@MapperScan({"com.yonglilian.dao.mapper"})
public interface UserIdeaMapper {
	/**
	 * 保存新意见信息
	 * @param map 新意见信息实体
	 * @return
	 * @throws Exception
	 */
	public Integer addUserIdea(Map<String, Object> map) throws Exception;

	/**
	 * 修改意见信息
	 * @param userIdea 新意见信息实体
	 * @return
	 * @throws Exception
	 */
	public Integer editUserIdea(Map<String, Object> map) throws Exception;

}