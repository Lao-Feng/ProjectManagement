package com.yonglilian.controller;

import com.yonglilian.bean.QueryConfigInitBean;
import com.yonglilian.common.util.R;
import com.yonglilian.intercept.annotation.ZrSafety;
import com.yonglilian.service.QueryConfigInitService;
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
@RequestMapping("queryconfiginit")
public class QueryConfigInitController {
	@Autowired
	private QueryConfigInitService queryConfigInitService;
	
	/**
	 * 列表
	 * @throws Exception 
	 */
	@RequestMapping("/list")
//	@RequiresPermissions("queryconfiginit:list")
	public R list(@RequestParam Map<String, Object> params) throws Exception{
		List<QueryConfigInitBean> queryConfigInitList = queryConfigInitService.queryList(params);
		return R.ok().put("list", queryConfigInitList);
	}
	
	
	/**
	 * 信息
	 * @throws Exception 
	 */
	@RequestMapping("/info/{id}")
//	@RequiresPermissions("queryconfiginit:info")
	public R info(@PathVariable("id") String id) throws Exception{
		QueryConfigInitBean queryConfigInit = queryConfigInitService.queryObject(id);
		
		return R.ok().put("queryConfigInit", queryConfigInit);
	}
	
	/**
	 * 保存
	 * @throws Exception 
	 */
	@RequestMapping("/save")
//	@RequiresPermissions("queryconfiginit:save")
	public R save(@RequestBody QueryConfigInitBean queryConfigInitBean) throws Exception{
		queryConfigInitService.save(queryConfigInitBean);
		
		return R.ok();
	}
	
	/**
	 * 修改
	 * @throws Exception 
	 */
	@RequestMapping("/update")
//	@RequiresPermissions("queryconfiginit:update")
	public R update(@RequestBody QueryConfigInitBean queryConfigInit) throws Exception{
		queryConfigInitService.update(queryConfigInit);
		
		return R.ok();
	}
	
	/**
	 * 删除
	 * @throws Exception 
	 */
	@RequestMapping("/delete")
//	@RequiresPermissions("queryconfiginit:delete")
	public R delete(@RequestBody String[] ids) throws Exception{
		queryConfigInitService.deleteBatch(ids);
		
		return R.ok();
	}
	
}
