package com.feiqu.web.controller.user;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.feiqu.common.annotation.RepeatSubmit;
import com.feiqu.common.base.BaseResult;
import com.feiqu.common.base.FqResult;
import com.feiqu.common.enums.*;
import com.feiqu.common.utils.EmojiUtils;
import com.feiqu.common.utils.SpringUtils;
import com.feiqu.framwork.constant.CommonConstant;
import com.feiqu.framwork.util.CommonUtils;
import com.feiqu.framwork.util.WebUtil;
import com.feiqu.framwork.web.base.BaseController;
import com.feiqu.system.model.*;
import com.feiqu.system.pojo.cache.FqUserCache;
import com.feiqu.system.pojo.response.ActionResult;
import com.feiqu.system.pojo.response.ArticleUserDetail;
import com.feiqu.system.pojo.response.DetailCommentResponse;
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
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Administrator on 2017/10/15.
 */
@Controller
@RequestMapping("article")
public class ArticleController extends BaseController {

    private final static Logger LOGGER = LoggerFactory.getLogger(ArticleController.class);

    @Resource
    private ArticleService articleService;
    @Resource
    private FqUserService userService;
    @Resource
    private GeneralCommentService commentService;
    @Resource
    private GeneralLikeService likeService;
    @Resource
    private CMessageService messageService;
    @Resource
    private WebUtil webUtil;
    @Resource
    private FqLabelService fqLabelService;
    @Resource
    private FqCollectService fqCollectService;
    @Resource
    private FqUserService fqUserService;
    @Resource
    private FqUserPayWayService fqUserPayWayService;

    @PostMapping(value = "/clickCount/{id}")
    @ResponseBody
    public BaseResult clickCount(@PathVariable Integer id) {
        Article article = articleService.selectByPrimaryKey(id);
        if (article != null) {
            Article toUpdate = new Article();
            toUpdate.setBrowseCount(article.getBrowseCount() == null ? 1 : article.getBrowseCount() + 1);
            toUpdate.setId(id);
            articleService.updateByPrimaryKeySelective(toUpdate);
        }
        return new BaseResult();
    }

    @GetMapping(value = "goWriteArticle")
    public String goWriteArticle(Model model) {
        FqLabelExample example = new FqLabelExample();
        example.createCriteria().andTypeEqualTo(TopicTypeEnum.ARTICLE_TYPE.getValue());
        List<FqLabel> labels = fqLabelService.selectByExample(example);
        model.addAttribute("labels", labels);
        return "/article/writeArticle";
    }

    @ResponseBody
    @PostMapping(value = "writeArticle")
    @RepeatSubmit
    public Object writeArticle(Article article) {
        ActionResult result = new ActionResult();
        try {
            FqUserCache user = getCurrentUser();
            if (user == null) {
                result.setResult(ResultEnum.USER_NOT_LOGIN);
                return result;
            }
            if (UserStatusEnum.FROZEN.getValue().equals(user.getStatus())) {
                result.setResult(ResultEnum.USER_FROZEN);
                return result;
            }
            if (article.getLabel() == null) {
                result.setResult(ResultEnum.LABEL_NULL);
                return result;
            }
            if (StringUtils.isBlank(article.getArticleTitle()) || StringUtils.isBlank(article.getArticleContent())) {
                result.setResult(ResultEnum.PARAM_NULL);
                return result;
            }
            if (article.getArticleContent().length() < 50) {
                return error("文章长度不能小于50，如若请去想法处发表");
            }
            article.setUserId(user.getId());
            //如果是admin角色的 就默认审核通过
            if(UserRoleEnum.ADMIN_USER_ROLE.getValue().equals(user.getRole())){
                article.setDelFlag(0);
            }else {
                article.setDelFlag(-1);//默认 审核不通过
            }
            Date now = new Date();
            article.setCreateTime(now);
            article.setArticleContent(EmojiUtils.toAliases(article.getArticleContent()));
            Integer length = StrUtil.byteLength(article.getArticleContent(), CharsetUtil.CHARSET_UTF_8);
            LOGGER.debug("文章的内容长度：{}", length);
            //大于250Kb
            if(length > 250000){
                return error("字符长度超过了限制！");
            }
            article.setContentType(2);
            int rs = articleService.insert(article);
            if(rs == 0){
                return error("保存失败，请联系官方人员！");
            }

            if(-1 == article.getDelFlag()){
                //发送消息
                ThreadPoolTaskExecutor executor = SpringUtils.getBean(ThreadPoolTaskExecutor.class);
                executor.execute(() -> {
                    FqUserExample example = new FqUserExample();
                    example.createCriteria().andRoleEqualTo(UserRoleEnum.ADMIN_USER_ROLE.getValue());
                    List<FqUser> fqUsers = fqUserService.selectByExample(example);
                    String content = "系统消息通知：用户："+user.getNickname()+":发表了一篇文章（<a class=\"c-fly-link\" href=\""+CommonConstant.DOMAIN_URL+
                            "/article/"+article.getId()+"\">"+article.getArticleTitle()+"</a>），等待你的审核。" + DateUtil.formatDateTime(now);
                    fqUsers.forEach(fqUser -> {
                        CommonUtils.sendMsg(MsgEnum.OFFICIAL_MSG.getValue(),fqUser.getId(),now,content);
                    });
                });
            }
            result.setResult(ResultEnum.SUCCESS);
        } catch (Exception e) {
            LOGGER.error("writeArticle error", e);
            result.setResult(ResultEnum.SYSTEM_ERROR);
        }
        return result;
    }

    @GetMapping("/edit/{articleId}")
    public String edit(@PathVariable Integer articleId, Model model) {
        try {
            FqUserCache user = getCurrentUser();
            if (user == null) {
                return USER_LOGIN_REDIRECT_URL;
            }
            Article article = articleService.selectByPrimaryKey(articleId);
            if (article == null) {
                return GENERAL_NOT_FOUNF_404_URL;
            }
            if (!user.getId().equals(article.getUserId())) {
                model.addAttribute(GENERAL_CUSTOM_ERROR_CODE,"你没有权限编辑他人的文章");
                return GENERAL_CUSTOM_ERROR_URL;
            }
            if (article.getContentType() == 2) {
                article.setArticleContent(HtmlUtils.htmlUnescape(article.getArticleContent()));
            }
            model.addAttribute("article", article);
            FqLabelExample example = new FqLabelExample();
            example.createCriteria().andTypeEqualTo(TopicTypeEnum.ARTICLE_TYPE.getValue());
            List<FqLabel> labels = fqLabelService.selectByExample(example);
            model.addAttribute("labels", labels);
        } catch (Exception e) {
            LOGGER.error("article edit error", e);
        }
        return "/article/edit";
    }

    //编辑操作
    @PostMapping("/doEdit")
    @ResponseBody
    public Object doEdit(Article article, HttpServletRequest request, HttpServletResponse response) {
        BaseResult result = new BaseResult();
        try {
            FqUserCache fqUser = webUtil.currentUser(request, response);
            if (fqUser == null) {
                result.setResult(ResultEnum.USER_NOT_LOGIN);
                return result;
            }
            LOGGER.info("用户：{} 编辑了一篇文章：{}", fqUser.getId(), article.getArticleTitle());
            if (UserStatusEnum.FROZEN.getValue().equals(fqUser.getStatus())) {
                result.setResult(ResultEnum.USER_FROZEN);
                return result;
            }
            Article articleDB = articleService.selectByPrimaryKey(article.getId());
            if (articleDB == null) {
                result.setResult(ResultEnum.ARTICLE_NOT_EXITS);
                return result;
            }
            if (!articleDB.getUserId().equals(fqUser.getId())) {
                result.setResult(ResultEnum.USER_NOT_SAME);
                return result;
            }
            if (article.getLabel() == null) {
                result.setResult(ResultEnum.LABEL_NULL);
                return result;
            }
            if (StringUtils.isBlank(article.getArticleTitle()) || StringUtils.isBlank(article.getArticleContent())) {
                result.setResult(ResultEnum.PARAM_NULL);
                return result;
            }
            articleDB.setArticleTitle(article.getArticleTitle());
            articleDB.setArticleContent(article.getArticleContent());
            articleDB.setLabel(article.getLabel());
            articleService.updateByPrimaryKeyWithBLOBs(articleDB);
        } catch (Exception e) {
            LOGGER.error("article edit error", e);
            result.setCode(CommonConstant.SYSTEM_ERROR_CODE);
        }
        return result;
    }

    @RequestMapping("/manage")
    public String manage() {
        FqUserCache fqUserCache = getCurrentUser();
        if (fqUserCache == null) {
            return USER_LOGIN_REDIRECT_URL;
        }
        if (fqUserCache.getRole() != 1) {
            return GENERAL_ERROR_URL;
        }
        return "/article/manage";
    }


    @GetMapping("/detail/{articleId}")
    @ResponseBody
    public FqResult articleDetail1(@PathVariable Integer articleId) {
        FqResult fqResult = new FqResult();
        try {
            Article article = articleService.selectByPrimaryKey(articleId);
            if (article == null) {
                return fqResult;
            }
            fqResult.setData(article);
        } catch (Exception e) {

        }
        return fqResult;
    }

    @RequestMapping("/{articleId}")
    public String articleDetail(@PathVariable Integer articleId, Model model) {
        try {
            Article article = articleService.selectByPrimaryKey(articleId);
            if (article == null) {
                return GENERAL_NOT_FOUNF_404_URL;
            }
            if (YesNoEnum.YES.getValue().equals(article.getDelFlag())) {
                return GENERAL_NOT_FOUNF_404_URL;
            }
            model.addAttribute("meta_keywords",article.getArticleTitle());
            model.addAttribute("meta_description",StrUtil.maxLength(article.getArticleContent(),100));
            if (article.getContentType() != null && 2 == article.getContentType()) {
                article.setArticleContent(HtmlUtils.htmlUnescape(article.getArticleContent()));
            }
            FqUser oUser = userService.selectByPrimaryKey(article.getUserId());
            GeneralCommentExample commentExample = new GeneralCommentExample();
            commentExample.setOrderByClause("create_time ");
            commentExample.createCriteria().andTopicIdEqualTo(articleId).andDelFlagEqualTo(YesNoEnum.NO.getValue()).andTopicTypeEqualTo(TopicTypeEnum.ARTICLE_TYPE.getValue());
            List<DetailCommentResponse> commentList = commentService.selectUserByExample(commentExample);
            model.addAttribute("commentList", commentList);
            model.addAttribute("article", article);
            model.addAttribute("oUser", oUser);

            //查询类似的文章 类别
            ArticleExample articleExample = new ArticleExample();
            articleExample.createCriteria().andLabelEqualTo(article.getLabel()).andDelFlagEqualTo(YesNoEnum.NO.getValue());
            articleExample.setOrderByClause("browse_count desc");
            PageHelper.startPage(1, 10, false);
            List<Article> articles = articleService.selectByExample(articleExample);
            articles = articles.stream().filter(e -> !e.getId().equals(article.getId())).collect(Collectors.toList());
            model.addAttribute("articles", articles);

            FqUserPayWayExample fqUserPayWayExample = new FqUserPayWayExample();
            fqUserPayWayExample.createCriteria().andDelFlagEqualTo(YesNoEnum.NO.getValue()).andUserIdEqualTo(article.getUserId());
            List<FqUserPayWay> fqUserPayWays = fqUserPayWayService.selectByExample(fqUserPayWayExample);
            if (CollectionUtil.isNotEmpty(fqUserPayWays)) {
                Map<Integer, String> map = MapUtil.newHashMap();
                fqUserPayWays.forEach(e -> {
                    map.put(e.getPayWay(), e.getPayImgUrl());
                });
                model.addAttribute("payMap", map);
            }
            model.addAttribute("advertisements", CommonConstant.FQ_ADVERTISEMENTS.get(AdvertisementPositionEnum.DETAIL.getPosition()));
        } catch (Exception e) {
            LOGGER.error("查看文章详情报错", e);
            model.addAttribute(CommonConstant.GENERAL_CUSTOM_ERROR_CODE, "查看文章详情报错，请联系管理员");
            return GENERAL_CUSTOM_ERROR_URL;
        }
        return "/article/detail";
    }

    @ResponseBody
    @RepeatSubmit
    @RequestMapping(value = "comment", method = RequestMethod.POST)
    public Object comment(GeneralComment comment, HttpServletRequest request, HttpServletResponse response) {
        LOGGER.info("articleReply:" + JSON.toJSONString(comment));
        BaseResult result = new BaseResult();
        try {
            FqUserCache user = webUtil.currentUser(request, response);
            if (user == null) {
                result.setResult(ResultEnum.USER_NOT_LOGIN);
                return result;
            }
            Article article = articleService.selectByPrimaryKey(comment.getTopicId());
            if (article == null) {
                result.setResult(ResultEnum.FAIL);
                return result;
            }
            if (!user.getId().equals(comment.getPostUserId())) {
                result.setResult(ResultEnum.USER_NOT_SAME);
                return result;
            }
            String commentContent = comment.getContent();
            if (StringUtils.isBlank(commentContent)) {
                return error("评论内容不能为空");
            }
            Date now = new Date();
            comment.setCreateTime(now);
            comment.setTopicType(TopicTypeEnum.ARTICLE_TYPE.getValue());
            comment.setDelFlag(YesNoEnum.NO.getValue());
            commentService.insert(comment);
            article.setCommentCount(article.getCommentCount() == null ? 1 : article.getCommentCount() + 1);
            articleService.updateByPrimaryKey(article);
            String shortCommentContent = CommonUtils.getShortThoughtContent(commentContent);
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
                            String content = "系统消息通知：<a class=\"c-fly-link\" href=\"" + CommonConstant.DOMAIN_URL + "/u/" + user.getId() + "\" target=\"_blank\">" + user.getNickname() + " </a> " +
                                    "在此<a class=\"c-fly-link\" href=\"" + CommonConstant.DOMAIN_URL + "/article/" + article.getId() + "\" target=\"_blank\">文章中</a>中@你并回复："+shortCommentContent;
                            CommonUtils.sendOfficialMsg(aiteUser.getId(),now,content);
                        }
                    }
                }
            }

            if(!user.getId().equals(article.getUserId()) && aiteSize == 0) {
                CMessage message = new CMessage();
                message.setPostUserId(-1);
                message.setCreateTime(new Date());
                message.setDelFlag(YesNoEnum.NO.getValue());
                message.setReceivedUserId(article.getUserId());
                message.setType(MsgEnum.OFFICIAL_MSG.getValue());
                message.setContent("系统消息通知：<a class=\"c-fly-link\" href=\"" + CommonConstant.DOMAIN_URL + "/u/" + user.getId() +
                        "\" target=\"_blank\">" + user.getNickname() + " </a>评论了你的" +
                        "<a class=\"c-fly-link\" href=\"" + CommonConstant.DOMAIN_URL + "/article/" + article.getId()
                        + "\" target=\"_blank\">文章</a>："+shortCommentContent);
                messageService.insert(message);
            }
            CommonUtils.addActiveNum(user.getId(), ActiveNumEnum.POST_COMMENT.getValue());
            result.setData(comment);
        } catch (Exception e) {
            LOGGER.error("文章评论出错", e);
            result.setResult(ResultEnum.FAIL);
        }
        return result;
    }

    @PostMapping("like")
    @ResponseBody
    public Object like(Integer articleId) {
        BaseResult result = new BaseResult();
        try {
            FqUserCache user = getCurrentUser();
            if (user == null) {
                result.setResult(ResultEnum.USER_NOT_LOGIN);
                return result;
            }
            Article article = articleService.selectByPrimaryKey(articleId);
            if (article == null) {
                result.setResult(ResultEnum.PARAM_NULL);
                return result;
            }
            GeneralLikeExample likeExample = new GeneralLikeExample();
            likeExample.createCriteria().andPostUserIdEqualTo(user.getId())
                    .andTopicIdEqualTo(articleId).andTopicTypeEqualTo(TopicTypeEnum.ARTICLE_TYPE.getValue())
                    .andDelFlagEqualTo(YesNoEnum.NO.getValue());
            GeneralLike like = likeService.selectFirstByExample(likeExample);
            if (like != null && like.getLikeValue() == 1) {
                result.setResult(ResultEnum.USER_ALREADY_LIKE);
                return result;
            }
            like = new GeneralLike(articleId, TopicTypeEnum.ARTICLE_TYPE.getValue(), 1, user.getId(), new Date(), YesNoEnum.NO.getValue());
            likeService.insert(like);
            article.setLikeCount(article.getLikeCount() == null ? 1 : article.getLikeCount() + 1);
            articleService.updateByPrimaryKey(article);

            if (!user.getId().equals(article.getUserId())) {
                CMessage message = new CMessage();
                message.setPostUserId(-1);
                message.setCreateTime(new Date());
                message.setDelFlag(YesNoEnum.NO.getValue());
                message.setReceivedUserId(article.getUserId());
                message.setType(MsgEnum.OFFICIAL_MSG.getValue());
                if (user.getNickname().length() > 20) {
                    user.setNickname(StringUtils.substring(user.getNickname(), 0, 20));
                }
                message.setContent("系统消息通知：<a class=\"c-fly-link\" href=\"" + CommonConstant.DOMAIN_URL + "/u/" + user.getId() +
                        "\" target=\"_blank\">" + user.getNickname() + " </a>赞了你的" +
                        "<a class=\"c-fly-link\" href=\"" + CommonConstant.DOMAIN_URL + "/article/" + article.getId() + "\" target=\"_blank\">文章</a> "
                        + DateUtil.formatDateTime(new Date()));
                messageService.insert(message);
            }
            CommonUtils.addActiveNum(user.getId(), ActiveNumEnum.POST_LIKE.getValue());
        } catch (Exception e) {
            LOGGER.error("article like error", e);
            result.setResult(ResultEnum.FAIL);
        }
        return result;
    }

    @PostMapping("delete")
    @ResponseBody
    public Object delete(Integer articleId, HttpServletRequest request, HttpServletResponse response) {
        BaseResult result = new BaseResult();
        try {
            FqUserCache user = webUtil.currentUser(request, response);
            if (user == null) {
                result.setResult(ResultEnum.USER_NOT_LOGIN);
                return result;
            }
            Article article = articleService.selectByPrimaryKey(articleId);
            if (article != null) {
                if (user.getId().equals(article.getUserId()) || UserRoleEnum.ADMIN_USER_ROLE.getValue().equals(user.getRole())) {
                    article.setDelFlag(YesNoEnum.YES.getValue());
                    articleService.updateByPrimaryKeySelective(article);
                    CommonUtils.addActiveNum(article.getUserId(), -ActiveNumEnum.POST_ARTICLE.getValue());
                } else {
                    result.setResult(ResultEnum.DELETE_NOT_MY);
                }
            } else {
                result.setResult(ResultEnum.FAIL);
            }
        } catch (Exception e) {
            LOGGER.error("article comment delete error", e);
            result.setResult(ResultEnum.FAIL);
        }
        return result;
    }

    @PostMapping("deleteComment")
    @ResponseBody
    public Object deleteComment(Integer commentId, HttpServletRequest request, HttpServletResponse response) {
        BaseResult result = new BaseResult();
        try {
            FqUserCache user = webUtil.currentUser(request, response);
            if (user == null) {
                result.setResult(ResultEnum.USER_NOT_LOGIN);
                return result;
            }
            GeneralComment comment = commentService.selectByPrimaryKey(commentId);
            if (comment != null) {
                comment.setDelFlag(YesNoEnum.YES.getValue());
                commentService.updateByPrimaryKey(comment);
            } else {
                result.setResult(ResultEnum.FAIL);
            }
        } catch (Exception e) {
            LOGGER.error("comment delete error", e);
            result.setResult(ResultEnum.FAIL);
        }
        return result;
    }

    /*
    我的文章
     */
    @GetMapping(value = "/my")
    public String myArticles(HttpServletRequest request, HttpServletResponse response,
                             @RequestParam(defaultValue = "1") Integer page,
                             Model model) {
        try {
            FqUserCache user = webUtil.currentUser(request, response);
            if (user == null) {
                return "/login";
            }
            PageHelper.startPage(page, 20);
            ArticleExample example = new ArticleExample();
            example.createCriteria().andUserIdEqualTo(user.getId()).andDelFlagEqualTo(YesNoEnum.NO.getValue());
            example.setOrderByClause("create_time desc");
            List<ArticleUserDetail> articles = articleService.selectUserByExampleWithBLOBs(example);

            PageInfo pageInfo = new PageInfo(articles);
            FqLabelExample labelExample = new FqLabelExample();
            labelExample.createCriteria().andDelFlagEqualTo(YesNoEnum.NO.getValue()).andTypeEqualTo(TopicTypeEnum.ARTICLE_TYPE.getValue());
            List<FqLabel> labels = fqLabelService.selectByExample(labelExample);
            Map<Integer, String> map = MapUtil.newHashMap();
            for (FqLabel label : labels) {
                map.put(label.getId(), label.getName());
            }
            model.addAttribute("labels", map);
            model.addAttribute("articles", articles);
            model.addAttribute("count", pageInfo.getTotal());
            model.addAttribute("p", page);
            model.addAttribute("pageSize", 20);
        } catch (Exception e) {
            LOGGER.error("获取我的文章失败！", e);
        }
        return "/article/articles";
    }
    @PostMapping("/collect/{type}")
    @ResponseBody
    public Object collect(@PathVariable String type, Integer aid, HttpServletRequest request, HttpServletResponse response) {
        BaseResult result = new BaseResult();
        try {
            FqUserCache fqUser = webUtil.currentUser(request, response);
            if (fqUser == null) {
                result.setResult(ResultEnum.USER_NOT_LOGIN);
                return result;
            }
            Article articleDB = articleService.selectByPrimaryKey(aid);
            if (articleDB == null) {
                result.setResult(ResultEnum.ARTICLE_NOT_EXITS);
                return result;
            }
            FqCollectExample collectExample = new FqCollectExample();
            collectExample.createCriteria().andTopicIdEqualTo(aid)
                    .andTopicTypeEqualTo(TopicTypeEnum.ARTICLE_TYPE.getValue())
                    .andUserIdEqualTo(fqUser.getId());
            FqCollect fqCollect = fqCollectService.selectFirstByExample(collectExample);

            if ("add".equals(type)) {
                if (fqCollect == null) {
                    fqCollect = new FqCollect();
                    fqCollect.setTopicId(aid);
                    fqCollect.setTopicType(TopicTypeEnum.ARTICLE_TYPE.getValue());
                    fqCollect.setCreateTime(new Date());
                    fqCollect.setDelFlag(YesNoEnum.NO.getValue());
                    fqCollect.setUserId(fqUser.getId());
                    fqCollectService.insert(fqCollect);
                } else {
                    fqCollect.setDelFlag(YesNoEnum.NO.getValue());
                    fqCollectService.updateByPrimaryKey(fqCollect);
                }
            } else if ("remove".equals(type)) {
                if (fqCollect == null) {
                    result.setResult(ResultEnum.PARAM_NULL);
                    return result;
                } else {
                    fqCollect.setDelFlag(YesNoEnum.YES.getValue());
                    fqCollectService.updateByPrimaryKey(fqCollect);
                }
            } else {
                result.setResult(ResultEnum.PARAM_ERROR);
                return result;
            }
            result.setData(fqCollect.getId());
        } catch (Exception e) {
            LOGGER.error("article collect error", e);
        }
        return result;
    }


    @PostMapping("/collection/find")
    @ResponseBody
    public Object collectFind(Integer aid, HttpServletRequest request, HttpServletResponse response) {
        BaseResult result = new BaseResult();
        try {
            FqUserCache fqUser = webUtil.currentUser(request, response);
            if (fqUser == null) {
                result.setData(null);
                return result;
            }
            Article articleDB = articleService.selectByPrimaryKey(aid);
            if (articleDB == null) {
                result.setData(null);
                return result;
            }
            FqCollectExample collectExample = new FqCollectExample();
            collectExample.createCriteria().andTopicIdEqualTo(aid)
                    .andTopicTypeEqualTo(TopicTypeEnum.ARTICLE_TYPE.getValue())
                    .andUserIdEqualTo(fqUser.getId());
            FqCollect fqCollect = fqCollectService.selectFirstByExample(collectExample);

            if (fqCollect == null) {
                result.setData(null);
                return result;
            } else {
                if (fqCollect.getDelFlag().equals(YesNoEnum.YES.getValue())) {
                    result.setData(null);
                    return result;
                }
                if (fqCollect.getDelFlag().equals(YesNoEnum.NO.getValue())) {
                    result.setData(fqCollect.getId());
                    return result;
                }
            }
            result.setData(null);
        } catch (Exception e) {
            LOGGER.error("article collect error", e);
        }
        return result;
    }
}
