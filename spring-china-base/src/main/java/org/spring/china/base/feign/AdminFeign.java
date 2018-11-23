package org.spring.china.base.feign;

 
import org.spring.china.base.feign.fallback.AdminFeignFallback;
import org.springframework.cloud.netflix.feign.FeignClient;

@FeignClient(value="spring-china-admin",fallback = AdminFeignFallback.class)
public interface AdminFeign {

}
