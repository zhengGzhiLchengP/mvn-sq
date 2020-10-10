package com.feiqu.system.service.sysData.impl;

import com.feiqu.system.base.BaseServiceImpl;
import com.feiqu.system.annotation.BaseService;
import com.feiqu.system.mapper.sysData.SysUserRoleMapper;
import com.feiqu.system.model.sysData.SysUserRole;
import com.feiqu.system.model.sysData.SysUserRoleExample;
import com.feiqu.system.service.sysData.SysUserRoleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
* SysUserRoleService实现
* Created by cwd on 2019/6/11.
*/
@Service
@Transactional
@BaseService
public class SysUserRoleServiceImpl extends BaseServiceImpl<SysUserRoleMapper, SysUserRole, SysUserRoleExample> implements SysUserRoleService {

    private static Logger _log = LoggerFactory.getLogger(SysUserRoleServiceImpl.class);

    @Resource
    SysUserRoleMapper sysUserRoleMapper;

}