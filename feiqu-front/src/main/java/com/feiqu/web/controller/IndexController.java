package com.feiqu.web.controller;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.CircleCaptcha;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.net.NetUtil;
import com.alibaba.fastjson.JSON;
import com.feiqu.common.constant.BizConstant;
import com.feiqu.common.enums.*;
import com.feiqu.framwork.constant.CommonConstant;
import com.feiqu.framwork.support.cache.CacheManager;
import com.feiqu.framwork.web.base.BaseController;
import com.feiqu.system.model.*;
import com.feiqu.system.model.collectData.Soul;
import com.feiqu.system.model.collectData.SoulExample;
import com.feiqu.system.pojo.response.ArticleUserDetail;
import com.feiqu.system.pojo.response.UserActiveNumResponse;
import com.feiqu.system.pojo.simple.FqUserSim;
import com.feiqu.system.service.collectData.SoulService;
import com.feiqu.system.service.mainData.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.assertj.core.util.Lists;
import org.beetl.ext.spring.BeetlGroupUtilConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author cwd
 * @create 2017-09-10:51
 **/
@Controller
public class IndexController extends BaseController {

    private static Logger logger = LoggerFactory.getLogger(IndexController.class);

    /**
     * 文章
     */
    @Resource
    private ArticleService articleService;
    /**
     * 标签
     */
    @Resource
    private FqLabelService fqLabelService;
    /**
     * 用户表 fqUser
     */
    @Resource
    private FqUserService fqUserService;
    @Resource
    BeetlGroupUtilConfiguration beetlContentTemplateConfig;
    @Resource
    private FqNoticeService fqNoticeService;
    @Resource
    private ThoughtService thoughtService;
    @Resource
    private SuperBeautyService beautyService;
    @Resource
    private SoulService soulService;
    @Resource
    private FqAdvertisementService fqAdvertisementService;
    private static final String CACHE_FQ_ACTIVE_USER_SORT = "cache_fq_active_user_sort";

    @Resource
    private StringRedisTemplate stringRedisTemplate;
    //广场 通用的功能
    @GetMapping("playground")
    public String playground(Model model){
        try{
            FqNoticeExample fqNoticeExample = new FqNoticeExample();
            fqNoticeExample.setOrderByClause("fq_order asc");
            fqNoticeExample.createCriteria().andIsShowEqualTo(YesNoEnum.YES.getValue());
            List<FqNotice> list = fqNoticeService.selectByExample(fqNoticeExample);
            model.addAttribute("noticeList", list);
            FqAdvertisementExample fqAdvertisementExample = new FqAdvertisementExample();
            fqAdvertisementExample.createCriteria().andPositionEqualTo(AdvertisementPositionEnum.INDEX_BANNER.getPosition());
            fqAdvertisementExample.setOrderByClause("GMT_CREATE DESC");
            List<FqAdvertisement> advertisements = fqAdvertisementService.selectByExample(fqAdvertisementExample);
            model.addAttribute("advertisements", advertisements);

            SoulExample soulExample = new SoulExample();
            soulExample.setOrderByClause("rand( ) limit 1");
            Soul soul = soulService.selectFirstByExample(soulExample);
            if (soul != null) {
                model.addAttribute("soulTitle", soul.getTitle());
            }
            int month = DateUtil.thisMonth()+1;

            String activeUsers = stringRedisTemplate.opsForValue().get(CACHE_FQ_ACTIVE_USER_SORT);
            if(StringUtils.isEmpty(activeUsers)){
                Set<ZSetOperations.TypedTuple<String>> tuples =stringRedisTemplate.opsForZSet().reverseRangeWithScores(CommonConstant.FQ_ACTIVE_USER_SORT+month,0,10);
                if(CollectionUtils.isNotEmpty(tuples)){
                    List<Integer> userIdList = tuples.stream().map(e->Integer.valueOf(e.getValue())).collect(Collectors.toList());
                    FqUserExample example = new FqUserExample();
                    example.createCriteria().andIdIn(userIdList);
                    List<FqUser> fqUsers = fqUserService.selectByExample(example);
                    Map<Integer, FqUser> userMap = MapUtil.newHashMap();
                    fqUsers.forEach(fqUser -> {
                        userMap.put(fqUser.getId(),fqUser);
                    });
                    List<UserActiveNumResponse> responses = Lists.newArrayList();
                    int i = 1;
                    for(ZSetOperations.TypedTuple<String> tuple : tuples){
                        FqUser fqUser = userMap.get(Integer.valueOf(tuple.getValue()));
                        if(fqUser == null){
                            continue;
                        }
                        UserActiveNumResponse userActiveNumResponse = new UserActiveNumResponse(fqUser,tuple.getScore(),i);
                        responses.add(userActiveNumResponse);
                        i++;
                    }
                    model.addAttribute("thisMonthActiveUsers",responses);
                    stringRedisTemplate.opsForValue().set(CACHE_FQ_ACTIVE_USER_SORT,JSON.toJSONString(responses),5, TimeUnit.MINUTES);
                }
            }else {
                List<UserActiveNumResponse> responses = JSON.parseArray(activeUsers,UserActiveNumResponse.class);
                model.addAttribute("thisMonthActiveUsers",responses);
            }

            FqUserExample userExample = new FqUserExample();
            userExample.setOrderByClause("id desc");
            PageHelper.startPage(0, BizConstant.MAX_NEW_USER_COUNT, false);
            List<FqUser> newUsers = fqUserService.selectByExample(userExample);
            List<FqUserSim> sims = newUsers.stream().map(FqUserSim::new).collect(Collectors.toList());
            model.addAttribute("newUserList", sims);

            model.addAttribute("activeNums", ActiveNumEnum.values());
            model.addAttribute("advertisements", getAdvertisements(AdvertisementPositionEnum.LIST));

        }catch (Exception e){
            logger.error("广场出错",e);
            return GENERAL_CUSTOM_ERROR_URL;
        }
        return "/playground";
    }

    @RequestMapping(value = {"index", "", "index.html", "index.jsp", "index.php","superGeek"})
    public String superGeekIndex(Model model, Integer labelId, String order, String keyword) {
        return superGeek(model, 1, order, labelId, keyword);
    }

    @RequestMapping(value = "/superGeek/{pageIndex}")
    public String superGeek(Model model, @PathVariable(required = false) Integer pageIndex,
                            String order, Integer labelId, String keyword) {
        PageHelper.startPage(pageIndex, 20);
        ArticleExample example = new ArticleExample();
        if ("hot".equals(order)) {
            example.setOrderByClause("like_count desc,comment_count desc ");
        } else {
            example.setOrderByClause("id desc");
        }
        if (labelId == null) {
            example.createCriteria().andDelFlagEqualTo(YesNoEnum.NO.getValue());
        } else {
            example.createCriteria().andDelFlagEqualTo(YesNoEnum.NO.getValue()).andLabelEqualTo(labelId);
            model.addAttribute("labelId", labelId);
        }
        if (StringUtils.isNotBlank(keyword)) {
            example.getOredCriteria().get(0).andArticleTitleLike("%" + keyword + "%");
        }
        List<ArticleUserDetail> articles = articleService.selectUserByExampleWithBLOBs(example);
        example.clear();
        example.setOrderByClause("id desc");
        example.createCriteria().andIsRecommendEqualTo(YesNoEnum.YES.getValue()).andDelFlagEqualTo(YesNoEnum.NO.getValue());
        List<Article> recommendArticles = articleService.selectByExample(example);
        PageInfo page = new PageInfo(articles);
        model.addAttribute("recommendArticles", recommendArticles);
        model.addAttribute("articles", articles);
        model.addAttribute("pageIndex", pageIndex);
        model.addAttribute("pageSize", 20);//文章放多点好，感觉，要不然老是需要翻页
        model.addAttribute("count", page.getTotal());
        model.addAttribute("order", order);
        model.addAttribute("articleList", CommonConstant.HOT_ARTICLE_LIST);
        model.addAttribute("allArticleList", CommonConstant.ALL_HOT_ARTICLE_LIST);
        FqLabelExample labelExample = new FqLabelExample();
        labelExample.createCriteria().andDelFlagEqualTo(YesNoEnum.NO.getValue()).andTypeEqualTo(TopicTypeEnum.ARTICLE_TYPE.getValue());
        List<FqLabel> labels = fqLabelService.selectByExample(labelExample);
        if(CollectionUtils.isNotEmpty(labels)){
            Map<Integer, String> map = MapUtil.newHashMap();
            for (FqLabel label : labels) {
                map.put(label.getId(), label.getName());
            }
            model.addAttribute("labels", map);
        }

        return "/superGeek";
    }

    /**
     * @param request
     * @param response
     * @param type     1 pc 2 h5
     */
    @GetMapping(value = "/captcha")
    @ResponseBody
    public void captcha(HttpServletResponse response, @RequestParam(defaultValue = "1", required = false) Integer type) {
        try {
//            LineCaptcha lineCaptcha = null;
            CircleCaptcha circleCaptcha = null;
             circleCaptcha = CaptchaUtil.createCircleCaptcha(320, 50);
            circleCaptcha.createCode();
            String code = circleCaptcha.getCode();
            String ip = getIP();
            if("0:0:0:0:0:0:0:1".equals(ip)){
                ip = NetUtil.LOCAL_IP;
            }
            //验证码 60s
            stringRedisTemplate.opsForValue().set(CacheManager.getCaptchaKey(ip),code, 60,TimeUnit.SECONDS);
            ImageIO.write(circleCaptcha.getImage(), "JPEG", response.getOutputStream());
        } catch (Exception e) {
            logger.error("获取验证码出错", e);
        }
    }

    @GetMapping("jump")
    public String about(String username, Model model) {
        FqUserExample example = new FqUserExample();
        example.createCriteria().andNicknameEqualTo(username);
        FqUser fqUser = fqUserService.selectFirstByExample(example);
        if (fqUser == null) {
            return "redirect:/";
        }
        if (UserStatusEnum.DEL.getValue().equals(fqUser.getStatus())) {
            model.addAttribute(CommonConstant.GENERAL_CUSTOM_ERROR_CODE, "用户已经被删除");
            return GENERAL_CUSTOM_ERROR_URL;
        } else if (UserStatusEnum.FROZEN.getValue().equals(fqUser.getStatus())) {
            model.addAttribute(CommonConstant.GENERAL_CUSTOM_ERROR_CODE, "用户已经被禁用");
            return GENERAL_CUSTOM_ERROR_URL;
        } else if (UserStatusEnum.NORMAL.getValue().equals(fqUser.getStatus())) {
            return "redirect:/u/" + fqUser.getId();
        }
        return "redirect:/";
    }


}
