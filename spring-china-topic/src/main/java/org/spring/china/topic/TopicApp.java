package org.spring.china.topic;

import org.spring.china.base.feign.fallback.TopicFeignFallback;
import org.spring.china.base.feign.fallback.UserFeignFallback;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.client.RestTemplate;

/**
 * Hello world!
 *
 */
@EnableCircuitBreaker
@EnableDiscoveryClient
@SpringBootApplication
@EnableTransactionManagement
@EnableFeignClients(basePackages = "org.spring.china.base.feign")
public class TopicApp
{
 
	@Bean
    public UserFeignFallback userServiceFallback(){
        return new UserFeignFallback();
    }

    @Bean
    public TopicFeignFallback postServiceFallback(){
    	return new TopicFeignFallback();
    }
    
    public static void main( String[] args )
    {
        new SpringApplicationBuilder(TopicApp.class).web(true).run(args);
    }
}
