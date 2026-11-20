package com.dino.registermodule;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Spring Boot 应用上下文的集成测试类，用于验证模块配置能够正确加载。
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class RegisterModuleApplicationTests {

	/**
	 * 验证 Spring 应用上下文能够正常启动并完成 Bean 装配。
	 */
	@Test
	public void contextLoads() {
	}

}
