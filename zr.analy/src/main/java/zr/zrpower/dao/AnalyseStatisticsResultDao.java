package zr.zrpower.dao;

import zr.zrpower.domain.AnalyseStatisticsResult;

/**
 * 
 * 数据库mybaits映射Mapper类
 * @author nfzr
 * @email ftl@qq.com
 * @date 2019-08-01 01:49:08
 */
public interface AnalyseStatisticsResultDao extends BaseDao<AnalyseStatisticsResult> {
	
	String getMaxNo();
	
}
