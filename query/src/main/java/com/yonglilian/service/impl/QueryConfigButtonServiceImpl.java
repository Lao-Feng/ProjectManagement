package com.yonglilian.service.impl;

import com.yonglilian.bean.QueryConfigButtonBean;
import com.yonglilian.dao.QueryConfigButtonDao;
import com.yonglilian.domain.QueryConfigButton;
import com.yonglilian.service.QueryConfigButtonService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Service("queryConfigButtonService")
public class QueryConfigButtonServiceImpl implements QueryConfigButtonService {
	@Autowired
	private QueryConfigButtonDao queryConfigButtonDao;
	
	@Override
	public QueryConfigButtonBean queryObject(String id) throws Exception{
	QueryConfigButtonBean queryConfigButtonBean = null;
		QueryConfigButton queryConfigButton =  queryConfigButtonDao.queryObject(id);
			if( queryConfigButton != null ) {
			queryConfigButtonBean = new QueryConfigButtonBean();
			BeanUtils.copyProperties( queryConfigButton, queryConfigButtonBean );
		}
		return queryConfigButtonBean;
	}
	
	@Override
	public List<QueryConfigButtonBean> queryList(Map<String, Object> map) throws Exception{
		List<QueryConfigButtonBean> queryConfigButtonBeans = new ArrayList<>();
		List<QueryConfigButton> list = queryConfigButtonDao.queryList( map );
		for( QueryConfigButton queryConfigButton : list ) {
			QueryConfigButtonBean queryConfigButtonBean = new QueryConfigButtonBean();
			BeanUtils.copyProperties( queryConfigButton, queryConfigButtonBean );
			queryConfigButtonBeans.add( queryConfigButtonBean );
		}
		return queryConfigButtonBeans;
	}
	
	@Override
	public int queryTotal(Map<String, Object> map) throws Exception{
		return queryConfigButtonDao.queryTotal(map);
	}
	
	@Override
	public void save(QueryConfigButtonBean queryConfigButtonBean) throws Exception{
		QueryConfigButton queryConfigButton = new QueryConfigButton();
		BeanUtils.copyProperties( queryConfigButtonBean, queryConfigButton );
		queryConfigButton.setId(getMaxNo());
		queryConfigButtonDao.save(queryConfigButton);
	}
	
	@Override
	public void update(QueryConfigButtonBean queryConfigButtonBean) throws Exception{
	QueryConfigButton queryConfigButton = new QueryConfigButton();
		BeanUtils.copyProperties( queryConfigButtonBean, queryConfigButton );
		queryConfigButtonDao.update(queryConfigButton);
	}
	
	@Override
	public void delete(String id) throws Exception{
		queryConfigButtonDao.delete(id);
	}
	
	@Override
	public void deleteBatch(String[] ids) throws Exception{
		queryConfigButtonDao.deleteBatch(ids);
	}

	@Override
	public String getMaxNo() throws Exception {
		String maxId=queryConfigButtonDao.getMaxNo();
		if(maxId==null||maxId.equals("null")) {
			maxId="00000001";
		}else {
			maxId="00000000"+(Integer.valueOf(maxId)+1);
			maxId=maxId.substring(maxId.length()-8, maxId.length());
		}
		return maxId;
	}
	
}
