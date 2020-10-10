package com.feiqu.framwork.aspectj;


import com.feiqu.common.enums.DataSourceType;
import com.feiqu.framwork.datasource.DynamicDataSourceContextHolder;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
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
@Order(1)
@Component
public class DataSourceAspect
{
    protected static Logger logger = LoggerFactory.getLogger(DataSourceAspect.class);


    @Pointcut("execution(* com.feiqu.system.base.BaseService.*(..))")
    private void dataSourceAspect() {
    }

    @After(value = "dataSourceAspect()")
    public void cleanDataSource() {
        DynamicDataSourceContextHolder.clearDataSoureType();
    }


    //这里切到你的方法目录 读写分离用的 需要 mysql主从同步 这个很麻烦 不想弄 以后数据量大了再说吧
    @Before(value = "dataSourceAspect()")
    public void chooseDataSource(JoinPoint joinPoint) {
        String str = joinPoint.getTarget().toString();
        if(str.contains("basicData")){
            DynamicDataSourceContextHolder.setDataSoureType(DataSourceType.FEIQU_BASIC_DATA.getSchemaName());
        } else if (str.contains("collectData")) {
            DynamicDataSourceContextHolder.setDataSoureType(DataSourceType.FEIQU_COLLECT_DATA.getSchemaName());
        }else if (str.contains("sysData")) {
            DynamicDataSourceContextHolder.setDataSoureType(DataSourceType.FEIQU_SYS_DATA.getSchemaName());
        }else {
            DynamicDataSourceContextHolder.setDataSoureType(DataSourceType.FEIQU_MAIN.getSchemaName());
        }
    }
}
