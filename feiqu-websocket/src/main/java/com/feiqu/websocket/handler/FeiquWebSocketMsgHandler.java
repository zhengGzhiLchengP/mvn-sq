package com.feiqu.websocket.handler;//package com.feiqu.web.websocket.handler;
//
//import cn.hutool.core.collection.CollectionUtil;
//import cn.hutool.core.date.DateField;
//import cn.hutool.core.date.DateUtil;
//import cn.hutool.core.util.EscapeUtil;
//import cn.hutool.core.util.StrUtil;
//import com.alibaba.fastjson.JSON;
//import com.feiqu.common.constant.BizConstant;
//import com.feiqu.common.utils.SpringUtils;
//import com.feiqu.framwork.constant.CommonConstant;
//import com.feiqu.framwork.util.CommonUtils;
//import com.feiqu.system.model.SuperBeauty;
//import com.feiqu.system.model.mainData.ChatMsg;
//import com.feiqu.system.model.mainData.ChatMsgExample;
//import com.feiqu.system.service.mainData.ChatMsgService;
//import com.feiqu.system.service.mainData.SuperBeautyService;
//import com.feiqu.web.dto.WsMsgDTO;
//import org.apache.commons.lang.StringUtils;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.tio.core.ChannelContext;
//import org.tio.core.Tio;
//import org.tio.http.common.HttpRequest;
//import org.tio.http.common.HttpResponse;
//import org.tio.websocket.common.WsRequest;
//import org.tio.websocket.common.WsResponse;
//import org.tio.websocket.common.WsSessionContext;
//import org.tio.websocket.server.handler.IWsMsgHandler;
//import org.tio.websocket.starter.TioWebSocketServerBootstrap;
//
//import java.util.Date;
//import java.util.List;
//import java.util.Objects;
//
//public class FeiquWebSocketMsgHandler implements IWsMsgHandler {
//    private static Logger logger = LoggerFactory.getLogger(FeiquWebSocketMsgHandler.class);
//
//    private String allGroup = "all-group";
//
//    @Override
//    public HttpResponse handshake(HttpRequest httpRequest, HttpResponse httpResponse, ChannelContext channelContext) throws Exception {
//        String clientip = httpRequest.getClientIp();
//        String myname = httpRequest.getParam("name");
////        String icon = httpRequest.getParam("icon");
//        if(myname.length() > 10){
//            Tio.close(channelContext,"名称长度太长，有问题");
//            return null;
//        }
//
//        Tio.bindUser(channelContext, myname);
////		channelContext.setUserid(myname);
//        logger.info("收到来自{}的ws握手包\r\n{}", clientip, httpRequest.toString());
//        return httpResponse;
//    }
//
//    @Override
//    public void onAfterHandshaked(HttpRequest httpRequest, HttpResponse httpResponse, ChannelContext channelContext) throws Exception {
//        //绑定到群组，后面会有群发
//        Tio.bindGroup(channelContext, CommonConstant.GROUP_ID);
//        int count = Tio.getAllChannelContexts(channelContext.groupContext).getObj().size();
//
//        WsMsgDTO wsMsgDTO = new WsMsgDTO();
//        wsMsgDTO.setContent(channelContext.userid + " 进来了，共【" + count + "】人在线");
//        wsMsgDTO.setUsername(channelContext.userid);
//        wsMsgDTO.setMsgType(1);
//        //用tio-websocket，服务器发送到客户端的Packet都是WsResponse
//        WsResponse wsResponse = WsResponse.fromText(JSON.toJSONString(wsMsgDTO), CommonConstant.CHARSET);
//        //群发
//        Tio.sendToGroup(channelContext.groupContext, CommonConstant.GROUP_ID, wsResponse);
//    }
//
//    @Override
//    public Object onBytes(WsRequest wsRequest, byte[] bytes, ChannelContext channelContext) throws Exception {
//        return null;
//    }
//
//    @Override
//    public Object onClose(WsRequest wsRequest, byte[] bytes, ChannelContext channelContext) throws Exception {
//        Tio.remove(channelContext, "receive close flag");
//        return null;
//    }
//
//    @Override
//    public Object onText(WsRequest wsRequest, String text, ChannelContext channelContext) throws Exception {
//        try {
//            if (StringUtils.isEmpty(text)) {
//                return null;
//            }
//            WsSessionContext wsSessionContext = (WsSessionContext) channelContext.getAttribute();
//            HttpRequest httpRequest = wsSessionContext.getHandshakeRequest();//获取websocket握手包
//            if (logger.isDebugEnabled()) {
//                logger.debug("握手包:{}", httpRequest);
//                logger.debug("收到ws消息:{}", text);
//            }
//
//            if (Objects.equals("心跳内容", text)) {
//                return null;
//            }
//            Date now = new Date();
//
//            WsMsgDTO wsMsgDTO = JSON.parseObject(text, WsMsgDTO.class);
//            if (wsMsgDTO.getMsgType() == 3) {
//                wsMsgDTO.setMsgType(1);
//                SuperBeautyService superBeautyService = SpringUtils.getBean(SuperBeautyService.class);
//                List<SuperBeauty> superBeautyList = superBeautyService.selectRandom(1);
//                if (CollectionUtil.isEmpty(superBeautyList)) {
//                    return null;
//                }
//                wsMsgDTO.setContent("<img src='" + superBeautyList.get(0).getImgUrl() + "'/>");
//                return JSON.toJSONString(wsMsgDTO);
//            }
//            ChatMsgService chatMsgService = SpringUtils.getBean(ChatMsgService.class);
//            ChatMsgExample chatMsgExample = new ChatMsgExample();
//            Date before = DateUtil.offset(now, DateField.MINUTE,-5);
//            chatMsgExample.createCriteria().andCreateTimeGreaterThanOrEqualTo(before).andIpEqualTo(wsMsgDTO.getIp());
//            int count = chatMsgService.countByExample(chatMsgExample);
//            if(count > BizConstant.WS_SEND_MAX_COUNT){
//                wsMsgDTO.setMsgType(1);
//                wsMsgDTO.setContent("用户："+wsMsgDTO.getUsername()+"，因评论频率过高已被系统禁言，解禁请联系管理员");
//                String msg = JSON.toJSONString(wsMsgDTO);
//                //用tio-websocket，服务器发送到客户端的Packet都是WsResponse
//                WsResponse wsResponse = WsResponse.fromText(msg, CommonConstant.CHARSET);
//                Tio.sendToGroup(channelContext.groupContext, CommonConstant.GROUP_ID, wsResponse);
//                SpringUtils.getBean(TioWebSocketServerBootstrap.class).getServerGroupContext().ipBlacklist.add(wsMsgDTO.getIp());
//                return null;
//            }
//            if (StringUtils.length(wsMsgDTO.getContent()) > 100) {
//                wsMsgDTO.setContent(StrUtil.sub(wsMsgDTO.getContent(), 0, 100));
//            }
//            //转义
//            wsMsgDTO.setContent(EscapeUtil.escapeHtml4(wsMsgDTO.getContent()));
//            StringBuilder content = new StringBuilder();
//
//            content.append(wsMsgDTO.getContent());
//            wsMsgDTO.setContent(content.toString());
//            String msg = JSON.toJSONString(wsMsgDTO);
//            //用tio-websocket，服务器发送到客户端的Packet都是WsResponse
//            WsResponse wsResponse = WsResponse.fromText(msg, CommonConstant.CHARSET);
//            //群发
//            Tio.sendToGroup(channelContext.groupContext, CommonConstant.GROUP_ID, wsResponse);
//
//            ChatMsg chatMsg = new ChatMsg();
//            chatMsg.setCreateTime(new Date());
//            chatMsg.setMsg(wsMsgDTO.getUsername() + "：" + content.toString()+ "("+CommonUtils.getRegionByIp(wsMsgDTO.getIp())+")");
//            chatMsg.setUserId(wsMsgDTO.getUserId());
//            chatMsg.setIp(wsMsgDTO.getIp());
//            chatMsgService.insert(chatMsg);
//            //返回值是要发送给客户端的内容，一般都是返回null
//            return null;
//        } catch (Exception e) {
//            logger.error("websocket聊天出错", e);
//            return null;
//        }
//    }
//}
