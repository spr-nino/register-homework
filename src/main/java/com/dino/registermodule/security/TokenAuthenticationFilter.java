package com.dino.registermodule.security;

import com.dino.registermodule.config.RedisComponent;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;

/**
 * 基于登录令牌的认证过滤器。
 *
 * <p>项目登录成功时会在 Redis 中以 {@code LG_<token> -> {loginId, userId}} 的哈希结构
 * 保存会话（见 NormalLoginServiceImpl / ShortMessageLoginServiceImpl / ThirdPartyLoginServiceImpl）。
 * 本过滤器从请求头解析该令牌，反查 Redis 得到 loginId，构造已认证身份写入
 * {@link SecurityContextHolder}，从而复用既有令牌体系完成鉴权，不改变登录/签发逻辑。</p>
 *
 * <p>令牌来源优先级：{@code Authorization: Bearer <token>} → {@code Authorization: <token>}
 * → 自定义头 {@code token}。Redis 访问失败时按“未认证”降级处理，由安全链返回 401，
 * 避免把后端故障放大为 500。</p>
 */
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    /** 与三个登录服务实现保持一致的 Redis 令牌键前缀 */
    private static final String REDIS_TOKEN_PREFIX = "LG_";
    /** 标准 Authorization 头 */
    private static final String HEADER_AUTHORIZATION = "Authorization";
    /** Bearer 方案前缀 */
    private static final String BEARER_PREFIX = "Bearer ";
    /** 兼容的自定义令牌头 */
    private static final String HEADER_TOKEN = "token";
    /** 认证通过时授予的角色 */
    private static final String ROLE_USER = "ROLE_USER";

    /** Redis 操作组件 */
    private final RedisComponent redisComponent;

    /**
     * 构造过滤器。
     *
     * @param redisComponent Redis 操作组件，用于按令牌查询登录会话
     */
    public TokenAuthenticationFilter(RedisComponent redisComponent) {
        this.redisComponent = redisComponent;
    }

    /**
     * 解析请求中的令牌，校验后写入安全上下文，再放行请求。
     *
     * @param request  当前请求
     * @param response 当前响应
     * @param chain    过滤器链
     * @throws ServletException Servlet 异常
     * @throws IOException      IO 异常
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        String token = resolveToken(request);
        // 未带令牌，或当前请求已完成认证，则无需重复解析
        if (!StringUtils.isEmpty(token) && SecurityContextHolder.getContext().getAuthentication() == null) {
            try {
                Map<String, Object> loginInfo = redisComponent.getMap(REDIS_TOKEN_PREFIX + token);
                if (loginInfo != null) {
                    Object loginIdObj = loginInfo.get("loginId");
                    if (loginIdObj != null && !StringUtils.isEmpty(loginIdObj.toString())) {
                        // principal 直接使用 loginId，控制器可通过 Authentication#getName() 取得
                        UsernamePasswordAuthenticationToken authentication =
                                new UsernamePasswordAuthenticationToken(
                                        loginIdObj.toString(),
                                        null,
                                        Collections.singletonList(new SimpleGrantedAuthority(ROLE_USER)));
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
                }
            } catch (Exception e) {
                // Redis 异常按未认证降级：受保护接口将由安全链返回 401
                SecurityContextHolder.clearContext();
            }
        }
        chain.doFilter(request, response);
    }

    /**
     * 从请求头解析令牌。
     *
     * @param request 当前请求
     * @return 令牌字符串，未携带时返回 null
     */
    private String resolveToken(HttpServletRequest request) {
        String authorization = request.getHeader(HEADER_AUTHORIZATION);
        if (!StringUtils.isEmpty(authorization)) {
            if (authorization.startsWith(BEARER_PREFIX)) {
                return authorization.substring(BEARER_PREFIX.length()).trim();
            }
            return authorization.trim();
        }
        return request.getHeader(HEADER_TOKEN);
    }
}
