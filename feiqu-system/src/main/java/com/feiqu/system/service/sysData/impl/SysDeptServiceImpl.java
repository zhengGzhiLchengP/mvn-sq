package com.feiqu.system.service.sysData.impl;

import cn.hutool.core.util.ObjectUtil;
import com.feiqu.common.constant.UserConstants;
import com.feiqu.common.core.domain.Ztree;
import com.feiqu.system.base.BaseServiceImpl;
import com.feiqu.system.annotation.BaseService;
import com.feiqu.system.mapper.sysData.SysDeptMapper;
import com.feiqu.system.model.sysData.SysDept;
import com.feiqu.system.model.sysData.SysDeptExample;
import com.feiqu.system.model.sysData.SysRole;
import com.feiqu.system.service.sysData.SysDeptService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
* SysDeptService实现
* Created by cwd on 2019/6/10.
*/
@Service
@Transactional
@BaseService
public class SysDeptServiceImpl extends BaseServiceImpl<SysDeptMapper, SysDept, SysDeptExample> implements SysDeptService {

    private static Logger _log = LoggerFactory.getLogger(SysDeptServiceImpl.class);

    @Resource
    SysDeptMapper sysDeptMapper;

    @Override
    public List<Ztree> selectDeptTree(SysDept sysDept) {
        List<SysDept> deptList = sysDeptMapper.selectDeptList(sysDept);
        List<Ztree> ztrees = initZtree(deptList);
        return ztrees;
    }

    @Override
    public List<Ztree> roleDeptTreeData(SysRole role) {
        Long roleId = role.getRoleId();
        List<Ztree> ztrees = new ArrayList<Ztree>();
        List<SysDept> deptList = selectByExample(new SysDeptExample());
        if (ObjectUtil.isNotNull(roleId)) {
            List<String> roleDeptList = sysDeptMapper.selectRoleDeptTree(roleId);
            ztrees = initZtree(deptList, roleDeptList);
        } else {
            ztrees = initZtree(deptList);
        }
        return ztrees;
    }

    @Override
    public String checkDeptNameUnique(SysDept dept) {
        Long deptId = ObjectUtil.isNull(dept.getDeptId()) ? -1L : dept.getDeptId();
        SysDeptExample sysDeptExample = new SysDeptExample();
        sysDeptExample.createCriteria().andParentIdEqualTo(dept.getParentId())
                .andDeptNameEqualTo(dept.getDeptName());
        SysDept info = selectFirstByExample(sysDeptExample);
        if (ObjectUtil.isNotNull(info) && info.getDeptId().longValue() != deptId.longValue()) {
            return UserConstants.DEPT_NAME_NOT_UNIQUE;
        }
        return UserConstants.DEPT_NAME_UNIQUE;
    }

    /**
     * 对象转部门树
     *
     * @param deptList 部门列表
     * @return 树结构列表
     */
    public List<Ztree> initZtree(List<SysDept> deptList) {
        return initZtree(deptList, null);
    }

    /**
     * 对象转部门树
     *
     * @param deptList     部门列表
     * @param roleDeptList 角色已存在菜单列表
     * @return 树结构列表
     */
    public List<Ztree> initZtree(List<SysDept> deptList, List<String> roleDeptList) {

        List<Ztree> ztrees = new ArrayList<Ztree>();
        boolean isCheck = ObjectUtil.isNotNull(roleDeptList);
        for (SysDept dept : deptList) {
            if (UserConstants.DEPT_NORMAL.equals(dept.getStatus())) {
                Ztree ztree = new Ztree();
                ztree.setId(dept.getDeptId());
                ztree.setpId(dept.getParentId());
                ztree.setName(dept.getDeptName());
                ztree.setTitle(dept.getDeptName());
                if (isCheck) {
                    ztree.setChecked(roleDeptList.contains(dept.getDeptId() + dept.getDeptName()));
                }
                ztrees.add(ztree);
            }
        }
        return ztrees;
    }
}