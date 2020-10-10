package com.feiqu.web.controller.resource;

import cn.hutool.core.date.DateUtil;
import cn.hutool.http.HtmlUtil;
import com.alibaba.fastjson.JSON;
import com.feiqu.common.base.BaseResult;
import com.feiqu.common.enums.*;
import com.feiqu.framwork.constant.CommonConstant;
import com.feiqu.framwork.util.CommonUtils;
import com.feiqu.framwork.web.base.BaseController;
import com.feiqu.system.model.*;
import com.feiqu.system.pojo.cache.FqUserCache;
import com.feiqu.system.pojo.response.DetailCommentResponse;
import com.feiqu.system.service.mainData.CMessageService;
import com.feiqu.system.service.mainData.FqNewsService;
import com.feiqu.system.service.mainData.FqUserService;
import com.feiqu.system.service.mainData.GeneralCommentService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.collections.CollectionUtils;
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
* FqNewscontroller
* Created by cwd on 2018/11/14.
*/
@Controller
@RequestMapping("/fqNews")
public class FqNewsController extends BaseController {

    private static Logger logger = LoggerFactory.getLogger(FqNewsController.class);

    @Resource
    private FqNewsService fqNewsService;
    @Resource
    private GeneralCommentService commentService;
    @Resource
    private FqUserService fqUserService;
    @Resource
    private CMessageService messageService;

    /**
    * 跳转到FqNews首页
    */
    @RequestMapping("")
    public String index(@RequestParam(defaultValue = "desc") String order, Model model,
                        @RequestParam(defaultValue = "1") Integer pageIndex) {
        PageHelper.startPage(pageIndex,20);
        FqNewsExample fqNewsExample = new FqNewsExample();
        fqNewsExample.setOrderByClause("gmt_create "+order);
        List<FqNews> fqNews = fqNewsService.selectByExample(fqNewsExample);
        model.addAttribute("fqNews",fqNews);
        PageInfo page = new PageInfo(fqNews);
        model.addAttribute("pageIndex",pageIndex);
        model.addAttribute("pageSize",20);//文章放多点好，感觉，要不然老是需要翻页
        model.addAttribute("count",page.getTotal());
        model.addAttribute("advertisements",CommonConstant.FQ_ADVERTISEMENTS.get(AdvertisementPositionEnum.LIST.getPosition()));

        return "/news/index";
    }

    /**
    * 添加FqNews页面
    */
    @RequestMapping("/fqNews_add")
    public String fqNews_add() {
        return "/system/FqNews/add";
    }

    /**
    * ajax添加FqNews
    */
    @ResponseBody
    @RequestMapping("/add")
    public Object add(FqNews fqNews) {
        BaseResult result = new BaseResult();
        fqNewsService.insert(fqNews);
        return result;
    }

    /**
    * ajax删除FqNews
    */
    @ResponseBody
    @RequestMapping("/delete")
    public Object delete(@RequestParam Integer fqNewsId) {
        BaseResult result = new BaseResult();
        fqNewsService.deleteByPrimaryKey(fqNewsId);
        return result;
    }

    /**
    * 更新FqNews页面
    */
    @RequestMapping("/edit/{fqNewsId}")
    public Object fqNewsEdit(@PathVariable Integer fqNewsId, Model model) {
        FqNews fqNews = fqNewsService.selectByPrimaryKey(fqNewsId);
        model.addAttribute("fqNews", fqNews);
        return "/system/FqNews/edit";
    }

    /**
     * 更新FqNews页面
     */
    @RequestMapping("/detail/{fqNewsId}")
    public String detail(@PathVariable Long fqNewsId, Model model) {
        FqNews fqNews = fqNewsService.selectByPrimaryKey(fqNewsId);
        if(fqNews == null){
            return "/404";
        }
        model.addAttribute("fqNews", fqNews);
        GeneralCommentExample commentExample = new GeneralCommentExample();
        commentExample.setOrderByClause("create_time desc");
        commentExample.createCriteria().andTopicIdEqualTo(fqNewsId.intValue()).andDelFlagEqualTo(YesNoEnum.NO.getValue()).andTopicTypeEqualTo(TopicTypeEnum.NEWS_TYPE.getValue());
        List<DetailCommentResponse> commentList = commentService.selectUserByExample(commentExample);
        model.addAttribute("commentList",commentList);
        FqNewsExample fqNewsExample = new FqNewsExample();
        fqNewsExample.createCriteria().andSourceEqualTo(fqNews.getSource()).andIdNotEqualTo(fqNewsId);
        PageHelper.startPage(1,CommonConstant.DEAULT_PAGE_SIZE,false);
        List<FqNews> fqNewsSame = fqNewsService.selectByExample(fqNewsExample);
        model.addAttribute("sameSource",fqNewsSame);
        return "/news/detail";
    }

    @RequestMapping("/detailJson/{fqNewsId}")
    @ResponseBody
    public Object detailJson(@PathVariable Long fqNewsId) {
        FqNews fqNews = fqNewsService.selectByPrimaryKey(fqNewsId);
        return HtmlUtil.cleanHtmlTag(fqNews.getContent());
    }

    /**
    * ajax更新FqNews
    */
    @ResponseBody
    @RequestMapping("/edit")
    public Object edit(FqNews fqNews) {
        BaseResult result = new BaseResult();
        fqNewsService.updateByPrimaryKey(fqNews);
        return result;
    }

    /**
    * 查询FqNews首页
    */
    @RequestMapping("list")
    @ResponseBody
    public Object list(@RequestParam(defaultValue = "0") Integer index, @RequestParam(defaultValue = "10") Integer size) {
        BaseResult result = new BaseResult();
        PageHelper.startPage(index, size);
        FqNewsExample example = new FqNewsExample();
        example.setOrderByClause("ID desc");
        List<FqNews> list = fqNewsService.selectByExample(example);
        if(CollectionUtils.isNotEmpty(list)){
            PageInfo page = new PageInfo(list);
            result.setData(page);
        }else {
            PageInfo page = new PageInfo();
            result.setData(page);
        }

        return result;
    }

    @ResponseBody
    @RequestMapping(value = "comment",method = RequestMethod.POST)
    public Object articleReply(GeneralComment comment){
        logger.info("newsReply:"+JSON.toJSONString(comment));
        BaseResult result = new BaseResult();
        try {
            FqUserCache user = getCurrentUser();
            if(user == null){
                result.setResult(ResultEnum.USER_NOT_LOGIN);
                return result;
            }
            FqNews fqNews = fqNewsService.selectByPrimaryKey(comment.getTopicId().longValue());
            if(fqNews == null){
                result.setResult(ResultEnum.FAIL);
                return result;
            }
            if(StringUtils.isBlank(comment.getContent())){
                result.setResult(ResultEnum.PARAM_NULL);
                result.setMessage("评论内容不能为空");
                return result;
            }
            Date now = new Date();
            comment.setCreateTime(now);
            comment.setTopicType(TopicTypeEnum.NEWS_TYPE.getValue());
            comment.setDelFlag(YesNoEnum.NO.getValue());
            commentService.insert(comment);
            List<String> aiteNames = CommonUtils.findAiteNicknames(comment.getContent());
            if(aiteNames.size() > 0){
                for(String aiteNickname : aiteNames){
                    FqUserExample example = new FqUserExample();
                    example.createCriteria().andNicknameEqualTo(aiteNickname);
                    FqUser aiteUser = fqUserService.selectFirstByExample(example);
                    if(aiteUser != null){
                        if(!aiteUser.getId().equals(user.getId())){
                            CMessage message = new CMessage();
                            message.setPostUserId(-1);
                            message.setCreateTime(now);
                            message.setDelFlag(YesNoEnum.NO.getValue());
                            message.setReceivedUserId(aiteUser.getId());
                            message.setType(MsgEnum.OFFICIAL_MSG.getValue());
                            message.setContent("系统消息通知：<a class=\"c-fly-link\" href=\""+ CommonConstant.DOMAIN_URL+"/u/"+user.getId()+"\" target=\"_blank\">"+user.getNickname()+" </a> " +
                                    "在<a class=\"c-fly-link\" href=\"" + CommonConstant.DOMAIN_URL + "/fqNews/" + comment.getTopicId() +"\" target=\"_blank\">此新闻</a>中回复了你 -"+ DateUtil.formatDateTime(now));
                            messageService.insert(message);
                        }
                    }
                }
            }
            result.setData(comment);
        } catch (Exception e) {
            logger.error("新闻评论出错",e);
            result.setResult(ResultEnum.FAIL);
        }
        return result;
    }

}