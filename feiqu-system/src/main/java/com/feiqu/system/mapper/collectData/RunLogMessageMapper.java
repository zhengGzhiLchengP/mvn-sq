package com.feiqu.system.mapper.collectData;

import com.feiqu.system.model.collectData.RunLogMessage;
import com.feiqu.system.model.collectData.RunLogMessageExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RunLogMessageMapper {
    long countByExample(RunLogMessageExample example);

    int deleteByExample(RunLogMessageExample example);

    int deleteByPrimaryKey(Long id);

    int insert(RunLogMessage record);

    int insertSelective(RunLogMessage record);

    List<RunLogMessage> selectByExampleWithBLOBs(RunLogMessageExample example);

    List<RunLogMessage> selectByExample(RunLogMessageExample example);

    RunLogMessage selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") RunLogMessage record, @Param("example") RunLogMessageExample example);

    int updateByExampleWithBLOBs(@Param("record") RunLogMessage record, @Param("example") RunLogMessageExample example);

    int updateByExample(@Param("record") RunLogMessage record, @Param("example") RunLogMessageExample example);

    int updateByPrimaryKeySelective(RunLogMessage record);

    int updateByPrimaryKeyWithBLOBs(RunLogMessage record);

    int updateByPrimaryKey(RunLogMessage record);
}