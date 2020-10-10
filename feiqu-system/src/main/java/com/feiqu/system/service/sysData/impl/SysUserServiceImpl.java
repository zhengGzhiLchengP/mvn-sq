package com.feiqu.system.service.sysData.impl;

import cn.hutool.core.util.ObjectUtil;
import com.feiqu.common.constant.UserConstants;
import com.feiqu.system.base.BaseServiceImpl;
import com.feiqu.system.annotation.BaseService;
import com.feiqu.system.mapper.sysData.*;
import com.feiqu.system.model.sysData.*;
import com.feiqu.system.service.sysData.SysUserService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
* SysUserService实现
* Created by cwd on 2019/6/10.
*/
@Service
@Transactional
@BaseService
public class SysUserServiceImpl extends BaseServiceImpl<SysUserMapper, SysUser, SysUserExample> implements SysUserService {

    private static Logger _log = LoggerFactory.getLogger(SysUserServiceImpl.class);

    @Resource
    SysUserMapper sysUserMapper;
    @Resource
    private SysRoleMapper roleMapper;
    @Resource
    private SysPostMapper postMapper;
    @Resource
    private SysUserPostMapper userPostMapper;
    @Resource
    private SysUserRoleMapper userRoleMapper;
    @Override
    public SysUser selectUserByLoginName(String username) {
        return sysUserMapper.selectUserByLoginName(username);
    }

    @Override
    public SysUser selectUserByPhoneNumber(String username) {
        return sysUserMapper.selectUserByPhoneNumber(username);
    }

    @Override
    public SysUser selectUserByEmail(String username) {
        return sysUserMapper.selectUserByEmail(username);
    }

    @Override
    public String checkLoginNameUnique(String loginName) {
        SysUserExample sysUserExample = new SysUserExample();
        sysUserExample.createCriteria().andLoginNameEqualTo(loginName);
        long count = countByExample(sysUserExample);
        if (count > 0)
        {
            return UserConstants.USER_NAME_NOT_UNIQUE;
        }
        return UserConstants.USER_NAME_UNIQUE;
    }

    @Override
    public List<SysUser> selectUnallocatedList(SysUser user) {
        return sysUserMapper.selectUnallocatedList(user);
    }

    @Override
    public List<SysUser> selectAllocatedList(SysUser user) {
        return sysUserMapper.selectAllocatedList(user);
    }

    @Override
    public String checkPhoneUnique(SysUser user) {
        Long userId = ObjectUtil.isNull(user.getUserId()) ? -1L : user.getUserId();
        SysUserExample sysUserExample = new SysUserExample();
        sysUserExample.createCriteria().andPhonenumberEqualTo(user.getPhonenumber());
        SysUser info = selectFirstByExample(sysUserExample);
        if (ObjectUtil.isNotNull(info) && info.getUserId().longValue() != userId.longValue()) {
            return UserConstants.USER_PHONE_NOT_UNIQUE;
        }
        return UserConstants.USER_PHONE_UNIQUE;
    }

    @Override
    public String checkEmailUnique(SysUser user) {
        Long userId = ObjectUtil.isNull(user.getUserId()) ? -1L : user.getUserId();
        SysUserExample sysUserExample = new SysUserExample();
        sysUserExample.createCriteria().andEmailEqualTo(user.getEmail());
        SysUser info = selectFirstByExample(sysUserExample);
        if (ObjectUtil.isNotNull(info) && info.getUserId().longValue() != userId.longValue()) {
            return UserConstants.USER_EMAIL_NOT_UNIQUE;
        }
        return UserConstants.USER_EMAIL_UNIQUE;
    }

    @Override
    public String selectUserRoleGroup(Long userId) {
        List<SysRole> list = roleMapper.selectRolesByUserId(userId);
        StringBuffer idsStr = new StringBuffer();
        for (SysRole role : list) {
            idsStr.append(role.getRoleName()).append(",");
        }
        if (StringUtils.isNotEmpty(idsStr.toString())) {
            return idsStr.substring(0, idsStr.length() - 1);
        }
        return idsStr.toString();
    }

    @Override
    public String selectUserPostGroup(Long userId) {
        List<SysPost> list = postMapper.selectPostsByUserId(userId);
        StringBuffer idsStr = new StringBuffer();
        for (SysPost post : list) {
            idsStr.append(post.getPostName()).append(",");
        }
        if (StringUtils.isNotEmpty(idsStr.toString())) {
            return idsStr.substring(0, idsStr.length() - 1);
        }
        return idsStr.toString();
    }

    @Override
    public int insertUser(SysUser user) {
        // 新增用户信息
        int rows = sysUserMapper.insert(user);
        // 新增用户岗位关联
        insertUserPost(user);
        // 新增用户与角色管理
        insertUserRole(user);
        return rows;
    }

    @Override
    public List<SysUser> selectUserList(SysUser user) {
        return sysUserMapper.selectUserList(user);
    }

    @Override
    public SysUser selectUserById(Long userId) {
        return sysUserMapper.selectUserById(userId);
    }

    private void insertUserRole(SysUser user) {
        Long[] roles = user.getRoleIds();
        if (ObjectUtil.isNotNull(roles)) {
            // 新增用户与角色管理
            List<SysUserRole> list = new ArrayList<SysUserRole>();
            for (Long roleId : roles) {
                SysUserRole ur = new SysUserRole();
                ur.setUserId(user.getUserId());
                ur.setRoleId(roleId);
                list.add(ur);
            }
            if (list.size() > 0) {
                userRoleMapper.batchUserRole(list);
            }
        }
    }

    private void insertUserPost(SysUser user) {
        Long[] posts = user.getPostIds();
        if (ObjectUtil.isNotNull(posts)) {
            // 新增用户与岗位管理
            List<SysUserPost> list = new ArrayList<SysUserPost>();
            for (Long postId : posts) {
                SysUserPost up = new SysUserPost();
                up.setUserId(user.getUserId());
                up.setPostId(postId);
                list.add(up);
            }
            if (list.size() > 0) {
                userPostMapper.batchUserPost(list);
            }
        }
    }
}