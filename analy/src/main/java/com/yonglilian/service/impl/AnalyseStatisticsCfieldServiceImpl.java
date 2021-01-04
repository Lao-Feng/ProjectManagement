package com.yonglilian.service.impl;

import com.yonglilian.bean.AnalyseStatisticsCconnectionBean;
import com.yonglilian.bean.AnalyseStatisticsCfieldBean;
import com.yonglilian.bean.AnalyseStatisticsCwhereBean;
import com.yonglilian.common.util.SysPreperty;
import com.yonglilian.dao.AnalyseStatisticsCfieldDao;
import com.yonglilian.domain.AnalyseStatisticsCfield;
import com.yonglilian.service.AnalyseStatisticsCconnectionService;
import com.yonglilian.service.AnalyseStatisticsCfieldService;
import com.yonglilian.service.AnalyseStatisticsCwhereService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zr.zrpower.common.db.DBEngine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service("analyseStatisticsCfieldService")
public class AnalyseStatisticsCfieldServiceImpl implements AnalyseStatisticsCfieldService {
    /** The QueryConfigServiceImpl Logger. */
	@SuppressWarnings("unused")
	private static final Logger LOGGER = LoggerFactory.getLogger(AnalyseStatisticsCfieldServiceImpl.class);
	private DBEngine dbEngine;
	private static int clients = 0;
	String DataBaseType = SysPreperty.getProperty().DataBaseType;// 数据库类型
	
	
	@Autowired
	private AnalyseStatisticsCfieldDao analyseStatisticsCfieldDao;
	
	@Autowired
	private AnalyseStatisticsCconnectionService analyseStatisticsCconnectionService;
	@Autowired
	private AnalyseStatisticsCwhereService analyseStatisticsCwhereService;
	
	/**
	 * 构造方法
	 */
	public AnalyseStatisticsCfieldServiceImpl() {
		dbEngine = new DBEngine(SysPreperty.getProperty().MainDataSource, 
				SysPreperty.getProperty().IsConvert);
		dbEngine.initialize();
		if (clients < 1) {
			// clients something todo
		}
		clients = 1;
	}
	
	@Override
	public AnalyseStatisticsCfieldBean queryObject(String id) throws Exception{
	AnalyseStatisticsCfieldBean analyseStatisticsCfieldBean = null;
		AnalyseStatisticsCfield analyseStatisticsCfield =  analyseStatisticsCfieldDao.queryObject(id);
			if( analyseStatisticsCfield != null ) {
			analyseStatisticsCfieldBean = new AnalyseStatisticsCfieldBean();
			BeanUtils.copyProperties( analyseStatisticsCfield, analyseStatisticsCfieldBean );
		}
		return analyseStatisticsCfieldBean;
	}
	
	@Override
	public List<AnalyseStatisticsCfieldBean> queryList(Map<String, Object> map) throws Exception{
		List<AnalyseStatisticsCfieldBean> analyseStatisticsCfieldBeans = new ArrayList<>();
		List<AnalyseStatisticsCfield> list = analyseStatisticsCfieldDao.queryList( map );
		for( AnalyseStatisticsCfield analyseStatisticsCfield : list ) {
			AnalyseStatisticsCfieldBean analyseStatisticsCfieldBean = new AnalyseStatisticsCfieldBean();
			BeanUtils.copyProperties( analyseStatisticsCfield, analyseStatisticsCfieldBean );
			
			Map<String, Object> fieldmap = new HashMap<String, Object>();
			fieldmap.put("fid", analyseStatisticsCfield.getId());
			//查询字段关联属性
			List<AnalyseStatisticsCconnectionBean> connetlist = analyseStatisticsCconnectionService.queryList(fieldmap);
			//查询字段初始化属性
			List<AnalyseStatisticsCwhereBean> cwherelist = analyseStatisticsCwhereService.queryList(fieldmap);
			analyseStatisticsCfieldBean.setConnetlist(connetlist);
			analyseStatisticsCfieldBean.setCwherelist(cwherelist);
			
			if(analyseStatisticsCfieldBean.getIsshow()!=null&&analyseStatisticsCfieldBean.getIsshow().equals("1")) {
				analyseStatisticsCfieldBean.setIsshow("true");
			}else {
				analyseStatisticsCfieldBean.setIsshow("false");
			}
			
			analyseStatisticsCfieldBeans.add( analyseStatisticsCfieldBean );
		}
		return analyseStatisticsCfieldBeans;
	}
	
	@Override
	public int queryTotal(Map<String, Object> map) throws Exception{
		return analyseStatisticsCfieldDao.queryTotal(map);
	}
	
	@Override
	public void save(AnalyseStatisticsCfieldBean analyseStatisticsCfieldBean) throws Exception{
		AnalyseStatisticsCfield analyseStatisticsCfield = new AnalyseStatisticsCfield();
		BeanUtils.copyProperties( analyseStatisticsCfieldBean, analyseStatisticsCfield );
		//保存主表
		if(analyseStatisticsCfield.getIsshow()!=null&&analyseStatisticsCfield.getIsshow().equals("true")) {
			analyseStatisticsCfield.setIsshow("1");
		}else {
			analyseStatisticsCfield.setIsshow("0");
		}
		String maxNo=getMaxNo();
		analyseStatisticsCfield.setId(maxNo);
		analyseStatisticsCfieldDao.save(analyseStatisticsCfield);
		//保存关联表
		List<AnalyseStatisticsCconnectionBean> connetlist = analyseStatisticsCfieldBean.getConnetlist();
		analyseStatisticsCconnectionService.saveBatchList(maxNo,connetlist);
		//查询字段初始化属性
		List<AnalyseStatisticsCwhereBean> cwherelist = analyseStatisticsCfieldBean.getCwherelist();
		analyseStatisticsCwhereService.saveBatchList(maxNo,cwherelist);
		
	}
	
	@Override
	public void update(AnalyseStatisticsCfieldBean analyseStatisticsCfieldBean) throws Exception{
		
		//删除关联表、初始化字段表
		analyseStatisticsCconnectionService.delete(analyseStatisticsCfieldBean.getId());
		analyseStatisticsCwhereService.delete(analyseStatisticsCfieldBean.getId());
		//再重新保存主表、关联表、字段初始化表
		save(analyseStatisticsCfieldBean);
	}
	
	@Override
	public void delete(String id) throws Exception{
		analyseStatisticsCfieldDao.delete(id);
	}
	
	@Override
	public void deleteBatch(String[] ids) throws Exception{
		//查询数据库，便于删除字段关联、初始化表数据
		Map<String,Object> map = new HashMap<>();
		map.put("fid", ids[0]);
		List<AnalyseStatisticsCfield> list = analyseStatisticsCfieldDao.queryList(map);
		if(list!=null&&list.size()>0) {
			int len = list.size();
			for(int i=0;i<len;i++) {
				analyseStatisticsCconnectionService.delete(list.get(i).getId());
				analyseStatisticsCwhereService.delete(list.get(i).getId());
			}
		}
		analyseStatisticsCfieldDao.deleteBatch(ids);
	}

	@Override
	public String getMaxNo() throws Exception {
		String maxId=analyseStatisticsCfieldDao.getMaxNo();
		if(maxId==null||maxId.equals("null")) {
			maxId="000000000001";
		}else {
			maxId="000000000000"+(Integer.valueOf(maxId)+1);
			maxId=maxId.substring(maxId.length()-12, maxId.length());
		}
		return maxId;
	}

	@Override
	public int saveBatchList(List<AnalyseStatisticsCfieldBean> list) throws Exception {
		if(list!=null&&list.size()>0) {
			int len = list.size();
			for(int i =0;i<len;i++) {
				if((list.get(i).getSfieldname()!=null&&list.get(i).getSfieldname().trim().length()>0)&&
				   (list.get(i).getExpressions()!=null&&list.get(i).getExpressions().trim().length()>0)) {
					save(list.get(i));
				}
			}
		}
		return 0;
	}

	@Override
	public int updateBatchList(List<AnalyseStatisticsCfieldBean> list) throws Exception {
		if(list!=null&&list.size()>0) {
			int len = list.size();
			//查询数据库存在的id
			List<String> ids=analyseStatisticsCfieldDao.getIdList(list.get(0).getFid());
			if(ids!=null&&ids.size()>0) {
				//调用toArray方法将List<String>转化为String[]
			    String[] strs = ids.toArray(new String[]{});
				//删除关联表、初始化字段表
				analyseStatisticsCconnectionService.deleteBatch(strs);
				analyseStatisticsCwhereService.deleteBatch(strs);
			}
			//修改主表
			delete(list.get(0).getFid());
			//再次插入数据
			for(int i =0;i<len;i++) {
				if((list.get(i).getSfieldname()!=null&&list.get(i).getSfieldname().trim().length()>0)&&
				   (list.get(i).getExpressions()!=null&&list.get(i).getExpressions().trim().length()>0)) {
					list.get(i).setSavefield("FIELD"+Integer.valueOf(i+2));
					save(list.get(i));
				}
			}
		}
		return 0;
	}
	
}
