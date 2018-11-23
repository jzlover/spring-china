

import $ from 'jquery';
import '../libs/plugins/bootstrap/js/bootstrap.min';
import webSocket from './common/websocket';
import '../libs/plugins/pagination/jquery.pagination';
import layer from '../libs/plugins/layer/layer';
import CommonAjax from './common/helper';

var account_wx_bind=function () {

    $(function () {
        console.log('in...');
        setTimeout(function(){
            console.log('infrl..');
            layer.closeAll('iframe');
        },1000);
    });
}();