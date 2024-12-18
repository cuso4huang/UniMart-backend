// src/main/java/org/jnu/unimart/controller/TransactionController.java

package org.jnu.unimart.controller;

import jakarta.validation.Valid;
import org.jnu.unimart.enums.PaymentMethod;
import org.jnu.unimart.enums.TransactionStatus;
import org.jnu.unimart.pojo.Transaction;
import org.jnu.unimart.service.TransactionService;
import org.jnu.unimart.service.PaymentService; // 模拟支付服务
import org.jnu.unimart.security.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private PaymentService paymentService; // 模拟支付服务

    /**
     * 创建交易
     */
    @PostMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Transaction> createTransaction(@Valid @RequestBody TransactionRequest transactionRequest,
                                                         @AuthenticationPrincipal UserDetailsImpl currentUser) {
        Transaction transaction = transactionService.createTransaction(
                currentUser.getId(),
                transactionRequest.getSellerId(),
                transactionRequest.getProductId(),
                transactionRequest.getPaymentMethod(),
                transactionRequest.getTotalAmount()
        );
        return ResponseEntity.status(201).body(transaction);
    }



    /**
     * 模拟支付
     */
    @PostMapping("/{transactionId}/pay")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Transaction> payTransaction(@PathVariable int transactionId,
                                                      @AuthenticationPrincipal UserDetailsImpl currentUser) {
        Transaction transaction = transactionService.getTransactionById(transactionId);

        // 验证当前用户是否是买家或管理员
        if ((transaction.getBuyer().getUserId()!=currentUser.getId()) &&
                !currentUser.hasRole("ADMIN")) {
            return ResponseEntity.status(403).build();
        }

        if (transaction.getTransactionStatus() != TransactionStatus.PENDING_PAYMENT) {
            return ResponseEntity.status(400).body(transaction); // 只能支付待支付状态的交易
        }

        // 调用模拟支付服务
        Transaction updatedTransaction = paymentService.simulatePayment(transaction);

        return ResponseEntity.ok(updatedTransaction);
    }

    /**
     * 申请退款
     */
    @PostMapping("/{transactionId}/refund")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Transaction> refundTransaction(@PathVariable int transactionId,
                                                          @AuthenticationPrincipal UserDetailsImpl currentUser) {
        Transaction transaction = transactionService.getTransactionById(transactionId);

        // 只有买家或管理员可以申请退款
        if ((transaction.getBuyer().getUserId()!=currentUser.getId()) &&
                !currentUser.hasRole("ADMIN")) {
            return ResponseEntity.status(403).build();
        }

        if (transaction.getTransactionStatus() != TransactionStatus.PAID) {
            return ResponseEntity.status(400).body(transaction); // 只能在已支付状态下退款
        }

        // 调用模拟退款服务
        Transaction updatedTransaction = paymentService.simulateRefund(transaction);

        return ResponseEntity.ok(updatedTransaction);
    }

    /**
     * 获取当前用户的所有交易
     */
    @GetMapping("/my")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<Transaction>> getMyTransactions(@AuthenticationPrincipal UserDetailsImpl currentUser) {
        List<Transaction> buyerTransactions = transactionService.getTransactionsByBuyer(currentUser.getId());
        List<Transaction> sellerTransactions = transactionService.getTransactionsBySeller(currentUser.getId());

        // 合并两个列表
        buyerTransactions.addAll(sellerTransactions);

        return ResponseEntity.ok(buyerTransactions);
    }

    /**
     * 获取交易状态
     */
    @GetMapping("/{transactionId}/status")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<TransactionStatus> getTransactionStatus(@PathVariable int transactionId,
                                                                   @AuthenticationPrincipal UserDetailsImpl currentUser) {
        Transaction transaction = transactionService.getTransactionById(transactionId);

        // 只有买家、卖家或管理员可以查看状态
        if ((transaction.getBuyer().getUserId()!=currentUser.getId()) &&
        (transaction.getSeller().getUserId()!=currentUser.getId()) &&
                !currentUser.hasRole("ADMIN")) {
            return ResponseEntity.status(403).build();
        }

        return ResponseEntity.ok(transaction.getTransactionStatus());
    }

    /**
     * 获取交易详情
     */
    @GetMapping("/{transactionId}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Transaction> getTransactionDetail(@PathVariable int transactionId,
                                                            @AuthenticationPrincipal UserDetailsImpl currentUser) {
        Transaction transaction = transactionService.getTransactionById(transactionId);

        // 验证权限：只有买家、卖家或管理员可以查看交易详情
        if ((transaction.getBuyer().getUserId() != currentUser.getId()) &&
                (transaction.getSeller().getUserId() != currentUser.getId()) &&
                !currentUser.hasRole("ADMIN")) {
            return ResponseEntity.status(403).build();
        }

        // 补充商品信息
        transaction = transactionService.enrichTransactionWithProductInfo(transaction);

        return ResponseEntity.ok(transaction);
    }


    /**
     * 取消交易
     */
    @PostMapping("/{transactionId}/cancel")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Transaction> cancelTransaction(
            @PathVariable int transactionId,
            @AuthenticationPrincipal UserDetailsImpl currentUser) {

        Transaction transaction = transactionService.getTransactionById(transactionId);

        // 验证权限：只有买家或管理员可以取消交易
        if (!currentUser.hasRole("ADMIN") &&
                transaction.getBuyer().getUserId() != currentUser.getId()) {
            return ResponseEntity.status(403).build();
        }

        // 只能取消待支付的交易
        if (transaction.getTransactionStatus() != TransactionStatus.PENDING_PAYMENT) {
            return ResponseEntity.status(400)
                    .body(transaction);
        }

        transaction.setTransactionStatus(TransactionStatus.CANCELLED);
        Transaction updatedTransaction = transactionService.updateTransaction(transaction);

        return ResponseEntity.ok(updatedTransaction);
    }

    /**
     * 完成交易
     */
    @PostMapping("/{transactionId}/complete")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Transaction> completeTransaction(
            @PathVariable int transactionId,
            @AuthenticationPrincipal UserDetailsImpl currentUser) {

        Transaction transaction = transactionService.getTransactionById(transactionId);

        // 验证权限：只有买家或管理员可以完成交易
        if (!currentUser.hasRole("ADMIN") &&
                transaction.getBuyer().getUserId() != currentUser.getId()) {
            return ResponseEntity.status(403).build();
        }

        // 只能完成已支付的交易
        if (transaction.getTransactionStatus() != TransactionStatus.PAID) {
            return ResponseEntity.status(400)
                    .body(transaction);
        }

        transaction.setTransactionStatus(TransactionStatus.COMPLETED);
        transaction.setTransactionTime(LocalDateTime.now());
        Transaction updatedTransaction = transactionService.updateTransaction(transaction);

        return ResponseEntity.ok(updatedTransaction);
    }

    /**
     * 更新交易状态
     */
    @PutMapping("/{transactionId}/status")
    @PreAuthorize("hasRole('ADMIN')")  // 只允许管理员直接修改状态
    public ResponseEntity<Transaction> updateTransactionStatus(
            @PathVariable int transactionId,
            @RequestParam TransactionStatus newStatus) {

        Transaction transaction = transactionService.getTransactionById(transactionId);
        TransactionStatus oldStatus = transaction.getTransactionStatus();

        // 验证状态转换的合法性
        if (!isValidStatusTransition(oldStatus, newStatus)) {
            return ResponseEntity.status(400)
                    .body(transaction);
        }

        transaction.setTransactionStatus(newStatus);

        // 如果转换为已完成状态，设置完成时间
        if (newStatus == TransactionStatus.COMPLETED) {
            transaction.setTransactionTime(LocalDateTime.now());
        }

        Transaction updatedTransaction = transactionService.updateTransaction(transaction);
        return ResponseEntity.ok(updatedTransaction);
    }

    /**
     * 验证状态转换是否合法
     */
    private boolean isValidStatusTransition(TransactionStatus oldStatus, TransactionStatus newStatus) {
        switch (oldStatus) {
            case PENDING_PAYMENT:
                return newStatus == TransactionStatus.PAID ||
                        newStatus == TransactionStatus.CANCELLED;
            case PAID:
                return newStatus == TransactionStatus.COMPLETED;
            case COMPLETED:
            case CANCELLED:
                return false; // 已完成或已取消的订单不能再改变状态
            default:
                return false;
        }
    }
    // 定义 TransactionRequest DTO 类
    public static class TransactionRequest {
        private int sellerId;
        private int productId;
        private PaymentMethod paymentMethod;
        private Double totalAmount;

        // Getters 和 Setters

        public int getSellerId() {
            return sellerId;
        }

        public void setSellerId(int sellerId) {
            this.sellerId = sellerId;
        }

        public int getProductId() {
            return productId;
        }

        public void setProductId(int productId) {
            this.productId = productId;
        }

        public PaymentMethod getPaymentMethod() {
            return paymentMethod;
        }

        public void setPaymentMethod(PaymentMethod paymentMethod) {
            this.paymentMethod = paymentMethod;
        }

        public Double getTotalAmount() {
            return totalAmount;
        }

        public void setTotalAmount(Double totalAmount) {
            this.totalAmount = totalAmount;
        }
    }

}
