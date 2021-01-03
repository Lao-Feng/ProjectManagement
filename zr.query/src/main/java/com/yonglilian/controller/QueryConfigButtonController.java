package com.yonglilian.controller;

import com.yonglilian.bean.QueryConfigButtonBean;
import com.yonglilian.service.QueryConfigButtonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import zr.zrpower.common.util.R;
import zr.zrpower.intercept.annotation.ZrSafety;

import java.util.List;
import java.util.Map;


/**
 * 查询按钮控制
 * 
 * @author nfzr
 * @email ftl@qq.com
 * @date 2019-07-26 11:09:04
 */
@ZrSafety
@RestController
@RequestMapping("queryconfigbutton")
public class QueryConfigButtonController {
	@Autowired
	private QueryConfigButtonService queryConfigButtonService;
	
	/**
	 * 列表
	 * @throws Exception 
	 */
	@RequestMapping("/list")
	public R list(@RequestParam Map<String, Object> params) throws Exception{
		List<QueryConfigButtonBean> queryConfigButtonList = queryConfigButtonService.queryList(params);
		return R.ok().put("list", queryConfigButtonList);
	}
	
	
	/**
	 * 信息
	 * @throws Exception 
	 */
	@RequestMapping("/info/{id}")
	public R info(@PathVariable("id") String id) throws Exception{
		QueryConfigButtonBean queryConfigButton = queryConfigButtonService.queryObject(id);
		
		return R.ok().put("queryConfigButton", queryConfigButton);
	}
	
	/**
	 * 保存
	 * @throws Exception 
	 */
	@RequestMapping("/save")
	public R save(@RequestBody QueryConfigButtonBean queryConfigButtonBean) throws Exception{
		queryConfigButtonService.save(queryConfigButtonBean);
		
		return R.ok();
	}
	
	/**
	 * 修改
	 * @throws Exception 
	 */
	@RequestMapping("/update")
	public R update(@RequestBody QueryConfigButtonBean queryConfigButton) throws Exception{
		queryConfigButtonService.update(queryConfigButton);
		
		return R.ok();
	}
	
	/**
	 * 删除
	 * @throws Exception 
	 */
	@RequestMapping("/delete")
	public R delete(@RequestBody String[] ids) throws Exception{
		queryConfigButtonService.deleteBatch(ids);
		
		return R.ok();
	}
	
}
