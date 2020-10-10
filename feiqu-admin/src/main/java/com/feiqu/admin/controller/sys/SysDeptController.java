package com.feiqu.admin.controller.sys;


import cn.hutool.core.util.ObjectUtil;
import com.feiqu.adminFramework.util.ShiroUtils;
import com.feiqu.adminFramework.web.base.BaseController;
import com.feiqu.common.annotation.Log;
import com.feiqu.common.base.AjaxResult;
import com.feiqu.common.core.domain.Ztree;
import com.feiqu.common.enums.BusinessType;
import com.feiqu.system.model.sysData.SysDept;
import com.feiqu.system.model.sysData.SysDeptExample;
import com.feiqu.system.model.sysData.SysRole;
import com.feiqu.system.model.sysData.SysUserExample;
import com.feiqu.system.service.sysData.SysDeptService;
import com.feiqu.system.service.sysData.SysUserService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 部门信息
 *
 * @author ruoyi
 */
@Controller
@RequestMapping("/system/dept")
public class SysDeptController extends BaseController {
    private String prefix = "system/dept";

    @Resource
    private SysDeptService deptService;
    @Resource
    private SysUserService sysUserService;

    @RequiresPermissions("system:dept:view")
    @GetMapping()
    public String dept() {
        return prefix + "/dept";
    }

    @RequiresPermissions("system:dept:list")
    @GetMapping("/list")
    @ResponseBody
    public List<SysDept> list(SysDept dept) {
        List<SysDept> deptList = deptService.selectByExample(new SysDeptExample());
        return deptList;
    }

    /**
     * 新增部门
     */
    @GetMapping("/add/{parentId}")
    public String add(@PathVariable("parentId") Long parentId, ModelMap mmap) {
        mmap.put("dept", deptService.selectByPrimaryKey(parentId));
        return prefix + "/add";
    }

    /**
     * 新增保存部门
     */
    @RequiresPermissions("system:dept:add")
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(SysDept dept) {
        dept.setCreateBy(ShiroUtils.getLoginName());
        dept.setDelFlag("0");
        dept.setStatus("0");
        dept.setCreateTime(new Date());
        return toAjax(deptService.insert(dept));
    }

    /**
     * 修改
     */
    @GetMapping("/edit/{deptId}")
    public String edit(@PathVariable("deptId") Long deptId, ModelMap mmap) {
        SysDept dept = deptService.selectByPrimaryKey(deptId);
        if (ObjectUtil.isNotNull(dept) && 100L == deptId) {
            dept.setParentName("无");
        }
        mmap.put("dept", dept);
        return prefix + "/edit";
    }

    /**
     * 保存
     */
    @Log(title = "部门管理", businessType = BusinessType.UPDATE)
    @RequiresPermissions("system:dept:edit")
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(SysDept dept) {
        dept.setUpdateBy(ShiroUtils.getLoginName());
        dept.setUpdateTime(new Date());
        return toAjax(deptService.updateByPrimaryKeySelective(dept));
    }

    /**
     * 删除
     */
    @Log(title = "部门管理", businessType = BusinessType.DELETE)
    @RequiresPermissions("system:dept:remove")
    @GetMapping("/remove/{deptId}")
    @ResponseBody
    public AjaxResult remove(@PathVariable("deptId") Long deptId) {
        SysDeptExample sysDeptExample = new SysDeptExample();
        sysDeptExample.createCriteria().andParentIdEqualTo(deptId);
        if (deptService.countByExample(sysDeptExample) > 0) {
            return AjaxResult.warn("存在下级部门,不允许删除");
        }
        SysUserExample sysUserExample = new SysUserExample();
        sysUserExample.createCriteria().andDeptIdEqualTo(deptId);
        if (sysUserService.countByExample(sysUserExample) > 0) {
            return AjaxResult.warn("部门存在用户,不允许删除");
        }
        return toAjax(deptService.deleteByPrimaryKey(deptId));
    }

    /**
     * 校验部门名称
     */
    @PostMapping("/checkDeptNameUnique")
    @ResponseBody
    public String checkDeptNameUnique(SysDept dept) {
        return deptService.checkDeptNameUnique(dept);
    }

    /**
     * 选择部门树
     */
    @GetMapping("/selectDeptTree/{deptId}")
    public String selectDeptTree(@PathVariable("deptId") Long deptId, ModelMap mmap) {
        mmap.put("dept", deptService.selectByPrimaryKey(deptId));
        return prefix + "/tree";
    }

    /**
     * 加载部门列表树
     */
    @GetMapping("/treeData")
    @ResponseBody
    public List<Ztree> treeData() {
        List<Ztree> ztrees = deptService.selectDeptTree(new SysDept());
        return ztrees;
    }

    /**
     * 加载角色部门（数据权限）列表树
     */
    @GetMapping("/roleDeptTreeData")
    @ResponseBody
    public List<Ztree> deptTreeData(SysRole role) {
        List<Ztree> ztrees = deptService.roleDeptTreeData(role);
        return ztrees;
    }
}
