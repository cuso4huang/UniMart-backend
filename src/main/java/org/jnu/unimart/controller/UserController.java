//package org.jnu.unimart.controller;
//
//
//import org.jnu.unimart.pojo.User;
//import org.jnu.unimart.service.impl.UserServiceImpl;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/user")
//public class UserController {
//
//    private final UserServiceImpl userService;
//
//    public UserController(UserServiceImpl userService) {
//        this.userService = userService;
//    }
//
//    //todo : 完善代码
//    @GetMapping("/{id}")
//    public User getUserById(@PathVariable int id) {
//        // 返回User对象，自动转化为JSON格式
//        return userService.getUserById(id).orElseThrow(()->new RuntimeException("User not found"));
//    }
//
////    @PostMapping
////    public User createUser(@RequestBody User user) {
////        return userService.createUser(user);
////    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteUserById(@PathVariable int id){
//        userService.deleteUser(id);
//        if(userService.getUserById(id).isPresent()){
//            return ResponseEntity.noContent().build(); // 返回204 no content
//        }
//        return ResponseEntity.notFound().build(); // 返回404 no found
//    }
//
//    @PutMapping("/{id}")
//    public ResponseEntity<User> updateUser(@PathVariable int id, @RequestBody User user) {
//        return userService.updateUser(id, user);
//    }
//
//}
