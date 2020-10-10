package com.feiqu.system.service.mainData.impl;


import com.feiqu.system.annotation.BaseService;
import com.feiqu.system.base.BaseServiceImpl;
import com.feiqu.system.mapper.mainData.FqBackgroundImgMapper;
import com.feiqu.system.model.FqBackgroundImg;
import com.feiqu.system.model.FqBackgroundImgExample;
import com.feiqu.system.service.mainData.FqBackgroundImgService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
* FqBackgroundImgService实现
* Created by cwd on 2018/1/11.
*/
@Service
@Transactional
@BaseService

public class FqBackgroundImgServiceImpl extends BaseServiceImpl<FqBackgroundImgMapper, FqBackgroundImg, FqBackgroundImgExample> implements FqBackgroundImgService {

    private static Logger _log = LoggerFactory.getLogger(FqBackgroundImgServiceImpl.class);

    @Resource
    FqBackgroundImgMapper fqBackgroundImgMapper;

}