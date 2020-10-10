package com.feiqu.system.service.sysData.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.feiqu.system.base.BaseServiceImpl;
import com.feiqu.system.annotation.BaseService;
import com.feiqu.system.mapper.sysData.SysUserOnlineMapper;
import com.feiqu.system.model.sysData.SysUserOnline;
import com.feiqu.system.model.sysData.SysUserOnlineExample;
import com.feiqu.system.service.sysData.SysUserOnlineService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
* SysUserOnlineService实现
* Created by cwd on 2019/6/10.
*/
@Service
@Transactional
@BaseService
public class SysUserOnlineServiceImpl extends BaseServiceImpl<SysUserOnlineMapper, SysUserOnline, SysUserOnlineExample> implements SysUserOnlineService {

    private static Logger _log = LoggerFactory.getLogger(SysUserOnlineServiceImpl.class);

    @Resource
    SysUserOnlineMapper sysUserOnlineMapper;

    @Override
    public void batchDeleteOnline(List<String> sessions) {
        for (String sessionId : sessions)
        {
            SysUserOnline userOnline = sysUserOnlineMapper.selectByPrimaryKey(sessionId);
            if (ObjectUtil.isNotNull(userOnline))
            {
                sysUserOnlineMapper.deleteByPrimaryKey(sessionId);
            }
        }
    }

    @Override
    public List<SysUserOnline> selectOnlineByExpired(Date expiredDate) {
        String lastAccessTime = DateUtil.format(expiredDate,"yyyy-MM-dd HH:mm:ss");
        return sysUserOnlineMapper.selectOnlineByExpired(lastAccessTime);
    }

    @Override
    public int saveOnline(SysUserOnline online) {
        return sysUserOnlineMapper.saveOnline(online);
    }
}