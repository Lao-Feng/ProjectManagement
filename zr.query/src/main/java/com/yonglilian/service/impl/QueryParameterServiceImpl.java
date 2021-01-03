package com.yonglilian.service.impl;

import com.yonglilian.bean.QueryParameterBean;
import com.yonglilian.dao.QueryParameterDao;
import com.yonglilian.domain.QueryParameter;
import com.yonglilian.service.QueryParameterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zr.zrpower.common.db.DBEngine;
import zr.zrpower.common.util.SysPreperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Service("queryParameterService")
public class QueryParameterServiceImpl implements QueryParameterService {
    /** The QueryConfigServiceImpl Logger. */
	@SuppressWarnings("unused")
	private static final Logger LOGGER = LoggerFactory.getLogger(QueryParameterServiceImpl.class);
	private DBEngine dbEngine;
	private static int clients = 0;
	String DataBaseType = SysPreperty.getProperty().DataBaseType;// 数据库类型
	
	
	@Autowired
	private QueryParameterDao queryParameterDao;
	
	/**
	 * 构造方法
	 */
	public QueryParameterServiceImpl() {
		dbEngine = new DBEngine(SysPreperty.getProperty().MainDataSource, 
				SysPreperty.getProperty().IsConvert);
		dbEngine.initialize();
		if (clients < 1) {
			// clients something todo
		}
		clients = 1;
	}
	
	@Override
	public QueryParameterBean queryObject(String userid) throws Exception{
	QueryParameterBean queryParameterBean = null;
		QueryParameter queryParameter =  queryParameterDao.queryObject(userid);
			if( queryParameter != null ) {
			queryParameterBean = new QueryParameterBean();
			BeanUtils.copyProperties( queryParameter, queryParameterBean );
		}
		return queryParameterBean;
	}
	
	@Override
	public List<QueryParameterBean> queryList(Map<String, Object> map) throws Exception{
		List<QueryParameterBean> queryParameterBeans = new ArrayList<>();
		List<QueryParameter> list = queryParameterDao.queryList( map );
		for( QueryParameter queryParameter : list ) {
			QueryParameterBean queryParameterBean = new QueryParameterBean();
			BeanUtils.copyProperties( queryParameter, queryParameterBean );
			queryParameterBeans.add( queryParameterBean );
		}
		return queryParameterBeans;
	}
	
	@Override
	public int queryTotal(Map<String, Object> map) throws Exception{
		return queryParameterDao.queryTotal(map);
	}
	
	@Override
	public void save(QueryParameterBean queryParameterBean) throws Exception{
		QueryParameter queryParameter = new QueryParameter();
		BeanUtils.copyProperties( queryParameterBean, queryParameter );
		queryParameterDao.save(queryParameter);
	}
	
	@Override
	public void update(QueryParameterBean queryParameterBean) throws Exception{
	QueryParameter queryParameter = new QueryParameter();
		BeanUtils.copyProperties( queryParameterBean, queryParameter );
		queryParameterDao.update(queryParameter);
	}
	
	@Override
	public void delete(String userid) throws Exception{
		queryParameterDao.delete(userid);
	}
	
	@Override
	public void deleteBatch(String[] userids) throws Exception{
		queryParameterDao.deleteBatch(userids);
	}
	
}
