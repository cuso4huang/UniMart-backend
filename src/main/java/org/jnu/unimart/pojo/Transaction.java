package org.jnu.unimart.pojo;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tansaction_id")
    private int transactionId;
    @Column(name = "product_id")
    private int productId;
    @Column(name = "buyer_id")
    private int buyerId;
    @Column(name = "seller_id")
    private int sellerId;
    @Column(name = "transaction_status")
    private int transactionStatus; // 交易状态 ：0：待支付，1：已支付，2：已完成
    @Column(name = "transaction_time")
    private LocalDateTime transactionTime;
    @Column(name = "payment")
    private int payment; // 交易方式 ： 0：微信，1：支付宝
    @Column(name = "total_amount")
    private double totalAmount; // 总金额

    public int getSellerId() {
        return sellerId;
    }

    public void setSellerId(int sellerId) {
        this.sellerId = sellerId;
    }

    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(int buyerId) {
        this.buyerId = buyerId;
    }

    public int getTransactionStatus() {
        return transactionStatus;
    }

    public void setTransactionStatus(int transactionStatus) {
        this.transactionStatus = transactionStatus;
    }

    public LocalDateTime getTransactionTime() {
        return transactionTime;
    }

    public void setTransactionTime(LocalDateTime transactionTime) {
        this.transactionTime = transactionTime;
    }

    public int getPayment() {
        return payment;
    }

    public void setPayment(int payment) {
        this.payment = payment;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "transactionId=" + transactionId +
                ", productId=" + productId +
                ", buyerId=" + buyerId +
                ", sellerId=" + sellerId +
                ", transactionStatus=" + transactionStatus +
                ", transactionTime=" + transactionTime +
                ", payment=" + payment +
                ", totalAmount=" + totalAmount +
                '}';
    }
}
