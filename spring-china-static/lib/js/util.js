var Util = {};

Util.specialchars = function (str) {
    var s = [];
    if (!str) {
        return '';
    }
    if (str.length == 0) {
        return '';
    }
    for (var i = 0; i < str.length; i++) {
        switch (str.substr(i, 1)) {
            case "<":
                s.push("&lt;");
                break;
            case ">":
                s.push("&gt;");
                break;
            case "&":
                s.push("&amp;");
                break;
            case " ":
                s.push("&nbsp;");
                break;
            case "\"":
                s.push("&quot;");
                break;
            default:
                s.push(str.substr(i, 1));
                break;
        }
    }
    return s.join('');
};

Util.text2html = function (str) {
    str = Util.specialchars(str);
    str = str.replace(/\n/g, '</p><p>');
    return '<p>' + str + '</p>';
};

Util.text2paragraph = function (str) {
    str = str.replace(/\n/g, '</p><p>');
    return '<p>' + str + '</p>';
};

Util.urlencode = function (str) {
    str = (str + '').toString();
    return encodeURIComponent(str).replace(/!/g, '%21').replace(/'/g, '%27').replace(/\(/g, '%28').replace(/\)/g, '%29').replace(/\*/g, '%2A').replace(/%20/g, '+');
};

Util.getRootWindow = function () {
    var w;
    w = window;
    while (w.self !== w.parent) {
        w = w.parent;
    }
    return w;
};

Util.fixPath = function (path, cdn) {
    cdn = cdn || '';
    if (!path) {
        return '';
    }
    if (path.indexOf('http://') === 0 || path.indexOf('https://') === 0 || path.indexOf('//') === 0) {
        return path;
    }
    if (path.indexOf('/') === 0) {
    } else {
        path = '/' + path;
    }
    if (cdn && (cdn.lastIndexOf('/') == cdn.length - 1)) {
        cdn = cdn.substr(0, cdn.length - 1);
    }
    return cdn + path;
};

/**
 * 设定对象的值
 *
 * @param object : array|object 数组或对象
 * @param standardKey : string i0.ahand.i9.ttt
 * @param value : any 值
 */
Util.set = function (object, standardKey, value) {
    var ref = object;
    var pcs = standardKey.split('.');
    var k;
    for (var i = 0; i < pcs.length - 1; i++) {
        k = pcs[i];
        if (k.indexOf('i') === 0) {
            k = parseInt(k.substr(1));
        } else {
            k = k.substr(1);
        }
        ref = ref[k];
    }
    k = pcs[pcs.length - 1];
    if (k.indexOf('i') === 0) {
        k = parseInt(k.substr(1));
    } else {
        k = k.substr(1);
    }
    ref[k] = value;
};

Util.push = function (object, standardKey, value) {
    var ref = object;
    var pcs = standardKey.split('.');
    var k;
    for (var i = 0; i < pcs.length - 1; i++) {
        k = pcs[i];
        if (k.indexOf('i') === 0) {
            k = parseInt(k.substr(1));
        } else {
            k = k.substr(1);
        }
        ref = ref[k];
    }
    k = pcs[pcs.length - 1];
    if (k.indexOf('i') === 0) {
        k = parseInt(k.substr(1));
    } else {
        k = k.substr(1);
    }
    if (ref[k] instanceof Array) {
        ref[k].push(value);
    } else {
        alert('仅支持数据');
    }
};

Util.remove = function (object, standardKey) {
    var ref = object;
    var pcs = standardKey.split('.');
    var k;
    for (var i = 0; i < pcs.length - 1; i++) {
        k = pcs[i];
        if (k.indexOf('i') === 0) {
            k = parseInt(k.substr(1));
        } else {
            k = k.substr(1);
        }
        ref = ref[k];
    }
    k = pcs[pcs.length - 1];
    if (k.indexOf('i') === 0) {
        k = parseInt(k.substr(1));
        ref.splice(k, 1);
    } else {
        k = k.substr(1);
        delete ref[k];
    }
};

Util.fullscreen = {
    enter: function (callback) {
        var docElm = document.documentElement;
        //W3C
        if (docElm.requestFullscreen) {
            docElm.requestFullscreen();
            callback && callback();
        }
        //FireFox
        else if (docElm.mozRequestFullScreen) {
            docElm.mozRequestFullScreen();
            callback && callback();
        }
        //Chrome等
        else if (docElm.webkitRequestFullScreen) {
            docElm.webkitRequestFullScreen();
            callback && callback();
        }
        //IE11
        else if (elem.msRequestFullscreen) {
            elem.msRequestFullscreen();
            callback && callback();
        }
    },
    exit: function (callback) {
        if (document.exitFullscreen) {
            document.exitFullscreen();
            callback && callback();
        }
        else if (document.mozCancelFullScreen) {
            document.mozCancelFullScreen();
            callback && callback();
        }
        else if (document.webkitCancelFullScreen) {
            document.webkitCancelFullScreen();
            callback && callback();
        }
        else if (document.msExitFullscreen) {
            document.msExitFullscreen();
            callback && callback();
        }
    },
    isFullScreen: function () {
        if (document.exitFullscreen) {
            return document.fullscreen;
        }
        else if (document.mozCancelFullScreen) {
            return document.mozFullScreen;
        }
        else if (document.webkitCancelFullScreen) {
            return document.webkitIsFullScreen;
        }
        else if (document.msExitFullscreen) {
            return document.msFullscreenElement;
        }
        return false;
    },
    trigger: function () {
        if (Util.fullscreen.isFullScreen()) {
            Util.fullscreen.exit();
        } else {
            Util.fullscreen.enter();
        }
    }
};

module.exports = Util;