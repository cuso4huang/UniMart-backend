package org.jnu.unimart.repository;


import jakarta.validation.constraints.NotBlank;
import org.jnu.unimart.pojo.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Integer> {

    Optional<Category> findByCategoryName(@NotBlank String categoryName);
}
