package com.feiqu.system.mapper.basicData;

import com.feiqu.system.model.basicData.XiaomiCity;
import com.feiqu.system.model.basicData.XiaomiCityExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface XiaomiCityMapper {
    long countByExample(XiaomiCityExample example);

    int deleteByExample(XiaomiCityExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(XiaomiCity record);

    int insertSelective(XiaomiCity record);

    List<XiaomiCity> selectByExample(XiaomiCityExample example);

    XiaomiCity selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") XiaomiCity record, @Param("example") XiaomiCityExample example);

    int updateByExample(@Param("record") XiaomiCity record, @Param("example") XiaomiCityExample example);

    int updateByPrimaryKeySelective(XiaomiCity record);

    int updateByPrimaryKey(XiaomiCity record);
}