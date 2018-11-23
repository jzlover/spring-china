
import $ from 'jquery';
import '../libs/plugins/bootstrap/js/bootstrap.min';
import webSocket from './common/websocket';
import layer from '../libs/plugins/layer/layer';
import CommonAjax from './common/helper';
import Form from './common/form';
import './common/extends';


var user_edit_account_binding=function () {
    var userId;
    var bln_show_bind=false;

    $(function () {
        userId=$('#hidden_userId').val();


        $('#btn-unbind-wx').click(function () {
            layer.open({
                type:0,
                skin: 'layui-layer-molv',
                icon:2,
                shadeClose:true,
                resize:false,
                content:'确定接触绑定吗？',
                yes:function(index, layero){
                    var loading = layer.load();
                    var model={
                        id:userId,
                        wxId:null
                    };
                    CommonAjax.postJSON('/account-ajax/edit-user-wxId',JSON.stringify(model),function (res) {
                        Form.defaultCallback(res,{
                           success:function () {
                               layer.msg('解绑成功！', {
                                   icon : 1,
                                   time : 1000
                               }, function() {
                                   location.reload();
                               });
                           },
                           error:function () {
                               layer.msg('解绑失败！', {
                                   icon : 2,
                                   time : 1000
                               });
                           } 
                        });
                    });

                }
            });
        });

        $('#btn-bind-wx').click(function () {
            layer.open({
                type : 2,
                title : '微信绑定',
                skin : 'layui-layer-molv',
                shadeClose : true,
                resize : false,
                scrollbar : false,
                area : [ '400px', '450px' ],
                content:'https://open.weixin.qq.com/connect/qrconnect?appid=wx60dfcd85bdcd3b0e&redirect_uri=http://beta.spring-china.org/account/wx-bind&response_type=code&scope=snsapi_login&self_redirect=true&state=STATE#wechat_redirect',
                success:function (layero,index) {
                    if(bln_show_bind){
                        setTimeout(function () {
                            layer.close(index);
                            window.location.reload();
                        },1000);
                    }
                    bln_show_bind=true;
                },
                yes:function(index, layero){


                }
            });
        });
    });
}();