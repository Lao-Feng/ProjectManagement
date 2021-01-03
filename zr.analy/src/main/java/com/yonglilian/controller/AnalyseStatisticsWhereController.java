package com.yonglilian.controller;

import com.yonglilian.bean.AnalyseStatisticsWhereBean;
import com.yonglilian.service.AnalyseStatisticsWhereService;
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
@RequestMapping("analysestatisticswhere")
public class AnalyseStatisticsWhereController {
	@Autowired
	private AnalyseStatisticsWhereService analyseStatisticsWhereService;
	
	/**
	 * 列表
	 * @throws Exception 
	 */
	@RequestMapping("/list")
//	@RequiresPermissions("analysestatisticswhere:list")
	public R list(@RequestParam Map<String, Object> params) throws Exception{
		List<AnalyseStatisticsWhereBean> analyseStatisticsWhereList = analyseStatisticsWhereService.queryList(params);
		return R.ok().put("list", analyseStatisticsWhereList);
	}
	
	
	/**
	 * 信息
	 * @throws Exception 
	 */
	@RequestMapping("/info/{id}")
//	@RequiresPermissions("analysestatisticswhere:info")
	public R info(@PathVariable("id") String id) throws Exception{
		AnalyseStatisticsWhereBean analyseStatisticsWhere = analyseStatisticsWhereService.queryObject(id);
		
		return R.ok().put("analyseStatisticsWhere", analyseStatisticsWhere);
	}
	
	/**
	 * 保存
	 * @throws Exception 
	 */
	@RequestMapping("/save")
//	@RequiresPermissions("analysestatisticswhere:save")
	public R save(@RequestBody AnalyseStatisticsWhereBean analyseStatisticsWhereBean) throws Exception{
		analyseStatisticsWhereService.save(analyseStatisticsWhereBean);
		
		return R.ok();
	}
	
	/**
	 * 批量保存
	 * @throws Exception 
	 */
	@RequestMapping("/savelist")
//	@RequiresPermissions("analysestatisticswhere:save")
	public R savelist(@RequestBody List<AnalyseStatisticsWhereBean> list) throws Exception{
		if(list!=null&&list.size()>0) {
			int len = list.size();
			//先删除
			analyseStatisticsWhereService.delete(list.get(0).getFid());
			for(int i =0;i<len; i++) {
				analyseStatisticsWhereService.save(list.get(i));
			}
		}
		return R.ok();
	}
	
	/**
	 * 修改
	 * @throws Exception 
	 */
	@RequestMapping("/update")
//	@RequiresPermissions("analysestatisticswhere:update")
	public R update(@RequestBody AnalyseStatisticsWhereBean analyseStatisticsWhere) throws Exception{
		analyseStatisticsWhereService.update(analyseStatisticsWhere);
		
		return R.ok();
	}
	
	/**
	 * 删除
	 * @throws Exception 
	 */
	@RequestMapping("/delete")
//	@RequiresPermissions("analysestatisticswhere:delete")
	public R delete(@RequestBody String[] ids) throws Exception{
		analyseStatisticsWhereService.deleteBatch(ids);
		
		return R.ok();
	}
	
}
