package org.jnu.unimart.pojo;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private int productId;
    @Column(name = "seller_id")
    private int sellID;
    @Column(name = "product_name")
    private String productName;
    @Column(name = "production_description")
    private String productDescription;
    @Column(name = "tategory_id")
    private int categoryId;
    @Column(name = "price")
    private double price;
    @Column(name = "publish_status")
    private int publishStatus; // 商品状态：0：上架，1：下架 2：已售
    @Column(name = "image")
    private String image;
    @Column(name = "publish_time")
    private LocalDateTime publishTime;
    @Column(name = "view_count")
    private String viewCount; // 浏览次数
}
