package com.feiqu.admin.controller.front;

import com.feiqu.adminFramework.web.base.BaseController;
import com.feiqu.common.base.BaseResult;
import com.feiqu.common.enums.MsgEnum;
import com.feiqu.common.enums.OrderEnum;
import com.feiqu.common.enums.ResultEnum;
import com.feiqu.common.enums.YesNoEnum;
import com.feiqu.system.model.CMessage;
import com.feiqu.system.model.CMessageExample;
import com.feiqu.system.model.FqUserExample;
import com.feiqu.system.pojo.cache.FqUserCache;
import com.feiqu.system.pojo.response.Dialog;
import com.feiqu.system.pojo.response.MessageUserDetail;
import com.feiqu.system.service.mainData.CMessageService;
import com.feiqu.system.service.mainData.FqUserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * MessageController
 *
 * @author chenweidong
 * @date 2017/11/21
 */
@RequestMapping("/front/message")
@Controller
public class MessageController extends BaseController {
    private final static Logger logger = LoggerFactory.getLogger(MessageController.class);

    @Resource
    private CMessageService messageService;


    @GetMapping("")
    public String manage() {
        return "/front/message/manage";
    }

    @GetMapping("/manage/list")
    @ResponseBody
    public Object list(Integer page, Integer limit) {
        BaseResult result = new BaseResult();
        try {
            PageHelper.startPage(page, limit);
            CMessageExample example = new CMessageExample();
            example.setOrderByClause("create_time desc");
            List<CMessage> messages = messageService.selectByExample(example);
            PageInfo pageInfo = new PageInfo(messages);
            result.setData(pageInfo);
        } catch (Exception e) {
            logger.error("信息分页出错", e);
            result.setResult(ResultEnum.SYSTEM_ERROR);
        }
        return result;
    }

    @PostMapping("/manage/delete")
    @ResponseBody
    public Object delete(Integer id) {
        BaseResult result = new BaseResult();
        try {
            CMessage cMessage = new CMessage();
            cMessage.setId(id);
            cMessage.setDelFlag(YesNoEnum.YES.getValue());
            messageService.updateByPrimaryKeySelective(cMessage);
        } catch (Exception e) {
            logger.error("信息删除出错", e);
            result.setResult(ResultEnum.SYSTEM_ERROR);
        }
        return result;
    }
}
