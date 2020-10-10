package com.feiqu.system.mapper.mainData;

import com.feiqu.system.model.FqGoodPic;
import com.feiqu.system.model.FqGoodPicExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface FqGoodPicMapper {
    long countByExample(FqGoodPicExample example);

    int deleteByExample(FqGoodPicExample example);

    int deleteByPrimaryKey(Long id);

    int insert(FqGoodPic record);

    int insertSelective(FqGoodPic record);

    List<FqGoodPic> selectByExample(FqGoodPicExample example);

    FqGoodPic selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") FqGoodPic record, @Param("example") FqGoodPicExample example);

    int updateByExample(@Param("record") FqGoodPic record, @Param("example") FqGoodPicExample example);

    int updateByPrimaryKeySelective(FqGoodPic record);

    int updateByPrimaryKey(FqGoodPic record);
}