package com.feiqu.system.service.basicData.impl;

import com.feiqu.system.base.BaseServiceImpl;
import com.feiqu.system.annotation.BaseService;
import com.feiqu.system.mapper.basicData.StockBasicMapper;
import com.feiqu.system.model.basicData.StockBasic;
import com.feiqu.system.model.basicData.StockBasicExample;
import com.feiqu.system.service.basicData.StockBasicService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * StockBasicService实现
 * Created by cwd on 2019/6/26.
 */
@Service
@Transactional
@BaseService
public class StockBasicServiceImpl extends BaseServiceImpl<StockBasicMapper, StockBasic, StockBasicExample> implements StockBasicService {

    private static Logger _log = LoggerFactory.getLogger(StockBasicServiceImpl.class);

    @Resource
    StockBasicMapper stockBasicMapper;

}