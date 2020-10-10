package com.feiqu.system.mapper.basicData;

import com.feiqu.system.model.basicData.Corporation;
import com.feiqu.system.model.basicData.CorporationExample;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface CorporationMapper {
    long countByExample(CorporationExample example);

    int deleteByExample(CorporationExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Corporation record);

    int insertSelective(Corporation record);

    List<Corporation> selectByExample(CorporationExample example);

    Corporation selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Corporation record, @Param("example") CorporationExample example);

    int updateByExample(@Param("record") Corporation record, @Param("example") CorporationExample example);

    int updateByPrimaryKeySelective(Corporation record);

    int updateByPrimaryKey(Corporation record);
}