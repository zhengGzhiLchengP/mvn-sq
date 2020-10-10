package com.feiqu.adminFramework.util;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.feiqu.common.constant.BizConstant;
import com.jeesuite.filesystem.FileSystemClient;
import com.jeesuite.filesystem.UploadTokenParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

/**
 * Created by ZhangShuzheng on 2017/5/15.
 */
@Service
public class AliyunOssService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AliyunOssService.class);

    @Value("${aliyun.filesystem.urlprefix}")
    private String urlprefix;

    /**
     * 签名生成
     *
     * @return
     */
    public JSONObject policy() {

        FileSystemClient fileSystemClient = FileSystemClient.getClient("aliyun");
        UploadTokenParam uploadTokenParam = new UploadTokenParam();
        // 存储目录
        String dir = DateUtil.format(new Date(), "yyyy/MM/dd");
        uploadTokenParam.setUploadDir(dir);
        // 签名有效期
        long expireEndTime = BizConstant.ALIYUN_OSS_EXPIRE;
        uploadTokenParam.setExpires(expireEndTime);
        uploadTokenParam.setFsizeMax(BizConstant.ALIYUN_OSS_MAX_SIZE * 1024 * 1024);
        Map<String, Object> map = fileSystemClient.createUploadToken(uploadTokenParam);
// 提交节点
        map.put("action", urlprefix);
        JSONObject result = new JSONObject(map);
        return result;
    }


}
