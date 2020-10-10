package com.feiqu.system.mapper.mainData;

import com.feiqu.system.model.mainData.FqMenu;
import com.feiqu.system.model.mainData.FqMenuExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface FqMenuMapper {
    long countByExample(FqMenuExample example);

    int deleteByExample(FqMenuExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(FqMenu record);

    int insertSelective(FqMenu record);

    List<FqMenu> selectByExample(FqMenuExample example);

    FqMenu selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") FqMenu record, @Param("example") FqMenuExample example);

    int updateByExample(@Param("record") FqMenu record, @Param("example") FqMenuExample example);

    int updateByPrimaryKeySelective(FqMenu record);

    int updateByPrimaryKey(FqMenu record);
}