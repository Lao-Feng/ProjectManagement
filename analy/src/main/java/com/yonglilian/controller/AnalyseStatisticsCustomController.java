package com.yonglilian.controller;

import com.yonglilian.bean.AnalyseStatisticsCustomBean;
import com.yonglilian.service.AnalyseStatisticsCustomService;
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
 * @date 2019-08-01 01:49:08
 */
@ZrSafety
@RestController
@RequestMapping("analysestatisticscustom")
public class AnalyseStatisticsCustomController {
	@Autowired
	private AnalyseStatisticsCustomService analyseStatisticsCustomService;
	
	/**
	 * 列表
	 * @throws Exception 
	 */
	@RequestMapping("/list")
//	@RequiresPermissions("analysestatisticscustom:list")
	public R list(@RequestParam Map<String, Object> params) throws Exception{
		List<AnalyseStatisticsCustomBean> analyseStatisticsCustomList = analyseStatisticsCustomService.queryList(params);
		return R.ok().put("list", analyseStatisticsCustomList);
	}
	
	
	/**
	 * 信息
	 * @throws Exception 
	 */
	@RequestMapping("/info/{id}")
//	@RequiresPermissions("analysestatisticscustom:info")
	public R info(@PathVariable("id") String id) throws Exception{
		AnalyseStatisticsCustomBean analyseStatisticsCustom = analyseStatisticsCustomService.queryObject(id);
		
		return R.ok().put("analyseStatisticsCustom", analyseStatisticsCustom);
	}
	
	/**
	 * 保存
	 * @throws Exception 
	 */
	@RequestMapping("/save")
//	@RequiresPermissions("analysestatisticscustom:save")
	public R save(@RequestBody AnalyseStatisticsCustomBean analyseStatisticsCustomBean) throws Exception{
		analyseStatisticsCustomService.save(analyseStatisticsCustomBean);
		
		return R.ok();
	}
	
	/**
	 * 修改
	 * @throws Exception 
	 */
	@RequestMapping("/update")
//	@RequiresPermissions("analysestatisticscustom:update")
	public R update(@RequestBody AnalyseStatisticsCustomBean analyseStatisticsCustom) throws Exception{
		analyseStatisticsCustomService.update(analyseStatisticsCustom);
		
		return R.ok();
	}
	
	/**
	 * 删除
	 * @throws Exception 
	 */
	@RequestMapping("/delete")
//	@RequiresPermissions("analysestatisticscustom:delete")
	public R delete(@RequestBody String[] ids) throws Exception{
		analyseStatisticsCustomService.deleteBatch(ids);
		
		return R.ok();
	}
	
}
