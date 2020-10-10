package com.feiqu.framwork.config;

import com.feiqu.framwork.interceptor.IpControlInterceptor;
import com.feiqu.framwork.interceptor.RepeatSubmitInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;


@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    //    private static Logger logger = LoggerFactory.getLogger(WebMvcConfig.class);
    //重复提交
    @Resource
    private RepeatSubmitInterceptor repeatSubmitInterceptor;
    //ip数据控制
    @Resource
    private IpControlInterceptor ipControlInterceptor;


    /**
     * 添加拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(repeatSubmitInterceptor).addPathPatterns("/**");
        registry.addInterceptor(ipControlInterceptor).addPathPatterns("/**").excludePathPatterns("/blackList/**");
    }
    
	/**
	 * 跨域访问
	 */
	@Override
	public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedOrigins("http://m.f2qu.com","https://www.f2qu.com","https://www.flyfun.site/")
                .allowedMethods("*").allowedHeaders("*").allowCredentials(true).maxAge(3600);
		/*registry.addMapping("/fqNews/**")
		.allowedOrigins("*")
		.allowedMethods("*");
		registry.addMapping("/thought/**")
		.allowedOrigins("*")
		.allowedMethods("*");
        registry.addMapping("/u/**")
                .allowedOrigins("*")
                .allowedMethods("*");

        registry.addMapping("/article/**")
                .allowedOrigins("*")
                .allowedMethods("*");
        registry.addMapping("/theme/**")
                .allowedOrigins("*")
                .allowedMethods("*");
        registry.addMapping("/api/**")
                .allowedOrigins("*")
                .allowedMethods("*");
        registry.addMapping("/fqLolita/**")
                .allowedOrigins("*")
                .allowedMethods("*");
        registry.addMapping("/beauty/**")
                .allowedOrigins("*")
                .allowedMethods("*");
        registry.addMapping("/aliyun/oss/**")
                .allowedOrigins("*")
                .allowedMethods("*");
        registry.addMapping("/websocket/**")
                .allowedOrigins("*")
                .allowedMethods("*");
		registry.addMapping("/chatMsg/**")
                .allowedOrigins("*")
                .allowedMethods("*");*/

	}
}
