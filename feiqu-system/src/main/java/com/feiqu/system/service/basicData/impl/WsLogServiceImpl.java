package com.feiqu.system.service.basicData.impl;

import com.feiqu.system.base.BaseServiceImpl;
import com.feiqu.system.annotation.BaseService;
import com.feiqu.system.mapper.basicData.WsLogMapper;
import com.feiqu.system.model.basicData.WsLog;
import com.feiqu.system.model.basicData.WsLogExample;
import com.feiqu.system.service.basicData.WsLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * WsLogService实现
 * Created by cwd on 2019/6/24.
 */
@Service
@Transactional
@BaseService
public class WsLogServiceImpl extends BaseServiceImpl<WsLogMapper, WsLog, WsLogExample> implements WsLogService {

    private static Logger _log = LoggerFactory.getLogger(WsLogServiceImpl.class);

    @Resource
    WsLogMapper wsLogMapper;

}