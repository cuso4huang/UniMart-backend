package org.jnu.unimart.pojo;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private int productId;

    @NotNull
    @Column(name = "seller_id")
    private int sellID;

    @NotBlank
    @Column(name = "product_name")
    private String productName;

    @NotBlank
    @Column(name = "production_description")
    private String productDescription;

    // 多对一关系，一个商品有一个分类，一个分类有多个商品
    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "category_id", nullable = false)
    private Category category;

    // 多对多关系：一个商品可以有多个标签，一个标签可以有多个商品
    @ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinTable(
            name = "product_tags",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private List<Tag> tags = new LinkedList<>(); // 商品和标签的多对多关系

    @Column(name = "price")
    private double price;
    @Column(name = "publish_status")
    private int publishStatus; // 商品状态：0：上架，1：下架 2：已售
    @Column(name = "image")
    private String image;
    @Column(name = "publish_time")
    private LocalDateTime publishTime;
    @Column(name = "view_count", nullable = false)
    private Integer viewCount = 0; // 浏览次数


    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    @NotNull
    public int getSellID() {
        return sellID;
    }

    public void setSellID(@NotNull int sellID) {
        this.sellID = sellID;
    }

    public @NotBlank String getProductName() {
        return productName;
    }

    public void setProductName(@NotBlank String productName) {
        this.productName = productName;
    }

    public @NotBlank String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(@NotBlank String productDescription) {
        this.productDescription = productDescription;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
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

    public Integer getViewCount() {
        return viewCount;
    }

    public void setViewCount(Integer viewCount) {
        this.viewCount = viewCount;
    }

    @Override
    public String toString() {
        return "Product{" +
                "productId=" + productId +
                ", sellID=" + sellID +
                ", productName='" + productName + '\'' +
                ", productDescription='" + productDescription + '\'' +
                ", category=" + category +
                ", tags=" + tags +
                ", price=" + price +
                ", publishStatus=" + publishStatus +
                ", image='" + image + '\'' +
                ", publishTime=" + publishTime +
                ", viewCount=" + viewCount +
                '}';
    }
}
