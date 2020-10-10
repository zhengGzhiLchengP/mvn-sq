package com.feiqu.system.mapper.mainData;

import com.feiqu.system.model.FqLolita;
import com.feiqu.system.model.FqLolitaExample;
import com.feiqu.system.pojo.condition.CommonCondition;
import com.feiqu.system.pojo.response.FqLolitaDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface FqLolitaMapper {
    long countByExample(FqLolitaExample example);

    int deleteByExample(FqLolitaExample example);

    int deleteByPrimaryKey(Long id);

    int insert(FqLolita record);

    int insertSelective(FqLolita record);

    List<FqLolita> selectByExample(FqLolitaExample example);

    FqLolita selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") FqLolita record, @Param("example") FqLolitaExample example);

    int updateByExample(@Param("record") FqLolita record, @Param("example") FqLolitaExample example);

    int updateByPrimaryKeySelective(FqLolita record);

    int updateByPrimaryKey(FqLolita record);

    List<FqLolitaDTO> selectWithUser(CommonCondition condition);
}