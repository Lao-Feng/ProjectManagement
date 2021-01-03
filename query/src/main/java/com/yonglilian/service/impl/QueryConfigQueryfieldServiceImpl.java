package com.yonglilian.service.impl;

import com.yonglilian.bean.QueryConfigQueryfieldBean;
import com.yonglilian.dao.QueryConfigQueryfieldDao;
import com.yonglilian.domain.QueryConfigQueryfield;
import com.yonglilian.service.QueryConfigQueryfieldService;
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


@Service("queryConfigQueryfieldService")
public class QueryConfigQueryfieldServiceImpl implements QueryConfigQueryfieldService {
    /** The QueryConfigServiceImpl Logger. */
	@SuppressWarnings("unused")
	private static final Logger LOGGER = LoggerFactory.getLogger(QueryConfigQueryfieldServiceImpl.class);
	private DBEngine dbEngine;
	private static int clients = 0;
	String DataBaseType = SysPreperty.getProperty().DataBaseType;// 数据库类型
	
	
	@Autowired
	private QueryConfigQueryfieldDao queryConfigQueryfieldDao;

	
	/**
	 * 构造方法
	 */
	public QueryConfigQueryfieldServiceImpl() {
		dbEngine = new DBEngine(SysPreperty.getProperty().MainDataSource, 
				SysPreperty.getProperty().IsConvert);
		dbEngine.initialize();
		if (clients < 1) {
			// clients something todo
		}
		clients = 1;
	}
	
	@Override
	public QueryConfigQueryfieldBean queryObject(String id) throws Exception{
	QueryConfigQueryfieldBean queryConfigQueryfieldBean = null;
		QueryConfigQueryfield queryConfigQueryfield =  queryConfigQueryfieldDao.queryObject(id);
			if( queryConfigQueryfield != null ) {
			queryConfigQueryfieldBean = new QueryConfigQueryfieldBean();
			BeanUtils.copyProperties( queryConfigQueryfield, queryConfigQueryfieldBean );
		}
		return queryConfigQueryfieldBean;
	}
	
	@Override
	public List<QueryConfigQueryfieldBean> queryList(Map<String, Object> map) throws Exception{
		List<QueryConfigQueryfieldBean> queryConfigQueryfieldBeans = new ArrayList<>();
		List<QueryConfigQueryfield> list = queryConfigQueryfieldDao.queryList( map );
		for( QueryConfigQueryfield queryConfigQueryfield : list ) {
			QueryConfigQueryfieldBean queryConfigQueryfieldBean = new QueryConfigQueryfieldBean();
			BeanUtils.copyProperties( queryConfigQueryfield, queryConfigQueryfieldBean );
			queryConfigQueryfieldBeans.add( queryConfigQueryfieldBean );
		}
		return queryConfigQueryfieldBeans;
	}
	
	@Override
	public int queryTotal(Map<String, Object> map) throws Exception{
		return queryConfigQueryfieldDao.queryTotal(map);
	}
	
	@Override
	public void save(QueryConfigQueryfieldBean queryConfigQueryfieldBean) throws Exception{
		QueryConfigQueryfield queryConfigQueryfield = new QueryConfigQueryfield();
		BeanUtils.copyProperties( queryConfigQueryfieldBean, queryConfigQueryfield );
		queryConfigQueryfieldDao.save(queryConfigQueryfield);
	}
	
	@Override
	public void update(QueryConfigQueryfieldBean queryConfigQueryfieldBean) throws Exception{
	QueryConfigQueryfield queryConfigQueryfield = new QueryConfigQueryfield();
		BeanUtils.copyProperties( queryConfigQueryfieldBean, queryConfigQueryfield );
		queryConfigQueryfieldDao.update(queryConfigQueryfield);
	}
	
	@Override
	public void delete(String fid) throws Exception{
		queryConfigQueryfieldDao.delete(fid);
	}
	
	@Override
	public void deleteBatch(String[] ids) throws Exception{
		queryConfigQueryfieldDao.deleteBatch(ids);
	}
	
	@Override
	public String getMaxNo() throws Exception {
		String maxId=queryConfigQueryfieldDao.getMaxNo();
		if(maxId==null||maxId.equals("null")) {
			maxId="00000001";
		}else {
			maxId="00000000"+(Integer.valueOf(maxId)+1);
			maxId=maxId.substring(maxId.length()-8, maxId.length());
		}
		return maxId;
	}

	@Override
	public int saveBatch(List<String> fieldList, String fid) throws Exception {
		List<QueryConfigQueryfieldBean> list = null;
		int count=0;
		if(fieldList != null && fieldList.size()>0) {
			list = new ArrayList<QueryConfigQueryfieldBean>();
			int len = fieldList.size();
			for(int i = 0;i<len;i++) {
				QueryConfigQueryfieldBean model = new QueryConfigQueryfieldBean();
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
				model.setField(fieldList.get(i).toString());
				list.add(model);
			}
			return queryConfigQueryfieldDao.saveBatchList(list);
		}
		return 0;
	}

	@Override
	public List<String> fieldList(Map<String, Object> map) throws Exception {
		List<String> fieldList = new ArrayList<>();
		List<QueryConfigQueryfield> list = queryConfigQueryfieldDao.queryList( map );
		if(list != null && list.size()>0) {
			for( QueryConfigQueryfield queryConfigQueryfield : list ) {
				fieldList.add(queryConfigQueryfield.getField());
			}
		}
		return fieldList;
	}
	
}
