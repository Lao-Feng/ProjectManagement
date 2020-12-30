package zr.zrpower.dao;

import zr.zrpower.domain.AnalyseStatisticsMain;

/**
 * 
 * 数据库mybaits映射Mapper类
 * @author nfzr
 * @email ftl@qq.com
 * @date 2019-08-01 01:49:08
 */
public interface AnalyseStatisticsMainDao extends BaseDao<AnalyseStatisticsMain> {
	
	String getMaxNo();
	
}
