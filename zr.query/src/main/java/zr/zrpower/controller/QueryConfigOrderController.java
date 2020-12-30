package zr.zrpower.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import zr.zrpower.bean.QueryConfigOrderBean;
import zr.zrpower.common.util.R;
import zr.zrpower.intercept.annotation.ZrSafety;
import zr.zrpower.service.QueryConfigOrderService;

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
@RequestMapping("queryconfigorder")
public class QueryConfigOrderController {
	@Autowired
	private QueryConfigOrderService queryConfigOrderService;
	
	/**
	 * 列表
	 * @throws Exception 
	 */
	@RequestMapping("/list")
//	@RequiresPermissions("queryconfigorder:list")
	public R list(@RequestParam Map<String, Object> params) throws Exception{

		List<QueryConfigOrderBean> queryConfigOrderList = queryConfigOrderService.queryList(params);
		return R.ok().put("list", queryConfigOrderList);
	}
	
	
	/**
	 * 信息
	 * @throws Exception 
	 */
	@RequestMapping("/info/{id}")
//	@RequiresPermissions("queryconfigorder:info")
	public R info(@PathVariable("id") String id) throws Exception{
		QueryConfigOrderBean queryConfigOrder = queryConfigOrderService.queryObject(id);
		
		return R.ok().put("queryConfigOrder", queryConfigOrder);
	}
	
	/**
	 * 保存
	 * @throws Exception 
	 */
	@RequestMapping("/save")
//	@RequiresPermissions("queryconfigorder:save")
	public R save(@RequestBody QueryConfigOrderBean queryConfigOrderBean) throws Exception{
		queryConfigOrderService.save(queryConfigOrderBean);
		
		return R.ok();
	}
	
	/**
	 * 修改
	 * @throws Exception 
	 */
	@RequestMapping("/update")
//	@RequiresPermissions("queryconfigorder:update")
	public R update(@RequestBody QueryConfigOrderBean queryConfigOrder) throws Exception{
		queryConfigOrderService.update(queryConfigOrder);
		
		return R.ok();
	}
	
	/**
	 * 删除
	 * @throws Exception 
	 */
	@RequestMapping("/delete")
//	@RequiresPermissions("queryconfigorder:delete")
	public R delete(@RequestBody String[] ids) throws Exception{
		queryConfigOrderService.deleteBatch(ids);
		
		return R.ok();
	}
	
}
