/*
package com.feiqu.quartz.config;

import com.feiqu.common.enums.DataSourceType;
import com.feiqu.framwork.datasource.DynamicDataSourceContextHolder;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class QuarzDataSourceAspect
{
    @Pointcut("execution(* com.feiqu.quartz.service.*.*(..)) || execution(* com.feiqu.quartz.mapper.*.*(..))")
    private void dataSourceAspect() {
    }

    @Before(value = "dataSourceAspect()")
    public void chooseDataSource(JoinPoint joinPoint) {
        DynamicDataSourceContextHolder.setDataSoureType(DataSourceType.FEIQU_TASK_DATA.getSchemaName());
    }
}
*/
