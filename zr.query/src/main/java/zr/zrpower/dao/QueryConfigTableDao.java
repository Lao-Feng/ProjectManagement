package zr.zrpower.dao;

import zr.zrpower.domain.QueryConfigTable;

/**
 * 
 * 
 * @author nfzr
 * @email ftl@qq.com
 * @date 2019-07-26 01:10:50
 */
public interface QueryConfigTableDao extends BaseDao<QueryConfigTable> {
	
	/**
	 * 获取数据库最大编码
	 * @return
	 */
	String getMaxNo();
	
}
