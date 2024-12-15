package org.jnu.unimart.controller;

import jakarta.validation.Valid;
import org.jnu.unimart.pojo.Product;
import org.jnu.unimart.security.UserDetailsImpl;
import org.jnu.unimart.service.ProductService;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    // 构造函数注入
    public ProductController(ProductService productService) {
        this.productService = productService;
    }



    /**
     * 查询所有可购买的产品（未售出）
     */
    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = productService.getAvailableProducts();
        return ResponseEntity.ok(products);
    }

    /**
     * 创建新产品，仅管理员和已登录的普通用户可以创建
     * 在请求中，必须提供有效的产品信息。创建成功后返回 201 状态和新产品信息
     * @param product 新创建的产品
     * @param currentUser 当前已认证的用户信息
     * @return 返回创建的产品信息，并设置 201 状态
     */
    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<Product> createProduct(
            @Valid @RequestBody Product product,
            @AuthenticationPrincipal UserDetailsImpl currentUser) {

        // 设置卖家 ID 为当前用户的 ID
        product.setSellID(currentUser.getId());

        Product createdProduct = productService.createProduct(product);
        return ResponseEntity.status(201).body(createdProduct);
    }

    /**
     * 更新产品信息，仅管理员或产品的卖家本人可以更新
     * 在请求中必须提供产品的 ID 和更新后的数据。
     *
     * @param id 要更新的产品 ID
     * @param product 更新后的产品信息
     * @param currentUser 当前登录的用户信息（从 JWT 中提取）
     * @return 返回更新后的产品信息，如果产品不存在则返回 404 Not Found
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or @productController.isProductOwner(#id, authentication)")
    public ResponseEntity<Product> updateProduct(@PathVariable int id, @Valid @RequestBody Product product, @AuthenticationPrincipal UserDetailsImpl currentUser) {
        // 查询产品，确保产品存在
        Product productById = productService.getProductById(id);
        // 确保产品 ID 与请求中的 ID 保持一致
        product.setProductId(id);

        // 调用 Service 层更新产品信息
        Product updatedProduct = productService.updateProduct(product);

        // 返回更新后的产品信息
        return ResponseEntity.ok(updatedProduct);

    }

    /**
     * 判断当前用户是否是该产品的卖家
     * 通过从 JWT 中获取的用户信息与产品的卖家 ID 比较
     *
     * @param productId 产品 ID
     * @param authentication 当前用户的认证信息（从 JWT 获取）
     * @return 如果是卖家或管理员返回 true，否则返回 false
     */
    public boolean isProductOwner(int productId, Authentication authentication) {
        // 获取当前用户的详细信息
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        // 获取产品信息
        Product product = productService.getProductById(productId);

        // 检查当前用户是否是该产品的卖家
        return product != null && (userDetails.getId()==product.getSellID());
    }

    /**
     * 查询单个产品，通过产品 ID 查找，开放给所有人
     * 如果产品不存在，则返回 404 错误
     * @param id 产品的唯一标识符
     * @return 返回找到的产品信息，若产品不存在则抛出 ProductNotFoundException
     */
    @GetMapping("/{id}")
    @PreAuthorize("permitAll()")
    public ResponseEntity<Product> getProductById(@PathVariable int id) {
        Product product = productService.getProductById(id);
        return ResponseEntity.ok(product);
    }


    /**
     * 删除产品，仅管理员或产品的卖家本人可以删除
     * 在请求中必须提供产品的 ID。
     *
     * @param id 要删除的产品 ID
     * @param currentUser 当前登录的用户信息（从 JWT 中提取）
     * @return 返回删除状态信息，如果产品不存在则返回 404 Not Found
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or @productController.isProductOwner(#id, authentication)")
    public ResponseEntity<Void> deleteProduct(@PathVariable int id, @AuthenticationPrincipal UserDetailsImpl currentUser) {
        // 检查产品是否存在
        Product productById = productService.getProductById(id);
        // 删除产品
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();  // 返回 204 状态表示删除成功

    }


    /**
     * 根据名称模糊查询，支持排序，开放给所有人
     *
     * @param keyword 搜索关键词
     * @param sortBy 排序字段（默认为 `productId`）
     * @param sortDir 排序方向（默认为 `asc`）
     * @return 返回符合条件的所有商品列表
     */
    @GetMapping("/search")
    @PreAuthorize("permitAll()")
    public ResponseEntity<List<Product>> searchProducts(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "productId") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {

        // 根据排序方向创建 Sort 对象
        Sort sort = sortDir.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        // 执行商品搜索并获取结果列表
        List<Product> products = productService.searchProducts(keyword, sort);

        // 返回搜索结果
        return ResponseEntity.ok(products);
    }

    /**
     * 根据分类ID查询产品
     */
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<Product>> getProductsByCategory(@PathVariable Integer categoryId) {
        List<Product> products = productService.getProductsByCategory(categoryId);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/my")
    public ResponseEntity<List<Product>> getUserProducts(@AuthenticationPrincipal UserDetailsImpl currentUser) {
        try {
            List<Product> products = productService.getProductsBySellerId(currentUser.getId());
            return ResponseEntity.ok(products);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
