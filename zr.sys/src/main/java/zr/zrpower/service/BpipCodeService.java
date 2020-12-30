package zr.zrpower.service;

import zr.zrpower.model.BPIP_TABLE;
import zr.zrpower.model.CODE_YWXT;

import java.util.List;
import java.util.Map;

/**
 * 
 * 字典管理
 * @author nfzr
 *
 */
public interface BpipCodeService {
	
	/**
	 * 获取数据库表
	 * @param map
	 * @return
	 */
	List<BPIP_TABLE> getTables(Map<String,Object> map) throws Exception;
	
	/**
	 * 获得详情
	 * @param table
	 * @param code
	 * @return
	 */
	CODE_YWXT queryObject(Map<String,Object> map) throws Exception;

	/**
	 * 获取条件下的list
	 * @param map
	 * @return
	 */
	List<CODE_YWXT> queryList(Map<String, Object> map) throws Exception;

	/**
	 * 保存数据
	 * @param model
	 */
	void save(CODE_YWXT model) throws Exception;

	/**
	 * 修改数据
	 * @param model
	 */
	void update(CODE_YWXT model) throws Exception;

	/**
	 * 删除数据
	 * @param map {table,code}
	 * @return
	 */
	boolean delete(Map<String, Object> map) throws Exception;
	
	/**
	 * 删除多条数据
	 * @param codes
	 * @return
	 */
	boolean deleteBatch(String[] codes) throws Exception;
	

}
