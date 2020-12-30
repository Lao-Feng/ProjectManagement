package zr.zrpower.dao.mapper;

import org.apache.ibatis.annotations.Param;
import tk.mybatis.spring.annotation.MapperScan;

import java.util.List;
import java.util.Map;

/**
 * 数据库引擎数据层
 * @author lwk
 *
 */
@MapperScan({"zr.zrpower.dao.mapper"})
public interface DBServiceMapper {
	/**
	 * 执行查询SQL语句，返回Integer
	 * @param execSQL
	 * @return
	 * @throws Exception
	 */
	public Integer selectIntExecSQL(@Param("execSQL") String execSQL) throws Exception;

	/**
	 * 执行查询SQL语句，返回String
	 * @param execSQL
	 * @return
	 * @throws Exception
	 */
	public String selectStrExecSQL(@Param("execSQL") String execSQL) throws Exception;

	/**
	 * 执行查询SQL语句，返回Map<String, Object>数据
	 * @param execSQL
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> selectMapExecSQL(@Param("execSQL") String execSQL) throws Exception;

	/**
	 * 执行查询SQL语句，返回List<Map<String, Object>>数据
	 * @param execSQL
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> selectListMapExecSQL(@Param("execSQL") String execSQL) throws Exception;

	/**
	 * 执行增加SQL语句 
	 * @param execSQL
	 * @return
	 * @throws Exception
	 */
	public Integer insertExecSQL(@Param("execSQL") String execSQL) throws Exception;

	/**
	 * 执行修改SQL语句
	 * @param execSQL
	 * @return
	 * @throws Exception
	 */
	public Integer updateExecSQL(@Param("execSQL") String execSQL) throws Exception;

	/**
	 * 执行删除SQL语句
	 * @param execSQL
	 * @return
	 * @throws Exception
	 */
	public Integer deleteExecSQL(@Param("execSQL") String execSQL) throws Exception;

}