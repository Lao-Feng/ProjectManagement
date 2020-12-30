package zr.zrpower.dao;

import zr.zrpower.domain.QueryConfigBrelation;

/**
 * 
 * 数据库mybaits映射Mapper类
 * @author nfzr
 * @email ftl@qq.com
 * @date 2019-07-30 22:31:25
 */
public interface QueryConfigBrelationDao extends BaseDao<QueryConfigBrelation> {
	
	/**
	 * 获取数据库最大编码
	 * @return
	 */
	String getMaxNo();
}
