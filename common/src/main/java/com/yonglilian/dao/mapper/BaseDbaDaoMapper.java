package com.yonglilian.dao.mapper;

import com.yonglilian.dao.BaseDbaDao;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * 数据库操作数据层
 * @author nfzr
 *
 */
@MapperScan({"zr.zrpower.dao.mapper"})
public interface BaseDbaDaoMapper extends BaseDbaDao {
	
}
