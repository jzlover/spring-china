package springcloud;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.context.annotation.Configuration;

/**
 * Created by jzlover on 2017/6/10.
 */
@Configuration
@EnableAutoConfiguration
@EnableDiscoveryClient
@EnableConfigServer
public class ConfigServerApp {
    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        new SpringApplicationBuilder(ConfigServerApp.class).web(true).run(args);
    }
}
