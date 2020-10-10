package com.feiqu.system.mapper.mainData;

import com.feiqu.system.model.FqChangeLogCollect;
import com.feiqu.system.model.FqChangeLogCollectExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface FqChangeLogCollectMapper {
    long countByExample(FqChangeLogCollectExample example);

    int deleteByExample(FqChangeLogCollectExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(FqChangeLogCollect record);

    int insertSelective(FqChangeLogCollect record);

    List<FqChangeLogCollect> selectByExampleWithBLOBs(FqChangeLogCollectExample example);

    List<FqChangeLogCollect> selectByExample(FqChangeLogCollectExample example);

    FqChangeLogCollect selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") FqChangeLogCollect record, @Param("example") FqChangeLogCollectExample example);

    int updateByExampleWithBLOBs(@Param("record") FqChangeLogCollect record, @Param("example") FqChangeLogCollectExample example);

    int updateByExample(@Param("record") FqChangeLogCollect record, @Param("example") FqChangeLogCollectExample example);

    int updateByPrimaryKeySelective(FqChangeLogCollect record);

    int updateByPrimaryKeyWithBLOBs(FqChangeLogCollect record);

    int updateByPrimaryKey(FqChangeLogCollect record);
}