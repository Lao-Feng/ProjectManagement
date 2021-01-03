package com.yonglilian.service.impl;

import com.yonglilian.bean.QueryTmpBean;
import com.yonglilian.dao.QueryTmpDao;
import com.yonglilian.domain.QueryTmp;
import com.yonglilian.service.QueryTmpService;
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


@Service("queryTmpService")
public class QueryTmpServiceImpl implements QueryTmpService {
    /** The QueryConfigServiceImpl Logger. */
	@SuppressWarnings("unused")
	private static final Logger LOGGER = LoggerFactory.getLogger(QueryTmpServiceImpl.class);
	private DBEngine dbEngine;
	private static int clients = 0;
	String DataBaseType = SysPreperty.getProperty().DataBaseType;// 数据库类型
	
	
	@Autowired
	private QueryTmpDao queryTmpDao;
	
	/**
	 * 构造方法
	 */
	public QueryTmpServiceImpl() {
		dbEngine = new DBEngine(SysPreperty.getProperty().MainDataSource, 
				SysPreperty.getProperty().IsConvert);
		dbEngine.initialize();
		if (clients < 1) {
			// clients something todo
		}
		clients = 1;
	}
	
	@Override
	public QueryTmpBean queryObject(String userid) throws Exception{
	QueryTmpBean queryTmpBean = null;
		QueryTmp queryTmp =  queryTmpDao.queryObject(userid);
			if( queryTmp != null ) {
			queryTmpBean = new QueryTmpBean();
			BeanUtils.copyProperties( queryTmp, queryTmpBean );
		}
		return queryTmpBean;
	}
	
	@Override
	public List<QueryTmpBean> queryList(Map<String, Object> map) throws Exception{
		List<QueryTmpBean> queryTmpBeans = new ArrayList<>();
		List<QueryTmp> list = queryTmpDao.queryList( map );
		for( QueryTmp queryTmp : list ) {
			QueryTmpBean queryTmpBean = new QueryTmpBean();
			BeanUtils.copyProperties( queryTmp, queryTmpBean );
			queryTmpBeans.add( queryTmpBean );
		}
		return queryTmpBeans;
	}
	
	@Override
	public int queryTotal(Map<String, Object> map) throws Exception{
		return queryTmpDao.queryTotal(map);
	}
	
	@Override
	public void save(QueryTmpBean queryTmpBean) throws Exception{
		QueryTmp queryTmp = new QueryTmp();
		BeanUtils.copyProperties( queryTmpBean, queryTmp );
		queryTmpDao.save(queryTmp);
	}
	
	@Override
	public void update(QueryTmpBean queryTmpBean) throws Exception{
	QueryTmp queryTmp = new QueryTmp();
		BeanUtils.copyProperties( queryTmpBean, queryTmp );
		queryTmpDao.update(queryTmp);
	}
	
	@Override
	public void delete(String userid) throws Exception{
		queryTmpDao.delete(userid);
	}
	
	@Override
	public void deleteBatch(String[] userids) throws Exception{
		queryTmpDao.deleteBatch(userids);
	}
	
}
