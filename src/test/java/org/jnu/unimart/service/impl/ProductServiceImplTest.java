package org.jnu.unimart.service.impl;

import jakarta.transaction.Transactional;
import org.jnu.unimart.pojo.Category;
import org.jnu.unimart.pojo.Product;
import org.jnu.unimart.pojo.Tag;
import org.jnu.unimart.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ProductServiceImplTest {

    @Autowired
    private ProductService service;

    @Test
    void createProduct() {
        Product product = new Product();
        product.setProductName("笔记本电脑");
        product.setProductDescription("联想小新");
        Category category = new Category();
        category.setCategoryName("电子产品");
        product.setCategory(category);
        Tag tag = new Tag();
        tag.setTagName("全新未拆封");
        product.setTags(List.of(tag));
        product.setSellID(11);
        Product product1 = service.createProduct(product);
        System.out.println(product1);

    }
//    @Transactional // 确保在事务中运行
    @Test
    void getProductById() {
        Product product = service.getProductById(3);
        System.out.println(product);
    }

//    @Transactional
    @Test
    void updateProduct() {
        Product product = service.getProductById(4);
        Set<Tag> tags = new HashSet<Tag>(product.getTags());
        Tag tag = new Tag();
        tag.setTagName("九九新");
        tags.add(tag);
        product.setTags(List.of(tag));
        Product product1 = service.updateProduct(product);
        System.out.println(product1);
    }

    @Test
    void deleteProduct() {
        Product product = service.getProductById(3);
        service.deleteProduct(3);
    }
}