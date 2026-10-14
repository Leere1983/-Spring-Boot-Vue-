package com.spboot.app.config;

import com.spboot.app.encoder.ClearPasswordEncoder;
import com.spboot.app.encoder.Md5PasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfigure implements WebMvcConfigurer {

    @Autowired
    private ApplicationContext context;

    /**
     * 设置上传路径存储位置,默认放在运行目录下的 upload
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 设置方位地址
        System.out.println("当前上传保存路径为：" + Configure.UPLOAD_DIR);
        registry
            .addResourceHandler("/upload/**")
            .addResourceLocations("file:" + Configure.UPLOAD_DIR)
            .addResourceLocations("file:" + System.getProperty("user.dir") + "/src/webapp/upload/");

        // swagger-ui.html
        registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");

        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //注册TokenInterceptor拦截器，主要设置前后端cors 头设置，和前端提交的token 进行处理
        InterceptorRegistration registration = registry.addInterceptor(context.getBean(TokenInterceptor.class));
        registration.addPathPatterns("/**"); //所有路径都被拦截
    }

    // 设置密码加密类
    @Bean
    public PasswordEncoder getPasswordEncoder() {
        if (Configure.isMd5) {
            return new Md5PasswordEncoder();
        }
        return new ClearPasswordEncoder();
    }
}
