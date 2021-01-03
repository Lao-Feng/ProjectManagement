package com.yonglilian.service.impl;

import com.yonglilian.bean.QueryConfigConnectionBean;
import com.yonglilian.dao.QueryConfigConnectionDao;
import com.yonglilian.domain.QueryConfigConnection;
import com.yonglilian.service.QueryConfigConnectionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zr.zrpower.common.db.DBEngine;
import zr.zrpower.common.util.SysPreperty;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


@Service("queryConfigConnectionService")
public class QueryConfigConnectionServiceImpl implements QueryConfigConnectionService {
    /** The QueryConfigServiceImpl Logger. */
	@SuppressWarnings("unused")
	private static final Logger LOGGER = LoggerFactory.getLogger(QueryConfigConnectionServiceImpl.class);
	private DBEngine dbEngine;
	private static int clients = 0;
	String DataBaseType = SysPreperty.getProperty().DataBaseType;// 数据库类型
	
	
	@Autowired
	private QueryConfigConnectionDao queryConfigConnectionDao;
	
	/**
	 * 构造方法
	 */
	public QueryConfigConnectionServiceImpl() {
		dbEngine = new DBEngine(SysPreperty.getProperty().MainDataSource, 
				SysPreperty.getProperty().IsConvert);
		dbEngine.initialize();
		if (clients < 1) {
			// clients something todo
		}
		clients = 1;
	}
	
	@Override
	public QueryConfigConnectionBean queryObject(String id) throws Exception{
	QueryConfigConnectionBean queryConfigConnectionBean = null;
		QueryConfigConnection queryConfigConnection =  queryConfigConnectionDao.queryObject(id);
			if( queryConfigConnection != null ) {
			queryConfigConnectionBean = new QueryConfigConnectionBean();
			BeanUtils.copyProperties( queryConfigConnection, queryConfigConnectionBean );
		}
		return queryConfigConnectionBean;
	}
	
	@Override
	public List<QueryConfigConnectionBean> queryList(Map<String, Object> map) throws Exception{
		List<QueryConfigConnectionBean> queryConfigConnectionBeans = new ArrayList<>();
		List<QueryConfigConnection> list = queryConfigConnectionDao.queryList( map );
		for( QueryConfigConnection queryConfigConnection : list ) {
			QueryConfigConnectionBean queryConfigConnectionBean = new QueryConfigConnectionBean();
			BeanUtils.copyProperties( queryConfigConnection, queryConfigConnectionBean );
			queryConfigConnectionBean.setMfieldtable(queryConfigConnection.getMfield().split("\\.")[0]);
			queryConfigConnectionBean.setCfieldtable(queryConfigConnection.getCfield().split("\\.")[0]);
			queryConfigConnectionBean.setMfield(queryConfigConnection.getMfield().split("\\.")[1]);
			queryConfigConnectionBean.setCfield(queryConfigConnection.getCfield().split("\\.")[1]);
			queryConfigConnectionBeans.add( queryConfigConnectionBean );
		}
		return queryConfigConnectionBeans;
	}
	
	@Override
	public int queryTotal(Map<String, Object> map) throws Exception{
		return queryConfigConnectionDao.queryTotal(map);
	}
	
	@Override
	public void save(QueryConfigConnectionBean queryConfigConnectionBean) throws Exception{
		QueryConfigConnection queryConfigConnection = new QueryConfigConnection();
		BeanUtils.copyProperties( queryConfigConnectionBean, queryConfigConnection );
		queryConfigConnectionDao.save(queryConfigConnection);
	}
	
	@Override
	public void update(QueryConfigConnectionBean queryConfigConnectionBean) throws Exception{
	QueryConfigConnection queryConfigConnection = new QueryConfigConnection();
		BeanUtils.copyProperties( queryConfigConnectionBean, queryConfigConnection );
		queryConfigConnectionDao.update(queryConfigConnection);
	}
	
	@Override
	public void delete(String fid) throws Exception{
		queryConfigConnectionDao.delete(fid);
	}
	
	@Override
	public void deleteBatch(String[] ids) throws Exception{
		queryConfigConnectionDao.deleteBatch(ids);
	}

	@Override
	public String getMaxNo() throws Exception {
		String maxId=queryConfigConnectionDao.getMaxNo();
		if(maxId==null||maxId.equals("null")) {
			maxId="00000001";
		}else {
			maxId="00000000"+(Integer.valueOf(maxId)+1);
			maxId=maxId.substring(maxId.length()-8, maxId.length());
		}
		return maxId;
	}

	@Override
	public int saveBatch(List<QueryConfigConnectionBean> list,String fid) throws Exception {
		List<QueryConfigConnectionBean> insertlist = new ArrayList<>();
		//遍历剔除异常字段数据
        Iterator<QueryConfigConnectionBean> iterator = list.iterator(); 
        int count=0;
        while (iterator.hasNext()) {  
        	QueryConfigConnectionBean model = iterator.next();  
            if (model.getCfield()==null||
            		model.getCfield().trim().length()==0||
            		model.getMfield()==null||
            		model.getMfield().trim().length()==0) {  
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
            	model.setMfield(model.getMfieldtable()+"."+model.getMfield());
            	model.setCfield(model.getCfieldtable()+"."+model.getCfield());
            	insertlist.add(model);
            }
        }
		if(insertlist != null && insertlist.size()>0) {
			return queryConfigConnectionDao.saveBatchList(insertlist);
		}
		return 0;
	}
	
}
