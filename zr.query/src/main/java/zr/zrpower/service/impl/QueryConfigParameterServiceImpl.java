package zr.zrpower.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zr.zrpower.bean.QueryConfigParameterBean;
import zr.zrpower.common.db.DBEngine;
import zr.zrpower.common.util.SysPreperty;
import zr.zrpower.dao.QueryConfigParameterDao;
import zr.zrpower.domain.QueryConfigParameter;
import zr.zrpower.service.QueryConfigParameterService;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


@Service("queryConfigParameterService")
public class QueryConfigParameterServiceImpl implements QueryConfigParameterService {
    /** The QueryConfigServiceImpl Logger. */
	@SuppressWarnings("unused")
	private static final Logger LOGGER = LoggerFactory.getLogger(QueryConfigParameterServiceImpl.class);
	private DBEngine dbEngine;
	private static int clients = 0;
	String DataBaseType = SysPreperty.getProperty().DataBaseType;// 数据库类型
	
	
	@Autowired
	private QueryConfigParameterDao queryConfigParameterDao;
	
	/**
	 * 构造方法
	 */
	public QueryConfigParameterServiceImpl() {
		dbEngine = new DBEngine(SysPreperty.getProperty().MainDataSource, 
				SysPreperty.getProperty().IsConvert);
		dbEngine.initialize();
		if (clients < 1) {
			// clients something todo
		}
		clients = 1;
	}
	
	@Override
	public QueryConfigParameterBean queryObject(String id) throws Exception{
	QueryConfigParameterBean queryConfigParameterBean = null;
		QueryConfigParameter queryConfigParameter =  queryConfigParameterDao.queryObject(id);
			if( queryConfigParameter != null ) {
			queryConfigParameterBean = new QueryConfigParameterBean();
			BeanUtils.copyProperties( queryConfigParameter, queryConfigParameterBean );
		}
		return queryConfigParameterBean;
	}
	
	@Override
	public List<QueryConfigParameterBean> queryList(Map<String, Object> map) throws Exception{
		List<QueryConfigParameterBean> queryConfigParameterBeans = new ArrayList<>();
		List<QueryConfigParameter> list = queryConfigParameterDao.queryList( map );
		for( QueryConfigParameter queryConfigParameter : list ) {
			QueryConfigParameterBean queryConfigParameterBean = new QueryConfigParameterBean();
			BeanUtils.copyProperties( queryConfigParameter, queryConfigParameterBean );
			queryConfigParameterBeans.add( queryConfigParameterBean );
		}
		return queryConfigParameterBeans;
	}
	
	@Override
	public int queryTotal(Map<String, Object> map) throws Exception{
		return queryConfigParameterDao.queryTotal(map);
	}
	
	@Override
	public void save(QueryConfigParameterBean queryConfigParameterBean) throws Exception{
		QueryConfigParameter queryConfigParameter = new QueryConfigParameter();
		BeanUtils.copyProperties( queryConfigParameterBean, queryConfigParameter );
		queryConfigParameterDao.save(queryConfigParameter);
	}
	
	@Override
	public void update(QueryConfigParameterBean queryConfigParameterBean) throws Exception{
	QueryConfigParameter queryConfigParameter = new QueryConfigParameter();
		BeanUtils.copyProperties( queryConfigParameterBean, queryConfigParameter );
		queryConfigParameterDao.update(queryConfigParameter);
	}
	
	@Override
	public void delete(String fid) throws Exception{
		queryConfigParameterDao.delete(fid);
	}
	
	@Override
	public void deleteBatch(String[] ids) throws Exception{
		queryConfigParameterDao.deleteBatch(ids);
	}
	
	@Override
	public String getMaxNo() throws Exception {
		String maxId=queryConfigParameterDao.getMaxNo();
		if(maxId==null||maxId.equals("null")) {
			maxId="00000001";
		}else {
			maxId="00000000"+(Integer.valueOf(maxId)+1);
			maxId=maxId.substring(maxId.length()-8, maxId.length());
		}
		return maxId;
	}

	@Override
	public int saveBatch(List<QueryConfigParameterBean> list, String fid) throws Exception {
		List<QueryConfigParameterBean> insertlist = new ArrayList<>();
		//遍历剔除异常字段数据
        Iterator<QueryConfigParameterBean> iterator = list.iterator();  
        int count=0;
        while (iterator.hasNext()) {  
        	QueryConfigParameterBean model = iterator.next();  
            if (model.getField()==null||
            		model.getField().trim().length()==0||
            		model.getBid()==null||
            		model.getBid().trim().length()==0) {  
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
			return queryConfigParameterDao.saveBatchList(insertlist);
		}
		return 0;
	}
	
}
