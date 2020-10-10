package com.feiqu.system.service.basicData.impl;

import com.feiqu.system.base.BaseServiceImpl;
import com.feiqu.system.annotation.BaseService;
import com.feiqu.system.mapper.basicData.ShellScriptMapper;
import com.feiqu.system.model.basicData.ShellScript;
import com.feiqu.system.model.basicData.ShellScriptExample;
import com.feiqu.system.service.basicData.ShellScriptService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.Resource;
/**
* ShellScriptService实现
* Created by cwd on 2019/9/26.
*/
@Service
@Transactional
@BaseService
public class ShellScriptServiceImpl extends BaseServiceImpl<ShellScriptMapper, ShellScript, ShellScriptExample> implements ShellScriptService {

    private static Logger _log = LoggerFactory.getLogger(ShellScriptServiceImpl.class);

@Resource
    ShellScriptMapper shellScriptMapper;

}