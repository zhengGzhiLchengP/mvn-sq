package com.feiqu.system.service.mainData.impl;

import com.feiqu.system.base.BaseServiceImpl;
import com.feiqu.system.annotation.BaseService;
import com.feiqu.system.mapper.mainData.FqChangeLogCollectMapper;
import com.feiqu.system.model.FqChangeLogCollect;
import com.feiqu.system.model.FqChangeLogCollectExample;
import com.feiqu.system.service.mainData.FqChangeLogCollectService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
* FqChangeLogCollectService实现
* Created by cwd on 2019/3/23.
*/
@Service
@Transactional
@BaseService
public class FqChangeLogCollectServiceImpl extends BaseServiceImpl<FqChangeLogCollectMapper, FqChangeLogCollect, FqChangeLogCollectExample> implements FqChangeLogCollectService {

    private static Logger _log = LoggerFactory.getLogger(FqChangeLogCollectServiceImpl.class);

    @Resource
    FqChangeLogCollectMapper fqChangeLogCollectMapper;

}