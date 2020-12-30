package zr.zrpower.service;

import zr.zrpower.bean.QueryParameterBean;

import java.util.List;
import java.util.Map;

/**
 * 
 * 
 * @author nfzr
 * @email ftl@qq.com
 * @date 2019-07-30 22:31:25
 */
public interface QueryParameterService {
	
	QueryParameterBean queryObject(String userid)  throws Exception;
	
	List<QueryParameterBean> queryList(Map<String, Object> map)  throws Exception;
	
	int queryTotal(Map<String, Object> map)  throws Exception;
	
	void save(QueryParameterBean queryParameter)  throws Exception;
	
	void update(QueryParameterBean queryParameter) throws Exception;
	
	void delete(String userid) throws Exception;
	
	void deleteBatch(String[] userids) throws Exception;
}
