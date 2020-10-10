package com.feiqu.system.mapper.basicData;

import com.feiqu.system.model.basicData.WsLog;
import com.feiqu.system.model.basicData.WsLogExample;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface WsLogMapper {
    long countByExample(WsLogExample example);

    int deleteByExample(WsLogExample example);

    int deleteByPrimaryKey(Long id);

    int insert(WsLog record);

    int insertSelective(WsLog record);

    List<WsLog> selectByExample(WsLogExample example);

    WsLog selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") WsLog record, @Param("example") WsLogExample example);

    int updateByExample(@Param("record") WsLog record, @Param("example") WsLogExample example);

    int updateByPrimaryKeySelective(WsLog record);

    int updateByPrimaryKey(WsLog record);
}