package zr.zrpower.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

/**
 * SpringBoot加载配置文件
 * @author lwk
 *
 */
@Configuration
@ImportResource(locations = {
	"classpath*:application-context.xml",
	"classpath*:spring-servlet.xml"
})
public class SpringBootConfig {

}