var accessid = '',
    accesskey = '',
    host = '',
    action = '',
    policyBase64 = '',
    signature = '',
    callbackbody = '',
    filename = '',
    tempFilename = '',
    key = '',
    expire = 0,
    g_object_name = '',
    now = timestamp = Date.parse(new Date()) / 1000, el = '';

layui.use(['element'], function () {
    el = layui.element;
});

function get_signature() {
    //可以判断当前expire是否超过了当前时间,如果超过了当前时间,就重新取一下.3s 做为缓冲
    now = timestamp = Date.parse(new Date()) / 1000;
    if (expire < now + 3) {
        $.ajax({
            url: "/aliyun/oss/policy", type: "GET", dataType: 'json', async: false, success: function (res) {
                if (res.code === 0) {
                    obj = res.data;
                    host = obj['host']
                    policyBase64 = obj['policy']
                    accessid = obj['OSSAccessKeyId']
                    signature = obj['signature']
                    expire = parseInt(obj['expire'])
                    callbackbody = obj['callback']
                    key = obj['dir']
                    myDomain = obj['myDomain']
                    action = obj['action']
                    return true;
                } else {
                    return false;
                }
            }
        });
    }
    return false;
}

function uploadFile(file, func) {
    get_signature();
    var filedata = new FormData();
    filedata.append('key', generateFileName(file.name));
    filedata.append('policy', policyBase64);
    filedata.append('OSSAccessKeyId', accessid);
    filedata.append('signature', signature);
    filedata.append('success_action_status', 200);
    filedata.append('file', file);
    $.ajax({
        url: action,
        processData: false,
        cache: false,
        contentType: false,
        type: 'POST',
        data: filedata,
        success: function (res) {
            func()
        },
        xhr: function () {
            myXhr = $.ajaxSettings.xhr();
            if (myXhr.upload) {
                myXhr.upload.addEventListener('progress', progressHandlingFunction, false);
            }
            return myXhr;
        },
        error: function (i) {
            console.log(i)
        }
    })
}

function generateFileName(filename) {
    var now = new Date();
    var fname = key + '' + now.getHours() + now.getMinutes() + now.getSeconds() + now.getMilliseconds() + get_suffix(filename);
    tempFilename = fname;
    return fname;
}

function get_suffix(filename) {
    var pos = filename.lastIndexOf('.')
    var suffix = ''
    if (pos !== -1) {
        suffix = filename.substring(pos)
    }
    return suffix.toLowerCase();
}

function progressHandlingFunction(e) {
    if (e.lengthComputable) {
        var percent = parseInt(e.loaded / e.total * 100) + "%";
        el.progress('thought-pic', percent);
    }
}