package zr.zrpower.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zr.zrpower.bean.QueryConfigShowfieldBean;
import zr.zrpower.common.db.DBEngine;
import zr.zrpower.common.util.SysPreperty;
import zr.zrpower.dao.QueryConfigShowfieldDao;
import zr.zrpower.domain.QueryConfigShowfield;
import zr.zrpower.service.QueryConfigShowfieldService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Service("queryConfigShowfieldService")
public class QueryConfigShowfieldServiceImpl implements QueryConfigShowfieldService {
    /** The QueryConfigServiceImpl Logger. */
	@SuppressWarnings("unused")
	private static final Logger LOGGER = LoggerFactory.getLogger(QueryConfigShowfieldServiceImpl.class);
	private DBEngine dbEngine;
	private static int clients = 0;
	String DataBaseType = SysPreperty.getProperty().DataBaseType;// 数据库类型
	
	
	@Autowired
	private QueryConfigShowfieldDao queryConfigShowfieldDao;
	
	/**
	 * 构造方法
	 */
	public QueryConfigShowfieldServiceImpl() {
		dbEngine = new DBEngine(SysPreperty.getProperty().MainDataSource, 
				SysPreperty.getProperty().IsConvert);
		dbEngine.initialize();
		if (clients < 1) {
			// clients something todo
		}
		clients = 1;
	}
	
	@Override
	public QueryConfigShowfieldBean queryObject(String id) throws Exception{
	QueryConfigShowfieldBean queryConfigShowfieldBean = null;
		QueryConfigShowfield queryConfigShowfield =  queryConfigShowfieldDao.queryObject(id);
			if( queryConfigShowfield != null ) {
			queryConfigShowfieldBean = new QueryConfigShowfieldBean();
			BeanUtils.copyProperties( queryConfigShowfield, queryConfigShowfieldBean );
		}
		return queryConfigShowfieldBean;
	}
	
	@Override
	public List<QueryConfigShowfieldBean> queryList(Map<String, Object> map,boolean boo) throws Exception{
		List<QueryConfigShowfieldBean> queryConfigShowfieldBeans = new ArrayList<>();
		List<QueryConfigShowfield> list = queryConfigShowfieldDao.queryList( map );
		for( QueryConfigShowfield queryConfigShowfield : list ) {
			QueryConfigShowfieldBean queryConfigShowfieldBean = new QueryConfigShowfieldBean();
			BeanUtils.copyProperties( queryConfigShowfield, queryConfigShowfieldBean );
			if(boo) {
				if(queryConfigShowfieldBean.getIsshow()!=null&&queryConfigShowfieldBean.getIsshow().equals("1")) {
					queryConfigShowfieldBean.setIsshow("true");
				}else {
					queryConfigShowfieldBean.setIsshow("false");
				}
				if(queryConfigShowfieldBean.getIsnumber()!=null&&queryConfigShowfieldBean.getIsnumber().equals("1")) {
					queryConfigShowfieldBean.setIsnumber("true");
				}else {
					queryConfigShowfieldBean.setIsnumber("false");
				}
				if(queryConfigShowfieldBean.getQalign()==null||queryConfigShowfieldBean.getQalign().trim().length()==0) {
					queryConfigShowfieldBean.setQalign("0");
				}
			}
			queryConfigShowfieldBeans.add( queryConfigShowfieldBean );
		}
		return queryConfigShowfieldBeans;
	}
	
	@Override
	public int queryTotal(Map<String, Object> map) throws Exception{
		return queryConfigShowfieldDao.queryTotal(map);
	}
	
	@Override
	public void save(QueryConfigShowfieldBean queryConfigShowfieldBean) throws Exception{
		QueryConfigShowfield queryConfigShowfield = new QueryConfigShowfield();
		BeanUtils.copyProperties( queryConfigShowfieldBean, queryConfigShowfield );
		queryConfigShowfieldDao.save(queryConfigShowfield);
	}
	
	@Override
	public void update(QueryConfigShowfieldBean queryConfigShowfieldBean) throws Exception{
	QueryConfigShowfield queryConfigShowfield = new QueryConfigShowfield();
		BeanUtils.copyProperties( queryConfigShowfieldBean, queryConfigShowfield );
		queryConfigShowfieldDao.update(queryConfigShowfield);
	}
	
	@Override
	public void delete(String fid) throws Exception{
		queryConfigShowfieldDao.delete(fid);
	}
	
	@Override
	public void deleteBatch(String[] ids) throws Exception{
		queryConfigShowfieldDao.deleteBatch(ids);
	}
	
	@Override
	public String getMaxNo() throws Exception {
		String maxId=queryConfigShowfieldDao.getMaxNo();
		if(maxId==null||maxId.equals("null")) {
			maxId="00000001";
		}else {
			maxId="00000000"+(Integer.valueOf(maxId)+1);
			maxId=maxId.substring(maxId.length()-8, maxId.length());
		}
		return maxId;
	}

	@Override
	public int saveBatch(List<QueryConfigShowfieldBean> list, String fid) throws Exception {
		int count=0;
		if(list != null && list.size()>0) {
			int len = list.size();
			for(int i = 0;i<len;i++) {
				if("true".equals(list.get(i).getIsshow())) {
					list.get(i).setIsshow("1");
				}else {
					list.get(i).setIsshow("0");
				}
				if("true".equals(list.get(i).getIsnumber())) {
					list.get(i).setIsnumber("1");
				}else {
					list.get(i).setIsnumber("0");
				}
				if(list.get(i).getQalign()==null||list.get(i).getQalign().trim().length()!=1) {
					list.get(i).setQalign("0");
				}
				String id="";
            	if(count==0) {
            		id=getMaxNo();
            		count=(Integer.valueOf(id)+1);
            	}else {
            		id="00000000"+count;
            		id=id.substring(id.length()-8, id.length());
            		count=(Integer.valueOf(id)+1);
            	}
				list.get(i).setId(id);
				list.get(i).setFid(fid);
			}
		}
		return queryConfigShowfieldDao.saveBatchList(list);
	}
	
}
