package com.yonglilian.analyseengine.bean;

import com.yonglilian.service.StatisticsConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/analyseConfigBean")
public class AnalyseConfigBean {
	/** 数据统计函数库服务层. */
	@Autowired
	private StatisticsConfigService statisticsConfigService;

	@RequestMapping("/getFieldList")
	@ResponseBody
	public List<Map<String, Object>> getFieldList(String TableName) {
		List<Map<String, Object>> result = null;
		try {
			result = statisticsConfigService.getFieldList(TableName);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return result;
	}

	@RequestMapping("/getTableList1")
	@ResponseBody
	public List<Map<String, Object>> getTableList1(String StrWhere) {
		List<Map<String, Object>> result = null;
		try {
			result = statisticsConfigService.getTableList1(StrWhere);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return result;
	}
}
