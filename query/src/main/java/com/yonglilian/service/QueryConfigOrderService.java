package com.yonglilian.service;

import com.yonglilian.bean.QueryConfigOrderBean;

import java.util.List;
import java.util.Map;

/**
 * 
 * 
 * @author nfzr
 * @email ftl@qq.com
 * @date 2019-07-30 22:31:25
 */
public interface QueryConfigOrderService {
	
	QueryConfigOrderBean queryObject(String id)  throws Exception;
	
	List<QueryConfigOrderBean> queryList(Map<String, Object> map)  throws Exception;
	
	int queryTotal(Map<String, Object> map)  throws Exception;
	
	void save(QueryConfigOrderBean queryConfigOrder)  throws Exception;
	
	void update(QueryConfigOrderBean queryConfigOrder) throws Exception;
	
	void delete(String fid) throws Exception;
	
	void deleteBatch(String[] ids) throws Exception;
	
	String getMaxNo() throws Exception;
	
	int saveBatch(List<QueryConfigOrderBean> list,String fid) throws Exception;
}
