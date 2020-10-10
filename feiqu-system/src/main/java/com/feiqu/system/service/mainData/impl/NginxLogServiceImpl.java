package com.feiqu.system.service.mainData.impl;


import com.feiqu.system.annotation.BaseService;
import com.feiqu.system.base.BaseServiceImpl;
import com.feiqu.system.mapper.mainData.NginxLogMapper;
import com.feiqu.system.model.NginxLog;
import com.feiqu.system.model.NginxLogExample;
import com.feiqu.system.service.mainData.NginxLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
* NginxLogService实现
* Created by cwd on 2017/11/14.
*/
@Service
@Transactional
@BaseService

public class NginxLogServiceImpl extends BaseServiceImpl<NginxLogMapper, NginxLog, NginxLogExample> implements NginxLogService {

    private static Logger _log = LoggerFactory.getLogger(NginxLogServiceImpl.class);

    @Resource
    NginxLogMapper nginxLogMapper;

    public long countUvByExample(NginxLogExample example) {
        return nginxLogMapper.countUvByExample(example);
    }
}