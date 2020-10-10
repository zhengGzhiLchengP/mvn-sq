package com.feiqu.system.mapper.sysData;

import com.feiqu.system.model.sysData.SysPost;
import com.feiqu.system.model.sysData.SysPostExample;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface SysPostMapper {
    long countByExample(SysPostExample example);

    int deleteByExample(SysPostExample example);

    int deleteByPrimaryKey(Long postId);

    int insert(SysPost record);

    int insertSelective(SysPost record);

    List<SysPost> selectByExample(SysPostExample example);

    SysPost selectByPrimaryKey(Long postId);

    int updateByExampleSelective(@Param("record") SysPost record, @Param("example") SysPostExample example);

    int updateByExample(@Param("record") SysPost record, @Param("example") SysPostExample example);

    int updateByPrimaryKeySelective(SysPost record);

    int updateByPrimaryKey(SysPost record);

    List<SysPost> selectPostsByUserId(Long userId);
}