package org.jnu.unimart.pojo;

import jakarta.persistence.*;
import lombok.Data;
@Entity
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private int userId;
    @Column(name = "user_name")
    private String userName;
    @Column(name = "user_password")
    private String userPassword;
    @Column(name = "account")
    private String account; // 电话或者邮箱
    @Column(name = "student_status")
    private int studentStatus; // 状态 0：未认证，1：认证
    @Column(name = "personal_rating_buyer")
    private String personalRatingBuyer; // 作为买家的评分
    @Column(name = "personal_rating_seller")
    private String personalRatingSeller; // 作为卖家的评分
    @Column(name = "avatar")
    private String avatar; //头像url
    @Column(name = "address")
    private String address; //地址
}
