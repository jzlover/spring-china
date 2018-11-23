
import $ from 'jquery';
import '../libs/plugins/bootstrap/js/bootstrap.min';
import webSocket from './common/websocket';
import '../libs/plugins/pagination/jquery.pagination';
import layer from '../libs/plugins/layer/layer';
import CommonAjax from './common/helper';

import Form from './common/form';
import './common/extends';

var user_msg_comment=function(){
 
	var PER_PAGE_COUNT=20;
	var msgCommentCounts=0;
	var hash_page="";
	var hashchanged_executed=false;
	var showType=0;
	
	function queryUserMsgPagely(page){
		var _json_pager={
			page:page,
            pageSize:PER_PAGE_COUNT
		};
		var loading = layer.load();
		CommonAjax.postJSON('/user-ajax/query-user-related-comment-pagely/'+showType,JSON.stringify(_json_pager),function(res){
			layer.close(loading);
			Form.defaultCallback(res,{
                success:function(){
                	$('#msg-container').empty();
                	$.each(res.data,function(i,o){
                		$('#msg-container').append(
                    			'<div class="media" data-type="'+o.type+'" data-rId="'+o.rId+'">'+
    	                			'<div class="media-left">'+
    				      			'</div>'+
    				      			'<div class="media-body">'+
    					      			'<a href="/user/home?id='+o.userId+'">'+o.nickName+'</a> 在 '+   					      			
    					      			function(){
                    						var _html='';
                    						if(o.type==1){
                    							_html='<a href="/topic/show/'+o.id+'?cId='+o.cId+'&rId='+o.rId+'&type='+o.type+'">'+o.title+'</a> 中 ';
                    							_html+= '给你留言';                   							
                    						}else if(o.type==2){
                    							_html='<a href="/topic/show/'+o.id+'?cId='+o.cId+'&rId='+o.rId+'&type='+o.type+'">'+o.title+'</a> 中 ';
                    							_html+= '@了你';
                    						}else if(o.type==3){
                    							_html='<a href="/topic/show/'+o.id+'?rId='+o.rId+'&type='+o.type+'">'+o.title+'</a> 中 ';
                    							_html+= '赞了你的话题';
                    						}else if(o.type==4){
                    							_html='<a href="/topic/show/'+o.id+'?cId='+o.cId+'&rId='+o.rId+'&type='+o.type+'">'+o.title+'</a> 中 ';
                    							_html+= '赞了你的评论';
                    						}
                    						
                    						_html+='<span class="pull-right c-info">'+o.createdAtFormat+'</span>';//日期
                    						if(!o.readed){
                    							_html+='<button type="button" class="btn btn-xs btn-primary pull-right btn-set-readed">标为已读</button>';
                    						}
                    						
                    						if(o.type==1 || o.type==2 || o.type==4){
                    							_html+='<div>'+o.contentHtml+'</div>';
                    						}
                    						return _html;
                    					}()+
    				      			'</div>'+
                    			'</div>');
                	});               	
                },
                error:function(){
              
                }
            });
        });
	}
	
	HashChangeEvent.Fire=function(){
		hash_page = window.location.hash.substr(1, location.hash.length - 1);
		var is_clicked=false;
		$('#pagination a').each(function(i,o){
			if($(o).html()==hash_page){
				$(o).trigger('click');
				is_clicked=true;
			}
		});
		if(!is_clicked){
			queryUserMsgPagely(hash_page);
		}
		hashchanged_executed=true;
	};
	
	$(function(){
		
		msgCommentCounts=$('#hidden_msgCommentCount').val();
		showType=$('#hidden_showType').val();
		
		if(showType==''){
			showType=0;
		}
		
		$(document).on('click','#msg-container .media .btn-set-readed',function(){
			var elm=this;
			var rId=$(elm).parents('.media').first().attr('data-rId');
			var type=$(elm).parents('.media').first().attr('data-type');
			CommonAjax.postJSON('/user-ajax/edit-readed/'+type+'/'+rId+'/1', null, function (res) {
	                	Form.defaultCallback(res,{
			                success:function(){
			                	if(data.data==1){
			                		var _counts=parseInt($('#nav-msg-comment-count').html());
			                		$('#nav-msg-comment-count').html(--_counts);
			                		$(elm).remove();
			                		layer.msg('操作成功！',{
			    						icon:1,
			    						time:1000
			    					});
			                	}
			                },
			                error:function(){
		    					layer.msg('设置失败！',{
		    						icon:2,
		    						time:1000
		    					});
			                }
			            });
						
	                }); 
		});
		
		$("#pagination").pagination(msgCommentCounts, {
			link_to:'javascript:;',
            current_page: 0,
            prev_text: "上页",
            next_text: "下页",
            num_edge_entries: 2,
            num_display_entries: 10,
            items_per_page: PER_PAGE_COUNT,
            callback: function (page_index, jq) {           	
            	queryUserMsgPagely(page_index+1);
           
            	window.location.hash=(page_index+1);
            	 
            }
        });
		
		//queryUserMsgPagely(1);
		
		if(hashchanged_executed==false){
			hash_page = window.location.hash.substr(1, location.hash.length - 1);
			if(hash_page!='') {
				queryUserMsgPagely(hash_page);
			}				
			else
				queryUserMsgPagely(1);
			
		}
	});
	
}();