package zr.zrpower.service;

import zr.zrpower.bean.QueryConfigBrelationBean;

import java.util.List;
import java.util.Map;

/**
 * 
 * 
 * @author nfzr
 * @email ftl@qq.com
 * @date 2019-07-30 22:31:25
 */
public interface QueryConfigBrelationService {
	
	QueryConfigBrelationBean queryObject(String id)  throws Exception;
	
	List<QueryConfigBrelationBean> queryList(Map<String, Object> map)  throws Exception;
	
	int queryTotal(Map<String, Object> map)  throws Exception;
	
	void save(QueryConfigBrelationBean queryConfigBrelation)  throws Exception;
	
	void update(QueryConfigBrelationBean queryConfigBrelation) throws Exception;
	
	void delete(String id) throws Exception;
	
	void deleteBatch(String[] ids) throws Exception;
	
	String getMaxNo() throws Exception;
}
