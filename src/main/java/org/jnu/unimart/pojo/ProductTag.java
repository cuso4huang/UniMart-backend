package org.jnu.unimart.pojo;

import jakarta.persistence.*;

@Entity
@Table(name = "product_tags")
public class ProductTag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private int productId;
    @Column(name = "tag_id")
    private int tagId;

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getTagId() {
        return tagId;
    }

    public void setTagId(int tagId) {
        this.tagId = tagId;
    }

    @Override
    public String toString() {
        return "ProductTag{" +
                "productId=" + productId +
                ", tagId=" + tagId +
                '}';
    }
}
