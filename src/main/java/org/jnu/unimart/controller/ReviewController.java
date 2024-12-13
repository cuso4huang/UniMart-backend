package org.jnu.unimart.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.jnu.unimart.payload.ReviewRequest;
import org.jnu.unimart.pojo.Review;
import org.jnu.unimart.pojo.ReviewImage;
import org.jnu.unimart.security.UserDetailsImpl;
import org.jnu.unimart.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    /**
     * 创建新评价，仅已登录的用户（ROLE_USER 或 ROLE_ADMIN）可以创建
     *
     * @param reviewRequest 需要创建的评价信息
     * @param user          当前已认证的用户信息
     * @return 返回创建的评价信息，并设置 201 状态
     */
    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Review> createReview(
            @Valid @RequestBody ReviewRequest reviewRequest,
            @AuthenticationPrincipal UserDetailsImpl user) {

        // 创建 Review 实体
        Review review = new Review();
        review.setTransactionId(reviewRequest.getTransactionId());
        review.setToUserId(reviewRequest.getToUserId());
        review.setRating(reviewRequest.getRating());
        review.setComment(reviewRequest.getComment());
        review.setAnonymous(reviewRequest.isAnonymous());
        review.setFromUserId(user.getId());

        // 处理 imageUrls（如果有）
        if (reviewRequest.getImageUrls() != null && !reviewRequest.getImageUrls().isEmpty()) {
            List<ReviewImage> images = reviewRequest.getImageUrls().stream()
                    .map(url -> {
                        ReviewImage image = new ReviewImage();
                        image.setImageUrl(url);
                        image.setReview(review);
                        return image;
                    })
                    .collect(Collectors.toList());
            review.setImages(images);
        }

        // 调用 Service 层创建评价
        Review createdReview = reviewService.createReview(review.getTransactionId(),
                review.getFromUserId(),user.getId(),review.getRating(),review.getComment(),
                review.getAnonymous(),reviewRequest.getImageUrls());

        return ResponseEntity.status(HttpStatus.CREATED).body(createdReview);
    }

    /**
     * 获取用户收到的所有评价
     * 需要用户认证，且只能查看自己的评价或管理员查看任何用户的评价
     */
    @GetMapping("/user/{userId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<Review>> getUserReviews(
            @PathVariable Integer userId,
            @AuthenticationPrincipal UserDetails currentUser) {

        // 获取当前用户ID
        Integer currentUserId = getCurrentUserId(currentUser);

        // 如果当前用户不是管理员，也只能查看自己的评价
        if (!isAdmin(currentUser) && !currentUserId.equals(userId)) {
            return ResponseEntity.status(403).build(); // 禁止访问
        }

        List<Review> reviews = reviewService.getUserReviews(userId);

        return ResponseEntity.ok(reviews);
    }

    /**
     * 获取特定订单的所有评价
     * 需要用户认证，且只能查看与自己相关订单的评价或管理员查看任何订单的评价
     */
    @GetMapping("/transaction/{transactionId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<Review>> getOrderReviews(
            @PathVariable Integer transactionId,
            @AuthenticationPrincipal UserDetails currentUser) {

        // 获取当前用户ID
        Integer currentUserId = getCurrentUserId(currentUser);

        // 校验当前用户是否参与了该订单或是管理员
        if (!reviewService.isUserPartOfOrder(transactionId, currentUserId) && !isAdmin(currentUser)) {
            return ResponseEntity.status(403).build(); // 禁止访问
        }

        List<Review> reviews = reviewService.getOrderReviews(transactionId);

        return ResponseEntity.ok(reviews);
    }

    // 辅助方法：获取当前用户ID
    private Integer getCurrentUserId(UserDetails userDetails) {
        // 假设UserDetails中包含用户ID，可以根据实际情况调整
        // 例如，如果UserDetails实现了自定义的接口包含userId
        if (userDetails instanceof org.jnu.unimart.security.UserDetailsImpl) {
            return ((org.jnu.unimart.security.UserDetailsImpl) userDetails).getId();
        }
        throw new RuntimeException("Unable to retrieve user ID from UserDetails");
    }

    // 辅助方法：检查当前用户是否为管理员
    private boolean isAdmin(UserDetails userDetails) {
        return userDetails.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));
    }


}
