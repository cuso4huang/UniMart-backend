// src/main/java/org/jnu/unimart/repository/RoleRepository.java

package org.jnu.unimart.repository;

import org.jnu.unimart.pojo.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByName(String name);
}
