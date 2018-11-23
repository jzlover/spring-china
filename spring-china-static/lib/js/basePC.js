var $ = require('jquery');
var DialogPC = require('./dialogPC.js');
var Form = require('./form.js');
var Convenient = require('./convenient.js');

var initForm = function () {
    $(function () {
        if ('__env' in window && 'token' in window.__env) {
            $.ajaxSetup({
                headers: {
                    'X-CSRF-TOKEN': window.__env.token
                }
            });
        }
        $('form').each(function (i, o) {
            $(o).unbind('submit');
            var isAjaxForm = ( $(o).attr('data-ajax-form') !== undefined );
            if (isAjaxForm) {
                Form.initAjax(o, DialogPC);
            } else {
                if (!$(o).is('[data-form-no-loading]')) {
                    Form.initCommon(o, DialogPC);
                }
            }
        });
    });
};
var initConvenient = function () {
    Convenient.init(DialogPC);
};

var Base = {
    init: function () {
        initForm();
        initConvenient();
    },
    defaultFormCallback: function (res, callback) {
        return Form.defaultCallback(res, callback, DialogPC);
    }
};


module.exports = Base;