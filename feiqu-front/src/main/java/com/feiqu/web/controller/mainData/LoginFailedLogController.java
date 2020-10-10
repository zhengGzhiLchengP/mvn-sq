package com.feiqu.web.controller.mainData;


import com.feiqu.common.base.BaseResult;
import com.feiqu.common.enums.ResultEnum;
import com.feiqu.framwork.web.base.BaseController;
import com.feiqu.system.model.mainData.LoginFailedLog;
import com.feiqu.system.model.mainData.LoginFailedLogExample;
import com.feiqu.system.pojo.cache.FqUserCache;
import com.feiqu.system.service.mainData.LoginFailedLogService;
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
 * LoginFailedLogcontroller
 * Created by cwd on 2019/5/29.
 */
@Controller
@RequestMapping("/loginFailedLog")
public class LoginFailedLogController extends BaseController {

    private static Logger logger = LoggerFactory.getLogger(LoginFailedLogController.class);

    @Resource
    private LoginFailedLogService loginFailedLogService;


    /**
     * 跳转到LoginFailedLog管理页面
     */
    @RequestMapping("manage")
    public String manage() {
        return "/loginFailedLog/manage";
    }

    /**
     * 跳转到LoginFailedLog首页
     */
    @RequestMapping("")
    public String index() {
        return "/loginFailedLog/index";
    }

    /**
     * 添加LoginFailedLog页面
     */
    @RequestMapping("/loginFailedLog_add")
    public String loginFailedLog_add() {
        return "/loginFailedLog/add";
    }

    /**
     * ajax删除LoginFailedLog
     */
    @ResponseBody
    @RequestMapping("/delete")
    public Object delete(@RequestParam Long id) {
        BaseResult result = new BaseResult();
        try {
            loginFailedLogService.deleteByPrimaryKey(id);
        } catch (Exception e) {
            logger.error("error", e);
            result.setCode("1");
        }
        return result;
    }

    /**
     * 更新LoginFailedLog页面
     */
    @RequestMapping("/edit/{loginFailedLogId}")
    public Object loginFailedLogEdit(@PathVariable Long loginFailedLogId, Model model) {
        LoginFailedLog loginFailedLog = loginFailedLogService.selectByPrimaryKey(loginFailedLogId);
        model.addAttribute("loginFailedLog", loginFailedLog);
        return "/loginFailedLog/edit";
    }

    /**
     * ajax更新LoginFailedLog
     */
    @ResponseBody
    @PostMapping("/save")
    public Object save(LoginFailedLog loginFailedLog) {
        BaseResult result = new BaseResult();
        try {
            FqUserCache fqUserCache = getCurrentUser();
            if (fqUserCache == null) {
                result.setResult(ResultEnum.USER_NOT_LOGIN);
                return result;
            }
            if (loginFailedLog.getId() == null) {
                loginFailedLogService.insert(loginFailedLog);
            } else {
                loginFailedLogService.updateByPrimaryKeySelective(loginFailedLog);
            }
        } catch (Exception e) {
            logger.error("error", e);
            result.setCode("1");
        }
        return result;
    }


    /**
     * 查询LoginFailedLog首页
     */
    @GetMapping("list")
    @ResponseBody
    public Object list(@RequestParam(defaultValue = "0") Integer index,
                       @RequestParam(defaultValue = "10") Integer size) {
        BaseResult result = new BaseResult();
        try {
            PageHelper.startPage(index, size);
            LoginFailedLogExample example = new LoginFailedLogExample();
            example.setOrderByClause("gmt_create desc");
            List<LoginFailedLog> list = loginFailedLogService.selectByExample(example);
            PageInfo page = new PageInfo(list);
            result.setData(page);
        } catch (Exception e) {
            logger.error("error", e);
            result.setCode("1");
        }
        return result;
    }
}