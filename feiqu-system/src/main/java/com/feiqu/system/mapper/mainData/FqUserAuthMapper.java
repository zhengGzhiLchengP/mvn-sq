package com.feiqu.system.mapper.mainData;

import com.feiqu.system.model.FqUserAuth;
import com.feiqu.system.model.FqUserAuthExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface FqUserAuthMapper {
    long countByExample(FqUserAuthExample example);

    int deleteByExample(FqUserAuthExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(FqUserAuth record);

    int insertSelective(FqUserAuth record);

    List<FqUserAuth> selectByExample(FqUserAuthExample example);

    FqUserAuth selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") FqUserAuth record, @Param("example") FqUserAuthExample example);

    int updateByExample(@Param("record") FqUserAuth record, @Param("example") FqUserAuthExample example);

    int updateByPrimaryKeySelective(FqUserAuth record);

    int updateByPrimaryKey(FqUserAuth record);
}