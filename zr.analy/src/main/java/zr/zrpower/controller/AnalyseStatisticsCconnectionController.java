package zr.zrpower.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import zr.zrpower.bean.AnalyseStatisticsCconnectionBean;
import zr.zrpower.common.util.R;
import zr.zrpower.intercept.annotation.ZrSafety;
import zr.zrpower.service.AnalyseStatisticsCconnectionService;

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
@RequestMapping("analysestatisticscconnection")
public class AnalyseStatisticsCconnectionController {
	@Autowired
	private AnalyseStatisticsCconnectionService analyseStatisticsCconnectionService;
	
	/**
	 * 列表
	 * @throws Exception 
	 */
	@RequestMapping("/list")
//	@RequiresPermissions("analysestatisticscconnection:list")
	public R list(@RequestParam Map<String, Object> params) throws Exception{

		List<AnalyseStatisticsCconnectionBean> analyseStatisticsCconnectionList = analyseStatisticsCconnectionService.queryList(params);
				
		return R.ok().put("list", analyseStatisticsCconnectionList);
	}
	
	
	/**
	 * 信息
	 * @throws Exception 
	 */
	@RequestMapping("/info/{id}")
//	@RequiresPermissions("analysestatisticscconnection:info")
	public R info(@PathVariable("id") String id) throws Exception{
		AnalyseStatisticsCconnectionBean analyseStatisticsCconnection = analyseStatisticsCconnectionService.queryObject(id);
		
		return R.ok().put("analyseStatisticsCconnection", analyseStatisticsCconnection);
	}
	
	/**
	 * 保存
	 * @throws Exception 
	 */
	@RequestMapping("/save")
//	@RequiresPermissions("analysestatisticscconnection:save")
	public R save(@RequestBody AnalyseStatisticsCconnectionBean analyseStatisticsCconnectionBean) throws Exception{
		analyseStatisticsCconnectionService.save(analyseStatisticsCconnectionBean);
		
		return R.ok();
	}
	
	/**
	 * 修改
	 * @throws Exception 
	 */
	@RequestMapping("/update")
//	@RequiresPermissions("analysestatisticscconnection:update")
	public R update(@RequestBody AnalyseStatisticsCconnectionBean analyseStatisticsCconnection) throws Exception{
		analyseStatisticsCconnectionService.update(analyseStatisticsCconnection);
		
		return R.ok();
	}
	
	/**
	 * 删除
	 * @throws Exception 
	 */
	@RequestMapping("/delete")
//	@RequiresPermissions("analysestatisticscconnection:delete")
	public R delete(@RequestBody String[] ids) throws Exception{
		analyseStatisticsCconnectionService.deleteBatch(ids);
		
		return R.ok();
	}
	
}
