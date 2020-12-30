package zr.zrpower.service;

import zr.zrpower.bean.QueryConfigConnectionBean;

import java.util.List;
import java.util.Map;

/**
 * 
 * 
 * @author nfzr
 * @email ftl@qq.com
 * @date 2019-07-30 22:31:25
 */
public interface QueryConfigConnectionService {
	
	QueryConfigConnectionBean queryObject(String id)  throws Exception;
	
	List<QueryConfigConnectionBean> queryList(Map<String, Object> map)  throws Exception;
	
	int queryTotal(Map<String, Object> map)  throws Exception;
	
	void save(QueryConfigConnectionBean queryConfigConnection)  throws Exception;
	
	void update(QueryConfigConnectionBean queryConfigConnection) throws Exception;
	
	void delete(String fid) throws Exception;
	
	void deleteBatch(String[] ids) throws Exception;
	
	String getMaxNo() throws Exception;
	
	int saveBatch(List<QueryConfigConnectionBean> list,String fid) throws Exception;
}
