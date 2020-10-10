package com.feiqu.web.controller.resource;


import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.lang.PatternPool;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ReUtil;
import com.alibaba.fastjson.JSON;
import com.feiqu.common.base.BaseResult;
import com.feiqu.common.enums.ResultEnum;
import com.feiqu.common.enums.YesNoEnum;
import com.feiqu.framwork.constant.CommonConstant;
import com.feiqu.framwork.support.cache.CacheManager;
import com.feiqu.framwork.web.base.BaseController;
import com.feiqu.system.model.FqWebsiteDir;
import com.feiqu.system.model.FqWebsiteDirExample;
import com.feiqu.system.pojo.cache.FqUserCache;
import com.feiqu.system.service.mainData.FqWebsiteDirService;
import com.github.pagehelper.PageHelper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.util.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * FqWebsiteDircontroller
 * Created by cwd on 2018/1/23.
 */
@Controller
@RequestMapping("/websiteDir")
public class FqWebsiteDirController extends BaseController {

    private static Logger logger = LoggerFactory.getLogger(FqWebsiteDirController.class);

    @Resource
    private FqWebsiteDirService fqWebsiteDirService;
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 跳转到FqWebsiteDir首页
     */
    @RequestMapping("")
    public String index(Model model, @RequestParam(defaultValue = "1") Integer page, String type, String word) {
        try {
            List<FqWebsiteDir> dirs;
            FqUserCache user = getCurrentUser();
            int uid = user == null?0:user.getId();
            FqWebsiteDirExample example = new FqWebsiteDirExample();
            FqWebsiteDirExample.Criteria criteria = example.createCriteria();
            criteria.andDelFlagEqualTo(YesNoEnum.NO.getValue());
            if(StringUtils.isNotEmpty(type)){
                criteria.andTypeEqualTo(type);
            }
            if(StringUtils.isNotEmpty(word)){
                criteria.andNameLike("%"+word+"%");
            }
            if(example.getOredCriteria().get(0).getAllCriteria().size() == 1){
                if(stringRedisTemplate.hasKey(CommonConstant.FQ_WEBSITE_ALL)){
                    String allWebsites = stringRedisTemplate.opsForValue().get(CommonConstant.FQ_WEBSITE_ALL);
                    dirs = JSON.parseArray(allWebsites,FqWebsiteDir.class);
                    if(CollectionUtils.isEmpty(dirs)){
                        dirs = fqWebsiteDirService.selectByExample(example);
                        stringRedisTemplate.opsForValue().set(CommonConstant.FQ_WEBSITE_ALL,JSON.toJSONString(dirs));
                    }
                }else {
                    dirs = fqWebsiteDirService.selectByExample(example);
                    stringRedisTemplate.opsForValue().set(CommonConstant.FQ_WEBSITE_ALL,JSON.toJSONString(dirs));
                }
            }else {
                dirs = fqWebsiteDirService.selectByExample(example);
            }
            Map<String,List<FqWebsiteDir>> map = dirs.stream().collect(Collectors.groupingBy(FqWebsiteDir::getType));

            List<FqWebsiteDir> personalHotWebs = Lists.newArrayList();
            PageHelper.startPage(0,20,false);
            example.clear();
            example.setOrderByClause("click_count desc");
            example.createCriteria().andDelFlagEqualTo(YesNoEnum.NO.getValue());
            List<FqWebsiteDir> commonHotWebs = fqWebsiteDirService.selectByExample(example);
            if(uid > 0){
                String key = CommonConstant.FQ_USER_WEBSITE_CLICK_COUNT +":"+uid;
                Set<ZSetOperations.TypedTuple<String>> tuples = stringRedisTemplate.opsForZSet().reverseRangeWithScores(key,0,9);
                if(CollectionUtil.isNotEmpty(tuples)){
                    List<String> eles = tuples.stream().map(ZSetOperations.TypedTuple::getValue).collect(Collectors.toList());
                    Map<Integer, FqWebsiteDir> websiteMap = MapUtil.newHashMap();
                    List<Integer> siteIds = Lists.newArrayList();
                    eles.forEach(e->siteIds.add(Integer.valueOf(e)));
                    example.clear();
                    example.createCriteria().andIdIn(siteIds).andDelFlagEqualTo(YesNoEnum.NO.getValue());
                    personalHotWebs = fqWebsiteDirService.selectByExample(example);
                    personalHotWebs.forEach(e->{
                        websiteMap.put(e.getId(), e);
                    });
                    personalHotWebs.clear();
                    for (ZSetOperations.TypedTuple<String> e : tuples) {
                        Integer id = Integer.valueOf(e.getValue());
                        FqWebsiteDir fqWebsiteDir = websiteMap.get(id);
                        if(fqWebsiteDir != null){
                            fqWebsiteDir.setClickCount(e.getScore().intValue());
                            personalHotWebs.add(fqWebsiteDir);
                        }else {
                            stringRedisTemplate.opsForZSet().remove(key,id.toString());
                        }
                    }
                }
            }

            model.addAttribute("commonHotWebs",commonHotWebs);
            model.addAttribute("personalHotWebs",personalHotWebs);

            model.addAttribute("dirs",dirs);
            List<String> types = fqWebsiteDirService.selectTypes();
            model.addAttribute("types",types);
            model.addAttribute("count",dirs.size());
            model.addAttribute("page",page);
            model.addAttribute("map",map);
            model.addAttribute("word",word);
        } catch (Exception e) {
            logger.error("网址页面报错",e);
            model.addAttribute(CommonConstant.GENERAL_CUSTOM_ERROR_CODE,"网址页面报错");
            return GENERAL_CUSTOM_ERROR_URL;
        }
        return "/websiteDir/index";
    }

    @ResponseBody
    @PostMapping("/record")
    public BaseResult record(FqWebsiteDir fqWebsiteDir) {
        BaseResult baseResult = new BaseResult();
        try {
            FqUserCache user = getCurrentUser();
            Integer uid = user == null?0:user.getId();
            if(StringUtils.isNotEmpty(fqWebsiteDir.getUrl())){
                FqWebsiteDirExample example = new FqWebsiteDirExample();
                example.createCriteria().andUrlEqualTo(fqWebsiteDir.getUrl()).andDelFlagEqualTo(YesNoEnum.NO.getValue());
                FqWebsiteDir fqWebsiteDirDB = fqWebsiteDirService.selectFirstByExample(example);
                if(fqWebsiteDirDB != null){
                    fqWebsiteDirDB.setClickCount(fqWebsiteDirDB.getClickCount() == null?1:fqWebsiteDirDB.getClickCount()+1);
                    fqWebsiteDirService.updateByPrimaryKey(fqWebsiteDirDB);
                    if(uid > 0){
                        //记录某个人的点击网站偏好
                        String key = CommonConstant.FQ_USER_WEBSITE_CLICK_COUNT +":"+uid;
                        Double scoreStore = stringRedisTemplate.opsForZSet().score(key,fqWebsiteDirDB.getId().toString());
                        if(scoreStore == null){
                            scoreStore = 1d;
                        }else {
                            scoreStore += 1;
                        }
                        stringRedisTemplate.opsForZSet().add(key,fqWebsiteDirDB.getId().toString(),scoreStore);
                    }
                }else {
                    logger.info("数据库未查到对应的url，网址为：{}",fqWebsiteDir.getUrl());
                }
            }
        } catch (Exception e) {
            logger.error("记录点击次数报错",e);
        }
        return baseResult;
    }

    /**
     * ajax添加FqWebsiteDir
     */
    @ResponseBody
    @PostMapping("/add")
    public Object add(FqWebsiteDir fqWebsiteDir, HttpServletRequest request, HttpServletResponse response) {
        BaseResult result = new BaseResult();
        if(StringUtils.isBlank(fqWebsiteDir.getName()) || StringUtils.isBlank(fqWebsiteDir.getType())){
            result.setResult(ResultEnum.PARAM_NULL);
            return result;
        }

        if(!ReUtil.isMatch(PatternPool.URL,fqWebsiteDir.getUrl())){
            result.setResult(ResultEnum.WEBSITE_URL_ERROR);
            return result;
        }
        FqUserCache fqUserCache = getCurrentUser();
        try {
            FqWebsiteDirExample example = new FqWebsiteDirExample();
            example.createCriteria().andUrlEqualTo(fqWebsiteDir.getUrl()).andDelFlagEqualTo(YesNoEnum.NO.getValue());
            FqWebsiteDir fqWebsiteDirDB = fqWebsiteDirService.selectFirstByExample(example);
            if(fqWebsiteDirDB != null){
                result.setResult(ResultEnum.WEBSITE_URL_EXISTS);
                return result;
            }

            fqWebsiteDir.setDelFlag(YesNoEnum.NO.getValue());
            fqWebsiteDir.setUserId(fqUserCache == null?0:fqUserCache.getId());
            fqWebsiteDir.setCreateTime(new Date());
            fqWebsiteDir.setClickCount(0);
            fqWebsiteDirService.insert(fqWebsiteDir);
            CacheManager.refreshWebsiteCache();
        } catch (Exception e) {
            logger.error("发生系统错误",e);
            result.setResult(ResultEnum.SYSTEM_ERROR);
            return result;
        }
        return result;
    }

    /**
     * ajax删除FqWebsiteDir
     */
    @ResponseBody
    @RequestMapping("/delete")
    public Object delete(@RequestParam Integer id) {
        BaseResult result = new BaseResult();
        try {
            FqUserCache fqUserCache = getCurrentUser();
            if(fqUserCache == null){
                result.setResult(ResultEnum.USER_NOT_LOGIN);
                return result;
            }
            logger.info("删除网址：用户id：{}，图片id：{}",fqUserCache.getId(),id);
            FqWebsiteDir fqWebsiteDir = fqWebsiteDirService.selectByPrimaryKey(id);
            Assert.notNull(fqWebsiteDir,"网址不能为空");
            fqWebsiteDir.setDelFlag(YesNoEnum.YES.getValue());
            fqWebsiteDirService.updateByPrimaryKey(fqWebsiteDir);
            CacheManager.refreshWebsiteCache();
        } catch (Exception e) {
            logger.error("删除网址报错",e);
            result.setResult(ResultEnum.SYSTEM_ERROR);
            return result;
        }
        return result;
    }

    /**
     * 更新FqWebsiteDir页面
     */
    @RequestMapping("/edit/{fqWebsiteDirId}")
    public Object fqWebsiteDirEdit(@PathVariable Integer fqWebsiteDirId, Model model) {
        FqWebsiteDir fqWebsiteDir = fqWebsiteDirService.selectByPrimaryKey(fqWebsiteDirId);
        model.addAttribute("fqWebsiteDir", fqWebsiteDir);
        return "/system/FqWebsiteDir/edit";
    }


    /**
     * 查询FqWebsiteDir首页
     */
    /*@RequestMapping("list")
    @ResponseBody
    public Object list(@RequestParam(defaultValue = "0") Integer index,
                       @RequestParam(defaultValue = "10") Integer size,FqWebsiteDir websiteDir) {
        BaseResult result = new BaseResult();
        PageHelper.startPage(index, size);
        FqWebsiteDirExample example = new FqWebsiteDirExample();
        FqWebsiteDirExample.Criteria criteria = example.createCriteria();
        if(StringUtils.isNotEmpty(websiteDir.getName())){
            criteria.andNameLike("%"+websiteDir.getName()+"%");
        }
        example.setOrderByClause("create_time desc");
        List<FqWebsiteDir> list = fqWebsiteDirService.selectByExample(example);
        PageInfo page = new PageInfo(list);
        result.setData(page);
        return result;
    }*/

    /**
     * 更新FqWebsiteDir页面
     */
    @RequestMapping("/techWiki")
    public Object techWiki() {
        return "/websiteDir/techWiki";
    }


}