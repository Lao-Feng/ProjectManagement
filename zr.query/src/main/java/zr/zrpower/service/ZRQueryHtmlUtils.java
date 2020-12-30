package zr.zrpower.service;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * 查询引擎生成view模板工具类
 * 
 * @author nfzr
 *
 */
public class ZRQueryHtmlUtils {

	/**
	 * 生成代码
	 */
	public static String generatorCode( Map<String, Object> query) {
		
		// 设置velocity资源加载器
		Properties prop = new Properties();
		prop.put( "file.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader" );
		Velocity.init( prop );
		//预先处理按钮参数P1-P5参数
        @SuppressWarnings("unchecked")
		List<String> queryClikeList = (List<String>) query.get("clicks");
        @SuppressWarnings("unchecked")
		Map<String,Object> hidden =  (Map<String, Object>) query.get("hidden");
        if(queryClikeList!=null&&queryClikeList.size()>0) {
        	List<String> queryNewClikeList = new ArrayList<>();
        	int len = queryClikeList.size();
        	for(int i = 0;i<len;i++) {
        		String jsStr = queryClikeList.get(i).toString();
        		jsStr = jsStr.replaceAll("\\{P1\\}",String.valueOf(hidden.get("P1")));
        		jsStr = jsStr.replaceAll("\\{P2\\}",String.valueOf(hidden.get("P2")));
        		jsStr = jsStr.replaceAll("\\{P3\\}",String.valueOf(hidden.get("P3")));
        		jsStr = jsStr.replaceAll("\\{P4\\}",String.valueOf(hidden.get("P4")));
        		jsStr = jsStr.replaceAll("\\{P5\\}",String.valueOf(hidden.get("P5")));
        		
//        		jsStr = jsStr.replaceAll("{P1}",String.valueOf(hidden.get("P1")));
//        		jsStr = jsStr.replaceAll("{P2}",String.valueOf(hidden.get("P2")));
//        		jsStr = jsStr.replaceAll("{P3}",String.valueOf(hidden.get("P3")));
//        		jsStr = jsStr.replaceAll("{P4}",String.valueOf(hidden.get("P4")));
//        		jsStr = jsStr.replaceAll("{P5}",String.valueOf(hidden.get("P5")));
        		queryNewClikeList.add(jsStr);
        	}
        	query.put("clicks", queryNewClikeList);
        }
		// 封装模板数据
		VelocityContext context = new VelocityContext( query );

		// 获取模板列表
		StringWriter sw = new StringWriter();
		// 渲染模板
		Template tpl = Velocity.getTemplate( "template/querylist.html.vm", "UTF-8" );
		tpl.merge( context, sw );

		return sw.toString();
	}

}
