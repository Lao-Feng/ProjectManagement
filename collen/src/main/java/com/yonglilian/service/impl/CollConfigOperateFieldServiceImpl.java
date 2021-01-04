package com.yonglilian.service.impl;

import com.yonglilian.bean.CollConfigOperateFieldBean;
import com.yonglilian.common.util.SysPreperty;
import com.yonglilian.dao.CollConfigOperateFieldDao;
import com.yonglilian.domain.CollConfigOperateField;
import com.yonglilian.service.CollConfigOperateFieldService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zr.zrpower.common.db.DBEngine;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Service("collConfigOperateFieldService")
public class CollConfigOperateFieldServiceImpl implements CollConfigOperateFieldService {
    /** The QueryConfigServiceImpl Logger. */
	@SuppressWarnings("unused")
	private static final Logger LOGGER = LoggerFactory.getLogger(CollConfigOperateFieldServiceImpl.class);
	private DBEngine dbEngine;
	private static int clients = 0;
	String DataBaseType = SysPreperty.getProperty().DataBaseType;// 数据库类型
	
	
	@Autowired
	private CollConfigOperateFieldDao collConfigOperateFieldDao;
	
	/**
	 * 构造方法
	 */
	public CollConfigOperateFieldServiceImpl() {
		dbEngine = new DBEngine(SysPreperty.getProperty().MainDataSource, 
				SysPreperty.getProperty().IsConvert);
		dbEngine.initialize();
		if (clients < 1) {
			// clients something todo
		}
		clients = 1;
	}
	
	@Override
	public CollConfigOperateFieldBean queryObject(String id) throws Exception{
	CollConfigOperateFieldBean collConfigOperateFieldBean = null;
		CollConfigOperateField collConfigOperateField =  collConfigOperateFieldDao.queryObject(id);
			if( collConfigOperateField != null ) {
			collConfigOperateFieldBean = new CollConfigOperateFieldBean();
			BeanUtils.copyProperties( collConfigOperateField, collConfigOperateFieldBean );
		}
		return collConfigOperateFieldBean;
	}
	
	@Override
	public List<CollConfigOperateFieldBean> queryList(Map<String, Object> map) throws Exception{
		List<CollConfigOperateFieldBean> collConfigOperateFieldBeans = new ArrayList<>();
		List<CollConfigOperateField> list = collConfigOperateFieldDao.queryList( map );
		for( CollConfigOperateField collConfigOperateField : list ) {
			CollConfigOperateFieldBean collConfigOperateFieldBean = new CollConfigOperateFieldBean();
			BeanUtils.copyProperties( collConfigOperateField, collConfigOperateFieldBean );
			collConfigOperateFieldBeans.add( collConfigOperateFieldBean );
		}
		return collConfigOperateFieldBeans;
	}
	
	@Override
	public int queryTotal(Map<String, Object> map) throws Exception{
		return collConfigOperateFieldDao.queryTotal(map);
	}
	
	@Override
	public void save(CollConfigOperateFieldBean collConfigOperateFieldBean) throws Exception{
		CollConfigOperateField collConfigOperateField = new CollConfigOperateField();
		BeanUtils.copyProperties( collConfigOperateFieldBean, collConfigOperateField );
		collConfigOperateField.setId(getMaxNo());
		collConfigOperateFieldDao.save(collConfigOperateField);
	}
	
	@Override
	public void update(CollConfigOperateFieldBean collConfigOperateFieldBean) throws Exception{
	CollConfigOperateField collConfigOperateField = new CollConfigOperateField();
		BeanUtils.copyProperties( collConfigOperateFieldBean, collConfigOperateField );
		collConfigOperateFieldDao.update(collConfigOperateField);
	}
	
	@Override
	public void delete(String id) throws Exception{
		collConfigOperateFieldDao.delete(id);
	}
	
	@Override
	public void deleteBatch(String[] ids) throws Exception{
		collConfigOperateFieldDao.deleteBatch(ids);
	}

	@Override
	public String getMaxNo() throws Exception {
		String maxId=collConfigOperateFieldDao.getMaxNo();
		if(maxId==null||maxId.equals("null")) {
			maxId="00000001";
		}else {
			maxId="00000000"+(Integer.valueOf(maxId)+1);
			maxId=maxId.substring(maxId.length()-8, maxId.length());
		}
		return maxId;
	}

	@Override
	public int saveList(List<CollConfigOperateFieldBean> list) throws Exception {
		int num = 0;
		if(list!=null && list.size()>0) {
			int len = list.size();
			for(int i = 0; i< len ; i++) {
				if(list.get(i).getIsdisplay()!=null&&list.get(i).getIsdisplay().equals("true")) {
					list.get(i).setIsdisplay("1");
				}else {
					list.get(i).setIsdisplay("0");
				}
				if(list.get(i).getIsedit()!=null&&list.get(i).getIsedit().equals("true")) {
					list.get(i).setIsedit("1");
				}else {
					list.get(i).setIsedit("0");
				}
				if(list.get(i).getIsforce()!=null&&list.get(i).getIsforce().equals("true")) {
					list.get(i).setIsforce("1");
				}else {
					list.get(i).setIsforce("0");
				}
				if(list.get(i).getIsmustfill()!=null&&list.get(i).getIsmustfill().equals("true")) {
					list.get(i).setIsmustfill("1");
				}else {
					list.get(i).setIsmustfill("0");
				}
				save(list.get(i));
				num++;
			}
		}
		return num;
	}
	
}
