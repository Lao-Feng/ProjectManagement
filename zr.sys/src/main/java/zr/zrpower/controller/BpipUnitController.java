package zr.zrpower.controller;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import zr.zrpower.common.util.R;
import zr.zrpower.intercept.annotation.ZrSafety;
import zr.zrpower.model.BPIP_UNIT;
import zr.zrpower.service.BpipUnitService;

import java.util.List;
import java.util.Map;

/**
 * 单位操作
 * @author nfzr
 *
 */
@ZrSafety
@RestController
@RequestMapping("/bpipunit")
public class BpipUnitController extends BaseController {

	private static final long serialVersionUID = 1L;
	
	@Autowired
	private BpipUnitService bpipUnitService;
	
	/**
	 * 查询最大单位编码
	 * @param map
	 * @return
	 */
	@RequestMapping("/maxno")
	public R maxno(@RequestParam Map<String, Object> map)  throws Exception{
		String maxNo="out";
		if(map.containsKey("unitId")&&map.containsKey("type")) {
			maxNo=bpipUnitService.getMaxNo(String.valueOf(map.get("unitId")), Integer.valueOf(String.valueOf(map.get("type"))));
			if (maxNo.equals("out")) {
				return R.error(500,"单位编码超过100，请创建新的父类单位");
			}
		}
		return R.ok().put("unitid", maxNo);
	}

	/**
	 * 列表
	 * @param map
	 * @return
	 */
	@RequestMapping("/list")
	@RequiresPermissions("bpipunit:list")
	public R list(@RequestParam Map<String, Object> map)  throws Exception{
		List<BPIP_UNIT> list=bpipUnitService.queryList(map);
		return R.ok().put("list", list);
	}
	
	/**
	 * 详情
	 * @param unitId
	 * @return
	 */
	@RequestMapping("/info/{unitId}")
	@RequiresPermissions("bpipunit:info")
	public R info(@PathVariable("unitId") String unitId) throws Exception{
		BPIP_UNIT model = bpipUnitService.queryObject(unitId);
		return R.ok().put("form", model);
	}
	
	/**
	 * 保存
	 * @param BPIP_UNIT
	 * @return
	 */
	@RequestMapping("/save")
	@RequiresPermissions("bpipunit:save")
	public R save(@RequestBody BPIP_UNIT model) throws Exception{
		bpipUnitService.save(model);
		return R.ok();
	}
	
	/**
	 * 修改
	 * @param BPIP_UNIT
	 * @return
	 */
	@RequestMapping("/update")
	@RequiresPermissions("bpipunit:update")
	public R update(@RequestBody BPIP_UNIT model) throws Exception{
		bpipUnitService.update(model);
		return R.ok();
	}
	
	/**
	 * 删除
	 * @param unitIds
	 * @return
	 */
	@RequestMapping("/delete")
	@RequiresPermissions("bpipunit:delete")
	public R delete(@RequestBody String[] unitIds) throws Exception{
		if(bpipUnitService.delete(unitIds[0])) {
			return R.ok();
		}
		return R.error(500,"当前机构下有下层机构或绑定的活动用户，无法进行删除!");
	}
	
}