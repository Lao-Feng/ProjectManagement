package com.yonglilian.queryengine.html;

import com.yonglilian.queryengine.ItemField;
import com.yonglilian.service.impl.QueryControlImpl;
import zr.zrpower.common.db.DBEngine;
import zr.zrpower.common.util.SysPreperty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ZRTextControl extends ZRControlBase {
	List<Map<String, Object>> resultHTML = null;
	Map<String, Object> map = null;
	QueryControlImpl queryControl = new QueryControlImpl();
    DBEngine dbengine;
    public ZRTextControl(ItemField itemField, String valueStr) {
        super();
        dbengine = new DBEngine(SysPreperty.getProperty().MainDataSource,
                                SysPreperty.getProperty().IsConvert);
        dbengine.initialize();

        resultHTML = null;
		resultHTML = new ArrayList<Map<String, Object>>();
		map = null;
		map = new HashMap<String, Object>();
		map.put("showtype", "text");
		resultHTML.add(map);
    }

    public List<Map<String, Object>> GetValuesList() {
		return resultHTML;
	}

}