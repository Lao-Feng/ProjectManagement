package com.yonglilian.common.web;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class ListPageBig {

	public ListPageBig(String PageFileName, int PageSize, int Page, int maxCount) {
		intPageSize = 0;
		intRowCount = 0;
		intPageCount = 0;
		intPage = 1;
		
		map = new HashMap<String, Object>();
		intPageSize = PageSize;
		intPage = Page;
		strPageFileName = PageFileName;
		intRowCount = maxCount;
		
		if (intRowCount == 0) {
			intPageCount = 0;
		} else if (intPageSize == 0) {
			intPageCount = 0;
		} else {
			intPageCount = intRowCount / intPageSize;
			if (intRowCount % intPageSize > 0)
				intPageCount++;
		}
	}

	@SuppressWarnings("rawtypes")
	public void SetMap(HttpServletRequest req) {
		Enumeration parameterNames = req.getParameterNames();
		do {
			if (!parameterNames.hasMoreElements())
				break;
			String name = (String) parameterNames.nextElement();
			String values[] = req.getParameterValues(name);
			String value = values[0];
			if (!name.equals("curpage") && !name.equals("pagesize") && !name.equals("Submit") && !name.equals("submit")
					&& value != null && value.length() != 0)
				map.put(name, value);
		} while (true);
	}

	public int getPageCount() {
		return intPageCount;
	}

	public String PageLegend() {
		StringBuffer strb = new StringBuffer("");
		strb.append("<form name=frmPage method=post action=" + strPageFileName + ">\n");
		strb.append("每页");
		strb.append(Integer.toString(intPageSize));
		strb.append("条记录 共" + Integer.toString(intRowCount) + "条记录 ");
		if (intPageCount == 0 || intPage == 1) {
			strb.append(" 首页 ");
			strb.append(" 前页 ");
		} else {
			strb.append("\n<A href=javascript:viewPage(1)>首页</A> ");
			strb.append(" \n<A href=javascript:viewPage(" + (intPage - 1) + ")>前页</A> ");
		}
		if (intPageCount == intPage) {
			strb.append(" 后页 ");
			strb.append(" 尾页 ");
		} else {
			strb.append(" \n<A href=javascript:viewPage(" + (intPage + 1) + ")>后页</A> ");
			strb.append(" \n<A href=javascript:viewPage(" + intPageCount + ")>尾页</A>");
		}
		strb.append(" 页次：" + intPage + "/" + intPageCount + "页 ");
		strb.append("\n<input name=pagesize type=text size=2 value=" + intPageSize + ">" + "条/页 ");
		strb.append("\n<select name=curpage>\n");
		for (int i = 1; i <= intPageCount; i++)
			if (i == intPage)
				strb.append("<option value=" + i + " selected=\"selected\">" + i + "</option>\n");
			else
				strb.append("<option value=" + i + ">" + i + "</option>\n");

		strb.append("</select>");
		if (!map.isEmpty()) {
			for (Iterator<Entry<String, Object>> imap = map.entrySet().iterator(); imap.hasNext(); strb.append("\">\n")) {
				Entry<String, Object> entry = (Entry<String, Object>) imap.next();
				String key = (String) entry.getKey();
				String value = (String) entry.getValue();
				strb.append("<input type=\"hidden\" name=\"");
				strb.append(key);
				strb.append("\" value=\"");
				strb.append(value);
			}

		}
		strb.append(
				"页 <input type=button name=Submit value=Go language=javascript onclick=viewPage(document.frmPage.curpage.value)></form>");
		return strb.toString();
	}

	public String javascript() {
		StringBuffer javascript = new StringBuffer("");
		javascript.append(" <script language='javascript'>\n");
		javascript.append("    function viewPage(ipage){\n");
		javascript.append("        document.frmPage.curpage.value=ipage;\n");
		javascript.append("        document.frmPage.submit();\n");
		javascript.append("    }\n");
		javascript.append(" </script>\n");
		return javascript.toString();
	}

	private int intPageSize;
	private int intRowCount;
	private int intPageCount;
	private int intPage;
	private String strPageFileName;
	private Map<String, Object> map;
}