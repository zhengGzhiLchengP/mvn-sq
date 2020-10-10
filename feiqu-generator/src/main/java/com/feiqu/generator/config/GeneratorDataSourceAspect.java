package com.feiqu.generator.config;


import com.feiqu.common.enums.DataSourceType;
import com.feiqu.framwork.datasource.DynamicDataSourceContextHolder;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 多数据源处理
 *
 * @author ruoyi
 */
@Aspect
@Order(2)
@Component
public class GeneratorDataSourceAspect {
    protected static Logger logger = LoggerFactory.getLogger(GeneratorDataSourceAspect.class);


    @Pointcut("execution(* com.feiqu.generator.service.sysData.*.*(..))")
    private void dataSourceAspect() {
    }

    @Before("dataSourceAspect()")
    public void choose(JoinPoint joinPoint) {
        DynamicDataSourceContextHolder.setDataSoureType(DataSourceType.FEIQU_SYS_DATA.getSchemaName());
    }

    // 在方法执行完结后打印返回内容
    @AfterReturning(returning = "o", pointcut = "dataSourceAspect()")
    public void methodAfterReturing(Object o) {
        DynamicDataSourceContextHolder.clearDataSoureType();
    }
}
