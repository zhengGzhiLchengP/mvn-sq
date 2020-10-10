package com.feiqu.system.service.mainData.impl;


import com.feiqu.system.annotation.BaseService;
import com.feiqu.system.base.BaseServiceImpl;
import com.feiqu.system.mapper.mainData.SuperBeautyMapper;
import com.feiqu.system.model.SuperBeauty;
import com.feiqu.system.model.SuperBeautyExample;
import com.feiqu.system.pojo.response.BeautyUserResponse;
import com.feiqu.system.service.mainData.SuperBeautyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
* SuperBeautyService实现
* Created by cwd on 2017/10/16.
*/
@Service
@Transactional
@BaseService

public class SuperBeautyServiceImpl extends BaseServiceImpl<SuperBeautyMapper, SuperBeauty, SuperBeautyExample> implements SuperBeautyService {

    private static Logger _log = LoggerFactory.getLogger(SuperBeautyServiceImpl.class);

    @Resource
    SuperBeautyMapper superBeautyMapper;

    public List<BeautyUserResponse> selectDetailByExample(SuperBeautyExample example) {
        return superBeautyMapper.selectDetailByExample(example);
    }

    @Override
    public List<BeautyUserResponse> selectDetailById(Integer beautyId) {
        return superBeautyMapper.selectDetailById(beautyId);
    }

    @Override
    public List<SuperBeauty> selectRandom(int i) {
        return superBeautyMapper.selectRandom(i);
    }
}