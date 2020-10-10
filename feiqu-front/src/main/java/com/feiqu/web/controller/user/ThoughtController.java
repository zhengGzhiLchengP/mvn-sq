package com.feiqu.web.controller.user;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.feiqu.common.annotation.RepeatSubmit;
import com.feiqu.common.base.BaseResult;
import com.feiqu.common.constant.BizConstant;
import com.feiqu.common.enums.*;
import com.feiqu.common.utils.EmojiUtils;
import com.feiqu.framwork.constant.CommonConstant;
import com.feiqu.framwork.support.cache.CacheManager;
import com.feiqu.framwork.util.CommonUtils;
import com.feiqu.framwork.util.WebUtil;
import com.feiqu.framwork.web.base.BaseController;
import com.feiqu.system.model.*;
import com.feiqu.system.pojo.cache.FqUserCache;
import com.feiqu.system.pojo.response.DetailCommentResponse;
import com.feiqu.system.pojo.response.FollowUserResponse;
import com.feiqu.system.pojo.response.ThoughtWithUser;
import com.feiqu.system.service.mainData.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.assertj.core.util.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Created by Administrator on 2017/10/21.
 */
@Controller
@RequestMapping("/thought")
public class ThoughtController extends BaseController {

    private static Logger logger = LoggerFactory.getLogger(ThoughtController.class);

    @Resource
    private ThoughtService thoughtService;
    @Resource
    private GeneralCommentService commentService;

    @Resource
    private GeneralLikeService likeService;
    @Resource
    private CMessageService messageService;
    @Resource
    private WebUtil webUtil;
    @Resource
    private FqUserService fqUserService;

    @Resource
    private FqCollectService fqCollectService;
    @Resource
    private UserFollowService userFollowService;
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 首页的地址 数据全部加载到本地缓存 后续考虑缓存整个页面 使用redis缓存 通过beetl
     *
     * @param
     * @return
     */
    @GetMapping(value = {""})
    public String index(String keyword,
                        @RequestParam(defaultValue = "1") Integer p,String order,Model model,String type) {
        try {
            FqUserCache fqUserCache = getCurrentUser();
            int uid = fqUserCache == null?0:fqUserCache.getId();

            PageHelper.startPage(p, 20);
            ThoughtExample example = new ThoughtExample();
            ThoughtExample.Criteria criteria = example.createCriteria();
            if ("zan".equals(order)) {
                example.setOrderByClause("comment_count desc,like_count desc");
            } else {
                example.setOrderByClause("id desc");
            }

            //我的关注
            if("follow".equals(type)){
                UserFollowExample userFollowExample = new UserFollowExample();
                userFollowExample.createCriteria().andFollowerUserIdEqualTo(uid).andDelFlagEqualTo(YesNoEnum.NO.getValue());
                List<FollowUserResponse> followees = userFollowService.selectFollowees(userFollowExample);
                List<Integer> uids = null;
                if(CollectionUtils.isNotEmpty(followees)){
                    uids = followees.stream().map(UserFollow::getFollowedUserId).collect(Collectors.toList());
                    criteria.andUserIdIn(uids);
                }
            }

            if (StringUtils.isNotEmpty(keyword)) {
                criteria.andThoughtContentLike("%" + keyword + "%");
                model.addAttribute("keyword", keyword);
            }
            criteria.andDelFlagEqualTo(YesNoEnum.NO.getValue());
            List<ThoughtWithUser> thoughts = thoughtService.getThoughtWithUser(example);
            Date now = new Date();
            if(uid == 0){
                if (CollUtil.isNotEmpty(thoughts)) {
                    for (ThoughtWithUser thoughtWithUser : thoughts) {
                        if (StringUtils.isNotEmpty(thoughtWithUser.getPicList())) {
                            thoughtWithUser.setPictures(Arrays.asList(thoughtWithUser.getPicList().split(",")));
                        }
                        thoughtWithUser.setCollected(false);
                        thoughtWithUser.dealCreateDate(now);
                        thoughtWithUser.setRoleName(UserRoleEnum.getDescByValue(thoughtWithUser.getRole()));
                    }
                }
            }else {
                List<Integer> list = fqCollectService.selectTopicIdsByTypeAndUid(TopicTypeEnum.THOUGHT_TYPE.getValue(), uid);
                if (CollUtil.isNotEmpty(thoughts)) {
                    String key = CacheManager.getCollectKey(TopicTypeEnum.THOUGHT_TYPE.name(), uid);
                    long redisCount = stringRedisTemplate.opsForSet().size(key);
                    if (!stringRedisTemplate.hasKey(key)) {
                        for (Integer tid : list) {
                            stringRedisTemplate.opsForSet().add(key, tid.toString());
                        }
                        stringRedisTemplate.expire(key, 1, TimeUnit.DAYS);
                    }
                    for (ThoughtWithUser thoughtWithUser : thoughts) {
                        if (StringUtils.isNotEmpty(thoughtWithUser.getPicList())) {
                            thoughtWithUser.setPictures(Arrays.asList(thoughtWithUser.getPicList().split(",")));
                        }
                        thoughtWithUser.setCollected(stringRedisTemplate.opsForSet().isMember(key, thoughtWithUser.getId().toString()));
                        thoughtWithUser.dealCreateDate(now);
                        thoughtWithUser.setRoleName(UserRoleEnum.getDescByValue(thoughtWithUser.getRole()));
                    }
                }
            }

            PageInfo page = new PageInfo(thoughts);
            model.addAttribute("thoughtList", thoughts);
            model.addAttribute("count", page.getTotal());
            model.addAttribute("p", p);
            model.addAttribute("pageSize", 20);
            model.addAttribute("page", page);
            model.addAttribute("ossAction", "https://" + CommonConstant.ALIYUN_OSS_BUCKET_NAME + "." + CommonConstant.ALIYUN_OSS_ENDPOINT);
            model.addAttribute("ossSuffix", BizConstant.MOBILE_ALIOSS_IMG_SUFFIX);
            model.addAttribute("advertisements", getAdvertisements(AdvertisementPositionEnum.LIST));
            return "/index";
        } catch (Exception e) {
            logger.error("主页获取数据出错", e);
            return GENERAL_ERROR_URL;
        }
    }

    @ResponseBody
    @GetMapping("/list")
    public Object list(String keyword,
                       @RequestParam(defaultValue = "1") Integer p,String order,String type){
        FqUserCache fqUserCache = getCurrentUser();
        int uid = fqUserCache == null?0:fqUserCache.getId();
        PageHelper.startPage(p, 20);
        ThoughtExample example = new ThoughtExample();
        if ("zan".equals(order)) {
            example.setOrderByClause("comment_count desc,like_count desc");
        } else {
            example.setOrderByClause("id desc");
        }
        ThoughtExample.Criteria criteria = example.createCriteria();
        //我的关注
        if("follow".equals(type)){
            UserFollowExample userFollowExample = new UserFollowExample();
            userFollowExample.createCriteria().andFollowerUserIdEqualTo(uid).andDelFlagEqualTo(YesNoEnum.NO.getValue());
            List<FollowUserResponse> followees = userFollowService.selectFollowees(userFollowExample);
            List<Integer> uids = null;
            if(CollectionUtils.isNotEmpty(followees)){
                uids = followees.stream().map(UserFollow::getFollowedUserId).collect(Collectors.toList());
                criteria.andUserIdIn(uids);
            }
        }
        if (StringUtils.isNotEmpty(keyword)) {
            criteria.andThoughtContentLike("%" + keyword + "%");
        }
        criteria.andDelFlagEqualTo(YesNoEnum.NO.getValue());
        List<ThoughtWithUser> thoughts = thoughtService.getThoughtWithUser(example);
        Date now = new Date();
        if(uid == 0){
            if (CollUtil.isNotEmpty(thoughts)) {
                for (ThoughtWithUser thoughtWithUser : thoughts) {
                    if (StringUtils.isNotEmpty(thoughtWithUser.getPicList())) {
                        thoughtWithUser.setPictures(Arrays.asList(thoughtWithUser.getPicList().split(",")));
                    }
                    thoughtWithUser.setCollected(false);
                    thoughtWithUser.dealCreateDate(now);
                    thoughtWithUser.setRoleName(UserRoleEnum.getDescByValue(thoughtWithUser.getRole()));
                }
            }
        }else {
            List<Integer> list = fqCollectService.selectTopicIdsByTypeAndUid(TopicTypeEnum.THOUGHT_TYPE.getValue(), uid);
            if (CollUtil.isNotEmpty(thoughts)) {
                String key = CacheManager.getCollectKey(TopicTypeEnum.THOUGHT_TYPE.name(), uid);
                long redisCount = stringRedisTemplate.opsForSet().size(key);
                if (!stringRedisTemplate.hasKey(key)) {
                    for (Integer tid : list) {
                        stringRedisTemplate.opsForSet().add(key, tid.toString());
                    }
                    stringRedisTemplate.expire(key, 1, TimeUnit.DAYS);
                }
                for (ThoughtWithUser thoughtWithUser : thoughts) {
                    if (StringUtils.isNotEmpty(thoughtWithUser.getPicList())) {
                        thoughtWithUser.setPictures(Arrays.asList(thoughtWithUser.getPicList().split(",")));
                    }
                    thoughtWithUser.setCollected(stringRedisTemplate.opsForSet().isMember(key, thoughtWithUser.getId().toString()));
                    thoughtWithUser.dealCreateDate(now);
                    thoughtWithUser.setRoleName(UserRoleEnum.getDescByValue(thoughtWithUser.getRole()));
                }
            }
        }
        PageInfo page = new PageInfo(thoughts);
        return success(page);
    }

    /*
    手动置顶
     */
    @ResponseBody
    @PostMapping(value = "/handTop/{id}")
    public Object handTop(@PathVariable Integer id) {
        BaseResult result = new BaseResult();
        try {
            FqUserCache user = getCurrentUser();
            if (user == null) {
                result.setResult(ResultEnum.USER_NOT_LOGIN);
                return result;
            }
            Thought thought = thoughtService.selectByPrimaryKey(id);
            if (thought == null) {
                result.setResult(ResultEnum.PARAM_NULL);
                return result;
            }
            List<ThoughtWithUser> list = Lists.newArrayList();
            ThoughtWithUser thoughtWithUser = new ThoughtWithUser(thought);
            FqUser fqUser = fqUserService.selectByPrimaryKey(thoughtWithUser.getUserId());
            thoughtWithUser.setIcon(fqUser.getIcon());
            thoughtWithUser.setNickname(fqUser.getNickname());
            thoughtWithUser.setUsername(fqUser.getUsername());
            thoughtWithUser.setRoleName(UserRoleEnum.getDescByValue(fqUser.getRole()));
            list.add(thoughtWithUser);
            String listStr = stringRedisTemplate.opsForValue().get(CommonConstant.THOUGHT_TOP_LIST);
            if (StringUtils.isEmpty(listStr)) {
                stringRedisTemplate.opsForValue().set(CommonConstant.THOUGHT_TOP_LIST,JSON.toJSONString(list), 24 * 60 * 60);
            } else {
                List<ThoughtWithUser> thoughtWithUsers = JSON.parseArray(listStr, ThoughtWithUser.class);
                if (thoughtWithUsers.size() >= 3) {
                    result.setResult(ResultEnum.THOUGHT_TOP_EXIST);
                    result.setMessage("置顶的想法已满，请等待！");
                    return result;
                }
                boolean exist = thoughtWithUsers.stream().anyMatch(e -> e.getId().equals(id));
                if (exist) {
                    return error("置顶想法已经存在！");
                }
                thoughtWithUsers.add(thoughtWithUser);
                stringRedisTemplate.opsForValue().set(CommonConstant.THOUGHT_TOP_LIST,JSON.toJSONString(thoughtWithUsers), 24 * 60 * 60);
            }
            CommonUtils.addOrDelUserQudouNum(user, -20);
        } catch (Exception e) {
            logger.error("想法置顶失败", e);
            result.setResult(ResultEnum.FAIL);
        }
        return result;
    }

    /*
   手动置顶
    */
    @ResponseBody
    @PostMapping(value = "/handCancelTop/{id}")
    public Object handCancelTop(@PathVariable Integer id) {
        BaseResult result = new BaseResult();
        try {
            FqUserCache user = getCurrentUser();
            if (user == null) {
                result.setResult(ResultEnum.USER_NOT_LOGIN);
                return result;
            }
            Thought thought = thoughtService.selectByPrimaryKey(id);
            if (thought == null) {
                result.setResult(ResultEnum.PARAM_NULL);
                return result;
            }
            String listStr = stringRedisTemplate.opsForValue().get(CommonConstant.THOUGHT_TOP_LIST);
            if (StringUtils.isEmpty(listStr)) {
            } else {
                List<ThoughtWithUser> thoughtWithUsers = JSON.parseArray(listStr, ThoughtWithUser.class);
                thoughtWithUsers = thoughtWithUsers.stream().filter(e -> !e.getId().equals(id)).collect(Collectors.toList());
                stringRedisTemplate.opsForValue().set(CommonConstant.THOUGHT_TOP_LIST,JSON.toJSONString(thoughtWithUsers), 24 * 60 * 60);
            }
        } catch (Exception e) {
            logger.error("想法取消置顶失败", e);
            result.setResult(ResultEnum.FAIL);
        }
        return result;
    }

    @RepeatSubmit
    @ResponseBody
    @PostMapping(value = "post")
    public Object postThoughts(@RequestBody Thought thought) {
        try {
            logger.info("发布想法：{}", JSON.toJSONString(thought));
            String ip = getIP();
            FqUserCache user = getCurrentUser();
            if (user == null) {
                return error();
            }
            if (StringUtils.isBlank(thought.getThoughtContent())) {
                //可以为空
                thought.setThoughtContent(StringUtils.EMPTY);
                if (StringUtils.isEmpty(thought.getPicList())) {
                    return error("想法和图片不能都为空！");
                }
            } else {
                if (thought.getThoughtContent().length() > 400) {
                    return error("想法长度不能超过400");
                }
                thought.setThoughtContent(EmojiUtils.toAliases(thought.getThoughtContent()));
            }
            String region = CommonUtils.getRegionByIp(ip);
            thought.init();
            thought.setArea(region);
            thought.setUserId(user.getId());
            thought.setCreateTime(new Date());
            thoughtService.insert(thought);
            thought.setThoughtContent(EmojiUtils.toUnicode(thought.getThoughtContent()));
            ThoughtWithUser thoughtWithUser = new ThoughtWithUser(thought);
            thoughtWithUser.bindUser(user);
            CommonUtils.addActiveNum(user.getId(), ActiveNumEnum.POST_THOUGHT.getValue());
            return success(thoughtWithUser);
        } catch (Exception e) {
            logger.error("发表想法失败", e);
            return error("发表想法失败,请联系管理员");
        }
    }

    @PostMapping("/like")
    @ResponseBody
    public Object like(Integer thoughtId) {
        try {
            FqUserCache user = getCurrentUser();
            if (user == null) {
                return error();
            }
            Thought thought = thoughtService.selectByPrimaryKey(thoughtId);
            if (thought == null) {
                return error();
            }
            GeneralLikeExample likeExample = new GeneralLikeExample();
            likeExample.createCriteria().andPostUserIdEqualTo(user.getId())
                    .andTopicIdEqualTo(thoughtId).andTopicTypeEqualTo(TopicTypeEnum.THOUGHT_TYPE.getValue())
                    .andDelFlagEqualTo(YesNoEnum.NO.getValue());
            GeneralLike like = likeService.selectFirstByExample(likeExample);
            if (like != null && like.getLikeValue() == 1) {
                return error("已经点赞");
            }
            like = new GeneralLike(thoughtId, TopicTypeEnum.THOUGHT_TYPE.getValue(), 1, user.getId(), new Date(), YesNoEnum.NO.getValue());
            likeService.insert(like);
            thought.setLikeCount(thought.getLikeCount() == null ? 1 : thought.getLikeCount() + 1);
            thoughtService.updateByPrimaryKey(thought);
            String shortThoughtContent = CommonUtils.getShortThoughtContent(thought.getThoughtContent());
            if (!thought.getUserId().equals(user.getId())) {
                CommonUtils.sendMsg(MsgEnum.OFFICIAL_MSG.getValue(),thought.getUserId(),null,
                        "系统消息通知：<a class=\"c-fly-link\" href=\"" + CommonConstant.DOMAIN_URL + "/u/" + user.getId()
                                + "\" target=\"_blank\">" + user.getNickname() + " </a> " +
                        "赞了你的想法：<a class=\"c-fly-link\" href=\"" + CommonConstant.DOMAIN_URL + "/thought/" + thoughtId
                                + "\" target=\"_blank\">" + shortThoughtContent + "</a> ");
            }
            CommonUtils.addActiveNum(user.getId(), ActiveNumEnum.POST_LIKE.getValue());
            return success(thought.getLikeCount());
        } catch (Exception e) {
            logger.error("thought like error", e);
            return error();
        }
    }

    @ResponseBody
    @RequestMapping(value = "comment", method = RequestMethod.POST)
    @RepeatSubmit
    public Object thoughtComment(GeneralComment comment, HttpServletRequest request, HttpServletResponse response) {
        BaseResult result = new BaseResult();
        try {
            FqUserCache user = webUtil.currentUser(request, response);
            if (user == null) {
                result.setResult(ResultEnum.USER_NOT_LOGIN);
                return result;
            }
            Integer thoughtId = comment.getTopicId();
            Thought thought = thoughtService.selectByPrimaryKey(thoughtId);
            if (thought == null || YesNoEnum.YES.getValue().equals(thought.getDelFlag())) {
                return error("此想法不存在，或已经删除！");
            }
            String commentContent = comment.getContent();
            if (StringUtils.isBlank(commentContent)) {
                return error("评论内容不能为空！");
            }
            if (commentContent.length() > 255) {
                return error("评论内容长度不能超过255！");
            }
            Date now = new Date();
            commentContent = EmojiUtils.toAliases(commentContent);
            String shortCommentContent = CommonUtils.getShortThoughtContent(commentContent);
            String shortThoughtContent = CommonUtils.getShortThoughtContent(thought.getThoughtContent());
            int aiteSize = 0;
            List<String> aiteNames = CommonUtils.findAiteNicknames(commentContent);
            if (CollectionUtil.isNotEmpty(aiteNames)) {
                for (String aiteNickname : aiteNames) {
                    FqUserExample example = new FqUserExample();
                    example.createCriteria().andNicknameEqualTo(aiteNickname);
                    FqUser aiteUser = fqUserService.selectFirstByExample(example);
                    if (aiteUser != null) {
                        aiteSize++;
                        if (!aiteUser.getId().equals(user.getId())) {
                            shortCommentContent = shortCommentContent.replaceAll("@" + aiteNickname, "");
                            CMessage message = new CMessage();
                            message.setPostUserId(-1);
                            message.setCreateTime(now);
                            message.setDelFlag(YesNoEnum.NO.getValue());
                            message.setReceivedUserId(aiteUser.getId());
                            message.setType(MsgEnum.OFFICIAL_MSG.getValue());
                            message.setContent("系统消息通知：<a class=\"c-fly-link\" href=\"" + CommonConstant.DOMAIN_URL + "/u/" + user.getId() + "\" target=\"_blank\">" + user.getNickname() + " </a> " +
                                    "在<a class=\"c-fly-link\" href=\"" + CommonConstant.DOMAIN_URL + "/thought/" + thoughtId + "\" target=\"_blank\">此想法（" + shortThoughtContent + "）</a>中回复了你 :" + shortCommentContent);
                            messageService.insert(message);
                        }
                    }
                }
            }
            thought.setCommentCount(thought.getCommentCount() == null ? 1 : thought.getCommentCount() + 1);
            thought.setLastReplyUserName(user.getNickname());
            thought.setLastReplyTime(DateUtil.formatDateTime(now));
            thoughtService.updateByPrimaryKey(thought);
            comment.setCreateTime(now);
            comment.setTopicType(TopicTypeEnum.THOUGHT_TYPE.getValue());
            comment.setPostUserId(user.getId());
            comment.setContent(commentContent);
            commentService.insert(comment);

            if (!user.getId().equals(thought.getUserId()) && aiteSize == 0) {
                CMessage message = new CMessage();
                message.setPostUserId(-1);
                message.setCreateTime(now);
                message.setDelFlag(YesNoEnum.NO.getValue());
                message.setReceivedUserId(thought.getUserId());
                message.setType(MsgEnum.OFFICIAL_MSG.getValue());
                message.setContent("系统消息通知：<a class=\"c-fly-link\" href=\"" + CommonConstant.DOMAIN_URL + "/u/" + user.getId() + "\" target=\"_blank\">" + user.getNickname() + " </a>" +
                        "回复了你的<a class=\"c-fly-link\" href=\"" + CommonConstant.DOMAIN_URL + "/thought/" + thoughtId + "\" target=\"_blank\">想法（" + shortThoughtContent + "）</a> :" + shortCommentContent);
                messageService.insert(message);
            }
            result.setData(thought.getCommentCount());
            CommonUtils.addActiveNum(user.getId(), ActiveNumEnum.POST_COMMENT.getValue());
        } catch (Exception e) {
            logger.error("thought 回复异常", e);
            result.setResult(ResultEnum.FAIL);
        }
        return result;
    }



    @GetMapping(value = "/{thoughtId}")
    public String commentList(Model model, @PathVariable Integer thoughtId) {
        try {
            FqUserCache fqUserCache = getCurrentUser();
            Thought thought = thoughtService.selectByPrimaryKey(thoughtId);
            FqUser thoughtUser = null;
            if (thought != null) {
                thoughtUser = fqUserService.selectByPrimaryKey(thought.getUserId());
                ThoughtWithUser thoughtWithUser = new ThoughtWithUser(thought);
                model.addAttribute("thought", thoughtWithUser);
                model.addAttribute("oUser", thoughtUser);
            } else {
                return GENERAL_NOT_FOUNF_404_URL;
            }
            //如果想法被删除
            if (YesNoEnum.YES.getValue().equals(thought.getDelFlag())) {
                return GENERAL_NOT_FOUNF_404_URL;
            }
            Thought toUpdate = new Thought();
            toUpdate.setId(thoughtId);
            toUpdate.setClickCount(thought.getClickCount() == null ? 1 : thought.getClickCount() + 1);
            thoughtService.updateByPrimaryKeySelective(toUpdate);
            boolean isCollected = false;
            if (fqUserCache != null) {
                FqCollectExample fqCollectExample = new FqCollectExample();
                fqCollectExample.createCriteria().andDelFlagEqualTo(YesNoEnum.NO.getValue())
                        .andUserIdEqualTo(fqUserCache.getId()).andTopicIdEqualTo(thoughtId)
                        .andTopicTypeEqualTo(TopicTypeEnum.THOUGHT_TYPE.getValue());
                FqCollect fqCollect = fqCollectService.selectFirstByExample(fqCollectExample);
                if (fqCollect != null) {
                    isCollected = true;
                }
            }
            model.addAttribute("isCollected", isCollected);
            GeneralCommentExample commentExample = new GeneralCommentExample();
            commentExample.setOrderByClause("create_time asc");
            commentExample.createCriteria().andTopicIdEqualTo(thoughtId)
                    .andTopicTypeEqualTo(TopicTypeEnum.THOUGHT_TYPE.getValue())
                    .andDelFlagEqualTo(YesNoEnum.NO.getValue());
            List<DetailCommentResponse> comments = commentService.selectUserByExample(commentExample);
            model.addAttribute("comments", comments);
            model.addAttribute("count", comments.size());

            ThoughtExample thoughtExample = new ThoughtExample();
            thoughtExample.setOrderByClause("create_time desc");
            thoughtExample.createCriteria().andDelFlagEqualTo(YesNoEnum.NO.getValue())
                    .andUserIdEqualTo(thought.getUserId())
                    .andIdNotEqualTo(thoughtId)
            .andThoughtContentNotEqualTo("");
            PageHelper.startPage(1, 10, false);
            List<Thought> theirThoughts = thoughtService.selectByExample(thoughtExample);
            model.addAttribute("theirThoughts", theirThoughts);
            int month = DateUtil.thisMonth() + 1;
            Double score = stringRedisTemplate.opsForZSet().score(CommonConstant.FQ_ACTIVE_USER_SORT + month, thoughtUser.getId().toString());
            model.addAttribute("activeNum", score == null ? 0 : score.intValue());
            model.addAttribute("advertisements", CommonConstant.FQ_ADVERTISEMENTS.get(AdvertisementPositionEnum.DETAIL.getPosition()));
        } catch (Exception e) {
            logger.error("thought 详情失败！", e);
            return GENERAL_CUSTOM_ERROR_URL;
        }
        return "/thought/detail";
    }

    @GetMapping(value = "/info/{thoughtId}")
    @ResponseBody
    public Object info(@PathVariable Integer thoughtId) {
        try {
            Thought thought = thoughtService.selectByPrimaryKey(thoughtId);
            //如果想法被删除
            if (YesNoEnum.YES.getValue().equals(thought.getDelFlag())) {
                return error();
            }
            ThoughtWithUser thoughtWithUser = new ThoughtWithUser(thought);
            return success(thoughtWithUser);
        } catch (Exception e) {
            logger.error("thought 详情失败！", e);
            return error();
        }
    }

    /*
    我的想法
     */
    @GetMapping(value = "/my")
    public String myThoughts(HttpServletRequest request, HttpServletResponse response,
                             @RequestParam(defaultValue = "1") Integer page,
                             Model model) {
        try {
            FqUserCache user = webUtil.currentUser(request, response);
            if (user == null) {
                return "/login";
            }
            PageHelper.startPage(page, 20);
            ThoughtExample example = new ThoughtExample();
            example.createCriteria().andUserIdEqualTo(user.getId()).andDelFlagEqualTo(YesNoEnum.NO.getValue());
            example.setOrderByClause("create_time desc");
            List<ThoughtWithUser> thoughts = thoughtService.getThoughtWithUser(example);

            List<Integer> list = fqCollectService.selectTopicIdsByTypeAndUid(TopicTypeEnum.THOUGHT_TYPE.getValue(), user.getId());
            if (CollectionUtil.isNotEmpty(thoughts)) {
                String key = CacheManager.getCollectKey(TopicTypeEnum.THOUGHT_TYPE.name(), user.getId());
                if (!stringRedisTemplate.hasKey(key)) {
                    for (Integer tid : list) {
                        stringRedisTemplate.opsForSet().add(key, tid.toString());
                    }
                    stringRedisTemplate.expire(key, 1,TimeUnit.DAYS);
                }
                for (ThoughtWithUser thoughtWithUser : thoughts) {
                    if (StringUtils.isNotEmpty(thoughtWithUser.getPicList())) {
                        thoughtWithUser.setPictures(Arrays.asList(thoughtWithUser.getPicList().split(",")));
                    }
                    thoughtWithUser.setCollected(stringRedisTemplate.opsForSet().isMember(key, thoughtWithUser.getId().toString()));
                }
            }

            PageInfo pageInfo = new PageInfo(thoughts);
            model.addAttribute("thoughtList", thoughts);
            model.addAttribute("count", pageInfo.getTotal());
            model.addAttribute("p", page);
            model.addAttribute("pageSize", 20);
        } catch (Exception e) {
            logger.error("thought 获取我的想法失败！", e);
        }
        return "/thought/thoughts";
    }

    @PostMapping("delete")
    @ResponseBody
    public Object delete(Integer thoughtId, HttpServletRequest request, HttpServletResponse response) {
        BaseResult result = new BaseResult();
        try {
            FqUserCache user = webUtil.currentUser(request, response);
            if (user == null) {
                result.setResult(ResultEnum.USER_NOT_LOGIN);
                return result;
            }
            Thought thought = thoughtService.selectByPrimaryKey(thoughtId);
            if (thought != null) {
                if (UserRoleEnum.ADMIN_USER_ROLE.getValue().equals(user.getRole())) {
                    thought.setDelFlag(YesNoEnum.YES.getValue());
                    thoughtService.updateByPrimaryKeySelective(thought);
                } else {
                    if (user.getId().equals(thought.getUserId())) {
                        thought.setDelFlag(YesNoEnum.YES.getValue());
                        thoughtService.updateByPrimaryKeySelective(thought);
                    } else {
                        result.setResult(ResultEnum.DELETE_NOT_MY);
                    }
                }
            } else {
                result.setResult(ResultEnum.FAIL);
            }
        } catch (Exception e) {
            logger.error("thought delete error", e);
            result.setResult(ResultEnum.FAIL);
        }
        return result;
    }

    @PostMapping("/collect/{type}")
    @ResponseBody
    public Object collect(@PathVariable String type, Integer tid, HttpServletRequest request, HttpServletResponse response) {
        BaseResult result = new BaseResult();
        try {
            FqUserCache fqUser = webUtil.currentUser(request, response);
            if (fqUser == null) {
                result.setResult(ResultEnum.USER_NOT_LOGIN);
                return result;
            }
            Thought thoughtDB = thoughtService.selectByPrimaryKey(tid);
            if (thoughtDB == null) {
                result.setResult(ResultEnum.ARTICLE_NOT_EXITS);
                return result;
            }
            FqCollectExample collectExample = new FqCollectExample();
            collectExample.createCriteria().andTopicIdEqualTo(tid)
                    .andTopicTypeEqualTo(TopicTypeEnum.THOUGHT_TYPE.getValue())
                    .andUserIdEqualTo(fqUser.getId());
            FqCollect fqCollect = fqCollectService.selectFirstByExample(collectExample);

            if ("add".equals(type)) {
                if (fqCollect == null) {
                    fqCollect = new FqCollect();
                    fqCollect.setTopicId(tid);
                    fqCollect.setTopicType(TopicTypeEnum.THOUGHT_TYPE.getValue());
                    fqCollect.setCreateTime(new Date());
                    fqCollect.setDelFlag(YesNoEnum.NO.getValue());
                    fqCollect.setUserId(fqUser.getId());
                    fqCollectService.insert(fqCollect);
                    CacheManager.addCollect(TopicTypeEnum.THOUGHT_TYPE.name(), fqUser.getId(), tid);
                    if (!fqUser.getId().equals(thoughtDB.getUserId())) {
                        CommonUtils.sendMsg(MsgEnum.OFFICIAL_MSG.getValue(), thoughtDB.getUserId(), new Date(), fqUser.getNickname() + ":收藏了你的想法(" + CommonUtils.getShortThoughtContent(thoughtDB.getThoughtContent()) + ")");
                    }
                } else {
                    if (!YesNoEnum.NO.getValue().equals(fqCollect.getDelFlag())) {
                        fqCollect.setDelFlag(YesNoEnum.NO.getValue());
                        fqCollectService.updateByPrimaryKey(fqCollect);
                    }
                    CacheManager.addCollect(TopicTypeEnum.THOUGHT_TYPE.name(), fqUser.getId(), tid);
                }
            } else if ("remove".equals(type)) {
                if (fqCollect == null) {
                    result.setResult(ResultEnum.PARAM_NULL);
                    return result;
                } else {
                    fqCollect.setDelFlag(YesNoEnum.YES.getValue());
                    fqCollectService.updateByPrimaryKey(fqCollect);
                    CacheManager.removeCollect(TopicTypeEnum.THOUGHT_TYPE.name(), fqUser.getId(), tid);
                }
            } else {
                result.setResult(ResultEnum.PARAM_ERROR);
                return result;
            }
            result.setData(fqCollect.getId());
        } catch (Exception e) {
            logger.error("thought collect error", e);
        }
        return result;
    }
}
