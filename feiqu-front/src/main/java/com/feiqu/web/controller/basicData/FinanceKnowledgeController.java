package com.feiqu.web.controller.basicData;


import cn.hutool.core.lang.Assert;
import com.feiqu.common.annotation.RepeatSubmit;
import com.feiqu.common.base.BaseResult;
import com.feiqu.common.enums.ResultEnum;
import com.feiqu.framwork.web.base.BaseController;
import com.feiqu.system.model.basicData.FinanceKnowledge;
import com.feiqu.system.model.basicData.FinanceKnowledgeExample;
import com.feiqu.system.pojo.cache.FqUserCache;
import com.feiqu.system.service.basicData.FinanceKnowledgeService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;


/**
 * FinanceKnowledgecontroller
 * Created by cwd on 2019/7/9.
 */
@Controller
@RequestMapping("/financeKnowledge")
public class FinanceKnowledgeController extends BaseController {

    private static Logger logger = LoggerFactory.getLogger(FinanceKnowledgeController.class);

    @Resource
    private FinanceKnowledgeService financeKnowledgeService;

    /**
     * ajax删除FinanceKnowledge
     */
    @ResponseBody
    @RequestMapping("/delete")
    public Object delete(@RequestParam Long id) {
        BaseResult result = new BaseResult();
        try {
            Assert.isTrue(getCurrentUser() != null);
            financeKnowledgeService.deleteByPrimaryKey(id);
        } catch (Exception e) {
            logger.error("error", e);
            result.setCode("1");
        }
        return result;
    }

    @ResponseBody
    @RequestMapping("/detail")
    public Object detail(@RequestParam Long id) {
        try {
            FinanceKnowledge financeKnowledge = financeKnowledgeService.selectByPrimaryKey(id);
            return success(financeKnowledge);
        } catch (Exception e) {
            logger.error("",e);
            return error();
        }
    }

    /**
     * ajax更新FinanceKnowledge
     */
    @ResponseBody
    @PostMapping("/save")
    @RepeatSubmit
    public Object save(FinanceKnowledge financeKnowledge) {
        BaseResult result = new BaseResult();
        try {
            FqUserCache fqUserCache = getCurrentUser();
            if (fqUserCache == null) {
                result.setResult(ResultEnum.USER_NOT_LOGIN);
                return result;
            }
            if (financeKnowledge.getId() == null) {
                financeKnowledge.setGmtCreate(new Date());
                financeKnowledgeService.insert(financeKnowledge);
            } else {
                financeKnowledgeService.updateByPrimaryKeySelective(financeKnowledge);
            }
        } catch (Exception e) {
            logger.error("error", e);
            result.setCode("1");
        }
        return result;
    }


    /**
     * 查询FinanceKnowledge首页
     */
    @GetMapping("list")
    @ResponseBody
    public Object list(@RequestParam(defaultValue = "0") Integer page,
                       @RequestParam(defaultValue = "10") Integer limit) {
        BaseResult result = new BaseResult();
        try {
            PageHelper.startPage(page, limit);
            FinanceKnowledgeExample example = new FinanceKnowledgeExample();
            example.setOrderByClause("gmt_create desc");
            List<FinanceKnowledge> list = financeKnowledgeService.selectByExample(example);
            PageInfo pageInfo = new PageInfo(list);
            result.setData(pageInfo);
        } catch (Exception e) {
            logger.error("error", e);
            result.setCode("1");
        }
        return result;
    }
}