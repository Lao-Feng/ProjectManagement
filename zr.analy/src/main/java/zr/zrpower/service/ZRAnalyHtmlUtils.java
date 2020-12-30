package zr.zrpower.service;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import java.io.StringWriter;
import java.util.Map;
import java.util.Properties;

/**
 * 统计引擎生成view模板工具类
 * 
 * @author nfzr
 *
 */
public class ZRAnalyHtmlUtils {

	/**
	 * 生成代码
	 */
	public static String htmlCode( Map<String, Object> map) {
		
		// 设置velocity资源加载器
		Properties prop = new Properties();
		prop.put( "file.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader" );
		Velocity.init( prop );
		
		// 封装模板数据
		VelocityContext context = new VelocityContext( map );

		// 获取模板列表
		StringWriter sw = new StringWriter();
		// 渲染模板
		Template tpl = Velocity.getTemplate( "template/analyview.html.vm", "UTF-8" );
		tpl.merge( context, sw );

		return sw.toString();
	}
	
	/**
	 * 生成统计引擎vue+js代码
	 */
	public static String scriptCode( Map<String, Object> map) {
		
		// 设置velocity资源加载器
		Properties prop = new Properties();
		prop.put( "file.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader" );
		Velocity.init( prop );
		
		// 封装模板数据
		VelocityContext context = new VelocityContext( map );

		// 获取模板列表
		StringWriter sw = new StringWriter();
		// 渲染模板
		Template tpl = Velocity.getTemplate( "template/collscript.html.vm", "UTF-8" );
		tpl.merge( context, sw );

		return sw.toString();
	}
	
	/**
	 * 生成表单引擎vue+dialog代码
	 */
	public static String dialogCode( Map<String, Object> map) {
		
		// 设置velocity资源加载器
		Properties prop = new Properties();
		prop.put( "file.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader" );
		Velocity.init( prop );
		
		// 封装模板数据
		VelocityContext context = new VelocityContext( map );

		// 获取模板列表
		StringWriter sw = new StringWriter();
		// 渲染模板
		Template tpl = Velocity.getTemplate( "template/colldialog.html.vm", "UTF-8" );
		tpl.merge( context, sw );

		return sw.toString();
	}

}
