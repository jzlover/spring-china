import $ from 'jquery';

var CommonAjax={
    postJSON:function(url,data,success,async){
        if(url==null || url ==undefined){
            alert('url参数没设置');
            return;
        }
        data=data||{};
        async=async||true;
        
        $.ajax({
            async: async,
            type: 'POST',
            url: url,
            data: data,
            contentType: 'application/json; charset=utf-8',
            dataType: 'json',
            success: function(res){
                if(success==null || success==undefined){
                    alert('SUCCESS');
                }else{
                    success(res);
                }
            },
            error: function(){
                alert('ajax error!');
            }
        });
    }
};

export default CommonAjax;
