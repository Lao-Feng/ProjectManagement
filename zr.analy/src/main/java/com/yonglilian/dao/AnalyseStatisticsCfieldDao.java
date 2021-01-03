package com.yonglilian.dao;

import com.yonglilian.domain.AnalyseStatisticsCfield;
import zr.zrpower.dao.BaseDao;

import java.util.List;

/**
 * 
 * 数据库mybaits映射Mapper类
 * @author nfzr
 * @email ftl@qq.com
 * @date 2019-08-01 01:49:08
 */
public interface AnalyseStatisticsCfieldDao extends BaseDao<AnalyseStatisticsCfield> {
	
	String getMaxNo();
	
	List<String> getIdList(Object fid);
	
}
