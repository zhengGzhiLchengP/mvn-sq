layui.use(['layer', 'laypage', 'fly','flow'], function () {
    var layer = layui.layer
        , laypage = layui.laypage
        , fly = layui.fly,flow=layui.flow,$htList = $('#hotTopic-list'),source='',keyword='';
    $('.selectTopic').on('change',function () {
        var source = $(this).val();
        $htList.html('');
        loadData(source,keyword)
    })
    function loadData(source,keyword){
        flow.load({
            isAuto:false,
            elem: '#hotTopic-list'
            ,done: function(page, next){
                var lis = [];
                $.get('/hotTopic/list?page='+page+'&source='+source+'&keyword='+keyword, function(res){
                    layui.each(res.data.list, function(index, item){
                        lis.push('<div class="hotsingle">' +
                            '                        <div class="">' +
                            '                            <a class="c-fly-link" href="'+item.url+'" target="_blank">' +
                            item.title +
                            '                            </a>' +
                            '                            <span class="layui-badge layui-bg-orange">'+item.source+'</span>' +
                            '                        </div>' +
                            '                    </div>');
                    });
                    next(lis.join(''), page < res.data.pages);
                });
            }
        });
    }
    loadData('','');

    $('#findHotTopic').on('keyup', function (e) {
        keyword = $(this).val()
        if (e.keyCode === 13)
            $htList.html('');
            loadData(source,keyword)
    })
    $('.send-btn').on('click', function () {
        sendMsg();
    })
    $('.meinv').on('click', function () {
        msg.msgType = 3;
        $content.val("1");
        sendMsg();
    })
    function sendMsg() {
        msg.content = $content.val();
        if ($.common.isEmpty(msg.content)) {
            $.modal.msgWarning("发送信息不能为空！");
            return
        }
        if(msg.content.length > 500){
            $.modal.msgWarning("发送信息长度不能超过500！");
            return
        }
        tiows.send(JSON.stringify(msg));
        if(msg.msgType == 4){
            dialogContainer.append('<li><div class="mysend">' + fly.content(msg.content) + '</div></li>');
        }else {
            msg.msgType = 2;
        }
        $content.val('');
        $content.focus();

    }
    $("#qie-qun").on('click',function () {
        if(msg.msgType != 2){
            msg.msgType = 2;
            $chatTitle.html('一起聊天吧');
            msg.toUsername = '';
            dialogContainer.html('');
        }
    })
    function onlineList() {
        fly.json('/websocket/getOnlineList',function (res) {
            var total = res.data.length;
            var olist = '';
            $.each(res.data,function (index,item) {
                olist+='<li>'+item+'</li>'
            })
            $onlineList.html(olist)
            $onlineList.on('click','li',function () {
                var toUsername = $(this).html();
                if(msg.username == toUsername){
                    return;
                }
                msg.toUsername = toUsername;
                msg.msgType = 4;
                $chatTitle.html(msg.toUsername)
                dialogContainer.html('')
                $content.focus();
            })
        })
    }
    var date = new Date();
    $.ajax({
        url: ctx+'/chatMsg/history',
        data: {date: date.getTime()},
        success: function (res) {
            if (res.code == 0) {
                if (res.data) {
                    var html = '';
                    var list = res.data.list;
                    layui.each(list, function (index, item) {
                        html += '<li>' + fly.content(item.msg) + '---' + item.createTimeStr + '</li>';
                    })
                }
                dialogContainer.append(html);
                dialogContainer.scrollTop(dialogContainer[0].scrollHeight);
            }
        }
    })
    var IMHandler = function () {
        this.onopen = function (event, ws) {
            onlineList();
        }
        /**
         * 收到服务器发来的消息
         * @param {*} event
         * @param {*} ws
         */
        this.onmessage = function (event, ws) {
            var data = event.data;
            var msgBody = eval('(' + data + ')');
            var ico, imgcls, spancls, ncikcls;
            ico = msgBody.icon;
            var msgType = msgBody.msgType;
            imgcls = "imgleft";
            spancls = "spanleft";
            nickcls = "nickleft";
            msgBody.content = fly.content(msgBody.content)
            if(msgType == 0){
                return
            }
            if (msgType == 1) {
                dialogContainer.append('<li><div class="sysinfo">系统消息：' + msgBody.content + '</div></li>');
            }else if(msgType == 5){
                onlineList();
                dialogContainer.append('<li><div class="sysinfo">' + msgBody.content + '</div></li>');
            }
            else if(msgType == 4){
                if(msg.toUsername == msgBody.username){
                    dialogContainer.append('<li><div class="sysinfo">' + msgBody.content + '</div></li>');
                }else {
                    dialogContainer.append('<li><div class="sysinfo">用户（'+msgBody.username+'）私聊你：'+ msgBody.content + '</div></li>');
                }
                msg.msgType = 4;
            }else {
                if(msg.msgType == 4){
                    return
                }
                msg.msgType = msgType;
                if (layui.cache.user.userId == 0) {
                    dialogContainer.append('<li><div class="youkeinfo">' + msgBody.content+"|"+area + '</div></li>');
                } else {
                    var iconStr = "";
                    if (ico) {
                        iconStr = '<img src="' + ico + '" class="' + imgcls + '">';
                    }
                    dialogContainer.append('<li class="wschat">' + iconStr + '<span class="' + nickcls + '">' + msgBody.username
                        + '</span>：<span class="' + spancls + '">' + msgBody.content +"|"+area+ '</span></li>');
                }
            }
            dialogContainer.scrollTop(dialogContainer[0].scrollHeight);
        }

        this.onclose = function (e, ws) {
            // error(e, ws)
        }

        this.onerror = function (e, ws) {
            // error(e, ws)
        }

        /**
         * 发送心跳，本框架会自动定时调用该方法，请在该方法中发送心跳
         * @param {*} ws
         */
        this.ping = function (ws) {
            ws.send('{"msgType":0}')
        }
    }

    var heartbeatTimeout = 5000; // 心跳超时时间，单位：毫秒
    var reconnInterval = 1000; // 重连间隔时间，单位：毫秒

    var binaryType = 'blob'; // 'blob' or 'arraybuffer';//arraybuffer是字节
    var handler = new IMHandler()
    var myself = 0;
    var mtime = "";
    var myname = "";


    var tiows;


    function initWs() {
        userId = layui.cache.user.userId;
        var queryString = 'userId=' + userId;
        var param = "";
        tiows = new tio.ws(wsUrl, queryString, param, handler, heartbeatTimeout, reconnInterval, binaryType)
        tiows.connect()
    }

    function showTime() {
        var myDate = new Date();
        var m = myDate.getMinutes();
        if (m < 10) m = "0" + m;
        var s = myDate.getSeconds();
        if (s < 10) s = "0" + s;
        mtime = myDate.getHours() + ":" + m + ":" + s;
        document.getElementById("mtime").innerText = mtime;
    }


    if (typeof (tio) == "undefined") {
        tio = {};
    }
    tio.ws = {};
    /**
     * @param {*} ws_protocol wss or ws
     * @param {*} ip
     * @param {*} port
     * @param {*} paramStr 加在ws url后面的请求参数，形如：name=张三&id=12
     * @param {*} param 作为tio.ws对象的参数，由业务自己使用，框架不使用
     * @param {*} handler
     * @param {*} heartbeatTimeout 心跳时间 单位：毫秒
     * @param {*} reconnInterval 重连间隔时间 单位：毫秒
     * @param {*} binaryType 'blob' or 'arraybuffer';//arraybuffer是字节
     */
    tio.ws = function (wsUrl, paramStr, param, handler, heartbeatTimeout, reconnInterval, binaryType) {
        this.url = wsUrl
        this.binaryType = binaryType || 'arraybuffer'

        if (paramStr) {
            this.url += '?' + paramStr
            this.reconnUrl = this.url + "&"
        } else {
            this.reconnUrl = this.url + "?"
        }
        this.reconnUrl += "tiows_reconnect=true";
        this.param = param

        this.handler = handler
        this.heartbeatTimeout = heartbeatTimeout
        this.reconnInterval = reconnInterval

        this.lastInteractionTime = function () {
            if (arguments.length == 1) {
                this.lastInteractionTimeValue = arguments[0]
            }
            return this.lastInteractionTimeValue
        }

        this.heartbeatSendInterval = heartbeatTimeout / 2

        this.connect = function (isReconnect) {
            var _url = this.url;
            if (isReconnect) {
                _url = this.reconnUrl;
            }
            var ws = new WebSocket(_url)
            this.ws = ws

            ws.binaryType = this.binaryType; // 'arraybuffer'; // 'blob' or 'arraybuffer';//arraybuffer是字节
            var self = this
            ws.onopen = function (event) {
                self.handler.onopen.call(self.handler, event, ws)
                self.lastInteractionTime(new Date().getTime())

                self.pingIntervalId = setInterval(function () {
                    self.ping(self)
                }, self.heartbeatSendInterval)
            }
            ws.onmessage = function (event) {
                self.handler.onmessage.call(self.handler, event, ws)
                self.lastInteractionTime(new Date().getTime())
            }
            ws.onclose = function (event) {
                clearInterval(self.pingIntervalId) // clear send heartbeat task

                try {
                    self.handler.onclose.call(self.handler, event, ws)
                } catch (error) {
                }

                self.reconn(event)
            }
            ws.onerror = function (event) {
                self.handler.onerror.call(self.handler, event, ws)
            }

            return ws
        }

        this.reconn = function (event) {
            var self = this
            setTimeout(function () {
                var ws = self.connect(true)
                self.ws = ws
            }, self.reconnInterval)
        }

        this.ping = function () {
            var iv = new Date().getTime() - this.lastInteractionTime(); // 已经多久没发消息了
            // 单位：秒
            if ((this.heartbeatSendInterval + iv) >= this.heartbeatTimeout) {
                this.handler.ping(this.ws)
            }
        };

        this.send = function (data) {
            this.ws.send(data);
        };
    }

    initWs();
    setInterval(showTime, 1000);
});