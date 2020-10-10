package com.feiqu.system.mapper.collectData;

import com.feiqu.system.model.collectData.StockHl;
import com.feiqu.system.model.collectData.StockHlExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface StockHlMapper {
    long countByExample(StockHlExample example);

    int deleteByExample(StockHlExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(StockHl record);

    int insertSelective(StockHl record);

    List<StockHl> selectByExample(StockHlExample example);

    StockHl selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") StockHl record, @Param("example") StockHlExample example);

    int updateByExample(@Param("record") StockHl record, @Param("example") StockHlExample example);

    int updateByPrimaryKeySelective(StockHl record);

    int updateByPrimaryKey(StockHl record);
}