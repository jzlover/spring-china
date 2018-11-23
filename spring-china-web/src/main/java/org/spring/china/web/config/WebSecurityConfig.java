package org.spring.china.web.config;

import org.spring.china.web.common.CustomAuthenticationProvider;
import org.spring.china.web.common.RestAuthenticationFailureHandler;
import org.spring.china.web.common.RestAuthenticationSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
 

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)//允许进入页面方法前检验
public class WebSecurityConfig extends WebSecurityConfigurerAdapter{

	@Autowired
    private CustomAuthenticationProvider provider;//自定义验证
	
	@Autowired
	private RestAuthenticationSuccessHandler successHandler;
	
	@Autowired
	private RestAuthenticationFailureHandler authenticationFailureHandler;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// TODO Auto-generated method stub
		http.authorizeRequests()
        .antMatchers("/",
        		"/msg/**","/endpointMsg/**",
                "/account/wx-register","/account/login","/user/home/**","/account/wx-login",
                "/user-ajax/auth","/account-ajax/logout","/account-ajax/register","/account-ajax/check-userName","/account-ajax/check-nickName","/user-ajax/query-user-topics-by-userId-pagely/**",
                "/user-ajax/find-ps-by-email","/user-ajax/query-user-recent-activities-by-userId-pagely/**","/user-ajax/query-user-recent-activities-pagely/**",
                "/user-ajax/query-user-by-id/**","/user-ajax/query-user-topics-pagely/**",
                "/topic/show/**",
                "/topic-ajax/query-topic-comments-pagely/**",
                "/public-resource/**",
                "/topics/**").permitAll()
        //其他地址的访问均需验证权限
        .anyRequest().authenticated()
        .and()
        .formLogin()
        .loginPage("/account/login")
        //指定登录页是"/login"                
        .failureHandler(authenticationFailureHandler)
        .successHandler(successHandler)
        .permitAll()
        .and()
        .logout()
        .logoutSuccessUrl("/topics")//退出登录后的默认url是"/home"
        .permitAll();

		http.headers().frameOptions().disable();
		http.csrf().disable();
	}
	
	@Override
    public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/scripts/**", "/content/**", "/system/**", "/**/favicon.ico");
    }
	
	@Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception
    {
    	//将验证过程交给自定义验证工具
        auth.authenticationProvider(provider);
    }
	
}
