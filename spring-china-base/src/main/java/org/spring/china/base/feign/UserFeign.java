package org.spring.china.base.feign;

import java.security.Timestamp;
import java.util.Date;
import java.util.List;

import org.spring.china.base.common.*;
import org.spring.china.base.feign.fallback.UserFeignFallback;
import org.spring.china.base.pojo.Tag;
import org.spring.china.base.pojo.Topic;
import org.spring.china.base.pojo.User;
import org.spring.china.base.pojo.UserMsg;
import org.spring.china.base.result.UserRecentActivity;
import org.spring.china.base.result.UserRelatedComment;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * Created by jzlover on 2017/7/11.
 */
@FeignClient(value="spring-china-user",fallback = UserFeignFallback.class)
public interface UserFeign {

	//通过用户ID来获取User信息
	@RequestMapping(value="/query-user-by-id/{id}", method = RequestMethod.POST)
    public Res<User> QueryUserById(@PathVariable("id") Long id);
	
	//注册User
	@RequestMapping(value = "/register", method = RequestMethod.POST)
    public Res<Long> Register(@RequestBody User user);
 	
	//检查用户名是否符合规范、重复...
	@RequestMapping(value="/check-userName",method = RequestMethod.POST)
    public Res<Boolean> CheckUserName(@RequestParam("u") String userName);
	
	//检查昵称是否符合规范、重复...
	@RequestMapping(value="/check-nickName",method = RequestMethod.POST)
    public Res<Boolean> CheckNickName(@RequestParam("n") String nickName);
	
	//通过用户名来查找用户信息
	@RequestMapping(value="/query-user-by-userName",method=RequestMethod.POST)
	public Res<User> QueryUserByUserName(@RequestParam("u") String userName);

	//编辑用户资料
	@RequestMapping(value="/edit-user-profile",method=RequestMethod.POST)
    public Res<Boolean> EditUserProfile(@RequestBody User user);
	
	//编辑用户头像
	@RequestMapping(value="/edit-user-avatar",method=RequestMethod.POST)
    public Res<Boolean> EditUserAvatar(@RequestParam("userId") Long userId,@RequestParam("avatar") Boolean avatar);
	
	//获取用户话题
	@RequestMapping(value="/query-user-topics-pagely",method=RequestMethod.POST)
    public ResPager<List<Topic>> QueryUserTopicsPagely(
    		@RequestParam("userId") Long userId,
    		@RequestParam(value="status",required=false) Integer status,
    		@RequestParam(value="categoryId",required=false) Long categoryId,
    		@RequestParam(value="tagId",required=false) Long tagId,
    		@RequestParam(value="page",required=false) Integer page,
    		@RequestParam(value="pageSize",required=false) Integer pageSize);
	
	//检查用户是否已经用微信登陆过
	@RequestMapping(value="/check-user-logined-wx",method=RequestMethod.POST)
    public Res<Boolean> CheckUserLoginedWX(@RequestParam("wxId") String wxId);
	
	//通过wxId查找用户
	@RequestMapping(value="/query-user-by-wxId",method=RequestMethod.POST)
    public Res<User> QueryUserByWxId(@RequestParam("wxId") String wxId);
	
	//获取用户的最近活动
	@RequestMapping(value="/query-user-recent-activities-pagely",method=RequestMethod.POST)
    public ResPager<List<UserRecentActivity>> QueryUserRecentActivitiesPagely(
    		@RequestParam("userId") Long userId,
    		@RequestParam(value="page",required=false) Integer page,
    		@RequestParam(value="pageSize",required=false) Integer pageSize,
    		@RequestParam(value="prevDate",required=false) Long prevDate);
	
	@RequestMapping(value="/query-user-comment-readed-count",method=RequestMethod.POST)
    public Res<Integer> QueryUserCommentReadedCount(@RequestParam("userId") Long userId,@RequestParam(value="readed",required=false) Boolean readed);

	@RequestMapping(value="/query-user-comment-readed",method=RequestMethod.POST)
    public ResPager<List<UserRelatedComment>> QueryUserCommentReaded(
    		@RequestParam(value="userId",required=true) Long userId,
    		@RequestParam(value="readed",required=false) Boolean readed,
    		@RequestParam(value="page",required=false) Integer page,
    		@RequestParam(value="pageSize",required=false) Integer pageSize);
	
	@RequestMapping(value="/add-user-msg",method=RequestMethod.POST)
    public Res<Long> AddUserMsg(@RequestBody UserMsg msg);
	
	@RequestMapping(value="/query-user-msg-pagely",method=RequestMethod.POST)
    public ResPager<List<UserMsg>> QueryUserMsgPagely(
    		@RequestParam(value="type",required=false) Integer type,
    		@RequestParam(value="userId",required=true) Long userId,
    		@RequestParam(value="readed",required=false) Boolean readed,
    		@RequestParam(value="senderUserId",required=false) Long senderUserId,
    		@RequestParam(value="page",required=false) Integer page,
    		@RequestParam(value="pageSize",required=false) Integer pageSize);
	
	@RequestMapping(value="/query-user-msg-history",method=RequestMethod.POST)
    public Res<UserMsgHistory> QueryUserMsgHistory(
    		@RequestParam(value="type",required=false) Integer type,
    		@RequestParam(value="toUserId",required=true) Long toUserId,
    		@RequestParam(value="senderUserId",required=false) Long senderUserId,
    		@RequestParam(value="readed",required=false) Boolean readed,
    		@RequestParam(value="preDate",required=false) Long preDate,
    		@RequestParam(value="perCoount",required=false) Integer perCount);
	
	@RequestMapping(value="/query-msg-to-me-users-pagely",method=RequestMethod.POST)
    public ResPager<List<User>> QueryMsgToMeUsersPagely(
    		@RequestParam(value="type",required=false) Integer type,
    		@RequestParam(value="toUserId",required=true) Long toUserId,
    		@RequestParam(value="readed",required=false) Boolean readed,
    		@RequestParam(value="page",required=false) Integer page,
    		@RequestParam(value="pageSize",required=false) Integer pageSize);
	
	@RequestMapping(value="/query-user-msg-count",method=RequestMethod.POST)
    public Res<Integer> QueryUserMsgCount(
    		@RequestParam(value="type",required=false) Integer type,
    		@RequestParam(value="userId",required=true) Long userId,
    		@RequestParam(value="senderUserId",required=false) Long senderUserId,
    		@RequestParam(value="readed",required=false) Boolean readed);
	
	@RequestMapping(value="/query-user-all-msg-count",method=RequestMethod.POST)
    public Res<UserAllMsgCount> QueryUserAllMsgCount(
    		@RequestParam(value="userId",required=true) Long userId);
	
	@RequestMapping(value="/edit-user-msg-readed",method=RequestMethod.POST)
    public Res<Integer> EditUserMsgReaded(
    		@RequestParam(value="id",required=true) Long id,
    		@RequestParam(value="userId",required=false) Long userId,
    		@RequestParam(value="readed",required=false) Boolean readed);
	
	@RequestMapping(value="/edit-user-msg-readed-multi",method=RequestMethod.POST)
    public Res<Integer> EditUserMsgReadedMulti(
    		@RequestParam(value="ids",required=true) List<Long> ids,
    		@RequestParam(value="userId",required=false) Long userId,
    		@RequestParam(value="readed",required=false) Boolean readed);

	@RequestMapping(value="/query-user-tags-pagely",method=RequestMethod.POST)
	public ResPager<List<Tag>> QueryUserTagsPagely(
			@RequestParam(value="userId",required = true) Long userId,
			@RequestParam(value="page",required=false) Integer page,
			@RequestParam(value="pageSize",required=false) Integer pageSize);
	
    @RequestMapping(value="/del-user-tag",method=RequestMethod.POST)
    public Res<Boolean> DelUserTag(
    		@RequestParam(value="userId",required=true) Long userId,
    		@RequestParam(value="id",required=true) Long tagId,
    		@RequestParam(value="status",required=true) Integer status);
    
    //添加Tag
  	@RequestMapping(value="/add-tag", method= RequestMethod.POST)
  	public Res<Long> AddTag(@RequestBody Tag tag);
  	
  	@RequestMapping(value="/edit-tag",method=RequestMethod.POST)
    public Res<Boolean> EditTag(@RequestBody Tag tag);
  	
  	@RequestMapping(value="/query-user-tags",method=RequestMethod.POST)
    public Res<List<Tag>> QueryUserTags(
    		@RequestParam("userId") long userId,
    		@RequestParam(value="topicId",required=false) Long topicId);
  	
  	@RequestMapping(value="/query-tag-by-id/{id}",method= RequestMethod.POST)
    public Res<Tag> QueryTagById(@PathVariable("id") long id);
  	
    @RequestMapping(value="/query-tags-pagely",method=RequestMethod.POST)
    public ResPager<List<Tag>> QueryTagsPagely(
    		@RequestParam(value="page",required=false) Integer page,
    		@RequestParam(value="pageSize",required=false) Integer pageSize);
    
    @RequestMapping(value="/edit-user-wxId",method=RequestMethod.POST)
    public Res<Boolean> EditUserWxId(
    		@RequestParam(value="userId",required=true) long userId,
    		@RequestParam(value="wxId",required=false) String wxId);
}
