package com.yonglilian.service.impl;

import com.yonglilian.common.util.SysPreperty;
import com.yonglilian.dao.mapper.BpipTablespaceMapper;
import com.yonglilian.model.dbmanage.BPIP_TABLESPACE;
import com.yonglilian.service.BpipTablespaceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zr.zrpower.common.db.DBEngine;

import java.util.List;
import java.util.Map;

/**
 * 接口实现
 * @author nfzr
 *
 */
@Service
public class BpipTablespaceServiceImpl implements BpipTablespaceService {
	/** The UserServiceImpl Logger. */
	@SuppressWarnings("unused")
	private static final Logger LOGGER = LoggerFactory.getLogger(BpipTablespaceServiceImpl.class);
	String dataBaseType = SysPreperty.getProperty().DataBaseType;
	/** 数据库引擎. */
	private DBEngine dbEngine; 
	private static int clients = 0;
	/** 用户操作数据层. */
	@Autowired
	private BpipTablespaceMapper bpipTablespaceMapper;
	
	/**
	 * 构造方法
	 */
	public BpipTablespaceServiceImpl() {
		dbEngine = new DBEngine(SysPreperty.getProperty().MainDataSource, 
				SysPreperty.getProperty().IsConvert);
		dbEngine.initialize();
		if (clients<1) {
		}
		clients++; 
	}
	
	@Override
	public BPIP_TABLESPACE queryObject(String id) throws Exception {
		// TODO Auto-generated method stub
		return bpipTablespaceMapper.queryObject(id);
	}
	@Override
	public List<BPIP_TABLESPACE> queryList(Map<String, Object> map) throws Exception {
		// TODO Auto-generated method stub
		return bpipTablespaceMapper.queryList(map);
	}
	@Override
	public void save(BPIP_TABLESPACE model) throws Exception {
		// TODO Auto-generated method stub
		//获取最大的编码
		String maxNo=bpipTablespaceMapper.getMaxNo();
		if(maxNo.equals("null")) {
			maxNo="01";
		}else {
			maxNo=(Integer.valueOf(maxNo)+1)+"";
		}
		model.setID(maxNo);
		bpipTablespaceMapper.save(model);
	}
	@Override
	public void update(BPIP_TABLESPACE model) throws Exception {
		// TODO Auto-generated method stub
		bpipTablespaceMapper.update(model);
	}
	@Override
	public void delete(String id) throws Exception {
		//查询表空间下是否有表
		if(bpipTablespaceMapper.queryTables(id)==0) {
			bpipTablespaceMapper.delete(id);
		}
	}

	
}