package com.feiqu.system.mapper.deployData;

import com.feiqu.system.model.deployData.DeployProject;
import com.feiqu.system.model.deployData.DeployProjectExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DeployProjectMapper {
    long countByExample(DeployProjectExample example);

    int deleteByExample(DeployProjectExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(DeployProject record);

    int insertSelective(DeployProject record);

    List<DeployProject> selectByExample(DeployProjectExample example);

    DeployProject selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") DeployProject record, @Param("example") DeployProjectExample example);

    int updateByExample(@Param("record") DeployProject record, @Param("example") DeployProjectExample example);

    int updateByPrimaryKeySelective(DeployProject record);

    int updateByPrimaryKey(DeployProject record);
}