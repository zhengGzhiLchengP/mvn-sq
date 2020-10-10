package com.feiqu.web.controller.support;

import com.feiqu.common.enums.AdvertisementPositionEnum;
import com.feiqu.common.enums.YesNoEnum;
import com.feiqu.framwork.constant.CommonConstant;
import com.feiqu.framwork.web.base.BaseController;
import com.feiqu.system.model.*;
import com.feiqu.system.service.mainData.ArticleService;
import com.feiqu.system.service.mainData.FqNewsService;
import com.feiqu.system.service.mainData.FqTopicService;
import com.feiqu.system.service.mainData.ThoughtService;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;

//全局搜索
@Controller
@RequestMapping("search")
public class SearchController extends BaseController {

    private Logger logger = LoggerFactory.getLogger(SearchController.class);

    @Resource
    private ArticleService articleService;
    @Resource
    private FqNewsService fqNewsService;
    @Resource
    private FqTopicService fqTopicService;
    @Resource
    private ThoughtService thoughtService;
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 搜索主页
     *
     * @param keyword
     * @param model
     * @return
     */
    @GetMapping("index")
    public String index(String keyword, Model model) {
        if (StringUtils.isBlank(keyword)) {
            return "/search/index";
        }
        logger.info("keyword{}",keyword);
        String key = "fq:search:keywords";
        Set<String> keywords = stringRedisTemplate.opsForSet().members(key);
        model.addAttribute("keywords", keywords);
        if(keywords != null && keywords.size() > 50){
            String value = stringRedisTemplate.opsForSet().randomMember(key);
            stringRedisTemplate.opsForSet().remove(key,value);
        }
        stringRedisTemplate.opsForSet().add(key,keyword);
        PageHelper.startPage(1, CommonConstant.DEAULT_PAGE_SIZE,false);
        ThoughtExample thoughtExample = new ThoughtExample();
        thoughtExample.createCriteria().andThoughtContentLike("%" + keyword + "%").andDelFlagEqualTo(YesNoEnum.NO.getValue());
        thoughtExample.setOrderByClause("id desc");
        List<Thought> thoughts = thoughtService.selectByExample(thoughtExample);
        model.addAttribute("thoughts", thoughts);
        PageHelper.startPage(1, CommonConstant.DEAULT_PAGE_SIZE,false);
        ArticleExample articleExample = new ArticleExample();
        articleExample.createCriteria().andArticleTitleLike("%" + keyword + "%").andDelFlagEqualTo(YesNoEnum.NO.getValue());
        articleExample.setOrderByClause("id desc");
        List<Article> articleList = articleService.selectByExample(articleExample);
        model.addAttribute("articleList", articleList);
        PageHelper.startPage(1, CommonConstant.DEAULT_PAGE_SIZE,false);
        FqNewsExample fqNewsExample = new FqNewsExample();
        fqNewsExample.createCriteria().andTitleLike("%" + keyword + "%");
        fqNewsExample.setOrderByClause("id desc");
        List<FqNews> fqNewsList = fqNewsService.selectByExample(fqNewsExample);
        model.addAttribute("fqNewsList", fqNewsList);

        PageHelper.startPage(1, CommonConstant.DEAULT_PAGE_SIZE,false);
        FqTopicExample fqTopicExample = new FqTopicExample();
        fqTopicExample.createCriteria().andTitleLike("%" + keyword + "%");
        fqTopicExample.setOrderByClause("id desc");
        List<FqTopic> fqTopicList = fqTopicService.selectByExample(fqTopicExample);
        model.addAttribute("fqTopicList", fqTopicList);

        model.addAttribute("keyword", keyword);
        model.addAttribute("advertisements", getAdvertisements(AdvertisementPositionEnum.LIST));
        return "/search/index";
    }
}
