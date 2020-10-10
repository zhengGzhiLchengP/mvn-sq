package com.feiqu.system.service.mainData.impl;

import com.feiqu.system.annotation.BaseService;
import com.feiqu.system.base.BaseServiceImpl;
import com.feiqu.system.mapper.mainData.FqMenuMapper;
import com.feiqu.system.model.mainData.FqMenu;
import com.feiqu.system.model.mainData.FqMenuExample;
import com.feiqu.system.service.mainData.FqMenuService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * FqMenuService实现
 * Created by cwd on 2020/4/13.
 */
@Service
@Transactional
@BaseService
public class FqMenuServiceImpl extends BaseServiceImpl<FqMenuMapper, FqMenu, FqMenuExample> implements FqMenuService {

    private static Logger logger = LoggerFactory.getLogger(FqMenuServiceImpl.class);

    @Resource
    FqMenuMapper fqMenuMapper;

}