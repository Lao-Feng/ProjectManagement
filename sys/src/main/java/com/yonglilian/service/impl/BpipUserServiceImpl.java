package com.yonglilian.service.impl;

import com.yonglilian.common.util.MD5;
import com.yonglilian.common.util.StringUtils;
import com.yonglilian.common.util.SysPreperty;
import com.yonglilian.dao.mapper.BpipUserMapper;
import com.yonglilian.model.BPIP_USER;
import com.yonglilian.service.BpipUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zr.zrpower.common.db.DBEngine;

import java.util.List;
import java.util.Map;

/**
 * 用户接口
 * @author nfzr
 *
 */
@Service
public class BpipUserServiceImpl implements BpipUserService {
	/** The UserServiceImpl Logger. */
	@SuppressWarnings("unused")
	private static final Logger LOGGER = LoggerFactory.getLogger(BpipUserServiceImpl.class);
	String dataBaseType = SysPreperty.getProperty().DataBaseType;
	/** 数据库引擎. */
	private DBEngine dbEngine;
	/** 用户操作数据层. */
	@Autowired
	private BpipUserMapper bpipUserMapper;

	/**
	 * 构造方法
	 */
	public BpipUserServiceImpl()  throws Exception{
		dbEngine = new DBEngine(SysPreperty.getProperty().MainDataSource, SysPreperty.getProperty().IsConvert);
		dbEngine.initialize();
	}

	@Override
	public BPIP_USER queryObject(String userId) throws Exception {
		// TODO Auto-generated method stub
		return bpipUserMapper.queryObject(userId);
	}



	@Override
	public List<BPIP_USER> queryList(Map<String, Object> map) throws Exception {
		// TODO Auto-generated method stub
		return bpipUserMapper.queryList(map);
	}



	@Override
	public int queryTotal(Map<String, Object> map) throws Exception {
		// TODO Auto-generated method stub
		return bpipUserMapper.queryTotal(map);
	}



	@Override
	public boolean save(BPIP_USER model) throws Exception {
		// TODO Auto-generated method stub
		boolean reback=false;
		//判断系统当前登录账号是否存在
		if(bpipUserMapper.queryUserId(model.getLOGINID())==0) {
			//生成最大用户编码
			String setUserId=getMaxNo(model.getUNITID());
			if(setUserId.length()==16) {
				model.setUSERID(setUserId);
				model.setLOGINPW(MD5.toMD5(model.getLOGINPW()));
				bpipUserMapper.saveBack(model);
				reback=true;
			}
		}
		return reback;
	}



	@Override
	public boolean update(BPIP_USER model) throws Exception {
		// TODO Auto-generated method stub
		bpipUserMapper.update(model);
		return true;
	}


	@Override
	public boolean delete(String userId) throws Exception {
		// TODO Auto-generated method stub
		bpipUserMapper.delete(userId);
		bpipUserMapper.delUserPhoto(userId);
		bpipUserMapper.delUserRemove(userId);
		bpipUserMapper.delUserIdea(userId);
		bpipUserMapper.delUserRole(userId);
		return true;
	}


	@Override
	public boolean deleteBatch(String[] userIds) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getMaxNo(String unitId)  throws Exception{
		// TODO Auto-generated method stub
		String userId = bpipUserMapper.getMaxNo(unitId);	
		System.out.println(userId);
		if (StringUtils.isBlank(userId)||userId=="null") {
			userId = unitId+"0001";
		} else {
			String suffuserId = userId.substring(userId.length()-4, userId.length());
			int max=Integer.valueOf(suffuserId)+1;
			if(max>9999) {
				userId="out";
			}else {
				String maxNo="0000"+max;
				maxNo=maxNo.substring(maxNo.length()-4, maxNo.length());
				userId=userId.substring(0, userId.length()-4)+maxNo;
			}
		}
		System.out.println(userId);
		return userId;
	}



	
}