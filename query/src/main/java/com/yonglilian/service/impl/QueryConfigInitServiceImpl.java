package com.yonglilian.service.impl;

import com.yonglilian.bean.QueryConfigInitBean;
import com.yonglilian.common.util.SysPreperty;
import com.yonglilian.dao.QueryConfigInitDao;
import com.yonglilian.domain.QueryConfigInit;
import com.yonglilian.service.QueryConfigInitService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zr.zrpower.common.db.DBEngine;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


@Service("queryConfigInitService")
public class QueryConfigInitServiceImpl implements QueryConfigInitService {
    /** The QueryConfigServiceImpl Logger. */
	@SuppressWarnings("unused")
	private static final Logger LOGGER = LoggerFactory.getLogger(QueryConfigInitServiceImpl.class);
	private DBEngine dbEngine;
	private static int clients = 0;
	String DataBaseType = SysPreperty.getProperty().DataBaseType;// 数据库类型
	
	
	@Autowired
	private QueryConfigInitDao queryConfigInitDao;
	
	/**
	 * 构造方法
	 */
	public QueryConfigInitServiceImpl() {
		dbEngine = new DBEngine(SysPreperty.getProperty().MainDataSource, 
				SysPreperty.getProperty().IsConvert);
		dbEngine.initialize();
		if (clients < 1) {
			// clients something todo
		}
		clients = 1;
	}
	
	@Override
	public QueryConfigInitBean queryObject(String id) throws Exception{
	QueryConfigInitBean queryConfigInitBean = null;
		QueryConfigInit queryConfigInit =  queryConfigInitDao.queryObject(id);
			if( queryConfigInit != null ) {
			queryConfigInitBean = new QueryConfigInitBean();
			BeanUtils.copyProperties( queryConfigInit, queryConfigInitBean );
		}
		return queryConfigInitBean;
	}
	
	@Override
	public List<QueryConfigInitBean> queryList(Map<String, Object> map) throws Exception{
		List<QueryConfigInitBean> queryConfigInitBeans = new ArrayList<>();
		List<QueryConfigInit> list = queryConfigInitDao.queryList( map );
		for( QueryConfigInit queryConfigInit : list ) {
			QueryConfigInitBean queryConfigInitBean = new QueryConfigInitBean();
			BeanUtils.copyProperties( queryConfigInit, queryConfigInitBean );
			queryConfigInitBeans.add( queryConfigInitBean );
		}
		return queryConfigInitBeans;
	}
	
	@Override
	public int queryTotal(Map<String, Object> map) throws Exception{
		return queryConfigInitDao.queryTotal(map);
	}
	
	@Override
	public void save(QueryConfigInitBean queryConfigInitBean) throws Exception{
		QueryConfigInit queryConfigInit = new QueryConfigInit();
		BeanUtils.copyProperties( queryConfigInitBean, queryConfigInit );
		queryConfigInitDao.save(queryConfigInit);
	}
	
	@Override
	public void update(QueryConfigInitBean queryConfigInitBean) throws Exception{
	QueryConfigInit queryConfigInit = new QueryConfigInit();
		BeanUtils.copyProperties( queryConfigInitBean, queryConfigInit );
		queryConfigInitDao.update(queryConfigInit);
	}
	
	@Override
	public void delete(String fid) throws Exception{
		queryConfigInitDao.delete(fid);
	}
	
	@Override
	public void deleteBatch(String[] ids) throws Exception{
		queryConfigInitDao.deleteBatch(ids);
	}
	
	@Override
	public String getMaxNo() throws Exception {
		String maxId=queryConfigInitDao.getMaxNo();
		if(maxId==null||maxId.equals("null")) {
			maxId="00000001";
		}else {
			maxId="00000000"+(Integer.valueOf(maxId)+1);
			maxId=maxId.substring(maxId.length()-8, maxId.length());
		}
		return maxId;
	}

	@Override
	public int saveBatch(List<QueryConfigInitBean> list, String fid) throws Exception {
		List<QueryConfigInitBean> insertlist = new ArrayList<>();
		//遍历剔除异常字段数据
        Iterator<QueryConfigInitBean> iterator = list.iterator();
        int count=0;
        while (iterator.hasNext()) {  
        	QueryConfigInitBean model = iterator.next();  
            if (model.getField()==null||
            		model.getField().trim().length()==0) {  
//            	iterator.remove();
            }else {
            	String id="";
            	if(count==0) {
            		id=getMaxNo();
            		count=(Integer.valueOf(id)+1);
            	}else {
            		id="00000000"+count;
            		id=id.substring(id.length()-8, id.length());
            		count=(Integer.valueOf(id)+1);
            	}
            	model.setId(id);
            	model.setFid(fid);
            	insertlist.add(model);
            }
        }
		if(insertlist != null && insertlist.size()>0) {
			return queryConfigInitDao.saveBatchList(insertlist);
		}
		return 0;
	}
	
}
