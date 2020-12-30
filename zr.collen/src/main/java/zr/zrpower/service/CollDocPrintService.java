package zr.zrpower.service;

import zr.zrpower.bean.CollDocPrintBean;

import java.util.List;
import java.util.Map;

/**
 * 
 * 
 * @author nfzr
 * @email ftl@qq.com
 * @date 2019-08-04 15:20:55
 */
public interface CollDocPrintService {
	
	CollDocPrintBean queryObject(String id)  throws Exception;
	
	List<CollDocPrintBean> queryList(Map<String, Object> map)  throws Exception;
	
	int queryTotal(Map<String, Object> map)  throws Exception;
	
	void save(CollDocPrintBean collDocPrint)  throws Exception;
	
	void update(CollDocPrintBean collDocPrint) throws Exception;
	
	void delete(String id) throws Exception;
	
	void deleteBatch(String[] ids) throws Exception;
	
	String getMaxNo() throws Exception;
}
