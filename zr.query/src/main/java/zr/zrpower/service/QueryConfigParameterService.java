package zr.zrpower.service;

import zr.zrpower.bean.QueryConfigParameterBean;

import java.util.List;
import java.util.Map;

/**
 * 
 * 
 * @author nfzr
 * @email ftl@qq.com
 * @date 2019-07-30 23:18:19
 */
public interface QueryConfigParameterService {
	
	QueryConfigParameterBean queryObject(String id)  throws Exception;
	
	List<QueryConfigParameterBean> queryList(Map<String, Object> map)  throws Exception;
	
	int queryTotal(Map<String, Object> map)  throws Exception;
	
	void save(QueryConfigParameterBean queryConfigParameter)  throws Exception;
	
	void update(QueryConfigParameterBean queryConfigParameter) throws Exception;
	
	void delete(String fid) throws Exception;
	
	void deleteBatch(String[] ids) throws Exception;
	
	String getMaxNo()  throws Exception;
	
	int saveBatch(List<QueryConfigParameterBean> list,String fid) throws Exception;
}
