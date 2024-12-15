package org.jnu.unimart.service.impl;

import jakarta.transaction.Transactional;
import org.jnu.unimart.exception.UserNotFoundException;
import org.jnu.unimart.pojo.User;
import org.jnu.unimart.repository.RoleRepository;
import org.jnu.unimart.repository.UserRepository;
import org.jnu.unimart.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }


    /**
     * 获取所有user信息
     *
     * @return
     */
    @Transactional
    @Override
    public List<User> getAllUsers() {
        // 密码设为空再传递
        // 由于 @JsonIgnore 已在 User 实体类中隐藏密码，这里无需额外处理
        return userRepository.findAll();
    }

    @Override
    public boolean changePassword(int userId, String oldPassword, String newPassword) {
        // 1. 获取用户
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            throw new UserNotFoundException("用户不存在");
        }

        User user = userOpt.get();

        // 2. 验证旧密码是否正确
        if (!passwordEncoder.matches(oldPassword, user.getUserPassword())) {
            return false;
        }

        // 3. 加密新密码并更新
        String encodedNewPassword = passwordEncoder.encode(newPassword);
        user.setUserPassword(encodedNewPassword);

        // 4. 保存更新
        userRepository.save(user);

        return true;
    }

    /**
     * 通过id获取用户信息，
     * Optional<User> 当可以接受一个null
     * @param id
     * @return
     */
    @Transactional
    @Override
    public Optional<User> getUserById(int id) {
        // 由于 @JsonIgnore 已在 User 实体类中隐藏密码，这里无需额外处理
        return userRepository.findById(id);
    }

    /**
     * 通过用户名获取用户信息
     * @param userName
     * @return
     */
    @Transactional
    @Override
    public Optional<User> getUserByUsername(String userName) {
        Optional<User> userByUsername = userRepository.findByUserName(userName);
        if(userByUsername.isPresent()) {
            return userByUsername;
        }
        else {
            throw new UserNotFoundException("User not found with username: " + userName);
        }
    }

    /**
     * 通过账户获得用户信息
     * @param account
     * @return
     */
    @Transactional
    @Override
    public Optional<User> getUserByAccount(String account) {
        Optional<User> userByAccount = userRepository.findUserByAccount(account);
        if (userByAccount.isPresent()) {
            return userByAccount;
        }
        else {
            throw new UserNotFoundException("User not found account:"+account);
        }
    }


    // 感觉这个方法可以不用
//    /**
//     * 创建用户
//     * @param userName
//     * @param userPassword
//     * @param account
//     * @return
//     */
//    @Transactional
//    @Override
//    public boolean createUser(String userName, String userPassword, String account) {
//        if (userRepository.findUserByAccount(account) != null) {
//            System.out.println(userRepository.findUserByAccount(account));
//            throw new UserNotFoundException("User already exists");
//        }
//        String encode = passwordEncoder.encode(userPassword);
//        System.out.println(encode);
//        User user = new User();
//        user.setUserName(userName);
//        user.setUserPassword(encode);
//        user.setAccount(account);
//        user.setRoles(Collections.singleton(roleRepository.findByName("ROLE_USER").get()));
//        userRepository.save(user);
//        return true;
//    }

    /**
     * 更新用户信息
     * @param user 用户实体
     * @return 更新后的用户
     */
    @Transactional
    @Override
    public User updateUser(User user) {
        // 检查用户是否存在
        Optional<User> optionalExistingUser = userRepository.findById(user.getUserId());
        if (optionalExistingUser.isEmpty()) {
            throw new UserNotFoundException("User with ID " + user.getUserId() + " not found");
        }

        User existingUser = optionalExistingUser.get();

        // 更新用户名
        if (user.getUserName() != null && !user.getUserName().isEmpty()) {
            existingUser.setUserName(user.getUserName());
        }

        // 更新账户（电话或邮箱）
        if (user.getAccount() != null && !user.getAccount().isEmpty()) {
            existingUser.setAccount(user.getAccount());
        }

        // 更新学生状态
        if (user.getStudentStatus() != existingUser.getStudentStatus()) {
            existingUser.setStudentStatus(user.getStudentStatus());
        }

        // 更新买家评分
        if (user.getPersonalRatingBuyer() != existingUser.getPersonalRatingBuyer()) {
            existingUser.setPersonalRatingBuyer(user.getPersonalRatingBuyer());
        }

        // 更新卖家评分
        if (user.getPersonalRatingSeller() != existingUser.getPersonalRatingSeller()) {
            existingUser.setPersonalRatingSeller(user.getPersonalRatingSeller());
        }

        // 更新头像
        if (user.getAvatar() != null && !user.getAvatar().isEmpty()) {
            existingUser.setAvatar(user.getAvatar());
        }

        // 更新地址
        if (user.getAddress() != null && !user.getAddress().isEmpty()) {
            existingUser.setAddress(user.getAddress());
        }

        // 更新密码（如果提供了新密码）
        if (user.getUserPassword() != null && !user.getUserPassword().isEmpty()) {
            String encryptedPassword = passwordEncoder.encode(user.getUserPassword());
            existingUser.setUserPassword(encryptedPassword);
        }

        // 保存更新后的用户
        return userRepository.save(existingUser);
    }


    /**
     * 删除用户
     * @param id 用户ID
     */
    @Transactional
    @Override
    public void deleteUser(int id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException("User with ID " + id + " not found");
        }
        userRepository.deleteById(id);
    }

}
