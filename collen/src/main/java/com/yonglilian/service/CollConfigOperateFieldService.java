package com.yonglilian.service;

import com.yonglilian.bean.CollConfigOperateFieldBean;

import java.util.List;
import java.util.Map;

/**
 * 
 * 
 * @author nfzr
 * @email ftl@qq.com
 * @date 2019-08-04 15:20:55
 */
public interface CollConfigOperateFieldService {
	
	CollConfigOperateFieldBean queryObject(String id)  throws Exception;
	
	List<CollConfigOperateFieldBean> queryList(Map<String, Object> map)  throws Exception;
	
	int queryTotal(Map<String, Object> map)  throws Exception;
	
	void save(CollConfigOperateFieldBean collConfigOperateField)  throws Exception;
	
	void update(CollConfigOperateFieldBean collConfigOperateField) throws Exception;
	
	void delete(String id) throws Exception;
	
	void deleteBatch(String[] ids) throws Exception;
	
	String getMaxNo() throws Exception;
	
	int saveList(List<CollConfigOperateFieldBean> list) throws Exception;
}
