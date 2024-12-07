// src/main/java/org/jnu/unimart/config/WebSecurityConfig.java

package org.jnu.unimart.config;

import org.jnu.unimart.security.JwtAuthEntryPoint;
import org.jnu.unimart.security.JwtAuthTokenFilter;
import org.jnu.unimart.security.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableMethodSecurity
public class WebSecurityConfig {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private JwtAuthEntryPoint unauthorizedHandler;

    @Bean
    public JwtAuthTokenFilter authenticationJwtTokenFilter() {
        return new JwtAuthTokenFilter();
    }

    /**
     * 配置 AuthenticationProvider
     * 使用 DaoAuthenticationProvider 集成 UserDetailsService 和 PasswordEncoder
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

    /**
     * 获取 AuthenticationManager
     * 通过 AuthenticationConfiguration
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    /**
     * 配置密码编码器
     * 使用 BCrypt 进行密码加密
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 配置 CORS 策略
     * 使用 CorsConfigurationSource Bean
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // 设置允许的域名，可以根据实际需求调整
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000")); // 例如，React 前端
        // 设置允许的 HTTP 方法
        configuration.setAllowedMethods(Arrays.asList("GET","POST","PUT","DELETE","OPTIONS"));
        // 设置允许的请求头
        configuration.setAllowedHeaders(Arrays.asList("Authorization","Content-Type"));
        // 允许凭证（如 Cookie）
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // 为所有路径应用 CORS 配置
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    /**
     * 配置 SecurityFilterChain
     * 使用基于 Lambda 的授权配置，并避免使用已弃用的方法
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // 启用 CORS 并应用自定义配置，同时避免使用已弃用的方法
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                // 禁用 CSRF
                .csrf(csrf -> csrf.disable())

                // 配置异常处理，指定未授权请求的处理器
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(unauthorizedHandler))

                // 配置会话管理为无状态（因为使用 JWT）
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // 配置授权规则
                .authorizeHttpRequests(authorize -> authorize
                        // 允许未认证访问的端点
                        .requestMatchers("/api/auth/login", "/api/auth/register").permitAll()
                        // 其他所有请求需要认证
                        .anyRequest().authenticated())

                // 配置 AuthenticationProvider
                .authenticationProvider(authenticationProvider())

                // 添加 JWT 认证过滤器
                .addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

}
