package com.feiqu.web.controller.collectData;


import com.feiqu.common.base.BaseResult;
import com.feiqu.common.enums.AdvertisementPositionEnum;
import com.feiqu.framwork.constant.CommonConstant;
import com.feiqu.framwork.web.base.BaseController;
import com.feiqu.system.model.collectData.FinanceNews;
import com.feiqu.system.model.collectData.FinanceNewsExample;
import com.feiqu.system.service.collectData.FinanceNewsService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@RequestMapping("/financeNews")
public class FinanceNewsController extends BaseController {

    private static Logger logger = LoggerFactory.getLogger(FinanceNewsController.class);

    @Resource
    private FinanceNewsService financeNewsService;

    /**
     * 跳转到HotTopic首页
     */
    @RequestMapping("")
    public String index(Model model) {
        model.addAttribute("advertisements", getAdvertisements(AdvertisementPositionEnum.LIST));
        return "/front/financeNews/financeNews";
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
            FinanceNewsExample example = new FinanceNewsExample();
            example.setOrderByClause("id desc");
            List<FinanceNews> list = financeNewsService.selectByExample(example);
            return success(new PageInfo<>(list));
        } catch (Exception e) {
            logger.error("error", e);
            result.setCode("1");
        }
        return result;
    }
}