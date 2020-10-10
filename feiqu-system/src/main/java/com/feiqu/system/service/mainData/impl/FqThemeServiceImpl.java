package com.feiqu.system.service.mainData.impl;


import com.feiqu.system.annotation.BaseService;
import com.feiqu.system.base.BaseServiceImpl;
import com.feiqu.system.mapper.mainData.FqThemeMapper;
import com.feiqu.system.model.mainData.FqTheme;
import com.feiqu.system.model.mainData.FqThemeExample;
import com.feiqu.system.pojo.condition.ThemeCondition;
import com.feiqu.system.pojo.response.FqThemeUserResponse;
import com.feiqu.system.service.mainData.FqThemeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
* FqThemeService实现
* Created by cwd on 2017/11/24.
*/
@Service
@Transactional
@BaseService
public class FqThemeServiceImpl extends BaseServiceImpl<FqThemeMapper, FqTheme, FqThemeExample> implements FqThemeService {

//    private static Logger _log = LoggerFactory.getLogger(FqThemeServiceImpl.class);

    @Resource
    FqThemeMapper fqThemeMapper;

    public List<FqThemeUserResponse> selectWithUserByExample(ThemeCondition condition) {
        return fqThemeMapper.selectWithUserByExample(condition);
    }
}