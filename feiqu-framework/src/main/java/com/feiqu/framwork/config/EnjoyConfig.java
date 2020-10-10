package com.feiqu.framwork.config;

/**
 * http://www.jfinal.com/doc/6-10
 * jfinal的enjoy模板配置
 */
//@Configuration
/*public class EnjoyConfig {
    @Bean(name = "jfinalViewResolver")
    public JFinalViewResolver getJFinalViewResolver() {

        // 创建用于整合 spring boot 的 ViewResolver 扩展对象
        JFinalViewResolver jfr = new JFinalViewResolver();

        // 对 spring boot 进行配置
        jfr.setSuffix(".html");
        jfr.setContentType("text/html;charset=UTF-8");
        jfr.setOrder(0);

        // 获取 engine 对象，对 enjoy 模板引擎进行配置，配置方式与前面章节完全一样
        Engine engine  = JFinalViewResolver.engine;

        // 热加载配置能对后续配置产生影响，需要放在最前面
        engine.setDevMode("dev".equals(Global.getEnv()));

        // 使用 ClassPathSourceFactory 从 class path 与 jar 包中加载模板文件
        engine.setToClassPathSourceFactory();

        // 在使用 ClassPathSourceFactory 时要使用 setBaseTemplatePath
        // 代替 jfr.setPrefix("/view/")
        engine.setBaseTemplatePath("/templates/");

        // 添加模板函数
        engine.addSharedFunction("/common/_fq_layout.html");

        // 更多配置与前面章节完全一样
        // engine.addDirective(...)
         engine.addSharedMethod(Functions.class);
        engine.addSharedObject("ENV", Global.getEnv());
        engine.addSharedObject("VERSION", Global.getConfig("feiqu.cssVersion"));
        engine.addSharedObject("LAYUI_VERSION", Global.getConfig("feiqu.layuiVersion"));
        engine.addSharedObject("DOMAIN_URL", Global.getConfig("feiqu.domainUrl"));
        engine.addSharedObject("WEBSOCKET_URL", Global.getConfig("feiqu.websocketUrl"));

        return jfr;
    }
}*/
