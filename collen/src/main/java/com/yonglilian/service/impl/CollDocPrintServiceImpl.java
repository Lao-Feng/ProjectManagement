package com.yonglilian.service.impl;

import com.yonglilian.bean.CollDocPrintBean;
import com.yonglilian.dao.CollDocPrintDao;
import com.yonglilian.domain.CollDocPrint;
import com.yonglilian.service.CollDocPrintService;
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


@Service("collDocPrintService")
public class CollDocPrintServiceImpl implements CollDocPrintService {
    /** The QueryConfigServiceImpl Logger. */
	@SuppressWarnings("unused")
	private static final Logger LOGGER = LoggerFactory.getLogger(CollDocPrintServiceImpl.class);
	private DBEngine dbEngine;
	private static int clients = 0;
	String DataBaseType = SysPreperty.getProperty().DataBaseType;// 数据库类型
	
	
	@Autowired
	private CollDocPrintDao collDocPrintDao;
	
	/**
	 * 构造方法
	 */
	public CollDocPrintServiceImpl() {
		dbEngine = new DBEngine(SysPreperty.getProperty().MainDataSource, 
				SysPreperty.getProperty().IsConvert);
		dbEngine.initialize();
		if (clients < 1) {
			// clients something todo
		}
		clients = 1;
	}
	
	@Override
	public CollDocPrintBean queryObject(String id) throws Exception{
	CollDocPrintBean collDocPrintBean = null;
		CollDocPrint collDocPrint =  collDocPrintDao.queryObject(id);
			if( collDocPrint != null ) {
			collDocPrintBean = new CollDocPrintBean();
			BeanUtils.copyProperties( collDocPrint, collDocPrintBean );
		}
		return collDocPrintBean;
	}
	
	@Override
	public List<CollDocPrintBean> queryList(Map<String, Object> map) throws Exception{
		List<CollDocPrintBean> collDocPrintBeans = new ArrayList<>();
		List<CollDocPrint> list = collDocPrintDao.queryList( map );
		for( CollDocPrint collDocPrint : list ) {
			CollDocPrintBean collDocPrintBean = new CollDocPrintBean();
			BeanUtils.copyProperties( collDocPrint, collDocPrintBean );
			collDocPrintBeans.add( collDocPrintBean );
		}
		return collDocPrintBeans;
	}
	
	@Override
	public int queryTotal(Map<String, Object> map) throws Exception{
		return collDocPrintDao.queryTotal(map);
	}
	
	@Override
	public void save(CollDocPrintBean collDocPrintBean) throws Exception{
		CollDocPrint collDocPrint = new CollDocPrint();
		BeanUtils.copyProperties( collDocPrintBean, collDocPrint );
		if(collDocPrint.getId()!=null&& collDocPrint.getId().length()>0) {//修改
			collDocPrintDao.update(collDocPrint);
		}else {
			collDocPrint.setId(getMaxNo());
			collDocPrintDao.save(collDocPrint);
		}
	}
	
	@Override
	public void update(CollDocPrintBean collDocPrintBean) throws Exception{
	CollDocPrint collDocPrint = new CollDocPrint();
		BeanUtils.copyProperties( collDocPrintBean, collDocPrint );
		collDocPrintDao.update(collDocPrint);
	}
	
	@Override
	public void delete(String id) throws Exception{
		collDocPrintDao.delete(id);
	}
	
	@Override
	public void deleteBatch(String[] ids) throws Exception{
		collDocPrintDao.deleteBatch(ids);
	}

	@Override
	public String getMaxNo() throws Exception {
		String maxId=collDocPrintDao.getMaxNo();
		if(maxId==null||maxId.equals("null")) {
			maxId="00000001";
		}else {
			maxId="00000000"+(Integer.valueOf(maxId)+1);
			maxId=maxId.substring(maxId.length()-8, maxId.length());
		}
		return maxId;
	}
	
}
