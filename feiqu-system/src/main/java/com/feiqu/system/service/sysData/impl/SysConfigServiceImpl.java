package com.feiqu.system.service.sysData.impl;

import cn.hutool.core.util.ObjectUtil;
import com.feiqu.common.constant.UserConstants;
import com.feiqu.system.base.BaseServiceImpl;
import com.feiqu.system.annotation.BaseService;
import com.feiqu.system.mapper.sysData.SysConfigMapper;
import com.feiqu.system.model.sysData.SysConfig;
import com.feiqu.system.model.sysData.SysConfigExample;
import com.feiqu.system.service.sysData.SysConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * SysConfigService实现
 * Created by cwd on 2019/7/15.
 */
@Service
@Transactional
@BaseService
public class SysConfigServiceImpl extends BaseServiceImpl<SysConfigMapper, SysConfig, SysConfigExample> implements SysConfigService {

    private static Logger _log = LoggerFactory.getLogger(SysConfigServiceImpl.class);

    @Resource
    SysConfigMapper sysConfigMapper;

    @Override
    public String checkConfigKeyUnique(SysConfig config) {
        Long configId = ObjectUtil.isNull(config.getConfigId()) ? -1L : config.getConfigId();
        SysConfigExample sysConfigExample = new SysConfigExample();
        sysConfigExample.createCriteria().andConfigKeyEqualTo(config.getConfigKey());
        SysConfig info = selectFirstByExample(sysConfigExample);
        if (ObjectUtil.isNotNull(info) && info.getConfigId().longValue() != configId) {
            return UserConstants.CONFIG_KEY_NOT_UNIQUE;
        }
        return UserConstants.CONFIG_KEY_UNIQUE;
    }
}