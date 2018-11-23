package org.spring.china.web.common;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.spring.china.base.common.Res;
import org.spring.china.base.common.UserAllMsgCount;
import org.spring.china.base.feign.TopicFeign;
import org.spring.china.base.feign.UserFeign;
import org.spring.china.web.model.Model_UserInitData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Component
public class UserInterceptor implements HandlerInterceptor{
 
	
	@Autowired
	private UserFeign userFeign;
	
	 
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		Object username=request.getSession().getAttribute("userName");
		if(username==null){
			//表示没有登录
		}else{
			//已经登录
			if (request.getHeader("x-requested-with") != null && 
					request.getHeader("x-requested-with").equalsIgnoreCase("XMLHttpRequest")){
				//ajax请求
				
			}else{
				Long userId=Long.parseLong(request.getSession().getAttribute("userId").toString());
		    	//Res<Integer> res_comment_unreaded=userFeign.QueryUserCommentReadedCount(userId, false);
				Res<UserAllMsgCount> res_all_msg_counts=userFeign.QueryUserAllMsgCount(userId);
		    	if(res_all_msg_counts.getCode()==0){
		    		request.getSession().setAttribute("commentUnReadedCount", res_all_msg_counts.getData().getCommentCount());
		    		request.getSession().setAttribute("privateMsgCount", res_all_msg_counts.getData().getPrivateMsgCount());
		    	}  
			}
		}
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

}
