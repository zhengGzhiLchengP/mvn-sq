layui.define(['layer'], function (exports) {
   var $ = layui.jquery,layer = layui.layer;
    accessid = ''
    accesskey = ''
    host = ''
    policyBase64 = ''
    signature = ''
    callbackbody = ''
    filename = ''
    tempFilename = ''
    key = ''
    expire = 0
    g_object_name = ''
    action = ''
    now = timestamp = Date.parse(new Date()) / 1000;
    var fileUtil = {
        get_signature: function () {
            //可以判断当前expire是否超过了当前时间,如果超过了当前时间,就重新取一下.3s 做为缓冲
            now = timestamp = Date.parse(new Date()) / 1000;
            if (expire < now + 3) {
                $.ajax({
                    url: "/aliyun/oss/policy", type: "GET", dataType: 'json', async: false, success: function (res) {
                        obj = res;
                        host = obj['host']
                        action = obj['action']
                        policyBase64 = obj['policy']
                        accessid = obj['OSSAccessKeyId']
                        signature = obj['signature']
                        expire = parseInt(obj['expire'])
                        callbackbody = obj['callback']
                        key = obj['dir']
                        myDomain = obj['myDomain']
                        return true;
                    }
                });
            }
            return false;
        },
        get_suffix: function (filename) {
            pos = filename.lastIndexOf('.')
            suffix = ''
            if (pos != -1) {
                suffix = filename.substring(pos)
            }
            return suffix.toLowerCase();
        },
        generateFileName:function(filename){
            var now = new Date();
            var fname = key+''+now.getHours()+ now.getMinutes()+now.getSeconds()+now.getMilliseconds()+this.get_suffix(filename);
            tempFilename = fname;
            return fname;
        },
        get_uploaded_object_name: function (filename) {
            return host+tempFilename;
        },
        set_upload_param: function (up, filename, ret) {
            if (ret === false) {
                ret = this.get_signature()
            }
            g_object_name = this.generateFileName(filename);
            new_multipart_params = {
                'key': g_object_name,
                'policy': policyBase64,
                'OSSAccessKeyId': accessid,
                'success_action_status': '200', //让服务端返回200,不然，默认会返回204
                'callback': callbackbody,
                'signature': signature,
            };
            up.setOption({
                'url': action,
                'multipart_params': new_multipart_params
            });

            up.start();
        }
    }
    exports('fileUtil', fileUtil);
})