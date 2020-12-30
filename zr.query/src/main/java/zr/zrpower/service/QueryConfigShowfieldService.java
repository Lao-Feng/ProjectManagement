package zr.zrpower.service;

import zr.zrpower.bean.QueryConfigShowfieldBean;

import java.util.List;
import java.util.Map;

/**
 * 
 * 
 * @author nfzr
 * @email ftl@qq.com
 * @date 2019-07-30 22:31:25
 */
public interface QueryConfigShowfieldService {
	
	QueryConfigShowfieldBean queryObject(String id)  throws Exception;
	
	/**
	 * 
	 * @param map
	 * @param boolean  true,实现1=true,0-false
	 * @return
	 * @throws Exception
	 */
	List<QueryConfigShowfieldBean> queryList(Map<String, Object> map,boolean boo)  throws Exception;
	
	int queryTotal(Map<String, Object> map)  throws Exception;
	
	void save(QueryConfigShowfieldBean queryConfigShowfield)  throws Exception;
	
	void update(QueryConfigShowfieldBean queryConfigShowfield) throws Exception;
	
	void delete(String fid) throws Exception;
	
	void deleteBatch(String[] ids) throws Exception;
	
	String getMaxNo() throws Exception;
	
	int saveBatch(List<QueryConfigShowfieldBean> list,String fid) throws Exception;
}
