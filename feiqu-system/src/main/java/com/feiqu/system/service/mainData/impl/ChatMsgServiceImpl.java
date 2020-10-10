package com.feiqu.system.service.mainData.impl;

import com.feiqu.system.annotation.BaseService;
import com.feiqu.system.base.BaseServiceImpl;
import com.feiqu.system.mapper.mainData.ChatMsgMapper;
import com.feiqu.system.model.mainData.ChatMsg;
import com.feiqu.system.model.mainData.ChatMsgExample;
import com.feiqu.system.service.mainData.ChatMsgService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * ChatMsgService实现
 * Created by cwd on 2019/6/23.
 */
@Service
@Transactional
@BaseService
public class ChatMsgServiceImpl extends BaseServiceImpl<ChatMsgMapper, ChatMsg, ChatMsgExample> implements ChatMsgService {

    private static Logger _log = LoggerFactory.getLogger(ChatMsgServiceImpl.class);

    @Resource
    ChatMsgMapper chatMsgMapper;

    @Override
    public List<ChatMsg> findDialogs(Integer userId,Integer toUserId, String orderBy) {
        return chatMsgMapper.findDialogs(userId,toUserId,orderBy);
    }
}