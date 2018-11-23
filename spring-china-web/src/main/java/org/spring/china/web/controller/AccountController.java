package org.spring.china.web.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.spring.china.base.common.Res;
import org.spring.china.base.feign.UserFeign;
import org.spring.china.base.util.HttpRequest;
import org.spring.china.web.common.CustomAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.databind.ObjectMapper;

@RequestMapping("/account")
@Controller
public class AccountController {
	
	@Autowired
    private CustomAuthenticationProvider provider;//自定义验证
	
    @Autowired
    private HttpServletRequest request;
    
	@Autowired
    private UserFeign userFeign;
    

    
    @SuppressWarnings("unchecked")
	@RequestMapping(value="/wx-register")
	public ModelAndView Register(@RequestParam(value="code",required=true) String code){
		ModelAndView model = new ModelAndView("views/account/register");
		String res_access_token=HttpRequest.sendGet("https://api.weixin.qq.com/sns/oauth2/access_token", "appid=wx60dfcd85bdcd3b0e&secret=337520ecb5f15aac9a7219c69c932a16&code="+code+"&grant_type=authorization_code");
		ObjectMapper mapper=new ObjectMapper();
		try{
			Map<String,String> result= mapper.readValue(res_access_token, Map.class);
			String access_token=result.get("access_token");
   
    		String res_userinfo= HttpRequest.sendGet("https://api.weixin.qq.com/sns/userinfo", "access_token="+access_token+"&openid="+code);
			Map<String,Object> map_userinfo=mapper.readValue(res_userinfo, Map.class);

			String avatarUrl=map_userinfo.get("headimgurl").toString();
			model.addObject("avatarUrl",avatarUrl);
			String _nickName=map_userinfo.get("nickname").toString();
			model.addObject("nickName",_nickName);
			model.addObject("sex",Integer.parseInt(map_userinfo.get("sex").toString()));
			String wxId=map_userinfo.get("unionid").toString();
			model.addObject("wxId",wxId);
			
			Res<Boolean> res_check_logined_wx=userFeign.CheckUserLoginedWX(wxId);
			
			if(res_check_logined_wx.getCode()==0){
				if(res_check_logined_wx.getData()){
					//表示已经注册过了用微信
					model.addObject("errMsg","您的微信帐号已经绑定过了，请直接登录！");
					model.setViewName("/error");
					return model;
				}
			}else{
				model.addObject("errMsg","系统内部错误，请联系管理员！");
				model.setViewName("/error");
				return model;
			}
			
		} catch(Exception e){
			
		}
		return model;
	}

    @SuppressWarnings("unchecked")
    @PreAuthorize("hasRole('ROLE_USER')")  
	@RequestMapping(value="/wx-bind")
	public ModelAndView WxBind(@RequestParam(value="code",required=true) String code){
    	ModelAndView model = new ModelAndView("views/account/wx_bind");
		String res_access_token=HttpRequest.sendGet("https://api.weixin.qq.com/sns/oauth2/access_token", "appid=wx60dfcd85bdcd3b0e&secret=337520ecb5f15aac9a7219c69c932a16&code="+code+"&grant_type=authorization_code");
		ObjectMapper mapper=new ObjectMapper();
		try{
			Object session_userId =request.getSession().getAttribute("userId");
			long userId_forUse=Long.parseLong(session_userId.toString());
			
			Map<String,String> result= mapper.readValue(res_access_token, Map.class);
			String access_token=result.get("access_token");
   
    		String res_userinfo= HttpRequest.sendGet("https://api.weixin.qq.com/sns/userinfo", "access_token="+access_token+"&openid="+code);
			Map<String,Object> map_userinfo=mapper.readValue(res_userinfo, Map.class);
 
			String wxId=map_userinfo.get("unionid").toString();

			Res<Boolean> res_check_logined_wx=userFeign.CheckUserLoginedWX(wxId);
			
			if(res_check_logined_wx.getCode()==0){
				if(res_check_logined_wx.getData()){
					//表示已经注册过了用微信
					model.addObject("binded",0);
				}else{
					this.userFeign.EditUserWxId(userId_forUse, wxId);
					model.addObject("binded",1);
				}
			}else{
				model.addObject("binded",0);
			}
			
		} catch(Exception e){
			model.addObject("binded",0);
		}
		return model;
	}
    
    @RequestMapping(value="/login")
    public ModelAndView Login(){
        ModelAndView model = new ModelAndView("views/account/login");
        return model;
    }

    @SuppressWarnings("unchecked")     
	@RequestMapping(value="/wx-login")
    public ModelAndView WxLogin(@RequestParam(value="code",required=true) String code){
    	ModelAndView model = new ModelAndView("views/account/wx_login");

		String res_access_token=HttpRequest.sendGet("https://api.weixin.qq.com/sns/oauth2/access_token", "appid=wx60dfcd85bdcd3b0e&secret=337520ecb5f15aac9a7219c69c932a16&code="+code+"&grant_type=authorization_code");
		
		ObjectMapper mapper=new ObjectMapper();
		
		try {
			Map<String,String> result= mapper.readValue(res_access_token, Map.class);
			String access_token=result.get("access_token");
   
    		String res_userinfo= HttpRequest.sendGet("https://api.weixin.qq.com/sns/userinfo", "access_token="+access_token+"&openid="+code);
			Map<String,Object> map_userinfo=mapper.readValue(res_userinfo, Map.class);

			String avatarUrl=map_userinfo.get("headimgurl").toString();
			model.addObject("avatarUrl",avatarUrl);
			String _nickName=map_userinfo.get("nickname").toString();
			model.addObject("nickName",_nickName);
			model.addObject("sex",Integer.parseInt(map_userinfo.get("sex").toString()));
			String wxId=map_userinfo.get("unionid").toString();
			model.addObject("wxId",wxId);
			
			Res<Boolean> res_check_logined_wx=userFeign.CheckUserLoginedWX(wxId);
			
			if(res_check_logined_wx.getCode()==0){
				model.addObject("wxLogined",res_check_logined_wx.getData());

				if(res_check_logined_wx.getData()){
					//如果已经用微信登陆过			
					request.setAttribute("wxId", wxId);
					UsernamePasswordAuthenticationToken token=new UsernamePasswordAuthenticationToken(wxId,wxId);
					token.setDetails(new WebAuthenticationDetails(request));
			        Authentication authentication = this.provider.authenticate(token);    		 
			        SecurityContextHolder.getContext().setAuthentication(authentication);
				}else{
					//请先绑定微信
					model.addObject("errMsg","请先绑定微信或注册！");
					model.setViewName("/error");
					return model;
				}
			}else{
				model.addObject("errMsg","系统内部错误，请联系管理员！");
				model.setViewName("/error");
				return model;
			}
			
			
			
		} catch (Exception ex) {
			model.addObject("res",false);//code无效
			model.addObject("wxLogined",false);
		}
     
        return model;
    }
}
