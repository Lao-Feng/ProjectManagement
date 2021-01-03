package com.yonglilian.service.impl;

import com.yonglilian.dao.mapper.BpipCodeMapper;
import com.yonglilian.model.BPIP_TABLE;
import com.yonglilian.service.BpipCodeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zr.zrpower.common.db.DBEngine;
import zr.zrpower.common.util.SysPreperty;
import zr.zrpower.model.CODE_YWXT;

import java.util.List;
import java.util.Map;

/**
 * 单位机构/接口
 * @author nfzr
 *
 */
@Service
public class BpipCodeServiceImpl implements BpipCodeService {
	/** The UserServiceImpl Logger. */
	@SuppressWarnings("unused")
	private static final Logger LOGGER = LoggerFactory.getLogger(BpipCodeServiceImpl.class);
	String dataBaseType = SysPreperty.getProperty().DataBaseType;
	/** 数据库引擎. */
	private DBEngine dbEngine;
	/** 用户操作数据层. */
	@Autowired
	private BpipCodeMapper bpipCodeMapper;

	/**
	 * 构造方法
	 */
	public BpipCodeServiceImpl()  throws Exception{
		dbEngine = new DBEngine(SysPreperty.getProperty().MainDataSource, SysPreperty.getProperty().IsConvert);
		dbEngine.initialize();
	}

	@Override
	public CODE_YWXT queryObject(Map<String,Object> map)  throws Exception{
		return bpipCodeMapper.queryObject(map);
	}

	@Override
	public List<CODE_YWXT> queryList(Map<String, Object> map)  throws Exception{
		// TODO Auto-generated method stub
		return bpipCodeMapper.queryList(map);
	}


	@Override
	public void save(CODE_YWXT model)  throws Exception{
		bpipCodeMapper.save(model);
	}

	@Override
	public void update(CODE_YWXT model)  throws Exception{
		// TODO Auto-generated method stub
		bpipCodeMapper.update(model);
	}

	@Override
	public boolean delete(Map<String, Object> map)  throws Exception{
		boolean reback = true;
		bpipCodeMapper.delete(map);
		return reback;
	}

	@Override
	public boolean deleteBatch(String[] codes)  throws Exception{
		boolean reback = true;
		return reback;
	}

	@Override
	public List<BPIP_TABLE> getTables(Map<String, Object> map)  throws Exception{
		// TODO Auto-generated method stub
		return bpipCodeMapper.getTables(map);
	}

	
}