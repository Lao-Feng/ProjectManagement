package zr.zrpower.intercept;

import com.blogspot.radialmind.html.HTMLParser;
import com.blogspot.radialmind.xss.XSSFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.StringReader;
import java.io.StringWriter;

/**
 * XSS保护
 * @author lwk
 */
public class XssHttpWrapper extends HttpServletRequestWrapper {
	private HttpServletRequest orgRequest;

	public XssHttpWrapper(HttpServletRequest request) {
		super(request);
		orgRequest = request;
	}

	/**
	 * 覆盖getParameter方法，将参数名和参数值都做xss过滤。<br/>
	 * 如果需要获得原始的值，则通过super.getParameterValues(name)来获取<br/>
	 * getParameterNames,getParameterValues和getParameterMap也可能需要覆盖
	 */
	@Override
	public String getParameter(String name) {
		String value = super.getParameter(xssEncode(name));
		if (value != null) {
			value = xssEncode(value);
		}
		return value;
	}

	/**
	 * 覆盖getHeader方法，将参数名和参数值都做xss过滤。<br/>
	 * 如果需要获得原始的值，则通过super.getHeaders(name)来获取<br/>
	 * getHeaderNames 也可能需要覆盖
	 */
	@Override
	public String getHeader(String name) {
		String value = super.getHeader(xssEncode(name));
		if (value != null) {
			value = xssEncode(value);
		}
		return value;
	}

	/**
	 * 将容易引起xss漏洞的半角字符直接替换成全角字符
	 * @param str
	 * @return
	 */
	private static String xssEncode(String str) {
		if (str == null || str.isEmpty()) {
			return str;
		}
		StringReader reader = new StringReader(str);
		StringWriter writer = new StringWriter();
		try {
			HTMLParser.process(reader, writer, new XSSFilter(), true);
			return writer.toString();
		} catch (NullPointerException e) {
			return str;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取最原始的request
	 * @return
	 */
	public HttpServletRequest getOrgRequest() {
		return orgRequest;
	}

	/**
	 * 获取最原始的request的静态方法
	 * @return
	 */
	public static HttpServletRequest getOrgRequest(HttpServletRequest req) {
		if (req instanceof XssHttpWrapper) {
			return ((XssHttpWrapper) req).getOrgRequest();
		}
		return req;
	}
}