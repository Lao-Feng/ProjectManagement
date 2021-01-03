package com.yonglilian.service;

import com.yonglilian.bean.AnalyseStatisticsCconnectionBean;
import com.yonglilian.bean.AnalyseStatisticsWhereBean;

import java.util.List;
import java.util.Map;

/**
 * 
 * 
 * @author nfzr
 * @email ftl@qq.com
 * @date 2019-08-01 01:49:08
 */
public interface AnalyseStatisticsWhereService {
	
	AnalyseStatisticsWhereBean queryObject(String id)  throws Exception;
	
	List<AnalyseStatisticsWhereBean> queryList(Map<String, Object> map)  throws Exception;
	
	int queryTotal(Map<String, Object> map)  throws Exception;
	
	void save(AnalyseStatisticsWhereBean analyseStatisticsWhere)  throws Exception;
	
	void update(AnalyseStatisticsWhereBean analyseStatisticsWhere) throws Exception;
	
	void delete(String id) throws Exception;
	
	void deleteBatch(String[] ids) throws Exception;
	
    String getMaxNo() throws Exception;
	
	int saveBatchList(List<AnalyseStatisticsCconnectionBean> list) throws Exception;
}
