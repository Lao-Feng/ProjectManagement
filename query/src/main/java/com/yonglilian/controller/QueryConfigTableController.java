package com.yonglilian.controller;

import com.yonglilian.bean.QueryConfigTableBean;
import com.yonglilian.service.QueryConfigTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import zr.zrpower.common.util.R;
import zr.zrpower.intercept.annotation.ZrSafety;

import java.util.List;
import java.util.Map;


/**
 * 查询主配置控制器
 * 
 * @author nfzr
 * @email ftl@qq.com
 * @date 2019-07-26 01:10:50
 */
@ZrSafety
@RestController
@RequestMapping("queryconfigtable")
public class QueryConfigTableController {
	@Autowired
	private QueryConfigTableService queryConfigTableService;
	
	/**
	 * 列表
	 * @throws Exception 
	 */
	@RequestMapping("/list")
	public R list(@RequestParam Map<String, Object> params) throws Exception{
		List<QueryConfigTableBean> queryConfigTableList = queryConfigTableService.queryList(params);
		
		return R.ok().put("list", queryConfigTableList);
	}
	
	
	/**
	 * 信息
	 * @throws Exception 
	 */
	@RequestMapping("/info/{id}")
	public R info(@PathVariable("id") String id) throws Exception{
		QueryConfigTableBean queryConfigTable = queryConfigTableService.queryObject(id);
		
		return R.ok().put("form", queryConfigTable);
	}
	
	/**
	 * 保存
	 * @throws Exception 
	 */
	@RequestMapping("/save")
	public R save(@RequestBody QueryConfigTableBean queryConfigTableBean) throws Exception{
		queryConfigTableService.save(queryConfigTableBean);
		
		return R.ok();
	}
	
	/**
	 * 修改
	 * @throws Exception 
	 */
	@RequestMapping("/update")
	public R update(@RequestBody QueryConfigTableBean queryConfigTable) throws Exception{
		queryConfigTableService.update(queryConfigTable);
		
		return R.ok();
	}
	
	/**
	 * 删除
	 * @throws Exception 
	 */
	@RequestMapping("/delete")
	public R delete(@RequestBody String[] ids) throws Exception{
		queryConfigTableService.deleteBatch(ids);
		
		return R.ok();
	}
	
	/**
	 * 删除清除所有
	 * @throws Exception 
	 */
	@RequestMapping("/deletebyid/{id}")
	public R delete(@PathVariable("id") String id) throws Exception{
		queryConfigTableService.delete(id);
		return R.ok();
	}
	
	/**
	 * 通过配置id进行复制
	 * @throws Exception 
	 */
	@RequestMapping("/copy/{id}")
	public R copy(@PathVariable("id") String id) throws Exception{
		queryConfigTableService.copy(id);
		return R.ok();
	}
	
}
