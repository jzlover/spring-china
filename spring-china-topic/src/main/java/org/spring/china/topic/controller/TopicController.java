package org.spring.china.topic.controller;

import org.spring.china.base.common.Res;
import org.spring.china.base.common.ResPager;
import org.spring.china.base.pojo.Category;
import org.spring.china.base.pojo.Tag;
import org.spring.china.base.pojo.Topic;
import org.spring.china.base.pojo.TopicComment;
import org.spring.china.base.pojo.TopicCommentLike;
import org.spring.china.base.pojo.TopicLike;
import org.spring.china.base.pojo.User;
import org.spring.china.topic.service.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;


import java.util.List;

/**
 * Created by jzlover on 2017/6/10.
 */

@RestController
@RefreshScope
public class TopicController {

   

    @Autowired
    private TopicService topicService;

    @ResponseBody
    @RequestMapping(value="/add-topic", method= RequestMethod.POST)
    public Res<Long> AddTopic(@RequestBody Topic topic) {
        return topicService.AddTopic(topic);
    }



    @ResponseBody
    @RequestMapping(value="/query-all-categories",method = RequestMethod.POST)
    public Res<List<Category>> QueryAllCategories(){
        return topicService.QueryAllCategories();
    }

    
    @ResponseBody
    @RequestMapping(value="/query-topic-by-id/{id}",method = RequestMethod.POST)
    public Res<Topic> QueryTopicById(@PathVariable("id") Long id,@RequestParam(value="userId",required=false) Long userId){
        return topicService.QueryTopicById(id,userId);
    }
    
    @ResponseBody
    @RequestMapping(value="/edit-topic", method= RequestMethod.POST)
    public Res<Boolean> EditTopic(@RequestBody Topic topic) {
        return topicService.EditTopic(topic);
    }
    
    @ResponseBody
    @RequestMapping(value="/check-topic-is-mine",method=RequestMethod.POST)
    public Res<Boolean> CheckTopicIsMine(@RequestParam(value="id",required=false) Long id,@RequestParam(value="uesrId",required=false) Long userId){
    	return topicService.CheckTopicIsMine(id, userId);
    }
    
    @ResponseBody
    @RequestMapping(value="/query-topics-pagely",method=RequestMethod.POST)
    public ResPager<List<Topic>> QueryTopicsPagely(
    		@RequestParam(value="status",required=false) Integer status,
    		@RequestParam(value="filter",required=false) Integer filter,
    		@RequestParam(value="title",required=false) String title,
    		@RequestParam(value="page",required=false) Integer page,
    		@RequestParam(value="pageSize",required=false) Integer pageSize
    		){
    	return topicService.QueryTopicsPagely(status, filter,title, page, pageSize);
    }
    
    
    @ResponseBody
    @RequestMapping(value="/query-topic-comments-pagely",method=RequestMethod.POST)
    public ResPager<List<TopicComment>> QueryTopicCommentsPagely(
    		@RequestParam(value="id",required=false) Long id,
    		@RequestParam(value="userId",required=false) Long userId,
    		@RequestParam(value="page",required=false) Integer page,
    		@RequestParam(value="pageSize",required=false) Integer pageSize
    		){
    	return topicService.QueryTopicCommentsPagely(id, userId, page, pageSize);
    }
    
    @ResponseBody
    @RequestMapping(value="/add-topic-like",method=RequestMethod.POST)
    public Res<Long> AddTopicLike(@RequestBody TopicLike like){
    	return topicService.AddTopicLike(like);
    }
    
    @ResponseBody
    @RequestMapping(value="/del-topic-like",method=RequestMethod.POST)
    public Res<Boolean> DelTopicLike(@RequestBody TopicLike like){
    	return topicService.DelTopicLike(like);
    }
    
    @ResponseBody
    @RequestMapping(value="/add-topic-comment",method=RequestMethod.POST)
    public Res<Long> AddTopicComment(@RequestBody TopicComment comment){
    	return topicService.AddTopicComment(comment);
    }
    
    @ResponseBody
    @RequestMapping(value="/del-topic-comment",method=RequestMethod.POST)
    public Res<Boolean> DelTopicComment(@RequestBody Long id){
    	return topicService.DelTopicComment(id);
    }
    
    @ResponseBody
    @RequestMapping(value="/add-topic-comment-like",method=RequestMethod.POST)
    public Res<Long> AddTopicCommentLike(@RequestBody TopicCommentLike like){
    	return topicService.AddTopicCommentLike(like);
    }
    
    @ResponseBody
    @RequestMapping(value="/del-topic-comment-like",method=RequestMethod.POST)
    public Res<Boolean> DelTopicCommentLike(@RequestBody TopicCommentLike like){
    	return topicService.DelTopicCommentLike(like);
    }
    
    @ResponseBody
    @RequestMapping(value="/check-topic-like-exist",method=RequestMethod.POST)
    public Res<Boolean> CheckTopicLikeExist(@RequestBody TopicLike like){
    	return topicService.CheckTopicLikeExist(like);
    }
    
    
    @ResponseBody
    @RequestMapping(value="/check-topic-comment-like-exist",method=RequestMethod.POST)
    public Res<Boolean> CheckTopicCommentLikeExist(@RequestBody TopicCommentLike like){
    	return topicService.CheckTopicCommentLikeExist(like);
    } 
    
    @ResponseBody
    @RequestMapping(value="/query-topic-comment",method=RequestMethod.POST)
    public Res<TopicComment> QueryTopicComment(@RequestParam("id") Long id,@RequestParam(value="userId",required=false) Long userId){
    	return topicService.QueryTopicComment(id, userId);
    }
    
    @ResponseBody
    @RequestMapping(value="/query-topic-commented-users",method=RequestMethod.POST)
    public Res<List<User>> QueryTopicCommentedUsers(@RequestParam("id") Long id,@RequestParam(value="userId",required=false) Long userId){
    	return topicService.QueryTopicCommentedUsers(id, userId);
    }
    
    @ResponseBody
    @RequestMapping(value="/edit-topic-status",method=RequestMethod.POST)
    public Res<Boolean> EditTopicStatus(@RequestParam("id") Long id,@RequestParam("status") Integer status){
    	return topicService.EditTopicStatus(id, status);
    }
    
    @ResponseBody
    @RequestMapping(value="/edit-topic-viewCount/{id}",method=RequestMethod.POST)
    public Res<Boolean> EditTopicViewCount(@PathVariable("id") Long id){
    	return topicService.EditTopicViewCount(id);
    }
    
    @ResponseBody
    @RequestMapping(value="/query-topic-comment-index",method=RequestMethod.POST)
    public Res<Integer> QueryTopicCommentIndex(
    		@RequestParam("topicId") Long topicId,
    		@RequestParam("commentId") Long commentId){
    	return topicService.QueryTopicCommentIndex(topicId, commentId);
    }
    
    @ResponseBody
    @RequestMapping(value="/edit-readed",method=RequestMethod.POST)
    public Res<Integer> EditReaded(
    		@RequestParam("userId") Long userId,
    		@RequestParam(value="type",required=false) Integer type,
    		@RequestParam(value="id",required=false) Long id,
    		@RequestParam(value="readed",required=false) Boolean readed){
    	return topicService.EditReaded(userId,type, id, readed);
    }
    
    
    @ResponseBody
    @RequestMapping(value="/query-topic-by-commentId/{id}",method = RequestMethod.POST)
    public Res<Topic> QueryTopicByCommentId(@PathVariable("id") Long id,@RequestParam(value="userId",required=false) Long userId){
        return topicService.QueryTopicByCommentId(id,userId);
    }
    
    @ResponseBody
    @RequestMapping(value="/check-topic-is-deleted/{id}",method = RequestMethod.POST)
    public Res<Boolean> CheckTopicIsDeleted(@PathVariable("id") Long id){
    	return topicService.CheckTopicIsDeleted(id);
    }
    
    @ResponseBody
    @RequestMapping(value="/edit-readed-multi",method=RequestMethod.POST)
    public Res<Integer> EditReadedMulti(
    		@RequestParam("userId") Long userId,
    		@RequestParam(value="strToEdit",required=true) String strToEdit ,
    		@RequestParam(value="readed",required=false) Boolean readed){
    	return topicService.EditReadedMulti(userId, strToEdit, readed);
    }
    
    @ResponseBody
    @RequestMapping(value="/query-topic-tags",method=RequestMethod.POST)
    public Res<List<Tag>> QueryTopicTags(
    		@RequestParam("userId") long userId,
    		@RequestParam("topicId") long topicId){
    	return this.topicService.QueryTopicTags(userId,topicId);
    }
}
