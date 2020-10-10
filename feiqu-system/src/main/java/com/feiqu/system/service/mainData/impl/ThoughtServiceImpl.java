package com.feiqu.system.service.mainData.impl;


import com.feiqu.system.annotation.BaseService;
import com.feiqu.system.base.BaseServiceImpl;
import com.feiqu.system.mapper.mainData.ThoughtMapper;
import com.feiqu.system.model.Thought;
import com.feiqu.system.model.ThoughtExample;
import com.feiqu.system.pojo.response.ThoughtWithUser;
import com.feiqu.system.service.mainData.ThoughtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
* ThoughtService实现
* Created by cwd on 2017/9/29.
*/
@Service
@Transactional
@BaseService

public class ThoughtServiceImpl extends BaseServiceImpl<ThoughtMapper, Thought, ThoughtExample> implements ThoughtService {

    private static Logger _log = LoggerFactory.getLogger(ThoughtServiceImpl.class);

    @Resource
    ThoughtMapper thoughtMapper;

    public List<ThoughtWithUser> getThoughtWithUser(ThoughtExample thoughtExample) {
        return thoughtMapper.selectByExampleWithUser(thoughtExample);
    }

    @Override
    public ThoughtWithUser getByIdWithUser(Integer thoughtId) {
        return thoughtMapper.getByIdWithUser(thoughtId);
    }
}