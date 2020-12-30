package zr.zrpower.service;

import zr.zrpower.bean.QueryConfigTableBean;

import java.util.List;
import java.util.Map;

/**
 * 
 * 
 * @author nfzr
 * @email ftl@qq.com
 * @date 2019-07-26 01:10:50
 */
public interface QueryConfigTableService {
	
	QueryConfigTableBean queryObject(String id)  throws Exception;
	
	List<QueryConfigTableBean> queryList(Map<String, Object> map)  throws Exception;
	
	int queryTotal(Map<String, Object> map)  throws Exception;
	
	void save(QueryConfigTableBean queryConfigTable)  throws Exception;
	
	void update(QueryConfigTableBean queryConfigTable)  throws Exception;
	
	void delete(String id)  throws Exception;
	
	void deleteBatch(String[] ids)  throws Exception;
	
	String getMaxNo()  throws Exception;
	
	boolean copy(String id) throws Exception;
}
