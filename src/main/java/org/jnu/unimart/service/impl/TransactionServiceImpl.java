// src/main/java/org/jnu/unimart/service/impl/TransactionServiceImpl.java

package org.jnu.unimart.service.impl;

import jakarta.transaction.Transactional;
import org.jnu.unimart.enums.PaymentMethod;
import org.jnu.unimart.enums.TransactionStatus;
import org.jnu.unimart.exception.InvalidOrderStatusTransitionException;
import org.jnu.unimart.exception.OrderNotFoundException;
import org.jnu.unimart.pojo.Transaction;
import org.jnu.unimart.pojo.Product;
import org.jnu.unimart.pojo.User;
import org.jnu.unimart.repository.TransactionRepository;
import org.jnu.unimart.repository.ProductRepository;
import org.jnu.unimart.repository.UserRepository;
import org.jnu.unimart.service.ProductService;
import org.jnu.unimart.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class TransactionServiceImpl implements TransactionService {
    private final ProductService productService;
    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

   @Autowired
   public TransactionServiceImpl(ProductService productService,TransactionRepository transactionRepository,
                                 UserRepository userRepository, ProductRepository productRepository) {
       this.productService = productService;
       this.transactionRepository = transactionRepository;
       this.userRepository = userRepository;
       this.productRepository = productRepository;
   }



    /**
     * 提供必要的参数，创建订单
     * @param buyerId
     * @param sellerId
     * @param productId
     * @param paymentMethod
     * @param totalAmount
     * @return
     */
    @Override
    public Transaction createTransaction(int buyerId, int sellerId, int productId, PaymentMethod paymentMethod, Double totalAmount) {
        User buyer = userRepository.findById(buyerId)
                .orElseThrow(() -> new OrderNotFoundException("Buyer not found with ID: " + buyerId));
        User seller = userRepository.findById(sellerId)
                .orElseThrow(() -> new OrderNotFoundException("Seller not found with ID: " + sellerId));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new OrderNotFoundException("Product not found with ID: " + productId));

        Transaction transaction = new Transaction();
        transaction.setBuyer(buyer);
        transaction.setSeller(seller);
        transaction.setProduct(product);
        transaction.setPaymentMethod(paymentMethod);
        transaction.setTotalAmount(totalAmount);
        transaction.setTransactionStatus(TransactionStatus.PENDING_PAYMENT);
        transaction.setTransactionTime(LocalDateTime.now());

        return transactionRepository.save(transaction);
    }

    /**
     * 通过订单id更新订单状态
     * @param transactionId
     * @param status
     * @return
     */
    @Override
    public Transaction updateTransactionStatus(int transactionId, TransactionStatus status) {
        Transaction transaction = getTransactionById(transactionId);
        if (isValidStatusTransition(transaction.getTransactionStatus(), status)) {
            transaction.setTransactionStatus(status);
            return transactionRepository.save(transaction);
        } else {
            throw new InvalidOrderStatusTransitionException("Invalid status transition from " + transaction.getTransactionStatus() + " to " + status);
        }
    }

    /**
     * 通过buyerId获取相关的订单
     * @param buyerId
     * @return
     */
    @Override
    public List<Transaction> getTransactionsByBuyer(int buyerId) {
        return transactionRepository.findByBuyerUserId(buyerId);
    }

    /**
     * 通过sellerId获取相关的订单
     * @param sellerId
     * @return
     */
    @Override
    public List<Transaction> getTransactionsBySeller(int sellerId) {
        return transactionRepository.findBySellerUserId(sellerId);
    }

    /**
     * 通过transactionId获取相关的订单信息
     * @param transactionId
     * @return
     */
    @Override
    public Transaction getTransactionById(int transactionId) {
        return transactionRepository.findById(transactionId)
                .orElseThrow(() -> new OrderNotFoundException("Transaction not found with ID: " + transactionId));
    }

    /**
     * 判断订单更改状态是否合法
     * @param currentStatus
     * @param newStatus
     * @return
     */
    private boolean isValidStatusTransition(TransactionStatus currentStatus, TransactionStatus newStatus) {
        switch (currentStatus) {
            case PENDING_PAYMENT:
                return newStatus == TransactionStatus.PAID || newStatus == TransactionStatus.CANCELLED;
            case PAID:
                return newStatus == TransactionStatus.COMPLETED || newStatus == TransactionStatus.CANCELLED;
            case COMPLETED:
            case CANCELLED:
                return false; // 终态，不可更改
            default:
                return false;
        }
    }
    @Override
    public Transaction enrichTransactionWithProductInfo(Transaction transaction) {
        // 获取商品信息
        Product product = productService.getProductById(transaction.getProduct().getProductId());
        if (product != null) {
            transaction.getProduct().setProductName(product.getProductName());
            // 可以设置其他需要的商品信息
        }
        return transaction;
    }

    @Override
    public Transaction updateTransaction(Transaction transaction) {
        // 1. 验证交易是否存在
        Transaction existingTransaction = transactionRepository.findById(transaction.getTransactionId())
                .orElseThrow(() -> new OrderNotFoundException("Transaction not found with ID: " +
                        transaction.getTransactionId()));

        // 2. 验证状态转换是否合法
        if (!isValidStatusTransition(existingTransaction.getTransactionStatus(),
                transaction.getTransactionStatus())) {
            throw new InvalidOrderStatusTransitionException(
                    "Invalid status transition from " + existingTransaction.getTransactionStatus() +
                            " to " + transaction.getTransactionStatus());
        }

        // 3. 更新交易信息
        existingTransaction.setTransactionStatus(transaction.getTransactionStatus());

        // 4. 根据不同状态执行相应的业务逻辑
        switch (transaction.getTransactionStatus()) {
            case PAID:
                // 设置支付时间
                existingTransaction.setTransactionTime(LocalDateTime.now());
                break;

            case COMPLETED:
                // 设置完成时间
                existingTransaction.setTransactionTime(LocalDateTime.now());
                // 更新商品状态为已售出
                Product product = existingTransaction.getProduct();
                product.setPublishStatus(2);
                productRepository.save(product);
                break;

            case CANCELLED:
                // 设置取消时间
                existingTransaction.setTransactionTime(LocalDateTime.now());
                break;
        }

        // 5. 更新其他可变字段
        if (transaction.getPaymentMethod() != null) {
            existingTransaction.setPaymentMethod(transaction.getPaymentMethod());
        }
        if (transaction.getTotalAmount() != null) {
            existingTransaction.setTotalAmount(transaction.getTotalAmount());
        }

        // 6. 保存更新
        try {
            return transactionRepository.save(existingTransaction);
        } catch (Exception e) {
            throw new RuntimeException("Failed to update transaction: " + e.getMessage());
        }
    }
}
