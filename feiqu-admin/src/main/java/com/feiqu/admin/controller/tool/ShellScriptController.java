package com.feiqu.admin.controller.tool;


import cn.hutool.core.io.FileUtil;
import com.feiqu.adminFramework.util.CommandUtil;
import com.feiqu.adminFramework.util.ShiroUtils;
import com.feiqu.adminFramework.web.base.BaseController;
import com.feiqu.system.model.basicData.ShellScript;
import com.feiqu.system.model.basicData.ShellScriptExample;
import com.feiqu.system.service.basicData.ShellScriptService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.File;
import java.util.Date;
import java.util.List;


/**
 * ShellScriptcontroller
 * Created by cwd on 2019/9/26.
 */
@Controller
@RequestMapping("/tool/shellScript")
public class ShellScriptController extends BaseController {

    private static Logger logger = LoggerFactory.getLogger(ShellScriptController.class);

    @Resource
    private ShellScriptService shellScriptService;


    /**
     * 跳转到ShellScript管理页面
     */
    @RequestMapping("")
    public String manage() {
        return "/tool/shellScript";
    }


    /**
     * ajax删除ShellScript
     */
    @ResponseBody
    @RequestMapping("/delete")
    public Object delete(@RequestParam Integer id) {
        try {
            shellScriptService.deleteByPrimaryKey(id);
        } catch (Exception e) {
            logger.error("error", e);
            return error();
        }
        return success();
    }

    /**
     * 更新ShellScript页面
     */
    @RequestMapping("/detail/{id}")
    @ResponseBody
    public Object detail(@PathVariable Integer id) {
        ShellScript shellScript = shellScriptService.selectByPrimaryKey(id);
        return success(shellScript);
    }

    /**
     * ajax更新ShellScript
     */
    @ResponseBody
    @PostMapping("/save")
    public Object save(ShellScript shellScript) {
        try {
            if (shellScript.getId() == null) {
                shellScript.setGmtCreate(new Date());
                shellScript.setOperator(ShiroUtils.getLoginName());
                shellScriptService.insert(shellScript);
            } else {
                shellScriptService.updateByPrimaryKeySelective(shellScript);
                File bashFile = new File(shellScript.getDictionary(),shellScript.getShellFileName());
                FileUtil.writeBytes(shellScript.getBash().getBytes(),bashFile);
            }
        } catch (Exception e) {
            logger.error("error", e);
            return error();
        }
        return success();
    }

    /**
     * ajax更新ShellScript
     */
    @ResponseBody
    @PostMapping("/run/{id}")
    public Object run(@PathVariable Integer id) {
        try {
            ShellScript shellScript = shellScriptService.selectByPrimaryKey(id);
            if("1".equals(shellScript.getType())){
                String msg = CommandUtil.execSystemCommand(shellScript.getBash(),new File(shellScript.getDictionary()));
                return success(msg);
            }else {
                File fileDic = new File(shellScript.getDictionary());
                if(!fileDic.exists()){
                    fileDic.mkdirs();
                }
                File bashFile = new File(shellScript.getDictionary(),shellScript.getShellFileName());
                if(!bashFile.exists()){
                    FileUtil.writeBytes(shellScript.getBash().getBytes(),bashFile);
                }
                boolean excute = bashFile.setExecutable(true);
                logger.info(""+excute);
                String msg="";
                if(CommandUtil.SUFFIX.equals("sh")){
                    msg = CommandUtil.execSystemCommand("./"+shellScript.getShellFileName(),new File(shellScript.getDictionary()));
                }else if("bat".equals(CommandUtil.SUFFIX)){
                    msg = CommandUtil.execSystemCommand("start "+shellScript.getShellFileName(),new File(shellScript.getDictionary()));
                }
                return success(msg);
            }
        } catch (Exception e) {
            logger.error("error", e);
            return error();
        }
    }


    /**
     * 查询ShellScript首页
     */
    @GetMapping("list")
    @ResponseBody
    public Object list(@RequestParam(defaultValue = "0") Integer page,
                       @RequestParam(defaultValue = "20") Integer limit) {
        try {
            PageHelper.startPage(page, limit);
            ShellScriptExample example = new ShellScriptExample();
            example.setOrderByClause("id desc");
            List<ShellScript> list = shellScriptService.selectByExample(example);
            PageInfo pageInfo = new PageInfo(list);
            return success(pageInfo);
        } catch (Exception e) {
            logger.error("error", e);
            return error();
        }
    }
}