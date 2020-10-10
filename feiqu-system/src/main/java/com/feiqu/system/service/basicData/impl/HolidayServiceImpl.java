package com.feiqu.system.service.basicData.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.feiqu.system.annotation.BaseService;
import com.feiqu.system.base.BaseServiceImpl;
import com.feiqu.system.mapper.basicData.HolidayMapper;
import com.feiqu.system.model.basicData.Holiday;
import com.feiqu.system.model.basicData.HolidayExample;
import com.feiqu.system.service.basicData.HolidayService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
* HolidayService实现
* Created by cwd on 2020/1/16.
*/
@Service
@Transactional
@BaseService
public class HolidayServiceImpl extends BaseServiceImpl<HolidayMapper, Holiday, HolidayExample> implements HolidayService {

    private static Logger logger = LoggerFactory.getLogger(HolidayServiceImpl.class);

    @Resource
    HolidayMapper holidayMapper;

    @Override
    public String holidayName(Date date) {
        HolidayExample holidayExample = new HolidayExample();
        holidayExample.createCriteria().andGmtStartLessThanOrEqualTo(date)
        .andGmtEndGreaterThanOrEqualTo(date);
        List<Holiday> holidayList = holidayMapper.selectByExample(holidayExample);
        return CollectionUtil.isEmpty(holidayList)?"" : holidayList.get(0).getName();
    }
}