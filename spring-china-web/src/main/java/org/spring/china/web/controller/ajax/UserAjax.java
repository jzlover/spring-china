package org.spring.china.web.controller.ajax;


import org.spring.china.base.common.*;
import org.spring.china.base.feign.TopicFeign;
import org.spring.china.base.feign.UserFeign;
import org.spring.china.base.pojo.Tag;
import org.spring.china.base.pojo.Topic;
import org.spring.china.base.pojo.User;
import org.spring.china.base.pojo.UserMsg;
import org.spring.china.base.pojo.UserRole;
import org.spring.china.base.result.UserRecentActivity;
import org.spring.china.base.result.UserRelatedComment;
import org.spring.china.base.util.Common;
import org.spring.china.base.util.ImageCut;
import org.spring.china.web.common.CustomAuthenticationProvider;
import org.spring.china.web.model.Model_QueryPagely;
import org.spring.china.web.model.Model_QueryTopicsPagely;
import org.spring.china.web.model.Model_ResponseMessage;
import org.spring.china.web.model.Model_UploadAvatar;
import org.spring.china.web.model.Model_User;
import org.spring.china.web.model.Model_UserInitData;
import org.spring.china.web.model.Model_UserMsg;
import org.spring.china.web.model.Model_UserTipInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by jzlover on 2017/6/10.
 */
@RequestMapping("/user-ajax")
@RestController
public class UserAjax {
 
	@Value("${UPLOAD_PATH}")
	private String UPLOAD_PATH;

	@Autowired 
	private SimpMessagingTemplate msgTemplate; 

	@Autowired
    private CustomAuthenticationProvider provider;//自定义验证
	
    @Autowired
    private HttpServletRequest request;

    @Autowired
    private UserFeign userFeign;
    
    @Autowired
    private TopicFeign topicFeign;

    

    
    
    
    @ResponseBody
    @RequestMapping(value="/query-user-topics-pagely/{userId}",method=RequestMethod.POST)
    public ResPager<List<Topic>> QueryUserTopicsPagely(
    		@PathVariable("userId") Long userId,
    		@RequestBody Model_QueryTopicsPagely model){
    	return userFeign.QueryUserTopicsPagely(userId, model.getStatus(), model.getCategoryId(), model.getTagId(), model.getPage(), model.getPageSize());
    }
    
    @ResponseBody
    @RequestMapping(value="/query-user-recent-activities-pagely/{userId}",method=RequestMethod.POST)
    public ResPager<List<UserRecentActivity>> QueryUserRecentActivitiesPagely(
    		@PathVariable("userId") Long userId,
    		@RequestBody Model_QueryPagely model){
    	return userFeign.QueryUserRecentActivitiesPagely(userId, model.getPage(), model.getPageSize(),model.getPrevDate());
    }
    
 
    @ResponseBody
    @RequestMapping(value="/query-user-by-id/{id}",method=RequestMethod.POST)
    public Res<Model_UserTipInfo> QueryUserById(@PathVariable("id") Long id){
    	Res<Model_UserTipInfo> res=new Res<Model_UserTipInfo>();
    	Res<User> res_user= userFeign.QueryUserById(id);
    	if(res_user.getCode()!=0){
    		res.setCode(res_user.getCode());
    		res.setMsg("获取用户错误！");
    	}else{
    		Model_UserTipInfo user_tipinfo=new Model_UserTipInfo();
    		user_tipinfo.setAvatarNormal(res_user.getData().getAvatarNormal());
    		user_tipinfo.setNickName(res_user.getData().getNickName());
    		user_tipinfo.setId(res_user.getData().getId());
    		user_tipinfo.setSignature(res_user.getData().getSignature());
    		user_tipinfo.setUserName(res_user.getData().getUserName());
    		user_tipinfo.setAvatarSmall(res_user.getData().getAvatarSmall());
    		Object userId =request.getSession().getAttribute("userId");
    		if(userId==null){
    			user_tipinfo.setMine(0);
    		}else{
    			Long longUserId=Long.parseLong(userId.toString());
    			user_tipinfo.setMine(longUserId==id?1:2);
    		}
    		res.setData(user_tipinfo);
    	}
    	return res;
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @ResponseBody
	@RequestMapping(value="/edit-profile",method=RequestMethod.POST)
	public Res<Boolean> EditProfile(@RequestBody User user){
    	Object userId =request.getSession().getAttribute("userId");
		user.setNickName(null);//不允许修改昵称
		user.setId(Long.parseLong(userId.toString()));
		return userFeign.EditUserProfile(user);
	}
    
    
    /**
     * 将用户上传的头像base64位数据进行保存，并同时设置用户头像字段avatar=1
     * @param model
     * @return
     */
    @PreAuthorize("hasRole('ROLE_USER')")
    @ResponseBody
    @RequestMapping(value="/edit-avatar",method=RequestMethod.POST)
    public Res<Boolean> EditAvatar(@RequestBody Model_UploadAvatar model){
    	Res<Boolean> res=new Res<Boolean>();
    	Object userId =request.getSession().getAttribute("userId");
    	Boolean convert_result=Common.Base64ToImage(model.getAvatar().split(",")[1], UPLOAD_PATH+"/avatar/"+userId+"_large.jpg");
    	if(convert_result){
    		ImageCut.scale(UPLOAD_PATH+"/avatar/"+userId+"_large.jpg", UPLOAD_PATH+"/avatar/"+userId+"_normal.jpg", 2, false);
    		ImageCut.scale(UPLOAD_PATH+"/avatar/"+userId+"_large.jpg", UPLOAD_PATH+"/avatar/"+userId+"_small.jpg", 4, false);
    		return userFeign.EditUserAvatar(Long.parseLong(userId.toString()),true);
    	}else{
    		res.setCode(-2);
    		res.setMsg("图片保存错误！");
    	}
    	return res;
    }
    
    @PreAuthorize("hasRole('ROLE_USER')")
    @ResponseBody
    @RequestMapping(value="/query-user-init-data/{userId}",method=RequestMethod.POST)
    public Res<Model_UserInitData> QueryUserInitData(@PathVariable("userId") Long userId){
    	Res<Model_UserInitData> res=new Res<Model_UserInitData>();
    	Model_UserInitData _model=new Model_UserInitData();
    	Res<Integer> res_comment_unreaded=userFeign.QueryUserCommentReadedCount(userId, false);
    	if(res_comment_unreaded.getCode()==0){
    		_model.setCommentUnReadedCount(res_comment_unreaded.getData());
    	}   	
    	return res;
    }
    
    
    
    @PreAuthorize("hasRole('ROLE_USER')")
    @ResponseBody
    @RequestMapping(value="/query-user-related-comment-pagely/{show_type}",method=RequestMethod.POST)
    public ResPager<List<UserRelatedComment>> QueryUserRelatedCommentPagely(
    		@PathVariable("show_type") Integer show_type,
    		@RequestBody Model_QueryPagely model){
    	Object session_userId =request.getSession().getAttribute("userId");
    	long userId_forUse;
		if(session_userId==null){
			return null;
		}else{
			userId_forUse=Long.parseLong(session_userId.toString());
		}
		Boolean readed=null;
		if(show_type==0){
			readed=null;
		}else if(show_type==1){
			readed=false;
		}else{
			readed=true;
		}
    	ResPager<List<UserRelatedComment>> comment_readed_res=userFeign.QueryUserCommentReaded(userId_forUse, readed, model.getPage(), model.getPageSize());
    	return comment_readed_res;
    }
 
    @PreAuthorize("hasRole('ROLE_USER')")
    @ResponseBody
    @RequestMapping(value="/edit-readed/{type}/{id}/{readed}",method=RequestMethod.POST)
    public Res<Integer> EditReaded(
    		@PathVariable("type") Integer type,
    		@PathVariable("id") Long id, 		
    		@PathVariable("readed") Boolean readed){
    	Object session_userId =request.getSession().getAttribute("userId");
    	long userId_forUse;
		if(session_userId==null){
			return null;
		}else{
			userId_forUse=Long.parseLong(session_userId.toString());
		}
    	return topicFeign.EditReaded(userId_forUse, type, id, readed);
    }
    
    @PreAuthorize("hasRole('ROLE_USER')")
    @ResponseBody
    @RequestMapping(value="/add-user-msg",method=RequestMethod.POST)
    public Res<Long> AddUserMsg(@RequestBody Model_UserMsg msg){
    	Res<Long> res=new Res<Long>();
    	Object session_userId =request.getSession().getAttribute("userId");
    	long longUserId;
		if(session_userId==null){
			return null;
		}else{
			longUserId=Long.parseLong(session_userId.toString());
		}
		
		UserMsg userMsg=new UserMsg();
		userMsg.setSenderUserId(longUserId);
		userMsg.setToUserId(msg.getToUserId());
		userMsg.setContent(msg.getContent());
		userMsg.setContentHtml(msg.getContentHtml());
		userMsg.setType(msg.getType());
		res= userFeign.AddUserMsg(userMsg);
		
		if(res.getCode()==0){
			Res<User> res_user= userFeign.QueryUserById(longUserId);
			Model_ResponseMessage websocket_response_msg=new Model_ResponseMessage();
			websocket_response_msg.setUser(res_user.getData());
			websocket_response_msg.setMsgType(Constant.MSG_TYPE_PRIVATE);	
			websocket_response_msg.setMsgContent(msg.getContentHtml());
			websocket_response_msg.setId(res.getData());
			msgTemplate.convertAndSendToUser(msg.getToUserId().toString(), "/msg/getResponse",websocket_response_msg);			
		}
		
		return res;
    }
    
    @PreAuthorize("hasRole('ROLE_USER')")
    @ResponseBody
    @RequestMapping(value="/query-msg-to-me-pagely",method=RequestMethod.POST)
    public ResPager<List<User>> QueryMsgToMeUsersPagely(){
    	Object session_userId =request.getSession().getAttribute("userId");
    	long longUserId;
		if(session_userId==null){
			return null;
		}else{
			longUserId=Long.parseLong(session_userId.toString());
		}
		
		return userFeign.QueryMsgToMeUsersPagely(Constant.MSG_TYPE_PRIVATE,longUserId, null, 1, 20);
    }
    
    @PreAuthorize("hasRole('ROLE_USER')")
    @ResponseBody
    @RequestMapping(value="/query-user-msg-history/{senderUserId}/{prevDate}",method=RequestMethod.POST)
    public Res<UserMsgHistory> QueryUserMsgHistory(
    		@PathVariable("senderUserId") Long senderUserId,
    		@PathVariable("prevDate") Long prevDate){
    	Object session_userId =request.getSession().getAttribute("userId");
    	long longUserId;
		if(session_userId==null){
			return null;
		}else{
			longUserId=Long.parseLong(session_userId.toString());
		}
		if(prevDate==0)
			prevDate=new Date().getTime();
		
		return userFeign.QueryUserMsgHistory(Constant.MSG_TYPE_PRIVATE,longUserId, senderUserId, null, prevDate, 5);
    }
    
    @PreAuthorize("hasRole('ROLE_USER')")
    @ResponseBody
    @RequestMapping(value="/edit-user-msg-readed-multi/{ids}",method=RequestMethod.POST)
    public Res<Integer> EditUserMsgReadedMulti(@PathVariable("ids") String ids){
    	Object session_userId =request.getSession().getAttribute("userId");
    	long longUserId;
		if(session_userId==null){
			return null;
		}else{
			longUserId=Long.parseLong(session_userId.toString());
		}
		
		List<String> lstIds= Arrays.asList(ids.split(","));
		List<Long> lstLongIds=new ArrayList<Long>();
		for(String s:lstIds){
			lstLongIds.add(Long.parseLong(s));
		}
		return userFeign.EditUserMsgReadedMulti(lstLongIds, longUserId, true);
    }

	@PreAuthorize("hasRole('ROLE_USER')")
	@ResponseBody
	@RequestMapping(value="/query-user-tags-pagely",method=RequestMethod.POST)
    public ResPager<List<Tag>> QueryUserTagsPagely(@RequestBody Model_QueryPagely model){
		Object session_userId =request.getSession().getAttribute("userId");
		long longUserId;
		if(session_userId==null){
			return null;
		}else{
			longUserId=Long.parseLong(session_userId.toString());
		}
    	return this.userFeign.QueryUserTagsPagely(longUserId,model.getPage(),model.getPageSize());
	}
	
	
	@PreAuthorize("hasRole('ROLE_USER')")
	@ResponseBody
	@RequestMapping(value="/del-user-tag-by-id/{id}",method=RequestMethod.POST)
	public Res<Boolean> DelUserTagById(@PathVariable("id") Long tagId){
		Object session_userId =request.getSession().getAttribute("userId");
		long longUserId;
		if(session_userId==null){
			return null;
		}else{
			longUserId=Long.parseLong(session_userId.toString());
		}
		return this.userFeign.DelUserTag(longUserId, tagId,-1);
	}
	
	@PreAuthorize("hasRole('ROLE_USER')")
	@ResponseBody
	@RequestMapping(value="/add-tag",method=RequestMethod.POST)
	public Res<Long> AddTag(@RequestBody Tag tag){
		Object session_userId =request.getSession().getAttribute("userId");
		long longUserId;
		if(session_userId==null){
			return null;
		}else{
			longUserId=Long.parseLong(session_userId.toString());
		}
		tag.setUserId(longUserId);
		return this.userFeign.AddTag(tag);
	}
	
	
	@PreAuthorize("hasRole('ROLE_USER')")
	@ResponseBody
	@RequestMapping(value="/edit-tag",method=RequestMethod.POST)
	public Res<Boolean> EditTag(@RequestBody Tag tag){
		Object session_userId =request.getSession().getAttribute("userId");
		long longUserId;
		if(session_userId==null){
			return null;
		}else{
			longUserId=Long.parseLong(session_userId.toString());
		}
		tag.setUserId(longUserId);
		return this.userFeign.EditTag(tag);
	}
	
}
