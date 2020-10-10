package com.feiqu.admin.controller.tool;

import com.alibaba.fastjson.JSONObject;
import com.feiqu.adminFramework.util.AliyunOssService;
import com.feiqu.adminFramework.web.base.BaseController;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * Created by ZhangShuzheng on 2017/5/15.
 */
@RestController
@RequestMapping("/aliyun/oss")
public class AliyunOssController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AliyunOssController.class);

    @Resource
    private AliyunOssService aliyunOssService;


    /**
     * 签名生成
     *
     * @param callback 跨域请求
     * @return jsonp
     */
    @GetMapping("/policy")
    @CrossOrigin(origins = "*", methods = RequestMethod.GET) // 该注解不支持JDK1.7
    public Object policy(@RequestParam(required = false) String callback) {
        LOGGER.info("AliyunOssController-policy:" + callback);
        JSONObject result = aliyunOssService.policy();
        if (StringUtils.isBlank(callback)) {
            return success(result);
        }
        MappingJacksonValue jsonp = new MappingJacksonValue(result);
        jsonp.setValue(callback);
        return success(jsonp);
    }


}
