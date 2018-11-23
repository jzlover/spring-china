
import $ from 'jquery';
import '../libs/plugins/bootstrap/js/bootstrap.min';
import webSocket from './common/websocket';
import '../libs/plugins/common/jquery.scrollto';
import '../libs/plugins/showdown/showdown.min';
import '../libs/plugins/simplemde/simplemde.min';
import '../libs/plugins/at/js/jquery.atwho';
import '../libs/plugins/common/jquery.caret';
import '../libs/plugins/pagination/jquery.pagination';
import layer from '../libs/plugins/layer/layer';
import Form from './common/form';
import CommonAjax from './common/helper';
import utils from './common/utils';
import './common/extends';


var topic_show=function(){

    var topicId=0;
    var commentCounts=0;
    var PER_PAGE_COUNT=20;

    var hidden_like=null;
    var hidden_userId=0;
    var hidden_portraitUrl=null;
    var hidden_nickName=null;
    var hidden_cId=null;//评论的ID号
    var hidden_commentIndex=null;
    var hidden_readedType=null;
    var hidden_rId=null;

    var hashchanged_executed=false;

    var hash_page=1;



    //分页获取评论，page=1,2,3...
    function queryTopicCommenetsPagely(page){
        var page_model={
            page:page,
            pageSize:PER_PAGE_COUNT
        };
        //var loading = layer.load();

        CommonAjax.postJSON('/topic-ajax/query-topic-comments-pagely/'+topicId,JSON.stringify(page_model),function(res){
            Form.defaultCallback(res,{
                success:function(){
                    $('.topic-comments-container .media').remove();
                    $.each(res.data,function(i,o){
                        $('<div id="u-c-'+o.id+'" class="media" '+function(){
                                if(o.liked){
                                    return 'data-liked="1"';
                                }
                                return '';
                            }()+' data-id="'+o.id+'" data-uid="'+o.user.id+'" data-nickName="'+o.user.nickName+'">'+
                            '<div class="media-left">'+
                            '<a href="/user/home?id='+o.user.id+'">'+
                            '<img alt="头像" class="media-object avatar avatar-small" src="'+o.user.avatarSmall+'">'+
                            '</a>'+
                            '</div>'+
                            '<div class="media-body" style="width:100%;">'+
                            '<div class="media-heading">'+
                            '<a href="/user/home?id='+o.user.id+'">'+o.user.nickName+'</a>'+
                            '<div class="pull-right">'+
                            '<span class="glyphicon glyphicon-thumbs-up" aria-hidden="true"></span> '+
                            function(){
                                if(o.likeCount>0){
                                    return '<span class="comment-like-counts">'+o.likeCount+'</span> ';
                                }
                                return '';
                            }()+
                            function(){
                                if(hidden_userId!=o.user.id){
                                    return '<span class="glyphicon glyphicon-share-alt" aria-hidden="true"></span>';
                                }else{
                                    return '';
                                }
                            }()+
                            function(){
                                if(hidden_userId==o.user.id){
                                    return '<span class="glyphicon glyphicon-trash" aria-hidden="true"></span>';
                                }else{
                                    return '';
                                }
                            }()+
                            '</div>'+
                            '</div>'+
                            '<div class="comment-content-body">'+o.contentHtml+'</div>'+
                            '</div>'+
                            '</div>').insertBefore('.pagination');
                    });

                    if(hidden_commentIndex!=null){
                        $('#u-c-'+hidden_cId).ScrollTo(800, -200);

                        $('#u-c-'+hidden_cId).css('background-color','lightblue');
                        var tmr=setTimeout(function(){
                            $('#u-c-'+hidden_cId).css('background-color','white');

                        },2000);
                    }
                },
                error:function(){
                    layer.msg('获取评论失败！',{
                        icon:2,
                        time:1000
                    });
                }
            });
        });



    }




    function setTopicLike(like){
        $('.btn-group-like button').attr('disabled','disabled');
        var loading = layer.load();
        if(like==1){
            CommonAjax.postJSON('/topic-ajax/add-topic-like/'+topicId, '',
                function (res) {
                    layer.close(loading);
                    Form.defaultCallback(res,{
                        success:function(tc){
                            hidden_like='true';
                            $('.btn-topic-like .val').html('已赞');
                            $('ul.voted-users').append(
                                '<li data-id="'+hidden_userId+'" id="voted-u-'+hidden_userId+'">'+
                                '<a href="/user/home?id='+hidden_userId+'">'+
                                '<img alt="头像" class="avatar avatar-small" src="'+hidden_portraitUrl+'">'+
                                '</a>'+
                                '</li>');

                            layer.msg('操作成功！',{
                                icon:1,
                                time:1000
                            },function(){
                                $('.btn-group-like button').removeAttr('disabled');
                            });
                        },
                        error:function(){
                            layer.msg('操作失败！',{
                                icon:2,
                                time:1000
                            },function(){
                                $('.btn-group-like button').removeAttr('disabled');
                            });
                        }
                    });
                });
        }else{
            //删除赞
            CommonAjax.postJSON('/topic-ajax/del-topic-like/'+topicId, '',
                function (res) {
                    layer.close(loading);
                    Form.defaultCallback(res,{
                        success:function(tc){
                            hidden_like='';
                            $('.btn-topic-like .val').html('赞');
                            $('#voted-u-'+hidden_userId).remove();

                            layer.msg('操作成功！',{
                                icon:1,
                                time:1000
                            },function(){
                                $('.btn-group-like button').removeAttr('disabled');
                            });
                        },
                        error:function(){
                            layer.msg('操作失败！',{
                                icon:2,
                                time:1000
                            },function(){
                                $('.btn-group-like button').removeAttr('disabled');
                            });
                        }
                    });
                });

        }

    }



    HashChangeEvent.Fire=function(){
        hash_page = window.location.hash.substr(1, location.hash.length - 1);
        $('#pagination a').each(function(i,o){
            if($(o).html()==hash_page){
                $(o).trigger('click');
            }
        });
        hashchanged_executed=true;
    }

    function EditReaded(){
        if(hidden_readedType==null || hidden_rId ==null || hidden_readedType=="" || hidden_rId==""){
            return;
        }
        CommonAjax.postJSON('/user-ajax/edit-readed/'+hidden_readedType+'/'+hidden_rId+'/1', null,
            function (res) {
                Form.defaultCallback(res,{
                    success:function(){
                        if(res.data==1){
                            var _counts=parseInt($('#nav-msg-comment-count').html());
                            $('#nav-msg-comment-count').html(--_counts);
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
    }


    $(function(){

        topicId=$('#hidden_topicId').val();

        commentCounts=$('#hidden_counts').val();

        hidden_like=$('#hidden_liked').val();

        hidden_userId=$('#hidden_userId').val();

        if(hidden_userId==undefined || hidden_userId==null) hidden_userId=0;

        hidden_portraitUrl=$('#hidden_portraitUrl').val();

        hidden_nickName=$('#hidden_nickName').val();

        hidden_cId=$('#hidden_cId').val();

        hidden_commentIndex=$('#hidden_commentIndex').val();

        hidden_readedType=$('#hidden_readedType').val();

        hidden_rId=$('#hidden_rId').val();

        $('#txt-reply').textareaAutoHeight();

        $('.utils-login').click(function () {
            utils.showLoginDialog(1,'请先登录');
        });

        $('#btn-reply').click(function(){
            //var content=$.trim($('#txt-reply').val());
            var content_html=$.trim($('#div-markdown-show').html());
            if(content_html==''){
                layer.msg('请输入回复内容！',{
                    icon:2,
                    time:1000
                });
                return;
            }
            var topiccomment={
                topicId:topicId,
                contentHtml:content_html,
                content:$('#txt-reply').val()
            };
            var loading = layer.load();
            CommonAjax.postJSON('/topic-ajax/add-topic-comment', JSON.stringify(topiccomment),
                function (res) {
                    Form.defaultCallback(res,{
                        success:function(){
                            CommonAjax.postJSON('/topic-ajax/query-topic-comment-by-id/'+res.data, {},
                                function (topic_comment) {
                                    layer.close(loading);

                                    Form.defaultCallback(topic_comment,{
                                        success:function(tc){
                                            if($('#pagination').length==0){
                                                $('.topic-comments-container').append('<div id="pagination" class="pagination"><span class="current prev">上页</span><span class="current">1</span><span class="current next">下页</span></div>');
                                            }
                                            layer.msg('回复成功！',{
                                                icon:1,
                                                time:1000
                                            });
                                            $('#txt-reply').val('');
                                            $('<div class="media" data-id="'+res.data+'">'+
                                                '<div class="media-left">'+
                                                '<a href="/user/home?id='+hidden_userId+'">'+
                                                '<img alt="头像" class="avatar avatar-small" src="'+hidden_portraitUrl+'">'+
                                                '</a>'+
                                                '</div>'+
                                                '<div class="media-body" style="width:100%;">'+
                                                '<div class="media-heading">'+
                                                '<a href="/user/home?id='+hidden_userId+'">'+hidden_nickName+'</a>'+
                                                '<div class="pull-right">'+
                                                '<span class="glyphicon glyphicon-thumbs-up" aria-hidden="true"></span> '+
                                                '<span class="glyphicon glyphicon-trash" aria-hidden="true"></span>'+
                                                '</div>'+
                                                '</div>'+
                                                '<div class="media-left comment-content-body">'+tc.data.contentHtml+'</div>'+
                                                '</div>'+
                                                '</div>').insertBefore('#pagination');
                                        },
                                        error:function(res1){
                                            layer.msg('获取评论失败！',{
                                                icon:2,
                                                time:1000
                                            });
                                        }
                                    });
                                });
                        },
                        error:function(){
                            layer.msg('获取评论失败！',{
                                icon:2,
                                time:1000
                            });
                        }
                    });
                });
        });

        $("#pagination").pagination(commentCounts, {
            link_to:'javascript:;',
            current_page: 0,
            prev_text: "上页",
            next_text: "下页",
            num_edge_entries: 2,
            num_display_entries: 10,
            items_per_page: PER_PAGE_COUNT,
            callback: function (page_index, jq) {
                queryTopicCommenetsPagely(page_index+1);
                if(hashchanged_executed==false){
                    window.location.hash=(page_index+1);
                }else{
                    window.location.href="/topic/show/"+topicId+"#"+(page_index+1);
                }
            }
        });

        $('#txt-reply').atwho({
            at: "@",
            callbacks: {
                remoteFilter: function (query, callback) {
                    var arr = new Array();

                    CommonAjax.postJSON('/topic-ajax/query-topic-commented-users/'+topicId, null,
                        function (res) {
                            Form.defaultCallback(res,{
                                success:function(){
                                    $.each(res.data,function(i,o){
                                        arr.push(o.nickName+'('+o.id+')');
                                    });
                                    callback(arr);
                                },
                                error:function(){
                                    layer.msg('获取用户失败！',{
                                        icon:2,
                                        time:1000
                                    });
                                }
                            });

                        });
                },
            }
        });

        /*
        $('#txt-reply').keyup(function(){
            var me=this;
            var converter = new showdown.Converter(),
            html=converter.makeHtml($(me).val());
            $('#div-markdown-show').html(html);
        });
        */

        $('.btn-topic-like').click(function(){
            var _like=-1;
            if(hidden_like==''){
                //表示还没有点过赞
                _like=1;
            }
            if(hidden_like=='false'){
                _like=1;
            }
            setTopicLike(_like);

        });



        $('#edit-topic').click(function(){
            layer.open({
                skin: 'layui-layer-molv',
                icon:0,
                shadeClose:true,
                resize:false,
                content:'确定编辑该博客吗？',
                yes:function(index,layero){
                    window.location.href='/topic/edit/'+topicId;
                }
            });
        });

        $('#del-topic').click(function(){
            layer.open({
                skin: 'layui-layer-molv',
                shadeClose:true,
                resize:false,
                icon:2,
                content:'确定删除该博客吗？',
                yes:function(index,layero){
                    var loading = layer.load();
                    CommonAjax.postJSON('/topic-ajax/edit-topic-status/'+topicId+'/-1', null,
                        function (res) {
                            layer.close(loading);
                            Form.defaultCallback(res,{
                                success:function(){
                                    layer.msg('删除成功！',{
                                        icon:1,
                                        time:1000
                                    },function(){
                                        window.location.href="/user/home";
                                    });
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


        //删除回复
        $(document).on('click','.topic-comments-container .glyphicon-trash',function(){
            var me=this;
            if(hidden_userId==undefined || hidden_userId==null || hidden_userId==0){
                //没有登录
                utils.showLoginDialog(1,'请先登录');
            }else{
                var _id=$(me).parents('.media').first().attr('data-id');
                layer.open({
                    skin: 'layui-layer-molv',
                    shadeClose:true,
                    resize:false,
                    icon:2,
                    content:'确定删除该回复？',
                    yes:function(index,layero){
                        var loading = layer.load();
                        CommonAjax.postJSON('/topic-ajax/del-topic-comment-by-id/'+_id, {},
                            function (res) {
                                layer.close(loading);
                                Form.defaultCallback(res,{
                                    success:function(tc){
                                        layer.msg('删除成功！',{
                                            icon:1,
                                            time:1000
                                        },function(){
                                            $(me).parents('.media').first().slideUp('slow');
                                        });
                                    },
                                    error:function(){
                                        layer.msg('删除评论失败！',{
                                            icon:2,
                                            time:1000
                                        });
                                    }
                                });

                            });
                    }
                });
            }
        });

        //回复
        $(document).on('click','.topic-comments-container .glyphicon-share-alt',function(){
            var me=this;
            if(hidden_userId==undefined || hidden_userId==null || hidden_userId==0){
                //没有登录
                utils.showLoginDialog(1,'请先登录');
            }else{
                var _uid=$(me).parents('.media').first().attr('data-uid');
                var _nickname=$(me).parents('.media').first().attr('data-nickName');
                $('#txt-reply').val($('#txt-reply').val()+'@'+_nickname+'('+_uid+') ');
                $('#txt-reply').ScrollTo(800, -200);
            }
        });

        $(document).on('click','.topic-comments-container .glyphicon-thumbs-up',function(){
            if(hidden_userId==undefined || hidden_userId==null || hidden_userId==0){
                //没有登录
                utils.showLoginDialog(1,'请先登录');
            }else{
                var me=this;
                var _commentid=$(me).parents('.media').first().attr('data-id');
                var _liked=$(me).parents('.media').first().attr('data-liked');
                var to_like=-1;
                if(_liked==undefined){
                    //表示还没有点过赞
                    to_like=1;
                }

                var loading = layer.load();

                if(to_like==1){
                    //点赞
                    CommonAjax.postJSON('/topic-ajax/add-topic-comment-like/'+_commentid, {},
                        function (v) {
                            layer.close(loading);
                            if(v){
                                $(me).parents('.media').first().attr('data-liked',to_like);

                                var _like_len=$(me).parents('.media').first().find('.comment-like-counts').length;

                                if(_like_len>0){
                                    //表示已经存在<span class="comment-like-counts"></span>
                                    var _like_counts=parseInt($(me).parents('.media').first().find('.comment-like-counts').html());
                                    if(to_like==1){
                                        // class="comment-like-counts"
                                        _like_counts++;
                                        $(me).parents('.media').first().find('.comment-like-counts').html(_like_counts);
                                    }else{
                                        if(_like_counts==1){
                                            $(me).next().remove();
                                        }else{
                                            _like_counts--;
                                            $(me).parents('.media').first().find('.comment-like-counts').html(_like_counts);
                                        }
                                    }
                                }else{
                                    //不存在
                                    $(me).after('<span class="comment-like-counts">1</span>');
                                }


                                layer.msg('操作成功！',{
                                    icon:1,
                                    time:1000
                                });
                            }else{
                                layer.msg('操作失败！',{
                                    icon:2,
                                    time:1000
                                });
                            }
                        });
                }else{
                    //删除
                    CommonAjax.postJSON('/topic-ajax/del-topic-comment-like/'+_commentid, {},
                        function (v) {
                            layer.close(loading);
                            if(v){
                                $(me).parents('.media').first().removeAttr('data-liked');

                                var _like_len=$(me).parents('.media').first().find('.comment-like-counts').length;

                                if(_like_len>0){
                                    //表示已经存在<span class="comment-like-counts"></span>
                                    var _like_counts=parseInt($(me).parents('.media').first().find('.comment-like-counts').html());
                                    if(to_like==1){
                                        // class="comment-like-counts"
                                        _like_counts++;
                                        $(me).parents('.media').first().find('.comment-like-counts').html(_like_counts);
                                    }else{
                                        if(_like_counts==1){
                                            $(me).next().remove();
                                        }else{
                                            _like_counts--;
                                            $(me).parents('.media').first().find('.comment-like-counts').html(_like_counts);
                                        }
                                    }
                                }else{
                                    //不存在
                                    $(me).after('<span class="comment-like-counts">1</span>');
                                }


                                layer.msg('操作成功！',{
                                    icon:1,
                                    time:1000
                                });
                            }else{
                                layer.msg('操作失败！',{
                                    icon:2,
                                    time:1000
                                });
                            }
                        });
                }


            }
        });

        // setInterval(function(){
        //     var converter = new showdown.Converter(),
        //         html=converter.makeHtml($('#txt-reply').val());
        //     $('#div-markdown-show').html(html);
        // },500);






        if(hashchanged_executed==false){
            if(hidden_commentIndex==null || hidden_commentIndex<=20){
                if(hash_page!='') {
                    queryTopicCommenetsPagely(hash_page);
                }
                else
                    queryTopicCommenetsPagely(1);
            }else{
                var _page=Math.ceil(hidden_commentIndex/PER_PAGE_COUNT);
                $('#pagination a').each(function(i,o){
                    if($(o).html()==_page){
                        $(o).trigger('click');
                    }
                });
            }

        }


        EditReaded();



    });
}();