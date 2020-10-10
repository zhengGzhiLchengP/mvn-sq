package com.feiqu.system.service.sysData;

import com.feiqu.system.base.BaseService;
import com.feiqu.system.model.sysData.SysConfig;
import com.feiqu.system.model.sysData.SysConfigExample;

/**
 * SysConfigService接口
 * created by cwd on 2019/7/15.
 */
public interface SysConfigService extends BaseService<SysConfig, SysConfigExample> {

    String checkConfigKeyUnique(SysConfig config);
}