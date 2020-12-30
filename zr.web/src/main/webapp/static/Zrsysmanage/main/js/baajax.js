layui.define('layer', function (exports) {
    "use strict";
    var $ = layui.jquery,
        layerTips = parent.layer === undefined ? layui.layer : parent.layer;


    var baajax = {
        post: function (url, params, callback) {
            $.post(url, params, function (res) {
                if (res.statusCode === 101) {
                    layerTips.alert(res.msg, {
                        icon: 2, title: '系统提示', cancel: function (index, layero) {
                            top.location.href = location.origin + '/checklogin';
                        }
                    }, function () {
                        top.location.href = location.origin + '/checklogin';
                    });
                }
                callback(res);
            }, 'json');
        },
        get: function (url, params, callback) {
            $.getJSON(url, params, function (res) {
                if (res.statusCode === 101) {
                    layerTips.alert(res.msg, {
                        icon: 2, title: '系统提示', cancel: function (index, layero) {
                            top.location.href = location.origin + '/checklogin';
                        }
                    }, function () {
                        top.location.href = location.origin + '/checklogin';
                    });
                }
                callback(res);
            });
        },
        v: '1.0.0'
    };


    exports('baajax', baajax);
});