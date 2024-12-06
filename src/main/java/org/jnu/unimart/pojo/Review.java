package org.jnu.unimart.pojo;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "reviews")
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

    public int getReviewId() {
        return reviewId;
    }

    public void setReviewId(int reviewId) {
        this.reviewId = reviewId;
    }

    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    public String getReviewContentBuyer() {
        return reviewContentBuyer;
    }

    public void setReviewContentBuyer(String reviewContentBuyer) {
        this.reviewContentBuyer = reviewContentBuyer;
    }

    public double getBuyerRating() {
        return buyerRating;
    }

    public void setBuyerRating(double buyerRating) {
        this.buyerRating = buyerRating;
    }

    public String getReviewContentSeller() {
        return reviewContentSeller;
    }

    public void setReviewContentSeller(String reviewContentSeller) {
        this.reviewContentSeller = reviewContentSeller;
    }

    public double getSellerRating() {
        return sellerRating;
    }

    public void setSellerRating(double sellerRating) {
        this.sellerRating = sellerRating;
    }

    public LocalDateTime getReviewTime() {
        return reviewTime;
    }

    public void setReviewTime(LocalDateTime reviewTime) {
        this.reviewTime = reviewTime;
    }

    public String toString() {
        return "Review{" +
                "reviewId=" + reviewId +
                ", transactionId=" + transactionId +
                ", reviewContentBuyer='" + reviewContentBuyer + '\'' +
                ", buyerRating=" + buyerRating +
                ", reviewContentSeller='" + reviewContentSeller + '\'' +
                ", sellerRating=" + sellerRating +
                ", reviewTime=" + reviewTime +
                '}';
    }
}
