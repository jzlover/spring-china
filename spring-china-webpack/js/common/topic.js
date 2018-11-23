import CommonAjax from './helper';
import layer from '../../libs/plugins/layer/layer';

var topic=function(){

	return {
		/*
		  userId：
		  status:Topic的状态，1：正常，-1：删除，0：草稿箱，
		  page:当前显示第几页，1...
		  pageSize:每页显示多少条
		  cb:回调函数，用户显示数据
		  userId,status,page,pageSize,cb
		 */
		queryUserTopicsByUserIdPagely:function(opts){
			var page_model={
			    status:opts.status || 1,
				page:opts.page || 1,
                pageSize:opts.pageSize || 20,
                tagId:opts.tagId
			};
			 
			var loading = layer.load();
			CommonAjax.postJSON('/user-ajax/query-user-topics-pagely/'+opts.userId, JSON.stringify(page_model),
	                function (res) {
						layer.close(loading);
					    var res_html='';
	    				$.each(res.data,function(i,o){
	    					res_html+='<div class="media">'+
	    			      			'<div class="media-left">'+
	    			      			'</div>'+
	    			      			'<div class="media-body">'+   	
	    			      				function(){
	    									var res_html='';
	    									if(o.isTop){
	    										res_html+= '<span class="hidden-xs label label-warning">置顶</span> ';
	    									}
	    									if(o.category.code=='C1'){
	    										res_html+='<span class="hidden-xs label label-default">原创</span> ';
	    									}else if(o.category.code=='C2'){
	    										res_html+='<span class="hidden-xs label label-default">分享</span> ';
	    									}else if(o.category.code=='C3'){
	    										res_html+='<span class="hidden-xs label label-default">回答</span> ';
	    									}
	    									$.each(o.tags,function(tag_i,tag_o){
	    										res_html+='<span class="badge">'+tag_o.title+'</span> ';
	    									});
	    									return res_html;
	    								}()+
	    			      				'<a href="/topic/show/'+o.id+'">'+o.title+'</a>'+
	    			      				'<div class="pull-right topic-info">'+
	    			      					'<span class="glyphicon glyphicon-comment" aria-hidden="true"></span> <span> '+o.commentCount+' </span>'+
	    			      					'<span class="glyphicon glyphicon-eye-open" aria-hidden="true"></span> <span> '+o.viewCount+' </span>'+
	    			      					'<span class="glyphicon glyphicon-thumbs-up" aria-hidden="true"></span> <span> '+o.likeCount+' </span>'+
	    			      					'<span class="glyphicon glyphicon-time" aria-hidden="true"></span> <span> '+o.createdAtFormat+' </span>'+
	    			      				'</div>'+
	    			      			'</div>'+
	    			      		'</div>';	    								
	    				});
	    				if(opts.cb){
	    					opts.cb(res_html);
	    				}
	                });
		}
	}
	
	
}();

export default topic;
