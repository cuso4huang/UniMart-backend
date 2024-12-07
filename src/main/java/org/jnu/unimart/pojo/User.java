package org.jnu.unimart.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users") // 确保表名为 "users"
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id") // 数据库中的列名
    private int userId;

    @Column(name = "username", nullable = false) // 确保非空
    private String userName;

    @Column(name = "password", nullable = false)
    @JsonIgnore // 序列化时忽略密码字段
    private String userPassword;

    @Column(name = "account", nullable = false, unique = true) // 电话或邮箱，确保唯一
    private String account;

    @Column(name = "student_status", nullable = false)
    private int studentStatus; // 状态 0：未认证，1：认证

    @Column(name = "personal_rating_buyer")
    private double personalRatingBuyer; // 作为买家的评分

    @Column(name = "personal_rating_seller")
    private double personalRatingSeller; // 作为卖家的评分

    @Column(name = "avatar")
    private String avatar; // 头像 URL

    @Column(name = "address")
    private String address; // 地址

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"), // 与数据库列名一致
            inverseJoinColumns = @JoinColumn(name = "role_id") // 与数据库列名一致
    )
    private Set<Role> roles = new HashSet<>();

    // Constructors

    public User() {}

    public User(String userName, String userPassword, String account, int studentStatus,
                double personalRatingBuyer, double personalRatingSeller, String avatar, String address) {
        this.userName = userName;
        this.userPassword = userPassword;
        this.account = account;
        this.studentStatus = studentStatus;
        this.personalRatingBuyer = personalRatingBuyer;
        this.personalRatingSeller = personalRatingSeller;
        this.avatar = avatar;
        this.address = address;
    }

    // Getters and Setters

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

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
}
