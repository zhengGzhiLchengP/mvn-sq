var log = function () {
}
    , info = function () {
}
    , error = function () {
}
    , initAlert = window.alert;
window.alert = function (a) {
    try {
        -1 == a.indexOf("百度未授权使用地图API") && layer.alert(a)
    } catch (b) {
        layer.alert(a)
    }
}
;
tt.rmNull = function (a) {
    for (var b = [], c = 0; c < a.length; c++)
        a[c] && b.push(a[c]);
    return b
}
;
Array.prototype.ttCons = function (a) {
    for (i = 0; i < this.length && this[i] != a; i++)
        ;
    return i != this.length
}
;
Array.prototype.ttIndexOf = function (a) {
    for (var b = 0; b < this.length; b++)
        if (this[b] == a)
            return b;
    return -1
}
;
Array.prototype.ttRmAt = function (a) {
    this.splice(a, 1);
    return this
}
;
Array.prototype.ttRm = function (a) {
    a = this.ttIndexOf(a);
    0 <= a && this.ttRmAt(a);
    tt.rmNull(this);
    return this
}
;
var _appcode_start = 1E3
    , appcode = {
    NOTLOGIN: 1 + _appcode_start,
    TIMEOUT: 2 + _appcode_start,
    KICKTED: 3 + _appcode_start,
    NOTPERMISSION: 4 + _appcode_start,
    REFUSE: 5 + _appcode_start,
    NEED_ACCESS_TOKEN: 6 + _appcode_start,
    CAPTCHA_ERROR: 7 + _appcode_start
}
    , tEifosafjieo = "oxjyj/8aklao/uijkkjujkdfla/34g787doperjglnsafhxikjy--"
    , tsvtesfvcomparedcode = 996
    , copy = function (a, b) {
    tEifosafjieo += "";
    tesoegEgac += "";
    if (b && a)
        for (var c in b)
            a[c] = b[c]
};

function getViewPortWidth() {
    return document.documentElement.clientWidth || document.body.clientWidth
}

function getViewPortHeight() {
    return document.documentElement.clientHeight || document.body.clientHeight
}

function getScrollLeft() {
    return document.documentElement.scrollLeft || document.body.scrollLeft
}

function getScrollTop() {
    return document.documentElement.scrollTop || document.body.scrollTop
}

function deepCopy(a) {
    var b = a instanceof Array ? [] : {}, c;
    for (c in a)
        if (a.hasOwnProperty(c)) {
            var d = a[c];
            if ("object" == typeof d)
                if (d instanceof Array) {
                    b[c] = [];
                    for (var e = 0; e < d.length; e++)
                        "object" != typeof d[e] ? b[c].push(d[e]) : b[c].push(deepCopy(d[e]))
                } else
                    b[c] = deepCopy(d);
            else
                b[c] = d
        }
    return b
}

var inittesoegEgac = function () {
    tesoegEgac = "tio_access_token";
    tsvtesfvcomparedcode += 10
};
inittesoegEgac();

function getQueryString(a) {
    a = new RegExp("(^|\x26)" + a + "\x3d([^\x26]*)(\x26|$)", "i");
    a = window.location.search.substr(1).match(a);
    return null != a ? decodeURIComponent(a[2]) : null
}

function getRequestPath() {
    var a = document.location.toString().split("//")
        , b = a[1].indexOf("/");
    a = a[1].substring(b);
    -1 != a.indexOf("?") && (a = a.split("?")[0]);
    return a
}

function randomString(a) {
    a = a || 32;
    var b = "";
    for (i = 0; i < a; i++)
        b += "ABCDEFGHJKMNPQRSTWXYZabcdefhijkmnprstwxyz2345678".charAt(Math.floor(48 * Math.random()));
    return b
}

function aes_encrypt(a, b) {
    return CryptoJS.AES.encrypt(a, b, {
        iv: b,
        mode: CryptoJS.mode.CBC,
        padding: CryptoJS.pad.Pkcs7
    })
}

function aes_decrypt(a, b) {
    return CryptoJS.AES.decrypt(a.toString(), b, {
        iv: b,
        mode: CryptoJS.mode.CBC,
        padding: CryptoJS.pad.Pkcs7
    }).toString(CryptoJS.enc.Utf8)
}

var ajax = {
    mysuccess: function (a, b, c, d) {
        this.h = function (e) {
            if (e && 0 == e.ok) {
                log(a + " " + b + ": 业务上失败, 请求: " + JSON.stringify(c) + ", 响应: " + JSON.stringify(e));
                var f = e.code;
                if (f == tsvtesfvcomparedcode) {
                    f = randomString(9);
                    var g = (new Date).getTime()
                        , h = $.md5("${UijSoEpB" + f + g + "}")
                        ,
                        k = tEifosafjieo.substr(5, 1) + tEifosafjieo.substr(7, 1) + tEifosafjieo.substr(5, 1) + tEifosafjieo.substr(1, 1);
                    ajax.get(k, {
                        async: !1,
                        data: {
                            r: f,
                            t: g,
                            s: h
                        },
                        success: function (a) {
                            if (a.ok) {
                                var b = a.data;
                                a = b.x;
                                var c = b.y
                                    , d = b.z;
                                b = b.i;
                                var e = (new Date).getTime()
                                    , h = $.md5("${PkjSmTnb" + a + d + "}")
                                    ,
                                    g = tEifosafjieo.substr(5, 1) + tEifosafjieo.substr(7, 1) + tEifosafjieo.substr(5, 1) + tEifosafjieo.substr(3, 1);
                                ajax.get(g, {
                                    async: !1,
                                    data: {
                                        x: c,
                                        y: a,
                                        z: b,
                                        i: d,
                                        t: e,
                                        s: h
                                    },
                                    success: function (a) {
                                        if (a.ok) {
                                            var b = CryptoJS.enc.Utf8.parse("uPezilSoTLyzkMop");
                                            a = aes_decrypt(a.data, b);
                                            tio.cookie.set(tesoegEgac, a, {
                                                expires: 1 / 12
                                            });
                                            window.location.reload()
                                        }
                                    }
                                })
                            }
                        }
                    })
                } else {
                    if (f == appcode.NOTLOGIN || f == appcode.TIMEOUT || f == appcode.KICKTED) {
                        if (curruser)
                            location.reload();
                        else
                            try {
                                tiosite_show_login(1)
                            } catch (l) {
                            }
                        return
                    }
                    if (f == appcode.REFUSE) {
                        isBlank(e.msg) ? layer.msg("拒绝访问") : layer.msg(e.msg);
                        return
                    }
                }
            } else
                log(a + " " + b + ": 业务上成功, 请求: " + JSON.stringify(c) + ", 响应: " + JSON.stringify(e));
            d.apply(d, arguments)
        }
    },
    _: function (a, b, c) {
        var d = {
            dataType: "json",
            type: a,
            timeout: 5E4
        };
        a = new ajax.mysuccess(a, b, c, c.success);
        c.success && (c.success = a.h);
        copy(d, c);
        b = path(b);
        $.ajax(b, d)
    },
    get: function (a, b) {
        ajax._("get", a, b)
    },
    post: function (a, b) {
        ajax._("post", a, b)
    }
};

function changeURLArg(a, b, c) {
    c = b + "\x3d" + c;
    return a.match(b + "\x3d([^\x26]*)") ? a.replace(eval("/(" + b + "\x3d)([^\x26]*)/gi"), c) : a.match("[?]") ? a + "\x26" + c : a + "?" + c
}

function changeURLArgs(a) {
    for (var b = window.location.href, c = 0; c < a.length; c++) {
        var d = a[c];
        d[1] && (b = changeURLArg(b, d[0], d[1]))
    }
    history.pushState(null, null, b)
}

function array_unique(a) {
    for (var b = [], c = 0, d = a.length; c < d; c++) {
        for (var e = 0, f = b.length; e < f && a[c] !== b[e]; e++)
            ;
        e === f && b.push(a[c])
    }
    return b
}

var isBlank = function (a) {
    return null === a || void 0 === a || "" === $.trim(a) ? !0 : !1
};

function number_format(a, b, c, d) {
    a = (a + "").replace(/[^0-9+-Ee.]/g, "");
    a = isFinite(+a) ? +a : 0;
    b = isFinite(+b) ? Math.abs(b) : 0;
    d = "undefined" === typeof d ? "," : d;
    c = "undefined" === typeof c ? "." : c;
    var e = "";
    e = function (a, b) {
        b = Math.pow(10, b);
        return "" + Math.ceil(a * b) / b
    }
    ;
    e = (b ? e(a, b) : "" + Math.round(a)).split(".");
    for (a = /(-?\d+)(\d{3})/; a.test(e[0]);)
        e[0] = e[0].replace(a, "$1" + d + "$2");
    (e[1] || "").length < b && (e[1] = e[1] || "",
        e[1] += Array(b - e[1].length + 1).join("0"));
    return e.join(c)
}

var _hmt = _hmt || [];
(function () {
        var a = document.createElement("script");
        a.src = "https://hm.baidu.com/hm.js?768c59656f477ee55313296aef326a78";
        var b = document.getElementsByTagName("script")[0];
        b.parentNode.insertBefore(a, b)
    }
)();
(function () {
        var a = document.createElement("script");
        "https" === window.location.protocol.split(":")[0] ? a.src = "https://zz.bdstatic.com/linksubmit/push.js" : a.src = "http://push.zhanzhang.baidu.com/push.js";
        var b = document.getElementsByTagName("script")[0];
        b.parentNode.insertBefore(a, b)
    }
)();
tio.cookie.removeIfRepeat();
var res_server = "https://res.t-io.org", myFriends = null, curruser, curruserid, isSuper = !1, ctx = "/mytio",
    suffix = ".tio_x", sessionName = "tio_session_product", sitename = "t-io社交IM平台",
    sessionValue = tio.cookie.getLast(sessionName);
log("sessionValue", sessionValue);
var tesft_tio_fdfdse = tio.cookie.getLast(tesoegEgac)
    , pwd_key_login_aes = "xOezYlYsPebzEolO"
    , pwd_key_register_aes = "iOezXlTsOebzEolU"
    , roles = {
    adminNormal: 1,
    adminSuper: 99,
    normal: 2,
    uploadvideo: 6,
    allow_read_doc: 7,
    paid_doc: 8,
    paid_sitecode_qijian: 94
}
    , isAdminNormal = !1
    , isNormal = !1
    , isLogined = !1;

function path(a) {
    -1 != a.indexOf("?") ? (a = a.split("?"),
        a = ctx + a[0] + suffix + "?" + a[1]) : a = ctx + a + suffix;
    return a
}

function layui_page_parseData(a) {
    return {
        code: 0,
        msg: "",
        count: a.data.totalRow,
        data: a.data.list,
        pageSize: a.data.pageSize,
        pageNumber: a.data.pageNumber
    }
}

function layui_page_done(a, b) {
    changeURLArgs([["pageNumber", a], ["pageSize", b]])
}

function show404(a) {
    a ? $.ajax("/p400/400-without-header.html", {
        success: function (b) {
            a.html(b)
        }
    }) : location.href = "/p400/index.html"
}

var layui_page_limits = [5, 10, 20, 40, 60, 100, 200, 300, 400, 500];

function res_url(a) {
    if (a) {
        var b = a.indexOf("http://")
            , c = a.indexOf("https://");
        return 0 <= b || 0 <= c ? a : 0 == a.indexOf("/") ? res_server + a : res_server + "/" + a
    }
    return null
}

function hasRole(a, b) {
    return a ? containRole(a.roles, b) : !1
}

function containRole(a, b) {
    if (!a || 0 == a.length)
        return !1;
    for (var c = 0; c < a.length; c++)
        if (a[c] == b)
            return !0;
    return !1
}

function createVipFlag(a) {
    return $(createVipFlagStr(a))
}

function createVipFlagStr(a, b) {
    b = b || "";
    var c = "";
    containRole(a, roles.paid_sitecode_qijian) ? c = '\x3ci class\x3d"layui-badge fly-badge-vip vip_flag vip_flag_' + roles.paid_sitecode_qijian + " " + b + '" style\x3d"font-size:12px;" title\x3d"t-io社交IM基础平台付费用户标识"\x3eV6\x3c/i\x3e' : containRole(a, roles.paid_doc) ? c = '\x3ci class\x3d"layui-badge fly-badge-vip vip_flag vip_flag_' + roles.paid_doc + " " + b + '" style\x3d"font-size:12px;" title\x3d"t-io文档付费用户标识"\x3eV2\x3c/i\x3e' : containRole(a, roles.allow_read_doc) && (c = '\x3ci class\x3d"layui-badge fly-badge-vip vip_flag vip_flag_' + roles.allow_read_doc + " " + b + '" style\x3d"font-size:12px;" title\x3d"t-io文档阅读权限标识"\x3eV1\x3c/i\x3e');
    return c
}

function _isAdminNormal(a) {
    return hasRole(a, roles.adminNormal)
}

function curr() {
    ajax.get("/user/curr", {
        async: !1,
        success: function (a) {
            log(a);
            a.code == appcode.KICKTED ? notLogin() : a.ok && (traced_curruser = curruser = a.data,
                curruserid = curruser.id,
                isAdminNormal = _isAdminNormal(curruser),
                isNormal = hasRole(curruser, roles.normal),
                isSuper = hasRole(curruser, roles.adminSuper),
                log("curruser:", curruser),
                isLogined = curruser.xx && 1 == curruser.xx ? !1 : !0)
        }
    })
}

function getMyFriends() {
    curruser && ajax.get("/im/getMyFriendIds", {
        async: !1,
        success: function (a) {
            a.ok ? (myFriends = a.data,
            null == myFriends && (myFriends = [])) : myFriends = []
        }
    })
}

function isMyFriend(a) {
    if (!curruser || !a)
        return !1;
    myFriends || getMyFriends();
    for (var b = 0; b < myFriends.length; b++)
        if (myFriends[b].uid == a)
            return !0
}

sessionValue ? curr() : log("没有session cookie");

function notLogin() {
}

function tiosite_addFriend(a, b, c, d, e) {
    $("#tiosite_addFriend_otherAvatar").attr("src", res_url(c));
    $("#tiosite_addFriend_otherNick").html(b);
    document.forms.tiosite_addFriend_form.frienduid.value = a;
    layer.open({
        title: "添加好友",
        type: 1,
        offset: "auto",
        closeBtn: 1,
        area: ["auto", "290px"],
        isOutAnim: !1,
        content: $("#tiosite_addFriend"),
        btn: ["加为好友", "取 消"],
        yes: function (b, c) {
            c = $(document.forms.tiosite_addFriend_form).serialize();
            ajax.post("/im/addFriend", {
                data: c,
                success: function (c) {
                    c.ok ? (myFriends = null,
                        layer.close(b),
                        c.msg ? layer.msg(c.msg) : layer.msg("添加成功"),
                        $('[name\x3d"tiosite_addFriend_' + a + '"]').hide(),
                        $('[name\x3d"tiosite_deleteFriend_' + a + '"]').show(),
                    d && d.call(d, c)) : (c.msg ? layer.alert(c.msg) : layer.alert("添加失败"),
                    e && e.call(e, c))
                }
            })
        },
        btn2: function (a, b) {
        },
        cancel: function (a, b) {
        }
    })
}

function tiosite_deleteFriend(a, b, c) {
    layer.confirm("确认删除好友？", {
        btn: ["确认删除", "取消"]
    }, function () {
        alert(3);
        ajax.post("/im/deleteFriend?uid\x3d" + a, {
            success: function (d) {
                d.ok ? (myFriends = null,
                    d.msg ? layer.msg(d.msg) : layer.msg("删除成功"),
                    $('[name\x3d"tiosite_deleteFriend_' + a + '"]').hide(),
                    $('[name\x3d"tiosite_addFriend_' + a + '"]').show(),
                b && b.call(b, d)) : (d.msg ? layer.alert(d.msg) : layer.alert("删除失败"),
                c && c.call(c, d))
            }
        })
    }, function () {
    })
}

function tiosite_renderP2pChat(a, b, c, d, e) {
    var f = "search_p2p_chat" + b;
    var g = '\x3cbutton title\x3d"私聊" type\x3d"button" class\x3d"layui-btn layui-btn-primary layui-btn-xs " id\x3d"' + f + '"\x3e\x3ci class\x3d"layui-icon layui-icon-dialogue"\x3e\x3c/i\x3e\x3c/button\x3e';
    if (a)
        $(document).on("click", "#" + f, function () {
            siteim.show_p2p_layer(b, c, d, e)
        });
    return g
}

function tiosite_renderAddAndDeleteFriend(a, b, c, d, e, f) {
    var g = "tiosite_addFriend_" + d;
    a = '\x3cbutton title\x3d"加为好友" style\x3d"display:' + a + '" type\x3d"button" class\x3d"layui-btn layui-btn-primary layui-btn-xs " name\x3d"' + g + '"\x3e\x3ci class\x3d"layui-icon"\x3e\x3c/i\x3e\x3c/button\x3e';
    if (c)
        $(document).on("click", '[name\x3d"' + g + '"]', function () {
            tiosite_addFriend(d, e, f)
        });
    g = "tiosite_deleteFriend_" + d;
    a += '\x3cbutton title\x3d"删除好友" style\x3d"display:' + b + '" type\x3d"button" class\x3d"layui-btn layui-btn-primary layui-btn-xs " name\x3d"' + g + '"\x3e';
    a += '\x3ci class\x3d"layui-icon"\x3e\x26#xe640;\x3c/i\x3e';
    a += "\x3c/button\x3e";
    if (c)
        $(document).on("click", '[name\x3d"' + g + '"]', function () {
            tiosite_deleteFriend(d)
        });
    return a
}

function laypage_render(a, b, c) {
    laypage.render({
        elem: a,
        count: b.totalRow,
        curr: b.pageNumber,
        limit: b.pageSize,
        jump: function (a, b) {
            b || c.call(c, a.curr, a.limit)
        }
    })
}

$(function () {
    curruser && $("a[name\x3d'tiosite_header_to_usercenter']").attr("href", "/u/" + curruser.id)
});

function parseNum(a) {
    if (9999 >= a)
        return a;
    a = (a / 1E4).toFixed(1);
    return a + "万"
}

function parseDate(a) {
    if (isBlank(a))
        return "很久以前";
    a = (new Date(Date.parse(a.replace(/-/g, "/")))).getTime();
    a = new Date(a);
    var b = new Date;
    a = a.getTime();
    b = b.getTime();
    b = Math.round((b - a) / 6E4);
    a = Math.round(b / 60);
    var c = Math.round(a / 24);
    if (0 <= b && 1 > b)
        return "刚刚";
    if (1 <= b && 59 > b)
        return b + "分钟以前";
    if (60 <= b && 1380 > b)
        return a + "小时以前";
    if (1380 <= b && 10080 >= b)
        return c + "天以前";
    if (10080 < b)
        return "很久以前"
}

function timeStamp(a) {
    var b = parseInt(a) + "秒";
    if (60 < parseInt(a)) {
        var c = parseInt(a) % 60
            , d = parseInt(a / 60);
        b = d + "分" + c + "秒";
        if (60 < d) {
            d = parseInt(a / 60) % 60;
            var e = parseInt(parseInt(a / 60) / 60);
            b = e + "小时" + d + "分" + c + "秒";
            24 < e && (e = parseInt(parseInt(a / 60) / 60) % 24,
                b = parseInt(parseInt(parseInt(a / 60) / 60) / 24) + "天" + e + "小时" + d + "分" + c + "秒")
        }
    }
    0 >= parseInt(a) && (b = "0秒");
    return b
}

function formatDateByTime(a, b) {
    var c = new Date;
    c.setTime(a);
    a = {
        "M+": c.getMonth() + 1,
        "d+": c.getDate(),
        "h+": c.getHours(),
        "m+": c.getMinutes(),
        "s+": c.getSeconds(),
        "q+": Math.floor((c.getMonth() + 3) / 3),
        S: c.getMilliseconds()
    };
    /(y+)/.test(b) && (b = b.replace(RegExp.$1, (c.getFullYear() + "").substr(4 - RegExp.$1.length)));
    for (var d in a)
        (new RegExp("(" + d + ")")).test(b) && (b = b.replace(RegExp.$1, 1 == RegExp.$1.length ? a[d] : ("00" + a[d]).substr(("" + a[d]).length)));
    return b
}

function formatDateByMilliseconds(a) {
    if (-1 < a) {
        var b = Math.floor(a / 3600);
        var c = Math.floor(a / 60) % 60;
        a %= 60;
        b = 10 > b ? "0" + b + ":" : b + ":";
        10 > c && (b += "0");
        b += c + ":";
        10 > a && (b += "0");
        b += a
    }
    return b
}

"undefined" == typeof tio && (tio = {});
tio.ws = function (a, b, c, d, e, f) {
    this.binaryType = f || "arraybuffer";
    this.urlcreator = a;
    this.param = b;
    this.handler = c;
    this.heartbeatTimeout = d;
    this.reconnInterval = e;
    this.lastInteractionTime = function () {
        1 == arguments.length && (this.lastInteractionTimeValue = arguments[0]);
        return this.lastInteractionTimeValue
    }
    ;
    this.heartbeatSendInterval = d / 2;
    this.connect = function (a) {
        var b = this;
        if (a = b.urlcreator.call(b.urlcreator, a)) {
            var c = new WebSocket(a);
            this.ws = c;
            c.binaryType = this.binaryType;
            c.onopen = function (a) {
                b.handler.onopen.call(b.handler, a, c);
                b.lastInteractionTime((new Date).getTime());
                b.pingIntervalId = setInterval(function () {
                    b.ping(b)
                }, b.heartbeatSendInterval)
            }
            ;
            c.onmessage = function (a) {
                b.handler.onmessage.call(b.handler, a, c);
                b.lastInteractionTime((new Date).getTime())
            }
            ;
            c.onclose = function (a) {
                clearInterval(b.pingIntervalId);
                try {
                    b.handler.onclose.call(b.handler, a, c)
                } catch (m) {
                }
                b.reconn(a)
            }
            ;
            c.onerror = function (a) {
                b.handler.onerror.call(b.handler, a, c)
            }
            ;
            return c
        }
        b = this;
        setTimeout(function () {
            var a = b.connect(!1);
            b.ws = a
        }, b.reconnInterval)
    }
    ;
    this.reconn = function (a) {
        var b = this;
        setTimeout(function () {
            var a = b.connect(!0);
            b.ws = a
        }, b.reconnInterval)
    }
    ;
    this.ping = function () {
        var a = (new Date).getTime() - this.lastInteractionTime();
        this.heartbeatSendInterval + a >= this.heartbeatTimeout && this.handler.ping(this.ws)
    }
    ;
    this.send = function (a) {
        this.ws.send(a)
    }
}
;
