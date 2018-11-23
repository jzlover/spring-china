package org.spring.china.web.config;

import org.spring.china.web.common.UserInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class AddInterceptor extends WebMvcConfigurerAdapter{

	@Bean
    public HandlerInterceptor getUserInterceptor(){
        return new UserInterceptor();
    }
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(getUserInterceptor()).addPathPatterns("/**");
		super.addInterceptors(registry);
	}
	
	
}
