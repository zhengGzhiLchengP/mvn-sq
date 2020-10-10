package com.feiqu.admin.controller.tool;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.extra.servlet.ServletUtil;
import com.feiqu.common.annotation.Log;
import com.feiqu.common.base.BaseResult;
import com.feiqu.common.constant.BizConstant;
import com.feiqu.common.enums.BusinessType;
import com.feiqu.common.enums.ResultEnum;
import com.feiqu.system.model.UploadImgRecord;
import com.feiqu.system.model.UploadImgRecordExample;
import com.feiqu.system.service.mainData.UploadImgRecordService;
import com.jeesuite.filesystem.FileSystemClient;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.Date;

@RestController
@RequestMapping("/file")
public class FileController {
    private static final Logger logger = LoggerFactory.getLogger(FileController.class);
    @Resource
    private UploadImgRecordService uploadImgRecordService;

    /**
     * 上传普通的文件 图片 文本之类
     * @param request
     * @param file
     * @param picNum 已经上传的图片数量
     * @return
     */
    @PostMapping("/upload")
    @Log(title = "文件上传", businessType = BusinessType.INSERT)
    public Object upload(HttpServletRequest request, MultipartFile file){
        BaseResult result = new BaseResult();
        java.io.File localFile = null;
        String fileName = "";
        File toFile = null;
        try {
            long size = file.getSize();
            if(size >  10000 * 1024){
                result.setResult(ResultEnum.FILE_TOO_LARGE);
                result.setMessage("上传文件大小不要超过10M");
                return result;
            }
            String picUrl = "";
            String picMd5 = DigestUtil.md5Hex(file.getInputStream());
            //根据md5查数据库有没有
            UploadImgRecordExample imgRecordExample = new UploadImgRecordExample();
            imgRecordExample.createCriteria().andPicMd5EqualTo(picMd5).andPicSizeEqualTo(size);
            UploadImgRecord uploadImgRecord = uploadImgRecordService.selectFirstByExample(imgRecordExample);
            if(uploadImgRecord != null && StringUtils.isNotBlank(uploadImgRecord.getPicUrl())){
                picUrl = uploadImgRecord.getPicUrl();
            }else {
                Date now = new Date();
                String extName = FileUtil.extName(file.getOriginalFilename()).toLowerCase();
                String format = DateUtil.format(now,"yyyyMMddHHmmss");
                String fileNameFormat =format + RandomUtil.randomNumbers(2);
                fileName = BizConstant.FILE_NAME_PREFIX + fileNameFormat + "." + extName;
                //只要大于300kb 就给它压缩 哼 算了 不压缩了吧
                picUrl = FileSystemClient.getPublicClient().upload(fileName, file.getInputStream(),extName);
                uploadImgRecord = new UploadImgRecord(picUrl,picMd5, now, ServletUtil.getClientIP(request),22, size);
                uploadImgRecordService.insert(uploadImgRecord);
            }
            result.setResult(ResultEnum.SUCCESS);
            result.setData(picUrl);
        } catch (Exception e) {
            logger.error("上传图片失败",e);
            result.setResult(ResultEnum.SYSTEM_ERROR);
        }finally {
            if (localFile != null) {
                boolean delete = localFile.delete();
                if(!delete){
                    logger.error("删除本地文件失败,文件名：{}",fileName);
                }
            }
            if (toFile != null) {
                boolean delete = toFile.delete();
                if(!delete){
                    logger.error("删除本地文件失败,文件名：{}",fileName);
                }
            }
        }
        return result;
    }
}
