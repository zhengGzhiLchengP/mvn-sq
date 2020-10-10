package com.feiqu.admin.controller.sys;


import cn.hutool.core.util.ObjectUtil;
import com.feiqu.adminFramework.shiro.service.SysPasswordService;
import com.feiqu.adminFramework.util.ShiroUtils;
import com.feiqu.adminFramework.web.base.BaseController;
import com.feiqu.common.annotation.Log;
import com.feiqu.common.base.AjaxResult;
import com.feiqu.common.base.BaseResult;
import com.feiqu.common.constant.UserConstants;
import com.feiqu.common.enums.BusinessType;
import com.feiqu.common.enums.UserStatus;
import com.feiqu.system.model.sysData.SysPostExample;
import com.feiqu.system.model.sysData.SysRoleExample;
import com.feiqu.system.model.sysData.SysUser;
import com.feiqu.system.service.sysData.SysPostService;
import com.feiqu.system.service.sysData.SysRoleService;
import com.feiqu.system.service.sysData.SysUserService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;


/**
 * SysUsercontroller
 * Created by cwd on 2019/6/10.
 */
@Controller
@RequestMapping("/system/user")
public class SysUserController extends BaseController {

    private static Logger logger = LoggerFactory.getLogger(SysUserController.class);
    private String prefix = "system/user";
    @Resource
    private SysUserService sysUserService;
    @Resource
    private SysRoleService roleService;
    @Resource
    private SysPostService postService;
    @Resource
    private SysPasswordService sysPasswordService;

    @RequiresPermissions("system:user:resetPwd")
    @PostMapping("/resetPwd")
    @Log(title = "重置密码", businessType = BusinessType.UPDATE)
    @ResponseBody
    public AjaxResult resetPwdSave(SysUser user) {
        user.setSalt(ShiroUtils.randomSalt());
        user.setPassword(sysPasswordService.encryptPassword(user.getLoginName(), user.getPassword(), user.getSalt()));
        user.setUpdateTime(new Date());
        if (sysUserService.updateByPrimaryKeySelective(user) > 0) {
            if (ShiroUtils.getUserId().equals(user.getUserId())) {
                ShiroUtils.setSysUser(sysUserService.selectUserById(user.getUserId()));
            }
            return success();
        }
        return error();
    }
    @RequiresPermissions("system:user:view")
    @GetMapping()
    public String user()
    {
        return prefix + "/manage";
    }


    /**
     * ajax删除SysUser
     */
    @ResponseBody
    @RequestMapping("/delete")
    public Object delete(@RequestParam Long id) {
        BaseResult result = new BaseResult();
        try {
            sysUserService.deleteByPrimaryKey(id);
        } catch (Exception e) {
            logger.error("error", e);
            result.setCode("1");
        }
        return result;
    }

    /**
     * 新增用户
     */
    @GetMapping("/add")
    public String add(ModelMap mmap) {
        mmap.put("roles", roleService.selectByExample(new SysRoleExample()));
        mmap.put("posts", postService.selectByExample(new SysPostExample()));
        return prefix + "/add";
    }
    /**
     * 校验用户名
     */
    @PostMapping("/checkLoginNameUnique")
    @ResponseBody
    public String checkLoginNameUnique(SysUser user) {
        return sysUserService.checkLoginNameUnique(user.getLoginName());
    }

    /**
     * 校验手机号码
     */
    @PostMapping("/checkPhoneUnique")
    @ResponseBody
    public String checkPhoneUnique(SysUser user) {
        return sysUserService.checkPhoneUnique(user);
    }

    /**
     * 校验email邮箱
     */
    @PostMapping("/checkEmailUnique")
    @ResponseBody
    public String checkEmailUnique(SysUser user) {
        return sysUserService.checkEmailUnique(user);
    }

    /**
     * 新增保存用户
     */
    @RequiresPermissions("system:user:add")
    @Log(title = "用户管理", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(SysUser user) {
        if (ObjectUtil.isNotNull(user.getUserId()) && SysUser.isAdmin(user.getUserId())) {
            return error("不允许修改超级管理员用户");
        }

        if (UserConstants.USER_NAME_NOT_UNIQUE.equals(sysUserService.checkLoginNameUnique(user.getLoginName()))) {
            return error("保存用户'" + user.getLoginName() + "'失败，登录账号已存在");
        }
        user.setSalt(ShiroUtils.randomSalt());
        user.setPassword(sysPasswordService.encryptPassword(user.getLoginName(), user.getPassword(), user.getSalt()));
        user.setCreateBy(ShiroUtils.getLoginName());
        user.setCreateTime(new Date());
        user.setDelFlag(UserStatus.OK.getCode());
        user.setUserType("00");
        return toAjax(sysUserService.insertUser(user));
    }

    /**
     * 修改保存用户
     */
    @RequiresPermissions("system:user:edit")
    @PostMapping("/edit")
    @Log(title = "用户管理", businessType = BusinessType.UPDATE)
    @ResponseBody
    public AjaxResult editSave(SysUser user)
    {
        if (ObjectUtil.isNotNull(user.getUserId()) && SysUser.isAdmin(user.getUserId()))
        {
            return error("不允许修改超级管理员用户");
        }
        user.setUpdateBy(ShiroUtils.getLoginName());
        return toAjax(sysUserService.updateByPrimaryKeySelective(user));
    }

    /**
     * 修改用户
     */
    @GetMapping("/edit/{userId}")
    public String edit(@PathVariable("userId") Long userId, ModelMap mmap) {
        mmap.put("user", sysUserService.selectUserById(userId));
        mmap.put("roles", roleService.selectRolesByUserId(userId));
        mmap.put("posts", postService.selectPostsByUserId(userId));
        return prefix + "/edit";
    }

    /**
     * 查询SysUser首页
     */
    @RequiresPermissions("system:user:list")
    @PostMapping("list")
    @ResponseBody
    public Object list(SysUser user) {
        BaseResult result = new BaseResult();
        try {
            startPage();
            List<SysUser> list = sysUserService.selectUserList(user);
            return getDataTable(list);
        } catch (Exception e) {
            logger.error("error", e);
            result.setCode("1");
        }
        return result;
    }
}