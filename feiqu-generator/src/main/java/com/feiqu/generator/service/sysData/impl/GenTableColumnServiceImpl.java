package com.feiqu.generator.service.sysData.impl;

import com.feiqu.generator.mapper.sysData.GenTableColumnMapper;
import com.feiqu.generator.model.sysData.GenTableColumn;
import com.feiqu.generator.model.sysData.GenTableColumnExample;
import com.feiqu.generator.service.sysData.GenTableColumnService;
import com.feiqu.system.annotation.BaseService;
import com.feiqu.system.base.BaseServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * GenTableColumnService实现
 * Created by cwd on 2020/3/18.
 */
@Service
@Transactional
@BaseService
public class GenTableColumnServiceImpl extends BaseServiceImpl<GenTableColumnMapper, GenTableColumn, GenTableColumnExample> implements GenTableColumnService {

    private static Logger logger = LoggerFactory.getLogger(GenTableColumnServiceImpl.class);

    @Resource
    GenTableColumnMapper genTableColumnMapper;


}