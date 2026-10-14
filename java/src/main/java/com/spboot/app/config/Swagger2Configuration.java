package com.spboot.app.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * api 接口定义访问路径，可以在系统上查看
 */
@Configuration
@EnableSwagger2
public class Swagger2Configuration implements org.springframework.boot.ApplicationRunner {

    //api接口包扫描路径
    public static final String SWAGGER_SCAN_BASE_PACKAGE = "com.spboot.app.controller";
    public static final String VERSION = "1.0.0";

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
            .apiInfo(apiInfo())
            .select()
            .apis(RequestHandlerSelectors.basePackage(SWAGGER_SCAN_BASE_PACKAGE))
            .paths(PathSelectors.any()) // 可以根据url路径设置哪些请求加入文档，忽略哪些请求
            .build();
    }

    @Value("${server.port}")
    private int port;

    private static final Logger logger = LoggerFactory.getLogger(Swagger2Configuration.class);

    /**
     * 系统自动运行这个方法
     * @param args
     * @throws Exception
     */
    @Override
    public void run(ApplicationArguments args) throws Exception {
        logger.info("start success :");
        logger.info("click Swagger2 Url to Document: http://localhost:" + port + "/swagger-ui.html");
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
            .title("系统API接口文档") //设置文档的标题
            .description("API 接口文档") // 设置文档的描述
            .version(VERSION) // 设置文档的版本信息-> 1.0.0 Version information
            .termsOfServiceUrl("http://www.baidu.com") // 设置文档的License信息->1.3 License information
            .build();
    }
}
