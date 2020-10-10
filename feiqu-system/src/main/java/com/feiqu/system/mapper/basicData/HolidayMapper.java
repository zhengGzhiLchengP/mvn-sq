package com.feiqu.system.mapper.basicData;

import com.feiqu.system.model.basicData.Holiday;
import com.feiqu.system.model.basicData.HolidayExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface HolidayMapper {
    long countByExample(HolidayExample example);

    int deleteByExample(HolidayExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Holiday record);

    int insertSelective(Holiday record);

    List<Holiday> selectByExample(HolidayExample example);

    Holiday selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Holiday record, @Param("example") HolidayExample example);

    int updateByExample(@Param("record") Holiday record, @Param("example") HolidayExample example);

    int updateByPrimaryKeySelective(Holiday record);

    int updateByPrimaryKey(Holiday record);
}