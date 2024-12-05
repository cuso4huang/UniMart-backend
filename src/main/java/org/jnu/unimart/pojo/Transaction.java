package org.jnu.unimart.pojo;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
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
}
