package com.feiqu.system.service.deployData.impl;

import com.feiqu.system.annotation.BaseService;
import com.feiqu.system.base.BaseServiceImpl;
import com.feiqu.system.mapper.deployData.DeployProjectMapper;
import com.feiqu.system.model.deployData.DeployProject;
import com.feiqu.system.model.deployData.DeployProjectExample;
import com.feiqu.system.service.deployData.DeployProjectService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
/**
* DeployProjectService实现
* Created by cwd on 2020/6/2.
*/
@Service
@Transactional
@BaseService
public class DeployProjectServiceImpl extends BaseServiceImpl<DeployProjectMapper, DeployProject, DeployProjectExample> implements DeployProjectService {

    private static Logger logger = LoggerFactory.getLogger(DeployProjectServiceImpl.class);

    @Resource
    DeployProjectMapper deployProjectMapper;

}