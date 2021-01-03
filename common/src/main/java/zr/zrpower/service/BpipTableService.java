package zr.zrpower.service;

import zr.zrpower.model.dbmanage.BPIP_TABLE;

import java.util.List;
import java.util.Map;

/**
 * 数据库表管理
 * @author nfzr
 *
 */
public interface BpipTableService {
	/**
	 * 获得详情
	 * @param tableId
	 * @return
	 */
	BPIP_TABLE queryObject(String tableId) throws Exception;

	/**
	 * 获取条件下的list
	 * @param map
	 * @return
	 */
	List<BPIP_TABLE> queryList(Map<String, Object> map) throws Exception;
	
	
	/**
	 * 获取条件下的select选择list
	 * @param map
	 * @return
	 */
	List<BPIP_TABLE> selectList(Map<String, Object> map) throws Exception;

	/**
	 * 保存数据
	 * @param model
	 */
	boolean save(BPIP_TABLE model) throws Exception;

	/**
	 * 修改数据
	 * @param model
	 */
	boolean update(BPIP_TABLE model) throws Exception;

	/**
	 * 删除数据
	 * @param tableId
	 * @param tableName
	 * @return
	 */
	boolean delete(String tableId,String tableName) throws Exception;	

	/**
	 * 通过数据库表id,生成配置文件下表的实体
	 * @param tableId
	 * @throws Exception
	 */
	void entity(String tableId) throws Exception;
}
