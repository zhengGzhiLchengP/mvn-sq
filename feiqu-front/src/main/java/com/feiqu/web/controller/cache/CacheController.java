package com.feiqu.web.controller.cache;

import com.feiqu.common.constant.BizConstant;
import com.feiqu.framwork.constant.CommonConstant;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("cache")
public class CacheController {

    @GetMapping("refresh")
    @ResponseBody
    public Object refresh(String cacheName,String cacheValue){
        switch (cacheName){
            case "USER_IP_VISIT_MAX_COUNT":
                BizConstant.USER_IP_VISIT_MAX_COUNT = Integer.valueOf(cacheValue);
                break;
            case "DEAULT_PAGE_SIZE":
                BizConstant.DEAULT_PAGE_SIZE = Integer.valueOf(cacheValue);
                break;
            case "WS_SEND_MAX_COUNT":
                BizConstant.WS_SEND_MAX_COUNT = Integer.valueOf(cacheValue);
                break;
            case "INDEX_IMG_RANDOM_NUM":
                Integer num = Integer.valueOf(cacheValue);
                if(num > 20){
                    num = 20;
                }
                CommonConstant.INDEX_IMG_RANDOM_NUM = num;
                break;
        }
        return true;
    }
}
