package org.spring.china.web.controller.ajax;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.spring.china.base.common.Constant;
import org.spring.china.base.common.Res;
import org.spring.china.base.common.ResPager;
import org.spring.china.base.feign.TopicFeign;
import org.spring.china.base.feign.UserFeign;
import org.spring.china.base.pojo.Topic;
import org.spring.china.base.pojo.TopicComment;
import org.spring.china.base.pojo.TopicCommentLike;
import org.spring.china.base.pojo.TopicCommentTo;
import org.spring.china.base.pojo.TopicLike;
import org.spring.china.base.pojo.User;
import org.spring.china.web.model.Model_QueryPagely;
import org.spring.china.web.model.Model_ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by jzlover on 2017/6/10.
 */
@RequestMapping("/topic-ajax")
@RestController
public class TopicAjax {
 
	@Autowired 
	private SimpMessagingTemplate msgTemplate; 

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private UserFeign userFeign;
    
    @Autowired
    private TopicFeign topicFeign;
    
    @PreAuthorize("hasRole('ROLE_USER')")
    @ResponseBody
    @RequestMapping(value="/add-topic",method=RequestMethod.POST)
    public Res<Long> AddTopic(@RequestBody Topic topic){
    	Res<Long> res=new Res<Long>();   	
    	Object userId=request.getSession().getAttribute("userId");
   
    	if(userId!=null)
    		topic.setUserId(Long.parseLong(userId.toString()));
    	else{
    		res.setCode(-2);
    		res.setMsg("用户ID未找到！");
    		return res;
    	}   		 
    	//TODO 进行后台数据校验
 
    	
    	return topicFeign.AddTopic(topic);
    }
    
    @PreAuthorize("hasRole('ROLE_USER')")
    @ResponseBody
    @RequestMapping(value="/edit-topic",method=RequestMethod.POST)
    public Res<Boolean> EditTopic(@RequestBody Topic topic){
    	Object session_userId =request.getSession().getAttribute("userId");
 		long longUserId;
 		if(session_userId==null){
 			return null;
 		}else{
 			longUserId=Long.parseLong(session_userId.toString());
 		}
 		
 		topic.setUserId(longUserId);
 		
    	return topicFeign.EditTopic(topic);
    }
    
    @ResponseBody
	@RequestMapping(value="/query-topic-comments-pagely/{id}",method=RequestMethod.POST)
	public ResPager<List<TopicComment>> QueryTopicCommentsPagely(
			@PathVariable("id") long id,
			@RequestBody Model_QueryPagely model){
		Object userid =request.getSession().getAttribute("userId");
		Long userId=null;
		if(userid!=null)
			userId=Long.parseLong(userid.toString());
		return topicFeign.QueryTopicCommentsPagely(id, userId, model.getPage(), model.getPageSize());
	}
    
    @PreAuthorize("hasRole('ROLE_USER')")
    @ResponseBody
	@RequestMapping(value="/add-topic-comment",method=RequestMethod.POST)
    public Res<Long> AddTopicComment(@RequestBody TopicComment comment){
    	Res<Long> res=new Res<Long>();   	
    	Object userId=request.getSession().getAttribute("userId");
    	Long longUserId=null;
    	if(userId!=null){
    		longUserId=Long.parseLong(userId.toString());
    		comment.setUserId(longUserId);
    	}else{
    		res.setCode(-2);
    		res.setMsg("用户ID未找到！");
    		return res;
    	}   		 
    	res= topicFeign.AddTopicComment(comment);
    	
    	if(res.getCode()==0){
    		//发送即时消息
    		
    		List<TopicCommentTo> topic_comment_tos=new ArrayList<TopicCommentTo>();
    		Res<TopicComment> res_topic_comment= topicFeign.QueryTopicComment(res.getData(), null);
    		if(res_topic_comment.getCode()==0){
    			topic_comment_tos= res_topic_comment.getData().getTos();
    			for(TopicCommentTo to:topic_comment_tos){
    				Res<User> res_user= userFeign.QueryUserById(longUserId);
    				if(res_user.getCode()==0){
    					Model_ResponseMessage websocket_response_msg=new Model_ResponseMessage();
    					websocket_response_msg.setUser(res_user.getData());
    					websocket_response_msg.setMsgType(Constant.MSG_TYPE_COMMENT_TO);//@
    					websocket_response_msg.setcId(res.getData());
    					websocket_response_msg.setrId(to.getId());
    					websocket_response_msg.setTopicId(comment.getTopicId());
    					msgTemplate.convertAndSendToUser(to.getUserId().toString(), "/msg/getResponse",websocket_response_msg);
    				}
    				
    			}
    		}
    		
    		Res<Topic> res_topic=topicFeign.QueryTopicById(comment.getTopicId(),null);
    		if(res_topic.getCode()==0){
    			Boolean _sended=false;
    			for(TopicCommentTo to:topic_comment_tos){
    				if(to.getUserId()==res_topic.getData().getUserId())
    					_sended=true;
    			}
    			if(res_topic.getData().getUserId()!=longUserId && !_sended){
    				Res<User> res_user= userFeign.QueryUserById(longUserId);
    				if(res_user.getCode()==0){
    					Model_ResponseMessage websocket_response_msg=new Model_ResponseMessage();
    					websocket_response_msg.setUser(res_user.getData());
    					websocket_response_msg.setMsgType(Constant.MSG_TYPE_COMMENT);
    					websocket_response_msg.setcId(res.getData());
    					websocket_response_msg.setrId(res.getData());
    					websocket_response_msg.setTopicId(comment.getTopicId());
    					msgTemplate.convertAndSendToUser(res_topic.getData().getUserId().toString(), "/msg/getResponse",websocket_response_msg);
    				}
    				
    			}
    		}
    	}
    	
    	return res;
    }
    
    @PreAuthorize("hasRole('ROLE_USER')")
    @ResponseBody
    @RequestMapping(value="/query-topic-comment-by-id/{id}",method=RequestMethod.POST)
    public Res<TopicComment> QueryTopicCommentById(@PathVariable("id") Long id){
		return topicFeign.QueryTopicComment(id, null);
    }
    
    @PreAuthorize("hasRole('ROLE_USER')")
    @ResponseBody
    @RequestMapping(value="/del-topic-comment-by-id/{id}",method=RequestMethod.POST)
    public Res<Boolean> DelTopicCommentById(@PathVariable("id") Long id){
    	return topicFeign.DelTopicComment(id);
    }
    
    @PreAuthorize("hasRole('ROLE_USER')")
    @ResponseBody
    @RequestMapping(value="/add-topic-like/{id}",method=RequestMethod.POST)
    public Res<Long> AddTopicLike(@PathVariable("id") Long topicId){
    	Res<Long> res=new Res<Long>();
    	TopicLike like=new TopicLike();
    	Object userId=request.getSession().getAttribute("userId");
    	Long longUserId=null;
    	if(userId!=null){
    		longUserId=Long.parseLong(userId.toString());
    		like.setUserId(longUserId);
    	}  		
    	else{
    		res.setCode(-2);
    		res.setMsg("用户ID不存在！");
    		return res;
    	}
    	like.setTopicId(topicId);
    	res= topicFeign.AddTopicLike(like);
    	if(res.getCode()==0){
    		//发送即时消息
    		Res<Topic> res_topic=topicFeign.QueryTopicById(topicId,null);
    		if(res_topic.getCode()==0 && res_topic.getData().getUserId()!=longUserId){
    			//不是自己的Topic才需要发送
    			Res<User> res_user= userFeign.QueryUserById(longUserId);
    			if(res_user.getCode()==0){
    				Model_ResponseMessage websocket_response_msg=new Model_ResponseMessage();
    				websocket_response_msg.setUser(res_user.getData());
    				websocket_response_msg.setMsgType(Constant.MSG_TYPE_TOPIC_LIKE);
    				websocket_response_msg.setrId(res.getData());
    				websocket_response_msg.setTopicId(topicId);
    				msgTemplate.convertAndSendToUser(res_topic.getData().getUserId().toString(), "/msg/getResponse",websocket_response_msg);
    			}
    		}
    		
    	}
    	return res;
    }
    
    @PreAuthorize("hasRole('ROLE_USER')")
    @ResponseBody
    @RequestMapping(value="/add-topic-comment-like/{id}",method=RequestMethod.POST)
    public Res<Long> AddTopicCommentLike(@PathVariable("id") Long commentId){
    	Res<Long> res=new Res<Long>();
    	TopicCommentLike like=new TopicCommentLike();
    	Object userId=request.getSession().getAttribute("userId");
    	Long longUserId=null;
    	if(userId!=null){
    		longUserId=Long.parseLong(userId.toString());
    		like.setUserId(longUserId);
    	}   		
    	else{
    		res.setCode(-2);
    		res.setMsg("用户ID不存在！");
    		return res;
    	}
    	like.setCommentId(commentId);
    	res= topicFeign.AddTopicCommentLike(like);
    	if(res.getCode()==0){

    		Res<TopicComment> res_topiccomment= topicFeign.QueryTopicComment(commentId, null);
    		if(res_topiccomment.getCode()==0 && res_topiccomment.getData().getUserId()!=longUserId){
    			//不是给自己点赞才需要发送
    			Res<User> res_user= userFeign.QueryUserById(longUserId);
    			if(res_user.getCode()==0){
    				Model_ResponseMessage websocket_response_msg=new Model_ResponseMessage();
    				websocket_response_msg.setUser(res_user.getData());
    				websocket_response_msg.setMsgType(Constant.MSG_TYPE_TOPIC_COMMENT_LIKE);
    				websocket_response_msg.setcId(commentId);
    				websocket_response_msg.setrId(res.getData());
    				//通过TopicCommentId来获取Topic
    				Res<Topic> res_topic= topicFeign.QueryTopicByCommentId(commentId, null);
    				if(res_topic.getCode()==0){
    					websocket_response_msg.setTopicId(res_topic.getData().getId());
    				}
    				
    				msgTemplate.convertAndSendToUser(res_topiccomment.getData().getUserId().toString(), "/msg/getResponse",websocket_response_msg);
    			}
    		}
    	}
    	return res;
    }
    
    @PreAuthorize("hasRole('ROLE_USER')")
    @ResponseBody
    @RequestMapping(value="/del-topic-like/{id}")
    public Res<Boolean> DeltTopicLike(@PathVariable("id") Long topicId){
    	Res<Boolean> res=new Res<Boolean>();
    	TopicLike like=new TopicLike();
    	Object userId=request.getSession().getAttribute("userId");
 	   
    	if(userId!=null)
    		like.setUserId(Long.parseLong(userId.toString()));
    	else{
    		res.setCode(-2);
    		res.setMsg("用户ID不存在！");
    		return res;
    	}
    	like.setTopicId(topicId);
    	return topicFeign.DelTopicLike(like);
    }
    
    @PreAuthorize("hasRole('ROLE_USER')")
    @ResponseBody
    @RequestMapping(value="/del-topic-comment-like/{id}")
    public Res<Boolean> DelTopicCommentLike(@PathVariable("id") Long commentId){
    	Res<Boolean> res=new Res<Boolean>();
    	TopicCommentLike like=new TopicCommentLike();
    	Object userId=request.getSession().getAttribute("userId");
 	   
    	if(userId!=null)
    		like.setUserId(Long.parseLong(userId.toString()));
    	else{
    		res.setCode(-2);
    		res.setMsg("用户ID不存在！");
    		return res;
    	}
    	like.setCommentId(commentId);
    	return topicFeign.DelTopicCommentLike(like);
    }
    
    @PreAuthorize("hasRole('ROLE_USER')")
    @ResponseBody
    @RequestMapping(value="/query-topic-commented-users/{id}")
    public Res<List<User>> QueryTopicCommentedUsers(@PathVariable("id") Long id){
    	Object userid =request.getSession().getAttribute("userId");
		Long userId=null;
		if(userid!=null)
			userId=Long.parseLong(userid.toString());
		return topicFeign.QueryTopicCommentedUsers(id, userId);
    }
    
    @PreAuthorize("hasRole('ROLE_USER')")
    @ResponseBody
    @RequestMapping(value="/edit-topic-status/{id}/{status}")
    public Res<Boolean> EditTopicStatus(@PathVariable("id") Long id,@PathVariable("status") Integer status){
    	return topicFeign.EditTopicStatus(id, status);
    }
    
}
