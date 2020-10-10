package com.feiqu.admin.controller.deploy;


import cn.hutool.core.util.StrUtil;
import com.feiqu.adminFramework.util.SshService;
import com.feiqu.adminFramework.web.base.BaseController;
import com.feiqu.common.base.BaseResult;
import com.feiqu.system.model.deployData.DeployProject;
import com.feiqu.system.model.deployData.DeployProjectExample;
import com.feiqu.system.model.deployData.SshModel;
import com.feiqu.system.model.deployData.SshModelExample;
import com.feiqu.system.service.deployData.DeployProjectService;
import com.feiqu.system.service.deployData.SshModelService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jcraft.jsch.JSchException;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Date;
import java.util.List;


/**
 * DeployProjectcontroller
 * Created by cwd on 2020/6/2.
 */
@Controller
@RequestMapping("/deploy/deployProject")
public class DeployProjectController extends BaseController {

    private static Logger logger = LoggerFactory.getLogger(DeployProjectController.class);

    @Resource
    private DeployProjectService deployProjectService;
    @Resource
    private SshModelService sshModelService;

    @Resource
    private SshService sshService;


    @PostMapping("/run/{id}")
    @ResponseBody
    public Object run(@PathVariable Integer id) throws IOException, JSchException {
        DeployProject deployProject = deployProjectService.selectByPrimaryKey(id);
        SshModel sshModel = sshModelService.selectByPrimaryKey(deployProject.getSshId());

        String[] commands = StrUtil.split(deployProject.getCommand(), StrUtil.LF);
        StringBuilder stringBuilder = new StringBuilder();
        for (String commandItem : commands) {
            try {
                String s = sshService.exec(sshModel, commandItem.replace(StrUtil.CR,""));
                stringBuilder.append(s);
                logger.info(s);
            } catch (Exception e) {
                logger.error("",e);
            }
        }
        return success(stringBuilder.toString());
    }

    /**
     * 跳转到DeployProject首页
     */
    @RequiresPermissions("deploy:deployProject:view")
    @RequestMapping("")
    public String index() {
        return "/deploy/deployProject";
    }

    /**
     * 添加DeployProject页面
     */
    @RequestMapping("/add")
    public String deployProject_add(Model model) {
        List<SshModel> sshModels = sshModelService.selectByExample(new SshModelExample());
        model.addAttribute("sshModels",sshModels);
        return "/deploy/deployProjectAdd";
    }

    /**
     * ajax删除DeployProject
     */
    @ResponseBody
    @RequestMapping("/delete")
    public Object delete(@RequestParam Integer id) {
        BaseResult result = new BaseResult();
        try {
            deployProjectService.deleteByPrimaryKey(id);
        } catch (Exception e) {
            logger.error("error", e);
            result.setCode("1");
        }
        return result;
    }

    /**
     * 更新DeployProject页面
     */
    @RequestMapping("/edit/{deployProjectId}")
    public Object deployProjectEdit(@PathVariable Long deployProjectId, Model model) {
        DeployProject deployProject = deployProjectService.selectByPrimaryKey(deployProjectId);
        model.addAttribute("deployProject", deployProject);
        return "/deployProject/edit";
    }

    /**
     * ajax更新DeployProject
     */
    @ResponseBody
    @PostMapping("/save")
    public Object save(DeployProject deployProject) {
        BaseResult result = new BaseResult();
        try {
            deployProject.setModifyUser(getUser().getUserName());
            deployProject.setModifyTime(new Date());
            if (deployProject.getId() == null) {
                deployProjectService.insert(deployProject);
            } else {
                deployProjectService.updateByPrimaryKeySelective(deployProject);
            }
        } catch (Exception e) {
            logger.error("error", e);
            result.setCode("1");
        }
        return result;
    }


    /**
     * 查询DeployProject首页
     */
    @GetMapping("list")
    @ResponseBody
    public Object list(@RequestParam(defaultValue = "0") Integer page,
                       @RequestParam(defaultValue = "10") Integer limit) {
        BaseResult result = new BaseResult();
        try {
            PageHelper.startPage(page, limit);
            DeployProjectExample example = new DeployProjectExample();
            example.setOrderByClause("id desc");
            List<DeployProject> list = deployProjectService.selectByExample(example);
            PageInfo pageInfo = new PageInfo(list);
            return success(pageInfo);
        } catch (Exception e) {
            logger.error("error", e);
            return error("查询失败");
        }
    }
}