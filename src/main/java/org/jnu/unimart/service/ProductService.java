package org.jnu.unimart.service;

import org.jnu.unimart.pojo.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    Product createProduct(Product product);
    Product getProductById(int id);
    Product updateProduct(Product product);
    void deleteProduct(int id);

    List<Product> getAllProducts();

    List<Product> searchProducts(String keyword, Sort sort);
}
