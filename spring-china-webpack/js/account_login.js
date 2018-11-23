


import $ from 'jquery';
import '../libs/plugins/bootstrap/js/bootstrap.min';
import layer from '../libs/plugins/layer/layer';


var account_login=function(){
	
	$(function(){
		$('#btn-reg').click(function(){
			var username=$('#userName').val();
			var password=$('#password').val(); 
			$.post('/account/login','username='+username+'&password='+password,function(data){
				if(data.result=='ok'){
					layer.msg('登录成功！',{
						icon:1,
						time:1000
					},function(){
						location.href='/topics';
					});
				}else{
					layer.tips('用户名或密码错误!', '#userName', {
						  tips: 1
					});
				}
			});
		});
	});
}();