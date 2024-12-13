package org.jnu.unimart.service.impl;

import org.jnu.unimart.pojo.Review;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ReviewServiceImplTest {

    @Autowired
    private ReviewServiceImpl reviewService;
    @Test
    void createReview() {
        Review review = reviewService.createReview(8, 11, 11, 5, "很好", false, new ArrayList<>());
        assertNotNull(review);
    }

    @Test
    void getUserReviews() {
        List<Review> userReviews = reviewService.getUserReviews(11);
        for (Review review : userReviews) {
            System.out.println(review);
        }
    }

    @Test
    void getOrderReviews() {
        List<Review> orderReviews = reviewService.getOrderReviews(8);
        for (Review review : orderReviews) {
            System.out.println(review);
        }
    }
}