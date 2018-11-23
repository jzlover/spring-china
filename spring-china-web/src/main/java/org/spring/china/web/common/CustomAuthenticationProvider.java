package org.spring.china.web.common;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;


import org.spring.china.base.common.Res;
import org.spring.china.base.feign.UserFeign;
import org.spring.china.base.pojo.User;
import org.spring.china.base.pojo.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.thoughtworks.xstream.core.util.Base64Encoder;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider{
 
	
	@Autowired  
	private  HttpServletRequest request; 
	
	@Autowired 
	private UserFeign userFeign;
	
	@Override
	public Authentication authenticate(Authentication authentication)
			throws AuthenticationException {
		Res<User> res_user;
		String _password=null;
		if(request.getAttribute("wxId")!=null){
			String wxId=request.getAttribute("wxId").toString();
			res_user=userFeign.QueryUserByWxId(wxId);
			_password=wxId;
		}else{
			//用户传进来的用户名
			String username_input=authentication.getName();
			//用户传进来的密码，未加密
		    _password=(String)authentication.getCredentials();
			
			res_user=userFeign.QueryUserByUserName(username_input);
			
			if(res_user.getCode()!=0){
				//如果通过用户名没有找到该用户则报错误。
				throw new BadCredentialsException("Username not found.");
			}
			
			if(res_user.getData()==null){
				throw new BadCredentialsException("Wrong password.");
			}
			
			MessageDigest md5;
			try {
				md5=MessageDigest.getInstance("MD5");
				Base64Encoder base64en=new Base64Encoder();
				String passwordSalt=res_user.getData().getPasswordSalt();
				//将用户的密码通过passwordSalt的字段的值相加进行MD5加密
				String md5password_with_salt=base64en.encode(md5.digest((_password+passwordSalt).getBytes("utf-8")));
				if(!md5password_with_salt.equals(res_user.getData().getPassword())){
					//如果密码不匹配
					throw new BadCredentialsException("Wrong password.");
				}
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		
		//获取用户角色
		Set<GrantedAuthority> authorities=new HashSet<GrantedAuthority>();
		for(UserRole r:res_user.getData().getRoles()){
			authorities.add(new SimpleGrantedAuthority(r.getRoleName()));
		}
		
		//如果用户名和密码匹配成功,获取该用户的角色信息
		request.getSession().setAttribute("userName", res_user.getData().getUserName());
		request.getSession().setAttribute("userId",res_user.getData().getId());
		request.getSession().setAttribute("nickName", res_user.getData().getNickName());
		request.getSession().setAttribute("avatarSmall",res_user.getData().getAvatarSmall());
		request.setAttribute("wxId", null);
	 
		return new UsernamePasswordAuthenticationToken(res_user.getData().getId(),_password,authorities);
	}

	@Override
	public boolean supports(Class<?> authentication) {
		// TODO Auto-generated method stub
		return true;
	}

}
