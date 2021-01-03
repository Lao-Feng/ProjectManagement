package com.yonglilian.controller;

import com.yonglilian.bean.QueryConfigParameterBean;
import com.yonglilian.service.QueryConfigParameterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import zr.zrpower.common.util.R;
import zr.zrpower.intercept.annotation.ZrSafety;

import java.util.List;
import java.util.Map;

//import org.apache.shiro.authz.annotation.RequiresPermissions;


/**
 * 控制器
 * 
 * @author nfzr
 * @email ftl@qq.com
 * @date 2019-07-30 23:18:19
 */
@ZrSafety
@RestController
@RequestMapping("queryconfigparameter")
public class QueryConfigParameterController {
	@Autowired
	private QueryConfigParameterService queryConfigParameterService;
	
	/**
	 * 列表
	 * @throws Exception 
	 */
	@RequestMapping("/list")
//	@RequiresPermissions("queryconfigparameter:list")
	public R list(@RequestParam Map<String, Object> params) throws Exception{
		List<QueryConfigParameterBean> queryConfigParameterList = queryConfigParameterService.queryList(params);
		return R.ok().put("list", queryConfigParameterList);
	}
	
	
	/**
	 * 信息
	 * @throws Exception 
	 */
	@RequestMapping("/info/{id}")
//	@RequiresPermissions("queryconfigparameter:info")
	public R info(@PathVariable("id") String id) throws Exception{
		QueryConfigParameterBean queryConfigParameter = queryConfigParameterService.queryObject(id);
		
		return R.ok().put("queryConfigParameter", queryConfigParameter);
	}
	
	/**
	 * 保存
	 * @throws Exception 
	 */
	@RequestMapping("/save")
//	@RequiresPermissions("queryconfigparameter:save")
	public R save(@RequestBody QueryConfigParameterBean queryConfigParameterBean) throws Exception{
		queryConfigParameterService.save(queryConfigParameterBean);
		
		return R.ok();
	}
	
	/**
	 * 修改
	 * @throws Exception 
	 */
	@RequestMapping("/update")
//	@RequiresPermissions("queryconfigparameter:update")
	public R update(@RequestBody QueryConfigParameterBean queryConfigParameter) throws Exception{
		queryConfigParameterService.update(queryConfigParameter);
		
		return R.ok();
	}
	
	/**
	 * 删除
	 * @throws Exception 
	 */
	@RequestMapping("/delete")
//	@RequiresPermissions("queryconfigparameter:delete")
	public R delete(@RequestBody String[] ids) throws Exception{
		queryConfigParameterService.deleteBatch(ids);
		
		return R.ok();
	}
	
}
