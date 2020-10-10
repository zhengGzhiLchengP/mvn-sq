package com.feiqu.web.controller.collectData;


import com.feiqu.common.base.BaseResult;
import com.feiqu.common.enums.AdvertisementPositionEnum;
import com.feiqu.framwork.constant.CommonConstant;
import com.feiqu.framwork.util.CommonUtils;
import com.feiqu.framwork.web.base.BaseController;
import com.feiqu.system.model.collectData.HotTopic;
import com.feiqu.system.model.collectData.HotTopicExample;
import com.feiqu.system.service.collectData.HotTopicService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;


/**
 * HotTopiccontroller 热点数据
 * Created by cwd on 2019/7/17.
 */
@Controller
@RequestMapping("/hotTopic")
public class HotTopicController extends BaseController {

    private static Logger logger = LoggerFactory.getLogger(HotTopicController.class);

    @Resource
    private HotTopicService hotTopicService;
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    /**
     * 跳转到HotTopic首页
     */
    @RequestMapping("")
    public String index(Model model) {
        int watchCountInt = 0;
        String key = "fq:hottopic:index";
        String watchCount = stringRedisTemplate.opsForValue().get(key);
        if (org.apache.commons.lang3.StringUtils.isEmpty(watchCount)) {
        } else {
            watchCountInt = Integer.valueOf(watchCount);
        }
        model.addAttribute("watchCountInt", watchCountInt);

        watchCountInt += 1;
        stringRedisTemplate.opsForValue().set(key,watchCountInt + "");

        String ip = getIP();
        model.addAttribute("ip", ip);
        model.addAttribute("area", CommonUtils.getRegionByIp(ip));
        model.addAttribute("advertisements", getAdvertisements(AdvertisementPositionEnum.LIST));
        return "/front/hotTopic/index";
    }


    /**
     * 查询HotTopic首页
     */
    @GetMapping("list")
    @ResponseBody
    public Object list(@RequestParam(defaultValue = "0") Integer page, String source, String keyword) {
        BaseResult result = new BaseResult();
        try {
            PageHelper.startPage(page, CommonConstant.DEAULT_PAGE_SIZE);
            HotTopicExample example = new HotTopicExample();
            example.setOrderByClause("id desc");
            HotTopicExample.Criteria criteria = example.createCriteria();
            if (StringUtils.isNotEmpty(source)) {
                criteria.andSourceEqualTo(source);
            }
            if (StringUtils.isNotEmpty(keyword)) {
                criteria.andTitleLike("%" + keyword + "%");
                logger.info("热点查询："+keyword);
            }
            List<HotTopic> list = hotTopicService.selectByExample(example);
            return success(new PageInfo<>(list));
        } catch (Exception e) {
            logger.error("error", e);
            result.setCode("1");
        }
        return result;
    }
}