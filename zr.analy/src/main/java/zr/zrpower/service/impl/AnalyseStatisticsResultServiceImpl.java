package zr.zrpower.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zr.zrpower.bean.AnalyseStatisticsCconnectionBean;
import zr.zrpower.bean.AnalyseStatisticsResultBean;
import zr.zrpower.common.db.DBEngine;
import zr.zrpower.common.util.SysPreperty;
import zr.zrpower.dao.AnalyseStatisticsResultDao;
import zr.zrpower.domain.AnalyseStatisticsResult;
import zr.zrpower.service.AnalyseStatisticsResultService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Service("analyseStatisticsResultService")
public class AnalyseStatisticsResultServiceImpl implements AnalyseStatisticsResultService {
    /** The QueryConfigServiceImpl Logger. */
	@SuppressWarnings("unused")
	private static final Logger LOGGER = LoggerFactory.getLogger(AnalyseStatisticsResultServiceImpl.class);
	private DBEngine dbEngine;
	private static int clients = 0;
	String DataBaseType = SysPreperty.getProperty().DataBaseType;// 数据库类型
	
	
	@Autowired
	private AnalyseStatisticsResultDao analyseStatisticsResultDao;
	
	/**
	 * 构造方法
	 */
	public AnalyseStatisticsResultServiceImpl() {
		dbEngine = new DBEngine(SysPreperty.getProperty().MainDataSource, 
				SysPreperty.getProperty().IsConvert);
		dbEngine.initialize();
		if (clients < 1) {
			// clients something todo
		}
		clients = 1;
	}
	
	@Override
	public AnalyseStatisticsResultBean queryObject(String id) throws Exception{
	AnalyseStatisticsResultBean analyseStatisticsResultBean = null;
		AnalyseStatisticsResult analyseStatisticsResult =  analyseStatisticsResultDao.queryObject(id);
			if( analyseStatisticsResult != null ) {
			analyseStatisticsResultBean = new AnalyseStatisticsResultBean();
			BeanUtils.copyProperties( analyseStatisticsResult, analyseStatisticsResultBean );
		}
		return analyseStatisticsResultBean;
	}
	
	@Override
	public List<AnalyseStatisticsResultBean> queryList(Map<String, Object> map) throws Exception{
		List<AnalyseStatisticsResultBean> analyseStatisticsResultBeans = new ArrayList<>();
		List<AnalyseStatisticsResult> list = analyseStatisticsResultDao.queryList( map );
		for( AnalyseStatisticsResult analyseStatisticsResult : list ) {
			AnalyseStatisticsResultBean analyseStatisticsResultBean = new AnalyseStatisticsResultBean();
			BeanUtils.copyProperties( analyseStatisticsResult, analyseStatisticsResultBean );
			analyseStatisticsResultBeans.add( analyseStatisticsResultBean );
		}
		return analyseStatisticsResultBeans;
	}
	
	@Override
	public int queryTotal(Map<String, Object> map) throws Exception{
		return analyseStatisticsResultDao.queryTotal(map);
	}
	
	@Override
	public void save(AnalyseStatisticsResultBean analyseStatisticsResultBean) throws Exception{
		AnalyseStatisticsResult analyseStatisticsResult = new AnalyseStatisticsResult();
		BeanUtils.copyProperties( analyseStatisticsResultBean, analyseStatisticsResult );
		analyseStatisticsResultDao.save(analyseStatisticsResult);
	}
	
	@Override
	public void update(AnalyseStatisticsResultBean analyseStatisticsResultBean) throws Exception{
	AnalyseStatisticsResult analyseStatisticsResult = new AnalyseStatisticsResult();
		BeanUtils.copyProperties( analyseStatisticsResultBean, analyseStatisticsResult );
		analyseStatisticsResultDao.update(analyseStatisticsResult);
	}
	
	@Override
	public void delete(String id) throws Exception{
		analyseStatisticsResultDao.delete(id);
	}
	
	@Override
	public void deleteBatch(String[] ids) throws Exception{
		analyseStatisticsResultDao.deleteBatch(ids);
	}

	@Override
	public String getMaxNo() throws Exception {
		String maxId=analyseStatisticsResultDao.getMaxNo();
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
