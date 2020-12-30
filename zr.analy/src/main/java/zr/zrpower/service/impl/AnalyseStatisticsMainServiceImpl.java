package zr.zrpower.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zr.zrpower.bean.AnalyseStatisticsCconnectionBean;
import zr.zrpower.bean.AnalyseStatisticsMainBean;
import zr.zrpower.common.db.DBEngine;
import zr.zrpower.common.util.SysPreperty;
import zr.zrpower.dao.AnalyseStatisticsMainDao;
import zr.zrpower.domain.AnalyseStatisticsMain;
import zr.zrpower.service.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Service("analyseStatisticsMainService")
public class AnalyseStatisticsMainServiceImpl implements AnalyseStatisticsMainService {
    /** The QueryConfigServiceImpl Logger. */
	@SuppressWarnings("unused")
	private static final Logger LOGGER = LoggerFactory.getLogger(AnalyseStatisticsMainServiceImpl.class);
	private DBEngine dbEngine;
	private static int clients = 0;
	String DataBaseType = SysPreperty.getProperty().DataBaseType;// 数据库类型
	
	
	@Autowired
	private AnalyseStatisticsMainDao analyseStatisticsMainDao;
	@SuppressWarnings("unused")
	@Autowired
	private AnalyseStatisticsCconnectionService analyseStatisticsCconnectionService;
	@Autowired
	private AnalyseStatisticsCfieldService analyseStatisticsCfieldService;
	@Autowired
	private AnalyseStatisticsCustomService analyseStatisticsCustomService;
	@SuppressWarnings("unused")
	@Autowired
	private AnalyseStatisticsCwhereService analyseStatisticsCwhereService;
	@Autowired
	private AnalyseStatisticsResultService analyseStatisticsResultService;
	@Autowired
	private AnalyseStatisticsWhereService analyseStatisticsWhereService;
	
	
	/**
	 * 构造方法
	 */
	public AnalyseStatisticsMainServiceImpl() {
		dbEngine = new DBEngine(SysPreperty.getProperty().MainDataSource, 
				SysPreperty.getProperty().IsConvert);
		dbEngine.initialize();
		if (clients < 1) {
			// clients something todo
		}
		clients = 1;
	}
	
	@Override
	public AnalyseStatisticsMainBean queryObject(String id) throws Exception{
	AnalyseStatisticsMainBean analyseStatisticsMainBean = null;
		AnalyseStatisticsMain analyseStatisticsMain =  analyseStatisticsMainDao.queryObject(id);
			if( analyseStatisticsMain != null ) {
			analyseStatisticsMainBean = new AnalyseStatisticsMainBean();
			BeanUtils.copyProperties( analyseStatisticsMain, analyseStatisticsMainBean );
		}
		return analyseStatisticsMainBean;
	}
	
	@Override
	public List<AnalyseStatisticsMainBean> queryList(Map<String, Object> map) throws Exception{
		List<AnalyseStatisticsMainBean> analyseStatisticsMainBeans = new ArrayList<>();
		List<AnalyseStatisticsMain> list = analyseStatisticsMainDao.queryList( map );
		for( AnalyseStatisticsMain analyseStatisticsMain : list ) {
			AnalyseStatisticsMainBean analyseStatisticsMainBean = new AnalyseStatisticsMainBean();
			BeanUtils.copyProperties( analyseStatisticsMain, analyseStatisticsMainBean );
			analyseStatisticsMainBeans.add( analyseStatisticsMainBean );
		}
		return analyseStatisticsMainBeans;
	}
	
	@Override
	public int queryTotal(Map<String, Object> map) throws Exception{
		return analyseStatisticsMainDao.queryTotal(map);
	}
	
	@Override
	public void save(AnalyseStatisticsMainBean analyseStatisticsMainBean) throws Exception{
		AnalyseStatisticsMain analyseStatisticsMain = new AnalyseStatisticsMain();
		BeanUtils.copyProperties( analyseStatisticsMainBean, analyseStatisticsMain );
		analyseStatisticsMain.setId(getMaxNo());
		analyseStatisticsMainDao.save(analyseStatisticsMain);
	}
	
	@Override
	public void update(AnalyseStatisticsMainBean analyseStatisticsMainBean) throws Exception{
	AnalyseStatisticsMain analyseStatisticsMain = new AnalyseStatisticsMain();
		BeanUtils.copyProperties( analyseStatisticsMainBean, analyseStatisticsMain );
		analyseStatisticsMainDao.update(analyseStatisticsMain);
	}
	
	@Override
	public void delete(String id) throws Exception{
		analyseStatisticsMainDao.delete(id);
		String[] ids= new String[1];
		ids[0]=id;
		analyseStatisticsCfieldService.deleteBatch(ids);
//		analyseStatisticsCconnectionDao.delete(id);
//		analyseStatisticsCwhereService.delete(id);
		analyseStatisticsCustomService.delete(id);
		analyseStatisticsResultService.delete(id);
		analyseStatisticsWhereService.delete(id);
	}
	
	@Override
	public void deleteBatch(String[] ids) throws Exception{
		analyseStatisticsMainDao.deleteBatch(ids);
	}

	@Override
	public String getMaxNo() throws Exception {
		String maxId = analyseStatisticsMainDao.getMaxNo();
		if (maxId == null || maxId.equals("null")) {
			maxId = "00000001";
		} else {
			maxId = "00000000" + (Integer.valueOf(maxId) + 1);
			maxId = maxId.substring(maxId.length() - 8, maxId.length());
		}
		return maxId;
	}

	@Override
	public int saveBatchList(List<AnalyseStatisticsCconnectionBean> list) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}
	
}
