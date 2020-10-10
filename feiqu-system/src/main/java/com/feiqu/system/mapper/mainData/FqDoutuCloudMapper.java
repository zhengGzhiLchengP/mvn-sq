package com.feiqu.system.mapper.mainData;

import com.feiqu.system.model.FqDoutuCloud;
import com.feiqu.system.model.FqDoutuCloudExample;
import com.feiqu.system.pojo.condition.CommonCondition;
import com.feiqu.system.pojo.response.FqDoutuCloudWithUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface FqDoutuCloudMapper {
    long countByExample(FqDoutuCloudExample example);

    int deleteByExample(FqDoutuCloudExample example);

    int deleteByPrimaryKey(Long id);

    int insert(FqDoutuCloud record);

    int insertSelective(FqDoutuCloud record);

    List<FqDoutuCloud> selectByExample(FqDoutuCloudExample example);

    FqDoutuCloud selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") FqDoutuCloud record, @Param("example") FqDoutuCloudExample example);

    int updateByExample(@Param("record") FqDoutuCloud record, @Param("example") FqDoutuCloudExample example);

    int updateByPrimaryKeySelective(FqDoutuCloud record);

    int updateByPrimaryKey(FqDoutuCloud record);

    List<FqDoutuCloudWithUser> selectWithUser(CommonCondition commonCondition);
}