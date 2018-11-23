package org.spring.china.web.controller;

import org.spring.china.base.common.Res;
import org.spring.china.base.feign.TopicFeign;
import org.spring.china.base.feign.UserFeign;
import org.spring.china.base.pojo.Category;
import org.spring.china.base.pojo.Tag;
import org.spring.china.base.pojo.Topic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by jzlover on 2017/6/10.
 */

@RequestMapping("/topic")
@Controller
public class TopicController {


    @Autowired
    private HttpServletRequest request;

    @Autowired
    private TopicFeign topicFeign;
    
    @Autowired
    private UserFeign userFeign;
    
    
    /**
     * 创建新Topic页面
     * @return
     */
    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value="/create")
    public ModelAndView Create(){
        ModelAndView model = new ModelAndView("views/topic/create_edit");

        Object session_userId =request.getSession().getAttribute("userId");
		long longUserId;
		if(session_userId==null){
			return null;
		}else{
			longUserId=Long.parseLong(session_userId.toString());
		}
		
        Res<List<Category>> res_categories=topicFeign.QueryAllCategories();
        Res<List<Tag>> res_tags=this.userFeign.QueryUserTags(longUserId, null);
        
        model.addObject("tags",res_tags.getData());
        model.addObject("categories",res_categories.getData());
        model.addObject("type","create");
        return model;
    }
    
    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value="/edit/{id}")
    public ModelAndView Edit(@PathVariable("id") long id){
    	ModelAndView model=new ModelAndView("views/topic/create_edit");
    	Object session_userId =request.getSession().getAttribute("userId");
		long longUserId;
		if(session_userId==null){
			return null;
		}else{
			longUserId=Long.parseLong(session_userId.toString());
		}
    	//判断是否是自己的Topic
    	Res<Boolean> res_isMine=topicFeign.CheckTopicIsMine(id, longUserId);
    	if(!res_isMine.getData()){
    		model.setViewName("/error");
			return model;
    	}
    	
    	Res<List<Category>> res_categories=topicFeign.QueryAllCategories();
    	Res<List<Tag>> res_tags=this.userFeign.QueryUserTags(longUserId, id);
    	Res<Topic> res_topic=topicFeign.QueryTopicById(id,null);
    	
        model.addObject("tags",res_tags.getData());
        model.addObject("categories",res_categories.getData());
        
        model.addObject("topic",res_topic.getData());
        model.addObject("type","edit");
        return model;
    }
    
    
    /**
     * 显示单个Topic页面
     * @param id topic的ID号
     * @param cId 评论的Id
     * @return
     */
    @RequestMapping(value="/show/{id}",method=RequestMethod.GET)
    public ModelAndView Show(
    		@PathVariable("id") Long id,
    		@RequestParam(value="cId",required=false) Long cId,
    		@RequestParam(value="rId",required=false) Long rId,
    		@RequestParam(value="type",required=false) Integer type) {
    	ModelAndView model = new ModelAndView("views/topic/show"); 
		Object userid =request.getSession().getAttribute("userId");
		Long userId=0L;
		if(userid==null){
			Res<Boolean> topic_is_deleted_res= topicFeign.CheckTopicIsDeleted(id);
			if(topic_is_deleted_res.getCode()!=0){
				model.setViewName("error");
				return model;
			}else{
				if(topic_is_deleted_res.getData()){
					//已经被删除
					model.addObject("errMsg","该话题已被删除！");
					model.setViewName("error");
					return model;
				}
			}
		}else{
			userId=Long.parseLong(userid.toString());
			Res<Boolean> check_res=topicFeign.CheckTopicIsMine(id, Long.parseLong(userId.toString()));
			if(check_res.getCode()!=0){
				model.setViewName("error");
				return model;
			}
			model.addObject("mine",check_res.getData());
		}
		
		//更新查看数
		topicFeign.EditTopicViewCount(id);
		
		Res<Topic> topic_res= topicFeign.QueryTopicById(id,userId);
		if(topic_res.getCode()!=0){
			model.setViewName("error");
			return model;
		}
		model.addObject("topic",topic_res.getData());
		if(cId!=null){
			Res<Integer> comment_index_res= topicFeign.QueryTopicCommentIndex(id, cId);
			if(comment_index_res.getCode()!=0){
				model.addObject("commentIndex",null);
			}else{
				model.addObject("commentIndex",comment_index_res.getData());
			}
		}
		
		model.addObject("cId",cId);
		model.addObject("readedType",type);
		model.addObject("rId",rId);
		return model;
    }
    
 
}
