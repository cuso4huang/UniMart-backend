package org.jnu.unimart.repository;

import org.jnu.unimart.pojo.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {
    User findByUserName(String userName);

    @Query("SELECT u FROM User u WHERE u.account = :account")
    User findUsersByAccount(@Param("account") String account);

}
