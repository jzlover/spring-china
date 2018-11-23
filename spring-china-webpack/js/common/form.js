var Form = {
    defaultCallback: function (res, callback, Dialog) {

        Dialog = Dialog || null;

        callback = callback || {};

        if (typeof res !== 'object') {
            alert("ErrorResponse:" + res);
            return;
        }

        if (!("code" in res)) {
            //alert("ErrorResponseObject:" + res.toString());
            return;
        }

        var code = res.code, msg = "", redirect = "", data = null;
        if ("msg" in res) {
            msg = res.msg;
        }
        if ("redirect" in res) {
            redirect = res.redirect;
        }
        if ("data" in res) {
            data = res.data;
        }

        var redirectFunc = function (redirect) {
            if (!redirect) {
                return;
            }
            if ('[reload]' === redirect) {
                window.location.reload();
            } else if ('[root-reload]' == redirect) {
                Util.getRootWindow().location.reload();
            } else if ('[back]' === redirect) {
                window.history.back();
            } else if (redirect.indexOf('[js]') === 0) {
                eval(redirect.substr(4));
            } else {
                window.location.href = redirect;
            }
        };

        var successFunc = function () {
            if ("success" in callback) {
                callback.success(res);
            } else if (redirect) {
                if (msg) {
                    if (Dialog) {
                        Dialog.alertSuccess(msg, function () {
                            redirectFunc(redirect);
                        });
                    } else {
                        alert(msg);
                        redirectFunc(redirect);
                    }
                } else {
                    redirectFunc(redirect);
                }
            } else {
                if (Dialog) {
                    Dialog.tipSuccess(msg);
                } else {
                    alert(msg);
                }
            }
        };

        var errorFunc = function () {
            if ("error" in callback) {
                callback.error(res);
            } else if (redirect) {
                if (msg) {
                    if (Dialog) {
                        Dialog.alertError(msg, function () {
                            redirectFunc(redirect);
                        });
                    } else {
                        alert(msg);
                        redirectFunc(redirect);
                    }
                } else {
                    redirectFunc(redirect);
                }
            } else {
                if (Dialog) {
                    Dialog.tipError(msg);
                } else {
                    alert(msg);
                }
            }
        };

        if (code == 0) {
            successFunc();
        } else {
            errorFunc();
        }

    },
    // ajax����ʼ��
    initAjax: function (form, Dialog) {

        Dialog = Dialog || null;

        var $form = $(form);
        $form.on('submit', function () {

            // ��ֹ����
            if ($form.data('submiting')) {
                return false;
            }

            var action = $(this).attr("action");
            var method = $(this).attr("method");
            var callbackValidate = $(this).data('callbackValidate');
            var callback = $(this).data('callback');

            if (callbackValidate && !callbackValidate()) {
                return false;
            }

            if (!callback) {
                callback = Form.defaultCallback;
            }

            if (!method) {
                method = 'get';
            }

            var data = $(this).serializeArray();

            $form.data('submiting', true);
            if (Dialog) {
                var msg = $(this).attr('data-form-loading');
                Dialog.loadingOn(msg);
            }
            $.ajax({
                type: method,
                url: action,
                dataType: "json",
                timeout: 10*60*1000,
                data: data,
                success: function (res) {
                    $form.data('submiting', null);
                    if (Dialog) {
                        Dialog.loadingOff();
                    }
                    return callback(res, {}, Dialog);
                },
                error: function () {
                    $form.data('submiting', null);
                    if (Dialog) {
                        Dialog.loadingOff();
                    }
                    return callback({
                        code: -999,
                        msg: "err"
                    }, {}, Dialog);
                }
            });

            return false;
        });
    },
    // ��ͨ����ʼ��
    initCommon: function (form, Dialog) {
        var $form = $(form);
        $form.on('submit', function () {

            // ��ֹ����
            if ($form.data('submiting')) {
                return false;
            }

            var action = $(this).attr("action");
            var method = $(this).attr("method");
            var data = $(this).serializeArray();
            var callbackValidate = $(this).data('callbackValidate');
            var callback = $(this).data('callback');

            if (callbackValidate && !callbackValidate()) {
                return false;
            }
            $form.data('submiting', true);

            if (Dialog) {
                var msg = $(this).attr('data-form-loading');
                Dialog.loadingOn(msg);
            }

            return true;
        });
    }
};

export default Form;