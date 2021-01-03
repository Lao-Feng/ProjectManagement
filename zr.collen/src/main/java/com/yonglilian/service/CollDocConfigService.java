package com.yonglilian.service;

import com.yonglilian.bean.CollDocConfigBean;
import com.yonglilian.bean.CollTempBean;

import java.util.List;
import java.util.Map;

/**
 * 
 * 
 * @author nfzr
 * @email ftl@qq.com
 * @date 2019-08-04 15:20:55
 */
public interface CollDocConfigService {
	
	CollDocConfigBean queryObject(String id)  throws Exception;
	
	List<CollDocConfigBean> queryList(Map<String, Object> map)  throws Exception;
	
	int queryTotal(Map<String, Object> map)  throws Exception;
	
	void save(CollDocConfigBean collDocConfig)  throws Exception;
	
	void update(CollDocConfigBean collDocConfig) throws Exception;
	
	void delete(String id) throws Exception;
	
	void deleteBatch(String[] ids) throws Exception;
	
	String getMaxNo() throws Exception;
	
	/**
	 * 生成表单模板
	 * @param collTempBean
	 * @throws Exception
	 */
	void tempform(CollTempBean collTempBean)  throws Exception;
}
