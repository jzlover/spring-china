package org.spring.china.web.common;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class WebAspect {

	@Pointcut("within(org.spring.china.web.controller.*) && @annotation(org.spring.china.web.common.JLogger)")
    public void controllerMethods(){}  
	
	@Around("controllerMethods()")
    public Object Interceptor(ProceedingJoinPoint joinPoint){
        Object result = null; 
//        Object[] args = pjp.getArgs();
//        if(args != null && args.length >0) {
//            String deviceId = (String) args[0];
//             if(!"03".equals(deviceId)) {
//                 return "no anthorization";
//             }
//        }     
        try {

            long start = System.currentTimeMillis();
            
            result = joinPoint.proceed();
         
            long executionTime = System.currentTimeMillis() - start;
         
            System.out.println(joinPoint.getSignature() + " executed in " + executionTime + "ms");
        } catch (Throwable e) {
            e.printStackTrace();
        }  
        return result;
    }
}
