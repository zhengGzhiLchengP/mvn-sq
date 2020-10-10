package com.feiqu.system.service.mainData.impl;

import com.feiqu.system.base.BaseServiceImpl;
import com.feiqu.system.annotation.BaseService;
import com.feiqu.system.mapper.mainData.FqAdvertisementMapper;
import com.feiqu.system.model.FqAdvertisement;
import com.feiqu.system.model.FqAdvertisementExample;
import com.feiqu.system.service.mainData.FqAdvertisementService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
* FqAdvertisementService实现
* Created by cwd on 2019/2/21.
*/
@Service
@Transactional
@BaseService
public class FqAdvertisementServiceImpl extends BaseServiceImpl<FqAdvertisementMapper, FqAdvertisement, FqAdvertisementExample> implements FqAdvertisementService {

    private static Logger _log = LoggerFactory.getLogger(FqAdvertisementServiceImpl.class);

    @Resource
    FqAdvertisementMapper fqAdvertisementMapper;

}