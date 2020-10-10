package com.feiqu.system.mapper.sysData;

import com.feiqu.system.model.sysData.SysMenu;
import com.feiqu.system.model.sysData.SysMenuExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface SysMenuMapper {
    long countByExample(SysMenuExample example);

    int deleteByExample(SysMenuExample example);

    int deleteByPrimaryKey(Long menuId);

    int insert(SysMenu record);

    int insertSelective(SysMenu record);

    List<SysMenu> selectByExample(SysMenuExample example);

    SysMenu selectByPrimaryKey(Long menuId);

    int updateByExampleSelective(@Param("record") SysMenu record, @Param("example") SysMenuExample example);

    int updateByExample(@Param("record") SysMenu record, @Param("example") SysMenuExample example);

    int updateByPrimaryKeySelective(SysMenu record);

    int updateByPrimaryKey(SysMenu record);

    List<String> selectPermsByUserId(Long userId);

    List<SysMenu> selectMenuNormalAll();

    List<SysMenu> selectMenusByUserId(Long userId);

    List<String> selectMenuTree(Long roleId);

    int deleteMenuById(Long menuId);
}