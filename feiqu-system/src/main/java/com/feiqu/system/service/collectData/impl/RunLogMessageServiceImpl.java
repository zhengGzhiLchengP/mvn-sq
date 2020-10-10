package com.feiqu.system.service.collectData.impl;

import com.feiqu.system.annotation.BaseService;
import com.feiqu.system.base.BaseServiceImpl;
import com.feiqu.system.mapper.collectData.RunLogMessageMapper;
import com.feiqu.system.model.collectData.RunLogMessage;
import com.feiqu.system.model.collectData.RunLogMessageExample;
import com.feiqu.system.service.collectData.RunLogMessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
/**
* RunLogMessageService实现
* Created by cwd on 2020/5/21.
*/
@Service
@Transactional
@BaseService
public class RunLogMessageServiceImpl extends BaseServiceImpl<RunLogMessageMapper, RunLogMessage, RunLogMessageExample> implements RunLogMessageService {

    private static Logger logger = LoggerFactory.getLogger(RunLogMessageServiceImpl.class);

    @Resource
    RunLogMessageMapper runLogMessageMapper;

}