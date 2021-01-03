package zr.zrpower.service;

import zr.zrpower.model.dbmanage.BPIP_FIELD;

import java.util.List;
import java.util.Map;

/**
 * 数据库表字典管理
 * @author nfzr
 *
 */
public interface BpipTableFieldService {
	/**
	 * 获得详情
	 * @param fieldId
	 * @return
	 */
	BPIP_FIELD queryObject(String fieldId) throws Exception;

	/**
	 * 获取条件下的list
	 * @param map
	 * @return
	 */
	List<BPIP_FIELD> queryList(Map<String, Object> map) throws Exception;
	
	/**
	 * 获取条件下的select选择list
	 * @param map
	 * @return
	 */
	List<BPIP_FIELD> selectList(Map<String, Object> map) throws Exception;


	/**
	 * 保存数据
	 * @param model
	 */
	boolean save(BPIP_FIELD model) throws Exception;

	/**
	 * 修改数据
	 * @param model
	 */
	boolean update(BPIP_FIELD model) throws Exception;

	/**
	 * 删除数据
	 * @param model
	 * @return
	 */
	boolean delete(BPIP_FIELD model) throws Exception;
	
	/**
	 * 返回数据库类型
	 * @return
	 * @throws Exception
	 */
	int dbaType() throws Exception;
	

}
