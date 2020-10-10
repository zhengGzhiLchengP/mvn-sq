package com.feiqu.framwork.function;

import com.feiqu.common.enums.YesNoEnum;
import com.feiqu.common.utils.SpringUtils;
import com.feiqu.framwork.support.cache.CacheManager;
import com.feiqu.framwork.util.WebUtil;
import com.feiqu.system.model.FqBackgroundImg;
import com.feiqu.system.model.FqBackgroundImgExample;
import com.feiqu.system.pojo.cache.FqUserCache;
import com.feiqu.system.service.mainData.FqBackgroundImgService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.concurrent.TimeUnit;

/**
 * @author cwd
 * @create 2017-10-14:59
 **/
@Service(value = "beetl_functions")
public class Functions {

    @Resource
    WebUtil webUtil;
    @Resource
    private StringRedisTemplate stringRedisTemplate;


    /**
     * 继续encode URL (url,传参tomcat会自动解码)
     * 要作为参数传递的话，需要再次encode
     * @return String
     */
    public String encodeUrl(String url) {
        try {
            url = URLEncoder.encode(url, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            // ignore
        }
        return url;
    }

    /**
     * 模版中拿取cookie中的用户信息
     *
     * @param request
     * @param response
     * @return
     */
    public FqUserCache currentUser(HttpServletRequest request, HttpServletResponse response) {
        return webUtil.currentUser(request, response);
    }

    public String currentBgImgUrl(int uid){
        if(uid <= 0){return "";}
        String key = CacheManager.getUserBackImgKey(uid);
        String picUrl = stringRedisTemplate.opsForValue().get(key);
        if(StringUtils.isEmpty(picUrl)){
            FqBackgroundImgService fqBackgroundImgService = SpringUtils.getBean("fqBackgroundImgServiceImpl");
            FqBackgroundImgExample example = new FqBackgroundImgExample();
            example.createCriteria().andUserIdEqualTo(uid).andDelFlagEqualTo(YesNoEnum.NO.getValue());
            FqBackgroundImg fqBackgroundImg = fqBackgroundImgService.selectFirstByExample(example);
            if(fqBackgroundImg != null){
                picUrl = fqBackgroundImg.getImgUrl();
            }else {
                picUrl = "null";
            }
            stringRedisTemplate.opsForValue().set(key,picUrl);
            stringRedisTemplate.expire(key,1, TimeUnit.DAYS);
            return picUrl;
        }else {
            return picUrl;
        }
    }
}
