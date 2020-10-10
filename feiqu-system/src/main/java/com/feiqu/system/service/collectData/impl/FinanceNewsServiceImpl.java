package com.feiqu.system.service.collectData.impl;

import com.feiqu.system.annotation.BaseService;
import com.feiqu.system.base.BaseServiceImpl;
import com.feiqu.system.mapper.collectData.FinanceNewsMapper;
import com.feiqu.system.model.collectData.FinanceNews;
import com.feiqu.system.model.collectData.FinanceNewsExample;
import com.feiqu.system.service.collectData.FinanceNewsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * FinanceNewsService实现
 * Created by cwd on 2020/4/22.
 */
@Service
@Transactional
@BaseService
public class FinanceNewsServiceImpl extends BaseServiceImpl<FinanceNewsMapper, FinanceNews, FinanceNewsExample> implements FinanceNewsService {

    private static Logger logger = LoggerFactory.getLogger(FinanceNewsServiceImpl.class);

    @Resource
    FinanceNewsMapper financeNewsMapper;

}