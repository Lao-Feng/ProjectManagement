package zr.zrpower.service;

import zr.zrpower.bean.AnalyseStatisticsCfieldBean;

import java.util.List;
import java.util.Map;

/**
 * 
 * 
 * @author nfzr
 * @email ftl@qq.com
 * @date 2019-08-01 01:49:08
 */
public interface AnalyseStatisticsCfieldService {
	
	AnalyseStatisticsCfieldBean queryObject(String id)  throws Exception;
	
	List<AnalyseStatisticsCfieldBean> queryList(Map<String, Object> map)  throws Exception;
	
	int queryTotal(Map<String, Object> map)  throws Exception;
	
	void save(AnalyseStatisticsCfieldBean analyseStatisticsCfield)  throws Exception;
	
	void update(AnalyseStatisticsCfieldBean analyseStatisticsCfield) throws Exception;
	
	void delete(String id) throws Exception;
	
	void deleteBatch(String[] ids) throws Exception;
	
    String getMaxNo() throws Exception;
	
	int saveBatchList(List<AnalyseStatisticsCfieldBean> list) throws Exception;
	
	int updateBatchList(List<AnalyseStatisticsCfieldBean> list) throws Exception;
}
