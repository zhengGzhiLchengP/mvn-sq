package com.feiqu.adminFramework.aspectj;


import com.feiqu.common.enums.DataSourceType;
import com.feiqu.framwork.datasource.DynamicDataSourceContextHolder;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
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
@Order(1)
@Component
public class DataSourceAspect
{
    protected static Logger logger = LoggerFactory.getLogger(DataSourceAspect.class);


    @Pointcut("execution(* com.feiqu.system.base.BaseService.*(..)) || execution(* com.feiqu.system.service.*.*.*(..)) || execution(* com.feiqu.quartz.service.*.*.*(..))")
    private void dataSourceAspect() {
    }


    private void decideDatasource(String str) {
        if(str.contains("basicData")){
            DynamicDataSourceContextHolder.setDataSoureType(DataSourceType.FEIQU_BASIC_DATA.getSchemaName());
        }else if(str.contains("sysData")){
            DynamicDataSourceContextHolder.setDataSoureType(DataSourceType.FEIQU_SYS_DATA.getSchemaName());
        } else if (str.contains("collectData")) {
            DynamicDataSourceContextHolder.setDataSoureType(DataSourceType.FEIQU_COLLECT_DATA.getSchemaName());
        } else if (str.contains("quartz")) {
            DynamicDataSourceContextHolder.setDataSoureType(DataSourceType.FEIQU_TASK_DATA.getSchemaName());
        }else if (str.contains("deployData")) {
            DynamicDataSourceContextHolder.setDataSoureType(DataSourceType.FEIQU_DEPLOY_DATA.getSchemaName());
        } else {
            DynamicDataSourceContextHolder.setDataSoureType(DataSourceType.FEIQU_MAIN.getSchemaName());
        }
    }

    //这里切到你的方法目录 读写分离用的 需要 mysql主从同步 这个很麻烦 不想弄 以后数据量大了再说吧
    @Around(value = "dataSourceAspect()")
    public Object chooseDataSource(ProceedingJoinPoint joinPoint) throws Throwable {

        String str=joinPoint.getTarget().toString();
        decideDatasource(str);
        try
        {
            return joinPoint.proceed();
        }
        finally
        {
            // 销毁数据源 在执行方法之后
            DynamicDataSourceContextHolder.clearDataSoureType();
        }
    }

    // 在方法执行完结后打印返回内容
    @AfterReturning(returning = "o", pointcut = "dataSourceAspect()")
    public void methodAfterReturing(Object o) {
        DynamicDataSourceContextHolder.clearDataSoureType();
    }
}
