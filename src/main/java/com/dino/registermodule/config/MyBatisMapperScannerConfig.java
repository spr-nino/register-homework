package com.dino.registermodule.config;

import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * MyBatis Mapper 接口扫描配置类。
 * 从环境变量读取扫描基础包，自动注册 Mapper 接口，并指定其使用的 SqlSessionFactory。
 */
@Configuration
//注意，由于MapperScannerConfigurer执行的比较早，所以必须有下面的注解
@AutoConfigureAfter(SpringMybatisConfig.class)
public class MyBatisMapperScannerConfig implements EnvironmentAware {
    /** Spring 环境对象，用于读取 mybatis 配置项。 */
    private Environment environment;

    /**
     * 创建 Mapper 扫描器，配置扫描基础包与 SqlSessionFactory 名称。
     *
     * @return 配置完成的 MapperScannerConfigurer
     */
    @Bean
    public MapperScannerConfigurer mapperScannerConfigurer() {
        MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
        mapperScannerConfigurer.setBasePackage(this.environment.getProperty("mybatis.basePackage"));
        mapperScannerConfigurer.setSqlSessionFactoryBeanName("sqlSessionFactory");
        return mapperScannerConfigurer;
    }

    /**
     * 注入 Spring 环境对象，供读取配置项使用。
     *
     * @param environment Spring 环境对象
     */
    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
}
