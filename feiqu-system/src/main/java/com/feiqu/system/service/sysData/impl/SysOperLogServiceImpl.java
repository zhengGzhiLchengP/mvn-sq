package com.feiqu.system.service.sysData.impl;

import com.feiqu.system.annotation.BaseService;
import com.feiqu.system.base.BaseServiceImpl;
import com.feiqu.system.mapper.sysData.SysOperLogMapper;
import com.feiqu.system.model.sysData.SysOperLog;
import com.feiqu.system.model.sysData.SysOperLogExample;
import com.feiqu.system.service.sysData.SysOperLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
* SysOperLogService实现
* Created by cwd on 2019/6/10.
*/
@Service
@Transactional
@BaseService
public class SysOperLogServiceImpl extends BaseServiceImpl<SysOperLogMapper, SysOperLog, SysOperLogExample> implements SysOperLogService {

    private static Logger _log = LoggerFactory.getLogger(SysOperLogServiceImpl.class);

    @Resource
    SysOperLogMapper sysOperLogMapper;

    @Override
    public void cleanOperLog() {
        sysOperLogMapper.cleanOperLog();
    }
}