package com.feiqu.system.mapper.collectData;

import com.feiqu.system.model.collectData.LogData;
import com.feiqu.system.model.collectData.LogDataExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface LogDataMapper {
    long countByExample(LogDataExample example);

    int deleteByExample(LogDataExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(LogData record);

    int insertSelective(LogData record);

    List<LogData> selectByExampleWithBLOBs(LogDataExample example);

    List<LogData> selectByExample(LogDataExample example);

    LogData selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") LogData record, @Param("example") LogDataExample example);

    int updateByExampleWithBLOBs(@Param("record") LogData record, @Param("example") LogDataExample example);

    int updateByExample(@Param("record") LogData record, @Param("example") LogDataExample example);

    int updateByPrimaryKeySelective(LogData record);

    int updateByPrimaryKeyWithBLOBs(LogData record);

    int updateByPrimaryKey(LogData record);
}