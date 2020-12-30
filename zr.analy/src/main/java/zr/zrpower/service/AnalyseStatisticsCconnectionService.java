package zr.zrpower.service;

import zr.zrpower.bean.AnalyseStatisticsCconnectionBean;

import java.util.List;
import java.util.Map;

/**
 * 
 * 
 * @author nfzr
 * @email ftl@qq.com
 * @date 2019-08-01 01:49:08
 */
public interface AnalyseStatisticsCconnectionService {
	
	AnalyseStatisticsCconnectionBean queryObject(String id)  throws Exception;
	
	List<AnalyseStatisticsCconnectionBean> queryList(Map<String, Object> map)  throws Exception;
	
	int queryTotal(Map<String, Object> map)  throws Exception;
	
	void save(AnalyseStatisticsCconnectionBean analyseStatisticsCconnection)  throws Exception;
	
	void update(AnalyseStatisticsCconnectionBean analyseStatisticsCconnection) throws Exception;
	
	void delete(String id) throws Exception;
	
	void deleteBatch(String[] ids) throws Exception;
	
	String getMaxNo() throws Exception;
	
	int saveBatchList(String fid,List<AnalyseStatisticsCconnectionBean> list) throws Exception;
}
