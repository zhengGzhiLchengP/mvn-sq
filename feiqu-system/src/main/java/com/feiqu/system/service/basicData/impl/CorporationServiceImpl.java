package com.feiqu.system.service.basicData.impl;

import com.feiqu.system.base.BaseServiceImpl;
import com.feiqu.system.annotation.BaseService;
import com.feiqu.system.mapper.basicData.CorporationMapper;
import com.feiqu.system.model.basicData.Corporation;
import com.feiqu.system.model.basicData.CorporationExample;
import com.feiqu.system.service.basicData.CorporationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * CorporationService实现
 * Created by cwd on 2019/6/27.
 */
@Service
@Transactional
@BaseService
public class CorporationServiceImpl extends BaseServiceImpl<CorporationMapper, Corporation, CorporationExample> implements CorporationService {

    private static Logger _log = LoggerFactory.getLogger(CorporationServiceImpl.class);

    @Resource
    CorporationMapper corporationMapper;

}