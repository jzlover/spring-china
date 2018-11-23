package org.spring.china.admin;
 
import org.spring.china.base.feign.fallback.AdminFeignFallback;
import org.spring.china.base.feign.fallback.TopicFeignFallback;
import org.spring.china.base.feign.fallback.UserFeignFallback;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableCircuitBreaker
@EnableDiscoveryClient
@SpringBootApplication
@EnableTransactionManagement
@EnableFeignClients(basePackages = "org.spring.china.base.feign")
public class AdminApp {

	@Bean
    public UserFeignFallback userServiceFallback(){
        return new UserFeignFallback();
    }

    @Bean
    public TopicFeignFallback postServiceFallback(){
    	return new TopicFeignFallback();
    }
    
    @Bean
    public AdminFeignFallback adminServiceFallback(){
    	return new AdminFeignFallback();
    }
    
	public static void main( String[] args )
    {
        new SpringApplicationBuilder(AdminApp.class).web(true).run(args);
    }
}
