package org.jnu.unimart.controller;


import org.jnu.unimart.pojo.User;
import org.jnu.unimart.service.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable int id) {
        // 返回User对象，自动转化为JSON格式
        return userService.getUserById(id).orElseThrow(()->new RuntimeException("User not found"));
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        return userService.createUser(user);
    }

}
