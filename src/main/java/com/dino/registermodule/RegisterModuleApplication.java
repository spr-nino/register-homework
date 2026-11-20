package com.dino.registermodule;

import com.dino.registermodule.config.MyBatisMapperScannerConfig;
import com.dino.registermodule.config.RedisConfig;
import com.dino.registermodule.config.SpringMybatisConfig;
import com.dino.registermodule.config.Swagger2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * 注册登录模块的 Spring Boot 启动入口。
 * <p>通过 {@link SpringBootApplication} 开启自动配置与组件扫描，并通过
 * {@link ImportAutoConfiguration} 显式导入 MyBatis、Mapper 扫描及 Redis 相关配置类。
 */
@SpringBootApplication
//@ComponentScan(basePackages = { "com.dino.registermodule.domain.entity", "com.dino.registermodule.domain.param", "com.dino.registermodule.domain.result",
//		"com.dino.registermodule.component","com.dino.registermodule.service",
//		"com.dino.registermodule.dao", "com.dino.registermodule.dao.sql","com.dino.registermodule.base","com.dino.registermodule.handler"})
@ImportAutoConfiguration(
		classes = {
				SpringMybatisConfig.class,//Spring 与 MyBatis 整合配置
				MyBatisMapperScannerConfig.class,//MyBatis Mapper 接口扫描配置
				RedisConfig.class//Redis 连接与缓存配置
		}
)
public class RegisterModuleApplication {

	/**
	 * 应用启动入口方法，启动 Spring Boot 容器。
	 *
	 * @param args 命令行参数，由 Spring Boot 解析并传递给应用
	 */
	public static void main(String[] args) {
		SpringApplication.run(RegisterModuleApplication.class, args);
	}
}
