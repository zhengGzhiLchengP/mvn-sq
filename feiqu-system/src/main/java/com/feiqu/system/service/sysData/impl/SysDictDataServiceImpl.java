package com.feiqu.system.service.sysData.impl;

import com.feiqu.system.base.BaseServiceImpl;
import com.feiqu.system.annotation.BaseService;
import com.feiqu.system.mapper.sysData.SysDictDataMapper;
import com.feiqu.system.model.sysData.SysDictData;
import com.feiqu.system.model.sysData.SysDictDataExample;
import com.feiqu.system.service.sysData.SysDictDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
* SysDictDataService实现
* Created by cwd on 2019/6/11.
*/
@Service
@Transactional
@BaseService
public class SysDictDataServiceImpl extends BaseServiceImpl<SysDictDataMapper, SysDictData, SysDictDataExample> implements SysDictDataService {

    private static Logger _log = LoggerFactory.getLogger(SysDictDataServiceImpl.class);

    @Resource
    SysDictDataMapper sysDictDataMapper;

}