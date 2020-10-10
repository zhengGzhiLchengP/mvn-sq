package com.feiqu.system.service.mainData.impl;

import com.feiqu.system.base.BaseServiceImpl;
import com.feiqu.system.annotation.BaseService;
import com.feiqu.system.mapper.mainData.FqLolitaMapper;
import com.feiqu.system.model.FqLolita;
import com.feiqu.system.model.FqLolitaExample;
import com.feiqu.system.pojo.condition.CommonCondition;
import com.feiqu.system.pojo.response.FqLolitaDTO;
import com.feiqu.system.service.mainData.FqLolitaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
* FqLolitaService实现
* Created by cwd on 2019/2/19.
*/
@Service
@Transactional
@BaseService
public class FqLolitaServiceImpl extends BaseServiceImpl<FqLolitaMapper, FqLolita, FqLolitaExample> implements FqLolitaService {

    private static Logger _log = LoggerFactory.getLogger(FqLolitaServiceImpl.class);

    @Resource
    FqLolitaMapper fqLolitaMapper;

    @Override
    public List<FqLolitaDTO> selectWithUser(CommonCondition condition) {
        return fqLolitaMapper.selectWithUser(condition);
    }
}