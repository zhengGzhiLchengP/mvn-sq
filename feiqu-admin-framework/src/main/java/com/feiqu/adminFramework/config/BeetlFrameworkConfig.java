package com.feiqu.adminFramework.config;

import com.feiqu.adminFramework.functions.ShiroExt;
import com.feiqu.adminFramework.web.ConfigService;
import com.feiqu.adminFramework.web.DictService;
import com.feiqu.common.config.Global;
import com.feiqu.common.utils.SpringUtils;
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
    public BeetlTemplateCustomize beetlTemplateCustomize(@Qualifier("dict") DictService dictService) {
        return groupTemplate -> {
            groupTemplate.registerFunctionPackage("dict", dictService);
            groupTemplate.registerFunctionPackage("config", SpringUtils.getBean(ConfigService.class));
            groupTemplate.registerFunctionPackage("shiro",new ShiroExt());
//            groupTemplate.registerFunctionPackage("JSON", JSONUtil);
            Map vars = new HashMap<String, Object>() {{
                put("ENV", Global.getEnv());
                put("CSS_VERSION", Global.getConfig("feiqu.cssVersion"));
                put("JS_VERSION", Global.getConfig("feiqu.jsVersion"));
                put("LAYUI_VERSION", Global.getConfig("feiqu.layuiVersion"));
                put("DOMAIN_URL", Global.getConfig("feiqu.domainUrl"));
            }};
            groupTemplate.setSharedVars(vars);

        };
    }

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
