package org.spring.china.topic.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.spring.china.base.pojo.*;
import org.spring.china.base.result.UserRelatedComment;

/**
 * Created by jzlover on 2017/6/10.
 */
@Mapper
public interface TopicMapper {
	
	public int addTopic(Topic topic);

    public List<Category> queryAllCategories();

    public List<Topic> queryTopicsPagely(Map<String,Object> map);

    public int queryTopicCounts(Map<String,Object> map);
    
    public Topic queryTopicById(Map<String,Object> map);
    
    public int editTopic(Topic topic);
    
    public int checkTopicIsMine(Map<String,Object> map);
    
    public List<TopicComment> queryTopicCommentsPagely(Map<String,Object> map);
    
    public int queryTopicCommentsCounts(Map<String,Object> map);

    public int addTopicLike(TopicLike like);
    
    public int delTopicLike(TopicLike like);
    
    public int addTopicComment(TopicComment comment);
    
    public int updateTopicCommentStatus(Map<String,Object> map);
    
    public int addTopicCommentLike(TopicCommentLike like);
    
    public int delTopicCommentLike(TopicCommentLike like);
    
    public int checkTopicLikeExist(TopicLike like);
    
    public int checkTopicCommentLikeExist(TopicCommentLike like);
    
    public int editTopicCommentCount(Long id);
    
    public int editTopicLikeCount(Long id);
    
    public int editTopicCommentLikeCount(Long id);
    
    public TopicComment queryTopicComment(Map<String,Object> map);
    
    public List<User> queryTopicCommentedUsers(Map<String,Object> map);
    
    public int editTopicStatus(Map<String,Object> map);
    
    public int editTopicViewCount(Long id);
    
    public int addTopicCommentTo(TopicCommentTo commentTo);
    
    public int queryTopicCommentIndex(Map<String,Object> map);
    
    public int checkTopicIsOwnComment(Map<String,Object> map);
    
    public int editTopicCommentReaded(Map<String,Object> map);
    
    public int editTopicLikeReaded(Map<String,Object> map);
    
    public int editTopicCommentLikeReaded(Map<String,Object> map);
    
    public int editTopicCommentToReaded(Map<String,Object> map);
    
    public int checkUserHasAuthEditTopicCommentReaded(Map<String,Object> map);
    
    public int checkUserHasAuthEditTopicLikeReaded(Map<String,Object> map);
    
    public int checkUserHasAuthEditTopicCommentLikeReaded(Map<String,Object> map);
    
    public int checkUserHasAuthEditTopicCommentToReaded(Map<String,Object> map);
    
    public int queryTopicCommentReadedStatus(Map<String,Object> map);
    
    public int queryTopicCommentToReadedStatus(Map<String,Object> map);
    
    public int queryTopicLikeReadedStatus(Map<String,Object> map);
    
    public int queryTopicCommentLikeReadedStatus(Map<String,Object> map);
    
    public Topic queryTopicByCommentId(Map<String,Object> map);
    
    public int checkTopicIsDeleted(Map<String,Object> map);

    public int addTagTopicRel(TagTopicRel rel);

    public int delTagTopicRel(TagTopicRel rel);

    public List<Tag> queryTopicTags(Map<String,Object> map);
    
    public int checkTopicIsInTag(TagTopicRel rel);
}
