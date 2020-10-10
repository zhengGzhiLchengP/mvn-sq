package com.feiqu.system.service.basicData;

import com.feiqu.system.base.BaseService;
import com.feiqu.system.model.basicData.Holiday;
import com.feiqu.system.model.basicData.HolidayExample;

import java.util.Date;

/**
* HolidayService接口
* created by cwd on 2020/1/16.
*/
public interface HolidayService extends BaseService<Holiday, HolidayExample> {

    /**
     * 是否是在节假日
     * @param date
     * @return
     */
    String holidayName(Date date);
}