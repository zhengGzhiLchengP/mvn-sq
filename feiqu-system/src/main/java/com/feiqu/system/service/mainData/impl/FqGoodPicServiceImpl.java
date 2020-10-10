package com.feiqu.system.service.mainData.impl;

import com.feiqu.system.base.BaseServiceImpl;
import com.feiqu.system.annotation.BaseService;
import com.feiqu.system.mapper.mainData.FqGoodPicMapper;
import com.feiqu.system.model.FqGoodPic;
import com.feiqu.system.model.FqGoodPicExample;
import com.feiqu.system.service.mainData.FqGoodPicService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
* FqGoodPicService实现
* Created by cwd on 2019/3/10.
*/
@Service
@Transactional
@BaseService
public class FqGoodPicServiceImpl extends BaseServiceImpl<FqGoodPicMapper, FqGoodPic, FqGoodPicExample> implements FqGoodPicService {

    private static Logger _log = LoggerFactory.getLogger(FqGoodPicServiceImpl.class);

    @Resource
    FqGoodPicMapper fqGoodPicMapper;

}