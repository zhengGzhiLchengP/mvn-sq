package com.feiqu.admin.controller.tool;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.feiqu.adminFramework.web.base.BaseController;
import com.feiqu.common.config.Global;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

@Controller
@RequestMapping("/tool/nginx")
public class NginxController extends BaseController {

    @RequiresPermissions("tool:nginx:view")
    @RequestMapping("")
    public String deploy(Model model){
        model.addAttribute("uploadPath", Global.getUploadPath());
        model.addAttribute("nginxLocation", Global.getConfig("feiqu.nginxLocation"));
        return "tool/nginx";
    }

    @PostMapping("/fileTree")
    @ResponseBody
    public Object fileTree(@RequestParam(defaultValue = "/root/static") String staticDic, String keyword){
        List<File> files =  FileUtil.loopFiles(staticDic, file -> {
            if(StringUtils.isEmpty(keyword)){
                return true;
            }
            return file.getPath().contains(keyword);
        });
        JSONArray jsonArray = new JSONArray();
        files.forEach(file -> {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("path",file.getPath());
//            jsonObject.put("name",file.getName());
            jsonArray.add(jsonObject);
        });

        return success(jsonArray);
    }

    @ResponseBody
    @PostMapping("/tihuanFile")
    public Object uploadFile(@RequestParam("file") MultipartFile file,String path){
        try {
            if (!file.isEmpty() && StrUtil.isNotEmpty(path)) {
                File toFile = new File(path);
                FileUtil.writeBytes(file.getBytes(),toFile);
                return success(path);
            }
            return error();
        } catch (Exception e) {
            return error(e.getMessage());
        }
    }

    /**
     *
     * @param configFileName ： nginx.conf
     * @return
     */
    @PostMapping("/readFileStr")
    @ResponseBody
    public Object nginxConf(String configFileName,String path){
        String txt="";
        File file;

            file = new File(path);
        if(file.exists()){
            txt = FileUtil.readString(file, CharsetUtil.CHARSET_UTF_8);
        }else {
        }
        return success(txt);
    }

    /**
     *
     * @param path nginx.conf
     * @param nginxConf 配置文件里面的内容
     * @return
     */
    @PostMapping("/fileStr/save")
    @ResponseBody
    public Object nginxConfSave(String path,String nginxConf){
        File file = new File(path);
        file.setWritable(true);
        FileUtil.writeBytes(nginxConf.getBytes(CharsetUtil.CHARSET_UTF_8), file);
        return success(1);
    }
}
