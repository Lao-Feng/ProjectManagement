package zr.zrpower.dao.mapper;

import tk.mybatis.spring.annotation.MapperScan;

import java.util.Map;

/**
 * 数据统计EJB服务数据层
 * @author lwk
 *
 */
@MapperScan({"zr.zrpower.dao.mapper"})
public interface StatisticsConfigMapper {
	/**
	 * 增加统计计算字段配置表数据实体类
	 * @param map 统计计算字段配置表数据实体
	 * @return
	 * @throws Exception
	 */
	public Integer addANALYSE_STATISTICS_CFIELD(Map<String, Object> map) throws Exception;

	/**
	 * 修改统计计算字段配置表数据实体类
	 * @param map 统计计算字段配置表数据实体
	 * @return
	 * @throws Exception
	 */
	public Integer editANALYSE_STATISTICS_CFIELD(Map<String, Object> map) throws Exception;

	/**
	 * 增加统计条件表数据实体类
	 * @param map 统计条件表数据实体
	 * @return
	 * @throws Exception
	 */
	public Integer addANALYSE_STATISTICS_WHERE(Map<String, Object> map) throws Exception;

	/**
	 * 修改统计条件表数据实体类
	 * @param map 统计条件表数据实体类
	 * @return
	 * @throws Exception
	 */
	public Integer editANALYSE_STATISTICS_WHERE(Map<String, Object> map) throws Exception;

	/**
	 * 增加统计计算字段关联配置表数据实体类
	 * @param map 统计计算字段关联配置表数据实体类
	 * @return
	 * @throws Exception
	 */
	public Integer addANALYSE_STATISTICS_CCONNECTION(Map<String, Object> map) throws Exception;

	/**
	 * 修改统计计算字段关联配置表数据实体类
	 * @param map 统计计算字段关联配置表数据实体类
	 * @return
	 * @throws Exception
	 */
	public Integer editANALYSE_STATISTICS_CCONNECTION(Map<String, Object> map) throws Exception;

	/**
	 * 增加统计条件表数据实体类
	 * @param map 统计条件表数据实体类
	 * @return
	 * @throws Exception
	 */
	public Integer addANALYSE_STATISTICS_CWHERE(Map<String, Object> map) throws Exception;

	/**
	 * 修改统计条件表数据实体类
	 * @param map 统计条件表数据实体类
	 * @return
	 * @throws Exception
	 */
	public Integer editANALYSE_STATISTICS_CWHERE(Map<String, Object> map) throws Exception;

}