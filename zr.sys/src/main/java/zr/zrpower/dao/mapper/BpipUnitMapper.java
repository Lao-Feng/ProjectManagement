package zr.zrpower.dao.mapper;


import tk.mybatis.spring.annotation.MapperScan;
import zr.zrpower.dao.BaseDao;
import zr.zrpower.model.BPIP_UNIT;

import java.util.Map;

/**
 * 单位机构dao
 * @author nfzr
 *
 */
@MapperScan({"zr.zrpower.dao.mapper"})
public interface BpipUnitMapper extends BaseDao<BPIP_UNIT>{
	
	/**
	 * 获取最大编码
	 * @param map
	 * @return
	 */
	String getMaxNo(Map<String,Object> map);
	
	/**
	 * 判断是否有下一级
	 * @param unitId
	 * @return
	 */
	int nextCount(String unitId);
	
	/**
	 * 判断当前单位下是否有活动用户
	 * @param unitId
	 * @return
	 */
	int nextUsers(String unitId);
	
	
	/**
	 * 查询最大编号
	 * @param unitId
	 * @return
	 */
	String getMaxNo(String unitId);  
	
}