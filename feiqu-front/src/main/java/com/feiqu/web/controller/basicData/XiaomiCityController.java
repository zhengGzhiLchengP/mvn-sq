package com.feiqu.web.controller.basicData;


import com.feiqu.common.base.BaseResult;
import com.feiqu.common.enums.ResultEnum;
import com.feiqu.framwork.web.base.BaseController;
import com.feiqu.system.model.basicData.XiaomiCity;
import com.feiqu.system.model.basicData.XiaomiCityExample;
import com.feiqu.system.pojo.cache.FqUserCache;
import com.feiqu.system.service.basicData.XiaomiCityService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;


/**
 * XiaomiCitycontroller
 * Created by cwd on 2019/3/29.
 */
@Controller
@RequestMapping("/xiaomiCity")
public class XiaomiCityController extends BaseController {

    private static Logger logger = LoggerFactory.getLogger(XiaomiCityController.class);

    @Resource
    private XiaomiCityService xiaomiCityService;


    /**
     * 跳转到XiaomiCity管理页面
     */
    @RequestMapping("manage")
    public String manage() {
        return "/xiaomiCity/manage";
    }

    /**
     * 跳转到XiaomiCity首页
     */
    @RequestMapping("")
    public String index() {
        return "/xiaomiCity/index";
    }

    /**
     * 添加XiaomiCity页面
     */
    @RequestMapping("/xiaomiCity_add")
    public String xiaomiCity_add() {
        return "/xiaomiCity/add";
    }

    /**
     * ajax删除XiaomiCity
     */
    @ResponseBody
    @RequestMapping("/delete")
    public Object delete(@RequestParam Long id) {
        BaseResult result = new BaseResult();
        try {
            xiaomiCityService.deleteByPrimaryKey(id);
        } catch (Exception e) {
            logger.error("error", e);
            result.setCode("1");
        }
        return result;
    }

    /**
     * 更新XiaomiCity页面
     */
    @RequestMapping("/edit/{xiaomiCityId}")
    public Object xiaomiCityEdit(@PathVariable Long xiaomiCityId, Model model) {
        XiaomiCity xiaomiCity = xiaomiCityService.selectByPrimaryKey(xiaomiCityId);
        model.addAttribute("xiaomiCity", xiaomiCity);
        return "/xiaomiCity/edit";
    }

    /**
     * ajax更新XiaomiCity
     */
    @ResponseBody
    @PostMapping("/save")
    public Object save(XiaomiCity xiaomiCity) {
        BaseResult result = new BaseResult();
        try {
            FqUserCache fqUserCache = getCurrentUser();
            if (fqUserCache == null) {
                result.setResult(ResultEnum.USER_NOT_LOGIN);
                return result;
            }
            if (xiaomiCity.getId() == null) {
                xiaomiCityService.insert(xiaomiCity);
            } else {
                xiaomiCityService.updateByPrimaryKeySelective(xiaomiCity);
            }
        } catch (Exception e) {
            logger.error("error", e);
            result.setCode("1");
        }
        return result;
    }


    /**
     * 查询XiaomiCity首页
     */
    @GetMapping("list")
    @ResponseBody
    public Object list(@RequestParam(defaultValue = "0") Integer index,
                       @RequestParam(defaultValue = "10") Integer size) {
        BaseResult result = new BaseResult();
        try {
            PageHelper.startPage(index, size);
            XiaomiCityExample example = new XiaomiCityExample();
            example.setOrderByClause("gmt_create desc");
            List<XiaomiCity> list = xiaomiCityService.selectByExample(example);
            PageInfo page = new PageInfo(list);
            result.setData(page);
        } catch (Exception e) {
            logger.error("error", e);
            result.setCode("1");
        }
        return result;
    }
}