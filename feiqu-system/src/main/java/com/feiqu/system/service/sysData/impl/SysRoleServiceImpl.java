package com.feiqu.system.service.sysData.impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import com.feiqu.common.constant.UserConstants;
import com.feiqu.common.exception.BusinessException;
import com.feiqu.system.base.BaseServiceImpl;
import com.feiqu.system.annotation.BaseService;
import com.feiqu.system.mapper.sysData.SysRoleMapper;
import com.feiqu.system.mapper.sysData.SysRoleMenuMapper;
import com.feiqu.system.mapper.sysData.SysUserRoleMapper;
import com.feiqu.system.model.sysData.*;
import com.feiqu.system.service.sysData.SysRoleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

/**
* SysRoleService实现
* Created by cwd on 2019/6/10.
*/
@Service
@Transactional
@BaseService
public class SysRoleServiceImpl extends BaseServiceImpl<SysRoleMapper, SysRole, SysRoleExample> implements SysRoleService {

    private static Logger _log = LoggerFactory.getLogger(SysRoleServiceImpl.class);

    @Resource
    SysRoleMapper sysRoleMapper;

    @Resource
    SysUserRoleMapper sysUserRoleMapper;
    @Resource
    SysRoleMenuMapper sysRoleMenuMapper;

    @Override
    public Set<String> selectRoleKeys(Long userId) {
        List<SysRole> perms = sysRoleMapper.selectRolesByUserId(userId);
        Set<String> permsSet = new HashSet<>();
        for (SysRole perm : perms)
        {
            if (ObjectUtil.isNotNull(perm))
            {
                permsSet.addAll(Arrays.asList(perm.getRoleKey().trim().split(",")));
            }
        }
        return permsSet;
    }

    @Override
    public int deleteRoleByIds(String ids) {
        Long[] roleIds = Convert.toLongArray(ids);
        SysUserRoleExample sysUserRoleExample = new SysUserRoleExample();
        for (Long roleId : roleIds) {
            SysRole role = selectByPrimaryKey(roleId);
            sysUserRoleExample.createCriteria().andRoleIdEqualTo(roleId);
            long count = sysUserRoleMapper.countByExample(sysUserRoleExample);
            if (count > 0) {
                throw new BusinessException(String.format("%1$s已分配,不能删除", role.getRoleName()));
            }
        }

        SysRoleExample sysRoleExample = new SysRoleExample();
        sysRoleExample.createCriteria().andRoleIdIn(Arrays.asList(roleIds));
        return sysRoleMapper.deleteByExample(sysRoleExample);
    }

    /**
     * 批量选择授权用户角色
     *
     * @param roleId  角色ID
     * @param userIds 需要删除的用户数据ID
     * @return 结果
     */
    @Override
    public int insertAuthUsers(Long roleId, String userIds) {
        Long[] users = Convert.toLongArray(userIds);
        // 新增用户与角色管理
        List<SysUserRole> list = new ArrayList<SysUserRole>();
        for (Long userId : users) {
            SysUserRole ur = new SysUserRole();
            ur.setUserId(userId);
            ur.setRoleId(roleId);
            list.add(ur);
        }
        return sysUserRoleMapper.batchUserRole(list);
    }

    /**
     * 批量取消授权用户角色
     *
     * @param roleId  角色ID
     * @param userIds 需要删除的用户数据ID
     * @return 结果
     */
    @Override
    public int deleteAuthUsers(Long roleId, String userIds) {
        return sysUserRoleMapper.deleteUserRoleInfos(roleId, Convert.toLongArray(userIds));
    }

    @Override
    public int deleteAuthUser(SysUserRole userRole) {
        SysUserRoleExample sysUserRoleExample = new SysUserRoleExample();
        sysUserRoleExample.createCriteria().andUserIdEqualTo(userRole.getUserId())
                .andRoleIdEqualTo(userRole.getRoleId());
        return sysUserRoleMapper.deleteByExample(sysUserRoleExample);
    }

    @Override
    public String checkRoleKeyUnique(SysRole role) {
        Long roleId = ObjectUtil.isNull(role.getRoleId()) ? -1L : role.getRoleId();
        SysRoleExample sysRoleExample = new SysRoleExample();
        sysRoleExample.createCriteria().andRoleKeyEqualTo(role.getRoleKey());
        SysRole info = selectFirstByExample(sysRoleExample);
        if (ObjectUtil.isNotNull(info) && info.getRoleId().longValue() != roleId.longValue()) {
            return UserConstants.ROLE_KEY_NOT_UNIQUE;
        }
        return UserConstants.ROLE_KEY_UNIQUE;
    }

    @Override
    public int authDataScope(SysRole role) {
        return 0;
    }

    @Override
    public int updateRole(SysRole role) {
        // 修改角色信息
        sysRoleMapper.updateByPrimaryKeySelective(role);
        // 删除角色与菜单关联
        SysRoleMenuExample sysRoleMenuExample = new SysRoleMenuExample();
        sysRoleMenuExample.createCriteria().andRoleIdEqualTo(role.getRoleId());
        sysRoleMenuMapper.deleteByExample(sysRoleMenuExample);
        return insertRoleMenu(role);
    }

    @Override
    public List<SysRole> selectRolesByUserId(Long userId) {
        List<SysRole> userRoles = sysRoleMapper.selectRolesByUserId(userId);
        List<SysRole> roles = selectByExample(new SysRoleExample());
        for (SysRole role : roles) {
            for (SysRole userRole : userRoles) {
                if (role.getRoleId().longValue() == userRole.getRoleId().longValue()) {
                    role.setFlag(true);
                    break;
                }
            }
        }
        return roles;
    }

    @Override
    public String checkRoleNameUnique(SysRole role) {
        Long roleId = ObjectUtil.isNull(role.getRoleId()) ? -1L : role.getRoleId();
        SysRoleExample sysRoleExample = new SysRoleExample();
        sysRoleExample.createCriteria().andRoleNameEqualTo(role.getRoleName());
        SysRole info = selectFirstByExample(sysRoleExample);
        if (ObjectUtil.isNotNull(info) && info.getRoleId().longValue() != roleId.longValue()) {
            return UserConstants.ROLE_NAME_NOT_UNIQUE;
        }
        return UserConstants.ROLE_NAME_UNIQUE;
    }

    private int insertRoleMenu(SysRole role) {
        int rows = 1;
        // 新增用户与角色管理
        List<SysRoleMenu> list = new ArrayList<SysRoleMenu>();
        for (Long menuId : role.getMenuIds()) {
            SysRoleMenu rm = new SysRoleMenu();
            rm.setRoleId(role.getRoleId());
            rm.setMenuId(menuId);
            list.add(rm);
        }
        if (list.size() > 0) {
            rows = sysRoleMenuMapper.batchRoleMenu(list);
        }
        return rows;
    }
}