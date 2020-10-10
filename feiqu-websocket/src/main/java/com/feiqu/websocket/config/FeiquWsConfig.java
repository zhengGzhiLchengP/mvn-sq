package com.feiqu.websocket.config;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.EscapeUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.feiqu.common.utils.SpringUtils;
import com.feiqu.framwork.util.CommonUtils;
import com.feiqu.system.model.SuperBeauty;
import com.feiqu.system.model.mainData.ChatMsg;
import com.feiqu.system.service.mainData.ChatMsgService;
import com.feiqu.system.service.mainData.SuperBeautyService;
import com.feiqu.websocket.dto.WsMsgDTO;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.timeout.IdleStateEvent;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.yeauty.annotation.*;
import org.yeauty.pojo.ParameterMap;
import org.yeauty.pojo.Session;

import java.io.IOException;
import java.util.*;

@ServerEndpoint(port = 9876)
@Component
public class FeiquWsConfig {
    private static Logger logger = LoggerFactory.getLogger(FeiquWsConfig.class);
    private static Map<Integer,Session> map = MapUtil.newHashMap();
    private String key = "userId";
    private String h5key = "token";

    public void pushAll(String msg){
        WsMsgDTO wsMsgDTO = new WsMsgDTO();
        wsMsgDTO.setContent(msg);
        wsMsgDTO.setMsgType(1);
        for(Session sessionSingle : map.values()){
            sessionSingle.sendText(JSON.toJSONString(wsMsgDTO));
        }
    }

    @OnOpen
    public void onOpen(Session session, HttpHeaders headers, ParameterMap parameterMap) throws IOException {
        logger.debug("new connection");
        String Origin = headers.get("Origin");
        if(StringUtils.isEmpty(Origin)){
            session.close();
            return;
        }
//        List<NameValuePair> nameValuePairs = URLEncodedUtils.parse(parameterMap.getParameter(key), CharsetUtil.CHARSET_UTF_8);
        Integer uid = null;
        String uidStr = parameterMap.getParameter(key);
        if(StrUtil.isEmpty(uidStr)){
            String token = parameterMap.getParameter(h5key);
            if(StrUtil.isEmpty(token)){
                return;
            }else {
                uid = CommonUtils.getUIdFromToken(token);
                map.put(uid,session);
            }
        }else {
            uid = Integer.valueOf(uidStr);
            map.put(uid,session);
            WsMsgDTO wsMsgDTO = new WsMsgDTO();
//        wsMsgDTO.setContent("【"+name+"】进来了，共【" + map.size() + "】人在线");
            wsMsgDTO.setContent("共【" + map.size() + "】人在线");
            wsMsgDTO.setUsername("");
            wsMsgDTO.setMsgType(5);
            for(Session sessionSingle : map.values()){
                sessionSingle.sendText(JSON.toJSONString(wsMsgDTO));
            }
        }
        session.setAttribute(key,uid);


    }

    @OnClose
    public void onClose(Session session) throws IOException {
        Integer uid = session.getAttribute(key);
        map.remove(uid);
        /*if(!map.isEmpty()){
            WsMsgDTO wsMsgDTO = new WsMsgDTO();
            wsMsgDTO.setContent("【"+name+"】离开了，共【" + map.size() + "】人在线");
            wsMsgDTO.setUsername(name);
            wsMsgDTO.setMsgType(5);
            for(Session sessionSingle : map.values()){
                sessionSingle.sendText(JSON.toJSONString(wsMsgDTO));
            }
        }*/
        session.close();
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        logger.error("websocket报错",throwable);
        Integer uid = session.getAttribute(key);
        map.remove(uid);
        session.close();
    }

    @OnMessage
    public void onMessage(Session session, String message) {
        try {
            if (StringUtils.isEmpty(message)) {
                return;
            }
            if (logger.isDebugEnabled()) {
                logger.debug("握手包:{}", JSON.toJSONString(session));
                logger.debug("收到ws消息:{}", message);
            }

            if (Objects.equals("心跳内容", message)) {
                return;
            }
            WsMsgDTO wsMsgDTO = JSON.parseObject(message, WsMsgDTO.class);
            if(wsMsgDTO.getMsgType() == 0){
                wsMsgDTO = new WsMsgDTO();
                //心跳返回给客户端 保持心跳
                wsMsgDTO.setMsgType(0);
                session.sendText(JSON.toJSONString(wsMsgDTO));
                //心跳
                return;
            }
            if (wsMsgDTO.getMsgType() == 3) {
                wsMsgDTO.setMsgType(1);
                SuperBeautyService superBeautyService = SpringUtils.getBean(SuperBeautyService.class);
                List<SuperBeauty> superBeautyList = superBeautyService.selectRandom(1);
                if (CollectionUtil.isEmpty(superBeautyList)) {
                    return;
                }
                wsMsgDTO.setContent("img[" + superBeautyList.get(0).getImgUrl() + "]");
                session.sendText(JSON.toJSONString(wsMsgDTO));
                return;
            }else if(wsMsgDTO.getMsgType() == 4){
                Session sessionTo = map.get(wsMsgDTO.getToUserId());
                if(sessionTo == null){
                    return;
                }
                sessionTo.sendText(JSON.toJSONString(wsMsgDTO));
                ChatMsgService chatMsgService = SpringUtils.getBean(ChatMsgService.class);
                ChatMsg chatMsg = new ChatMsg();
                chatMsg.setCreateTime(new Date());
                chatMsg.setMsg(wsMsgDTO.getContent());
                chatMsg.setUserId(CommonUtils.getUIdFromToken(wsMsgDTO.getToken()));
                chatMsg.setToUserId(wsMsgDTO.getToUserId());
                //ip获取 todo
                chatMsg.setIp("");
                chatMsgService.insert(chatMsg);
                return;
            }
            ChatMsgService chatMsgService = SpringUtils.getBean(ChatMsgService.class);
            if (StringUtils.length(wsMsgDTO.getContent()) > 100) {
                wsMsgDTO.setContent(StrUtil.sub(wsMsgDTO.getContent(), 0, 100));
            }
            //转义
            wsMsgDTO.setContent(EscapeUtil.escapeHtml4(wsMsgDTO.getContent()));
            StringBuilder content = new StringBuilder();

            content.append(wsMsgDTO.getContent());
            wsMsgDTO.setContent(content.toString());
            String msg = JSON.toJSONString(wsMsgDTO);
            for(Session sessionSingle : map.values()){
                sessionSingle.sendText(msg);
            }
            ChatMsg chatMsg = new ChatMsg();
            chatMsg.setCreateTime(new Date());
            chatMsg.setMsg(wsMsgDTO.getUsername() + "：" + content.toString()+ "("+ CommonUtils.getRegionByIp(wsMsgDTO.getIp())+")");
            chatMsg.setUserId(wsMsgDTO.getUserId());
            chatMsg.setIp(wsMsgDTO.getIp());
            chatMsg.setToUserId(0);
            chatMsgService.insert(chatMsg);
            return;
        } catch (Exception e) {
            logger.error("websocket聊天出错", e);
            return;
        }
    }

    @OnBinary
    public void onBinary(Session session, byte[] bytes) {
        for (byte b : bytes) {
            System.out.println(b);
        }
        session.sendBinary(bytes);
    }

    @OnEvent
    public void onEvent(Session session, Object evt) {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent idleStateEvent = (IdleStateEvent) evt;
            switch (idleStateEvent.state()) {
                case READER_IDLE:
                    System.out.println("read idle");
                    break;
                case WRITER_IDLE:
                    System.out.println("write idle");
                    break;
                case ALL_IDLE:
                    System.out.println("all idle");
                    break;
                default:
                    break;
            }
        }
    }

    public Set<Integer> getOnlineList() {
        return map.keySet();
    }

    public boolean isOnline(Integer userId) {
        return map.containsKey(userId);
    }
}
