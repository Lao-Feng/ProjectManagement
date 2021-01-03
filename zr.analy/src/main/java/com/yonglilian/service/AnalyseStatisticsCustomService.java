package com.yonglilian.service;

import com.yonglilian.bean.AnalyseStatisticsCconnectionBean;
import com.yonglilian.bean.AnalyseStatisticsCustomBean;

import java.util.List;
import java.util.Map;

/**
 * 
 * 
 * @author nfzr
 * @email ftl@qq.com
 * @date 2019-08-01 01:49:08
 */
public interface AnalyseStatisticsCustomService {
	
	AnalyseStatisticsCustomBean queryObject(String id)  throws Exception;
	
	List<AnalyseStatisticsCustomBean> queryList(Map<String, Object> map)  throws Exception;
	
	int queryTotal(Map<String, Object> map)  throws Exception;
	
	void save(AnalyseStatisticsCustomBean analyseStatisticsCustom)  throws Exception;
	
	void update(AnalyseStatisticsCustomBean analyseStatisticsCustom) throws Exception;
	
	void delete(String id) throws Exception;
	
	void deleteBatch(String[] ids) throws Exception;
	
    String getMaxNo() throws Exception;
	
	int saveBatchList(List<AnalyseStatisticsCconnectionBean> list) throws Exception;
}
