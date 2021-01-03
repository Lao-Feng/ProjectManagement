package com.yonglilian.service;

import com.yonglilian.bean.QueryTmpBean;

import java.util.List;
import java.util.Map;

/**
 * 
 * 
 * @author nfzr
 * @email ftl@qq.com
 * @date 2019-07-30 22:31:25
 */
public interface QueryTmpService {
	
	QueryTmpBean queryObject(String userid)  throws Exception;
	
	List<QueryTmpBean> queryList(Map<String, Object> map)  throws Exception;
	
	int queryTotal(Map<String, Object> map)  throws Exception;
	
	void save(QueryTmpBean queryTmp)  throws Exception;
	
	void update(QueryTmpBean queryTmp) throws Exception;
	
	void delete(String userid) throws Exception;
	
	void deleteBatch(String[] userids) throws Exception;
}
