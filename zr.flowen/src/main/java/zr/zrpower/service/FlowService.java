package zr.zrpower.service;

import zr.zrpower.model.CODE_YWXT;
import zr.zrpower.model.dbmanage.BPIP_TABLE;

import java.util.List;
import java.util.Map;

/**
 * 流程引擎服务层
 * @author lwk
 *
 */
public interface FlowService {
	/**
	 * 手机流程方法 List list 要显示的字段
	 */
	public Map<String, Object> getListValues(String tabName, String whereStr, List<?> listMap, String tabId) throws Exception;

	public String getCodeValues(String tabId, String fieldname) throws Exception;

	public String getCodeValues2(String tabname, String code) throws Exception;

	/**
	 * 获取字典列表
	 * @param CODE_YWXT
	 * @return String[][]
	 */
	public CODE_YWXT[] getCodeList(String strwhere1) throws Exception;

	/**
	 * 读出表的主键
	 * 
	 * @param BPIP_TABLE
	 *            String 首页设置的标识SETUPNO
	 * @return BPIP_INDEX_SETUP
	 */
	public BPIP_TABLE getTabKey(String sql) throws Exception;

}
