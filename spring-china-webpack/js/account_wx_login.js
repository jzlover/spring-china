

import $ from 'jquery';
import '../libs/plugins/bootstrap/js/bootstrap.min';
import webSocket from './common/websocket';
import '../libs/plugins/pagination/jquery.pagination';
import layer from '../libs/plugins/layer/layer';
import CommonAjax from './common/helper';


import './common/extends';

var account_wx_login=function(){
	var hiddenWxId=null;
	var hiddenWxLogined=null;
	var hiddenWxAvatar=null;
	//表单是否有错
	var has_error = false;
	
	$(function(){
		
		
		hiddenWxId=$('#wxId').val();
		hiddenWxLogined=$('#wxLogined').val();
		hiddenWxAvatar=$('#avatarUrl').val();
		
		if(hiddenWxLogined=="true"){
			setTimeout(function(){
				window.location.href="/topics";
			},1000);
		}
		

	});
}();