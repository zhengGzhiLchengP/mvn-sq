package com.feiqu.system.service.mainData;

import com.feiqu.system.base.BaseService;
import com.feiqu.system.model.mainData.ChatMsg;
import com.feiqu.system.model.mainData.ChatMsgExample;

import java.util.List;

/**
 * ChatMsgService接口
 * created by cwd on 2019/6/23.
 */
public interface ChatMsgService extends BaseService<ChatMsg, ChatMsgExample> {
    //查看这个人的聊天记录
    List<ChatMsg> findDialogs(Integer userId,Integer toUserId,String orderBy);

}