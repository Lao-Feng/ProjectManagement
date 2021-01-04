package com.yonglilian.service;

import com.yonglilian.model.dbmanage.BPIP_TABLESPACE;

import java.util.List;
import java.util.Map;

/**
 * 表空间管理
 * @author nfzr
 *
 */
public interface BpipTablespaceService {
	/**
	 * 获得详情
	 * @param id
	 * @return
	 */
	BPIP_TABLESPACE queryObject(String id) throws Exception;

	/**
	 * 获取条件下的list
	 * @param map
	 * @return
	 */
	List<BPIP_TABLESPACE> queryList(Map<String, Object> map) throws Exception;


	/**
	 * 保存数据
	 * @param model
	 */
	void save(BPIP_TABLESPACE model) throws Exception;

	/**
	 * 修改数据
	 * @param model
	 */
	void update(BPIP_TABLESPACE model) throws Exception;

	/**
	 * 删除数据
	 * @param id
	 * @return
	 */
	void delete(String id) throws Exception;
	

}
