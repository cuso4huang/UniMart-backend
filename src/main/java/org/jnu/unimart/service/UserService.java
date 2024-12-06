package org.jnu.unimart.service;

import org.jnu.unimart.pojo.User;
import org.jnu.unimart.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    // Optional<User> 当可以接受一个null
    public Optional<User> getUserById(int id) {
        return userRepository.findById(id);
    }
    public Optional<User> getUserByUsername(String userName) {
        return Optional.ofNullable(userRepository.findByUserName(userName));
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }
    public User updateUser(User user) {
        return userRepository.save(user);
    }
    public void deleteUser(int id) {
        userRepository.deleteById(id);
    }

}
