package com.feiqu.system.mapper.collectData;

import com.feiqu.system.model.collectData.Soul;
import com.feiqu.system.model.collectData.SoulExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface SoulMapper {
    long countByExample(SoulExample example);

    int deleteByExample(SoulExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Soul record);

    int insertSelective(Soul record);

    List<Soul> selectByExample(SoulExample example);

    Soul selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Soul record, @Param("example") SoulExample example);

    int updateByExample(@Param("record") Soul record, @Param("example") SoulExample example);

    int updateByPrimaryKeySelective(Soul record);

    int updateByPrimaryKey(Soul record);
}