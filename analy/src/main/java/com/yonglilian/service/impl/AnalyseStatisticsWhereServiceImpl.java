package com.yonglilian.service.impl;

import com.yonglilian.bean.AnalyseStatisticsCconnectionBean;
import com.yonglilian.bean.AnalyseStatisticsWhereBean;
import com.yonglilian.common.util.SysPreperty;
import com.yonglilian.dao.AnalyseStatisticsWhereDao;
import com.yonglilian.domain.AnalyseStatisticsWhere;
import com.yonglilian.service.AnalyseStatisticsWhereService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zr.zrpower.common.db.DBEngine;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Service("analyseStatisticsWhereService")
public class AnalyseStatisticsWhereServiceImpl implements AnalyseStatisticsWhereService {
    /** The QueryConfigServiceImpl Logger. */
	@SuppressWarnings("unused")
	private static final Logger LOGGER = LoggerFactory.getLogger(AnalyseStatisticsWhereServiceImpl.class);
	private DBEngine dbEngine;
	private static int clients = 0;
	String DataBaseType = SysPreperty.getProperty().DataBaseType;// 数据库类型
	
	
	@Autowired
	private AnalyseStatisticsWhereDao analyseStatisticsWhereDao;
	
	/**
	 * 构造方法
	 */
	public AnalyseStatisticsWhereServiceImpl() {
		dbEngine = new DBEngine(SysPreperty.getProperty().MainDataSource, 
				SysPreperty.getProperty().IsConvert);
		dbEngine.initialize();
		if (clients < 1) {
			// clients something todo
		}
		clients = 1;
	}
	
	@Override
	public AnalyseStatisticsWhereBean queryObject(String id) throws Exception{
	AnalyseStatisticsWhereBean analyseStatisticsWhereBean = null;
		AnalyseStatisticsWhere analyseStatisticsWhere =  analyseStatisticsWhereDao.queryObject(id);
			if( analyseStatisticsWhere != null ) {
			analyseStatisticsWhereBean = new AnalyseStatisticsWhereBean();
			BeanUtils.copyProperties( analyseStatisticsWhere, analyseStatisticsWhereBean );
		}
		return analyseStatisticsWhereBean;
	}
	
	@Override
	public List<AnalyseStatisticsWhereBean> queryList(Map<String, Object> map) throws Exception{
		List<AnalyseStatisticsWhereBean> analyseStatisticsWhereBeans = new ArrayList<>();
		List<AnalyseStatisticsWhere> list = analyseStatisticsWhereDao.queryList( map );
		for( AnalyseStatisticsWhere analyseStatisticsWhere : list ) {
			AnalyseStatisticsWhereBean analyseStatisticsWhereBean = new AnalyseStatisticsWhereBean();
			BeanUtils.copyProperties( analyseStatisticsWhere, analyseStatisticsWhereBean );
			analyseStatisticsWhereBeans.add( analyseStatisticsWhereBean );
		}
		return analyseStatisticsWhereBeans;
	}
	
	@Override
	public int queryTotal(Map<String, Object> map) throws Exception{
		return analyseStatisticsWhereDao.queryTotal(map);
	}
	
	@Override
	public void save(AnalyseStatisticsWhereBean analyseStatisticsWhereBean) throws Exception{
		AnalyseStatisticsWhere analyseStatisticsWhere = new AnalyseStatisticsWhere();
		BeanUtils.copyProperties( analyseStatisticsWhereBean, analyseStatisticsWhere );
		analyseStatisticsWhere.setId(getMaxNo());
		if(analyseStatisticsWhere.getField()!=null&&analyseStatisticsWhere.getField().trim().length()>0) {
			analyseStatisticsWhereDao.save(analyseStatisticsWhere);
		}
	}
	
	@Override
	public void update(AnalyseStatisticsWhereBean analyseStatisticsWhereBean) throws Exception{
	AnalyseStatisticsWhere analyseStatisticsWhere = new AnalyseStatisticsWhere();
		BeanUtils.copyProperties( analyseStatisticsWhereBean, analyseStatisticsWhere );
		analyseStatisticsWhereDao.update(analyseStatisticsWhere);
	}
	
	@Override
	public void delete(String id) throws Exception{
		analyseStatisticsWhereDao.delete(id);
	}
	
	@Override
	public void deleteBatch(String[] ids) throws Exception{
		analyseStatisticsWhereDao.deleteBatch(ids);
	}

	@Override
	public String getMaxNo() throws Exception {
		String maxId=analyseStatisticsWhereDao.getMaxNo();
		if(maxId==null||maxId.equals("null")) {
			maxId="000000000001";
		}else {
			maxId="000000000000"+(Integer.valueOf(maxId)+1);
			maxId=maxId.substring(maxId.length()-12, maxId.length());
		}
		return maxId;
	}

	@Override
	public int saveBatchList(List<AnalyseStatisticsCconnectionBean> list) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}
	
}
