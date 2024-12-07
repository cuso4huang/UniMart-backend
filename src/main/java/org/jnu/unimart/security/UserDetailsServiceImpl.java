// src/main/java/org/jnu/unimart/security/UserDetailsServiceImpl.java

package org.jnu.unimart.security;

import jakarta.transaction.Transactional;
import org.jnu.unimart.pojo.User;
import org.jnu.unimart.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.stream.Collectors;


@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    
    @Autowired
    UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUserName(username)
                      .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));

        // 获取用户角色信息并转换为 GrantedAuthority
        Collection<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))  // 假设每个用户有一个角色集合
                .collect(Collectors.toList());


        return new UserDetailsImpl(user.getUserName(), user.getUserPassword(), authorities);

    }
}
