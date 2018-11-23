

import $ from 'jquery';
import '../libs/plugins/bootstrap/js/bootstrap.min';
import webSocket from './common/websocket';
import '../libs/plugins/pagination/jquery.pagination';
import '../libs/plugins/cropper/cropper.min';
import layer from '../libs/plugins/layer/layer';
import CommonAjax from './common/helper';
import topic from './common/topic';

import Form from './common/form';
import './common/extends';


var user_edit_avatar=function(){
	var $cropper=null;
	var URL = window.URL || window.webkitURL;
	var uploadedImageURL;
	$(function(){
		var cropperOptions = {
		    aspectRatio: 1 / 1,
		    crop: function (e) {
		    }
		};
		$cropper=$('#avatarctrl > img').cropper(cropperOptions);
		
		$('#sel-avatar').change(function(){
			var files = this.files;
		    if (!$cropper.data('cropper')) {
		        return;
		    }
		    if (files && files.length) {
		        var file = files[0];
		        if (/^image\/\w+$/.test(file.type)) {
		            if (uploadedImageURL) {
		                URL.revokeObjectURL(uploadedImageURL);
		            }
		            uploadedImageURL = URL.createObjectURL(file);
		            $cropper.cropper('destroy').attr('src', uploadedImageURL).cropper(cropperOptions);
		            $(this).val('');
		        } else {
		            
		        }
		    }
		});
		
		$('#save-avatar').click(function(){
			var me=this;
			var image = $cropper.cropper('getCroppedCanvas').toDataURL('image/jpeg');
			var json_avatar={
				avatar:image	
			};
			var loading = layer.load();
			$(me).attr('disabled','disabled');
			CommonAjax.postJSON('/user-ajax/edit-avatar', JSON.stringify(json_avatar),
					function(data){
				layer.close(loading);
				Form.defaultCallback(data,{
	                success:function(res){
	                	layer.msg('编辑成功！',{
    						icon:1,
    						time:1000
    					},function(){
    						window.location.reload();
    					});
    					 
	                },
	                error:function(res){
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