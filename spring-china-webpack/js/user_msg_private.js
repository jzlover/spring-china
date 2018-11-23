

import $ from 'jquery';
import '../libs/plugins/bootstrap/js/bootstrap.min';
import webSocket from './common/websocket';
import '../libs/plugins/pagination/jquery.pagination';
import layer from '../libs/plugins/layer/layer';
import CommonAjax from './common/helper';
import topic from './common/topic';

import Form from './common/form';
import './common/extends';

var user_msg_private=function(){
	
	var hiddenMsgCount=null;
	
	$(function(){
		
		hiddenMsgCount=$('#hidden_msgCount').val();
		
		
	});
	
}();