package com.yonglilian.service.impl;

import com.yonglilian.common.util.DaoSqlUtils;
import com.yonglilian.common.util.StringUtils;
import com.yonglilian.common.util.SysPreperty;
import com.yonglilian.dao.UserMapper;
import com.yonglilian.model.BPIP_USER;
import com.yonglilian.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zr.zrpower.common.db.DBEngine;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户操作服务层实现
 * @author lwk
 *
 */
@Service
public class UserServiceImpl implements UserService {
	/** The UserServiceImpl Logger. */
	@SuppressWarnings("unused")
	private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);
	String dataBaseType = SysPreperty.getProperty().DataBaseType;
	/** 数据库引擎. */
	private DBEngine dbEngine;
	/** 用户操作数据层. */
	@Autowired
	private UserMapper userMapper;

	/**
	 * 构造方法
	 */
	public UserServiceImpl() {
		dbEngine = new DBEngine(SysPreperty.getProperty().MainDataSource, 
				SysPreperty.getProperty().IsConvert);
		dbEngine.initialize();
    }

	

	@Override
	public BPIP_USER getUserInfo(String userID) throws Exception {
		BPIP_USER retValue = userMapper.getUserInfo(userID);
	    return retValue;
	}


	@Override
	public String getUserName(String userID) throws Exception {
		if(StringUtils.isBlank(userID)) {
			return null;
		}
	    // 如果是系统内建用户
	    if (userID.equals("0000000000000000")) {
	    	return "系统内建用户";
	    }
	    userID = DaoSqlUtils.transactSqlInjection(userID);
	    String execSQL = "Select Name From BPIP_USER Where UserID = '"+userID+"'";
	    String userName = userMapper.selectStrExecSQL(execSQL);
		return userName;
	}

	
	public List<String> getArrayList(String strItems, String strItemMark) throws Exception {
		int itemLen, i = 0, n = 0;
        String strItem;
        List<String> strList = new ArrayList<String>();
        itemLen = strItems.length();
        while (i < itemLen) {
        	n = strItems.indexOf(strItemMark, i);
        	strItem = strItems.substring(i, n);
        	strList.add(strItem);
        	i = n + 1;
        }
        return strList;
	}

	

}