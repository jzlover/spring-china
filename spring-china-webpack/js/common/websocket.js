import $ from 'jquery';
import SockJS from '../../libs/plugins/socket/sockjs.min';
import  '../../libs/plugins/socket/stomp.min';
import '../../libs/plugins/toast/jquery.toast.min';
import CommonAjax from './helper';
import utils from './utils';
import Form from './form';

var webSocket=function(){
	var stompClient = null;
	
	function connect(){
		var socket = new SockJS('/endpointMsg');
        stompClient = Stomp.over(socket);
        stompClient.connect({}, function (frame) {
            console.log('Connected:' + frame);
            stompClient.subscribe('/user/msg/getResponse', function (response) {
            	var msg=JSON.parse(response.body);
            	if(msg.msgType>=1 && msg.msgType<=4){
            		var _counts=parseInt($('#nav-msg-comment-count').html());
            		$('#nav-msg-comment-count').html(++_counts);
            		
            		$.toast({
            		    text: '<img alt="头像" class="avatar" style="width:20px;height:20px;" src="/public-resource/download-user-avatar/'+msg.user.id+'/small/jpg"/> '+
            		          '<a href="/user/home?id='+msg.user.id+'"/>'+msg.user.nickName+'</a>' +
	            		    function(){
        						var _html='';
        						if(msg.msgType==1){
        							_html='<a href="/topic/show/'+msg.topicId+'?cId='+msg.cId+'&rId='+msg.rId+'&type='+msg.msgType+'">评论了你的话题！</a>';
        						}else if(msg.msgType==2){
        							_html='<a href="/topic/show/'+msg.topicId+'?cId='+msg.cId+'&rId='+msg.rId+'&type='+msg.msgType+'">@了你！</a> ';
        						}else if(msg.msgType==3){
        							_html='<a href="/topic/show/'+msg.topicId+'?rId='+msg.rId+'&type='+msg.msgType+'">给你的话题点赞了！</a>';
        						}else {
        							_html='<a href="/topic/show/'+msg.topicId+'?cId='+msg.cId+'&rId='+msg.rId+'&type='+msg.msgType+'">给你的评论点赞了！</a>';
        						}
        						return _html;
        					}()
            		    
            		});           		
            	}else if(msg.msgType==10){
            		//Private Msg
            		
            		//判断 Layer 是否打开
            		if($('#dlg-chat').length>0){
            			//表示已经打开了
            			//float:left;width:284px;margin:5px;word-break: break-all;
            			if($('#msg-wrapper .msg-user #chat-user-'+msg.user.id).length==0){
            				//表示没有该用户，则添加
            				$('#msg-wrapper .msg-user').append(
	                				'<div id="chat-user-'+msg.user.id+'" class="chat-user" onclick="utils.LoadUserMsg(this,'+msg.user.id+');">'+
	                					'<img src="'+msg.user.avatarSmall+'" style="width:35px;height:35px;border-radius:50%;"/>'+
	                					'<label style="margin-left:5px;">'+msg.user.nickName+'</label>'+
	                					'<span class="num badge" style="margin:8px;float:right;">1</span>'+
	                				'</div>');
            			}else{
            				if(webSocket.sendToUserId==msg.user.id){
            					//如果当前打开了正在聊天的窗口
            					$('#dlg-chat .msg-private').append(
                    					'<div style="padding:2px;" id="msg-private-'+msg.id+'" data-id="'+msg.id+'" data-time="'+msg.createdAt+'">'+
                    						'<img src="'+msg.user.avatarSmall+'" style="float:left;width:35px;height:35px;border-radius:50%;"/>'+
                    						'<div class="bubble clearfix">'+
                    							'<span class="triangle"></span>'+
                    							'<div class="article">'+msg.msgContent+'</div>'+
                    						'</div>'+           						
                    					'</div>');
                    			$('#msg-wrapper .msg-private').scrollTop($('#msg-wrapper .msg-private')[0].scrollHeight);
                    			
                    			//如果当前正在聊天，则readed设置为1
                    			CommonAjax.postJSON('/user-ajax/edit-user-msg-readed-multi/'+msg.id, '',
		        	                    function (data) {
		        							Form.defaultCallback(data,{
		        				                success:function(res){
		        				                	
		        				                },
		        				                error:function(res){
		        				                	layer.msg('操作失败！',{
		        			    						icon:2,
		        			    						time:1000
		        			    					});
		        				                }
		        				            });
		        	                    });
            				}else{
            					var _pre_msg_count=$('#chat-user-'+msg.user.id+' .num').html();
            					if(_pre_msg_count!=''){
            						_pre_msg_count=parseInt(_pre_msg_count);
            					}else{
            						_pre_msg_count=0;
            					}
            					_pre_msg_count++;
            					$('#chat-user-'+msg.user.id+' .num').html(_pre_msg_count);
            				}    				
            			}
            			
            		}else{
            			//表示没有打开聊天窗口
            			if(webSocket.chatTmr==null){
             
            				webSocket.chatTmr=setInterval(function(){
            					 if($('#i-chat .c').hasClass('on')){
            						 $('#i-chat .c').removeClass('on off').addClass('off');
            					 }else{
            						 $('#i-chat .c').removeClass('on off').addClass('on');
            					 }
            				},500);
            			}
            		}
            	}
            })
        });
	}
	
	function disconnect() {
        if (stompClient != null) {
            stompClient.disconnect();
        }
        console.log('Disconnected');
    }
	
	$(function(){
		$('#i-chat').click(function () {
            utils.showChatDialog(0,0,0,true);
        });

		$(document).on('click','.chat-user',function () {
			var userId=$(this).attr('data-id');
            utils.LoadUserMsg(this,userId);
        });

		$(document).on('click','.load-more-chat-msg',function () {
            utils.LoadMoreChatMsg(this);
        });

		$(document).on('click','#btn-send-chat-msg',function () {
            webSocket.sendPrivateMsgLayer(this);
        });

		if($('#hiddenUserName').val()!=""){
			connect();
		}
		
		$(document).on('keypress','#msg-wrapper .txt',function(e){
			if(e.which == 13 || e.which == 10) {
				$('#msg-wrapper .btn-send').trigger('click');
			}
		});
		
		if(parseInt($('#hiddenPrivateMsgCount').val())>0){
			if(webSocket.chatTmr==null){
	             
				webSocket.chatTmr=setInterval(function(){
					 if($('#i-chat .c').hasClass('on')){
						 $('#i-chat .c').removeClass('on off').addClass('off');
					 }else{
						 $('#i-chat .c').removeClass('on off').addClass('on');
					 }
				},500);
			}
		}
		
	});
	
	return {
		sendToUserId:null,
		
		chatTmr:null,
		
		sendMsgToUser:function(sendObject){
			stompClient.send("/send-msg-to-user", {}, JSON.stringify(sendObject));
		},
		sendPrivateMsgLayer:function(elm){
			var me=this;
			var userId=me.sendToUserId;
			var msg_content_wrapper=$(elm).parentsUntil('.msg-content-wrapper').first();
			var txt=$(msg_content_wrapper).find('.txt').first();
			if($.trim($(txt).val())==''){
				layer.msg('请输入内容！',{
					icon:2,
					time:1000
				});
				return;
			}
			var json_msg={
				toUserId:userId,
				type:10,
				content:$(txt).val(),
				contentHtml:$(txt).val()
			};
			//add-user-msg
			CommonAjax.postJSON('/user-ajax/add-user-msg', JSON.stringify(json_msg),
	                    function (data) {
							Form.defaultCallback(data,{
				                success:function(res){
				                	var _avatarsmall=$('#hiddenAvatarSmall').val();
				                	$('#dlg-chat .msg-private').append(
			            					'<div style="padding:2px;" id="msg-private-'+data.data+'" data-id="'+data.data+'">'+
			            						'<img src="'+_avatarsmall+'" style="float:right;width:35px;height:35px;border-radius:50%;"/>'+
			            						'<div class="bubble clearfix fr">'+
			            							'<span class="triangle" style="border-left-color:rgb(160,232,88);"></span>'+
			            							'<div class="article" style="background-color:rgb(160,232,88) !important;color:black;">'+$(txt).val()+'</div>'+
			            						'</div>'+
			            					'</div>');
				                	$(txt).val('');
				                	$('#msg-wrapper .msg-private').scrollTop($('#msg-wrapper .msg-private')[0].scrollHeight);
				                },
				                error:function(res){
				                	layer.msg('发送失败！',{
			    						icon:2,
			    						time:1000
			    					});
				                }
				            });
	                    });
		}
	}
	
}();


export default webSocket;