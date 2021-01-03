package com.yonglilian.service;


import com.yonglilian.bean.AnalyseStatisticsCwhereBean;

import java.util.List;
import java.util.Map;

/**
 * 
 * 
 * @author nfzr
 * @email ftl@qq.com
 * @date 2019-08-01 01:49:08
 */
public interface AnalyseStatisticsCwhereService {
	
	AnalyseStatisticsCwhereBean queryObject(String id)  throws Exception;
	
	List<AnalyseStatisticsCwhereBean> queryList(Map<String, Object> map)  throws Exception;
	
	int queryTotal(Map<String, Object> map)  throws Exception;
	
	void save(AnalyseStatisticsCwhereBean analyseStatisticsCwhere)  throws Exception;
	
	void update(AnalyseStatisticsCwhereBean analyseStatisticsCwhere) throws Exception;
	
	void delete(String id) throws Exception;
	
	void deleteBatch(String[] ids) throws Exception;
	
    String getMaxNo() throws Exception;
	
	int saveBatchList(String fid,List<AnalyseStatisticsCwhereBean> list) throws Exception;
}
