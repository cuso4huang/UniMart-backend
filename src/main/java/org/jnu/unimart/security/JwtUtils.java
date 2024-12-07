package org.jnu.unimart.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtUtils {

    @Value("${app.jwtSecret}")
    private String jwtSecret;

    @Value("${app.jwtExpirationMs}")
    private int jwtExpirationMs;

    /**
     * 生成JWT签名密钥
     */
    private Key getSigningKey() {
        byte[] keyBytes = jwtSecret.getBytes(StandardCharsets.UTF_8);
        // HS512 需要至少 64 字节的密钥
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * 生成令牌
     * @param authentication
     * @return
     */
    public String generateJwtToken(Authentication authentication) {
        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userPrincipal.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        return Jwts.builder()
                .setSubject(userPrincipal.getUsername()) // 用户名作为主体
                .setIssuedAt(new Date()) // 签发时间
                .claim("roles", roles) // 添加角色信息
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs)) // 过期时间
                .signWith(getSigningKey(), SignatureAlgorithm.HS512) // 使用生成的密钥
                .compact();
    }


    /**
     * 从JWT令牌中获取用户名
     * @param token JWT令牌
     * @return 用户名
     */
    public String getUserNameFromJwtToken(String token) {
        return Jwts.parser()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();  // 从 Token 的负载中提取用户名
    }

    /**
     * 验证JWT令牌
     * @param authToken JWT令牌
     * @return 是否有效
     */
    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(authToken);
            return true;
        } catch (SignatureException e) {
            System.err.println("无效的 JWT 签名: " + e.getMessage());
        } catch (MalformedJwtException e) {
            System.err.println("无效的 JWT 格式: " + e.getMessage());
        } catch (ExpiredJwtException e) {
            System.err.println("JWT 已过期: " + e.getMessage());
        } catch (UnsupportedJwtException e) {
            System.err.println("不支持的 JWT: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.err.println("空的 JWT 字符串: " + e.getMessage());
        }
        return false;
    }

}
