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
import org.jnu.unimart.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

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
}
