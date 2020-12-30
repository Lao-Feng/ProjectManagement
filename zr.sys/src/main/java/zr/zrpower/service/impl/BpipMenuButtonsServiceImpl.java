package zr.zrpower.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zr.zrpower.common.db.DBEngine;
import zr.zrpower.common.util.SysPreperty;
import zr.zrpower.dao.mapper.BpipMenuButtonsMapper;
import zr.zrpower.model.BpipMenuButtons;
import zr.zrpower.service.BpipMenuButtonsService;

import java.util.List;
import java.util.Map;


@Service("bpipMenuButtonsService")
public class BpipMenuButtonsServiceImpl implements BpipMenuButtonsService {
    /** The QueryConfigServiceImpl Logger. */
	@SuppressWarnings("unused")
	private static final Logger LOGGER = LoggerFactory.getLogger(BpipMenuButtonsServiceImpl.class);
	private DBEngine dbEngine;
	private static int clients = 0;
	String DataBaseType = SysPreperty.getProperty().DataBaseType;// 数据库类型
	
	
	@Autowired
	private BpipMenuButtonsMapper bpipMenuButtonsDao;
	
	/**
	 * 构造方法
	 */
	public BpipMenuButtonsServiceImpl() {
		dbEngine = new DBEngine(SysPreperty.getProperty().MainDataSource, 
				SysPreperty.getProperty().IsConvert);
		dbEngine.initialize();
		if (clients < 1) {
			// clients something todo
		}
		clients = 1;
	}
	
	@Override
	public BpipMenuButtons queryObject(Long id) throws Exception{
		BpipMenuButtons bpipMenuButtons =  bpipMenuButtonsDao.queryObject(id);
		return bpipMenuButtons;
	}
	
	@Override
	public List<BpipMenuButtons> queryList(Map<String, Object> map) throws Exception{
		List<BpipMenuButtons> list = bpipMenuButtonsDao.queryList( map );
		
		return list;
	}
	
	@Override
	public int queryTotal(Map<String, Object> map) throws Exception{
		return bpipMenuButtonsDao.queryTotal(map);
	}
	
	@Override
	public void save(BpipMenuButtons bpipMenuButtonsBean) throws Exception{
		bpipMenuButtonsDao.save(bpipMenuButtonsBean);
	}
	
	@Override
	public void update(BpipMenuButtons bpipMenuButtonsBean) throws Exception{
		bpipMenuButtonsDao.update(bpipMenuButtonsBean);
	}
	
	@Override
	public void delete(Long id) throws Exception{
		bpipMenuButtonsDao.delete(id);
	}
	
	@Override
	public void deleteBatch(Long[] ids) throws Exception{
		bpipMenuButtonsDao.deleteBatch(ids);
	}

}
