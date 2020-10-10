package com.feiqu.system.service.mainData.impl;


import com.feiqu.system.annotation.BaseService;
import com.feiqu.system.base.BaseServiceImpl;
import com.feiqu.system.mapper.mainData.FqUserActivityRecordMapper;
import com.feiqu.system.model.FqUserActivityRecord;
import com.feiqu.system.model.FqUserActivityRecordExample;
import com.feiqu.system.service.mainData.FqUserActivityRecordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
* FqUserActivityRecordService实现
* Created by cwd on 2019/1/10.
*/
@Service
@Transactional
@BaseService

public class FqUserActivityRecordServiceImpl extends BaseServiceImpl<FqUserActivityRecordMapper, FqUserActivityRecord, FqUserActivityRecordExample> implements FqUserActivityRecordService {

    private static Logger _log = LoggerFactory.getLogger(FqUserActivityRecordServiceImpl.class);

    @Resource
    FqUserActivityRecordMapper fqUserActivityRecordMapper;

}