package com.feiqu.web.controller.resource;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.feiqu.common.base.BaseResult;
import com.feiqu.common.constant.BizConstant;
import com.feiqu.common.enums.ResultEnum;
import com.feiqu.framwork.constant.CommonConstant;
import com.feiqu.framwork.support.store.AliyunOssService;
import com.feiqu.framwork.web.base.BaseController;
import com.feiqu.system.pojo.cache.FqUserCache;
import com.feiqu.system.pojo.response.FileItemDTO;
import com.feiqu.system.util.FileComparator;
import com.jeesuite.filesystem.FileSystemClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 云存储服务
 */
@Controller
@RequestMapping("cloud")
public class CloudController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(CloudController.class);

    private String baseDir = "cloud";
    @Resource
    private AliyunOssService aliyunOssService;

    @GetMapping("")
    public String index(Model model,String q,String parentDir){
        FqUserCache fqUserCache = getCurrentUser();
        if(fqUserCache == null){
            return loginRedirect("/cloud");
        }
        if(fqUserCache.getLevel() == null || fqUserCache.getLevel() < 3){
            addErrorMsg(model,"暂时只支持3级以上用户访问");
            return GENERAL_CUSTOM_ERROR_URL;
        }
        String userDir = getUserDir(fqUserCache.getId());
        if(StrUtil.isNotBlank(parentDir)){
            userDir = userDir+parentDir+StrUtil.SLASH;
        }
        List<FileItemDTO> fileItemDTOS =  aliyunOssService.fileList(userDir);
        if(StrUtil.isNotEmpty(q)){
            fileItemDTOS = fileItemDTOS.stream().filter(fileItemDTO -> StrUtil.containsIgnoreCase(fileItemDTO.getName(), q)).collect(Collectors.toList());
        }
        fileItemDTOS.sort(new FileComparator());
        model.addAttribute("fileItems",fileItemDTOS);
        model.addAttribute("parentDir",parentDir);
        return "/front/cloud/cloud";
    }

    private String getUserDir(int id){
//        return baseDir+ StrUtil.SLASH+"user"+id+StrUtil.SLASH;
        return baseDir+ StrUtil.SLASH;
    }

    @PostMapping("delFile")
    @ResponseBody
    public Object delFile(String filename,String parentDir){

        FqUserCache fqUserCache = getCurrentUser();
        if(fqUserCache == null){
            return error("用户未登陆，不能删除");
        }

        if(StrUtil.isEmpty(parentDir)){
            filename = getUserDir(fqUserCache.getId())+filename;
        }else {
            filename = getUserDir(fqUserCache.getId())+parentDir+StrUtil.SLASH+filename;
        }
        aliyunOssService.delFile(CommonConstant.ALIYUN_OSS_BUCKET_NAME,filename);
        return success();
    }

    @PostMapping("/upload")
    @ResponseBody
    public Object upload(HttpServletRequest request, MultipartFile file,@RequestParam Boolean isOrigin,String parentDir){
        BaseResult result = new BaseResult();
        String fileName = null;
        try {
            FqUserCache fqUser = getCurrentUser();
            if(fqUser == null){
                result.setResult(ResultEnum.USER_NOT_LOGIN);
                return result;
            }
            long size = file.getSize();
            if(size >  20 * 1024 * 1024){
                result.setResult(ResultEnum.FILE_TOO_LARGE);
                result.setMessage("上传文件大小不要超过20M");
                return result;
            }
            String extName = FileUtil.extName(file.getOriginalFilename());
            if(isOrigin){
                fileName = file.getOriginalFilename();
            }else {
                fileName = BizConstant.FILE_NAME_PREFIX + DateUtil.format(new Date(), "yyyyMMddHHmmss") + "." + extName;
            }

            String userDir = getUserDir(fqUser.getId());
            //一级目录
            if(StrUtil.isNotBlank(parentDir)){
                userDir = userDir+parentDir+StrUtil.SLASH;
            }
            String url = FileSystemClient.getClient(BizConstant.FILE_SYSTEM_ALIYUN).upload(userDir + fileName, file.getInputStream(), extName);
            return success(url);
        } catch (Exception e) {
            logger.error("上传至云盘失败",e);
            result.setResult(ResultEnum.SYSTEM_ERROR);
        }
        return result;
    }
}
