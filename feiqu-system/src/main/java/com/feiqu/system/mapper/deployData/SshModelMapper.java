package com.feiqu.system.mapper.deployData;

import com.feiqu.system.model.deployData.SshModel;
import com.feiqu.system.model.deployData.SshModelExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SshModelMapper {
    long countByExample(SshModelExample example);

    int deleteByExample(SshModelExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(SshModel record);

    int insertSelective(SshModel record);

    List<SshModel> selectByExample(SshModelExample example);

    SshModel selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") SshModel record, @Param("example") SshModelExample example);

    int updateByExample(@Param("record") SshModel record, @Param("example") SshModelExample example);

    int updateByPrimaryKeySelective(SshModel record);

    int updateByPrimaryKey(SshModel record);
}