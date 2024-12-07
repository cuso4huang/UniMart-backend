package org.jnu.unimart.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private int userId;
    @Column(name = "username")
    private String userName;
    @Column(name = "password")
    @JsonIgnore // 这个注解将会在序列化时忽略这个字段
    private String userPassword;
    @Column(name = "account")
    private String account; // 电话或者邮箱
    @Column(name = "student_status")
    private int studentStatus; // 状态 0：未认证，1：认证
    @Column(name = "personal_rating_buyer")
    private double personalRatingBuyer; // 作为买家的评分
    @Column(name = "personal_rating_seller")
    private double personalRatingSeller; // 作为卖家的评分
    @Column(name = "avatar")
    private String avatar; //头像url
    @Column(name = "address")
    private String address; //地址

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public int getStudentStatus() {
        return studentStatus;
    }

    public void setStudentStatus(int studentStatus) {
        this.studentStatus = studentStatus;
    }

    public double getPersonalRatingBuyer() {
        return personalRatingBuyer;
    }

    public void setPersonalRatingBuyer(double personalRatingBuyer) {
        this.personalRatingBuyer = personalRatingBuyer;
    }

    public double getPersonalRatingSeller() {
        return personalRatingSeller;
    }

    public void setPersonalRatingSeller(double personalRatingSeller) {
        this.personalRatingSeller = personalRatingSeller;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", userName='" + userName + '\'' +
                ", userPassword='" + userPassword + '\'' +
                ", account='" + account + '\'' +
                ", studentStatus=" + studentStatus +
                ", personalRatingBuyer=" + personalRatingBuyer +
                ", personalRatingSeller=" + personalRatingSeller +
                ", avatar='" + avatar + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
