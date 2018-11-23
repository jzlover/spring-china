package org.spring.china.user.mapper;


import org.apache.ibatis.annotations.Mapper;
import org.spring.china.base.pojo.*;
import org.spring.china.base.result.UserRecentActivity;
import org.spring.china.base.result.UserRelatedComment;

import java.util.List;
import java.util.Map;
import java.util.Observable;

/**
 * Created by jzlover on 2017/6/9.
 */
@Mapper
public interface UserMapper {
	//根据用户ID查询用户对象
    public User queryUserById(Map<String,Object> map);
    
    //注册用户，返回值为影响行数
    public int regUser(User user);
	
    //插入用户角色，返回值为影响行数
	public int saveUserRole(UserRole role);
	
	//检查用户名是否存在
	public int checkUserNameExist(Map<String,Object> map);
	
	//检查昵称是否已经存在
	public int checkNickNameExist(Map<String,Object> map);
	
	//根据用户名查询用户对象
	public User queryUserByUserName(Map<String,Object> map);
	
	//编辑用户资料
	public int editUserProfile(User user);
	
	//编辑头像字段
	public int editUserAvatar(Map<String,Object> map);
	
	//获取用户的话题
	public List<Topic> queryUserTopicsPagely(Map<String,Object> map);
	
	//获取用户话题数量
	public int queryUserTopicCounts(Map<String,Object> map);
	
	//检查用户受否已经用微信登录过了
	public int checkUserLoginedWX(Map<String,Object> map);
	
	//根据wxId进行查询
	public User queryUserByWxId(Map<String,Object> map);
	
	//获取用户最近的活动
	public List<UserRecentActivity> queryUserRecentActivitiesPagely(Map<String,Object> map);
	
	//获取用户最近活动数量
	public int queryUserRecentActivitiesCounts(Map<String,Object> map);
	
	public int queryUserCommentReadedCount(Map<String,Object> map);
    
    public List<UserRelatedComment> queryUserCommentReaded(Map<String,Object> map);
    
    //给用户发送消息
    public int addUserMsg(UserMsg msg);
    
    //查询用户的消息
    public List<UserMsg> queryUserMsgPagely(Map<String,Object> map);
    
    public int queryUserMsgCount(Map<String,Object> map);
    
    //获取用户聊天的记录
    public List<UserMsg> queryUserMsgHistory(Map<String,Object> map);
    
    //获取谁都给我私信了
    public List<User> queryMsgToMeUsersPagely(Map<String,Object> map);
    
    //获取给我私信的人数
    public int queryMsgToMeUsersCount(Map<String,Object> map);
    
    //获取用户剩
    public int queryRemainUserMsgHistoryCount(Map<String,Object> map);
    
    //更新用户消息的readed状态
    public int editUserMsgReaded(Map<String,Object> map);
    
    //检查消息是否是发给我的
    public int checkUserMsgToMe(Map<String,Object> map);
    
    
    public int checkUserMsgReaded(Map<String,Object> map);

    //获取用户的所有标签
    public List<Tag> queryUserTagsPagely(Map<String,Object> map);

    //获取用户所有标签数量
    public int queryUserTagsCount(Map<String,Object> map);
    
    //更新标签状态
    public int updateUserTagStatus(Map<String,Object> map);
    
    //添加标签
    public int addTag(Tag tag);
    
    //编辑标签
    public int editTag(Tag tag);
    
    //获取用户的所有标签
    public List<Tag> queryUserTags(Map<String,Object> map);
    
    //获取单个Tag
    public Tag queryTagById(long id);
    
    //获取所有tag，按照访问量排序
    public List<Tag> queryTagsPagely(Map<String,Object> map);
    
    //修改wxId
    public int editUserWxId(Map<String,Object> map);

}
