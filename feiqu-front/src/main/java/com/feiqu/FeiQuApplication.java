package com.feiqu;

//import com.feiqu.web.listener.FeiquListener;

import com.feiqu.common.config.Global;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * 启动程序
 *
 * @author chenweidong
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class FeiQuApplication {
    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(FeiQuApplication.class);
        springApplication.run(args);
        System.out.println("(♥◠‿◠)ﾉﾞ  飞趣社区启动成功   ლ(´ڡ`ლ)ﾞ  \n");
        if("dev".equals(Global.getEnv())){
            System.out.println("本地开发地址：http://localhost:8080/  \n");
        }
    }
}