package com.yonglilian.dao;

import com.yonglilian.domain.AnalyseStatisticsWhere;
import zr.zrpower.dao.BaseDao;

/**
 * 
 * 数据库mybaits映射Mapper类
 * @author nfzr
 * @email ftl@qq.com
 * @date 2019-08-01 01:49:08
 */
public interface AnalyseStatisticsWhereDao extends BaseDao<AnalyseStatisticsWhere> {
	
	String getMaxNo();
	
}
