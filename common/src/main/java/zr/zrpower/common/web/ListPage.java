package zr.zrpower.common.web;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class ListPage {

	public ListPage(String PageFileName, int PageSize, int Page) {
		intPageSize = 0;
		intRowCount = 0;
		intPageCount = 0;
		intPage = 1;
		map = new HashMap<String, Object>();
		objs = null;
		intPageSize = PageSize;
		intPage = Page;
		strPageFileName = PageFileName;
	}

	public void SetObjectSet(Object obj[]) {
		if (obj != null) {
			objs = obj;
			intRowCount = objs.length;
			if (intRowCount == 0)
				intPageCount = 0;
			else if (intPageSize == 0) {
				intPageCount = 0;
			} else {
				intPageCount = intRowCount / intPageSize;
				if (intRowCount % intPageSize > 0)
					intPageCount++;
			}
		} else {
			intRowCount = 0;
			intPageCount = 0;
		}
		System.out.println("装载数据集后：");
		System.out.println("intRowCount:" + Integer.toString(intRowCount) + "\nintPageCount:" + Integer.toString(intPageCount));
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

	public Object[] getThisObjectSet() {
		if (intRowCount == 0)
			return null;
		Object tmpObj[] = null;
		int iStartRow;
		int iEndRow;
		if (intPage == 1) {
			iStartRow = 0;
			if (intPageSize < intRowCount)
				iEndRow = intPageSize - 1;
			else
				iEndRow = intRowCount - 1;
		} else {
			iStartRow = (intPage - 1) * intPageSize;
			if (iStartRow + intPageSize < intRowCount)
				iEndRow = (iStartRow + intPageSize) - 1;
			else
				iEndRow = intRowCount - 1;
		}
		System.out.println("iEndRow:" + Integer.toString(iEndRow) + "\niStartRow" + Integer.toString(iStartRow));
		tmpObj = new Object[(iEndRow - iStartRow) + 1];
		int j = 0;
		for (int i = iStartRow; i <= iEndRow; i++) {
			tmpObj[j] = objs[i];
			if (tmpObj[j] != null)
				System.out.println("返回的数据集不为空！");
			else
				System.out.println("返回的数据集是空的！");
			j++;
		}
		return tmpObj;
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
		strb.append("页 <input type=button name=Submit value=Go language=javascript onclick=viewPage(document.frmPage.curpage.value)></form>");
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

	private Object objs[];
	private int intPageSize;
	private int intRowCount;
	private int intPageCount;
	private int intPage;
	private String strPageFileName;
	private Map<String, Object> map;
}