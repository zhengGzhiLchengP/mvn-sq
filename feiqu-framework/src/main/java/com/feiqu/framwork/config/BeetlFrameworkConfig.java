package com.feiqu.framwork.config;

import com.feiqu.common.config.Global;
import com.feiqu.framwork.function.Functions;
import com.ibeetl.starter.BeetlTemplateCustomize;
import org.beetl.ext.spring.BeetlGroupUtilConfiguration;
import org.beetl.ext.spring.BeetlSpringViewResolver;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2019/1/22.
 */
@Configuration
public class BeetlFrameworkConfig {
    @Bean
    public BeetlTemplateCustomize beetlTemplateCustomize(@Qualifier("beetl_functions") Functions fn) {
        return groupTemplate -> {
            groupTemplate.registerFunctionPackage("c", fn);
            Map vars = new HashMap<String, Object>() {{
                put("ENV", Global.getEnv());
                put("VERSION", Global.getConfig("feiqu.cssVersion"));
                put("LAYUI_VERSION", Global.getConfig("feiqu.layuiVersion"));
                put("DOMAIN_URL", Global.getConfig("feiqu.domainUrl"));
                put("WEBSOCKET_URL", Global.getConfig("feiqu.websocketUrl"));
            }};
            groupTemplate.setSharedVars(vars);

        };
    }

   /* *//**
     *内容模板配置
     * @return
     *//*
    @Bean(name = "beetlConfig")
    public BeetlGroupUtilConfiguration getContentBeetlUtilConfiguration() {
        BeetlGroupUtilConfiguration beetlGroupUtilConfiguration = new BeetlGroupUtilConfiguration();
        ResourcePatternResolver patternResolver = ResourcePatternUtils.getResourcePatternResolver(new DefaultResourceLoader());

        beetlGroupUtilConfiguration.setConfigFileResource(patternResolver.getResource("classpath:beetl.properties"));
        beetlGroupUtilConfiguration.
//        ClasspathResourceLoader cploder = new ClasspathResourceLoader("/elasticsearch");
//        beetlGroupUtilConfiguration.setResourceLoader(cploder);
//        beetlGroupUtilConfiguration.setRoot("/");
        beetlGroupUtilConfiguration.init();

        return beetlGroupUtilConfiguration;
    }*/

    @Bean(name = "beetlViewResolver")
    public BeetlSpringViewResolver getBeetlSpringViewResolver(@Qualifier("beetlConfig") BeetlGroupUtilConfiguration beetlGroupUtilConfiguration) {
        BeetlSpringViewResolver beetlSpringViewResolver = new BeetlSpringViewResolver();
        beetlSpringViewResolver.setContentType("text/html;charset=UTF-8");
        beetlSpringViewResolver.setOrder(0);
        beetlSpringViewResolver.setConfig(beetlGroupUtilConfiguration);
        beetlSpringViewResolver.setSuffix(".html");
        return beetlSpringViewResolver;
    }
}
