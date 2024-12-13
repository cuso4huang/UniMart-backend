package org.jnu.unimart.service.impl;

import jakarta.transaction.Transactional;
import org.jnu.unimart.exception.UserNotFoundException;
import org.jnu.unimart.pojo.Review;
import org.jnu.unimart.pojo.ReviewImage;
import org.jnu.unimart.pojo.Transaction;
import org.jnu.unimart.repository.ReviewRepository;
import org.jnu.unimart.repository.TransactionRepository;
import org.jnu.unimart.service.ReviewService;
import org.jnu.unimart.service.TransactionService;
import org.jnu.unimart.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewServiceImpl implements ReviewService {
    @Autowired
    private ReviewRepository reviewRepository;
    private UserService userService;
    private TransactionService transactionService;
    @Autowired
    private TransactionRepository transactionRepository;

    public ReviewServiceImpl(UserService userService, TransactionService transactionService) {
        this.userService = userService;
        this.transactionService = transactionService;
    }

    /**
     * 创建评价
     * @param transactionId
     * @param fromUserId
     * @param toUserId
     * @param rating
     * @param comment
     * @param isAnonymous
     * @param imageUrls
     * @return
     */
    @Transactional
    @Override
    public Review createReview(Integer transactionId, Integer fromUserId, Integer toUserId, Integer rating, String comment, boolean isAnonymous, List<String> imageUrls) {
        // 校验订单状态、校验用户是否有权评价等
        isExistUser(fromUserId);
        isExistUser(toUserId);
        isCompleted(transactionId);
        havePrivilege( fromUserId, toUserId,transactionId);
        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5.");
        }


        Review review = new Review();
        review.setTransactionId(transactionId);
        review.setFromUserId(fromUserId);
        review.setToUserId(toUserId);
        review.setRating(rating);
        review.setComment(comment);
        review.setAnonymous(isAnonymous);

        Review saved = reviewRepository.save(review);

        if (imageUrls != null && !imageUrls.isEmpty()) {
            List<ReviewImage> images = imageUrls.stream().map(url -> {
                ReviewImage img = new ReviewImage();
                img.setReview(saved);
                img.setImageUrl(url);
                return img;
            }).toList();
            saved.setImages(images);
            reviewRepository.save(saved);
        }

        return saved;
    }

    /**
     * 获取用户的评价表
     * @param userId
     * @return
     */
    @Transactional
    @Override
    public List<Review> getUserReviews(Integer userId) {
        return reviewRepository.findByToUserIdOrderByCreateTimeDesc(userId);
    }


    /**
     * 通过商品id获取评价表
     * @param transactionId
     * @return
     */
    @Transactional
    @Override
    public List<Review> getOrderReviews(Integer transactionId) {
        return reviewRepository.findByTransactionId(transactionId);
    }


    private void isExistUser(Integer userId) {
        if(userService.getUserById(userId).isEmpty())
            throw new UserNotFoundException("User not found with id " + userId);
    }
    private  void isCompleted(Integer transactionId) {
        Transaction transaction = transactionService.getTransactionById(transactionId);
        if (!transaction.isCompleted()) {
            throw new IllegalStateException("Order is not completed. Cannot review yet.");
        }
    }

    private  void havePrivilege(Integer fromUserId, Integer toUserId, Integer transactionId) {
        Transaction transaction = transactionService.getTransactionById(transactionId);
        // 确保fromUser是订单中的参与者之一
        if (transaction.getBuyer().getUserId()!=fromUserId && transaction.getSeller().getUserId()!=fromUserId) {
            throw new SecurityException("You are not a participant in this order.");
        }

// 确保toUserId是另一个参与者
        if (transaction.getBuyer().getUserId()!=toUserId && transaction.getSeller().getUserId()!=toUserId) {
            throw new IllegalArgumentException("The user you are reviewing is not part of this order.");
        }

// 检查是否已存在此用户对该订单的评价
        List<Review> existingReviews = reviewRepository.findByTransactionId(transactionId);
        boolean alreadyReviewed = existingReviews.stream().anyMatch(r -> r.getFromUserId().equals(fromUserId));
        if (alreadyReviewed) {
            throw new IllegalStateException("You have already reviewed this order.");
        }

    }

    @Override
    public boolean isUserPartOfOrder(Integer transactionId, Integer userId) {
        Transaction order = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        return order.getBuyer().getUserId()==userId || order.getSeller().getUserId()==userId;
    }
}