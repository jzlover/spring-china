/**
 * Created by jzlover on 2017/7/5.
 */
var $ = require('jquery');

var CommonAjax={
    postJSON:function(url,data,success){
        if(url==null || url ==undefined){
            alert('url参数没设置');
            return;
        }
        if(data==null || data ==undefined){
            alert('data参数没设置');
            return;
        }
        $.ajax({
            async: true,
            type: 'POST',
            url: url,
            data: data,
            contentType: 'application/json; charset=utf-8',
            dataType: 'json',
            success: function(v){
                if(success==null || success==undefined){
                    alert('SUCCESS');
                }else{
                    success(v);
                }
            },
            error: function(){
                alert('ajax error!');
            }
        });
    }
};


module.exports=CommonAjax;