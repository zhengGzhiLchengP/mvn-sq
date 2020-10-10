package com.feiqu.common.enums;

/**
 * 数据源
 * 
 * @author ruoyi
 */
public enum DataSourceType
{
    /**
     * 主要的数据库
     */
    FEIQU_MAIN("main_data"),
    FEIQU_SYS_DATA("sys_data"),
    //收集爬虫的数据
    FEIQU_COLLECT_DATA("collect_data"),
    //定时任务的库
    FEIQU_TASK_DATA("task_data"),
    //ci di 持续集成
    FEIQU_DEPLOY_DATA("deploy_data"),

    /**
     * 基础数据的数据库
     */
    FEIQU_BASIC_DATA("basic_data");



    //数据库的名称
    private String schemaName;

    public String getSchemaName() {
        return schemaName;
    }

    public void setSchemaName(String schemaName) {
        this.schemaName = schemaName;
    }

    DataSourceType(String data) {
        this.setSchemaName(data);
    }
}
