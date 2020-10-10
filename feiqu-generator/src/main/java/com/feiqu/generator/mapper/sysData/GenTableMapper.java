package com.feiqu.generator.mapper.sysData;

import com.feiqu.generator.model.sysData.GenTable;
import com.feiqu.generator.model.sysData.GenTableExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface GenTableMapper {
    long countByExample(GenTableExample example);

    int deleteByExample(GenTableExample example);

    int deleteByPrimaryKey(Long tableId);

    int insert(GenTable record);

    int insertSelective(GenTable record);

    List<GenTable> selectByExample(GenTableExample example);

    GenTable selectByPrimaryKey(Long tableId);

    int updateByExampleSelective(@Param("record") GenTable record, @Param("example") GenTableExample example);

    int updateByExample(@Param("record") GenTable record, @Param("example") GenTableExample example);

    int updateByPrimaryKeySelective(GenTable record);

    int updateByPrimaryKey(GenTable record);

    List<GenTable> selectDbTableList(GenTable genTable);

    List<GenTable> selectDbTableListByNames(String[] tableNames);

    GenTable selectGenTableByName(String tableName);
}