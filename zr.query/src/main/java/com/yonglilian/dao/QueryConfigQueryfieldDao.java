package com.yonglilian.dao;

import com.yonglilian.bean.QueryConfigQueryfieldBean;
import com.yonglilian.domain.QueryConfigQueryfield;
import zr.zrpower.dao.BaseDao;

import java.util.List;

/**
 * 
 * 数据库mybaits映射Mapper类
 * @author nfzr
 * @email ftl@qq.com
 * @date 2019-07-30 22:31:25
 */
public interface QueryConfigQueryfieldDao extends BaseDao<QueryConfigQueryfield> {
	
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
	int saveBatchList(List<QueryConfigQueryfieldBean> list);
}
