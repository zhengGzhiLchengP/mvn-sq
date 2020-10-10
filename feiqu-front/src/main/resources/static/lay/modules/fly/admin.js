layui.define(['layer'], function (exports) {
    var $ = layui.jquery;

    //通过遍历给菜单项加上data-index属性
    $(".menuItem").each(function(index) {
        if (!$(this).attr('data-index')) {
            $(this).attr('data-index', index);
        }
    });

    $('.menuItem').on('click', function () {
        fqAdmin['menuItem'].call(this);
        return false;
    });

    // 点击选项卡菜单
    $('.menuTabs').on('click', '.menuTab', function () {
        fqAdmin['activeTab'].call(this);
    });

    $('.menuTabs').on('click', '.menuTab i', function () {
        fqAdmin['closeTab'].call(this);
    });
    $('.tabReload').on('click', function () {
        fqAdmin['refreshTab'].call(this);
    });
    // 左移按扭
    $('.tabLeft').on('click', function () {
        fqAdmin['scrollTabLeft'].call(this);
    });

    var fqAdmin ={
        scrollTabLeft:function(){
            var marginLeftVal = Math.abs(parseInt($('.page-tabs-content').css('margin-left')) + 50);
            // 可视区域非tab宽度
            var tabOuterWidth = fqAdmin.calSumWidth($(".content-tabs").children().not(".menuTabs"));
            //可视区域tab宽度
            var visibleWidth = $(".content-tabs").outerWidth(true) - tabOuterWidth;
            //实际滚动宽度
            var scrollVal = 0;
            if (($(".page-tabs-content").width() + 50) < visibleWidth) {
                return false;
            } else {
                var tabElement = $(".menuTab:first");
                var offsetVal = 0;
                while ((offsetVal + $(tabElement).outerWidth(true)) <= marginLeftVal) { //找到离当前tab最近的元素
                    offsetVal += $(tabElement).outerWidth(true);
                    tabElement = $(tabElement).next();
                }
                offsetVal = 0;
                if (fqAdmin.calSumWidth($(tabElement).prevAll()) > visibleWidth) {
                    while ((offsetVal + $(tabElement).outerWidth(true)) < (visibleWidth) && tabElement.length > 0) {
                        offsetVal += $(tabElement).outerWidth(true);
                        tabElement = $(tabElement).prev();
                    }
                    scrollVal = fqAdmin.calSumWidth($(tabElement).prevAll());
                }
            }
            $('.page-tabs-content').animate({
                    marginLeft: 0 - scrollVal + 'px'
                },
                "fast");
        },
        refreshTab:function(){
            var currentId = $('.page-tabs-content').find('.active').attr('data-id');
            var target = $('.RuoYi_iframe[data-id="' + currentId + '"]');
            var url = target.attr('src');
            target.attr('src', url).ready();
        },
        activeTab:function(){
            if (!$(this).hasClass('active')) {
                var currentId = $(this).data('id');
                // 显示tab对应的内容区
                $('.mainContent .RuoYi_iframe').each(function() {
                    if ($(this).data('id') == currentId) {
                        $(this).show().siblings('.RuoYi_iframe').hide();
                        return false;
                    }
                });
                $(this).addClass('active').siblings('.menuTab').removeClass('active');
                fqAdmin.scrollToTab(this);
            }
        },
        closeTab:function(){
            var closeTabId = $(this).parents('.menuTab').data('id');
            var currentWidth = $(this).parents('.menuTab').width();
            var panelUrl = $(this).parents('.menuTab').data('panel');
            // 当前元素处于活动状态
            if ($(this).parents('.menuTab').hasClass('active')) {

                // 当前元素后面有同辈元素，使后面的一个元素处于活动状态
                if ($(this).parents('.menuTab').next('.menuTab').size()) {

                    var activeId = $(this).parents('.menuTab').next('.menuTab:eq(0)').data('id');
                    $(this).parents('.menuTab').next('.menuTab:eq(0)').addClass('active');

                    $('.mainContent .RuoYi_iframe').each(function() {
                        if ($(this).data('id') == activeId) {
                            $(this).show().siblings('.RuoYi_iframe').hide();
                            return false;
                        }
                    });

                    var marginLeftVal = parseInt($('.page-tabs-content').css('margin-left'));
                    if (marginLeftVal < 0) {
                        $('.page-tabs-content').animate({
                                marginLeft: (marginLeftVal + currentWidth) + 'px'
                            },
                            "fast");
                    }

                    //  移除当前选项卡
                    $(this).parents('.menuTab').remove();

                    // 移除tab对应的内容区
                    $('.mainContent .RuoYi_iframe').each(function() {
                        if ($(this).data('id') == closeTabId) {
                            $(this).remove();
                            return false;
                        }
                    });
                }

                // 当前元素后面没有同辈元素，使当前元素的上一个元素处于活动状态
                if ($(this).parents('.menuTab').prev('.menuTab').size()) {
                    var activeId = $(this).parents('.menuTab').prev('.menuTab:last').data('id');
                    $(this).parents('.menuTab').prev('.menuTab:last').addClass('active');
                    $('.mainContent .RuoYi_iframe').each(function() {
                        if ($(this).data('id') == activeId) {
                            $(this).show().siblings('.RuoYi_iframe').hide();
                            return false;
                        }
                    });

                    //  移除当前选项卡
                    $(this).parents('.menuTab').remove();

                    // 移除tab对应的内容区
                    $('.mainContent .RuoYi_iframe').each(function() {
                        if ($(this).data('id') == closeTabId) {
                            $(this).remove();
                            return false;
                        }
                    });

                    if(panelUrl){
                        $('.menuTab[data-id="' + panelUrl + '"]').addClass('active').siblings('.menuTab').removeClass('active');
                        $('.mainContent .RuoYi_iframe').each(function() {
                            if ($(this).data('id') == panelUrl) {
                                $(this).show().siblings('.RuoYi_iframe').hide();
                                return false;
                            }
                        });
                    }
                }
            }
            // 当前元素不处于活动状态
            else {
                //  移除当前选项卡
                $(this).parents('.menuTab').remove();

                // 移除相应tab对应的内容区
                $('.mainContent .RuoYi_iframe').each(function() {
                    if ($(this).data('id') == closeTabId) {
                        $(this).remove();
                        return false;
                    }
                });
            }
            fqAdmin.scrollToTab($('.menuTab.active'));
            return false;
        },
        calSumWidth:function(elements){
            var width = 0;
            $(elements).each(function() {
                width += $(this).outerWidth(true);
            });
            return width;
        },
        menuItem:function () {
            // 获取标识数据
            var dataUrl = $(this).attr('href'),
                dataIndex = $(this).data('index'),
                menuName = $.trim($(this).text()),
                flag = true;
            if (dataUrl == undefined || $.trim(dataUrl).length == 0) return false;

            // 选项卡菜单已存在
            $('.menuTab').each(function() {
                if ($(this).data('id') == dataUrl) {
                    if (!$(this).hasClass('active')) {
                        $(this).addClass('active').siblings('.menuTab').removeClass('active');
                        fqAdmin.scrollToTab(this);
                        // 显示tab对应的内容区
                        $('.mainContent .RuoYi_iframe').each(function() {
                            if ($(this).data('id') == dataUrl) {
                                $(this).show().siblings('.RuoYi_iframe').hide();
                                return false;
                            }
                        });
                    }
                    flag = false;
                    return false;
                }
            });
            // 选项卡菜单不存在
            if (flag) {
                var str = '<a href="javascript:;" class="active menuTab" data-id="' + dataUrl + '">' + menuName + ' <i class="fa fa-times-circle"></i></a>';
                $('.menuTab').removeClass('active');

                // 添加选项卡对应的iframe
                var str1 = '<iframe class="RuoYi_iframe" name="iframe' + dataIndex + '" width="100%" height="100%" src="' + dataUrl + '" frameborder="0" data-id="' + dataUrl + '" seamless></iframe>';
                $('.mainContent').find('iframe.RuoYi_iframe').hide().parents('.mainContent').append(str1);

                var loadIndex = layer.load(2, {shade: 0.8})

                $('.mainContent iframe:visible').load(function () {
                    layer.close(loadIndex)
                });

                // 添加选项卡
                $('.menuTabs .page-tabs-content').append(str);
                fqAdmin.scrollToTab($('.menuTab.active'));
            }
            return false;
        },
        scrollToTab:function (element) {
            var marginLeftVal = this.calSumWidth($(element).prevAll()),
                marginRightVal = this.calSumWidth($(element).nextAll());
            // 可视区域非tab宽度
            var tabOuterWidth = this.calSumWidth($(".content-tabs").children().not(".menuTabs"));
            //可视区域tab宽度
            var visibleWidth = $(".content-tabs").outerWidth(true) - tabOuterWidth;
            //实际滚动宽度
            var scrollVal = 0;
            if ($(".page-tabs-content").outerWidth() < visibleWidth) {
                scrollVal = 0;
            } else if (marginRightVal <= (visibleWidth - $(element).outerWidth(true) - $(element).next().outerWidth(true))) {
                if ((visibleWidth - $(element).next().outerWidth(true)) > marginRightVal) {
                    scrollVal = marginLeftVal;
                    var tabElement = element;
                    while ((scrollVal - $(tabElement).outerWidth()) > ($(".page-tabs-content").outerWidth() - visibleWidth)) {
                        scrollVal -= $(tabElement).prev().outerWidth();
                        tabElement = $(tabElement).prev();
                    }
                }
            } else if (marginLeftVal > (visibleWidth - $(element).outerWidth(true) - $(element).prev().outerWidth(true))) {
                scrollVal = marginLeftVal - $(element).prev().outerWidth(true);
            }
            $('.page-tabs-content').animate({
                    marginLeft: 0 - scrollVal + 'px'
                },
                "fast");
        }
    }

    exports('admin', null);
})