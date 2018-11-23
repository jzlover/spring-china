package org.spring.china.user.service;

import java.security.Timestamp;
import java.util.Date;
import java.util.List;

import org.spring.china.base.common.*;
import org.spring.china.base.pojo.Tag;
import org.spring.china.base.pojo.Topic;
import org.spring.china.base.pojo.User;
import org.spring.china.base.pojo.UserMsg;
import org.spring.china.base.pojo.UserRole;
import org.spring.china.base.result.UserRecentActivity;
import org.spring.china.base.result.UserRelatedComment;

/**
 * Created by jzlover on 2017/6/9.
 */
public interface UserService {
	
    public Res<User> QueryUser(Long id);
    
    public Res<Long> Register(User user);
    
    public Res<Boolean> CheckUserName(String userName);
    
    public Res<Boolean> CheckNickName(String nickName);
    
    public Res<User> QueryUser(String userName);

    public Res<Boolean> EditUserProfile(User user);
    
    public Res<Boolean> EditUserAvatar(Long userId,Boolean avatar);
    
    public ResPager<List<Topic>> QueryUserTopicsPagely(Long userId,Integer status,Long categoryId,Long tagId,Integer page,Integer perPageCount);

    public Res<Boolean> CheckUserLoginedWX(String wxId);
    
    public Res<User> QueryUserByWxId(String wxId);
    
    public ResPager<List<UserRecentActivity>> QueryUserRecentActivitiesPagely(Long userId,Integer page,Integer perPageCount,Long preDate);
    
    //获取用户所有未读取的评论和@...，readed=true，读取了，readed=false 未读取
    public Res<Integer> QueryUserCommentReadedCount(Long userId,Boolean readed);
    
    //获取用户相关未读取的评论和@
    public ResPager<List<UserRelatedComment>> QueryUserCommentReaded(Long userId,Boolean readed,Integer page,Integer perPageCount);
    
    //给指定用户发送消息
    public Res<Long> AddUserMsg(UserMsg msg);
    
    public ResPager<List<UserMsg>> QueryUserMsgPagely(Integer type,Long userId,Boolean readed,Long senderUserId,Integer page,Integer perPageCount);
    
    //获取用的聊天记录
    public Res<UserMsgHistory> QueryUserMsgHistory(Integer type,Long senderUserId,Long toUserId,Boolean readed,Long prevDate,Integer perCount);
    
    //发送给我的消息过的用户
    public ResPager<List<User>> QueryMsgToMeUsersPagely(Integer type,Long toUserId,Boolean readed,Integer page,Integer perPageCount);
    
    public Res<Integer> QueryUserMsgCount(Integer type,Long userId,Long senderUserId,Boolean readed);
    
    //获取用户的所有类型的消息的数量
    public Res<UserAllMsgCount> QueryUserAllMsgCount(Long userId);
    
    //设置用户消息的readed状态，并且首先检查该条消息是否是发给我的
    public Res<Integer> EditUserMsgReaded(Long id,Long userId,Boolean readed);
    
    public Res<Integer> EditUserMsgReadedMulti(List<Long> ids,Long userId,Boolean readed);

    //获取用户的所有标签
    public ResPager<List<Tag>> QueryUserTagsPagely(Long userId,Integer page,Integer pageSize);
    
    //更新用户的标签
    public Res<Boolean> UpdateUserTagStatus(Long userId,Long tagId,Integer status);
    
    //添加用户标签
    public Res<Long> AddTag(Tag tag);
    
    //编辑标签
    public Res<Boolean> EditTag(Tag tag);
    
    //查询用户的所有标签，用selected状态表示是否选中
    public Res<List<Tag>> QueryUserTags(long userId,Long topicId);
    
    //获取一个Tag
    public Res<Tag> QueryTagById(long id);
    
    //获取所有Tag，按照访问量排序
    public ResPager<List<Tag>> QueryTagsPagely(Integer page,Integer pageSize);
    
    //修改wxId
    public Res<Boolean> EditUserWxId(Long userId,String wxId);

}
