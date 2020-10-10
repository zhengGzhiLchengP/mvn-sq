package com.feiqu.system.mapper.sysData;

import com.feiqu.system.model.sysData.SysLogininfor;
import com.feiqu.system.model.sysData.SysLogininforExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface SysLogininforMapper {
    long countByExample(SysLogininforExample example);

    int deleteByExample(SysLogininforExample example);

    int deleteByPrimaryKey(Long infoId);

    int insert(SysLogininfor record);

    int insertSelective(SysLogininfor record);

    List<SysLogininfor> selectByExample(SysLogininforExample example);

    SysLogininfor selectByPrimaryKey(Long infoId);

    int updateByExampleSelective(@Param("record") SysLogininfor record, @Param("example") SysLogininforExample example);

    int updateByExample(@Param("record") SysLogininfor record, @Param("example") SysLogininforExample example);

    int updateByPrimaryKeySelective(SysLogininfor record);

    int updateByPrimaryKey(SysLogininfor record);
}