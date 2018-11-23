var LayoutBase = require('./../../app_lib/layout_base.js');
var Form= require('./../../lib/js/form.js');
var DlgLogin=require('./utilities/dlg_login.js');
LayoutBase.init();

//表单是否有错
var has_error = false;

$(function () {

    $('#userName').qtip({
        content: '用户名长度为2-20个字符，且不能包含 /,等特殊字符！',
        position: {
            my: 'left middle',  // Position my top left...
            at: 'middle right'
        },
        show: 'focusin',
        hide: 'focusout'
    });

    $('#password').qtip({
        content: '密码长度为6-20个字符！',
        position: {
            my: 'left middle',  // Position my top left...
            at: 'middle right'
        },
        show: 'focusin',
        hide: 'focusout'
    });

    $('#re-password').qtip({
        content: '请再输一次您的密码！',
        position: {
            my: 'left middle',  // Position my top left...
            at: 'middle right'
        },
        show: 'focusin',
        hide: 'focusout'
    });

    $('#nickName').qtip({
        content: '昵称长度为2-20个字符！',
        position: {
            my: 'left middle',  // Position my top left...
            at: 'middle right'
        },
        show: 'focusin',
        hide: 'focusout'
    });

    $('#userName').blur(function () {
        var username = {
            userName: $(this).val()
        };
        $('#userName-err').removeClass('hidden').addClass('hidden');
        if (username.userName.length < 2 || username.userName.length > 20) {
            $('#userName-err').removeClass('hidden');
            $('#userName-err').html('用户名长度必须在2-20个字符之间！');
            has_error = true;
            return;
        }
        jAjax.postJSON('/user-ajax/check-userName',JSON.stringify(username),function(data){
            Form.defaultCallback(data,{
                success:function(res){
                },
                error:function(res){
                    $('#userName-err').removeClass('hidden');
                    $('#userName-err').html('用户名已存在！');
                    has_error=true;
                }
            });
        });
    });

    $('#nickName').blur(function () {
        var nickname = {
            nickName: $(this).val()
        };
        $('#nickName-err').removeClass('hidden').addClass('hidden');
        if (nickname.nickName.length < 2 || nickname.nickName.length > 20) {
            $('#nickName-err').removeClass('hidden');
            $('#nickName-err').html('昵称长度必须在2-20个字符之间！');
            has_error = true;
            return;
        }

        jAjax.postJSON('/user-ajax/check-nickName',JSON.stringify(nickname),function(data){
            Form.defaultCallback(data,{
                success:function(res){
                },
                error:function(res){
                    $('#nickName-err').removeClass('hidden');
                    $('#nickName-err').html('昵称已存在！');
                    has_error=true;
                }
            });
        });
    });

    $('#password').blur(function(){

        var password=$(this).val();
        $('#password-err').removeClass('hidden').addClass('hidden');
        if(password.length<6 || password.length>20){
            $('#password-err').removeClass('hidden');
            $('#password-err').html('密码长度为6-20个字符！');
            has_error=true;
            return;
        }
    });


    $('#re-password').blur(function(){
        var repassword=$(this).val();
        var password=$('#password').val();
        $('#re-password-err').removeClass('hidden').addClass('hidden');
        if(repassword!=password){
            $('#re-password-err').removeClass('hidden');
            $('#re-password-err').html('两次密码不一致！');
            has_error=true;
            return;
        }
    });

    $('#btn-reg').click(function(){
        var me=this;
        has_error=false;
        $('#userName').trigger('blur');
        $('#password').trigger('blur');
        $('#re-password').trigger('blur');
        $('#nickName').trigger('blur');
        if(has_error){
            return;
        }
        var user_name=$.trim($('#userName').val());
        var password=$('#password').val();
        var nickname=$('#nickName').val();
        var user={
            userName:user_name,
            password:password,
            nickName:nickname
        };
        $(me).attr('disabled','disabled');
        var loading = layer.load();

        jAjax.postJSON('/user-ajax/register',JSON.stringify(user),function(data){
            layer.close(loading);
            Form.defaultCallback(data,{
                success:function(res){
                    layer.msg('注册成功！',{
                        icon:1,
                        time:1000
                    },function(){
                        $(me).removeAttr('disabled');
                        window.location.href="/topics";
                    });
                },
                error:function(res){
                    $(me).removeAttr('disabled');
                    layer.msg('注册失败！',{
                        icon:2,
                        time:1000
                    });
                }
            });
        });
    });

});