package com.yonglilian.service.impl;

import com.yonglilian.bean.QueryConfigBrelationBean;
import com.yonglilian.dao.QueryConfigBrelationDao;
import com.yonglilian.domain.QueryConfigBrelation;
import com.yonglilian.service.QueryConfigBrelationService;
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


@Service("queryConfigBrelationService")
public class QueryConfigBrelationServiceImpl implements QueryConfigBrelationService {
    /** The QueryConfigServiceImpl Logger. */
	@SuppressWarnings("unused")
	private static final Logger LOGGER = LoggerFactory.getLogger(QueryConfigBrelationServiceImpl.class);
	private DBEngine dbEngine;
	private static int clients = 0;
	String DataBaseType = SysPreperty.getProperty().DataBaseType;// 数据库类型
	
	
	@Autowired
	private QueryConfigBrelationDao queryConfigBrelationDao;
	
	/**
	 * 构造方法
	 */
	public QueryConfigBrelationServiceImpl() {
		dbEngine = new DBEngine(SysPreperty.getProperty().MainDataSource, 
				SysPreperty.getProperty().IsConvert);
		dbEngine.initialize();
		if (clients < 1) {
			// clients something todo
		}
		clients = 1;
	}
	
	@Override
	public QueryConfigBrelationBean queryObject(String id) throws Exception{
	QueryConfigBrelationBean queryConfigBrelationBean = null;
		QueryConfigBrelation queryConfigBrelation =  queryConfigBrelationDao.queryObject(id);
			if( queryConfigBrelation != null ) {
			queryConfigBrelationBean = new QueryConfigBrelationBean();
			BeanUtils.copyProperties( queryConfigBrelation, queryConfigBrelationBean );
		}
		return queryConfigBrelationBean;
	}
	
	@Override
	public List<QueryConfigBrelationBean> queryList(Map<String, Object> map) throws Exception{
		List<QueryConfigBrelationBean> queryConfigBrelationBeans = new ArrayList<>();
		List<QueryConfigBrelation> list = queryConfigBrelationDao.queryList( map );
		for( QueryConfigBrelation queryConfigBrelation : list ) {
			QueryConfigBrelationBean queryConfigBrelationBean = new QueryConfigBrelationBean();
			BeanUtils.copyProperties( queryConfigBrelation, queryConfigBrelationBean );
			queryConfigBrelationBeans.add( queryConfigBrelationBean );
		}
		return queryConfigBrelationBeans;
	}
	
	@Override
	public int queryTotal(Map<String, Object> map) throws Exception{
		return queryConfigBrelationDao.queryTotal(map);
	}
	
	@Override
	public void save(QueryConfigBrelationBean queryConfigBrelationBean) throws Exception{
		QueryConfigBrelation queryConfigBrelation = new QueryConfigBrelation();
		BeanUtils.copyProperties( queryConfigBrelationBean, queryConfigBrelation );
		queryConfigBrelationDao.save(queryConfigBrelation);
	}
	
	@Override
	public void update(QueryConfigBrelationBean queryConfigBrelationBean) throws Exception{
	QueryConfigBrelation queryConfigBrelation = new QueryConfigBrelation();
		BeanUtils.copyProperties( queryConfigBrelationBean, queryConfigBrelation );
		queryConfigBrelationDao.update(queryConfigBrelation);
	}
	
	@Override
	public void delete(String id) throws Exception{
		queryConfigBrelationDao.delete(id);
	}
	
	@Override
	public void deleteBatch(String[] ids) throws Exception{
		queryConfigBrelationDao.deleteBatch(ids);
	}

	@Override
	public String getMaxNo() throws Exception {
		String maxId=queryConfigBrelationDao.getMaxNo();
		if(maxId==null||maxId.equals("null")) {
			maxId="00000001";
		}else {
			maxId="00000000"+(Integer.valueOf(maxId)+1);
			maxId=maxId.substring(maxId.length()-8, maxId.length());
		}
		return maxId;
	}
	
}
