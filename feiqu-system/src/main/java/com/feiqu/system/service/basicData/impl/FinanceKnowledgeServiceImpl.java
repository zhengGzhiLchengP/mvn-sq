package com.feiqu.system.service.basicData.impl;

import com.feiqu.system.base.BaseServiceImpl;
import com.feiqu.system.annotation.BaseService;
import com.feiqu.system.mapper.basicData.FinanceKnowledgeMapper;
import com.feiqu.system.model.basicData.FinanceKnowledge;
import com.feiqu.system.model.basicData.FinanceKnowledgeExample;
import com.feiqu.system.service.basicData.FinanceKnowledgeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * FinanceKnowledgeService实现
 * Created by cwd on 2019/7/9.
 */
@Service
@Transactional
@BaseService
public class FinanceKnowledgeServiceImpl extends BaseServiceImpl<FinanceKnowledgeMapper, FinanceKnowledge, FinanceKnowledgeExample> implements FinanceKnowledgeService {

    private static Logger _log = LoggerFactory.getLogger(FinanceKnowledgeServiceImpl.class);

    @Resource
    FinanceKnowledgeMapper financeKnowledgeMapper;

}