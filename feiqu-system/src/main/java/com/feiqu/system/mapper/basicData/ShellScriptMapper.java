package com.feiqu.system.mapper.basicData;

import com.feiqu.system.model.basicData.ShellScript;
import com.feiqu.system.model.basicData.ShellScriptExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ShellScriptMapper {
    long countByExample(ShellScriptExample example);

    int deleteByExample(ShellScriptExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(ShellScript record);

    int insertSelective(ShellScript record);

    List<ShellScript> selectByExampleWithBLOBs(ShellScriptExample example);

    List<ShellScript> selectByExample(ShellScriptExample example);

    ShellScript selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") ShellScript record, @Param("example") ShellScriptExample example);

    int updateByExampleWithBLOBs(@Param("record") ShellScript record, @Param("example") ShellScriptExample example);

    int updateByExample(@Param("record") ShellScript record, @Param("example") ShellScriptExample example);

    int updateByPrimaryKeySelective(ShellScript record);

    int updateByPrimaryKeyWithBLOBs(ShellScript record);

    int updateByPrimaryKey(ShellScript record);
}