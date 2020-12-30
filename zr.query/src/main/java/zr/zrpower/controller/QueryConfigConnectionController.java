package zr.zrpower.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import zr.zrpower.bean.QueryConfigConnectionBean;
import zr.zrpower.common.util.R;
import zr.zrpower.intercept.annotation.ZrSafety;
import zr.zrpower.service.QueryConfigConnectionService;

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
@RequestMapping("queryconfigconnection")
public class QueryConfigConnectionController {
	@Autowired
	private QueryConfigConnectionService queryConfigConnectionService;
	
	/**
	 * 列表
	 * @throws Exception 
	 */
	@RequestMapping("/list")
//	@RequiresPermissions("queryconfigconnection:list")
	public R list(@RequestParam Map<String, Object> params) throws Exception{

		List<QueryConfigConnectionBean> queryConfigConnectionList = queryConfigConnectionService.queryList(params);
		return R.ok().put("list", queryConfigConnectionList);
	}
	
	
	/**
	 * 信息
	 * @throws Exception 
	 */
	@RequestMapping("/info/{id}")
//	@RequiresPermissions("queryconfigconnection:info")
	public R info(@PathVariable("id") String id) throws Exception{
		QueryConfigConnectionBean queryConfigConnection = queryConfigConnectionService.queryObject(id);
		
		return R.ok().put("queryConfigConnection", queryConfigConnection);
	}
	
	/**
	 * 保存
	 * @throws Exception 
	 */
	@RequestMapping("/save")
//	@RequiresPermissions("queryconfigconnection:save")
	public R save(@RequestBody QueryConfigConnectionBean queryConfigConnectionBean) throws Exception{
		queryConfigConnectionService.save(queryConfigConnectionBean);
		
		return R.ok();
	}
	
	/**
	 * 修改
	 * @throws Exception 
	 */
	@RequestMapping("/update")
//	@RequiresPermissions("queryconfigconnection:update")
	public R update(@RequestBody QueryConfigConnectionBean queryConfigConnection) throws Exception{
		queryConfigConnectionService.update(queryConfigConnection);
		
		return R.ok();
	}
	
	/**
	 * 删除
	 * @throws Exception 
	 */
	@RequestMapping("/delete")
//	@RequiresPermissions("queryconfigconnection:delete")
	public R delete(@RequestBody String[] ids) throws Exception{
		queryConfigConnectionService.deleteBatch(ids);
		
		return R.ok();
	}
	
}
