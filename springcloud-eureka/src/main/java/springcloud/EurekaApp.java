package springcloud;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * Created by jzlover on 2017/6/10.
 */
@EnableEurekaServer
@SpringBootApplication
@RefreshScope
public class EurekaApp {
    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        new SpringApplicationBuilder(EurekaApp.class).web(true).run(args);
    }
}
