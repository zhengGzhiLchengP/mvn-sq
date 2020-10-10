package com.feiqu.system.mapper.mainData;

import com.feiqu.system.model.mainData.ChatMsg;
import com.feiqu.system.model.mainData.ChatMsgExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ChatMsgMapper {
    long countByExample(ChatMsgExample example);

    int deleteByExample(ChatMsgExample example);

    int deleteByPrimaryKey(Long id);

    int insert(ChatMsg record);

    int insertSelective(ChatMsg record);

    List<ChatMsg> selectByExample(ChatMsgExample example);

    ChatMsg selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") ChatMsg record, @Param("example") ChatMsgExample example);

    int updateByExample(@Param("record") ChatMsg record, @Param("example") ChatMsgExample example);

    int updateByPrimaryKeySelective(ChatMsg record);

    int updateByPrimaryKey(ChatMsg record);

    List<ChatMsg> findDialogs(@Param("userId") Integer userId,@Param("toUserId") Integer toUserId, @Param("orderBy") String orderBy);
}