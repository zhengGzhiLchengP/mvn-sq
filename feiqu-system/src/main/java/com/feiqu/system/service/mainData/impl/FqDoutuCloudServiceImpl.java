package com.feiqu.system.service.mainData.impl;

import com.feiqu.system.base.BaseServiceImpl;
import com.feiqu.system.annotation.BaseService;
import com.feiqu.system.mapper.mainData.FqDoutuCloudMapper;
import com.feiqu.system.model.FqDoutuCloud;
import com.feiqu.system.model.FqDoutuCloudExample;
import com.feiqu.system.pojo.condition.CommonCondition;
import com.feiqu.system.pojo.response.FqDoutuCloudWithUser;
import com.feiqu.system.service.mainData.FqDoutuCloudService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
* FqDoutuCloudService实现
* Created by cwd on 2019/3/6.
*/
@Service
@Transactional
@BaseService
public class FqDoutuCloudServiceImpl extends BaseServiceImpl<FqDoutuCloudMapper, FqDoutuCloud, FqDoutuCloudExample> implements FqDoutuCloudService {

    private static Logger _log = LoggerFactory.getLogger(FqDoutuCloudServiceImpl.class);

    @Resource
    FqDoutuCloudMapper fqDoutuCloudMapper;

    @Override
    public List<FqDoutuCloudWithUser> selectWithUser(CommonCondition commonCondition) {
        return fqDoutuCloudMapper.selectWithUser(commonCondition);
    }
}