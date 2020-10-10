package com.feiqu.system.service.mainData.impl;

import com.feiqu.system.annotation.BaseService;
import com.feiqu.system.base.BaseServiceImpl;
import com.feiqu.system.mapper.mainData.SubscribeMapper;
import com.feiqu.system.model.mainData.Subscribe;
import com.feiqu.system.model.mainData.SubscribeExample;
import com.feiqu.system.service.mainData.SubscribeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * SubscribeService实现
 * Created by cwd on 2020/4/13.
 */
@Service
@Transactional
@BaseService
public class SubscribeServiceImpl extends BaseServiceImpl<SubscribeMapper, Subscribe, SubscribeExample> implements SubscribeService {

    private static Logger logger = LoggerFactory.getLogger(SubscribeServiceImpl.class);

    @Resource
    SubscribeMapper subscribeMapper;

}