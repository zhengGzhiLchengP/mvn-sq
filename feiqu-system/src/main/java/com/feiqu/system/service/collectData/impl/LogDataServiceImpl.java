package com.feiqu.system.service.collectData.impl;

import com.feiqu.system.base.BaseServiceImpl;
import com.feiqu.system.annotation.BaseService;
import com.feiqu.system.mapper.collectData.LogDataMapper;
import com.feiqu.system.model.collectData.LogData;
import com.feiqu.system.model.collectData.LogDataExample;
import com.feiqu.system.service.collectData.LogDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.Resource;
/**
* LogDataService实现
* Created by cwd on 2019/8/21.
*/
@Service
@Transactional
@BaseService
public class LogDataServiceImpl extends BaseServiceImpl<LogDataMapper, LogData, LogDataExample> implements LogDataService {

    private static Logger _log = LoggerFactory.getLogger(LogDataServiceImpl.class);

@Resource
    LogDataMapper logDataMapper;

}