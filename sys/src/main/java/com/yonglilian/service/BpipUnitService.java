package com.yonglilian.service;

import com.yonglilian.model.BPIP_UNIT;

import java.util.List;
import java.util.Map;

/**
 * 
 * 单位/机构管理
 * @author nfzr
 *
 */
public interface BpipUnitService {
	/**
	 * 获得详情
	 * @param unitId
	 * @return
	 */
	BPIP_UNIT queryObject(String unitId) throws Exception;

	/**
	 * 获取条件下的list
	 * @param map
	 * @return
	 */
	List<BPIP_UNIT> queryList(Map<String, Object> map) throws Exception;

	/**
	 * 获取条件下的list总数
	 * @param map
	 * @return
	 */
	int queryTotal(Map<String, Object> map) throws Exception;

	/**
	 * 保存数据
	 * @param model
	 */
	void save(BPIP_UNIT model) throws Exception;

	/**
	 * 修改数据
	 * @param model
	 */
	void update(BPIP_UNIT model) throws Exception;

	/**
	 * 删除数据
	 * @param unitId
	 * @return
	 */
	boolean delete(String unitId) throws Exception;
	
	/**
	 * 删除多条数据
	 * @param unitIds
	 * @return
	 */
	boolean deleteBatch(String[] unitIds) throws Exception;
	
	/**
	 * 获取系统最大单位编码
	 * @param unitId
	 * @param type 0，同级别，1下级
	 * @return
	 */
	String getMaxNo(String unitId,int type) throws Exception;

}
