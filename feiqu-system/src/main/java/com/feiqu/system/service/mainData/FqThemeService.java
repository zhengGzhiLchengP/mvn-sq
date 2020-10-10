package com.feiqu.system.service.mainData;


import com.feiqu.system.base.BaseService;
import com.feiqu.system.model.mainData.FqTheme;
import com.feiqu.system.model.mainData.FqThemeExample;
import com.feiqu.system.pojo.condition.ThemeCondition;
import com.feiqu.system.pojo.response.FqThemeUserResponse;

import java.util.List;

/**
* FqThemeService接口
* created by cwd on 2017/11/24.
*/
public interface FqThemeService extends BaseService<FqTheme, FqThemeExample> {

    List<FqThemeUserResponse> selectWithUserByExample(ThemeCondition condition);
}