package com.feiqu.quartz.util;


import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.feiqu.common.enums.MsgEnum;
import com.feiqu.common.enums.YesNoEnum;
import com.feiqu.common.utils.SpringUtils;
import com.feiqu.system.model.CMessage;
import com.feiqu.system.service.mainData.CMessageService;

import java.util.Date;

/**
 * Created by chenweidong on 2018/1/16.
 */
public class CommonUtils {

    private static Log logger = LogFactory.get();

    public static void sendOfficialMsg(Integer receiveUserId, Date time, String content){
        CMessage message = new CMessage();
        message.setPostUserId(-1);
        message.setCreateTime(time == null?new Date():time);
        message.setDelFlag(YesNoEnum.NO.getValue());
        message.setReceivedUserId(receiveUserId);
        message.setType(MsgEnum.OFFICIAL_MSG.getValue());
        message.setContent(content);
        CMessageService messageService = SpringUtils.getBean(CMessageService.class);
        messageService.insert(message);
    }

}
