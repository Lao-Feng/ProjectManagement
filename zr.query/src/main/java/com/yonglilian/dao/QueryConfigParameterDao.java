package com.yonglilian.dao;

import com.yonglilian.bean.QueryConfigParameterBean;
import com.yonglilian.domain.QueryConfigParameter;
import zr.zrpower.dao.BaseDao;

import java.util.List;

/**
 * 
 * 数据库mybaits映射Mapper类
 * @author nfzr
 * @email ftl@qq.com
 * @date 2019-07-30 23:18:19
 */
public interface QueryConfigParameterDao extends BaseDao<QueryConfigParameter> {
	
	/**
	 * 获取数据库最大编码
	 * @return
	 */
	String getMaxNo();
	
	/**
	 * 批量保存数据
	 * @param list
	 * @return
	 */
	int saveBatchList(List<QueryConfigParameterBean> list);
}
