package com.yonglilian.service.impl;

import com.yonglilian.bean.QueryConfigOrderBean;
import com.yonglilian.common.util.SysPreperty;
import com.yonglilian.dao.QueryConfigOrderDao;
import com.yonglilian.domain.QueryConfigOrder;
import com.yonglilian.service.QueryConfigOrderService;
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


@Service("queryConfigOrderService")
public class QueryConfigOrderServiceImpl implements QueryConfigOrderService {
    /** The QueryConfigServiceImpl Logger. */
	@SuppressWarnings("unused")
	private static final Logger LOGGER = LoggerFactory.getLogger(QueryConfigOrderServiceImpl.class);
	private DBEngine dbEngine;
	private static int clients = 0;
	String DataBaseType = SysPreperty.getProperty().DataBaseType;// 数据库类型
	
	
	@Autowired
	private QueryConfigOrderDao queryConfigOrderDao;
	
	/**
	 * 构造方法
	 */
	public QueryConfigOrderServiceImpl() {
		dbEngine = new DBEngine(SysPreperty.getProperty().MainDataSource, 
				SysPreperty.getProperty().IsConvert);
		dbEngine.initialize();
		if (clients < 1) {
			// clients something todo
		}
		clients = 1;
	}
	
	@Override
	public QueryConfigOrderBean queryObject(String id) throws Exception{
	QueryConfigOrderBean queryConfigOrderBean = null;
		QueryConfigOrder queryConfigOrder =  queryConfigOrderDao.queryObject(id);
			if( queryConfigOrder != null ) {
			queryConfigOrderBean = new QueryConfigOrderBean();
			BeanUtils.copyProperties( queryConfigOrder, queryConfigOrderBean );
		}
		return queryConfigOrderBean;
	}
	
	@Override
	public List<QueryConfigOrderBean> queryList(Map<String, Object> map) throws Exception{
		List<QueryConfigOrderBean> queryConfigOrderBeans = new ArrayList<>();
		List<QueryConfigOrder> list = queryConfigOrderDao.queryList( map );
		for( QueryConfigOrder queryConfigOrder : list ) {
			QueryConfigOrderBean queryConfigOrderBean = new QueryConfigOrderBean();
			BeanUtils.copyProperties( queryConfigOrder, queryConfigOrderBean );
			queryConfigOrderBeans.add( queryConfigOrderBean );
		}
		return queryConfigOrderBeans;
	}
	
	@Override
	public int queryTotal(Map<String, Object> map) throws Exception{
		return queryConfigOrderDao.queryTotal(map);
	}
	
	@Override
	public void save(QueryConfigOrderBean queryConfigOrderBean) throws Exception{
		QueryConfigOrder queryConfigOrder = new QueryConfigOrder();
		BeanUtils.copyProperties( queryConfigOrderBean, queryConfigOrder );
		queryConfigOrderDao.save(queryConfigOrder);
	}
	
	@Override
	public void update(QueryConfigOrderBean queryConfigOrderBean) throws Exception{
	QueryConfigOrder queryConfigOrder = new QueryConfigOrder();
		BeanUtils.copyProperties( queryConfigOrderBean, queryConfigOrder );
		queryConfigOrderDao.update(queryConfigOrder);
	}
	
	@Override
	public void delete(String fid) throws Exception{
		queryConfigOrderDao.delete(fid);
	}
	
	@Override
	public void deleteBatch(String[] ids) throws Exception{
		queryConfigOrderDao.deleteBatch(ids);
	}
	
	@Override
	public String getMaxNo() throws Exception {
		String maxId=queryConfigOrderDao.getMaxNo();
		if(maxId==null||maxId.equals("null")) {
			maxId="00000001";
		}else {
			maxId="00000000"+(Integer.valueOf(maxId)+1);
			maxId=maxId.substring(maxId.length()-8, maxId.length());
		}
		return maxId;
	}

	@Override
	public int saveBatch(List<QueryConfigOrderBean> list, String fid) throws Exception {
		List<QueryConfigOrderBean> insertlist = new ArrayList<>();
		//遍历剔除异常字段数据
        Iterator<QueryConfigOrderBean> iterator = list.iterator(); 
        int count=0;
        while (iterator.hasNext()) {  
        	QueryConfigOrderBean model = iterator.next();  
            if (model.getField()==null||
            		model.getField().trim().length()==0||
            		model.getType()==null||
            		model.getType().trim().length()==0) {  
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
			return queryConfigOrderDao.saveBatchList(insertlist);
		}
		return 0;
	}
	
}
