/**
 * Created by jzlover on 2017/7/5.
 */
var LayoutBase = require('./../../../app_lib/layout_base.js');


var DlgLogin={
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
                    '<input type="text" class="form-control" id="userName" name="userName" placeholder="用户名"/>'+
                    '</div>'+
                    '<div class="input-group mt-10">'+
                    '<span class="input-group-addon"><span class="glyphicon glyphicon-lock" aria-hidden="true"></span></span>'+
                    '<input type="password" class="form-control" id="password" name="password" placeholder="密码"/>'+
                    '</div>'+
                    '</form>';
                return _html;
            }(),
            yes: function(index, layero){
                var username=$.trim($(layero).find('#userName').val());
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
                $.ajax({
                    type: 'POST',
                    url: '/login',
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
                            layer.tips('用户名或密码错误!', '#userName', {
                                tips: 1
                            });
                        }
                    },
                    error: function (data) {
                        var xxx=data;
                    }
                });

            },
            btn2:function(index,layero){

            }
        });
    }
};

module.exports=DlgLogin;