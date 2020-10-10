package com.feiqu.system.service.collectData.impl;

import com.feiqu.system.base.BaseServiceImpl;
import com.feiqu.system.annotation.BaseService;
import com.feiqu.system.mapper.collectData.SoulMapper;
import com.feiqu.system.model.collectData.Soul;
import com.feiqu.system.model.collectData.SoulExample;
import com.feiqu.system.service.collectData.SoulService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.Resource;
/**
* SoulService实现
* Created by cwd on 2019/11/14.
*/
@Service
@Transactional
@BaseService
public class SoulServiceImpl extends BaseServiceImpl<SoulMapper, Soul, SoulExample> implements SoulService {

    private static Logger _log = LoggerFactory.getLogger(SoulServiceImpl.class);

@Resource
    SoulMapper soulMapper;

}