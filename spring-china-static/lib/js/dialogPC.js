var $ = require('jquery');

var layer = require('./../../layer/layer.js');
var layerCss = require('./../../layer/layer.css');
var Util = require('./util.js');

var Dialog = {
    // [开始] 这部分的提示需处理
    loadingOn: function (msg) {
        msg = msg || null;
        if (msg) {
            var index = layer.open({
                type: 1,
                content: '<div style="padding:10px;height:32px;box-sizing:content-box;"><div class="layui-layer-ico16" style="display:inline-block;margin-right:10px;"></div><div style="display:inline-block;line-height:32px;vertical-align:top;font-size:13px;">' + msg + '</div></div>',
                shade: [0.3, '#000'],
                closeBtn: false,
                title: false,
                area: ['auto', 'auto']
            });
            $('#layui-layer' + index).attr('type', 'loading');
        } else {
            layer.load(2);
        }
    },
    loadingOff: function () {
        layer.closeAll('loading');
    },
    tipSuccess: function (msg) {
        var ms = 2000;
        if (msg.length > 10) {
            ms = 1000 * parseInt(msg.length / 5);
        }
        layer.msg(msg, {icon: 1, shade: 0.3, time: ms, shadeClose: true});
    },
    tipError: function (msg) {
        var ms = 2000;
        if (msg.length > 10) {
            ms = 1000 * parseInt(msg.length / 5);
        }
        layer.msg(msg, {icon: 2, shade: 0.3, time: ms, shadeClose: true});
    },
    tipPopoverShow: function (ele, msg) {
        var index = $(ele).data('popover-dialog');
        if (index) {
            layer.close(index);
        }
        index = layer.tips(msg, ele, {
            tips: [1, '#333']
        });
        $(ele).data('popover-dialog', index);
    },
    tipPopoverHide: function (ele) {
        var index = $(ele).data('popover-dialog');
        if (index) {
            layer.close(index);
        }
    },
    alertSuccess: function (msg, callback) {
        layer.alert(msg, {icon: 1, closeBtn: 0}, function (index) {
            layer.close(index);
            if (callback) {
                callback();
            }
        });
    },
    alertError: function (msg, callback) {
        layer.alert(msg, {icon: 2, closeBtn: 0}, function (index) {
            layer.close(index);
            if (callback) {
                callback();
            }
        });
    },
    confirm: function (msg, callbackYes, callbackNo, options) {
        options = options || {icon: 3, title: '提示'};
        callbackYes = callbackYes || false;
        callbackNo = callbackNo || false;
        layer.confirm(msg, options, function (index) {
            layer.close(index);
            if (callbackYes) {
                callbackYes();
            }
        }, function (index) {
            layer.close(index);
            if (callbackNo) {
                callbackNo();
            }
        });
    },
    dialog: function (url, option) {
        var opt = $.extend({
            title: null,
            width: '600px',
            height: '80%',
            shadeClose: true,
            closeCallback: function () {
            }
        }, option);
        return layer.open({
            type: 2,
            title: '玩命加载中...',
            shadeClose: opt.shadeClose,
            shade: 0.5,
            maxmin: true,
            area: [opt.width, opt.height],
            scrollbar: false,
            content: url,
            success: function (layero, index) {
                if (null !== opt.title) {
                    layer.title(opt.title, index);
                    return;
                }
                try {
                    var title = $(layero).find('iframe')[0].contentWindow.document.title;
                    layer.title(title, index);
                } catch (e) {
                }
            },
            end: function () {
                opt.closeCallback();
            }
        });
    },
    dialogContent: function (content, option) {
        var opt = $.extend({
            closeBtn: true,
            width: 'auto',
            height: 'auto',
            shade: [0.3, '#000'],
            shadeClose: true,
            openCallback: function () {
            },
            closeCallback: function () {
            }
        }, option);
        return layer.open({
            shade: opt.shade,
            type: 1,
            title: false,
            zindex: 2019,
            closeBtn: opt.closeBtn,
            shadeClose: opt.shadeClose,
            scrollbar: false,
            content: content,
            area: [opt.width, opt.height],
            success: function () {
                opt.openCallback();
            },
            end: function () {
                opt.closeCallback();
            }
        });
    },
    dialogClose: function (index) {
        layer.close(index);
    },
    input: function (callback, option) {
        var opt = $.extend({
            label: '请输入',
            width: '200px',
            height: 'auto',
            defaultValue: ''
        }, option);
        var value = opt.defaultValue;
        var inputDialog = Dialog.dialogContent([
            '<div id="dialog-input-box" style="width:', opt.width, ';height:', opt.height, ';background:#FFF;border-radius:3px;">',
            '<div style="padding:10px 10px 0 10px;">', opt.label, '</div>',
            '<div style="padding:10px;"><input type="text" style="border:1px solid #CCC;height:30px;line-height:30px;padding:0 5px;width:100%;display:block;box-sizing:border-box;outline:none;border-radius:2px;" value="', Util.specialchars(opt.defaultValue), '" /></div>',
            '<div style="padding:10px;text-align:center;color:#40AFFE;line-height:20px;border-top:1px solid #EEE;cursor:default;" class="close">确定</div>',
            '</div>'
        ].join(''), {
            openCallback: function () {
                $('#dialog-input-box').find('.close').on('click', function () {
                    Dialog.dialogClose(inputDialog);
                });
                $('#dialog-input-box').find('input').on('change', function () {
                    value = $(this).val();
                });
            },
            closeCallback: function () {
                callback && callback(value);
            }
        });
    }
    // [结束] 这部分的提示需处理
};


module.exports = Dialog;