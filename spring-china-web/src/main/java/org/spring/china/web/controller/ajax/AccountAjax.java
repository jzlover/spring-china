package org.spring.china.web.controller.ajax;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.spring.china.base.common.Res;
import org.spring.china.base.feign.TopicFeign;
import org.spring.china.base.feign.UserFeign;
import org.spring.china.base.pojo.User;
import org.spring.china.base.pojo.UserRole;
import org.spring.china.base.util.Common;
import org.spring.china.base.util.ImageCut;
import org.spring.china.web.common.CustomAuthenticationProvider;
import org.spring.china.web.model.Model_User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/account-ajax")
@RestController
public class AccountAjax {
	
	@Value("${UPLOAD_PATH}")
	private String UPLOAD_PATH;

	@Autowired
    private CustomAuthenticationProvider provider;//自定义验证
	
    @Autowired
    private HttpServletRequest request;

    @Autowired
    private UserFeign userFeign;
    

    @ResponseBody
    @RequestMapping(value="/check-userName",method= RequestMethod.POST)
    public Res<Boolean> CheckUserName(@RequestBody Model_User user){
    	return userFeign.CheckUserName(user.getUserName());
    }

    @ResponseBody
    @RequestMapping(value="/check-nickName",method=RequestMethod.POST)
    public Res<Boolean> CheckUserNickName(@RequestBody Model_User user){
    	return userFeign.CheckNickName(user.getNickName());
    }
    
    
	@ResponseBody
    @RequestMapping(value="/register",method=RequestMethod.POST)
    public Res<Long> Register(@RequestBody User user){
    	Res<Long> res=new Res<Long>();
        List<UserRole> roles=new ArrayList<UserRole>();
        UserRole role=new UserRole();
        role.setRoleName("ROLE_USER");//默认的用户角色为ROLE_USER
        roles.add(role);
        user.setRoles(roles);
        user.setId(0L);//好奇怪，这个不加居然会传不过去，熔断错误直接
        res=userFeign.Register(user);

        if(res.getCode()==0){
        	try {
            	if(user.getWxAvatarUrl()!=null || user.getWxAvatarUrl() !=""){
            		Common.DownloadWebPic(user.getWxAvatarUrl(), res.getData()+"_large.jpg", UPLOAD_PATH+"/avatar/");
            		ImageCut.scale(UPLOAD_PATH+"/avatar/"+res.getData()+"_large.jpg", UPLOAD_PATH+"/avatar/"+res.getData()+"_normal.jpg", 2, false);
            		ImageCut.scale(UPLOAD_PATH+"/avatar/"+res.getData()+"_large.jpg", UPLOAD_PATH+"/avatar/"+res.getData()+"_small.jpg", 4, false);
        			userFeign.EditUserAvatar(res.getData(),true);
            	}			
    		} catch (Exception e) {
    			e.printStackTrace();
    		}
            
            //设置用户自动登录
            UsernamePasswordAuthenticationToken token=new UsernamePasswordAuthenticationToken(user.getUserName(),user.getPassword());
    		token.setDetails(new WebAuthenticationDetails(request));
            Authentication authentication = this.provider.authenticate(token);    		 
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        
        
        return res;
    }
	
	@PreAuthorize("hasRole('ROLE_USER')")
	@ResponseBody
    @RequestMapping(value="/edit-user-wxId",method=RequestMethod.POST)
	public Res<Boolean> EditUserWxId(@RequestBody User user){
		return this.userFeign.EditUserWxId(user.getId(),user.getWxId());
	}
	
	
}
