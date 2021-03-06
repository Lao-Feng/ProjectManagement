package com.yonglilian.controller;

import com.yonglilian.bean.QueryTmpBean;
import com.yonglilian.common.util.R;
import com.yonglilian.intercept.annotation.ZrSafety;
import com.yonglilian.service.QueryTmpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
@RequestMapping("querytmp")
public class QueryTmpController {
	@Autowired
	private QueryTmpService queryTmpService;
	
	/**
	 * 列表
	 * @throws Exception 
	 */
	@RequestMapping("/list")
//	@RequiresPermissions("querytmp:list")
	public R list(@RequestParam Map<String, Object> params) throws Exception{
		List<QueryTmpBean> queryTmpList = queryTmpService.queryList(params);
		return R.ok().put("list", queryTmpList);
	}
	
	
	/**
	 * 信息
	 * @throws Exception 
	 */
	@RequestMapping("/info/{userid}")
//	@RequiresPermissions("querytmp:info")
	public R info(@PathVariable("userid") String userid) throws Exception{
		QueryTmpBean queryTmp = queryTmpService.queryObject(userid);
		
		return R.ok().put("queryTmp", queryTmp);
	}
	
	/**
	 * 保存
	 * @throws Exception 
	 */
	@RequestMapping("/save")
//	@RequiresPermissions("querytmp:save")
	public R save(@RequestBody QueryTmpBean queryTmpBean) throws Exception{
		queryTmpService.save(queryTmpBean);
		
		return R.ok();
	}
	
	/**
	 * 修改
	 * @throws Exception 
	 */
	@RequestMapping("/update")
//	@RequiresPermissions("querytmp:update")
	public R update(@RequestBody QueryTmpBean queryTmp) throws Exception{
		queryTmpService.update(queryTmp);
		
		return R.ok();
	}
	
	/**
	 * 删除
	 * @throws Exception 
	 */
	@RequestMapping("/delete")
//	@RequiresPermissions("querytmp:delete")
	public R delete(@RequestBody String[] userids) throws Exception{
		queryTmpService.deleteBatch(userids);
		
		return R.ok();
	}
	
}
