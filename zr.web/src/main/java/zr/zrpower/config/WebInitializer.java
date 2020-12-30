//package zr.zrpower.config;
//
//import javax.servlet.ServletContext;
//import javax.servlet.ServletException;
//import javax.servlet.ServletRegistration.Dynamic;
//
//import org.springframework.web.WebApplicationInitializer;
//import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
//import org.springframework.web.servlet.DispatcherServlet;
//
///**
// * 项目的Web配置，替代web.xml的位置(与web.xml等价)
// * @author lwk
// *
// */
//public class WebInitializer implements WebApplicationInitializer {
//
//	@Override
//	public void onStartup(ServletContext servletContext) throws ServletException {
//		AnnotationConfigWebApplicationContext ctx = new AnnotationConfigWebApplicationContext();
//		ctx.register(SpringMvcConfig.class);
//		// 新建WebApplicationContext，注册配置类，并将其和当前的servletContext关联
//		ctx.setServletContext(servletContext);
//		
//		// 注册Spring MVC的DispatcherServlet
//		Dynamic servlet = servletContext.addServlet("dispatcher", new DispatcherServlet(ctx));
//		servlet.setLoadOnStartup(1);
//		servlet.addMapping("/");
//	}
//}