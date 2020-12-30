package zr.zrpower.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zr.zrpower.bean.AnalyseStatisticsCconnectionBean;
import zr.zrpower.common.db.DBEngine;
import zr.zrpower.common.util.SysPreperty;
import zr.zrpower.dao.AnalyseStatisticsCconnectionDao;
import zr.zrpower.domain.AnalyseStatisticsCconnection;
import zr.zrpower.service.AnalyseStatisticsCconnectionService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Service("analyseStatisticsCconnectionService")
public class AnalyseStatisticsCconnectionServiceImpl implements AnalyseStatisticsCconnectionService {
    /** The QueryConfigServiceImpl Logger. */
	@SuppressWarnings("unused")
	private static final Logger LOGGER = LoggerFactory.getLogger(AnalyseStatisticsCconnectionServiceImpl.class);
	private DBEngine dbEngine;
	private static int clients = 0;
	String DataBaseType = SysPreperty.getProperty().DataBaseType;// 数据库类型
	
	
	@Autowired
	private AnalyseStatisticsCconnectionDao analyseStatisticsCconnectionDao;
	
	/**
	 * 构造方法
	 */
	public AnalyseStatisticsCconnectionServiceImpl() {
		dbEngine = new DBEngine(SysPreperty.getProperty().MainDataSource, 
				SysPreperty.getProperty().IsConvert);
		dbEngine.initialize();
		if (clients < 1) {
			// clients something todo
		}
		clients = 1;
	}
	
	@Override
	public AnalyseStatisticsCconnectionBean queryObject(String id) throws Exception{
	AnalyseStatisticsCconnectionBean analyseStatisticsCconnectionBean = null;
		AnalyseStatisticsCconnection analyseStatisticsCconnection =  analyseStatisticsCconnectionDao.queryObject(id);
			if( analyseStatisticsCconnection != null ) {
			analyseStatisticsCconnectionBean = new AnalyseStatisticsCconnectionBean();
			BeanUtils.copyProperties( analyseStatisticsCconnection, analyseStatisticsCconnectionBean );
		}
		return analyseStatisticsCconnectionBean;
	}
	
	@Override
	public List<AnalyseStatisticsCconnectionBean> queryList(Map<String, Object> map) throws Exception{
		List<AnalyseStatisticsCconnectionBean> analyseStatisticsCconnectionBeans = new ArrayList<>();
		List<AnalyseStatisticsCconnection> list = analyseStatisticsCconnectionDao.queryList( map );
		for( AnalyseStatisticsCconnection analyseStatisticsCconnection : list ) {
			AnalyseStatisticsCconnectionBean analyseStatisticsCconnectionBean = new AnalyseStatisticsCconnectionBean();
			BeanUtils.copyProperties( analyseStatisticsCconnection, analyseStatisticsCconnectionBean );
			analyseStatisticsCconnectionBean.setMfieldtable(analyseStatisticsCconnection.getMfield().split("\\.")[0]);
			analyseStatisticsCconnectionBean.setCfieldtable(analyseStatisticsCconnection.getCfield().split("\\.")[0]);
			analyseStatisticsCconnectionBean.setMfield(analyseStatisticsCconnection.getMfield().split("\\.")[1]);
			analyseStatisticsCconnectionBean.setCfield(analyseStatisticsCconnection.getCfield().split("\\.")[1]);
			analyseStatisticsCconnectionBeans.add( analyseStatisticsCconnectionBean );
		}
		return analyseStatisticsCconnectionBeans;
	}
	
	@Override
	public int queryTotal(Map<String, Object> map) throws Exception{
		return analyseStatisticsCconnectionDao.queryTotal(map);
	}
	
	@Override
	public void save(AnalyseStatisticsCconnectionBean analyseStatisticsCconnectionBean) throws Exception{
		AnalyseStatisticsCconnection analyseStatisticsCconnection = new AnalyseStatisticsCconnection();
		BeanUtils.copyProperties( analyseStatisticsCconnectionBean, analyseStatisticsCconnection );
		analyseStatisticsCconnection.setId(getMaxNo());
		analyseStatisticsCconnection.setCfield(analyseStatisticsCconnectionBean.getCfieldtable()+"."+analyseStatisticsCconnectionBean.getCfield());
		analyseStatisticsCconnection.setMfield(analyseStatisticsCconnectionBean.getMfieldtable()+"."+analyseStatisticsCconnectionBean.getMfield());
		analyseStatisticsCconnectionDao.save(analyseStatisticsCconnection);
	}
	
	@Override
	public void update(AnalyseStatisticsCconnectionBean analyseStatisticsCconnectionBean) throws Exception{
	AnalyseStatisticsCconnection analyseStatisticsCconnection = new AnalyseStatisticsCconnection();
		BeanUtils.copyProperties( analyseStatisticsCconnectionBean, analyseStatisticsCconnection );
		analyseStatisticsCconnectionDao.update(analyseStatisticsCconnection);
	}
	
	@Override
	public void delete(String id) throws Exception{
		analyseStatisticsCconnectionDao.delete(id);
	}
	
	@Override
	public void deleteBatch(String[] ids) throws Exception{
		analyseStatisticsCconnectionDao.deleteBatch(ids);
	}

	@Override
	public String getMaxNo() throws Exception {
		String maxId=analyseStatisticsCconnectionDao.getMaxNo();
		if(maxId==null||maxId.equals("null")) {
			maxId="00000000000001";
		}else {
			maxId="00000000000000"+(Integer.valueOf(maxId)+1);
			maxId=maxId.substring(maxId.length()-14, maxId.length());
		}
		return maxId;
	}

	@Override
	public int saveBatchList(String fid,List<AnalyseStatisticsCconnectionBean> list) throws Exception {
		if(list!=null&&list.size()>0) {
			int len = list.size();
			for(int i =0;i<len;i++) {
				if((list.get(i).getCfield()!=null&&list.get(i).getCfield().trim().length()>0)&&
				   (list.get(i).getMfield()!=null&&list.get(i).getMfield().trim().length()>0)) {
					list.get(i).setFid(fid);
					save(list.get(i));
				}
			}
		}
		return 0;
	}
	
}
