package com.feiqu.system.service.mainData.impl;


import com.feiqu.system.annotation.BaseService;
import com.feiqu.system.base.BaseServiceImpl;
import com.feiqu.system.mapper.mainData.FqFriendLinkMapper;
import com.feiqu.system.model.FqFriendLink;
import com.feiqu.system.model.FqFriendLinkExample;
import com.feiqu.system.service.mainData.FqFriendLinkService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
* FqFriendLinkService实现
* Created by cwd on 2018/1/2.
*/
@Service
@Transactional
@BaseService

public class FqFriendLinkServiceImpl extends BaseServiceImpl<FqFriendLinkMapper, FqFriendLink, FqFriendLinkExample> implements FqFriendLinkService {

    private static Logger _log = LoggerFactory.getLogger(FqFriendLinkServiceImpl.class);

    @Resource
    FqFriendLinkMapper fqFriendLinkMapper;

}