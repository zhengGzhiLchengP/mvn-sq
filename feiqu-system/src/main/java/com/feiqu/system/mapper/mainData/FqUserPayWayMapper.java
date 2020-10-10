package com.feiqu.system.mapper.mainData;

import com.feiqu.system.model.FqUserPayWay;
import com.feiqu.system.model.FqUserPayWayExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface FqUserPayWayMapper {
    long countByExample(FqUserPayWayExample example);

    int deleteByExample(FqUserPayWayExample example);

    int deleteByPrimaryKey(Long id);

    int insert(FqUserPayWay record);

    int insertSelective(FqUserPayWay record);

    List<FqUserPayWay> selectByExample(FqUserPayWayExample example);

    FqUserPayWay selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") FqUserPayWay record, @Param("example") FqUserPayWayExample example);

    int updateByExample(@Param("record") FqUserPayWay record, @Param("example") FqUserPayWayExample example);

    int updateByPrimaryKeySelective(FqUserPayWay record);

    int updateByPrimaryKey(FqUserPayWay record);
}