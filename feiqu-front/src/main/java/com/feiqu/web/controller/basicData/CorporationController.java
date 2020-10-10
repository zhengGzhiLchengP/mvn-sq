package com.feiqu.web.controller.basicData;


import com.feiqu.common.base.BaseResult;
import com.feiqu.common.enums.ResultEnum;
import com.feiqu.framwork.web.base.BaseController;
import com.feiqu.system.model.basicData.Corporation;
import com.feiqu.system.model.basicData.CorporationExample;
import com.feiqu.system.pojo.cache.FqUserCache;
import com.feiqu.system.service.basicData.CorporationService;
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
 * Corporationcontroller
 * Created by cwd on 2019/6/27.
 */
@Controller
@RequestMapping("/corporation")
public class CorporationController extends BaseController {

    private static Logger logger = LoggerFactory.getLogger(CorporationController.class);

    @Resource
    private CorporationService corporationService;


    /**
     * 跳转到Corporation管理页面
     */
    @RequestMapping("manage")
    public String manage() {
        return "/corporation/manage";
    }

    /**
     * 跳转到Corporation首页
     */
    @RequestMapping("")
    public String index() {
        return "/corporation/index";
    }

    /**
     * 添加Corporation页面
     */
    @RequestMapping("/corporation_add")
    public String corporation_add() {
        return "/corporation/add";
    }

    /**
     * ajax删除Corporation
     */
    @ResponseBody
    @RequestMapping("/delete")
    public Object delete(@RequestParam Long id) {
        BaseResult result = new BaseResult();
        try {
            corporationService.deleteByPrimaryKey(id);
        } catch (Exception e) {
            logger.error("error", e);
            result.setCode("1");
        }
        return result;
    }

    /**
     * 更新Corporation页面
     */
    @RequestMapping("/edit/{corporationId}")
    public Object corporationEdit(@PathVariable Long corporationId, Model model) {
        Corporation corporation = corporationService.selectByPrimaryKey(corporationId);
        model.addAttribute("corporation", corporation);
        return "/corporation/edit";
    }

    /**
     * ajax更新Corporation
     */
    @ResponseBody
    @PostMapping("/save")
    public Object save(Corporation corporation) {
        BaseResult result = new BaseResult();
        try {
            FqUserCache fqUserCache = getCurrentUser();
            if (fqUserCache == null) {
                result.setResult(ResultEnum.USER_NOT_LOGIN);
                return result;
            }
            if (corporation.getId() == null) {
                corporationService.insert(corporation);
            } else {
                corporationService.updateByPrimaryKeySelective(corporation);
            }
        } catch (Exception e) {
            logger.error("error", e);
            result.setCode("1");
        }
        return result;
    }


    /**
     * 查询Corporation首页
     */
    @GetMapping("list")
    @ResponseBody
    public Object list(@RequestParam(defaultValue = "0") Integer page,
                       @RequestParam(defaultValue = "10") Integer limit, Corporation corporation) {
        BaseResult result = new BaseResult();
        try {
            PageHelper.startPage(page, limit);
            CorporationExample example = new CorporationExample();
            CorporationExample.Criteria criteria = example.createCriteria();
            if (StringUtils.isNotEmpty(corporation.getCorporationName())) {
                criteria.andCorporationNameLike("%" + corporation.getCorporationName() + "%");
            }
            example.setOrderByClause("id asc");
            List<Corporation> list = corporationService.selectByExample(example);
            PageInfo pageInfo = new PageInfo(list);
            result.setData(pageInfo);
        } catch (Exception e) {
            logger.error("error", e);
            result.setCode("1");
        }
        return result;
    }
}