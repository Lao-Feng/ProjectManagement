package com.yonglilian.controller;

import com.yonglilian.bean.AnalyseStatisticsCfieldBean;
import com.yonglilian.service.AnalyseStatisticsCfieldService;
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
@RequestMapping("analysestatisticscfield")
public class AnalyseStatisticsCfieldController {
	@Autowired
	private AnalyseStatisticsCfieldService analyseStatisticsCfieldService;
	
	/**
	 * 列表
	 * @throws Exception 
	 */
	@RequestMapping("/list")
//	@RequiresPermissions("analysestatisticscfield:list")
	public R list(@RequestParam Map<String, Object> params) throws Exception{
		List<AnalyseStatisticsCfieldBean> analyseStatisticsCfieldList = analyseStatisticsCfieldService.queryList(params);
		return R.ok().put("list", analyseStatisticsCfieldList);
	}
	
	
	/**
	 * 信息
	 * @throws Exception 
	 */
	@RequestMapping("/info/{id}")
//	@RequiresPermissions("analysestatisticscfield:info")
	public R info(@PathVariable("id") String id) throws Exception{
		AnalyseStatisticsCfieldBean analyseStatisticsCfield = analyseStatisticsCfieldService.queryObject(id);
		
		return R.ok().put("analyseStatisticsCfield", analyseStatisticsCfield);
	}

	/**
	 * 保存list
	 * @throws Exception 
	 */
	@RequestMapping("/savelist")
	public R savelist(@RequestBody List<AnalyseStatisticsCfieldBean> list) throws Exception{
		analyseStatisticsCfieldService.saveBatchList(list);
		return R.ok();
	}
	
	/**
	 * 修改list
	 * @throws Exception 
	 */
	@RequestMapping("/updatelist")
	public R updatelist(@RequestBody List<AnalyseStatisticsCfieldBean> list) throws Exception{
		analyseStatisticsCfieldService.updateBatchList(list);
		return R.ok();
	}
	
	/**
	 * 保存
	 * @throws Exception 
	 */
	@RequestMapping("/save")
//	@RequiresPermissions("analysestatisticscfield:save")
	public R save(@RequestBody AnalyseStatisticsCfieldBean analyseStatisticsCfieldBean) throws Exception{
		analyseStatisticsCfieldService.save(analyseStatisticsCfieldBean);
		
		return R.ok();
	}
	
	/**
	 * 修改
	 * @throws Exception 
	 */
	@RequestMapping("/update")
//	@RequiresPermissions("analysestatisticscfield:update")
	public R update(@RequestBody AnalyseStatisticsCfieldBean analyseStatisticsCfield) throws Exception{
		analyseStatisticsCfieldService.update(analyseStatisticsCfield);
		
		return R.ok();
	}
	
	/**
	 * 删除
	 * @throws Exception 
	 */
	@RequestMapping("/delete")
//	@RequiresPermissions("analysestatisticscfield:delete")
	public R delete(@RequestBody String[] ids) throws Exception{
		analyseStatisticsCfieldService.deleteBatch(ids);
		
		return R.ok();
	}
	
}
