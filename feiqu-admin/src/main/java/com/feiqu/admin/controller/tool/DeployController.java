package com.feiqu.admin.controller.tool;

import cn.hutool.core.io.FileUtil;
import cn.hutool.http.HttpUtil;
import com.feiqu.adminFramework.util.CommandUtil;
import com.feiqu.adminFramework.util.ShiroUtils;
import com.feiqu.adminFramework.web.base.BaseController;
import com.feiqu.common.config.Global;
import com.feiqu.common.utils.FileUploadUtils;
import com.feiqu.system.model.sysData.SysUser;
import com.jeesuite.filesystem.FileSystemClient;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@Controller
@RequestMapping("/tool/deploy")
public class DeployController extends BaseController {
    private static final Logger log = LoggerFactory.getLogger(DeployController.class);

    @RequiresPermissions("tool:deploy:view")
    @RequestMapping("")
    public String deploy(Model model) {
        model.addAttribute("uploadPath", Global.getUploadPath());
        return "tool/deploy";
    }

    @RequiresPermissions("tool:ossdeploy:view")
    @RequestMapping("oss")
    public String ossdeploy(Model model) {
        model.addAttribute("uploadPath", Global.getUploadPath());
        return "tool/ossDeploy";
    }

    //command ipconfig
    @RequiresPermissions("tool:deploy:excute")
    @ResponseBody
    @PostMapping("/excute")
    public Object excute(String command, String dict) {
        if (StringUtils.isEmpty(dict)) {
            String msg = CommandUtil.execSystemCommand(command);
            return success(msg);
        } else {
            File file = new File(dict);
            String msg = CommandUtil.execSystemCommand(command, file);
            return success(msg);
        }
    }

    @RequiresPermissions("tool:deploy:uploadFile")
    @ResponseBody
    @PostMapping("/uploadFile")
    public Object uploadFile(@RequestParam("deployfile") MultipartFile file) {
        SysUser currentUser = ShiroUtils.getSysUser();
        try {
            log.info(currentUser.getLoginName() + "上传文件");
            if (!file.isEmpty()) {
                String uploadPath = FileUploadUtils.upload(Global.getUploadPath(), file, ".jar");
                return success(uploadPath);
            }
            return error();
        } catch (Exception e) {
            log.error("上传文件失败！", e);
            return error(e.getMessage());
        }
    }

    @ResponseBody
    @PostMapping("/ossDeploy")
    public Object ossDeploy(String jarUrl, String jarDestPath, String fileKey) {
        try {
            if (StringUtils.isEmpty(jarUrl) || StringUtils.isEmpty(jarDestPath)) {
                return error();
            }
            File destFile = FileUtil.file(jarDestPath);
            HttpUtil.downloadFile(jarUrl, destFile);
            boolean delete = FileSystemClient.getClient("aliyun").delete(fileKey);
            log.info("删除是否成功：{}", delete);
            return success();
        } catch (Exception e) {
            log.error("部署失败", e);
            return error("部署失败");
        }
    }
}
