package com.feiqu.system.mapper.basicData;

import com.feiqu.system.model.basicData.FinanceKnowledge;
import com.feiqu.system.model.basicData.FinanceKnowledgeExample;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface FinanceKnowledgeMapper {
    long countByExample(FinanceKnowledgeExample example);

    int deleteByExample(FinanceKnowledgeExample example);

    int deleteByPrimaryKey(Long id);

    int insert(FinanceKnowledge record);

    int insertSelective(FinanceKnowledge record);

    List<FinanceKnowledge> selectByExample(FinanceKnowledgeExample example);

    FinanceKnowledge selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") FinanceKnowledge record, @Param("example") FinanceKnowledgeExample example);

    int updateByExample(@Param("record") FinanceKnowledge record, @Param("example") FinanceKnowledgeExample example);

    int updateByPrimaryKeySelective(FinanceKnowledge record);

    int updateByPrimaryKey(FinanceKnowledge record);
}