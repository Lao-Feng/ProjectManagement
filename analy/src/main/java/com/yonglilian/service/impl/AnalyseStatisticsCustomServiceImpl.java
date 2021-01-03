package com.yonglilian.service.impl;

import com.yonglilian.bean.AnalyseStatisticsCconnectionBean;
import com.yonglilian.bean.AnalyseStatisticsCustomBean;
import com.yonglilian.dao.AnalyseStatisticsCustomDao;
import com.yonglilian.domain.AnalyseStatisticsCustom;
import com.yonglilian.service.AnalyseStatisticsCustomService;
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


@Service("analyseStatisticsCustomService")
public class AnalyseStatisticsCustomServiceImpl implements AnalyseStatisticsCustomService {
    /** The QueryConfigServiceImpl Logger. */
	@SuppressWarnings("unused")
	private static final Logger LOGGER = LoggerFactory.getLogger(AnalyseStatisticsCustomServiceImpl.class);
	private DBEngine dbEngine;
	private static int clients = 0;
	String DataBaseType = SysPreperty.getProperty().DataBaseType;// 数据库类型
	
	
	@Autowired
	private AnalyseStatisticsCustomDao analyseStatisticsCustomDao;
	
	/**
	 * 构造方法
	 */
	public AnalyseStatisticsCustomServiceImpl() {
		dbEngine = new DBEngine(SysPreperty.getProperty().MainDataSource, 
				SysPreperty.getProperty().IsConvert);
		dbEngine.initialize();
		if (clients < 1) {
			// clients something todo
		}
		clients = 1;
	}
	
	@Override
	public AnalyseStatisticsCustomBean queryObject(String id) throws Exception{
	AnalyseStatisticsCustomBean analyseStatisticsCustomBean = null;
		AnalyseStatisticsCustom analyseStatisticsCustom =  analyseStatisticsCustomDao.queryObject(id);
			if( analyseStatisticsCustom != null ) {
			analyseStatisticsCustomBean = new AnalyseStatisticsCustomBean();
			BeanUtils.copyProperties( analyseStatisticsCustom, analyseStatisticsCustomBean );
		}
		return analyseStatisticsCustomBean;
	}
	
	@Override
	public List<AnalyseStatisticsCustomBean> queryList(Map<String, Object> map) throws Exception{
		List<AnalyseStatisticsCustomBean> analyseStatisticsCustomBeans = new ArrayList<>();
		List<AnalyseStatisticsCustom> list = analyseStatisticsCustomDao.queryList( map );
		for( AnalyseStatisticsCustom analyseStatisticsCustom : list ) {
			AnalyseStatisticsCustomBean analyseStatisticsCustomBean = new AnalyseStatisticsCustomBean();
			BeanUtils.copyProperties( analyseStatisticsCustom, analyseStatisticsCustomBean );
			analyseStatisticsCustomBeans.add( analyseStatisticsCustomBean );
		}
		return analyseStatisticsCustomBeans;
	}
	
	@Override
	public int queryTotal(Map<String, Object> map) throws Exception{
		return analyseStatisticsCustomDao.queryTotal(map);
	}
	
	@Override
	public void save(AnalyseStatisticsCustomBean analyseStatisticsCustomBean) throws Exception{
		AnalyseStatisticsCustom analyseStatisticsCustom = new AnalyseStatisticsCustom();
		BeanUtils.copyProperties( analyseStatisticsCustomBean, analyseStatisticsCustom );
		analyseStatisticsCustomDao.save(analyseStatisticsCustom);
	}
	
	@Override
	public void update(AnalyseStatisticsCustomBean analyseStatisticsCustomBean) throws Exception{
	AnalyseStatisticsCustom analyseStatisticsCustom = new AnalyseStatisticsCustom();
		BeanUtils.copyProperties( analyseStatisticsCustomBean, analyseStatisticsCustom );
		analyseStatisticsCustomDao.update(analyseStatisticsCustom);
	}
	
	@Override
	public void delete(String id) throws Exception{
		analyseStatisticsCustomDao.delete(id);
	}
	
	@Override
	public void deleteBatch(String[] ids) throws Exception{
		analyseStatisticsCustomDao.deleteBatch(ids);
	}

	@Override
	public String getMaxNo() throws Exception {
		String maxId=analyseStatisticsCustomDao.getMaxNo();
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
