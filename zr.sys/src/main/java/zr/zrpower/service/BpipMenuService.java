package zr.zrpower.service;

import zr.zrpower.model.BPIP_MENU;

import java.util.List;
import java.util.Map;

/**
 * 菜单管理
 * @author nfzr
 *
 */
public interface BpipMenuService {
	/**
	 * 获得详情
	 * @param menuNo
	 * @return
	 */
	BPIP_MENU queryObject(String menuNo) throws Exception;

	/**
	 * 获取条件下的list
	 * @param map
	 * @return
	 */
	List<BPIP_MENU> queryList(Map<String, Object> map) throws Exception;

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
	void save(BPIP_MENU model) throws Exception;

	/**
	 * 修改数据
	 * @param model
	 */
	void update(BPIP_MENU model) throws Exception;

	/**
	 * 删除数据
	 * @param menuNo
	 * @return
	 */
	boolean delete(String menuNo) throws Exception;
	
	/**
	 * 删除多条数据
	 * @param menuNos
	 * @return
	 */
	boolean deleteBatch(String[] menuNos) throws Exception;

}
