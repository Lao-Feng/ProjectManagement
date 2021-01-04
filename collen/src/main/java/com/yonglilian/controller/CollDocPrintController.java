package com.yonglilian.controller;

import com.yonglilian.bean.CollDocPrintBean;
import com.yonglilian.common.util.R;
import com.yonglilian.intercept.annotation.ZrSafety;
import com.yonglilian.service.CollDocPrintService;
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
 * @date 2019-08-04 15:20:55
 */
@ZrSafety
@RestController
@RequestMapping("colldocprint")
public class CollDocPrintController {
	@Autowired
	private CollDocPrintService collDocPrintService;
	
	/**
	 * 列表
	 * @throws Exception 
	 */
	@RequestMapping("/list")
//	@RequiresPermissions("colldocprint:list")
	public R list(@RequestParam Map<String, Object> params) throws Exception{
		List<CollDocPrintBean> collDocPrintList = collDocPrintService.queryList(params);
		
		return R.ok().put("list", collDocPrintList);
	}
	
	
	/**
	 * 信息
	 * @throws Exception 
	 */
	@RequestMapping("/info/{id}")
//	@RequiresPermissions("colldocprint:info")
	public R info(@PathVariable("id") String id) throws Exception{
		CollDocPrintBean collDocPrint = collDocPrintService.queryObject(id);
		
		return R.ok().put("collDocPrint", collDocPrint);
	}
	
	/**
	 * 保存
	 * @throws Exception 
	 */
	@RequestMapping("/save")
//	@RequiresPermissions("colldocprint:save")
	public R save(@RequestBody CollDocPrintBean collDocPrintBean) throws Exception{
		collDocPrintService.save(collDocPrintBean);
		
		return R.ok();
	}
	
	/**
	 * 修改
	 * @throws Exception 
	 */
	@RequestMapping("/update")
//	@RequiresPermissions("colldocprint:update")
	public R update(@RequestBody CollDocPrintBean collDocPrint) throws Exception{
		collDocPrintService.update(collDocPrint);
		
		return R.ok();
	}
	
	/**
	 * 删除
	 * @throws Exception 
	 */
	@RequestMapping("/delete")
//	@RequiresPermissions("colldocprint:delete")
	public R delete(@RequestBody String[] ids) throws Exception{
		collDocPrintService.deleteBatch(ids);
		
		return R.ok();
	}
	
}
