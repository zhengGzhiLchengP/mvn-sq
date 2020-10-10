package com.feiqu.admin.controller.deploy;


import com.feiqu.adminFramework.web.base.BaseController;
import com.feiqu.common.base.BaseResult;
import com.feiqu.system.model.deployData.SshModel;
import com.feiqu.system.model.deployData.SshModelExample;
import com.feiqu.system.service.deployData.SshModelService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;


/**
 * SshModelcontroller
 * Created by cwd on 2020/5/26.
 */
@Controller
@RequestMapping("/deploy/sshModel")
public class SshModelController extends BaseController {

    private static Logger logger = LoggerFactory.getLogger(SshModelController.class);

    @Resource
    private SshModelService sshModelService;

    /**
     * 跳转到SshModel首页
     */
    @RequiresPermissions("deploy:sshModel:view")
    @RequestMapping("")
    public String index() {
        return "/deploy/sshModel";
    }

    /**
     * 添加SshModel页面
     */
    @RequestMapping("/sshModel_add")
    public String sshModel_add() {
        return "/sshModel/add";
    }

    /**
     * ajax删除SshModel
     */
    @ResponseBody
    @RequestMapping("/delete")
    public Object delete(@RequestParam Integer id) {
        BaseResult result = new BaseResult();
        try {
            sshModelService.deleteByPrimaryKey(id);
        } catch (Exception e) {
            logger.error("error", e);
            result.setCode("1");
        }
        return result;
    }

    /**
     * 更新SshModel页面
     */
    @RequestMapping("/edit/{sshModelId}")
    public Object sshModelEdit(@PathVariable Long sshModelId, Model model) {
        SshModel sshModel = sshModelService.selectByPrimaryKey(sshModelId);
        model.addAttribute("sshModel", sshModel);
        return "/sshModel/edit";
    }

    /**
     * ajax更新SshModel
     */
    @ResponseBody
    @PostMapping("/save")
    public Object save(SshModel sshModel) {
        BaseResult result = new BaseResult();
        try {
            if (sshModel.getId() == null) {
                sshModelService.insert(sshModel);
            } else {
                sshModelService.updateByPrimaryKeySelective(sshModel);
            }
        } catch (Exception e) {
            logger.error("error", e);
            result.setCode("1");
        }
        return result;
    }


    /**
     * 查询SshModel首页
     */
    @GetMapping("list")
    @ResponseBody
    public Object list(@RequestParam(defaultValue = "0") Integer page,
                       @RequestParam(defaultValue = "10") Integer limit) {
        BaseResult result = new BaseResult();
        try {
            PageHelper.startPage(page, limit);
            SshModelExample example = new SshModelExample();
            example.setOrderByClause("id desc");
            List<SshModel> list = sshModelService.selectByExample(example);
            PageInfo pageInfo = new PageInfo(list);
            return success(pageInfo);
        } catch (Exception e) {
            logger.error("error", e);
           return error("查询失败");
        }
    }
}