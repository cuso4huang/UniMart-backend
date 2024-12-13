package org.jnu.unimart.service;

import jakarta.transaction.Transactional;
import org.jnu.unimart.pojo.Review;

import java.util.List;

public interface ReviewService {
    @Transactional
    Review createReview(Integer transactionId, Integer fromUserId, Integer toUserId, Integer rating, String comment, boolean isAnonymous, List<String> imageUrls);

    List<Review> getUserReviews(Integer userId);

    List<Review> getOrderReviews(Integer transactionId);

    /**
     * 检查用户是否参与了指定订单
     */
    boolean isUserPartOfOrder(Integer transactionId, Integer userId);
}
