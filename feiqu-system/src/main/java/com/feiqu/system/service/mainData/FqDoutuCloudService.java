package com.feiqu.system.service.mainData;

import com.feiqu.system.base.BaseService;
import com.feiqu.system.model.FqDoutuCloud;
import com.feiqu.system.model.FqDoutuCloudExample;
import com.feiqu.system.pojo.condition.CommonCondition;
import com.feiqu.system.pojo.response.FqDoutuCloudWithUser;

import java.util.List;

/**
* FqDoutuCloudService接口
* created by cwd on 2019/3/6.
*/
public interface FqDoutuCloudService extends BaseService<FqDoutuCloud, FqDoutuCloudExample> {

    List<FqDoutuCloudWithUser> selectWithUser(CommonCondition commonCondition);
}