package com.yonglilian.service.impl;

import com.yonglilian.common.util.StringUtils;
import com.yonglilian.common.util.SysPreperty;
import com.yonglilian.dao.mapper.BpipMenuMapper;
import com.yonglilian.model.BPIP_MENU;
import com.yonglilian.service.BpipMenuService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zr.zrpower.common.db.DBEngine;

import java.util.List;
import java.util.Map;

/**
 * 菜单接口实现
 * @author nfzr
 *
 */
@Service
public class BpipMenuServiceImpl implements BpipMenuService {
	/** The UserServiceImpl Logger. */
	private static final Logger LOGGER = LoggerFactory.getLogger(BpipMenuServiceImpl.class);
	String dataBaseType = SysPreperty.getProperty().DataBaseType;
	/** 数据库引擎. */
	private DBEngine dbEngine;
	/** 用户操作数据层. */
	@Autowired
	private BpipMenuMapper bpipMenuMapper;

	/**
	 * 构造方法
	 */
	public BpipMenuServiceImpl()  throws Exception{
		dbEngine = new DBEngine(SysPreperty.getProperty().MainDataSource, SysPreperty.getProperty().IsConvert);
		dbEngine.initialize();
	}

	@Override
	public BPIP_MENU queryObject(String menuNo)  throws Exception{
		return bpipMenuMapper.queryObject(menuNo);
	}

	@Override
	public List<BPIP_MENU> queryList(Map<String, Object> map)  throws Exception{
		// TODO Auto-generated method stub
		return bpipMenuMapper.queryList(map);
	}

	@Override
	public int queryTotal(Map<String, Object> map)  throws Exception{
		// TODO Auto-generated method stub
		return bpipMenuMapper.queryTotal(map);
	}

	@Override
	public void save(BPIP_MENU model)  throws Exception{
		String strMenuNo = "001";// 得到最大编号
		if (model != null && model.getParentId() != "") {
			int intMoxno = getMaxNo(model.getParentId(), 1);
			if (intMoxno == 0) {
				strMenuNo = model.getParentId() + "001";
			} else {
				strMenuNo = model.getParentId() + String.valueOf(intMoxno + 1001).substring(1, 4);
			}
		} else {
			int intMoxno = getMaxNo("", 0);
			strMenuNo = String.valueOf(intMoxno + 1001).substring(1, 4);
		}
		model.setMENUNO(strMenuNo);
		bpipMenuMapper.save(model);
	}

	@Override
	public void update(BPIP_MENU model)  throws Exception{
		// TODO Auto-generated method stub
		bpipMenuMapper.update(model);
	}

	@Override
	public boolean delete(String menuNo)  throws Exception{
		boolean reback = true;
		if(bpipMenuMapper.nextCount(menuNo+"___")>0) {
			reback = false;
		}else {
			bpipMenuMapper.deleteRoleMenu(menuNo);
			bpipMenuMapper.delete(menuNo);
		}
		return reback;
	}

	@Override
	public boolean deleteBatch(String[] menuNos)  throws Exception{
		boolean reback = true;
		
		return reback;
	}
	
	/**
	 * 得到最大的菜单编号
	 * @param strMenuNo
	 * @param intFlag
	 * @return
	 */
	private int getMaxNo(String strMenuNo, int intFlag)  throws Exception{
		int intResult = 0;
		String strMaxNO = "0";
		switch (intFlag) {
		case 0: // '\0'	
			strMenuNo="___";
			break;
		case 1: // '\001'
			strMenuNo=strMenuNo+"___";
			break;
		}
		try {
			strMaxNO = bpipMenuMapper.getMaxNo(strMenuNo);
			if (StringUtils.isBlank(strMaxNO)||strMaxNO=="null") {
				strMaxNO = "001";
			} else {
				strMaxNO = strMaxNO.substring(strMaxNO.length() - 3, strMaxNO.length());
			}
		} catch (Exception ex) {
			LOGGER.error("SecurityServiceImpl.getMaxNo Exception:\n", ex);
		}
		intResult = Integer.parseInt(strMaxNO);
		return intResult;
	}

	
}