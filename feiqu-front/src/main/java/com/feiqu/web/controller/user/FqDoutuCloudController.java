package com.feiqu.web.controller.user;


import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import com.alibaba.fastjson.JSON;
import com.feiqu.common.annotation.RepeatSubmit;
import com.feiqu.common.base.BaseResult;
import com.feiqu.common.enums.*;
import com.feiqu.common.utils.EmojiUtils;
import com.feiqu.framwork.constant.CommonConstant;
import com.feiqu.framwork.support.cache.CacheManager;
import com.feiqu.framwork.util.CommonUtils;
import com.feiqu.framwork.util.WebUtil;
import com.feiqu.framwork.web.base.BaseController;
import com.feiqu.system.model.*;
import com.feiqu.system.pojo.cache.FqUserCache;
import com.feiqu.system.pojo.condition.CommonCondition;
import com.feiqu.system.pojo.response.DetailCommentResponse;
import com.feiqu.system.pojo.response.FqDoutuCloudWithUser;
import com.feiqu.system.service.mainData.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang.StringUtils;
import org.assertj.core.util.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;


/**
 * FqDoutuCloudcontroller
 * Created by cwd on 2019/3/6.
 */
@Controller
@RequestMapping("/fqDoutuCloud")
public class FqDoutuCloudController extends BaseController {

    private static Logger logger = LoggerFactory.getLogger(FqDoutuCloudController.class);

    /**
     * 斗图功能
     */
    @Resource
    private FqDoutuCloudService fqDoutuCloudService;
    /**
     * 点赞功能
     */
    @Resource
    private GeneralLikeService likeService;
    /**
     * 收藏功能
     */
    @Resource
    private FqCollectService fqCollectService;
    @Resource
    private CMessageService messageService;
    /**
     * 评论service
     */
    @Resource
    private GeneralCommentService commentService;
    /**
     * 用户service
     */
    @Resource
    private FqUserService fqUserService;

    @PostMapping("/collect/{type}")
    @ResponseBody
    public Object collect(@PathVariable String type, Long id, HttpServletRequest request, HttpServletResponse response){
        BaseResult result = new BaseResult();
        try {
            FqUserCache fqUser = getCurrentUser();
            if(fqUser == null){
                result.setResult(ResultEnum.USER_NOT_LOGIN);
                return result;
            }
            FqDoutuCloud fqDoutuCloud = fqDoutuCloudService.selectByPrimaryKey(id);
            if(fqDoutuCloud == null){
                result.setResult(ResultEnum.PARAM_NULL);
                return result;
            }
            FqCollectExample collectExample = new FqCollectExample();
            collectExample.createCriteria().andTopicIdEqualTo(id.intValue())
                    .andTopicTypeEqualTo(TopicTypeEnum.DOUTU.getValue())
                    .andUserIdEqualTo(fqUser.getId());
            FqCollect fqCollect = fqCollectService.selectFirstByExample(collectExample);

            if("add".equals(type)){
                if(fqCollect == null){
                    fqCollect = new FqCollect();
                    fqCollect.setTopicId(id.intValue());
                    fqCollect.setTopicType(TopicTypeEnum.DOUTU.getValue());
                    fqCollect.setCreateTime(new Date());
                    fqCollect.setDelFlag(YesNoEnum.NO.getValue());
                    fqCollect.setUserId(fqUser.getId());
                    fqCollectService.insert(fqCollect);
                    CacheManager.addCollect(TopicTypeEnum.DOUTU.name(),fqUser.getId(),id.intValue());
                }else {
                    if(!YesNoEnum.NO.getValue().equals(fqCollect.getDelFlag())){
                        FqCollect fqCollectUpdate = new FqCollect();
                        fqCollectUpdate.setId(fqCollect.getId());
                        fqCollectUpdate.setDelFlag(YesNoEnum.NO.getValue());
                        fqCollectService.updateByPrimaryKeySelective(fqCollectUpdate);
                    }
                    CacheManager.addCollect(TopicTypeEnum.DOUTU.name(),fqUser.getId(),id.intValue());
                }
            }else if("remove".equals(type)){
                if(fqCollect == null){
                    result.setResult(ResultEnum.PARAM_NULL);
                    return result;
                }else {
                    FqCollect fqCollectUpdate = new FqCollect();
                    fqCollectUpdate.setId(fqCollect.getId());
                    fqCollectUpdate.setDelFlag(YesNoEnum.YES.getValue());
                    fqCollectService.updateByPrimaryKeySelective(fqCollectUpdate);
                    CacheManager.removeCollect(TopicTypeEnum.DOUTU.name(),fqUser.getId(),id.intValue());
                }
            }else {
                result.setResult(ResultEnum.PARAM_ERROR);
                return result;
            }
            result.setData(fqCollect.getId());
        } catch (Exception e) {
            logger.error("thought collect error",e);
        }
        return result;
    }

    @PostMapping("like/{id}")
    @ResponseBody
    public Object like(@PathVariable Long id){
        BaseResult result = new BaseResult();
        try {
            FqUserCache user = getCurrentUser();
            if(null ==  user){
                result.setResult(ResultEnum.USER_NOT_LOGIN);
                return result;
            }
            FqDoutuCloud fqDoutuCloud = fqDoutuCloudService.selectByPrimaryKey(id);
            if(fqDoutuCloud == null){
                result.setResult(ResultEnum.PARAM_NULL);
                return result;
            }
            GeneralLikeExample likeExample = new GeneralLikeExample();
            likeExample.createCriteria().andPostUserIdEqualTo(user.getId())
                    .andTopicIdEqualTo(id.intValue()).andTopicTypeEqualTo(TopicTypeEnum.THOUGHT_TYPE.getValue())
                    .andDelFlagEqualTo(YesNoEnum.NO.getValue());
            GeneralLike like = likeService.selectFirstByExample(likeExample);
            if(like!=null && like.getLikeValue()==1){
                result.setResult(ResultEnum.USER_ALREADY_LIKE);
                return result;
            }
            like = new GeneralLike(id.intValue(),TopicTypeEnum.THOUGHT_TYPE.getValue(),1,user.getId(),new Date(),YesNoEnum.NO.getValue());
            likeService.insert(like);

            FqDoutuCloud fqDoutuCloudUpdate = new FqDoutuCloud();
            fqDoutuCloudUpdate.setLikeCount(fqDoutuCloud.getLikeCount() == null? 1: fqDoutuCloud.getLikeCount()+1);
            fqDoutuCloudUpdate.setId(fqDoutuCloud.getId());
            fqDoutuCloudService.updateByPrimaryKeySelective(fqDoutuCloudUpdate);
            if(!fqDoutuCloud.getUserId().equals(user.getId())){
                Date now = new Date();
                CMessage message = new CMessage();
                message.setPostUserId(-1);
                message.setCreateTime(now);
                message.setDelFlag(YesNoEnum.NO.getValue());
                message.setReceivedUserId(fqDoutuCloud.getUserId());
                message.setType(MsgEnum.OFFICIAL_MSG.getValue());
                message.setContent("系统消息通知：<a class=\"c-fly-link\" href=\""+ CommonConstant.DOMAIN_URL+"/u/"+user.getId()+"\" target=\"_blank\">"+user.getNickname()+" </a> " +
                        "赞了你的<a class=\"c-fly-link\" href=\"" + CommonConstant.DOMAIN_URL + "/fqDoutuCloud/" + id +"\" target=\"_blank\">斗图（"+fqDoutuCloud.getTitle()+"）</a> "+ DateUtil.formatDateTime(now));
                messageService.insert(message);
            }
            result.setData(fqDoutuCloudUpdate.getLikeCount());
            CommonUtils.addActiveNum(user.getId(),ActiveNumEnum.POST_LIKE.getValue());
        } catch (Exception e) {
            logger.error("斗图点赞报错 like error",e);
            result.setResult(ResultEnum.FAIL);
        }
        return result;
    }

    @RequestMapping(value = "/{id}")
    public String detail(Model model, @PathVariable Long id){
        try {
            FqUserCache fqUserCache = getCurrentUser();
            FqDoutuCloud fqDoutuCloud = fqDoutuCloudService.selectByPrimaryKey(id);
            //如果被删除
            if(fqDoutuCloud == null || YesNoEnum.YES.getValue().equals(fqDoutuCloud.getDelFlag())){
                addErrorMsg(model,"已经被删除");
                return GENERAL_CUSTOM_ERROR_URL;
            }
            FqUser doutuUser = fqUserService.selectByPrimaryKey(fqDoutuCloud.getUserId());
            model.addAttribute("fqDoutuCloud",fqDoutuCloud);
            model.addAttribute("oUser",doutuUser);
            boolean isCollected = false;
            if(fqUserCache != null){
                FqCollectExample fqCollectExample = new FqCollectExample();
                fqCollectExample.createCriteria().andDelFlagEqualTo(YesNoEnum.NO.getValue())
                        .andUserIdEqualTo(fqUserCache.getId()).andTopicIdEqualTo(id.intValue())
                        .andTopicTypeEqualTo(TopicTypeEnum.DOUTU.getValue());
                FqCollect fqCollect = fqCollectService.selectFirstByExample(fqCollectExample);
                if(fqCollect != null){
                    isCollected = true;
                }
            }
            model.addAttribute("isCollected",isCollected);
            GeneralCommentExample commentExample = new GeneralCommentExample();
            commentExample.setOrderByClause("create_time asc");
            commentExample.createCriteria().andTopicIdEqualTo(id.intValue())
                    .andTopicTypeEqualTo(TopicTypeEnum.DOUTU.getValue())
                    .andDelFlagEqualTo(YesNoEnum.NO.getValue());
            List<DetailCommentResponse> comments = commentService.selectUserByExample(commentExample);
            model.addAttribute("comments",comments);
            model.addAttribute("count",comments.size());
            model.addAttribute("advertisements",CommonConstant.FQ_ADVERTISEMENTS.get(AdvertisementPositionEnum.DETAIL.getPosition()));
        } catch (Exception e) {
            logger.error("斗图 详情失败！",e);
        }
        return "/fqDoutuCloud/detail";
    }

    @ResponseBody
    @RequestMapping(value = "comment",method = RequestMethod.POST)
    @RepeatSubmit
    public Object thoughtComment(GeneralComment comment,HttpServletRequest request){
        BaseResult result = new BaseResult();
        try {
            String ip = WebUtil.getIP(request);
            FqUserCache user = getCurrentUser();
            if(user == null){
                result.setResult(ResultEnum.USER_NOT_LOGIN);
                return result;
            }
            Integer id = comment.getTopicId();
            FqDoutuCloud fqDoutuCloud = fqDoutuCloudService.selectByPrimaryKey(id.longValue());
            if(fqDoutuCloud == null || YesNoEnum.YES.getValue().equals(fqDoutuCloud.getDelFlag())){
                result.setResult(ResultEnum.FAIL);
                result.setMessage("此想法不存在，或已经删除！");
                return result;
            }
            String commentContent = comment.getContent();
            if(StringUtils.isBlank(commentContent)){
                result.setResult(ResultEnum.PARAM_NULL);
                result.setMessage("评论内容不能为空！");
                return result;
            }
            if(commentContent.length() > 255){
                result.setResult(ResultEnum.STR_LENGTH_TOO_LONG);
                result.setMessage("评论内容长度不能超过255！");
                return result;
            }
            Date now = new Date();
            commentContent = EmojiUtils.toAliases(commentContent);
            String shortCommentContent = "";
            String shortContent = fqDoutuCloud.getTitle();
            if(commentContent.length()>20){
                shortCommentContent = commentContent.substring(0,20)+"...";
            }else {
                shortCommentContent = commentContent;
            }

            int aiteSize = 0;
            List<String> aiteNames = CommonUtils.findAiteNicknames(commentContent);
            if(CollectionUtil.isNotEmpty(aiteNames)){
                for(String aiteNickname : aiteNames){
                    FqUserExample example = new FqUserExample();
                    example.createCriteria().andNicknameEqualTo(aiteNickname);
                    FqUser aiteUser = fqUserService.selectFirstByExample(example);
                    if(aiteUser != null){
                        aiteSize++;
                        if(!aiteUser.getId().equals(user.getId())){
                            shortCommentContent = shortCommentContent.replaceAll("@"+aiteNickname,"");
                            CMessage message = new CMessage();
                            message.setPostUserId(-1);
                            message.setCreateTime(now);
                            message.setDelFlag(YesNoEnum.NO.getValue());
                            message.setReceivedUserId(aiteUser.getId());
                            message.setType(MsgEnum.OFFICIAL_MSG.getValue());
                            message.setContent("系统消息通知：<a class=\"c-fly-link\" href=\""+ CommonConstant.DOMAIN_URL+"/u/"+user.getId()+"\" target=\"_blank\">"+user.getNickname()+" </a> " +
                                    "在<a class=\"c-fly-link\" href=\"" + CommonConstant.DOMAIN_URL + "/fqDoutuCloud/" + id +"\" target=\"_blank\">此斗图（"+shortContent+"）</a>中回复了你 :"+shortCommentContent+"-"+ DateUtil.formatDateTime(now));
                            messageService.insert(message);
                        }
                    }
                }
            }
            comment.setCreateTime(now);
            comment.setTopicType(TopicTypeEnum.DOUTU.getValue());
            comment.setPostUserId(user.getId());
            comment.setContent(commentContent);
            commentService.insert(comment);

            if(!user.getId().equals(fqDoutuCloud.getUserId()) && aiteSize == 0){
                CMessage message = new CMessage();
                message.setPostUserId(-1);
                message.setCreateTime(now);
                message.setDelFlag(YesNoEnum.NO.getValue());
                message.setReceivedUserId(fqDoutuCloud.getUserId());
                message.setType(MsgEnum.OFFICIAL_MSG.getValue());
                message.setContent("系统消息通知：<a class=\"c-fly-link\" href=\""+ CommonConstant.DOMAIN_URL+"/u/"+user.getId()+"\" target=\"_blank\">"+user.getNickname()+" </a>" +
                        "回复了你的<a class=\"c-fly-link\" href=\"" + CommonConstant.DOMAIN_URL + "/fqDoutuCloud/" + id + "\" target=\"_blank\">斗图（"+shortContent+"）</a> :"+shortCommentContent+"-"+ DateUtil.formatDateTime(now));
                messageService.insert(message);
            }
            result.setData(null);
            CommonUtils.addActiveNum(user.getId(), ActiveNumEnum.POST_COMMENT.getValue());
        } catch (Exception e) {
            logger.error("斗图 回复异常",e);
            result.setResult(ResultEnum.FAIL);
        }
        return result;
    }

    /**
     * 跳转到FqDoutuCloud首页
     */
    @RequestMapping("")
    public String index(Model model) {
        model.addAttribute("advertisements", CommonConstant.FQ_ADVERTISEMENTS.get(AdvertisementPositionEnum.LIST.getPosition()));
        return "/fqDoutuCloud/index";
    }

    /**
     * 添加FqDoutuCloud页面
     */
    @RequestMapping("/fqDoutuCloud_add")
    public String fqDoutuCloud_add() {
        return "/fqDoutuCloud/add";
    }

    /**
     * ajax删除FqDoutuCloud
     */
    @ResponseBody
    @RequestMapping("/delete")
    public Object delete(@RequestParam Long id) {
        BaseResult result = new BaseResult();
        try {
            fqDoutuCloudService.deleteByPrimaryKey(id);
        } catch (Exception e) {
            logger.error("error", e);
            result.setCode("1");
        }
        return result;
    }

    /**
     * 更新FqDoutuCloud页面
     */
    @RequestMapping("/edit/{fqDoutuCloudId}")
    public Object fqDoutuCloudEdit(@PathVariable Long fqDoutuCloudId, Model model) {
        FqDoutuCloud fqDoutuCloud = fqDoutuCloudService.selectByPrimaryKey(fqDoutuCloudId);
        model.addAttribute("fqDoutuCloud", fqDoutuCloud);
        return "/fqDoutuCloud/edit";
    }

    /**
     * ajax更新FqDoutuCloud
     */
    @ResponseBody
    @PostMapping("/save")
    public Object save(FqDoutuCloud fqDoutuCloud) {
        logger.info("入参：{}", JSON.toJSONString(fqDoutuCloud));
        BaseResult result = new BaseResult();
        try {
            FqUserCache fqUserCache = getCurrentUser();
            if (fqUserCache == null) {
                result.setResult(ResultEnum.USER_NOT_LOGIN);
                return result;
            }

            if(StringUtils.isEmpty(fqDoutuCloud.getImgUrl())){
                result.setResult(ResultEnum.PARAM_NULL);
                return result;
            }
            String extName = FileUtil.extName(fqDoutuCloud.getImgUrl());
            if (fqDoutuCloud.getId() == null) {
                fqDoutuCloud.setDelFlag(YesNoEnum.NO.getValue());
                fqDoutuCloud.setLikeCount(0);
                if(CommonConstant.picExtList.contains(extName)){
                    fqDoutuCloud.setVideoUrl("");
                }else if(CommonConstant.videoExtList.contains(extName)){
                    if(fqUserCache.getLevel() < 2){
                        return error("用户须两级才能上传视频！");
                    }
                    fqDoutuCloud.setVideoUrl(fqDoutuCloud.getImgUrl());
                    fqDoutuCloud.setImgUrl("");
                }else {
                    return error("文件后缀名不允许！");
                }
                fqDoutuCloud.setGmtCreate(new Date());
                fqDoutuCloud.setUserId(fqUserCache.getId());
                fqDoutuCloudService.insert(fqDoutuCloud);
                CommonUtils.addActiveNum(fqUserCache.getId(), ActiveNumEnum.DOUTU.getValue());
            } else {
                fqDoutuCloudService.updateByPrimaryKeySelective(fqDoutuCloud);
            }
            result.setData(fqDoutuCloud);
        } catch (Exception e) {
            logger.error("error", e);
            result.setCode("1");
        }
        return result;
    }


    /**
     * 查询FqDoutuCloud首页
     */
    @GetMapping("list")
    @ResponseBody
    public Object list(@RequestParam(defaultValue = "0") Integer index) {
        BaseResult result = new BaseResult();
        try {
            PageHelper.startPage(index, CommonConstant.DEAULT_PAGE_SIZE);
            CommonCondition commonCondition = new CommonCondition();
            commonCondition.setOrderByClause("GMT_CREATE desc");
            commonCondition.setDelFlag(YesNoEnum.NO.getValue());
            List<FqDoutuCloudWithUser> list = fqDoutuCloudService.selectWithUser(commonCondition);
            list = list==null? Lists.newArrayList():list;
            list.forEach(e->{
                e.setRoleName(UserRoleEnum.getDescByValue(e.getRole()));
            });
            PageInfo page = new PageInfo(list);
            result.setData(page);
        } catch (Exception e) {
            logger.error("error", e);
            result.setResult(ResultEnum.SYSTEM_ERROR);
        }
        return result;
    }
}