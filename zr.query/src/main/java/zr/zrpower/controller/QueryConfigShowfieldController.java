package zr.zrpower.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import zr.zrpower.bean.QueryConfigShowfieldBean;
import zr.zrpower.common.util.R;
import zr.zrpower.intercept.annotation.ZrSafety;
import zr.zrpower.service.QueryConfigShowfieldService;

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
@RequestMapping("queryconfigshowfield")
public class QueryConfigShowfieldController {
	@Autowired
	private QueryConfigShowfieldService queryConfigShowfieldService;
	
	/**
	 * 列表
	 * @throws Exception 
	 */
	@RequestMapping("/list")
//	@RequiresPermissions("queryconfigshowfield:list")
	public R list(@RequestParam Map<String, Object> params) throws Exception{

		List<QueryConfigShowfieldBean> queryConfigShowfieldList = queryConfigShowfieldService.queryList(params,false);
		return R.ok().put("list", queryConfigShowfieldList);
	}
	
	
	/**
	 * 信息
	 * @throws Exception 
	 */
	@RequestMapping("/info/{id}")
//	@RequiresPermissions("queryconfigshowfield:info")
	public R info(@PathVariable("id") String id) throws Exception{
		QueryConfigShowfieldBean queryConfigShowfield = queryConfigShowfieldService.queryObject(id);
		
		return R.ok().put("queryConfigShowfield", queryConfigShowfield);
	}
	
	/**
	 * 保存
	 * @throws Exception 
	 */
	@RequestMapping("/save")
//	@RequiresPermissions("queryconfigshowfield:save")
	public R save(@RequestBody QueryConfigShowfieldBean queryConfigShowfieldBean) throws Exception{
		queryConfigShowfieldService.save(queryConfigShowfieldBean);
		
		return R.ok();
	}
	
	/**
	 * 修改
	 * @throws Exception 
	 */
	@RequestMapping("/update")
//	@RequiresPermissions("queryconfigshowfield:update")
	public R update(@RequestBody QueryConfigShowfieldBean queryConfigShowfield) throws Exception{
		queryConfigShowfieldService.update(queryConfigShowfield);
		
		return R.ok();
	}
	
	/**
	 * 删除
	 * @throws Exception 
	 */
	@RequestMapping("/delete")
//	@RequiresPermissions("queryconfigshowfield:delete")
	public R delete(@RequestBody String[] ids) throws Exception{
		queryConfigShowfieldService.deleteBatch(ids);
		
		return R.ok();
	}
	
}
