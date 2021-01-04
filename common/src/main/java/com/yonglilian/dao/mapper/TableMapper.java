package com.yonglilian.dao.mapper;

import org.apache.ibatis.annotations.Param;
import tk.mybatis.spring.annotation.MapperScan;

import java.util.Map;

/**
 * 表名表管理数据层
 * @author lwk
 *
 */
@MapperScan({"com.yonglilian.dao.mapper"})
public interface TableMapper {
	/**
	 * 动态创建数据表
	 * @param execSQL
	 * @return
	 * @throws Exception
	 */
	public Integer createTable(@Param("execSQL") String execSQL) throws Exception;

	/**
	 * 动态修改数据表
	 * @param execSQL
	 * @return
	 * @throws Exception
	 */
	public Integer updateTable(@Param("execSQL") String execSQL) throws Exception;

	/**
	 * 增加表名表信息
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public Integer insertTable(Map<String, Object> map) throws Exception;

	/**
	 * 增加字段名表信息
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public Integer insertField(Map<String, Object> map) throws Exception;

	/**
	 * 增加表名表信息
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public Integer editTable(Map<String, Object> map) throws Exception;

	/**
	 * 增加字段名表信息
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public Integer editField(Map<String, Object> map) throws Exception;
}