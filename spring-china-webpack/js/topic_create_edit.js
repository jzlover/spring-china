
import '../libs/plugins/simplemde/simplemde.min.css';

import $ from 'jquery';
import '../libs/plugins/bootstrap/js/bootstrap.min';
import webSocket from './common/websocket';
import WebUploader from '../libs/plugins/webuploader/webuploader.min';
import SimpleMDE from '../libs/plugins/simplemde/simplemde.min';
import layer from '../libs/plugins/layer/layer';
import '../libs/plugins/common/jquery.sticky-kit';
import '../libs/plugins/common/jquery.scrollto';
import Form from './common/form';
import CommonAjax from './common/helper';

var topic_create_edit=function(){
	var TYPE=null;
	var hidden_topicId=null;

	var simplemde = null;
	var uploader = null;

	$(function(){
		
		TYPE=$('#hidden_type').val();
		
		hidden_topicId=$('#hidden_topicId').val();

		simplemde=new SimpleMDE({ element: $("#simplemde")[0] });

		simplemde.codemirror.on("drop", function(editor,e){
		    
		});
		simplemde.codemirror.on("paste",function(editor,e){
			var files=e.clipboardData.files;
			if(files.length>0){
				uploader.addFiles(files[0]);
				uploader.upload();
			}
		});
		
		//创建uploader对象
		uploader=WebUploader.create({
			swf:'/scripts/plugins/webuploader/Uploader.swf',
			chunked: true,//分片上传
			server:'/public-resource/upload'
		});
		
		// 当有文件被添加进队列的时候
		uploader.on('fileQueued', function( file ) {
		    var $list=$("#list");
		    $list.append('<div id="' + file.id + '" class="item">' +
		        '<h4 class="info">' + file.name + '</h4>' +
		        '<p class="state">等待上传...</p>' +
		    '</div>' );
		});
		
		// 文件上传过程中创建进度条实时显示。
		uploader.on('uploadProgress', function( file, percentage ) {
		    var $li = $( '#'+file.id ),
		    $percent = $li.find('.progress .progress-bar');
		    // 避免重复创建
		    if ( !$percent.length ) {
		        $percent = $('<div class="progress progress-striped active">' +
		          '<div class="progress-bar" role="progressbar" style="width: 0%">' +
		          '</div>' +
		        '</div>').appendTo( $li ).find('.progress-bar');
		    }
		 
		    $li.find('p.state').text('上传中');
		    $percent.css('width', percentage * 100 + '%' );
		});
		
		uploader.on('uploadSuccess', function(file,data ) {
			Form.defaultCallback(data,{
                success:function(res){
                	//上传图片成功
                	simplemde.codemirror.replaceSelection('![未命名]('+res.data.url+')');
                },
                error:function(res){
					layer.msg('上传图片失败！',{
						icon:2,
						time:1000
					});
                }
            });
		});
		 
		uploader.on('uploadError', function( file ) {
		    $('#'+file.id ).find('p.state').text('上传出错');
		});
		 
		uploader.on( 'uploadComplete', function( file ) {
		    $('#'+file.id ).find('.progress').fadeOut();
		});
		
		
		$('.help-box').stick_in_parent({
            parent: $('body'),
            offset_top:10
        });
		
		
		//发布
		$('#publish').click(function(){
			
			var title=$.trim($('#topic-title').val());
			var content=simplemde.value();
			var sel_tags=[];
			
			if(title==''){
				layer.msg('话题标题不能为空！',{
					icon:2,
					time:1000
				});
				$('#topic-title').ScrollTo(800, -200);
				return;
			}
			if(title.length>50){
				layer.msg('话题标题最大不能超过50个字符！',{
					icon:2,
					time:1000
				});
				$('#topic-title').ScrollTo(800, -200);
				return;
			}
 
			if($.trim(content)==''){
				layer.msg('话题内容不能为空！',{
					icon:2,
					time:1000
				});
				$('#simplemde').ScrollTo(800, -200);
				return;
			}
			
			
			var sel_categoryId=$('#sel-category').val();
			
			$.each($('.tags > .tag.sel'),function(i,o){
				var tag_id=$(o).attr('data-id');
				sel_tags.push({
					id:tag_id
				});
			});
			
			var topic={
				status:1,//发布
				title:title,
				content:content,
				contentHtml:simplemde.markdown(content),
				categoryId:sel_categoryId,
				tags:sel_tags
			};
			
			if(TYPE=='edit'){
				topic['id']=hidden_topicId;
			}
			
			layer.open({
				skin: 'layui-layer-molv',
				icon:0,
				shadeClose:true,
				resize:false,
				content:function(){
					if(TYPE=='create'){
						return '确定发布？';
					}else if(TYPE=="edit"){
						return '确认编辑？';
					}
				}(),
				yes:function(index,layero){					
					$('#publish').attr('disabled','disabled');
					var loading = layer.load();
					CommonAjax.postJSON(function(){
						if(TYPE=='create'){
							return '/topic-ajax/add-topic';
						}else if(TYPE=='edit'){
							return '/topic-ajax/edit-topic';
						}
					}(), JSON.stringify(topic),
		                    function (data) {
								layer.close(loading);
								Form.defaultCallback(data,{
					                success:function(res){
					                	if(TYPE=='create'){
					                		layer.msg('发布成功！',{
				        						icon:1,
				        						time:1000
				        					},function(){
				        						window.location.href='/topic/show/'+res.data;
				        					});
					                	}else if(TYPE=='edit'){
					                		layer.msg('编辑成功！',{
				        						icon:1,
				        						time:1000
				        					},function(){
				        						window.location.href='/topic/show/'+hidden_topicId;
				        					});
					                	}
					                	
			        					layer.close(index);
					                },
					                error:function(res){
					                	$('#publish').removeAttr('disabled');
					                	if(TYPE=='create'){
					                		layer.msg('发布失败！',{
				        						icon:2,
				        						time:1000
				        					});
					                	}else{
					                		layer.msg('编辑失败！',{
				        						icon:2,
				        						time:1000
				        					});
					                	}
			        					
					                }
					            });
		      
		                    });
				}
			});
			
		});
		
		$('#save-to-draft').click(function(){
			var title=$.trim($('#topic-title').val());
			var content=simplemde.value();
			if(title==''){
				layer.msg('话题标题不能为空！',{
					icon:2,
					time:1000
				});
				$('#topic-title').ScrollTo(800, -200);
				return;
			}
			if(title.length>50){
				layer.msg('话题标题最大不能超过50个字符！',{
					icon:2,
					time:1000
				});
				$('#topic-title').ScrollTo(800, -200);
				return;
			}
 
			if($.trim(content)==''){
				layer.msg('话题内容不能为空！',{
					icon:2,
					time:1000
				});
				$('#simplemde').ScrollTo(800, -200);
				return;
			}
			
			
			var sel_categoryId=$('#sel-category').val();
			
			var topic={
				status:0,//草稿箱
				title:title,
				content:content,
				contentHtml:simplemde.markdown(content),
				categoryId:sel_categoryId
			};
			
			layer.open({
				skin: 'layui-layer-molv',
				icon:0,
				shadeClose:true,
				resize:false,
				content:'确认保存到草稿箱？',
				yes:function(index,layero){					
					$('#save-to-draft').attr('disabled','disabled');
					var loading = layer.load();
					CommonAjax.postJSON('/topic-ajax/add-topic',JSON.stringify(topic),
		                    function (data) {
								layer.close(loading);
								Form.defaultCallback(data,{
					                success:function(res){					            
				                		layer.msg('保存草稿箱成功！',{
			        						icon:1,
			        						time:1000
			        					},function(){
			        						window.location.href='/topic/show/'+res.data;
			        					});
					       
					                	
			        					layer.close(index);
					                },
					                error:function(res){
					                	$('#save-to-draft').removeAttr('disabled');
					               
				                		layer.msg('保存失败！',{
			        						icon:2,
			        						time:1000
			        					});
					               
			        					
					                }
					            });
		      
		                    });
				}
			});
			
		});
		
		
		//标签单击事件
		$('.tags > .tag').click(function(){
			var sel=$(this).hasClass('sel');
			if(sel){
				$(this).removeClass('sel');
			}else{
				$(this).removeClass('sel').addClass('sel');
			}
			
			
		});
		
	});
}();