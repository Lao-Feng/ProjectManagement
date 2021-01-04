package com.yonglilian.controller;

import com.yonglilian.common.util.R;
import com.yonglilian.intercept.annotation.ZrSafety;
import com.yonglilian.model.BPIP_MENU;
import com.yonglilian.service.BpipMenuService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 菜单操作
 * @author nfzr
 *
 */
@ZrSafety
@RestController
@RequestMapping("/bpipmenu")
public class BpipMenuController extends BaseController {

	private static final long serialVersionUID = 1L;
	
	@Autowired
	private BpipMenuService bpipMenuService;

	/**
	 * 列表
	 * @param map
	 * @return
	 */
	@RequestMapping("/list")
	@RequiresPermissions("bpipmenu:list")
	public R list(@RequestParam Map<String, Object> map)  throws Exception{
		List<BPIP_MENU> list=bpipMenuService.queryList(map);
		return R.ok().put("list", list);
	}
	
	/**
	 * 详情
	 * @param menuno
	 * @return
	 */
	@RequestMapping("/info/{menuno}")
	@RequiresPermissions("bpipmenu:info")
	public R info(@PathVariable("menuno") String menuno) throws Exception{
		BPIP_MENU model = bpipMenuService.queryObject(menuno);
		return R.ok().put("bpipmenu", model);
	}
	
	/**
	 * 保存
	 * @param BPIP_MENU
	 * @return
	 */
	@RequestMapping("/save")
	@RequiresPermissions("bpipmenu:save")
	public R save(@RequestBody BPIP_MENU model) throws Exception{
		bpipMenuService.save(model);
		return R.ok();
	}
	
	/**
	 * 修改
	 * @param BPIP_MENU
	 * @return
	 */
	@RequestMapping("/update")
	@RequiresPermissions("bpipmenu:update")
	public R update(@RequestBody BPIP_MENU model) throws Exception{
		bpipMenuService.update(model);
		return R.ok();
	}
	
	/**
	 * 删除
	 * @param menunos
	 * @return
	 */
	@RequestMapping("/delete")
	@RequiresPermissions("bpipmenu:delete")
	public R delete(@RequestBody String[] menunos) throws Exception{
		if(bpipMenuService.delete(menunos[0])) {
			return R.ok();
		}
		return R.error(500,"当前菜单有下一级菜单，无法进行删除，请先删除底层菜单!");
	}
	
}