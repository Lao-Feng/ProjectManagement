package com.yonglilian.service;

import com.yonglilian.bean.QueryConfigInitBean;

import java.util.List;
import java.util.Map;

/**
 * 
 * 
 * @author nfzr
 * @email ftl@qq.com
 * @date 2019-07-30 22:31:25
 */
public interface QueryConfigInitService {
	
	QueryConfigInitBean queryObject(String id)  throws Exception;
	
	List<QueryConfigInitBean> queryList(Map<String, Object> map)  throws Exception;
	
	int queryTotal(Map<String, Object> map)  throws Exception;
	
	void save(QueryConfigInitBean queryConfigInit)  throws Exception;
	
	void update(QueryConfigInitBean queryConfigInit) throws Exception;
	
	void delete(String fid) throws Exception;
	
	void deleteBatch(String[] ids) throws Exception;
	
	String getMaxNo() throws Exception;
	
	int saveBatch(List<QueryConfigInitBean> list,String fid) throws Exception;
}
