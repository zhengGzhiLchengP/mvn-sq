package com.feiqu.system.service.sysData;

import com.feiqu.system.base.BaseService;
import com.feiqu.system.model.sysData.SysDictType;
import com.feiqu.system.model.sysData.SysDictTypeExample;

/**
 * SysDictTypeService接口
 * created by cwd on 2019/6/12.
 */
public interface SysDictTypeService extends BaseService<SysDictType, SysDictTypeExample> {

    int deleteDictTypeByIds(String ids);
}