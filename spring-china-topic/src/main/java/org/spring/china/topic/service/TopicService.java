package org.spring.china.topic.service;

import org.spring.china.base.common.Res;
import org.spring.china.base.common.ResPager;
import org.spring.china.base.pojo.Category;
import org.spring.china.base.pojo.Tag;
import org.spring.china.base.pojo.Topic;
import org.spring.china.base.pojo.TopicComment;
import org.spring.china.base.pojo.TopicCommentLike;
import org.spring.china.base.pojo.TopicLike;
import org.spring.china.base.pojo.User;
import org.spring.china.base.result.UserRelatedComment;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by jzlover on 2017/6/10.
 */
public interface TopicService {
	
    public Res<Long> AddTopic(Topic topic);

    public Res<List<Category>> QueryAllCategories();
    
    public Res<Topic> QueryTopicById(Long id,Long userId);
    
    public Res<Boolean> EditTopic(Topic topic);
    
    public Res<Boolean> CheckTopicIsMine(Long id,Long userId);
    
    public ResPager<List<Topic>> QueryTopicsPagely(Integer status,Integer filter,String title, Integer page,Integer perPageCount);
    
    public ResPager<List<TopicComment>> QueryTopicCommentsPagely(Long id,Long userId,Integer page,Integer perPageCount);
    
    //添加Topic点赞
    public Res<Long> AddTopicLike(TopicLike like);
    
    //删除Topic的赞
    public Res<Boolean> DelTopicLike(TopicLike like);
    
    //添加Topic评论
    public Res<Long> AddTopicComment(TopicComment comment);
    
    //删除Topic评论
    public Res<Boolean> DelTopicComment(Long id);
    
    //添加Topic评论的赞
    public Res<Long> AddTopicCommentLike(TopicCommentLike like);
    
    //删除Topic评论的俄
    public Res<Boolean> DelTopicCommentLike(TopicCommentLike like);
    
    //检查Topic赞是否已经存在
    public Res<Boolean> CheckTopicLikeExist(TopicLike like);
    
    //检查Topic评论的赞是否已经存在
    public Res<Boolean> CheckTopicCommentLikeExist(TopicCommentLike like);
    
    //获取单个TopicComment
    public Res<TopicComment> QueryTopicComment(Long id,Long userId);
    
    //获取Topic评论过的用户
    public Res<List<User>> QueryTopicCommentedUsers(Long id,Long userId);
    
    //編輯Topic的状态，-1:删除，0，1...
    public Res<Boolean> EditTopicStatus(Long id,Integer status);
    
    //更新Topic里面的viewCount字段值+1
    public Res<Boolean> EditTopicViewCount(Long id);
    
    //根据comment的ID号来查找该ID号在Topic的所有评论中的排序
    public Res<Integer> QueryTopicCommentIndex(Long topicId,Long commentId);
    
    //设置readed=状态
    public Res<Integer> EditReaded(Long userId,Integer type,Long id,Boolean readed);
    
    //通过CommentId来获取改Topic
    public Res<Topic> QueryTopicByCommentId(Long id,Long userId);
    
    public Res<Boolean> CheckTopicIsDeleted(Long id);
    
    public Res<Integer> EditReadedMulti(Long userId,String strToEdit,Boolean readed);
    
    //查询Topic的所有标签，用selected状态表示是否选中
    public Res<List<Tag>> QueryTopicTags(long userId,long topicId);
}
 