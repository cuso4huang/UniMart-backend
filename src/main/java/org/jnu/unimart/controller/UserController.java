// src/main/java/org/jnu/unimart/controller/UserController.java

package org.jnu.unimart.controller;

import org.jnu.unimart.exception.UserNotFoundException;
import org.jnu.unimart.payload.PasswordChangeRequest;
import org.jnu.unimart.payload.UserUpdateRequest;
import org.jnu.unimart.pojo.Product;
import org.jnu.unimart.pojo.User;
import org.jnu.unimart.pojo.UserInfo;
import org.jnu.unimart.security.UserDetailsImpl;
import org.jnu.unimart.service.impl.ProductServiceImpl;
import org.jnu.unimart.service.impl.UserServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserServiceImpl userService;
    private ProductServiceImpl productService;

    public UserController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @GetMapping("/info")
    public ResponseEntity<UserInfo> getUserInfo(@AuthenticationPrincipal UserDetailsImpl currentUser) {
        Optional<User> userOpt = userService.getUserById(currentUser.getId());

        if (userOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        User user = userOpt.get();
        UserInfo userInfo = new UserInfo();
        userInfo.setId(user.getUserId());
        userInfo.setAccount(user.getAccount());  // account字段就是email
        userInfo.setUsername(user.getUserName());
        userInfo.setAvatar(user.getAvatar());

        return ResponseEntity.ok(userInfo);
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateUserInfo(
            @AuthenticationPrincipal UserDetailsImpl currentUser,
            @Valid @RequestBody UserUpdateRequest request) {
        try {
            User user = new User();
            user.setUserId(currentUser.getId());
            user.setUserName(request.getUsername());

            User updatedUser = userService.updateUser(user);

            return ResponseEntity.ok().build();
        } catch (UserNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/password")
    public ResponseEntity<?> changePassword(
            @AuthenticationPrincipal UserDetailsImpl currentUser,
            @Valid @RequestBody PasswordChangeRequest request) {
        try {
            boolean success = userService.changePassword(
                    currentUser.getId(),
                    request.getOldPassword(),
                    request.getNewPassword()
            );

            if (success) {
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("旧密码不正确");
            }
        } catch (UserNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteAccount(@AuthenticationPrincipal UserDetailsImpl currentUser) {
        try {
            userService.deleteUser(currentUser.getId());
            return ResponseEntity.noContent().build();
        } catch (UserNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }


}