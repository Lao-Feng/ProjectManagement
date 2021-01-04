package com.yonglilian.controller;

import com.yonglilian.common.util.MD5;
import com.yonglilian.common.util.R;
import com.yonglilian.intercept.annotation.ZrSafety;
import com.yonglilian.model.BPIP_USER;
import com.yonglilian.service.BpipUserService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 用户操作
 * @author nfzr
 *
 */
@ZrSafety
@RestController
@RequestMapping("/bpipuser")
public class BpipUserController extends BaseController {

	private static final long serialVersionUID = 1L;
	
	@Autowired
	private BpipUserService bpipUserService;
	

	/**
	 * 列表
	 * @param map
	 * @return
	 */
	@RequestMapping("/list")
	@RequiresPermissions("bpipuser:list")
	public R list(@RequestParam Map<String, Object> map)  throws Exception{
		List<BPIP_USER> list=bpipUserService.queryList(map);
		return R.ok().put("list", list);
	}
	
	/**
	 * 详情
	 * @param userId
	 * @return
	 */
	@RequestMapping("/info/{userId}")
	@RequiresPermissions("bpipuser:info")
	public R info(@PathVariable("userId") String userId) throws Exception{
		BPIP_USER model = bpipUserService.queryObject(userId);
		return R.ok().put("form", model);
	}
	
	/**
	 * 保存
	 * @param BPIP_USER
	 * @return
	 */
	@RequestMapping("/save")
	@RequiresPermissions("bpipuser:save")
	public R save(@RequestBody BPIP_USER model) throws Exception{
		if(bpipUserService.save(model)) {
			return R.ok();
		}
		return R.error(500,"添加用户错误，请联系系统管理员!");
	}
	
	/**
	 * 修改
	 * @param BPIP_USER
	 * @return
	 */
	@RequestMapping("/update")
	@RequiresPermissions("bpipuser:update")
	public R update(@RequestBody BPIP_USER model) throws Exception{
		bpipUserService.update(model);
		return R.ok();
	}
	
	/**
	 * 重置密码
	 * @param BPIP_USER
	 * @return
	 */
	@RequestMapping("/uppw")
	@RequiresPermissions("bpipuser:uppw")
	public R uppw(@RequestBody BPIP_USER model) throws Exception{
		model.setLOGINPW(MD5.toMD5(model.getLOGINPW()));
		bpipUserService.update(model);
		return R.ok();
	}
	
	/**
	 * 删除
	 * @param userId
	 * @return
	 */
	@RequestMapping("/delete")
	@RequiresPermissions("bpipuser:delete")
	public R delete(@RequestBody String[] userIds) throws Exception{
		if(bpipUserService.delete(userIds[0])) {
			return R.ok();
		}
		return R.error(500,"删除用户错误，请联系系统管理员!");
	}
	
}