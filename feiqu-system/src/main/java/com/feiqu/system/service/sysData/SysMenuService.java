package com.feiqu.system.service.sysData;

import com.feiqu.common.core.domain.Ztree;
import com.feiqu.system.base.BaseService;
import com.feiqu.system.model.sysData.SysMenu;
import com.feiqu.system.model.sysData.SysMenuExample;
import com.feiqu.system.model.sysData.SysRole;
import com.feiqu.system.model.sysData.SysUser;

import java.util.List;
import java.util.Set;

/**
* SysMenuService接口
* created by cwd on 2019/6/10.
*/
public interface SysMenuService extends BaseService<SysMenu, SysMenuExample> {

    Set<String> selectPermsByUserId(Long userId);

    List<SysMenu> selectMenusByUser(SysUser user);

    String checkMenuNameUnique(SysMenu menu);

    List<Ztree> roleMenuTreeData(SysRole role);

    List<Ztree> menuTreeData();

    long selectCountRoleMenuByMenuId(Long menuId);

    int deleteById(Long menuId);
}