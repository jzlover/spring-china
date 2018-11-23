package org.spring.china.user.controller;


import java.util.List;

import org.spring.china.base.common.*;
import org.spring.china.base.pojo.Tag;
import org.spring.china.base.pojo.Topic;
import org.spring.china.base.pojo.User;
import org.spring.china.base.pojo.UserMsg;
import org.spring.china.base.result.UserRecentActivity;
import org.spring.china.base.result.UserRelatedComment;
import org.spring.china.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


/**
 * Created by jzlover on 2017/6/9.
 */

@RestController
@RefreshScope
public class UserController {
 
    @Autowired
    private UserService userService;


    @ResponseBody
    @RequestMapping(value="/query-user-by-id/{id}", method= RequestMethod.POST)
    public Res<User> QueryUserById(@PathVariable("id") Long id){
        return userService.QueryUser(id);
    }

    
 
    @ResponseBody
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public Res<Long> Register(@RequestBody User user){
    	return userService.Register(user);
    }
 

    @ResponseBody
    @RequestMapping(value="/check-userName",method=RequestMethod.POST)
    public Res<Boolean> CheckUserName(@RequestParam("u") String userName){
    	return userService.CheckUserName(userName);
    }
    
 
    
    @ResponseBody
    @RequestMapping(value="/check-nickName",method=RequestMethod.POST)
    public Res<Boolean> CheckNickName(@RequestParam("n") String nickName){
    	return userService.CheckNickName(nickName);
    }
    
 
    @ResponseBody
    @RequestMapping(value="/query-user-by-userName",method=RequestMethod.POST)
	public Res<User> QueryUserByUserName(@RequestParam("u") String userName){
    	return userService.QueryUser(userName);
    }
    
    @ResponseBody
    @RequestMapping(value="/edit-user-profile",method=RequestMethod.POST)
    public Res<Boolean> EditUserProfile(@RequestBody User user){
    	return userService.EditUserProfile(user);
    }
    
    @ResponseBody
    @RequestMapping(value="/edit-user-avatar",method=RequestMethod.POST)
    public Res<Boolean> EditUserAvatar(@RequestParam("userId") Long userId,@RequestParam("avatar") Boolean avatar){
    	return userService.EditUserAvatar(userId,avatar);
    }
    
    @ResponseBody
    @RequestMapping(value="/query-user-topics-pagely",method=RequestMethod.POST)
    public ResPager<List<Topic>> QueryUserTopicsPagely(
    		@RequestParam("userId") Long userId,
    		@RequestParam(value="status",required=false) Integer status,
    		@RequestParam(value="categoryId",required=false) Long categoryId,
    		@RequestParam(value="tagId",required=false) Long tagId,
    		@RequestParam(value="page",required=false) Integer page,
    		@RequestParam(value="pageSize",required=false) Integer pageSize){
    	return userService.QueryUserTopicsPagely(userId, status, categoryId,tagId, page, pageSize);
    }
    
    @ResponseBody
    @RequestMapping(value="/check-user-logined-wx",method=RequestMethod.POST)
    public Res<Boolean> CheckUserLoginedWX(@RequestParam("wxId") String wxId){
    	return userService.CheckUserLoginedWX(wxId);
    }
    
    @ResponseBody
    @RequestMapping(value="/query-user-by-wxId",method=RequestMethod.POST)
    public Res<User> QueryUserByWxId(@RequestParam("wxId") String wxId){
    	return userService.QueryUserByWxId(wxId);
    }
    
    @ResponseBody
    @RequestMapping(value="/query-user-recent-activities-pagely",method=RequestMethod.POST)
    public ResPager<List<UserRecentActivity>> QueryUserRecentActivitiesPagely(
    		@RequestParam("userId") Long userId,
    		@RequestParam(value="page",required=false) Integer page,
    		@RequestParam(value="pageSize",required=false) Integer pageSize,
    		@RequestParam(value="prevDate",required=false) Long prevDate){
    	return userService.QueryUserRecentActivitiesPagely(userId, page, pageSize,prevDate);
    }
    
    @ResponseBody
    @RequestMapping(value="/query-user-comment-readed-count",method=RequestMethod.POST)
    public Res<Integer> QueryUserCommentReadedCount(@RequestParam("userId") Long userId,@RequestParam(value="readed",required=false) Boolean readed){
    	return userService.QueryUserCommentReadedCount(userId, readed);
    }
    
    @ResponseBody
    @RequestMapping(value="/query-user-comment-readed",method=RequestMethod.POST)
    public ResPager<List<UserRelatedComment>> QueryUserCommentReaded(
    		@RequestParam(value="userId",required=true) Long userId,
    		@RequestParam(value="readed",required=false) Boolean readed,
    		@RequestParam(value="page",required=false) Integer page,
    		@RequestParam(value="pageSize",required=false) Integer pageSize){
    	return userService.QueryUserCommentReaded(userId, readed, page, pageSize);
    }
    
    @ResponseBody
    @RequestMapping(value="/add-user-msg",method=RequestMethod.POST)
    public Res<Long> AddUserMsg(@RequestBody UserMsg msg){
    	return userService.AddUserMsg(msg);
    }
    
    @ResponseBody
    @RequestMapping(value="/query-user-msg-pagely",method=RequestMethod.POST)
    public ResPager<List<UserMsg>> QueryUserMsgPagely(
    		@RequestParam(value="type",required=false) Integer type,
    		@RequestParam(value="userId",required=true) Long userId,
    		@RequestParam(value="readed",required=false) Boolean readed,
    		@RequestParam(value="senderUserId",required=false) Long senderUserId,
    		@RequestParam(value="page",required=false) Integer page,
    		@RequestParam(value="pageSize",required=false) Integer pageSize){
    	return userService.QueryUserMsgPagely(type,userId, readed, senderUserId, page, pageSize);
    }
    
    
    @ResponseBody
    @RequestMapping(value="/query-user-msg-history",method=RequestMethod.POST)
    public Res<UserMsgHistory> QueryUserMsgHistory(
    		@RequestParam(value="type",required=false) Integer type,
    		@RequestParam(value="toUserId",required=true) Long toUserId,
    		@RequestParam(value="senderUserId",required=false) Long senderUserId,
    		@RequestParam(value="readed",required=false) Boolean readed,   		
    		@RequestParam(value="preDate",required=false) Long preDate,
    		@RequestParam(value="perCoount",required=false) Integer perCount){
    	return userService.QueryUserMsgHistory(type,senderUserId, toUserId, readed, preDate, perCount);
    }
    
    @ResponseBody
    @RequestMapping(value="/query-msg-to-me-users-pagely",method=RequestMethod.POST)
    public ResPager<List<User>> QueryMsgToMeUsersPagely(
    		@RequestParam(value="type",required=false) Integer type,
    		@RequestParam(value="toUserId",required=true) Long toUserId,
    		@RequestParam(value="readed",required=false) Boolean readed,
    		@RequestParam(value="page",required=false) Integer page,
    		@RequestParam(value="pageSize",required=false) Integer pageSize){
    	return userService.QueryMsgToMeUsersPagely(type,toUserId, readed, page, pageSize);
    }
    
    @ResponseBody
    @RequestMapping(value="/query-user-msg-count",method=RequestMethod.POST)
    public Res<Integer> QueryUserMsgCount(
    		@RequestParam(value="type",required=false) Integer type,
    		@RequestParam(value="userId",required=true) Long userId,
    		@RequestParam(value="senderUserId",required=false) Long senderUserId,
    		@RequestParam(value="readed",required=false) Boolean readed){
    	return userService.QueryUserMsgCount(type,userId, senderUserId, readed);
    }
    
    @ResponseBody
    @RequestMapping(value="/query-user-all-msg-count",method=RequestMethod.POST)
    public Res<UserAllMsgCount> QueryUserAllMsgCount(
    		@RequestParam(value="userId",required=true) Long userId){
    	return userService.QueryUserAllMsgCount(userId);
    }
    
    @ResponseBody
    @RequestMapping(value="/edit-user-msg-readed",method=RequestMethod.POST)
    public Res<Integer> EditUserMsgReaded(
    		@RequestParam(value="id",required=true) Long id,
    		@RequestParam(value="userId",required=false) Long userId,
    		@RequestParam(value="readed",required=false) Boolean readed){
    	return userService.EditUserMsgReaded(id, userId, readed);
    }
    
    
    @ResponseBody
    @RequestMapping(value="/edit-user-msg-readed-multi",method=RequestMethod.POST)
    public Res<Integer> EditUserMsgReadedMulti(
    		@RequestParam(value="ids",required=true) List<Long> ids,
    		@RequestParam(value="userId",required=false) Long userId,
    		@RequestParam(value="readed",required=false) Boolean readed){
    	return userService.EditUserMsgReadedMulti(ids, userId, readed);
    }

    @ResponseBody
    @RequestMapping(value="/query-user-tags-pagely",method=RequestMethod.POST)
    public ResPager<List<Tag>> QueryUserTagsPagely(
            @RequestParam(value="userId",required = true) Long userId,
            @RequestParam(value="page",required=false) Integer page,
            @RequestParam(value="pageSize",required=false) Integer pageSize){
        return this.userService.QueryUserTagsPagely(userId,page,pageSize);
    }
    
    @ResponseBody
    @RequestMapping(value="/del-user-tag",method=RequestMethod.POST)
    public Res<Boolean> DelUserTag(
    		@RequestParam(value="userId",required=true) Long userId,
    		@RequestParam(value="id",required=true) Long tagId,
    		@RequestParam(value="status",required=true) Integer status){
    	return this.userService.UpdateUserTagStatus(userId, tagId, status);
    }
    
    @ResponseBody
    @RequestMapping(value="/add-tag", method= RequestMethod.POST)
  	public Res<Long> AddTag(@RequestBody Tag tag){
    	return this.userService.AddTag(tag);
    }
    
    @ResponseBody
    @RequestMapping(value="/edit-tag",method=RequestMethod.POST)
    public Res<Boolean> EditTag(@RequestBody Tag tag){
    	return this.userService.EditTag(tag);
    }
    
    @ResponseBody
    @RequestMapping(value="/query-user-tags",method=RequestMethod.POST)
    public Res<List<Tag>> QueryUserTags(
    		@RequestParam("userId") long userId,
    		@RequestParam(value="topicId",required=false) Long topicId){
    	return this.userService.QueryUserTags(userId,topicId);
    }
    
    @ResponseBody
    @RequestMapping(value="/query-tag-by-id/{id}",method= RequestMethod.POST)
    public Res<Tag> QueryTagById(
    		@PathVariable("id") long id){
    	return this.userService.QueryTagById(id);
    }
    
    @ResponseBody
    @RequestMapping(value="/query-tags-pagely",method=RequestMethod.POST)
    public ResPager<List<Tag>> QueryTagsPagely(
    		@RequestParam(value="page",required=false) Integer page,
    		@RequestParam(value="pageSize",required=false) Integer pageSize){
    	return this.userService.QueryTagsPagely(page, pageSize);
    }
    
    @ResponseBody
    @RequestMapping(value="/edit-user-wxId",method=RequestMethod.POST)
    public Res<Boolean> EditUserWxId(
    		@RequestParam(value="userId",required=true) long userId,
    		@RequestParam(value="wxId",required=false) String wxId){
    	return this.userService.EditUserWxId(userId, wxId);
    }
    
}