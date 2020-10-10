package com.feiqu.system.service.sysData;

import com.feiqu.system.base.BaseService;
import com.feiqu.system.model.sysData.SysRole;
import com.feiqu.system.model.sysData.SysUser;
import com.feiqu.system.model.sysData.SysUserExample;

import java.util.List;

/**
* SysUserService接口
* created by cwd on 2019/6/10.
*/
public interface SysUserService extends BaseService<SysUser, SysUserExample> {

    SysUser selectUserByLoginName(String username);

    SysUser selectUserByPhoneNumber(String username);

    SysUser selectUserByEmail(String username);

    String checkLoginNameUnique(String loginName);

    List<SysUser> selectUnallocatedList(SysUser user);

    List<SysUser> selectAllocatedList(SysUser user);

    String checkPhoneUnique(SysUser user);

    String checkEmailUnique(SysUser user);

    String selectUserRoleGroup(Long userId);

    String selectUserPostGroup(Long userId);

    int insertUser(SysUser user);

    List<SysUser> selectUserList(SysUser user);

    SysUser selectUserById(Long userId);
}