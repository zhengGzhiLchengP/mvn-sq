package com.feiqu.system.mapper.mainData;

import com.feiqu.system.model.mainData.LoginFailedLog;
import com.feiqu.system.model.mainData.LoginFailedLogExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface LoginFailedLogMapper {
    long countByExample(LoginFailedLogExample example);

    int deleteByExample(LoginFailedLogExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(LoginFailedLog record);

    int insertSelective(LoginFailedLog record);

    List<LoginFailedLog> selectByExample(LoginFailedLogExample example);

    LoginFailedLog selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") LoginFailedLog record, @Param("example") LoginFailedLogExample example);

    int updateByExample(@Param("record") LoginFailedLog record, @Param("example") LoginFailedLogExample example);

    int updateByPrimaryKeySelective(LoginFailedLog record);

    int updateByPrimaryKey(LoginFailedLog record);
}