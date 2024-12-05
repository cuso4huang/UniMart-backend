package org.jnu.unimart.pojo;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class Review{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private int reviewId;
    @Column(name = "transaction_id")
    private int transactionId;
    @Column(name = "review_content_buyer")
    private String reviewContentBuyer; //买家评价内容
    @Column(name = "buyer_rating")
    private double buyerRating; // 买家评分
    @Column(name = "review_content_seller")
    private String reviewContentSeller;//卖家评价内容
    @Column(name = "seller_rating")
    private double sellerRating; //卖家评分
    @Column(name = "review_time")
    private LocalDateTime reviewTime; //评分时间
}
