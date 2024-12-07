package org.jnu.unimart.service.impl;

import jakarta.transaction.Transactional;
import org.jnu.unimart.pojo.User;
import org.jnu.unimart.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

@SpringBootTest
@ActiveProfiles("test") // 指定使用application-test.properties
@Transactional // 确保每个测试方法结束后事务回滚，保持测试环境干净
class UserServiceImplTest {

    @Autowired
    private UserServiceImpl userServiceImpl;

    @Autowired
    private UserRepository userRepository;

    @Test
    void getAllUsers() {

        List<User> users = userServiceImpl.getAllUsers();
        for (User user : users) {
            System.out.println(user);
        }
    }

    @Test
    void getUserById() {
        Optional<User> userById = userServiceImpl.getUserById(12);
        System.out.println(userById);

    }

    @Test
    void getUserByUsername() {
        Optional<User> userByUsername = userServiceImpl.getUserByUsername("huang");
        System.out.println(userByUsername);
        Optional<User> userByAccount = userServiceImpl.getUserByAccount("znegqin.huang@qq.com");
        System.out.println(userByAccount);
    }

    @Test
    void createUser() {
        System.out.println(userServiceImpl.createUser("test", "123", "1fds23"));
        System.out.println(userRepository.findUserByAccount("1fds23"));
    }

    @Test
    void updateUser() {
        User user = userServiceImpl.getUserById(11).get();
        user.setAccount("zengqin.huang@qq.com");
        user.setUserPassword("cuso4@123");
        User user1 = userServiceImpl.updateUser(user);
        System.out.println(user1);


    }

    @Test
    void deleteUser() {
        userServiceImpl.deleteUser(11);
    }

}