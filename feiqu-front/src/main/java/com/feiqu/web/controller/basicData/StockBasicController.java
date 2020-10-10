package com.feiqu.web.controller.basicData;


import com.feiqu.common.base.BaseResult;
import com.feiqu.common.enums.ResultEnum;
import com.feiqu.framwork.web.base.BaseController;
import com.feiqu.system.model.basicData.StockBasic;
import com.feiqu.system.model.basicData.StockBasicExample;
import com.feiqu.system.pojo.cache.FqUserCache;
import com.feiqu.system.service.basicData.StockBasicService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;


/**
 * StockBasiccontroller
 * Created by cwd on 2019/6/26.
 */
@Controller
@RequestMapping("/stockBasic")
public class StockBasicController extends BaseController {

    private static Logger logger = LoggerFactory.getLogger(StockBasicController.class);

    @Resource
    private StockBasicService stockBasicService;


    /**
     * 跳转到StockBasic管理页面
     */
    @RequestMapping("manage")
    public String manage() {
        return "/stockBasic/manage";
    }

    /**
     * 跳转到StockBasic首页
     */
    @RequestMapping("")
    public String index() {
        return "/stockBasic/index";
    }

    /**
     * 添加StockBasic页面
     */
    @RequestMapping("/stockBasic_add")
    public String stockBasic_add() {
        return "/stockBasic/add";
    }

    /**
     * ajax删除StockBasic
     */
    @ResponseBody
    @RequestMapping("/delete")
    public Object delete(@RequestParam Long id) {
        BaseResult result = new BaseResult();
        try {
            stockBasicService.deleteByPrimaryKey(id);
        } catch (Exception e) {
            logger.error("error", e);
            result.setCode("1");
        }
        return result;
    }

    /**
     * 更新StockBasic页面
     */
    @RequestMapping("/edit/{stockBasicId}")
    public Object stockBasicEdit(@PathVariable Long stockBasicId, Model model) {
        StockBasic stockBasic = stockBasicService.selectByPrimaryKey(stockBasicId);
        model.addAttribute("stockBasic", stockBasic);
        return "/stockBasic/edit";
    }

    /**
     * ajax更新StockBasic
     */
    @ResponseBody
    @PostMapping("/save")
    public Object save(StockBasic stockBasic) {
        BaseResult result = new BaseResult();
        try {
            FqUserCache fqUserCache = getCurrentUser();
            if (fqUserCache == null) {
                result.setResult(ResultEnum.USER_NOT_LOGIN);
                return result;
            }
            if (stockBasic.getId() == null) {
                stockBasicService.insert(stockBasic);
            } else {
                stockBasicService.updateByPrimaryKeySelective(stockBasic);
            }
        } catch (Exception e) {
            logger.error("error", e);
            result.setCode("1");
        }
        return result;
    }


    /**
     * 查询StockBasic首页
     */
    @GetMapping("list")
    @ResponseBody
    public Object list(@RequestParam(defaultValue = "0") Integer page,
                       @RequestParam(defaultValue = "10") Integer limit, StockBasic param) {
        BaseResult result = new BaseResult();
        try {
            PageHelper.startPage(page, limit);
            StockBasicExample example = new StockBasicExample();
            StockBasicExample.Criteria criteria = example.createCriteria();
            if (StringUtils.isNotEmpty(param.getName())) {
                criteria.andNameLike("%" + param.getName() + "%");
            }
            if (StringUtils.isNotEmpty(param.getTsCode())) {
                criteria.andTsCodeLike("%" + param.getTsCode() + "%");
            }
            example.setOrderByClause("id asc");
            List<StockBasic> list = stockBasicService.selectByExample(example);
            PageInfo pageInfo = new PageInfo(list);
            result.setData(pageInfo);
        } catch (Exception e) {
            logger.error("error", e);
            result.setCode("1");
        }
        return result;
    }
}