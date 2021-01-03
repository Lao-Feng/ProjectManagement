package com.yonglilian.queryengine.html;

import com.yonglilian.queryengine.ItemField;
import com.yonglilian.service.impl.QueryControlImpl;
import zr.zrpower.common.db.DBEngine;
import zr.zrpower.common.util.SysPreperty;
import zr.zrpower.model.SessionUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ZRSelectControl extends ZRControlBase {
	List<Map<String, Object>> resultHTML = null;
	Map<String, Object> map = null;
	DBEngine dbengine;
	String _valueStr;
	SessionUser _userInfo;
	QueryControlImpl queryControl = new QueryControlImpl();

	public ZRSelectControl(ItemField itemField, SessionUser userInfo, String valueStr) {
		super();
		_userInfo = userInfo;
		String[][] optionsStr = null;
		_valueStr = valueStr;
		dbengine = new DBEngine(SysPreperty.getProperty().MainDataSource, SysPreperty.getProperty().IsConvert);
		dbengine.initialize();

		resultHTML = null;
		resultHTML = new ArrayList<Map<String, Object>>();
		
		if (itemField.isUser()) {
			map = null;
			map = new HashMap<String, Object>();
			map.put("showtype", "user");
			resultHTML.add(map);
		}else if (itemField.isUnit()) {
			map = null;
			map = new HashMap<String, Object>();
			map.put("showtype", "unit");
			resultHTML.add(map);
		}else {
			map = null;
			map = new HashMap<String, Object>();
			map.put("showtype", "select");
			resultHTML.add(map);
		}
		if (itemField.isCode()) {
			optionsStr = getDictList(itemField.getCodeTable());
		}
		if (itemField.isUser()) {
			optionsStr = getUserList(userInfo.getUnitID());
		}
		if (itemField.isUnit()) {
			optionsStr = getUnitList();
		}
		if (optionsStr != null && optionsStr.length > 0) {
			for (int i = 0; i < optionsStr.length; i++) {
				map = null;
				map = new HashMap<String, Object>();
				map.put("value", optionsStr[i][0]);
				map.put("name", optionsStr[i][1]);
				resultHTML.add(map);
			}
		}
	}

	public List<Map<String, Object>> GetValuesList() {
		return resultHTML;
	}

	private String[][] getDictList(String codeTable) {
		String[][] result = null;
		try {
			result = queryControl.getCodeTable(codeTable);
		} catch (Exception ex) {
			System.err.println("查询字典异常"+ex);
		}
		return result;

	}

	private String[][] getUserList(String UnitID) {
		String[][] saUser = null;
		try {
			saUser = queryControl.getCodeTable("BPIP_USER");
		} catch (Exception ex) {
		}
		return saUser;
	}

	private String[][] getUnitList() {
		String[][] saUnit = null;
		try {
			saUnit = queryControl.getCodeTable("BPIP_UNIT");
		} catch (Exception ex) {
		}
		return saUnit;
	}
}
