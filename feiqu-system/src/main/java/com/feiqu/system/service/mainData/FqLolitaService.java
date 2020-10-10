package com.feiqu.system.service.mainData;

import com.feiqu.system.base.BaseService;
import com.feiqu.system.model.FqLolita;
import com.feiqu.system.model.FqLolitaExample;
import com.feiqu.system.pojo.condition.CommonCondition;
import com.feiqu.system.pojo.response.FqLolitaDTO;

import java.util.List;

/**
* FqLolitaService接口
* created by cwd on 2019/2/19.
*/
public interface FqLolitaService extends BaseService<FqLolita, FqLolitaExample> {

    List<FqLolitaDTO> selectWithUser(CommonCondition condition);
}