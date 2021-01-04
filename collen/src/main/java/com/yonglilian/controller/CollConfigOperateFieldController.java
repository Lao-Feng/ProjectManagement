package com.yonglilian.controller;

import com.yonglilian.bean.CollConfigOperateFieldBean;
import com.yonglilian.common.util.R;
import com.yonglilian.intercept.annotation.ZrSafety;
import com.yonglilian.service.CollConfigOperateFieldService;
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
@RequestMapping("collconfigoperatefield")
public class CollConfigOperateFieldController {
	@Autowired
	private CollConfigOperateFieldService collConfigOperateFieldService;
	
	/**
	 * 列表
	 * @throws Exception 
	 */
	@RequestMapping("/list")
//	@RequiresPermissions("collconfigoperatefield:list")
	public R list(@RequestParam Map<String, Object> params) throws Exception{

		List<CollConfigOperateFieldBean> collConfigOperateFieldList = collConfigOperateFieldService.queryList(params);
		
		return R.ok().put("list", collConfigOperateFieldList);
	}
	
	
	/**
	 * 信息
	 * @throws Exception 
	 */
	@RequestMapping("/info/{id}")
//	@RequiresPermissions("collconfigoperatefield:info")
	public R info(@PathVariable("id") String id) throws Exception{
		CollConfigOperateFieldBean collConfigOperateField = collConfigOperateFieldService.queryObject(id);
		
		return R.ok().put("collConfigOperateField", collConfigOperateField);
	}
	
	/**
	 * 保存
	 * @throws Exception 
	 */
	@RequestMapping("/save")
//	@RequiresPermissions("collconfigoperatefield:save")
	public R save(@RequestBody CollConfigOperateFieldBean collConfigOperateFieldBean) throws Exception{
		collConfigOperateFieldService.save(collConfigOperateFieldBean);
		
		return R.ok();
	}
	
	/**
	 * 修改
	 * @throws Exception 
	 */
	@RequestMapping("/update")
//	@RequiresPermissions("collconfigoperatefield:update")
	public R update(@RequestBody CollConfigOperateFieldBean collConfigOperateField) throws Exception{
		collConfigOperateFieldService.update(collConfigOperateField);
		
		return R.ok();
	}
	
	/**
	 * 删除
	 * @throws Exception 
	 */
	@RequestMapping("/delete")
//	@RequiresPermissions("collconfigoperatefield:delete")
	public R delete(@RequestBody String[] ids) throws Exception{
		collConfigOperateFieldService.deleteBatch(ids);
		
		return R.ok();
	}
	
}
