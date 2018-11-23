package org.spring.china.user;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Created by jzlover on 2017/6/9.
 */
@EnableCircuitBreaker
@EnableDiscoveryClient
@SpringBootApplication
@EnableTransactionManagement
@EnableFeignClients(basePackages = "org.spring.china.base.feign")
public class UserApp {

    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        new SpringApplicationBuilder(UserApp.class).web(true).run(args);
    }
}
