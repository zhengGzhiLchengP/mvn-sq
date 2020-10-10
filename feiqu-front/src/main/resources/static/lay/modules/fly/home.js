layui.define(['fly','flow','laypage','element'], function(exports){
    var $ = layui.jquery;
    var layer = layui.layer,form = layui.form,laypage = layui.laypage
        ,fly = layui.fly,flow=layui.flow,upload=layui.upload,laytpl=layui.laytpl,element=layui.element;
    var device = layui.device();
    var editor = $('#thoughtContent');
    $('#findThought').on('keyup', function (e) {
        if (e.keyCode === 13)
            location.href = layui.cache.domainUrl + '?keyword=' + $(this).val();
    })
    var formLayerIndex;
    $('.showThought').on('click',function () {
        if (layui.cache.user.userId === 0) {
            $.modal.msgWarning("用户未登陆");
            return
        }
        $('#thought-block').removeClass('layui-hide')
        var width = (device.ios || device.android) ? ($(window).width() + 'px') : '420px'
        formLayerIndex = layer.open({
            title:'想法',
            type: 1,
            area: [width, '370px'],
            shadeClose:true,
            content: $('#thought-block')
            ,cancel: function(){
                $('#thought-block').addClass('layui-hide')
            }
            ,success(){
            }
            ,end(){
                $('#thought-block').addClass('layui-hide')
                $('#thoughtContent').val('')
                closeLayerPic()
            }
        });
    })
    $('.thought-text').each(function(){
        var othis = $(this);
        var content = othis.html().replace(/\n/g, '<br>');
        content = content.replace(/\:(\w+)\:/g,function (word,kuo) {
            return emoticons[kuo].char;
        })
        content = twemoji.parse(content);
        var linesArray = content.split('<br>');
        var lines = linesArray.length;
        if (lines > 5) {
            var shortstr = '',hiddenStr = '';
            for(var i = 0;i<lines;i++){
                if (i <= 5) {
                    shortstr += linesArray[i]+"<br>";
                }else {
                    if(i === lines-1){
                        hiddenStr +=linesArray[i];
                    }else {
                        hiddenStr +=linesArray[i]+"<br>";
                    }
                }
            }
            shortstr += '<div class="layui-hide">'+hiddenStr+'</div><a cmd="expand" href="javascript:;" class="toggle-btn">展示全文</a>';
            othis.html(shortstr);
            var $toggleBtn = othis.find('.toggle-btn');
            $toggleBtn.on('click',function () {
                var $toggle = $(this);
                if($toggle.attr('cmd') === 'expand'){
                    $toggle.siblings('div').removeClass('layui-hide');
                    $toggle.attr('cmd','hide');$toggle.html('收起');
                }else {
                    $toggle.siblings('div').addClass('layui-hide');
                    $toggle.attr('cmd','expand');$toggle.html('展示全部');
                }
            })
        }else {
            othis.html(content);
        }
    });

    $('#closeLayerPic').on('click',function () {
        if(thoughtPics.length > 0){
            layer.confirm('真的要放弃上传图片么', function(index){
                closeLayerPic();
                layer.close(index);
            });
        }else closeLayerPic();
    })
    var closeLayerPic = function () {
        var $lis = $('.to-upload-pictures').find('li');
        layui.each($lis,function (index,item) {
            if(!$(item).hasClass('upload-img-add')){
                $(item).remove();
            }
        })
        thoughtPics = [];
        element.progress('thought-pic', '0%');
    }
    $('.collect').on('click',function () {
        var othis = $(this), type = othis.data('type'),div=othis.parents('li');
        fly.json('/thought/collect/'+type, {tid:div.data('id')}, function(res){
            if(type === 'add'){
                othis.data('type', 'remove').html('<i class="fa fa-heart"></i>取消收藏');
            } else if(type === 'remove'){
                othis.data('type', 'add').html('<i class="fa fa-heart-o"></i>收藏');
            }
        });
    })
    $('.zan').on('click',function () {
        var othis = $(this), tid = othis.parents('li').data('id');
        var numEle = othis.find('cite');
        fly.json('/thought/like?thoughtId='+tid,{},function(res){
            numEle.html(res.data)
            othis.addClass('zanok')
            layer.msg('点赞成功')
        })
    })
    $('.handTop').on('click',function () {
        var othis = $(this), tid = othis.parents('li').data('id');
        layer.confirm('确认要置顶么，将会消耗20个Q豆', function (index) {
            fly.json('/thought/handTop/' + tid, {}, function (res) {
                layer.msg('置顶成功,将会存在24个小时！');
                location.reload();
            })
        })
    })
    $('.cancel-hantop').on('click',function () {
        var othis = $(this), tid = othis.parents('li').data('id');
        layer.confirm('真的取消置顶么', function(index){
            fly.json('/thought/handCancelTop/'+tid,{},function(res){
                layer.msg('已经取消置顶');
                location.reload();
            })
        });
    })

    $('.comment-show').click(function () {
        location.href='/thought/'+ $(this).parents('li').data('id')+'?action=comment'
    })
    var op_list = '<div class="op-list">' +
        '<a href="javascript:;" data-cmd="detail">详情</a><a href="javascript:;" data-cmd="hide">隐藏此想法</a></div>';
    $('.layui-icon-down').on('click',function () {
        var index = layer.tips(op_list, $(this), {
            tips: 4
            ,time: 0
            ,skin: 'layer-names'
        });
        var li = $(this).parents('li');
        $('.op-list a').on('click',function () {
            var cmd = $(this).data('cmd');
            mod[cmd].call(this, li);
        })
        $(document).on('click', function(event){
            if(!$(event.target).hasClass('layui-icon-down')){
                layer.closeAll('tips');
            }
        });
    })

    var mod ={
        handTop:function (li) {
            var tid = li.data('id');
            fly.json('/thought/handTop/'+tid,{},function(res){
                layer.msg('置顶成功,将持续24个小时！');
            })
        },detail:function (li) {
            location.href='/thought/'+ li.data('id')
        },hide:function (li) {
            li.fadeOut(500);
        }
    }

    var html = ['<div class="layui-unselect fly-edit">'
        , '<span type="faceX" title="插入表情"><i class="layui-icon layui-icon-face-smile"></i>表情</span>'].join('');
    $('.kind').html(html);
    var thoughtPics = [];
    var uploadedPicNum = 0;
    var loadIndex;
    var $picList = $('input[name="picList"]');
    upload.render({
        elem: '.addOnePic'
        ,url: action
        ,size: 10000
        ,acceptMime:'image/*'
        ,auto: false
        ,choose: function(obj) {
            if (layui.cache.user.userId == 0) {
                $.modal.msgWarning("用户未登陆");
                return
            }
            $('.layui-progress').removeClass('layui-hide')
            var files = this.files = obj.pushFile();
            for (var filekey in files) {
                uploadFile(files[filekey],function () {
                    $('.layui-progress').addClass('layui-hide')
                    var picUrl = host+tempFilename+ossSuffix;
                    thoughtPics.push(picUrl);
                    $.modal.msgSuccess('共 '+thoughtPics.length+' 张，还能上传 '+(9-thoughtPics.length)+' 张')
                    $('.to-upload-pictures').append('<li class="upload-img-item"><div class="img-wrap"><img src="'+picUrl+'"></div>' +
                        '<i class="layui-icon layui-icon-close remove-img"></i></li>');
                    layui.each($('.remove-img'),function (index,item) {
                        $(item).on('click',function () {
                            var $li = $(this).parent('li');
                            $li.fadeOut(1000,function () {
                                $li.remove();
                                thoughtPics.splice(index,1);
                                $.modal.msgSuccess('已移除，还能上传 '+(9-thoughtPics.length)+' 张')
                            });
                        })
                    })
                    delete files[filekey];
                });
            }
            layer.close(loadIndex);
        }
        ,error: function(){
            layer.close(loadIndex);
        }
    });

    $('.fly-edit span').on('click', function(event){
        var type = $(this).attr('type');
        if(type === 'face'){
            var str = '', ul, face = fly.faces;
            for(var key in face){
                str += '<li title="'+ key +'"><img src="'+ face[key] +'"></li>';
            }
            str = '<ul id="LAY-editface" class="layui-clear">'+ str +'</ul>';
            layer.tips(str, this, {
                tips: 1
                ,time: 0
                ,skin: 'layui-edit-face'
            });
            $(document).on('click', function(){
                layer.closeAll('tips');
            });
            $('#LAY-editface li').on('click', function(){
                var title = $(this).attr('title') + ' ';
                layui.focusInsert(editor[0], 'face' + title);
            });
            event.stopPropagation()
        }else if(type === 'faceX'){
            var str = '', ul;
            for(var key in emoticons){
                str += '<li title="'+ key +'">'+emoticons[key].char+'</li>';
            }
            str = '<ul id="LAY-editface" class="layui-clear emoji-list">'+ str +'</ul>';
            layer.tips(str, this, {
                tips: 1
                ,time: 0
                ,skin: 'layui-edit-face'
            });
            $(document).on('click', function(){
                layer.closeAll('tips');
            });
            $('#LAY-editface li').on('click', function(){
                var title = $(this).attr('title');
                layui.focusInsert(editor[0], ':' + title+":");
            });
            event.stopPropagation()
        }else if(type === 'picture'){
            var $layerPic = $('.layer-pic-list');
            if($layerPic.hasClass('layui-hide')){
                $layerPic.removeClass('layui-hide');
            }else $layerPic.addClass('layui-hide');
        }else if(type === 'video'){
            layer.open({
                type: 1
                ,id: 'home-video-upload'
                ,title: '插入视频'
                ,shade: false
                ,area: (device.ios || device.android) ? ($(window).width() + 'px') : '660px'
                ,fixed: false
                ,offset: [
                    editor.offset().top - $(window).scrollTop() + 'px'
                    ,editor.offset().left + 'px'
                ]
                ,skin: 'layui-layer-border'
                ,content: ['<ul class="layui-form layui-form-pane" style="margin: 20px;">'
                    ,'<li class="layui-form-item">'
                    ,'<label class="layui-form-label">URL</label>'
                    ,'<div class="layui-input-inline">'
                    ,'<input required name="video" placeholder="支持直接粘贴远程视频地址" value="" class="layui-input">'
                    ,'</div>'
                    ,'</li>'
                    ,'<li class="layui-form-item" style="text-align: center;">'
                    ,'<button type="button" lay-submit lay-filter="uploadVideo" class="layui-btn">确认</button>'
                    ,'</li>'
                    ,'</ul>'].join('')
                ,success: function(layero, index){
                    var video =  layero.find('input[name="video"]');
                    var loadIndex;
                    upload.render({
                        elem: '#uploadVideo'
                        ,url: '/api/uploadVideo'
                        ,size: 20000
                        ,accept:'video'
                        ,before:function (obj) {
                            loadIndex = layer.load(2, {shade: 0.8})
                        }
                        ,done: function(res){
                            layer.close(loadIndex);
                            if(res.code == '0000'){
                                video.val(res.data);
                            } else {
                                layer.msg(res.message, {icon: 5});
                            }
                        }
                        ,error: function(){
                            layer.close(loadIndex);
                        }
                    });
                    form.on('submit(uploadVideo)', function(data){
                        var field = data.field;
                        if(!field.video) return video.focus();
                        layui.focusInsert(editor[0], 'video['+ field.video + '] ');
                        layer.close(index);
                    });
                }
            });
        }
    });
    form.on('submit(tform)', function (data) {
        var action = $(data.form).attr('action');
        data.field.picList = thoughtPics.join(",");
        $.ajax({
            type: "post",
            url:  action,
            contentType: "application/json",
            data: JSON.stringify(data.field),
            dataType: "json",
            success: function(res) {
                if(res.code === 0){
                    layer.msg("发布成功~");
                    layer.close(formLayerIndex)
                    closeLayerPic();
                    $('#thoughtContent').val('');
                    var pictures = res.data.pictures,picsHtml = '<ul>';
                    if(pictures != null && pictures.length > 0){
                        layui.each(pictures,function (index, item) {
                            picsHtml+='<li><img src="'+item+'"/></li>'
                        })
                    }
                    picsHtml+="</ul>"
                    var content = fly.content(res.data.thoughtContent)
                    content = content.replace(/\:(\w+)\:/g,function (word,kuo) {
                        return emoticons[kuo].char;
                    })
                    $('#thoughtList').prepend('<li class="layui-anim layui-anim-fadein"><div class="detail-about"><a href="/u/'+layui.cache.user.userId+'" class="fly-avatar"><img src="'+layui.cache.user.icon+'"></a>'+
                        '<div class="fly-detail-user"><a href="/u/'+layui.cache.user.userId+'" class="c-fly-link">' +
                        '<cite>'+layui.cache.user.username+'</cite></a><span class="staff">'+layui.cache.user.roleName+'</span></div>'+
                        '<div class="detail-hits"><span class="t-area">'+res.data.area+'</span ><span>刚刚</span></div></div><div class="detail-body thought-body photos">'+
                        '<div class="thought-text">'+twemoji.parse(content)+'</div>' +
                        '<div class="thought-pic">'+picsHtml+'</div></div><div class="c-list-info"></div></li>')
                }else {
                    $.modal.msgError(result.msg)
                }
            },
            error: function(msg) {
                console.log(msg)
                $.modal.msgError('操作失败')
            }
        })
        return false
    });
    laypage.render({
        elem: 'pagesplit'
        ,theme: 'custom'
        , curr:layui.cache.pageInfo.curr
        , count:layui.cache.pageInfo.count
        , limit:layui.cache.pageInfo.limit
        , layout: ['count', 'prev', 'page', 'next']
        , jump: function (obj, first) {
            if (!first) {
                location.href = "?p=" + obj.curr + "&keyword=" + keyword
            }
        }
    });
    laypage.render({
        elem: 'pagesplit-head'
        ,theme: 'custom'
        , curr:layui.cache.pageInfo.curr
        , count:layui.cache.pageInfo.count
        , limit:layui.cache.pageInfo.limit
        ,groups:3
        , layout: [ 'page']
        , jump: function (obj, first) {
            if (!first) {
                location.href = "/?p=" + obj.curr + "&keyword=" + keyword
            }
        }
    });
    flow.lazyimg();
    exports('home', {});
});