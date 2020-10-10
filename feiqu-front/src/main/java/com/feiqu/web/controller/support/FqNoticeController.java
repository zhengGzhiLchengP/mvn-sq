package com.feiqu.web.controller.support;

import cn.hutool.core.date.DateUtil;
import com.feiqu.common.base.BaseResult;
import com.feiqu.common.enums.*;
import com.feiqu.framwork.constant.CommonConstant;
import com.feiqu.framwork.util.CommonUtils;
import com.feiqu.framwork.util.WebUtil;
import com.feiqu.framwork.web.base.BaseController;
import com.feiqu.system.model.*;
import com.feiqu.system.pojo.cache.FqUserCache;
import com.feiqu.system.pojo.response.DetailCommentResponse;
import com.feiqu.system.service.mainData.CMessageService;
import com.feiqu.system.service.mainData.FqNoticeService;
import com.feiqu.system.service.mainData.FqUserService;
import com.feiqu.system.service.mainData.GeneralCommentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;


/**
* FqNoticecontroller
* Created by cwd on 2017/11/9.
*/
@Controller
@RequestMapping("/fqNotice")
public class FqNoticeController extends BaseController {

//    private static Logger logger = LoggerFactory.getLogger(FqNoticeController.class);

    @Resource
    private FqNoticeService fqNoticeService;
    @Resource
    private FqUserService fqUserService;
    @Resource
    private GeneralCommentService commentService;
    @Resource
    private WebUtil webUtil;
    @Resource
    private CMessageService messageService;

    /**
     * 查看FqNotice页面
     */
    @RequestMapping("/view/{fqNoticeId}")
    public Object fqNoticeView(@PathVariable Integer fqNoticeId, Model model) {

        FqNotice fqNotice = fqNoticeService.selectByPrimaryKey(fqNoticeId);
        if(fqNotice != null){
            FqUser fqUser = fqUserService.selectByPrimaryKey(fqNotice.getUserId());
            model.addAttribute("oUser",fqUser);
        }else {
            return "/404";
        }
        model.addAttribute("notice",fqNotice);
        FqNoticeExample example = new FqNoticeExample();
        example.createCriteria().andIsShowEqualTo(YesNoEnum.NO.getValue());
        example.setOrderByClause("fq_order desc");
        List<FqNotice> oldNotices = fqNoticeService.selectByExample(example);
        model.addAttribute("oldNotices",oldNotices);
        GeneralCommentExample commentExample = new GeneralCommentExample();
        commentExample.setOrderByClause("create_time");
        commentExample.createCriteria().andDelFlagEqualTo(YesNoEnum.NO.getValue())
                .andTopicIdEqualTo(fqNoticeId).andTopicTypeEqualTo(TopicTypeEnum.NOTICE_TYPE.getValue());
        List<DetailCommentResponse> comments = commentService.selectUserByExample(commentExample);
        model.addAttribute("commentList",comments);
        model.addAttribute("advertisements", getAdvertisements(AdvertisementPositionEnum.DETAIL));
        return "/front/notice/detail";
    }

    @ResponseBody
    @PostMapping(value = "comment")
    public Object articleReply(GeneralComment comment, Model model, HttpServletRequest request, HttpServletResponse response){
        BaseResult result = new BaseResult();
        try {
            FqUserCache user = webUtil.currentUser(request,response);
            if(user == null){
                result.setResult(ResultEnum.FAIL);
                return result;
            }
            FqNotice notice = fqNoticeService.selectByPrimaryKey(comment.getTopicId());
            if(notice == null){
                result.setResult(ResultEnum.PARAM_NULL);
                return result;
            }
            notice.setCommentNum(notice.getCommentNum() == null?1:notice.getCommentNum()+1);
            FqNotice toUpdate = new FqNotice();
            toUpdate.setId(notice.getId());
            toUpdate.setCommentNum(notice.getCommentNum());
            fqNoticeService.updateByPrimaryKeySelective(toUpdate);

            String commentContent= comment.getContent();
            if(org.apache.commons.lang3.StringUtils.isBlank(commentContent)){
                result.setResult(ResultEnum.PARAM_NULL);
                return result;
            }
            List<String> aiteNames = CommonUtils.findAiteNicknames(commentContent);
            if(aiteNames.size() > 0){
                for(String aiteNickname : aiteNames){
                    FqUserExample example = new FqUserExample();
                    example.createCriteria().andNicknameEqualTo(aiteNickname);
                    FqUser aiteUser = fqUserService.selectFirstByExample(example);
                    if (aiteUser != null && !aiteUser.getId().equals(user.getId())) {
                        CMessage message = new CMessage();
                        message.setPostUserId(-1);
                        message.setCreateTime(new Date());
                        message.setDelFlag(YesNoEnum.NO.getValue());
                        message.setReceivedUserId(aiteUser.getId());
                        message.setType(MsgEnum.OFFICIAL_MSG.getValue());
                        message.setContent("系统消息通知：<a class=\"c-fly-link\" href=\"" + CommonConstant.DOMAIN_URL + "/u/" + user.getId() + "\" target=\"_blank\">" + user.getNickname() + " </a> " +
                                "在此<a class=\"c-fly-link\" href=\"" + CommonConstant.DOMAIN_URL + "/notice/view/" + notice.getId() + "\" target=\"_blank\">通知</a>中回复了你 " + DateUtil.formatDateTime(new Date()));
                        messageService.insert(message);
                    }
                }
            }
            comment.setCreateTime(new Date());
            comment.setTopicType(TopicTypeEnum.NOTICE_TYPE.getValue());
            comment.setDelFlag(YesNoEnum.NO.getValue());
            comment.setPostUserId(user.getId());
            commentService.insert(comment);
            result.setData(comment);
        } catch (Exception e) {
            result.setResult(ResultEnum.FAIL);
        }
        return result;
    }

}