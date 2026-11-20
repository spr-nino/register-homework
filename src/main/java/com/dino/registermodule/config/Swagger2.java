package com.dino.registermodule.config;

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
 * Swagger2 配置类。
 * @author zh
 * @ClassName cn.saytime.Swgger2
 * @Description 启用 Swagger2 并生成 controller 包下接口的 REST API 文档
 * @date 2017-07-10 22:12:31
 */
@Configuration
@EnableSwagger2
public class Swagger2 {

    /**
     * 创建 API 文档 Docket，扫描 controller 包下的所有接口。
     *
     * @return Swagger Docket 实例
     */
    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.dino.registermodule.controller"))
                .paths(PathSelectors.any())
                .build();
    }

    /**
     * 构建 API 文档的基本信息（标题、描述、服务条款、版本等）。
     *
     * @return API 文档基本信息
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("springboot+mybatis+mysql+swagger")
                .description("")//简单优雅的restfun风格，http://blog.csdn.net/saytime
                .termsOfServiceUrl("")//http://blog.csdn.net/saytime
                .version("1.0")
                .build();
    }
}
