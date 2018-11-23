

import $ from 'jquery';
import '../libs/plugins/bootstrap/js/bootstrap.min';
import webSocket from './common/websocket';
import '../libs/plugins/pagination/jquery.pagination';
import layer from '../libs/plugins/layer/layer';
import CommonAjax from './common/helper';
import topic from './common/topic';

import Form from './common/form';
import './common/extends';


var user_edit_profile=function(){
	
	$(function(){
		
		$('#btn-edit').click(function(){
			var me=this;
			var _nickName=$('#nickName').val();
			var _gender=$('#gender').val();
			var _signature=$('#signature').val();
			
			$(me).attr('disabled','disabled');
			var user={
				nickName:_nickName,
				gender:_gender,
				signature:_signature
			};
			var loading = layer.load();
			CommonAjax.postJSON('/user-ajax/edit-profile', JSON.stringify(user),
                    function (res) {
						layer.close(loading);
						Form.defaultCallback(res,{
			                success:function(){
			                	layer.msg('编辑成功！',{
	        						icon:1,
	        						time:1000
	        					},function(){
	        						$(me).removeAttr('disabled');
	        						window.location.reload();
	        					});
			                },
			                error:function(){
			                	$(me).removeAttr('disabled');
	        					layer.msg('编辑失败！',{
	        						icon:2,
	        						time:1000
	        					});
			                }
			            });
 
                    });
		});
	});
}();