package com.feiqu.framwork.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import com.feiqu.common.enums.DataSourceType;
import com.feiqu.framwork.config.properties.DruidProperties;
import com.feiqu.framwork.datasource.DynamicDataSource;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * druid 配置多数据源
 * 
 * @author ruoyi
 */
@Configuration
public class DruidConfig
{
    @Bean
    @ConfigurationProperties("spring.datasource.druid.master")
    public DataSource masterDataSource(DruidProperties druidProperties)
    {
        DruidDataSource dataSource = DruidDataSourceBuilder.create().build();
        return druidProperties.dataSource(dataSource);
    }

    @Bean
    @ConfigurationProperties("spring.datasource.druid.basic-data")
    @ConditionalOnProperty(prefix = "spring.datasource.druid.basic-data", name = "enabled", havingValue = "true")
    public DataSource basicDataDataSource(DruidProperties druidProperties)
    {
        DruidDataSource dataSource = DruidDataSourceBuilder.create().build();
        return druidProperties.dataSource(dataSource);
    }

    @Bean
    @ConfigurationProperties("spring.datasource.druid.collect-data")
    public DataSource collectDataDataSource(DruidProperties druidProperties) {
        DruidDataSource dataSource = DruidDataSourceBuilder.create().build();
        return druidProperties.dataSource(dataSource);
    }

    @Bean
    @ConfigurationProperties("spring.datasource.druid.sys-data")
    public DataSource sysDataSource(DruidProperties druidProperties)
    {
        DruidDataSource dataSource = DruidDataSourceBuilder.create().build();
        return druidProperties.dataSource(dataSource);
    }

    /**
     * 配置多个数据源
     * @param masterDataSource
     * @param basicDataDataSource
     * @param collectDataDataSource
     * @param sysDataSource
     * @return
     */
    @Bean(name = "dynamicDataSource")
    @Primary
    public DynamicDataSource dataSource(DataSource masterDataSource, DataSource basicDataDataSource,
                                        DataSource collectDataDataSource,DataSource sysDataSource)
    {
        Map<Object, Object> targetDataSources = new HashMap<>();
        targetDataSources.put(DataSourceType.FEIQU_MAIN.getSchemaName(), masterDataSource);
        targetDataSources.put(DataSourceType.FEIQU_BASIC_DATA.getSchemaName(), basicDataDataSource);
        targetDataSources.put(DataSourceType.FEIQU_COLLECT_DATA.getSchemaName(), collectDataDataSource);
        targetDataSources.put(DataSourceType.FEIQU_SYS_DATA.getSchemaName(), sysDataSource);
        return new DynamicDataSource(masterDataSource, targetDataSources);
    }
}
