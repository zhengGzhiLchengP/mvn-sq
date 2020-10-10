package com.feiqu.system.mapper.mainData;

import com.feiqu.system.model.FqAdvertisement;
import com.feiqu.system.model.FqAdvertisementExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface FqAdvertisementMapper {
    long countByExample(FqAdvertisementExample example);

    int deleteByExample(FqAdvertisementExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(FqAdvertisement record);

    int insertSelective(FqAdvertisement record);

    List<FqAdvertisement> selectByExample(FqAdvertisementExample example);

    FqAdvertisement selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") FqAdvertisement record, @Param("example") FqAdvertisementExample example);

    int updateByExample(@Param("record") FqAdvertisement record, @Param("example") FqAdvertisementExample example);

    int updateByPrimaryKeySelective(FqAdvertisement record);

    int updateByPrimaryKey(FqAdvertisement record);
}