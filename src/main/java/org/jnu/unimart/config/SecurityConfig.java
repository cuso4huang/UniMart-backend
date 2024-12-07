// src/main/java/org/jnu/unimart/config/SecurityConfig.java

package org.jnu.unimart.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    /**
     * 定义PasswordEncoder Bean，使用BCryptPasswordEncoder实现
     * @return PasswordEncoder实例
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 配置SecurityFilterChain，使用最新的Lambda配置方式
     * @param http HttpSecurity对象
     * @return SecurityFilterChain实例
     * @throws Exception 异常
     */
    @Bean
    public SecurityFilterChain filterChain(org.springframework.security.config.annotation.web.builders.HttpSecurity http) throws Exception {
        http
                // 禁用CSRF（根据需要）
                .csrf(csrf -> csrf.disable())
                // 配置授权规则
                .authorizeHttpRequests(authorize -> authorize
                        .anyRequest().permitAll()
                )
                // 其他配置（如表单登录、HTTP Basic认证等）可根据需要添加
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }
}
