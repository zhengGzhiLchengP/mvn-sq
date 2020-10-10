package com.feiqu.system.service.sysData.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.feiqu.common.constant.UserConstants;
import com.feiqu.common.core.domain.Ztree;
import com.feiqu.system.base.BaseServiceImpl;
import com.feiqu.system.annotation.BaseService;
import com.feiqu.system.mapper.sysData.SysMenuMapper;
import com.feiqu.system.mapper.sysData.SysRoleMenuMapper;
import com.feiqu.system.model.sysData.*;
import com.feiqu.system.service.sysData.SysMenuService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

/**
* SysMenuService实现
* Created by cwd on 2019/6/10.
*/
@Service
@Transactional
@BaseService
public class SysMenuServiceImpl extends BaseServiceImpl<SysMenuMapper, SysMenu, SysMenuExample> implements SysMenuService {

    private static Logger _log = LoggerFactory.getLogger(SysMenuServiceImpl.class);

    @Resource
    SysMenuMapper sysMenuMapper;

    @Resource
    private SysRoleMenuMapper roleMenuMapper;

    @Override
    public Set<String> selectPermsByUserId(Long userId) {
        List<String> perms = sysMenuMapper.selectPermsByUserId(userId);
        Set<String> permsSet = new HashSet<>();
        for (String perm : perms)
        {
            if (StringUtils.isNotEmpty(perm))
            {
                permsSet.addAll(Arrays.asList(perm.trim().split(",")));
            }
        }
        return permsSet;
    }

    @Override
    public List<SysMenu> selectMenusByUser(SysUser user) {
        List<SysMenu> menus = new LinkedList<SysMenu>();
        // 管理员显示所有菜单信息
        if (user.isAdmin())
        {
            menus = sysMenuMapper.selectMenuNormalAll();
        }
        else
        {
            menus = sysMenuMapper.selectMenusByUserId(user.getUserId());
        }
        return getChildPerms(menus, 0);
    }

    @Override
    public String checkMenuNameUnique(SysMenu menu) {
        Long menuId = ObjectUtil.isNull(menu.getMenuId()) ? -1L : menu.getMenuId();
        SysMenuExample sysMenuExample = new SysMenuExample();
        sysMenuExample.createCriteria().andMenuNameEqualTo(menu.getMenuName())
                .andParentIdEqualTo(menu.getParentId());
        List<SysMenu> infos = sysMenuMapper.selectByExample(sysMenuExample);
        if(CollectionUtil.isNotEmpty(infos)){
            SysMenu info = infos.get(0);
            if (ObjectUtil.isNotNull(info) && info.getMenuId().longValue() != menuId.longValue())
            {
                return UserConstants.MENU_NAME_NOT_UNIQUE;
            }
        }
        return UserConstants.MENU_NAME_UNIQUE;
    }

    @Override
    public List<Ztree> roleMenuTreeData(SysRole role) {
        Long roleId = role.getRoleId();
        List<Ztree> ztrees = new ArrayList<Ztree>();
        SysMenuExample sysMenuExample = new SysMenuExample();
        List<SysMenu> menuList = sysMenuMapper.selectByExample(sysMenuExample);
        if (ObjectUtil.isNotNull(roleId))
        {
            List<String> roleMenuList = sysMenuMapper.selectMenuTree(roleId);
            ztrees = initZtree(menuList, roleMenuList, true);
        }
        else
        {
            ztrees = initZtree(menuList, null, true);
        }
        return ztrees;
    }

    @Override
    public List<Ztree> menuTreeData() {
        List<SysMenu> menuList = sysMenuMapper.selectByExample(new SysMenuExample());
        List<Ztree> ztrees = initZtree(menuList);
        return ztrees;
    }

    @Override
    public long selectCountRoleMenuByMenuId(Long menuId) {
        SysRoleMenuExample sysRoleMenuExample = new SysRoleMenuExample();
        sysRoleMenuExample.createCriteria().andMenuIdEqualTo(menuId);
        return roleMenuMapper.countByExample(sysRoleMenuExample);
    }

    @Override
    public int deleteById(Long menuId) {
        return sysMenuMapper.deleteMenuById(menuId);
    }

    /**
     * 对象转菜单树
     *
     * @param menuList 菜单列表
     * @return 树结构列表
     */
    public List<Ztree> initZtree(List<SysMenu> menuList)
    {
        return initZtree(menuList, null, false);
    }

    /**
     * 对象转菜单树
     *
     * @param menuList 菜单列表
     * @param roleMenuList 角色已存在菜单列表
     * @param permsFlag 是否需要显示权限标识
     * @return 树结构列表
     */
    public List<Ztree> initZtree(List<SysMenu> menuList, List<String> roleMenuList, boolean permsFlag)
    {
        List<Ztree> ztrees = new ArrayList<Ztree>();
        boolean isCheck = ObjectUtil.isNotNull(roleMenuList);
        for (SysMenu menu : menuList)
        {
            Ztree ztree = new Ztree();
            ztree.setId(menu.getMenuId());
            ztree.setpId(menu.getParentId());
            ztree.setName(transMenuName(menu, roleMenuList, permsFlag));
            ztree.setTitle(menu.getMenuName());
            if (isCheck)
            {
                ztree.setChecked(roleMenuList.contains(menu.getMenuId() + menu.getPerms()));
            }
            ztrees.add(ztree);
        }
        return ztrees;
    }

    public String transMenuName(SysMenu menu, List<String> roleMenuList, boolean permsFlag)
    {
        StringBuffer sb = new StringBuffer();
        sb.append(menu.getMenuName());
        if (permsFlag)
        {
            sb.append("<font color=\"#888\">&nbsp;&nbsp;&nbsp;" + menu.getPerms() + "</font>");
        }
        return sb.toString();
    }

    /**
     * 根据父节点的ID获取所有子节点
     *
     * @param list 分类表
     * @param parentId 传入的父节点ID
     * @return String
     */
    public List<SysMenu> getChildPerms(List<SysMenu> list, int parentId)
    {
        List<SysMenu> returnList = new ArrayList<SysMenu>();
        for (Iterator<SysMenu> iterator = list.iterator(); iterator.hasNext();)
        {
            SysMenu t = (SysMenu) iterator.next();
            // 一、根据传入的某个父节点ID,遍历该父节点的所有子节点
            if (t.getParentId() == parentId)
            {
                recursionFn(list, t);
                returnList.add(t);
            }
        }
        return returnList;
    }

    /**
     * 递归列表
     *
     * @param list
     * @param t
     */
    private void recursionFn(List<SysMenu> list, SysMenu t)
    {
        // 得到子节点列表
        List<SysMenu> childList = getChildList(list, t);
        t.setChildren(childList);
        for (SysMenu tChild : childList)
        {
            if (hasChild(list, tChild))
            {
                // 判断是否有子节点
                Iterator<SysMenu> it = childList.iterator();
                while (it.hasNext())
                {
                    SysMenu n = (SysMenu) it.next();
                    recursionFn(list, n);
                }
            }
        }
    }

    /**
     * 得到子节点列表
     */
    private List<SysMenu> getChildList(List<SysMenu> list, SysMenu t)
    {
        List<SysMenu> tlist = new ArrayList<SysMenu>();
        Iterator<SysMenu> it = list.iterator();
        while (it.hasNext())
        {
            SysMenu n = (SysMenu) it.next();
            if (n.getParentId().longValue() == t.getMenuId().longValue())
            {
                tlist.add(n);
            }
        }
        return tlist;
    }

    /**
     * 判断是否有子节点
     */
    private boolean hasChild(List<SysMenu> list, SysMenu t)
    {
        return getChildList(list, t).size() > 0 ? true : false;
    }
}