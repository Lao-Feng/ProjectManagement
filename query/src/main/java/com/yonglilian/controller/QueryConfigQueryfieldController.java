package com.yonglilian.controller;

import com.yonglilian.bean.QueryConfigQueryfieldBean;
import com.yonglilian.common.util.R;
import com.yonglilian.intercept.annotation.ZrSafety;
import com.yonglilian.service.QueryConfigQueryfieldService;
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
@RequestMapping("queryconfigqueryfield")
public class QueryConfigQueryfieldController {
	@Autowired
	private QueryConfigQueryfieldService queryConfigQueryfieldService;
	
	/**
	 * 列表
	 * @throws Exception 
	 */
	@RequestMapping("/list")
//	@RequiresPermissions("queryconfigqueryfield:list")
	public R list(@RequestParam Map<String, Object> params) throws Exception{

		List<QueryConfigQueryfieldBean> queryConfigQueryfieldList = queryConfigQueryfieldService.queryList(params);
		return R.ok().put("list", queryConfigQueryfieldList);
	}
	
	
	/**
	 * 信息
	 * @throws Exception 
	 */
	@RequestMapping("/info/{id}")
//	@RequiresPermissions("queryconfigqueryfield:info")
	public R info(@PathVariable("id") String id) throws Exception{
		QueryConfigQueryfieldBean queryConfigQueryfield = queryConfigQueryfieldService.queryObject(id);
		
		return R.ok().put("queryConfigQueryfield", queryConfigQueryfield);
	}
	
	/**
	 * 保存
	 * @throws Exception 
	 */
	@RequestMapping("/save")
//	@RequiresPermissions("queryconfigqueryfield:save")
	public R save(@RequestBody QueryConfigQueryfieldBean queryConfigQueryfieldBean) throws Exception{
		queryConfigQueryfieldService.save(queryConfigQueryfieldBean);
		
		return R.ok();
	}
	
	/**
	 * 修改
	 * @throws Exception 
	 */
	@RequestMapping("/update")
//	@RequiresPermissions("queryconfigqueryfield:update")
	public R update(@RequestBody QueryConfigQueryfieldBean queryConfigQueryfield) throws Exception{
		queryConfigQueryfieldService.update(queryConfigQueryfield);
		
		return R.ok();
	}
	
	/**
	 * 删除
	 * @throws Exception 
	 */
	@RequestMapping("/delete")
//	@RequiresPermissions("queryconfigqueryfield:delete")
	public R delete(@RequestBody String[] ids) throws Exception{
		queryConfigQueryfieldService.deleteBatch(ids);
		
		return R.ok();
	}
	
}
