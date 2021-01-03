package com.yonglilian.dao;

import com.yonglilian.domain.CollDocConfig;
import zr.zrpower.dao.BaseDao;
import zr.zrpower.model.dbmanage.BPIP_FIELD;
import zr.zrpower.model.dbmanage.BPIP_TABLE;

import java.util.List;
import java.util.Map;

/**
 * 
 * 数据库mybaits映射Mapper类
 * @author nfzr
 * @email ftl@qq.com
 * @date 2019-08-04 15:20:55
 */
public interface CollDocConfigDao extends BaseDao<CollDocConfig> {
	
	String getMaxNo();
	
	BPIP_TABLE getTable(Map<String,Object> map);
	
	List<BPIP_FIELD> getTableFields(Map<String,Object> map);
}
