package com.feiqu.framwork.interceptor;

import cn.hutool.core.date.DateUtil;
import cn.hutool.extra.servlet.ServletUtil;
import com.feiqu.common.constant.BizConstant;
import com.feiqu.common.utils.IpUtils;
import com.feiqu.common.utils.ServletUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Component
public class IpControlInterceptor extends HandlerInterceptorAdapter {
    private static Logger logger = LoggerFactory.getLogger(IpControlInterceptor.class);
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String ip = ServletUtil.getClientIP(request);
        if (IpUtils.internalIp(ip)) {
            return true;
        }
        //如果不是ajax 代表是点击页面 则统计点击数量
        if (!ServletUtils.isAjaxRequest(request)) {
            try {
                Boolean isBlackIp = stringRedisTemplate.opsForSet().isMember(BizConstant.FQ_BLACK_LIST_REDIS_KEY, ip);
                if (isBlackIp != null && isBlackIp) {
                    logger.debug("该ip:{} 存在于黑名单，禁止其访问！", ip);
                    response.sendRedirect(request.getContextPath() + "/blackList/denyService");
                    return false;
                }
                String clickkey = BizConstant.FQ_IP_DATA_THIS_DAY_FORMAT_PREFIX + DateUtil.formatDate(new Date());
                Double score = stringRedisTemplate.opsForZSet().score(clickkey, ip);
                if (score == null) {
                    stringRedisTemplate.opsForZSet().add(clickkey, ip,1);
                    stringRedisTemplate.expire(clickkey, 60, TimeUnit.DAYS);//存放一个月
                } else {
                    stringRedisTemplate.opsForZSet().add(clickkey, ip,score + 1);
                }
            } catch (Exception e){
                logger.error("",e);
            }finally {
            }
        }
        return true;
    }
}
