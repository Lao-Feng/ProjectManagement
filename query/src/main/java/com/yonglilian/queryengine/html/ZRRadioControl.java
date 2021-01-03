package com.yonglilian.queryengine.html;

import com.yonglilian.queryengine.ItemField;
import com.yonglilian.service.impl.QueryControlImpl;
import zr.zrpower.common.db.DBEngine;
import zr.zrpower.common.util.SysPreperty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ZRRadioControl extends ZRControlBase {
	List<Map<String, Object>> resultHTML = null;
	Map<String, Object> map = null;
	QueryControlImpl queryControl = new QueryControlImpl();
    DBEngine dbengine;
    public ZRRadioControl(ItemField itemField, String valueStr) {
        super();
        dbengine = new DBEngine(SysPreperty.getProperty().MainDataSource,
                                SysPreperty.getProperty().IsConvert);
        dbengine.initialize();

        resultHTML = null;
		resultHTML = new ArrayList<Map<String, Object>>();
		
		map = null;
		map = new HashMap<String, Object>();
		map.put("showtype", "radio");
		resultHTML.add(map);
		
		String[][] optionsStr = getDictList(itemField.getCodeTable());
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
		}
		return result;

	}
}