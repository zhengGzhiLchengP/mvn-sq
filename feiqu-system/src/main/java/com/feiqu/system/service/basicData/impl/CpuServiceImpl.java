package com.feiqu.system.service.basicData.impl;

import com.feiqu.system.annotation.BaseService;
import com.feiqu.system.base.BaseServiceImpl;
import com.feiqu.system.mapper.basicData.CpuMapper;
import com.feiqu.system.model.basicData.Cpu;
import com.feiqu.system.model.basicData.CpuExample;
import com.feiqu.system.service.basicData.CpuService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
/**
* CpuService实现
* Created by cwd on 2020/7/29.
*/
@Service
@Transactional
@BaseService
public class CpuServiceImpl extends BaseServiceImpl<CpuMapper, Cpu, CpuExample> implements CpuService {

    private static Logger logger = LoggerFactory.getLogger(CpuServiceImpl.class);

    @Resource
    CpuMapper cpuMapper;

}