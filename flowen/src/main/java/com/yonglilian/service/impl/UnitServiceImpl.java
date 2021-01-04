package com.yonglilian.service.impl;

import com.yonglilian.common.util.SysPreperty;
import com.yonglilian.dao.UserMapper;
import com.yonglilian.model.BPIP_UNIT;
import com.yonglilian.service.UnitService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zr.zrpower.common.db.DBEngine;

import java.util.Map;

/**
 * 单位信息服务层实现
 * @author lwk
 *
 */
@Service
public class UnitServiceImpl implements UnitService {
	/** The UnitServiceImpl Logger. */
	@SuppressWarnings("unused")
	private static final Logger LOGGER = LoggerFactory.getLogger(UnitServiceImpl.class);
	/** 用户操作数据层. */
	@Autowired
	private UserMapper userMapper;
	/** 单位信息数据层. */
	private DBEngine dbEngine;
	private static int clients=0;

	/**
	 * UnitServiceImpl构造方法
	 */
	public UnitServiceImpl() {
		dbEngine = new DBEngine(SysPreperty.getProperty().MainDataSource, 
				SysPreperty.getProperty().IsConvert);
		dbEngine.initialize();
		if (clients<1) {
			// code
		}
	    clients++;
	}

	
	@Override
	public BPIP_UNIT getUnit(String unitID) throws Exception {
	    BPIP_UNIT retValue = null;
	    String  strSQL = "Select UNITID,UNITNAME,STATE,ORDERCODE From BPIP_UNIT Where UnitID = '"+unitID+"'";
	    Map<String, Object> retMap = userMapper.selectMapExecSQL(strSQL);
	    if (retMap != null && retMap.size() > 0) {
	    	  retValue = new BPIP_UNIT();
	    	  retValue.setUNITID(retMap.get("UNITID").toString());
	    	  retValue.setUNITNAME(retMap.get("UNITNAME").toString());
	    	  retValue.setSTATE(retMap.get("STATE").toString());
	    	  retValue.setORDERCODE(retMap.get("ORDERCODE").toString());
	    }
	    return retValue;
	}

	
}