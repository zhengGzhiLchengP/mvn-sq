package com.feiqu.system.mapper.collectData;

import com.feiqu.system.model.collectData.HotTopic;
import com.feiqu.system.model.collectData.HotTopicExample;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface HotTopicMapper {
    long countByExample(HotTopicExample example);

    int deleteByExample(HotTopicExample example);

    int deleteByPrimaryKey(Long id);

    int insert(HotTopic record);

    int insertSelective(HotTopic record);

    List<HotTopic> selectByExample(HotTopicExample example);

    HotTopic selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") HotTopic record, @Param("example") HotTopicExample example);

    int updateByExample(@Param("record") HotTopic record, @Param("example") HotTopicExample example);

    int updateByPrimaryKeySelective(HotTopic record);

    int updateByPrimaryKey(HotTopic record);
}