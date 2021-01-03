package com.yonglilian.service;


import com.yonglilian.bean.QueryConfigQueryfieldBean;

import java.util.List;
import java.util.Map;

/**
 * 
 * 
 * @author nfzr
 * @email ftl@qq.com
 * @date 2019-07-30 22:31:25
 */
public interface QueryConfigQueryfieldService {
	
	QueryConfigQueryfieldBean queryObject(String id)  throws Exception;
	
	List<QueryConfigQueryfieldBean> queryList(Map<String, Object> map)  throws Exception;
	
	int queryTotal(Map<String, Object> map)  throws Exception;
	
	void save(QueryConfigQueryfieldBean queryConfigQueryfield)  throws Exception;
	
	void update(QueryConfigQueryfieldBean queryConfigQueryfield) throws Exception;
	
	void delete(String fid) throws Exception;
	
	void deleteBatch(String[] ids) throws Exception;
	
	String getMaxNo() throws Exception;
	
	int saveBatch(List<String> fieldList,String fid) throws Exception;
	
	List<String> fieldList(Map<String, Object> map)  throws Exception;
}
