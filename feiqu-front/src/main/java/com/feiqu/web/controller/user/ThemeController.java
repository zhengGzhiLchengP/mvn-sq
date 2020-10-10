package com.feiqu.web.controller.user;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.feiqu.common.base.BaseResult;
import com.feiqu.common.base.FqResult;
import com.feiqu.common.enums.*;
import com.feiqu.common.utils.SpringUtils;
import com.feiqu.framwork.constant.CommonConstant;
import com.feiqu.framwork.util.CommonUtils;
import com.feiqu.framwork.util.WebUtil;
import com.feiqu.framwork.web.base.BaseController;
import com.feiqu.system.model.*;
import com.feiqu.system.model.mainData.FqTheme;
import com.feiqu.system.pojo.cache.FqUserCache;
import com.feiqu.system.pojo.condition.ThemeCondition;
import com.feiqu.system.pojo.response.DetailCommentResponse;
import com.feiqu.system.pojo.response.DetailReplyResponse;
import com.feiqu.system.pojo.response.FqThemeUserResponse;
import com.feiqu.system.service.mainData.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.HtmlUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

/**
 * themeController
 *
 * @author chenweidong
 * @date 2017/11/23
 */
@RequestMapping("theme")
@Controller
public class ThemeController extends BaseController {

    private final static Logger logger = LoggerFactory.getLogger(ThemeController.class);

    @Resource
    private FqUserService userService;
    @Resource
    private FqLabelService labelService;
    @Resource
    private FqThemeService themeService;
    @Resource
    private WebUtil webUtil;
    @Resource
    private GeneralCommentService commentService;
    @Resource
    private GeneralReplyService replyService;
    @Resource
    private CMessageService messageService;

    @GetMapping("list")
    @ResponseBody
    public Object list(@RequestParam(defaultValue = "0") Integer index,
                       @RequestParam(defaultValue = "10") Integer size) {
        BaseResult result = new BaseResult();
        try {
            PageHelper.startPage(index, size);
            ThemeCondition condition = new ThemeCondition();
            condition.setStatus(CommonStatusEnum.AUTHED.getValue());
            condition.setOrderByClause("id desc");
            List<FqThemeUserResponse> list = themeService.selectWithUserByExample(condition);
            PageInfo page = new PageInfo(list);
            result.setData(page);
        } catch (Exception e) {
            logger.error("error", e);
            result.setCode("1");
        }
        return result;
    }

    @RequestMapping("/my")
    public String my(@RequestParam(defaultValue = "0") Integer index,
                     @RequestParam(defaultValue = "10") Integer size, Model model) {
        FqUserCache fqUserCache = getCurrentUser();
        PageHelper.startPage(index, size);
        ThemeCondition themeCondition = new ThemeCondition();
        themeCondition.setUserId(fqUserCache.getId());
        themeCondition.setStatus(CommonStatusEnum.AUTHED.getValue());
        themeCondition.setOrderByClause("create_time desc");
        List<FqThemeUserResponse> list = themeService.selectWithUserByExample(themeCondition);
        PageInfo page = new PageInfo(list);
        model.addAttribute("page", page);
        return "/theme/myThemes";
    }

    @GetMapping("index")
    public String index(Model model, String order, String label) {
        return page(model, 0, order, label);
    }

    @GetMapping("/index/{page}")
    public String page(Model model, @PathVariable Integer page, String order, String label) {
       /* FqLabelExample labelExample = new FqLabelExample();
        labelExample.createCriteria().andDelFlagEqualTo(YesNoEnum.NO.getValue()).andTypeEqualTo(TopicTypeEnum.BBS_TYPE.getValue());
        List<FqLabel> labelList = labelService.selectByExample(labelExample);
        request.setAttribute("labelList",labelList);
        request.setAttribute("areas",CommonConstant.AREA_LIST);*/

        PageHelper.startPage(page, CommonConstant.DEAULT_PAGE_SIZE, true);
        ThemeCondition condition = new ThemeCondition();
        if ("comment".equals(order)) {
            condition.setOrderByClause("comment_count desc");
        } else {
            condition.setOrderByClause("create_time desc");
        }
        model.addAttribute("order", order);
        condition.setStatus(CommonStatusEnum.AUTHED.getValue());
        List<FqThemeUserResponse> themes = themeService.selectWithUserByExample(condition);
        PageInfo pageInfo = new PageInfo(themes);
        model.addAttribute("themes", themes);
        model.addAttribute("count", pageInfo.getTotal());
        model.addAttribute("page", page);
        model.addAttribute("pageSize", CommonConstant.DEAULT_PAGE_SIZE);
        return "/theme/index";
    }

    @Resource
    private FqUserService fqUserService;

    /**
     * 移动版发帖
     * @param theme
     * @return
     */
    @ResponseBody
    @PostMapping(value = "/h5/save")
    public Object h5pubTheme(@RequestBody FqTheme theme) {
        Integer userId = 0;
        try {
            FqUserCache fqUserCache = null;
            if(StringUtils.isEmpty(theme.getToken())){
                fqUserCache = getCurrentUser();
                if(fqUserCache == null){
                    return error("用户未登录");
                }
            }else {
                userId = CommonUtils.getUIdFromToken(theme.getToken());
                if (userId <= 0) {
                    return error("用户未登录");
                }
                FqUser fqUser = fqUserService.selectByPrimaryKey(userId);
                if (fqUser == null) {
                    return error("用户未查到");
                }
                fqUserCache = new FqUserCache(fqUser);
            }

            if (theme.getId() != null) {
                themeService.updateByPrimaryKeySelective(theme);
                return success();
            }
            userId = fqUserCache.getId();
            theme.setUserId(userId);
            if (StringUtils.isBlank(theme.getTitle())) {
                theme.setTitle(StrUtil.sub(theme.getContent(),0,30));
            }
            Date now = new Date();
            theme.setCreateTime(now);
            theme.setPicPrefix("");
            //如果是admin角色的 就默认审核通过
            /*if (UserRoleEnum.ADMIN_USER_ROLE.getValue().equals(fqUserCache.getRole())) {
                theme.setStatus(CommonStatusEnum.AUTHED.getValue());
            } else {
                theme.setStatus(CommonStatusEnum.AUTHING.getValue());//默认 审核中
            }*/
            theme.setStatus(CommonStatusEnum.AUTHED.getValue());
//            theme.setArea("");
            theme.setLabel("");
            themeService.insert(theme);
            String content = "系统消息通知：用户：" + fqUserCache.getNickname() + ":发表了一篇帖子（<a class=\"c-fly-link\" href=\"" + CommonConstant.DOMAIN_URL +
                    "/theme/" + theme.getId() + "\">" + theme.getTitle() + "</a>），等待你的审核。" + DateUtil.formatDateTime(now);
            if (CommonStatusEnum.AUTHING.getValue().equals(theme.getStatus())) {
                CommonUtils.sendMsg(MsgEnum.OFFICIAL_MSG.getValue(), 22, now, content);
            }
            return success();
        } catch (Exception e) {
            logger.error("postbbs error,userId:" + userId, e);
            return error();
        }
    }

    /**
     * 发帖
     * @param theme
     * @return
     */
    @ResponseBody
    @PostMapping(value = "pubTheme")
    public Object pubTheme(FqTheme theme) {
        BaseResult result = new BaseResult();
        Integer userId = 0;
        try {
            FqUserCache user = getCurrentUser();
            if (user == null || user.getId() == null) {
                result.setResult(ResultEnum.USER_NOT_LOGIN);
                return result;
            }
            if (theme.getId() != null) {
                themeService.updateByPrimaryKeySelective(theme);
                return result;
            }
            userId = user.getId();
            theme.setUserId(userId);
            if (StringUtils.isBlank(theme.getTitle())) {
                result.setResult(ResultEnum.PARAM_NULL);
                return result;
            }
            theme.setTitle(HtmlUtils.htmlEscape(theme.getTitle()));
            Date now = new Date();
            theme.setCreateTime(now);
            //如果是admin角色的 就默认审核通过
            if (UserRoleEnum.ADMIN_USER_ROLE.getValue().equals(user.getRole())) {
                theme.setStatus(CommonStatusEnum.AUTHED.getValue());
            } else {
                theme.setStatus(CommonStatusEnum.AUTHING.getValue());//默认 审核中
            }

//            theme.setArea("");
            theme.setLabel("");
            themeService.insert(theme);
            result.setResult(ResultEnum.SUCCESS);

            if (CommonStatusEnum.AUTHING.getValue().equals(theme.getStatus())) {
                //发送消息
                ThreadPoolTaskExecutor executor = SpringUtils.getBean(ThreadPoolTaskExecutor.class);
                executor.execute(() -> {
                    FqUserExample example = new FqUserExample();
                    example.createCriteria().andRoleEqualTo(UserRoleEnum.ADMIN_USER_ROLE.getValue());
                    List<FqUser> fqUsers = userService.selectByExample(example);
                    String content = "系统消息通知：用户：" + user.getNickname() + ":发表了一篇帖子（<a class=\"c-fly-link\" href=\"" + CommonConstant.DOMAIN_URL +
                            "/theme/" + theme.getId() + "\">" + theme.getTitle() + "</a>），等待你的审核。" + DateUtil.formatDateTime(now);
                    fqUsers.forEach(fqUser -> {
                        CommonUtils.sendMsg(MsgEnum.OFFICIAL_MSG.getValue(), fqUser.getId(), now, content);
                    });
                });
            }
        } catch (Exception e) {
            logger.error("postbbs error,userId:" + userId, e);
            result.setResult(ResultEnum.SYSTEM_ERROR);
        }
        return result;
    }

    @PostMapping(value = "/{themeId}")
    @ResponseBody
    public Object detail(@PathVariable Integer themeId) {
        FqTheme theme = themeService.selectByPrimaryKey(themeId);
        return success(theme);
    }

    @GetMapping(value = "/{themeId}")
    public Object themeDetail(@PathVariable Integer themeId, HttpServletRequest request) {
        return themeDetailPage(themeId, 0, request);
    }

    @GetMapping(value = "/detail/{themeId}")
    @ResponseBody
    public Object themeDetail1(@PathVariable Integer themeId) {
        FqTheme theme = themeService.selectByPrimaryKey(themeId);
        if (theme == null || theme.getStatus() == CommonStatusEnum.DEL.getValue()) {
            return error("未查询到帖子");
        }
        return success(theme);
    }

    @GetMapping(value = "/{themeId}/page/{page}")
    public Object themeDetailPage(@PathVariable Integer themeId, @PathVariable Integer page, HttpServletRequest request) {
        try {
            FqTheme theme = themeService.selectByPrimaryKey(themeId);
            if (theme == null) {
                return GENERAL_NOT_FOUNF_404_URL;
            }
            request.setAttribute("theme", theme);
            request.setAttribute("louzhu", userService.selectByPrimaryKey(theme.getUserId()));
            theme.setSeeCount(theme.getSeeCount() == null ? 0 : theme.getSeeCount() + 1);
            themeService.updateByPrimaryKey(theme);
            request.setAttribute("page", page);

            GeneralCommentExample commentExample = new GeneralCommentExample();
            commentExample.createCriteria().andTopicIdEqualTo(themeId).andTopicTypeEqualTo(TopicTypeEnum.BBS_TYPE.getValue())
                    .andDelFlagEqualTo(YesNoEnum.NO.getValue());
            PageHelper.startPage(page, CommonConstant.DEAULT_PAGE_SIZE, true);
            List<DetailCommentResponse> comments = commentService.selectUserByExample(commentExample);
            PageInfo pageInfo = new PageInfo(comments);
            request.setAttribute("comments", comments);
            request.setAttribute("count", pageInfo.getTotal());
            request.setAttribute("pageSize", CommonConstant.DEAULT_PAGE_SIZE);
            GeneralReplyExample replyExample = new GeneralReplyExample();
            for (DetailCommentResponse comment : comments) {
                replyExample.clear();
                replyExample.createCriteria().andCommentIdEqualTo(comment.getId());
                List<DetailReplyResponse> replyList = replyService.selectWithUserByExample(replyExample);
                comment.setReplyList(replyList);
            }
        } catch (Exception e) {
            logger.error("themeDetail error themeId:" + themeId, e);
        }
        return "/theme/themeDetail";
    }

    @ResponseBody
    @PostMapping(value = "/comment")
    public Object comment(@RequestBody GeneralComment comment) {
        BaseResult result = new BaseResult();
        try {
            logger.info("评论内容：{}", JSON.toJSONString(comment));
            FqUserCache user = getCurrentUser();
            if (user == null || user.getId() == null) {
                result.setResult(ResultEnum.USER_NOT_LOGIN);
                return result;
            }
            if (comment.getContent().length() > 500) {
                return error("评论内容长度不能超过500！");
            }
            comment.setPostUserId(user.getId());
            if (comment.getContent().length() > 500) {
                result.setResult(ResultEnum.STR_LENGTH_TOO_LONG);
                return result;
            }
            FqTheme theme = themeService.selectByPrimaryKey(comment.getTopicId());
            if (theme == null) {
                result.setResult(ResultEnum.PARAM_NULL);
                return result;
            }
            FqTheme themeUpdate = new FqTheme();
            themeUpdate.setId(theme.getId());
            themeUpdate.setCommentCount(theme.getCommentCount() == null ? 1 : theme.getCommentCount() + 1);
            themeService.updateByPrimaryKeySelective(themeUpdate);
            Date now = new Date();
            comment.setCreateTime(now);
            comment.setTopicType(TopicTypeEnum.BBS_TYPE.getValue());
            comment.setDelFlag(YesNoEnum.NO.getValue());
            commentService.insert(comment);

            if (!theme.getUserId().equals(user.getId())) {
                String content = new StringBuilder().append("系统消息通知：<a class=\"c-fly-link\" href=\"").append(CommonConstant.DOMAIN_URL).append("/u/").append(user.getId()).append("\" target=\"_blank\">").append(user.getNickname()).append(" </a> ").append("回复了<a class=\"c-fly-link\" href=\"").append(CommonConstant.DOMAIN_URL).append("/theme/").append(theme.getId()).append("\" target=\"_blank\">你的帖子（").append(theme.getTitle()).append("）</a> ").append(DateUtil.formatDateTime(now)).toString();
                CommonUtils.sendMsg(MsgEnum.OFFICIAL_MSG.getValue(), theme.getUserId(), now, content);
            }

            result.setData(comment);
        } catch (Exception e) {
            logger.error("评论失败", e);
            result.setResult(ResultEnum.FAIL);
        }
        return result;
    }

    @PostMapping("/delete/{themeId}")
    @ResponseBody
    public Object deleteTheme(HttpServletRequest request, HttpServletResponse response, @PathVariable Integer themeId) {
        BaseResult result = new BaseResult();
        try {
            FqUserCache fqUserCache = webUtil.currentUser(request, response);
            if (fqUserCache == null) {
                result.setResult(ResultEnum.USER_NOT_LOGIN);
                return result;
            }
            FqTheme theme = themeService.selectByPrimaryKey(themeId);
            if (theme == null) {
                result.setResult(ResultEnum.PARAM_NULL);
                return result;
            }
            if (!UserRoleEnum.ADMIN_USER_ROLE.getValue().equals(fqUserCache.getRole())) {
                result.setResult(ResultEnum.USER_NOT_AUTHORIZED);
                return result;
            }
            FqTheme toUpdate = new FqTheme();
            toUpdate.setId(themeId);
            toUpdate.setStatus(CommonStatusEnum.DEL.getValue());
            themeService.updateByPrimaryKeySelective(toUpdate);
            logger.info("帖子删除成功，帖子id :{},用户id：{} ", themeId, fqUserCache.getId());
        } catch (Exception e) {
            logger.error("删除帖子失败，帖子id : " + themeId, e);
        }
        return result;
    }

    @GetMapping("/theme/list/{userId}")
    @ResponseBody
    public Object themes(@PathVariable Integer userId) {
        BaseResult result = new BaseResult();
        ThemeCondition condition = new ThemeCondition();
        condition.setStatus(CommonStatusEnum.AUTHED.getValue());
        condition.setUserId(userId);
        List<FqThemeUserResponse> themes = themeService.selectWithUserByExample(condition);
        result.setData(themes);
        return result;
    }

    @PostMapping("/applyToDelete/{themeId}")
    @ResponseBody
    public Object applyToDelete(@PathVariable Integer themeId, HttpServletRequest request, HttpServletResponse response) {
        BaseResult result = new BaseResult();
        FqUserCache fqUser = webUtil.currentUser(request, response);

        FqTheme theme = themeService.selectByPrimaryKey(themeId);
        if (!theme.getUserId().equals(fqUser.getId())) {
            result.setResult(ResultEnum.DELETE_NOT_MY);
            return result;
        }

        FqUserExample example = new FqUserExample();
        example.createCriteria().andRoleEqualTo(UserRoleEnum.ADMIN_USER_ROLE.getValue());
        List<FqUser> officials = userService.selectByExample(example);

        for (FqUser user : officials) {
            CMessage message = new CMessage();
            message.setPostUserId(-1);
            message.setCreateTime(new Date());
            message.setDelFlag(YesNoEnum.NO.getValue());
            message.setReceivedUserId(user.getId());
            message.setType(MsgEnum.OFFICIAL_MSG.getValue());
            message.setContent("系统消息通知：<a class=\"c-fly-link\" href=\"" + CommonConstant.DOMAIN_URL + "/u/" + fqUser.getId() + "\" target=\"_blank\">" + fqUser.getNickname() + " </a> " +
                    "申请删除帖子，帖子id： " + theme.getId() + ",帖子标题：" + theme.getTitle() + ",时间" + DateUtil.formatDateTime(new Date()));
            messageService.insert(message);
        }
        return result;
    }
    @Resource
    private GeneralLikeService likeService;

    @PostMapping("like")
    @ResponseBody
    public Object like(@RequestBody FqTheme fqLolitaDTO){
        FqResult result = new FqResult();
        try {
            Integer userId = CommonUtils.getUIdFromToken(fqLolitaDTO.getToken());
            if (userId == null || userId <= 0) {
                return result.fail("用户未登录");
            }
            FqUser fqUser = fqUserService.selectByPrimaryKey(userId);
            if (fqUser == null) {
                return result.fail("用户未查到");
            }
            FqTheme theme = themeService.selectByPrimaryKey(fqLolitaDTO.getId());
            if(theme == null){
                result.fail("参数不能为空");
                return result;
            }
            GeneralLikeExample likeExample = new GeneralLikeExample();
            likeExample.createCriteria().andPostUserIdEqualTo(userId)
                    .andTopicIdEqualTo(fqLolitaDTO.getId().intValue()).andTopicTypeEqualTo(TopicTypeEnum.BBS_TYPE.getValue())
                    .andDelFlagEqualTo(YesNoEnum.NO.getValue());
            GeneralLike like = likeService.selectFirstByExample(likeExample);
            if(like!=null && like.getLikeValue()==1){
                result.fail("已经点过赞了");
                return result;
            }
            like = new GeneralLike(fqLolitaDTO.getId().intValue(), TopicTypeEnum.BBS_TYPE.getValue(),1,userId,new Date(),YesNoEnum.NO.getValue());
            likeService.insert(like);
            FqTheme toUpdate = new FqTheme();
            toUpdate.setId(theme.getId());
            toUpdate.setLikeCount(theme.getLikeCount() == null? 1: theme.getLikeCount()+1);
            themeService.updateByPrimaryKeySelective(toUpdate);
            result.setData(toUpdate.getLikeCount());
            CommonUtils.addActiveNum(userId, ActiveNumEnum.POST_LIKE.getValue());
        } catch (Exception e) {
            logger.error("thought like error",e);
            result.setResult(ResultEnum.FAIL);
        }
        return result;
    }
}
