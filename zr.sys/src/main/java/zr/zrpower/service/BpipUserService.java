package zr.zrpower.service;

import zr.zrpower.model.BPIP_USER;

import java.util.List;
import java.util.Map;

/**
 * 
 * 单位/机构管理
 * @author nfzr
 *
 */
public interface BpipUserService {
	/**
	 * 获得详情
	 * @param userId
	 * @return
	 */
	BPIP_USER queryObject(String userId) throws Exception;

	/**
	 * 获取条件下的list
	 * @param map
	 * @return
	 */
	List<BPIP_USER> queryList(Map<String, Object> map) throws Exception;

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
	boolean save(BPIP_USER model) throws Exception;

	/**
	 * 修改数据
	 * @param model
	 */
	boolean update(BPIP_USER model) throws Exception;

	/**
	 * 删除数据
	 * @param userId
	 * @return
	 */
	boolean delete(String userId) throws Exception;
	
	/**
	 * 删除多条数据
	 * @param userIds
	 * @return
	 */
	boolean deleteBatch(String[] userIds) throws Exception;
	
	/**
	 * 生成单位下最大用户编码
	 * @param unitId
	 * @return
	 */
	String getMaxNo(String unitId) throws Exception;
	
	

}
