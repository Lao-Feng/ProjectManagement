package zr.zrpower.intercept;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Xss保护过滤器
 * @author lwk
 */
public class XssFilter implements Filter {
	public void init(FilterConfig filterConfig) throws ServletException {
		System.out.println("Xss filter inited!");
	}

	public void doFilter(ServletRequest request, ServletResponse response, 
			FilterChain chain) throws IOException, ServletException {
		XssHttpWrapper xssRequest = new XssHttpWrapper((HttpServletRequest) request);
		chain.doFilter(xssRequest, response);
	}

	public void destroy() {
		System.out.println("Xss filter destroyed!");
	}
}