package com.feiqu.framwork.util;


import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.URLUtil;
import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.feiqu.common.config.Global;
import com.feiqu.common.constant.BizConstant;
import com.feiqu.common.enums.MsgEnum;
import com.feiqu.common.enums.YesNoEnum;
import com.feiqu.common.utils.SpringUtils;
import com.feiqu.framwork.constant.CommonConstant;
import com.feiqu.framwork.support.cache.CacheManager;
import com.feiqu.system.model.CMessage;
import com.feiqu.system.model.FqUser;
import com.feiqu.system.pojo.cache.FqUserCache;
import com.feiqu.system.service.mainData.CMessageService;
import com.feiqu.system.service.mainData.FqUserService;
import org.apache.commons.lang3.StringUtils;
import org.lionsoul.ip2region.DataBlock;
import org.lionsoul.ip2region.DbConfig;
import org.lionsoul.ip2region.DbMakerConfigException;
import org.lionsoul.ip2region.DbSearcher;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.io.FileNotFoundException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

/**
 * Created by chenweidong on 2018/1/16.
 */
public class CommonUtils {

    private static Log logger = LogFactory.get();

    private static DbSearcher searcher = null;
    //匹配@ 的所有的昵称
    private static Pattern aitePattern = Pattern.compile("@([\\w\\u4e00-\\u9fa5]+\\s{1})");

    static {
        try {
            String dbPath = Global.getConfig("feiqu.ip2regionDbPath");
            logger.info("ip2region db数据初始化路径：{}",dbPath);
            DbConfig config = new DbConfig();
            searcher = new DbSearcher(config, dbPath);
        } catch (DbMakerConfigException | FileNotFoundException e) {
            logger.error(e);
        }
    }

    public static String getShortThoughtContent(String thoughtContent) {
        if (thoughtContent.length() > 20) {
            return thoughtContent.substring(0, 20) + "...";
        } else {
            return thoughtContent;
        }
    }

    public static String getSalt() {
        return RandomUtil.randomString(6);
    }

    public static String encryptPassword(String password, String salt) {
        return DigestUtil.md5Hex(password+salt);
    }

    public static String getRegionByIp(String ip){
        try {
            DataBlock dataBlock = searcher.memorySearch(ip);
            String region = dataBlock.getRegion();
            logger.debug("ip:{},region:{}",ip,region);
            String[] regions = StringUtils.split(region,"|");
            if("0".equals(regions[3])){
                if(!"0".equals(regions[2])){
                    return regions[0]+regions[2];
                }
                return regions[0];
            }
            if(!regions[2].equals(regions[3])){
                return regions[2]+regions[3];
            }
            return regions[3];
        } catch (Exception e) {
            logger.error(e);
            return "未知";
        }
    }

    public static String getFullRegionByIp(String ip){
        try {
            DataBlock dataBlock = searcher.memorySearch(ip);
            return dataBlock.getRegion();
        } catch (Exception e) {
            logger.error(e);
            return "未知";
        }
    }

    public static List<String> findAiteNicknames(String content){
        return ReUtil.findAll(aitePattern,content,1);
    }

    /**
     * 获取短内容 用于消息通知，加上省略号
     * @param content
     * @return
     */
    public static String getShortContent(String content) {
        if(content.length() > 20){
            return content.substring(0,20)+"...";
        }else {
            return content;
        }
    }

    //增加活跃度
    public static void addActiveNum(Integer userId,double score){
        int month = DateUtil.thisMonth()+1;
        String key = CommonConstant.FQ_ACTIVE_USER_SORT+month;
        try {
            StringRedisTemplate stringRedisTemplate = SpringUtils.getBean(StringRedisTemplate.class);
            Double scoreStore = stringRedisTemplate.opsForZSet().score(key,userId.toString());
            if(scoreStore == null){
                scoreStore = score;
            }else {
                if(scoreStore > BizConstant.USER_MAX_ACTIVE_NUM){
                    return;
                }
                scoreStore += score;
            }
            stringRedisTemplate.opsForZSet().add(key,userId.toString(),scoreStore);
            stringRedisTemplate.expire(key,60, TimeUnit.DAYS);
        } catch (Exception e) {
            logger.error(e);
        }
    }

    public static void  addOrDelUserQudouNum(FqUserCache user, int num) {
        FqUser toUpdateUser = new FqUser();
        toUpdateUser.setId(user.getId());
        toUpdateUser.setQudouNum(user.getQudouNum() == null? 1:user.getQudouNum()+num);
        FqUserService fqUserService = SpringUtils.getBean("fqUserServiceImpl");
        fqUserService.updateByPrimaryKeySelective(toUpdateUser);
        user.setQudouNum(toUpdateUser.getQudouNum());
        CacheManager.refreshUserCacheByUser(user);
    }

    public static String genToken(Integer uid) {


        String key = getUserKey(uid);
        StringRedisTemplate stringRedisTemplate = SpringUtils.getBean(StringRedisTemplate.class);
        if(stringRedisTemplate.hasKey(key)){
            String token = stringRedisTemplate.opsForValue().get(key);
            logger.info("根据用户id： {} ，获取已经存在的token：{}",uid,token);
            return token;
        }
        String token = CommonConstant.FQ_USER_TOKEN_VAL_PREFIX + AESUtil.aesEncode(uid + "-" + RandomUtil.randomNumbers(7));
        stringRedisTemplate.opsForValue().set(key,token,1,TimeUnit.DAYS);
        logger.info("根据用户id {} 生成token{}",uid,token);
        return token;
    }

    private static String getUserKey(Integer uid) {
        String key = CommonConstant.FQ_USER_TOKEN_PREFIX + "-" + uid;
        return key;
    }
    public static Integer getUIdFromToken(String token) {
        if(StringUtils.isEmpty(token)) return 0;
        token = URLUtil.decode(token);
        String encodedStr = token.substring(CommonConstant.FQ_USER_TOKEN_VAL_PREFIX.length());
        String decodedStr = AESUtil.aesDecode(encodedStr);
        String uidStr = decodedStr.split("-")[0];
        logger.info("根据token{} 获取用户id {}",token,uidStr);
        return Integer.valueOf(uidStr);
    }

    public static void sendFriendMsg(Integer receiveUserId, Date time, String content,Integer postUserId){
        CMessage message = new CMessage();
        message.setPostUserId(postUserId);
        message.setCreateTime(time == null?new Date():time);
        message.setDelFlag(YesNoEnum.NO.getValue());
        message.setReceivedUserId(receiveUserId);
        message.setType(MsgEnum.FRIEND_MSG.getValue());
        message.setContent(content);
        CMessageService messageService = SpringUtils.getBean(CMessageService.class);
        messageService.insert(message);
    }

    public static void sendMsg(Integer type, Integer receiveUserId, Date time, String content){
        if(MsgEnum.OFFICIAL_MSG.getValue().equals(type)){
            CMessage message = new CMessage();
            message.setPostUserId(-1);
            message.setCreateTime(time == null?new Date():time);
            message.setDelFlag(YesNoEnum.NO.getValue());
            message.setReceivedUserId(receiveUserId);
            message.setType(type);
            message.setContent(content);
            CMessageService messageService = SpringUtils.getBean(CMessageService.class);
            messageService.insert(message);
        }

    }

    public static void sendOfficialMsg(Integer receiveUserId, Date time, String content){
        CMessage message = new CMessage();
        message.setPostUserId(-1);
        message.setCreateTime(time == null?new Date():time);
        message.setDelFlag(YesNoEnum.NO.getValue());
        message.setReceivedUserId(receiveUserId);
        message.setType(MsgEnum.OFFICIAL_MSG.getValue());
        message.setContent(content);
        CMessageService messageService = SpringUtils.getBean(CMessageService.class);
        messageService.insert(message);
    }

}
