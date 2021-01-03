package com.yonglilian.queryengine;

import com.yonglilian.service.QueryEngineConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/queryCfgBean")
public class QueryCfgBean {
	/** 查询配置管理EJB服务层. */
	@Autowired
	private QueryEngineConfigService queryConfig;

	/**
	 * 获取不包含指定表名称的数据表列表
	 * @return List 包含(表名称、表中文名称)的List
	 */
	@RequestMapping("/getTableList")
	@ResponseBody
	public List<Map<String, Object>> getTableList(String tableName) {
		List<Map<String, Object>> result = null;
		try {
			result = queryConfig.getTableList(tableName);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return result;
	}

	@RequestMapping("/getFieldList")
	@ResponseBody
	public List<Map<String, Object>> getFieldList(String TableName) {
		List<Map<String, Object>> result = null;
		try {
			result = queryConfig.getFieldList(TableName);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return result;
	}

	/**
	 * 获取数据表的字段列表 表名.表名.表名......
	 * @param TableName 数据表名称
	 * @param flag 是否包含字段类型
	 * @return List 包含(字段名称、字段中文名称)的List
	 */
	@RequestMapping("/getTableFieldList")
	@ResponseBody
	public List<Map<String, Object>> getTableFieldList(String TableName, boolean flag) {
		List<Map<String, Object>> result = null;
		try {
			result = queryConfig.getTableFieldList(TableName, flag);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return result;
	}

	/**
	 * 获取表名.字段名中文
	 * @param tableFieldName
	 *            String 表名.字段名(英文)
	 * @return resultStr 表名.字段名中文
	 */
	@RequestMapping("/getTableFieldCnNameByEn")
	@ResponseBody
	public String getTableFieldCnNameByEn(String tableFieldName) {
		String resultStr = "";
		try {
			resultStr = queryConfig.getTableFieldCnNameByEn(tableFieldName);
		} catch (Exception ex) {
			ex.printStackTrace();
			resultStr = "";
		}
		return resultStr;
	}
}