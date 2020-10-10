package com.feiqu.system.service.mainData.impl;


import com.feiqu.system.annotation.BaseService;
import com.feiqu.system.base.BaseServiceImpl;
import com.feiqu.system.mapper.mainData.FqThirdPartyMapper;
import com.feiqu.system.mapper.mainData.FqUserMapper;
import com.feiqu.system.model.FqThirdParty;
import com.feiqu.system.model.FqUser;
import com.feiqu.system.model.FqUserExample;
import com.feiqu.system.pojo.ThirdPartyUser;
import com.feiqu.system.service.mainData.FqUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
* FqUserService实现
* Created by cwd on 2017/11/7.
*/
@Service
@Transactional
@BaseService
public class FqUserServiceImpl extends BaseServiceImpl<FqUserMapper, FqUser, FqUserExample> implements FqUserService {

    private static Logger _log = LoggerFactory.getLogger(FqUserServiceImpl.class);

    @Resource
    FqUserMapper fqUserMapper;
    @Resource
    FqThirdPartyMapper fqThirdPartyMapper;

    public int insertThirdPartyUser(FqUser fqUser, ThirdPartyUser thirdUser) {
        int userId = fqUserMapper.insert(fqUser);
        thirdUser.setUserId(fqUser.getId());
        FqThirdParty fqThirdParty = new FqThirdParty(thirdUser);
        fqThirdPartyMapper.insert(fqThirdParty);
        return userId;
    }
}