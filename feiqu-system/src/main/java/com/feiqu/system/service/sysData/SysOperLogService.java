package com.feiqu.system.service.sysData;

import com.feiqu.system.base.BaseService;
import com.feiqu.system.model.sysData.SysOperLog;
import com.feiqu.system.model.sysData.SysOperLogExample;
/**
* SysOperLogService接口
* created by cwd on 2019/6/10.
*/
public interface SysOperLogService extends BaseService<SysOperLog, SysOperLogExample> {

    void cleanOperLog();
}