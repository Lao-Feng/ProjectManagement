package zr.zrpower.dao.mapper;

import tk.mybatis.spring.annotation.MapperScan;
import zr.zrpower.dao.BaseDao;
import zr.zrpower.model.dbmanage.BPIP_TABLESPACE;

/**
 * 表分类管理数据层
 * @author nfzr
 *
 */
@MapperScan({"zr.zrpower.dao.mapper"})
public interface BpipTablespaceMapper extends BaseDao<BPIP_TABLESPACE>{
	
	
	/**
	 * 生成最大的id
	 * @return
	 */
	String getMaxNo();
	
	/**
	 * 查询表空间下是否存在表
	 * @param id
	 * @return
	 */
	int queryTables(String id);
}
