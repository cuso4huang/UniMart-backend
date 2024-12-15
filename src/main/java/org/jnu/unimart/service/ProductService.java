package org.jnu.unimart.service;

import org.jnu.unimart.pojo.Product;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface ProductService {
    List<Product> getAvailableProducts();

    Product createProduct(Product product);
    Product getProductById(int id);
    Product updateProduct(Product product);
    void deleteProduct(int id);

    List<Product> getAllProducts();

    List<Product> searchProducts(String keyword, Sort sort);

    List<Product> getProductsByCategory(Integer categoryId);

    List<Product> getProductsBySellerId(int id);
}
