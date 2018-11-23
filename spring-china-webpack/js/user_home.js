
import $ from 'jquery';
import '../libs/plugins/bootstrap/js/bootstrap.min';
import webSocket from './common/websocket';
import '../libs/plugins/pagination/jquery.pagination';
import layer from '../libs/plugins/layer/layer';
import CommonAjax from './common/helper';
import topic from './common/topic';

import Form from './common/form';
import utils from './common/utils';
import './common/extends';

var user_home=function(){
	var hidden_topic_page=1;
	var hidden_topic_perPageCount=20;
	var hidden_topic_counts=0;
	var hidden_userId=0;
	var hidden_tagId=0;
	var hashchanged_executed=false;
	var hash_page="";
	
	var hidden_user_recent_activities_counts=0;
	
	var hidden_recent_activity_page=1;
	var hidden_recent_activity_perPageCount=20;
	var hidden_recent_activity_counts=0;
	
	var last_recent_activities_date=null;
	
 
	
	function queryUserRecentActivities(first_time){
		var page_model={
			page:1,
            pageSize:20,
			prevDate:last_recent_activities_date
		};
		
		var loading = layer.load();
		CommonAjax.postJSON('/user-ajax/query-user-recent-activities-pagely/'+hidden_userId, JSON.stringify(page_model),
                function (v) {
					layer.close(loading);
					if(v.data.length<1){
						if(!first_time){
							//表示没有更多了
							layer.msg('没有更多了！',{
								icon:2,
								time:1000
							});
						}
						
					}else{
						$.each(v.data,function(i,o){
	    					$('#recent-activities').append('<div class="media">'+
	    			      			'<div class="media-left">'+
	    			      			'</div>'+
	    			      			'<div class="media-body" style="width:100%;">'+   	
	    			      				'<div class="media-heading">'+
	    			      					function(){
	    										if(o.type==3){
	    											return '<a href="/topic/show/'+o.pId+'">'+o.title+'</a>';
	    										}else{
	    											return '<a href="/topic/show/'+o.id+'">'+o.title+'</a>';
	    										}
	    									}()+	    			      					
	    			      					'<div class="pull-right activity-info">'+
	    			      						'<span class="glyphicon glyphicon-time" aria-hidden="true"></span> <span>'+o.createdAtFormat+'</span>'+
	    			      					'</div>'+
	    			      				'</div>'+    			      				
	    			      				function(){
	    									if(o.type==1){
	    										return '<div>'+o.contentHtml+'</div>';
	    									}else if(o.type==2){
	    										return '<span class="glyphicon glyphicon-thumbs-up" aria-hidden="true"></span>';
	    									}else if(o.type==3){
	    										return '<span class="glyphicon glyphicon-thumbs-up" aria-hidden="true"></span>';
	    									}else{
	    										return '';
	    									}
	    								}()+
	    			      			'</div>'+
	    			      		'</div>');
	    					last_recent_activities_date=o.createdAt;
	    					
	    				});
					}
                });
	}
	
	HashChangeEvent.Fire=function(){
		hash_page = window.location.hash.substr(1, location.hash.length - 1);
		var is_clicked=false;
		$('#topic-pagination a').each(function(i,o){
			if($(o).html()==hash_page){
				$(o).trigger('click');		
				is_clicked=true;
			}
		});
		
		if(!is_clicked){
			topic.queryUserTopicsByUserIdPagely({
				userId:hidden_userId,
				status:1,
				page:hash_page,
				pageSize:hidden_topic_perPageCount,
				tagId:hidden_tagId,
				cb:function(html){
					$('#topics-container .media').remove();
	        		$(html).insertBefore('#topic-pagination');
				}
			});
		}
		hashchanged_executed=true;
	}
	
	$(function(){
		hidden_topic_page=$('#hidden_topic_page').val();
		hidden_topic_perPageCount=$('#hidden_topic_perPageCount').val();
		hidden_topic_counts=$('#hidden_topic_counts').val();
		hidden_userId=$('#hidden_userId').val();
		hidden_tagId=$('#hidden_tagId').val();
		hidden_user_recent_activities_counts=$('#hidden_user_recent_acvitivies_counts').val();
			
		hidden_recent_activity_page=$('#hidden_recent_activity_page').val();
		hidden_recent_activity_perPageCount=$('#hidden_recent_activity_perPageCount').val();
		hidden_recent_activity_counts=$('#hidden_recent_activity_counts').val();
		
		$("#topic-pagination").pagination(hidden_topic_counts, {
			link_to:'javascript:;',
            current_page: (hidden_topic_page-1),
            prev_text: "上页",
            next_text: "下页",
            num_edge_entries: 2,
            num_display_entries: 10,
            items_per_page: hidden_topic_perPageCount,
            callback: function (page_index, jq) {
            	topic.queryUserTopicsByUserIdPagely({
    				userId:hidden_userId,
    				status:1,
    				page:page_index+1,
    				pageSize:hidden_topic_perPageCount,
    				tagId:hidden_tagId,
    				cb:function(html){
    					$('#topics-container .media').remove();
    	        		$(html).insertBefore('#topic-pagination');
    				}
    			});
            	
            	window.location.hash=(page_index+1);
            }
        });
		
 
		$('#a-load-more').click(function(){
			queryUserRecentActivities(false);
		});
		
		
		
		if(hashchanged_executed==false){
			hash_page = window.location.hash.substr(1, location.hash.length - 1);
			if(hash_page!='') {
				HashChangeEvent.Fire();
			}				
			else{
				topic.queryUserTopicsByUserIdPagely({
					userId:hidden_userId,
					status:1,
					page:1,
					pageSize:hidden_topic_perPageCount,
					tagId:hidden_tagId,
					cb:function(html){
						$('#topics-container .media').remove();
	            		$(html).insertBefore('#topic-pagination');
					}
				});
			}
				
			
		}
		
		queryUserRecentActivities(true);
		
		
	});
	return{
	};
}();