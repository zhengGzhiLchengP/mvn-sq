package com.feiqu.system.service.deployData.impl;

import com.feiqu.system.annotation.BaseService;
import com.feiqu.system.base.BaseServiceImpl;
import com.feiqu.system.mapper.deployData.SshModelMapper;
import com.feiqu.system.model.deployData.SshModel;
import com.feiqu.system.model.deployData.SshModelExample;
import com.feiqu.system.service.deployData.SshModelService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
/**
* SshModelService实现
* Created by cwd on 2020/5/26.
*/
@Service
@Transactional
@BaseService
public class SshModelServiceImpl extends BaseServiceImpl<SshModelMapper, SshModel, SshModelExample> implements SshModelService {

    private static Logger logger = LoggerFactory.getLogger(SshModelServiceImpl.class);

    @Resource
    SshModelMapper sshModelMapper;

}