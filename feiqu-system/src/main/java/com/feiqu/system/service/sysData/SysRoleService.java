package com.feiqu.system.service.sysData;

import com.feiqu.system.base.BaseService;
import com.feiqu.system.model.sysData.SysRole;
import com.feiqu.system.model.sysData.SysRoleExample;
import com.feiqu.system.model.sysData.SysUserRole;

import java.util.List;
import java.util.Set;

/**
* SysRoleService接口
* created by cwd on 2019/6/10.
*/
public interface SysRoleService extends BaseService<SysRole, SysRoleExample> {

    Set<String> selectRoleKeys(Long userId);

    int deleteRoleByIds(String ids);

    int insertAuthUsers(Long roleId, String userIds);

    int deleteAuthUsers(Long roleId, String userIds);

    int deleteAuthUser(SysUserRole userRole);

    String checkRoleKeyUnique(SysRole role);

    int authDataScope(SysRole role);

    int updateRole(SysRole role);

    List<SysRole> selectRolesByUserId(Long userId);

    String checkRoleNameUnique(SysRole role);
}