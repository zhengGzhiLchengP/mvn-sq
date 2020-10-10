package com.feiqu.system.mapper.basicData;

import com.feiqu.system.model.basicData.TaobaoProduct;
import com.feiqu.system.model.basicData.TaobaoProductExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TaobaoProductMapper {
    long countByExample(TaobaoProductExample example);

    int deleteByExample(TaobaoProductExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(TaobaoProduct record);

    int insertSelective(TaobaoProduct record);

    List<TaobaoProduct> selectByExample(TaobaoProductExample example);

    TaobaoProduct selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") TaobaoProduct record, @Param("example") TaobaoProductExample example);

    int updateByExample(@Param("record") TaobaoProduct record, @Param("example") TaobaoProductExample example);

    int updateByPrimaryKeySelective(TaobaoProduct record);

    int updateByPrimaryKey(TaobaoProduct record);
}