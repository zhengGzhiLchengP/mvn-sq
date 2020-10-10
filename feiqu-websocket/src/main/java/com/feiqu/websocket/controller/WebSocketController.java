package com.feiqu.websocket.controller;

import cn.hutool.core.util.StrUtil;
import com.feiqu.framwork.web.base.BaseController;
import com.feiqu.system.service.mainData.FqUserService;
import com.feiqu.websocket.config.FeiquWsConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Set;

/**
 * websocket 及时聊天
 */
@RequestMapping("websocket")
@Controller
public class WebSocketController extends BaseController {
    private static Logger logger = LoggerFactory.getLogger(WebSocketController.class);
    /*@Resource
    private TioWebSocketServerBootstrap bootstrap;*/
    @Resource
    private FeiquWsConfig feiquWsConfig;

    @Resource
    private FqUserService fqUserService;

    @ResponseBody
    @PostMapping("/isOnline")
    private Object isOnline(Integer userId) {
        return success(feiquWsConfig.isOnline(userId));
    }

    /**
     * 获取在线用户
     */
    @ResponseBody
    @PostMapping("/getOnlineList")
    private Object getOnlineList(String username) {
        //遍历webSocketMap
        Set<Integer> list = feiquWsConfig.getOnlineList();

        return success(list);
    }

    //主页
   /* @RequestMapping("index")
    public String index(Model model) {
        String key = CacheManager.getWsKey(getIP());
        RedisString redisString = new RedisString(key);
        String token;
        if(redisString.exists()){
            token = redisString.get();
        }else {
            token = RandomUtil.randomString(4);
            redisString.set(token,60*60);
        }
        model.addAttribute("token",token);
        return "/websocket/index";
    }*/

    @PostMapping("/pushAllMessage")
    @ResponseBody
    public Object pushAllMessage(String msg) {
        if (StrUtil.isEmpty(msg)) {
            return error("群发消息不能为空！");
        }
        feiquWsConfig.pushAll(msg);
        return success();
    }

    @RequestMapping("manage")
    public String manage() {
        return "/websocket/manage";
    }
/*
    @PostMapping("addToBlack")
    @ResponseBody
    public Object addToBlack(String ip) {
        if (!Validator.isIpv4(ip)) {
            return error("ip格式不正确！");
        }
        bootstrap.getServerGroupContext().ipBlacklist.add(ip);
        return success();
    }

    @PostMapping("blackList")
    @ResponseBody
    public Object blackList() {
        Collection<String> blackList = bootstrap.getServerGroupContext().ipBlacklist.getAll();
        return AjaxResult.success(blackList);
    }

    @PostMapping("removeBlackList")
    @ResponseBody
    public Object removeBlackList(String ip) {
        if (!Validator.isIpv4(ip)) {
            return error("ip格式不正确！");
        }
        bootstrap.getServerGroupContext().ipBlacklist.remove(ip);
        return success();
    }*/

}
