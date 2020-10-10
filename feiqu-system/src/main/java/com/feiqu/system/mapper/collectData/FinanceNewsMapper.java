package com.feiqu.system.mapper.collectData;

import com.feiqu.system.model.collectData.FinanceNews;
import com.feiqu.system.model.collectData.FinanceNewsExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface FinanceNewsMapper {
    long countByExample(FinanceNewsExample example);

    int deleteByExample(FinanceNewsExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(FinanceNews record);

    int insertSelective(FinanceNews record);

    List<FinanceNews> selectByExample(FinanceNewsExample example);

    FinanceNews selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") FinanceNews record, @Param("example") FinanceNewsExample example);

    int updateByExample(@Param("record") FinanceNews record, @Param("example") FinanceNewsExample example);

    int updateByPrimaryKeySelective(FinanceNews record);

    int updateByPrimaryKey(FinanceNews record);
}