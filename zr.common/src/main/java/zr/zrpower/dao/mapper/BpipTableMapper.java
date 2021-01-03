package zr.zrpower.dao.mapper;

import tk.mybatis.spring.annotation.MapperScan;
import zr.zrpower.dao.BaseDao;
import zr.zrpower.model.dbmanage.BPIP_TABLE;

import java.util.List;
import java.util.Map;

/**
 * 表分管理数据层
 * @author nfzr
 *
 */
@MapperScan({"com.yonglilian.dao.mapper"})
public interface BpipTableMapper extends BaseDao<BPIP_TABLE> {
	

	/**
	 * 通过表id,删除表字段数据表
	 * @param tableId
	 * @return
	 */
	int deleteFields(String tableId);
	
	/**
	 * 通过表英文名称，删除数据库表
	 * @param tableName
	 * @return
	 */
	int dropTable(String tableName);
	
	/**
	 * 查询数据库表最大的tableId
	 * @return
	 */
	String getMaxNo();
	
	/**
	 * 通过表英文名称查询数据库是否已经创建表
	 * @param tableName
	 * @return
	 */
	int isTable(String tableName);
	
	
	List<BPIP_TABLE> selectList(Map<String,Object> map);
	
}
