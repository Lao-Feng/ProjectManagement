package com.yonglilian.controller;

import com.yonglilian.common.util.R;
import com.yonglilian.model.BPIP_MENU;
import com.yonglilian.service.UIService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 系统公共接口类
 * @author nfzr
 *
 */

@RestController
@RequestMapping("/untils")
public class UntilController {
	
	/** The IndexController Logger */
	@SuppressWarnings("unused")
	private static final Logger LOGGER = LoggerFactory.getLogger(UntilController.class);
	/** 用户操作服务层. */
	@Autowired
	private UIService uiService;
	
	/**
	 * 获取用户权限菜单
	 * @param userId
	 * @return
	 * @throws Exception 
	 */
    @RequestMapping("/usermenu/{userId}")
    public R info(@PathVariable("userId") String userId) throws Exception {
    	List<BPIP_MENU> list = uiService.loadUserMenu(userId);
        return R.ok().put("list", list);
    }


}
