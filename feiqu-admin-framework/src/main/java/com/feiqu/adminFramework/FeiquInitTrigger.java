package com.feiqu.adminFramework;

import cn.hutool.core.lang.Console;
import com.feiqu.common.utils.SpringUtils;
import com.feiqu.system.annotation.BaseService;
import com.feiqu.system.base.BaseInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * Created by Administrator on 2019/1/27.
 */
@Component
public class FeiquInitTrigger implements CommandLineRunner {
    private static Logger _log = LoggerFactory.getLogger(FeiquInitTrigger.class);

    @Override
    public void run(String... strings) {
        _log.info(">>>>> spring初始化完毕 <<<<<");
        // spring初始化完毕后，通过反射调用所有使用BaseService注解的initMapper方法
        Map<String, Object> baseServices = SpringUtils.getBeansWithAnnotation(BaseService.class);
        for(Object service : baseServices.values()) {
            _log.debug(">>>>> {}.initMapper()", service.getClass().getName());
            try {
                Method initMapper = service.getClass().getMethod("initMapper");
                initMapper.invoke(service);
            } catch (Exception e) {
                _log.error("初始化BaseService的initMapper方法异常", e);
            }
        }

        // 系统入口初始化
        Map<String, BaseInterface> baseInterfaceBeans = (Map<String, BaseInterface>) SpringUtils.getBeansOfType(BaseInterface.class);
        for(Object service : baseInterfaceBeans.values()) {
            _log.debug(">>>>> {}.init()", service.getClass().getName());
            try {
                Method init = service.getClass().getMethod("init");
                init.invoke(service);
            } catch (Exception e) {
                _log.error("初始化BaseInterface的init方法异常", e);
            }
        }
        _log.info(">>>>> service以及初始化方法完成 <<<<<");
        Console.log("本地开发地址：http://localhost:8081/");
    }
}
