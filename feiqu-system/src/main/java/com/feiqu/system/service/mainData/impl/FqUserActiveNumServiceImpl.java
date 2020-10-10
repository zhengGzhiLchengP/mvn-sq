package com.feiqu.system.service.mainData.impl;

import com.feiqu.system.base.BaseServiceImpl;
import com.feiqu.system.annotation.BaseService;
import com.feiqu.system.mapper.mainData.FqUserActiveNumMapper;
import com.feiqu.system.model.FqUserActiveNum;
import com.feiqu.system.model.FqUserActiveNumExample;
import com.feiqu.system.service.mainData.FqUserActiveNumService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
* FqUserActiveNumService实现
* Created by cwd on 2019/2/1.
*/
@Service
@Transactional
@BaseService
public class FqUserActiveNumServiceImpl extends BaseServiceImpl<FqUserActiveNumMapper, FqUserActiveNum, FqUserActiveNumExample> implements FqUserActiveNumService {

    private static Logger _log = LoggerFactory.getLogger(FqUserActiveNumServiceImpl.class);

    @Resource
    FqUserActiveNumMapper fqUserActiveNumMapper;

}