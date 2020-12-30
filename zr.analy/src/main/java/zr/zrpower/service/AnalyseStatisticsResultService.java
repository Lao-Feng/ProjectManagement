package zr.zrpower.service;

import zr.zrpower.bean.AnalyseStatisticsCconnectionBean;
import zr.zrpower.bean.AnalyseStatisticsResultBean;

import java.util.List;
import java.util.Map;

/**
 * 
 * 
 * @author nfzr
 * @email ftl@qq.com
 * @date 2019-08-01 01:49:08
 */
public interface AnalyseStatisticsResultService {
	
	AnalyseStatisticsResultBean queryObject(String id)  throws Exception;
	
	List<AnalyseStatisticsResultBean> queryList(Map<String, Object> map)  throws Exception;
	
	int queryTotal(Map<String, Object> map)  throws Exception;
	
	void save(AnalyseStatisticsResultBean analyseStatisticsResult)  throws Exception;
	
	void update(AnalyseStatisticsResultBean analyseStatisticsResult) throws Exception;
	
	void delete(String id) throws Exception;
	
	void deleteBatch(String[] ids) throws Exception;
	
    String getMaxNo() throws Exception;
	
	int saveBatchList(List<AnalyseStatisticsCconnectionBean> list) throws Exception;
}
