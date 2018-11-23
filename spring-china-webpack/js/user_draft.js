

import $ from 'jquery';
import '../libs/plugins/bootstrap/js/bootstrap.min';
import webSocket from './common/websocket';
import '../libs/plugins/pagination/jquery.pagination';
import layer from '../libs/plugins/layer/layer';
import topic from './common/topic';
import './common/extends';

var user_draft=function(){
	
	var hidden_topic_draft_page=1;
	var hidden_topic_draft_perPageCount=20;
	var hidden_topic_draft_counts=0;
	var hidden_userId=0;
	var hashchanged_executed=false;
	var hash_page="";
	
	
	HashChangeEvent.Fire=function(){
		hash_page = window.location.hash.substr(1, location.hash.length - 1);
		var is_clicked=false;
		$('#topic-drafts-pagination a').each(function(i,o){
			if($(o).html()==hash_page){
				$(o).trigger('click');		
				is_clicked=true;
			}
		});
		
		if(!is_clicked){
			topic.queryUserTopicsByUserIdPagely({
				userId:hidden_userId,
				status:0,
				page:hash_page,
				pageSize:hidden_topic_draft_perPageCount,
				cb:function(html){
					$('#topic-drafts-container .media').remove();
	        		$(html).insertBefore('#topic-drafts-pagination');
				}
			});
		}
		hashchanged_executed=true;
	};
	
	$(function(){
		hidden_topic_draft_page=$('#hidden_topic_draft_page').val();
		hidden_topic_draft_perPageCount=$('#hidden_topic_draft_perPageCount').val();
		hidden_topic_draft_counts=$('#hidden_topic_draft_counts').val();
		hidden_userId=$('#hidden_userId').val();
 
 
		
		$("#topic-drafts-pagination").pagination(hidden_topic_draft_counts, {
			link_to:'javascript:;',
            current_page: (hidden_topic_draft_page-1),
            prev_text: "上页",
            next_text: "下页",
            num_edge_entries: 2,
            num_display_entries: 10,
            items_per_page: hidden_topic_draft_perPageCount,
            callback: function (page_index, jq) {
            	
            	topic.queryUserTopicsByUserIdPagely({
    				userId:hidden_userId,
    				status:0,
    				page:page_index+1,
    				pageSize:hidden_topic_draft_perPageCount,
    				cb:function(html){
    					$('#topic-drafts-container .media').remove();
                		$(html).insertBefore('#topic-drafts-pagination');
    				}
    			});
            	
            	window.location.hash=(page_index+1);
            }
        });
		
		if(hashchanged_executed==false){
			hash_page = window.location.hash.substr(1, location.hash.length - 1);
			if(hash_page!='') {
				HashChangeEvent.Fire();
			}				
			else{
				topic.queryUserTopicsByUserIdPagely({
    				userId:hidden_userId,
    				status:0,
    				page:1,
    				pageSize:hidden_topic_draft_perPageCount,
    				cb:function(html){
    					$('#topic-drafts-container .media').remove();
                		$(html).insertBefore('#topic-drafts-pagination');
    				}
    			});
			}
				
			
		}
	});
	
	return {
		
	};
}();
