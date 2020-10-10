package com.feiqu.generator.mapper.sysData;

import com.feiqu.generator.model.sysData.GenTableColumn;
import com.feiqu.generator.model.sysData.GenTableColumnExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface GenTableColumnMapper {
    long countByExample(GenTableColumnExample example);

    int deleteByExample(GenTableColumnExample example);

    int deleteByPrimaryKey(Long columnId);

    int insert(GenTableColumn record);

    int insertSelective(GenTableColumn record);

    List<GenTableColumn> selectByExample(GenTableColumnExample example);

    GenTableColumn selectByPrimaryKey(Long columnId);

    int updateByExampleSelective(@Param("record") GenTableColumn record, @Param("example") GenTableColumnExample example);

    int updateByExample(@Param("record") GenTableColumn record, @Param("example") GenTableColumnExample example);

    int updateByPrimaryKeySelective(GenTableColumn record);

    int updateByPrimaryKey(GenTableColumn record);

    List<GenTableColumn> selectDbTableColumnsByName(String tableName);
}