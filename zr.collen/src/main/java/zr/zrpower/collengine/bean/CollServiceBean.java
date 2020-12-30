package zr.zrpower.collengine.bean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import zr.zrpower.service.CollectionService;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/collServiceBean")
public class CollServiceBean {
	/** 自定义表单引擎的核心服务 . */
	@Autowired
	private CollectionService collService;

	@RequestMapping("/getUserList")
	@ResponseBody
	public List<Map<String, Object>> getUserList(String UnitID) {
		List<Map<String, Object>> result = null;
		try {
			result = collService.getUserList(UnitID);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return result;
	}
}