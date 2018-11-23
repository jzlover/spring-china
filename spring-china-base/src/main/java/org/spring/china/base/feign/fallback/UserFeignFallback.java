package org.spring.china.base.feign.fallback;

import java.security.Timestamp;
import java.util.Date;
import java.util.List;

import org.spring.china.base.common.*;
import org.spring.china.base.feign.UserFeign;
import org.spring.china.base.pojo.Tag;
import org.spring.china.base.pojo.Topic;
import org.spring.china.base.pojo.User;
import org.spring.china.base.pojo.UserMsg;
import org.spring.china.base.result.UserRecentActivity;
import org.spring.china.base.result.UserRelatedComment;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by jzlover on 2017/7/11.
 */
@Component
public class UserFeignFallback implements UserFeign {

	@Override
	public Res<User> QueryUserById(Long id) {
		Res<User> res=new Res<User>();
		res.setCode(-1);
		res.setMsg("查询用户"+id+"失败！");
		return res;
	}

	@Override
	public Res<Long> Register(User user) {
		Res<Long> res =new Res<Long>();
    	res.setCode(-1);
    	res.setMsg("注册用户熔断错误！");
    	return res;
	}

	@Override
	public Res<Boolean> CheckUserName(String userName) {
		Res<Boolean> res=new Res<Boolean>();
    	res.setCode(-1);
    	res.setMsg("检查用户名"+userName+"熔断错误！");
    	return res;
	}

	@Override
	public Res<Boolean> CheckNickName(String nickName) {
		Res<Boolean> res=new Res<Boolean>();
    	res.setCode(-1);
    	res.setMsg("检查用户昵称"+nickName+"错误！");
    	return res;
	}

	@Override
	public Res<User> QueryUserByUserName(String userName) {
		Res<User> res=new Res<User>();
		res.setCode(-1);
		res.setMsg("查询用户名"+userName+"失败！");
		return res;
	}

	@Override
	public Res<Boolean> EditUserProfile(User user) {
		Res<Boolean> res=new Res<Boolean>();
    	res.setCode(-1);
    	res.setMsg("编辑用户错误！");
    	return res;
	}

	@Override
	public Res<Boolean> EditUserAvatar(Long userId, Boolean avatar) {
		Res<Boolean> res=new Res<Boolean>();
    	res.setCode(-1);
    	res.setMsg("编辑头像错误！");
    	return res;
	}

	@Override
	public ResPager<List<Topic>> QueryUserTopicsPagely(Long userId, Integer status,
			Long categoryId, Long tagId, Integer page, Integer pageSize) {
		ResPager<List<Topic>> res=new ResPager<List<Topic>>();
		res.setCode(-1);
		res.setMsg("获取用户话题失败！");
		return res;
	}

	@Override
	public Res<Boolean> CheckUserLoginedWX(String wxId) {
		Res<Boolean> res=new Res<Boolean>();
		res.setCode(-1);
		res.setMsg("检查用户是否已经用微信登录熔断错误！");
		return res;
	}

	@Override
	public Res<User> QueryUserByWxId(String wxId) {
		Res<User> res=new Res<User>();
		res.setCode(-1);
		res.setMsg("通过wxId查找用户熔断错误！");
		return res;
	}

	@Override
	public ResPager<List<UserRecentActivity>> QueryUserRecentActivitiesPagely(
			Long userId, Integer page, Integer pageSize,Long prevDate) {
		ResPager<List<UserRecentActivity>> res=new ResPager<List<UserRecentActivity>>();
		res.setCode(-1);
		res.setMsg("获取用户的最近活动熔断错误！");
		return res;
	}
 
 
	@Override
	public Res<Integer> QueryUserCommentReadedCount(Long userId, Boolean readed) {
		Res<Integer> res=new Res<Integer>();
		res.setCode(-1);
		res.setMsg("获取用户未读取评论数熔断错误！");
		return res;
	}

	@Override
	public ResPager<List<UserRelatedComment>> QueryUserCommentReaded(Long userId,
			Boolean readed, Integer page, Integer pageSize) {
		ResPager<List<UserRelatedComment>> res=new ResPager<List<UserRelatedComment>>();
		res.setCode(-1);
		res.setMsg("获取用户未读取评论熔断错误！");
		return res;
	}

	@Override
	public Res<Long> AddUserMsg(UserMsg msg) {
		Res<Long> res=new Res<Long>();
		res.setCode(-1);
		res.setMsg("添加用户消息熔断错误！");
		return res;
	}

	@Override
	public ResPager<List<UserMsg>> QueryUserMsgPagely(Integer type,Long userId, Boolean readed,
			Long senderUserId, Integer page, Integer pageSize) {
		ResPager<List<UserMsg>> res=new ResPager<List<UserMsg>>();
		res.setCode(-1);
		res.setMsg("查询用户消息熔断错误！");
		return res;
	}

	@Override
	public Res<UserMsgHistory> QueryUserMsgHistory(Integer type,Long toUserId,
			Long senderUserId, Boolean readed, Long preDate, Integer perCount) {
		Res<UserMsgHistory> res =new Res<UserMsgHistory>();
		res.setCode(-1);
		res.setMsg("获取用户历史消息熔断错误！");
		return res;
	}

	@Override
	public ResPager<List<User>> QueryMsgToMeUsersPagely(Integer type,Long toUserId,
			 Boolean readed, Integer page,
			Integer pageSize) {
		ResPager<List<User>> res=new ResPager<List<User>>();
		res.setCode(-1);
		res.setMsg("获取给我消息的所有用户熔断错误！");
		return res;
	}

	@Override
	public Res<Integer> QueryUserMsgCount(Integer type,Long userId, Long senderUserId,
			Boolean readed) {
		Res<Integer> res=new Res<Integer>();
		res.setCode(-1);
		res.setMsg("获取用户消息数量熔断错误！");
		return res;
	}

	@Override
	public Res<UserAllMsgCount> QueryUserAllMsgCount(Long userId) {
		Res<UserAllMsgCount> res=new Res<UserAllMsgCount>();
		res.setCode(-1);
		res.setMsg("获取用户所有的消息数量熔断错误！");
		return res;
	}

	@Override
	public Res<Integer> EditUserMsgReaded(Long id, Long userId, Boolean readed) {
		Res<Integer> res =new Res<Integer>();
		res.setCode(-1);
		res.setMsg("编辑用户的消息状态熔断错误！");
		return res;
	}

	@Override
	public Res<Integer> EditUserMsgReadedMulti(List<Long> ids, Long userId,
			Boolean readed) {
		Res<Integer> res =new Res<Integer>();
		res.setCode(-1);
		res.setMsg("设置用户消息多个熔断错误！");
		return res;
	}

	@Override
	public ResPager<List<Tag>> QueryUserTagsPagely(Long userId, Integer page, Integer pageSize) {
		ResPager<List<Tag>> res=new ResPager<List<Tag>>();
		res.setCode(-1);
		res.setMsg("获取用户Tags熔断错误！");
		return res;
	}

	@Override
	public Res<Boolean> DelUserTag(Long userId, Long tagId,Integer status) {
		Res<Boolean> res=new Res<Boolean>();
		res.setCode(-1);
		res.setMsg("删除Tag熔断错误！");
		return res;
	}
	
	@Override
	public Res<Long> AddTag(Tag tag) {
		Res<Long> res=new Res<Long>();
		res.setCode(-1);
		res.setMsg("添加Tag熔断错误！");
		return res;
	}

	@Override
	public Res<Boolean> EditTag(Tag tag) {
		Res<Boolean> res=new Res<Boolean>();
		res.setCode(-1);
		res.setMsg("编辑Tag熔断错误！");
		return res;
	}

	@Override
	public Res<List<Tag>> QueryUserTags(long userId, Long topicId) {
		Res<List<Tag>> res = new Res<List<Tag>>();
		res.setCode(-1);
		res.setMsg("查询用户的标签熔断错误！");
		return res;
	}

	@Override
	public Res<Tag> QueryTagById(long id) {
		Res<Tag> res = new Res<Tag>();
		res.setCode(-1);
		res.setMsg("获取Tag熔断错误！");
		return res;
	}

	@Override
	public ResPager<List<Tag>> QueryTagsPagely(Integer page, Integer pageSize) {
		ResPager<List<Tag>> res = new ResPager<List<Tag>>();
		res.setCode(-1);
		res.setMsg("查询所有Tags熔断错误！");
		return res;
	}

	@Override
	public Res<Boolean> EditUserWxId(long userId, String wxId) {
		Res<Boolean> res = new Res<Boolean>();
		res.setCode(-1);
		res.setMsg("修改wxId熔断错误！");
		return res;
	}

}
