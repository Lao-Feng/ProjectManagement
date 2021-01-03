package zr.zrpower.dao.mapper;

import tk.mybatis.spring.annotation.MapperScan;

import java.util.List;
import java.util.Map;

/**
 * 代码生成器DAO
 * @author nfzr
 *
 */
@MapperScan({"com.yonglilian.dao.mapper"})
public interface DbaSysGeneratorMapper {

	List<Map<String, Object>> queryList( Map<String, Object> map );

	int queryTotal( Map<String, Object> map );

	Map<String, String> queryTable( String tableName );

	List<Map<String, String>> queryColumns( String tableName );
}
