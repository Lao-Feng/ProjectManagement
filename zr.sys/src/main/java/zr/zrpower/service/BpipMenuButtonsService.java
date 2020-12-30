package zr.zrpower.service;

import zr.zrpower.model.BpipMenuButtons;

import java.util.List;
import java.util.Map;

/**
 * 系统菜单按钮表
 * 
 * @author nfzr
 * @email ftl@qq.com
 * @date 2019-10-14 12:18:01
 */
public interface BpipMenuButtonsService {
	
	BpipMenuButtons queryObject(Long id)  throws Exception;
	
	List<BpipMenuButtons> queryList(Map<String, Object> map)  throws Exception;
	
	int queryTotal(Map<String, Object> map)  throws Exception;
	
	void save(BpipMenuButtons bpipMenuButtons)  throws Exception;
	
	void update(BpipMenuButtons bpipMenuButtons) throws Exception;
	
	void delete(Long id) throws Exception;
	
	void deleteBatch(Long[] ids) throws Exception;
}
