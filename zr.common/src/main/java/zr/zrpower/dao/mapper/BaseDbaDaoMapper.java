package zr.zrpower.dao.mapper;

import tk.mybatis.spring.annotation.MapperScan;
import zr.zrpower.dao.BaseDbaDao;

/**
 * 数据库操作数据层
 * @author nfzr
 *
 */
@MapperScan({"zr.zrpower.dao.mapper"})
public interface BaseDbaDaoMapper extends BaseDbaDao {
	
}
