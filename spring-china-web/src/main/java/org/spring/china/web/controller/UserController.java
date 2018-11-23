package org.spring.china.web.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.spring.china.base.common.*;
import org.spring.china.base.feign.TopicFeign;
import org.spring.china.base.feign.UserFeign;
import org.spring.china.base.pojo.Category;
import org.spring.china.base.pojo.Tag;
import org.spring.china.base.pojo.Topic;
import org.spring.china.base.pojo.User;
import org.spring.china.base.pojo.UserRole;
import org.spring.china.base.result.UserRecentActivity;
import org.spring.china.base.util.Common;
import org.spring.china.base.util.HttpRequest;
import org.spring.china.web.common.CustomAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;




import javax.servlet.http.HttpServletRequest;

/**
 * Created by jzlover on 2017/6/10.
 */

@RequestMapping("/user")
@Controller
public class UserController {

	@Value("${UPLOAD_PATH}")
	private String UPLOAD_PATH;

	
    @Autowired
    private HttpServletRequest request;
 

    @Autowired
    private UserFeign userFeign;
    
    @Autowired
    private TopicFeign topicFeign;

    
    
    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value="/edit-avatar")
	public ModelAndView EditAvatar(){
		ModelAndView model=new ModelAndView("views/user/edit_avatar");
		model.addObject("viewName","user_edit_avatar");
		return model;
	}
    
    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value="/edit-profile")
	public ModelAndView EditProfile(){
		ModelAndView model=new ModelAndView("views/user/edit_profile");
		Object userId =request.getSession().getAttribute("userId");
		Res<User> res=userFeign.QueryUserById(Long.parseLong(userId.toString()));
		if(res.getCode()!=0){
			model.setViewName("/error");
			return model;
		}			
		model.addObject("user",res.getData());
		model.addObject("viewName","user_edit_profile");
		return model;
	}
	
    @PreAuthorize("hasRole('ROLE_USER')")
	@RequestMapping(value="/edit-password")
	public ModelAndView EditPassword(){
		ModelAndView model=new ModelAndView("views/user/edit_password");
		model.addObject("viewName","user_edit_password");
		return model;
	}
    
    @PreAuthorize("hasRole('ROLE_USER')")
	@RequestMapping(value="/edit-account-binding")
    public ModelAndView EditAccountBinding(){
    	ModelAndView model=new ModelAndView("views/user/edit_account_binding");
    	model.addObject("viewName","user_edit_account_binding");
  
		Object session_userId =request.getSession().getAttribute("userId");
		long userId_forUse=Long.parseLong(session_userId.toString());			
		
		Res<User> user= this.userFeign.QueryUserById(userId_forUse);
		if(user.getCode()==0){
			String wxId = user.getData().getWxId();
			Boolean wx_binded=wxId!=null;
			model.addObject("wxBinded",wx_binded);
		}
		
    	return model;
    }
    
    
    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value="/msg-comment")
    public ModelAndView MsgComment(@RequestParam(value="show_type",required=false) Integer show_type){
    	ModelAndView model = new ModelAndView("views/user/msg_comment");
    	model.addObject("viewName","user_msg_comment");
    	Object session_userId =request.getSession().getAttribute("userId");
    	Boolean readed=null;
    	if(show_type==null|| show_type==0){
    		//全部
    		show_type=0;
    		readed=null;
    	}else if(show_type==1){
    		//未读取
    		readed=false;
    	}else{
    		//已读取
    		show_type=2;
    		readed=true;
    	}
    	
    	Res<Integer> res_comment_count=userFeign.QueryUserCommentReadedCount(Long.parseLong(session_userId.toString()), readed);
    	if(res_comment_count.getCode()!=0){
    		model.setViewName("/error");
			return model;
    	}
    	model.addObject("msgCommentCount",res_comment_count.getData());
    	model.addObject("showType",show_type);
    	return model;   	
    }
    
    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value="/msg-private")
    public ModelAndView MsgPrivate(){
    	ModelAndView model=new ModelAndView("/views/user/msg_private");
    	model.addObject("viewName","user_msg_private");
    	Object session_userId =request.getSession().getAttribute("userId");
    	return model;
    }
	
	@RequestMapping(value="/home")
	public ModelAndView Home(
			@RequestParam(value="id",required=false) Long userId,
			@RequestParam(value="categoryId",required=false) Long categoryId,
			@RequestParam(value="tagId",required=false) Long tagId){
		ModelAndView model = new ModelAndView("views/user/home");
		//表示看自己的
		Object session_userId =request.getSession().getAttribute("userId");
		long userId_forUse;
		if(userId==null){			
			if(session_userId==null){
				//如果没有登录则登录
				model.setViewName("redirect:/user/login");
				return model;
			}
			userId_forUse=Long.parseLong(session_userId.toString());			
		}else{
			userId_forUse=userId;
		}
		
		Res<User> user_res=userFeign.QueryUserById(userId_forUse);
		if(user_res.getCode()!=0){
			model.setViewName("/error");
			return model;
		}
		model.addObject("user",user_res.getData());
		
		//TODO 增加类别选择
		Res<List<Category>> categories_res=topicFeign.QueryAllCategories();
		if(categories_res.getCode()!=0){
			model.setViewName("/error");
			return model;
		}
		model.addObject("categories", categories_res.getData());
		ResPager<List<Topic>> topic_pager_res=userFeign.QueryUserTopicsPagely(userId_forUse,1,categoryId,tagId,1,20);
		if(topic_pager_res.getCode()!=0){
			model.setViewName("/error");
			return model;
		}
		model.addObject("modelTopic",topic_pager_res);
		
		Res<List<Tag>> res_tags = this.userFeign.QueryUserTags(userId_forUse, null);
		if(res_tags.getCode()!=0){
			model.setViewName("/error");
			return model;
		}
		model.addObject("tags",res_tags.getData());
		
		if(tagId!=null){
			Res<Tag> res_tag = this.userFeign.QueryTagById(tagId);
			if(res_tag.getCode()!=0){
				model.setViewName("/error");
				return model;
			}
			
			model.addObject("selTag",res_tag.getData());
		}
        
        
		
		/*
		Res<UserRelatedCommentPager> comment_readed_res=topicFeign.QueryUserCommentReaded(userId_forUse, false, 1, 20);
		if(comment_readed_res.getCode()!=0){
			model.setViewName("/error");
			return model;
		}
		model.addObject("modelCommentReaded",comment_readed_res.getData());
		*/
		
		ResPager<List<UserRecentActivity>> user_recent_activities_res=userFeign.QueryUserRecentActivitiesPagely(userId_forUse, 1, 20,null);
		
		if(user_recent_activities_res.getCode()!=0){
			model.setViewName("/error");
			return model;
		}
		
		model.addObject("modelRecentActivity",user_recent_activities_res);
		
		
		if(session_userId!=null){
			//如果是自己：1，别人：2
			model.addObject("mine",Long.parseLong(session_userId.toString())==userId_forUse?1:2);
		}else{
			//没有登录：0
			model.addObject("mine",0);
		}
		return model;
	}
	
	//只有自己登录后才能看
	@PreAuthorize("hasRole('ROLE_USER')")
	@RequestMapping("/draft")
	public ModelAndView Draft(
			@RequestParam(value="categoryId",required=false) Long categoryId,
			@RequestParam(value="tagId",required=false) Long tagId){
		ModelAndView model = new ModelAndView("views/user/draft");
		Object session_userId =request.getSession().getAttribute("userId");
		long userId_forUse;
		if(session_userId==null){
			//如果没有登录则登录
			model.setViewName("redirect:/user/login");
			return model;
		}
		userId_forUse=Long.parseLong(session_userId.toString());			
 
		Res<User> user_res=userFeign.QueryUserById(userId_forUse);
		if(user_res.getCode()!=0){
			model.setViewName("/error");
			return model;
		}
		model.addObject("user",user_res.getData());
		
		ResPager<List<Topic>> topic_pager_res= userFeign.QueryUserTopicsPagely(userId_forUse, 0, categoryId, tagId, 1, 20);
		if(topic_pager_res.getCode()!=0){
			model.setViewName("/error");
			return model;
		}
		model.addObject("modelTopicDrafts",topic_pager_res);
		Res<List<Tag>> res_tags = this.userFeign.QueryUserTags(userId_forUse, null);
		if(res_tags.getCode()!=0){
			model.setViewName("/error");
			return model;
		}
		model.addObject("tags",res_tags.getData());
		
		model.addObject("mine",1);
		return model;
	}

	@PreAuthorize("hasRole('ROLE_USER')")
	@RequestMapping("/tags")
	public ModelAndView Tags(@RequestParam(value="userId",required = true) Long userId,
							 @RequestParam(value="page",required = false) Integer page,
							 @RequestParam(value="pageSize",required = false) Integer pageSize){
		ModelAndView model =new ModelAndView("views/user/tags");
		Object session_userId =request.getSession().getAttribute("userId");
		long userId_forUse;
		if(userId==null){
			if(session_userId==null){
				//如果没有登录则登录
				model.setViewName("redirect:/user/login");
				return model;
			}
			userId_forUse=Long.parseLong(session_userId.toString());
		}else{
			userId_forUse=userId;
		}

		Res<User> user_res=userFeign.QueryUserById(userId_forUse);
		if(user_res.getCode()!=0){
			model.setViewName("/error");
			return model;
		}
		model.addObject("user",user_res.getData());

		Res<List<Tag>> res_tags = this.userFeign.QueryUserTags(userId_forUse, null);
		if(res_tags.getCode()!=0){
			model.setViewName("/error");
			return model;
		}
		model.addObject("tags",res_tags.getData());
		
		model.addObject("mine",1);
 

		return model;
	}
}
