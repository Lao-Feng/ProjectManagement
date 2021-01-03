package com.yonglilian.collengine.bean;

import com.yonglilian.service.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/collConfigBean")
public class CollConfigBean {
	/** 自定义表单引擎配置管理服务层. */
	@Autowired
	private ConfigService configService;

	@RequestMapping("/getFieldList")
	@ResponseBody
	public List<Map<String, Object>> getFieldList(String TableID) {
		List<Map<String, Object>> result = null;
		try {
			result = configService.getFieldList(TableID);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return result;
	}

	/**
	 * 得到表单的字段设置.
	 * @param ID
	 * @return
	 */
	@RequestMapping("/getCtrlAbleFieldByFID")
	@ResponseBody
	public String getCtrlAbleFieldByFID(String ID) {
		String strResult = "";
		try {
			strResult = configService.getCtrlAbleFieldByFID(ID);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return strResult;
	}

	@RequestMapping("/getTableList")
	@ResponseBody
	public List<Map<String, Object>> getTableList() {
		List<Map<String, Object>> result = null;
		try {
			result = configService.getTableList1();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return result;
	}

	@RequestMapping("/getFieldList1")
	@ResponseBody
	public List<Map<String, Object>> getFieldList1(String TableName) {
		List<Map<String, Object>> result = null;
		try {
			String tableID = configService.getTableIDByTableName(TableName);
			result = configService.getFieldList(tableID);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return result;
	}

}