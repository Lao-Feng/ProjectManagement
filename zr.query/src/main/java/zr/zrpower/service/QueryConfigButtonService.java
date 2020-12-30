package zr.zrpower.service;

import zr.zrpower.bean.QueryConfigButtonBean;

import java.util.List;
import java.util.Map;

/**
 * 
 * 
 * @author nfzr
 * @email ftl@qq.com
 * @date 2019-07-26 11:09:04
 */
public interface QueryConfigButtonService {
	
	QueryConfigButtonBean queryObject(String id) throws Exception;
	
	List<QueryConfigButtonBean> queryList(Map<String, Object> map) throws Exception;
	
	int queryTotal(Map<String, Object> map) throws Exception;
	
	void save(QueryConfigButtonBean queryConfigButton) throws Exception;
	
	void update(QueryConfigButtonBean queryConfigButton) throws Exception;
	
	void delete(String id) throws Exception;
	
	void deleteBatch(String[] ids) throws Exception;
	
	String getMaxNo() throws Exception;
}
