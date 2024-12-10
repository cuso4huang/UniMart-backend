// src/main/java/org/jnu/unimart/controller/AuthController.java

package org.jnu.unimart.controller;

import org.jnu.unimart.pojo.Role;
import org.jnu.unimart.pojo.User;
import org.jnu.unimart.repository.RoleRepository;
import org.jnu.unimart.repository.UserRepository;
import org.jnu.unimart.security.JwtUtils;
import org.jnu.unimart.security.UserDetailsImpl;
import org.jnu.unimart.payload.LoginRequest;
import org.jnu.unimart.payload.SignupRequest;
import org.jnu.unimart.payload.JwtResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.Collections;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;
    
    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    /**
     * 登录接口
     * @param loginRequest 包含 username 和 password 的请求体
     * @return 返回 JWT 令牌和用户信息
     */
    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

            // 生成 JWT 令牌
            String jwt = jwtUtils.generateJwtToken(authentication);

            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            // 返回 JWT 令牌和用户信息
            return ResponseEntity.ok(new JwtResponse(jwt, userDetails.getId(), userDetails.getUsername()));
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(401).body("Error: Invalid username or password");
        }
    }

    /**
     * 注册接口
     * @param signUpRequest 包含 account, username, password 的请求体
     * @return 返回注册成功的消息
     */
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        if (userRepository.findByUserName(signUpRequest.getUsername()).isPresent()) {
            return ResponseEntity
                .badRequest()
                .body("Error: Username is already taken!");
        }

        // 创建新用户
        User user = new User();
        user.setAccount(signUpRequest.getAccount());
        user.setUserName(signUpRequest.getUsername());
        user.setUserPassword(encoder.encode(signUpRequest.getPassword())); // 加密密码

        // 赋予用户默认角色
        // 这里假设有 ROLE_USER 存在
        Role userRole = roleRepository.findByName("ROLE_USER")
            .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
        user.setRoles(Collections.singleton(userRole));

        userRepository.save(user);

        return ResponseEntity.ok("User registered successfully!");
    }
}
