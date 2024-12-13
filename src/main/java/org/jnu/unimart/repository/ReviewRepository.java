package org.jnu.unimart.repository;

import org.jnu.unimart.pojo.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Integer> {
    List<Review> findByToUserIdOrderByCreateTimeDesc(Integer toUserId);
    List<Review> findByTransactionId(Integer transactionId);
}


