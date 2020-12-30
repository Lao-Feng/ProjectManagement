package zr.zrpower.controller;


import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import zr.zrpower.common.util.R;
import zr.zrpower.intercept.annotation.ZrSafety;
import zr.zrpower.model.dbmanage.BPIP_FIELD;
import zr.zrpower.service.BpipTableFieldService;

import java.util.List;
import java.util.Map;

/**
 * 控制器
 * @author nfzr
 *
 */
@ZrSafety
@RestController
@RequestMapping("/bpipfield")
public class BpipTableFieldController extends BaseController {

	private static final long serialVersionUID = 1L;
	
	@Autowired
	private BpipTableFieldService bpipTableFieldService;
	
	/**
	 * 列表select选择
	 * @param map
	 * @return
	 */
	@RequestMapping("/select")
	public R select(@RequestParam Map<String, Object> map)  throws Exception{
		List<BPIP_FIELD> list=bpipTableFieldService.selectList(map);
		return R.ok().put("list", list);
	}
	
	
	/**
	 * 查询数据类型
	 * @param id
	 * @return
	 */
	@RequestMapping("/dbatype")
	public R dbatype() throws Exception{
		int type = bpipTableFieldService.dbaType();
		return R.ok().put("type", type);
	}

	/**
	 * 列表
	 * @param map
	 * @return
	 */
	@RequestMapping("/list")
	@RequiresPermissions("bpipfield:list")
	public R list(@RequestParam Map<String, Object> map)  throws Exception{
		List<BPIP_FIELD> list=bpipTableFieldService.queryList(map);
		return R.ok().put("list", list);
	}
	
	/**
	 * 详情
	 * @param id
	 * @return
	 */
	@RequestMapping("/info/{id}")
	@RequiresPermissions("bpipfield:info")
	public R info(@PathVariable("id") String id) throws Exception{
		BPIP_FIELD model = bpipTableFieldService.queryObject(id);
		return R.ok().put("form", model);
	}
	
	/**
	 * 保存
	 * @param BPIP_FIELD
	 * @return
	 */
	@RequestMapping("/save")
	@RequiresPermissions("bpipfield:save")
	public R save(@RequestBody BPIP_FIELD model) throws Exception{
		bpipTableFieldService.save(model);
		return R.ok();
	}
	
	/**
	 * 修改
	 * @param BPIP_FIELD
	 * @return
	 */
	@RequestMapping("/update")
	@RequiresPermissions("bpipfield:update")
	public R update(@RequestBody BPIP_FIELD model) throws Exception{
		bpipTableFieldService.update(model);
		return R.ok();
	}
	
	/**
	 * 删除
	 * @param ids
	 * @return
	 */
	@RequestMapping("/delete")
	@RequiresPermissions("bpipfield:delete")
	public R delete(@RequestParam Map<String, Object> map) throws Exception{
		// TABLENAME  FIELDNAME FIELDID
		BPIP_FIELD model = new BPIP_FIELD();
		if(map.containsKey("fieldId")&&map.containsKey("tableName")&&map.containsKey("fieldName")) {
			model.setFIELDID(String.valueOf(map.get("fieldId")));
			model.setTABLENAME(String.valueOf(map.get("tableName")));
			model.setFIELDNAME(String.valueOf(map.get("fieldName")));
			bpipTableFieldService.delete(model);
		}
		model = null;
		return R.ok();
	}
}