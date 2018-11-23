

import $ from 'jquery';
import '../libs/plugins/bootstrap/js/bootstrap.min';
import webSocket from './common/websocket';
import '../libs/plugins/pagination/jquery.pagination';
import layer from '../libs/plugins/layer/layer';
import Form from './common/form';
import CommonAjax from './common/helper';
import utils from './common/utils';
import './common/extends';
import user from './common/user';

var user_tags=function () {

    function queryUserTagsPagely(page,pageSize) {
        var page_model={
            page:page,
            pageSize:pageSize
        };

        var loading = layer.load();
        CommonAjax.postJSON('/user-ajax/query-user-tags-pagely',JSON.stringify(page_model),function (res) {
            layer.close(loading);
            Form.defaultCallback(res, {
            	success:function(){
            		$.each(res.data,function(i,o){
            			$('#tb-user-tags > tbody').append(
            				'<tr data-id="'+o.id+'">'+
            					'<td ><a href="/user/home?tagId='+o.id+'" class="title">'+o.title+'</a></td>'+
            					'<td class="description">'+o.description+'</td>'+
            					'<td>'+
            						'<button class="btn btn-xs btn-danger pull-right btn-del" data-id="'+o.id+'">删除</button>'+
            						'<button class="btn btn-primary btn-xs pull-right btn-edit" data-id="'+o.id+'">编辑</button>'+
            					'</td>'+
            				'</tr>'
            			);
            		});
            	},
            	error:function(){
            		layer.msg('加载失败！',{
                        icon:2,
                        time:1000
                    });
            	}
            });
         
        });
    }

    $(function () {
        queryUserTagsPagely(1,20);

        $('#btn-add-user').click(function () {
            user.dlgAddUserTag();
        });

        $(document).on('click','.btn-edit',function () {
        	var elm=this;
			var id=$(this).attr('data-id');
            layer.open({
                type : 1,
                title : '编辑标签',
                skin : 'layui-layer-molv',
                shadeClose : true,
                resize : false,
                scrollbar : false,
                area : [ '400px', 'auto' ],
                btn : [ '确定' ],
                content : '<div class="p-10">' +
                '<div class="form-group">' +
                '<label for="tag-title">标签名称</label>' +
                '<input type="text" class="form-control" id="tag-title" placeholder="请输入标签名称">' +
                '</div>' +
                '<div class="form-group">' +
                '<label for="tag-description">标签说明</label>' +
                '<textarea class="form-control" id="tag-description" placeholder="请输入标签说明"></textarea>' +
                '</div>' +
                '</div>',
                success:function(layero,index){
                    var tag_title=$(elm).parents('tr').first().find('.title').html();
                    var tag_description=$(elm).parents('tr').first().find('.description').html();
                    $(layero).find('#tag-title').val(tag_title);
                    $(layero).find('#tag-description').val(tag_description);
                },
                yes : function(index, layero) {
                    var tag_id=$(elm).parents('tr').first().attr('data-id');
                    var tag_title = $(layero).find('#tag-title').val();
                    var tag_description = $(layero).find('#tag-description').val();
                    if (tag_title.length < 1) {
                        layer.msg('请输入标签名称！', {
                            icon : 2,
                            time : 1000
                        });
                        return false;
                    }

                    var model = {
                        id:parseInt(tag_id),
                        title : tag_title,
                        description : tag_description
                    };
                    var loading = layer.load();
                    CommonAjax.postJSON('/user-ajax/edit-tag', JSON.stringify(model),
                        function(res) {
                            layer.close(loading);
                            Form.defaultCallback(res, {
                                success : function() {
                                    layer.close(index);
                                    layer.msg('添加成功！', {
                                        icon : 1,
                                        time : 1000
                                    }, function() {
                                        location.reload();
                                    });
                                },
                                error : function() {
                                    layer.msg('添加失败！', {
                                        icon : 2,
                                        time : 1000
                                    });
                                }
                            });

                        });
                }
            });
        });

        $(document).on('click','.btn-del',function () {
        	var elm=this;
        	var id=$(elm).attr('data-id');
            layer.open({
                type:0,
                skin: 'layui-layer-molv',
                icon:2,
                shadeClose:true,
                resize:false,
                content:'确定删除？',
                yes:function(index,layero){
                    var loading = layer.load();
                    CommonAjax.postJSON('/user-ajax/del-user-tag-by-id/'+id,null,function(res){
                        layer.close(loading);
                        Form.defaultCallback(res,{
                            success:function(){
                                layer.msg('删除成功！',{
                                    icon:1,
                                    time:1000
                                });
                                $(elm).parents('tr').first().slideUp('slow');
                            },
                            error:function(){
                                layer.msg('删除失败！',{
                                    icon:2,
                                    time:1000
                                });
                            }
                        });
                    });
                }
            });
        });
    });
    
    return {

    }

}();