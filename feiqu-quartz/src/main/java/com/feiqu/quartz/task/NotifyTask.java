package com.feiqu.quartz.task;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Validator;
import com.feiqu.common.enums.UserStatusEnum;
import com.feiqu.quartz.util.CommonUtils;
import com.feiqu.system.model.FqUser;
import com.feiqu.system.model.FqUserExample;
import com.feiqu.system.service.mainData.FqUserService;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Component("notifyTask")
public class NotifyTask {

    @Resource
    private FqUserService fqUserService;
    @Resource
    private JavaMailSenderImpl mailSender;
    //定时过生日通知 每天定时扫
    public void birthNotify(String msg){
        String dateStr = DateUtil.date().toString("-MM-dd");
        FqUserExample fqUserExample = new FqUserExample();
        fqUserExample.createCriteria().andBirthLike("%"+dateStr).andStatusEqualTo(UserStatusEnum.NORMAL.getValue());
        List<FqUser> fqUserList = fqUserService.selectByExample(fqUserExample);
        if(CollectionUtil.isNotEmpty(fqUserList)){
            fqUserList.forEach(fqUser -> {
                String msgSingle = "尊敬的 "+fqUser.getNickname()+":"+msg;
                String username = fqUser.getUsername();
                CommonUtils.sendOfficialMsg(fqUser.getId(), new Date(), msgSingle);
                if(Validator.isEmail(username)){
                    SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
                    simpleMailMessage.setFrom(mailSender.getUsername());
                    simpleMailMessage.setTo(username);
                    simpleMailMessage.setText(msgSingle);
                    simpleMailMessage.setSubject("飞趣用户生日-自动通知");
                    mailSender.send(simpleMailMessage);
                }
            });
        }
    }
}
