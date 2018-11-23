package org.spring.china.user.service.impl;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.spring.china.base.common.Constant;
import org.spring.china.base.common.Res;
import org.spring.china.base.common.ResPager;
import org.spring.china.base.common.UserAllMsgCount;
import org.spring.china.base.common.UserMsgHistory;
import org.spring.china.base.pojo.Tag;
import org.spring.china.base.pojo.Topic;
import org.spring.china.base.pojo.User;
import org.spring.china.base.pojo.UserMsg;
import org.spring.china.base.pojo.UserRole;
import org.spring.china.base.result.UserRecentActivity;
import org.spring.china.base.result.UserRelatedComment;
import org.spring.china.user.mapper.UserMapper;
import org.spring.china.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thoughtworks.xstream.core.util.Base64Encoder;

/**
 * Created by jzlover on 2017/6/9.
 */
@Service("userService")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

 


    @Transactional
	@Override
	public Res<Long> Register(User user) {
    	Res<Long> res=new Res<Long>();    	
		String username=user.getUserName();
		String password=user.getPassword();
		String nickname=user.getNickName();
		
		if (username.length()>20 || username.length()<2) {
            res.setCode(2);
            res.setMsg("用户名长度必须在2-20个字符之间！");
            return res;
        }
		
        if (password.length()>20 || password.length()<6) {
            res.setCode(3);
            res.setMsg("密码长度必须在6-20个字符之间！");
            return res;
        }
		if(nickname.length()>20 || nickname.length()<2){
			res.setCode(1);
			res.setMsg("昵称长度必须在2-20个字符！");
			return res;
		}

        Res ck_username= this.CheckUserName(username);
        if(ck_username.getCode()!=0) {
        	res.setCode(ck_username.getCode());
        	res.setMsg(ck_username.getMsg());
        	return res;
        }
        
        Res ck_nickname= this.CheckNickName(nickname);
        if(ck_nickname.getCode()!=0) {
        	res.setCode(ck_nickname.getCode());
        	res.setMsg(ck_nickname.getMsg());
        	return res;
        }
        
        String password_salt=UUID.randomUUID().toString();
        String password_tosave=null;       
		try {
			MessageDigest md5;
			md5 = MessageDigest.getInstance("MD5");
			Base64Encoder base64en = new Base64Encoder();
	        //加密后的字符串
	        password_tosave = base64en.encode(md5.digest((password+password_salt).getBytes("utf-8")));
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(password_tosave==null) {
			res.setCode(4);
			res.setMsg("MD5加密错误！");
			return res;
		}
        User user_tosave=new User();
        user_tosave.setUserName(username);
        user_tosave.setNickName(nickname);
        user_tosave.setPassword(password_tosave);
        user_tosave.setPasswordSalt(password_salt);
        user_tosave.setCreatedAt(new Date());
        user_tosave.setWxId(user.getWxId());
        
        if(userMapper.regUser(user_tosave)==0) {
        	res.setCode(5);
        	res.setMsg("用户插入错误！");
        	return res;        	
        }
        
        List<UserRole> roles_tosave=user.getRoles();
        for(UserRole r:roles_tosave) {
        	r.setUserId(user_tosave.getId());   
        	r.setCreatedAt(new Date());
        	if(userMapper.saveUserRole(r)==0) {
        		res.setCode(6);
        		res.setMsg("用户角色插入错误！");
        		return res;
        	}
        }
        res.setData(user_tosave.getId());
        return res;
	}


	@Override
	public Res<Boolean> CheckUserName(String userName) {
		Res<Boolean> res=new Res<Boolean>();
		Pattern p = null;
        Matcher m = null;
        boolean flg = true;
        Scanner sc = new Scanner(userName);
        //匹配用户名
        String name = sc.nextLine();
        p = Pattern.compile("[a-zA-Z]{1}[a-zA-Z0-9_]{1,19}");
        m = p.matcher(name);
        flg = m.matches();
        if (!flg) {
            res.setCode(1);
            res.setMsg("用户名格式不正确！");
            return res;
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("userName", userName);
        if(userMapper.checkUserNameExist(map)>0){
        	res.setCode(2);
        	res.setMsg("用户名已经存在！");
        }
        return res;
	}


	@Override
	public Res<Boolean> CheckNickName(String nickName) {
		Res<Boolean> res=new Res<Boolean>();
		if(nickName.length()>20 || nickName.length()<2){
			res.setCode(1);
			res.setMsg("昵称长度必须在2-20个字符！");
			return res;
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("nickName", nickName);
		if(userMapper.checkNickNameExist(map)>0){
			res.setCode(2);
			res.setMsg("昵称已经存在！");
		}
		return res;
	}


	@Override
	public Res<User> QueryUser(Long id) {
		Res<User> res=new Res<User>();		
		Map<String,Object> map=new HashMap<String,Object>();
        map.put("id",id);
        User user=userMapper.queryUserById(map);
        //用户的所有话题的数量
        map.put("status", null);
        user.setTopicCounts(userMapper.queryUserTopicCounts(map));
        //用户正常发表的话题的数量
        map.put("status", 1);
        user.setTopicStatusReleaseCounts(userMapper.queryUserTopicCounts(map));
        //用户放入垃圾箱的数量
        map.put("status", 0);
        user.setTopicStatusDraftCounts(userMapper.queryUserTopicCounts(map));
        //用户放入垃圾箱的数量
        map.put("status", -1);
        user.setTopicStatusDeletedCounts(userMapper.queryUserTopicCounts(map));
        res.setData(user);
        return res;
	}


	@Override
	public Res<User> QueryUser(String userName) {
		Res<User> res=new Res<User>();
		Map<String,Object> map=new HashMap<String,Object>();
        map.put("userName",userName);
        res.setData(userMapper.queryUserByUserName(map));
        return res;
	}


	@Override
	public Res<Boolean> EditUserProfile(User user) {
		Res<Boolean> res=new Res<Boolean>();
		int count=userMapper.editUserProfile(user);
		if(count<1){
			res.setCode(-2);
			res.setMsg("编辑用户错误！");
		}
		return res;
	}


	@Override
	public Res<Boolean> EditUserAvatar(Long userId,Boolean avatar) {
		Res<Boolean> res=new Res<Boolean>();
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("id", userId);
		map.put("avatar", avatar);
		int count=userMapper.editUserAvatar(map);
		if(count<1){
			res.setCode(-2);
			res.setMsg("编辑头像错误！");
		}
		return res;
	}


	@Override
	public ResPager<List<Topic>> QueryUserTopicsPagely(Long userId, Integer status,
			Long categoryId,Long tagId, Integer page, Integer pageSize) {
		ResPager<List<Topic>> res =new ResPager<List<Topic>>();
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("id", userId);
		map.put("status", status);
		map.put("categoryId", categoryId);
		map.put("tagId",tagId);//标签的Id号
		map.put("page", page);
		map.put("pageSize", pageSize);
 
		List<Topic> topics=userMapper.queryUserTopicsPagely(map);
		res.setData(topics);
		res.setCounts(userMapper.queryUserTopicCounts(map));
		res.setPage(page);
		res.setPageSize(pageSize);
		return res;
	}


	@Override
	public Res<Boolean> CheckUserLoginedWX(String wxId) {
		Res<Boolean> res =new Res<Boolean>();
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("wxId", wxId);
		int count=userMapper.checkUserLoginedWX(map);
		if(count>0){
			res.setData(true);
		}else{
			res.setData(false);
		}
		return res;
	}


	@Override
	public Res<User> QueryUserByWxId(String wxId) {
		Res<User> res=new Res<User>();
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("wxId", wxId);
		User user=userMapper.queryUserByWxId(map);
		res.setData(user);
		return res;
	}


	@Override
	public ResPager<List<UserRecentActivity>> QueryUserRecentActivitiesPagely(
			Long userId, Integer page, Integer pageSize,Long prevDate) {
		ResPager<List<UserRecentActivity>> res=new ResPager<List<UserRecentActivity>>();
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("userId", userId);
		map.put("page", page);
		map.put("pageSize", pageSize);
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS");

    	sdf.applyPattern("yyyy/MM/dd HH:mm:ss.SSS");
    	if(prevDate!=null){
    		map.put("prevDate", sdf.format(prevDate));
    	}
		List<UserRecentActivity> activities=userMapper.queryUserRecentActivitiesPagely(map);
 
		res.setData(activities);
		res.setPage(page);
		res.setPageSize(pageSize);
		int counts=userMapper.queryUserRecentActivitiesCounts(map);
		res.setCounts(counts);
		return res;
		
	}



	@Override
	public Res<Integer> QueryUserCommentReadedCount(Long userId, Boolean readed) {
		Res<Integer> res=new Res<Integer>();
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("userId", userId);
		map.put("readed", readed);
		res.setData(userMapper.queryUserCommentReadedCount(map));
		return res;
	}

	@Override
	public ResPager<List<UserRelatedComment>> QueryUserCommentReaded(Long userId,
			Boolean readed, Integer page, Integer pageSize) {
		ResPager<List<UserRelatedComment>> res=new ResPager<List<UserRelatedComment>>();			
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("userId", userId);
		map.put("readed", readed);
		map.put("page", page);
		map.put("pageSize", pageSize);
 
		List<UserRelatedComment> related_comments=userMapper.queryUserCommentReaded(map);
		res.setData(related_comments);
		int counts=userMapper.queryUserCommentReadedCount(map);
		res.setCounts(counts);
		res.setPage(page);
		res.setPageSize(pageSize);
		return res;
	}


	@Transactional
	@Override
	public Res<Long> AddUserMsg(UserMsg msg) {
		Res<Long> res=new Res<Long>();
		int count=userMapper.addUserMsg(msg);
		if(count<1){
			//影响的行数
			res.setCode(-2);
			res.setMsg("添加用户消息失败！");
		}else{
			res.setData(msg.getId());
		}
		
		return res;
	}


	@Override
	public ResPager<List<UserMsg>> QueryUserMsgPagely(Integer type,Long userId, Boolean readed,Long senderUserId, Integer page,Integer pageSize) {
		ResPager<List<UserMsg>> res=new ResPager<List<UserMsg>>();
		Map<String,Object> map =new HashMap<String,Object>();
		map.put("type", type);
		map.put("userId", userId);
		map.put("readed", readed);
		map.put("senderUserId", senderUserId);
		map.put("page", page);
		map.put("pageSize", pageSize);
		List<UserMsg> msgs=userMapper.queryUserMsgPagely(map);
		res.setData(msgs);
		int counts=userMapper.queryUserMsgCount(map);
		res.setCounts(counts);
		res.setPage(page);
		res.setPageSize(pageSize);
		return res;
	}


	@Override
	public Res<UserMsgHistory> QueryUserMsgHistory(Integer type,Long senderUserId,
			Long toUserId, Boolean readed, Long prevDate, Integer perCount) {
		Res<UserMsgHistory> res = new Res<UserMsgHistory>();
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("type", type);
		map.put("toUserId", toUserId);
		map.put("senderUserId", senderUserId);
		map.put("readed", readed);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS");

    	sdf.applyPattern("yyyy/MM/dd HH:mm:ss.SSS");
    	map.put("prevDate", sdf.format(prevDate));
 
		map.put("perCount", perCount);
		UserMsgHistory user_msg_history=new UserMsgHistory();
		List<UserMsg> msgs=userMapper.queryUserMsgHistory(map);
		user_msg_history.setUserMsgs(msgs);
		int remain_count=userMapper.queryRemainUserMsgHistoryCount(map);
		user_msg_history.setRemainedMsgCount(remain_count-perCount);
		res.setData(user_msg_history);
		return res;
	}


	@Override
	public ResPager<List<User>> QueryMsgToMeUsersPagely(Integer type,Long toUserId, Boolean readed, Integer page, Integer pageSize) {
		ResPager<List<User>> res=new ResPager<List<User>>();
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("type", type);
		map.put("toUserId", toUserId);
		map.put("readed", readed);
		map.put("page", page);
		map.put("pageSize", pageSize);
		List<User> users=userMapper.queryMsgToMeUsersPagely(map);
		res.setData(users);
		int counts=userMapper.queryMsgToMeUsersCount(map);
		res.setCounts(counts);
		res.setPage(page);
		res.setPageSize(pageSize);
		return res;
	}


	@Override
	public Res<Integer> QueryUserMsgCount(Integer type,Long userId, Long senderUserId,
			Boolean readed) {
		Res<Integer> res=new Res<Integer>();
		Map<String,Object> map =new HashMap<String,Object>();
		map.put("type", type);
		map.put("userId",userId);
		map.put("senderUserId", senderUserId);
		map.put("readed", readed);
		res.setData(userMapper.queryUserMsgCount(map));
		return res;
		
	}


	@Override
	public Res<UserAllMsgCount> QueryUserAllMsgCount(Long userId) {
		Res<UserAllMsgCount> res=new Res<UserAllMsgCount>();
		UserAllMsgCount all_msgs=new UserAllMsgCount();
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("userId", userId);
		map.put("readed", false);
		int counts=userMapper.queryUserCommentReadedCount(map);
		all_msgs.setCommentCount(counts);
		map.put("type", Constant.MSG_TYPE_PRIVATE);//privatemsg
		counts=userMapper.queryUserMsgCount(map);
		all_msgs.setPrivateMsgCount(counts);
		res.setData(all_msgs);
		return res;
	}


	@Transactional
	@Override
	public Res<Integer> EditUserMsgReaded(Long id, Long userId,Boolean readed) {
		Res<Integer> res = new Res<Integer>();
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("id", id);
		map.put("userId", userId);
		map.put("readed", readed);
		int counts=userMapper.checkUserMsgToMe(map);
		if(counts>0){
			//表示是发给我的消息
			int edited_count=userMapper.checkUserMsgReaded(map);
			if(edited_count>0){
				//表示已经修改了
				res.setMsg("该消息已经修改过了！");
				res.setData(2);
			}else{
				counts=userMapper.editUserMsgReaded(map);
				if(counts<1){
					//修改成功
					res.setCode(-2);
					res.setMsg("设置用户消息的状态失败！");
				}else{
					res.setData(1);
				}
			}
			
			
		}else{
			//根本不是发给自己的消息，无权限修改
			res.setData(0);
		}
		
		return res;
	}


	@Transactional
	@Override
	public Res<Integer> EditUserMsgReadedMulti(List<Long> ids, Long userId,
			Boolean readed) {
		Res<Integer> res =new Res<Integer>();
		int edited_count=0;
		for(Long id:ids){
			Res<Integer> single_res=this.EditUserMsgReaded(id, userId, readed);
			if(single_res.getCode()!=0){
				throw new RuntimeException("设置用户消息失败，用户自定义异常，用户ID："+userId+"，消息ID："+id);	 
			}else{
				if(single_res.getData()==1){
					edited_count++;
				}
			}
		}
		res.setData(edited_count);
		
		return res;
	}

	@Override
	public ResPager<List<Tag>> QueryUserTagsPagely(Long userId, Integer page, Integer pageSize) {
		ResPager<List<Tag>> res=new ResPager<List<Tag>>();
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("userId", userId);
		if(page==null) page=1;
		if(pageSize==null) pageSize=20;
		map.put("page",page);
		map.put("pageSize",pageSize);
		res.setData(this.userMapper.queryUserTagsPagely(map));
		int count=this.userMapper.queryUserTagsCount(map);
		res.setCounts(count);
		return res;
	}

	@Transactional
	@Override
	public Res<Boolean> UpdateUserTagStatus(Long userId, Long tagId,Integer status) {
		Res<Boolean> res=new Res<Boolean>();
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("userId", userId);
		map.put("id", tagId);
		map.put("status",status);
		int del=this.userMapper.updateUserTagStatus(map);
		if(del<1){
			res.setCode(-2);
			res.setMsg("更新Tag的status失败！");
			return res;
		}
		res.setData(del>0);
		return res;
	}

	@Transactional
	@Override
	public Res<Long> AddTag(Tag tag) {
		Res<Long> res=new Res<Long>();
		int add=this.userMapper.addTag(tag);
		if(add<1){
			res.setCode(-2);
			res.setMsg("添加Tag失败！");
			return res;
		}
		res.setData(tag.getId());
		return res;
	}

	@Transactional
	@Override
	public Res<Boolean> EditTag(Tag tag) {
		Res<Boolean> res =new Res<Boolean>();
		int edit=this.userMapper.editTag(tag);
		if(edit<1){
			res.setCode(-2);
			res.setMsg("修改Tag失败！");
			return res;
		}
		res.setData(edit>0);
		return res;
	}


	@Override
	public Res<List<Tag>> QueryUserTags(long userId, Long topicId) {
		Res<List<Tag>> res = new Res<List<Tag>>();
		Map<String,Object> map = new HashMap<String,Object>();
		if(topicId!=null)
			map.put("topicId", topicId);
		map.put("userId", userId);
		List<Tag> tags = this.userMapper.queryUserTags(map);
		res.setData(tags);
		return res;
	}


	@Override
	public Res<Tag> QueryTagById(long id) {
		Res<Tag> res = new Res<Tag>();
		Tag tag = this.userMapper.queryTagById(id);
		res.setData(tag);
		return res;	
	}


	@Override
	public ResPager<List<Tag>> QueryTagsPagely(Integer page, Integer pageSize) {
		ResPager<List<Tag>> res = new ResPager<List<Tag>>();
		Map<String,Object> map = new HashMap<String,Object>();
		if(page==null) page=1;
		if(pageSize==null) pageSize=10;
		map.put("page", page);
		map.put("pageSize",pageSize);
	    List<Tag> tags= this.userMapper.queryTagsPagely(map);
	    res.setData(tags);
	    return res;
	}


	@Override
	public Res<Boolean> EditUserWxId(Long userId, String wxId) {
		Res<Boolean> res = new Res<Boolean>();
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("userId", userId);
		map.put("wxId", wxId);
		res.setData(this.userMapper.editUserWxId(map)>0);
		return res;
	}

}
