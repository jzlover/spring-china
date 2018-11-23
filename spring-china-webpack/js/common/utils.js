import $ from 'jquery';
import Form from './form';
import CommonAjax from './helper';
import layer from '../../libs/plugins/layer/layer';
import webSocket from './websocket';

var utils=function(){
    var loadmore=false;
    var arrMsgIds=new Array();

    function queryUserHistoryMsgs(senderUserId,preDate){
        //清空数组
        arrMsgIds=new Array();

        CommonAjax.postJSON('/user-ajax/query-user-msg-history/'+senderUserId+'/'+preDate, '',
            function (data) {
                Form.defaultCallback(data,{
                    success:function(res){
                        if($('#dlg-chat').length>0){
                            if($('#hiddenUserId').val()!=""){
                                var _userId=$('#hiddenUserId').val();
                                $.each(data.data.userMsgs,function(i,o){
                                    if(o.toUserId==_userId){
                                        //将消息的ID号添加到数组中
                                        arrMsgIds.push(o.id);
                                        //如果是发送给我的
                                        $('#dlg-chat .msg-private').prepend(
                                            '<div style="padding:2px;" id="msg-private-'+o.id+'" data-id="'+o.id+'" data-time="'+o.createdAt+'">'+
                                            '<img src="'+o.senderUser.avatarSmall+'" style="float:left;width:35px;height:35px;border-radius:50%;"/>'+
                                            '<div class="bubble clearfix">'+
                                            '<span class="triangle"></span>'+
                                            '<div class="article">'+o.contentHtml+'</div>'+
                                            '</div>'+
                                            '</div>');
                                    }else{
                                        //我发送给别人的
                                        $('#dlg-chat .msg-private').prepend(
                                            '<div style="padding:2px;" id="msg-private-'+o.id+'" data-id="'+o.id+'" data-time="'+o.createdAt+'">'+
                                            '<img src="'+o.senderUser.avatarSmall+'" style="float:right;width:35px;height:35px;border-radius:50%;"/>'+
                                            '<div class="bubble clearfix fr">'+
                                            '<span class="triangle" style="border-left-color:rgb(160,232,88);"></span>'+
                                            '<div class="article" style="background-color:rgb(160,232,88) !important;color:black;">'+o.contentHtml+'</div>'+
                                            '</div>'+
                                            '</div>');
                                    }
                                });

                                $('#dlg-chat .msg-load-prev img').replaceWith('<a href="javascript:;" class="load-more-chat-msg">加载更多</a>');
                                if(!loadmore){
                                    $('#msg-wrapper .msg-private').scrollTop($('#msg-wrapper .msg-private')[0].scrollHeight);
                                }


                                if(data.data.remainedMsgCount>0){
                                    $('#dlg-chat .msg-load-prev').removeClass('hidden');
                                }else{
                                    $('#dlg-chat .msg-load-prev').removeClass('hidden').addClass('hidden');
                                }
                            }

                        }

                        //将arrMsgIds中的数组readed设置为1
                        if(arrMsgIds.length>0){
                            var strMsgIds=arrMsgIds.join(',');
                            CommonAjax.postJSON('/user-ajax/edit-user-msg-readed-multi/'+strMsgIds, '',
                                function (data) {
                                    Form.defaultCallback(data,{
                                        success:function(res){
                                            var _pre_msg_count=$('#chat-user-'+senderUserId+' .num').html();
                                            if(_pre_msg_count!=''){
                                                _pre_msg_count=parseInt(_pre_msg_count);
                                            }else{
                                                _pre_msg_count=0;
                                            }
                                            _pre_msg_count-=data.data;
                                            if(_pre_msg_count<=0){
                                                $('#chat-user-'+senderUserId+' .num').html('');
                                            }else{
                                                $('#chat-user-'+senderUserId+' .num').html(_pre_msg_count);
                                            }
                                        },
                                        error:function(res){
                                            layer.msg('操作失败！',{
                                                icon:2,
                                                time:1000
                                            });
                                        }
                                    });
                                });
                        }
                    },
                    error:function(res){
                        layer.msg('操作失败！',{
                            icon:2,
                            time:1000
                        });
                    }
                });
            });
    }


    return{
        LoadUserMsg:function(elm,senderUserId){
            if(senderUserId==webSocket.sendToUserId) return;
            webSocket.sendToUserId=senderUserId;
            $('#dlg-chat .msg-private').empty();
            $('#dlg-chat .msg-user .chat-user').removeClass('chat-user-sel');
            $(elm).removeClass('chat-user-sel').addClass('chat-user-sel');
            queryUserHistoryMsgs(senderUserId,0);
        },
        LoadMoreChatMsg: function(elm){

            var _len=$('#dlg-chat .msg-private').children().length;
            if(_len>0){
                loadmore=true;
                var _fst=$('#dlg-chat .msg-private').children().first();
                var _prevDate=$(_fst).attr('data-time');
                $('#dlg-chat .msg-load-prev a').replaceWith('<img src="/scripts/plugins/layer/skin/default/loading-1.gif"/>');
                queryUserHistoryMsgs(webSocket.sendToUserId,_prevDate);
            }
        },
        showChatDialog:function(userId,nickName,avatar,open){

            loadmore=false;
            if($('#dlg-chat').length>0){
                //如果已经存在了该窗口
                if(userId==webSocket.sendToUserId || open !=undefined) return;
                $('#msg-wrapper .msg-user .chat-user').removeClass('chat-user-sel');
                if($('#msg-wrapper .msg-user #chat-user-'+userId).length==0){
                    $('#msg-wrapper .msg-user').append(
                        '<div id="chat-user-'+userId+'" class="chat-user chat-user-sel" data-id="'+userId+'">'+
                        '<img src="'+avatar+'" style="width:35px;height:35px;border-radius:50%;"/>'+
                        '<label style="margin-left:5px;">'+nickName+'</label>'+
                        '<span class="badge" style="margin:8px;float:right;"></span>'+
                        '</div>'
                    );
                }
                $('#msg-wrapper .msg-user #chat-user-'+userId).trigger('click');

            }else{
                //创建窗口
                if(open==undefined)
                    webSocket.sendToUserId=userId;

                var dlgChat=layer.open({
                    id:'dlg-chat',
                    title:'聊天',
                    type:1,
                    shade:0,
                    area: ['500px', '400px'],
                    offset: 'rb',
                    shadeClose:false,
                    resize:false,
                    anim:3,
                    cancel:function(){
                        webSocket.sendToUserId=0;
                    },
                    content:'<table style="width:100%;" id="msg-wrapper">'+
                    '<tbody>'+
                    '<tr>'+
                    '<td style="width:30%;">'+
                    '<div class="msg-user" style="height:358px;padding:2px;"></div>'+
                    '</td>'+
                    '<td style="width:70%;">'+
                    '<div style="height:358px;">'+
                    '<div class="msg-load-prev hidden" style="text-align:center;"><a href="javascript:;" class="load-more-chat-msg">加载更多</a></div>'+
                    '<div class="msg-private" style="height:220px;overflow-y: auto;">'+

                    '</div>'+
                    '<div>'+
                    '<textarea class="txt form-control" style="width:100%;height:80px;resize:none;">'+
                    '</textarea>'+
                    '<button type="button" class="btn-send btn btn-default" id="btn-send-chat-msg">发送</button>'+
                    '</div>'+
                    '</div>'+
                    '</td>'+
                    '</tr>'+
                    '</tbody>'+
                    '</table>'
                });

                clearInterval(webSocket.chatTmr);
                webSocket.chatTmr=null;
                $('#i-chat .c').removeClass('on off').addClass('off');

                if(open==undefined){
                    $('#msg-wrapper .msg-user').append(
                        '<div id="chat-user-'+userId+'" class="chat-user chat-user-sel" data-id="'+userId+'">'+
                        '<img src="'+avatar+'" style="width:35px;height:35px;border-radius:50%;"/>'+
                        '<label style="margin-left:5px;">'+nickName+'</label>'+
                        '<span class="num badge" style="margin:8px;float:right;"></span>'+
                        '</div>');
                }

                //获取所有给我私信过的用户
                CommonAjax.postJSON('/user-ajax/query-msg-to-me-pagely/', '',
                    function (res) {
                        Form.defaultCallback(res,{
                            success:function(){
                                $.each(res.data,function(i,o){
                                    if($('#msg-wrapper .msg-user #chat-user-'+o.id).length==0){
                                        $('#msg-wrapper .msg-user').append(
                                            '<div id="chat-user-'+o.id+'" class="chat-user" data-id="'+o.id+'">'+
                                            '<img src="'+o.avatarSmall+'" style="width:35px;height:35px;border-radius:50%;"/>'+
                                            '<label style="margin-left:5px;">'+o.nickName+'</label>'+
                                            function(){
                                                if(o.unReadedMsgCount>0){
                                                    return '<span class="num badge" style="margin:8px;float:right;">'+o.unReadedMsgCount+'</span>';
                                                }else{
                                                    return '<span class="num badge" style="margin:8px;float:right;"></span>';
                                                }
                                            }()+

                                            '</div>');
                                    }

                                });

                                if(open){
                                    if($('#msg-wrapper .msg-user').children().length>0){
                                        $('#msg-wrapper .msg-user').children().first().trigger('click');
                                    }
                                }
                            },
                            error:function(){
                                layer.msg('操作失败！',{
                                    icon:2,
                                    time:1000
                                });
                            }
                        });
                    });

                if(open==undefined)
                    queryUserHistoryMsgs(userId,0);



            }

        },
        showLoginDialog:function(type,title,redirect_url){
            layer.open({
                id:'dlg-login',
                skin: 'layui-layer-molv',
                shadeClose:true,
                resize:false,
                btn:['登录','找回密码'],
                title:function(){
                    var _title='登录';
                    if(title!=null || title!=undefined)
                        _title=title;
                    return _title;
                }(),
                success: function(layero, index){
                    $(layero).find('#userName,#password').on('keydown',function(e){
                        if(e.keyCode==13){
                            $(layero).find('.layui-layer-btn0').first().trigger('click');
                        }
                    });
                    /*
                    $(layero).find('#btnFindPsByEmail').on('click',function(e){
                        layer.close(index);
                        utils.showFindPsDialog();
                    })*/
                },
                content:function(){
                    var _html='';
                    _html+='<form id="login-form">'+
                        '<div class="input-group">'+
                        '<span class="input-group-addon"><span class="glyphicon glyphicon-user" aria-hidden="true"></span></span>'+
                        '<input type="text" class="form-control" id="username" name="username" placeholder="用户名"/>'+
                        '</div>'+
                        '<div class="input-group mt-10">'+
                        '<span class="input-group-addon"><span class="glyphicon glyphicon-lock" aria-hidden="true"></span></span>'+
                        '<input type="password" class="form-control" id="password" name="password" placeholder="密码"/>'+
                        '</div>'+
                        '</form>';
                    return _html;
                }(),
                yes: function(index, layero){
                    var username=$.trim($(layero).find('#username').val());
                    var password=$.trim($(layero).find('#password').val());
                    var user={
                        userName:username,
                        password:password
                    };
                    var err_counts=0;
                    if(user.userName==''){
                        layer.tips('请输入用户名！', '#userName',{tipsMore:true});
                        err_counts++;
                    }

                    if(user.password==''){
                        layer.tips('请输入密码！', '#password',{tipsMore:true});
                        err_counts++;
                    }
                    if(err_counts>0){
                        return;
                    }
                    var loading = layer.load();

                    var dataXXXX=$(layero).find('#login-form').serialize();
                    //$(elm.DOM.content[0]).parents('.ui_dialog').first().find('.ui_buttons input').first().attr('disabled','disabled');
                    //new AjaxOperator('/user-ajax/auth', JSON.stringify(user),
                    $.ajax({
                        type: 'POST',
                        url: '/user/login',
                        data: $(layero).find('#login-form').serialize(),
                        cache: false,
                        dataType: "json",
                        crossDomain: false,
                        success: function (data) {
                            if(data.result=='ok'){
                                layer.msg('登录成功！',{
                                    icon:1,
                                    time:1000
                                },function(){
                                    window.location.reload();
                                });
                                layer.close(index);
                            }else{
                                layer.msg('用户名或密码错误！',{
                                    icon:2,
                                    time:1000
                                });
                                layer.close(loading);
                            }

                        },
                        error: function (data) {
                            var xxx=data;
                        }
                    });
                    /*
                    new AjaxOperator('/login', JSON.stringify(user),
                            function (v) {
                                layer.close(loading);
                                if(v){
                                    layer.msg('登录成功！',{
                                        icon:1,
                                        time:1000
                                    },function(){
                                        window.location.reload();
                                    });
                                    layer.close(index);
                                }else{
                                    layer.tips('用户名或密码错误!', '#userName', {
                                          tips: 1
                                    });
                                }

                            }).Operate();

                    */
                },
                btn2:function(index,layero){
                    utils.showFindPsDialog();
                }
            });
        },
        logout:function(){
            CommonAjax.postJSON('/user-ajax/logout', {},
                function (v) {
                    if(v){
                        layer.msg('登出成功！',{
                            icon:1,
                            time:1000
                        },function(){
                            window.location.reload();
                        });
                    }else{
                        layer.msg('登出失败！',{
                            icon:2,
                            time:1000
                        });
                    }
                });
        },
        showAddTagDialog:function(cb_function){
            layer.open({
                skin: 'layui-layer-molv',
                shadeClose:true,
                resize:false,
                title:'新建标签',
                success: function(layero, index){

                },
                content:'<div style="width:300px;">'+
                '<div class="form-group">'+
                '<label for="tag-name">名称</label>'+
                '<input type="email" class="form-control" id="tag-name" placeholder="请输入标签名称"/>'+
                '</div>'+
                '<div class="form-group">'+
                '<label for="tag-description">说明</label>'+
                '<textarea type="password" class="form-control" id="tag-description" placeholder="请输入标签说明"></textarea>'+
                '</div>'+
                '</div>',
                yes:function(index, layero){
                    var name=$.trim($(layero).find('#tag-name').val());
                    var description=$.trim($(layero).find('#tag-description').val());
                    if(name==''){
                        layer.tips('标签名字不能为空！', '#tag-name');
                        return;
                    }
                    if(name.length>20){
                        layer.tips('标签名字最大不能超过20个字符！', '#tag-name');
                        return;
                    }
                    if(description.length>200){
                        layer.tips('标签说明最大不能超过200个字符！', '#tag-description');
                        return;
                    }
                    var tag={
                        name:name
                    };

                    var bln_tag_exists=false;
                    CommonAjax.postJSON('/user-ajax/check-tag-exists', JSON.stringify(tag),
                        function (v) {
                            if(v){
                                bln_tag_exists=true;
                                layer.tips('该名称已经存在！', '#tag-name');
                            }
                        },false);
                    if(bln_tag_exists){
                        return;
                    }

                    tag['description']=description;
                    var loading = layer.load();

                    CommonAjax.postJSON('/user-ajax/add-tag', JSON.stringify(tag),
                        function (v) {
                            layer.close(loading);
                            if(v){
                                tag['id']=v;
                                layer.msg('添加成功！',{
                                    icon:1,
                                    time:1000
                                });
                                if(cb_function!=null && cb_function !='undefined'){
                                    cb_function(tag);
                                }
                            }
                        },false);
                }
            });
        },
        showFindPsDialog:function(){
            layer.open({
                skin: 'layui-layer-molv',
                shadeClose:true,
                resize:false,
                title:'找回密码',
                content:'<div style="width:300px;">'+
                '<div class="form-group">'+
                '<label for="userName">请输入您注册时填写的邮箱地址</label>'+
                '<input type="text" class="form-control" id="email" placeholder="邮箱"/>'+
                '</div>'+
                '</div>',
                yes:function(index,layero){
                    var _email=$.trim($(layero).find('#email').val());
                    var user={
                        email:_email
                    };
                    if(_email==''){
                        layer.tips('请输入邮箱！', '#email');
                        return;
                    }
                    var loading = layer.load();
                    CommonAjax.postJSON('/user-ajax/find-ps-by-email', JSON.stringify(user),
                        function (v) {
                            layer.close(loading);
                            layer.close(index);
                            if(v){
                                layer.msg('发送成功！',{
                                    icon:1,
                                    time:1000
                                },function(){

                                });
                            }else{
                                layer.msg('发送失败！',{
                                    icon:2,
                                    time:1000
                                });
                            }

                        });
                }
            });
        },
        showEditTagsDialog:function(userId){
            var loading = layer.load();
            CommonAjax.postJSON('/user-ajax/query-user-tags-by-userId/'+userId, '',
                function (v) {
                    var html='<table class="table table-condensed table-hover">'+
                        '<thead>'+
                        '<tr>'+
                        '<td><label>名称</label></td>'+
                        '<td><label>说明</label></td>'+
                        '<td></td>'+
                        '</tr>'+
                        '</thead><tbody>';
                    $.each(v,function(i,o){
                        html+='<tr data-id="'+o.id+'">'+
                            '<td>'+o.name+'</td>'+
                            '<td>'+o.description+'</td>'+
                            '<td><a href="javascript:;" class="del">删除</a> <a href="javascript:;" class="edit">编辑</a></td>'+
                            '</tr>';
                    });
                    html+='</tbody></table>';
                    layer.close(loading);
                    layer.open({
                        type:1,
                        skin: 'layui-layer-molv',
                        shadeClose:true,
                        resize:false,
                        maxWidth:600,
                        title:'标签管理',
                        success: function(layero, index){
                            $(layero).find('.del').on('click',function(){
                                var me=this;
                                var _id=$(me).parents('tr').first().attr('data-id');
                                layer.open({
                                    type:0,
                                    skin: 'layui-layer-molv',
                                    icon:2,
                                    shadeClose:true,
                                    resize:false,
                                    content:'确定删除？',
                                    yes:function(index1,layero){
                                        var loading1 = layer.load();
                                        CommonAjax.postJSON('/user-ajax/del-tag-by-id/'+_id, '',
                                            function (v1) {
                                                layer.close(loading1);
                                                layer.close(index1);
                                                if(v1==1){
                                                    layer.msg('删除成功！',{
                                                        icon:1,
                                                        time:1000
                                                    });
                                                    $(me).parents('tr').first().slideUp('slow');
                                                }else{
                                                    layer.msg('删除失败！',{
                                                        icon:2,
                                                        time:1000
                                                    });
                                                }
                                            });
                                    }
                                });
                            });
                        },
                        content:html,
                        yes:function(index,layero){
                            layer.close(index);
                        }
                    });
                });

        }

    };
}();

export default utils;
