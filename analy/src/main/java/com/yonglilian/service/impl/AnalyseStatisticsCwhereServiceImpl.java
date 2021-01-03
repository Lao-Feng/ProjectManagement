package com.yonglilian.service.impl;

import com.yonglilian.bean.AnalyseStatisticsCwhereBean;
import com.yonglilian.dao.AnalyseStatisticsCwhereDao;
import com.yonglilian.domain.AnalyseStatisticsCwhere;
import com.yonglilian.service.AnalyseStatisticsCwhereService;
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


@Service("analyseStatisticsCwhereService")
public class AnalyseStatisticsCwhereServiceImpl implements AnalyseStatisticsCwhereService {
    /** The QueryConfigServiceImpl Logger. */
	@SuppressWarnings("unused")
	private static final Logger LOGGER = LoggerFactory.getLogger(AnalyseStatisticsCwhereServiceImpl.class);
	private DBEngine dbEngine;
	private static int clients = 0;
	String DataBaseType = SysPreperty.getProperty().DataBaseType;// 数据库类型
	
	
	@Autowired
	private AnalyseStatisticsCwhereDao analyseStatisticsCwhereDao;
	
	/**
	 * 构造方法
	 */
	public AnalyseStatisticsCwhereServiceImpl() {
		dbEngine = new DBEngine(SysPreperty.getProperty().MainDataSource, 
				SysPreperty.getProperty().IsConvert);
		dbEngine.initialize();
		if (clients < 1) {
			// clients something todo
		}
		clients = 1;
	}
	
	@Override
	public AnalyseStatisticsCwhereBean queryObject(String id) throws Exception{
	AnalyseStatisticsCwhereBean analyseStatisticsCwhereBean = null;
		AnalyseStatisticsCwhere analyseStatisticsCwhere =  analyseStatisticsCwhereDao.queryObject(id);
			if( analyseStatisticsCwhere != null ) {
			analyseStatisticsCwhereBean = new AnalyseStatisticsCwhereBean();
			BeanUtils.copyProperties( analyseStatisticsCwhere, analyseStatisticsCwhereBean );
		}
		return analyseStatisticsCwhereBean;
	}
	
	@Override
	public List<AnalyseStatisticsCwhereBean> queryList(Map<String, Object> map) throws Exception{
		List<AnalyseStatisticsCwhereBean> analyseStatisticsCwhereBeans = new ArrayList<>();
		List<AnalyseStatisticsCwhere> list = analyseStatisticsCwhereDao.queryList( map );
		for( AnalyseStatisticsCwhere analyseStatisticsCwhere : list ) {
			AnalyseStatisticsCwhereBean analyseStatisticsCwhereBean = new AnalyseStatisticsCwhereBean();
			BeanUtils.copyProperties( analyseStatisticsCwhere, analyseStatisticsCwhereBean );
			analyseStatisticsCwhereBeans.add( analyseStatisticsCwhereBean );
		}
		return analyseStatisticsCwhereBeans;
	}
	
	@Override
	public int queryTotal(Map<String, Object> map) throws Exception{
		return analyseStatisticsCwhereDao.queryTotal(map);
	}
	
	@Override
	public void save(AnalyseStatisticsCwhereBean analyseStatisticsCwhereBean) throws Exception{
		AnalyseStatisticsCwhere analyseStatisticsCwhere = new AnalyseStatisticsCwhere();
		BeanUtils.copyProperties( analyseStatisticsCwhereBean, analyseStatisticsCwhere );
		analyseStatisticsCwhere.setId(getMaxNo());
		analyseStatisticsCwhereDao.save(analyseStatisticsCwhere);
	}
	
	@Override
	public void update(AnalyseStatisticsCwhereBean analyseStatisticsCwhereBean) throws Exception{
	AnalyseStatisticsCwhere analyseStatisticsCwhere = new AnalyseStatisticsCwhere();
		BeanUtils.copyProperties( analyseStatisticsCwhereBean, analyseStatisticsCwhere );
		analyseStatisticsCwhereDao.update(analyseStatisticsCwhere);
	}
	
	@Override
	public void delete(String id) throws Exception{
		analyseStatisticsCwhereDao.delete(id);
	}
	
	@Override
	public void deleteBatch(String[] ids) throws Exception{
		analyseStatisticsCwhereDao.deleteBatch(ids);
	}

	@Override
	public String getMaxNo() throws Exception {
		String maxId=analyseStatisticsCwhereDao.getMaxNo();
		if(maxId==null||maxId.equals("null")) {
			maxId="00000000000001";
		}else {
			maxId="00000000000000"+(Integer.valueOf(maxId)+1);
			maxId=maxId.substring(maxId.length()-14, maxId.length());
		}
		return maxId;
	}

	@Override
	public int saveBatchList(String fid,List<AnalyseStatisticsCwhereBean> list) throws Exception {
		if(list!=null&&list.size()>0) {
			int len = list.size();
			for(int i =0;i<len;i++) {
				if(list.get(i).getField()!=null&&list.get(i).getField().trim().length()>0) {
					list.get(i).setFid(fid);
					save(list.get(i));
				}
			}
		}
		return 0;
	}
	
}
