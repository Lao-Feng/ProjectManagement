package com.yonglilian.dao.mapper;

import tk.mybatis.spring.annotation.MapperScan;

import java.util.Map;

/**
 * 查询配置的DAO层
 * @author lwk
 *
 */
@MapperScan({"com.yonglilian.dao.mapper"})
public interface QueryConfigTableMapper {
	/**
	 * 增加配置表数据实体类
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public Integer insertQUERY_CONFIG_TABLE(Map<String, Object> map) throws Exception;

	/**
	 * 编辑配置表数据实体类
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public Integer editQUERY_CONFIG_TABLE(Map<String, Object> map) throws Exception;

	/**
	 * 增加查询显示结果配置表数据实体类
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public Integer insertQUERY_CONFIG_SHOWFIELD(Map<String, Object> map) throws Exception;

	/**
	 * 编辑查询显示结果配置表数据实体类
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public Integer editQUERY_CONFIG_SHOWFIELD(Map<String, Object> map) throws Exception;

	/**
	 * 增加查询字段配置表数据实体类
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public Integer insertQUERY_CONFIG_QUERYFIELD(Map<String, Object> map) throws Exception;

	/**
	 * 编辑查询字段配置表数据实体类
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public Integer editQUERY_CONFIG_QUERYFIELD(Map<String, Object> map) throws Exception;

	/**
	 * 增加查询配置初始条件表数据实体类
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public Integer insertQUERY_CONFIG_INIT(Map<String, Object> map) throws Exception;

	/**
	 * 编辑查询配置初始条件表数据实体类
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public Integer editQUERY_CONFIG_INIT(Map<String, Object> map) throws Exception;

	/**
	 * 增加查询关系配置表数据实体类
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public Integer insertQUERY_CONFIG_CONNECTION(Map<String, Object> map) throws Exception;

	/**
	 * 编辑查询关系配置表数据实体类
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public Integer editQUERY_CONFIG_CONNECTION(Map<String, Object> map) throws Exception;

	/**
	 * 增加按钮配置关系表数据实体类
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public Integer insertQUERY_CONFIG_BRELATION(Map<String, Object> map) throws Exception;

	/**
	 * 编辑按钮配置关系表数据实体类
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public Integer editQUERY_CONFIG_BRELATION(Map<String, Object> map) throws Exception;

	/**
	 * 增加按钮参数配置表数据实体类
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public Integer insertQUERY_CONFIG_PARAMETER(Map<String, Object> map) throws Exception;

	/**
	 * 编辑按钮参数配置表数据实体类
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public Integer editQUERY_CONFIG_PARAMETER(Map<String, Object> map) throws Exception;

	/**
	 * 增加数据实体类
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public Integer insertQUERY_CONFIG_ORDER(Map<String, Object> map) throws Exception;

	/**
	 * 编辑数据实体类
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public Integer editQUERY_CONFIG_ORDER(Map<String, Object> map) throws Exception;

	/**
	 * 增加查询按钮表数据实体类
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public Integer insertQUERY_CONFIG_BUTTON(Map<String, Object> map) throws Exception;

	/**
	 * 编辑查询按钮表数据实体类
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public Integer editQUERY_CONFIG_BUTTON(Map<String, Object> map) throws Exception;
}