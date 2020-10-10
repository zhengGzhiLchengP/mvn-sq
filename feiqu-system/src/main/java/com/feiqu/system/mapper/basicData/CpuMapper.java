package com.feiqu.system.mapper.basicData;

import com.feiqu.system.model.basicData.Cpu;
import com.feiqu.system.model.basicData.CpuExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CpuMapper {
    long countByExample(CpuExample example);

    int deleteByExample(CpuExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Cpu record);

    int insertSelective(Cpu record);

    List<Cpu> selectByExample(CpuExample example);

    Cpu selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Cpu record, @Param("example") CpuExample example);

    int updateByExample(@Param("record") Cpu record, @Param("example") CpuExample example);

    int updateByPrimaryKeySelective(Cpu record);

    int updateByPrimaryKey(Cpu record);
}