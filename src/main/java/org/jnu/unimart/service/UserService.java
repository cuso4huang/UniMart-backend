package org.jnu.unimart.service;

import org.jnu.unimart.pojo.User;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface UserService {
    public List<User> getAllUsers();
    public Optional<User> getUserById(int id);
    public Optional<User> getUserByUsername(String userName);
//    public boolean createUser(String userName, String userPassword, String account);
    public User updateUser(User user);
    public void deleteUser(int id);
    public Optional<User> getUserByAccount(String account);

}
