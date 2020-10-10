package com.feiqu.web.controller.user;


import com.feiqu.common.base.BaseResult;
import com.feiqu.common.enums.ResultEnum;
import com.feiqu.framwork.web.base.BaseController;
import com.feiqu.system.model.FqUserAuth;
import com.feiqu.system.model.FqUserAuthExample;
import com.feiqu.system.pojo.cache.FqUserCache;
import com.feiqu.system.service.mainData.FqUserAuthService;
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
 * FqUserAuthcontroller
 * Created by cwd on 2019/3/10.
 */
@Controller
@RequestMapping("/fqUserAuth")
public class FqUserAuthController extends BaseController {

    private static Logger logger = LoggerFactory.getLogger(FqUserAuthController.class);

    @Resource
    private FqUserAuthService fqUserAuthService;

    /**
     * 跳转到FqUserAuth首页
     */
    @RequestMapping("manage")
    public String manage() {
        return "/fqUserAuth/manage";
    }

    /**
     * 跳转到FqUserAuth首页
     */
    @RequestMapping("")
    public String index() {
        return "/fqUserAuth/index";
    }

    /**
     * 添加FqUserAuth页面
     */
    @RequestMapping("/fqUserAuth_add")
    public String fqUserAuth_add() {
        return "/fqUserAuth/add";
    }

    /**
     * ajax删除FqUserAuth
     */
    @ResponseBody
    @RequestMapping("/delete")
    public Object delete(@RequestParam Integer id) {
        BaseResult result = new BaseResult();
        try {
            fqUserAuthService.deleteByPrimaryKey(id);
        } catch (Exception e) {
            logger.error("error", e);
            result.setCode("1");
        }
        return result;
    }

    /**
     * 更新FqUserAuth页面
     */
    @RequestMapping("/edit/{fqUserAuthId}")
    public Object fqUserAuthEdit(@PathVariable Integer fqUserAuthId, Model model) {
        FqUserAuth fqUserAuth = fqUserAuthService.selectByPrimaryKey(fqUserAuthId);
        model.addAttribute("fqUserAuth", fqUserAuth);
        return "/fqUserAuth/edit";
    }

    /**
     * ajax更新FqUserAuth
     */
    @ResponseBody
    @PostMapping("/save")
    public Object save(FqUserAuth fqUserAuth) {
        BaseResult result = new BaseResult();
        try {
            FqUserCache fqUserCache = getCurrentUser();
            if (fqUserCache == null) {
                result.setResult(ResultEnum.USER_NOT_LOGIN);
                return result;
            }
            if (fqUserAuth.getId() == null) {
                fqUserAuthService.insert(fqUserAuth);
            } else {
                fqUserAuthService.updateByPrimaryKeySelective(fqUserAuth);
            }
        } catch (Exception e) {
            logger.error("error", e);
            result.setCode("1");
        }
        return result;
    }


    /**
     * 查询FqUserAuth首页
     */
    @GetMapping("list")
    @ResponseBody
    public Object list(@RequestParam(defaultValue = "0") Integer index,
                       @RequestParam(defaultValue = "10") Integer size) {
        BaseResult result = new BaseResult();
        try {
            PageHelper.startPage(index, size);
            FqUserAuthExample example = new FqUserAuthExample();
            example.setOrderByClause("create_time desc");
            List<FqUserAuth> list = fqUserAuthService.selectByExample(example);
            PageInfo page = new PageInfo(list);
            result.setData(page);
        } catch (Exception e) {
            logger.error("error", e);
            result.setCode("1");
        }
        return result;
    }
}