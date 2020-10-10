package com.feiqu.system.service.mainData.impl;

import com.feiqu.system.base.BaseServiceImpl;
import com.feiqu.system.annotation.BaseService;
import com.feiqu.system.mapper.mainData.FqUserAuthMapper;
import com.feiqu.system.model.FqUserAuth;
import com.feiqu.system.model.FqUserAuthExample;
import com.feiqu.system.service.mainData.FqUserAuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
* FqUserAuthService实现
* Created by cwd on 2019/3/10.
*/
@Service
@Transactional
@BaseService
public class FqUserAuthServiceImpl extends BaseServiceImpl<FqUserAuthMapper, FqUserAuth, FqUserAuthExample> implements FqUserAuthService {

    private static Logger _log = LoggerFactory.getLogger(FqUserAuthServiceImpl.class);

    @Resource
    FqUserAuthMapper fqUserAuthMapper;

}