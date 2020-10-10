package com.feiqu.system.mapper.basicData;

import com.feiqu.system.model.basicData.StockBasic;
import com.feiqu.system.model.basicData.StockBasicExample;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface StockBasicMapper {
    long countByExample(StockBasicExample example);

    int deleteByExample(StockBasicExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(StockBasic record);

    int insertSelective(StockBasic record);

    List<StockBasic> selectByExample(StockBasicExample example);

    StockBasic selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") StockBasic record, @Param("example") StockBasicExample example);

    int updateByExample(@Param("record") StockBasic record, @Param("example") StockBasicExample example);

    int updateByPrimaryKeySelective(StockBasic record);

    int updateByPrimaryKey(StockBasic record);
}