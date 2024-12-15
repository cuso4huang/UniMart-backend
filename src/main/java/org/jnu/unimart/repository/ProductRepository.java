package org.jnu.unimart.repository;

import org.jnu.unimart.pojo.Product;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    @Query("SELECT p FROM Product p LEFT JOIN FETCH p.tags WHERE p.productId = :id")
    Optional<Product> findByIdWithTags(@Param("id") int id);
    List<Product> findAll(); // 返回所有产品
    List<Product> findByProductNameContaining(String keyword,  Sort sort);
    /**
     * 根据分类ID查询商品列表
     * @param categoryId 分类ID
     * @return 商品列表
     */
    List<Product> findByCategoryCategoryId(Integer categoryId);

    List<Product> findByProductIdNotIn(List<Integer> soldProductIds);

    List<Product> findBySellID(int sellerId);
}
