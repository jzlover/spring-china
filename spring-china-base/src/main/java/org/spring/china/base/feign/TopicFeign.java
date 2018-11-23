package org.spring.china.base.feign;

import java.util.List;

import org.spring.china.base.common.Res;
import org.spring.china.base.common.ResPager;
import org.spring.china.base.feign.fallback.TopicFeignFallback;
import org.spring.china.base.pojo.*;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value="spring-china-topic",fallback = TopicFeignFallback.class)
public interface TopicFeign {
	
	//获取所有类别
	@RequestMapping(value="/query-all-categories",method = RequestMethod.POST)
    public Res<List<Category>> QueryAllCategories();
	
	//添加Topic
	@RequestMapping(value="/add-topic", method= RequestMethod.POST)
    public Res<Long> AddTopic(@RequestBody Topic topic);
	
	//通过Topic的ID号查询单个Topic
	@RequestMapping(value="/query-topic-by-id/{id}",method = RequestMethod.POST)
    public Res<Topic> QueryTopicById(@PathVariable("id") Long id,@RequestParam(value="userId",required=false) Long userId);
	
	//编辑Topic
	@RequestMapping(value="/edit-topic", method= RequestMethod.POST)
    public Res<Boolean> EditTopic(@RequestBody Topic topic);
	
	//检查用户是否有权限
	@RequestMapping(value="/check-topic-is-mine",method=RequestMethod.POST)
    public Res<Boolean> CheckTopicIsMine(@RequestParam(value="id",required=false) Long id,@RequestParam(value="uesrId",required=false) Long userId);
	
	//获取topics
	@RequestMapping(value="/query-topics-pagely",method=RequestMethod.POST)
    public ResPager<List<Topic>> QueryTopicsPagely(
    		@RequestParam(value="status",required=false) Integer status,
    		@RequestParam(value="filter",required=false) Integer filter,
    		@RequestParam(value="title",required=false) String title,
    		@RequestParam(value="page",required=false) Integer page,
    		@RequestParam(value="pageSize",required=false) Integer pageSize
    		);
	
	//获取topic comments
	@RequestMapping(value="/query-topic-comments-pagely",method=RequestMethod.POST)
    public ResPager<List<TopicComment>> QueryTopicCommentsPagely(
    		@RequestParam(value="id",required=false) Long id,
    		@RequestParam(value="userId",required=false) Long userId,
    		@RequestParam(value="page",required=false) Integer page,
    		@RequestParam(value="pageSize",required=false) Integer pageSize
    		);

	//添加Topic赞
	@RequestMapping(value="/add-topic-like",method=RequestMethod.POST)
    public Res<Long> AddTopicLike(@RequestBody TopicLike like);
	
	//删除Topic赞
	@RequestMapping(value="/del-topic-like",method=RequestMethod.POST)
    public Res<Boolean> DelTopicLike(@RequestBody TopicLike like);
	
	//添加Topic评论
	@RequestMapping(value="/add-topic-comment",method=RequestMethod.POST)
    public Res<Long> AddTopicComment(@RequestBody TopicComment comment);
	
	//删除Topic评论
	@RequestMapping(value="/del-topic-comment",method=RequestMethod.POST)
    public Res<Boolean> DelTopicComment(@RequestBody Long id);
	
	//添加Topic评论的赞
	@RequestMapping(value="/add-topic-comment-like",method=RequestMethod.POST)
    public Res<Long> AddTopicCommentLike(@RequestBody TopicCommentLike like);
	
	//删除Topic评论的赞
	@RequestMapping(value="/del-topic-comment-like",method=RequestMethod.POST)
    public Res<Boolean> DelTopicCommentLike(@RequestBody TopicCommentLike like);
	
	
	@RequestMapping(value="/check-topic-like-exist",method=RequestMethod.POST)
    public Res<Boolean> CheckTopicLikeExist(@RequestBody TopicLike like);
	
	@RequestMapping(value="/check-topic-comment-like-exist",method=RequestMethod.POST)
    public Res<Boolean> CheckTopicCommentLikeExist(@RequestBody TopicCommentLike like);
	
	@RequestMapping(value="/query-topic-comment",method=RequestMethod.POST)
    public Res<TopicComment> QueryTopicComment(@RequestParam("id") Long id,@RequestParam(value="userId",required=false) Long userId);
	
	@RequestMapping(value="/query-topic-commented-users",method=RequestMethod.POST)
    public Res<List<User>> QueryTopicCommentedUsers(@RequestParam("id") Long id,@RequestParam(value="userId",required=false) Long userId);
	
	@RequestMapping(value="/edit-topic-status",method=RequestMethod.POST)
    public Res<Boolean> EditTopicStatus(@RequestParam("id") Long id,@RequestParam("status") Integer status);
	
	//更新Topic的viewCount字段值+1
	@RequestMapping(value="/edit-topic-viewCount/{id}",method=RequestMethod.POST)
    public Res<Boolean> EditTopicViewCount(@PathVariable("id") Long id);
	
	@RequestMapping(value="/query-topic-comment-index",method=RequestMethod.POST)
    public Res<Integer> QueryTopicCommentIndex(
    		@RequestParam("topicId") Long topicId,
    		@RequestParam("commentId") Long commentId);
	
	//设置已经读取
	@RequestMapping(value="/edit-readed",method=RequestMethod.POST)
    public Res<Integer> EditReaded(
    		@RequestParam("userId") Long userId,
    		@RequestParam(value="type",required=false) Integer type,
    		@RequestParam(value="id",required=false) Long id,
    		@RequestParam(value="readed",required=false) Boolean readed);
	
	//通过TopicCommentId号来获取Topic
	@RequestMapping(value="/query-topic-by-commentId/{id}",method = RequestMethod.POST)
    public Res<Topic> QueryTopicByCommentId(@PathVariable("id") Long id,@RequestParam(value="userId",required=false) Long userId);
	
	//判断当前Topic是否已经被删除
	@RequestMapping(value="/check-topic-is-deleted/{id}",method = RequestMethod.POST)
    public Res<Boolean> CheckTopicIsDeleted(@PathVariable("id") Long id);
	
	//设置已经读取字段
	@RequestMapping(value="/edit-readed-multi",method=RequestMethod.POST)
    public Res<Integer> EditReadedMulti(
    		@RequestParam("userId") Long userId,
    		@RequestParam(value="strToEdit",required=true) String strToEdit ,
    		@RequestParam(value="readed",required=false) Boolean readed);

	
 

	//添加TagTopicRel
	@RequestMapping(value="/add-tag-topic-rel",method = RequestMethod.POST)
	public Res<Long> AddTagTopicRel(@RequestBody TagTopicRel rel);

	@RequestMapping(value="/del-tag-topic-rel",method = RequestMethod.POST)
	public Res<Boolean> DelTagTopicRel(@RequestBody TagTopicRel rel);
	
	
    @RequestMapping(value="/query-topic-tags",method=RequestMethod.POST)
    public Res<List<Tag>> QueryTopicTags(
    		@RequestParam("userId") long userId,
    		@RequestParam("topicId") long topicId);
}