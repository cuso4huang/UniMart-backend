// src/main/java/org/jnu/unimart/controller/UserController.java

package org.jnu.unimart.controller;

import org.jnu.unimart.exception.UserNotFoundException;
import org.jnu.unimart.pojo.User;
import org.jnu.unimart.service.impl.UserServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users") // 确保路径与安全配置中的匹配
public class UserController {

    private final UserServiceImpl userService;

    public UserController(UserServiceImpl userService) {
        this.userService = userService;
    }

    /**
     * 获取所有用户信息，仅限 ADMIN 角色
     * @return 所有用户列表
     */
    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<User>> getAllUsers(){
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    /**
     * 获取指定 ID 的用户信息，仅限 ADMIN 角色或用户本人
     * @param id 用户 ID
     * @return 用户信息
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or #id == principal.id")
    public ResponseEntity<User> getUserById(@PathVariable int id) {
        Optional<User> userOpt = userService.getUserById(id);
        if(userOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(userOpt.get());
    }

    /**
     * 根据用户名搜索用户，仅限 ADMIN 角色
     * @param username 用户名
     * @return 用户信息
     */
    @GetMapping("/search")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<User> getUserByUsername(@RequestParam String username) {
        Optional<User> userOpt = userService.getUserByUsername(username);
        if(userOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(userOpt.get());
    }

    //感觉这个方法可以不用
//    /**
//     * 创建新用户，仅限 ADMIN 角色
//     * @param user 用户信息
//     * @return 创建结果
//     */
//    @PostMapping
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
//    public ResponseEntity<String> createUser(@Valid @RequestBody User user) {
//        // 确保必填字段不为空
//        if (user.getUserName() == null || user.getUserName().isEmpty() ||
//                user.getAccount() == null || user.getAccount().isEmpty() ||
//                user.getUserPassword() == null || user.getUserPassword().isEmpty()) {
//            return ResponseEntity.badRequest().body("Username, account and password are required");
//        }
//
//        boolean created = userService.createUser(user.getAccount(), user.getUserPassword(), user.getUserPassword());
//        if (created) {
//            return ResponseEntity.status(201).body("User created successfully");
//        } else {
//            return ResponseEntity.status(409).body("User already exists");
//        }
//    }

    /**
     * 删除指定 ID 的用户，仅限 ADMIN 角色
     * @param id 用户 ID
     * @return 删除结果
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteUser(@PathVariable int id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.noContent().build();
        } catch (UserNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * 更新指定 ID 的用户，仅限 ADMIN 角色或用户本人
     * @param id 用户 ID
     * @param user 更新信息
     * @return 更新后的用户信息
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or #id == principal.id")
    public ResponseEntity<User> updateUser(@PathVariable int id,
                                           @Valid @RequestBody User user) {
        try {
            user.setUserId(id); // 确保ID一致
            User updatedUser = userService.updateUser(user);
            // 隐藏密码（虽然已使用 @JsonIgnore，但额外设置为 null 以防万一）
            updatedUser.setUserPassword(null);
            return ResponseEntity.ok(updatedUser);
        } catch (UserNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
