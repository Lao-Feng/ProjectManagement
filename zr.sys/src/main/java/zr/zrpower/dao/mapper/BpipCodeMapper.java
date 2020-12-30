package zr.zrpower.dao.mapper;


import tk.mybatis.spring.annotation.MapperScan;
import zr.zrpower.dao.BaseDao;
import zr.zrpower.model.BPIP_TABLE;
import zr.zrpower.model.CODE_YWXT;

import java.util.List;
import java.util.Map;

/**
 * 字典数据操作dao
 * @author nfzr
 *
 */
@MapperScan({"zr.zrpower.dao.mapper"})
public interface BpipCodeMapper extends BaseDao<CODE_YWXT>{
	
	/**
	 * 查询数据库的表
	 * @param map
	 * @return
	 */
	List<BPIP_TABLE> getTables(Map<String,Object> map);
	
	/**
	 * 查询详情
	 * @param map {table,code}
	 * @return
	 */
	CODE_YWXT queryObject(Map<String,Object> map);
	
	
}