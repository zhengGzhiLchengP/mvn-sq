package com.feiqu.system.service.mainData.impl;


import com.feiqu.system.annotation.BaseService;
import com.feiqu.system.base.BaseServiceImpl;
import com.feiqu.system.mapper.mainData.FqLabelMapper;
import com.feiqu.system.model.FqLabel;
import com.feiqu.system.model.FqLabelExample;
import com.feiqu.system.service.mainData.FqLabelService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
* FqLabelService实现
* Created by cwd on 2017/11/20.
*/
@Service
@Transactional
@BaseService

public class FqLabelServiceImpl extends BaseServiceImpl<FqLabelMapper, FqLabel, FqLabelExample> implements FqLabelService {

    private static Logger _log = LoggerFactory.getLogger(FqLabelServiceImpl.class);

    @Resource
    FqLabelMapper fqLabelMapper;

}