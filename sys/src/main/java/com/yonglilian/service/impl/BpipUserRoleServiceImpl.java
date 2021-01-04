package com.yonglilian.service.impl;

import com.yonglilian.common.util.SysPreperty;
import com.yonglilian.dao.mapper.BpipUserRoleMapper;
import com.yonglilian.model.BPIP_USER_ROLE;
import com.yonglilian.service.BpipUserRoleService;
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
public class BpipUserRoleServiceImpl implements BpipUserRoleService {
	/** The UserServiceImpl Logger. */
	@SuppressWarnings("unused")
	private static final Logger LOGGER = LoggerFactory.getLogger(BpipUserRoleServiceImpl.class);
	String dataBaseType = SysPreperty.getProperty().DataBaseType;
	/** 数据库引擎. */
	private DBEngine dbEngine;
	/** 用户操作数据层. */
	@Autowired
	private BpipUserRoleMapper bpipUserRoleMapper;

	/**
	 * 构造方法
	 */
	public BpipUserRoleServiceImpl()  throws Exception{
		dbEngine = new DBEngine(SysPreperty.getProperty().MainDataSource, SysPreperty.getProperty().IsConvert);
		dbEngine.initialize();
	}

	@Override
	public List<BPIP_USER_ROLE> queryList(Map<String, Object> map) throws Exception {
		// TODO Auto-generated method stub
		return bpipUserRoleMapper.queryList(map);
	}

	@Override
	public void save(BPIP_USER_ROLE model) throws Exception {
		// TODO Auto-generated method stub
		bpipUserRoleMapper.save(model);
	}

	@Override
	public void delete(int roleId) throws Exception {
		// TODO Auto-generated method stub
		bpipUserRoleMapper.delete(roleId);
	}

	@Override
	public void delUserRole(String userId) throws Exception {
		// TODO Auto-generated method stub
		bpipUserRoleMapper.delUserRole(userId);
	}

	
}