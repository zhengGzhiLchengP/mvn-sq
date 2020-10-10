package com.feiqu.adminFramework.manager.factory;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.extra.servlet.ServletUtil;
import cn.hutool.http.useragent.UserAgent;
import cn.hutool.http.useragent.UserAgentUtil;
import com.feiqu.adminFramework.shiro.session.OnlineSession;
import com.feiqu.adminFramework.util.CommonUtils;
import com.feiqu.adminFramework.util.LogUtils;
import com.feiqu.adminFramework.util.ShiroUtils;
import com.feiqu.common.constant.BizConstant;
import com.feiqu.common.enums.DataSourceType;
import com.feiqu.common.utils.ServletUtils;
import com.feiqu.common.utils.SpringUtils;
import com.feiqu.framwork.datasource.DynamicDataSourceContextHolder;
import com.feiqu.system.model.sysData.SysLogininfor;
import com.feiqu.system.model.sysData.SysOperLog;
import com.feiqu.system.model.sysData.SysUserOnline;
import com.feiqu.system.service.sysData.SysLogininforService;
import com.feiqu.system.service.sysData.SysOperLogService;
import com.feiqu.system.service.sysData.SysUserOnlineService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.TimerTask;

/**
 * 异步工厂（产生任务用）
 * 
 * @author liuhulu
 *
 */
public class AsyncFactory
{
    private static final Logger sys_user_logger = LoggerFactory.getLogger("sys-user");

    /**
     * 同步session到数据库
     * 
     * @param session 在线用户会话
     * @return 任务task
     */
    public static TimerTask syncSessionToDb(final OnlineSession session)
    {
        return new TimerTask()
        {
            @Override
            public void run()
            {
                SysUserOnlineService sysUserOnlineService = SpringUtils.getBean(SysUserOnlineService.class);
                SysUserOnline online = new SysUserOnline();
                online.setSessionid(String.valueOf(session.getId()));
                online.setDeptName(session.getDeptName());
                online.setLoginName(session.getLoginName());
                online.setStartTimestamp(session.getStartTimestamp());
                online.setLastAccessTime(session.getLastAccessTime());
                online.setExpireTime((int) session.getTimeout());
                online.setIpaddr(session.getHost());
                online.setLoginLocation(CommonUtils.getFullRegionByIp(session.getHost()));
                online.setBrowser(session.getBrowser());
                online.setOs(session.getOs());
                online.setStatus(String.valueOf(session.getStatus()));
                DynamicDataSourceContextHolder.setDataSoureType(DataSourceType.FEIQU_SYS_DATA.getSchemaName());
                sysUserOnlineService.saveOnline(online);

            }
        };
    }

    /**
     * 操作日志记录
     * 
     * @param operLog 操作日志信息
     * @return 任务task
     */
    public static TimerTask recordOper(final SysOperLog operLog)
    {
        return new TimerTask()
        {
            @Override
            public void run()
            {
                // 远程查询操作地点
                operLog.setOperLocation(CommonUtils.getFullRegionByIp(operLog.getOperIp()));
                DynamicDataSourceContextHolder.setDataSoureType(DataSourceType.FEIQU_SYS_DATA.getSchemaName());
                SpringUtils.getBean(SysOperLogService.class).insert(operLog);
            }
        };
    }

    /**
     * 记录登陆信息
     * 
     * @param username 用户名
     * @param status 状态
     * @param message 消息
     * @param args 列表
     * @return 任务task
     */
    public static TimerTask recordLogininfor(final String username, final String status, final String message, final Object... args)
    {
        final UserAgent userAgent = UserAgentUtil.parse(ServletUtil.getHeader(ServletUtils.getRequest(),"User-Agent", CharsetUtil.CHARSET_UTF_8.name()));
        final String ip = ShiroUtils.getIp();
        return new TimerTask()
        {
            @Override
            public void run()
            {
                StringBuilder s = new StringBuilder();
                s.append(LogUtils.getBlock(ip));
                s.append(CommonUtils.getFullRegionByIp(ip));
                s.append(LogUtils.getBlock(username));
                s.append(LogUtils.getBlock(status));
                s.append(LogUtils.getBlock(message));
                // 打印信息到日志
                sys_user_logger.info(s.toString(), args);
                // 获取客户端操作系统
                String os = userAgent.getPlatform().getName();
                // 获取客户端浏览器
                String browser = userAgent.getBrowser().getName();
                // 封装对象
                SysLogininfor logininfor = new SysLogininfor();
                logininfor.setLoginName(username);
                logininfor.setIpaddr(ip);
                logininfor.setLoginLocation(CommonUtils.getFullRegionByIp(ip));
                logininfor.setBrowser(browser);
                logininfor.setOs(os);
                logininfor.setMsg(message);
                // 日志状态
                if (BizConstant.LOGIN_SUCCESS.equals(status) || BizConstant.LOGOUT.equals(status))
                {
                    logininfor.setStatus(BizConstant.SUCCESS);
                }
                else if (BizConstant.LOGIN_FAIL.equals(status))
                {
                    logininfor.setStatus(BizConstant.FAIL);
                }
                DynamicDataSourceContextHolder.setDataSoureType(DataSourceType.FEIQU_SYS_DATA.getSchemaName());
                // 插入数据
                SpringUtils.getBean(SysLogininforService.class).insert(logininfor);
            }
        };
    }
}
