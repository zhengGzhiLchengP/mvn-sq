package com.feiqu.system.service.collectData.impl;

import com.feiqu.system.base.BaseServiceImpl;
import com.feiqu.system.annotation.BaseService;
import com.feiqu.system.mapper.collectData.HotTopicMapper;
import com.feiqu.system.model.collectData.HotTopic;
import com.feiqu.system.model.collectData.HotTopicExample;
import com.feiqu.system.service.collectData.HotTopicService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * HotTopicService实现
 * Created by cwd on 2019/7/17.
 */
@Service
@Transactional
@BaseService
public class HotTopicServiceImpl extends BaseServiceImpl<HotTopicMapper, HotTopic, HotTopicExample> implements HotTopicService {

    private static Logger _log = LoggerFactory.getLogger(HotTopicServiceImpl.class);

    @Resource
    HotTopicMapper hotTopicMapper;

}