package org.jnu.unimart.pojo;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "products")
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

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getSellID() {
        return sellID;
    }

    public void setSellID(int sellID) {
        this.sellID = sellID;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getPublishStatus() {
        return publishStatus;
    }

    public void setPublishStatus(int publishStatus) {
        this.publishStatus = publishStatus;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public LocalDateTime getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(LocalDateTime publishTime) {
        this.publishTime = publishTime;
    }

    public String getViewCount() {
        return viewCount;
    }

    public void setViewCount(String viewCount) {
        this.viewCount = viewCount;
    }

    @Override
    public String toString() {
        return "Product{" +
                "productId=" + productId +
                ", sellID=" + sellID +
                ", productName='" + productName + '\'' +
                ", productDescription='" + productDescription + '\'' +
                ", categoryId=" + categoryId +
                ", price=" + price +
                ", publishStatus=" + publishStatus +
                ", image='" + image + '\'' +
                ", publishTime=" + publishTime +
                ", viewCount='" + viewCount + '\'' +
                '}';
    }
}
