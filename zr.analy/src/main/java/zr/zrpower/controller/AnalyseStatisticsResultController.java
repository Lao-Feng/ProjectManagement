package zr.zrpower.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import zr.zrpower.bean.AnalyseStatisticsResultBean;
import zr.zrpower.common.util.R;
import zr.zrpower.intercept.annotation.ZrSafety;
import zr.zrpower.service.AnalyseStatisticsResultService;

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
@RequestMapping("analysestatisticsresult")
public class AnalyseStatisticsResultController {
	@Autowired
	private AnalyseStatisticsResultService analyseStatisticsResultService;
	
	/**
	 * 列表
	 * @throws Exception 
	 */
	@RequestMapping("/list")
//	@RequiresPermissions("analysestatisticsresult:list")
	public R list(@RequestParam Map<String, Object> params) throws Exception{
		List<AnalyseStatisticsResultBean> analyseStatisticsResultList = analyseStatisticsResultService.queryList(params);
		return R.ok().put("list", analyseStatisticsResultList);
	}
	
	
	/**
	 * 信息
	 * @throws Exception 
	 */
	@RequestMapping("/info/{id}")
//	@RequiresPermissions("analysestatisticsresult:info")
	public R info(@PathVariable("id") String id) throws Exception{
		AnalyseStatisticsResultBean analyseStatisticsResult = analyseStatisticsResultService.queryObject(id);
		
		return R.ok().put("analyseStatisticsResult", analyseStatisticsResult);
	}
	
	/**
	 * 保存
	 * @throws Exception 
	 */
	@RequestMapping("/save")
//	@RequiresPermissions("analysestatisticsresult:save")
	public R save(@RequestBody AnalyseStatisticsResultBean analyseStatisticsResultBean) throws Exception{
		analyseStatisticsResultService.save(analyseStatisticsResultBean);
		
		return R.ok();
	}
	
	/**
	 * 修改
	 * @throws Exception 
	 */
	@RequestMapping("/update")
//	@RequiresPermissions("analysestatisticsresult:update")
	public R update(@RequestBody AnalyseStatisticsResultBean analyseStatisticsResult) throws Exception{
		analyseStatisticsResultService.update(analyseStatisticsResult);
		
		return R.ok();
	}
	
	/**
	 * 删除
	 * @throws Exception 
	 */
	@RequestMapping("/delete")
//	@RequiresPermissions("analysestatisticsresult:delete")
	public R delete(@RequestBody String[] ids) throws Exception{
		analyseStatisticsResultService.deleteBatch(ids);
		
		return R.ok();
	}
	
}
