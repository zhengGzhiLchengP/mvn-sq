package com.feiqu.system.service.sysData.impl;

import com.feiqu.system.base.BaseServiceImpl;
import com.feiqu.system.annotation.BaseService;
import com.feiqu.system.mapper.sysData.SysUserPostMapper;
import com.feiqu.system.model.sysData.SysUserPost;
import com.feiqu.system.model.sysData.SysUserPostExample;
import com.feiqu.system.service.sysData.SysUserPostService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * SysUserPostService实现
 * Created by cwd on 2019/7/15.
 */
@Service
@Transactional
@BaseService
public class SysUserPostServiceImpl extends BaseServiceImpl<SysUserPostMapper, SysUserPost, SysUserPostExample> implements SysUserPostService {

    private static Logger _log = LoggerFactory.getLogger(SysUserPostServiceImpl.class);

    @Resource
    SysUserPostMapper sysUserPostMapper;

}