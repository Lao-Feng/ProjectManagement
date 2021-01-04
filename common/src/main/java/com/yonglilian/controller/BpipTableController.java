package com.yonglilian.controller;


import com.yonglilian.common.util.R;
import com.yonglilian.intercept.annotation.ZrSafety;
import com.yonglilian.model.dbmanage.BPIP_TABLE;
import com.yonglilian.service.BpipTableService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 控制器
 * @author nfzr
 *
 */
@ZrSafety
@RestController
@RequestMapping("/bpiptable")
public class BpipTableController extends BaseController {

	private static final long serialVersionUID = 1L;
	
	@Autowired
	private BpipTableService bpipTableService;
	
	/**
	 * 列表select选择
	 * @param map
	 * @return
	 */
	@RequestMapping("/select")
	public R select(@RequestParam Map<String, Object> map)  throws Exception{
		List<BPIP_TABLE> list=bpipTableService.selectList(map);
		return R.ok().put("list", list);
	}

	/**
	 * 列表
	 * @param map
	 * @return
	 */
	@RequestMapping("/list")
	@RequiresPermissions("bpiptable:list")
	public R list(@RequestParam Map<String, Object> map)  throws Exception{
		List<BPIP_TABLE> list=bpipTableService.queryList(map);
		return R.ok().put("list", list);
	}
	
	/**
	 * 详情
	 * @param id
	 * @return
	 */
	@RequestMapping("/info/{id}")
	@RequiresPermissions("bpiptable:info")
	public R info(@PathVariable("id") String id) throws Exception{
		BPIP_TABLE model = bpipTableService.queryObject(id);
		return R.ok().put("form", model);
	}
	
	/**
	 * 保存
	 * @param BPIP_TABLE
	 * @return
	 */
	@RequestMapping("/save")
	@RequiresPermissions("bpiptable:save")
	public R save(@RequestBody BPIP_TABLE model) throws Exception{
		bpipTableService.save(model);
		return R.ok();
	}
	
	/**
	 * 修改
	 * @param BPIP_TABLE
	 * @return
	 */
	@RequestMapping("/update")
	public R update(@RequestBody BPIP_TABLE model) throws Exception{
		bpipTableService.update(model);
		return R.ok();
	}
	
	/**
	 * 删除
	 * @param ids
	 * @return
	 */
	@RequestMapping("/delete")
	@RequiresPermissions("bpiptable:delete")
	public R delete(@RequestParam Map<String, Object> map) throws Exception{
		if(map.containsKey("tableId")&&map.containsKey("tableName")) {
			bpipTableService.delete(String.valueOf(map.get("tableId")),String.valueOf(map.get("tableName")));
		}
		return R.ok();
	}
	
	/**
	 * 生成数据库表实体
	 * @param id
	 * @return
	 */
	@RequestMapping("/entity/{id}")
	public R entity(@PathVariable("id") String id) throws Exception{
		bpipTableService.entity(id);
		return R.ok();
	}
}