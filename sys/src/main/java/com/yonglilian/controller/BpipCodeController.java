package com.yonglilian.controller;

import com.yonglilian.common.util.R;
import com.yonglilian.intercept.annotation.ZrSafety;
import com.yonglilian.model.BPIP_TABLE;
import com.yonglilian.model.CODE_YWXT;
import com.yonglilian.service.BpipCodeService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 字典控制操作
 * @author nfzr
 *
 */
@ZrSafety
@RestController
@RequestMapping("/bpipcode")
public class BpipCodeController extends BaseController {

	private static final long serialVersionUID = 1L;
	
	@Autowired
	private BpipCodeService bpipCodeService;
	
	/**
	 * 数据库库列表
	 * @param map
	 * @return
	 */
	@RequestMapping("/tables")
	@RequiresPermissions("bpipcode:tables")
	public R tables(@RequestParam Map<String, Object> map)  throws Exception{
		List<BPIP_TABLE> list=bpipCodeService.getTables(map);
		return R.ok().put("list", list);
	}

	/**
	 * 列表
	 * @param map
	 * @return
	 */
	@RequestMapping("/list")
	@RequiresPermissions("bpipcode:list")
	public R list(@RequestParam Map<String, Object> map)  throws Exception{
		if(map.containsKey("table")) {
			List<CODE_YWXT> list=bpipCodeService.queryList(map);
			return R.ok().put("list", list);
		}
		return R.error(500, "查询数据异常，请联系管理员");
	}
	
	/**
	 * 详情
	 * @param map
	 * @return
	 */
	@RequestMapping("/info")
	@RequiresPermissions("bpipcode:info")
	public R info(@RequestParam Map<String, Object> map)  throws Exception{
		if(map.containsKey("table")&&map.containsKey("code")) {
			CODE_YWXT model = bpipCodeService.queryObject(map);
			return R.ok().put("form", model);
		}
		return R.error(500, "查询数据异常，请联系管理员");
	}
	
	/**
	 * 保存
	 * @param CODE_YWXT
	 * @return
	 */
	@RequestMapping("/save")
	@RequiresPermissions("bpipcode:save")
	public R save(@RequestBody CODE_YWXT model) throws Exception{
		if(model!=null&&model.getTable()!=null&&model.getTable().length()>0) {
			bpipCodeService.save(model);
			return R.ok();
		}
		return R.error(500, "保存数据异常，请联系管理员");
	}
	
	/**
	 * 修改
	 * @param CODE_YWXT
	 * @return
	 */
	@RequestMapping("/update")
	@RequiresPermissions("bpipcode:update")
	public R update(@RequestBody CODE_YWXT model) throws Exception{
		if(model!=null&&model.getTable()!=null&&model.getTable().length()>0) {
			bpipCodeService.update(model);
			return R.ok();
		}
		return R.error(500, "修改数据异常，请联系管理员");
	}
	
	/**
	 * 删除
	 * @param map
	 * @return
	 */
	@RequestMapping("/delete")
	@RequiresPermissions("bpipcode:delete")
	public R delete(@RequestParam Map<String, Object> map) throws Exception{//
		if(map.containsKey("table")) {
			bpipCodeService.delete(map);
			return R.ok();
		}
		return R.error(500,"删除异常，请联系系统管理员!");
	}
	
}