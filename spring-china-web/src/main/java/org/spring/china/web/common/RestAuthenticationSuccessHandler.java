package org.spring.china.web.common;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
public class RestAuthenticationSuccessHandler implements AuthenticationSuccessHandler{
	public String getIpAddress(HttpServletRequest request){      
        String ip = request.getHeader("x-forwarded-for");      
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {      
            ip = request.getHeader("Proxy-Client-IP");      
        }      
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {      
            ip = request.getHeader("WL-Proxy-Client-IP");      
        }      
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {      
            ip = request.getHeader("HTTP_CLIENT_IP");      
        }      
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {      
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");      
        }      
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {      
            ip = request.getRemoteAddr();      
        }      
        return ip;      
    }

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request,
			HttpServletResponse response, Authentication authentication)
			throws IOException, ServletException {
		response.setContentType("application/json");  
        response.setCharacterEncoding("UTF-8");  
        response.getWriter().write("{\"result\":\"ok\"}");  
        response.getWriter().flush(); 
	}

  
}
