package zr.zrpower.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import zr.zrpower.bean.AnalyseStatisticsMainBean;
import zr.zrpower.common.util.R;
import zr.zrpower.intercept.annotation.ZrSafety;
import zr.zrpower.service.AnalyseStatisticsMainService;

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
@RequestMapping("analysestatisticsmain")
public class AnalyseStatisticsMainController {
	@Autowired
	private AnalyseStatisticsMainService analyseStatisticsMainService;
	
	/**
	 * 列表
	 * @throws Exception 
	 */
	@RequestMapping("/list")
	public R list(@RequestParam Map<String, Object> params) throws Exception{
		List<AnalyseStatisticsMainBean> analyseStatisticsMainList = analyseStatisticsMainService.queryList(params);
		return R.ok().put("list", analyseStatisticsMainList);
	}
	
	
	/**
	 * 信息
	 * @throws Exception 
	 */
	@RequestMapping("/info/{id}")
	public R info(@PathVariable("id") String id) throws Exception{
		AnalyseStatisticsMainBean analyseStatisticsMain = analyseStatisticsMainService.queryObject(id);
		
		return R.ok().put("form", analyseStatisticsMain);
	}
	
	/**
	 * 保存
	 * @throws Exception 
	 */
	@RequestMapping("/save")
	public R save(@RequestBody AnalyseStatisticsMainBean analyseStatisticsMainBean) throws Exception{
		analyseStatisticsMainService.save(analyseStatisticsMainBean);
		
		return R.ok();
	}
	
	/**
	 * 修改
	 * @throws Exception 
	 */
	@RequestMapping("/update")
	public R update(@RequestBody AnalyseStatisticsMainBean analyseStatisticsMain) throws Exception{
		analyseStatisticsMainService.update(analyseStatisticsMain);
		
		return R.ok();
	}
	
	/**
	 * 删除
	 * @throws Exception 
	 */
	@RequestMapping("/delete")
	public R delete(@RequestBody String[] ids) throws Exception{
		analyseStatisticsMainService.deleteBatch(ids);
		
		return R.ok();
	}
	
	
	/**
	 * 通过id删除主配置及对应的数据
	 * @throws Exception 
	 */
	@RequestMapping("/deletebyid/{id}")
	public R deletebyid(@PathVariable("id") String id) throws Exception{
		analyseStatisticsMainService.delete(id);
		return R.ok();
	}
	
}
