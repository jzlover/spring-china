package org.spring.china.topic.service.impl;

import org.apache.commons.lang.StringUtils;
import org.spring.china.base.common.Res;
import org.spring.china.base.common.ResPager;
import org.spring.china.base.feign.UserFeign;
import org.spring.china.base.pojo.Category;
import org.spring.china.base.pojo.Tag;
import org.spring.china.base.pojo.TagTopicRel;
import org.spring.china.base.pojo.Topic;
import org.spring.china.base.pojo.TopicComment;
import org.spring.china.base.pojo.TopicCommentLike;
import org.spring.china.base.pojo.TopicCommentTo;
import org.spring.china.base.pojo.TopicLike;
import org.spring.china.base.pojo.User;
import org.spring.china.base.result.UserRelatedComment;
import org.spring.china.topic.mapper.TopicMapper;
import org.spring.china.topic.service.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by jzlover on 2017/6/10.
 */
@Service("topicService")
public class TopicServiceImpl implements TopicService {


    @Autowired
    private TopicMapper topicMapper;

    @Autowired
    private UserFeign userFeign;
    

    @Transactional
    @Override
    public Res<Long> AddTopic(Topic topic) {
    	Res<Long> res=new Res<Long>();
    	topic.setCreatedAt(new Date());
    	
        int count=topicMapper.addTopic(topic);//影响的行数
        for(Tag tag:topic.getTags()){
    		TagTopicRel rel = new TagTopicRel();
    		rel.setTopicId(topic.getId());
    		rel.setTagId(tag.getId());
    		this.topicMapper.addTagTopicRel(rel);
    	}
        
        res.setData(topic.getId());//获取自增ID值
        if(count<1){
        	res.setCode(-1);
            res.setMsg("插入Topic失败！");
        }
        return res;
    }

    @Override
    public Res<List<Category>> QueryAllCategories() {
    	Res<List<Category>> res=new Res<List<Category>>();
    	res.setData(topicMapper.queryAllCategories());
        return res;
    }

	@Override
	public Res<Topic> QueryTopicById(Long id,Long userId) {
		Res<Topic> res=new Res<Topic>();		
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("id", id);
		map.put("userId", userId);
		res.setData(topicMapper.queryTopicById(map));
		return res;
	}

	@Transactional
	@Override
	public Res<Boolean> EditTopic(Topic topic) {
		Res<Boolean> res=new Res<Boolean>();
		
		List<Long> original_topic_tags = new ArrayList<Long>();
		List<Long> new_topic_tags = new ArrayList<Long>();
		List<Long> to_del_tags = new ArrayList<Long>();
		List<Long> to_add_tags = new ArrayList<Long>();
		
    	Res<List<Tag>> res_tags= this.QueryTopicTags(topic.getUserId(), topic.getId());
    	
    	if(res_tags.getCode()==0){
    		if(!res_tags.getData().isEmpty()){
    			//不为空
    			for(Tag tag:res_tags.getData()){
            		original_topic_tags.add(tag.getId());
            	}
    		}
    		
    	}else{
    		res.setCode(-2);
    		res.setMsg("获取Topic的标签失败！");
    		return res;
    	}
    	
    	if(!topic.getTags().isEmpty()){
    		for(Tag tag : topic.getTags()){
    			new_topic_tags.add(tag.getId());
    		}
    	}
    	
    	
    	to_del_tags.addAll(original_topic_tags);
    	to_del_tags.removeAll(new_topic_tags);    	
    	
    	for(Long tag_id:to_del_tags){
    		TagTopicRel rel = new TagTopicRel();
    		rel.setTagId(tag_id);
    		rel.setTopicId(topic.getId());
    		this.topicMapper.delTagTopicRel(rel);
    	}
    	
    	to_add_tags.addAll(new_topic_tags);
    	to_add_tags.removeAll(original_topic_tags);
		
    	
    	for(Long tag_id:to_add_tags){
    		TagTopicRel rel = new TagTopicRel();
    		rel.setTagId(tag_id);
    		rel.setTopicId(topic.getId());
    		if(this.topicMapper.checkTopicIsInTag(rel)==0){
    			this.topicMapper.addTagTopicRel(rel);
    		}   		
    	}
    	    	
		int count=topicMapper.editTopic(topic);
		if(count<1){
			res.setCode(-2);
			res.setData(false);
			res.setMsg("更新Topic"+topic.getId()+"失败！");
		}else{
			res.setData(true);
		}
		return res;
	}

	@Override
	public Res<Boolean> CheckTopicIsMine(Long id,Long userId) {
		Res<Boolean> res=new Res<Boolean>();
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("id", id);
		map.put("userId", userId);
		int count=topicMapper.checkTopicIsMine(map);
		if(count<1){
			res.setMsg("Topic:"+id+"不是用户:"+userId);
			res.setData(false);
		}else{
			res.setData(true);
		}
		return res;
	}

	/**
	 * 
	 */
	@Override
	public ResPager<List<Topic>> QueryTopicsPagely(Integer status, Integer filter,String title,
			Integer page, Integer pageSize) {
		ResPager<List<Topic>> res=new ResPager<List<Topic>>();
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("status", status);
		map.put("filter", filter);
		map.put("page", page);
		map.put("pageSize", pageSize);
		map.put("title",title);
		List<Topic> topics=topicMapper.queryTopicsPagely(map);
		res.setData(topics);
		int counts=topicMapper.queryTopicCounts(map);
		res.setCounts(counts);
		res.setPage(page);
		res.setPageSize(pageSize);
		return res;
	}

	@Override
	public ResPager<List<TopicComment>> QueryTopicCommentsPagely(Long id,
			Long userId, Integer page, Integer pageSize) {
		ResPager<List<TopicComment>> res=new ResPager<List<TopicComment>>();
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("id", id);
		map.put("userId", userId);
		map.put("page", page);
		map.put("pageSize", pageSize);
 
		List<TopicComment> comments=topicMapper.queryTopicCommentsPagely(map);
		res.setData(comments);
		int counts=topicMapper.queryTopicCommentsCounts(map);
		res.setCounts(counts);
		res.setPage(page);
		res.setPageSize(pageSize);
		return res;
	}

	@Transactional
	@Override
	public Res<Long> AddTopicLike(TopicLike like) {
		Res<Long> res=new Res<Long>();
		like.setCreatedAt(new Date());
		int count=topicMapper.addTopicLike(like);
		if(count<1){
			res.setCode(-2);
			res.setMsg("添加Topic赞失败!");
		}
		res.setData(like.getId());
		return res;
	}

	@Transactional
	@Override
	public Res<Boolean> DelTopicLike(TopicLike like) {
		Res<Boolean> res=new Res<Boolean>();
		int count=topicMapper.delTopicLike(like);
		if(count<1){
			res.setCode(-2);
			res.setMsg("删除Topic赞失败!");
			res.setData(false);
		}else{
			res.setData(true);
		}		
		return res;
	}

	@Transactional
	@Override
	public Res<Long> AddTopicComment(TopicComment comment) {
		Res<Long> res=new Res<Long>();
		comment.setCreatedAt(new Date());
		String original_content_html=comment.getContentHtml();
		StringBuffer sb = new StringBuffer();  
		Matcher m_out=Pattern.compile("@[^\\)]*\\)").matcher(comment.getContentHtml());
		List<Long> comment_to_userids=new ArrayList<Long>();
		Res<Topic> res_topic= this.QueryTopicById(comment.getTopicId(), null);
		Long topic_userId=null;
		if(res_topic.getCode()==0){
			topic_userId=res_topic.getData().getUserId();//存储该评论的Topic是谁的，以免@的时候发送两次
		}
		//comment_to_userids.add(e)
		while(m_out.find()){
			String _at_str=m_out.group(0);//@xxx(xx)
	
			String _userid=_at_str.substring(_at_str.indexOf('(')+1, _at_str.indexOf(')'));
			String _hypocorismName=_at_str.substring(1, _at_str.indexOf('('));
 
			if(StringUtils.isNumeric(_userid)){
				//ResponseEntity<User> entity_user=restTemplate.postForEntity(SVW_BBS_USER_HOST+"/user-service/query-user-by-id/"+_userid, null, User.class);
				Res<User> user_res=userFeign.QueryUserById(Long.parseLong(_userid));
				if(user_res.getCode()!=0){
					continue;
				}
				User u=user_res.getData();
				if(u!=null && _hypocorismName.equals(u.getNickName())){
					m_out.appendReplacement(sb, "<a href=\"/user/home?id="+u.getId()+"\">@"+u.getNickName()+"</a>");					
					if(!comment_to_userids.contains(u.getId()))
						comment_to_userids.add(u.getId());
				}
			}
		}
		if(sb.length()>0){
			m_out.appendTail(sb); 
			comment.setContentHtml(sb.toString());
		}else{
			comment.setContentHtml(original_content_html);
		}
		
		int count=topicMapper.addTopicComment(comment);
		if(count<1){
			res.setCode(-2);
			res.setMsg("添加Topic评论失败!");
		}else{
			//表示添加成功
			if(comment_to_userids.size() > 0){
				for(Long _u_id:comment_to_userids){
					if(_u_id != topic_userId){
						TopicCommentTo _commentTo=new TopicCommentTo();
						_commentTo.setCommentId(comment.getId());
						_commentTo.setUserId(_u_id);
						topicMapper.addTopicCommentTo(_commentTo);
					}					
				}
			}
		}
		topicMapper.editTopicCommentCount(comment.getTopicId());//修改评论数量
		res.setData(comment.getId());
		return res;
	}

	@Transactional
	@Override
	public Res<Boolean> DelTopicComment(Long id) {
		Res<Boolean> res=new Res<Boolean>();
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("id", id);
		map.put("status",-1);
		TopicComment comment=topicMapper.queryTopicComment(map);
		int count=topicMapper.updateTopicCommentStatus(map);
		if(count<1){
			res.setCode(-2);
			res.setMsg("修改status状态失败!");
			res.setData(false);
		}else{
			res.setData(true);			
			topicMapper.editTopicCommentCount(comment.getTopicId());//修改评论数量
		}
		return res;
	}

	@Transactional
	@Override
	public Res<Long> AddTopicCommentLike(TopicCommentLike like) {
		Res<Long> res=new Res<Long>();
		like.setCreatedAt(new Date());
		int count=topicMapper.addTopicCommentLike(like);
		if(count<1){
			res.setCode(-2);
			res.setMsg("添加Topic评论赞失败!");
		}
		topicMapper.editTopicCommentLikeCount(like.getCommentId());//更新topic评论的点赞数量
		res.setData(like.getId());
		return res;
	}

	@Transactional
	@Override
	public Res<Boolean> DelTopicCommentLike(TopicCommentLike like) {
		Res<Boolean> res=new Res<Boolean>();
		int count=topicMapper.delTopicCommentLike(like);
		if(count<1){
			res.setCode(-2);
			res.setMsg("删除Topic评论赞失败!");
			res.setData(false);
		}else{
			res.setData(true);
			topicMapper.editTopicCommentLikeCount(like.getCommentId());
		}
		return res;
	}


	@Override
	public Res<Boolean> CheckTopicLikeExist(TopicLike like) {
		Res<Boolean> res=new Res<Boolean>();
		int count=topicMapper.checkTopicLikeExist(like);
		if(count<1){
			res.setData(false);
		}else{
			res.setData(true);
		}
		return res;
	}

	@Override
	public Res<Boolean> CheckTopicCommentLikeExist(TopicCommentLike like) {
		Res<Boolean> res=new Res<Boolean>();
		int count=topicMapper.checkTopicCommentLikeExist(like);
		if(count<1){
			res.setData(false);
		}else{
			res.setData(true);
		}
		return res;
	}

	@Override
	public Res<TopicComment> QueryTopicComment(Long id, Long userId) {
		Res<TopicComment> res =new Res<TopicComment>();
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("userId", userId);
		map.put("id", id);
		res.setData(topicMapper.queryTopicComment(map));
		return res;
	}

	@Override
	public Res<List<User>> QueryTopicCommentedUsers(Long id, Long userId) {
		Res<List<User>> res=new Res<List<User>>();
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("id", id);
		map.put("userId", userId);
		List<User> users=topicMapper.queryTopicCommentedUsers(map);
		res.setData(users);
		return res;
	}

	@Override
	public Res<Boolean> EditTopicStatus(Long id, Integer status) {
		Res<Boolean> res=new Res<Boolean>();
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("id", id);
		map.put("status", status);
		int count=topicMapper.editTopicStatus(map);
		if(count<1){
			res.setCode(-2);
			res.setMsg("编辑Topic状态错误！");
			res.setData(false);
		}else{
			res.setData(true);
		}
		
		return res;
	}

	@Override
	public Res<Boolean> EditTopicViewCount(Long id) {
		Res<Boolean> res=new Res<Boolean>();
		int count=topicMapper.editTopicViewCount(id);
		if(count<1){
			res.setCode(-2);
			res.setMsg("修改Topic里的viewCount失败！");
		}
		return res;
	}

	@Override
	public Res<Integer> QueryTopicCommentIndex(Long topicId, Long commentId) {
		Res<Integer> res=new Res<Integer>();
		Map<String,Object> map= new HashMap<String,Object>();
		map.put("topicId", topicId);
		map.put("commentId", commentId);
		int count =topicMapper.checkTopicIsOwnComment(map);
		if(count<1){
			res.setCode(-2);
			res.setMsg("该评论不属于Topic");
			return res;
		}		
		res.setData(topicMapper.queryTopicCommentIndex(map));
		return res;
		
	}

	@Transactional
	@Override
	public Res<Integer> EditReaded(Long userId,Integer type, Long id, Boolean readed) {
		Res<Integer> res=new Res<Integer>();
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("id", id);
		map.put("readed", readed);
		map.put("userId", userId);
		int count=0;
		int auth=0;
		int readed_status=0;
		if(type==1){
			auth=topicMapper.checkUserHasAuthEditTopicCommentReaded(map);
			if(auth>0){
				readed_status=topicMapper.queryTopicCommentReadedStatus(map);
				if(readed_status<1){
					//如果状态不一致才需要修改
					count=topicMapper.editTopicCommentReaded(map);
				}
				
			}
		}else if(type==2){
			auth=topicMapper.checkUserHasAuthEditTopicCommentToReaded(map);
			if(auth>0){
				readed_status=topicMapper.queryTopicCommentToReadedStatus(map);
				if(readed_status<1){
					count=topicMapper.editTopicCommentToReaded(map);
				}				
			}		
		}else if(type==3){
			auth=topicMapper.checkUserHasAuthEditTopicLikeReaded(map);
			if(auth>0){
				readed_status=topicMapper.queryTopicLikeReadedStatus(map);
				if(readed_status<1){
					count=topicMapper.editTopicLikeReaded(map);
				}				
			}			
		}else if(type==4){
			auth=topicMapper.checkUserHasAuthEditTopicCommentLikeReaded(map);
			if(auth>0){
				readed_status=topicMapper.queryTopicCommentLikeReadedStatus(map);
				if(readed_status<1){
					count=topicMapper.editTopicCommentLikeReaded(map);
				}				
			}			
		}
		
		if(count<1){
			if(readed_status>0){
				res.setData(-1);
				res.setMsg("Read已经为该状态，不需要修改！");
			}else{
				res.setCode(-2);
				res.setData(0);
				res.setMsg("设置Readed失败！");
			}
			
		}else{
			res.setData(1);
		}
		return res;
	}

	@Override
	public Res<Topic> QueryTopicByCommentId(Long id, Long userId) {
		Res<Topic> res=new Res<Topic>();		
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("id", id);
		map.put("userId", userId);
		res.setData(topicMapper.queryTopicByCommentId(map));
		return res;
	}

	@Override
	public Res<Boolean> CheckTopicIsDeleted(Long id) {
		Res<Boolean> res=new Res<Boolean>();
		Map<String,Object> map =new HashMap<String,Object>();
		map.put("id", id);
		map.put("status", -1);
		int counts=topicMapper.checkTopicIsDeleted(map);
		if(counts>0){
			res.setData(true);
		}else{
			res.setData(false);
		}
		return res;
	}

	@Transactional
	@Override
	public Res<Integer> EditReadedMulti(Long userId, String strToEdit,
			Boolean readed) {
		Res<Integer> res=new Res<Integer>();
		String[] sptStrToEdit=strToEdit.split(",");
		for(String s:sptStrToEdit){
			String[] spt= s.split("|");
			if(spt.length!=2) continue;
			Res<Integer> res_in=this.EditReaded(userId, Integer.parseInt(spt[0]), Long.parseLong(spt[1]), readed);			
		}		
		return res;
	}

	@Override
	public Res<List<Tag>> QueryTopicTags(long userId,long topicId) {
		Res<List<Tag>> res = new Res<List<Tag>>();
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("topicId", topicId);
		map.put("userId", userId);
		List<Tag> tags = this.topicMapper.queryTopicTags(map);
		res.setData(tags);
		return res;
	}

	
}
