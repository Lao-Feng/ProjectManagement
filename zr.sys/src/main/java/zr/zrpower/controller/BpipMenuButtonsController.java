package zr.zrpower.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import zr.zrpower.common.util.R;
import zr.zrpower.intercept.annotation.ZrSafety;
import zr.zrpower.model.BpipMenuButtons;
import zr.zrpower.service.BpipMenuButtonsService;

import java.util.List;
import java.util.Map;

//import org.apache.shiro.authz.annotation.RequiresPermissions;


/**
 * 系统菜单按钮表控制器
 * 
 * @author nfzr
 * @email ftl@qq.com
 * @date 2019-10-14 12:18:01
 */
@ZrSafety
@RestController
@RequestMapping("bpipmenubuttons")
public class BpipMenuButtonsController {
	@Autowired
	private BpipMenuButtonsService bpipMenuButtonsService;
	
	/**
	 * 列表
	 * @throws Exception 
	 */
	@RequestMapping("/list")
//	@RequiresPermissions("bpipmenubuttons:list")
	public R list(@RequestParam Map<String, Object> params) throws Exception{
		List<BpipMenuButtons> bpipMenuButtonsList = bpipMenuButtonsService.queryList(params);
		return R.ok().put("list", bpipMenuButtonsList);
	}
	
	
	/**
	 * 信息
	 * @throws Exception 
	 */
	@RequestMapping("/info/{id}")
//	@RequiresPermissions("bpipmenubuttons:info")
	public R info(@PathVariable("id") Long id) throws Exception{
		BpipMenuButtons bpipMenuButtons = bpipMenuButtonsService.queryObject(id);
		
		return R.ok().put("bpipMenuButtons", bpipMenuButtons);
	}
	
	/**
	 * 保存
	 * @throws Exception 
	 */
	@RequestMapping("/save")
//	@RequiresPermissions("bpipmenubuttons:save")
	public R save(@RequestBody BpipMenuButtons bpipMenuButtonsBean) throws Exception{
		bpipMenuButtonsService.save(bpipMenuButtonsBean);
		
		return R.ok();
	}
	
	/**
	 * 修改
	 * @throws Exception 
	 */
	@RequestMapping("/update")
//	@RequiresPermissions("bpipmenubuttons:update")
	public R update(@RequestBody BpipMenuButtons bpipMenuButtons) throws Exception{
		bpipMenuButtonsService.update(bpipMenuButtons);
		
		return R.ok();
	}
	
	/**
	 * 删除
	 * @throws Exception 
	 */
	@RequestMapping("/delete")
//	@RequiresPermissions("bpipmenubuttons:delete")
	public R delete(@RequestBody Long[] ids) throws Exception{
		bpipMenuButtonsService.deleteBatch(ids);
		
		return R.ok();
	}
	
}
