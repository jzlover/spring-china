package org.spring.china.web.controller;

import java.security.Principal;
import java.util.List;

import org.spring.china.base.common.Res;
import org.spring.china.base.common.ResPager;
import org.spring.china.base.feign.TopicFeign;
import org.spring.china.base.feign.UserFeign;
import org.spring.china.base.pojo.Tag;
import org.spring.china.base.pojo.Topic;
import org.spring.china.web.common.JLogger;
import org.spring.china.web.model.Model_RequestMessage;
import org.spring.china.web.model.Model_ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
 

/**
 * Created by jzlover on 2017/7/5.
 */
@Controller
public class RootController {

	
	
	@Autowired
    private TopicFeign topicFeign;
	
	@Autowired
	private UserFeign userFeign;
	
	@RequestMapping(value="/")
	public String Root(){
		return "redirect:/topics";
	}
	
	@JLogger(value = "aaa")
	@RequestMapping(value="/topics")
	public ModelAndView Topics(
			@RequestParam(value="filter",required=false) Integer filter,
			@RequestParam(value="title",required=false) String title,
			@RequestParam(value="page",required=false) Integer page,
			@RequestParam(value="pc",required=false) Integer pageSize){
		ModelAndView model = new ModelAndView("views/root/topics"); 
 
		int status=1;
		if(pageSize==null) pageSize=20;
		if(page==null) page=1;
		if(filter==null) filter=null;
		ResPager<List<Topic>> res_topics= topicFeign.QueryTopicsPagely(status, filter,title, page, pageSize);
		ResPager<List<Tag>> res_tags=this.userFeign.QueryTagsPagely(1, 20);
		model.addObject("modelTags",res_tags);
		model.addObject("model",res_topics);
		model.addObject("filter",filter);
		model.addObject("title",title);
		model.addObject("viewName","topics");
		return model;
	}
	
}
