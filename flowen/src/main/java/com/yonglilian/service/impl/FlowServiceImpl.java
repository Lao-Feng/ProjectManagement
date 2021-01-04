package com.yonglilian.service.impl;

import com.yonglilian.common.util.FunctionMessage;
import com.yonglilian.common.util.ReflectionUtil;
import com.yonglilian.common.util.SysPreperty;
import com.yonglilian.dao.UserMapper;
import com.yonglilian.model.CODE_YWXT;
import com.yonglilian.model.dbmanage.BPIP_TABLE;
import com.yonglilian.service.FlowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zr.zrpower.common.db.DBEngine;
import zr.zrpower.common.db.DBSet;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 流程引擎服务层实现
 * @author lwk
 *
 */
@Service
public class FlowServiceImpl implements FlowService {
	/** 用户操作数据层. */
	@Autowired
	private UserMapper userMapper;
	/** 数据库引擎. */
	private DBEngine dbEngine;
	static private int clients = 0;

	/**
	 * 构造方法
	 */
	public FlowServiceImpl() {
		dbEngine = new DBEngine(SysPreperty.getProperty().MainDataSource, 
				SysPreperty.getProperty().IsConvert);
		dbEngine.initialize();
		if (clients < 1) {
			// clients with something todo
		}
		clients++;
    }

	@Override
	public Map<String, Object> getListValues(String tabName, String where, 
			List<?> listMap, String tabId) throws Exception {
		Map<String, Object> listValues = null;
		String sql = "select * from " + tabName + " where " + where + "";
		List<Map<String, Object>> retlist = userMapper.selectListMapExecSQL(sql);
		int length = retlist != null ? retlist.size() : 0;
		if (length > 0 && retlist.get(0) != null) {
			listValues = new HashMap<String, Object>();
			for (int i = 0; i < listMap.size(); i++) {
				String value = "", codeName = "";
				codeName = getCodeValues(tabId, listMap.get(i).toString());
				if (!"".equals(codeName)) {
					value = getCodeValues2(codeName, retlist.get(0).get(listMap.get(i).toString()).toString());
				} else {
					value = retlist.get(0).get(listMap.get(i).toString()).toString();
				}
				listValues.put(listMap.get(i).toString(), value);
			}
		}
		return listValues;
	}

	@Override
	public String getCodeValues(String tabId, String fieldname) throws Exception {
		String tabname = "", flage = "";
		String sql = "select fieldtag, dicttable from bpip_field "
				   + "where tableid='"+tabId+"' and fieldname='"+fieldname+"'";
		List<Map<String, Object>> retlist = userMapper.selectListMapExecSQL(sql);
		int length = retlist != null ? retlist.size() : 0;
		if (length > 0 && retlist.get(0) != null) {
			tabname = retlist.get(0).get("dicttable") != null ? retlist.get(0).get("dicttable").toString() : "";
			flage = retlist.get(0).get("fieldtag") != null ? retlist.get(0).get("fieldtag").toString() : "";
			if ("4".equals(flage)) {
				tabname = "bpip_unit";
			}
			if ("3".equals(flage)) {
				tabname = "bpip_user";
			}
		}
		return tabname;
	}

	@Override
	public String getCodeValues2(String tabname, String code) throws Exception {
		String values = "", name = "";
		String sql = "";
		if ("bpip_user".equals(tabname)) {
			sql = "select name from bpip_user where userid='" + code + "'";
			name = "name";
		} else if ("bpip_unit".equals(tabname)) {
			sql = "select unitname from bpip_unit where unitid='" + code + "'";
			name = "unitname";
		} else {
			sql = "select name from " + tabname + " where code='" + code + "'";
			name = "name";
		}
		List<Map<String, Object>> retlist = userMapper.selectListMapExecSQL(sql);
		if (retlist != null && retlist.size() > 0) {
			if (retlist.get(0) != null && retlist.get(0).get(name) != null) {
				values = retlist.get(0).get(name).toString();
			}
		}
		return values;
	}

	@Override
	public CODE_YWXT[] getCodeList(String strwhere1) throws Exception {
		CODE_YWXT bgs[] = null;
		String strSql = "Select * from " + strwhere1 + " order by CODE";
		List<Map<String, Object>> retlist = userMapper.selectListMapExecSQL(strSql);
		int length = retlist != null ? retlist.size() : 0;
		if (length > 0) {
			bgs = new CODE_YWXT[length];
			for (int i = 0; i < length; i++) {
				Map<String, Object> retmap = retlist.get(i);
				bgs[i] = (CODE_YWXT) ReflectionUtil.convertMapToBean(retmap, CODE_YWXT.class);
			}
		}
		return bgs;
	}

	@Override
	public BPIP_TABLE getTabKey(String sql) throws Exception {
		BPIP_TABLE bp = new BPIP_TABLE();
		FunctionMessage fm = new FunctionMessage(1);
		try {
			DBSet mdbset = dbEngine.QuerySQL(sql);
			if (mdbset != null) {
				bp.fullData(mdbset.Row(0));
				mdbset = null;
			} else {
				fm.setResult(false);
				fm.setMessage("显示失败");
			}
		} catch (Exception e) {
			return null;
		}
		return bp;
	}
}