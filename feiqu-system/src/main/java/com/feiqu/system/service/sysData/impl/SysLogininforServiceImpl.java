package com.feiqu.system.service.sysData.impl;

import com.feiqu.system.base.BaseServiceImpl;
import com.feiqu.system.annotation.BaseService;
import com.feiqu.system.mapper.sysData.SysLogininforMapper;
import com.feiqu.system.model.sysData.SysLogininfor;
import com.feiqu.system.model.sysData.SysLogininforExample;
import com.feiqu.system.service.sysData.SysLogininforService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
* SysLogininforService实现
* Created by cwd on 2019/6/10.
*/
@Service
@Transactional
@BaseService
public class SysLogininforServiceImpl extends BaseServiceImpl<SysLogininforMapper, SysLogininfor, SysLogininforExample> implements SysLogininforService {

    private static Logger _log = LoggerFactory.getLogger(SysLogininforServiceImpl.class);

    @Resource
    SysLogininforMapper sysLogininforMapper;

}