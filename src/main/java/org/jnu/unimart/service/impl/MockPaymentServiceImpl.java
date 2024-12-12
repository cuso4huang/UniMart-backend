// src/main/java/org/jnu/unimart/service/impl/MockPaymentServiceImpl.java

package org.jnu.unimart.service.impl;

import org.jnu.unimart.enums.TransactionStatus;
import org.jnu.unimart.pojo.Transaction;
import org.jnu.unimart.repository.TransactionRepository;
import org.jnu.unimart.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MockPaymentServiceImpl implements PaymentService {

    @Autowired
    private TransactionRepository transactionRepository;

    /**
     * 模拟支付过程，将交易状态从 PENDING_PAYMENT 更新为 PAID
     */
    @Override
    public Transaction simulatePayment(Transaction transaction) {
        transaction.setTransactionStatus(TransactionStatus.PAID);
        // 可选：设置支付时间
        transaction.setTransactionTime(java.time.LocalDateTime.now());
        // 保存更新
        return transactionRepository.save(transaction);
    }

    /**
     * 模拟退款过程，将交易状态从 PAID 更新为 CANCELLED
     */
    @Override
    public Transaction simulateRefund(Transaction transaction) {
        transaction.setTransactionStatus(TransactionStatus.CANCELLED);
        // 可选：设置退款时间
        transaction.setTransactionTime(java.time.LocalDateTime.now());
        // 保存更新
        return transactionRepository.save(transaction);
    }
}
