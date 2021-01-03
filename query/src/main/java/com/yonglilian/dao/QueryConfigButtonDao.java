package com.yonglilian.dao;

import com.yonglilian.domain.QueryConfigButton;
import zr.zrpower.dao.BaseDao;

/**
 * 
 * 
 * @author nfzr
 * @email ftl@qq.com
 * @date 2019-07-26 11:09:04
 */
public interface QueryConfigButtonDao extends BaseDao<QueryConfigButton> {
	
	/**
	 * 获取数据库最大编码
	 * @return
	 */
	String getMaxNo();
}
