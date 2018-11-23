package org.spring.china.web;


import org.spring.china.base.feign.fallback.TopicFeignFallback;
import org.spring.china.base.feign.fallback.UserFeignFallback;
import org.spring.china.web.common.UserInterceptor;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * Hello world!
 *
 */
@EnableCircuitBreaker
@EnableDiscoveryClient
@SpringBootApplication
@EnableFeignClients(basePackages = "org.spring.china.base.feign")
public class WebApp
{

    @Bean
    public UserFeignFallback userFeignFallback(){
        return new UserFeignFallback();
    }

    @Bean
    public TopicFeignFallback postFeignFallback(){
    	return new TopicFeignFallback();
    }
    
    

    public static void main( String[] args )
    {
        new SpringApplicationBuilder(WebApp.class).web(true).run(args);
    }
}
