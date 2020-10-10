package com.feiqu.system.service.mainData.impl;

import com.feiqu.system.base.BaseServiceImpl;
import com.feiqu.system.annotation.BaseService;
import com.feiqu.system.mapper.mainData.LoginFailedLogMapper;
import com.feiqu.system.model.mainData.LoginFailedLog;
import com.feiqu.system.model.mainData.LoginFailedLogExample;
import com.feiqu.system.service.mainData.LoginFailedLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
* LoginFailedLogService实现
* Created by cwd on 2019/5/29.
*/
@Service
@Transactional
@BaseService
public class LoginFailedLogServiceImpl extends BaseServiceImpl<LoginFailedLogMapper, LoginFailedLog, LoginFailedLogExample> implements LoginFailedLogService {

    private static Logger _log = LoggerFactory.getLogger(LoginFailedLogServiceImpl.class);

    @Resource
    LoginFailedLogMapper loginFailedLogMapper;

}