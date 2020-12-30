package zr.zrpower.flowengine.bean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import zr.zrpower.service.FlowManageActService;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/flowMaActBean")
public class FlowMaActBean {
	/** 管理活动的各种信息服务层. */
	@Autowired
	private FlowManageActService flowManageAct;

	@RequestMapping("/getFieldList")
	@ResponseBody
	public List<Map<String, Object>> getFieldList(String TableName) {
		List<Map<String, Object>> result = null;
		try {
			result = flowManageAct.getFieldList(TableName);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return result;
	}

	@RequestMapping("/getTableList")
	@ResponseBody
	public List<Map<String, Object>> getTableList() {
		List<Map<String, Object>> result = null;
		try {
			result = flowManageAct.getTableList();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return result;
	}

	@RequestMapping("/getCtrlAbleFieldByFID")
	@ResponseBody
	public String getCtrlAbleFieldByFID(String FID, String AID) {
		String strResult = "";
		try {
			strResult = flowManageAct.getCtrlAbleFieldByFID(FID, AID);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return strResult;
	}

	@RequestMapping("/getTableList1")
	@ResponseBody
	public List<Map<String, Object>> getTableList1(String StrWhere) {
		List<Map<String, Object>> result = null;
		try {
			result = flowManageAct.getTableList1(StrWhere);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return result;
	}

}