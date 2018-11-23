
import $ from 'jquery';
import '../libs/plugins/qtip2/jquery.qtip.min';
import Form from './common/form';
import '../libs/plugins/bootstrap/js/bootstrap.min';
import webSocket from './common/websocket';
import '../libs/plugins/pagination/jquery.pagination';
import '../libs/plugins/common/jquery.sticky-kit';
import utils from './common/utils';


var root_topics=function(){
    var hidden_filter='';
    var hidden_page=1;
    var hidden_perPageCount=20;
    var hidden_counts=0;
    var hidden_title=null;



    $(function(){
        hidden_filter=$('#hidden_filter').val();
        hidden_page=$('#hidden_page').val();
        hidden_perPageCount=$('#hidden_perPageCount').val();
        hidden_counts=$('#hidden_counts').val();
        hidden_title=$('#hidden_title').val();



        $('#root-right').stick_in_parent({
            parent: $('body'),
            offset_top:10
        });



        $(".pagination").pagination(hidden_counts, {
            link_to:'javascript:;',
            current_page: (hidden_page-1),
            prev_text: "上页",
            next_text: "下页",
            num_edge_entries: 2,
            num_display_entries: 10,
            items_per_page: hidden_perPageCount,
            callback: function (page_index, jq) {
                var href='';
                if(hidden_filter=='')
                    href='/topics?page='+(page_index+1)+'&pc='+hidden_perPageCount;
                else
                    href='/topics?filter='+hidden_filter+'&page='+(page_index+1)+'&pc='+hidden_perPageCount;

                if(hidden_title!=null){
                    href+='&title='+hidden_title;
                }

                location.href=href;
            }
        });

        $(document).on('click','.btn-qtip-send-msg',function () {
            var userId=$(this).attr('data-userId');
            var nickName=$(this).attr('data-nickName');
            var avatarSmall=$(this).attr('data-avatarSmall');
            utils.showChatDialog(userId,nickName,avatarSmall);
        });

        /*
         * var _id= $(o).parent().attr('data-uId');
                       return '<button type="button" onclick="root_topics.SendMsgToUser('+_id+')">Send</button>';
         */
        $('.avatar-wrapper').each(function(i,o){
            var _user_id=$(o).parent().attr('data-uId');
            $(o).qtip({
                content:{
                    text:function (event,api) {
                        $.ajax({
                            url: '/user-ajax/query-user-by-id/'+_user_id,
                            type:'POST',
                            data:{},
                            dataType:'json'
                        })
                        .then(function(data) {
                            Form.defaultCallback(data,{
                                success:function(){
                                    var user=data.data;
                                    var _html='';
                                    _html+='<img src="'+user.avatarNormal+'" class="pull-left" style="position:relative;left: -20px;top: -30px;height:100px;width:100px;border-radius: 50%;"/>';
                                    _html += '<label style="position:relative;left:-10px;float:left;text-align:left;margin-top:10px;width:190px;background-color:rgb(255, 183, 148);">' + user.nickName + '</label>';
                                    _html += '<div style="float:left;position:relative;left:-10px;overflow: hidden;text-overflow:ellipsis;width:190px;height:80px;">' + (user.signature==null?'':user.signature) + '</div>';
                                    if(user.mine==2){
                                        _html += '<div style="float:right;"><button style="position:relative;margin-right:10px;" type="button" data-avatarSmall="'+user.avatarSmall+'" data-nickName="'+user.nickName+'" data-userId="'+user.id+'" class="btn btn-primary btn-xs btn-qtip-send-msg">发送私信</button></div>';
                                    }
                                    api.set('content.text', _html);
                                },
                                error:function(){
                                    layer.msg('加载失败！',{
                                        icon:2,
                                        time:1000
                                    });
                                }
                            });
                        }, function(xhr, status, error) {
                            // Upon failure... set the tooltip content to the status and error value
                            api.set('content.text', status + ': ' + error);
                        });
                    }
                },
                position: {
                    at: 'right center',
                    my: 'left center',
                    viewport: $(window),
                    effect: false
                },
                show: {
                    solo: true
                },
                hide: {
                    fixed: true,
                    event: 'mouseleave',
                    delay: 100
                },
                style: {
                    classes: 'ui-tooltip-shadow ui-tooltip-rounded qtip-bootstrap',
                    width: 300,
                    height: 150,
                    tip: {
                        width: 10,
                        height: 10
                    }

                }
            });
        });

        $('#txt-search').keypress(function(event) {
            event = event || window.event;
            if(event.keyCode==13){
                //回车键
                var href="/topics";
                var title=$(this).val();
                if(hidden_filter!=''){
                    href="/topics?filter="+hidden_filter;
                    if(title!='' && $.trim(title).length>0){
                        href+='&title='+title;
                    }
                }else {
                    href="/topics";
                    if(title!='' && $.trim(title).length>0){
                        href+='?title='+title;
                    }
                }

                console.log('href--------',href);

                location.href=href;

            }
        });

    });
    return{
        SendMsgToUser:function(userId){
            //alert(userId);
            var _user={
                id:userId,
                msgType:1
            };
            webSocket.SendMsgToUser(_user);
        }
    };
}();