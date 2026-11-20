package com.dino.registermodule.config;

import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;

import javax.sql.DataSource;

/**
 * Spring 与 MyBatis 整合配置类。
 * 依据环境变量中的 mybatis 配置项创建 SqlSessionFactory，并启用基于注解的事务管理。
 */
@Configuration
@EnableTransactionManagement
public class SpringMybatisConfig implements TransactionManagementConfigurer,EnvironmentAware {
    /** 数据源，来自 Spring Boot 自动配置。 */
    @Autowired
    private DataSource dataSource;
    /** Spring 环境对象，用于读取 mybatis 配置项。 */
    private Environment environment;

    /**
     * 创建 SqlSessionFactory，设置数据源、MyBatis 主配置文件与 Mapper XML 位置。
     *
     * @return 初始化完成的 SqlSessionFactory
     * @throws Exception 配置文件读取或工厂创建失败时抛出
     */
    @Bean
    public SqlSessionFactory sqlSessionFactory() throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        ResourcePatternResolver resource = new PathMatchingResourcePatternResolver();
        bean.setConfigLocation(resource.getResource(this.environment.getProperty("mybatis.configLocation")));
        bean.setMapperLocations(resource.getResources(this.environment.getProperty("mybatis.mapperLocations")));
        return bean.getObject();
    }
    /**
     * 创建基于数据源的事务管理器，供注解驱动的事务管理使用。
     *
     * @return 数据源事务管理器
     */
    @Bean
    @Override
    public PlatformTransactionManager annotationDrivenTransactionManager() {
        return new DataSourceTransactionManager(dataSource);
    }

    /**
     * 注入 Spring 环境对象，供读取 mybatis 配置项使用。
     *
     * @param environment Spring 环境对象
     */
    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
}
