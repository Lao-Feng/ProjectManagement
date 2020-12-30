//package zr.zrpower.config;
//
//import java.util.List;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.ComponentScan;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.format.FormatterRegistry;
//import org.springframework.validation.MessageCodesResolver;
//import org.springframework.validation.Validator;
//import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer;
//import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
//import org.springframework.web.servlet.config.annotation.CorsRegistry;
//import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
//import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
//import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
//import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
//import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
//import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//import org.springframework.web.servlet.view.InternalResourceViewResolver;
//import org.springframework.web.servlet.view.JstlView;
//
///**
// * SpringMvc静态资源映射
// * @author lwk
// *
// */
//@Configuration
////@EnableWebMvc //这个注解会覆盖掉SpringBoot的默认的静态资源映射配置
//@ComponentScan("zr.zrpower.config")
//public class SpringMvcConfig implements WebMvcConfigurer {
//	/**
//	 * 设置项目的视图解析器只解析views下的jsp文件
//	 * @return
//	 */
//	@Bean
//	public InternalResourceViewResolver viewResolver() {
//		InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
//		viewResolver.setPrefix("/WEB-INF/views/");
//		viewResolver.setSuffix(".jsp");
//		viewResolver.setViewClass(JstlView.class);
//
//		return viewResolver;
//	}
//
//	/**
//	 * 项目静态文件(js/css/img)等需要直接访问，这里重写addResourceHandlers方法即可
//	 */
//	@Override
//	public void addResourceHandlers(ResourceHandlerRegistry registry) {
//		// addResourceHandler指的是对外暴露的访问路径，addResourceLocations指的是文件放置的目录
//		registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
//	}
//
//	@Override
//	public MessageCodesResolver getMessageCodesResolver() {
//		return null;
//	}
//
//	@Override
//	public void configurePathMatch(PathMatchConfigurer pathmatchconfigurer) {
//	}
//
//	@Override
//	public void configureContentNegotiation(
//			ContentNegotiationConfigurer contentnegotiationconfigurer) {
//	}
//
//	@Override
//	public void configureAsyncSupport(AsyncSupportConfigurer asyncsupportconfigurer) {
//	}
//
//	@Override
//	public void configureDefaultServletHandling(
//			DefaultServletHandlerConfigurer defaultservlethandlerconfigurer) {
//	}
//
//	@Override
//	public void addFormatters(FormatterRegistry formatterregistry) {
//	}
//
//	@Override
//	public void addInterceptors(InterceptorRegistry interceptorregistry) {
//	}
//
//	@Override
//	public void addCorsMappings(CorsRegistry corsregistry) {
//	}
//
//	@Override
//	public void addViewControllers(ViewControllerRegistry viewcontrollerregistry) {
//	}
//
//	@Override
//	public void configureViewResolvers(ViewResolverRegistry viewresolverregistry) {
//	}
//
//	@SuppressWarnings("rawtypes")
//	@Override
//	public void addArgumentResolvers(List list) {
//	}
//
//	@SuppressWarnings("rawtypes")
//	@Override
//	public void addReturnValueHandlers(List list) {
//	}
//
//	@SuppressWarnings("rawtypes")
//	@Override
//	public void configureMessageConverters(List list) {
//	}
//
//	@SuppressWarnings("rawtypes")
//	@Override
//	public void extendMessageConverters(List list) {
//	}
//
//	@SuppressWarnings("rawtypes")
//	@Override
//	public void configureHandlerExceptionResolvers(List list) {
//	}
//
//	@SuppressWarnings("rawtypes")
//	@Override
//	public void extendHandlerExceptionResolvers(List list) {
//	}
//
//	@Override
//	public Validator getValidator() {
//		return null;
//	}
//
//}