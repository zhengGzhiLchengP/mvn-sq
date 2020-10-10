package com.feiqu.system.service.sysData.impl;

import com.feiqu.system.base.BaseServiceImpl;
import com.feiqu.system.annotation.BaseService;
import com.feiqu.system.mapper.sysData.SysRoleMenuMapper;
import com.feiqu.system.model.sysData.SysRoleMenu;
import com.feiqu.system.model.sysData.SysRoleMenuExample;
import com.feiqu.system.service.sysData.SysRoleMenuService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
* SysRoleMenuService实现
* Created by cwd on 2019/6/11.
*/
@Service
@Transactional
@BaseService
public class SysRoleMenuServiceImpl extends BaseServiceImpl<SysRoleMenuMapper, SysRoleMenu, SysRoleMenuExample> implements SysRoleMenuService {

    private static Logger _log = LoggerFactory.getLogger(SysRoleMenuServiceImpl.class);

    @Resource
    SysRoleMenuMapper sysRoleMenuMapper;

}