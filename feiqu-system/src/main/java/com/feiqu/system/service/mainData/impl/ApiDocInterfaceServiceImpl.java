package com.feiqu.system.service.mainData.impl;


import com.feiqu.system.annotation.BaseService;
import com.feiqu.system.base.BaseServiceImpl;
import com.feiqu.system.mapper.mainData.ApiDocInterfaceMapper;
import com.feiqu.system.model.ApiDocInterface;
import com.feiqu.system.model.ApiDocInterfaceExample;
import com.feiqu.system.service.mainData.ApiDocInterfaceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
* ApiDocInterfaceService实现
* Created by cwd on 2019/1/13.
*/
@Service
@Transactional
@BaseService

public class ApiDocInterfaceServiceImpl extends BaseServiceImpl<ApiDocInterfaceMapper, ApiDocInterface, ApiDocInterfaceExample> implements ApiDocInterfaceService {

    private static Logger _log = LoggerFactory.getLogger(ApiDocInterfaceServiceImpl.class);

    @Resource
    ApiDocInterfaceMapper apiDocInterfaceMapper;

}