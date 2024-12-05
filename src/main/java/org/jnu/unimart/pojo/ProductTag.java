package org.jnu.unimart.pojo;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class ProductTag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private int productId;
    @Column(name = "tag_id")
    private int tagId;
}
