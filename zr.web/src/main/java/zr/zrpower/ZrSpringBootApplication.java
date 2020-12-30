package zr.zrpower;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

/**
 * SpringBoot项目启动类
 * 如果使用Hibernate，需要加入HibernateJpaAutoConfiguration.class
 * @author ftl
 * @version vue-7.0
 */
//解决多模块无法注解：配置扫描注解
//@ComponentScan(basePackages = {"zr.zrpower"})
//@SpringBootApplication
////@MapperScan({"zr.zrpower.dao.mapper"})
////@MapperScan(value = { "zr.zrpower.dao.mapper"})

@ComponentScan(basePackages = {"zr.zrpower"})
@SpringBootApplication 
@EnableAutoConfiguration(
exclude={
		DataSourceAutoConfiguration.class,
        HibernateJpaAutoConfiguration.class,
        DataSourceTransactionManagerAutoConfiguration.class
})
public class ZrSpringBootApplication {// extends WebMvcConfigurationSupport

	public static void main(String[] args) {
		SpringApplication.run(ZrSpringBootApplication.class, args);
	}
}