package com.yonglilian.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * SpringBoot集成Spring Security的配置
 * @author lwk
 *
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	private static final String[] exclusivePaths = {
			"/**", "/static/**", "/css/**", "/js/**", 
			"/image/**", "/fonts/**", "/favicon.ico"
	};

	/**
	 * authorizeRequests(): 定义哪些URL需要被保护，哪些不需要被保护
	 */
	@Override
    protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable();
		http.authorizeRequests().antMatchers(exclusivePaths).permitAll()
         	.anyRequest().authenticated()
         	.and()
         	.formLogin().loginPage("/").defaultSuccessUrl("/index").permitAll()
         	.and()
         	.headers().frameOptions().disable()
         	.and()
         	.logout().permitAll();// 注销请求可任意访问
    }

	/**
	 * 基于内存的用户存储
	 */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
            .withUser("root").password("root").roles("ADMIN")
            .and()
            .withUser("admin").password("admin").roles("ADMIN")
            .and()
            .withUser("user").password("user").roles("USER");
    }

}
