package com.feiqu.system.mapper.mainData;

import com.feiqu.system.model.FqUserActiveNum;
import com.feiqu.system.model.FqUserActiveNumExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface FqUserActiveNumMapper {
    long countByExample(FqUserActiveNumExample example);

    int deleteByExample(FqUserActiveNumExample example);

    int insert(FqUserActiveNum record);

    int insertSelective(FqUserActiveNum record);

    List<FqUserActiveNum> selectByExample(FqUserActiveNumExample example);

    int updateByExampleSelective(@Param("record") FqUserActiveNum record, @Param("example") FqUserActiveNumExample example);

    int updateByExample(@Param("record") FqUserActiveNum record, @Param("example") FqUserActiveNumExample example);
}