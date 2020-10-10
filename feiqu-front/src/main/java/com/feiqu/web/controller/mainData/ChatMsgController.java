package com.feiqu.web.controller.mainData;


import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import com.feiqu.common.base.AjaxResult;
import com.feiqu.common.base.BaseResult;
import com.feiqu.common.enums.ResultEnum;
import com.feiqu.framwork.util.CommonUtils;
import com.feiqu.framwork.web.base.BaseController;
import com.feiqu.system.model.mainData.ChatMsg;
import com.feiqu.system.model.mainData.ChatMsgExample;
import com.feiqu.system.pojo.cache.FqUserCache;
import com.feiqu.system.service.mainData.ChatMsgService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;


/**
 * ChatMsgcontroller
 * Created by cwd on 2019/6/23.
 */
@Controller
@RequestMapping("/chatMsg")
public class ChatMsgController extends BaseController {

    private static Logger logger = LoggerFactory.getLogger(ChatMsgController.class);

    @Resource
    private ChatMsgService chatMsgService;


    /**
     * 跳转到ChatMsg管理页面
     */
    @RequestMapping("manage")
    public String manage() {
        return "/chatMsg/manage";
    }

    /**
     * 跳转到ChatMsg首页
     */
    @RequestMapping("")
    public String index() {
        return "/chatMsg/index";
    }

    /**
     * 添加ChatMsg页面
     */
    @RequestMapping("/chatMsg_add")
    public String chatMsg_add() {
        return "/chatMsg/add";
    }

    /**
     * ajax删除ChatMsg
     */
    @ResponseBody
    @RequestMapping("/delete")
    public Object delete(@RequestParam Long id) {
        BaseResult result = new BaseResult();
        try {
            chatMsgService.deleteByPrimaryKey(id);
        } catch (Exception e) {
            logger.error("error", e);
            result.setCode("1");
        }
        return result;
    }

    /**
     * 更新ChatMsg页面
     */
    @RequestMapping("/edit/{chatMsgId}")
    public Object chatMsgEdit(@PathVariable Long chatMsgId, Model model) {
        ChatMsg chatMsg = chatMsgService.selectByPrimaryKey(chatMsgId);
        model.addAttribute("chatMsg", chatMsg);
        return "/chatMsg/edit";
    }

    /**
     * ajax更新ChatMsg
     */
    @ResponseBody
    @PostMapping("/save")
    public Object save(ChatMsg chatMsg) {
        BaseResult result = new BaseResult();
        try {
            FqUserCache fqUserCache = getCurrentUser();
            if (fqUserCache == null) {
                result.setResult(ResultEnum.USER_NOT_LOGIN);
                return result;
            }
            if (chatMsg.getId() == null) {
                chatMsgService.insert(chatMsg);
            } else {
                chatMsgService.updateByPrimaryKeySelective(chatMsg);
            }
        } catch (Exception e) {
            logger.error("error", e);
            result.setCode("1");
        }
        return result;
    }


    /**
     * 查询ChatMsg首页
     */
    @GetMapping("list")
    @ResponseBody
    public Object list(@RequestParam(defaultValue = "0") Integer index,
                       @RequestParam(defaultValue = "10") Integer size,String token,Integer toUserId) {
        BaseResult result = new BaseResult();
        try {
            PageHelper.startPage(index, size,false);
            if(StringUtils.isNotEmpty(token) && toUserId != null){
                Integer userId = CommonUtils.getUIdFromToken(token);
                List<ChatMsg> chatMsgs = chatMsgService.findDialogs(userId,toUserId,"id desc");
                if(chatMsgs == null){
                    return success(null);
                }else {
                    chatMsgs.forEach(e->e.setMySend(userId.equals(e.getUserId())));
                    return success(chatMsgs);
                }
            }
            ChatMsgExample example = new ChatMsgExample();
            example.setOrderByClause("id desc");
            List<ChatMsg> list = chatMsgService.selectByExample(example);
            return success(list);
        } catch (Exception e) {
            logger.error("error", e);
            result.setCode("1");
        }
        return result;
    }

    /**
     * 查询ChatMsg首页
     */
    @GetMapping("history")
    @ResponseBody
    public Object history(@RequestParam(defaultValue = "0") Integer index,
                          @RequestParam(defaultValue = "10") Integer size, Long date) {
        try {
            if (size > 10) {
                size = 10;
            }
            PageHelper.startPage(index, size);
            ChatMsgExample example = new ChatMsgExample();
            example.setOrderByClause("id desc");
            example.createCriteria().andCreateTimeLessThan(new Date(date));
            List<ChatMsg> list = chatMsgService.selectByExample(example);
            if(CollectionUtil.isNotEmpty(list)){
                Date now = DateUtil.beginOfDay(new Date());
                list.forEach(chatMsg -> {
                    chatMsg.setCreateTimeStr(getTime(chatMsg.getCreateTime(),now));
                });
            }
            CollectionUtil.reverse(list);
            PageInfo page = new PageInfo(list);
            return AjaxResult.success(page);
        } catch (Exception e) {
            logger.error("error", e);
            return error();
        }
    }

    private String getTime(Date createTime, Date today) {
        if(createTime.after(today)){
            return DateUtil.format(createTime,"HH:mm:ss");
        }else {
            return DateUtil.format(createTime,"yyyy/MM/dd HH:mm:ss");
        }
    }
}