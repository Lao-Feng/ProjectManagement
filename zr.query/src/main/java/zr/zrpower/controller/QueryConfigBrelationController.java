package zr.zrpower.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import zr.zrpower.bean.QueryConfigBrelationBean;
import zr.zrpower.common.util.R;
import zr.zrpower.intercept.annotation.ZrSafety;
import zr.zrpower.service.QueryConfigBrelationService;

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
@RequestMapping("queryconfigbrelation")
public class QueryConfigBrelationController {
	@Autowired
	private QueryConfigBrelationService queryConfigBrelationService;
	
	/**
	 * 列表
	 * @throws Exception 
	 */
	@RequestMapping("/list")
//	@RequiresPermissions("queryconfigbrelation:list")
	public R list(@RequestParam Map<String, Object> params) throws Exception{
		List<QueryConfigBrelationBean> queryConfigBrelationList = queryConfigBrelationService.queryList(params);
		
		return R.ok().put("list", queryConfigBrelationList);
	}
	
	
	/**
	 * 信息
	 * @throws Exception 
	 */
	@RequestMapping("/info/{id}")
//	@RequiresPermissions("queryconfigbrelation:info")
	public R info(@PathVariable("id") String id) throws Exception{
		QueryConfigBrelationBean queryConfigBrelation = queryConfigBrelationService.queryObject(id);
		
		return R.ok().put("queryConfigBrelation", queryConfigBrelation);
	}
	
	/**
	 * 保存
	 * @throws Exception 
	 */
	@RequestMapping("/save")
//	@RequiresPermissions("queryconfigbrelation:save")
	public R save(@RequestBody QueryConfigBrelationBean queryConfigBrelationBean) throws Exception{
		queryConfigBrelationService.save(queryConfigBrelationBean);
		
		return R.ok();
	}
	
	/**
	 * 修改
	 * @throws Exception 
	 */
	@RequestMapping("/update")
//	@RequiresPermissions("queryconfigbrelation:update")
	public R update(@RequestBody QueryConfigBrelationBean queryConfigBrelation) throws Exception{
		queryConfigBrelationService.update(queryConfigBrelation);
		
		return R.ok();
	}
	
	/**
	 * 删除
	 * @throws Exception 
	 */
	@RequestMapping("/delete")
//	@RequiresPermissions("queryconfigbrelation:delete")
	public R delete(@RequestBody String[] ids) throws Exception{
		queryConfigBrelationService.deleteBatch(ids);
		
		return R.ok();
	}
	
}
