package com.yonglilian.controller;


import com.yonglilian.common.util.R;
import com.yonglilian.intercept.annotation.ZrSafety;
import com.yonglilian.service.DbaSysGeneratorService;
import com.yonglilian.utils.Page;
import com.yonglilian.utils.Query;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;


/**
 * 
 * @author nfzr
 *
 */

@ZrSafety
@RestController
@RequestMapping("/dba/generator")
public class DbaSysGeneratorController  extends BaseController {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Autowired
    private DbaSysGeneratorService sysGeneratorService;

    /**
     * 列表
     */

    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {
    	//查询列表数据
        Query query = new Query(params);
        List<Map<String, Object>> list = sysGeneratorService.queryList(query);
        int total = sysGeneratorService.queryTotal(query);

        Page pageUtil = new Page(list, total, query.getLimit(), query.getPage());

        return R.ok().put("page", pageUtil);
    }

    /**
     * 生成代码
     */
    @RequestMapping("/code")
    public void code(String tables, HttpServletResponse response) throws IOException {
        String[] tableNames = new String[]{};
        if(tables!=null&&tables.split(",").length>0) {
        	tableNames=tables.split(",");
        }
        byte[] data = sysGeneratorService.generatorCode(tableNames);

        response.reset();
        response.setHeader("Content-Disposition", "attachment; filename=\"zrpower.zip\"");
        response.addHeader("Content-Length", "" + data.length);
        response.setContentType("application/octet-stream; charset=UTF-8");

        IOUtils.write(data, response.getOutputStream());
    }
}
