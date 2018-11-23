package org.spring.china.base.feign.fallback;

import java.util.List;

import org.spring.china.base.common.Res;
import org.spring.china.base.common.ResPager;
import org.spring.china.base.feign.TopicFeign;
import org.spring.china.base.pojo.*;
import org.springframework.stereotype.Component;

@Component
public class TopicFeignFallback implements TopicFeign {

	@Override
	public Res<List<Category>> QueryAllCategories() {
		Res<List<Category>> res=new Res<List<Category>>();
		res.setCode(-1);
		res.setMsg("查询AllCategory错误！");
		return res;
	}

	@Override
	public Res<Long> AddTopic(Topic topic) {
		Res<Long> res =new Res<Long>();
        res.setCode(-1);
        res.setMsg("添加Topic失败！");
        return res;
	}

	@Override
	public Res<Topic> QueryTopicById(Long id, Long userId) {
		Res<Topic> res =new Res<Topic>();
		res.setCode(-1);
		res.setMsg("查询Topic失败！");
		return res;
	}

	@Override
	public Res<Boolean> EditTopic(Topic topic) {
		Res<Boolean> res =new Res<Boolean>();
        res.setCode(-1);
        res.setMsg("编辑Topic失败！");
        return res;
	}

	@Override
	public Res<Boolean> CheckTopicIsMine(Long id, Long userId) {
		Res<Boolean> res=new Res<Boolean>();
		res.setCode(-1);
		res.setMsg("CheckTopicIsMine云新失败！");
		return res;
	}

	@Override
	public ResPager<List<Topic>> QueryTopicsPagely(Integer status, Integer filter, String title,
			Integer page, Integer pageSize) {
		ResPager<List<Topic>> res=new ResPager<List<Topic>>();
		res.setCode(-1);
		res.setMsg("获取topics错误！");
		return res;
	}

	@Override
	public ResPager<List<TopicComment>> QueryTopicCommentsPagely(Long id,
			Long userId, Integer page, Integer pageSize) {
		ResPager<List<TopicComment>> res=new ResPager<List<TopicComment>>();
		res.setCode(-1);
		res.setMsg("获取topic comment错误！");
		return res;
	}

	@Override
	public Res<Long> AddTopicLike(TopicLike like) {
		Res<Long> res=new Res<Long>();
		res.setCode(-1);
		res.setMsg("添加Topic赞错误!");
		return res;
	}

	@Override
	public Res<Boolean> DelTopicLike(TopicLike like) {
		Res<Boolean> res=new Res<Boolean>();
		res.setCode(-1);
		res.setMsg("删除Topic赞错误!");
		return res;
	}

	@Override
	public Res<Long> AddTopicComment(TopicComment comment) {
		Res<Long> res=new Res<Long>();
		res.setCode(-1);
		res.setMsg("添加Topic评论错误!");
		return res;
	}

	@Override
	public Res<Boolean> DelTopicComment(Long id) {
		Res<Boolean> res=new Res<Boolean>();
		res.setCode(-1);
		res.setMsg("删除Topic评论错误!");
		return res;
	}

	@Override
	public Res<Long> AddTopicCommentLike(TopicCommentLike like) {
		Res<Long> res=new Res<Long>();
		res.setCode(-1);
		res.setMsg("添加Topic评论赞错误!");
		return res;
	}

	@Override
	public Res<Boolean> DelTopicCommentLike(TopicCommentLike like) {
		Res<Boolean> res=new Res<Boolean>();
		res.setCode(-1);
		res.setMsg("删除Topic评论赞错误!");
		return res;
	}


	@Override
	public Res<Boolean> CheckTopicLikeExist(TopicLike like) {
		Res<Boolean> res=new Res<Boolean>();
		res.setCode(-1);
		res.setMsg("查询是否给Topic点赞失败!");
		return res;
	}

	@Override
	public Res<Boolean> CheckTopicCommentLikeExist(TopicCommentLike like) {
		Res<Boolean> res=new Res<Boolean>();
		res.setCode(-1);
		res.setMsg("查询是否给Topic评论点赞失败!");
		return res;
	}

	@Override
	public Res<TopicComment> QueryTopicComment(Long id, Long userId) {
		Res<TopicComment> res=new Res<TopicComment>();
		res.setCode(-1);
		res.setMsg("查询Topic评论错误！");
		return res;
	}

	@Override
	public Res<List<User>> QueryTopicCommentedUsers(Long id, Long userId) {
		Res<List<User>> res=new Res<List<User>>();
		res.setCode(-1);
		res.setMsg("查询Topic评论过的用户错误!");
		return res;
	}

	@Override
	public Res<Boolean> EditTopicStatus(Long id, Integer status) {
		Res<Boolean> res=new Res<Boolean>();
		res.setCode(-1);
		res.setMsg("编辑Topic状态失败！");
		return res;
	}

	@Override
	public Res<Boolean> EditTopicViewCount(Long id) {
		Res<Boolean> res=new Res<Boolean>();
		res.setCode(-1);
		res.setMsg("编辑Topic的查看次数失败！");
		return res;
	}

	@Override
	public Res<Integer> QueryTopicCommentIndex(Long topicId, Long commentId) {
		Res<Integer> res=new Res<Integer>();
		res.setCode(-1);
		res.setMsg("获取评论的INDEX熔断错误!");
		return res;
	}

	@Override
	public Res<Integer> EditReaded(Long userId,Integer type, Long id, Boolean readed) {
		Res<Integer> res=new Res<Integer>();
		res.setCode(-1);
		res.setMsg("设置Readed状态熔断错误!");
		return res;
	}

	@Override
	public Res<Topic> QueryTopicByCommentId(Long id, Long userId) {
		Res<Topic> res=new Res<Topic>();
		res.setCode(-1);
		res.setMsg("通过TopicCommentId来获取Topic发生熔断错误！");
		return res;
	}

	@Override
	public Res<Boolean> CheckTopicIsDeleted(Long id) {
		Res<Boolean> res =new Res<Boolean>();
		res.setCode(-1);
		res.setMsg("判断Topic是否被删除熔断错误！");
		return res;
	}

	@Override
	public Res<Integer> EditReadedMulti(Long userId, String strToEdit,
			Boolean readed) {
		Res<Integer> res=new Res<Integer>();
		res.setCode(-1);
		res.setMsg("设置多个Readed状态熔断错误！");
		return res;
	}

	

 

	@Override
	public Res<Long> AddTagTopicRel(TagTopicRel rel) {
		Res<Long> res=new Res<Long>();
		res.setCode(-1);
		res.setMsg("添加TagTopicRel熔断错误！");
		return res;
	}

	@Override
	public Res<Boolean> DelTagTopicRel(TagTopicRel rel) {
		Res<Boolean> res=new Res<Boolean>();
		res.setCode(-1);
		res.setMsg("删除TagTopicRel熔断错误！");
		return res;
	}

	@Override
	public Res<List<Tag>> QueryTopicTags(long userId, long topicId) {
		Res<List<Tag>> res = new Res<List<Tag>>();
		res.setCode(-1);
		res.setMsg("查询Topic的所有标签错误！");
		return res;
	}


}
