package com.yonglilian.controller;

import com.yonglilian.bean.AnalyseStatisticsCwhereBean;
import com.yonglilian.common.util.R;
import com.yonglilian.intercept.annotation.ZrSafety;
import com.yonglilian.service.AnalyseStatisticsCwhereService;
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
 * @date 2019-08-01 01:49:08
 */
@ZrSafety
@RestController
@RequestMapping("analysestatisticscwhere")
public class AnalyseStatisticsCwhereController {
	@Autowired
	private AnalyseStatisticsCwhereService analyseStatisticsCwhereService;
	
	/**
	 * 列表
	 * @throws Exception 
	 */
	@RequestMapping("/list")
//	@RequiresPermissions("analysestatisticscwhere:list")
	public R list(@RequestParam Map<String, Object> params) throws Exception{
		List<AnalyseStatisticsCwhereBean> analyseStatisticsCwhereList = analyseStatisticsCwhereService.queryList(params);
		return R.ok().put("list", analyseStatisticsCwhereList);
	}
	
	
	/**
	 * 信息
	 * @throws Exception 
	 */
	@RequestMapping("/info/{id}")
//	@RequiresPermissions("analysestatisticscwhere:info")
	public R info(@PathVariable("id") String id) throws Exception{
		AnalyseStatisticsCwhereBean analyseStatisticsCwhere = analyseStatisticsCwhereService.queryObject(id);
		
		return R.ok().put("analyseStatisticsCwhere", analyseStatisticsCwhere);
	}
	
	/**
	 * 保存
	 * @throws Exception 
	 */
	@RequestMapping("/save")
//	@RequiresPermissions("analysestatisticscwhere:save")
	public R save(@RequestBody AnalyseStatisticsCwhereBean analyseStatisticsCwhereBean) throws Exception{
		analyseStatisticsCwhereService.save(analyseStatisticsCwhereBean);
		
		return R.ok();
	}
	
	/**
	 * 修改
	 * @throws Exception 
	 */
	@RequestMapping("/update")
//	@RequiresPermissions("analysestatisticscwhere:update")
	public R update(@RequestBody AnalyseStatisticsCwhereBean analyseStatisticsCwhere) throws Exception{
		analyseStatisticsCwhereService.update(analyseStatisticsCwhere);
		
		return R.ok();
	}
	
	/**
	 * 删除
	 * @throws Exception 
	 */
	@RequestMapping("/delete")
//	@RequiresPermissions("analysestatisticscwhere:delete")
	public R delete(@RequestBody String[] ids) throws Exception{
		analyseStatisticsCwhereService.deleteBatch(ids);
		
		return R.ok();
	}
	
}
