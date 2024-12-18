// src/main/java/org/jnu/unimart/service/TransactionService.java

package org.jnu.unimart.service;

import org.jnu.unimart.enums.PaymentMethod;
import org.jnu.unimart.enums.TransactionStatus;
import org.jnu.unimart.pojo.Transaction;

import java.util.List;

public interface TransactionService {
    Transaction createTransaction(int buyerId, int sellerId, int productId, PaymentMethod paymentMethod, Double totalAmount);
    Transaction updateTransactionStatus(int transactionId, TransactionStatus status);
    List<Transaction> getTransactionsByBuyer(int buyerId);
    List<Transaction> getTransactionsBySeller(int sellerId);
    Transaction getTransactionById(int transactionId);

    Transaction enrichTransactionWithProductInfo(Transaction transaction);

    Transaction updateTransaction(Transaction transaction);
}
