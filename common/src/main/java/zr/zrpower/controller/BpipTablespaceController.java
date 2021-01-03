package zr.zrpower.controller;


import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import zr.zrpower.common.util.R;
import zr.zrpower.intercept.annotation.ZrSafety;
import zr.zrpower.model.dbmanage.BPIP_TABLESPACE;
import zr.zrpower.service.BpipTablespaceService;

import java.util.List;
import java.util.Map;

/**
 * 控制器
 * @author nfzr
 *
 */
@ZrSafety
@RestController
@RequestMapping("/bpipspce")
public class BpipTablespaceController extends BaseController {

	private static final long serialVersionUID = 1L;
	
	@Autowired
	private BpipTablespaceService bpipTablespaceService;

	/**
	 * 列表
	 * @param map
	 * @return
	 */
	@RequestMapping("/list")
	@RequiresPermissions("bpipspce:list")
	public R list(@RequestParam Map<String, Object> map)  throws Exception{
		List<BPIP_TABLESPACE> list=bpipTablespaceService.queryList(map);
		return R.ok().put("list", list);
	}
	
	/**
	 * 详情
	 * @param id
	 * @return
	 */
	@RequestMapping("/info/{id}")
	@RequiresPermissions("bpipspce:info")
	public R info(@PathVariable("id") String id) throws Exception{
		BPIP_TABLESPACE model = bpipTablespaceService.queryObject(id);
		return R.ok().put("form", model);
	}
	
	/**
	 * 保存
	 * @param BPIP_TABLESPACE
	 * @return
	 */
	@RequestMapping("/save")
	@RequiresPermissions("bpipspce:save")
	public R save(@RequestBody BPIP_TABLESPACE model) throws Exception{
		bpipTablespaceService.save(model);
		return R.ok();
	}
	
	/**
	 * 修改
	 * @param BPIP_TABLESPACE
	 * @return
	 */
	@RequestMapping("/update")
	@RequiresPermissions("bpipspce:update")
	public R update(@RequestBody BPIP_TABLESPACE model) throws Exception{
		bpipTablespaceService.update(model);
		return R.ok();
	}
	
	/**
	 * 删除
	 * @param ids
	 * @return
	 */
	@RequestMapping("/delete")
	@RequiresPermissions("bpipspce:delete")
	public R delete(@RequestBody String[] ids) throws Exception{
		bpipTablespaceService.delete(ids[0]);
		return R.ok();
	}
}