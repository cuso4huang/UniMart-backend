package org.jnu.unimart.service.impl;

import jakarta.transaction.Transactional;
import org.jnu.unimart.enums.TransactionStatus;
import org.jnu.unimart.exception.CategoryNotFoundException;
import org.jnu.unimart.exception.ProductNotFoundException;
import org.jnu.unimart.exception.UserNotFoundException;
import org.jnu.unimart.pojo.*;
import org.jnu.unimart.repository.CategoryRepository;
import org.jnu.unimart.repository.ProductRepository;
import org.jnu.unimart.repository.TagRepository;
import org.jnu.unimart.repository.TransactionRepository;
import org.jnu.unimart.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;


@Service
public class ProductServiceImpl implements ProductService {
    private static final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final TagRepository tagRepository;
    private final CategoryService categoryService;
    private final TagService tagService;
    private final UserService userService; // 使用接口
    private final TransactionRepository transactionRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository,
                              CategoryRepository categoryRepository,
                              TagRepository tagRepository,
                              CategoryService categoryService,
                              TagService tagService,
                              UserService userService,
                              TransactionRepository transactionRepository) { // 使用接口
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.tagRepository = tagRepository;
        this.categoryService = categoryService;
        this.tagService = tagService;
        this.userService = userService;
        this.transactionRepository = transactionRepository;
    }


    @Override
    public List<Product> getAvailableProducts() {
        // 获取所有已支付或已完成的交易
        List<Transaction> soldTransactions = transactionRepository.findByTransactionStatusIn(
                Arrays.asList(TransactionStatus.PAID, TransactionStatus.COMPLETED)
        );

        // 从交易记录中获取商品ID
        List<Integer> soldProductIds = soldTransactions.stream()
                .map(transaction -> transaction.getProduct().getProductId())
                .collect(Collectors.toList());

        // 如果没有已售出的商品，返回所有商品
        if (soldProductIds.isEmpty()) {
            return productRepository.findAll();
        }

        // 返回未售出的商品
        return productRepository.findByProductIdNotIn(soldProductIds);
    }


    /**
     * Creates and saves a new product with the current timestamp.
     *
     * @param product the product to be created, must include sellID, productName, and productDescription
     * @return the saved Product entity
     * @throws CategoryNotFoundException   if the category is invalid or default category not found
     */
    @Transactional
    @Override
    public Product createProduct(Product product) {
        logger.info("Creating product: {}", product.getProductName());


        // 验证产品数据
        validateProduct(product);

        // 获取卖家
        User seller = getSeller(product.getSellID());

        product.setSellID(seller.getUserId());

        // 处理分类
        Category category = handleCategory(product.getCategory());
        product.setCategory(category);
        logger.info("Assigned category: {}", category.getCategoryName());

        // 处理标签
        List<Tag> tags = handleTags(product.getTags());
        product.setTags(tags);
        logger.info("Assigned tags: {}", tags.stream().map(Tag::getTagName).collect(Collectors.joining(", ")));

        // 设置发布时间
        product.setPublishTime(LocalDateTime.now());

        //

        // 保存产品
        Product savedProduct = productRepository.save(product);
        logger.info("Product created with ID: {}", savedProduct.getProductId());

        return savedProduct;
    }

    private void validateProduct(Product product) {
        if (product.getProductName() == null || product.getProductName().trim().isEmpty()) {
            logger.error("Product name is blank");
            throw new IllegalArgumentException("Product name cannot be blank");
        }
        if (product.getProductDescription() == null || product.getProductDescription().trim().isEmpty()) {
            logger.error("Product description is blank");
            throw new IllegalArgumentException("Product description cannot be blank");
        }
    }

    private User getSeller(Integer sellID) {
        return userService.getUserById(sellID)
                .orElseThrow(() -> {
                    logger.error("User with ID {} not found", sellID);
                    return new UserNotFoundException("User with ID " + sellID + " not found");
                });
    }

    private Category handleCategory(Category category) {
        if (category == null || category.getCategoryName() == null || category.getCategoryName().trim().isEmpty()) {
            Category defaultCategory = categoryService.getDefaultCategory();
            return defaultCategory;
        }

        return categoryRepository.findByCategoryName(category.getCategoryName())
                .orElseGet(() -> {
                    Category newCategory = new Category();
                    newCategory.setCategoryName(category.getCategoryName());
                    Category savedCategory = categoryRepository.save(newCategory);
                    logger.info("Created new category: {}", savedCategory.getCategoryName());
                    return savedCategory;
                });
    }

    private List<Tag> handleTags(List<Tag> tags) {
        if (tags == null || tags.isEmpty()) {
            return Collections.emptyList();
        }

        Set<String> tagNames = tags.stream()
                .map(Tag::getTagName)
                .collect(Collectors.toSet());

        List<Tag> existingTags = tagRepository.findByTagNameIn(tagNames);
        Map<String, Tag> existingTagMap = existingTags.stream()
                .collect(Collectors.toMap(Tag::getTagName, Function.identity()));

        List<Tag> resultTags = new LinkedList<>();
        List<Tag> newTags = new ArrayList<>();

        for (Tag tag : tags) {
            Tag existingTag = existingTagMap.get(tag.getTagName());
            if (existingTag != null) {
                resultTags.add(existingTag);
            } else {
                Tag newTag = new Tag();
                newTag.setTagName(tag.getTagName());
                newTags.add(newTag);
            }
        }

        if (!newTags.isEmpty()) {
            List<Tag> savedTags = tagRepository.saveAll(newTags);
            logger.info("Created new tags: {}", savedTags.stream().map(Tag::getTagName).collect(Collectors.joining(", ")));
            resultTags.addAll(savedTags);
        }

        return resultTags;
    }


    @Transactional
    @Override
    public Product getProductById(int id) {
        logger.info("Fetching product with ID: {}", id);
        Optional<Product> byId = productRepository.findById(id);
        if(byId.isEmpty()) {
            logger.error("Product with ID {} not found", id);
            throw new IllegalArgumentException("Product with ID " + id + " not found");
        }
        return byId.get();
    }

    /**
     * Updates an existing product with provided details.
     *
     * @param product the product entity containing updated information
     * @return the updated Product entity
     * @throws ProductNotFoundException  if the product does not exist
     * @throws CategoryNotFoundException if the specified category does not exist
     */
    @Transactional
    @Override
    public Product updateProduct(Product product) {
        logger.info("Updating product with ID: {}", product.getProductId());

        // 获取现有的 Product 实体
        Product existingProduct = productRepository.findById(product.getProductId())
                .orElseThrow(() -> {
                    logger.error("Product not found with ID: {}", product.getProductId());
                    return new ProductNotFoundException("Product not found with ID: " + product.getProductId());
                });

        // 更新各个字段
        updateProductFields(existingProduct, product);

        // 处理分类更新
        existingProduct.setCategory(handleCategoryUpdate(product.getCategory()));
        logger.info("Updated category to: {}", existingProduct.getCategory().getCategoryName());

        // 处理标签更新
        existingProduct.setTags(handleTagsUpdate(product.getTags()));
        logger.info("Updated tags to: {}", existingProduct.getTags().stream().map(Tag::getTagName).collect(Collectors.joining(", ")));

        // 保存更新后的实体
        Product savedProduct = productRepository.save(existingProduct);
        logger.info("Product updated successfully with ID: {}", savedProduct.getProductId());

        return savedProduct;
    }

    private void updateProductFields(Product existingProduct, Product newProduct) {
        if (newProduct.getProductName() != null && !newProduct.getProductName().trim().isEmpty()) {
            existingProduct.setProductName(newProduct.getProductName());
        }
        if (newProduct.getProductDescription() != null && !newProduct.getProductDescription().trim().isEmpty()) {
            existingProduct.setProductDescription(newProduct.getProductDescription());
        }
        if (newProduct.getPrice() > 0) {
            existingProduct.setPrice(newProduct.getPrice());
        }
        if (newProduct.getPublishStatus() >= 0) {
            existingProduct.setPublishStatus(newProduct.getPublishStatus());
        }
        if (newProduct.getImage() != null && !newProduct.getImage().trim().isEmpty()) {
            existingProduct.setImage(newProduct.getImage());
        }
        if (newProduct.getViewCount() >= 0) {
            existingProduct.setViewCount(newProduct.getViewCount());
        }
        if (newProduct.getPublishTime() != null) {
            existingProduct.setPublishTime(newProduct.getPublishTime());
        }
    }

    private Category handleCategoryUpdate(Category category) {
        if (category != null && category.getCategoryName() != null && !category.getCategoryName().trim().isEmpty()) {
            return categoryRepository.findByCategoryName(category.getCategoryName())
                    .orElseThrow(() -> {
                        logger.error("Category not found with name: {}", category.getCategoryName());
                        return new CategoryNotFoundException("Category not found with name: " + category.getCategoryName());
                    });
        } else {
            return categoryService.getDefaultCategory();
        }
    }

    private List<Tag> handleTagsUpdate(List<Tag> tags) {
        if (tags == null || tags.isEmpty()) {
            return Collections.emptyList();
        }

        Set<String> tagNames = tags.stream()
                .map(Tag::getTagName)
                .collect(Collectors.toSet());

        List<Tag> existingTags = tagRepository.findByTagNameIn(tagNames);
        Map<String, Tag> existingTagMap = existingTags.stream()
                .collect(Collectors.toMap(Tag::getTagName, Function.identity()));

        List<Tag> resultTags = new LinkedList<>();
        List<Tag> newTags = new ArrayList<>();

        for (Tag tag : tags) {
            Tag existingTag = existingTagMap.get(tag.getTagName());
            if (existingTag != null) {
                resultTags.add(existingTag);
            } else {
                Tag tag1 = new Tag();
                tag1.setTagName(tag.getTagName());
                newTags.add(tag1);
            }
        }

        if (!newTags.isEmpty()) {
            List<Tag> savedTags = tagRepository.saveAll(newTags);
            logger.info("Created new tags: {}", savedTags.stream().map(Tag::getTagName).collect(Collectors.joining(", ")));
            resultTags.addAll(savedTags);
        }

        return resultTags;
    }

    /**
     * Deletes a product by its ID.
     *
     * @param id the ID of the product to be deleted
     * @throws ProductNotFoundException if the product does not exist
     */
    @Transactional
    @Override
    public void deleteProduct(int id) {
        logger.info("Deleting product with ID: {}", id);

        Product product = productRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Product not found with ID: {}", id);
                    return new ProductNotFoundException("Product not found with ID: " + id);
                });

        // 解除关联
        if (product.getCategory() != null) {
            product.setCategory(null);
            logger.debug("Unlinked category from product ID: {}", id);
        }

        if (product.getTags() != null && !product.getTags().isEmpty()) {
            product.setTags(new LinkedList<>());
            logger.debug("Unlinked tags from product ID: {}", id);
        }

        // 执行删除操作
        productRepository.delete(product);
        logger.info("Product with ID {} has been deleted successfully.", id);
    }

    @Transactional
    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();  // 直接获取所有产品
    }
    @Transactional
    @Override
    public List<Product> searchProducts(String keyword, Sort sort) {
        // 使用关键字进行模糊查询，并根据传入的 Sort 对象进行排序
        return productRepository.findByProductNameContaining(keyword, sort);
    }

    @Override
    public List<Product> getProductsByCategory(Integer categoryId) {
        return productRepository.findByCategoryCategoryId(categoryId);
    }

    @Override
    public List<Product> getProductsBySellerId(int sellerId) {
        return productRepository.findBySellID(sellerId);
    }
}
