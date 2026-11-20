package com.dino.registermodule.security;

import com.dino.registermodule.base.HttpStatus;
import com.dino.registermodule.base.RestResponseStatus;
import com.dino.registermodule.config.RedisComponent;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Spring Security 安全配置。
 *
 * <p>设计原则：默认全部放行，仅对需要登录态的接口（当前为 /reset/password）强制认证，
 * 从而不破坏既有登录、注册、找回密码等匿名接口的调用方式。采用无状态会话（STATELESS），
 * 关闭 CSRF，认证信息完全来自 {@link TokenAuthenticationFilter} 解析的登录令牌。</p>
 *
 * <p>未认证访问受保护接口返回项目统一的 JSON 结构
 * （status={@link RestResponseStatus#NOT_LOGIN}），避免 Security 默认的 HTML 登录页或空响应。</p>
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    /** Redis 操作组件，用于构建令牌认证过滤器 */
    @Autowired
    private RedisComponent redisComponent;
    /** JSON 序列化器，用于输出统一的错误响应 */
    @Autowired
    private ObjectMapper objectMapper;

    /**
     * 配置 HTTP 安全规则。
     *
     * @param http HttpSecurity 构建器
     * @throws Exception 配置异常
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                // REST 无状态接口：关闭 CSRF 与会话，不启用表单登录/Basic 认证
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .formLogin().disable()
                .httpBasic().disable()
                .logout().disable()
                // 授权规则：放行全部既有匿名接口，仅 /reset/password 要求已认证
                .authorizeRequests()
                    .antMatchers("/reset/password").authenticated()
                    .anyRequest().permitAll()
                .and()
                // 认证/授权失败时返回统一 JSON
                .exceptionHandling()
                    .authenticationEntryPoint((request, response, authException) ->
                            writeSecurityError(response, RestResponseStatus.NOT_LOGIN, "未登录或登录已失效"))
                    .accessDeniedHandler((request, response, accessDeniedException) ->
                            writeSecurityError(response, RestResponseStatus.NOT_LOGIN, "没有权限执行该操作"))
                .and()
                // 在用户名密码过滤器之前插入令牌认证过滤器（内联 new，避免被容器重复注册为普通 Filter）
                .addFilterBefore(new TokenAuthenticationFilter(redisComponent), UsernamePasswordAuthenticationFilter.class);
    }

    /**
     * 输出与项目 BaseRestResponse 一致的错误 JSON。
     *
     * @param response HTTP 响应
     * @param status   状态码字符串
     * @param message  错误描述
     * @throws IOException IO 异常
     */
    private void writeSecurityError(HttpServletResponse response, String status, String message) throws IOException {
        response.setStatus(Integer.parseInt(status));
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        Map<String, String> body = new LinkedHashMap<>();
        body.put("status", status);
        body.put("message", message);
        objectMapper.writeValue(response.getWriter(), body);
    }
}
