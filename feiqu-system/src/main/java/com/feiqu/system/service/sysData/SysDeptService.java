package com.feiqu.system.service.sysData;

import com.feiqu.common.core.domain.Ztree;
import com.feiqu.system.base.BaseService;
import com.feiqu.system.model.sysData.SysDept;
import com.feiqu.system.model.sysData.SysDeptExample;
import com.feiqu.system.model.sysData.SysRole;

import java.util.List;

/**
* SysDeptService接口
* created by cwd on 2019/6/10.
*/
public interface SysDeptService extends BaseService<SysDept, SysDeptExample> {

    List<Ztree> selectDeptTree(SysDept sysDept);

    List<Ztree> roleDeptTreeData(SysRole role);

    String checkDeptNameUnique(SysDept dept);
}