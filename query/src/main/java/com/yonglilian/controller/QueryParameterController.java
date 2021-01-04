package com.yonglilian.controller;

import com.yonglilian.bean.QueryParameterBean;
import com.yonglilian.common.util.R;
import com.yonglilian.intercept.annotation.ZrSafety;
import com.yonglilian.service.QueryParameterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

//import org.apache.shiro.authz.annotation.RequiresPermissions;


/**
 * 控制器
 * 
 * @author nfzr
 * @email ftl@qq.com
 * @date 2019-07-30 22:31:25
 */
@ZrSafety
@RestController
@RequestMapping("queryparameter")
public class QueryParameterController {
	@Autowired
	private QueryParameterService queryParameterService;
	
	/**
	 * 列表
	 * @throws Exception 
	 */
	@RequestMapping("/list")
//	@RequiresPermissions("queryparameter:list")
	public R list(@RequestParam Map<String, Object> params) throws Exception{
		List<QueryParameterBean> queryParameterList = queryParameterService.queryList(params);
		return R.ok().put("list", queryParameterList);
	}
	
	
	/**
	 * 信息
	 * @throws Exception 
	 */
	@RequestMapping("/info/{userid}")
//	@RequiresPermissions("queryparameter:info")
	public R info(@PathVariable("userid") String userid) throws Exception{
		QueryParameterBean queryParameter = queryParameterService.queryObject(userid);
		
		return R.ok().put("queryParameter", queryParameter);
	}
	
	/**
	 * 保存
	 * @throws Exception 
	 */
	@RequestMapping("/save")
//	@RequiresPermissions("queryparameter:save")
	public R save(@RequestBody QueryParameterBean queryParameterBean) throws Exception{
		queryParameterService.save(queryParameterBean);
		
		return R.ok();
	}
	
	/**
	 * 修改
	 * @throws Exception 
	 */
	@RequestMapping("/update")
//	@RequiresPermissions("queryparameter:update")
	public R update(@RequestBody QueryParameterBean queryParameter) throws Exception{
		queryParameterService.update(queryParameter);
		
		return R.ok();
	}
	
	/**
	 * 删除
	 * @throws Exception 
	 */
	@RequestMapping("/delete")
//	@RequiresPermissions("queryparameter:delete")
	public R delete(@RequestBody String[] userids) throws Exception{
		queryParameterService.deleteBatch(userids);
		
		return R.ok();
	}
	
}
