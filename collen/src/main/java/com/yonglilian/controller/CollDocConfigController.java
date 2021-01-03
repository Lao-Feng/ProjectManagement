package com.yonglilian.controller;

import com.yonglilian.bean.CollDocConfigBean;
import com.yonglilian.bean.CollTempBean;
import com.yonglilian.service.CollDocConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import zr.zrpower.common.util.R;
import zr.zrpower.intercept.annotation.ZrSafety;

import java.util.List;
import java.util.Map;

//import org.apache.shiro.authz.annotation.RequiresPermissions;


/**
 * 表单引擎控制器
 * 
 * @author nfzr
 * @email ftl@qq.com
 * @date 2019-08-04 15:20:55
 */
@ZrSafety
@RestController
@RequestMapping("colldocconfig")
public class CollDocConfigController {
	@Autowired
	private CollDocConfigService collDocConfigService;
	
	/**
	 * 列表
	 * @throws Exception 
	 */
	@RequestMapping("/list")
	public R list(@RequestParam Map<String, Object> params) throws Exception{
		List<CollDocConfigBean> collDocConfigList = collDocConfigService.queryList(params);
		return R.ok().put("list", collDocConfigList);
	}
	
	
	/**
	 * 信息
	 * @throws Exception 
	 */
	@RequestMapping("/info/{id}")
	public R info(@PathVariable("id") String id) throws Exception{
		CollDocConfigBean collDocConfig = collDocConfigService.queryObject(id);
		
		return R.ok().put("form", collDocConfig);
	}
	
	/**
	 * 保存
	 * @throws Exception 
	 */
	@RequestMapping("/save")
	public R save(@RequestBody CollDocConfigBean collDocConfigBean) throws Exception{
		collDocConfigService.save(collDocConfigBean);
		return R.ok();
	}
	
	/**
	 * 修改
	 * @throws Exception 
	 */
	@RequestMapping("/update")
	public R update(@RequestBody CollDocConfigBean collDocConfig) throws Exception{
		collDocConfigService.update(collDocConfig);
		return R.ok();
	}
	
	/**
	 * 删除
	 * @throws Exception 
	 */
	@RequestMapping("/delete")
	public R delete(@RequestBody String[] ids) throws Exception{
		collDocConfigService.deleteBatch(ids);
		
		return R.ok();
	}
	
	/**
	 * 生成表单
	 * @throws Exception 
	 */
	@RequestMapping("/tempform")
	public R tempform(@RequestBody CollTempBean collTempBean) throws Exception{
		collDocConfigService.tempform(collTempBean);
		return R.ok();
	}
	
}
