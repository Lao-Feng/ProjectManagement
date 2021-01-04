package com.yonglilian.dao;

import com.yonglilian.domain.CollDocPrint;

/**
 * 
 * 数据库mybaits映射Mapper类
 * @author nfzr
 * @email ftl@qq.com
 * @date 2019-08-04 15:20:55
 */
public interface CollDocPrintDao extends BaseDao<CollDocPrint> {
	
	String getMaxNo();
}
