package zr.zrpower.dao.mapper;

import tk.mybatis.spring.annotation.MapperScan;

import java.util.Map;

/**
 * 单位信息数据层
 * @author lwk
 *
 */
@MapperScan({"zr.zrpower.dao.mapper"})
public interface UnitMapper {
	/**
	 * 保存新单位信息
	 * @param map 新单位信息实体
	 * @return
	 * @throws Exception
	 */
	public Integer addUnit(Map<String, Object> map) throws Exception;

	/**
	 * 编辑单位信息
	 * @param map 单位信息实体
	 * @return
	 * @throws Exception
	 */
	public Integer editUnit(Map<String, Object> map) throws Exception;

}