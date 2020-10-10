package com.feiqu.framwork.support.cache;

import com.alibaba.fastjson.JSON;
import com.feiqu.common.utils.SpringUtils;
import com.feiqu.framwork.constant.CommonConstant;
import com.feiqu.system.model.FqUser;
import com.feiqu.system.pojo.cache.FqUserCache;
import com.feiqu.system.service.mainData.FqUserService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2017/11/11.
 */
public class CacheManager {

    private CacheManager(){

    }

    private static StringRedisTemplate getRedis(){
        return SpringUtils.getBean(StringRedisTemplate.class);
    }

    private static Logger logger = LoggerFactory.getLogger(CacheManager.class);

    public static void removeUserCacheByUid(Integer userId){
        try {
            String cacheKey = "FqUser.id:"+userId;
            getRedis().delete(cacheKey);
        } catch (Exception e) {
            logger.error("",e);
        }
    }

    public static FqUserCache getUserCacheByUid(Integer userId){
        FqUserCache fqUserCache = null;
        try {
            String cacheKey = "FqUser.id:"+userId;
            String userCacheStr = getRedis().opsForValue().get(cacheKey);
            if(StringUtils.isEmpty(userCacheStr)){
                FqUserService fqUserService = SpringUtils.getBean(FqUserService.class);
                FqUser fqUser = fqUserService.selectByPrimaryKey(userId);
                fqUserCache = new FqUserCache(fqUser);
                getRedis().opsForValue().set(cacheKey,JSON.toJSONString(fqUserCache));
                getRedis().expire(cacheKey,5, TimeUnit.MINUTES);
                return fqUserCache;
            }else {
                fqUserCache = JSON.parseObject(userCacheStr,FqUserCache.class);
                return fqUserCache;
            }
        } catch (Exception e) {
            logger.error("获取用户信息出错",e);
        }
        return fqUserCache;
    }

    public static void refreshUserCacheByUid(Integer userId){
        String cacheKey = "FqUser.id:"+userId;
        try {
            FqUserService fqUserService = SpringUtils.getBean(FqUserService.class);
            FqUser fqUser = fqUserService.selectByPrimaryKey(userId);
            FqUserCache fqUserCache = new FqUserCache(fqUser);
            getRedis().opsForValue().set(cacheKey,JSON.toJSONString(fqUserCache));
            getRedis().expire(cacheKey,5, TimeUnit.MINUTES);
        } catch (Exception e) {
            logger.error("refreshUserCacheByUid",e);
        }
    }

    public static void refreshUserCacheByUser(FqUserCache user) {
        refreshUserCacheByUid(user.getId());
    }


    public static void addCollect(String type,Integer uid, Integer thoughtId) {
        try {
            String key = getCollectKey(type,uid);
            getRedis().opsForSet().add(key,thoughtId.toString());
            getRedis().expire(key,1,TimeUnit.DAYS);
        } catch (Exception e) {
            logger.error("refreshUserCacheByUid",e);
        }
    }

    public static void removeCollect(String type,Integer uid, Integer thoughtId) {
        try {
            String key = getCollectKey(type,uid);
            getRedis().opsForSet().remove(key,thoughtId.toString());
        } catch (Exception e) {
            logger.error("refreshUserCacheByUid",e);
        }
    }

    public static void refreshCollect(String type,Integer uid,List<Integer> list) {
        try {
            String key = getCollectKey(type,uid);
            for(Integer tid : list){
                getRedis().opsForSet().add(key, tid.toString());
            }
            getRedis().expire(key,24,TimeUnit.DAYS);
        } catch (Exception e) {
            logger.error("refreshUserCacheByUid",e);
        }
    }

    public static String getCollectKey(String type,Integer uid){
        return type.concat(":").concat(uid.toString()).concat(":collected");
    }

    public static String getUserBackImgKey(Integer uid){
        return  "fq:bgImg:"+uid;
    }


    public static void refreshWebsiteCache() {
        try {
            getRedis().delete(CommonConstant.FQ_WEBSITE_ALL);
        } catch (Exception e) {
            logger.error("refreshUserCacheByUid",e);
        }
    }

    public static String getCaptchaCode(String ip) {
        return getRedis().opsForValue().get(getCaptchaKey(ip));
    }

    public static String getCaptchaKey(String ip) {
        return "captcha:"+ip;
    }

    public static void removeCaptchaCode(String ip) {
        getRedis().delete(getCaptchaKey(ip));
    }

    public static String getWsKey(String ip) {
        return "ws:token:"+ip;
    }


}
